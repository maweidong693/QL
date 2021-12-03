package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/11/21 16:25 
 */
public class GroupInfoRequestBody {
    private int id;
    private String state;
    private String group_id;

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public GroupInfoRequestBody(String group_id) {
        this.group_id = group_id;
    }

    public GroupInfoRequestBody(int id, String state) {
        this.id = id;
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GroupInfoRequestBody(int id) {
        this.id = id;
    }
}
