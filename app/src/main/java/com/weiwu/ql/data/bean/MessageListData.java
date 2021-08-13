package com.weiwu.ql.data.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/4 09:21 
 */
@Entity
public class MessageListData {
    @Id
    private String mId;
    private String nickName;
    private String lastContent;
    private String avator;
    private String time;
    private String receiverType;

    @Generated(hash = 1621799257)
    public MessageListData(String mId, String nickName, String lastContent,
            String avator, String time, String receiverType) {
        this.mId = mId;
        this.nickName = nickName;
        this.lastContent = lastContent;
        this.avator = avator;
        this.time = time;
        this.receiverType = receiverType;
    }
    @Generated(hash = 1650272673)
    public MessageListData() {
    }
    public String getMId() {
        return this.mId;
    }
    public void setMId(String mId) {
        this.mId = mId;
    }
    public String getNickName() {
        return this.nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getLastContent() {
        return this.lastContent;
    }
    public void setLastContent(String lastContent) {
        this.lastContent = lastContent;
    }
    public String getAvator() {
        return this.avator;
    }
    public void setAvator(String avator) {
        this.avator = avator;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getReceiverType() {
        return this.receiverType;
    }
    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }
}
