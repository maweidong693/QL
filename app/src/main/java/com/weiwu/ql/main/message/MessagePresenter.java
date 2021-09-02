package com.weiwu.ql.main.message;

import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfoData;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.main.contact.ContactContract;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/25 16:48 
 */
public class MessagePresenter implements ContactContract.IMessagePresenter {

    private ContactContract.IMessageView mView;
    private ContactContract.ContactSource mSource;

    public MessagePresenter(ContactContract.ContactSource source) {
        mSource = source;
    }

    @Override
    public void getGroupInfo(String groupId) {
        mSource.getGroupInfo((LifecycleProvider) mView, groupId, new IBaseCallBack<GroupInfoData>() {
            @Override
            public void onSuccess(GroupInfoData data) {
                mView.groupInfoReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void attachView(ContactContract.IMessageView view) {
        mView = view;
    }

    @Override
    public void detachView(ContactContract.IMessageView view) {
        mView = null;
    }
}
