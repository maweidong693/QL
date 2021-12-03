package com.weiwu.ql.main.contact.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.ScreenUtils;
import com.tencent.common.Constant;
import com.tencent.qcloud.tim.uikit.component.CircleImageView;
import com.tencent.qcloud.tim.uikit.component.LineControllerView;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.dialog.TUIKitDialog;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.modules.contact.FriendProfileLayout;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.weiwu.ql.AppConstant;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.data.bean.FriendInfoData;
import com.weiwu.ql.data.bean.FriendPicsData;
import com.weiwu.ql.data.bean.MessageEvent;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.repositories.ContactRepository;
import com.weiwu.ql.data.request.AddFriendRequestBody;
import com.weiwu.ql.data.request.FriendInfoRequestBody;
import com.weiwu.ql.data.request.SetFriendRequestBody;
import com.weiwu.ql.main.contact.ContactContract;
import com.weiwu.ql.main.contact.chat.ChatActivity;
import com.weiwu.ql.main.contact.group.info.SelectionActivity;
import com.weiwu.ql.main.mine.friends.FriendsAndMeCircleActivity;
import com.weiwu.ql.main.mine.friends.issue.FullyGridLayoutManager;
import com.weiwu.ql.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FriendProfileActivity extends BaseActivity implements View.OnClickListener, ContactContract.IFriendDetailView {

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
    private ContactContract.IFriendDetailPresenter mPresenter;
    private FriendInfoData.DataDTO dto;
    private int mIs_black;

    private LinearLayout mllCircleOfFriends;
    private RecyclerView mRecyclerView;
    private RelativeLayout mFriend;
    private GridImageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        setPresenter(new FriendProfilePresenter(ContactRepository.getInstance()));
        Intent intent = getIntent();
        if (intent != null) {
            mFriendInfo = (ContactItemBean) intent.getSerializableExtra(TUIKitConstants.ProfileType.CONTENT);
        }
        initView();
    }

    private void initView() {
        mHeadImageView = findViewById(R.id.avatar);
        mNickNameView = findViewById(R.id.name);
        mIDView = findViewById(R.id.id);
        mAddWordingView = findViewById(R.id.add_wording);

        mRemarkView = findViewById(R.id.remark);
        mRemarkView.setOnClickListener(this);
        mAuthorityView = findViewById(R.id.authority);
        mAuthorityView.setOnClickListener(this);
        mChatTopView = findViewById(R.id.chat_to_top);
        mAddBlackView = findViewById(R.id.blackList);

        mAddBlackView.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ((mIs_black == 1) != isChecked) {

                    mPresenter.setFriendInfo(new SetFriendRequestBody(mFriendInfo.getId(), null, null, isChecked ? "1" : "0", null));
                }
            }
        });

        llCircleOfFriends = findViewById(R.id.llCircleOfFriends);
        mDeleteView = findViewById(R.id.btnDel);
        mDeleteView.setOnClickListener(this);
        mChatView = findViewById(R.id.btnChat);
        mChatView.setOnClickListener(this);
        mTitleBar = findViewById(R.id.friend_titlebar);
        mTitleBar.setTitle(getResources().getString(R.string.profile_detail), TitleBarLayout.POSITION.MIDDLE);
        mTitleBar.getRightGroup().setVisibility(View.GONE);
        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mllCircleOfFriends = findViewById(R.id.llCircleOfFriends);
        mRecyclerView = findViewById(R.id.recycler);
        mFriend = findViewById(R.id.rl_friend);
        mRecyclerView.setOnTouchListener((v, event) -> {

            return mFriend.onTouchEvent(event);
        });
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this,
                4, GridLayoutManager.VERTICAL, false);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(4,
                ScreenUtils.dip2px(this, 8), false));
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new GridImageAdapter(this);
        mllCircleOfFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("im_id", mFriendInfo.getId());
//                FriendsAndMeCircleActivity.start(FriendProfileActivity.this, intent);
            }
        });
        mFriend.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("im_id", mFriendInfo.getId());

