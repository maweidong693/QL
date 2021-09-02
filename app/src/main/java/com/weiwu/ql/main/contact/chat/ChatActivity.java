package com.weiwu.ql.main.contact.chat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.gyf.immersionbar.OnKeyboardListener;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfoData;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.weiwu.ql.AppConstant;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.data.bean.FunctionData;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.bean.MessageListData;
import com.weiwu.ql.data.bean.OrderData;
import com.weiwu.ql.data.repositories.ContactRepository;
import com.weiwu.ql.greendao.db.ChatMessageDao;
import com.weiwu.ql.greendao.db.DaoMaster;
import com.weiwu.ql.greendao.db.DaoSession;
import com.weiwu.ql.greendao.db.MessageListDataDao;
import com.weiwu.ql.main.contact.ContactContract;
import com.weiwu.ql.main.contact.chat.adapter.Adapter_ChatMessage;
import com.weiwu.ql.main.contact.chat.adapter.ChatMessageAdapter;
import com.weiwu.ql.main.contact.chat.im.JWebSocketClient;
import com.weiwu.ql.main.contact.chat.im.JWebSocketClientService;
import com.weiwu.ql.main.contact.chat.modle.ChatMessage;
import com.weiwu.ql.main.contact.chat.modle.NoticeOrderData;
import com.weiwu.ql.main.contact.chat.util.Util;
import com.weiwu.ql.main.contact.group.GroupInfoActivity;
import com.weiwu.ql.main.find.OrderDetailActivity;
import com.weiwu.ql.utils.AudioRecoderUtils;
import com.weiwu.ql.utils.GlideEngine;
import com.weiwu.ql.utils.SPUtils;
import com.weiwu.ql.utils.SystemFacade;
import com.weiwu.ql.view.MessageLongPopup;
import com.weiwu.ql.view.VoicePopup;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.github.rockerhieu.emojicon.EmojiconGridFragment;
import io.github.rockerhieu.emojicon.EmojiconsFragment;
import io.github.rockerhieu.emojicon.emoji.Emojicon;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ChatActivity extends BaseActivity implements View.OnClickListener, ContactContract.IChatView, EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {

    private Context mContext;
    private JWebSocketClient client;
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSClientService;
    private EditText et_content;
    private RecyclerView listView;
    private Button btn_send;
    private List<ChatMessage> chatMessageList = new ArrayList<>();//消息列表
    private ChatMessageAdapter mAdapter;
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
    private ImageView voiceOrText;
    private boolean isVoice = false;
    private RelativeLayout rlInput;
    private Button btVoice;
    private VoicePopup mVoicePopup;
    private AudioRecoderUtils mAudioRecoderUtils;
    private double mPosX;
    private double mPosY;
    private double mCurPosX;
    private double mCurPosY;
    private TextView mVoiceHint;
    private ChatMessageDao mChatMessageDao;
    private QueryBuilder<ChatMessage> qb;
    private String mReceiverType;
    private MessageListDataDao messageListDataDao;
    private Button btMute;
    private TextView tv_quote;
    private ChatMessage mQuoteMessage;
    private DaoSession daoSession;
    private ImageView bt_face;

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        jWebSClientService.onDestroy();
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

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(et_content, emojicon);
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(et_content);
    }

    @Override
    public void onBackPressed() {
        if (hasClick) {
            findViewById(R.id.emojicons).setVisibility(View.GONE);
            hasClick = !hasClick;
        } else {
            super.onBackPressed();
        }
    }

    private class ChatMessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            ChatMessage receiveMessage = new Gson().fromJson(message, ChatMessage.class);
            if (receiveMessage.getType().equals("notice")) {
                if (mReceiverType.equals("group")) {
                    mPresenter.getGroupInfo(mFriendInfo.getId());
                }
            }

            if (mReceiverType.equals("member") && mFriendInfo.getId().equals(receiveMessage.getMemberId())) {
                setChatMessage();
            } else if (mReceiverType.equals("group") && mFriendInfo.getId().equals(receiveMessage.getReceiverId())) {
                setChatMessage();
            }
        }
    }

    private static final String TAG = "ChatActivity";

    private void setEmojiconFragment(boolean useSystemDefault) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault))
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_chat);
        ImmersionBar.with(this).keyboardEnable(true)  //解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)  //单独指定软键盘模式
                .setOnKeyboardListener(new OnKeyboardListener() {    //软键盘监听回调，keyboardEnable为true才会回调此方法
                    @Override
                    public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
//                        listView.setSelection(mAdapter.getCount() - 1);
                        listView.smoothScrollToPosition(mAdapter.getItemCount());
                        if (isPopup) {
                            mClChatMore.setVisibility(View.GONE);
                        }
                    }
                })
                .init();
        Intent intent = getIntent();
        if (intent != null) {
            mFriendInfo = (ContactItemBean) intent.getSerializableExtra(TUIKitConstants.ProfileType.CONTENT);
            mReceiverType = intent.getStringExtra(AppConstant.CHAT_TYPE);
        }

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, SPUtils.getValue(AppConstant.USER, AppConstant.USER_ID) + ".db");
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        daoSession.clear();

        mContext = ChatActivity.this;
        setPresenter(new ChatPresenter(ContactRepository.getInstance()));
        findViewById();
        initView();
        setEmojiconFragment(false);
        if (mReceiverType.equals("group")) {
            mPresenter.getGroupInfo(mFriendInfo.getId());
        }
        //启动服务
