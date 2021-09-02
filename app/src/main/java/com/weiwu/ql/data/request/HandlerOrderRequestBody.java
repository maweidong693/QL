package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/30 13:22 
 */
public class HandlerOrderRequestBody {
    private String handlerMemberCoinAddress;
    private String tradeId;

    public HandlerOrderRequestBody(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getHandlerMemberCoinAddress() {
        return handlerMemberCoinAddress;
    }

    public void setHandlerMemberCoinAddress(String handlerMemberCoinAddress) {
        this.handlerMemberCoinAddress = handlerMemberCoinAddress;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public HandlerOrderRequestBody(String handlerMemberCoinAddress, String tradeId) {
        this.handlerMemberCoinAddress = handlerMemberCoinAddress;
        this.tradeId = tradeId;
    }
}
