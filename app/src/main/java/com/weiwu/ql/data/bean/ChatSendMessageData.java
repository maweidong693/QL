package com.weiwu.ql.data.bean;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/22 14:24 
 */
public class ChatSendMessageData {
    private String type;
    private String receiverType;
    private String msgType;
    private Long receiverId;
    private String textMsg;
    private String imgUrl;

    @Override
    public String toString() {
        return "ChatMessageData{" +
                "type='" + type + '\'' +
                ", receiverType='" + receiverType + '\'' +
                ", msgType='" + msgType + '\'' +
                ", receiverId=" + receiverId +
                ", textMsg='" + textMsg + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getTextMsg() {
        return textMsg;
    }

    public void setTextMsg(String textMsg) {
        this.textMsg = textMsg;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public ChatSendMessageData(String type, String receiverType, String msgType, Long receiverId, String textMsg, String imgUrl) {
        this.type = type;
        this.receiverType = receiverType;
        this.msgType = msgType;
        this.receiverId = receiverId;
        this.textMsg = textMsg;
        this.imgUrl = imgUrl;
    }
}