//            FriendsAndMeCircleActivity.start(FriendProfileActivity.this, intent);
        });
        initData();
    }

    private void initData() {
        /*String avatarurl = mFriendInfo.getAvatarurl();
        if (!TextUtils.isEmpty(avatarurl)) {
            Glide.with(this).load(avatarurl).into(mHeadImageView);
        }
        mNickNameView.setText(mFriendInfo.getNickname());
        mIDView.setContent(mFriendInfo.getId());
        if (mFriendInfo.getId().equals(SPUtils.getValue(AppConstant.USER, AppConstant.USER_ID))) {
            mRemarkView.setVisibility(View.GONE);
            mAuthorityView.setVisibility(View.GONE);
            mAddBlackView.setVisibility(View.GONE);
            mChatView.setVisibility(View.GONE);
            mDeleteView.setVisibility(View.GONE);
        }*/
        mPresenter.getFriendInfo(new FriendInfoRequestBody(mFriendInfo.getId()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnChat:
                Intent chatIntent = new Intent(this, ChatActivity.class);
                chatIntent.putExtra(TUIKitConstants.ProfileType.CONTENT, mFriendInfo);
                chatIntent.putExtra(AppConstant.CHAT_TYPE, 1);
                startActivity(chatIntent);
//                finish();
                break;
            case R.id.btnDel:
                new TUIKitDialog(this)
                        .builder()
                        .setCancelable(true)
                        .setCancelOutside(true)
                        .setTitle("您确认删除该好友？")
                        .setDialogWidth(0.75f)
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPresenter.deleteFriend(new AddFriendRequestBody(mFriendInfo.getId()));
                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();
                break;

            case R.id.remark:
                Bundle bundle = new Bundle();
                bundle.putString(TUIKitConstants.Selection.TITLE, "修改备注");
                bundle.putString(TUIKitConstants.Selection.INIT_CONTENT, mFriendInfo.getRemark());
                bundle.putInt(TUIKitConstants.Selection.LIMIT, 266);
                bundle.putString(TUIKitConstants.Selection.ID, mFriendInfo.getId());
                SelectionActivity.startTextSelection(this, bundle, new SelectionActivity.OnResultReturnListener() {
                    @Override
                    public void onReturn(final Object text) {
//                       mPresenter.modifyGroupName(text.toString());
                        mRemarkView.setContent(text.toString());
                    }
                });
                break;

            case R.id.authority:
                Intent intent = new Intent(this, UserDataSettingActivity.class);
                intent.putExtra("im_id", dto.getUser_id());
                intent.putExtra("see_to", dto.getNot_see());
                intent.putExtra("to_see", dto.getNot_show());
                startActivity(intent);

                break;
        }
    }

    @Override
    public void friendInfoReceive(FriendInfoData data) {
        dto = data.getData();
        String avatarurl = dto.getFace_url();
        if (!TextUtils.isEmpty(avatarurl)) {
            Glide.with(this).load(avatarurl).into(mHeadImageView);
        }
        mNickNameView.setText(dto.getNick_name());
        mIDView.setContent(dto.getUser_id());

        if (mFriendInfo.getId().equals(SPUtils.getValue(AppConstant.USER, AppConstant.USER_ID))) {
            mRemarkView.setVisibility(View.GONE);
            mAuthorityView.setVisibility(View.GONE);
            mAddBlackView.setVisibility(View.GONE);
            mChatView.setVisibility(View.GONE);
            mDeleteView.setVisibility(View.GONE);
        } else if (dto.getIs_friend() == 1) {
            mRemarkView.setContent(dto.getAlias());
            mIs_black = dto.getIs_black();
            mAddBlackView.setChecked(dto.getIs_black() == 1);
            mPresenter.getFriendPics(new SetFriendRequestBody(dto.getUser_id()));
        }else {
            mRemarkView.setVisibility(View.GONE);
            mAuthorityView.setVisibility(View.GONE);
            mAddBlackView.setVisibility(View.GONE);
            mChatView.setVisibility(View.GONE);
            mDeleteView.setVisibility(View.GONE);
            mllCircleOfFriends.setVisibility(View.GONE);
        }
    }

    @Override
    public void friendPicsReceive(FriendPicsData data) {

        if (data.getData().size() > 0) {
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setList(data.getData());
        }

    }

    @Override
    public void deleteFriendResult(HttpResult result) {
        if (result.getCode() == 200) {
            showToast("删除成功");
            EventBus.getDefault().post(new MessageEvent("刷新好友列表", 101));
            finish();
        }
    }

    @Override
    public void setFriendInfoResult(HttpResult result) {
        if (result.getCode() == 200) {
            showToast(result.getMsg());
            EventBus.getDefault().post(new MessageEvent("刷新好友列表", 101));
            finish();
        }
    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
        if (code == 10001) {
            MyApplication.getInstance().loginAgain();
        }
    }

    @Override
    public void setPresenter(ContactContract.IFriendDetailPresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return this;
    }
}