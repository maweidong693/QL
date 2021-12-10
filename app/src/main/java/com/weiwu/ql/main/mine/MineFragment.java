package com.weiwu.ql.main.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.tencent.qcloud.tim.uikit.component.LineControllerView;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.dialog.TUIKitDialog;
import com.weiwu.ql.AppConstant;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseFragment;
import com.weiwu.ql.data.bean.MessageEvent;
import com.weiwu.ql.data.bean.MineInfoData;
import com.weiwu.ql.data.bean.NewMsgData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.repositories.MineRepository;
import com.weiwu.ql.main.mine.collection.MyFavoritesActivity;
import com.weiwu.ql.main.mine.friends.FriendsActivity;
import com.weiwu.ql.main.mine.detail.PersonalInformationActivity;
import com.weiwu.ql.main.mine.setting.AboutUsActivity;
import com.weiwu.ql.main.mine.setting.AnQuanActivity;
import com.weiwu.ql.main.mine.setting.ClearCacheActicity;
import com.weiwu.ql.main.mine.setting.GeneralSettingActivity;
import com.weiwu.ql.main.mine.setting.PrivacySettingActivity;
import com.weiwu.ql.utils.IntentUtil;
import com.weiwu.ql.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    private LineControllerView mWallet;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void onViewCreated(View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setPresenter(new MinePresenter(MineRepository.getInstance()));
        initView(view);
    }

    private void initView(View view) {
        mFind = view.findViewById(R.id.rl_find);
        mFind.setOnClickListener(v -> {
            FriendsActivity.start(getActivity(), null);
//            getActivity().finish();
        });
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
        mGeneralSettingView.setOnClickListener(v -> {

            IntentUtil.redirectToNextActivity(getActivity(), GeneralSettingActivity.class);
//            getActivity().finish();

        });
        mPrivacySettingView.setOnClickListener(v -> {
            IntentUtil.redirectToNextActivity(getActivity(), PrivacySettingActivity.class);

        });
        mAboutIM.setOnClickListener(v -> {
                    IntentUtil.redirectToNextActivity(getActivity(), AboutUsActivity.class);
                }
        );
        mItemHeadView.setOnClickListener(v -> {
            //跳转个人信息设置界面
//            IntentUtil.redirectToNextActivity(getActivity(), PersonalInformationActicity.class);
            Intent intent = new Intent(getActivity(), PersonalInformationActivity.class);
            startActivity(intent);
//            getActivity().finish();
        });
        mGeneralAnQuanView.setOnClickListener(v -> {
            IntentUtil.redirectToNextActivity(getActivity(), AnQuanActivity.class);
//            getActivity().finish();

        });
        mFavoritesView.setOnClickListener(v -> {
            IntentUtil.redirectToNextActivity(getActivity(), MyFavoritesActivity.class);

        });
        mclearCacheView.setOnClickListener(v -> {
            IntentUtil.redirectToNextActivity(getActivity(), ClearCacheActicity.class);
        });


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
                                MyApplication.getInstance().loginAgain();

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
            if (!TextUtils.isEmpty(dto.getFace_url())) {
                Glide.with(getActivity()).load(dto.getFace_url()).into(mUserIcon);
            }
            mAccountView.setText("洽聊号：" + dto.getIm_id());
            mPhoneView.setText(dto.getNick_name());
            SPUtils.commitValue(AppConstant.USER, AppConstant.USER_ID, dto.getIm_id());
            SPUtils.commitValue(AppConstant.USER, AppConstant.USER_NAME, dto.getNick_name());
            SPUtils.commitValue(AppConstant.USER, AppConstant.USER_PHONE, dto.getMobile());
        }
    }

    @Override
    public void newMsgCountResult(NewMsgData data) {

    }

    @Override
    public void addFriendReceive(HttpResult data) {

    }

    @Override
    public void onSuccess(HttpResult data) {

    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
        if (code == 10000) {
            MyApplication.getInstance().loginAgain();
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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGetStickyEvent(MessageEvent event) {
        if (event != null) {
            int count = event.code;
            if (count == 111) {
                initData();
            }
        }
    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }*/
}
