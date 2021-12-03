package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/24 13:48 
 */
public class NewFriendRequestBody {
    private String status;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NewFriendRequestBody(String status, String requestId) {
        this.status = status;
        this.id = requestId;
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