//        startJWebSClientService();
        //绑定服务
        bindService();
        //注册广播
        doRegisterReceiver();
        //检测通知是否开启
//        checkNotification(mContext);
    }

    /**
     * 绑定服务
     */
    private void bindService() {
        Intent bindIntent = new Intent(mContext, JWebSocketClientService.class);
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    /* *//**
     * 启动服务（websocket客户端服务）
     *//*
    private void startJWebSClientService() {
        Intent intent = new Intent(mContext, JWebSocketClientService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android8.0以上通过startForegroundService启动service
            startForegroundService(intent);
        } else {
            startService(intent);
        }
//        startService(intent);
    }*/

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
        mTitleBar.setRightIcon(R.drawable.more);
        mTitleBar.getRightIcon().setImageResource(R.drawable.more);

        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (mReceiverType.equals("group")) {
            mTitleBar.getRightGroup().setVisibility(View.VISIBLE);
            mTitleBar.setOnRightClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ChatActivity.this, GroupInfoActivity.class);
                    intent.putExtra(TUIKitConstants.Group.GROUP_ID, mFriendInfo.getId());
                    startActivityForResult(intent, 1000);
//                    startActivity(intent);
//                    finish();
                }
            });
        } else {
            mTitleBar.getRightGroup().setVisibility(View.GONE);
        }
        listView = findViewById(R.id.chatmsg_listView);
        listView.setOnClickListener(this);
        listView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ChatMessageAdapter();
        listView.setAdapter(mAdapter);
        mAdapter.setMessageLongClickListener(new ChatMessageAdapter.IMessageLongClickListener() {
            @Override
            public void mLongClick(ChatMessage message, View view) {
                mQuoteMessage = message;
                if (TextUtils.isEmpty(mQuoteMessage.getMemberNickname())) {
                    mQuoteMessage.setMemberNickname(SPUtils.getValue(AppConstant.USER, AppConstant.USER_NAME));

                }
                PopupMenu popupMenu = new PopupMenu(ChatActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_message, popupMenu.getMenu());
                Menu menu = popupMenu.getMenu();
                MenuItem withdrawMenuItem = menu.getItem(1);
                if (message.getIsMeSend() == 0) {
                    withdrawMenuItem.setVisible(false);
                } else
                    withdrawMenuItem.setVisible(message.getIsMeSend() == 1 && System.currentTimeMillis() - Long.parseLong(message.getSendTime()) < 20000);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.tv_message_quote) {
                            tv_quote.setVisibility(View.VISIBLE);
                            String quoteContent = "";
                            if (mQuoteMessage.getMsgType().equals("img")) {
                                quoteContent = "[图片]";
                            } else if (mQuoteMessage.getMsgType().equals("video")) {
                                quoteContent = "[视频]";

                            } else {
                                quoteContent = SystemFacade.base64ToString(mQuoteMessage.getTextMsg());
                            }
                            tv_quote.setText(mQuoteMessage.getMemberNickname() + ":" + quoteContent);
                            /*chatData.remove(pos);
                            chatAdapter.notifyItemRemoved(pos);*/
                        } else if (item.getItemId() == R.id.tv_message_withdraw) {
                            FunctionData.FunctionArgDTO functionArgDTO = new FunctionData.FunctionArgDTO(901, "撤回");
                            FunctionData function = new FunctionData("function", mReceiverType, mFriendInfo.getId(), message.getMessageId(), functionArgDTO);
                            String s = new Gson().toJson(function);
                            jWebSClientService.sendMsg(s);
                            mChatMessageDao.deleteByKey(message.getMessageId());
                            setChatMessage();
                        }
                        return false;
                    }
                });
