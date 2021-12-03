package com.weiwu.ql.main.mine.friends;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.base.IBasePresenter;
import com.weiwu.ql.base.IBaseView;
import com.weiwu.ql.data.bean.FindData;
import com.weiwu.ql.data.bean.FriendsData;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.bean.MineInfoData;
import com.weiwu.ql.data.bean.NewMsgCount;
import com.weiwu.ql.data.bean.NewMsgData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.AddCommentRequestBody;
import com.weiwu.ql.data.request.FriendsRequestBody;
import com.weiwu.ql.data.request.GroupInfoRequestBody;
import com.weiwu.ql.data.request.IssueMessageRequestBody;
import com.weiwu.ql.data.request.RemindRequestBody;
import com.weiwu.ql.data.request.RemoveCommentRequestBody;
import com.weiwu.ql.data.request.UpdateBgBody;
import com.weiwu.ql.data.request.UpdateMineInfoRequestBody;
import com.weiwu.ql.main.mine.friends.data.MsgData;

import okhttp3.MultipartBody;
import retrofit2.http.Body;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/17 15:47 
 */
public interface FriendsContract {
    interface IIssueView extends IBaseView<IIssuePresenter> {
        void uploadResult(LoginData data);

        void issueResult(HttpResult data);

        void onFail(String msg, int code);
    }

    interface IIssuePresenter extends IBasePresenter<IIssueView> {
        void issueMessage(IssueMessageRequestBody body);

        void uploadPic(MultipartBody.Part file);

    }

    interface IFriendsView extends IBaseView<IFriendsPresenter> {
        void allFriendsReceive(FriendsData data);

        void newMsgCountResult(NewMsgData data);
//        void mineInfoReceive(MineInfoData data);

        void uploadReceive(LoginData data);

        void updateBgReceive(HttpResult data);

        void onSuccess(HttpResult data);

        void onFail(String msg, int code);
    }

    public interface IMsgView extends IBaseView<IMsgPresenter> {
        void msgReceive(MsgData data);

        void onSuccess(HttpResult data);

        void onFail(String msg, int code);
    }

    public interface IMsgPresenter extends IBasePresenter<IMsgView> {
        void getMsgList();

        void readMsg(RemindRequestBody body);

    }

    interface IFriendsPresenter extends IBasePresenter<IFriendsView> {
        void addComment(AddCommentRequestBody body);

        void getNewMsgCount();

        void deleteComment(RemoveCommentRequestBody body);

        void getAllFriends(FriendsRequestBody body);

        void deleteFriends(FriendsRequestBody body);

        void like(RemoveCommentRequestBody body);

        void uploadPic(MultipartBody.Part file);

        void updateBg(UpdateBgBody body);
    }

    interface IFindView extends IBaseView<IFindPresenter> {
        void onSuccess(FindData data);

        void onFail(String msg, int code);
    }

    interface IFindPresenter extends IBasePresenter<IFindView> {
        void getFindList();
    }

    interface FindSource {
        void issueMessage(LifecycleProvider provider, IssueMessageRequestBody body, IBaseCallBack<HttpResult> callBack);

        void uploadPic(LifecycleProvider provider, MultipartBody.Part file, IBaseCallBack<LoginData> callBack);

        void getAllFriends(LifecycleProvider provider, FriendsRequestBody body, IBaseCallBack<FriendsData> callBack);

        void deleteFriends(LifecycleProvider provider, FriendsRequestBody body, IBaseCallBack<HttpResult> callBack);

        void addComment(LifecycleProvider provider, AddCommentRequestBody body, IBaseCallBack<HttpResult> callBack);

        void deleteComment(LifecycleProvider provider, RemoveCommentRequestBody body, IBaseCallBack<HttpResult> callBack);

        void updateBg(LifecycleProvider provider, UpdateBgBody body, IBaseCallBack<HttpResult> callBack);

//        void getMineInfo(LifecycleProvider provider, IBaseCallBack<MineInfoData> callBack);

        void getFindList(LifecycleProvider provider, IBaseCallBack<FindData> callBack);

        void like(LifecycleProvider provider, RemoveCommentRequestBody body, IBaseCallBack<HttpResult> callBack);

        void getNewMsgCount(LifecycleProvider provider, IBaseCallBack<NewMsgData> callBack);

        void getMsgList(LifecycleProvider provider, IBaseCallBack<MsgData> callBack);

        void readMsg(LifecycleProvider provider, RemindRequestBody body, IBaseCallBack<HttpResult> callBack);

    }
}
