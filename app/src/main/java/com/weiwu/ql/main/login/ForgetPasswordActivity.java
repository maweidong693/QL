package com.weiwu.ql.main.login;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.common.Constant;
import com.tencent.common.dialog.DialogMaker;
import com.tencent.common.http.BaseBean;
import com.tencent.common.http.HttpCallBack;
import com.tencent.common.http.YHttp;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.weiwu.ql.AppConstant;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.data.bean.LoginReceive;
import com.weiwu.ql.data.bean.SocketDataBean;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.repositories.MineRepository;
import com.weiwu.ql.data.request.RegisterRequestBody;
import com.weiwu.ql.data.request.SendPhoneMsgRequestBody;
import com.weiwu.ql.data.request.VerifyCodeRequestBody;
import com.weiwu.ql.main.login.country.ChoiceCountryActivity;
import com.weiwu.ql.main.login.country.CountryEntity;
import com.weiwu.ql.main.mine.MineContract;
import com.weiwu.ql.utils.IntentUtil;
import com.weiwu.ql.utils.SPUtils;
import com.weiwu.ql.utils.TimeCountUtil;
import com.weiwu.ql.utils.ToastHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener, MineContract.ILoginView {

    private TitleBarLayout forgetPasswordTitleBar;
    private LinearLayout linForgetPasswordCode;
    private TextView tvForgetPasswordMobileArea;
    private TextView tvForgetPasswordMobileCode;
    private EditText etForgetPasswordMobile;
    private EditText etForgetPasswordCode;
    private TextView tvForgetPasswordUpCode;
    private TextView forgetPasswordBtnGo;
    private TimeCountUtil timeCountUtil;

    private boolean update = true;
    private LinearLayout llVerify;
    private EditText etForgetPassword;
    private EditText etForgetPasswordConfirm;
    private View v1;
    private int mIntExtra;
    private MineContract.ILoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_passwrod);
        setPresenter(new LoginPresenter(MineRepository.getInstance()));
        Intent intent = getIntent();
        mIntExtra = intent.getIntExtra(Constant.PASSWORD_TYPE, 0);
        initView();
        EventBus.getDefault().register(this);
    }

    private void initView() {
        forgetPasswordTitleBar = (TitleBarLayout) findViewById(R.id.forget_password_title_bar);
        etForgetPasswordMobile = (EditText) findViewById(R.id.et_forget_password_mobile);

        forgetPasswordTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        forgetPasswordTitleBar.getRightIcon().setVisibility(View.GONE);
        linForgetPasswordCode = (LinearLayout) findViewById(R.id.lin_forget_password_code);
        linForgetPasswordCode.setOnClickListener(this);
        tvForgetPasswordMobileArea = (TextView) findViewById(R.id.tv_forget_password_mobile_area);
        tvForgetPasswordMobileCode = (TextView) findViewById(R.id.tv_forget_password_mobile_code);
        etForgetPasswordCode = (EditText) findViewById(R.id.et_forget_password_code);
        tvForgetPasswordUpCode = (TextView) findViewById(R.id.tv_forget_password_upCode);
        llVerify = (LinearLayout) findViewById(R.id.ll_verify);
        tvForgetPasswordUpCode.setOnClickListener(this);
        forgetPasswordBtnGo = (TextView) findViewById(R.id.forget_password_btn_go);
        forgetPasswordBtnGo.setOnClickListener(this);
        etForgetPassword = (EditText) findViewById(R.id.et_forget_password);
        v1 = (View) findViewById(R.id.v_1);
        etForgetPasswordConfirm = (EditText) findViewById(R.id.et_forget_password_confirm);
        timeCountUtil = new TimeCountUtil(this, 60 * 1000, 1000, tvForgetPasswordUpCode, getResources().getColor(R.color.black));

        if (mIntExtra == 1) {
            forgetPasswordTitleBar.setTitle(getResources().getString(R.string.updatePassword), TitleBarLayout.POSITION.MIDDLE);
            String tel = SPUtils.getValue(AppConstant.USER, AppConstant.USER_PHONE);
            String nativeCode = SPUtils.getValue(AppConstant.USER, AppConstant.USER_COUNTRY_CODE);
            String country = SPUtils.getValue(AppConstant.USER, AppConstant.USER_COUNTRY);
            if (!TextUtils.isEmpty(tel)) {
                etForgetPasswordMobile.setFocusable(false);
                etForgetPasswordMobile.setFocusableInTouchMode(false);
                etForgetPasswordMobile.setText(tel);
                tvForgetPasswordMobileArea.setText(country);
                tvForgetPasswordMobileCode.setText(nativeCode);
            }
        } else {
            forgetPasswordTitleBar.setTitle(getResources().getString(R.string.forgetPassword), TitleBarLayout.POSITION.MIDDLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lin_forget_password_code:
                if (mIntExtra != 1) {

                    IntentUtil.redirectToNextActivity(this, ChoiceCountryActivity.class);
                }
                break;

            case R.id.tv_forget_password_upCode:
                //获取验证码
                if (TextUtils.isEmpty(etForgetPasswordMobile.getText().toString())) {
                    ToastHelper.showToast(this, getString(R.string.input_mobile));
                    return;
                }
                timeCountUtil.start();
                StringBuilder sb = new StringBuilder();
                if (!tvForgetPasswordMobileCode.getText().toString().equals("+86")) {
                    //tvMobileCode.getText().toString()+"-"+
                    sb.append(tvForgetPasswordMobileCode.getText().toString()).append("-");
                }
                mPresenter.sendPhone(new SendPhoneMsgRequestBody(etForgetPasswordMobile.getText().toString(), tvForgetPasswordMobileCode.getText().toString().substring(1)));
                /*sb.append(etForgetPasswordMobile.getText().toString());
//                HttpRequestParams params = new HttpRequestParams(Constant.GET_MSG_CODE + sb.toString());
                Map params = new HashMap();
                params.put("tel", etForgetPasswordMobile.getText().toString());
                params.put("nationCode", tvForgetPasswordMobileCode.getText().toString().substring(1));
                YHttp.obtain().post(Constant.GET_MSG_CODE, params, new HttpCallBack<BaseBean>() {

                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        try {
                            ToastUtil.toastShortMessage(baseBean.getMsg());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(String error) {

                    }
                });*/
                break;

            case R.id.forget_password_btn_go:
                if (update) {
                    if (TextUtils.isEmpty(etForgetPasswordMobile.getText().toString())) {
                        ToastHelper.showToast(this, getString(R.string.input_mobile));
                        return;
                    }
                    if (TextUtils.isEmpty(etForgetPasswordCode.getText().toString())) {
                        ToastHelper.showToast(this, getString(R.string.input_code));
                        return;
                    }
                    mPresenter.verify(new VerifyCodeRequestBody(etForgetPasswordMobile.getText().toString(), etForgetPasswordCode.getText().toString()));
                    /*Map map = new HashMap();
                    map.put("tel", etForgetPasswordMobile.getText().toString());
                    map.put("yzm", etForgetPasswordCode.getText().toString());
                    map.put("nationCode", tvForgetPasswordMobileCode.getText().toString().substring(1));
                    DialogMaker.showProgressDialog(this, null, getString(R.string.request), true, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            onLoginDone();

                        }
                    }).setCanceledOnTouchOutside(false);
                    YHttp.obtain().post(Constant.URL_CHECK_VERIFY, map, new HttpCallBack<BaseBean>() {

                        @Override
                        public void onSuccess(BaseBean baseBean) {
                            if (baseBean == null) {
                                return;
                            }
                            if (baseBean.getCode() == 200) {
                                etForgetPasswordMobile.setVisibility(View.GONE);
                                llVerify.setVisibility(View.GONE);
                                linForgetPasswordCode.setVisibility(View.GONE);
                                etForgetPassword.setVisibility(View.VISIBLE);
                                etForgetPasswordConfirm.setVisibility(View.VISIBLE);
                                forgetPasswordBtnGo.setText("修改密码");
                                v1.setVisibility(View.GONE);
                                update = false;
                            }
                        }

                        @Override
                        public void onFailed(String error) {

                        }
                    });*/
                } else {
                    if (TextUtils.isEmpty(etForgetPassword.getText().toString())) {
                        ToastHelper.showToast(this, getString(R.string.input_password));
                        return;
                    }
                    if (TextUtils.isEmpty(etForgetPasswordConfirm.getText().toString())) {
                        ToastHelper.showToast(this, getString(R.string.input_password_confirm));
                        return;
                    }
                    String regex = "^[a-zA-Z0-9]{6,12}$";
                    if (!etForgetPassword.getText().toString().matches(regex)) {
                        ToastHelper.showToast(this, "请输入6-12位密码");
                        return;
                    }
                    if (!etForgetPassword.getText().toString().equals(etForgetPasswordConfirm.getText().toString())) {
                        ToastHelper.showToast(this, getString(R.string.input_password_confirm_message));
                        return;
                    }
                    if (mIntExtra == 1) {
                        mPresenter.updatePassword(new RegisterRequestBody(etForgetPasswordConfirm.getText().toString()));
                    } else {
                        mPresenter.forgetPassword(new RegisterRequestBody(etForgetPasswordMobile.getText().toString(), etForgetPasswordConfirm.getText().toString()));

                    }
                    /*Map map = new HashMap();
                    map.put("tel", etForgetPasswordMobile.getText().toString());
                    map.put("password", etForgetPasswordConfirm.getText().toString());
                    DialogMaker.showProgressDialog(this, null, getString(R.string.request), true, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            onLoginDone();

                        }
                    }).setCanceledOnTouchOutside(false);

                    YHttp.obtain().post(Constant.URL_UPDATE_PASSWORD, map, new HttpCallBack<BaseBean>() {

                        @Override
                        public void onSuccess(BaseBean baseBean) {
                            if (baseBean == null) {
                                return;
                            }
                            if (baseBean.getCode() == 200) {
                                ToastUtil.toastShortMessage(baseBean.getMsg());
                                finish();
                            }
                        }

                        @Override
                        public void onFailed(String error) {

                        }
                    });*/
                }

                break;
        }
    }

    private void onLoginDone() {
        DialogMaker.dismissProgressDialog();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CountryEntity countryEntity) {
        if (countryEntity != null) {
            tvForgetPasswordMobileCode.setText(countryEntity.getCode());
            tvForgetPasswordMobileArea.setText(countryEntity.getCountry());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setPresenter(MineContract.ILoginPresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return this;
    }

    @Override
    public void updateSuccess(HttpResult data) {

        ToastUtil.toastShortMessage(data.getMsg());
        finish();

    }

    @Override
    public void verifySuccess(HttpResult data) {
        etForgetPasswordMobile.setVisibility(View.GONE);
        llVerify.setVisibility(View.GONE);
        linForgetPasswordCode.setVisibility(View.GONE);
        etForgetPassword.setVisibility(View.VISIBLE);
        etForgetPasswordConfirm.setVisibility(View.VISIBLE);
        forgetPasswordBtnGo.setText("修改密码");
        v1.setVisibility(View.GONE);
        update = false;
    }

    @Override
    public void registerSuccess(HttpResult data) {

    }

    @Override
    public void loginResult(LoginReceive data) {

    }

    @Override
    public void socketUrl(SocketDataBean dataBean) {

    }

    @Override
    public void onSuccess(HttpResult data) {
        ToastUtil.toastShortMessage(data.getMsg());
    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
    }
}