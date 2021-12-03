package com.weiwu.ql.main.contact.new_friend;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.tencent.common.Constant;
import com.tencent.common.http.BaseBean;
import com.tencent.common.http.HttpCallBack;
import com.tencent.common.http.YHttp;
import com.tencent.qcloud.tim.uikit.NewFriendBean;
import com.tencent.qcloud.tim.uikit.component.CircleImageView;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.weiwu.ql.R;
import com.weiwu.ql.data.bean.NewFriendListData;
import com.weiwu.ql.main.contact.detail.FriendProfileActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/5/20 13:03 
 */
public class NewFriendAdapter extends ArrayAdapter<NewFriendListData.DataDTO> {

    private static final String TAG = "cccccc";

    private int mResourceId;
    private View mView;
    private ViewHolder mViewHolder;

    public NewFriendAdapter(Context context, int resource, List<NewFriendListData.DataDTO> objects) {
        super(context, resource, objects);
        mResourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        NewFriendListData.DataDTO data = getItem(position);
        if (convertView != null) {
            mView = convertView;
            mViewHolder = (ViewHolder) mView.getTag();

        } else {
            mView = LayoutInflater.from(getContext()).inflate(mResourceId, null);
            if (data.getStatus() == 0) {
                /*mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(parent.getContext(), FriendProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Constant.CONTENT, data);
                        parent.getContext().startActivity(intent);
                    }
                });*/
            }
            mViewHolder = new ViewHolder();
            mViewHolder.avatar = (CircleImageView) mView.findViewById(R.id.avatar);
            mViewHolder.name = mView.findViewById(R.id.name);
            mViewHolder.des = mView.findViewById(R.id.description);
            mViewHolder.agree = mView.findViewById(R.id.agree);
            mViewHolder.refuse = mView.findViewById(R.id.refuse);
            mView.setTag(mViewHolder);
        }
        Resources res = getContext().getResources();
        if (TextUtils.isEmpty(data.getFace_url())) {
            mViewHolder.avatar.setImageResource(R.drawable.ic_personal_member);
        } else {
            Glide.with(mView.getContext()).load(data.getFace_url()).into(mViewHolder.avatar);
        }

        mViewHolder.name.setText(TextUtils.isEmpty(data.getNick_name()) ? data.getId() + "" : data.getNick_name());
        mViewHolder.des.setText(TextUtils.isEmpty(data.getRemark()) ? data.getCreate_time() : data.getRemark());
        switch (data.getStatus()) {
            case 0:
                mViewHolder.agree.setText(res.getString(R.string.request_agree));
                mViewHolder.agree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mNewFriendRequestItemClickListener != null) {
                            mNewFriendRequestItemClickListener.mItemClick(data);
                        }
                    }
                });

                break;

            case 1:
                mViewHolder.agree.setText("已通过");
                break;
            case 2:
                mViewHolder.agree.setText("已拒绝");
                break;
        }
        return mView;
    }

    private void doResponse(final TextView view, NewFriendListData.DataDTO data) {

        /*Map map = new HashMap();
        map.put("id", data.getId());
        map.put("status", 1);
        YHttp.obtain().post(Constant.URL_ADD_FRIEND_STATUS, map, new HttpCallBack<BaseBean>() {

            @Override
            public void onSuccess(BaseBean baseBean) {
                view.setText(getContext().getResources().getString(R.string.request_accepted));
                ToastUtil.toastShortMessage(baseBean.getMsg());
            }

            @Override
            public void onFailed(String error) {

            }
        });*/
    }

    public class ViewHolder {
        Button refuse;
        ImageView avatar;
        TextView name;
        TextView des;
        Button agree;
    }

    private INewFriendRequestItemClickListener mNewFriendRequestItemClickListener;

    public void setNewFriendRequestItemClickListener(INewFriendRequestItemClickListener newFriendRequestItemClickListener) {
        mNewFriendRequestItemClickListener = newFriendRequestItemClickListener;
    }

    public interface INewFriendRequestItemClickListener {
        void mItemClick(NewFriendListData.DataDTO dataDTO);
    }
}
