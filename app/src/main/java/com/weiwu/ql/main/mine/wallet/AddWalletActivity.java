package com.weiwu.ql.main.mine.wallet;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tencent.qcloud.tim.uikit.base.ITitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.LineControllerView;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.data.bean.CoinData;
import com.weiwu.ql.data.bean.CoinListData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.repositories.MineRepository;
import com.weiwu.ql.main.mine.MineContract;
import com.weiwu.ql.view.BankListPopup;

import java.util.List;

public class AddWalletActivity extends BaseActivity implements View.OnClickListener, MineContract.IAddWalletView {

    private TitleBarLayout mAddWalletTitleBar;
    private LineControllerView mViewCheckCoin;
    private EditText mEtCoinAddress;
    private EditText mEtCoinRemark;
    private Button mBtAddCoin;
    private BankListPopup mBankListPopup;
    private RecyclerView mRvBankList;
    private BankListAdapter mAdapter;
    private MineContract.IAddWalletPresenter mPresenter;
    private CoinData.CoinDto mCoinDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet);
        setPresenter(new AddCoinPresenter(MineRepository.getInstance()));
        initView();
    }

    private void initView() {
        mAddWalletTitleBar = (TitleBarLayout) findViewById(R.id.add_wallet_title_bar);
        mAddWalletTitleBar.setTitle("添加币种", TitleBarLayout.POSITION.MIDDLE);
        mAddWalletTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAddWalletTitleBar.getRightIcon().setVisibility(View.GONE);
        mViewCheckCoin = (LineControllerView) findViewById(R.id.view_check_coin);
        mEtCoinAddress = (EditText) findViewById(R.id.et_coin_address);
        mEtCoinRemark = (EditText) findViewById(R.id.et_coin_remark);
        mBtAddCoin = (Button) findViewById(R.id.bt_add_coin);
        mViewCheckCoin.setOnClickListener(this);
        mBtAddCoin.setOnClickListener(this);
        mBankListPopup = new BankListPopup(this);
        mRvBankList = mBankListPopup.findViewById(R.id.rv_bank_list);
        mRvBankList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BankListAdapter();
        mRvBankList.setAdapter(mAdapter);
        mPresenter.getCoinList();

        mAdapter.setBankListItemClickListener(new BankListAdapter.IBankListItemClickListener() {
            @Override
            public void mItemClickListener(CoinData.CoinDto bankName) {
                mCoinDto = bankName;
                mViewCheckCoin.setContent(mCoinDto.getCoinName());
                mBankListPopup.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_check_coin:
                mBankListPopup.showPopupWindow();

                break;
            case R.id.bt_add_coin:
                if (mCoinDto == null) {
                    showToast("请选择币种！");
                    return;
                }
                if (TextUtils.isEmpty(mEtCoinAddress.getText().toString())) {
                    showToast("请输入币种地址！");
                    return;
                }
                CoinListData data = new CoinListData(mCoinDto.getId(), mCoinDto.getCoinName(), mEtCoinAddress.getText().toString(), mEtCoinRemark.getText().toString());
                mPresenter.addWallet(data);
                break;
        }
    }

    @Override
    public void coinListReceive(CoinData data) {
        if (data.getData() != null) {
            if (data.getData() != null && data.getData().size() > 0) {
                mAdapter.setList(data.getData());
            }
        }
    }

    @Override
    public void onSuccess(HttpResult data) {
        if (data != null) {
            showToast("添加成功！");
            finish();
        }
    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
        if (code == 10001) {
            MyApplication.loginAgain();
        }
    }

    @Override
    public void setPresenter(MineContract.IAddWalletPresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return this;
    }
}