package com.weiwu.ql.data.bean;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/22 14:29 
 */
public class ChatReceiveMessageData {
    private String memberId;                        //发送人ID
    private String memberNickname;           //发送人昵称
    private String msgType;                          //msg文本消息（img图片消息，doc文件，voi音频，video视频。暂无）

    @Override
    public String toString() {
        return "ChatReceiveMessageData{" +
                "memberId='" + memberId + '\'' +
                ", memberNickname='" + memberNickname + '\'' +
                ", msgType='" + msgType + '\'' +
                ", textMsg='" + textMsg + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }

    private String textMsg;                           //文本消息

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

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
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

    public ChatReceiveMessageData(String memberId, String memberNickname, String msgType, String textMsg, String imgUrl) {
        this.memberId = memberId;
        this.memberNickname = memberNickname;
        this.msgType = msgType;
        this.textMsg = textMsg;
        this.imgUrl = imgUrl;
    }

    private String imgUrl;                                  // 图片消息时，图片url
}
