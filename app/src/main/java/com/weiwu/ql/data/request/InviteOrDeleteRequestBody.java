package com.weiwu.ql.data.request;

import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/11 16:56 
 */
public class InviteOrDeleteRequestBody {
    private String group_id;
    private String member_id;
    private String group_role;

    public InviteOrDeleteRequestBody(String group_id, String member_id, String group_role) {
        this.group_id = group_id;
        this.member_id = member_id;
        this.group_role = group_role;
    }

    public String getGroup_role() {
        return group_role;
    }

    public void setGroup_role(String group_role) {
        this.group_role = group_role;
    }

    public InviteOrDeleteRequestBody(String groupId, String memberIds) {
        this.group_id = groupId;
        this.member_id = memberIds;
    }

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
}
