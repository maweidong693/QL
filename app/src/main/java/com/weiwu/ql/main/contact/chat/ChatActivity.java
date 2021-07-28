package com.weiwu.ql.main.contact.chat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tencent.qcloud.tim.uikit.base.ITitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.weiwu.ql.AppConstant;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.repositories.ContactRepository;
import com.weiwu.ql.main.contact.ContactContract;
import com.weiwu.ql.main.contact.chat.adapter.Adapter_ChatMessage;
import com.weiwu.ql.main.contact.chat.im.JWebSocketClient;
import com.weiwu.ql.main.contact.chat.im.JWebSocketClientService;
import com.weiwu.ql.main.contact.chat.modle.ChatMessage;
import com.weiwu.ql.main.contact.chat.util.Util;
import com.weiwu.ql.utils.GlideEngine;
import com.weiwu.ql.utils.SystemFacade;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ChatActivity extends BaseActivity implements View.OnClickListener, ContactContract.IChatView {

    private Context mContext;
    private JWebSocketClient client;
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSClientService;
    private EditText et_content;
    private ListView listView;
    private Button btn_send;
    private List<ChatMessage> chatMessageList = new ArrayList<>();//消息列表
    private Adapter_ChatMessage adapter_chatMessage;
    private ChatMessageReceiver chatMessageReceiver;
    private ConstraintLayout mClChatMore;
    private ImageView mIvChatPic;
    private TextView mTvChatPic;
    private ImageView mIvChatTake;
    private TextView mTvChatTake;
    private ImageView btMore;
    //    private String mBankPicPath = "";
    private ProgressDialog mProgressDialog;
    private ContactContract.IChatPresenter mPresenter;
    private List<LocalMedia> media = new ArrayList<>();
    private String msgType = "";

    @Override
    protected void onDestroy() {
        super.onDestroy();
        jWebSClientService.onDestroy();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("MainActivity", "服务与活动成功绑定");
            binder = (JWebSocketClientService.JWebSocketClientBinder) iBinder;
            jWebSClientService = binder.getService();
            client = jWebSClientService.client;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("MainActivity", "服务与活动成功断开");
        }
    };
    private ContactItemBean mFriendInfo;
    private TitleBarLayout mTitleBar;


    private class ChatMessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            ChatMessage receiveMessage = new Gson().fromJson(message, ChatMessage.class);
            if (mFriendInfo.getId().equals(receiveMessage.getMemberId())) {
                receiveMessage.setIsMeSend(0);
                chatMessageList.add(receiveMessage);
                initChatMsgListView();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        if (intent != null) {
            mFriendInfo = (ContactItemBean) intent.getSerializableExtra(TUIKitConstants.ProfileType.CONTENT);
        }
        mContext = ChatActivity.this;
        setPresenter(new ChatPresenter(ContactRepository.getInstance()));
        findViewById();
        initView();
        //启动服务
        startJWebSClientService();
        //绑定服务
        bindService();
        //注册广播
        doRegisterReceiver();
        //检测通知是否开启
        checkNotification(mContext);
    }

    /**
     * 绑定服务
     */
    private void bindService() {
        Intent bindIntent = new Intent(mContext, JWebSocketClientService.class);
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    /**
     * 启动服务（websocket客户端服务）
     */
    private void startJWebSClientService() {
        Intent intent = new Intent(mContext, JWebSocketClientService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android8.0以上通过startForegroundService启动service
            startForegroundService(intent);
        } else {
            startService(intent);
        }
//        startService(intent);
    }

    /**
     * 动态注册广播
     */
    private void doRegisterReceiver() {
        chatMessageReceiver = new ChatMessageReceiver();
        IntentFilter filter = new IntentFilter("com.xch.servicecallback.content");
        registerReceiver(chatMessageReceiver, filter);
    }


    private void findViewById() {
        mTitleBar = findViewById(R.id.chat_titlebar);
        mTitleBar.setTitle(mFriendInfo.getNickname(), TitleBarLayout.POSITION.LEFT);
        mTitleBar.getRightGroup().setVisibility(View.GONE);
        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        listView = findViewById(R.id.chatmsg_listView);
        btn_send = findViewById(R.id.btn_send);
        et_content = findViewById(R.id.et_content);
        btMore = findViewById(R.id.btn_multimedia);
        btn_send.setOnClickListener(this);
        btMore.setOnClickListener(this);
    }

    private void initView() {
        //监听输入框的变化
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_content.getText().toString().length() > 0) {
                    btn_send.setVisibility(View.VISIBLE);
                } else {
                    btn_send.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mClChatMore = (ConstraintLayout) findViewById(R.id.cl_chat_more);
        mIvChatPic = (ImageView) findViewById(R.id.iv_chat_pic);
        mTvChatPic = (TextView) findViewById(R.id.tv_chat_pic);
        mIvChatTake = (ImageView) findViewById(R.id.iv_chat_take);
        mTvChatTake = (TextView) findViewById(R.id.tv_chat_take);
        mIvChatPic.setOnClickListener(this);
        mTvChatPic.setOnClickListener(this);
        mIvChatTake.setOnClickListener(this);
        mTvChatTake.setOnClickListener(this);
        mProgressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                String content = et_content.getText().toString();
                if (content.length() <= 0) {
                    Util.showToast(mContext, "消息不能为空哟");
                    return;
                }

                if (client != null && client.isOpen()) {
                    ChatMessage message = new ChatMessage("message", "member", "msg", mFriendInfo.getId(), content, "");
                    String s = new Gson().toJson(message);
                    jWebSClientService.sendMsg(s);
                    message.setIsMeSend(1);
                    chatMessageList.add(message);
                    initChatMsgListView();
                    et_content.setText("");
                } else {
                    Util.showToast(mContext, "连接已断开，请稍等或重启App哟");
                }
                break;

            case R.id.btn_multimedia:
                mClChatMore.setVisibility(View.VISIBLE);
                SystemFacade.hideSoftKeyboard(this);
                break;

            case R.id.iv_chat_pic:
            case R.id.tv_chat_pic:
                checkPermissionAndCamera();
                break;
            default:
                break;
        }
    }

    private void initChatMsgListView() {
        adapter_chatMessage = new Adapter_ChatMessage(mContext, chatMessageList);
        listView.setAdapter(adapter_chatMessage);
        listView.setSelection(chatMessageList.size());
    }


    /**
     * 检测是否开启通知
     *
     * @param context
     */
    private void checkNotification(final Context context) {
        if (!isNotificationEnabled(context)) {
            new AlertDialog.Builder(context).setTitle("温馨提示")
                    .setMessage("你还未开启系统通知，将影响消息的接收，要去开启吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setNotification(context);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        }
    }

    /**
     * 如果没有开启通知，跳转至设置界面
     *
     * @param context
     */
    private void setNotification(Context context) {
        Intent localIntent = new Intent();
        //直接跳转到应用通知设置的代码：
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            localIntent.putExtra("app_package", context.getPackageName());
            localIntent.putExtra("app_uid", context.getApplicationInfo().uid);
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.addCategory(Intent.CATEGORY_DEFAULT);
            localIntent.setData(Uri.parse("package:" + context.getPackageName()));
        } else {
            //4.4以下没有从app跳转到应用通知设置页面的Action，可考虑跳转到应用详情页面,
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
            } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.setAction(Intent.ACTION_VIEW);
                localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
                localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
            }
        }
        context.startActivity(localIntent);
    }

    /**
     * 获取通知权限,监测是否开启了系统通知
     *
     * @param context
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean isNotificationEnabled(Context context) {

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 申请相机权限的requestCode
    private static final int CAMERA_REQUEST_CODE = 0x00000012;

    /**
     * 检查权限并拍照。
     * 调用相机前先检查权限。
     */
    private void checkPermissionAndCamera() {
        int hasCameraPermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.CAMERA);
        if (hasCameraPermission == PackageManager.PERMISSION_GRANTED) {
            //有调起相机拍照。
            openCamera();
        } else {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQUEST_CODE);
        }
    }

    /**
     * 处理权限申请的回调。
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //允许权限，有调起相机拍照。
                openCamera();
            } else {
                //拒绝权限，弹出提示框。
                Toast.makeText(this, "拍照权限被拒绝", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openCamera() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofAll())
                .selectionMode(PictureConfig.MULTIPLE)
                .isSingleDirectReturn(false)
                .previewImage(true)
                .isWithVideoImage(false)//图片和视频是否可以同选,只在ofAll模式下有效
                .loadImageEngine(GlideEngine.createGlideEngine())
                .isCamera(false)
                .compress(true)
                .forResult(AppConstant.BANK_CARD_PIC_REQUEST);
    }

    private static final String TAG = "aaabbb";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == AppConstant.BANK_CARD_PIC_REQUEST) {
            media = PictureSelector.obtainMultipleResult(data);

            for (int i = 0; i < media.size(); i++) {
                mProgressDialog.setMessage("正在提交中，请稍后");
                mProgressDialog.show();
                String mimeType = media.get(i).getMimeType();
                if (mimeType.contains("image")) {
                    msgType = "img";
                } else if (mimeType.contains("video")) {
                    msgType = "video";
                }
                Log.d(TAG, "onActivityResult: " + mimeType);
                String mBankPicPath = "";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    mBankPicPath = media.get(i).getAndroidQToPath();
                } else {
                    mBankPicPath = media.get(i).getPath();
                }
                File file = new File(mBankPicPath);
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                mPresenter.uploadPic(body);
            }

        }
    }

    @Override
    public void uploadPicResult(LoginData data) {
        if (data != null) {
            if (client != null && client.isOpen()) {
                ChatMessage message = new ChatMessage("message", "member", msgType, mFriendInfo.getId(), "", data.getData());
                String s = new Gson().toJson(message);
                jWebSClientService.sendMsg(s);
                message.setIsMeSend(1);
                chatMessageList.add(message);
                initChatMsgListView();
                mProgressDialog.dismiss();
            } else {
                Util.showToast(mContext, "连接已断开，请稍等或重启App哟");
            }
        }
    }

    @Override
    public void onFail(String msg, int code) {
        mProgressDialog.cancel();
        showToast(msg);
    }

    @Override
    public void setPresenter(ContactContract.IChatPresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return this;
    }
}