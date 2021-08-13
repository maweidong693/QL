package com.tencent.qcloud.tim.uikit.modules.group.info;

import android.text.TextUtils;

import com.tencent.qcloud.tim.uikit.component.indexlib.IndexBar.bean.BaseIndexPinyinBean;

import java.io.Serializable;

public class GroupMemberInfo extends BaseIndexPinyinBean implements Serializable {

    private String iconUrl;
    private String account;
    private String signature;
    private String location;
    private String birthday;
    private String nameCard;
    private String remark;
    private String nickname;
    private String online_status;
    private boolean isTopChat;
    private boolean isFriend;
    private long joinTime;
    private long tinyId;
    private int memberType;
    private int isForbidden;

    @Override
    public String toString() {
        return "GroupMemberInfo{" +
                "iconUrl='" + iconUrl + '\'' +
                ", account='" + account + '\'' +
                ", signature='" + signature + '\'' +
                ", location='" + location + '\'' +
                ", birthday='" + birthday + '\'' +
                ", nameCard='" + nameCard + '\'' +
                ", remark='" + remark + '\'' +
                ", nickname='" + nickname + '\'' +
                ", online_status='" + online_status + '\'' +
                ", isTopChat=" + isTopChat +
                ", isFriend=" + isFriend +
                ", joinTime=" + joinTime +
                ", tinyId=" + tinyId +
                ", memberType=" + memberType +
                '}';
    }

    public int getIsForbidden() {
        return isForbidden;
    }

    public void setIsForbidden(int isForbidden) {
        this.isForbidden = isForbidden;
    }

    public String getOnline_status() {
        return online_status;
    }

    public void setOnline_status(String online_status) {
        this.online_status = online_status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setNameCard(String nameCard) {
        this.nameCard = nameCard;
    }

    public String getNameCard() {
        return nameCard;
    }

    public boolean isTopChat() {
        return isTopChat;
    }

    public void setTopChat(boolean topChat) {
        isTopChat = topChat;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public long getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(long joinTime) {
        this.joinTime = joinTime;
    }

    public long getTinyId() {
        return tinyId;
    }

    public void setTinyId(long tinyId) {
        this.tinyId = tinyId;
    }

    public int getMemberType() {
        return memberType;
    }

    public void setMemberType(int memberType) {
        this.memberType = memberType;
    }

    public GroupMemberInfo covertTIMGroupMemberInfo(GroupMemberData.DataDTO info) {
//        setJoinTime(info.getJoinTime());
        setMemberType(info.getRole());
        setAccount(info.getId());
        setNameCard(info.getNickName());
        setRemark(info.getNickName());
        setNickname(info.getNickName());
        setIconUrl(info.getAvator());
        setIsForbidden(info.getIsForbidden());
        return this;
    }

    @Override
    public String getTarget() {
        if (!TextUtils.isEmpty(remark)) {
            return remark;
        }
        if (!TextUtils.isEmpty(nickname)) {
            return nickname;
        }
        return account;
    }
}
