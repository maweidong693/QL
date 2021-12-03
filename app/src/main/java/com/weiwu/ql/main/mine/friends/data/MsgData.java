package com.weiwu.ql.main.mine.friends.data;

import java.util.List;

public class MsgData {
    private String msg;
    private int code;
    private List<MsgEntity> data;

    public MsgData(String msg, int code, List<MsgEntity> data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<MsgEntity> getData() {
        return data;
    }

    public void setData(List<MsgEntity> data) {
        this.data = data;
    }


    public static class MsgEntity {
        private String content;
        private String createTime;
        private int id;
        private String msgContent;
        private int msgId;
        private String msgPic;
        private int readed;
        private int remindTo;
        private int reminder;
        private String reminderIcon;
        private String reminderName;
        private String replyTonNick;
        private int type;
        private String updateTime;

        public String getReplyTonNick() {
            return replyTonNick;
        }

        public void setReplyTonNick(String replyTonNick) {
            this.replyTonNick = replyTonNick;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMsgContent() {
            return msgContent;
        }

        public void setMsgContent(String msgContent) {
            this.msgContent = msgContent;
        }

        public int getMsgId() {
            return msgId;
        }

        public void setMsgId(int msgId) {
            this.msgId = msgId;
        }

        public String getMsgPic() {
            return msgPic;
        }

        public void setMsgPic(String msgPic) {
            this.msgPic = msgPic;
        }

        public int getReaded() {
            return readed;
        }

        public void setReaded(int readed) {
            this.readed = readed;
        }

        public int getRemindTo() {
            return remindTo;
        }

        public void setRemindTo(int remindTo) {
            this.remindTo = remindTo;
        }

        public int getReminder() {
            return reminder;
        }

        public void setReminder(int reminder) {
            this.reminder = reminder;
        }

        public String getReminderIcon() {
            return reminderIcon;
        }

        public void setReminderIcon(String reminderIcon) {
            this.reminderIcon = reminderIcon;
        }

        public String getReminderName() {
            return reminderName;
        }

        public void setReminderName(String reminderName) {
            this.reminderName = reminderName;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }
}
