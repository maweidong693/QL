package com.weiwu.ql.main.find;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.bean.HandlerData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.DistributeRequestBody;
import com.weiwu.ql.main.mine.MineContract;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/31 15:07 
 */
public class DistributePresenter implements MineContract.IDistributePresenter {

    private MineContract.IDistributeView mView;
    private MineContract.MineSource mSource;

    public DistributePresenter(MineContract.MineSource source) {
        mSource = source;
    }

    @Override
    public void getAllHandler() {
        mSource.getAllHandler((LifecycleProvider) mView, new IBaseCallBack<HandlerData>() {
            @Override
            public void onSuccess(HandlerData data) {
                mView.handlerReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void distributeOrder(DistributeRequestBody body) {
        mSource.distributeOrder((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
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
    public void attachView(MineContract.IDistributeView view) {
        mView = view;
    }

    @Override
    public void detachView(MineContract.IDistributeView view) {
        mView = null;
    }
}
