package com.weiwu.ql.main.mine.setting;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.utils.AppUtils;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;

/**
 * 关于我们
 */
public class AboutUsActivity extends BaseActivity {
    protected TextView versionName;
    protected LinearLayout  mllVersionUpdate;
    private TitleBarLayout mTitleBar;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        setContentView(R.layout.activity_about);
        initView();
    }

    private void initView() {
        mTitleBar = findViewById(R.id.title);
//        mTitleBar.setBackgroundColor(getResources().getColor(R.color.white));
//        mTitleBar.setTitle(getResources().getString(R.string.about_im), TitleBarLayout.POSITION.LEFT);
        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleBar.getRightIcon().setVisibility(View.GONE);
        mllVersionUpdate = findViewById(R.id.ll_version_update);
        versionName = findViewById(R.id.version_name);
        versionName.setText("Version "+ AppUtils.packageName(this));
        mllVersionUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Beta.checkUpgrade();
            }
        });
    }
}