package com.weiwu.ql.main.mine.detail;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.main.mine.MineContract;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/9/1 17:57 
 */
public class InvitePresenter implements MineContract.IInvitePresenter {

    private MineContract.IInviteView mView;
    private MineContract.MineSource mSource;

    public InvitePresenter(MineContract.MineSource source) {
        mSource = source;
    }

    @Override
    public void getInviteKey() {
        mSource.getInviteKey((LifecycleProvider) mView, new IBaseCallBack<LoginData>() {
            @Override
            public void onSuccess(LoginData data) {
                mView.inviteKeyReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void attachView(MineContract.IInviteView view) {
        mView = view;
    }

    @Override
    public void detachView(MineContract.IInviteView view) {
        mView = null;
    }
}
