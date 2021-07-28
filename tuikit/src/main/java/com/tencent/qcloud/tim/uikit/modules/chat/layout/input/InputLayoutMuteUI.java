package com.tencent.qcloud.tim.uikit.modules.chat.layout.input;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.tencent.qcloud.tim.uikit.modules.chat.base.BaseInputFragment;
import com.tencent.qcloud.tim.uikit.modules.chat.interfaces.IInputLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.inputmore.InputMoreActionUnit;
import com.tencent.qcloud.tim.uikit.utils.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class InputLayoutMuteUI extends LinearLayout implements IInputLayout {

    protected static final int CAPTURE = 1;
    protected static final int AUDIO_RECORD = 2;
    protected static final int VIDEO_RECORD = 3;
    protected static final int SEND_PHOTO = 4;
    protected static final int SEND_FILE = 5;
    private static String TAG = InputLayoutMuteUI.class.getSimpleName();


    protected Activity mActivity;
    protected View mInputMoreLayout;
    //    protected ShortcutArea mShortcutArea;
    protected List<InputMoreActionUnit> mInputMoreActionList = new ArrayList<>();
    protected List<InputMoreActionUnit> mInputMoreCustomActionList = new ArrayList<>();
    private AlertDialog mPermissionDialog;
    private boolean mSendPhotoDisable;
    private boolean mCaptureDisable;
    private boolean mVideoRecordDisable;
    private boolean mSendFileDisable;
    private boolean mEnableAudioCall;
    private boolean mEnableVideoCall;
    protected TextView muteText;

    public InputLayoutMuteUI(Context context) {
        super(context);
        initViews();
    }

    public InputLayoutMuteUI(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public InputLayoutMuteUI(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        mActivity = (Activity) getContext();
        inflate(mActivity, R.layout.chat_mute_layout, this);
        muteText = findViewById(R.id.mute_text);
//        mShortcutArea = findViewById(R.id.shortcut_area);
        // 子类实现所有的事件处理
    }

    public TextView  getMuteText(){
        return muteText ;
    }

    protected void assembleActions() {


    }

    protected boolean checkPermission(int type) {
        if (!PermissionUtils.checkPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            return false;
        }
        if (!PermissionUtils.checkPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            return false;
        }
        if (type == SEND_FILE || type == SEND_PHOTO) {
            return true;
        } else if (type == CAPTURE) {
            return PermissionUtils.checkPermission(mActivity, Manifest.permission.CAMERA);
        } else if (type == AUDIO_RECORD) {
            return PermissionUtils.checkPermission(mActivity, Manifest.permission.RECORD_AUDIO);
        } else if (type == VIDEO_RECORD) {
            return PermissionUtils.checkPermission(mActivity, Manifest.permission.CAMERA)
                    && PermissionUtils.checkPermission(mActivity, Manifest.permission.RECORD_AUDIO);
        }
        return true;
    }

    @Override
    public void disableAudioInput(boolean disable) {

    }

    @Override
    public void disableEmojiInput(boolean disable) {

    }

    @Override
    public void disableMoreInput(boolean disable) {

    }

    @Override
    public void replaceMoreInput(BaseInputFragment fragment) {

    }

    @Override
    public void replaceMoreInput(OnClickListener listener) {

    }

    @Override
    public void disableSendPhotoAction(boolean disable) {
        mSendPhotoDisable = disable;
    }

    @Override
    public void disableCaptureAction(boolean disable) {
        mCaptureDisable = disable;
    }

    @Override
    public void disableVideoRecordAction(boolean disable) {
        mVideoRecordDisable = disable;
    }

    @Override
    public void disableSendFileAction(boolean disable) {
        mSendFileDisable = disable;
    }

    @Override
    public boolean enableAudioCall() {
        if (TUIKitConfigs.getConfigs().getGeneralConfig().isSupportAVCall()) {
            mEnableAudioCall = true;
            return true;
        } else {
            mEnableAudioCall = false;
            return false;
        }
    }

    @Override
    public boolean enableVideoCall() {
        if (TUIKitConfigs.getConfigs().getGeneralConfig().isSupportAVCall()) {
            mEnableVideoCall = true;
            return true;
        } else {
            mEnableVideoCall = false;
            return false;
        }
    }

    @Override
    public void addAction(InputMoreActionUnit action) {
        mInputMoreCustomActionList.add(action);
    }

    @Override
    public EditText getInputText() {
        return null;
    }


    public void clearCustomActionList() {
        mInputMoreCustomActionList.clear();
    }
}
