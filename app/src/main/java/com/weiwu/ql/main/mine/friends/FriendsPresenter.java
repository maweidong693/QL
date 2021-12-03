package com.weiwu.ql.main.mine.friends;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.bean.FriendsData;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.bean.MineInfoData;
import com.weiwu.ql.data.bean.NewMsgData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.AddCommentRequestBody;
import com.weiwu.ql.data.request.FriendsRequestBody;
import com.weiwu.ql.data.request.RemoveCommentRequestBody;
import com.weiwu.ql.data.request.UpdateBgBody;
import com.weiwu.ql.data.request.UpdateMineInfoRequestBody;

import okhttp3.MultipartBody;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/19 09:36 
 */
public class FriendsPresenter implements FriendsContract.IFriendsPresenter {

    private FriendsContract.IFriendsView mView;
    private FriendsContract.FindSource mSource;

    public FriendsPresenter(FriendsContract.FindSource source) {
        mSource = source;
    }

    @Override
    public void addComment(AddCommentRequestBody body) {
        mSource.addComment((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
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
    public void getNewMsgCount() {
        mSource.getNewMsgCount((LifecycleProvider) mView, new IBaseCallBack<NewMsgData>() {
            @Override
            public void onSuccess(NewMsgData data) {
                mView.newMsgCountResult(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void deleteComment(RemoveCommentRequestBody body) {
        mSource.deleteComment((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
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
    public void getAllFriends(FriendsRequestBody body) {
        mSource.getAllFriends((LifecycleProvider) mView, body, new IBaseCallBack<FriendsData>() {
            @Override
            public void onSuccess(FriendsData data) {
                mView.allFriendsReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void deleteFriends(FriendsRequestBody body) {
        mSource.deleteFriends((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
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
    public void like(RemoveCommentRequestBody body) {
        mSource.like((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
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
    public void updateBg(UpdateBgBody body) {
        mSource.updateBg((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
            @Override
            public void onSuccess(HttpResult data) {
                mView.updateBgReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

//    @Override
//    public void getMineInfo() {
//        mSource.getMineInfo((LifecycleProvider) mView, new IBaseCallBack<MineInfoData>() {
//            @Override
//            public void onSuccess(MineInfoData data) {
//                mView.mineInfoReceive(data);
//            }
//
//            @Override
//            public void onFail(String msg, int statusCode) {
//                mView.onFail(msg, statusCode);
//            }
//        });
//    }

    @Override
    public void attachView(FriendsContract.IFriendsView view) {
        mView = view;
    }

    @Override
    public void detachView(FriendsContract.IFriendsView view) {
        mView = null;
    }
}
