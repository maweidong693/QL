package com.weiwu.ql.main.login;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.bean.LoginReceive;
import com.weiwu.ql.data.bean.SocketDataBean;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.LoginRequestBody;
import com.weiwu.ql.data.request.RegisterRequestBody;
import com.weiwu.ql.data.request.SendPhoneMsgRequestBody;
import com.weiwu.ql.data.request.VerifyCodeRequestBody;
import com.weiwu.ql.main.mine.MineContract;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/22 09:57 
 */
public class LoginPresenter implements MineContract.ILoginPresenter {

    private MineContract.ILoginView mView;
    private final MineContract.MineSource mSource;

    public LoginPresenter(MineContract.MineSource source) {
        mSource = source;
    }

    @Override
    public void forgetPassword(RegisterRequestBody body) {
        mSource.forgetPassword((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
            @Override
            public void onSuccess(HttpResult data) {
                mView.updateSuccess(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void updatePassword(RegisterRequestBody body) {
        mSource.updatePassword((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
            @Override
            public void onSuccess(HttpResult data) {
                mView.updateSuccess(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);

            }
        });
    }

    @Override
    public void register(RegisterRequestBody body) {
        mSource.register((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
            @Override
            public void onSuccess(HttpResult data) {
                mView.registerSuccess(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void verify(VerifyCodeRequestBody body) {
        mSource.verify((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
            @Override
            public void onSuccess(HttpResult data) {
                mView.verifySuccess(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);

            }
        });
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
    public void sendPhone(SendPhoneMsgRequestBody body) {
        mSource.sendPhoneMsg((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
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
    public void getSocketUrl() {
        mSource.getSocketUrl((LifecycleProvider) mView, new IBaseCallBack<SocketDataBean>() {
            @Override
            public void onSuccess(SocketDataBean data) {
                mView.socketUrl(data);
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
