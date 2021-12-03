package com.weiwu.ql.main.contact.group.member;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfoData;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupMemberData;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupMemberInfo;
import com.tencent.qcloud.tim.uikit.modules.group.info.InfoData;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.weiwu.ql.AppConstant;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.base.BaseFragment;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.repositories.GroupRepository;
import com.weiwu.ql.data.request.ForbiddenRequestBody;
import com.weiwu.ql.data.request.GroupInfoRequestBody;
import com.weiwu.ql.main.contact.group.GroupContract;
import com.weiwu.ql.utils.SPUtils;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class GroupMemberMuteFragment extends BaseFragment implements GroupContract.IGroupForbiddenView {

    private GroupMemberMuteLayout mInviteLayout;
    private GroupContract.IGroupForbiddenPresenter mPresenter;
    private GroupInfo mGroupInfo;

    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.group_fragment_mute_members, container, false);
        mInviteLayout = mBaseView.findViewById(R.id.group_member_invite_layout);
        mInviteLayout.setParentLayout(this);
        init();
        return mBaseView;
    }*/

    @Override
    protected int getLayoutId() {
        return R.layout.group_fragment_mute_members;
    }

    @Override
    public void onViewCreated(View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGroupInfo = (GroupInfo) getArguments().getSerializable(TUIKitConstants.Group.GROUP_INFO);
        setPresenter(new GroupForbiddenPresenter(GroupRepository.getInstance()));
        mInviteLayout = view.findViewById(R.id.group_member_invite_layout);
        mInviteLayout.setParentLayout(this);
        init();
    }

    private void init() {
        List<GroupMemberInfo> members = new ArrayList<>();
        for (int i = 0; i < mGroupInfo.getMemberDetails().size(); i++) {
            GroupMemberInfo memberInfo = mGroupInfo.getMemberDetails().get(i);
            if (!memberInfo.getAccount().equals(SPUtils.getValue(AppConstant.USER, AppConstant.USER_ID))) {
                members.add(memberInfo);
            }
        }
        mGroupInfo.setMemberDetails(members);
        mInviteLayout.setDataSource(mGroupInfo);
        mInviteLayout.getTitleBar().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backward();
            }
        });
        mInviteLayout.setMuteListener(new GroupMemberMuteLayout.IMuteListener() {
            @Override
            public void mute(ContactItemBean contact, boolean selected) {
                List<String> list = new ArrayList<>();
                list.add(contact.getId());
                if (selected) {
                    mPresenter.forbiddenMember(new ForbiddenRequestBody(mGroupInfo.getGroupId(), listToString(list, ','), "1"));
                } else {
                    mPresenter.cancelForbiddenMember(new ForbiddenRequestBody(mGroupInfo.getGroupId(), listToString(list, ','), "0"));
                }
            }
        });
    }

    public String listToString(List list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(separator);
        }
        return list.isEmpty() ? "" : sb.toString().substring(0, sb.toString().length() - 1);
    }

    @Override
    public void groupMemberReceive(GroupInfoData data) {
        if (data.getData() != null) {
            List<GroupMemberInfo> members = new ArrayList<>();
            for (int i = 0; i < data.getData().getGroupUser().size(); i++) {
                GroupMemberInfo memberInfo = new GroupMemberInfo();
                GroupInfoData.DataDTO.GroupUserDTO dataDTO = data.getData().getGroupUser().get(i);
                memberInfo.covertTIMGroupMemberInfo(dataDTO);
                if (!dataDTO.getMember_id().equals(SPUtils.getValue(AppConstant.USER, AppConstant.USER_ID))) {
                    members.add(memberInfo);
                }
            }
            mGroupInfo.setMemberDetails(members);
            mInviteLayout.setDataSource(mGroupInfo);
        }
    }

    @Override
    public void forbiddenReceive(HttpResult data) {
        showToast(data.getMsg());
    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
        if (code == 10001) {
            MyApplication.getInstance().loginAgain();
        }
    }

    @Override
    public void setPresenter(GroupContract.IGroupForbiddenPresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return getActivity();
    }
}
