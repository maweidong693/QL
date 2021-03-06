package com.tencent.qcloud.tim.uikit.modules.group.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.base.BaseFragment;
import com.tencent.qcloud.tim.uikit.modules.group.apply.GroupInvitationFragment;
import com.tencent.qcloud.tim.uikit.modules.group.member.GroupMemberDeleteFragment;
import com.tencent.qcloud.tim.uikit.modules.group.member.GroupMemberInviteFragment;
import com.tencent.qcloud.tim.uikit.modules.group.member.GroupMemberManagerFragment;
import com.tencent.qcloud.tim.uikit.modules.group.member.GroupMemberMuteFragment;
import com.tencent.qcloud.tim.uikit.modules.group.member.GroupMemberTransferFragment;
import com.tencent.qcloud.tim.uikit.modules.group.member.IGroupMemberRouter;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;

import androidx.annotation.Nullable;


public class GroupInfoFragment extends BaseFragment {

    private View mBaseView;
    private GroupInfoLayout mGroupInfoLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.group_info_fragment, container, false);
        initView();
        return mBaseView;    }

    private void initView() {
        mGroupInfoLayout = mBaseView.findViewById(R.id.group_info_layout);
        mGroupInfoLayout.setGroupId(getArguments().getString(TUIKitConstants.Group.GROUP_ID));
        mGroupInfoLayout.setRouter(new IGroupMemberRouter() {
            @Override
            public void forwardListMember(GroupInfo info) {
                GroupMemberManagerFragment fragment = new GroupMemberManagerFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(TUIKitConstants.Group.GROUP_INFO, info);
                fragment.setArguments(bundle);
                forward(fragment, false);
            }

            @Override
            public void forwardAddMember(GroupInfo info) {
                GroupMemberInviteFragment fragment = new GroupMemberInviteFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(TUIKitConstants.Group.GROUP_INFO, info);
                fragment.setArguments(bundle);
                forward(fragment, false);
            }

            @Override
            public void forwardDeleteMember(GroupInfo info) {
                GroupMemberDeleteFragment fragment = new GroupMemberDeleteFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(TUIKitConstants.Group.GROUP_INFO, info);
                fragment.setArguments(bundle);
                forward(fragment, false);
            }

            @Override
            public void forwardTransFerMember(GroupInfo info) {
                GroupMemberTransferFragment fragment = new GroupMemberTransferFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(TUIKitConstants.Group.GROUP_INFO, info);
                bundle.putSerializable("info",new InfoData("???????????????",1));
                fragment.setArguments(bundle);
                forward(fragment, false);
            }

            @Override
            public void forwardGroupManager(GroupInfo info) {
                GroupMemberTransferFragment fragment = new GroupMemberTransferFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(TUIKitConstants.Group.GROUP_INFO, info);
                bundle.putSerializable("info",new InfoData("????????????",2));
                fragment.setArguments(bundle);
                forward(fragment, false);

            }

            @Override
            public void forwardGroupMuteManager(GroupInfo info) {
                GroupMemberMuteFragment fragment = new GroupMemberMuteFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(TUIKitConstants.Group.GROUP_INFO, info);
                bundle.putSerializable("info",new InfoData("????????????",3));
                fragment.setArguments(bundle);
                forward(fragment, false);

            }

            @Override
            public void forwardGroupInvitation(GroupInfo info) {
                GroupInvitationFragment fragment = new GroupInvitationFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(TUIKitConstants.Group.GROUP_INFO, info);
                fragment.setArguments(bundle);
                forward(fragment, false);
            }
        });

    }
}
