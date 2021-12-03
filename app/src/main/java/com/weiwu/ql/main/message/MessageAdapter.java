package com.weiwu.ql.main.message;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.weiwu.ql.R;
import com.weiwu.ql.main.contact.chat.adapter.ChatMessageAdapter;
import com.weiwu.ql.main.contact.chat.modle.HistoryMessageData;
import com.weiwu.ql.main.contact.chat.modle.MessageListData;
import com.weiwu.ql.utils.SystemFacade;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/22 14:35 
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<MessageListData.DataDTO.ListDTO> mList = new ArrayList<>();

    public void setList(List<MessageListData.DataDTO.ListDTO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_message, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MessageViewHolder holder, int position) {
        MessageListData.DataDTO.ListDTO data = mList.get(position);
        holder.setData(data);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChatClickListener != null) {
                    mChatClickListener.mClick(data);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mMessageLongClickListener != null) {

                    mMessageLongClickListener.mLongClick(data,holder.mLastContent);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        private ImageView mAvator;
        private TextView mLastContent, mNickName, mTime, mCount;

        public MessageViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mAvator = itemView.findViewById(R.id.iv_message_head);
            mLastContent = itemView.findViewById(R.id.tv_message_info);
            mNickName = itemView.findViewById(R.id.tv_message_nick_name);
            mTime = itemView.findViewById(R.id.tv_message_time);
            mCount = itemView.findViewById(R.id.tv_chat_new_msg_count);
        }

        public void setData(MessageListData.DataDTO.ListDTO data) {
            if (TextUtils.isEmpty(data.getAvatar_url())) {
                Glide.with(itemView.getContext()).load(itemView.getContext().getResources().getDrawable(R.drawable.ic_launcher)).into(mAvator);
            } else {
                Glide.with(itemView.getContext()).load(data.getAvatar_url()).into(mAvator);
            }
            mLastContent.setText(data.getContent());
            mNickName.setText(data.getName());
            if (!TextUtils.isEmpty(data.getLast_time())) {
                mTime.setText(SystemFacade.getNewChatTime(data.getLast_time()));
            }
            if (data.getUnread_count() == 0) {
                mCount.setVisibility(View.GONE);
            } else {
                mCount.setVisibility(View.VISIBLE);
                mCount.setText(data.getUnread_count() + "");
            }
        }
    }

    private IMessageLongClickListener mMessageLongClickListener;

    public void setMessageLongClickListener(IMessageLongClickListener messageLongClickListener) {
        mMessageLongClickListener = messageLongClickListener;
    }

    public interface IMessageLongClickListener {
        void mLongClick(MessageListData.DataDTO.ListDTO data,View view);
    }


    private IChatClickListener mChatClickListener;

    public void setChatClickListener(IChatClickListener chatClickListener) {
        mChatClickListener = chatClickListener;
    }

    public interface IChatClickListener {
        void mClick(MessageListData.DataDTO.ListDTO data);
    }
}
