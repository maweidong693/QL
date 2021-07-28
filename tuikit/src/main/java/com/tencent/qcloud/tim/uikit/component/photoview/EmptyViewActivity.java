package com.tencent.qcloud.tim.uikit.component.photoview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureMediaScannerConnection;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.thread.PictureThreadUtils;
import com.luck.picture.lib.tools.DateUtils;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.luck.picture.lib.tools.ToastUtils;
import com.luck.picture.lib.tools.ValueOf;
import com.tencent.imsdk.v2.V2TIMDownloadCallback;
import com.tencent.imsdk.v2.V2TIMElem;
import com.tencent.imsdk.v2.V2TIMImageElem;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.component.face.FaceManager;
import com.tencent.qcloud.tim.uikit.utils.FileUtil;
import com.tencent.qcloud.tim.uikit.utils.PopWindowUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.io.File;
import java.io.OutputStream;
import java.util.Objects;

import okio.BufferedSource;
import okio.Okio;

import static android.view.View.inflate;


public class EmptyViewActivity extends Activity {

    private TextView mViewDescription;
    private TextView mViewTvContent;
    private LinearLayout mViewContent;
    private LinearLayout mViewllImage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去除状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarColor(this);
        }

        setContentView(R.layout.activity_empty_view);
        mViewContent = findViewById(R.id.content);
        mViewDescription = findViewById(R.id.view_description);
        mViewTvContent = findViewById(R.id.tv_content);
        mViewllImage = findViewById(R.id.ll_image);
        int msgType = getIntent().getIntExtra("msgType",0);
        String  quoteMessage = getIntent().getStringExtra("quoteMessage");
        if (msgType== V2TIMMessage.V2TIM_ELEM_TYPE_IMAGE){
            mViewDescription.setText("图片已过期或已被清理");
            findViewById(R.id.content).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }else if (msgType== V2TIMMessage.V2TIM_ELEM_TYPE_VIDEO){
            mViewDescription.setText("视频已过期或已被清理");
            findViewById(R.id.content).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }else {
            mViewContent.setBackgroundColor(getResources().getColor(R.color.status_bar_color));
            mViewllImage.setVisibility(View.GONE);
            mViewTvContent.setVisibility(View.VISIBLE);
            FaceManager.handlerEmojiText(mViewTvContent, quoteMessage, false);
//            mViewTvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
            mViewTvContent.setTextIsSelectable(true);
        }
    }




    public  void setStatusBarColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.text_color_black));
        }
    }
}
