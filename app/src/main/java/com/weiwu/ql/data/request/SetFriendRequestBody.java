package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/11/23 13:33 
 */
public class SetFriendRequestBody {
    private String friend_id;
    private String not_show;
    private String not_see;
    private String is_black;
    private String alias;
    private String im_id;

    public String getIm_id() {
        return im_id;
    }

    public void setIm_id(String im_id) {
        this.im_id = im_id;
    }

    public SetFriendRequestBody(String im_id) {
        this.im_id = im_id;
    }

    public SetFriendRequestBody(String friend_id, String not_show, String not_see, String is_black, String alias) {
        this.friend_id = friend_id;
        this.not_show = not_show;
        this.not_see = not_see;
        this.is_black = is_black;
        this.alias = alias;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    public String getNot_show() {
        return not_show;
    }

    public void setNot_show(String not_show) {
        this.not_show = not_show;
    }

    public String getNot_see() {
        return not_see;
    }

    public void setNot_see(String not_see) {
        this.not_see = not_see;
    }

    public String getIs_black() {
        return is_black;
    }

    public void setIs_black(String is_black) {
        this.is_black = is_black;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
