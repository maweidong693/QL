package com.tencent.common;

/**
 * Created by jikun on 17/5/23.
 */

public class Constant {
    public static final int RADIUS = 10;
    public static final String sp_Person_Name = "sp_person_name";
    public static final String sp_Face_Url = "sp_face_url";
    public static final String sp_User_Account = "sp_user_account";
    public static final String sp_User_Id = "sp_user_id";
    public static final String sp_domain = "sp_domain";
    public static final String sp_Token = "sp_token"; //token
    public static final String sp_code = "sp_code";
    public static final String sp_mobile = "sp_mobile";  //账号(邮箱)
    public static final String sp_wxAppId = "sp_wxAppId";
    // 存储
    public static final String USERINFO = "userInfo";
    public static final String ACCOUNT = "account";
    public static final String PWD = "password";
    public static final String ROOM = "room";
    public static final String AUTO_LOGIN = "auto_login";
    public static final String LOGOUT = "logout";
    public static final String ICON_URL = "icon_url";

    public static final String CHAT_INFO = "chatInfo";
    public static final String sp_sound = "sp_sound";
    public static final String sp_vibration = "sp_vibration";
    public static final String sp_chat_background = "chat_background_";
    public static final String sp_request_chat_background = "request_chat_background";
    public static final String SP_FontScale = "字体大小调整";
    public static final String CONTENT = "content";

    private static final String WS = "ws://";
    public static final String PUBLIC_URL = "https://raw.githubusercontent.com";
    public static final String PUBLIC_OWN_URL = "https://raw.githubusercontent.com/yanpengqing/download/master/own_update.json";
    public static final String PUBLIC_FIXED_URL = "https://raw.githubusercontent.com/yanpengqing/download/master/fixed_update.json";
    public static final String DOMAIN = "https://api.wmqt.net";
    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";
    //    private static String IP = "https://my.xiehehai.top";
    private static String IP = "http://47.108.192.155:8802/";
    public static String wxAppId = "";
    public static String wxAppSecret = "";
    private static final String LOCAL_IP = "192.168.8.125";
    private static final String PORT = "443";
    private static final String WS_PORT = "8282";
    public static final String LOGIN_PASSENGER_ID = "login_passenger_id";
    public static final String key = "KJ4-4F5-vs-4sd2_jk-km#545@lion";
    public static final int pageSise = 10;
    public static final String USER = "user";
    public static final String USER_TEL = "user_tel";

    public static String getWxAppId() {
        return wxAppId;
    }

    public static void setWxAppId(String wxAppId) {
        Constant.wxAppId = wxAppId;
    }

    public static String getWxAppSecret() {
        return wxAppSecret;
    }

    public static void setWxAppSecret(String wxAppSecret) {
        Constant.wxAppSecret = wxAppSecret;
    }

    public static String getIP() {
        return IP;
    }

    public static void setIP(String IP) {
        Constant.IP = IP;
    }

    public static final String PHONE_NUM = "phone_number";

    public static final String PASSWORD_TYPE = "password_type";

    public static final String PASS_WORD = "pass_word";

    public static final String IS_LOGIN_SUCCESS = "is_login_success";


    public static String getBaseUrl() {

        return IP + "/";
//        return IP + ":" + PORT + "/";
    }

    public static String getWsBaseUrl() {
        return WS + IP + ":" + WS_PORT + "/";
    }

