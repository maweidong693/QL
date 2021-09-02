package com.weiwu.ql.main.message;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfoData;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.weiwu.ql.AppConstant;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseFragment;
import com.weiwu.ql.data.bean.MessageListData;
import com.weiwu.ql.data.repositories.ContactRepository;
import com.weiwu.ql.greendao.db.ChatMessageDao;
import com.weiwu.ql.greendao.db.DaoMaster;
import com.weiwu.ql.greendao.db.DaoSession;
import com.weiwu.ql.greendao.db.MessageListDataDao;
import com.weiwu.ql.main.contact.ContactContract;
import com.weiwu.ql.main.contact.chat.ChatActivity;
import com.weiwu.ql.main.contact.chat.im.JWebSocketClient;
import com.weiwu.ql.main.contact.chat.im.JWebSocketClientService;
import com.weiwu.ql.main.contact.chat.modle.ChatMessage;
import com.weiwu.ql.main.contact.chat.modle.NoticeData;
import com.weiwu.ql.utils.SPUtils;
import com.weiwu.ql.utils.SystemFacade;
import com.weiwu.ql.view.Menu;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;
import java.util.Random;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/22 13:35 
 */
public class MessageFragment extends BaseFragment implements ContactContract.IMessageView {

    private JWebSocketClient client;
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSClientService;
    private ChatMessageReceiver chatMessageReceiver;

    private TitleBarLayout mConversationTitle;
    private RecyclerView mRvMessageList;
    private MessageAdapter mMessageAdapter;
    private List<MessageListData> list;
    private MessageListDataDao messageListDataDao;
    private QueryBuilder<MessageListData> qb;
    private DaoSession daoSession;
    private Menu mMenu;
    private ContactContract.IMessagePresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragemtn_message;
    }

    @Override
    public void onViewCreated(View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPresenter(new MessagePresenter(ContactRepository.getInstance()));
        initView(view);
        //绑定服务
        bindService();
        //注册广播
        doRegisterReceiver();
    }

    private static final String TAG = "MessageFragment";

    @Override
    public void groupInfoReceive(GroupInfoData data) {
        Log.d(TAG, "groupInfoReceive: ccc");
        if (data.getData() != null) {
            GroupInfoData.DataDTO dto = data.getData();
            List<MessageListData> list = messageListDataDao.queryBuilder().where(MessageListDataDao.Properties.MId.eq(dto.getId())).list();
            MessageListData messageListData = list.get(0);
            messageListData.setNickName(dto.getName());
            messageListDataDao.update(messageListData);

            this.list = messageListDataDao.queryBuilder().orderDesc(MessageListDataDao.Properties.Time).list();
            mMessageAdapter.setList(this.list);
        }
    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
        if (code == 10001) {
            MyApplication.getInstance().loginAgain();
        }
    }

    @Override
    public void setPresenter(ContactContract.IMessagePresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return getActivity();
    }


    private class ChatMessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            ChatMessage receiveMessage = new Gson().fromJson(message, ChatMessage.class);
            if (receiveMessage.getType().equals("messageInfo")) {
                if (receiveMessage.getCode() == 200) {
                    return;
                }

            }

            MessageListData messageListData;
            String content = "";
            String msgType = receiveMessage.getMsgType();
            String type = receiveMessage.getType();
            if (type.equals("message")) {
                if (msgType.equals("img")) {
                    content = "[图片]";
                } else if (msgType.equals("video")) {
                    content = "[视频]";
                } else if (msgType.equals("voi")) {
                    content = "[语音]";
                } else if (msgType.equals("msg") || msgType.equals("quote")) {
                    content = receiveMessage.getTextMsg();
                } else {
                    content = " ";
                }
            }

            ChatMessageDao chatMessageDao = daoSession.getChatMessageDao();
            if (type.equals("notice")) {
                NoticeData noticeData = receiveMessage.getNotice();
//                receiveMessage.setMessageId(String.valueOf(System.currentTimeMillis() + getNum(100)));
                receiveMessage.setNoticeInfo(new Gson().toJson(noticeData));
                receiveMessage.setIsMeSend(1);

                int dataType = noticeData.getType();
                if (dataType == 400) {
                    messageListDataDao.deleteByKey(receiveMessage.getReceiverId());
//                chatMessageDao.deleteByKey(receiveMessage.getReceiverId());
                } else if (dataType == 120) {
                    messageListDataDao.deleteByKey(receiveMessage.getMemberId());
                } else if (dataType != 101 && dataType != 102 && dataType != 122 && dataType != 1201 && dataType != 1200 && dataType != 1202 && dataType != 1203 && dataType != 1204) {
                    chatMessageDao.insertOrReplace(receiveMessage);
                    if (receiveMessage.getReceiverType().equals("member")) {
                        messageListData = new MessageListData(receiveMessage.getMemberId(), noticeData.getFromMemberNickName(), SystemFacade.toBase64(noticeData.getNoticeMessage()), "", receiveMessage.getSendTime(), receiveMessage.getReceiverType());
                    } else {
                        messageListData = new MessageListData(receiveMessage.getReceiverId(), receiveMessage.getReceiverId(), SystemFacade.toBase64(content), "", receiveMessage.getSendTime(), receiveMessage.getReceiverType());
                    }
                    qb.where(MessageListDataDao.Properties.MId.eq(messageListData.getMId()));
                    if (qb.buildCount().count() > 0) {
                        messageListDataDao.update(messageListData);
                    } else {
                        messageListDataDao.insertOrReplace(messageListData);
                    }
                }
            } else if (type.equals("function")) {

                int functionTab = receiveMessage.getFunctionArg().getFunctionTab();
                if (functionTab == 901) {
                    chatMessageDao.deleteByKey(receiveMessage.getMessageId());
                    NoticeData data = new NoticeData(901, "撤回", receiveMessage.getMemberNickname(), "");

                    String functionInfo = new Gson().toJson(data);
                    receiveMessage.setNoticeInfo(functionInfo);
//                    chatMessageDao.insertOrReplace(receiveMessage);

                    /*if (receiveMessage.getReceiverType().equals("member")) {
                        messageListData = new MessageListData(receiveMessage.getMemberId(), data.getFromMemberNickName(), data.getNoticeMessage(), "", receiveMessage.getSendTime(), receiveMessage.getReceiverType());

                    } else {
                        messageListData = new MessageListData(receiveMessage.getReceiverId(), receiveMessage.getReceiverId(), content, "", receiveMessage.getSendTime(), receiveMessage.getReceiverType());
                    }
                    qb.where(MessageListDataDao.Properties.MId.eq(messageListData.getMId()));
                    if (qb.buildCount().count() > 0) {
                        messageListDataDao.update(messageListData);
                    } else {
                        messageListDataDao.insertOrReplace(messageListData);
                    }*/
                }

            } else {

                if (msgType.equals("quote")) {
                    receiveMessage.setQuoteMessageInfo(new Gson().toJson(receiveMessage.getQuoteMessage()));
                }
                chatMessageDao.insertOrReplace(receiveMessage);
                if (receiveMessage.getReceiverType().equals("member")) {
                    messageListData = new MessageListData(receiveMessage.getMemberId(), receiveMessage.getMemberNickname(), content, "", receiveMessage.getSendTime(), receiveMessage.getReceiverType());
                } else {
                    messageListData = new MessageListData(receiveMessage.getReceiverId(), receiveMessage.getReceiverId(), content, "", receiveMessage.getSendTime(), receiveMessage.getReceiverType());
                }
                qb.where(MessageListDataDao.Properties.MId.eq(messageListData.getMId()));
                if (qb.buildCount().count() > 0) {
                    messageListDataDao.update(messageListData);
                } else {
                    messageListDataDao.insertOrReplace(messageListData);
                }
            }


            list = messageListDataDao.queryBuilder().orderDesc(MessageListDataDao.Properties.Time).list();

            if (receiveMessage.getReceiverType().equals("group")) {
                mPresenter.getGroupInfo(receiveMessage.getReceiverId());
            } else {
                mMessageAdapter.setList(list);
            }

