package com.weiwu.ql.main.contact.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.qcloud.tim.uikit.component.CircleImageView;
import com.tencent.qcloud.tim.uikit.component.LineControllerView;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.main.contact.chat.ChatActivity;

import java.io.Serializable;

public class FriendProfileActivity extends BaseActivity implements View.OnClickListener {

    private TitleBarLayout mTitleBar;
    private CircleImageView mHeadImageView;
    private TextView mNickNameView;
    private LineControllerView mIDView;
    private LineControllerView mAddWordingView;
    private LineControllerView mRemarkView;
    private LineControllerView mAuthorityView;
    private LineControllerView mAddBlackView;
    private LineControllerView mChatTopView;
    private LinearLayout llCircleOfFriends;
    private TextView mDeleteView;
    private TextView mChatView;
    private ContactItemBean mFriendInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        Intent intent = getIntent();
        if (intent != null) {
            mFriendInfo = (ContactItemBean) intent.getSerializableExtra(TUIKitConstants.ProfileType.CONTENT);
        }
        initView();
    }

    private void initView() {
        mHeadImageView = findViewById(com.tencent.qcloud.tim.uikit.R.id.avatar);
        mNickNameView = findViewById(com.tencent.qcloud.tim.uikit.R.id.name);
        mIDView = findViewById(com.tencent.qcloud.tim.uikit.R.id.id);
        mAddWordingView = findViewById(com.tencent.qcloud.tim.uikit.R.id.add_wording);

        mRemarkView = findViewById(com.tencent.qcloud.tim.uikit.R.id.remark);
        mRemarkView.setOnClickListener(this);
        mAuthorityView = findViewById(com.tencent.qcloud.tim.uikit.R.id.authority);
        mChatTopView = findViewById(com.tencent.qcloud.tim.uikit.R.id.chat_to_top);
        mAddBlackView = findViewById(com.tencent.qcloud.tim.uikit.R.id.blackList);
        llCircleOfFriends = findViewById(com.tencent.qcloud.tim.uikit.R.id.llCircleOfFriends);
        mDeleteView = findViewById(com.tencent.qcloud.tim.uikit.R.id.btnDel);
        mDeleteView.setOnClickListener(this);
        mChatView = findViewById(com.tencent.qcloud.tim.uikit.R.id.btnChat);
        mChatView.setOnClickListener(this);
        mTitleBar = findViewById(com.tencent.qcloud.tim.uikit.R.id.friend_titlebar);
        mTitleBar.setTitle(getResources().getString(com.tencent.qcloud.tim.uikit.R.string.profile_detail), TitleBarLayout.POSITION.MIDDLE);
        mTitleBar.getRightGroup().setVisibility(View.GONE);
        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initData();
    }

    private void initData() {
        String avatarurl = mFriendInfo.getAvatarurl();
        if (!TextUtils.isEmpty(avatarurl)) {
            Glide.with(this).load(avatarurl).into(mHeadImageView);
        }
        mNickNameView.setText(mFriendInfo.getNickname());
        mIDView.setContent(mFriendInfo.getId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnChat:
                Intent chatIntent = new Intent(this, ChatActivity.class);
                chatIntent.putExtra(TUIKitConstants.ProfileType.CONTENT, mFriendInfo);
                startActivity(chatIntent);
                break;
        }
    }
}