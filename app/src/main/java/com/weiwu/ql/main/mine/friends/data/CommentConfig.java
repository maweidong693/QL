package com.weiwu.ql.main.mine.friends.data;

import com.weiwu.ql.data.bean.FriendsData;

/**
 * Created by yiwei on 16/3/2.
 */
public class CommentConfig {
    public static enum Type{
        PUBLIC("public"), REPLY("reply");

        private String value;
        private Type(String value){
            this.value = value;
        }

    }

    public String msgId;
    public String commentId;
    public int circlePosition;
    public int commentPosition;
    public Type commentType;
    public FriendsData.DataDTO.ThumbListDTO.FromMemberInfoDTO replyUser;

    @Override
    public String toString() {
        String replyUserStr = "";
        if(replyUser != null){
            replyUserStr = replyUser.toString();
        }
        return "msgId = " + msgId
                + "; commentId = " + commentId
                + "; circlePosition = " + circlePosition
                + "; commentPosition = " + commentPosition
                + "; commentType ＝ " + commentType
                + "; replyUser = " + replyUserStr;
    }
}
