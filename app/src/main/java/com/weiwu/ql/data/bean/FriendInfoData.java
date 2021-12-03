package com.weiwu.ql.data.bean;

import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/12 09:21 
 */
public class FriendInfoData {

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
        private String user_id;
        private String nick_name;
        private String alias;
        private String face_url;
        private int is_black;
        private int not_show;
        private int not_see;
        private int is_friend;
        private List<?> moments;

        public int getIs_friend() {
            return is_friend;
        }

        public void setIs_friend(int is_friend) {
            this.is_friend = is_friend;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getFace_url() {
            return face_url;
        }

        public void setFace_url(String face_url) {
            this.face_url = face_url;
        }

        public int getIs_black() {
            return is_black;
        }

        public void setIs_black(int is_black) {
            this.is_black = is_black;
        }

        public int getNot_show() {
            return not_show;
        }

        public void setNot_show(int not_show) {
            this.not_show = not_show;
        }

        public int getNot_see() {
            return not_see;
        }

        public void setNot_see(int not_see) {
            this.not_see = not_see;
        }

        public List<?> getMoments() {
            return moments;
        }

        public void setMoments(List<?> moments) {
            this.moments = moments;
        }
    }
}
