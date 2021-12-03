package com.weiwu.ql.main.contact.group.member;

import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfoData;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupMemberData;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.request.GroupInfoRequestBody;
import com.weiwu.ql.main.contact.group.GroupContract;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/11/22 16:44 
 */
public class GroupMemberPresenter implements GroupContract.IGroupMemberPresenter {

    private GroupContract.GroupSource mSource;
    private GroupContract.IGroupMemberView mView;

    public GroupMemberPresenter(GroupContract.GroupSource source) {
        mSource = source;
    }


    @Override
    public void attachView(GroupContract.IGroupMemberView view) {
        mView = view;
    }

    @Override
    public void detachView(GroupContract.IGroupMemberView view) {
        mView = null;
    }

    @Override
    public void getGroupMember(GroupInfoRequestBody body) {
        mSource.getGroupMember((LifecycleProvider) mView, body, new IBaseCallBack<GroupInfoData>() {
            @Override
            public void onSuccess(GroupInfoData data) {
                mView.groupMemberReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }
}
