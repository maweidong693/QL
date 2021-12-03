package com.weiwu.ql.main.contact.new_friend;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.bean.NewFriendListData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.NewFriendRequestBody;
import com.weiwu.ql.main.contact.ContactContract;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/24 13:57 
 */
public class NewFriendPresenter implements ContactContract.INewFriendPresenter {

    private ContactContract.INewFriendView mView;
    private ContactContract.ContactSource mSource;

    public NewFriendPresenter(ContactContract.ContactSource source) {
        mSource = source;
    }

    @Override
    public void getNewFriendList() {
        mSource.getNewFriendList((LifecycleProvider) mView, new IBaseCallBack<NewFriendListData>() {
            @Override
            public void onSuccess(NewFriendListData data) {
                mView.getNewFriendListReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void handlerNewFriendRequest(NewFriendRequestBody body) {
        mSource.handlerNewFriendRequest((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
            @Override
            public void onSuccess(HttpResult data) {
                mView.handlerFriendRequestReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void attachView(ContactContract.INewFriendView view) {
        mView = view;
    }

    @Override
    public void detachView(ContactContract.INewFriendView view) {
        mView = null;
    }
}
