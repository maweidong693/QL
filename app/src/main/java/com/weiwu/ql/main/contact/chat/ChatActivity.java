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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.qiniu.android.utils.Json;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfoData;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.weiwu.ql.AppConstant;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.repositories.ContactRepository;
import com.weiwu.ql.data.request.CollectionRequestBody;
import com.weiwu.ql.data.request.DelMsgRequestBody;
import com.weiwu.ql.data.request.GroupInfoRequestBody;
import com.weiwu.ql.data.request.HistoryMsgRequestBody;
import com.weiwu.ql.data.request.MessageRequestBody;
import com.weiwu.ql.data.request.SendGroupMsgRequestBody;
import com.weiwu.ql.data.request.SendMsgRequestBody;
import com.weiwu.ql.main.contact.ContactContract;
import com.weiwu.ql.main.contact.chat.adapter.ChatMessageAdapter;
import com.weiwu.ql.main.contact.chat.im.JWebSocketClient;
import com.weiwu.ql.main.contact.chat.im.JWebSocketClientService;
import com.weiwu.ql.main.contact.chat.modle.HistoryMessageData;
import com.weiwu.ql.main.contact.chat.modle.SendMessageData;
import com.weiwu.ql.main.contact.chat.util.Util;
import com.weiwu.ql.main.contact.detail.FriendProfileActivity;
import com.weiwu.ql.main.contact.group.GroupInfoActivity;
import com.weiwu.ql.utils.AudioRecoderUtils;
import com.weiwu.ql.utils.GlideEngine;
import com.weiwu.ql.utils.SPUtils;
import com.weiwu.ql.utils.SystemFacade;
import com.weiwu.ql.view.VoicePopup;


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
    private List<HistoryMessageData.DataDTO.ListDTO> chatMessageList = new ArrayList<>();//????????????
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
    private int mReceiverType;
    private Button btMute;
    private TextView tv_quote;
    private HistoryMessageData.DataDTO.ListDTO mQuoteMessage;
    private ImageView bt_face;
    private int mGroupId;
    private String mGroup_id;
    //    private int mIn;
    private int mChatId;
    private SmartRefreshLayout mSrlChat;
    private int start = 1;
    private int end = 20;
    private int mIs_add_friend;

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        jWebSClientService.onDestroy();
        mPresenter.setChatStatus(new MessageRequestBody(mChatId + "", "0"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.setChatStatus(new MessageRequestBody(mChatId + "", "0"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.setChatStatus(new MessageRequestBody(mChatId + "", "0"));

    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("MainActivity", "???????????????????????????");
            binder = (JWebSocketClientService.JWebSocketClientBinder) iBinder;
            jWebSClientService = binder.getService();
            client = jWebSClientService.client;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("MainActivity", "???????????????????????????");
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
            mAdapter.clearList();
            start = 1;

            if (mReceiverType == 1) {
                mPresenter.getHistoryMsg(new HistoryMsgRequestBody(mFriendInfo.getId(), start++ + ""));
            } else {
                mGroup_id = mFriendInfo.getId();
                mPresenter.getGroupHistoryMsg(new HistoryMsgRequestBody(mFriendInfo.getId(), start++ + "", "20"));
            }
        }
    }

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
        ImmersionBar.with(this).keyboardEnable(true)  //?????????????????????????????????????????????????????????false???????????????????????????????????????????????????mode
                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)  //???????????????????????????
                .setOnKeyboardListener(new OnKeyboardListener() {    //????????????????????????keyboardEnable???true?????????????????????
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
            mReceiverType = intent.getIntExtra(AppConstant.CHAT_TYPE, 0);
//            mIn = intent.getIntExtra(AppConstant.IN, 0);
            mChatId = intent.getIntExtra(AppConstant.CHAT_ID, 0);
        }

        /*DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, SPUtils.getValue(AppConstant.USER, AppConstant.USER_ID) + ".db");
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        daoSession.clear();*/

        mContext = ChatActivity.this;
        setPresenter(new ChatPresenter(ContactRepository.getInstance()));
        findViewById();
        initView();
        setEmojiconFragment(false);
//        if (mIn == 1) {
        mGroup_id = mFriendInfo.getId();

//            mPresenter.getGroupInfo(new GroupInfoRequestBody(mFriendInfo.getId()));
//        }
        //????????????
//        startJWebSClientService();
        //????????????
        bindService();
        //????????????
        doRegisterReceiver();
        //????????????????????????
//        checkNotification(mContext);
    }

    /**
     * ????????????
     */
    private void bindService() {
        Intent bindIntent = new Intent(mContext, JWebSocketClientService.class);
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    /* *//**
     * ???????????????websocket??????????????????
     *//*
    private void startJWebSClientService() {
        Intent intent = new Intent(mContext, JWebSocketClientService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android8.0????????????startForegroundService??????service
            startForegroundService(intent);
        } else {
            startService(intent);
        }
//        startService(intent);
    }*/

    /**
     * ??????????????????
     */
    private void doRegisterReceiver() {
        chatMessageReceiver = new ChatMessageReceiver();
        IntentFilter filter = new IntentFilter("com.xch.servicecallback.content");
        registerReceiver(chatMessageReceiver, filter);
    }


    private void findViewById() {
        mTitleBar = findViewById(R.id.chat_titlebar);
        mSrlChat = findViewById(R.id.srl_chat_load);
        mTitleBar.setTitle(mFriendInfo.getNickname(), TitleBarLayout.POSITION.MIDDLE);
        mTitleBar.setRightIcon(R.drawable.more);
        mTitleBar.getRightIcon().setImageResource(R.drawable.more);

        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (mReceiverType == 2) {
            mTitleBar.getRightGroup().setVisibility(View.VISIBLE);
            mTitleBar.setOnRightClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ChatActivity.this, GroupInfoActivity.class);
                    intent.putExtra(TUIKitConstants.Group.GROUP_ID, mGroupId);
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
        mAdapter.setFriendClickListener(new ChatMessageAdapter.IFriendClickListener() {
            @Override
            public void click(HistoryMessageData.DataDTO.ListDTO mChatMessage) {
                if (mReceiverType == 1) {
                    ContactItemBean contact = new ContactItemBean(mChatMessage.getFid(), mChatMessage.getNick_name());
                    Intent intent = new Intent(ChatActivity.this, FriendProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(TUIKitConstants.ProfileType.CONTENT, contact);
                    intent.putExtra(AppConstant.FRIEND_TYPE, 120);
                    startActivity(intent);
                } else {
                    if (mIs_add_friend == 0) {
                        showToast("?????????????????????????????????");
                    } else {
                        ContactItemBean contact = new ContactItemBean(mChatMessage.getFid(), mChatMessage.getNick_name());
                        Intent intent = new Intent(ChatActivity.this, FriendProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(TUIKitConstants.ProfileType.CONTENT, contact);
                        intent.putExtra(AppConstant.FRIEND_TYPE, 120);
                        startActivity(intent);
                    }
                }
            }
        });
        mAdapter.setMessageLongClickListener(new ChatMessageAdapter.IMessageLongClickListener() {
            @Override
            public void mLongClick(HistoryMessageData.DataDTO.ListDTO message, View view) {
                mQuoteMessage = message;
                if (TextUtils.isEmpty(mQuoteMessage.getNick_name())) {
                    mQuoteMessage.setNick_name(SPUtils.getValue(AppConstant.USER, AppConstant.USER_NAME));

                }
                PopupMenu popupMenu = new PopupMenu(ChatActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_message, popupMenu.getMenu());
                Menu menu = popupMenu.getMenu();
                MenuItem withdrawMenuItem = menu.getItem(1);
                MenuItem mQuote = menu.getItem(0);
                MenuItem mDelete = menu.getItem(3);
                mDelete.setVisible(false);
                if (message.getFid().equals(SPUtils.getValue(AppConstant.USER, AppConstant.USER_ID))) {
                    withdrawMenuItem.setVisible(message.getFid().equals(SPUtils.getValue(AppConstant.USER, AppConstant.USER_ID))
                            && System.currentTimeMillis() - SystemFacade.parseServerTime(message.getSend_time(), null) < 120000);
                } else
                    withdrawMenuItem.setVisible(false);

                if (mQuoteMessage.getCate() == 5) {
                    mQuote.setVisible(false);
                } else {
                    mQuote.setVisible(true);

                }
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.tv_message_quote) {
                            tv_quote.setVisibility(View.VISIBLE);
                            String quoteContent = "";
                            if (mQuoteMessage.getCate() == 2) {
                                quoteContent = "[??????]";
                            } else if (mQuoteMessage.getCate() == 4) {
                                quoteContent = "[??????]";

                            } else {
                                quoteContent = mQuoteMessage.getContent();
                            }
                            tv_quote.setText(mQuoteMessage.getNick_name() + ":" + quoteContent);
                            /*chatData.remove(pos);
                            chatAdapter.notifyItemRemoved(pos);*/
                        } else if (item.getItemId() == R.id.tv_message_withdraw) {
                            /*FunctionData.FunctionArgDTO functionArgDTO = new FunctionData.FunctionArgDTO(901, "??????");
                            FunctionData function = new FunctionData("function", mReceiverType, mFriendInfo.getId(), message.getMessageId(), functionArgDTO);
                            String s = new Gson().toJson(function);
                            jWebSClientService.sendMsg(s);
//                            mChatMessageDao.deleteByKey(message.getMessageId());*/
                            mPresenter.delMsg(new DelMsgRequestBody(message.getMsg_id() + "", mReceiverType + ""));

                            setChatMessage();
                        } else if (item.getItemId() == R.id.tv_message_collection) {
                            if (message.getCate() == 1) {
                                mPresenter.collection(new CollectionRequestBody(message.getNick_name(), message.getContent(), null, null, "1"));
                            } else {
                                mPresenter.collection(new CollectionRequestBody(message.getNick_name(), null, message.getContent(), null, message.getCate() + ""));
                            }
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
        //????????????
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {

            //?????????....db??????????????????time???????????????
            @Override
            public void onUpdate(double db, long time) {
                /*//??????????????????????????????????????????????????????????????????????????????
                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                mTextView.setText(TimeUtils.long2String(time));*/
            }

            //???????????????filePath???????????????
            @Override
            public void onStop(String filePath) {
//                Toast.makeText(ChatActivity.this, "??????????????????" + filePath, Toast.LENGTH_SHORT).show();
                msgType = "3";
                File file = new File(filePath);
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                mPresenter.uploadPic(body);
            }

            @Override
            public void onShort(String msg) {

            }
        });
        //Button???touch??????
        btVoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        mPosX = event.getX();
                        mPosY = event.getY();

                        mVoicePopup.showPopupWindow();

                        btVoice.setText("????????????");
                        mAudioRecoderUtils.startRecord();

                        break;

                    case MotionEvent.ACTION_UP:

                        if (mCurPosY - mPosY < 0
                                && (Math.abs(mCurPosY - mPosY) > 60)) {
                            mAudioRecoderUtils.cancelRecord();    //???????????????????????????????????????
                        } else {
                            mAudioRecoderUtils.stopRecord();        //????????????????????????????????????
                        }
//                        mAudioRecoderUtils.cancelRecord();    //???????????????????????????????????????
                        mVoiceHint.setText("???????????????");
                        mVoicePopup.dismiss();
                        btVoice.setText("????????????");

                        break;

                    case MotionEvent.ACTION_MOVE:
                        mCurPosX = event.getX();
                        mCurPosY = event.getY();
                        if (mCurPosY - mPosY < 0
                                && (Math.abs(mCurPosY - mPosY) > 60)) {
                            mVoiceHint.setText("??????????????????");
                        } else {
                            mVoiceHint.setText("???????????????");
                        }
                        break;
                }
                return true;
            }
        });
        initVoice();

//        setChatMessage();
        if (mReceiverType == 1) {
            mPresenter.getHistoryMsg(new HistoryMsgRequestBody(mFriendInfo.getId(), start++ + ""));
        } else {
//            if (mIn == 2) {
//                mPresenter.getGroupInfo(new GroupInfoRequestBody(Integer.parseInt(mFriendInfo.getId())));
//            } else {
            mPresenter.getGroupHistoryMsg(new HistoryMsgRequestBody(mFriendInfo.getId(), start++ + "", end + ""));
//            }
//            mPresenter.getGroupHistoryMsg(new HistoryMsgRequestBody(mFriendInfo.getId(), "0", "20"));
        }

        mSrlChat.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (mReceiverType == 1) {
                    mPresenter.getHistoryMsg(new HistoryMsgRequestBody(mFriendInfo.getId(), start++ + ""));
                } else {
//                    if (mIn == 2) {
//                        mPresenter.getGroupInfo(new GroupInfoRequestBody(Integer.parseInt(mFriendInfo.getId())));
//                    } else {
                    mPresenter.getGroupHistoryMsg(new HistoryMsgRequestBody(mFriendInfo.getId(), start++ + "", end + ""));
//                    }
//            mPresenter.getGroupHistoryMsg(new HistoryMsgRequestBody(mFriendInfo.getId(), "0", "20"));
                }
            }
        });
        mSrlChat.setEnableLoadMore(false);

        /*listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!hasClick){

                    findViewById(R.id.emojicons).setVisibility(View.GONE);


                    mClChatMore.setVisibility(View.GONE);
                    hasClick = !hasClick;
                    SystemFacade.hideSoftKeyboard(ChatActivity.this);
                }
            }
        });*/

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
        //????????????????????????
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
        mPresenter.setChatStatus(new MessageRequestBody(mChatId + "", "1"));
    }

    private boolean hasClick;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chatmsg_listView:
