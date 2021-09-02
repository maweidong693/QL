package com.weiwu.ql.main.mine.wallet;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tencent.qcloud.tim.uikit.base.ITitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.data.bean.WalletData;
import com.weiwu.ql.data.repositories.MineRepository;
import com.weiwu.ql.main.mine.MineContract;
import com.weiwu.ql.utils.IntentUtil;

public class WalletActivity extends BaseActivity implements MineContract.IWalletView {

    private TitleBarLayout mWalletTitleBar;
    private TextView mTvWalletManger;
    private RecyclerView mRvWallet;
    private Button mBtAddWallet;
    private WalletAdapter mAdapter;
    private MineContract.IWalletPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        setPresenter(new WalletPresenter(MineRepository.getInstance()));
        initView();
    }

    private void initView() {
        mWalletTitleBar = (TitleBarLayout) findViewById(R.id.wallet_title_bar);
        mWalletTitleBar.setTitle("我的钱包", TitleBarLayout.POSITION.MIDDLE);
        mWalletTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mWalletTitleBar.getRightIcon().setVisibility(View.GONE);
        mTvWalletManger = (TextView) findViewById(R.id.tv_wallet_manger);
        mRvWallet = (RecyclerView) findViewById(R.id.rv_wallet);
        mBtAddWallet = (Button) findViewById(R.id.bt_add_wallet);
        mBtAddWallet.setOnClickListener(v -> {
            IntentUtil.redirectToNextActivity(this, AddWalletActivity.class);
        });

        mRvWallet.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new WalletAdapter();
        mRvWallet.setAdapter(mAdapter);

        mPresenter.getAllWallet();
    }

    @Override
    public void walletReceive(WalletData data) {
        if (data.getData() != null) {
            mAdapter.setList(data.getData());
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
    public void setPresenter(MineContract.IWalletPresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return this;
    }
}