package com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.tencent.common.UserInfo;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.component.gatherimage.UserIconView;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;
import com.tencent.qcloud.tim.uikit.modules.message.MessageQuoteInfo;

import java.util.ArrayList;
import java.util.List;

public abstract class MessageContentHolder extends MessageEmptyHolder {

    public UserIconView leftUserIcon;
    public UserIconView rightUserIcon;
    public TextView usernameText;
    public LinearLayout msgContentLinear;
    public ProgressBar sendingProgress;
    public ImageView statusImage;
    public TextView isReadText;
    public TextView unreadAudioText;

    public TextView msgBodyText;
    public TextView msgQuoteText;
    public LinearLayout msgTextContent;


    public MessageContentHolder(View itemView) {
        super(itemView);
        rootView = itemView;

        leftUserIcon = itemView.findViewById(R.id.left_user_icon_view);
        rightUserIcon = itemView.findViewById(R.id.right_user_icon_view);
        usernameText = itemView.findViewById(R.id.user_name_tv);
        msgContentLinear = itemView.findViewById(R.id.msg_content_ll);
        statusImage = itemView.findViewById(R.id.message_status_iv);
        sendingProgress = itemView.findViewById(R.id.message_sending_pb);
        isReadText = itemView.findViewById(R.id.is_read_tv);
        unreadAudioText = itemView.findViewById(R.id.audio_unread);
        if (getVariableLayout()==R.layout.message_adapter_content_text){
            msgBodyText = itemView.findViewById(R.id.msg_body_tv);
            msgQuoteText = itemView.findViewById(R.id.msg_quote);
            msgTextContent = itemView.findViewById(R.id.text_content);
        }
    }

