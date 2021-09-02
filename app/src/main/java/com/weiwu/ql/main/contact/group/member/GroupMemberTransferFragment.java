package com.weiwu.ql.main.contact.group.member;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;
import com.tencent.qcloud.tim.uikit.modules.group.info.InfoData;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.base.BaseFragment;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.repositories.GroupRepository;
import com.weiwu.ql.data.request.GroupOwnerRequestBody;
import com.weiwu.ql.data.request.InviteOrDeleteRequestBody;
import com.weiwu.ql.main.contact.group.GroupContract;

import androidx.annotation.Nullable;

import java.util.List;


public class GroupMemberTransferFragment extends BaseFragment implements GroupContract.IGroupMangerView {

    private GroupMemberTransferLayout mInviteLayout;
    private GroupInfo mGroupInfo;
    private InfoData mInfo;
    private GroupContract.IGroupMangerPresenter mPresenter;

    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.group_fragment_transfer_members, container, false);
        mInviteLayout = mBaseView.findViewById(R.id.group_member_invite_layout);
        mInviteLayout.setParentLayout(this);
        init();
        return mBaseView;
    }*/

    @Override
    protected int getLayoutId() {
        return R.layout.group_fragment_transfer_members;
    }

    @Override
    public void onViewCreated(View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGroupInfo = (GroupInfo) getArguments().getSerializable(TUIKitConstants.Group.GROUP_INFO);
        mInfo = (InfoData) getArguments().getSerializable("info");
        setPresenter(new GroupMangerPresenter(GroupRepository.getInstance()));
        mInviteLayout = view.findViewById(R.id.group_member_invite_layout);
        init();
//        mInviteLayout.setParentLayout(this);
    }

    private static final String TAG = "GroupMemberTransferFrag";

    private void init() {
        mInviteLayout.initTitle(mInfo);
        mInviteLayout.setDataSource(mGroupInfo);
        mInviteLayout.getTitleBar().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backward();
            }
        });
        int type = mInfo.getType();
        mInviteLayout.setMangerSelectItemListener(new GroupMemberTransferLayout.IMangerSelectItemListener() {
            @Override
            public void select(List<String> list) {
                if (type == 1) {
                    if (list != null && list.size() > 0) {
                        mPresenter.changGroupOwner(mGroupInfo.getId(), list.get(0));
                    }else {
                        showToast("请选择成员！");
                    }
                } else if (type == 2) {
                    mPresenter.changeGroupManger(new InviteOrDeleteRequestBody(mGroupInfo.getId(), list));
                }
            }
        });
    }

    @Override
    public void onSuccess(HttpResult data) {
        showToast(data.getMessage());
        getActivity().finish();
    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
        if (code == 10001) {
            MyApplication.getInstance().loginAgain();
        }

    }

    @Override
    public void setPresenter(GroupContract.IGroupMangerPresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return getActivity();
    }
}
