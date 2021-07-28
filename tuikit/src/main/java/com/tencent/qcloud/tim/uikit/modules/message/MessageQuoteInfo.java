package com.tencent.qcloud.tim.uikit.modules.message;

import java.util.UUID;

/**
 * @author Joseph_Yan
 * on 2021/1/25
 * 引用消息格式
 */
public class MessageQuoteInfo {

    private String id = "";
    private String fromUserId ="";
    private String fromNameCard="";
    private int msgType;  //文本 =1  图片 =3  视频 =5
    private String message = "";     // 文本内容
    private String quoteMessage="";  // 引用文本内容()

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromNameCard() {
        return fromNameCard;
    }

    public void setFromNameCard(String fromNameCard) {
        this.fromNameCard = fromNameCard;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getQuoteMessage() {
        return quoteMessage;
    }

    public void setQuoteMessage(String quoteMessage) {
        this.quoteMessage = quoteMessage;
    }
}
