package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/11 16:56 
 */
public class ForbiddenRequestBody {
    private String group_id;
    private String member_id;
    private String is_say;

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getIs_say() {
        return is_say;
    }

    public void setIs_say(String is_say) {
        this.is_say = is_say;
    }

    public ForbiddenRequestBody(String group_id, String member_id, String is_say) {
        this.group_id = group_id;
        this.member_id = member_id;
        this.is_say = is_say;
    }
}
