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
        private String id;
        private String name;
        private String createdTime;
        private String updatedTime;
        private int del;
        private String createdBy;
        private String updatedBy;
        private List<GroupMemberData.DataDTO> memberGroupResultVOList;
        private String currentMemberRoleName;
        private int currentMemberRole;
        private String currentMemberId;
        private String avator;
        private String notice;
        private String introduction;
        private int isForbidden;
        private int isAllForbidden;
        private int isExamine;

        public int getIsAllForbidden() {
            return isAllForbidden;
        }

        public void setIsAllForbidden(int isAllForbidden) {
            this.isAllForbidden = isAllForbidden;
        }

        public int getIsExamine() {
            return isExamine;
        }

        public void setIsExamine(int isExamine) {
            this.isExamine = isExamine;
        }

        public int getIsForbidden() {
            return isForbidden;
        }

        public void setIsForbidden(int isForbidden) {
            this.isForbidden = isForbidden;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
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

        public List<GroupMemberData.DataDTO> getMemberGroupResultVOList() {
            return memberGroupResultVOList;
        }

        public void setMemberGroupResultVOList(List<GroupMemberData.DataDTO> memberGroupResultVOList) {
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

        public String getAvator() {
            return avator;
        }

        public void setAvator(String avator) {
            this.avator = avator;
        }

        public String getNotice() {
            return notice;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }

    }
}
