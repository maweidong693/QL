package com.tencent.qcloud.tim.uikit.modules.contact;

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


public class GrouoMemberAdapter extends RecyclerView.Adapter<GrouoMemberAdapter.ViewHolder> {

    protected List<ContactItemBean> mData;
    protected LayoutInflater mInflater;
    private GroupMemberListView.OnSelectChangedListener mOnSelectChangedListener;
    private GroupMemberListView.OnItemClickListener mOnClickListener;

    private int mPreSelectedPosition;
    private boolean isSingleSelectMode;
    private int type; //1选择新群主   2管理员    3选择联系人发送名片

    public GrouoMemberAdapter(List<ContactItemBean> data) {
        this.mData = data;
        mInflater = LayoutInflater.from(TUIKit.getAppContext());
    }

    @Override
    public GrouoMemberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GrouoMemberAdapter.ViewHolder(mInflater.inflate(R.layout.contact_selecable_adapter_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final GrouoMemberAdapter.ViewHolder holder, final int position) {
        final ContactItemBean contactBean = mData.get(position);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.line.getLayoutParams();
        if (position < mData.size() - 1) {
            String tag1 = contactBean.getSuspensionTag();
            String tag2 = mData.get(position + 1).getSuspensionTag();
            // tag不同时对item的分割线进行重新处理
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
        if (isSingleSelectMode && contactBean.isSelected()) {
            mPreSelectedPosition = position;
        }

        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!contactBean.isEnable()) {
                    return;
                }
                if (type == 2 && !holder.ccSelect.isChecked()) {
                    int selectCount = 0;
                    for (int i = 0; i < mData.size(); i++) {
                        if (mData.get(i).isSelected()) {
                            selectCount++;
                            if (selectCount == 5) {
                                ToastUtil.toastShortMessage("最多只能设置5个");
                                return;
                            }
                        }
                    }
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
                    //单选模式的prePos处理
                    mData.get(mPreSelectedPosition).setSelected(false);
                    if (mOnSelectChangedListener != null) {
                        mOnSelectChangedListener.onSelectChanged(mData.get(mPreSelectedPosition), false);
                    }
                    notifyItemChanged(mPreSelectedPosition);
                }
                mPreSelectedPosition = position;
            }
        });
        holder.unreadText.setVisibility(View.GONE);
        if (TextUtils.equals(TUIKit.getAppContext().getResources().getString(R.string.new_friend), contactBean.getId())) {
            holder.avatar.setImageResource(R.drawable.group_new_friend);

            /*V2TIMManager.getFriendshipManager().getFriendApplicationList(new V2TIMValueCallback<V2TIMFriendApplicationResult>() {
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
            });*/
            Map map = new HashMap();
            YHttp.obtain().post(Constant.URL_NEW_FRIEND_COUNT, map, new HttpCallBack<NewFriendCount>() {
                @Override
                public void onSuccess(NewFriendCount newFriendCount) {
                    if (newFriendCount != null) {
                        int count = newFriendCount.getData().getCount();
                        if (count == 0) {
                            holder.unreadText.setVisibility(View.GONE);
                        } else {
                            holder.unreadText.setVisibility(View.VISIBLE);
                            holder.unreadText.setText(String.valueOf(count));
                        }
                    }
                }

                @Override
                public void onFailed(String error) {
                    ToastUtil.toastShortMessage("desc = " + error);
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

    private static final String TAG = "GrouoMemberAdapter";

    @Override
    public void onViewRecycled(GrouoMemberAdapter.ViewHolder holder) {
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

    public boolean getSingleSelectMode() {
        return isSingleSelectMode;
    }

    public void setOnSelectChangedListener(GroupMemberListView.OnSelectChangedListener selectListener) {
        mOnSelectChangedListener = selectListener;
    }

    public void setOnItemClickListener(GroupMemberListView.OnItemClickListener listener) {
        mOnClickListener = listener;
    }

    public void setType(int type) {
        this.type = type;
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
