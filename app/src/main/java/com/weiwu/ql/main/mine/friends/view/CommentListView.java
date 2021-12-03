package com.weiwu.ql.main.mine.friends.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tencent.common.UserInfo;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMFriendInfo;
import com.tencent.imsdk.v2.V2TIMFriendInfoResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.R;
import com.weiwu.ql.data.bean.FriendsData;
import com.weiwu.ql.main.contact.detail.FriendProfileActivity;
import com.weiwu.ql.main.mine.friends.CircleMovementMethod;
import com.weiwu.ql.main.mine.friends.SpannableClickable;
import com.weiwu.ql.main.mine.friends.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yiwei on 16/7/9.
 */
public class CommentListView extends LinearLayout {
    private int itemColor;
    private int itemSelectorColor;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private List<FriendsData.DataDTO.MessageDTO.CommentAndRepliesDTO> mDatas;
    private LayoutInflater layoutInflater;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setDatas(List<FriendsData.DataDTO.MessageDTO.CommentAndRepliesDTO> datas) {
        if (datas == null) {
            datas = new ArrayList<FriendsData.DataDTO.MessageDTO.CommentAndRepliesDTO>();
        }
        mDatas = datas;
        notifyDataSetChanged();
    }

    public List<FriendsData.DataDTO.MessageDTO.CommentAndRepliesDTO> getDatas() {
        return mDatas;
    }

    public CommentListView(Context context) {
        super(context);
    }

    public CommentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public CommentListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    protected void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.PraiseListView, 0, 0);
        try {
            //textview的默认颜色
            itemColor = typedArray.getColor(R.styleable.PraiseListView_item_color, getResources().getColor(R.color.color_black_333333));
            itemSelectorColor = typedArray.getColor(R.styleable.PraiseListView_item_selector_color, getResources().getColor(R.color.praise_item_selector_default));

        } finally {
            typedArray.recycle();
        }
    }

    public void notifyDataSetChanged() {

        removeAllViews();
        if (mDatas == null || mDatas.size() == 0) {
            return;
        }
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < mDatas.size(); i++) {
            final int index = i;
            View view = getView(index);
            if (view == null) {
                throw new NullPointerException("listview item layout is null, please check getView()...");
            }

            addView(view, index, layoutParams);
        }

    }

    private View getView(final int position) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(getContext());
        }
        View convertView = layoutInflater.inflate(R.layout.friends_item_comment, null, false);

        TextView commentTv = (TextView) convertView.findViewById(R.id.commentTv);
        final CircleMovementMethod circleMovementMethod = new CircleMovementMethod(itemSelectorColor, itemSelectorColor);

        final FriendsData.DataDTO.MessageDTO.CommentAndRepliesDTO bean = mDatas.get(position);
        String name = TextUtils.isEmpty(bean.getCommentatornick_name()) ? bean.getReplyName() : bean.getCommentatornick_name();
        int id = bean.getId();
        String toReplyName = "";
        if (bean.getType() == 2) {
            toReplyName = bean.getReplyToName();
        }

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(setClickableSpan(name, bean.getIm_id()));

        if (!TextUtils.isEmpty(toReplyName)) {

            builder.append(" 回复 ");
            builder.append(setClickableSpan(toReplyName, bean.getReplyToId()));
        }
        builder.append(": ");
        //转换表情字符
        String contentBodyStr = bean.getContent();
        builder.append(UrlUtils.formatUrlString(contentBodyStr));
        commentTv.setText(builder);

        commentTv.setMovementMethod(circleMovementMethod);
        commentTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circleMovementMethod.isPassToTv()) {
                    if (onItemClickListener != null) {
//                        Log.d(TAG, "onClick: po==="+position);
                        onItemClickListener.onItemClick(position, bean);
                    }
                }

            }
        });
        commentTv.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (circleMovementMethod.isPassToTv()) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(position);
                    }
                    return true;
                }
                return false;
            }
        });

        return convertView;
    }

    private static final String TAG = "HHH";

    @NonNull
    private SpannableString setClickableSpan(final String textStr, final String id) {
        SpannableString subjectSpanText = new SpannableString(textStr);
        subjectSpanText.setSpan(new SpannableClickable(itemColor) {
                                    @Override
                                    public void onClick(View widget) {
                                        //跳转详细资料
                                        ArrayList<String> list = new ArrayList<>();
                                        list.add(id);
                                        if (UserInfo.getInstance().getUserId().equals(id)) {
                                            //是自己
                                            ChatInfo chatInfo = new ChatInfo();
                                            chatInfo.setId(id);
                                            chatInfo.setType(V2TIMConversation.V2TIM_C2C);
                                            Intent intent = new Intent(MyApplication.getInstance(), FriendProfileActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.putExtra(TUIKitConstants.ProfileType.CONTENT, chatInfo);
                                            MyApplication.getInstance().startActivity(intent);
                                        } else {
                                            final ContactItemBean bean = new ContactItemBean();
                                            bean.setFriend(true);
                                            V2TIMManager.getFriendshipManager().getFriendsInfo(list, new V2TIMValueCallback<List<V2TIMFriendInfoResult>>() {
                                                @Override
                                                public void onError(int i, String s) {

                                                }

                                                @Override
                                                public void onSuccess(List<V2TIMFriendInfoResult> v2TIMFriendInfoResults) {
                                                    if (v2TIMFriendInfoResults != null && v2TIMFriendInfoResults.size() > 0) {

                                                        V2TIMFriendInfoResult v2TIMFriendInfoResult = v2TIMFriendInfoResults.get(0);
                                                        V2TIMFriendInfo friendInfo = v2TIMFriendInfoResult.getFriendInfo();
                                                        V2TIMUserFullInfo userProfile = friendInfo.getUserProfile();

                                                        bean.setRemark(friendInfo.getFriendRemark());
                                                        bean.setId(friendInfo.getUserID());
                                                        bean.setNickname(userProfile.getNickName());
                                                        bean.setAvatarurl(userProfile.getFaceUrl());
                                                        Intent intent = new Intent(MyApplication.getInstance(), FriendProfileActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        intent.putExtra(TUIKitConstants.ProfileType.CONTENT, bean);
                                                        MyApplication.getInstance().startActivity(intent);

                                                    }
                                                }
                                            });
                                        }
                                    }
                                }, 0, subjectSpanText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }

    public static interface OnItemClickListener {
        public void onItemClick(int position, FriendsData.DataDTO.MessageDTO.CommentAndRepliesDTO bean);
    }

    public static interface OnItemLongClickListener {
        public void onItemLongClick(int position);
    }


}