    public static final String GET_MSG_CODE = "api/Tencentcloud/send";
    public static final String URL_LOGIN = "api/Pub/login";
    public static final String URL_REGISTER = "api/Pub/register";
    public static final String URL_CONUNTRY_CODE = "api/Pub/getCountryCode";
    public static final String URL_IS_ADD_FRIEND = "api/tencentim/getGroupIsAddFriend";
    public static final String URL_UPLOAD = "api/Pub/upload";
    public static final String URL_VIDEO_ADD = "api/Pub/video_add";
    public static final String URL_NEWS = "api/news/news";
    public static final String URL_PORTRAIT_GET = "api/tencentim/portrait_get";
    public static final String URL_GET_IMID = "api/Pub/geuImId";
    //朋友圈
    public static final String URL_ADD_MESSAGE = "api/Msgcircle/addMessage";
    public static final String URL_MY_MSGCIRCLE = "api/Msgcircle/myMsgCircle";
    public static final String URL_MODIFYBACKGROUNDIMG = "api/Msgcircle/modifyBackgroundImg";
    public static final String URL_REMOVEMESSAGE = "api/Msgcircle/removeMessage";
    public static final String URL_LIKEDORNOT = "api/Scmessage/likedOrNot";
    public static final String URL_COMMENT = "api/Scmessage/comment";
    public static final String URL_REMOVEREPLY = "api/Scmessage/removeReply";
    public static final String URL_NOT_SEE = "api/Msgcircle/not_see";
    public static final String URL_NOT_SHOW = "api/Msgcircle/not_show";
    public static final String URL_SECURITY = "api/Msgcircle/security";
    public static final String URL_REMIND = "api/Scmessage/remind";
    public static final String URL_READ_NOTICE = "api/Scmessage/readnotice";
    public static final String URL_NOTREAD = "api/Scmessage/notRead";
    public static final String URL_READED = "api/Scmessage/readed";
    public static final String URL_DETAIL = "api/Scmessage/detail";
    public static final String URL_CIRCLE_INFO = "api/Msgcircle/circleInfo";
    public static final String URL_FRIEND_CIRCLE = "api/Msgcircle/friendMsgCircle";
    public static final String URL_RANGE = "api/Msgcircle/range";
    public static final String URL_SHOW_RANGE = "api/Msgcircle/set_show_range";
    public static final String URL_SET_CHAT_IMAGE = "api/Scmessage/setChatImage";
    public static final String URL_CHAT_IMAGE = "api/Scmessage/chatImage";
    public static final String URL_GROUP_MSG_RECALL = "api/tencentim/group_msg_recall";
    public static final String URL_GET_GROUP_USER_SORT = "api/tencentim/getGroupUserSort";
    public static final String URL_WEB_STATUS = "api/News/getStatus"; //App端查询当前web端是否登陆
    public static final String URL_WEB_LOGOUT = "api/News/web_logOut"; //退出web端的登录
    public static final String URL_WEB_CHECK_LOGIN = "api/News/check_login"; //App端扫码识别之后提交
    public static final String URL_COLLECTION = "api/tencentim/collection";
    public static final String URL_COLLECTION_LIST = "api/tencentim/collectionList";
    public static final String URL_DEL_COLLECTION = "api/tencentim/delCollection";
    public static final String URL_GET_INVITATION = "api/Tencentim/getInvitation";
    public static final String URL_INVITATION_USER = "api/Tencentim/invitationUser";
    public static final String URL_INVITATION_LIST = "api/Tencentim/invitationList";
    public static final String URL_AUDIT_USER = "api/Tencentim/auditUser";
    public static final String URL_SET_ALIAS = "api/Tencentim/setAlias";
    public static final String URL_AUDIT_COUNT = "api/Tencentim/auditCount";
    public static final String URL_CHECK_VERIFY = "api/Tencentcloud/verify";
    public static final String URL_UPDATE_PASSWORD = "api/Pub/resetPassword";
    public static final String URL_ADD_FRIEND = "api/Tencentim/addFriend";
    public static final String URL_ADD_FRIEND_STATUS = "api/Tencentim/setFriendStatus";
    public static final String URL_NEW_FRIEND_LIST = "api/Tencentim/newFriendList";
    public static final String URL_NEW_FRIEND_COUNT = "api/Tencentim/newFriendCount";
    public static final String FRIEND_LIST = "applet/member/frd/getMailList";

}
