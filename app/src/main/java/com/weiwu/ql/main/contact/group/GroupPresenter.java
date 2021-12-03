package com.weiwu.ql.main.contact.group;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfoData;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.ForbiddenRequestBody;
import com.weiwu.ql.data.request.GroupInfoRequestBody;
import com.weiwu.ql.data.request.InviteOrDeleteRequestBody;
import com.weiwu.ql.data.request.UpdateGroupRequestBody;

import okhttp3.MultipartBody;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/11 13:29 
 */
public class GroupPresenter implements GroupContract.IGroupPresenter {

    private GroupContract.IGroupView mView;
    private GroupContract.GroupSource mSource;

    public GroupPresenter(GroupContract.GroupSource source) {
        mSource = source;
    }

    @Override
    public void getGroupInfo(GroupInfoRequestBody body) {
        mSource.getGroupInfo((LifecycleProvider) mView, body, new IBaseCallBack<GroupInfoData>() {
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
    public void dissolveGroup(GroupInfoRequestBody body) {
        mSource.dissolveGroup((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
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
    public void signGroup(GroupInfoRequestBody body) {
        mSource.signGroup((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
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
    public void uploadPic(MultipartBody.Part file) {
        mSource.uploadPic((LifecycleProvider) mView, file, new IBaseCallBack<LoginData>() {
            @Override
            public void onSuccess(LoginData data) {
                mView.uploadReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void allMute(ForbiddenRequestBody body) {
        mSource.forbiddenMember((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
            @Override
            public void onSuccess(HttpResult data) {
                mView.allMuteReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void cancelMute(ForbiddenRequestBody body) {
        mSource.cancelForbiddenMember((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
            @Override
            public void onSuccess(HttpResult data) {
                mView.allMuteReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void updateGroupInfo(UpdateGroupRequestBody body) {
        mSource.updateGroupInfo((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
            @Override
            public void onSuccess(HttpResult data) {
                mView.allMuteReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void attachView(GroupContract.IGroupView view) {
        mView = view;
    }

    @Override
    public void detachView(GroupContract.IGroupView view) {
        mView = null;
    }
}
