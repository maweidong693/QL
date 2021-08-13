package com.weiwu.ql.main.contact.group.member;

import com.tencent.qcloud.tim.uikit.modules.group.info.GroupMemberData;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.InviteOrDeleteRequestBody;
import com.weiwu.ql.main.contact.group.GroupContract;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/13 10:14 
 */
public class GroupForbiddenPresenter implements GroupContract.IGroupForbiddenPresenter {

    private GroupContract.IGroupForbiddenView mView;
    private GroupContract.GroupSource mSource;

    public GroupForbiddenPresenter(GroupContract.GroupSource source) {
        mSource = source;
    }

    @Override
    public void getGroupMember(String groupId) {
        mSource.getGroupMember((LifecycleProvider) mView, groupId, new IBaseCallBack<GroupMemberData>() {
            @Override
            public void onSuccess(GroupMemberData data) {
                mView.groupMemberReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void forbiddenMember(InviteOrDeleteRequestBody body) {
        mSource.forbiddenMember((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
            @Override
            public void onSuccess(HttpResult data) {
                mView.forbiddenReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void cancelForbiddenMember(InviteOrDeleteRequestBody body) {
        mSource.cancelForbiddenMember((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
            @Override
            public void onSuccess(HttpResult data) {
                mView.forbiddenReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void attachView(GroupContract.IGroupForbiddenView view) {
        mView = view;
    }

    @Override
    public void detachView(GroupContract.IGroupForbiddenView view) {
        mView = null;
    }
}
