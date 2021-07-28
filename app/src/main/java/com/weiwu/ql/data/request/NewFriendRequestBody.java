package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/24 13:48 
 */
public class NewFriendRequestBody {
    private String status;
    private String requestId;
    private String handlerRemark;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getHandlerRemark() {
        return handlerRemark;
    }

    public void setHandlerRemark(String handlerRemark) {
        this.handlerRemark = handlerRemark;
    }

    public NewFriendRequestBody(String status, String requestId, String handlerRemark) {
        this.status = status;
        this.requestId = requestId;
        this.handlerRemark = handlerRemark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public NewFriendRequestBody(String status) {
        this.status = status;
    }
}
