package com.tencent.qcloud.tim.uikit.modules.group.member;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.base.BaseFragment;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;


public class GroupMemberDeleteFragment extends BaseFragment {

    private GroupMemberDeleteLayout mMemberDelLayout;

    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.group_fragment_del_members, container, false);
        mMemberDelLayout = mBaseView.findViewById(R.id.group_member_del_layout);
        init();
        return mBaseView;
    }*/


    @Override
    public void onViewCreated(View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMemberDelLayout = view.findViewById(R.id.group_member_del_layout);
        init();
    }

    private void init() {
        mMemberDelLayout.setDataSource((GroupInfo) getArguments().getSerializable(TUIKitConstants.Group.GROUP_INFO), null);
        mMemberDelLayout.getTitleBar().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backward();
            }
        });
    }
}
