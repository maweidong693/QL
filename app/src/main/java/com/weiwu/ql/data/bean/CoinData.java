package com.weiwu.ql.data.bean;

import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/28 11:25 
 */
public class CoinData {
    private int code;
    private String message;
    private List<CoinData.CoinDto> data;

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

    public List<CoinDto> getData() {
        return data;
    }

    public void setData(List<CoinDto> data) {
        this.data = data;
    }

    public static class CoinDto {
        private int id;
        private int coinId;
        private String coinName;
        private String coinAddress;
        private String remark;

        public CoinDto() {
        }

        public CoinDto(int id, int coinId, String coinName, String coinAddress, String remark) {
            this.id = id;
            this.coinId = coinId;
            this.coinName = coinName;
            this.coinAddress = coinAddress;
            this.remark = remark;
        }

        public CoinDto(int coinId, String coinName, String coinAddress, String remark) {
            this.coinId = coinId;
            this.coinName = coinName;
            this.coinAddress = coinAddress;
            this.remark = remark;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

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
    }


}
