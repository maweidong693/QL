package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/19 15:19 
 */
public class AddCommentRequestBody {
    private String type;       //0评论 1回复 2点赞
    private String msgId;  //动态ID
    private String content;         //内容（评论和回复时传）
    private String commentId;      //评论ID（回复时传）
    private String replyTo;      //评论ID（回复时传）

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public AddCommentRequestBody(String momentsId, String type) {
        this.msgId = momentsId;
        this.type = type;
    }

    public AddCommentRequestBody(String momentsId, String type, String content) {
        this.msgId = momentsId;
        this.type = type;
        this.content = content;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AddCommentRequestBody(String type, String msgId, String content, String commentId, String replyTo) {
        this.type = type;
        this.msgId = msgId;
        this.content = content;
        this.commentId = commentId;
        this.replyTo = replyTo;
    }
}
