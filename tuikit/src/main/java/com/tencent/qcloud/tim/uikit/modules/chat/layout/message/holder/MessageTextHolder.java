package com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tencent.imsdk.v2.V2TIMDownloadCallback;
import com.tencent.imsdk.v2.V2TIMElem;
import com.tencent.imsdk.v2.V2TIMImageElem;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMVideoElem;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.component.face.FaceManager;
import com.tencent.qcloud.tim.uikit.component.photoview.EmptyViewActivity;
import com.tencent.qcloud.tim.uikit.component.photoview.PhotoViewActivity;
import com.tencent.qcloud.tim.uikit.component.video.VideoViewActivity;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;
import com.tencent.qcloud.tim.uikit.modules.message.MessageQuoteInfo;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.io.File;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MessageTextHolder extends MessageContentHolder {

    private boolean mClicking;

    public MessageTextHolder(View itemView) {
        super(itemView);
    }

    @Override
    public int getVariableLayout() {
        return R.layout.message_adapter_content_text;
    }

    @Override
    public void initVariableViews() {


    }

    @Override
    public void layoutVariableViews(MessageInfo msg, int position) {
//        msgBodyText.setVisibility(View.VISIBLE);
        if (msg.getExtra() != null) {
            FaceManager.handlerEmojiText(msgBodyText, msg.getExtra().toString(), false);
            boolean isQuote = MessageInfoUtil.checkMsgIsQuote(msg.getQuoteMsg());
            if (isQuote){
                msgQuoteText.setVisibility(View.VISIBLE);
                Gson gson = new Gson();
                MessageQuoteInfo quoteInfo = gson.fromJson(msg.getQuoteMsg(), MessageQuoteInfo.class);
                List<MessageInfo> dataSource = mAdapter.getDataSource();
                boolean  isflag = false ;
                for (MessageInfo info : dataSource) {
                    if (info.getId().equals(quoteInfo.getId())){
                        isflag = true ;
                        //引用的对象
                        // 聊天界面设置头像和昵称
                        V2TIMMessage timMessage = info.getTimMessage();
                        String  name = "" ;
                        if (!TextUtils.isEmpty(timMessage.getNameCard())) {
                            name = timMessage.getNameCard();
                        } else if (!TextUtils.isEmpty(timMessage.getFriendRemark())) {
                            name = timMessage.getFriendRemark();
                        } else if (!TextUtils.isEmpty(timMessage.getNickName())) {
                            name = timMessage.getNickName();
                        } else {
                            name =  timMessage.getSender();
                        }
                        if (info.getStatus()== MessageInfo.MSG_STATUS_REVOKE){
                            FaceManager.handlerEmojiText(msgQuoteText, "引用内容已撤回", false);
                            msgQuoteText.setOnClickListener(null);
                        }else {
                            FaceManager.handlerEmojiText(msgQuoteText, name+"：" +quoteInfo.getQuoteMessage(), false);
                            if (quoteInfo.getMsgType()==V2TIMMessage.V2TIM_ELEM_TYPE_TEXT){
                                msgQuoteText.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Intent intent = new Intent(TUIKit.getAppContext(), EmptyViewActivity.class);
                                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("msgType", quoteInfo.getMsgType());
                                        intent.putExtra("quoteMessage", quoteInfo.getQuoteMessage());
                                        TUIKit.getAppContext().startActivity(intent);
                                    }
                                });
                            }
                            else if (quoteInfo.getMsgType()==V2TIMMessage.V2TIM_ELEM_TYPE_IMAGE){
                                msgQuoteText.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        V2TIMMessage timMessage = info.getTimMessage();
                                        V2TIMImageElem imageEle = timMessage.getImageElem();
                                        List<V2TIMImageElem.V2TIMImage> imgs = imageEle.getImageList();
                                        for (int i = 0; i < imgs.size(); i++) {
                                            V2TIMImageElem.V2TIMImage img = imgs.get(i);
                                            if (img.getType() == V2TIMImageElem.V2TIM_IMAGE_TYPE_ORIGIN) {
                                                PhotoViewActivity.mCurrentOriginalImage = img;
                                                break;
                                            }
                                        }
                                        Intent intent = new Intent(TUIKit.getAppContext(), PhotoViewActivity.class);
                                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra(TUIKitConstants.IMAGE_DATA, info.getDataPath());
                                        intent.putExtra(TUIKitConstants.SELF_MESSAGE, info.isSelf());
                                        TUIKit.getAppContext().startActivity(intent);

                                    }
                                });



                            }else if (quoteInfo.getMsgType()==V2TIMMessage.V2TIM_ELEM_TYPE_VIDEO){
                                msgQuoteText.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        V2TIMMessage timMessage = info.getTimMessage();
                                        if (timMessage.getElemType() != V2TIMMessage.V2TIM_ELEM_TYPE_VIDEO) {
                                            return;
                                        }
                                         V2TIMVideoElem videoEle = timMessage.getVideoElem();
                                         String videoPath = TUIKitConstants.VIDEO_DOWNLOAD_DIR + videoEle.getVideoUUID();
                                         File videoFile = new File(videoPath);

                                        if (mClicking) {
                                            return;
                                        }
                                        sendingProgress.setVisibility(View.VISIBLE);
                                        mClicking = true;
                                        //以下代码为zanhanding修改，用于fix点击发送失败视频后无法播放，并且红色感叹号消失的问题
                                        if (videoFile.exists()) {//若存在本地文件则优先获取本地文件

                                            mClicking = false;
                                            play(info);
                                            // 有可能播放的Activity还没有显示，这里延迟200ms，拦截压力测试的快速点击
                                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mClicking = false;
                                                }
                                            }, 200);
                                        } else {
                                            getVideo(videoEle, videoPath, info, true, position);
                                        }
                                    }
                                });
                            }
                        }

                        break;
                    }
                }
                if (!isflag){
                    //引用找不到
                    FaceManager.handlerEmojiText(msgQuoteText, TextUtils.isEmpty(quoteInfo.getFromNameCard())?quoteInfo.getFromUserId():quoteInfo.getFromNameCard()+"：" +quoteInfo.getQuoteMessage(), false);
                     msgQuoteText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                            PhotoViewActivity.mCurrentOriginalImage = null;
                                Intent intent = new Intent(TUIKit.getAppContext(), EmptyViewActivity.class);
                                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("msgType", quoteInfo.getMsgType());
                                intent.putExtra("quoteMessage", quoteInfo.getQuoteMessage());
                                TUIKit.getAppContext().startActivity(intent);
                            }
                        });
                }
            }else {
                msgQuoteText.setText("");
                msgQuoteText.setVisibility(View.GONE);
                msgQuoteText.setOnClickListener(null);
            }

        }
        if (properties.getChatContextFontSize() != 0) {
            msgBodyText.setTextSize(properties.getChatContextFontSize());
        }
        if (msg.isSelf()) {
            if (properties.getRightChatContentFontColor() != 0) {
                msgBodyText.setTextColor(properties.getRightChatContentFontColor());
            }
        } else {
            if (properties.getLeftChatContentFontColor() != 0) {
                msgBodyText.setTextColor(properties.getLeftChatContentFontColor());
            }
        }
    }


    private void play(final MessageInfo msg) {
        Intent intent = new Intent(TUIKit.getAppContext(), VideoViewActivity.class);
        intent.putExtra(TUIKitConstants.CAMERA_IMAGE_PATH, msg.getDataPath());
        intent.putExtra(TUIKitConstants.CAMERA_VIDEO_PATH, msg.getDataUri());
        intent.putExtra(TUIKitConstants.SELF_MESSAGE, msg.isSelf());
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        TUIKit.getAppContext().startActivity(intent);
    }

    private void getVideo(V2TIMVideoElem videoElem, String videoPath, final MessageInfo msg, final boolean autoPlay, final int position) {
        videoElem.downloadVideo(videoPath, new V2TIMDownloadCallback() {
            @Override
            public void onProgress(V2TIMElem.V2ProgressInfo progressInfo) {
                TUIKitLog.i("downloadVideo progress current:", progressInfo.getCurrentSize() + ", total:" + progressInfo.getTotalSize());
            }

            @Override
            public void onError(int code, String desc) {
                ToastUtil.toastLongMessage("下载视频失败" );
                msg.setStatus(MessageInfo.MSG_STATUS_DOWNLOADED);
                mClicking = false;
            }

            @Override
            public void onSuccess() {
                if (autoPlay) {
                    play(msg);
                }
                // 有可能播放的Activity还没有显示，这里延迟200ms，拦截压力测试的快速点击
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mClicking = false;
                    }
                }, 200);
            }
        });
    }

}
