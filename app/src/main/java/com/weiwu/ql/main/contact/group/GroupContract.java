package com.weiwu.ql.main.contact.group;

import com.tencent.common.http.ContactListData;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupMemberData;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.base.IBasePresenter;
import com.weiwu.ql.base.IBaseView;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfoData;
import com.weiwu.ql.data.bean.CheckInvitesData;
import com.weiwu.ql.data.bean.LoginData;
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

import java.util.Calendar;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Query;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/11 13:23 
 */
public interface GroupContract {
    interface IGroupView extends IBaseView<IGroupPresenter> {
        void groupInfoReceive(GroupInfoData data);

        void uploadReceive(LoginData data);

        void allMuteReceive(HttpResult data);

        void onSuccess(HttpResult data);

        void onFail(String msg, int code);
    }

    interface IGroupAddView extends IBaseView<IGroupAddPresenter> {
        void contactReceive(ContactListData data);

        void inviteFriendsReceive(HttpResult data);

        void deleteFriendReceive(HttpResult data);

        void onFail(String msg, int code);

    }

    interface IGroupAddPresenter extends IBasePresenter<IGroupAddView> {
        void getContactList();

        void addMembers(InviteOrDeleteRequestBody body);

        void deleteMembers(InviteOrDeleteRequestBody body);

    }

    interface IGroupPresenter extends IBasePresenter<IGroupView> {
        void getGroupInfo(GroupInfoRequestBody body);

        void dissolveGroup(GroupInfoRequestBody body);

        void signGroup(GroupInfoRequestBody body);

        void uploadPic(MultipartBody.Part file);

        void allMute(ForbiddenRequestBody body);

        void cancelMute(ForbiddenRequestBody body);

        void updateGroupInfo(UpdateGroupRequestBody body);
    }

    interface IGroupMangerView extends IBaseView<IGroupMangerPresenter> {
        void onSuccess(HttpResult data);

        void onFail(String msg, int code);
    }

    interface IGroupMangerPresenter extends IBasePresenter<IGroupMangerView> {
        void changGroupOwner(InviteOrDeleteRequestBody body);

        void changeGroupManger(InviteOrDeleteRequestBody body);
    }

    interface IGroupForbiddenView extends IBaseView<IGroupForbiddenPresenter> {
        void groupMemberReceive(GroupInfoData data);

        void forbiddenReceive(HttpResult data);

        void onFail(String msg, int code);
    }

    interface IGroupForbiddenPresenter extends IBasePresenter<IGroupForbiddenView> {
        void getGroupMember(GroupInfoRequestBody body);

        void forbiddenMember(ForbiddenRequestBody body);

        void cancelForbiddenMember(ForbiddenRequestBody body);
    }

    interface IUpdateGroupInfoView extends IBaseView<IUpdateGroupInfoPresenter> {
        void onSuccess(HttpResult data);

        void onFail(String msg, int code);
    }

    interface IUpdateGroupInfoPresenter extends IBasePresenter<IUpdateGroupInfoView> {
        void updateGroupInfo(UpdateGroupRequestBody body);

        void updateFriendInfo(SetFriendRequestBody body);

        void updateMineInfo(UpdateMineInfoRequestBody body);
    }

    interface IGroupMemberView extends IBaseView<IGroupMemberPresenter> {
        void groupMemberReceive(GroupInfoData data);

        void onFail(String msg, int code);
    }

    interface IGroupMemberPresenter extends IBasePresenter<IGroupMemberView> {
        void getGroupMember(GroupInfoRequestBody body);
    }

    interface ICheckInvitesView extends IBaseView<ICheckInvitesPresenter> {
        void onInvitesReceive(CheckInvitesData data);

        void onSuccess(HttpResult data);

        void onFail(String msg, int code);
    }

    interface ICheckInvitesPresenter extends IBasePresenter<ICheckInvitesView> {
        void getAllInvites(GroupRequestBody body);

        void checkInvites(GroupInfoRequestBody body);
    }

    interface GroupSource {
        void getGroupInfo(LifecycleProvider provider, GroupInfoRequestBody body, IBaseCallBack<GroupInfoData> callBack);

        void getContactList(LifecycleProvider provider, IBaseCallBack<ContactListData> callBack);

        void addMembers(LifecycleProvider provider, InviteOrDeleteRequestBody body, IBaseCallBack<HttpResult> callBack);

        void deleteMembers(LifecycleProvider provider, InviteOrDeleteRequestBody body, IBaseCallBack<HttpResult> callBack);

        void changGroupOwner(LifecycleProvider provider, InviteOrDeleteRequestBody body, IBaseCallBack<HttpResult> callBack);

        void changeGroupManger(LifecycleProvider provider, InviteOrDeleteRequestBody body, IBaseCallBack<HttpResult> callBack);

        void dissolveGroup(LifecycleProvider provider, GroupInfoRequestBody body, IBaseCallBack<HttpResult> callBack);

        void signGroup(LifecycleProvider provider, GroupInfoRequestBody body, IBaseCallBack<HttpResult> callBack);

        void getGroupMember(LifecycleProvider provider, GroupInfoRequestBody body, IBaseCallBack<GroupInfoData> callBack);

        void forbiddenMember(LifecycleProvider provider, ForbiddenRequestBody body, IBaseCallBack<HttpResult> callBack);

        void cancelForbiddenMember(LifecycleProvider provider, ForbiddenRequestBody body, IBaseCallBack<HttpResult> callBack);

        void updateGroupInfo(LifecycleProvider provider, UpdateGroupRequestBody body, IBaseCallBack<HttpResult> callBack);

        void updateMineInfo(LifecycleProvider provider, UpdateMineInfoRequestBody body, IBaseCallBack<HttpResult> callBack);

        void getAllGroupInvites(LifecycleProvider provider, GroupRequestBody body, IBaseCallBack<CheckInvitesData> callBack);

        void checkInvites(LifecycleProvider provider, GroupInfoRequestBody body, IBaseCallBack<HttpResult> callBack);

        void uploadPic(LifecycleProvider provider, MultipartBody.Part file, IBaseCallBack<LoginData> callBack);

        void setFriendInfo(LifecycleProvider provider, SetFriendRequestBody body, IBaseCallBack<HttpResult> callBack);

    }
}
