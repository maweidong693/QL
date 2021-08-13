package com.weiwu.ql.data.network;

import com.tencent.common.http.ContactListData;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfoData;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupMemberData;
import com.weiwu.ql.data.bean.FriendInfoData;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.bean.MineInfoData;
import com.weiwu.ql.data.bean.NewFriendListData;
import com.weiwu.ql.data.request.AddFriendRequestBody;
import com.weiwu.ql.data.request.CreateGroupRequestBody;
import com.weiwu.ql.data.request.GroupOwnerRequestBody;
import com.weiwu.ql.data.request.InviteOrDeleteRequestBody;
import com.weiwu.ql.data.request.LoginRequestBody;
import com.weiwu.ql.data.request.NewFriendRequestBody;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    @POST("applet/login/in")
    Observable<LoginData> login(@Body LoginRequestBody body);

    @POST("applet/member/regist")
    Observable<LoginData> register(@Body LoginRequestBody body);

    @GET("applet/member/frd/getMailList")
    Observable<ContactListData> getContactList();

    @POST("applet/member/frd/request/add")
    Observable<HttpResult> addFriend(@Body AddFriendRequestBody body);

    @POST("applet/member/frd/request/getAllRequest")
    Observable<NewFriendListData> getNewFriendList(@Body NewFriendRequestBody body);

    @POST("applet/member/frd/request/handler")
    Observable<HttpResult> handlerNewFriendRequest(@Body NewFriendRequestBody body);

    @GET("applet/member/info")
    Observable<MineInfoData> getMineInfo();

    @Multipart
    @POST("applet/server/message/file/upload")
    Observable<LoginData> uploadPicture(@Part MultipartBody.Part file);

    @GET("applet/member/frd/remove")
    Observable<HttpResult> deleteFriend(@Query("memberId") String memberId);

    @POST("applet/group/establish")
    Observable<HttpResult> createGroup(@Body CreateGroupRequestBody body);

    @GET("applet/group/info")
    Observable<GroupInfoData> getGroupInfo(@Query("groupId") String groupId);

    @POST("applet/member/group/invite")
    Observable<HttpResult> inviteMembers(@Body InviteOrDeleteRequestBody body);

    @POST("applet/member/group/kickout")
    Observable<HttpResult> deleteMembers(@Body InviteOrDeleteRequestBody body);

    @GET("applet/member/findByMobile")
    Observable<FriendInfoData> findFriendId(@Query("mobile") String mobile);

    @POST("applet/member/group/leader-change")
    Observable<HttpResult> changeGroupOwner(@Body GroupOwnerRequestBody body);

    @POST("applet/member/group/admin-change")
    Observable<HttpResult> setGroupManger(@Body InviteOrDeleteRequestBody body);

    @GET("applet/group/dissolution")
    Observable<HttpResult> quitGroup(@Query("groupId") String groupId);

    @GET("applet/member/group/signOut")
    Observable<HttpResult> signOutGroup(@Query("groupId") String groupId);

    @GET("applet/member/group/getByGroupId")
    Observable<GroupMemberData> getGroupMember(@Query("groupId") String groupId);

    @POST("applet/member/group/forbidden")
    Observable<HttpResult> forbiddenMember(@Body InviteOrDeleteRequestBody body);

    @POST("applet/member/group/unforbidden")
    Observable<HttpResult> cancelForbiddenMember(@Body InviteOrDeleteRequestBody body);
}
