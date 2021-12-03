package com.weiwu.ql.data.repositories;

import com.tencent.common.http.BlackListData;
import com.tencent.common.http.ContactListData;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfoData;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.BaseRepository;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.bean.FriendInfoData;
import com.weiwu.ql.data.bean.FriendPicsData;
import com.weiwu.ql.data.bean.GroupListData;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.bean.NewFriendListData;
import com.weiwu.ql.data.network.DataService;
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
import com.weiwu.ql.main.contact.ContactContract;
import com.weiwu.ql.main.contact.chat.modle.HistoryMessageData;
import com.weiwu.ql.main.contact.chat.modle.MessageListData;
import com.weiwu.ql.main.contact.chat.modle.SendMessageData;

import okhttp3.MultipartBody;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/22 15:34 
 */
public class ContactRepository extends BaseRepository implements ContactContract.ContactSource {

    public volatile static ContactRepository mRepository;

    public static ContactRepository getInstance() {
        if (mRepository == null) {
            synchronized (ContactRepository.class) {
                if (mRepository == null) {
                    mRepository = new ContactRepository();
                }
            }
        }
        return mRepository;
    }

    @Override
    public void sendMessage(LifecycleProvider provider, SendMsgRequestBody body, IBaseCallBack<SendMessageData> callBack) {
        observerNoMap(provider, DataService.getApiService().sendMessage(body), callBack);
    }

    @Override
    public void sendGroupMessage(LifecycleProvider provider, SendGroupMsgRequestBody body, IBaseCallBack<SendMessageData> callBack) {
        observerNoMap(provider, DataService.getApiService().sendGroupMsg(body), callBack);
    }

    @Override
    public void getContactList(LifecycleProvider provider, IBaseCallBack<ContactListData> callBack) {
        observerNoMap(provider, DataService.getApiService().getContactList(), callBack);
    }

    @Override
    public void addFriend(LifecycleProvider provider, AddFriendRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().addFriend(body), callBack);
    }

    @Override
    public void getNewFriendList(LifecycleProvider provider, IBaseCallBack<NewFriendListData> callBack) {
        observerNoMap(provider, DataService.getApiService().getNewFriendList(), callBack);
    }

    @Override
    public void handlerNewFriendRequest(LifecycleProvider provider, NewFriendRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().handlerNewFriendRequest(body), callBack);
    }

    @Override
    public void uploadPic(LifecycleProvider lifecycleProvider, MultipartBody.Part file, IBaseCallBack<LoginData> callBack) {
        observerNoMap(lifecycleProvider, DataService.getApiService().uploadPicture(file), callBack);
    }

    @Override
    public void deleteFriend(LifecycleProvider provider, AddFriendRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().deleteFriend(body), callBack);
    }

    @Override
    public void createGroup(LifecycleProvider provider, CreateGroupRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().createGroup(body), callBack);
    }

    @Override
    public void findFriendId(LifecycleProvider provider, String mobile, IBaseCallBack<FriendInfoData> callBack) {
        observerNoMap(provider, DataService.getApiService().findFriendId(mobile), callBack);
    }

    @Override
    public void getGroupInfo(LifecycleProvider provider, GroupInfoRequestBody body, IBaseCallBack<GroupInfoData> callBack) {
        observerNoMap(provider, DataService.getApiService().getGroupInfo(body), callBack);
    }

    @Override
    public void getMessageList(LifecycleProvider provider, IBaseCallBack<MessageListData> callBack) {
        observerNoMap(provider, DataService.getApiService().getMessageList(), callBack);
    }

    @Override
    public void getHistoryMsg(LifecycleProvider provider, HistoryMsgRequestBody body, IBaseCallBack<HistoryMessageData> callBack) {
        observerNoMap(provider, DataService.getApiService().getHistoryMsg(body), callBack);
    }

    @Override
    public void getGroupHistoryMsg(LifecycleProvider provider, HistoryMsgRequestBody body, IBaseCallBack<HistoryMessageData> callBack) {
        observerNoMap(provider, DataService.getApiService().getGroupHistoryMsg(body), callBack);
    }

    @Override
    public void getGroupList(LifecycleProvider provider, IBaseCallBack<GroupListData> callBack) {
        observerNoMap(provider, DataService.getApiService().getGroupList(), callBack);
    }

    @Override
    public void setMessageStatus(LifecycleProvider provider, MessageRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().setChatStatus(body), callBack);
    }

    @Override
    public void setFriendInfo(LifecycleProvider provider, SetFriendRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().setFriendsInfo(body), callBack);
    }

    @Override
    public void getBlackList(LifecycleProvider provider, IBaseCallBack<BlackListData> callBack) {
        observerNoMap(provider, DataService.getApiService().getBlackList(), callBack);
    }

    @Override
    public void getFriendInfo(LifecycleProvider provider, FriendInfoRequestBody body, IBaseCallBack<FriendInfoData> callBack) {
        observerNoMap(provider, DataService.getApiService().getFriendInfo(body), callBack);
    }

    @Override
    public void delMsg(LifecycleProvider provider, DelMsgRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().delMsg(body), callBack);
    }

    @Override
    public void collection(LifecycleProvider provider, CollectionRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().collection(body), callBack);
    }

    @Override
    public void getFriendPics(LifecycleProvider provider, SetFriendRequestBody body, IBaseCallBack<FriendPicsData> callBack) {
        observerNoMap(provider, DataService.getApiService().getFriendsPics(body), callBack);
    }

    @Override
    public void delChat(LifecycleProvider provider, DelChatBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().delChat(body), callBack);
    }
}
