package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/20 15:36 
 */
public class CheckInvitesRequestBody {
    private String inviteId;
    private String flag;

    public String getInviteId() {
        return inviteId;
    }

    public void setInviteId(String inviteId) {
        this.inviteId = inviteId;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public CheckInvitesRequestBody(String inviteId, String flag) {
        this.inviteId = inviteId;
        this.flag = flag;
    }
}
