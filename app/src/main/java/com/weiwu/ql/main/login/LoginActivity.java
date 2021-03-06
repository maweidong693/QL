package com.weiwu.ql.main.login;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.qiniu.android.utils.StringUtils;
import com.tencent.common.Constant;
import com.weiwu.ql.AppConstant;
import com.weiwu.ql.MainActivity;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.data.bean.LoginReceive;
import com.weiwu.ql.data.bean.SocketDataBean;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.repositories.MineRepository;
import com.weiwu.ql.data.request.LoginRequestBody;
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
import com.weiwu.ql.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LoginActivity extends BaseActivity implements View.OnClickListener, MineContract.ILoginView {

    protected TextView tvMobileArea;
    protected TextView etMobileCode;
    protected TextView tvMobileCode;
    protected LinearLayout linCode;
    protected EditText etMobile;
    protected EditText etCode;
    protected EditText invite_code;
    protected TextView tvUpCode;
    protected LinearLayout choiceAgreement;
    protected TextView choiceAgreement1;
    protected TextView choiceAgreement2;
    protected TextView splashBtnGo;
    private TimeCountUtil timeCountUtil;
    private ImageView ivChoice;
    private LinearLayout llChoice;
    private boolean isChoice = true;
    private String choice;
    TextView mRegisteredOrLogin;
    private EditText etPassword;
    private CheckBox cbPasswordLogin;
    private TextView tvForgetPassword;
    private LinearLayout llVerify;
    private View v2;
    private View v3;
    private MineContract.ILoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImmersionBar.with(this).transparentNavigationBar().hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();
        setPresenter(new LoginPresenter(MineRepository.getInstance()));
        startMain();
        initView();
        Utils.checkPermission(this);
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);

        }
    }

    public void startMain() {
        String token = SPUtils.getValue(AppConstant.USER, AppConstant.USER_TOKEN);
        if (StringUtils.isNullOrEmpty(token)) {
            return;
        } else {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }

    private void initView() {
        mRegisteredOrLogin = findViewById(R.id.registered_or_login);
        tvMobileArea = (TextView) findViewById(R.id.tv_mobile_area);
        tvMobileCode = (TextView) findViewById(R.id.tv_mobile_code);
        linCode = (LinearLayout) findViewById(R.id.lin_code);
        linCode.setOnClickListener(this);
        etMobile = (EditText) findViewById(R.id.et_mobile);
        etCode = (EditText) findViewById(R.id.et_code);
        invite_code = (EditText) findViewById(R.id.invite_code);
        tvUpCode = (TextView) findViewById(R.id.tv_upCode);
        tvUpCode.setOnClickListener(this);
        ivChoice = (ImageView) findViewById(R.id.iv_choice);
        llChoice = (LinearLayout) findViewById(R.id.ll_choice);
        llChoice.setOnClickListener(this);
        choiceAgreement = (LinearLayout) findViewById(R.id.choice_agreement);
        choiceAgreement1 = (TextView) findViewById(R.id.choice_agreement1);
        choiceAgreement2 = (TextView) findViewById(R.id.choice_agreement2);
        etPassword = (EditText) findViewById(R.id.et_password);
        cbPasswordLogin = (CheckBox) findViewById(R.id.cb_password_login);
        cbPasswordLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    cbPasswordLogin.setText("??????????????????");
                    etCode.setVisibility(View.GONE);
                    tvUpCode.setVisibility(View.GONE);
                    etPassword.setVisibility(View.VISIBLE);
                    llVerify.setVisibility(View.GONE);
                    v2.setVisibility(View.GONE);
                    etPassword.setText("");

                } else {
                    cbPasswordLogin.setText("????????????");
                    etCode.setVisibility(View.VISIBLE);
                    tvUpCode.setVisibility(View.VISIBLE);
                    etPassword.setVisibility(View.GONE);
                    llVerify.setVisibility(View.VISIBLE);
                    v2.setVisibility(View.GONE);
                    etCode.setText("");
                }
            }
        });
        choiceAgreement1.setOnClickListener(this);
        choiceAgreement2.setOnClickListener(this);
        mRegisteredOrLogin.setOnClickListener(this);
        splashBtnGo = (TextView) findViewById(R.id.splash_btn_go);
        splashBtnGo.setOnClickListener(this);
        timeCountUtil = new TimeCountUtil(this, 60 * 1000, 1000, tvUpCode, getResources().getColor(R.color.white));


        choiceAgreement1.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        choiceAgreement2.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvForgetPassword = (TextView) findViewById(R.id.tv_forget_password);
        tvForgetPassword.setOnClickListener(this);
        llVerify = (LinearLayout) findViewById(R.id.ll_verify);
        v2 = (View) findViewById(R.id.v_2);
        v3 = (View) findViewById(R.id.v_3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registered_or_login:
                registeredOrLogin(mRegisteredOrLogin.getText().equals("??????"));
                break;
            case R.id.choice_agreement1:
                //????????? ????????????
                /*Intent intent1 = new Intent(this, AgreementActivity.class);
                intent1.putExtra("type", "1");
                startActivity(intent1);*/
                break;
            case R.id.choice_agreement2:
                //????????? ????????????
                /*Intent intent2 = new Intent(this, AgreementActivity.class);
                intent2.putExtra("type", "2");
                startActivity(intent2);*/
                break;
            case R.id.lin_code:
                //????????????
                IntentUtil.redirectToNextActivity(this, ChoiceCountryActivity.class);
                break;
            case R.id.tv_upCode:
//                ???????????????
                if (TextUtils.isEmpty(etMobile.getText().toString())) {
                    ToastHelper.showToast(this, getString(R.string.input_mobile));
                    return;
                }
                timeCountUtil.start();
                StringBuilder sb = new StringBuilder();
                if (!tvMobileCode.getText().toString().equals("+86")) {
                    //tvMobileCode.getText().toString()+"-"+
                    sb.append(tvMobileCode.getText().toString()).append("-");
                }
                sb.append(etMobile.getText().toString());
                mPresenter.sendPhone(new SendPhoneMsgRequestBody(etMobile.getText().toString(), tvMobileCode.getText().toString()));
//                HttpRequestParams params = new HttpRequestParams(Constant.GET_MSG_CODE + sb.toString());*/
                break;
            case R.id.splash_btn_go:
                //??????????????????
                boolean isRegister = mRegisteredOrLogin.getText().equals("??????");

                if (isRegister) {
                    if (TextUtils.isEmpty(etMobile.getText().toString())) {
                        ToastHelper.showToast(this, getString(R.string.input_mobile));
                        return;
                    }
                    if (!cbPasswordLogin.isChecked()) {
                        if (TextUtils.isEmpty(etPassword.getText().toString())) {
                            ToastHelper.showToast(this, getString(R.string.input_password));
                            return;
                        }
                        String regex = "^[a-zA-Z0-9]{6,12}$";
                        if (!etPassword.getText().toString().matches(regex)) {
                            ToastHelper.showToast(this, "?????????6-12?????????");
                            return;
                        }
                    } else {
                        if (TextUtils.isEmpty(etCode.getText().toString())) {
                            ToastHelper.showToast(this, getString(R.string.input_code));
                            return;
                        }
                    }
                    if (TextUtils.isEmpty(choice)) {
                        ToastHelper.showToast(this, getString(R.string.input_yhxy));
                        return;
                    }
                    if (!cbPasswordLogin.isChecked()) {
                        mPresenter.login(new LoginRequestBody(etMobile.getText().toString(), etPassword.getText().toString(), null, "1"));
                    } else {
                        mPresenter.login(new LoginRequestBody(etMobile.getText().toString(), null, etCode.getText().toString(), "2"));

                    }

                } else {
                    if (TextUtils.isEmpty(etMobile.getText().toString())) {
                        ToastHelper.showToast(this, getString(R.string.input_mobile));
                        return;
                    }
                    if (TextUtils.isEmpty(etPassword.getText().toString())) {
                        ToastHelper.showToast(this, getString(R.string.input_password));
                        return;
                    }
                    String regex = "^[a-zA-Z0-9]{6,12}$";
                    if (!etPassword.getText().toString().matches(regex)) {
                        ToastHelper.showToast(this, "?????????6-12?????????");
                        return;
                    }
                    if (TextUtils.isEmpty(etCode.getText().toString())) {
                        ToastHelper.showToast(this, getString(R.string.input_code));
                        return;
                    }
                    if (TextUtils.isEmpty(choice)) {
                        ToastHelper.showToast(this, getString(R.string.input_yhxy));
                        return;
                    }
                    if (!tvMobileCode.getText().toString().equals("+86") && isRegister && TextUtils.isEmpty(invite_code.getText().toString())) {
                        //??????????????????
                        ToastHelper.showToast(this, "??????????????????");
                        return;
                    }
//                    mPresenter.register(new RegisterRequestBody(etMobile.getText().toString(), etPassword.getText().toString(), tvMobileCode.getText().toString(), invite_code.getText().toString()));

                    mPresenter.verify(new VerifyCodeRequestBody(etMobile.getText().toString(), etCode.getText().toString()));

                }

                break;
            case R.id.ll_choice:
                if (isChoice) {
                    ivChoice.setImageResource(R.drawable.login_5);
                    choice = "1";
                } else {
                    ivChoice.setImageResource(R.drawable.login_6);
                    choice = "";
                }
                isChoice = !isChoice;
                break;

            case R.id.tv_forget_password:
                Intent intent = new Intent(this, ForgetPasswordActivity.class);
                intent.putExtra(Constant.PASSWORD_TYPE, 2);
                startActivity(intent);
                break;
        }
    }

    private void registeredOrLogin(boolean b) {
        etCode.setText("");
        etPassword.setText("");
        mRegisteredOrLogin.setText(b ? "?????????????????????????" : "??????");
        if (b) {
            cbPasswordLogin.setChecked(false);
            tvForgetPassword.setVisibility(View.GONE);
            cbPasswordLogin.setVisibility(View.GONE);
            etPassword.setVisibility(View.VISIBLE);
            llVerify.setVisibility(View.VISIBLE);
            etCode.setVisibility(View.VISIBLE);
            v2.setVisibility(View.VISIBLE);
            tvUpCode.setVisibility(View.VISIBLE);
            if (!tvMobileCode.getText().toString().equals("86")) {
                invite_code.setVisibility(View.VISIBLE);
                v3.setVisibility(View.VISIBLE);
            } else {
                invite_code.setVisibility(View.GONE);
                v3.setVisibility(View.GONE);
            }
        } else {

            invite_code.setVisibility(View.GONE);
            cbPasswordLogin.setChecked(false);
            v3.setVisibility(View.GONE);
            tvForgetPassword.setVisibility(View.VISIBLE);
            cbPasswordLogin.setVisibility(View.VISIBLE);
            etPassword.setVisibility(View.VISIBLE);
            llVerify.setVisibility(View.GONE);
            etCode.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            tvUpCode.setVisibility(View.GONE);
        }
        splashBtnGo.setText(b ? "??????" : "??????");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CountryEntity countryEntity) {
        if (countryEntity != null) {
            tvMobileCode.setText(countryEntity.getCode());
            tvMobileArea.setText(countryEntity.getCountry());
            invite_code.setText("");
            registeredOrLogin(!mRegisteredOrLogin.getText().equals("??????"));
//            if (countryEntity.getCode().equals("86")) {
//                invite_code.setVisibility(View.GONE);
//            } else {
//                invite_code.setVisibility(View.VISIBLE);
//            }
        }
    }

    /**
     * ????????????????????????
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Utils.REQ_PERMISSION_CODE:
                try {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        ToastHelper.showToastLong(this, "???????????????????????????????????????????????????");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void updateSuccess(HttpResult data) {

    }

    @Override
    public void verifySuccess(HttpResult data) {
        if (data != null) {
            if (data.getCode() == 200) {
                mPresenter.register(new RegisterRequestBody(etMobile.getText().toString(), etPassword.getText().toString(), tvMobileCode.getText().toString()));
            } else {
                showToast(data.getMsg());
            }
        }
    }

    @Override
    public void registerSuccess(HttpResult data) {
        if (data != null) {
            if (data.getCode() == 200) {
                showToast(data.getMsg());
                registeredOrLogin(mRegisteredOrLogin.getText().equals("??????"));
            } else {
                showToast(data.getMsg());
            }

        }
    }

    @Override
    public void loginResult(LoginReceive data) {
        if (data.getData() != null) {
            showToast(data.getMsg());
            SPUtils.commitValue(AppConstant.USER, AppConstant.USER_TOKEN, data.getData().getToken());
//            SPUtils.commitValue(AppConstant.USER, AppConstant.USER_ID, data.getData().getMemberInfo().getId());
           /* DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, SPUtils.getValue(AppConstant.USER, AppConstant.USER_ID) + ".db");
            SQLiteDatabase database = helper.getWritableDatabase();
            DaoMaster daoMaster = new DaoMaster(database);
            MyApplication.getInstance().setDaoSession(daoMaster.newSession());*/
            /*startActivity(new Intent(this, MainActivity.class));
            finish();*/
            mPresenter.getSocketUrl();

        }
    }

    @Override
    public void socketUrl(SocketDataBean dataBean) {
        if (dataBean != null && dataBean.getCode() == 200) {
            SPUtils.commitValue(AppConstant.USER, AppConstant.USER_URL, dataBean.getData().getUrl());
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onSuccess(HttpResult data) {
        showToast(data.getMsg());
    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
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
}