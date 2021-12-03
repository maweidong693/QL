package com.tencent.qcloud.tim.uikit.modules.group.member;


import com.tencent.qcloud.tim.uikit.modules.group.info.GroupMemberInfo;

public interface IGroupMemberChangedCallback {
    void onMemberRemoved(GroupMemberInfo memberInfo);
}
