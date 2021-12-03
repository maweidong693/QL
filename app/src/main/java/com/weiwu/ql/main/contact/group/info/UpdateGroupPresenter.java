package com.weiwu.ql.main.contact.group.info;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.SetFriendRequestBody;
import com.weiwu.ql.data.request.UpdateGroupRequestBody;
import com.weiwu.ql.data.request.UpdateMineInfoRequestBody;
import com.weiwu.ql.main.contact.group.GroupContract;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/14 17:40 
 */
public class UpdateGroupPresenter implements GroupContract.IUpdateGroupInfoPresenter {

    private GroupContract.IUpdateGroupInfoView mView;
    private GroupContract.GroupSource mSource;

    public UpdateGroupPresenter(GroupContract.GroupSource source) {
        mSource = source;
    }

    @Override
    public void updateGroupInfo(UpdateGroupRequestBody body) {
        mSource.updateGroupInfo((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
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
    public void updateFriendInfo(SetFriendRequestBody body) {
        mSource.setFriendInfo((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
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
    public void updateMineInfo(UpdateMineInfoRequestBody body) {
        mSource.updateMineInfo((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
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
    public void attachView(GroupContract.IUpdateGroupInfoView view) {
        mView = view;
    }

    @Override
    public void detachView(GroupContract.IUpdateGroupInfoView view) {
        mView = null;
    }
}
