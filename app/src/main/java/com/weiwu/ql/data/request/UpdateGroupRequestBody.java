package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/14 17:18 
 */
public class UpdateGroupRequestBody {
    private String id;
    private String group_name;
    private String introduction;
    private String notification;
    private String avatar_url;
    private String is_join_check;
    private String is_add_friend;
    private String is_ban_say;
    private String is_top;

    public UpdateGroupRequestBody(String id, String is_ban_say) {
        this.id = id;
        this.is_ban_say = is_ban_say;
    }

    public UpdateGroupRequestBody(String id, String group_name, String introduction, String notification, String avatar_url, String is_join_check, String is_add_friend, String is_ban_say, String is_top) {
        this.id = id;
        this.group_name = group_name;
        this.introduction = introduction;
        this.notification = notification;
        this.avatar_url = avatar_url;
        this.is_join_check = is_join_check;
        this.is_add_friend = is_add_friend;
        this.is_ban_say = is_ban_say;
        this.is_top = is_top;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getIs_add_friend() {
        return is_add_friend;
    }

    public void setIs_add_friend(String is_add_friend) {
        this.is_add_friend = is_add_friend;
    }

    public String getIs_top() {
        return is_top;
    }

    public void setIs_top(String is_top) {
        this.is_top = is_top;
    }

    public UpdateGroupRequestBody() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getIs_join_check() {
        return is_join_check;
    }

    public void setIs_join_check(String is_join_check) {
        this.is_join_check = is_join_check;
    }

    public String getIs_ban_say() {
        return is_ban_say;
    }

    public void setIs_ban_say(String is_ban_say) {
        this.is_ban_say = is_ban_say;
    }
}
