package com.weiwu.ql.main.contact.chat.modle;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/9/1 16:39 
 */
public class NoticeOrderData {

    private String id;
    private String pId;
    private String orderNo;
    private String releaseUserId;
    private int releaseCoinType;
    private String releaseCoinName;
    private String releaseUserCoinAddress;
    private ReleaseTimeDTO releaseTime;
    private int money;
    private String dispatchMemberId;
    private String handlerMemberId;
    private int handlerReturnCoinType;
    private String handlerReturnCoinName;
    private int type;
    private int status;
    private CreatedTimeDTO createdTime;
    private String createdBy;
    private UpdatedTimeDTO updatedTime;
    private String updatedBy;
    private int del;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPId() {
        return pId;
    }

    public void setPId(String pId) {
        this.pId = pId;
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

    public ReleaseTimeDTO getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(ReleaseTimeDTO releaseTime) {
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

    public CreatedTimeDTO getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(CreatedTimeDTO createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public UpdatedTimeDTO getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(UpdatedTimeDTO updatedTime) {
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

    public static class ReleaseTimeDTO {
        private DateDTO date;
        private TimeDTO time;

        public DateDTO getDate() {
            return date;
        }

        public void setDate(DateDTO date) {
            this.date = date;
        }

        public TimeDTO getTime() {
            return time;
        }

        public void setTime(TimeDTO time) {
            this.time = time;
        }

        public static class DateDTO {
            private int year;
            private int month;
            private int day;

            public int getYear() {
                return year;
            }

            public void setYear(int year) {
                this.year = year;
            }

            public int getMonth() {
                return month;
            }

            public void setMonth(int month) {
                this.month = month;
            }

            public int getDay() {
                return day;
            }

            public void setDay(int day) {
                this.day = day;
            }
        }

        public static class TimeDTO {
            private int hour;
            private int minute;
            private int second;
            private int nano;

            public int getHour() {
                return hour;
            }

            public void setHour(int hour) {
                this.hour = hour;
            }

            public int getMinute() {
                return minute;
            }

            public void setMinute(int minute) {
                this.minute = minute;
            }

            public int getSecond() {
                return second;
            }

            public void setSecond(int second) {
                this.second = second;
            }

            public int getNano() {
                return nano;
            }

            public void setNano(int nano) {
                this.nano = nano;
            }
        }
    }

    public static class CreatedTimeDTO {
        private ReleaseTimeDTO.DateDTO date;
        private ReleaseTimeDTO.TimeDTO time;

        public ReleaseTimeDTO.DateDTO getDate() {
            return date;
        }

        public void setDate(ReleaseTimeDTO.DateDTO date) {
            this.date = date;
        }

        public ReleaseTimeDTO.TimeDTO getTime() {
            return time;
        }

        public void setTime(ReleaseTimeDTO.TimeDTO time) {
            this.time = time;
        }
    }

    public static class UpdatedTimeDTO {
        private ReleaseTimeDTO.DateDTO date;
        private ReleaseTimeDTO.TimeDTO time;

        public ReleaseTimeDTO.DateDTO getDate() {
            return date;
        }

        public void setDate(ReleaseTimeDTO.DateDTO date) {
            this.date = date;
        }

        public ReleaseTimeDTO.TimeDTO getTime() {
            return time;
        }

        public void setTime(ReleaseTimeDTO.TimeDTO time) {
            this.time = time;
        }
    }
}
