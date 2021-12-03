package com.weiwu.ql.main.mine.friends.msg;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.GroupInfoRequestBody;
import com.weiwu.ql.data.request.RemindRequestBody;
import com.weiwu.ql.main.mine.friends.FriendsContract;
import com.weiwu.ql.main.mine.friends.data.MsgData;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/11/24 18:32 
 */
public class NewMsgPresenter implements FriendsContract.IMsgPresenter {

    private FriendsContract.IMsgView mView;
    private FriendsContract.FindSource mSource;

    public NewMsgPresenter(FriendsContract.FindSource source) {
        mSource = source;
    }

    @Override
    public void attachView(FriendsContract.IMsgView view) {
        mView = view;
    }

    @Override
    public void detachView(FriendsContract.IMsgView view) {
        mView = null;
    }

    @Override
    public void getMsgList() {
        mSource.getMsgList((LifecycleProvider) mView, new IBaseCallBack<MsgData>() {
            @Override
            public void onSuccess(MsgData data) {
                mView.msgReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void readMsg(RemindRequestBody body) {
        mSource.readMsg((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
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
}
