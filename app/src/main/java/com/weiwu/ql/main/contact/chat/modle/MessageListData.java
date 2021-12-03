package com.weiwu.ql.main.contact.chat.modle;


import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/4 09:21 
 */
public class MessageListData {

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
        private List<ListDTO> list;
        private int unread_count;

        public List<ListDTO> getList() {
            return list;
        }

        public void setList(List<ListDTO> list) {
            this.list = list;
        }

        public int getUnread_count() {
            return unread_count;
        }

        public void setUnread_count(int unread_count) {
            this.unread_count = unread_count;
        }

        public static class ListDTO {
            private int id;
            private String fid;
            private String toid;
            private String chat_id;
            private int chat_type;
            private String avatar_url;
            private String name;
            private String last_time;
            private int unread_count;
            private int cate;
            private String content;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public String getChat_id() {
                return chat_id;
            }

            public void setChat_id(String chat_id) {
                this.chat_id = chat_id;
            }

            public int getChat_type() {
                return chat_type;
            }

            public void setChat_type(int chat_type) {
                this.chat_type = chat_type;
            }

            public String getAvatar_url() {
                return avatar_url;
            }

            public void setAvatar_url(String avatar_url) {
                this.avatar_url = avatar_url;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getLast_time() {
                return last_time;
            }

            public void setLast_time(String last_time) {
                this.last_time = last_time;
            }

            public int getUnread_count() {
                return unread_count;
            }

            public void setUnread_count(int unread_count) {
                this.unread_count = unread_count;
            }

            public int getCate() {
                return cate;
            }

            public void setCate(int cate) {
                this.cate = cate;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }
}
