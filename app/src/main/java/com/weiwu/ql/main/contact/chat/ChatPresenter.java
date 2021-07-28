package com.weiwu.ql.main.contact.chat;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.main.contact.ContactContract;

import okhttp3.MultipartBody;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/24 17:29 
 */
public class ChatPresenter implements ContactContract.IChatPresenter {

    private ContactContract.IChatView mView;
    private ContactContract.ContactSource mSource;

    public ChatPresenter(ContactContract.ContactSource source) {
        mSource = source;
    }

    @Override
    public void uploadPic(MultipartBody.Part file) {
        mSource.uploadPic((LifecycleProvider) mView, file, new IBaseCallBack<LoginData>() {
            @Override
            public void onSuccess(LoginData data) {
                mView.uploadPicResult(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void attachView(ContactContract.IChatView view) {
        mView = view;
    }

    @Override
    public void detachView(ContactContract.IChatView view) {
        mView = null;
    }
}
