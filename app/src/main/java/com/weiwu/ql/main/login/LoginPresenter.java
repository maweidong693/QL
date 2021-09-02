package com.weiwu.ql.main.login;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.bean.LoginReceive;
import com.weiwu.ql.data.request.LoginRequestBody;
import com.weiwu.ql.main.mine.MineContract;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/22 09:57 
 */
public class LoginPresenter implements MineContract.ILoginPresenter {

    private MineContract.ILoginView mView;
    private MineContract.MineSource mSource;

    public LoginPresenter(MineContract.MineSource source) {
        mSource = source;
    }

    @Override
    public void login(LoginRequestBody body) {
        mSource.login((LifecycleProvider) mView, body, new IBaseCallBack<LoginReceive>() {
            @Override
            public void onSuccess(LoginReceive data) {
                mView.loginResult(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void register(LoginRequestBody body) {
        mSource.register((LifecycleProvider) mView, body, new IBaseCallBack<LoginData>() {
            @Override
            public void onSuccess(LoginData data) {
                mView.registerResult(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void attachView(MineContract.ILoginView view) {
        mView = view;
    }

    @Override
    public void detachView(MineContract.ILoginView view) {
        mView = null;
    }
}
