package com.weiwu.ql.main.contact.group.member;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.GroupOwnerRequestBody;
import com.weiwu.ql.data.request.InviteOrDeleteRequestBody;
import com.weiwu.ql.main.contact.group.GroupContract;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/12 16:43 
 */
public class GroupMangerPresenter implements GroupContract.IGroupMangerPresenter {

    private GroupContract.IGroupMangerView mView;
    private GroupContract.GroupSource mSource;

    public GroupMangerPresenter(GroupContract.GroupSource source) {
        mSource = source;
    }

    @Override
    public void changGroupOwner(GroupOwnerRequestBody body) {
        mSource.changGroupOwner((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
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
    public void changeGroupManger(InviteOrDeleteRequestBody body) {
        mSource.changeGroupManger((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
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
    public void attachView(GroupContract.IGroupMangerView view) {
        mView = view;
    }

    @Override
    public void detachView(GroupContract.IGroupMangerView view) {
        mView = null;
    }
}