    @Override
    public void layoutViews(final MessageInfo msg, final int position) {
        super.layoutViews(msg, position);

        //// ????????????
        if (msg.isSelf()) {
            leftUserIcon.setVisibility(View.GONE);
            rightUserIcon.setVisibility(View.VISIBLE);
        } else {
            leftUserIcon.setVisibility(View.VISIBLE);
            rightUserIcon.setVisibility(View.GONE);
        }
        if (properties.getAvatar() != 0) {
            leftUserIcon.setDefaultImageResId(properties.getAvatar());
            rightUserIcon.setDefaultImageResId(properties.getAvatar());
        } else {
            leftUserIcon.setDefaultImageResId(R.drawable.ic_launcher);
            rightUserIcon.setDefaultImageResId(R.drawable.ic_launcher);
        }
        if (properties.getAvatarRadius() != 0) {
            leftUserIcon.setRadius(properties.getAvatarRadius());
            rightUserIcon.setRadius(properties.getAvatarRadius());
        } else {
            leftUserIcon.setRadius(5);
            rightUserIcon.setRadius(5);
        }
        if (properties.getAvatarSize() != null && properties.getAvatarSize().length == 2) {
            ViewGroup.LayoutParams params = leftUserIcon.getLayoutParams();
            params.width = properties.getAvatarSize()[0];
            params.height = properties.getAvatarSize()[1];
            leftUserIcon.setLayoutParams(params);

            params = rightUserIcon.getLayoutParams();
            params.width = properties.getAvatarSize()[0];
            params.height = properties.getAvatarSize()[1];
            rightUserIcon.setLayoutParams(params);
        }
        leftUserIcon.invokeInformation(msg);
        rightUserIcon.invokeInformation(msg);

        //// ??????????????????
        if (msg.isSelf()) { // ??????????????????????????????
            if (properties.getRightNameVisibility() == 0) {
                usernameText.setVisibility(View.GONE);
            } else {
                usernameText.setVisibility(properties.getRightNameVisibility());
            }
        } else {
            if (properties.getLeftNameVisibility() == 0) {
                if (msg.isGroup()) { // ?????????????????????????????????
                    usernameText.setVisibility(View.VISIBLE);
                } else { // ?????????????????????????????????
                    usernameText.setVisibility(View.GONE);
                }
            } else {
                usernameText.setVisibility(properties.getLeftNameVisibility());
            }
        }
        if (properties.getNameFontColor() != 0) {
            usernameText.setTextColor(properties.getNameFontColor());
        }
        if (properties.getNameFontSize() != 0) {
            usernameText.setTextSize(properties.getNameFontSize());
        }
        // ?????????????????????????????????
        V2TIMMessage timMessage = msg.getTimMessage();
        if (!TextUtils.isEmpty(timMessage.getNameCard())) {
            usernameText.setText(timMessage.getNameCard());
        } else if (!TextUtils.isEmpty(timMessage.getFriendRemark())) {
            usernameText.setText(timMessage.getFriendRemark());
        } else if (!TextUtils.isEmpty(timMessage.getNickName())) {
            usernameText.setText(timMessage.getNickName());
        } else {
            usernameText.setText(timMessage.getSender());
        }

        if (!TextUtils.isEmpty(timMessage.getFaceUrl())) {
            List<Object> urllist = new ArrayList<>();
            if (msg.isSelf()) {
                urllist.add(UserInfo.getInstance().getAvatar());
                rightUserIcon.setIconUrls(urllist);
            } else {
                urllist.add(timMessage.getFaceUrl());
                leftUserIcon.setIconUrls(urllist);
            }
        }else {
            List<Object> urllist = new ArrayList<>();
            if (msg.isSelf()) {
                urllist.add(UserInfo.getInstance().getAvatar());
                rightUserIcon.setIconUrls(urllist);
            } else {
                urllist.add(timMessage.getFaceUrl());
                leftUserIcon.setIconUrls(urllist);
            }
        }

        if (msg.isSelf()) {
            if (msg.getStatus() == MessageInfo.MSG_STATUS_SEND_FAIL
                    || msg.getStatus() == MessageInfo.MSG_STATUS_SEND_SUCCESS
                    || msg.isPeerRead()) {
                sendingProgress.setVisibility(View.GONE);
            } else {
                sendingProgress.setVisibility(View.VISIBLE);
            }
        } else {
            sendingProgress.setVisibility(View.GONE);
        }

        //// ??????????????????
        if (msg.isSelf()) {
            if (properties.getRightBubble() != null && properties.getRightBubble().getConstantState() != null) {
                msgContentFrame.setBackground(properties.getRightBubble().getConstantState().newDrawable());
            } else {
                if (msg.getExtra().equals("[????????????]")){
                    msgContentFrame.setBackgroundResource(R.drawable.chat_bfe);
                }else {
                    if (this instanceof MessageTextHolder){
                         msgTextContent.setGravity(Gravity.RIGHT);
//                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) msgBodyText.getLayoutParams();
//                        layoutParams.gravity = Gravity.RIGHT;
                        if (MessageInfoUtil.checkMsgIsQuote(msg.getQuoteMsg())){
                            //?????????????????????holder??????
                            msgContentFrame.setBackground(null);
                            msgBodyText.setBackgroundResource(R.drawable.chat_bubble_myself);
                        }else {
                            try {
                                msgBodyText.setBackground(null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            msgContentFrame.setBackgroundResource(R.drawable.chat_bubble_myself);
                        }
                    }else {
                        try {
                            msgBodyText.setBackground(null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        msgContentFrame.setBackgroundResource(R.drawable.chat_bubble_myself);

                    }

                }
            }
        } else {
            if (properties.getLeftBubble() != null && properties.getLeftBubble().getConstantState() != null) {
                msgContentFrame.setBackground(properties.getLeftBubble().getConstantState().newDrawable());
                msgContentFrame.setLayoutParams(msgContentFrame.getLayoutParams());
            } else {
                if (this instanceof MessageTextHolder){
                    msgTextContent.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
//                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) msgBodyText.getLayoutParams();
//                    layoutParams.gravity = Gravity.LEFT;
                    if (MessageInfoUtil.checkMsgIsQuote(msg.getQuoteMsg())){
                        msgBodyText.setBackgroundResource(R.drawable.chat_other_bg);
                        msgContentFrame.setBackground(null);
                    }else {
                        try {
                            msgBodyText.setBackground(null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        msgContentFrame.setBackgroundResource(R.drawable.chat_other_bg);
                    }
                }else {
                    try {
                        msgBodyText.setBackground(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    msgContentFrame.setBackgroundResource(R.drawable.chat_other_bg);
                }

            }
        }

        //// ?????????????????????????????????
        if (onItemClickListener != null) {
//            if (MessageInfoUtil.checkMsgIsQuote(msg.getQuoteMsg())){
//                msgBodyText.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        onItemClickListener.onMessageLongClick(v, position, msg);
//                        return true;
//                    }
//                });
//            }else {
                msgContentFrame.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onItemClickListener.onMessageLongClick(v, position, msg);
                        return true;
                    }
                });
//            }
            leftUserIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onUserIconClick(view, position, msg);
                }
            });
            leftUserIcon.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onItemClickListener.onUserIconLongClick(view,position, msg);
                    return false;
                }
            });
            rightUserIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onUserIconClick(view, position, msg);
                }
            });
        }

        //// ?????????????????????
        if (msg.getStatus() == MessageInfo.MSG_STATUS_SEND_FAIL) {
            statusImage.setVisibility(View.VISIBLE);
            if (!MessageInfoUtil.checkMsgIsQuote(msg.getQuoteMsg())){
                msgContentFrame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onMessageLongClick(msgContentFrame, position, msg);
                        }
                    }
                });
            }
        } else {
            msgContentFrame.setOnClickListener(null);
            statusImage.setVisibility(View.GONE);
        }

        //// ???????????????????????????????????????????????????
        if (msg.isSelf()) {
            msgContentLinear.removeView(msgContentFrame);
            msgContentLinear.addView(msgContentFrame);
        } else {
            msgContentLinear.removeView(msgContentFrame);
            msgContentLinear.addView(msgContentFrame, 0);
        }
        msgContentLinear.setVisibility(View.VISIBLE);

        //// ???????????????????????????
        if (TUIKitConfigs.getConfigs().getGeneralConfig().isShowRead()) {
            if (msg.isSelf()) {
                if (msg.isGroup()) {
                    isReadText.setVisibility(View.GONE);
                } else {
                    isReadText.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) isReadText.getLayoutParams();
                    params.gravity = Gravity.CENTER_VERTICAL;
                    isReadText.setLayoutParams(params);
                    if (msg.isPeerRead()) {
                        isReadText.setText(R.string.has_read);
                    } else {
                        isReadText.setText(R.string.unread);
                    }
                }
            } else {
                isReadText.setVisibility(View.GONE);
            }
        }

        //// ????????????
        unreadAudioText.setVisibility(View.GONE);

        //// ????????????????????????????????????views
        layoutVariableViews(msg, position);
    }

    public abstract void layoutVariableViews(final MessageInfo msg, final int position);
}
