package com.weiwu.ql.main.contact;

import com.tencent.common.http.ContactListData;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfoData;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.base.IBasePresenter;
import com.weiwu.ql.base.IBaseView;
import com.weiwu.ql.data.bean.FriendInfoData;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.bean.MineInfoData;
import com.weiwu.ql.data.bean.NewFriendListData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.AddFriendRequestBody;
import com.weiwu.ql.data.request.CreateGroupRequestBody;
import com.weiwu.ql.data.request.NewFriendRequestBody;

import okhttp3.MultipartBody;
import retrofit2.http.HTTP;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/22 15:30 
 */
public interface ContactContract {
    interface IContactView extends IBaseView<IContactPresenter> {
        void contactReceive(ContactListData data);

        void createGroupResult(HttpResult result);

        void onFail(String msg, int code);
    }

    interface IContactPresenter extends IBasePresenter<IContactView> {
        void getContactList();

        void createGroup(CreateGroupRequestBody body);
    }

    interface IAddFriendView extends IBaseView<IAddFriendPresenter> {
        void addFriendReceive(HttpResult data);

        void findFriendIdReceive(FriendInfoData data);

        void onFail(String msg, int code);
    }

    interface IAddFriendPresenter extends IBasePresenter<IAddFriendView> {
        void addFriend(AddFriendRequestBody body);

        void findFriendId(String mobile);
    }

    interface INewFriendView extends IBaseView<INewFriendPresenter> {
        void getNewFriendListReceive(NewFriendListData data);

        void handlerFriendRequestReceive(HttpResult data);

        void onFail(String msg, int code);
    }

    interface INewFriendPresenter extends IBasePresenter<INewFriendView> {
        void getNewFriendList(NewFriendRequestBody body);

        void handlerNewFriendRequest(NewFriendRequestBody body);
    }

    interface IChatView extends IBaseView<IChatPresenter> {
        void uploadPicResult(LoginData data);

        void groupInfoReceive(GroupInfoData data);

        void onFail(String msg, int code);
    }

    interface IChatPresenter extends IBasePresenter<IChatView> {
        void uploadPic(MultipartBody.Part file);

        void getGroupInfo(String groupId);
    }

    interface IMessageView extends IBaseView<IMessagePresenter> {
        void groupInfoReceive(GroupInfoData data);

        void onFail(String msg, int code);
    }

    interface IMessagePresenter extends IBasePresenter<IMessageView> {

        void getGroupInfo(String groupId);
    }

    interface IFriendDetailView extends IBaseView<IFriendDetailPresenter> {
        void deleteFriendResult(HttpResult result);

        void onFail(String msg, int code);
    }

    interface IFriendDetailPresenter extends IBasePresenter<IFriendDetailView> {
        void deleteFriend(String memberId);
    }

    interface ContactSource {
        void getContactList(LifecycleProvider provider, IBaseCallBack<ContactListData> callBack);

        void addFriend(LifecycleProvider provider, AddFriendRequestBody body, IBaseCallBack<HttpResult> callBack);

        void getNewFriendList(LifecycleProvider provider, NewFriendRequestBody body, IBaseCallBack<NewFriendListData> callBack);

        void handlerNewFriendRequest(LifecycleProvider provider, NewFriendRequestBody body, IBaseCallBack<HttpResult> callBack);

        void uploadPic(LifecycleProvider lifecycleProvider, MultipartBody.Part file, IBaseCallBack<LoginData> callBack);

        void deleteFriend(LifecycleProvider provider, String memberId, IBaseCallBack<HttpResult> callBack);

        void createGroup(LifecycleProvider provider, CreateGroupRequestBody body, IBaseCallBack<HttpResult> callBack);

        void findFriendId(LifecycleProvider provider, String mobile, IBaseCallBack<FriendInfoData> callBack);

        void getGroupInfo(LifecycleProvider provider, String groupId, IBaseCallBack<GroupInfoData> callBack);
    }
}
