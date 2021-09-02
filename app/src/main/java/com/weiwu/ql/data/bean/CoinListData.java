package com.weiwu.ql.data.bean;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/28 14:36 
 */
public class CoinListData {
    private int coinId;
    private String coinName;
    private String coinAddress;
    private String remark;

    public int getCoinId() {
        return coinId;
    }

    public void setCoinId(int coinId) {
        this.coinId = coinId;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getCoinAddress() {
        return coinAddress;
    }

    public void setCoinAddress(String coinAddress) {
        this.coinAddress = coinAddress;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public CoinListData(int coinId, String coinName, String coinAddress, String remark) {
        this.coinId = coinId;
        this.coinName = coinName;
        this.coinAddress = coinAddress;
        this.remark = remark;
    }
}
