package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/11/23 17:32 
 */
public class FriendInfoRequestBody {
    private String member_id;

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public FriendInfoRequestBody(String member_id) {
        this.member_id = member_id;
    }
}
