package com.weiwu.ql.data.network;

import com.tencent.common.http.BlackListData;
import com.tencent.common.http.ContactListData;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfoData;
import com.weiwu.ql.data.bean.CheckInvitesData;
import com.weiwu.ql.data.bean.CoinData;
import com.weiwu.ql.data.bean.CoinListData;
import com.weiwu.ql.data.bean.FindData;
import com.weiwu.ql.data.bean.FriendInfoData;
import com.weiwu.ql.data.bean.FriendPicsData;
import com.weiwu.ql.data.bean.FriendsData;
import com.weiwu.ql.data.bean.GroupListData;
import com.weiwu.ql.data.bean.HandlerData;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.bean.LoginReceive;
import com.weiwu.ql.data.bean.MineInfoData;
import com.weiwu.ql.data.bean.NewFriendListData;
import com.weiwu.ql.data.bean.NewMsgData;
import com.weiwu.ql.data.bean.OrderData;
import com.weiwu.ql.data.bean.OrderDetailData;
import com.weiwu.ql.data.bean.RangeBean;
import com.weiwu.ql.data.bean.SocketDataBean;
import com.weiwu.ql.data.bean.WalletData;
import com.weiwu.ql.data.request.AddCommentRequestBody;
import com.weiwu.ql.data.request.AddFriendRequestBody;
import com.weiwu.ql.data.request.ClientIdBody;
import com.weiwu.ql.data.request.CollectionRequestBody;
import com.weiwu.ql.data.request.CreateGroupRequestBody;
import com.weiwu.ql.data.request.DelChatBody;
import com.weiwu.ql.data.request.DelMsgRequestBody;
import com.weiwu.ql.data.request.DistributeRequestBody;
import com.weiwu.ql.data.request.ForbiddenRequestBody;
import com.weiwu.ql.data.request.FriendInfoRequestBody;
import com.weiwu.ql.data.request.FriendsRequestBody;
import com.weiwu.ql.data.request.GroupInfoRequestBody;
import com.weiwu.ql.data.request.GroupRequestBody;
import com.weiwu.ql.data.request.HandlerOrderRequestBody;
import com.weiwu.ql.data.request.HistoryMsgRequestBody;
import com.weiwu.ql.data.request.InviteOrDeleteRequestBody;
import com.weiwu.ql.data.request.IssueMessageRequestBody;
import com.weiwu.ql.data.request.LoginRequestBody;
import com.weiwu.ql.data.request.MessageRequestBody;
import com.weiwu.ql.data.request.NewFriendRequestBody;
import com.weiwu.ql.data.request.OrderRequestBody;
import com.weiwu.ql.data.request.RegisterRequestBody;
import com.weiwu.ql.data.request.RemindRequestBody;
import com.weiwu.ql.data.request.RemoveCommentRequestBody;
import com.weiwu.ql.data.request.SendGroupMsgRequestBody;
import com.weiwu.ql.data.request.SendMsgRequestBody;
import com.weiwu.ql.data.request.SendPhoneMsgRequestBody;
import com.weiwu.ql.data.request.SetFriendRequestBody;
import com.weiwu.ql.data.request.SetModifyBgRequestBody;
import com.weiwu.ql.data.request.SetRangeRequestBody;
import com.weiwu.ql.data.request.UpdateBgBody;
import com.weiwu.ql.data.request.UpdateGroupRequestBody;
import com.weiwu.ql.data.request.UpdateMineInfoRequestBody;
import com.weiwu.ql.data.request.VerifyCodeRequestBody;
import com.weiwu.ql.main.contact.chat.modle.HistoryMessageData;
import com.weiwu.ql.main.contact.chat.modle.MessageListData;
import com.weiwu.ql.main.contact.chat.modle.SendMessageData;
import com.weiwu.ql.main.login.country.CountryData;
import com.weiwu.ql.main.mine.collection.FavoritesData;
import com.weiwu.ql.main.mine.friends.data.MsgData;

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

    @POST("Login/resetPassword")
    Observable<HttpResult> forgetPassword(@Body RegisterRequestBody body);

    @POST("im/savePassword")
    Observable<HttpResult> updatePassword(@Body RegisterRequestBody body);

    @POST("Pub/getSocketUrl")
    Observable<SocketDataBean> getSocketUrl();

    @POST("applet/member/regist")
    Observable<LoginData> register(@Body LoginRequestBody body);

    @POST("Im/friendList")
    Observable<ContactListData> getContactList();

    @POST("Im/searchFriend")
    Observable<ContactListData.DataDTO.FriendsDTO> searchFriend(@Body AddFriendRequestBody body);

    @POST("Im/addFriend")
    Observable<HttpResult> addFriend(@Body AddFriendRequestBody body);

    @POST("Im/newFriendList")
    Observable<NewFriendListData> getNewFriendList();

    @POST("Im/setNewFriend")
    Observable<HttpResult> handlerNewFriendRequest(@Body NewFriendRequestBody body);

    @POST("Im/myInfo")
    Observable<MineInfoData> getMineInfo();

    @Multipart
    @POST("Pub/upload")
    Observable<LoginData> uploadPicture(@Part MultipartBody.Part file);

    @POST("Im/delFriend")
    Observable<HttpResult> deleteFriend(@Body AddFriendRequestBody body);

    @POST("Im/createGroup")
    Observable<HttpResult> createGroup(@Body CreateGroupRequestBody body);

    @POST("Im/groupInfo")
    Observable<GroupInfoData> getGroupInfo(@Body GroupInfoRequestBody body);

    @POST("Im/addGroupUser")
    Observable<HttpResult> inviteMembers(@Body InviteOrDeleteRequestBody body);

    @POST("Im/delGroupUser")
    Observable<HttpResult> deleteMembers(@Body InviteOrDeleteRequestBody body);

    @GET("applet/member/findByMobile")
    Observable<FriendInfoData> findFriendId(@Query("mobile") String mobile);

    @POST("Im/setGroupOwner")
    Observable<HttpResult> changeGroupOwner(@Body InviteOrDeleteRequestBody body);

    @POST("Im/setGroupAdmin")
    Observable<HttpResult> setGroupManger(@Body InviteOrDeleteRequestBody body);

    @POST("Im/unGroup")
    Observable<HttpResult> quitGroup(@Body GroupInfoRequestBody body);

    @POST("Im/unGroup")
    Observable<HttpResult> signOutGroup(@Body GroupInfoRequestBody body);

    @POST("Im/groupUserList")
    Observable<GroupInfoData> getGroupMember(@Body GroupInfoRequestBody body);

    @POST("Im/setIsBan")
    Observable<HttpResult> forbiddenMember(@Body ForbiddenRequestBody body);

    @POST("Im/setIsBan")
    Observable<HttpResult> cancelForbiddenMember(@Body ForbiddenRequestBody body);

    @POST("Im/saveGroupInfo")
    Observable<HttpResult> updateGroupInfo(@Body UpdateGroupRequestBody body);

    @POST("im/saveMyInfo")
    Observable<HttpResult> updateMineInfo(@Body UpdateMineInfoRequestBody body);

    @GET("applet/login/loginByCode")
    Observable<HttpResult> webLogin(@Query("token") String token, @Query("code") String code);

    @POST("Msgcircle/addMessage")
    Observable<HttpResult> issueMessage(@Body IssueMessageRequestBody body);

    @POST("Msgcircle/modifyBackgroundImg")
    Observable<HttpResult> updateBg(@Body UpdateBgBody body);

    @POST("Msgcircle/myMsgCircle")
    Observable<FriendsData> getAllFriends(@Body FriendsRequestBody body);

    @POST("Msgcircle/removeMessage")
    Observable<HttpResult> deleteFriends(@Body FriendsRequestBody body);

    @POST("Scmessage/comment")
    Observable<HttpResult> addComment(@Body AddCommentRequestBody body);

    @POST("Scmessage/removeReply")
    Observable<HttpResult> removeComment(@Body RemoveCommentRequestBody body);

    @POST("Scmessage/likedOrNot")
    Observable<HttpResult> like(@Body RemoveCommentRequestBody body);

    @POST("Im/groupCheckList")
    Observable<CheckInvitesData> getAllInvites(@Body GroupRequestBody body);

    @POST("Im/setNewGroupUser")
    Observable<HttpResult> checkInvites(@Body GroupInfoRequestBody body);

    @POST("Pub/getInvitation")
    Observable<LoginData> getInviteKey();

    @POST("news/news")
    Observable<FindData> getFindList();

    @POST("Im/sendPrivateMsg")
    Observable<SendMessageData> sendMessage(@Body SendMsgRequestBody body);

    @POST("Im/getChatList")
    Observable<MessageListData> getMessageList();

    @POST("Im/getPrivateList")
    Observable<HistoryMessageData> getHistoryMsg(@Body HistoryMsgRequestBody body);

    @POST("Im/getGroupMsgList")
    Observable<HistoryMessageData> getGroupHistoryMsg(@Body HistoryMsgRequestBody body);

    @POST("login/send")
    Observable<HttpResult> sendMsg(@Body SendPhoneMsgRequestBody body);

    @POST("login/verify")
    Observable<HttpResult> verify(@Body VerifyCodeRequestBody body);

    @POST("Login/register")
    Observable<HttpResult> register(@Body RegisterRequestBody body);

    @POST("Im/sendGroupMsg")
    Observable<SendMessageData> sendGroupMsg(@Body SendGroupMsgRequestBody body);

    @POST("Im/groupList")
    Observable<GroupListData> getGroupList();

    @POST("Im/chatInit")
    Observable<HttpResult> init(@Body ClientIdBody body);

    @POST("Im/setChatStatus")
    Observable<HttpResult> setChatStatus(@Body MessageRequestBody body);

    @POST("Im/setFriendAuth")
    Observable<HttpResult> setFriendsInfo(@Body SetFriendRequestBody body);

    @POST("Im/banList")
    Observable<BlackListData> getBlackList();

    @POST("Im/seeFriendInfo")
    Observable<FriendInfoData> getFriendInfo(@Body FriendInfoRequestBody body);

    @POST("Im/delMsg")
    Observable<HttpResult> delMsg(@Body DelMsgRequestBody body);

    @POST("Pub/collection")
    Observable<HttpResult> collection(@Body CollectionRequestBody body);

    @POST("Pub/collectionList")
    Observable<FavoritesData> getCollectionList();

    @POST("Pub/delCollection")
    Observable<HttpResult> delCollection(@Body GroupInfoRequestBody body);

    @POST("Msgcircle/range")
    Observable<RangeBean> getRange();

    @POST("Msgcircle/set_show_range")
    Observable<HttpResult> setRange(@Body SetRangeRequestBody body);

    @POST("Msgcircle/modifyBackgroundImg")
    Observable<HttpResult> setModifyBg(@Body SetModifyBgRequestBody body);

    @POST("Scmessage/remind")
    Observable<NewMsgData> getNewMsgCount();

    @POST("Scmessage/readed")
    Observable<HttpResult> readAll(@Body RemindRequestBody body);

    @POST("Scmessage/notRead")
    Observable<MsgData> getMsgList();

    @POST("Msgcircle/circleInfo")
    Observable<FriendPicsData> getFriendsPics(@Body SetFriendRequestBody body);

    @POST("Login/getCountryCode")
    Observable<CountryData> getCountryList();

    @POST("Im/delChat")
    Observable<HttpResult> delChat(@Body DelChatBody body);

}
