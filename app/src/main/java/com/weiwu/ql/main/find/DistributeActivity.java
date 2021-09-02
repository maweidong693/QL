package com.weiwu.ql.main.find;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tencent.qcloud.tim.uikit.base.ITitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.weiwu.ql.AppConstant;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.data.bean.HandlerData;
import com.weiwu.ql.data.bean.MessageEvent;
import com.weiwu.ql.data.bean.OrderData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.repositories.MineRepository;
import com.weiwu.ql.data.request.DistributeRequestBody;
import com.weiwu.ql.main.mine.MineContract;
import com.weiwu.ql.utils.FileUtils;
import com.weiwu.ql.view.AllHandlerPopup;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class DistributeActivity extends BaseActivity implements View.OnClickListener, MineContract.IDistributeView {

    private TitleBarLayout mDistributeTitle;
    private TextView mTvDistributeMoney;
    private RecyclerView mRvDistribute;
    private Button mBtDistributeConfirm;
    private HandlerData mCheckData;
    private OrderData.DataDTO.ListDTO mOrderInfo;
    private DistributeAdapter mDistributeAdapter;
    private boolean isRight = true;
    private MineContract.IDistributePresenter mPresenter;
    private AllHandlerPopup mAllHandlerPopup;
    private RecyclerView mRvHandler;
    private TextView mConfirmHandler;
    private TextView mCancelHandler;
    private List<HandlerData.DataDTO> mCheckList = new ArrayList<>();
    private AllHandlerAdapter mHandlerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribute);
        Intent intent = getIntent();
        if (intent != null) {
            mCheckData = (HandlerData) intent.getSerializableExtra(AppConstant.DISTRIBUTE_LIST);
            mOrderInfo = (OrderData.DataDTO.ListDTO) intent.getSerializableExtra(AppConstant.ORDER_INFO);
        }
        setPresenter(new DistributePresenter(MineRepository.getInstance()));
        initView();
    }


    private void initView() {
        mDistributeTitle = (TitleBarLayout) findViewById(R.id.distribute_title);
        mDistributeTitle.setTitle("派单", ITitleBarLayout.POSITION.MIDDLE);
        mDistributeTitle.setRightIcon(R.mipmap.ic_distribute_add);
        mDistributeTitle.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCheckList = mCheckData.getData();
        mDistributeTitle.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAllHandlerPopup = new AllHandlerPopup(DistributeActivity.this);
                mRvHandler = mAllHandlerPopup.findViewById(R.id.rv_all_handler);
                mConfirmHandler = mAllHandlerPopup.findViewById(R.id.tv_confirm_handler);
                mCancelHandler = mAllHandlerPopup.findViewById(R.id.tv_cancel_handler);
                mConfirmHandler.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCheckList.size() > 0) {
                            /*Intent intent = new Intent(DistributeActivity.this, DistributeActivity.class);
                            HandlerData handlerData = new HandlerData();
                            handlerData.setData(mCheckList);
                            intent.putExtra(AppConstant.DISTRIBUTE_LIST, andlerData);
                            intent.putExtra(AppConstant.ORDER_INFO, data);
                            getActivity().startActivity(intent);h
//                                        getActivity().finish();*/
                            mDistributeAdapter.setList(mCheckList);
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
                mRvHandler.setLayoutManager(new LinearLayoutManager(DistributeActivity.this));
                mHandlerAdapter = new AllHandlerAdapter();
                mRvHandler.setAdapter(mHandlerAdapter);
                mPresenter.getAllHandler();
                mHandlerAdapter.setHandlerCheckListener(new AllHandlerAdapter.IHandlerCheckListener() {
                    @Override
                    public void onCheck(HandlerData.DataDTO data, boolean isCheck) {
                        if (isCheck) {
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
            }
        });
        mTvDistributeMoney = (TextView) findViewById(R.id.tv_distribute_money_count);
        if (mOrderInfo != null) {
            mTvDistributeMoney.setText(String.valueOf(mOrderInfo.getMoney()));
        }
        mRvDistribute = (RecyclerView) findViewById(R.id.rv_distribute);
        mBtDistributeConfirm = (Button) findViewById(R.id.bt_distribute_confirm);
        mBtDistributeConfirm.setOnClickListener(this);

        mRvDistribute.setLayoutManager(new LinearLayoutManager(this));
        mDistributeAdapter = new DistributeAdapter();
        mRvDistribute.setAdapter(mDistributeAdapter);
        mDistributeAdapter.setList(mCheckData.getData());

        mDistributeAdapter.setListener(new DistributeAdapter.EditAbleListAdapterListener() {
            @Override
            public void onEditTextChanged(int position, String value) {
                int money = mOrderInfo.getMoney();
                List<HandlerData.DataDTO> list = mDistributeAdapter.getList();
                for (int i = 0; i < list.size(); i++) {
                    money = money - list.get(i).getMoney();
                }
                if (money < 0) {
                    isRight = false;
                    money = 0;
                    showToast("分配金额大于可分配金额，请重新输入！");
                } else {
                    isRight = true;
                }
                mTvDistributeMoney.setText(String.valueOf(money));
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_distribute_confirm:
                List<HandlerData.DataDTO> list = mDistributeAdapter.getList();
                List<DistributeRequestBody.ChildTradeParamData> data = new ArrayList<>();

                int moneyCount = 0;
                for (int i = 0; i < list.size(); i++) {
                    HandlerData.DataDTO dto = list.get(i);
                    int money = dto.getMoney();
                    moneyCount = moneyCount + money;
                    if (money <= 0) {
                        isRight = false;
                        showToast("请正确输入分配金额！");
                        return;
                    } else {
                        data.add(new DistributeRequestBody.ChildTradeParamData(dto.getId(), money*100));
                    }
                }
                if (isRight && moneyCount == mOrderInfo.getMoney()) {
//TODO
                    mPresenter.distributeOrder(new DistributeRequestBody(mOrderInfo.getId(), data));
                } else {
                    showToast("请正确输入分配金额！");
                }
                break;
        }
    }

    @Override
    public void handlerReceive(HandlerData data) {
        if (data.getData() != null) {
            List<HandlerData.DataDTO> allList = data.getData();
            for (int i = 0; i < allList.size(); i++) {
                for (int j = 0; j < mCheckList.size(); j++) {
                    HandlerData.DataDTO handler1 = allList.get(i);
                    HandlerData.DataDTO handler2 = mCheckList.get(j);
                    if (handler1.getId().equals(handler2.getId())) {
                        handler1.setCheck(true);
                    }
                }
            }
            mHandlerAdapter.setList(allList);
            mAllHandlerPopup.showPopupWindow();
        }
    }

    @Override
    public void onSuccess(HttpResult data) {
        showToast(data.getMessage());
        EventBus.getDefault().post(new MessageEvent("刷新",101));
        finish();
    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
        if (code == 10001) {
            MyApplication.loginAgain();
        }
    }

    @Override
    public void setPresenter(MineContract.IDistributePresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return this;
    }
}