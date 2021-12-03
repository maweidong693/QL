package com.weiwu.ql.data.request;

import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/6 13:21 
 */
public class CreateGroupRequestBody {
    private String member_ids;

    public String getMember_ids() {
        return member_ids;
    }

    public void setMember_ids(String member_ids) {
        this.member_ids = member_ids;
    }

    public CreateGroupRequestBody(String memberIds) {
        this.member_ids = memberIds;
    }
}
