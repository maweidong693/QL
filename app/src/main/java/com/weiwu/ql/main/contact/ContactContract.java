package com.weiwu.ql.main.contact;

import com.tencent.common.http.BlackListData;
import com.tencent.common.http.ContactListData;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfoData;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.base.IBasePresenter;
import com.weiwu.ql.base.IBaseView;
import com.weiwu.ql.data.bean.FriendInfoData;
import com.weiwu.ql.data.bean.FriendPicsData;
import com.weiwu.ql.data.bean.GroupListData;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.bean.NewFriendListData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.AddFriendRequestBody;
import com.weiwu.ql.data.request.CollectionRequestBody;
import com.weiwu.ql.data.request.CreateGroupRequestBody;
import com.weiwu.ql.data.request.DelChatBody;
import com.weiwu.ql.data.request.DelMsgRequestBody;
import com.weiwu.ql.data.request.FriendInfoRequestBody;
import com.weiwu.ql.data.request.GroupInfoRequestBody;
import com.weiwu.ql.data.request.HistoryMsgRequestBody;
import com.weiwu.ql.data.request.MessageRequestBody;
import com.weiwu.ql.data.request.NewFriendRequestBody;
import com.weiwu.ql.data.request.SendGroupMsgRequestBody;
import com.weiwu.ql.data.request.SendMsgRequestBody;
import com.weiwu.ql.data.request.SetFriendRequestBody;
import com.weiwu.ql.main.contact.chat.modle.HistoryMessageData;
import com.weiwu.ql.main.contact.chat.modle.MessageListData;
import com.weiwu.ql.main.contact.chat.modle.SendMessageData;

import okhttp3.MultipartBody;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/22 15:30 
 */
public interface ContactContract {
    interface IContactView extends IBaseView<IContactPresenter> {
        void contactReceive(ContactListData data);

        void groupListReceive(GroupListData data);

        void blackListReceive(BlackListData data);

        void createGroupResult(HttpResult result);

        void onFail(String msg, int code);
    }

    interface IContactPresenter extends IBasePresenter<IContactView> {
        void getContactList();

        void getBlackList();

        void getGroupList();

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
        void getNewFriendList();

        void handlerNewFriendRequest(NewFriendRequestBody body);
    }

    interface IChatView extends IBaseView<IChatPresenter> {
        void delMsgSuccess(HttpResult data);

        void chatHistoryResult(HistoryMessageData data);

        void sendResult(SendMessageData data);

        void uploadPicResult(LoginData data);

        void groupInfoReceive(GroupInfoData data);

        void onFail(String msg, int code);

        void onSuccess(HttpResult data);
    }

    interface IChatPresenter extends IBasePresenter<IChatView> {
        void delMsg(DelMsgRequestBody body);

        void collection(CollectionRequestBody body);

        void getHistoryMsg(HistoryMsgRequestBody body);

        void setChatStatus(MessageRequestBody body);

        void getGroupHistoryMsg(HistoryMsgRequestBody body);

        void sendMessage(SendMsgRequestBody body);

        void sendGroupMessage(SendGroupMsgRequestBody body);

        void uploadPic(MultipartBody.Part file);

        void getGroupInfo(GroupInfoRequestBody body);
    }

    interface IMessageView extends IBaseView<IMessagePresenter> {

        void messageListReceive(MessageListData data);

        void onSuccess(HttpResult data);

        void onFail(String msg, int code);
    }

    interface IMessagePresenter extends IBasePresenter<IMessageView> {

        void delChat(DelChatBody body);

        void getMessageList();
    }

    interface IFriendDetailView extends IBaseView<IFriendDetailPresenter> {
        void friendInfoReceive(FriendInfoData data);

        void friendPicsReceive(FriendPicsData data);

        void deleteFriendResult(HttpResult result);

        void setFriendInfoResult(HttpResult result);

        void onFail(String msg, int code);
    }

    interface IFriendDetailPresenter extends IBasePresenter<IFriendDetailView> {
        void getFriendInfo(FriendInfoRequestBody body);

        void getFriendPics(SetFriendRequestBody body);

        void deleteFriend(AddFriendRequestBody body);

        void setFriendInfo(SetFriendRequestBody body);
    }

    interface ContactSource {
        void sendMessage(LifecycleProvider provider, SendMsgRequestBody body, IBaseCallBack<SendMessageData> callBack);

        void sendGroupMessage(LifecycleProvider provider, SendGroupMsgRequestBody body, IBaseCallBack<SendMessageData> callBack);

        void getContactList(LifecycleProvider provider, IBaseCallBack<ContactListData> callBack);

        void addFriend(LifecycleProvider provider, AddFriendRequestBody body, IBaseCallBack<HttpResult> callBack);

        void getNewFriendList(LifecycleProvider provider, IBaseCallBack<NewFriendListData> callBack);

        void handlerNewFriendRequest(LifecycleProvider provider, NewFriendRequestBody body, IBaseCallBack<HttpResult> callBack);

        void uploadPic(LifecycleProvider lifecycleProvider, MultipartBody.Part file, IBaseCallBack<LoginData> callBack);

        void deleteFriend(LifecycleProvider provider, AddFriendRequestBody body, IBaseCallBack<HttpResult> callBack);

        void createGroup(LifecycleProvider provider, CreateGroupRequestBody body, IBaseCallBack<HttpResult> callBack);

        void findFriendId(LifecycleProvider provider, String mobile, IBaseCallBack<FriendInfoData> callBack);

        void getGroupInfo(LifecycleProvider provider, GroupInfoRequestBody body, IBaseCallBack<GroupInfoData> callBack);

        void getMessageList(LifecycleProvider provider, IBaseCallBack<MessageListData> callBack);

        void getHistoryMsg(LifecycleProvider provider, HistoryMsgRequestBody body, IBaseCallBack<HistoryMessageData> callBack);

        void getGroupHistoryMsg(LifecycleProvider provider, HistoryMsgRequestBody body, IBaseCallBack<HistoryMessageData> callBack);

        void getGroupList(LifecycleProvider provider, IBaseCallBack<GroupListData> callBack);

        void setMessageStatus(LifecycleProvider provider, MessageRequestBody body, IBaseCallBack<HttpResult> callBack);

        void setFriendInfo(LifecycleProvider provider, SetFriendRequestBody body, IBaseCallBack<HttpResult> callBack);

        void getBlackList(LifecycleProvider provider, IBaseCallBack<BlackListData> callBack);

        void getFriendInfo(LifecycleProvider provider, FriendInfoRequestBody body, IBaseCallBack<FriendInfoData> callBack);

        void delMsg(LifecycleProvider provider, DelMsgRequestBody body, IBaseCallBack<HttpResult> callBack);

        void collection(LifecycleProvider provider, CollectionRequestBody body, IBaseCallBack<HttpResult> callBack);

        void getFriendPics(LifecycleProvider provider, SetFriendRequestBody body, IBaseCallBack<FriendPicsData> callBack);

        void delChat(LifecycleProvider provider, DelChatBody body, IBaseCallBack<HttpResult> callBack);

//        void collectionList(LifecycleProvider provider,IBaseCallBack)
    }
}
