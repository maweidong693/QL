package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/11/24 15:08 
 */
public class RemoveCommentRequestBody {
    private String id;
    private String type;
    private String msg_id;

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public RemoveCommentRequestBody(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RemoveCommentRequestBody(String id, String type) {
        this.id = id;
        this.type = type;
    }
}
