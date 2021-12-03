package com.weiwu.ql.data.bean;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/11/22 21:09 
 */
public class SocketData {

    private String type;
    private String client_id;
    private int new_friend_count;

    public int getNew_friend_count() {
        return new_friend_count;
    }

    public void setNew_friend_count(int new_friend_count) {
        this.new_friend_count = new_friend_count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }
}
