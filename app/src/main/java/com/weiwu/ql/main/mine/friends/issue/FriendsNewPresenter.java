package com.weiwu.ql.main.mine.friends.issue;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.IssueMessageRequestBody;
import com.weiwu.ql.main.mine.friends.FriendsContract;

import okhttp3.MultipartBody;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/17 16:37 
 */
public class FriendsNewPresenter implements FriendsContract.IIssuePresenter {

    private FriendsContract.IIssueView mView;
    private FriendsContract.FindSource mSource;

    public FriendsNewPresenter(FriendsContract.FindSource source) {
        mSource = source;
    }

    @Override
    public void issueMessage(IssueMessageRequestBody body) {
        mSource.issueMessage((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
            @Override
            public void onSuccess(HttpResult data) {
                mView.issueResult(data);
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
                mView.uploadResult(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void attachView(FriendsContract.IIssueView view) {
        mView = view;
    }

    @Override
    public void detachView(FriendsContract.IIssueView view) {
        mView = null;
    }
}
