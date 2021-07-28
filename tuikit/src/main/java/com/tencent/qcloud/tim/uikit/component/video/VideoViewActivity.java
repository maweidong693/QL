package com.tencent.qcloud.tim.uikit.component.video;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.luck.picture.lib.PictureMediaScannerConnection;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.thread.PictureThreadUtils;
import com.luck.picture.lib.tools.DateUtils;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.luck.picture.lib.tools.ToastUtils;
import com.luck.picture.lib.tools.ValueOf;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.component.photoview.PhotoViewActivity;
import com.tencent.qcloud.tim.uikit.component.video.proxy.IPlayer;
import com.tencent.qcloud.tim.uikit.utils.FileUtil;
import com.tencent.qcloud.tim.uikit.utils.ImageUtil;
import com.tencent.qcloud.tim.uikit.utils.PopWindowUtil;
import com.tencent.qcloud.tim.uikit.utils.ScreenUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;

import java.io.File;
import java.io.OutputStream;
import java.util.Objects;

import okio.BufferedSource;
import okio.Okio;

import static android.view.View.inflate;

public class VideoViewActivity extends Activity {

    private static final String TAG = VideoViewActivity.class.getSimpleName();

    private UIKitVideoView mVideoView;
    private int videoWidth = 0;
    private int videoHeight = 0;
    private AlertDialog mDialog;
    private  Uri videoUri ;
    private  boolean isSelf ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TUIKitLog.i(TAG, "onCreate start");
        super.onCreate(savedInstanceState);
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去除状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarColor(this);
        }
        setContentView(R.layout.activity_video_view);
        mVideoView = findViewById(R.id.video_play_view);

        String imagePath = getIntent().getStringExtra(TUIKitConstants.CAMERA_IMAGE_PATH);
        videoUri = getIntent().getParcelableExtra(TUIKitConstants.CAMERA_VIDEO_PATH);
        isSelf = getIntent().getBooleanExtra(TUIKitConstants.SELF_MESSAGE, false);
        Bitmap firstFrame = ImageUtil.getBitmapFormPath(imagePath);
        if (firstFrame != null) {
            videoWidth = firstFrame.getWidth();
            videoHeight = firstFrame.getHeight();
            updateVideoView();
        }

        mVideoView.setVideoURI(videoUri);
        mVideoView.setOnPreparedListener(new IPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IPlayer mediaPlayer) {
                mVideoView.start();
            }
        });
        mVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                } else {
                    mVideoView.start();
                }
            }
        });
        mVideoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                buildPopMenu();
                return false;
            }
        });
        findViewById(R.id.video_view_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.stop();
                finish();
            }
        });
        TUIKitLog.i(TAG, "onCreate end");
    }
    private void buildPopMenu() {
        if (mDialog == null) {
            mDialog = PopWindowUtil.buildFullScreenDialog(this);
            View moreActionView = inflate(this, R.layout.save_img_or_video_pop_menu, null);
            moreActionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                }
            });
            Button addBtn = moreActionView.findViewById(R.id.add_group_member);
            addBtn.setText("保存视频");
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
//                        savePictureAlbumAndroidQ(videoUri); //自己发的视频（后续扩展）
                        String path ;
                        if (videoUri.toString().startsWith("content://")){
                             path = FileUtil.getPathFromUri(videoUri);
                        }else {
                             path = FileUtil.getRealFilePath(videoUri);
                        }
                        File file = new File(path);
                        if (file.exists()) {
                            savePictureAlbum(path);
                        }

