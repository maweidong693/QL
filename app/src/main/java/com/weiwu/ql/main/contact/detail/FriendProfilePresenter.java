package com.weiwu.ql.main.contact.detail;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.AddFriendRequestBody;
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
    public void deleteFriend(String memberId) {
        mSource.deleteFriend((LifecycleProvider) mView, memberId, new IBaseCallBack<HttpResult>() {
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
    public void attachView(ContactContract.IFriendDetailView view) {
        mView = view;
    }

    @Override
    public void detachView(ContactContract.IFriendDetailView view) {
        mView = null;
    }
}
