package com.tencent.qcloud.tim.uikit.modules.group.member;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.base.BaseFragment;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;


public class MessageForwardFragment extends BaseFragment {

    private MessageForwardLayout mInviteLayout;
    private View mBaseView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.message_forward, container, false);
        mInviteLayout = mBaseView.findViewById(R.id.group_member_invite_layout);
        mInviteLayout.setParentLayout(getActivity());
        init();
        return mBaseView;
    }

    private void init() {
//        mInviteLayout.initTitle((InfoData) getArguments().getSerializable("info"));
//        mInviteLayout.setDataSource((GroupInfo) getArguments().getSerializable(TUIKitConstants.Group.GROUP_INFO));
//        mInviteLayout.setMsgSource((MessageInfo) getArguments().getSerializable("msg"));
        mInviteLayout.getTitleBar().setOnLeftClickListener(v -> {
            getActivity().finish();
        });
    }
}
