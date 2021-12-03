package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/11/22 16:04 
 */
public class SendGroupMsgRequestBody {
    private String group_id;
    private String content;
    private String cate;
    private String cite;

    public String getCite() {
        return cite;
    }

    public void setCite(String cite) {
        this.cite = cite;
    }

    public SendGroupMsgRequestBody(String group_id, String content, String cate, String cite) {
        this.group_id = group_id;
        this.content = content;
        this.cate = cate;
        this.cite = cite;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }

    public SendGroupMsgRequestBody(String group_id, String content, String cate) {
        this.group_id = group_id;
        this.content = content;
        this.cate = cate;
    }
}
