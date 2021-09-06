package com.weiwu.ql.main.find;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.qcloud.tim.uikit.component.dialog.TUIKitDialog;
import com.weiwu.ql.AppConstant;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseFragment;
import com.weiwu.ql.data.bean.HandlerData;
import com.weiwu.ql.data.bean.MessageEvent;
import com.weiwu.ql.data.bean.OrderData;
import com.weiwu.ql.data.bean.OrderDetailData;
import com.weiwu.ql.data.bean.WalletData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.repositories.MineRepository;
import com.weiwu.ql.data.request.HandlerOrderRequestBody;
import com.weiwu.ql.data.request.OrderRequestBody;
import com.weiwu.ql.main.mine.MineContract;
import com.weiwu.ql.main.mine.wallet.WalletAdapter;
import com.weiwu.ql.utils.SPUtils;
import com.weiwu.ql.view.AllHandlerPopup;
import com.weiwu.ql.view.BankListPopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/22 14:03 
 */
public class FindFragment extends BaseFragment implements MineContract.IOrderView, View.OnClickListener {
    private TextView mTvOrderAll;
    /*private TextView mTvOrderNoGet;
    private TextView mTvOrderNoConfirm;
    private TextView mTvOrderNoPay;
    private TextView mTvOrderOkConfirm;*/
    private RecyclerView mRvOrderList;
    private MineContract.IOrderPresenter mPresenter;
    private OrderAdapter mAdapter;
    private int pageNum = 1;
    private int pageSize = 20;
    private AllHandlerPopup mAllHandlerPopup;
    private RecyclerView mRvHandler;
    private AllHandlerAdapter mHandlerAdapter;
    private TextView mConfirmHandler;
    private TextView mCancelHandler;
    private List<HandlerData.DataDTO> mCheckList = new ArrayList<>();
    private BankListPopup mCoinListPopup;
    private RecyclerView mRvCoin;
    private WalletAdapter mWalletAdapter;
    private SmartRefreshLayout mOrderRefresh;
    private RadioGroup mOrderTag;
    private int mRole;
    private int status;
    private RadioButton mTvOrderNoGet;
    private RadioButton mTvOrderNoConfirm;
    private RadioButton mTvOrderNoPay;
    private RadioButton mTvOrderOkConfirm;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    public void onViewCreated(View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mRole = Integer.parseInt(SPUtils.getValue(AppConstant.USER, AppConstant.USER_TYPE));
        setPresenter(new OrderPresenter(MineRepository.getInstance()));
        initView(view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent msg) {
        if (msg.code == 101) {
            mAdapter.clearList();
            mPresenter.getAllOrder(new OrderRequestBody(pageSize, pageNum, String.valueOf(status)));
        }
    }

    private void initView(View view) {
        mTvOrderAll = (TextView) view.findViewById(R.id.tv_order_all);

        mTvOrderNoGet = (RadioButton) view.findViewById(R.id.tv_order_no_get);
        mTvOrderNoConfirm = (RadioButton) view.findViewById(R.id.tv_order_no_confirm);
        mTvOrderNoPay = (RadioButton) view.findViewById(R.id.tv_order_no_pay);
        mTvOrderOkConfirm = (RadioButton) view.findViewById(R.id.tv_order_ok_confirm);
        if (mRole == 1) {
            mTvOrderNoConfirm.setText("待派单");
            mTvOrderNoPay.setText("已派单");
        }

        mOrderTag = (RadioGroup) view.findViewById(R.id.rg_order_tag);
        mOrderTag.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tv_order_no_get:
                        status = mRole == 1 ? 100 : 200;
                        mTvOrderNoGet.setBackground(getResources().getDrawable(R.color.hint));
                        mTvOrderNoConfirm.setBackground(null);
                        mTvOrderNoPay.setBackground(null);
                        mTvOrderOkConfirm.setBackground(null);
                        break;
                    case R.id.tv_order_no_confirm:
                        status = mRole == 1 ? 101 : 202;
                        mTvOrderNoGet.setBackground(null);
                        mTvOrderNoConfirm.setBackground(getResources().getDrawable(R.color.hint));
                        mTvOrderNoPay.setBackground(null);
                        mTvOrderOkConfirm.setBackground(null);
                        break;
                    case R.id.tv_order_no_pay:
                        status = mRole == 1 ? 102 : 203;
                        mTvOrderNoGet.setBackground(null);
                        mTvOrderNoConfirm.setBackground(null);
                        mTvOrderNoPay.setBackground(getResources().getDrawable(R.color.hint));
                        mTvOrderOkConfirm.setBackground(null);
                        break;
                    case R.id.tv_order_ok_confirm:
                        status = mRole == 1 ? 104 : 205;
                        mTvOrderNoGet.setBackground(null);
                        mTvOrderNoConfirm.setBackground(null);
                        mTvOrderNoPay.setBackground(null);
                        mTvOrderOkConfirm.setBackground(getResources().getDrawable(R.color.hint));
                        break;
                }
                pageNum = 1;
                mAdapter.clearList();
                mPresenter.getAllOrder(new OrderRequestBody(pageSize, pageNum, String.valueOf(status)));
            }
        });


        mOrderRefresh = view.findViewById(R.id.srl_order);
        mOrderRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum = 1;
                mAdapter.clearList();
                mPresenter.getAllOrder(new OrderRequestBody(pageSize, pageNum, String.valueOf(status)));
            }
        });
        /*mTvOrderNoGet = (TextView) view.findViewById(R.id.tv_order_no_get);
        mTvOrderNoConfirm = (TextView) view.findViewById(R.id.tv_order_no_confirm);
        mTvOrderNoPay = (TextView) view.findViewById(R.id.tv_order_no_pay);
        mTvOrderOkConfirm = (TextView) view.findViewById(R.id.tv_order_ok_confirm);*/
        /*mTvOrderNoGet.setOnClickListener(this);
        mTvOrderNoConfirm.setOnClickListener(this);
        mTvOrderNoPay.setOnClickListener(this);
        mTvOrderOkConfirm.setOnClickListener(this);*/
        mTvOrderAll.setOnClickListener(this);

        mRvOrderList = (RecyclerView) view.findViewById(R.id.rv_order_list);
        mRvOrderList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new OrderAdapter();
        mRvOrderList.setAdapter(mAdapter);
        mTvOrderNoGet.setChecked(true);

