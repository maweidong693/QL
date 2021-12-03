package com.weiwu.ql.main.contact.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.common.Constant;
import com.tencent.common.http.BaseBean;
import com.tencent.common.http.HttpCallBack;
import com.tencent.common.http.YHttp;
import com.tencent.qcloud.tim.uikit.component.LineControllerView;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.data.bean.FriendInfoData;
import com.weiwu.ql.data.bean.FriendPicsData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.repositories.ContactRepository;
import com.weiwu.ql.data.request.SetFriendRequestBody;
import com.weiwu.ql.main.contact.ContactContract;

import java.util.HashMap;
import java.util.Map;

public class UserDataSettingActivity extends BaseActivity implements ContactContract.IFriendDetailView {


    private TitleBarLayout mTitlebar;
    private LineControllerView mProfileSeeTo;
    private LineControllerView mProfileToSee;
    private ContactContract.IFriendDetailPresenter mPresenter;
    private int mSee_to;
    private int mTo_see;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data_setting);
        setPresenter(new FriendProfilePresenter(ContactRepository.getInstance()));
        String im_id = getIntent().getStringExtra("im_id");
        mSee_to = getIntent().getIntExtra("see_to", 0);
        mTo_see = getIntent().getIntExtra("to_see", 0);
        mTitlebar = findViewById(R.id.user_data_friend_titlebar);
        mTitlebar.setTitle(getResources().getString(R.string.profile_authority), TitleBarLayout.POSITION.MIDDLE);
        mTitlebar.getRightGroup().setVisibility(View.GONE);
        mTitlebar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mProfileSeeTo = findViewById(R.id.profile_see_to);
        mProfileToSee = findViewById(R.id.profile_to_see);
//        getSecurity(im_id);
        mProfileToSee.setChecked(mTo_see == 1);
        mProfileSeeTo.setChecked(mSee_to == 1);
        mProfileSeeTo.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!compoundButton.isPressed()) {
                    return;
                }
                mPresenter.setFriendInfo(new SetFriendRequestBody(im_id,isChecked ? "1" : "0", null,  null, null));

            }
        });
        mProfileToSee.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!compoundButton.isPressed()) {
                    return;
                }
                mPresenter.setFriendInfo(new SetFriendRequestBody(im_id,  null,isChecked ? "1" : "0", null, null));

            }
        });
    }

    private void getSecurity(String im_id) {
        Map map = new HashMap();
        map.put("im_id", im_id);
        YHttp.obtain().post(Constant.URL_SECURITY, map, new HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean bean) {
                if (bean.getData() == null) {
                    return;
                }
                JSONObject jsonObject = JSON.parseObject(bean.getData().toString());
                mProfileToSee.setChecked(jsonObject.getBoolean("notSee"));
                mProfileSeeTo.setChecked(jsonObject.getBoolean("notShow"));
            }

            @Override
            public void onFailed(String error) {

            }
        });


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

    @Override
    public void friendInfoReceive(FriendInfoData data) {

    }

    @Override
    public void friendPicsReceive(FriendPicsData data) {

    }

    @Override
    public void deleteFriendResult(HttpResult result) {

    }

    @Override
    public void setFriendInfoResult(HttpResult result) {

    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
    }
}
