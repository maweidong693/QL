package com.weiwu.ql.main.contact.group.info.invites;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.bean.CheckInvitesData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.CheckInvitesRequestBody;
import com.weiwu.ql.main.contact.group.GroupContract;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/20 15:45 
 */
public class GroupInvitesPresenter implements GroupContract.ICheckInvitesPresenter {

    private GroupContract.ICheckInvitesView mView;
    private GroupContract.GroupSource mSource;

    public GroupInvitesPresenter(GroupContract.GroupSource source) {
        mSource = source;
    }

    @Override
    public void getAllInvites(String groupId) {
        mSource.getAllGroupInvites((LifecycleProvider) mView, groupId, new IBaseCallBack<CheckInvitesData>() {
            @Override
            public void onSuccess(CheckInvitesData data) {
                mView.onInvitesReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void checkInvites(String inviteId, String flag) {
        mSource.checkInvites((LifecycleProvider) mView, inviteId, flag, new IBaseCallBack<HttpResult>() {
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
    public void attachView(GroupContract.ICheckInvitesView view) {
        mView = view;
    }

    @Override
    public void detachView(GroupContract.ICheckInvitesView view) {
        mView = null;
    }
}
