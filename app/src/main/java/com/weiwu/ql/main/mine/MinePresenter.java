package com.weiwu.ql.main.mine;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.bean.MineInfoData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.AddFriendRequestBody;
import com.weiwu.ql.data.request.WebLoginRequestBody;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/24 14:33 
 */
public class MinePresenter implements MineContract.IMinePresenter {

    private MineContract.IMineView mView;
    private MineContract.MineSource mSource;

    public MinePresenter(MineContract.MineSource source) {
        mSource = source;
    }

    @Override
    public void getMineInfo() {
        mSource.getMineInfo((LifecycleProvider) mView, new IBaseCallBack<MineInfoData>() {
            @Override
            public void onSuccess(MineInfoData data) {
                mView.mineInfoReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void addFriend(AddFriendRequestBody body) {
        mSource.addFriend((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
            @Override
            public void onSuccess(HttpResult data) {
                mView.addFriendReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void attachView(MineContract.IMineView view) {
        mView = view;
    }

    @Override
    public void detachView(MineContract.IMineView view) {
        mView = null;
    }
}
