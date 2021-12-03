package com.weiwu.ql.data.repositories;

import com.tencent.common.http.ContactListData;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupMemberData;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.BaseRepository;
import com.weiwu.ql.base.IBaseCallBack;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfoData;
import com.weiwu.ql.data.bean.CheckInvitesData;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.network.DataService;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.CheckInvitesRequestBody;
import com.weiwu.ql.data.request.ForbiddenRequestBody;
import com.weiwu.ql.data.request.GroupInfoRequestBody;
import com.weiwu.ql.data.request.GroupOwnerRequestBody;
import com.weiwu.ql.data.request.GroupRequestBody;
import com.weiwu.ql.data.request.InviteOrDeleteRequestBody;
import com.weiwu.ql.data.request.SetFriendRequestBody;
import com.weiwu.ql.data.request.UpdateGroupRequestBody;
import com.weiwu.ql.data.request.UpdateMineInfoRequestBody;
import com.weiwu.ql.main.contact.group.GroupContract;

import java.util.List;

import okhttp3.MultipartBody;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/11 13:26 
 */
public class GroupRepository extends BaseRepository implements GroupContract.GroupSource {

    public volatile static GroupRepository mRepository;

    public static GroupRepository getInstance() {
        if (mRepository == null) {
            synchronized (GroupRepository.class) {
                if (mRepository == null) {
                    mRepository = new GroupRepository();
                }
            }
        }
        return mRepository;
    }

    @Override
    public void getGroupInfo(LifecycleProvider provider, GroupInfoRequestBody body, IBaseCallBack<GroupInfoData> callBack) {
        observerNoMap(provider, DataService.getApiService().getGroupInfo(body), callBack);
    }

    @Override
    public void getContactList(LifecycleProvider provider, IBaseCallBack<ContactListData> callBack) {
        observerNoMap(provider, DataService.getApiService().getContactList(), callBack);
    }

    @Override
    public void addMembers(LifecycleProvider provider, InviteOrDeleteRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().inviteMembers(body), callBack);
    }

    @Override
    public void deleteMembers(LifecycleProvider provider, InviteOrDeleteRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().deleteMembers(body), callBack);
    }

    @Override
    public void changGroupOwner(LifecycleProvider provider, InviteOrDeleteRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().changeGroupOwner(body), callBack);
    }

    @Override
    public void changeGroupManger(LifecycleProvider provider, InviteOrDeleteRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().setGroupManger(body), callBack);
    }

    @Override
    public void dissolveGroup(LifecycleProvider provider, GroupInfoRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().quitGroup(body), callBack);
    }

    @Override
    public void getGroupMember(LifecycleProvider provider, GroupInfoRequestBody body, IBaseCallBack<GroupInfoData> callBack) {
        observerNoMap(provider, DataService.getApiService().getGroupMember(body), callBack);
    }

    @Override
    public void forbiddenMember(LifecycleProvider provider, ForbiddenRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().forbiddenMember(body), callBack);
    }

    @Override
    public void cancelForbiddenMember(LifecycleProvider provider, ForbiddenRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().cancelForbiddenMember(body), callBack);
    }

    @Override
    public void updateGroupInfo(LifecycleProvider provider, UpdateGroupRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().updateGroupInfo(body), callBack);
    }

    @Override
    public void updateMineInfo(LifecycleProvider provider, UpdateMineInfoRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().updateMineInfo(body), callBack);
    }

    @Override
    public void getAllGroupInvites(LifecycleProvider provider, GroupRequestBody body, IBaseCallBack<CheckInvitesData> callBack) {
        observerNoMap(provider, DataService.getApiService().getAllInvites(body), callBack);
    }

    @Override
    public void checkInvites(LifecycleProvider provider, GroupInfoRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().checkInvites(body), callBack);
    }

    @Override
    public void uploadPic(LifecycleProvider provider, MultipartBody.Part file, IBaseCallBack<LoginData> callBack) {
        observerNoMap(provider, DataService.getApiService().uploadPicture(file), callBack);
    }

    @Override
    public void setFriendInfo(LifecycleProvider provider, SetFriendRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().setFriendsInfo(body), callBack);
    }

    @Override
    public void signGroup(LifecycleProvider provider, GroupInfoRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().signOutGroup(body), callBack);
    }
}
