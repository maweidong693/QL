package com.weiwu.ql.main.find;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qiniu.android.utils.StringUtils;
import com.tencent.qcloud.tim.uikit.base.ITitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.dialog.TUIKitDialog;
import com.weiwu.ql.AppConstant;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
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
import com.weiwu.ql.view.AllHandlerPopup;
import com.weiwu.ql.view.BankListPopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends BaseActivity implements MineContract.IOrderView {

    private TitleBarLayout mOrderDetailTitle;
    private TextView mTvOrderNo;
    private TextView mTvOrderGetTime;
    private TextView mTvOrderOutTime;
    private TextView mTvOrderOutPerson;
    private TextView mTvOrderMoney;
    private TextView mTvOrderOutCoin;
    private TextView mTvOrderCoinCount;
    private TextView mTvOrderDetailMore;
    private RecyclerView mRvOrderDetailRecord;
    private OrderData.DataDTO.ListDTO mOrderInfo;
    private OrderDetailAdapter mAdapter;
    private Button mBtGetOrder;
    private OrderDetailData.DataDTO detailData;
    private AllHandlerPopup mAllHandlerPopup;
    private RecyclerView mRvHandler;
    private TextView mConfirmHandler;
    private TextView mCancelHandler;
    private List<HandlerData.DataDTO> mCheckList = new ArrayList<>();
    private BankListPopup mCoinListPopup;
    private RecyclerView mRvCoin;
    private WalletAdapter mWalletAdapter;
    private AllHandlerAdapter mHandlerAdapter;
    private MineContract.IOrderPresenter mPresenter;
    private TextView mTvOrderCoinAddress;
    private TextView mTvPaste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Intent intent = getIntent();
        if (intent != null) {
            mOrderInfo = (OrderData.DataDTO.ListDTO) intent.getSerializableExtra(AppConstant.ORDER_INFO);
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setPresenter(new OrderPresenter(MineRepository.getInstance()));
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void initView() {
        mOrderDetailTitle = (TitleBarLayout) findViewById(R.id.order_detail_title);
        mOrderDetailTitle.getRightGroup().setVisibility(View.GONE);
        mOrderDetailTitle.setTitle("订单详情", ITitleBarLayout.POSITION.MIDDLE);
        mOrderDetailTitle.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTvOrderNo = (TextView) findViewById(R.id.tv_order_no);
        mTvOrderGetTime = (TextView) findViewById(R.id.tv_order_get_time);
        mTvOrderOutTime = (TextView) findViewById(R.id.tv_order_out_time);
        mTvOrderOutPerson = (TextView) findViewById(R.id.tv_order_out_person);
        mTvOrderMoney = (TextView) findViewById(R.id.tv_order_money);
        mTvOrderOutCoin = (TextView) findViewById(R.id.tv_order_out_coin);
        mTvOrderCoinCount = (TextView) findViewById(R.id.tv_order_coin_release_coin);
        mTvOrderCoinAddress = (TextView) findViewById(R.id.tv_order_coin_release_coin_address);
        mTvPaste = (TextView) findViewById(R.id.tv_order_paste_address);
        mTvPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtils.isNullOrEmpty(detailData.getReleaseUserCoinAddress())) {
                    //获取剪贴板管理器：
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText("Label", detailData.getReleaseUserCoinAddress());
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    showToast("钱包链接已复制到剪切板，快去发送给好友吧！");
                } else {
                    showToast("获取钱包链接失败！");
                }
            }
        });
        mTvOrderDetailMore = (TextView) findViewById(R.id.tv_order_detail_more);
        mRvOrderDetailRecord = (RecyclerView) findViewById(R.id.rv_order_detail_record);
        mBtGetOrder = (Button) findViewById(R.id.bt_get_order);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRvOrderDetailRecord.setLayoutManager(layoutManager);
        mAdapter = new OrderDetailAdapter();
        mRvOrderDetailRecord.setAdapter(mAdapter);

        mPresenter.getOrderDetail(mOrderInfo.getId());

        mTvOrderDetailMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.setExp(true);
                mPresenter.getOrderDetail(mOrderInfo.getId());
                mTvOrderDetailMore.setVisibility(View.GONE);
            }
        });

        mBtGetOrder.setBackground(getResources().getDrawable(R.drawable.bg_order_status));
        mBtGetOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (detailData != null) {
                    int status = detailData.getStatus();
                    if (status == 100 || status == 101 || status == 200 || status == 202 || status == 203) {
                        final String id = detailData.getId();
                        int type = detailData.getType();
                        if (type == 1) {
                            switch (status) {
                                case 100:
                                    new TUIKitDialog(OrderDetailActivity.this)
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
                                    mAllHandlerPopup = new AllHandlerPopup(OrderDetailActivity.this);
                                    mRvHandler = mAllHandlerPopup.findViewById(R.id.rv_all_handler);
                                    mConfirmHandler = mAllHandlerPopup.findViewById(R.id.tv_confirm_handler);
                                    mCancelHandler = mAllHandlerPopup.findViewById(R.id.tv_cancel_handler);
                                    mConfirmHandler.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (mCheckList.size() > 0) {
                                                OrderData.DataDTO.ListDTO listDTO = new OrderData.DataDTO.ListDTO();
                                                listDTO.setId(detailData.getId());
                                                listDTO.setMoney(detailData.getMoney()/100);
                                                Intent intent = new Intent(OrderDetailActivity.this, DistributeActivity.class);
                                                HandlerData handlerData = new HandlerData();
                                                handlerData.setData(mCheckList);
                                                intent.putExtra(AppConstant.DISTRIBUTE_LIST, handlerData);
                                                intent.putExtra(AppConstant.ORDER_INFO, listDTO);
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
                                    mRvHandler.setLayoutManager(new LinearLayoutManager(OrderDetailActivity.this));
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
                            new TUIKitDialog(OrderDetailActivity.this)
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
                                                    mCoinListPopup = new BankListPopup(OrderDetailActivity.this);
                                                    mRvCoin = mCoinListPopup.findViewById(R.id.rv_bank_list);
                                                    mRvCoin.setLayoutManager(new LinearLayoutManager(OrderDetailActivity.this));
                                                    mWalletAdapter = new WalletAdapter();
                                                    mRvCoin.setAdapter(mWalletAdapter);

                                                    mPresenter.getAllWallet();

                                                    mWalletAdapter.setWalletClickListener(new WalletAdapter.IWalletClickListener() {
                                                        @Override
                                                        public void onClick(WalletData.DataDTO data) {
                                                            mPresenter.handlerOrder(new HandlerOrderRequestBody(data.getCoinAddress(), id));
                                                        }
                                                    });
//                                            mPresenter.handlerOrder("", detailData.getId());
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
                    if (status == 102 || status == 103 || status == 104 || status == 201 || status == 204 || status == 205) {
                        mBtGetOrder.setBackground(getResources().getDrawable(R.drawable.bg_quote));
                        mBtGetOrder.setOnClickListener(null);
                    }
                }
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent msg) {
        if (msg.code == 101) {
            mPresenter.getOrderDetail(mOrderInfo.getId());
        }
    }

    @Override
    public void detailReceive(OrderDetailData data) {
        if (data.getData() != null && data.getData().getStatusInfoList() != null) {
            detailData = data.getData();
            mTvOrderNo.setText(detailData.getOrderNo());
            mTvOrderGetTime.setText(detailData.getCreatedTime());
            mTvOrderOutTime.setText(detailData.getReleaseTime());
            mTvOrderOutPerson.setText(detailData.getReleaseUserInfo().getName());
            mTvOrderMoney.setText(String.valueOf(detailData.getMoney()/100));
            mTvOrderOutCoin.setText(detailData.getReleaseCoinName());
            mTvOrderCoinCount.setText(detailData.getHandlerReturnCoinName());
            mTvOrderCoinAddress.setText(detailData.getReleaseUserCoinAddress());

            mAdapter.setList(data.getData().getStatusInfoList());
//            mTvOrderCoinCount.setText(detailData.getcoin);

            int status = detailData.getStatus();
            if (status == 100 || status == 101 || status == 200 || status == 202 || status == 203) {
                mBtGetOrder.setBackground(getResources().getDrawable(R.drawable.bg_order_status));

            }
            if (status == 102 || status == 103 || status == 104 || status == 201 || status == 204 || status == 205) {
                mBtGetOrder.setBackground(getResources().getDrawable(R.drawable.bg_quote));
//                mBtGetOrder.setOnClickListener(null);
            }

            String s = "";
            switch (status) {
                case 100:
                    s = "接单";
                    break;
                case 101:
                    s = "待派单";
                    break;
                case 102:
                    s = "已派单";

                    break;
                case 103:
                    s = "待发币";

                    break;
                case 104:
                    s = "已完成";

                    break;
                case 200:
                    s = "接单";

                    break;
                case 201:
                    s = "待发币";

                    break;
                case 202:
                    s = "确认收币";

                    break;
                case 203:
                    s = "确认回币";

                    break;
                case 204:
                    s = "待确认";

                    break;
                case 205:
                    s = "已完成";

                    break;

            }
            mBtGetOrder.setText(s);
        }
    }

    @Override
    public void orderReceive(OrderData data) {

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
        mPresenter.getOrderDetail(mOrderInfo.getId());
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