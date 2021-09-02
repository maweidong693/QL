package com.weiwu.ql.data.bean;

import java.io.Serializable;
import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/28 16:19 
 */
public class OrderData {

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
        private int pageNum;
        private int pageSize;
        private int size;
        private int startRow;
        private int endRow;
        private int total;
        private int pages;
        private List<ListDTO> list;
        private int prePage;
        private int nextPage;
        private boolean isFirstPage;
        private boolean isLastPage;
        private boolean hasPreviousPage;
        private boolean hasNextPage;
        private int navigatePages;
        private List<Integer> navigatepageNums;
        private int navigateFirstPage;
        private int navigateLastPage;
        private int firstPage;
        private int lastPage;

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getStartRow() {
            return startRow;
        }

        public void setStartRow(int startRow) {
            this.startRow = startRow;
        }

        public int getEndRow() {
            return endRow;
        }

        public void setEndRow(int endRow) {
            this.endRow = endRow;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public List<ListDTO> getList() {
            return list;
        }

        public void setList(List<ListDTO> list) {
            this.list = list;
        }

        public int getPrePage() {
            return prePage;
        }

        public void setPrePage(int prePage) {
            this.prePage = prePage;
        }

        public int getNextPage() {
            return nextPage;
        }

        public void setNextPage(int nextPage) {
            this.nextPage = nextPage;
        }

        public boolean isIsFirstPage() {
            return isFirstPage;
        }

        public void setIsFirstPage(boolean isFirstPage) {
            this.isFirstPage = isFirstPage;
        }

        public boolean isIsLastPage() {
            return isLastPage;
        }

        public void setIsLastPage(boolean isLastPage) {
            this.isLastPage = isLastPage;
        }

        public boolean isHasPreviousPage() {
            return hasPreviousPage;
        }

        public void setHasPreviousPage(boolean hasPreviousPage) {
            this.hasPreviousPage = hasPreviousPage;
        }

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public int getNavigatePages() {
            return navigatePages;
        }

        public void setNavigatePages(int navigatePages) {
            this.navigatePages = navigatePages;
        }

        public List<Integer> getNavigatepageNums() {
            return navigatepageNums;
        }

        public void setNavigatepageNums(List<Integer> navigatepageNums) {
            this.navigatepageNums = navigatepageNums;
        }

        public int getNavigateFirstPage() {
            return navigateFirstPage;
        }

        public void setNavigateFirstPage(int navigateFirstPage) {
            this.navigateFirstPage = navigateFirstPage;
        }

        public int getNavigateLastPage() {
            return navigateLastPage;
        }

        public void setNavigateLastPage(int navigateLastPage) {
            this.navigateLastPage = navigateLastPage;
        }

        public int getFirstPage() {
            return firstPage;
        }

        public void setFirstPage(int firstPage) {
            this.firstPage = firstPage;
        }

        public int getLastPage() {
            return lastPage;
        }

        public void setLastPage(int lastPage) {
            this.lastPage = lastPage;
        }

        public static class ListDTO implements Serializable {
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
            private String overTime;
            private String createdTime;
            private String createdBy;
            private String updatedTime;
            private String updatedBy;
            private int del;
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

            public String getOverTime() {
                return overTime;
            }

            public void setOverTime(String overTime) {
                this.overTime = overTime;
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

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }
        }
    }
}
