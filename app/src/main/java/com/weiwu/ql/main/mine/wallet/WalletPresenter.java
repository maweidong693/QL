package com.weiwu.ql.main.mine.wallet;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.bean.WalletData;
import com.weiwu.ql.main.mine.MineContract;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/9/1 13:13 
 */
public class WalletPresenter implements MineContract.IWalletPresenter {

    private MineContract.IWalletView mView;
    private MineContract.MineSource mSource;

    public WalletPresenter(MineContract.MineSource source) {
        mSource = source;
    }

    @Override
    public void getAllWallet(String coinType) {
        mSource.getAllWallet((LifecycleProvider) mView,coinType, new IBaseCallBack<WalletData>() {
            @Override
            public void onSuccess(WalletData data) {
                mView.walletReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void attachView(MineContract.IWalletView view) {
        mView = view;
    }

    @Override
    public void detachView(MineContract.IWalletView view) {
        mView = null;
    }
}
