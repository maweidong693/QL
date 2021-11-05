package com.weiwu.ql.data.network;

import com.tencent.common.http.ContactListData;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfoData;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupMemberData;
import com.weiwu.ql.data.bean.CheckInvitesData;
import com.weiwu.ql.data.bean.CoinData;
import com.weiwu.ql.data.bean.CoinListData;
import com.weiwu.ql.data.bean.FindData;
import com.weiwu.ql.data.bean.FriendInfoData;
import com.weiwu.ql.data.bean.FriendsData;
import com.weiwu.ql.data.bean.HandlerData;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.bean.LoginReceive;
import com.weiwu.ql.data.bean.MineInfoData;
import com.weiwu.ql.data.bean.NewFriendListData;
import com.weiwu.ql.data.bean.OrderData;
import com.weiwu.ql.data.bean.OrderDetailData;
import com.weiwu.ql.data.bean.SocketDataBean;
import com.weiwu.ql.data.bean.WalletData;
import com.weiwu.ql.data.request.AddCommentRequestBody;
import com.weiwu.ql.data.request.AddFriendRequestBody;
import com.weiwu.ql.data.request.CheckInvitesRequestBody;
import com.weiwu.ql.data.request.CreateGroupRequestBody;
import com.weiwu.ql.data.request.DistributeRequestBody;
import com.weiwu.ql.data.request.GroupOwnerRequestBody;
import com.weiwu.ql.data.request.HandlerOrderRequestBody;
import com.weiwu.ql.data.request.InviteOrDeleteRequestBody;
import com.weiwu.ql.data.request.IssueMessageRequestBody;
import com.weiwu.ql.data.request.LoginRequestBody;
import com.weiwu.ql.data.request.NewFriendRequestBody;
import com.weiwu.ql.data.request.OrderRequestBody;
import com.weiwu.ql.data.request.UpdateGroupRequestBody;
import com.weiwu.ql.data.request.UpdateMineInfoRequestBody;
import com.weiwu.ql.data.request.WebLoginRequestBody;

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

    @POST("Login/login")
    Observable<LoginReceive> login(@Body LoginRequestBody body);

    @POST("Pub/getSocketUrl")
    Observable<SocketDataBean> getSocketUrl();

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

    @GET("applet/member/group/leader-change")
    Observable<HttpResult> changeGroupOwner(@Query("groupId") String groupId, @Query("memberId") String memberId);

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

    @POST("applet/group/edit")
    Observable<HttpResult> updateGroupInfo(@Body UpdateGroupRequestBody body);

    @POST("applet/member/update")
    Observable<HttpResult> updateMineInfo(@Body UpdateMineInfoRequestBody body);

    @GET("applet/login/loginByCode")
    Observable<HttpResult> webLogin(@Query("token") String token, @Query("code") String code);

    @POST("applet/moments/release")
    Observable<HttpResult> issueMessage(@Body IssueMessageRequestBody body);

    @GET("applet/moments/getAll")
    Observable<FriendsData> getAllFriends(@Query("pageSize") String pageSize, @Query("pageNum") String pageNum);

    @GET("applet/moments/remove")
    Observable<HttpResult> deleteFriends(@Query("momentId") String momentId);

    @POST("applet/moments-comment/commit")
    Observable<HttpResult> addComment(@Body AddCommentRequestBody body);

    @GET("applet/moments-comment/remove")
    Observable<HttpResult> removeComment(@Query("commentId") String commentId);

    @GET("applet/chat-group-invite/getGroupInvites")
    Observable<CheckInvitesData> getAllInvites(@Query("groupId") String groupId);

    @GET("applet/chat-group-invite/examine")
    Observable<HttpResult> checkInvites(@Query("inviteId") String inviteId, @Query("flag") String flag);

    @GET("applet/coin/getAllCoin")
    Observable<CoinData> getCoinList();

    @POST("applet/member-coin-address/addAddress")
    Observable<HttpResult> addCoin(@Body CoinListData body);

    @POST("applet/order/query")
    Observable<OrderData> getAllOrder(@Body OrderRequestBody body);

    @POST("applet/order/handler")
    Observable<HttpResult> handlerOrder(@Body HandlerOrderRequestBody body);

    @GET("applet/order/receive")
    Observable<HttpResult> receiveOrder(@Query("tradeId") String tradeId);

    @POST("applet/order/toHandler")
    Observable<HttpResult> distributeOrder(@Body DistributeRequestBody body);

    @GET("applet/member/getAllHandler")
    Observable<HandlerData> getAllHandler();

    @GET("applet/order/info")
    Observable<OrderDetailData> getOrderDetail(@Query("tradeId") String tradeId);

    @GET("applet/member-coin-address/getAll")
    Observable<WalletData> getAllWallet(@Query("coinType") String coinType);

    @GET("applet/order/confirm-release")
    Observable<HttpResult> confirmGetCoin(@Query("cTradeId") String cTradeId);

    @GET("applet/order/over-return")
    Observable<HttpResult> confirmRecycleCoin(@Query("cTradeId") String cTradeId);

    @GET("applet/member/getInviteKey")
    Observable<LoginData> getInviteKey();

    @GET("applet/discover/list")
    Observable<FindData> getFindList();
}