//                        insertVideoToMediaStore(VideoViewActivity.this,FileUtil.getPathFromUri(videoUri),0,0,0,5000);

                    } catch (Exception e) {
                        ToastUtils.s(VideoViewActivity.this, getString(R.string.video_save_error) + "\n" + e.getMessage());
                        e.printStackTrace();
                    }

                    mDialog.dismiss();

                }
            });
            Button deleteBtn = moreActionView.findViewById(R.id.remove_group_member);

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mDialog.dismiss();
                }
            });
            Button cancelBtn = moreActionView.findViewById(R.id.cancel);
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
    /**
     * 保存视频至本地相册
     *
     * @throws Exception
     * @param downloadPath
     */
    private void savePictureAlbum(String downloadPath) throws Exception {
        String suffix = PictureMimeType.getLastImgSuffix( PictureMimeType.MIME_TYPE_VIDEO);
        String state = Environment.getExternalStorageState();
        File rootDir = state.equals(Environment.MEDIA_MOUNTED)
                ? Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                : getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (rootDir != null && !rootDir.exists() && rootDir.mkdirs()) {
        }
        File folderDir = new File(SdkVersionUtils.checkedAndroid_Q() || !state.equals(Environment.MEDIA_MOUNTED)
                ? rootDir.getAbsolutePath() : rootDir.getAbsolutePath() + File.separator + PictureMimeType.CAMERA + File.separator);
        if (folderDir != null && !folderDir.exists() && folderDir.mkdirs()) {
        }
        String fileName = DateUtils.getCreateFileName("VIDEO_") + suffix;
        File file = new File(folderDir, fileName);
        PictureFileUtils.copyFile(downloadPath, file.getAbsolutePath());
        onSuccessful(file.getAbsolutePath());
    }



    /**
     * 保存视频到picture 目录，Android Q适配，最简单的做法就是保存到公共目录，不用SAF存储
     *
     * @param inputUri
     */
    private void savePictureAlbumAndroidQ(Uri inputUri) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Video.VideoColumns.DISPLAY_NAME, DateUtils.getCreateFileName("VIDEO_"));
        contentValues.put(MediaStore.Video.VideoColumns.DATE_TAKEN, ValueOf.toString(System.currentTimeMillis()));
        contentValues.put(MediaStore.Video.VideoColumns.MIME_TYPE, PictureMimeType.MIME_TYPE_VIDEO);
        contentValues.put(MediaStore.Video.VideoColumns.RELATIVE_PATH, PictureMimeType.MP4_Q);
        Uri uri = getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
        if (uri == null) {
            ToastUtils.s(this, getString(R.string.video_save_error));
            return;
        }
        PictureThreadUtils.executeByIo(new PictureThreadUtils.SimpleTask<String>() {

            @Override
            public String doInBackground() {
                BufferedSource buffer = null;
                try {
                    buffer = Okio.buffer(Okio.source(Objects.requireNonNull(getContentResolver().openInputStream(inputUri))));
                    OutputStream outputStream = getContentResolver().openOutputStream(uri);
                    boolean bufferCopy = PictureFileUtils.bufferCopy(buffer, outputStream);
                    if (bufferCopy) {
                        return PictureFileUtils.getPath(VideoViewActivity.this, uri);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (buffer != null && buffer.isOpen()) {
                        PictureFileUtils.close(buffer);
                    }
                }
                return "";
            }

            @Override
            public void onSuccess(String result) {
                PictureThreadUtils.cancel(PictureThreadUtils.getIoPool());
                onSuccessful(result);
            }
        });
    }
    /**
     * 图片保存成功
     *
     * @param result
     */
    private void onSuccessful(String result) {
        if (!TextUtils.isEmpty(result)) {
            try {
                if (!SdkVersionUtils.checkedAndroid_Q()) {
                    File file = new File(result);
                    MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), null);
                    new PictureMediaScannerConnection(this, file.getAbsolutePath(), () -> {
                    });
                }
                ToastUtils.s(this, getString(R.string.video_save_success) + "\n" + result);
            } catch (Exception e) {
                ToastUtils.s(this, getString(R.string.video_save_success) + "\n" + result);
                e.printStackTrace();
            }
        } else {
            ToastUtils.s(this, getString(R.string.video_save_error));
        }
    }

    private static long getTimeWrap(long time) {
        if (time <= 0) {
            return System.currentTimeMillis();
        }
        return time;
    }

        @Override
    public void onConfigurationChanged(Configuration newConfig) {
        TUIKitLog.i(TAG, "onConfigurationChanged start");
        super.onConfigurationChanged(newConfig);
        updateVideoView();
        TUIKitLog.i(TAG, "onConfigurationChanged end");
    }

    private void updateVideoView() {
        TUIKitLog.i(TAG, "updateVideoView videoWidth: " + videoWidth + " videoHeight: " + videoHeight);
        if (videoWidth <= 0 && videoHeight <= 0) {
            return;
        }
        boolean isLandscape = true;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            isLandscape = false;
        }

        int deviceWidth;
        int deviceHeight;
        if (isLandscape) {
            deviceWidth = Math.max(ScreenUtil.getScreenWidth(this), ScreenUtil.getScreenHeight(this));
            deviceHeight = Math.min(ScreenUtil.getScreenWidth(this), ScreenUtil.getScreenHeight(this));
        } else {
            deviceWidth = Math.min(ScreenUtil.getScreenWidth(this), ScreenUtil.getScreenHeight(this));
            deviceHeight = Math.max(ScreenUtil.getScreenWidth(this), ScreenUtil.getScreenHeight(this));
        }
        int[] scaledSize = ScreenUtil.scaledSize(deviceWidth, deviceHeight, videoWidth, videoHeight);
        TUIKitLog.i(TAG, "scaled width: " + scaledSize[0] + " height: " + scaledSize[1]);
        ViewGroup.LayoutParams params = mVideoView.getLayoutParams();
        params.width = scaledSize[0];
        params.height = scaledSize[1];
        mVideoView.setLayoutParams(params);
    }
    public  void setStatusBarColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.text_color_black));
        }
    }
    @Override
    protected void onStop() {
        TUIKitLog.i(TAG, "onStop");
        super.onStop();
        if (mVideoView != null) {
            mVideoView.stop();
        }
    }
}
