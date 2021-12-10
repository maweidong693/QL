package com.weiwu.ql.main.mine.setting;

import android.os.Bundle;
import android.view.View;

import com.tencent.qcloud.tim.uikit.component.LineControllerView;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.utils.IntentUtil;

/**
 * @author Joseph_Yan
 * on 2020/12/23
 */
public class GeneralSettingActivity extends BaseActivity {

    private TitleBarLayout mTitleBar;
    private LineControllerView viewFontSize;
    private LineControllerView mChatCackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_set);
        initView();
    }

    private void initView() {
        mTitleBar = findViewById(R.id.title);
        mTitleBar.setTitle("通用", TitleBarLayout.POSITION.LEFT);
        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleBar.getRightIcon().setVisibility(View.GONE);
        viewFontSize = findViewById(R.id.view_font_size);
        mChatCackground =findViewById(R.id.set_chat_background);
        mChatCackground.setOnClickListener(view -> {
            IntentUtil.redirectToNextActivity(this, SetChatbackGroundActivity.class);
        });

        viewFontSize.setOnClickListener(view ->
                IntentUtil.redirectToNextActivity(this,FontSizeActivity.class));
    }




}
