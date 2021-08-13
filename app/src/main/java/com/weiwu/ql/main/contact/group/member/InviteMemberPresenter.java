package com.weiwu.ql.main.contact.group.member;

import com.tencent.common.http.ContactListData;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.InviteOrDeleteRequestBody;
import com.weiwu.ql.main.contact.group.GroupContract;

import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/11 16:44 
 */
public class InviteMemberPresenter implements GroupContract.IGroupAddPresenter {

    private GroupContract.IGroupAddView mView;
    private GroupContract.GroupSource mSource;

    public InviteMemberPresenter(GroupContract.GroupSource source) {
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
    public void addMembers(InviteOrDeleteRequestBody body) {
        mSource.addMembers((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
            @Override
            public void onSuccess(HttpResult data) {
                mView.inviteFriendsReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void deleteMembers(InviteOrDeleteRequestBody body) {
        mSource.deleteMembers((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
            @Override
            public void onSuccess(HttpResult data) {
                mView.deleteFriendReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void attachView(GroupContract.IGroupAddView view) {
        mView = view;
    }

    @Override
    public void detachView(GroupContract.IGroupAddView view) {
        mView = null;
    }
}
