package com.weiwu.ql.main.mine.setting;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.common.dialog.DialogMaker;
import com.tencent.qcloud.tim.uikit.base.ITitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.utils.LinCleanData;

public class ClearCacheActicity extends BaseActivity {
    private TitleBarLayout mTitleBar;
    private TextView mCacheSize;
    private Button mBtnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_clear_cache);
        initView();
    }

    public void initView(){
        mTitleBar = findViewById(R.id.clear_cache_title_bar);
        mCacheSize = findViewById(R.id.cache_size);
        mBtnClear = findViewById(R.id.btn_send);
        mTitleBar.setTitle("清理缓存", ITitleBarLayout.POSITION.LEFT);
        String allCacheSize = LinCleanData.getAllCacheSize(this);
        if (allCacheSize.equals("0K")){
            mBtnClear.setEnabled(false);
        }else {
            mBtnClear.setEnabled(true);
        }
        mCacheSize.setText(allCacheSize);
        mTitleBar.setOnLeftClickListener(v -> finish());
        mTitleBar.getRightGroup().setVisibility(View.GONE);
        mBtnClear.setOnClickListener(v -> {
            DialogMaker.showProgressDialog(this,"清理中…",false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    LinCleanData.clearAllCache(ClearCacheActicity.this);
                    String allCacheSize = LinCleanData.getAllCacheSize(ClearCacheActicity.this);
                    if (allCacheSize.equals("0K")){
                        mBtnClear.setEnabled(false);
                    }else {
                        mBtnClear.setEnabled(true);
                    }
                    mCacheSize.setText(allCacheSize);
                    DialogMaker.dismissProgressDialog();
                }
            }, 1500);

        });
    }
}
