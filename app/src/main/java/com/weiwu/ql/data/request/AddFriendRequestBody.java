package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/23 13:43 
 */
public class AddFriendRequestBody {
    private String account;
    private String memberId;
    private String remark;

    public AddFriendRequestBody(String account, String remark) {
        this.account = account;
        this.remark = remark;
    }

    public AddFriendRequestBody(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
