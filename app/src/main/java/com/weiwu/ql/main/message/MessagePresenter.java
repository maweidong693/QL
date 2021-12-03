package com.weiwu.ql.main.message;

import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfoData;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.DelChatBody;
import com.weiwu.ql.data.request.GroupInfoRequestBody;
import com.weiwu.ql.main.contact.ContactContract;
import com.weiwu.ql.main.contact.chat.modle.MessageListData;

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
    public void attachView(ContactContract.IMessageView view) {
        mView = view;
    }

    @Override
    public void detachView(ContactContract.IMessageView view) {
        mView = null;
    }

    @Override
    public void delChat(DelChatBody body) {
        mSource.delChat((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
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
    public void getMessageList() {
        mSource.getMessageList((LifecycleProvider) mView, new IBaseCallBack<MessageListData>() {
            @Override
            public void onSuccess(MessageListData data) {
                mView.messageListReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }
}
