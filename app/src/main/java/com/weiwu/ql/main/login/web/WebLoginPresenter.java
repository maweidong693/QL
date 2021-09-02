package com.weiwu.ql.main.login.web;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.WebLoginRequestBody;
import com.weiwu.ql.main.mine.MineContract;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/16 11:31 
 */
public class WebLoginPresenter implements MineContract.IWebLoginPresenter {

    private MineContract.IWebLoginView mView;
    private MineContract.MineSource mSource;

    public WebLoginPresenter(MineContract.MineSource source) {
        mSource = source;
    }

    @Override
    public void webLogin(String token, String code) {
        mSource.webLogin((LifecycleProvider) mView, token, code, new IBaseCallBack<HttpResult>() {
            @Override
            public void onSuccess(HttpResult data) {
                mView.webLoginResult(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void attachView(MineContract.IWebLoginView view) {
        mView = view;
    }

    @Override
    public void detachView(MineContract.IWebLoginView view) {
        mView = null;
    }
}
