package com.tencent.qcloud.tim.uikit.modules.contact;

import android.text.TextUtils;

import com.tencent.common.http.ContactListData;
import com.tencent.qcloud.tim.uikit.component.indexlib.IndexBar.bean.BaseIndexPinyinBean;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupMemberInfo;

import java.util.HashMap;
import java.util.Map;

public class ContactItemBean extends BaseIndexPinyinBean {

    public static final String INDEX_STRING_TOP = "↑";
    private String id = "";
    private boolean isTop;//是否是最上面的 不需要被转化成拼音的
    private boolean isSelected;
    private boolean isBlackList;
    private String remark;
    private String nickname;
    private String avatarurl;
    private int unread;
    private Map<String, byte[]> customInfo = new HashMap<>(); //群自定义字段 mute 1禁言  0取消禁言
    private long silenceSeconds;
    private boolean isGroup;
    private boolean isFriend = true;
    private boolean isEnable = true;
    private int isForbidden;
    private int isManger;

    public int getIsManger() {
        return isManger;
    }

    public void setIsManger(int isManger) {
        this.isManger = isManger;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public int getIsForbidden() {
        return isForbidden;
    }

    public void setIsForbidden(int isForbidden) {
        this.isForbidden = isForbidden;
    }

    public ContactItemBean(String id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public long getSilenceSeconds() {
        return silenceSeconds;
    }

    public void setSilenceSeconds(long silenceSeconds) {
        this.silenceSeconds = silenceSeconds;
    }

    public Map<String, byte[]> getCustomInfo() {
        return customInfo;
    }

    public void setCustomInfo(Map<String, byte[]> customInfo) {
        this.customInfo = customInfo;
    }

    public ContactItemBean() {
    }

    public ContactItemBean(String id, int unread) {
        this.id = id;
        this.unread = unread;
    }

    public String getId() {
        return id;
    }

    public ContactItemBean setId(String id) {
        this.id = id;
        return this;
    }

    public boolean isTop() {
        return isTop;
    }

    public ContactItemBean setTop(boolean top) {
        isTop = top;
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
        return id;
    }

    @Override
    public boolean isNeedToPinyin() {
        return !isTop;
    }

    @Override
    public boolean isShowSuspension() {
        return !isTop;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public boolean isBlackList() {
        return isBlackList;
    }

    public void setBlackList(boolean blacklist) {
        isBlackList = blacklist;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public ContactItemBean covertTIMFriend(ContactListData.DataDTO.FriendsDTO friendInfo) {
        if (friendInfo == null) {
            return this;
        }
        setId(friendInfo.getIm_id());
        setRemark(friendInfo.getRemark());
        setNickname(friendInfo.getNick_name());
        setAvatarurl(friendInfo.getFace_url());
        setUnread(friendInfo.getUnread());
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public String getAvatarurl() {
        return avatarurl;
    }

    public void setAvatarurl(String avatarurl) {
        this.avatarurl = avatarurl;
    }

    public ContactItemBean covertTIMGroupBaseInfo(ContactListData.DataDTO.GroupsDTO group) {
        if (group == null) {
            return this;
        }
        setNickname(group.getName());
        setId(group.getId() + "");
        setRemark(group.getName());
        setAvatarurl("");
        setGroup(true);
        return this;
    }

    public ContactItemBean covertTIMGroupMemberFullInfo(GroupMemberInfo member) {
        if (member == null) {
            return this;
        }
        setId(member.getAccount());
        if (TextUtils.isEmpty(member.getNickname())) {
            setRemark(member.getNameCard());
            setNickname(member.getNameCard());
        } else {
            setRemark(member.getNickname());
            setNickname(member.getNickname());
        }
//        setCustomInfo(member.getNickname());
//        setSilenceSeconds(member.get());
        setAvatarurl(member.getIconUrl());
        setGroup(true);
        setIsForbidden(member.getIsForbidden());
        setIsManger(member.getMemberType());
        return this;
    }
}
