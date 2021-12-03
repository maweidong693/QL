package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/11/25 09:44 
 */
public class RemindRequestBody {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RemindRequestBody(String id) {
        this.id = id;
    }
}
