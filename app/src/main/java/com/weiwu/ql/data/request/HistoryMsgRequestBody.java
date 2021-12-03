package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/11/22 13:09 
 */
public class HistoryMsgRequestBody {
    private String toid;
    private String group_id;
    private String start;
    private String end = "20";

    public HistoryMsgRequestBody(String toid, String start) {
        this.toid = toid;
        this.start = start;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public HistoryMsgRequestBody(String group_id, String start, String end) {
        this.group_id = group_id;
        this.start = start;
        this.end = end;
    }

    public HistoryMsgRequestBody(String toid) {
        this.toid = toid;
    }

    public String getToid() {
        return toid;
    }

    public void setToid(String toid) {
        this.toid = toid;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