//                Log.d(TAG, "mLongClick: " + message.getTextMsg() + "--------" + x + "-----" + y);
            }
        });
//        registerForContextMenu(listView);

        /*listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemLongClick: " + position);
                return true;
            }
        });*/

        mAdapter.setOrderNoticeClickListener(new ChatMessageAdapter.IOrderNoticeClickListener() {
            @Override
            public void mOrderClick(NoticeOrderData data) {
                OrderData.DataDTO.ListDTO dto = new OrderData.DataDTO.ListDTO();
                dto.setId(data.getId());
                Intent intent = new Intent(ChatActivity.this, OrderDetailActivity.class);
                intent.putExtra(AppConstant.ORDER_INFO, dto);
                startActivity(intent);
            }
        });

        btn_send = findViewById(R.id.btn_send);
        et_content = findViewById(R.id.et_content);
        tv_quote = findViewById(R.id.tv_input_quote);
        bt_face = findViewById(R.id.btn_face);
        btMore = findViewById(R.id.btn_multimedia);
        voiceOrText = findViewById(R.id.btn_voice_or_text);
        btVoice = findViewById(R.id.btn_voice);
        btMute = findViewById(R.id.bt_mute);
        rlInput = findViewById(R.id.rl_input);
        voiceOrText.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        btMore.setOnClickListener(this);
        bt_face.setOnClickListener(this);
        et_content.setOnClickListener(this);

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

        mVoicePopup = new VoicePopup(this);
        mVoiceHint = mVoicePopup.findViewById(R.id.tv_in_voice_hint);
        mAudioRecoderUtils = new AudioRecoderUtils(this);
        //录音回调
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {

            //录音中....db为声音分贝，time为录音时长
            @Override
            public void onUpdate(double db, long time) {
                /*//根据分贝值来设置录音时话筒图标的上下波动，下面有讲解
                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                mTextView.setText(TimeUtils.long2String(time));*/
            }

            //录音结束，filePath为保存路径
            @Override
            public void onStop(String filePath) {
//                Toast.makeText(ChatActivity.this, "录音保存在：" + filePath, Toast.LENGTH_SHORT).show();
                msgType = "voi";
                File file = new File(filePath);
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                mPresenter.uploadPic(body);
            }

            @Override
            public void onShort(String msg) {

            }
        });
        //Button的touch监听
        btVoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        mPosX = event.getX();
                        mPosY = event.getY();

                        mVoicePopup.showPopupWindow();

                        btVoice.setText("松开保存");
                        mAudioRecoderUtils.startRecord();

                        break;

                    case MotionEvent.ACTION_UP:

                        if (mCurPosY - mPosY < 0
                                && (Math.abs(mCurPosY - mPosY) > 60)) {
                            mAudioRecoderUtils.cancelRecord();    //取消录音（不保存录音文件）
                        } else {
                            mAudioRecoderUtils.stopRecord();        //结束录音（保存录音文件）
                        }
//                        mAudioRecoderUtils.cancelRecord();    //取消录音（不保存录音文件）
                        mVoiceHint.setText("正在说话中");
                        mVoicePopup.dismiss();
                        btVoice.setText("按住说话");

                        break;

                    case MotionEvent.ACTION_MOVE:
                        mCurPosX = event.getX();
                        mCurPosY = event.getY();
                        if (mCurPosY - mPosY < 0
                                && (Math.abs(mCurPosY - mPosY) > 60)) {
                            mVoiceHint.setText("上滑取消发送");
                        } else {
                            mVoiceHint.setText("正在说话中");
                        }
                        break;
                }
                return true;
            }
        });
        initVoice();

