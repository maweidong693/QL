package com.weiwu.ql.main.mine;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.tencent.common.Constant;
import com.tencent.common.UserInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.component.LineControllerView;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.dialog.TUIKitDialog;
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.GlideEngine;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseFragment;
import com.weiwu.ql.data.bean.MineInfoData;
import com.weiwu.ql.data.bean.NewMsgCount;
import com.weiwu.ql.data.repositories.MineRepository;
import com.weiwu.ql.utils.IntentUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/22 14:03 
 */
public class MineFragment extends BaseFragment implements MineContract.IMineView {
    private ImageView mUserIcon;
    private TextView mAccountView;
    private TextView mPhoneView;
    private TitleBarLayout mTitleBar;
    private LineControllerView mPrivacySettingView;
    private LineControllerView mGeneralSettingView;
    private LineControllerView mclearCacheView;
    private LineControllerView mFavoritesView;
    private LinearLayout mItemHeadView;
    private LineControllerView mAboutIM;
    private TextView mMsgCircleUnread;
    private RelativeLayout mFind;
    private LineControllerView mGeneralAnQuanView;
    private MineContract.IMinePresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void onViewCreated(View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }*/
        setPresenter(new MinePresenter(MineRepository.getInstance()));
        initView(view);
    }

    private void initView(View view) {
        mMsgCircleUnread = view.findViewById(R.id.msg_circle_total_unread);
        mTitleBar = view.findViewById(R.id.self_info_title_bar);
        mTitleBar.getLeftGroup().setVisibility(View.GONE);
        mTitleBar.getRightGroup().setVisibility(View.GONE);
        mTitleBar.setTitle(getResources().getString(R.string.tab_profile_tab_text), TitleBarLayout.POSITION.MIDDLE);
        mUserIcon = view.findViewById(R.id.self_icon);
        mAccountView = view.findViewById(R.id.self_account);
        mPhoneView = view.findViewById(R.id.self_phone);
        mPrivacySettingView = view.findViewById(R.id.view_privacy_setting);
        mGeneralSettingView = view.findViewById(R.id.view_general_setting);
        mGeneralAnQuanView = view.findViewById(R.id.view_general_anquan);
        mFavoritesView = view.findViewById(R.id.view_favorites);
        mItemHeadView = view.findViewById(R.id.item_head);
        mclearCacheView = view.findViewById(R.id.clear_cache);
        mAboutIM = view.findViewById(R.id.about_im);
        /*mGeneralSettingView.setOnClickListener(v -> {

            IntentUtil.redirectToNextActivity(getActivity(), GeneralSettingActivity.class);

        });
        mPrivacySettingView.setOnClickListener(v -> {
            IntentUtil.redirectToNextActivity(getActivity(), PrivacySettingActivity.class);

        });
        mAboutIM.setOnClickListener(v ->
                {
                    IntentUtil.redirectToNextActivity(getActivity(), AboutUsActivity.class);
                }
        );
        mItemHeadView.setOnClickListener(v -> {
            //跳转个人信息设置界面
            IntentUtil.redirectToNextActivity(getActivity(), PersonalInformationActicity.class);

        });
        mGeneralAnQuanView.setOnClickListener(v -> {
            //跳转个人信息设置界面
            IntentUtil.redirectToNextActivity(getActivity(), AnQuanActivity.class);

        });
        mFavoritesView.setOnClickListener(v -> {
            IntentUtil.redirectToNextActivity(getActivity(), MyFavoritesActivity.class);

        });
        mclearCacheView.setOnClickListener(v -> {
            IntentUtil.redirectToNextActivity(getActivity(), ClearCacheActicity.class);
        });*/


        view.findViewById(R.id.logout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TUIKitDialog(getActivity())
                        .builder()
                        .setCancelable(true)
                        .setCancelOutside(true)
                        .setTitle("您确定要退出登录么？")
                        .setDialogWidth(0.75f)
                        .setSureButtonColor(getActivity().getResources().getColor(R.color.text_negative))
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MyApplication.loginAgain();
                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();

            }
        });
        initData();
    }

    private void initData() {
        mPresenter.getMineInfo();
    }

    @Override
    public void mineInfoReceive(MineInfoData data) {
        if (data != null) {
            MineInfoData.DataDTO dto = data.getData();
            if (!TextUtils.isEmpty(dto.getAvator())) {
                Glide.with(getContext()).load(dto.getAvator()).into(mUserIcon);
            }
            mAccountView.setText("洽聊号：" + dto.getId());
            mPhoneView.setText(dto.getNickName());
        }
    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
        if (code == 10000) {
            MyApplication.loginAgain();
        }
    }

    @Override
    public void setPresenter(MineContract.IMinePresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return getActivity();
    }

    /*@Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGetStickyEvent(NewMsgCount event) {
        if (event != null) {
            int count = event.getCount();
            if (count > 0) {
                mMsgCircleUnread.setVisibility(View.VISIBLE);
                mMsgCircleUnread.setText(count + "");
            } else {
                mMsgCircleUnread.setVisibility(GONE);
            }
        }
    }*/

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }*/
}
