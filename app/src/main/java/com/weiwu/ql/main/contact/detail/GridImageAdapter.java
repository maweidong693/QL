package com.weiwu.ql.main.contact.detail;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnItemClickListener;
import com.weiwu.ql.R;
import com.weiwu.ql.view.GlideEngine;

import java.util.ArrayList;
import java.util.List;


/**
 * @author：luck
 * @date：2016-7-27 23:02
 * @describe：GridImageAdapter
 */
public class GridImageAdapter extends
        RecyclerView.Adapter<GridImageAdapter.ViewHolder> {
    public static final String TAG = "PictureSelector";
    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_PICTURE = 2;
    private LayoutInflater mInflater;
    private List<String> list = new ArrayList<>();
    private int selectMax = 9;
    /**
     * 点击添加图片跳转
     */
    private onAddPicClickListener mOnAddPicClickListener;

    public interface onAddPicClickListener {
        void onAddPicClick();
    }


    public GridImageAdapter(Context context, onAddPicClickListener mOnAddPicClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mOnAddPicClickListener = mOnAddPicClickListener;
    }
    public GridImageAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public List<String> getData() {
        return list == null ? new ArrayList<>() : list;
    }

    public void remove(int position) {
        if (list != null && position < list.size()) {
            list.remove(position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImg;
        TextView tvDuration;

        public ViewHolder(View view) {
            super(view);
            mImg = view.findViewById(R.id.fiv);
            tvDuration = view.findViewById(R.id.tv_duration);
        }
    }

    @Override
    public int getItemCount() {
            return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowAddItem(position)) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PICTURE;
        }
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.gv_filter_image,
                viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    private boolean isShowAddItem(int position) {
        int size = list.size() == 0 ? 0 : list.size();
        return position == size;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        String media = list.get(position);
        if (media == null
                || TextUtils.isEmpty(media)) {
            return;
        }

//        viewHolder.tvDuration.setVisibility(PictureMimeType.isHasVideo(media.getMimeType())
//                ? View.VISIBLE : View.GONE);
//        if (PictureMimeType.isHasVideo(media.getMimeType())) {
//            viewHolder.tvDuration.setCompoundDrawablesRelativeWithIntrinsicBounds
//                    (R.drawable.picture_icon_video, 0, 0, 0);
//
//        }
//        Glide.with(viewHolder.itemView.getContext())
//                .load(path)
//                .centerCrop()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(viewHolder.mImg);
        GlideEngine.createGlideEngine().loadCornerImage(viewHolder.mImg,media,null,2);
//        viewHolder.mImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mItemClickListener!=null){
//                    mItemClickListener.onItemClick(view,position);
//                }
//            }
//        });

    }

    private OnItemClickListener mItemClickListener;

    public void setOnItemClickListener(OnItemClickListener l) {
        this.mItemClickListener = l;
    }

}
