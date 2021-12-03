package com.tencent.qcloud.tim.uikit.modules.group.member;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.tencent.common.http.ContactListData;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMGroupInfo;
import com.tencent.imsdk.v2.V2TIMGroupMemberFullInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.base.BaseFragment;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.modules.contact.GroupMemberListView;
import com.tencent.qcloud.tim.uikit.modules.contact.GroupMuteMemberListView;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;
import com.tencent.qcloud.tim.uikit.modules.group.info.InfoData;
import com.tencent.qcloud.tim.uikit.modules.group.interfaces.IGroupMemberLayout;
import com.tencent.qcloud.tim.uikit.utils.CustomInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupMemberMuteLayout extends LinearLayout implements IGroupMemberLayout {

    private static final String TAG = GroupMemberMuteLayout.class.getSimpleName();

    private TitleBarLayout mTitleBar;
    private GroupMuteMemberListView mContactListView;
    private List<String> mInviteMembers = new ArrayList<>();
    private Object mParentLayout;
    private GroupInfo mGroupInfo;
    private int mType;  //1选择新群主   2管理员  3禁言

    public GroupMemberMuteLayout(Context context) {
        super(context);
        init();
    }

    public GroupMemberMuteLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GroupMemberMuteLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.group_member_mute_layout, this);
        mTitleBar = findViewById(R.id.group_invite_title_bar);
        mTitleBar.setTitle("确定", TitleBarLayout.POSITION.RIGHT);
//        mTitleBar.setTitle("选择新群主", TitleBarLayout.POSITION.MIDDLE);
//        mTitleBar.getRightTitle().setTextColor(Color.BLUE);
        mTitleBar.getRightIcon().setVisibility(View.GONE);
        mTitleBar.getRightGroup().setVisibility(GONE);
        mContactListView = findViewById(R.id.group_invite_member_list);
//        mContactListView.loadDataSource(GroupMemberListView.DataSource.GROUP_MEMBER_LIST);
        mContactListView.setSingleSelectMode(true);
        mContactListView.setOnSelectChangeListener(new GroupMuteMemberListView.OnSelectChangedListener() {
            @Override
            public void onSelectChanged(final ContactItemBean contact, final boolean selected) {

                V2TIMGroupMemberFullInfo v2TIMGroupMemberFullInfo = new V2TIMGroupMemberFullInfo();
                v2TIMGroupMemberFullInfo.setUserID(contact.getId());
                String mute = "1";
                String noMute = "0";
                Map<String, byte[]> customInfo = contact.getCustomInfo();
                customInfo.put("mute", selected ? mute.getBytes() : noMute.getBytes());
                v2TIMGroupMemberFullInfo.setCustomInfo(customInfo);
                V2TIMManager.getGroupManager().setGroupMemberInfo(mGroupInfo.getId(), v2TIMGroupMemberFullInfo, new V2TIMCallback() {
                    @Override
                    public void onError(int code, String desc) {

                    }

                    @Override
                    public void onSuccess() {
                        String introduction = mGroupInfo.getGroupIntroduction();
                        HashMap<String, Object> hashMap = CustomInfo.InfoStrToMap(introduction);
                        hashMap.put(selected ? CustomInfo.SINGLE_MUTE : CustomInfo.CANCEL_MUTE, contact.getId());
                        hashMap.put(CustomInfo.LAST_EDIT, selected ? CustomInfo.SINGLE_MUTE : CustomInfo.CANCEL_MUTE);
                        String s = CustomInfo.InfoMapToStr(hashMap);
                        V2TIMGroupInfo v2TIMGroupInfo = new V2TIMGroupInfo();
                        v2TIMGroupInfo.setGroupID(mGroupInfo.getId());
                        v2TIMGroupInfo.setIntroduction(s);
                        V2TIMManager.getGroupManager().setGroupInfo(v2TIMGroupInfo, new V2TIMCallback() {
                            @Override
                            public void onError(int code, String desc) {

                            }

                            @Override
                            public void onSuccess() {

                            }
                        });

                    }
                });
            }
        });
    }

    public void setDataSource(GroupInfo groupInfo) {
        mGroupInfo = groupInfo;
        if (mType == 2) {
            String introduction = mGroupInfo.getGroupIntroduction();
            HashMap<String, Object> hashMap = CustomInfo.InfoStrToMap(introduction);
            List<String> list = (List<String>) hashMap.get(CustomInfo.GROUP_MANAGEMENT);
            if (list != null && list.size() > 0) {
                mInviteMembers.addAll(list);
            }
        }
        if (mContactListView != null) {
            mContactListView.setGroupInfo(mGroupInfo);
            mContactListView.loadDataSource(GroupMemberListView.DataSource.GROUP_MEMBER_LIST);
        }
    }

    public TitleBarLayout getTitleBar() {
        return mTitleBar;
    }

    @Override
    public void setParentLayout(Object parent) {
        mParentLayout = parent;
    }

    private void finish() {
        if (mParentLayout instanceof Activity) {
            ((Activity) mParentLayout).finish();
        } else if (mParentLayout instanceof BaseFragment) {
            ((BaseFragment) mParentLayout).backward();
        }
    }

    public void initTitle(InfoData infoData) {
        if (infoData != null) {
            String title = infoData.getTitle();
            mType = infoData.getType();
            mContactListView.setType(mType);
            mTitleBar.setTitle(title, TitleBarLayout.POSITION.MIDDLE);
        }

    }

    @Override
    public void setDataSource(GroupInfo dataSource, ContactListData.DataDTO list) {

    }
}
