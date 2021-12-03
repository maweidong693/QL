package com.tencent.common.http;

import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;

import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/22 15:21 
 */
public class ContactListData {

    private int code;
    private String message;
    private DataDTO data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public static class DataDTO {
        private int new_friend_count;
        private List<FriendsDTO> list;
        private List<FriendsDTO> blockFriends;
        private List<GroupsDTO> groups;

        public int getNew_friend_count() {
            return new_friend_count;
        }

        public void setNew_friend_count(int new_friend_count) {
            this.new_friend_count = new_friend_count;
        }

        public List<FriendsDTO> getBlockFriends() {
            return blockFriends;
        }

        public void setBlockFriends(List<FriendsDTO> blockFriends) {
            this.blockFriends = blockFriends;
        }

        public List<FriendsDTO> getList() {
            return list;
        }

        public void setList(List<FriendsDTO> list) {
            this.list = list;
        }

        public List<GroupsDTO> getGroups() {
            return groups;
        }

        public void setGroups(List<GroupsDTO> groups) {
            this.groups = groups;
        }

        public static class FriendsDTO {
            private int id;
            private String im_id;
            private String member_id;
            private String nick_name;
            private String mobile;
            private String lastLoginTime;
            private String createdTime;
            private String updatedTime;
            private String remark;
            private String face_url;
            private int sex;
            private int unread;

            public FriendsDTO covertTIMFriend(BlackListData.DataDTO friendInfo) {
                if (friendInfo == null) {
                    return this;
                }
                setIm_id(friendInfo.getMember_id());
                setRemark(friendInfo.getNick_name());
                setNick_name(friendInfo.getNick_name());
                setMember_id(friendInfo.getMember_id());
                setFace_url(friendInfo.getFace_url());
                return this;
            }

            public String getMember_id() {
                return member_id;
            }

            public void setMember_id(String member_id) {
                this.member_id = member_id;
            }

            public int getUnread() {
                return unread;
            }

            public void setUnread(int unread) {
                this.unread = unread;
            }

            public String getIm_id() {
                return im_id;
            }

            public void setIm_id(String im_id) {
                this.im_id = im_id;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getFace_url() {
                return face_url;
            }

            public void setFace_url(String face_url) {
                this.face_url = face_url;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getNick_name() {
                return nick_name;
            }

            public void setNick_name(String nick_name) {
                this.nick_name = nick_name;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getLastLoginTime() {
                return lastLoginTime;
            }

            public void setLastLoginTime(String lastLoginTime) {
                this.lastLoginTime = lastLoginTime;
            }

            public String getCreatedTime() {
                return createdTime;
            }

            public void setCreatedTime(String createdTime) {
                this.createdTime = createdTime;
            }

            public String getUpdatedTime() {
                return updatedTime;
            }

            public void setUpdatedTime(String updatedTime) {
                this.updatedTime = updatedTime;
            }

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }
        }

        public static class GroupsDTO {
            private String id;
            private String name;
            private String mobile;
            private String createdBy;
            private String updatedBy;
            private String createdTime;
            private String updatedTime;
            private String avatar;
            private int del;

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getCreatedBy() {
                return createdBy;
            }

            public void setCreatedBy(String createdBy) {
                this.createdBy = createdBy;
            }

            public String getUpdatedBy() {
                return updatedBy;
            }

            public void setUpdatedBy(String updatedBy) {
                this.updatedBy = updatedBy;
            }

            public String getCreatedTime() {
                return createdTime;
            }

            public void setCreatedTime(String createdTime) {
                this.createdTime = createdTime;
            }

            public String getUpdatedTime() {
                return updatedTime;
            }

            public void setUpdatedTime(String updatedTime) {
                this.updatedTime = updatedTime;
            }

            public int getDel() {
                return del;
            }

            public void setDel(int del) {
                this.del = del;
            }
        }
    }
}
