package com.weiwu.ql.main.contact.add;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.AddFriendRequestBody;
import com.weiwu.ql.main.contact.ContactContract;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/23 13:46 
 */
public class AddFriendPresenter implements ContactContract.IAddFriendPresenter {

    private ContactContract.IAddFriendView mView;
    private ContactContract.ContactSource mSource;

    public AddFriendPresenter(ContactContract.ContactSource source) {
        mSource = source;
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
    public void attachView(ContactContract.IAddFriendView view) {
        mView = view;
    }

    @Override
    public void detachView(ContactContract.IAddFriendView view) {
        mView = null;
    }
}
