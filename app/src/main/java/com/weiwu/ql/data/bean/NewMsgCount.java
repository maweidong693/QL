package com.weiwu.ql.data.bean;

/**
 * 朋友圈未读消息
 */
public class NewMsgCount {

    public NewMsgCount(String faceUrl, int count) {
        this.faceUrl = faceUrl;
        this.count = count;
    }

    String faceUrl ;
    int count ;

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