//        mPresenter.getAllOrder(new OrderRequestBody(pageSize, pageNum, String.valueOf(status)));

        mAdapter.setOrderDetailClickListener(new OrderAdapter.IOrderDetailClickListener() {
            @Override
            public void onClick(OrderData.DataDTO.ListDTO data) {
                Intent intent = new Intent(getContext(), OrderDetailActivity.class);
                intent.putExtra(AppConstant.ORDER_INFO, data);
                startActivity(intent);
            }
        });
        mAdapter.setOrderClickListener(new OrderAdapter.IOrderClickListener() {
            @Override
            public void onClick(OrderData.DataDTO.ListDTO data) {
                final String id = data.getId();
                final int handlerReturnCoinType = data.getReleaseCoinType();
                int type = data.getType();
                int status = data.getStatus();
                if (type == 1) {
                    switch (status) {
                        case 100:
                            new TUIKitDialog(getContext())
                                    .builder()
                                    .setCancelable(true)
                                    .setCancelOutside(true)
                                    .setTitle("确认接单？")
                                    .setDialogWidth(0.75f)
                                    .setSureButtonColor(getResources().getColor(R.color.text_negative))
                                    .setPositiveButton("确认", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mPresenter.receiveOrder(id);
                                        }
                                    })
                                    .setNegativeButton("取消", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    })
                                    .show();
                            break;

                        case 101:
                            mAllHandlerPopup = new AllHandlerPopup(getContext());
                            mRvHandler = mAllHandlerPopup.findViewById(R.id.rv_all_handler);
                            mConfirmHandler = mAllHandlerPopup.findViewById(R.id.tv_confirm_handler);
                            mCancelHandler = mAllHandlerPopup.findViewById(R.id.tv_cancel_handler);
                            mConfirmHandler.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mCheckList.size() > 0) {
                                        Intent intent = new Intent(getContext(), DistributeActivity.class);
                                        HandlerData handlerData = new HandlerData();
                                        handlerData.setData(mCheckList);
                                        data.setMoney(data.getMoney() / 100);
                                        intent.putExtra(AppConstant.DISTRIBUTE_LIST, handlerData);
                                        intent.putExtra(AppConstant.ORDER_INFO, data);
                                        getActivity().startActivity(intent);
//                                        getActivity().finish();
                                        mAllHandlerPopup.dismiss();
                                    } else {
                                        showToast("请选择派单人！");
                                    }
                                }
                            });

                            mCancelHandler.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mAllHandlerPopup.dismiss();
                                }
                            });
                            mRvHandler.setLayoutManager(new LinearLayoutManager(getContext()));
                            mHandlerAdapter = new AllHandlerAdapter();
                            mRvHandler.setAdapter(mHandlerAdapter);
                            mPresenter.getAllHandler();
                            mHandlerAdapter.setHandlerCheckListener(new AllHandlerAdapter.IHandlerCheckListener() {
                                @Override
                                public void onCheck(HandlerData.DataDTO data, boolean isCheck) {
                                    if (isCheck) {
                                        /*GroupMemberInfo memberInfo = new GroupMemberInfo();
                                        memberInfo.setAccount(contact.getId());
                                        memberInfo.setNickname(contact.getNickname());*/
                                        mCheckList.add(data);
                                    } else {
                                        for (int i = mCheckList.size() - 1; i >= 0; i--) {
                                            if (mCheckList.get(i).getId().equals(data.getId())) {
                                                mCheckList.remove(i);
                                            }
                                        }
                                    }
                                }
                            });
                            break;
                    }
                } else {
                    String content = "";
                    if (status == 200) {
                        content = "确认接单？";
                    } else if (status == 202) {
                        content = "确认收到货币？";
                    } else if (status == 203) {
                        content = "确认回币？";
                    }
                    new TUIKitDialog(getContext())
                            .builder()
                            .setCancelable(true)
                            .setCancelOutside(true)
                            .setTitle(content)
                            .setDialogWidth(0.75f)
                            .setSureButtonColor(getResources().getColor(R.color.text_negative))
                            .setPositiveButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    switch (status) {
                                        case 200:
                                            mCoinListPopup = new BankListPopup(getContext());
                                            mRvCoin = mCoinListPopup.findViewById(R.id.rv_bank_list);
                                            mRvCoin.setLayoutManager(new LinearLayoutManager(getContext()));
                                            mWalletAdapter = new WalletAdapter();
                                            mRvCoin.setAdapter(mWalletAdapter);

                                            mPresenter.getAllWallet(String.valueOf(handlerReturnCoinType));

                                            mWalletAdapter.setWalletClickListener(new WalletAdapter.IWalletClickListener() {
                                                @Override
                                                public void onClick(WalletData.DataDTO data) {
                                                    mPresenter.handlerOrder(new HandlerOrderRequestBody(data.getCoinAddress(), id, String.valueOf(handlerReturnCoinType)));
                                                }
                                            });
//                                            mPresenter.handlerOrder("", data.getId());
                                            break;

                                        case 202:
                                            mPresenter.confirmGetCoin(id);
                                            break;

                                        case 203:
                                            mPresenter.confirmRecycleCoin(id);
                                            break;
                                    }

                                }
                            })
                            .setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .show();


                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        /*switch (v.getId()) {
            case R.id.tv_order_no_get:
                break;

            case R.id.tv_order_no_confirm:
                intent.putExtra(AppConstant.ORDER_TYPE, 1);
                break;

            case R.id.tv_order_no_pay:
                intent.putExtra(AppConstant.ORDER_TYPE, 2);
                break;

            case R.id.tv_order_ok_confirm:
                intent.putExtra(AppConstant.ORDER_TYPE, 3);
                break;
        }*/
        switch (v.getId()) {
            case R.id.tv_order_all:
                Intent intent = new Intent(getContext(), OrderTypeActivity.class);
                intent.putExtra(AppConstant.ORDER_TYPE, 4);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void detailReceive(OrderDetailData data) {

    }

    @Override
    public void orderReceive(OrderData data) {
        if (data.getData() != null && data.getData().getList() != null) {
            mAdapter.setList(data.getData().getList());
            mOrderRefresh.finishRefresh();
        } else {
            mAdapter.clearList();
            mAdapter.notifyDataSetChanged();
            mOrderRefresh.finishRefresh();
        }
    }

    @Override
    public void walletReceive(WalletData data) {
        if (data.getData() != null) {
            mWalletAdapter.setList(data.getData());
            mCoinListPopup.showPopupWindow();
        } else {
            showToast("您暂未添加币种钱包，无法接单！");
        }

    }

    @Override
    public void handlerReceive(HandlerData data) {
        if (data.getData() != null) {
            mHandlerAdapter.setList(data.getData());
            mAllHandlerPopup.showPopupWindow();
        }
    }

    @Override
    public void onSuccess(HttpResult data) {
        showToast(data.getMessage());
        if (mCoinListPopup != null) {
            mCoinListPopup.dismiss();
        }
        mAdapter.clearList();
        mPresenter.getAllOrder(new OrderRequestBody(pageSize, pageNum, String.valueOf(status)));
    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
        if (code == 10001) {
            MyApplication.loginAgain();
        }
        mOrderRefresh.finishRefresh();

    }

    @Override
    public void setPresenter(MineContract.IOrderPresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return getActivity();
    }


}
