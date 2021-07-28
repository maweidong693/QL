package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/23 13:43 
 */
public class AddFriendRequestBody {
    private String memberId;
    private String remarks;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public AddFriendRequestBody(String memberId, String remarks) {
        this.memberId = memberId;
        this.remarks = remarks;
    }
}
