package com.weiwu.ql.main.mine.friends.adapter;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.GlideEngine;
import com.tencent.qcloud.tim.uikit.utils.TimeUtil;
import com.weiwu.ql.R;
import com.weiwu.ql.main.mine.friends.data.MsgData;

import java.util.ArrayList;

/**
 * 图片展示
 */
public class NotRedMsgsAdapter extends BaseQuickAdapter<MsgData.MsgEntity, BaseViewHolder> {
    public NotRedMsgsAdapter() {
        super(R.layout.item_not_red_msg,new ArrayList<>());
    }


    @Override
    protected void convert(BaseViewHolder helper, MsgData.MsgEntity item) {
        if (item.getType() == 1) {
            // 1 评论
            helper.getView(R.id.img_like).setVisibility(View.GONE);
            helper.setText(R.id.tv_content,item.getContent());
        } else if (item.getType() == 2){
            // 2回复
            helper.getView(R.id.img_like).setVisibility(View.GONE);
//            SpannableStringBuilder builder = new SpannableStringBuilder();
//            builder.append(" 回复 ");
//            builder.append(setClickableSpan(item.getReplyTonNick(), bean.getToReplyUser().getIm_id()));
//            builder.append(": ");
//            builder.append(item.getContent());
//            helper.setText(R.id.tv_content,builder);

            SpannableStringBuilder builder = new SpannableStringBuilder();
            builder.append("回复").append(item.getReplyTonNick()).append(": ").append(item.getContent());

            SpannableString spannableString = new SpannableString(builder);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#8290AF"))
                    , 2
                    , 2+ item.getReplyTonNick().length()
                    , Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            helper.setText(R.id.tv_content,spannableString);
        }else {
            //3点赞
            helper.getView(R.id.img_like).setVisibility(View.VISIBLE);

        }
        ImageView img = helper.getView(R.id.img);
        if (!TextUtils.isEmpty(item.getMsgPic())) {
            GlideEngine.loadCornerImage(img,item.getMsgPic(),null,10);
            helper.getView(R.id.content).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.content).setVisibility(View.VISIBLE);
            helper.setText(R.id.content,item.getMsgContent());
        }

        ImageView iv_head = helper.getView(R.id.iv_head);
        helper.setText(R.id.tv_neme,item.getReminderName());
        helper.setText(R.id.tv_time, TimeUtil.timeParseToday(item.getCreateTime()));
        GlideEngine.loadCornerImage(iv_head,item.getReminderIcon(),null,10);

    }

}