//        setChatMessage();
    }

    private void initVoice() {
        mClChatMore.setVisibility(View.GONE);

        if (isVoice) {
            rlInput.setVisibility(View.GONE);
            btVoice.setVisibility(View.VISIBLE);
        } else {
            rlInput.setVisibility(View.VISIBLE);
            btVoice.setVisibility(View.GONE);
        }
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


    }

    @Override
    protected void onResume() {
        super.onResume();
        setChatMessage();
    }

    private boolean hasClick;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chatmsg_listView:
                findViewById(R.id.emojicons).setVisibility(View.GONE);
                hasClick = !hasClick;
                break;
            case R.id.btn_face: // 表情按钮
                if (hasClick) {
                    findViewById(R.id.emojicons).setVisibility(View.GONE);
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
                    mClChatMore.setVisibility(View.GONE);
                    findViewById(R.id.emojicons).setVisibility(View.VISIBLE);
                }
                hasClick = !hasClick;
                break;
            /*case R.id.bt_fs: // 发送按钮
                Toast.makeText(this, mTxtEmojicon.getText(), Toast.LENGTH_SHORT).show();
                tvShow.setText(mTxtEmojicon.getText() + "" + mEditEmojicon.getText());
                mTxtEmojicon.setText(mTxtEmojicon.getText() + "" + mEditEmojicon.getText());

                mEditEmojicon.setText("");
                break;*/
            case R.id.et_content: // 输入框
                findViewById(R.id.emojicons).setVisibility(View.GONE);
                hasClick = !hasClick;
                break;

            case R.id.btn_send:
                String content = SystemFacade.toBase64(et_content.getText().toString());
                if (content.length() <= 0) {
                    Util.showToast(mContext, "消息不能为空哟");
                    return;
                }

                if (client != null && client.isOpen()) {
                    if (tv_quote.getVisibility() == View.VISIBLE) {
                        msgType = "quote";
                    } else {
                        msgType = "msg";
                    }

                    ChatMessage message = new ChatMessage(System.currentTimeMillis() + "", System.currentTimeMillis() + "", "message", mReceiverType, msgType, mFriendInfo.getId(), content, "", 0, 0);
                    if (tv_quote.getVisibility() == View.VISIBLE) {
                        message.setQuoteMessage(mQuoteMessage);
                        String s = new Gson().toJson(mQuoteMessage);
                        message.setQuoteMessageInfo(s);
                    }
//                    ChatMessage message = new ChatMessage("message", "member", "msg", mFriendInfo.getId(), content, "");
                    String s = new Gson().toJson(message);
                    jWebSClientService.sendMsg(s);
                    message.setIsMeSend(1);
                    tv_quote.setVisibility(View.GONE);
                    chatMessageList.add(message);
                    initChatMsgListView();
                    daoSession.getChatMessageDao().insert(message);
                    et_content.setText("");
                    MessageListData messageListData;
                    if (message.getReceiverType().equals("member")) {
                        messageListData = new MessageListData(message.getReceiverId(), mFriendInfo.getNickname(), message.getTextMsg(), "", String.valueOf(System.currentTimeMillis()), message.getReceiverType());
                    } else {
                        messageListData = new MessageListData(message.getReceiverId(), mFriendInfo.getNickname(), message.getTextMsg(), "", String.valueOf(System.currentTimeMillis()), message.getReceiverType());
                    }
//                    messageListQb.where(MessageListDataDao.Properties.MId.eq(messageListData.getMId()));
                    /*if (qb.buildCount().count() > 0 ? true : false) {
                        messageListDataDao.update(messageListData);
                    } else {
                    }*/
                    messageListDataDao.insertOrReplace(messageListData);

                } else {
                    Util.showToast(mContext, "连接已断开，请稍等或重启App哟");
                }
                break;

            case R.id.btn_multimedia:
                mClChatMore.setVisibility(View.VISIBLE);
                findViewById(R.id.emojicons).setVisibility(View.GONE);
                hasClick = !hasClick;
                SystemFacade.hideSoftKeyboard(this);
                break;

            case R.id.iv_chat_pic:
            case R.id.tv_chat_pic:
                checkPermissionAndCamera();
                break;

            case R.id.btn_voice_or_text:
                if (isVoice) {
                    isVoice = false;
                } else {
                    isVoice = true;
                }
                initVoice();
                break;

            case R.id.btn_voice:

                break;
            default:
                break;
        }
    }

    public void setChatMessage() {
        mChatMessageDao = daoSession.getChatMessageDao();
        messageListDataDao = daoSession.getMessageListDataDao();
//        messageListQb = messageListDataDao.queryBuilder();
        qb = mChatMessageDao.queryBuilder();
        qb.where(ChatMessageDao.Properties.ReceiverType.eq(mReceiverType));
        qb.whereOr(ChatMessageDao.Properties.ReceiverId.eq(mFriendInfo.getId()), ChatMessageDao.Properties.MemberId.eq(mFriendInfo.getId()));
//        qb.orderAsc(ChatMessageDao.Properties.SendTime).offset(1).limit(15);
        chatMessageList = qb.list();
        Log.d(TAG, "setChatMessage: " + chatMessageList.size());
        initChatMsgListView();

    }

    private void initChatMsgListView() {

        mAdapter.setList(chatMessageList);
//        mAdapter = new Adapter_ChatMessage(mContext, chatMessageList);
        listView.smoothScrollToPosition(chatMessageList.size());
        mAdapter.setVideoClickListener(new Adapter_ChatMessage.IVideoClickListener() {
            @Override
            public void mVideClick(ChatMessage message) {
                String msgType = message.getMsgType();
                if (msgType.equals("img") || msgType.equals("video")) {
                    Intent imgOrVideoIntent = new Intent(ChatActivity.this, ImgOrVideoActivity.class);
                    imgOrVideoIntent.putExtra(AppConstant.VIDEO_URL, message.getUrl());
                    imgOrVideoIntent.putExtra(AppConstant.IMG_OR_VIDEO, msgType);
                    startActivity(imgOrVideoIntent);
                } else if (msgType.equals("voi")) {

                }
            }
        });


        /*mAdapter.setMessageLongListener(new Adapter_ChatMessage.IMessageLongListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void mLongListener(ChatMessage message, float x, float y) {
                Log.d(TAG, "mLongListener: " + x + "--------" + y);
                listView.showContextMenu(x,y);
            }
        });*/
    }


    /**
     * 检测是否开启通知
     *
     * @param context
     */
    /*private void checkNotification(final Context context) {
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
    }*/

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
        if (resultCode == 1002) {

            messageListDataDao.deleteByKey(mFriendInfo.getId());
            mChatMessageDao.deleteByKey(mFriendInfo.getId());
            finish();
        }
    }

    @Override
    public void uploadPicResult(LoginData data) {
        if (data != null) {
            if (client != null && client.isOpen()) {
                ChatMessage message = new ChatMessage(System.currentTimeMillis() + "", System.currentTimeMillis() + "", "message", mReceiverType, msgType, mFriendInfo.getId(), "", data.getData(), 0, 0);
                String s = new Gson().toJson(message);
                jWebSClientService.sendMsg(s);
                message.setIsMeSend(1);
                chatMessageList.add(message);
                initChatMsgListView();
                daoSession.getChatMessageDao().insert(message);

                MessageListData messageListData;
                String content = "";
                if (msgType.equals("img")) {
                    content = "[图片]";
                } else if (msgType.equals("video")) {
                    content = "[视频]";
                } else if (msgType.equals("voi")) {
                    content = "[语音]";
                }
                if (message.getReceiverType().equals("member")) {
                    messageListData = new MessageListData(message.getReceiverId(), mFriendInfo.getNickname(), content, "", String.valueOf(System.currentTimeMillis()), message.getReceiverType());
                } else {
                    messageListData = new MessageListData(message.getReceiverId(), mFriendInfo.getNickname(), content, "", String.valueOf(System.currentTimeMillis()), message.getReceiverType());
                }
                messageListDataDao.insertOrReplace(messageListData);
                mProgressDialog.dismiss();
            } else {
                Util.showToast(mContext, "连接已断开，请稍等或重启App哟");
            }
        }
    }

    @Override
    public void groupInfoReceive(GroupInfoData data) {
        if (data != null) {
            int isForbidden = data.getData().getIsForbidden();
            mTitleBar.setTitle(data.getData().getName(), TitleBarLayout.POSITION.LEFT);
            if (isForbidden == 1) {
                btMute.setVisibility(View.VISIBLE);
            } else {
                btMute.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onFail(String msg, int code) {
        mProgressDialog.cancel();
        showToast(msg);
        if (code == 10001) {
            MyApplication.loginAgain();
        }
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