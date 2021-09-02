package com.weiwu.ql.data.request;

import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/30 13:26 
 */
public class DistributeRequestBody {
    private String tradeId;
    private List<ChildTradeParamData> childTradeParamVOList;

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public List<ChildTradeParamData> getChildTradeParamVOList() {
        return childTradeParamVOList;
    }

    public void setChildTradeParamVOList(List<ChildTradeParamData> childTradeParamVOList) {
        this.childTradeParamVOList = childTradeParamVOList;
    }

    public DistributeRequestBody() {
    }

    public DistributeRequestBody(String tradeId, List<ChildTradeParamData> childTradeParamVOList) {
        this.tradeId = tradeId;
        this.childTradeParamVOList = childTradeParamVOList;
    }

    public static class ChildTradeParamData {
        private String memberId;
        private int money;

        public ChildTradeParamData() {
        }

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public ChildTradeParamData(String memberId, int money) {
            this.memberId = memberId;
            this.money = money;
        }
    }

}
