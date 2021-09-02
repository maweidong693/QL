package com.weiwu.ql;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.common.Constant;
import com.tencent.common.dialog.DialogMaker;
import com.tencent.common.http.BaseBean;
import com.tencent.common.http.HttpCallBack;
import com.tencent.common.http.YHttp;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.repositories.MineRepository;
import com.weiwu.ql.data.request.WebLoginRequestBody;
import com.weiwu.ql.main.login.web.WebLoginPresenter;
import com.weiwu.ql.main.mine.MineContract;
import com.weiwu.ql.utils.SPUtils;

import java.util.HashMap;
import java.util.Map;

public class WebLoginConfirmActivity extends BaseActivity implements MineContract.IWebLoginView {

    private TitleBarLayout mTitleBar;
    private Button mBtnLogin;
    private TextView mBtnCancel;
    private TextView mTvDescription;
    private ImageView mImg;
    private MineContract.IWebLoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_login_activity);
        setPresenter(new WebLoginPresenter(MineRepository.getInstance()));
        mBtnLogin = findViewById(R.id.btn_login);
        mTvDescription = findViewById(R.id.tv_description);
        mBtnCancel = findViewById(R.id.btn_cancel_login);
        mImg = findViewById(R.id.img_type);
        String type = getIntent().getStringExtra("type");
        String content = getIntent().getStringExtra("content");
        if (TextUtils.equals(type, "logout")) {
            mImg.setImageDrawable(getResources().getDrawable(R.drawable.pc_app));
            mTvDescription.setText("Web 洽聊已登录");
            mBtnLogin.setText("退出 Web 洽聊");
            mBtnLogin.setOnClickListener(v -> {
                //退出
//                EventBus.getDefault().post(new WebBean());
                DialogMaker.showProgressDialog(WebLoginConfirmActivity.this, "请稍后");
                YHttp.obtain().post(Constant.URL_WEB_LOGOUT, null, new HttpCallBack<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean o) {
                        finish();
                    }

                    @Override
                    public void onFailed(String error) {

                    }
                });

            });
            mBtnCancel.setVisibility(View.INVISIBLE);
            mBtnCancel.setOnClickListener(v -> {
                //取消
            });
        }
        if (TextUtils.equals(type, "login")) {
            mImg.setImageDrawable(getResources().getDrawable(R.drawable.web_login));
            mTvDescription.setText("Web 洽聊登录确认");
            mBtnLogin.setOnClickListener(v -> {
                mPresenter.webLogin(SPUtils.getValue(AppConstant.USER, AppConstant.USER_TOKEN), content);
            });

            mBtnCancel.setOnClickListener(v -> {
                //取消
                finish();
            });
        }

        mTitleBar = findViewById(R.id.web_title_bar);
        mTitleBar.setOnLeftClickListener(v -> {
            finish();
        });
        mTitleBar.getRightGroup().setVisibility(View.GONE);
    }

    @Override
    public void webLoginResult(HttpResult data) {
        finish();
    }

    @Override
    public void onFail(String msg, int code) {

    }

    @Override
    public void setPresenter(MineContract.IWebLoginPresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return this;
    }
}
