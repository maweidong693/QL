package com.weiwu.ql.main.contact.group;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;
import com.weiwu.ql.base.BaseFragment;
import com.tencent.qcloud.tim.uikit.modules.group.info.InfoData;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupMemberData;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupMemberInfo;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.main.contact.group.member.GroupMemberDeleteFragment;
import com.weiwu.ql.main.contact.group.member.GroupMemberInviteFragment;
import com.weiwu.ql.main.contact.group.member.GroupMemberManagerFragment;
import com.weiwu.ql.main.contact.group.member.GroupMemberMuteFragment;
import com.weiwu.ql.main.contact.group.member.GroupMemberTransferFragment;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.weiwu.ql.R;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfoData;
import com.weiwu.ql.data.repositories.GroupRepository;

import java.util.ArrayList;
import java.util.List;


public class GroupInfoFragment extends BaseFragment implements GroupContract.IGroupView {

    private GroupInfoLayout mGroupInfoLayout;
    private GroupContract.IGroupPresenter mPresenter;
    private GroupInfo groupInfo;

    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.group_info_fragment, container, false);
        setPresenter(new GroupPresenter(GroupRepository.getInstance()));
        initView();
        return mBaseView;
    }*/

    @Override
    protected int getLayoutId() {
        return R.layout.group_info_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPresenter(new GroupPresenter(GroupRepository.getInstance()));
        initView(view);
    }

    private static final String TAG = "GroupInfoFragment";

    private void initView(View view) {
        mGroupInfoLayout = view.findViewById(R.id.group_info_layout);
        mGroupInfoLayout.setGroupId(getArguments().getString(TUIKitConstants.Group.GROUP_ID));
        mPresenter.getGroupInfo(getArguments().getString(TUIKitConstants.Group.GROUP_ID));
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
//                Log.d(TAG, "forwardTransFerMember: ccc===" + info.getMemberDetails().size());
                GroupMemberTransferFragment fragment = new GroupMemberTransferFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(TUIKitConstants.Group.GROUP_INFO, info);
                bundle.putSerializable("info", new InfoData("选择新群主", 1));
                fragment.setArguments(bundle);
                forward(fragment, false);
            }

            @Override
            public void forwardGroupManager(GroupInfo info) {
                GroupMemberTransferFragment fragment = new GroupMemberTransferFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(TUIKitConstants.Group.GROUP_INFO, info);
                bundle.putSerializable("info", new InfoData("群管理员", 2));
                fragment.setArguments(bundle);
                forward(fragment, false);

            }

            @Override
            public void forwardGroupMuteManager(GroupInfo info) {
                GroupMemberMuteFragment fragment = new GroupMemberMuteFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(TUIKitConstants.Group.GROUP_INFO, info);
                bundle.putSerializable("info", new InfoData("禁言管理", 3));
                fragment.setArguments(bundle);
                forward(fragment, false);

            }

            @Override
            public void forwardGroupInvitation(GroupInfo info) {
                /*GroupInvitationFragment fragment = new GroupInvitationFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(TUIKitConstants.Group.GROUP_INFO, info);
                fragment.setArguments(bundle);
                forward(fragment, false);*/
            }
        });

        mGroupInfoLayout.setQuitGroupListener(new GroupInfoLayout.IQuitGroupListener() {
            @Override
            public void quitGroup(int type, String groupId) {
                if (type == 1) {
                    mPresenter.dissolveGroup(groupId);
                } else if (type == 2) {
                    mPresenter.signGroup(groupId);
                }
            }
        });

    }

    @Override
    public void groupInfoReceive(GroupInfoData data) {
        if (data != null) {
            groupInfo = new GroupInfo();
            List<GroupMemberInfo> member = new ArrayList<>();
            List<GroupMemberData.DataDTO> list = data.getData().getMemberGroupResultVOList();
            for (int i = 0; i < list.size(); i++) {
                GroupMemberInfo groupMemberInfo = new GroupMemberInfo();
                member.add(groupMemberInfo.covertTIMGroupMemberInfo(list.get(i)));
            }
            groupInfo.covertTIMGroupDetailInfo(data);
            groupInfo.setMemberDetails(member);
            mGroupInfoLayout.setGroupInfo(groupInfo);
        }
    }

    @Override
    public void onSuccess(HttpResult data) {
        showToast(data.getMessage());
        getActivity().setResult(1002);
        getActivity().finish();
    }

    @Override
    public void onFail(String msg, int code) {

    }

    @Override
    public void setPresenter(GroupContract.IGroupPresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return getActivity();
    }
}
