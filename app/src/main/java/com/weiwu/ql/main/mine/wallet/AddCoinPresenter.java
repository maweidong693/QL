package com.weiwu.ql.main.mine.wallet;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.bean.CoinData;
import com.weiwu.ql.data.bean.CoinListData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.main.mine.MineContract;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/28 14:05 
 */
public class AddCoinPresenter implements MineContract.IAddWalletPresenter {

    private MineContract.IAddWalletView mView;
    private MineContract.MineSource mSource;

    public AddCoinPresenter(MineContract.MineSource source) {
        mSource = source;
    }

    @Override
    public void getCoinList() {
        mSource.getCoin((LifecycleProvider) mView, new IBaseCallBack<CoinData>() {
            @Override
            public void onSuccess(CoinData data) {
                mView.coinListReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void addWallet(CoinListData body) {
        mSource.addWallet((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
            @Override
            public void onSuccess(HttpResult data) {
                mView.onSuccess(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void attachView(MineContract.IAddWalletView view) {
        mView = view;
    }

    @Override
    public void detachView(MineContract.IAddWalletView view) {
        mView = null;
    }
}