//                receiveMessage.setIsMeSend(0);
//                chatMessageList.add(receiveMessage);
//                initChatMsgListView();
        }
    }

    public static int getNum(int endNum) {
        if (endNum > 0) {
            Random random = new Random();
            return random.nextInt(endNum);
        }
        return 0;
    }

    public void setMessageList() {
        list = messageListDataDao.queryBuilder().orderDesc(MessageListDataDao.Properties.Time).list();
        mMessageAdapter.setList(list);
    }

    @Override
    public void onResume() {
        super.onResume();
        setMessageList();
        Log.d(TAG, "onResume: aaa");
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

    /**
     * 绑定服务
     */
    private void bindService() {
        Intent bindIntent = new Intent(getContext(), JWebSocketClientService.class);
        getActivity().bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    /**
     * 动态注册广播
     */
    private void doRegisterReceiver() {
        chatMessageReceiver = new ChatMessageReceiver();
        IntentFilter filter = new IntentFilter("com.xch.servicecallback.content");
        getActivity().registerReceiver(chatMessageReceiver, filter);
    }

    private void initView(View view) {
        mConversationTitle = (TitleBarLayout) view.findViewById(R.id.conversation_title);
        mRvMessageList = (RecyclerView) view.findViewById(R.id.rv_message_list);

        mConversationTitle.setTitle(getResources().getString(com.tencent.qcloud.tim.uikit.R.string.conversation_title), TitleBarLayout.POSITION.MIDDLE);
        mConversationTitle.getLeftGroup().setVisibility(View.GONE);
        mConversationTitle.setRightIcon(com.tencent.qcloud.tim.uikit.R.drawable.conversation_more);
        mMenu = new Menu(getActivity(), mConversationTitle, Menu.MENU_TYPE_CONVERSATION);

        mRvMessageList.setLayoutManager(new LinearLayoutManager(getContext()));
        mMessageAdapter = new MessageAdapter();
        mRvMessageList.setAdapter(mMessageAdapter);


        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getContext(), SPUtils.getValue(AppConstant.USER, AppConstant.USER_ID) + ".db");
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
        daoSession.clear();
        messageListDataDao = daoSession.getMessageListDataDao();
        qb = messageListDataDao.queryBuilder();

        list = messageListDataDao.queryBuilder().orderDesc(MessageListDataDao.Properties.Time).list();

        mMessageAdapter.setList(list);

        mMessageAdapter.setChatClickListener(new MessageAdapter.IChatClickListener() {
            @Override
            public void mClick(MessageListData data) {
                ContactItemBean bean = new ContactItemBean(data.getMId(), data.getNickName());
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra(TUIKitConstants.ProfileType.CONTENT, bean);
                intent.putExtra(AppConstant.CHAT_TYPE, data.getReceiverType());
                startActivity(intent);
//                getActivity().finish();
            }
        });
        initTitleAction();
    }

    private void initTitleAction() {
        mConversationTitle.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMenu.isShowing()) {
                    mMenu.hide();
                } else {
                    mMenu.show();
                }
            }
        });
    }
}
