package com.weiwu.ql.main.mine.setting;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.common.Constant;
import com.tencent.qcloud.tim.uikit.base.ITitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.utils.DensityUtils;
import com.weiwu.ql.view.FontSizeView;


public class FontSizeActivity extends BaseActivity {

    FontSizeView fsvFontSize;
    TextView tv_font_size1;
    TextView tv_font_size2;
    TextView tv_font_size3;
    private float fontSizeScale;
    private boolean isChange;//用于监听字体大小是否有改动
    private int defaultPos;
    private Button btnSend;
    private TitleBarLayout mTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_font_size);
        initView();
    }

    public void initView() {
        fsvFontSize = findViewById(R.id.fsv_font_size);
        tv_font_size1 = findViewById(R.id.tv_font_size1);
        tv_font_size2 = findViewById(R.id.tv_font_size2);
        tv_font_size3 = findViewById(R.id.tv_font_size3);
        mTitleBar = findViewById(R.id.friend_title_bar);
        mTitleBar.setTitle("字体大小", ITitleBarLayout.POSITION.LEFT);
        View inflate = LayoutInflater.from(this).inflate(R.layout.title_button, null);
        btnSend = inflate.findViewById(R.id.btn_send);
        btnSend.setText("完成");
        btnSend.setEnabled(true);
        mTitleBar.getRightGroup().removeAllViews();
        mTitleBar.getRightGroup().addView(inflate);
        mTitleBar.setOnLeftClickListener(v -> finish());
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isChange){
                    YPreferencesUtils.put(Constant.SP_FontScale,fontSizeScale);
                }
                finish();
            }
        });
        //滑动返回监听
        fsvFontSize.setChangeCallbackListener(new FontSizeView.OnChangeCallbackListener() {
            @Override
            public void onChangeListener(int position) {
                int dimension = getResources().getDimensionPixelSize(R.dimen.sp_stander);
                //根据position 获取字体倍数
                fontSizeScale = (float) (0.875 + 0.125 * position);
                //放大后的sp单位
                double v = fontSizeScale * (int) DensityUtils.px2sp(FontSizeActivity.this, dimension);
                //改变当前页面大小
                changeTextSize((int) v);
                isChange = !(position==defaultPos);
            }
        });

        float  scale = (float) YPreferencesUtils.get(Constant.SP_FontScale,0.0f);
        if (scale > 0.5) {
            defaultPos = (int) ((scale - 0.875) / 0.125);
        } else {
            defaultPos=1;
        }
        //注意： 写在改变监听下面 —— 否则初始字体不会改变
        fsvFontSize.setDefaultPosition(defaultPos);
    }

    /**
     * 改变textsize 大小
     */
    private void changeTextSize(int dimension) {
        tv_font_size1.setTextSize(dimension);
        tv_font_size2.setTextSize(dimension);
        tv_font_size3.setTextSize(dimension);
    }

    /**
     * 重新配置缩放系数
     * @return
     */
    @Override
    public Resources getResources() {
        Resources res =super.getResources();
        Configuration config = res.getConfiguration();
        config.fontScale= 1;//1 设置正常字体大小的倍数
        res.updateConfiguration(config,res.getDisplayMetrics());
        return res;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);
    }

}