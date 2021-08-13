package com.weiwu.ql.main.contact;

import com.tencent.common.http.ContactListData;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.CreateGroupRequestBody;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/22 15:38 
 */
public class ContactPresenter implements ContactContract.IContactPresenter {

    private ContactContract.IContactView mView;
    private ContactContract.ContactSource mSource;

    public ContactPresenter(ContactContract.ContactSource source) {
        mSource = source;
    }

    @Override
    public void getContactList() {
        mSource.getContactList((LifecycleProvider) mView, new IBaseCallBack<ContactListData>() {
            @Override
            public void onSuccess(ContactListData data) {
                mView.contactReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void createGroup(CreateGroupRequestBody body) {
        mSource.createGroup((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
            @Override
            public void onSuccess(HttpResult data) {
                mView.createGroupResult(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void attachView(ContactContract.IContactView view) {
        mView = view;
    }

    @Override
    public void detachView(ContactContract.IContactView view) {
        mView = null;
    }
}
