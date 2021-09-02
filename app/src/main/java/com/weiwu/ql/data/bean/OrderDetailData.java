package com.weiwu.ql.data.bean;

import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/31 17:04 
 */
public class OrderDetailData {

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
        private String orderNo;
        private String releaseUserId;
        private int releaseCoinType;
        private String releaseCoinName;
        private String releaseUserCoinAddress;
        private String releaseTime;
        private int money;
        private String dispatchMemberId;
        private int handlerReturnCoinType;
        private String handlerReturnCoinName;
        private int type;
        private int status;
        private String createdTime;
        private String createdBy;
        private String updatedTime;
        private String updatedBy;
        private int del;
        private List<ChildTradeVOListDTO> childTradeVOList;
        private DispatchMemberInfoDTO dispatchMemberInfo;
        private ReleaseUserInfoDTO releaseUserInfo;
        private List<ChildTradeVOListDTO.StatusInfoListDTO> statusInfoList;
        private String pid;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getReleaseUserId() {
            return releaseUserId;
        }

        public void setReleaseUserId(String releaseUserId) {
            this.releaseUserId = releaseUserId;
        }

        public int getReleaseCoinType() {
            return releaseCoinType;
        }

        public void setReleaseCoinType(int releaseCoinType) {
            this.releaseCoinType = releaseCoinType;
        }

        public String getReleaseCoinName() {
            return releaseCoinName;
        }

        public void setReleaseCoinName(String releaseCoinName) {
            this.releaseCoinName = releaseCoinName;
        }

        public String getReleaseUserCoinAddress() {
            return releaseUserCoinAddress;
        }

        public void setReleaseUserCoinAddress(String releaseUserCoinAddress) {
            this.releaseUserCoinAddress = releaseUserCoinAddress;
        }

        public String getReleaseTime() {
            return releaseTime;
        }

