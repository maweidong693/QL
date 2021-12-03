package com.weiwu.ql.main.mine.collection;

import static android.view.View.inflate;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.face.FaceManager;
import com.tencent.qcloud.tim.uikit.utils.PopWindowUtil;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;


public class MyTextFavoritesActivity extends BaseActivity {
    private TitleBarLayout mTitleBar;
    private TextView tv_content;
    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_favorites);
        initView();
    }

    private void initView() {
        FavoritesData.FavoritesItem item = new FavoritesData.FavoritesItem();
        try {
            item = (FavoritesData.FavoritesItem) getIntent().getSerializableExtra("data");
        } catch (Exception e) {
            e.printStackTrace();
        }

        mTitleBar = findViewById(R.id.title);
        mTitleBar.setBackgroundColor(getResources().getColor(R.color.white));
        mTitleBar.setTitle(getResources().getString(R.string.message_details), TitleBarLayout.POSITION.MIDDLE);
        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleBar.getRightIcon().setVisibility(View.GONE);
        tv_content = findViewById(R.id.tv_content);
        FaceManager.handlerEmojiText(tv_content, item.getContent(), false);
        tv_content.setOnLongClickListener(v ->
                {
                    buildPopMenu();


                    return false;
                }
        );
    }

    private void buildPopMenu() {
        if (mDialog == null) {
            mDialog = PopWindowUtil.buildFullScreenDialog(this);
            View moreActionView = inflate(this, com.tencent.qcloud.tim.uikit.R.layout.save_img_or_video_pop_menu, null);
            moreActionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                }
            });
            Button addBtn = moreActionView.findViewById(com.tencent.qcloud.tim.uikit.R.id.add_group_member);
            addBtn.setText("复制");
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    if (clipboard == null) {
                        return;
                    }
                    ClipData clip = ClipData.newPlainText("message", tv_content.getText().toString());
                    clipboard.setPrimaryClip(clip);

                    ToastUtil.toastShortMessage("已复制");
                    mDialog.dismiss();

                }
            });
            Button deleteBtn = moreActionView.findViewById(com.tencent.qcloud.tim.uikit.R.id.remove_group_member);

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mDialog.dismiss();
                }
            });
            Button cancelBtn = moreActionView.findViewById(com.tencent.qcloud.tim.uikit.R.id.cancel);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                }
            });
            mDialog.setContentView(moreActionView);
        } else {
            mDialog.show();
        }

    }
}