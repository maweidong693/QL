package com.tencent.qcloud.tim.uikit;

import com.tencent.imsdk.v2.V2TIMFriendApplication;

import java.io.Serializable;
import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/5/20 12:44 
 */
public class NewFriendBean implements Serializable {

    private int code;
    private String msg;
    private List<DataDTO> data;

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

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public static class DataDTO implements Serializable {
        private int id;
        private String user_id;
        private String friend_id;
        private String friend_nick;
        private String face_url;
        private int status;
        private String create_time;
        private String status_msg;
        private String user_nick;
        private String remark;

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        @Override
        public String toString() {
            return "DataDTO{" +
                    "id=" + id +
                    ", user_id='" + user_id + '\'' +
                    ", friend_id='" + friend_id + '\'' +
                    ", friend_nick='" + friend_nick + '\'' +
                    ", face_url='" + face_url + '\'' +
                    ", status=" + status +
                    ", create_time='" + create_time + '\'' +
                    ", status_msg='" + status_msg + '\'' +
                    ", user_nick='" + user_nick + '\'' +
                    '}';
        }

        public String getUser_nick() {
            return user_nick;
        }

        public void setUser_nick(String user_nick) {
            this.user_nick = user_nick;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getFriend_id() {
            return friend_id;
        }

        public void setFriend_id(String friend_id) {
            this.friend_id = friend_id;
        }

        public String getFriend_nick() {
            return friend_nick;
        }

        public void setFriend_nick(String friend_nick) {
            this.friend_nick = friend_nick;
        }

        public String getFace_url() {
            return face_url;
        }

        public void setFace_url(String face_url) {
            this.face_url = face_url;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getStatus_msg() {
            return status_msg;
        }

        public void setStatus_msg(String status_msg) {
            this.status_msg = status_msg;
        }
    }
}
