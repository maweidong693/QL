package com.tencent.qcloud.tim.uikit.modules.group.info;

import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;

import java.util.ArrayList;
import java.util.List;


public class GroupInfo extends ChatInfo {

    private String groupType;
    private int memberCount;
    private String groupName;
    private String notice;
    private String groupIntroduction;
    private List<GroupMemberInfo> memberDetails;
    private int joinType;
    private String owner;
    private boolean isManger;
    private boolean isOwner;
    private int isExamine;
    private int isAllForbidden;
    private int isAddFriend;

    public String getOwner() {
        return owner;
    }

    public int getIsAddFriend() {
        return isAddFriend;
    }

    public void setIsAddFriend(int isAddFriend) {
        this.isAddFriend = isAddFriend;
    }

    public int getIsExamine() {
        return isExamine;
    }

    public void setIsExamine(int isExamine) {
        this.isExamine = isExamine;
    }

    public int getIsAllForbidden() {
        return isAllForbidden;
    }

    public void setIsAllForbidden(int isAllForbidden) {
        this.isAllForbidden = isAllForbidden;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public boolean isManger() {
        return isManger;
    }

    public void setManger(boolean manger) {
        isManger = manger;
    }

    public boolean isOwner() {
        return isOwner;
    }

    @Override
    public String toString() {
        return "GroupInfo{" +
                "groupType='" + groupType + '\'' +
                ", memberCount=" + memberCount +
                ", groupName='" + groupName + '\'' +
                ", notice='" + notice + '\'' +
                ", groupIntroduction='" + groupIntroduction + '\'' +
                ", memberDetails=" + memberDetails +
                ", joinType=" + joinType +
                ", owner='" + owner + '\'' +
                '}';
    }

    public GroupInfo() {
        setType(V2TIMConversation.V2TIM_GROUP);
    }

    /**
     * 获取群名称
     *
     * @return
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * 设置群名称
     *
     * @param groupName
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * 获取群公告
     *
     * @return
     */
    public String getNotice() {
        return notice;
    }

    /**
     * 设置群公告
     *
     * @param signature
     */
    public void setNotice(String signature) {
        this.notice = signature;
    }

    /**
     * 获取群简介
     */
    public String getGroupIntroduction() {
        return groupIntroduction;
    }

    /**
     * 设置群简介
     *
     * @param groupIntroduction
     */
    public void setGroupIntroduction(String groupIntroduction) {
        this.groupIntroduction = groupIntroduction;
    }

    /**
     * 回去加群验证方式
     *
     * @return
     */
    public int getJoinType() {
        return joinType;
    }

    /**
     * 设置加群验证方式
     *
     * @param joinType
     */
    public void setJoinType(int joinType) {
        this.joinType = joinType;
    }

    /**
     * 获取群类型，Public/Private/ChatRoom
     *
     * @return
     */
    public String getGroupType() {
        return groupType;
    }

    /**
     * 设置群类型
     *
     * @param groupType
     */
    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    /**
     * 获取成员详细信息
     *
     * @return
     */
    public List<GroupMemberInfo> getMemberDetails() {
        return memberDetails;
    }

    /**
     * 设置成员详细信息
     *
     * @param memberDetails
     */
    public void setMemberDetails(List<GroupMemberInfo> memberDetails) {
        this.memberDetails = memberDetails;
    }

    /**
     * 获取群成员数量
     *
     * @return
     */
    public int getMemberCount() {
        if (memberDetails != null) {
            return memberDetails.size();
        }
        return memberCount;
    }

    /**
     * 设置群成员数量
     *
     * @param memberCount
     */
    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    /**
     * 返回是否是群管理
     *
     * @return
     */
    public boolean isManager() {

        return isManger();
    }

    /**
     * 设置是否是群主
     *
     * @param owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * 从SDK转化为TUIKit的群信息bean
     *
     * @param infoResult
     * @return
     */
    public GroupInfo covertTIMGroupDetailInfo(GroupInfoData infoResult) {
        if (infoResult.getCode() != 200) {
            return this;
        }
        GroupInfoData.DataDTO data = infoResult.getData();
        setChatName(data.getGroup_name());
        setGroupName(data.getGroup_name());
        setId(data.getId());
        setGroupId(data.getGroup_id());
        setNotice(data.getNotification());
        setGroupIntroduction(data.getIntroduction());
        setMemberCount(data.getGroup_user_count());
        List<GroupMemberInfo> members = new ArrayList<>();
        List<GroupInfoData.DataDTO.GroupUserDTO> groupUser = infoResult.getData().getGroupUser();
        for (int i = 0; i < groupUser.size(); i++) {
            GroupMemberInfo memberInfo = new GroupMemberInfo();
            GroupMemberInfo info = memberInfo.covertTIMGroupMemberInfo(groupUser.get(i));
            members.add(info);
        }
        setMemberDetails(members);
        setGroupType("群聊");
        setOwner(data.getGroup_name());
        setManger(data.getGroup_role() == 1);
        setOwner(data.getGroup_role() == 2);
        setIsExamine(data.getIs_join_check());
        setIsAllForbidden(data.getIs_ban_say());
        setIsAddFriend(data.getIs_add_friend());
        setTopChat(data.getIs_top() == 1);
        return this;
    }
}
