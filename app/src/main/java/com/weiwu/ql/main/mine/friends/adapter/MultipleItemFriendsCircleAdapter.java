package com.weiwu.ql.main.mine.friends.adapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.GlideEngine;
import com.tencent.qcloud.tim.uikit.utils.TimeUtil;
import com.weiwu.ql.R;
import com.weiwu.ql.data.bean.FriendsData;
import com.weiwu.ql.main.mine.friends.data.PhotoInfo;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Joseph_Yan
 * on 2020/11/5
 */
public class MultipleItemFriendsCircleAdapter extends BaseMultiItemQuickAdapter<FriendsData.DataDTO.MessageDTO, BaseViewHolder> {

    public MultipleItemFriendsCircleAdapter(List<FriendsData.DataDTO.MessageDTO> data) {
        super(data);
        // 绑定 layout 对应的 type
        addItemType(FriendsData.DataDTO.MessageDTO.TYPE_CIRCLE_TEXT, R.layout.item_text_view);
        addItemType(FriendsData.DataDTO.MessageDTO.TYPE_CIRCLE_IMG, R.layout.item_image_view);
        addItemType(FriendsData.DataDTO.MessageDTO.TYPE_CIRCLE_VIDEO, R.layout.item_video_view);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, FriendsData.DataDTO.MessageDTO item) {
        // 根据返回的 type 分别设置数据
        String createTime  =  TimeUtil.getTimeSString(TimeUtil.dateToString(item.getCreate_time()));
        List<FriendsData.DataDTO.MessageDTO> data = getData();
        if (helper.getAdapterPosition()>0){
            FriendsData.DataDTO.MessageDTO lastCircleItem = data.get(helper.getAdapterPosition() - 1);
            String lastDate = TimeUtil.getTimeSString(TimeUtil.dateToString(lastCircleItem.getCreate_time()));
            if (lastDate.equals(createTime)){
                //同一天
                helper.setVisible(R.id.tv_data,false);
            }
        }
        helper.setText(R.id.tv_content,item.getText_content());
        if (createTime.equals("今天")||createTime.equals("昨天")||createTime.equals("前天")){
            ((TextView)helper.getView(R.id.tv_data)).setText(createTime);
        }else {
            SpannableString spannableString = new SpannableString(createTime);
              spannableString.setSpan(new AbsoluteSizeSpan(70), 0, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
             spannableString.setSpan(new AbsoluteSizeSpan(40), 2, createTime.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            ((TextView)helper.getView(R.id.tv_data)).setText(spannableString);
        }
        switch (helper.getItemViewType()) {
            case FriendsData.DataDTO.MessageDTO.TYPE_CIRCLE_TEXT:

                break;
            case FriendsData.DataDTO.MessageDTO.TYPE_CIRCLE_IMG:
                List<PhotoInfo> photos = new ArrayList<>();
                List<String> imgUrl = item.getPics();
                for (int i = 0; i < imgUrl.size(); i++) {
                    PhotoInfo photoInfo = new PhotoInfo();
                    photoInfo.url = imgUrl.get(i);
                    photos.add(photoInfo);
                }
                if (photos != null && photos.size() > 0) {
                    if (photos.size()==1){
                        helper.setGone(R.id.img_2_2,true)
                                .setGone(R.id.img_3,true)
                                .setGone(R.id.img_4,true)
                        .setVisible(R.id.img_2_1,true);
                        GlideEngine.loadImage(helper.getView(R.id.img_2_1),photos.get(0).url);

                    }else if (photos.size()==2){
                        helper.setVisible(R.id.img_2_2,true)
                                .setGone(R.id.img_3,true)
                                .setGone(R.id.img_4,true)
                                .setVisible(R.id.img_2_1,true);
                        GlideEngine.loadImage(helper.getView(R.id.img_2_1),photos.get(0).url);
                        GlideEngine.loadImage(helper.getView(R.id.img_2_2),photos.get(1).url);

                    }else if (photos.size()==3){
                        helper.setGone(R.id.img_2_2,true)
                                .setVisible(R.id.img_3,true)
                                .setGone(R.id.img_4,true)
                                .setVisible(R.id.img_2_1,true);
                        GlideEngine.loadImage(helper.getView(R.id.img_2_1),photos.get(0).url);
                        GlideEngine.loadImage(helper.getView(R.id.img_3_1),photos.get(1).url);
                        GlideEngine.loadImage(helper.getView(R.id.img_3_2),photos.get(2).url);

                    }else if (photos.size()>=4){
                        helper.setGone(R.id.img_2_2,true)
                                .setVisible(R.id.img_3,true)
                                .setVisible(R.id.img_4,true)
                                .setGone(R.id.img_2_1,true);
                        GlideEngine.loadImage(helper.getView(R.id.img_4_1),photos.get(0).url);
                        GlideEngine.loadImage(helper.getView(R.id.img_4_2),photos.get(1).url);
                        GlideEngine.loadImage(helper.getView(R.id.img_3_1),photos.get(2).url);
                        GlideEngine.loadImage(helper.getView(R.id.img_3_2),photos.get(3).url);

                    }
                    helper
                            .setGone(R.id.tv_num,photos.size()==1)
                    .setText(R.id.tv_num,"共"+photos.size()+"张");

                }

                break;
            case FriendsData.DataDTO.MessageDTO.TYPE_CIRCLE_VIDEO:
                Glide.with(getContext())
                        .setDefaultRequestOptions(
                                new RequestOptions()
                                        .frame(500000)
                                        .centerCrop()
                                        .error(R.drawable.nim_default_img_failed)
                                        .placeholder(R.drawable.nim_default_img_failed))
                        .load(item.getMediaUrl())
                        .into((ImageView) helper.getView(R.id.Thumbnail));
                break;
            default:
                break;
        }
    }
}
