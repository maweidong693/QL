package com.tencent.qcloud.tim.uikit.modules.contact;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.tencent.imsdk.v2.V2TIMFriendApplicationResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.GlideEngine;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;


public class MuteMemberAdapter extends RecyclerView.Adapter<MuteMemberAdapter.ViewHolder> {

    protected List<ContactItemBean> mData;
    protected int mType;
    protected LayoutInflater mInflater;
    private GroupMuteMemberListView.OnSelectChangedListener mOnSelectChangedListener;
    private GroupMuteMemberListView.OnItemClickListener mOnClickListener;

    private int mPreSelectedPosition;
    private boolean isSingleSelectMode;

    public MuteMemberAdapter(List<ContactItemBean> data, int type) {
        this.mData = data;
        this.mType = type;
        mInflater = LayoutInflater.from(TUIKit.getAppContext());
    }

    @Override
    public MuteMemberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MuteMemberAdapter.ViewHolder(mInflater.inflate(R.layout.mute_adapter_item, parent, false));
    }

    private static final String TAG = "MuteMemberAdapter";

    @Override
    public void onBindViewHolder(final MuteMemberAdapter.ViewHolder holder, final int position) {
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
        holder.ccSelect.setVisibility(View.GONE);

        if (isSingleSelectMode && contactBean.isSelected()) {
            mPreSelectedPosition = position;
        }
        int isForbidden = contactBean.getIsForbidden();
        int isManger = contactBean.getIsManger();
//        byte[] mutes = customInfo.get("mute");
        if (mType == 3) {
            if (isForbidden == 1) {
                holder.mBtnSwitch.setChecked(true);
            } else {
                holder.mBtnSwitch.setChecked(false);
            }
        } else {
            if (isManger == 1) {
                holder.mBtnSwitch.setChecked(true);
            } else {
                holder.mBtnSwitch.setChecked(false);
            }
        }
        holder.mBtnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!buttonView.isPressed()) {
                    return;
                }
                if (mOnSelectChangedListener != null) {
                    mOnSelectChangedListener.onSelectChanged(getItem(position), isChecked);
                }
            }
        });
        holder.unreadText.setVisibility(View.GONE);
        if (TextUtils.equals(TUIKit.getAppContext().getResources().getString(R.string.new_friend), contactBean.getId())) {
            holder.avatar.setImageResource(R.drawable.group_new_friend);

            V2TIMManager.getFriendshipManager().getFriendApplicationList(new V2TIMValueCallback<V2TIMFriendApplicationResult>() {
                @Override
                public void onError(int code, String desc) {
                    ToastUtil.toastShortMessage("Error code = " + code + ", desc = " + desc);
                }

                @Override
                public void onSuccess(V2TIMFriendApplicationResult v2TIMFriendApplicationResult) {
                    if (v2TIMFriendApplicationResult.getFriendApplicationList() != null) {
                        int pendingRequest = v2TIMFriendApplicationResult.getFriendApplicationList().size();
                        if (pendingRequest == 0) {
                            holder.unreadText.setVisibility(View.GONE);
                        } else {
                            holder.unreadText.setVisibility(View.VISIBLE);
                            holder.unreadText.setText("" + pendingRequest);
                        }
                    }
                }
            });
        } else if (TextUtils.equals(TUIKit.getAppContext().getResources().getString(R.string.group), contactBean.getId())) {
            holder.avatar.setImageResource(R.drawable.group_common_list);
        } else if (TextUtils.equals(TUIKit.getAppContext().getResources().getString(R.string.blacklist), contactBean.getId())) {
            holder.avatar.setImageResource(R.drawable.group_black_list);
        } else {
            if (TextUtils.isEmpty(contactBean.getAvatarurl())) {
                if (contactBean.isGroup()) {
                    holder.avatar.setImageResource(R.drawable.default_head);
                } else {
                    holder.avatar.setImageResource(R.drawable.default_head);
                }
            } else {
                GlideEngine.loadCornerImage(holder.avatar, contactBean.getAvatarurl(), null, 10);
//                GlideEngine.loadImage(holder.avatar, Uri.parse(contactBean.getAvatarurl()));
            }
        }

    }

    @Override
    public void onViewRecycled(MuteMemberAdapter.ViewHolder holder) {
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

    public void setDataSource(List<ContactItemBean> datas, int type) {
        this.mData = datas;
        this.mType = type;
        notifyDataSetChanged();
    }

    public void setSingleSelectMode(boolean mode) {
        isSingleSelectMode = mode;
    }

    public boolean getSingleSelectMode() {
        return isSingleSelectMode;
    }

    public void setOnSelectChangedListener(GroupMuteMemberListView.OnSelectChangedListener selectListener) {
        mOnSelectChangedListener = selectListener;
    }

    public void setOnItemClickListener(GroupMuteMemberListView.OnItemClickListener listener) {
        mOnClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView unreadText;
        ImageView avatar;
        CheckBox ccSelect;
        Switch mBtnSwitch;
        View content;
        View line;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCity);
            unreadText = itemView.findViewById(R.id.conversation_unread);
            unreadText.setVisibility(View.GONE);
            avatar = itemView.findViewById(R.id.ivAvatar);
            mBtnSwitch = itemView.findViewById(R.id.btnSwitch);
            ccSelect = itemView.findViewById(R.id.contact_check_box);
            content = itemView.findViewById(R.id.selectable_contact_item);
            line = itemView.findViewById(R.id.view_line);
        }
    }
}
