package com.weiwu.ql.main.contact.chat.modle;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/14 13:37 
 */
public class NoticeData {
    /*【0，更新】、
      【101，禁言】、【102，解除禁言】、【103，禁言通知】、【104，取消禁言通知】
      【110，设置管理员】、【111、转让群主】、【112、修改公告】、【113、修改群名】、【114、修改群邀请审核】、
      【115、修改群内加好友设置】、【116、退群】、【117、更换群头像】、【118、新建群】【119、新群员加入】
      【400，群组已解散！】*/
    private int type;
    private String noticeMessage;
    private String fromMemberNickName;
    private String relationInfo;
    private NoticeOrderData tradeInfo;

    public NoticeOrderData getTradeInfo() {
        return tradeInfo;
    }

    public void setTradeInfo(NoticeOrderData tradeInfo) {
        this.tradeInfo = tradeInfo;
    }

    public NoticeData(int type, String noticeMessage, String fromMemberNickName, String relationInfo) {
        this.type = type;
        this.noticeMessage = noticeMessage;
        this.fromMemberNickName = fromMemberNickName;
        this.relationInfo = relationInfo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNoticeMessage() {
        return noticeMessage;
    }

    public void setNoticeMessage(String noticeMessage) {
        this.noticeMessage = noticeMessage;
    }

    public String getFromMemberNickName() {
        return fromMemberNickName;
    }

    public void setFromMemberNickName(String fromMemberNickName) {
        this.fromMemberNickName = fromMemberNickName;
    }

    public String getRelationInfo() {
        return relationInfo;
    }

    public void setRelationInfo(String relationInfo) {
        this.relationInfo = relationInfo;
    }
}