//                findViewById(R.id.emojicons).setVisibility(View.GONE);
//                hasClick = !hasClick;
                break;
            case R.id.btn_face: // ????????????
                if (hasClick) {
                    findViewById(R.id.emojicons).setVisibility(View.GONE);
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //??????????????????
                    mClChatMore.setVisibility(View.GONE);
                    findViewById(R.id.emojicons).setVisibility(View.VISIBLE);
                }
                hasClick = !hasClick;
                break;
            /*case R.id.bt_fs: // ????????????
                Toast.makeText(this, mTxtEmojicon.getText(), Toast.LENGTH_SHORT).show();
                tvShow.setText(mTxtEmojicon.getText() + "" + mEditEmojicon.getText());
                mTxtEmojicon.setText(mTxtEmojicon.getText() + "" + mEditEmojicon.getText());

                mEditEmojicon.setText("");
                break;*/
            case R.id.et_content: // ?????????
                findViewById(R.id.emojicons).setVisibility(View.GONE);
                hasClick = !hasClick;
                break;

            case R.id.btn_send:
                String content = et_content.getText().toString();
                if (content.length() <= 0) {
                    Util.showToast(mContext, "?????????????????????");
                    return;
                }

                if (tv_quote.getVisibility() == View.VISIBLE) {
                    msgType = "5";
                } else {
                    msgType = "1";
                }
