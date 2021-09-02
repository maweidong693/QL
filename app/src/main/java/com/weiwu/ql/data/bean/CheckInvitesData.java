package com.weiwu.ql.data.bean;

import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/20 15:16 
 */
public class CheckInvitesData {

    private int code;
    private String message;
    private List<DataDTO> data;

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

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public static class DataDTO {
        private String id;
        private String groupId;
        private String inviteMemberId;
        private String fromMemberId;
        private String createdTime;
        private AppletGroupInfoReusltVODTO appletGroupInfoReusltVO;
        private InviteMemberInfoDTO inviteMemberInfo;
        private InviteMemberInfoDTO fromMemberInfo;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getInviteMemberId() {
            return inviteMemberId;
        }

        public void setInviteMemberId(String inviteMemberId) {
            this.inviteMemberId = inviteMemberId;
        }

        public String getFromMemberId() {
            return fromMemberId;
        }

        public void setFromMemberId(String fromMemberId) {
            this.fromMemberId = fromMemberId;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        public AppletGroupInfoReusltVODTO getAppletGroupInfoReusltVO() {
            return appletGroupInfoReusltVO;
        }

        public void setAppletGroupInfoReusltVO(AppletGroupInfoReusltVODTO appletGroupInfoReusltVO) {
            this.appletGroupInfoReusltVO = appletGroupInfoReusltVO;
        }

        public InviteMemberInfoDTO getInviteMemberInfo() {
            return inviteMemberInfo;
        }

        public void setInviteMemberInfo(InviteMemberInfoDTO inviteMemberInfo) {
            this.inviteMemberInfo = inviteMemberInfo;
        }

        public InviteMemberInfoDTO getFromMemberInfo() {
            return fromMemberInfo;
        }

        public void setFromMemberInfo(InviteMemberInfoDTO fromMemberInfo) {
            this.fromMemberInfo = fromMemberInfo;
        }

        public static class AppletGroupInfoReusltVODTO {
            private String id;
            private String name;
            private String createdTime;
            private String updatedTime;
            private int del;
            private String createdBy;
            private String updatedBy;
            private List<MemberGroupResultVOListDTO> memberGroupResultVOList;
            private String currentMemberRoleName;
            private int currentMemberRole;
            private String currentMemberId;
            private int isExamine;
            private int isFrd;
            private int isForbidden;
            private int isAllForbidden;

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

            public List<MemberGroupResultVOListDTO> getMemberGroupResultVOList() {
                return memberGroupResultVOList;
            }

            public void setMemberGroupResultVOList(List<MemberGroupResultVOListDTO> memberGroupResultVOList) {
                this.memberGroupResultVOList = memberGroupResultVOList;
            }

            public String getCurrentMemberRoleName() {
                return currentMemberRoleName;
            }

            public void setCurrentMemberRoleName(String currentMemberRoleName) {
                this.currentMemberRoleName = currentMemberRoleName;
            }

            public int getCurrentMemberRole() {
                return currentMemberRole;
            }

            public void setCurrentMemberRole(int currentMemberRole) {
                this.currentMemberRole = currentMemberRole;
            }

            public String getCurrentMemberId() {
                return currentMemberId;
            }

            public void setCurrentMemberId(String currentMemberId) {
                this.currentMemberId = currentMemberId;
            }

            public int getIsExamine() {
                return isExamine;
            }

            public void setIsExamine(int isExamine) {
                this.isExamine = isExamine;
            }

            public int getIsFrd() {
                return isFrd;
            }

            public void setIsFrd(int isFrd) {
                this.isFrd = isFrd;
            }

            public int getIsForbidden() {
                return isForbidden;
            }

            public void setIsForbidden(int isForbidden) {
                this.isForbidden = isForbidden;
            }

            public int getIsAllForbidden() {
                return isAllForbidden;
            }

            public void setIsAllForbidden(int isAllForbidden) {
                this.isAllForbidden = isAllForbidden;
            }

            public static class MemberGroupResultVOListDTO {
                private String id;
                private String nickName;
                private String mobile;
                private int sex;
                private String roleName;
                private int role;
                private int isForbidden;
                private String avator;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getNickName() {
                    return nickName;
                }

                public void setNickName(String nickName) {
                    this.nickName = nickName;
                }

                public String getMobile() {
                    return mobile;
                }

                public void setMobile(String mobile) {
                    this.mobile = mobile;
                }

                public int getSex() {
                    return sex;
                }

                public void setSex(int sex) {
                    this.sex = sex;
                }

                public String getRoleName() {
                    return roleName;
                }

                public void setRoleName(String roleName) {
                    this.roleName = roleName;
                }

                public int getRole() {
                    return role;
                }

                public void setRole(int role) {
                    this.role = role;
                }

                public int getIsForbidden() {
                    return isForbidden;
                }

                public void setIsForbidden(int isForbidden) {
                    this.isForbidden = isForbidden;
                }

                public String getAvator() {
                    return avator;
                }

                public void setAvator(String avator) {
                    this.avator = avator;
                }
            }
        }

        public static class InviteMemberInfoDTO {
            private String id;
            private String nickName;
            private String mobile;
            private String lastLoginTime;
            private String createdTime;
            private String updatedTime;
            private String avator;
            private int sex;

            public String getAvator() {
                return avator;
            }

            public void setAvator(String avator) {
                this.avator = avator;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
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
    }
}
