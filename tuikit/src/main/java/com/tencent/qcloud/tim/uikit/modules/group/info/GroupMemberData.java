package com.tencent.qcloud.tim.uikit.modules.group.info;

import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/11 13:18 
 */
public class GroupMemberData {


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

    public static class DataDTO {
        private String member_id;
        private String face_url;
        private String nick_name;
        private int group_role;
        private int is_ban_say;

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getFace_url() {
            return face_url;
        }

        public void setFace_url(String face_url) {
            this.face_url = face_url;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public int getGroup_role() {
            return group_role;
        }

        public void setGroup_role(int group_role) {
            this.group_role = group_role;
        }

        public int getIs_ban_say() {
            return is_ban_say;
        }

        public void setIs_ban_say(int is_ban_say) {
            this.is_ban_say = is_ban_say;
        }
    }
}
