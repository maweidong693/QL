package com.weiwu.ql.main.contact.group.member;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.View;

import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfoData;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupMemberData;
import com.tencent.qcloud.tim.uikit.modules.group.member.IGroupMemberRouter;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseFragment;
import com.weiwu.ql.main.contact.group.GroupContract;

/**
 * 群成员管理
 */
public class GroupMemberManagerFragment extends BaseFragment implements GroupContract.IGroupMemberView {

    private GroupMemberManagerLayout mMemberLayout;
    private GroupInfo mGroupInfo;

    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.group_fragment_members, container, false);
        mMemberLayout = mBaseView.findViewById(R.id.group_member_grid_layout);
        init();
        return mBaseView;
    }*/

    @Override
    protected int getLayoutId() {
        return R.layout.group_fragment_members;
    }

    @Override
    public void onViewCreated(View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMemberLayout = view.findViewById(R.id.group_member_grid_layout);
        init();
    }

    private void init() {
        mGroupInfo = (GroupInfo) getArguments().getSerializable(TUIKitConstants.Group.GROUP_INFO);
        mMemberLayout.setDataSource(mGroupInfo);

        mMemberLayout.getTitleBar().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backward();
            }
        });
        mMemberLayout.setRouter(new IGroupMemberRouter() {

            @Override
            public void forwardListMember(GroupInfo info) {

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

            }

            @Override
            public void forwardGroupManager(GroupInfo info) {

            }

            @Override
            public void forwardGroupMuteManager(GroupInfo info) {

            }

            @Override
            public void forwardGroupInvitation(GroupInfo info) {

            }
        });

    }

    @Override
    public void groupMemberReceive(GroupInfoData data) {
        if (data != null && data.getData() != null) {

        }
    }

    @Override
    public void onFail(String msg, int code) {

    }

    @Override
    public void setPresenter(GroupContract.IGroupMemberPresenter presenter) {

    }

    @Override
    public Activity getActivityObject() {
        return null;
    }
}
