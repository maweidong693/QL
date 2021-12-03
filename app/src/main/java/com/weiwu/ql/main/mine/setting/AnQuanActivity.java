package com.weiwu.ql.main.mine.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tencent.common.Constant;
import com.tencent.qcloud.tim.uikit.component.LineControllerView;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.weiwu.ql.R;
import com.weiwu.ql.main.login.ForgetPasswordActivity;

public class AnQuanActivity extends Activity {

    private TitleBarLayout anQuanTitle;
    private LineControllerView viewUpdatePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_an_quan);
        initView();
    }

    private void initView() {
        anQuanTitle = (TitleBarLayout) findViewById(R.id.an_quan_title);
        anQuanTitle.setTitle("安全中心", TitleBarLayout.POSITION.LEFT);
        anQuanTitle.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        anQuanTitle.getRightIcon().setVisibility(View.GONE);

        viewUpdatePassword = (LineControllerView) findViewById(R.id.view_update_password);
        viewUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnQuanActivity.this, ForgetPasswordActivity.class);
                intent.putExtra(Constant.PASSWORD_TYPE, 1);
                startActivity(intent);

            }
        });
    }
}