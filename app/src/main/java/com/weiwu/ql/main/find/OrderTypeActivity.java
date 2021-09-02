package com.weiwu.ql.main.find;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.qcloud.tim.uikit.base.ITitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.dialog.TUIKitDialog;
import com.weiwu.ql.AppConstant;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.data.bean.HandlerData;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OrderTypeActivity extends BaseActivity implements MineContract.IOrderView {

    private int mType;
    private int mRole;
    private int page;
    private int size;
    private TitleBarLayout mOrderTypeTitle;
    private RecyclerView mRvOrderTypeList;
    private OrderAdapter mAdapter;
    private MineContract.IOrderPresenter mPresenter;
    private AllHandlerPopup mAllHandlerPopup;
    private RecyclerView mRvHandler;
    private TextView mConfirmHandler;
    private TextView mCancelHandler;
    private List<HandlerData.DataDTO> mCheckList = new ArrayList<>();
    private AllHandlerAdapter mHandlerAdapter;
    private BankListPopup mCoinListPopup;
    private RecyclerView mRvCoin;
    private WalletAdapter mWalletAdapter;
    private SmartRefreshLayout mSrlOrderType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_type);
        setPresenter(new OrderPresenter(MineRepository.getInstance()));
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getIntExtra(AppConstant.ORDER_TYPE, 0);
            mRole = Integer.parseInt(SPUtils.getValue(AppConstant.USER, AppConstant.USER_TYPE));
        }
        initView();
    }

    private void initView() {
        mOrderTypeTitle = (TitleBarLayout) findViewById(R.id.order_type_title);
        mOrderTypeTitle.getRightGroup().setVisibility(View.GONE);

        mOrderTypeTitle.setOnLeftClickListener(view -> finish());
        mRvOrderTypeList = (RecyclerView) findViewById(R.id.rv_order_type_list);
        mRvOrderTypeList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new OrderAdapter();
        mRvOrderTypeList.setAdapter(mAdapter);

        String title = "";
        switch (mType) {
            case 0:
                title = "待接取";
                mPresenter.getAllOrder(new OrderRequestBody(size, page, String.valueOf(mRole == 1 ? 100 : 200)));
                break;
            case 1:
                title = "待确认";
                mPresenter.getAllOrder(new OrderRequestBody(size, page, String.valueOf(mRole == 1 ? 101 : 202)));
                break;
            case 2:
                title = "待支付";
                mPresenter.getAllOrder(new OrderRequestBody(size, page, String.valueOf(mRole == 1 ? 103 : 203)));

                break;
            case 3:
                title = "已完结";
                mPresenter.getAllOrder(new OrderRequestBody(size, page, String.valueOf(mRole == 1 ? 104 : 205)));

                break;
            case 4:
                title = "全部订单";
                mPresenter.getAllOrder(new OrderRequestBody(size, page));
                break;
        }
        mOrderTypeTitle.setTitle(title, ITitleBarLayout.POSITION.MIDDLE);

        mAdapter.setOrderDetailClickListener(new OrderAdapter.IOrderDetailClickListener() {
            @Override
            public void onClick(OrderData.DataDTO.ListDTO data) {
                Intent intent = new Intent(OrderTypeActivity.this, OrderDetailActivity.class);
                intent.putExtra(AppConstant.ORDER_INFO, data);
                startActivity(intent);
            }
        });

        mAdapter.setOrderClickListener(new OrderAdapter.IOrderClickListener() {
            @Override
            public void onClick(OrderData.DataDTO.ListDTO data) {
                final String id = data.getId();
                int type = data.getType();
                int status = data.getStatus();
                if (type == 1) {
                    switch (status) {
                        case 100:
                            new TUIKitDialog(OrderTypeActivity.this)
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
                            mAllHandlerPopup = new AllHandlerPopup(OrderTypeActivity.this);
                            mRvHandler = mAllHandlerPopup.findViewById(R.id.rv_all_handler);
                            mConfirmHandler = mAllHandlerPopup.findViewById(R.id.tv_confirm_handler);
                            mCancelHandler = mAllHandlerPopup.findViewById(R.id.tv_cancel_handler);
                            mConfirmHandler.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mCheckList.size() > 0) {
                                        Intent intent = new Intent(OrderTypeActivity.this, DistributeActivity.class);
                                        HandlerData handlerData = new HandlerData();
                                        handlerData.setData(mCheckList);
                                        intent.putExtra(AppConstant.DISTRIBUTE_LIST, handlerData);
                                        intent.putExtra(AppConstant.ORDER_INFO, data);
                                        startActivity(intent);
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
                            mRvHandler.setLayoutManager(new LinearLayoutManager(OrderTypeActivity.this));
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
                    new TUIKitDialog(OrderTypeActivity.this)
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
                                            mCoinListPopup = new BankListPopup(OrderTypeActivity.this);
                                            mRvCoin = mCoinListPopup.findViewById(R.id.rv_bank_list);
                                            mRvCoin.setLayoutManager(new LinearLayoutManager(OrderTypeActivity.this));
                                            mWalletAdapter = new WalletAdapter();
                                            mRvCoin.setAdapter(mWalletAdapter);

                                            mPresenter.getAllWallet();

                                            mWalletAdapter.setWalletClickListener(new WalletAdapter.IWalletClickListener() {
                                                @Override
                                                public void onClick(WalletData.DataDTO data) {
                                                    mPresenter.handlerOrder(new HandlerOrderRequestBody(data.getCoinAddress(), id));
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
        mSrlOrderType = (SmartRefreshLayout) findViewById(R.id.srl_order_type);
        mSrlOrderType.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                page = 1;
                mAdapter.clearList();
                mPresenter.getAllOrder(new OrderRequestBody(size, page));

            }
        });
    }

    @Override
    public void detailReceive(OrderDetailData data) {

    }

    @Override
    public void orderReceive(OrderData data) {
        if (data.getData() != null && data.getData().getList() != null) {
            mAdapter.setList(data.getData().getList());
            mSrlOrderType.finishRefresh();

        }else {
            mAdapter.clearList();
            mAdapter.notifyDataSetChanged();
            mSrlOrderType.finishRefresh();
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
//        mAdapter.clearList();
//        mPresenter.getAllOrder(new OrderRequestBody(size, page));
    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
        if (code == 10001) {
            MyApplication.loginAgain();
        }
    }

    @Override
    public void setPresenter(MineContract.IOrderPresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return this;
    }
}