//
                if (mReceiverType == 1) {
                    SendMsgRequestBody dataDTO = new SendMsgRequestBody(content, msgType, mFriendInfo.getId());
                    if (tv_quote.getVisibility() == View.VISIBLE) {
                        String q = new Gson().toJson(mQuoteMessage);
                        dataDTO.setCite(q);
                        dataDTO.setCate("5");
                    }
                    mPresenter.sendMessage(dataDTO);
                } else {
                    SendGroupMsgRequestBody body = new SendGroupMsgRequestBody(mGroup_id, content, msgType);
                    if (tv_quote.getVisibility() == View.VISIBLE) {
                        String q = new Gson().toJson(mQuoteMessage);
                        body.setCite(q);
                        body.setCate("5");
                    }
                    mPresenter.sendGroupMessage(body);
                }
                tv_quote.setVisibility(View.GONE);

//                initChatMsgListView();
//                    daoSession.getChatMessageDao().insert(message);
                et_content.setText("");

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
        /*mChatMessageDao = daoSession.getChatMessageDao();
        messageListDataDao = daoSession.getMessageListDataDao();
//        messageListQb = messageListDataDao.queryBuilder();
        qb = mChatMessageDao.queryBuilder();
        qb.where(ChatMessageDao.Properties.ReceiverType.eq(mReceiverType));
        qb.whereOr(ChatMessageDao.Properties.ReceiverId.eq(mFriendInfo.getId()), ChatMessageDao.Properties.MemberId.eq(mFriendInfo.getId()));
//        qb.orderAsc(ChatMessageDao.Properties.SendTime).offset(1).limit(15);
        chatMessageList = qb.list();
        Log.d(TAG, "setChatMessage: " + chatMessageList.size());
        initChatMsgListView();*/

    }

    private void initChatMsgListView() {

        if (start == 1) {
            mAdapter.clearList();
        }

        mAdapter.setList(chatMessageList, mReceiverType);
//        mAdapter = new Adapter_ChatMessage(mContext, chatMessageList);
        listView.smoothScrollToPosition(chatMessageList.size());
        mAdapter.setVideoClickListener(new ChatMessageAdapter.IVideoClickListener() {
            @Override
            public void mVideClick(HistoryMessageData.DataDTO.ListDTO message) {
                int msgType = message.getCate();
                if (msgType == 2 || msgType == 4) {
                    Intent imgOrVideoIntent = new Intent(ChatActivity.this, ImgOrVideoActivity.class);
                    imgOrVideoIntent.putExtra(AppConstant.VIDEO_URL, message.getContent());
                    imgOrVideoIntent.putExtra(AppConstant.IMG_OR_VIDEO, msgType);
                    startActivity(imgOrVideoIntent);
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
     * ????????????????????????
     *
     * @param context
     */
    /*private void checkNotification(final Context context) {
        if (!isNotificationEnabled(context)) {
            new AlertDialog.Builder(context).setTitle("????????????")
                    .setMessage("???????????????????????????????????????????????????????????????????????????")
                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setNotification(context);
                        }
                    }).setNegativeButton("??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        }
    }*/

    /**
     * ????????????????????????????????????????????????
     *
     * @param context
     */
    private void setNotification(Context context) {
        Intent localIntent = new Intent();
        //?????????????????????????????????????????????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            localIntent.putExtra("app_package", context.getPackageName());
            localIntent.putExtra("app_uid", context.getApplicationInfo().uid);
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.addCategory(Intent.CATEGORY_DEFAULT);
            localIntent.setData(Uri.parse("package:" + context.getPackageName()));
        } else {
            //4.4???????????????app????????????????????????????????????Action???????????????????????????????????????,
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
     * ??????????????????,?????????????????????????????????
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

    // ?????????????????????requestCode
    private static final int CAMERA_REQUEST_CODE = 0x00000012;

    /**
     * ????????????????????????
     * ?????????????????????????????????
     */
    private void checkPermissionAndCamera() {
        int hasCameraPermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.CAMERA);
        if (hasCameraPermission == PackageManager.PERMISSION_GRANTED) {
            //????????????????????????
            openCamera();
        } else {
            //??????????????????????????????
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQUEST_CODE);
        }
    }

    /**
     * ??????????????????????????????
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //???????????????????????????????????????
                openCamera();
            } else {
                //?????????????????????????????????
                Toast.makeText(this, "?????????????????????", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openCamera() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofAll())
                .selectionMode(PictureConfig.MULTIPLE)
                .isSingleDirectReturn(false)
                .previewImage(true)
                .isWithVideoImage(false)//?????????????????????????????????,??????ofAll???????????????
                .loadImageEngine(GlideEngine.createGlideEngine())
                .isCamera(false)
                .compress(true)
                .forResult(AppConstant.BANK_CARD_PIC_REQUEST);
    }

    private static final String TAG = "ChatActivity";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == AppConstant.BANK_CARD_PIC_REQUEST) {
            media = PictureSelector.obtainMultipleResult(data);


//            List<HistoryMessageData.DataDTO.ListDTO> list = mAdapter.getList();

            for (int i = 0; i < media.size(); i++) {
                String mPicPath = "";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    mPicPath = media.get(i).getAndroidQToPath();
                } else {
                    mPicPath = media.get(i).getPath();
                }
                String mimeType = media.get(i).getMimeType();
                if (mimeType.contains("image")) {
                    msgType = "2";
                } else if (mimeType.contains("video")) {
                    msgType = "4";
                }
                HistoryMessageData.DataDTO.ListDTO listDTO = new HistoryMessageData.DataDTO.ListDTO(
                        SPUtils.getValue(AppConstant.USER, AppConstant.USER_ID),
                        SPUtils.getValue(AppConstant.USER, AppConstant.USER_NAME),
                        SPUtils.getValue(AppConstant.USER, AppConstant.USER_AVATAR),
                        mPicPath, Integer.parseInt(msgType), 0, 1, "0000-00-00 00:00:00");
                listDTO.setSending(1);
                Log.d(TAG, "onActivityResult: vvv==" + mPicPath);

                chatMessageList.add(listDTO);
            }

            mAdapter.clearList();
//            Log.d(TAG, "onActivityResult: vvv==" + list.size());
            listView.smoothScrollToPosition(chatMessageList.size());

            mAdapter.setList(chatMessageList, mReceiverType);
            mAdapter.notifyDataSetChanged();
            for (int i = 0; i < media.size(); i++) {
//                mProgressDialog.setMessage("???????????????????????????");
//                mProgressDialog.show();
                String mimeType = media.get(i).getMimeType();
                if (mimeType.contains("image")) {
                    msgType = "2";
                } else if (mimeType.contains("video")) {
                    msgType = "4";
                }
                String mPicPath = "";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    mPicPath = media.get(i).getAndroidQToPath();
                } else {
                    mPicPath = media.get(i).getPath();
                }

                File file = new File(mPicPath);
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                mPresenter.uploadPic(body);
            }

        }
        if (resultCode == 1002) {

//            messageListDataDao.deleteByKey(mFriendInfo.getId());
//            mChatMessageDao.deleteByKey(mFriendInfo.getId());
            finish();
        }
    }

    @Override
    public void delMsgSuccess(HttpResult data) {
        if (data.getCode() == 200) {

            if (mReceiverType == 1) {
                mAdapter.clearList();
                start = 1;
                mPresenter.getHistoryMsg(new HistoryMsgRequestBody(mFriendInfo.getId(), start++ + ""));
            } else {
//                if (mIn == 2) {
//                    mPresenter.getGroupInfo(new GroupInfoRequestBody(Integer.parseInt(mFriendInfo.getId())));
//                } else {
//                mPresenter.getGroupHistoryMsg(new HistoryMsgRequestBody(mFriendInfo.getId(), start++ + "", "20"));
//                }
            }
        }
        /*if (mReceiverType == 1) {
            mPresenter.getHistoryMsg(new HistoryMsgRequestBody(mFriendInfo.getId(), start++ + ""));
        } else {
//            if (mIn == 2) {
//                mPresenter.getGroupInfo(new GroupInfoRequestBody(Integer.parseInt(mFriendInfo.getId())));
//            } else {
            mPresenter.getGroupHistoryMsg(new HistoryMsgRequestBody(mFriendInfo.getId(), "0", "100"));
//            }
//                mPresenter.getGroupHistoryMsg(new HistoryMsgRequestBody(mFriendInfo.getId(), "0", "20"));
        }*/
    }

    @Override
    public void chatHistoryResult(HistoryMessageData data) {
        if (mReceiverType != 1) {

            mPresenter.getGroupInfo(new GroupInfoRequestBody(data.getData().getGroupInfo().getId()));
            mGroupId = data.getData().getGroupInfo().getId();
        }
        if (data != null && data.getData().getList().size() > 0) {
            if (data.getData() != null) {
                chatMessageList = data.getData().getList();
                initChatMsgListView();
            } else {
                start = start - 1;
            }
        } else {
            start = start - 1;
        }
        mSrlChat.finishRefresh();
    }

    @Override
    public void sendResult(SendMessageData data) {
        if (data != null) {
            if (mReceiverType == 1) {

                start = 1;
                mAdapter.clearList();
                mPresenter.getHistoryMsg(new HistoryMsgRequestBody(mFriendInfo.getId(), start++ + ""));
            }

        }
    }

    @Override
    public void uploadPicResult(LoginData data) {
        if (data != null) {
            if (mReceiverType == 1) {

                SendMsgRequestBody dataDTO = new SendMsgRequestBody(data.getData().getSrc(), msgType, mFriendInfo.getId());
                mPresenter.sendMessage(dataDTO);
            } else {

                SendGroupMsgRequestBody dataDTO = new SendGroupMsgRequestBody(mGroup_id, data.getData().getSrc(), msgType);
                mPresenter.sendGroupMessage(dataDTO);
            }

//            mPresenter.getHistoryMsg(new HistoryMsgRequestBody(mFriendInfo.getId()));

//            initChatMsgListView();
//                daoSession.getChatMessageDao().insert(message);
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void groupInfoReceive(GroupInfoData data) {
        if (data != null) {
            mGroup_id = data.getData().getGroup_id();
            mIs_add_friend = data.getData().getIs_add_friend();
            int isForbidden = data.getData().getIs_ban_say();
            int isMeForbidden = data.getData().getMy_is_ban_say();
            int role = data.getData().getGroup_role();
            if (role == 0) {
                if (isForbidden == 1) {
                    btMute.setVisibility(View.VISIBLE);
                } else {
                    if (isMeForbidden == 1) {
                        btMute.setVisibility(View.VISIBLE);
                    } else {
                        btMute.setVisibility(View.GONE);
                    }
                }
            } else {
                btMute.setVisibility(View.GONE);

            }
//            mTitleBar.setTitle(data.getData().getGroup_name(), TitleBarLayout.POSITION.LEFT);
//            mPresenter.getGroupHistoryMsg(new HistoryMsgRequestBody(data.getData().getGroup_id(), "0", "100"));
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
    public void onSuccess(HttpResult data) {
//        showToast(data.getMsg());

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