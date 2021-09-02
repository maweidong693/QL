package com.weiwu.ql.data.bean;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/18 14:42 
 */
public class FunctionData {
    private String type;
    private String receiverType;
    private String receiverId;
    private String messageId;
    private FunctionArgDTO functionArg;

    public FunctionData(String type, String receiverType, String receiverId, String messageId, FunctionArgDTO functionArg) {
        this.type = type;
        this.receiverType = receiverType;
        this.receiverId = receiverId;
        this.messageId = messageId;
        this.functionArg = functionArg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public FunctionArgDTO getFunctionArg() {
        return functionArg;
    }

    public void setFunctionArg(FunctionArgDTO functionArg) {
        this.functionArg = functionArg;
    }

    public static class FunctionArgDTO {
        private int functionTab;
        private String functionMsg;

        public FunctionArgDTO(int functionTab, String functionMsg) {
            this.functionTab = functionTab;
            this.functionMsg = functionMsg;
        }

        public int getFunctionTab() {
            return functionTab;
        }

        public void setFunctionTab(int functionTab) {
            this.functionTab = functionTab;
        }

        public String getFunctionMsg() {
            return functionMsg;
        }

        public void setFunctionMsg(String functionMsg) {
            this.functionMsg = functionMsg;
        }
    }
}
