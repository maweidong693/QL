package com.weiwu.ql.main.contact.chat.modle;

import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/11/22 13:07 
 */
public class HistoryMessageData {


    private int code;
    private String msg;
    private DataDTO data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public static class DataDTO {
        private int start;
        private int end;
        private int count;
        private GroupInfoDTO groupInfo;
        private List<ListDTO> list;

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public GroupInfoDTO getGroupInfo() {
            return groupInfo;
        }

        public void setGroupInfo(GroupInfoDTO groupInfo) {
            this.groupInfo = groupInfo;
        }

        public List<ListDTO> getList() {
            return list;
        }

        public void setList(List<ListDTO> list) {
            this.list = list;
        }

        public static class GroupInfoDTO {
            private int id;
            private String group_name;
            private String group_id;
            private String owner_id;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getGroup_name() {
                return group_name;
            }

            public void setGroup_name(String group_name) {
                this.group_name = group_name;
            }

            public String getGroup_id() {
                return group_id;
            }

            public void setGroup_id(String group_id) {
                this.group_id = group_id;
            }

            public String getOwner_id() {
                return owner_id;
            }

            public void setOwner_id(String owner_id) {
                this.owner_id = owner_id;
            }
        }

        public static class ListDTO {
            private int msg_id;
            private String fid;
            private String toid;
            private String nick_name;
            private String face_url;
            private String last_time;
            private String content;
            private String cite;
            private int cate;
            private int type;
            private int sending;

            public int getSending() {
                return sending;
            }

            public void setSending(int sending) {
                this.sending = sending;
            }

            public ListDTO(String fid, String nick_name, String face_url, String content, int cate, int type, int sneding, String lastTime) {
                this.fid = fid;
                this.nick_name = nick_name;
                this.face_url = face_url;
                this.cate = cate;
                this.type = type;
                this.sending = sneding;
                this.content = content;
                this.last_time = lastTime;
            }

            public String getCite() {
                return cite;
            }

            public void setCite(String cite) {
                this.cite = cite;
            }

            public int getMsg_id() {
                return msg_id;
            }

            public void setMsg_id(int msg_id) {
                this.msg_id = msg_id;
            }

            public String getFid() {
                return fid;
            }

            public void setFid(String fid) {
                this.fid = fid;
            }

            public String getToid() {
                return toid;
            }

            public void setToid(String toid) {
                this.toid = toid;
            }

            public String getNick_name() {
                return nick_name;
            }

            public void setNick_name(String nick_name) {
                this.nick_name = nick_name;
            }

            public String getFace_url() {
                return face_url;
            }

            public void setFace_url(String face_url) {
                this.face_url = face_url;
            }

            public String getLast_time() {
                return last_time;
            }

            public void setLast_time(String last_time) {
                this.last_time = last_time;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getCate() {
                return cate;
            }

            public void setCate(int cate) {
                this.cate = cate;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