        public void setReleaseTime(String releaseTime) {
            this.releaseTime = releaseTime;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public String getDispatchMemberId() {
            return dispatchMemberId;
        }

        public void setDispatchMemberId(String dispatchMemberId) {
            this.dispatchMemberId = dispatchMemberId;
        }

        public int getHandlerReturnCoinType() {
            return handlerReturnCoinType;
        }

        public void setHandlerReturnCoinType(int handlerReturnCoinType) {
            this.handlerReturnCoinType = handlerReturnCoinType;
        }

        public String getHandlerReturnCoinName() {
            return handlerReturnCoinName;
        }

        public void setHandlerReturnCoinName(String handlerReturnCoinName) {
            this.handlerReturnCoinName = handlerReturnCoinName;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getUpdatedTime() {
            return updatedTime;
        }

        public void setUpdatedTime(String updatedTime) {
            this.updatedTime = updatedTime;
        }

        public String getUpdatedBy() {
            return updatedBy;
        }

        public void setUpdatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
        }

        public int getDel() {
            return del;
        }

        public void setDel(int del) {
            this.del = del;
        }

        public List<ChildTradeVOListDTO> getChildTradeVOList() {
            return childTradeVOList;
        }

        public void setChildTradeVOList(List<ChildTradeVOListDTO> childTradeVOList) {
            this.childTradeVOList = childTradeVOList;
        }

        public DispatchMemberInfoDTO getDispatchMemberInfo() {
            return dispatchMemberInfo;
        }

        public void setDispatchMemberInfo(DispatchMemberInfoDTO dispatchMemberInfo) {
            this.dispatchMemberInfo = dispatchMemberInfo;
        }

        public ReleaseUserInfoDTO getReleaseUserInfo() {
            return releaseUserInfo;
        }

        public void setReleaseUserInfo(ReleaseUserInfoDTO releaseUserInfo) {
            this.releaseUserInfo = releaseUserInfo;
        }

        public List<ChildTradeVOListDTO.StatusInfoListDTO> getStatusInfoList() {
            return statusInfoList;
        }

        public void setStatusInfoList(List<ChildTradeVOListDTO.StatusInfoListDTO> statusInfoList) {
            this.statusInfoList = statusInfoList;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public static class DispatchMemberInfoDTO {
            private String id;
            private String nickName;
            private String avator;
            private String mobile;
            private String personalSignature;
            private String lastLoginTime;
            private String createdTime;
            private String updatedTime;
            private int sex;

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

            public String getAvator() {
                return avator;
            }

            public void setAvator(String avator) {
                this.avator = avator;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getPersonalSignature() {
                return personalSignature;
            }

            public void setPersonalSignature(String personalSignature) {
                this.personalSignature = personalSignature;
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

        public static class ReleaseUserInfoDTO {
            private String id;
            private String name;
            private String username;
            private String createdTime;
            private String createdBy;
            private String updatedTime;
            private String updtaedBy;
            private int role;

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

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getCreatedTime() {
                return createdTime;
            }

            public void setCreatedTime(String createdTime) {
                this.createdTime = createdTime;
            }

            public String getCreatedBy() {
                return createdBy;
            }

            public void setCreatedBy(String createdBy) {
                this.createdBy = createdBy;
            }

            public String getUpdatedTime() {
                return updatedTime;
            }

            public void setUpdatedTime(String updatedTime) {
                this.updatedTime = updatedTime;
            }

            public String getUpdtaedBy() {
                return updtaedBy;
            }

            public void setUpdtaedBy(String updtaedBy) {
                this.updtaedBy = updtaedBy;
            }

            public int getRole() {
                return role;
            }

            public void setRole(int role) {
                this.role = role;
            }
        }

        public static class ChildTradeVOListDTO {
            private String id;
            private String orderNo;
            private String releaseUserId;
            private int releaseCoinType;
            private String releaseCoinName;
            private String releaseUserCoinAddress;
            private String releaseTime;
            private int money;
            private String dispatchMemberId;
            private String handlerMemberId;
            private int handlerReturnCoinType;
            private String handlerReturnCoinName;
            private String handlerMemberCoinAddress;
            private int type;
            private int status;
            private String createdTime;
            private String createdBy;
            private String updatedTime;
            private String updatedBy;
            private int del;
            private DispatchMemberInfoDTO dispatchMemberInfo;
            private DispatchMemberInfoDTO handlerMemberInfo;
            private ReleaseUserInfoDTO releaseUserInfo;
            private List<StatusInfoListDTO> statusInfoList;
            private String pid;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(String orderNo) {
                this.orderNo = orderNo;
            }

            public String getReleaseUserId() {
                return releaseUserId;
            }

            public void setReleaseUserId(String releaseUserId) {
                this.releaseUserId = releaseUserId;
            }

            public int getReleaseCoinType() {
                return releaseCoinType;
            }

            public void setReleaseCoinType(int releaseCoinType) {
                this.releaseCoinType = releaseCoinType;
            }

            public String getReleaseCoinName() {
                return releaseCoinName;
            }

            public void setReleaseCoinName(String releaseCoinName) {
                this.releaseCoinName = releaseCoinName;
            }

            public String getReleaseUserCoinAddress() {
                return releaseUserCoinAddress;
            }

            public void setReleaseUserCoinAddress(String releaseUserCoinAddress) {
                this.releaseUserCoinAddress = releaseUserCoinAddress;
            }

            public String getReleaseTime() {
                return releaseTime;
            }

            public void setReleaseTime(String releaseTime) {
                this.releaseTime = releaseTime;
            }

            public int getMoney() {
                return money;
            }

            public void setMoney(int money) {
                this.money = money;
            }

            public String getDispatchMemberId() {
                return dispatchMemberId;
            }

            public void setDispatchMemberId(String dispatchMemberId) {
                this.dispatchMemberId = dispatchMemberId;
            }

            public String getHandlerMemberId() {
                return handlerMemberId;
            }

            public void setHandlerMemberId(String handlerMemberId) {
                this.handlerMemberId = handlerMemberId;
            }

            public int getHandlerReturnCoinType() {
                return handlerReturnCoinType;
            }

            public void setHandlerReturnCoinType(int handlerReturnCoinType) {
                this.handlerReturnCoinType = handlerReturnCoinType;
            }

            public String getHandlerReturnCoinName() {
                return handlerReturnCoinName;
            }

            public void setHandlerReturnCoinName(String handlerReturnCoinName) {
                this.handlerReturnCoinName = handlerReturnCoinName;
            }

            public String getHandlerMemberCoinAddress() {
                return handlerMemberCoinAddress;
            }

            public void setHandlerMemberCoinAddress(String handlerMemberCoinAddress) {
                this.handlerMemberCoinAddress = handlerMemberCoinAddress;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getCreatedTime() {
                return createdTime;
            }

            public void setCreatedTime(String createdTime) {
                this.createdTime = createdTime;
            }

            public String getCreatedBy() {
                return createdBy;
            }

            public void setCreatedBy(String createdBy) {
                this.createdBy = createdBy;
            }

            public String getUpdatedTime() {
                return updatedTime;
            }

            public void setUpdatedTime(String updatedTime) {
                this.updatedTime = updatedTime;
            }

            public String getUpdatedBy() {
                return updatedBy;
            }

            public void setUpdatedBy(String updatedBy) {
                this.updatedBy = updatedBy;
            }

            public int getDel() {
                return del;
            }

            public void setDel(int del) {
                this.del = del;
            }

            public DispatchMemberInfoDTO getDispatchMemberInfo() {
                return dispatchMemberInfo;
            }

            public void setDispatchMemberInfo(DispatchMemberInfoDTO dispatchMemberInfo) {
                this.dispatchMemberInfo = dispatchMemberInfo;
            }

            public DispatchMemberInfoDTO getHandlerMemberInfo() {
                return handlerMemberInfo;
            }

            public void setHandlerMemberInfo(DispatchMemberInfoDTO handlerMemberInfo) {
                this.handlerMemberInfo = handlerMemberInfo;
            }

            public ReleaseUserInfoDTO getReleaseUserInfo() {
                return releaseUserInfo;
            }

            public void setReleaseUserInfo(ReleaseUserInfoDTO releaseUserInfo) {
                this.releaseUserInfo = releaseUserInfo;
            }

            public List<StatusInfoListDTO> getStatusInfoList() {
                return statusInfoList;
            }

            public void setStatusInfoList(List<StatusInfoListDTO> statusInfoList) {
                this.statusInfoList = statusInfoList;
            }

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }

            public static class StatusInfoListDTO {
                private int beforeStatus;
                private int afterStatus;
                private String afterStatusInfo;
                private String changeTime;

                public int getBeforeStatus() {
                    return beforeStatus;
                }

                public void setBeforeStatus(int beforeStatus) {
                    this.beforeStatus = beforeStatus;
                }

                public int getAfterStatus() {
                    return afterStatus;
                }

                public void setAfterStatus(int afterStatus) {
                    this.afterStatus = afterStatus;
                }

                public String getAfterStatusInfo() {
                    return afterStatusInfo;
                }

                public void setAfterStatusInfo(String afterStatusInfo) {
                    this.afterStatusInfo = afterStatusInfo;
                }

                public String getChangeTime() {
                    return changeTime;
                }

                public void setChangeTime(String changeTime) {
                    this.changeTime = changeTime;
                }
            }
        }
    }
}
