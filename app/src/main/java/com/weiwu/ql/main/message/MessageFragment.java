package com.weiwu.ql.main.message;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.tencent.common.http.ContactListData;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.weiwu.ql.AppConstant;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseFragment;
import com.weiwu.ql.data.bean.MessageListData;
import com.weiwu.ql.greendao.db.ChatMessageDao;
import com.weiwu.ql.greendao.db.DaoMaster;
import com.weiwu.ql.greendao.db.DaoSession;
import com.weiwu.ql.greendao.db.MessageListDataDao;
import com.weiwu.ql.main.contact.chat.ChatActivity;
import com.weiwu.ql.main.contact.chat.im.JWebSocketClient;
import com.weiwu.ql.main.contact.chat.im.JWebSocketClientService;
import com.weiwu.ql.main.contact.chat.modle.ChatMessage;
import com.weiwu.ql.view.Menu;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/22 13:35 
 */
public class MessageFragment extends BaseFragment {

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

    @Override
    protected int getLayoutId() {
        return R.layout.fragemtn_message;
    }

    @Override
    public void onViewCreated(View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        //绑定服务
        bindService();
        //注册广播
        doRegisterReceiver();
    }

    private static final String TAG = "MessageFragment";


    private class ChatMessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            ChatMessage receiveMessage = new Gson().fromJson(message, ChatMessage.class);

            if (receiveMessage.getCode() == 200) {
                return;
            }
            MessageListData messageListData;
            String content = "";
            String msgType = receiveMessage.getMsgType();
            if (receiveMessage.getType().equals("message")) {
                if (msgType.equals("img")) {
                    content = "[图片]";
                } else if (msgType.equals("video")) {
                    content = "[视频]";
                } else if (msgType.equals("voi")) {
                    content = "[语音]";
                } else if (msgType.equals("msg")) {
                    content = receiveMessage.getTextMsg();
                } else {
                    content = " ";
                }
            }
            if (receiveMessage.getReceiverType().equals("member")) {
                messageListData = new MessageListData(receiveMessage.getMemberId(), receiveMessage.getMemberNickname(), content, "", receiveMessage.getSendTime(), receiveMessage.getReceiverType());
            } else {
                messageListData = new MessageListData(receiveMessage.getReceiverId(), receiveMessage.getReceiverId(), content, "", receiveMessage.getSendTime(), receiveMessage.getReceiverType());
            }
            ChatMessageDao chatMessageDao = daoSession.getChatMessageDao();
            if (!receiveMessage.getType().equals("notice")) {
                chatMessageDao.insertOrReplace(receiveMessage);
            } else {
//chatMessageDao.insertOrReplace()
            }


            qb.where(MessageListDataDao.Properties.MId.eq(messageListData.getMId()));
            if (qb.buildCount().count() > 0 ? true : false) {
                messageListDataDao.update(messageListData);
            } else {
                messageListDataDao.insertOrReplace(messageListData);
            }
            list = messageListDataDao.queryBuilder().orderDesc(MessageListDataDao.Properties.Time).list();
            mMessageAdapter.setList(list);
//                receiveMessage.setIsMeSend(0);
//                chatMessageList.add(receiveMessage);
//                initChatMsgListView();
        }
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

        daoSession = MyApplication.instance().getDaoSession();
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
