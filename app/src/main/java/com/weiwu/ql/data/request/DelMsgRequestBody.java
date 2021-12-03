package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/11/23 18:15 
 */
public class DelMsgRequestBody {
    private String msg_id;
    private String type;

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DelMsgRequestBody(String msg_id, String type) {
        this.msg_id = msg_id;
        this.type = type;
    }
}
