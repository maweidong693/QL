package com.weiwu.ql.main.contact.detail;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.bean.FriendInfoData;
import com.weiwu.ql.data.bean.FriendPicsData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.AddFriendRequestBody;
import com.weiwu.ql.data.request.FriendInfoRequestBody;
import com.weiwu.ql.data.request.SetFriendRequestBody;
import com.weiwu.ql.main.contact.ContactContract;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/30 16:24 
 */
public class FriendProfilePresenter implements ContactContract.IFriendDetailPresenter {

    private ContactContract.IFriendDetailView mView;
    private ContactContract.ContactSource mSource;

    public FriendProfilePresenter(ContactContract.ContactSource source) {
        mSource = source;
    }

    @Override
    public void getFriendInfo(FriendInfoRequestBody body) {
        mSource.getFriendInfo((LifecycleProvider) mView, body, new IBaseCallBack<FriendInfoData>() {
            @Override
            public void onSuccess(FriendInfoData data) {
                mView.friendInfoReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void getFriendPics(SetFriendRequestBody body) {
        mSource.getFriendPics((LifecycleProvider) mView, body, new IBaseCallBack<FriendPicsData>() {
            @Override
            public void onSuccess(FriendPicsData data) {
                mView.friendPicsReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void deleteFriend(AddFriendRequestBody body) {
        mSource.deleteFriend((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
            @Override
            public void onSuccess(HttpResult data) {
                mView.deleteFriendResult(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void setFriendInfo(SetFriendRequestBody body) {
        mSource.setFriendInfo((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
            @Override
            public void onSuccess(HttpResult data) {
                mView.setFriendInfoResult(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void attachView(ContactContract.IFriendDetailView view) {
        mView = view;
    }

    @Override
    public void detachView(ContactContract.IFriendDetailView view) {
        mView = null;
    }
}
