package com.weiwu.ql.main.contact.chat;

import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfoData;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.CollectionRequestBody;
import com.weiwu.ql.data.request.DelMsgRequestBody;
import com.weiwu.ql.data.request.GroupInfoRequestBody;
import com.weiwu.ql.data.request.HistoryMsgRequestBody;
import com.weiwu.ql.data.request.MessageRequestBody;
import com.weiwu.ql.data.request.SendGroupMsgRequestBody;
import com.weiwu.ql.data.request.SendMsgRequestBody;
import com.weiwu.ql.main.contact.ContactContract;
import com.weiwu.ql.main.contact.chat.modle.HistoryMessageData;
import com.weiwu.ql.main.contact.chat.modle.SendMessageData;

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
    public void delMsg(DelMsgRequestBody body) {
        mSource.delMsg((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
            @Override
            public void onSuccess(HttpResult data) {
                mView.delMsgSuccess(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void collection(CollectionRequestBody body) {
        mSource.collection((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
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
    public void getHistoryMsg(HistoryMsgRequestBody body) {
        mSource.getHistoryMsg((LifecycleProvider) mView, body, new IBaseCallBack<HistoryMessageData>() {
            @Override
            public void onSuccess(HistoryMessageData data) {
                mView.chatHistoryResult(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void setChatStatus(MessageRequestBody body) {
        mSource.setMessageStatus((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
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
    public void getGroupHistoryMsg(HistoryMsgRequestBody body) {
        mSource.getGroupHistoryMsg((LifecycleProvider) mView, body, new IBaseCallBack<HistoryMessageData>() {
            @Override
            public void onSuccess(HistoryMessageData data) {
                mView.chatHistoryResult(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void sendMessage(SendMsgRequestBody body) {
        mSource.sendMessage((LifecycleProvider) mView, body, new IBaseCallBack<SendMessageData>() {
            @Override
            public void onSuccess(SendMessageData data) {
                mView.sendResult(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void sendGroupMessage(SendGroupMsgRequestBody body) {
        mSource.sendGroupMessage((LifecycleProvider) mView, body, new IBaseCallBack<SendMessageData>() {
            @Override
            public void onSuccess(SendMessageData data) {
                mView.sendResult(data);
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
                mView.uploadPicResult(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
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
    public void attachView(ContactContract.IChatView view) {
        mView = view;
    }

    @Override
    public void detachView(ContactContract.IChatView view) {
        mView = null;
    }
}
