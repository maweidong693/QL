package com.tencent.qcloud.tim.uikit.modules.contact;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.common.Constant;
import com.tencent.common.http.HttpCallBack;
import com.tencent.common.http.YHttp;
import com.tencent.imsdk.v2.V2TIMFriendApplicationResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.uikit.NewFriendCount;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.GlideEngine;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;


public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    protected List<ContactItemBean> mData;
    private ContactListView.OnSelectChangedListener mOnSelectChangedListener;
    private ContactListView.OnItemClickListener mOnClickListener;

    private int mPreSelectedPosition;
    private boolean isSingleSelectMode;

    public ContactAdapter(List<ContactItemBean> data) {
        this.mData = data;
    }

    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_selecable_adapter_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ContactAdapter.ViewHolder holder, final int position) {
        final ContactItemBean contactBean = mData.get(position);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.line.getLayoutParams();
        if (position < mData.size() - 1) {
            String tag1 = contactBean.getSuspensionTag();
            String tag2 = mData.get(position + 1).getSuspensionTag();
            // tag????????????item??????????????????????????????
            if (TextUtils.equals(tag1, tag2)) {
                params.leftMargin = holder.tvName.getLeft();
            } else {
                params.leftMargin = 0;
            }
        } else {
            params.leftMargin = 0;
        }
        holder.line.setLayoutParams(params);
        if (!TextUtils.isEmpty(contactBean.getRemark())) {
            holder.tvName.setText(contactBean.getRemark());
        } else if (!TextUtils.isEmpty(contactBean.getNickname())) {
            holder.tvName.setText(contactBean.getNickname());
        } else {
            holder.tvName.setText(contactBean.getId());
        }
        if (mOnSelectChangedListener != null) {
            holder.ccSelect.setVisibility(View.VISIBLE);
            holder.ccSelect.setChecked(contactBean.isSelected());
        }

        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!contactBean.isEnable()) {
                    return;
                }
                holder.ccSelect.setChecked(!holder.ccSelect.isChecked());
                if (mOnSelectChangedListener != null) {
                    mOnSelectChangedListener.onSelectChanged(getItem(position), holder.ccSelect.isChecked());
                }
                contactBean.setSelected(holder.ccSelect.isChecked());
                if (mOnClickListener != null) {
                    mOnClickListener.onItemClick(position, contactBean);
                }
                if (isSingleSelectMode && position != mPreSelectedPosition && contactBean.isSelected()) {
                    //???????????????prePos??????
                    mData.get(mPreSelectedPosition).setSelected(false);
                    notifyItemChanged(mPreSelectedPosition);
                }
                mPreSelectedPosition = position;
            }
        });
        if (contactBean.getUnread() == 0) {

            holder.unreadText.setVisibility(View.GONE);
        } else {
            holder.unreadText.setVisibility(View.VISIBLE);
            holder.unreadText.setText(contactBean.getUnread() + "");
        }
        if (TextUtils.equals(holder.itemView.getContext().getResources().getString(R.string.new_friend), contactBean.getId())) {
            holder.avatar.setImageResource(R.drawable.group_new_friend);

        } else if (TextUtils.equals(holder.itemView.getContext().getResources().getString(R.string.group), contactBean.getId())) {
            holder.avatar.setImageResource(R.drawable.group_common_list);
        } else if (TextUtils.equals(holder.itemView.getContext().getResources().getString(R.string.blacklist), contactBean.getId())) {
            holder.avatar.setImageResource(R.drawable.group_black_list);
        } else {
            if (TextUtils.isEmpty(contactBean.getAvatarurl())) {
                if (contactBean.isGroup()) {
                    holder.avatar.setImageResource(R.drawable.ic_launcher);
                } else {
                    holder.avatar.setImageResource(R.drawable.ic_launcher);
                }
            } else {
                GlideEngine.loadCornerImage(holder.avatar, contactBean.getAvatarurl(), null, 10);
//                GlideEngine.loadImage(holder.avatar, Uri.parse(contactBean.getAvatarurl()));
            }
        }

    }

    private static final String TAG = "ContactAdapter";

    @Override
    public void onViewRecycled(ContactAdapter.ViewHolder holder) {
        if (holder != null) {
            GlideEngine.clear(holder.avatar);
            holder.avatar.setImageResource(0);

        }
        super.onViewRecycled(holder);
    }

    private ContactItemBean getItem(int position) {
        if (position < mData.size()) {
            return mData.get(position);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public void setDataSource(List<ContactItemBean> datas) {
        this.mData = datas;
        notifyDataSetChanged();
    }

    public void setSingleSelectMode(boolean mode) {
        isSingleSelectMode = mode;
    }

    public void setOnSelectChangedListener(ContactListView.OnSelectChangedListener selectListener) {
        mOnSelectChangedListener = selectListener;
    }

    public void setOnItemClickListener(ContactListView.OnItemClickListener listener) {
        mOnClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView unreadText;
        ImageView avatar;
        CheckBox ccSelect;
        View content;
        View line;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCity);
            unreadText = itemView.findViewById(R.id.conversation_unread);
            unreadText.setVisibility(View.GONE);
            avatar = itemView.findViewById(R.id.ivAvatar);
            ccSelect = itemView.findViewById(R.id.contact_check_box);
            content = itemView.findViewById(R.id.selectable_contact_item);
            line = itemView.findViewById(R.id.view_line);
        }
    }
}
