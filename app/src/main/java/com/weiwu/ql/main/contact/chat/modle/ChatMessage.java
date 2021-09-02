package com.weiwu.ql.main.contact.chat.modle;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

@Entity
public class ChatMessage {

    @Id
    private String messageId;

    private String type;    // message
    private String receiverType;    // member
    private String msgType;     // msg文本消息，（img图片消息，doc文件，voi音频，video视频。暂无）
    private String receiverId;    // 接收会员ID
    private String textMsg;     // 文本消息
    private String url;
    private String quoteMessageInfo;
    private String sendTime;
    private int isTop;
    private int isMeSend;//0是对方发送 1是自己发送
    @Transient
    private int isTemporary;
    private String noticeInfo;

    private String memberId;    //发送人ID
    private String memberNickname;      //发送人昵称

    @Transient
    private functionArgData functionArg;
    @Transient
    private ChatMessage quoteMessage;
    @Transient
    private NoticeData notice;
    @Transient
    private String message;
    @Transient
    private int code;

    public int getIsTemporary() {
        return isTemporary;
    }

    public void setIsTemporary(int isTemporary) {
        this.isTemporary = isTemporary;
    }

    public functionArgData getFunctionArg() {
        return functionArg;
    }

    public void setFunctionArg(functionArgData functionArg) {
        this.functionArg = functionArg;
    }

    public ChatMessage getQuoteMessage() {
        return quoteMessage;
    }

    public void setQuoteMessage(ChatMessage quoteMessage) {
        this.quoteMessage = quoteMessage;
    }

    public NoticeData getNotice() {
        return notice;
    }

    public void setNotice(NoticeData notice) {
        this.notice = notice;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /*public ChatMessage(String messageId, String type, String receiverType, String msgType, String receiverId, String textMsg, String url, int isTop, ChatMessage quoteMessage) {
        this.messageId = messageId;
        this.type = type;
        this.receiverType = receiverType;
        this.msgType = msgType;
        this.receiverId = receiverId;
        this.textMsg = textMsg;
        this.url = url;
        this.isTop = isTop;
        this.quoteMessage = quoteMessage;
    }*/

    public ChatMessage(String messageId, String sendTime, String type, String receiverType, String msgType, String receiverId, String textMsg, String url, int isTop, int isTemporary) {
        this.messageId = messageId;
        this.type = type;
        this.receiverType = receiverType;
        this.msgType = msgType;
        this.receiverId = receiverId;
        this.textMsg = textMsg;
        this.url = url;
        this.isTop = isTop;
        this.sendTime = sendTime;
        this.isTemporary = isTemporary;
    }

    @Generated(hash = 29147650)
    public ChatMessage(String messageId, String type, String receiverType,
                       String msgType, String receiverId, String textMsg, String url,
                       String quoteMessageInfo, String sendTime, int isTop, int isMeSend,
                       String noticeInfo, String memberId, String memberNickname) {
        this.messageId = messageId;
        this.type = type;
        this.receiverType = receiverType;
        this.msgType = msgType;
        this.receiverId = receiverId;
        this.textMsg = textMsg;
        this.url = url;
        this.quoteMessageInfo = quoteMessageInfo;
        this.sendTime = sendTime;
        this.isTop = isTop;
        this.isMeSend = isMeSend;
        this.noticeInfo = noticeInfo;
        this.memberId = memberId;
        this.memberNickname = memberNickname;
    }

    @Generated(hash = 2271208)
    public ChatMessage() {
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReceiverType() {
        return this.receiverType;
    }

    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }

    public String getMsgType() {
        return this.msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getReceiverId() {
        return this.receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getTextMsg() {
        return this.textMsg;
    }

    public void setTextMsg(String textMsg) {
        this.textMsg = textMsg;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getQuoteMessageInfo() {
        return this.quoteMessageInfo;
    }

    public void setQuoteMessageInfo(String quoteMessageInfo) {
        this.quoteMessageInfo = quoteMessageInfo;
    }

    public String getSendTime() {
        return this.sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public int getIsTop() {
        return this.isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public int getIsMeSend() {
        return this.isMeSend;
    }

    public void setIsMeSend(int isMeSend) {
        this.isMeSend = isMeSend;
    }

    public String getNoticeInfo() {
        return this.noticeInfo;
    }

    public void setNoticeInfo(String noticeInfo) {
        this.noticeInfo = noticeInfo;
    }

    public String getMemberId() {
        return this.memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberNickname() {
        return this.memberNickname;
    }

    public void setMemberNickname(String memberNickname) {
        this.memberNickname = memberNickname;
    }

}
