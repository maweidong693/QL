package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/28 17:15 
 */
public class OrderRequestBody {
    private int pageSize;
    private int pageNum;
    private String status;
    /*private String orderNo;
    private int releaseCoinType;
    private int handlerReturnCoinType;*/

    public OrderRequestBody() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OrderRequestBody(int pageSize, int pageNum, String status) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.status = status;
    }

    public OrderRequestBody(int pageSize, int pageNum) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    /*public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getReleaseCoinType() {
        return releaseCoinType;
    }

    public void setReleaseCoinType(int releaseCoinType) {
        this.releaseCoinType = releaseCoinType;
    }

    public int getHandlerReturnCoinType() {
        return handlerReturnCoinType;
    }

    public void setHandlerReturnCoinType(int handlerReturnCoinType) {
        this.handlerReturnCoinType = handlerReturnCoinType;
    }*/
}
