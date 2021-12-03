package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/11/21 16:25 
 */
public class GroupRequestBody {
    private String group_id;

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public GroupRequestBody(String group_id) {
        this.group_id = group_id;
    }
}
