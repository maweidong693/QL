package com.weiwu.ql.data.network;

import com.tencent.common.http.ContactListData;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.bean.MineInfoData;
import com.weiwu.ql.data.bean.NewFriendListData;
import com.weiwu.ql.data.request.AddFriendRequestBody;
import com.weiwu.ql.data.request.LoginRequestBody;
import com.weiwu.ql.data.request.NewFriendRequestBody;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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
}
