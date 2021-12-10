package com.tencent.qcloud.tim.uikit.modules.group.info;

import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/11 13:20 
 */
public class GroupInfoData {


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
        private String id;
        private int group_role;
        private int group_user_count;
        private String group_name;
        private Object group_avatar_url;
        private String group_id;
        private String my_group_nickname;
        private String notification;
        private String introduction;
        private int is_add_friend;
        private int is_join_check;
        private int is_ban_say;
        private int my_is_ban_say;
        private int is_top;
        private int new_user_count;
        private List<GroupUserDTO> groupUser;

        public int getNew_user_count() {
            return new_user_count;
        }

        public void setNew_user_count(int new_user_count) {
            this.new_user_count = new_user_count;
        }

        public int getMy_is_ban_say() {
            return my_is_ban_say;
        }

        public void setMy_is_ban_say(int my_is_ban_say) {
            this.my_is_ban_say = my_is_ban_say;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getGroup_role() {
            return group_role;
        }

        public void setGroup_role(int group_role) {
            this.group_role = group_role;
        }

        public int getGroup_user_count() {
            return group_user_count;
        }

        public void setGroup_user_count(int group_user_count) {
            this.group_user_count = group_user_count;
        }

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public Object getGroup_avatar_url() {
            return group_avatar_url;
        }

        public void setGroup_avatar_url(Object group_avatar_url) {
            this.group_avatar_url = group_avatar_url;
        }

        public String getGroup_id() {
            return group_id;
        }

        public void setGroup_id(String group_id) {
            this.group_id = group_id;
        }

        public String getMy_group_nickname() {
            return my_group_nickname;
        }

        public void setMy_group_nickname(String my_group_nickname) {
            this.my_group_nickname = my_group_nickname;
        }

        public String getNotification() {
            return notification;
        }

        public void setNotification(String notification) {
            this.notification = notification;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public int getIs_add_friend() {
            return is_add_friend;
        }

        public void setIs_add_friend(int is_add_friend) {
            this.is_add_friend = is_add_friend;
        }

        public int getIs_join_check() {
            return is_join_check;
        }

        public void setIs_join_check(int is_join_check) {
            this.is_join_check = is_join_check;
        }

        public int getIs_ban_say() {
            return is_ban_say;
        }

        public void setIs_ban_say(int is_ban_say) {
            this.is_ban_say = is_ban_say;
        }

        public int getIs_top() {
            return is_top;
        }

        public void setIs_top(int is_top) {
            this.is_top = is_top;
        }

        public List<GroupUserDTO> getGroupUser() {
            return groupUser;
        }

        public void setGroupUser(List<GroupUserDTO> groupUser) {
            this.groupUser = groupUser;
        }

        public static class GroupUserDTO {
            private int id;
            private String group_id;
            private int join_type;
            private String operator_id;
            private Object my_group_nickname;
            private String member_id;
            private int is_ban_say;
            private String join_time;
            private int group_role;
            private String face_url;
            private String nick_name;
            private MemberDTO member;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getGroup_id() {
                return group_id;
            }

            public void setGroup_id(String group_id) {
                this.group_id = group_id;
            }

            public int getJoin_type() {
                return join_type;
            }

            public void setJoin_type(int join_type) {
                this.join_type = join_type;
            }

            public String getOperator_id() {
                return operator_id;
            }

            public void setOperator_id(String operator_id) {
                this.operator_id = operator_id;
            }

            public Object getMy_group_nickname() {
                return my_group_nickname;
            }

            public void setMy_group_nickname(Object my_group_nickname) {
                this.my_group_nickname = my_group_nickname;
            }

            public String getMember_id() {
                return member_id;
            }

            public void setMember_id(String member_id) {
                this.member_id = member_id;
            }

            public int getIs_ban_say() {
                return is_ban_say;
            }

            public void setIs_ban_say(int is_ban_say) {
                this.is_ban_say = is_ban_say;
            }

            public String getJoin_time() {
                return join_time;
            }

            public void setJoin_time(String join_time) {
                this.join_time = join_time;
            }

            public int getGroup_role() {
                return group_role;
            }

            public void setGroup_role(int group_role) {
                this.group_role = group_role;
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

            public MemberDTO getMember() {
                return member;
            }

            public void setMember(MemberDTO member) {
                this.member = member;
            }

            public static class MemberDTO {
                private int id;
                private Object pid;
                private String nation_code;
                private String mobile;
                private String im_id;
                private int is_check_friend;
                private String password;
                private String token;
                private String nick_name;
                private String sing;
                private String face_url;
                private int sex;
                private int status;
                private Object master_code;
                private String my_code;
                private String reg_time;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public Object getPid() {
                    return pid;
                }

                public void setPid(Object pid) {
                    this.pid = pid;
                }

                public String getNation_code() {
                    return nation_code;
                }

                public void setNation_code(String nation_code) {
                    this.nation_code = nation_code;
                }

                public String getMobile() {
                    return mobile;
                }

                public void setMobile(String mobile) {
                    this.mobile = mobile;
                }

                public String getIm_id() {
                    return im_id;
                }

                public void setIm_id(String im_id) {
                    this.im_id = im_id;
                }

                public int getIs_check_friend() {
                    return is_check_friend;
                }

                public void setIs_check_friend(int is_check_friend) {
                    this.is_check_friend = is_check_friend;
                }

                public String getPassword() {
                    return password;
                }

                public void setPassword(String password) {
                    this.password = password;
                }

                public String getToken() {
                    return token;
                }

                public void setToken(String token) {
                    this.token = token;
                }

                public String getNick_name() {
                    return nick_name;
                }

                public void setNick_name(String nick_name) {
                    this.nick_name = nick_name;
                }

                public String getSing() {
                    return sing;
                }

                public void setSing(String sing) {
                    this.sing = sing;
                }

                public String getFace_url() {
                    return face_url;
                }

                public void setFace_url(String face_url) {
                    this.face_url = face_url;
                }

                public int getSex() {
                    return sex;
                }

                public void setSex(int sex) {
                    this.sex = sex;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public Object getMaster_code() {
                    return master_code;
                }

                public void setMaster_code(Object master_code) {
                    this.master_code = master_code;
                }

                public String getMy_code() {
                    return my_code;
                }

                public void setMy_code(String my_code) {
                    this.my_code = my_code;
                }

                public String getReg_time() {
                    return reg_time;
                }

                public void setReg_time(String reg_time) {
                    this.reg_time = reg_time;
                }
            }
        }
    }
}
