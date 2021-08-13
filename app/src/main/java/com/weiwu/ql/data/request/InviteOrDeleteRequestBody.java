package com.weiwu.ql.data.request;

import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/11 16:56 
 */
public class InviteOrDeleteRequestBody {
    private String groupId;
    private List<String> memberIds;

    public InviteOrDeleteRequestBody(String groupId, List<String> memberIds) {
        this.groupId = groupId;
        this.memberIds = memberIds;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<String> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<String> memberIds) {
        this.memberIds = memberIds;
    }
}
