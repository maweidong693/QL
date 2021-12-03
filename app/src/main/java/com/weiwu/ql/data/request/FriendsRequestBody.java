package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/11/24 15:02 
 */
public class FriendsRequestBody {
    private String page;
    private String size;
    private String message_id;

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public FriendsRequestBody(String message_id) {
        this.message_id = message_id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public FriendsRequestBody(String page, String size) {
        this.page = page;
        this.size = size;
    }
}
