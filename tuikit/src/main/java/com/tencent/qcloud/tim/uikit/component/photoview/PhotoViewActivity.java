package com.tencent.qcloud.tim.uikit.component.photoview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.luck.picture.lib.PictureExternalPreviewActivity;
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
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.utils.FileUtil;
import com.tencent.qcloud.tim.uikit.utils.PopWindowUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;

import java.io.File;
import java.io.OutputStream;
import java.util.Objects;

import okio.BufferedSource;
import okio.Okio;

import static android.view.View.inflate;


public class PhotoViewActivity extends Activity {

    public static V2TIMImageElem.V2TIMImage mCurrentOriginalImage;
    private PhotoView mPhotoView;
    private Matrix mCurrentDisplayMatrix = null;
    private TextView mViewOriginalBtn;
    private AlertDialog mDialog;
    private boolean isSelf ;
    private Uri uri;
    private String mMimeType;
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

        setContentView(R.layout.activity_photo_view);
        uri = FileUtil.getUriFromPath(getIntent().getStringExtra(TUIKitConstants.IMAGE_DATA));
        isSelf = getIntent().getBooleanExtra(TUIKitConstants.SELF_MESSAGE, false);
        mCurrentDisplayMatrix = new Matrix();
        mPhotoView = findViewById(R.id.photo_view);
        mPhotoView.setDisplayMatrix(mCurrentDisplayMatrix);
        mPhotoView.setOnMatrixChangeListener(new MatrixChangeListener());
        mPhotoView.setOnPhotoTapListener(new PhotoTapListener());
        mPhotoView.setOnSingleFlingListener(new SingleFlingListener());
        mViewOriginalBtn = findViewById(R.id.view_original_btn);
        if (isSelf || mCurrentOriginalImage == null) {
            mPhotoView.setImageURI(uri);
        } else {
            if (mCurrentOriginalImage != null) {
                String path = TUIKitConstants.IMAGE_DOWNLOAD_DIR + mCurrentOriginalImage.getUUID();
                File file = new File(path);
                if (file.exists()) {
                    mPhotoView.setImageURI(FileUtil.getUriFromPath(file.getPath()));
                }
                else {
                    mPhotoView.setImageURI(uri);
                    mViewOriginalBtn.setVisibility(View.VISIBLE);
                    mViewOriginalBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mCurrentOriginalImage != null) {

                                final String path = TUIKitConstants.IMAGE_DOWNLOAD_DIR + mCurrentOriginalImage.getUUID();
                                final File file = new File(path);
                                if (!file.exists()) {
                                    mCurrentOriginalImage.downloadImage(path, new V2TIMDownloadCallback() {
                                        @Override
                                        public void onProgress(V2TIMElem.V2ProgressInfo progressInfo) {

                                        }

                                        @Override
                                        public void onError(int code, String desc) {

                                        }

                                        @Override
                                        public void onSuccess() {
                                            mPhotoView.setImageURI(FileUtil.getUriFromPath(file.getPath()));
                                            mViewOriginalBtn.setText("已完成");
                                            mViewOriginalBtn.setOnClickListener(null);
                                        }
                                    });
                                } else {
                                    mPhotoView.setImageURI(FileUtil.getUriFromPath(file.getPath()));
                                }
                            }
                        }
                    });
                }

            }
        }

        findViewById(R.id.photo_view_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mPhotoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                buildPopMenu();
                return false;
            }
        });
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
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (isSelf || mCurrentOriginalImage == null) {
                            String path ;
                            if (uri.toString().startsWith("content://")){
                                path = FileUtil.getPathFromUri(uri);
                            }else {
                                path = FileUtil.getRealFilePath(uri);
                            }
//                            String path = isSelf?FileUtil.getPathFromUri(uri):FileUtil.getRealFilePath(uri);
                            if (SdkVersionUtils.checkedAndroid_Q()){
                                String currentMimeType = PictureMimeType.getImageMimeType(path);
                                mMimeType = PictureMimeType.isJPG(currentMimeType) ? PictureMimeType.MIME_TYPE_JPEG : currentMimeType;
                                savePictureAlbumAndroidQ(uri);
                            }else {
                                File file = new File(path);
                                if (file.exists()) {
                                    savePictureAlbum(path);
                                }
                            }
                        } else {
                            if (mCurrentOriginalImage != null) {
                                String path = TUIKitConstants.IMAGE_DOWNLOAD_DIR + mCurrentOriginalImage.getUUID();
                                File file = new File(path);
                                if (file.exists()) {
                                    savePictureAlbum(path);
                                }
                            }
                        }
                    } catch (Exception e) {
                        ToastUtils.s(PhotoViewActivity.this, getString(R.string.picture_save_error) + "\n" + e.getMessage());
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
     * 保存图片到picture 目录，Android Q适配，最简单的做法就是保存到公共目录，不用SAF存储
     *
     * @param inputUri
     */
    private void savePictureAlbumAndroidQ(Uri inputUri) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, DateUtils.getCreateFileName("IMG_"));
        contentValues.put(MediaStore.Images.Media.DATE_TAKEN, ValueOf.toString(System.currentTimeMillis()));
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, mMimeType);
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, PictureMimeType.DCIM);
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        if (uri == null) {
            ToastUtils.s(this, getString(R.string.picture_save_error));
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
                        return PictureFileUtils.getPath(PhotoViewActivity.this, uri);
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
     * 保存相片至本地相册
     *
     * @throws Exception
     * @param downloadPath
     */
    private void savePictureAlbum(String downloadPath) throws Exception {
        String suffix = PictureMimeType.getLastImgSuffix("image/jpg");
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
        String fileName = DateUtils.getCreateFileName("IMG_") + suffix;
        File file = new File(folderDir, fileName);
        PictureFileUtils.copyFile(downloadPath, file.getAbsolutePath());
        onSuccessful(file.getAbsolutePath());
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
                ToastUtils.s(this, getString(R.string.picture_save_success) + "\n" + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ToastUtils.s(this, getString(R.string.picture_save_error));
        }
    }


    private class PhotoTapListener implements OnPhotoTapListener {

        @Override
        public void onPhotoTap(ImageView view, float x, float y) {
            float xPercentage = x * 100f;
            float yPercentage = y * 100f;
        }
    }


    private class MatrixChangeListener implements OnMatrixChangedListener {

        @Override
        public void onMatrixChanged(RectF rect) {

        }
    }

    private class SingleFlingListener implements OnSingleFlingListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
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
