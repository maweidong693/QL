package com.weiwu.ql.main.contact.chat.modle;

public class ChatMessage {
    private int isMeSend;//0是对方发送 1是自己发送
    private int isRead;//是否已读（0未读 1已读）

    private String type;    // message
    private String receiverType;    // member
    private String messageId;    // member
    private String msgType;     // msg文本消息，（img图片消息，doc文件，voi音频，video视频。暂无）
    private String receiverId;    // 接收会员ID
    private String textMsg;     // 文本消息
    private String url;     // 文本消息

    private String memberId;    //发送人ID
    private String memberNickname;      //发送人昵称

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public ChatMessage() {
    }

    public ChatMessage(String type, String receiverType, String msgType, String receiverId, String textMsg, String imgUrl) {
        this.type = type;
        this.receiverType = receiverType;
        this.msgType = msgType;
        this.receiverId = receiverId;
        this.textMsg = textMsg;
        this.url = imgUrl;
    }

    public ChatMessage(String msgType, String textMsg, String imgUrl, String memberId, String memberNickname) {
        this.msgType = msgType;
        this.textMsg = textMsg;
        this.url = imgUrl;
        this.memberId = memberId;
        this.memberNickname = memberNickname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getTextMsg() {
        return textMsg;
    }

    public void setTextMsg(String textMsg) {
        this.textMsg = textMsg;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberNickname() {
        return memberNickname;
    }

    public void setMemberNickname(String memberNickname) {
        this.memberNickname = memberNickname;
    }

    public int getIsMeSend() {
        return isMeSend;
    }

    public void setIsMeSend(int isMeSend) {
        this.isMeSend = isMeSend;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }
}
