package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/19 15:19 
 */
public class AddCommentRequestBody {
    private String momentsId;  //动态ID
    private int type;       //0评论 1回复 2点赞
    private String replyCommentId;      //评论ID（回复时传）
    private String content;         //内容（评论和回复时传）

    public AddCommentRequestBody(String momentsId, int type) {
        this.momentsId = momentsId;
        this.type = type;
    }

    public AddCommentRequestBody(String momentsId, int type, String content) {
        this.momentsId = momentsId;
        this.type = type;
        this.content = content;
    }

    public String getMomentsId() {
        return momentsId;
    }

    public void setMomentsId(String momentsId) {
        this.momentsId = momentsId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getReplyCommentId() {
        return replyCommentId;
    }

    public void setReplyCommentId(String replyCommentId) {
        this.replyCommentId = replyCommentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AddCommentRequestBody(String momentsId, int type, String replyCommentId, String content) {
        this.momentsId = momentsId;
        this.type = type;
        this.replyCommentId = replyCommentId;
        this.content = content;
    }
}
