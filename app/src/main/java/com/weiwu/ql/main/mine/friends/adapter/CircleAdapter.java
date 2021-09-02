package com.weiwu.ql.main.mine.friends.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hitomi.tilibrary.style.index.NumberIndexIndicator;
import com.hitomi.tilibrary.style.progress.ProgressBarIndicator;
import com.hitomi.tilibrary.transfer.TransferConfig;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.tencent.common.Constant;
import com.tencent.common.UserInfo;
import com.tencent.common.dialog.CustomAlertDialog;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMFriendInfo;
import com.tencent.imsdk.v2.V2TIMFriendInfoResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.uikit.component.dialog.TUIKitDialog;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.vansz.glideimageloader.GlideImageLoader;
import com.weiwu.ql.AppConstant;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.R;
import com.weiwu.ql.data.bean.FriendsData;
import com.weiwu.ql.data.bean.NewMsgCount;
import com.weiwu.ql.main.contact.detail.FriendProfileActivity;
import com.weiwu.ql.main.mine.friends.FriendsActivity;
import com.weiwu.ql.main.mine.friends.data.ActionItem;
import com.weiwu.ql.main.mine.friends.data.CircleItem;
import com.weiwu.ql.main.mine.friends.data.CommentConfig;
import com.weiwu.ql.main.mine.friends.data.CommentItem;
import com.weiwu.ql.main.mine.friends.data.FavortItem;
import com.weiwu.ql.main.mine.friends.data.PhotoInfo;
import com.weiwu.ql.main.mine.friends.utils.DatasUtil;
import com.weiwu.ql.main.mine.friends.utils.GlideCircleTransform;
import com.weiwu.ql.main.mine.friends.utils.UrlUtils;
import com.weiwu.ql.main.mine.friends.view.CommentDialog;
import com.weiwu.ql.main.mine.friends.view.CommentListView;
import com.weiwu.ql.main.mine.friends.view.ExpandTextView;
import com.weiwu.ql.main.mine.friends.view.MultiImageView;
import com.weiwu.ql.main.mine.friends.view.PraiseListView;
import com.weiwu.ql.main.mine.friends.view.SnsPopupWindow;
import com.weiwu.ql.utils.SPUtils;
import com.weiwu.ql.view.GlideEngine;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiwei on 16/5/17.
 */
public class CircleAdapter extends BaseRecycleViewAdapter {

    public final static int TYPE_HEAD = 0;

    private static final int STATE_IDLE = 0;
    private static final int STATE_ACTIVED = 1;
    private static final int STATE_DEACTIVED = 2;
    private int videoState = STATE_IDLE;
    public static final int HEADVIEW_SIZE = 1;
    public static final String TAG = "";

    int curPlayIndex = -1;

    //    private CirclePresenter presenter;
    private Context context;
    private List<FriendsData.DataDTO.ThumbListDTO> favortDatas;
    private List<FriendsData.DataDTO.CommentAndReplyListDTO> commentsDatas;

   /* public void setCirclePresenter(CirclePresenter presenter) {
        this.presenter = presenter;
    }*/

    public CircleAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        }

        int itemType = 0;
        FriendsData.DataDTO item = (FriendsData.DataDTO) datas.get(position - 1);
        if (item.getType() == 0) {
            itemType = CircleViewHolder.TYPE_TEXT;
        } else if (item.getType() == 5) {
            itemType = CircleViewHolder.TYPE_URL;
//            itemType = CircleViewHolder.TYPE_URL;
        } else if (item.getType() == 1) {
            itemType = CircleViewHolder.TYPE_IMAGE;
        } else if (item.getType() == 2) {
            itemType = CircleViewHolder.TYPE_VIDEO;
        } else if (item.getType() == 3) {
            itemType = CircleViewHolder.TYPE_URL;
//            itemType = CircleViewHolder.TYPE_MUSIC;
        }
        return itemType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == TYPE_HEAD) {
            View headView = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_head_circle, parent, false);
            viewHolder = new HeaderViewHolder(headView);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_adapter_circle_item, parent, false);

            if (viewType == CircleViewHolder.TYPE_TEXT) {
                viewHolder = new TextViewHolder(view);
            } else if (viewType == CircleViewHolder.TYPE_IMAGE) {
                viewHolder = new ImageViewHolder(view);
            } else if (viewType == CircleViewHolder.TYPE_VIDEO) {
//                viewHolder = new VideoViewHolder(view);
                viewHolder = new VideoPlayerViewHolder(view);
            } else if (viewType == CircleViewHolder.TYPE_MUSIC) {
//                viewHolder = new MusicViewHolder(view);
            }
        }

        return viewHolder;
    }

    //爬虫后刷新item
    WHandler handler = new WHandler();

    public class WHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what < datas.size()) {
                notifyItemChanged(msg.what);
            }
        }
    }

    public IHeadListener mHeadListener;

    public void setHeadListener(IHeadListener headListener) {
        mHeadListener = headListener;
    }

    public interface IHeadListener {
        void replaceHeadImg(LocalMedia data);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        if (getItemViewType(position) == TYPE_HEAD) {
            HeaderViewHolder mHeaderHolder = (HeaderViewHolder) viewHolder;
            GlideEngine.createGlideEngine().loadImage(context, headUser.getCircleBgUrl(), mHeaderHolder.mcircleBg);
//            GlideEngine.createGlideEngine().loadImage(context,headUser.getHeadUrl(),mHeaderHolder.circleHead);
            com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.GlideEngine.loadCornerImage(mHeaderHolder.circleHead, headUser.getHeadUrl(), null, Constant.RADIUS);
            mHeaderHolder.circleName.setText(headUser.getName());
            mHeaderHolder.mcircleBg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //弹出一个列表选择拍照或相册
                    CustomAlertDialog alertDialog = new CustomAlertDialog(context);
                    alertDialog.setTitle("");
                    String title = "更换封面";
                    alertDialog.addItem(title, () -> {
                        PictureSelector.create((Activity) context)
                                .openGallery(PictureMimeType.ofImage())
                                .selectionMode(PictureConfig.SINGLE)
                                .isGif(false)
                                .isEnableCrop(true)
                                .withAspectRatio(4, 3)
                                .showCropFrame(true)
                                .showCropGrid(false)
                                .isCompress(true)
                                .minimumCompressSize(1000)
                                .imageEngine(GlideEngine.createGlideEngine())
                                .forResult(new OnResultCallbackListener<LocalMedia>() {
                                    @Override
                                    public void onResult(List<LocalMedia> result) {
                                        if (result == null || result.isEmpty()) {
                                            return;
                                        }
                                        mHeadListener.replaceHeadImg(result.get(0));
                                        /*DialogMaker.showProgressDialog(context, null, "正在上传", true, new DialogInterface.OnCancelListener() {
                                            @Override
                                            public void onCancel(DialogInterface dialog) {

                                            }
                                        }).setCanceledOnTouchOutside(false);
                                        // 结果回调
                                        Map map = new HashMap();
                                        map.put("image", new File(result.get(0).getCutPath()));*/
                                        /*YHttp.obtain().post(Constant.URL_UPLOAD, map, new HttpCallBack<BaseBean>() {
                                            @Override
                                            public void onSuccess(BaseBean bean) {
                                                if (bean.getData() == null) {
                                                    return;
                                                }
                                                FileUploadBean uploadBean = JSON.parseObject(bean.getData().toString(), FileUploadBean.class);
                                                String mIconUrl = uploadBean.getUrl();
                                                Map map = new HashMap();
                                                map.put("background_img", mIconUrl);
                                                YHttp.obtain().post(Constant.URL_MODIFYBACKGROUNDIMG, map, new HttpCallBack<BaseBean>() {
                                                    @Override
                                                    public void onSuccess(BaseBean bean) {
                                                        if (bean.getData() == null) {
                                                            return;
                                                        }

                                                        UserInfo.getInstance().setCircleBg(mIconUrl);
                                                        GlideEngine.createGlideEngine().loadImage(context,mIconUrl,mHeaderHolder.mcircleBg);
                                                    }

                                                    @Override
                                                    public void onFailed(String error) {

                                                    }
                                                });

                                            }

                                            @Override
                                            public void onFailed(String error) {

                                            }
                                        });*/


                                    }

                                    @Override
                                    public void onCancel() {
                                        // 取消
                                    }
                                });

                    });
//                    title = "从相册选择";
//                    alertDialog.addItem(title, new CustomAlertDialog.onSeparateItemClickListener() {
//                        @Override
//                        public void onClick() {
//
//
//                        }
//                    });
                    alertDialog.show();

                }
            });
            mHeaderHolder.newRemind.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //新消息提醒
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("notice", true);
//                    IntentUtil.redirectToNextActivity(context, MsgListActivity.class,bundle );
                }
            });
            mHeaderHolder.circleHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //自己的朋友圈列表
                    ContactItemBean contactItemBean = new ContactItemBean();
                    contactItemBean.setId(headUser.getId());
                    contactItemBean.setAvatarurl(headUser.getHeadUrl());
                    contactItemBean.setNickname(headUser.getName());
                    /*ChatInfo chatInfo = new ChatInfo();
                    chatInfo.setId(SPUtils.getValue(AppConstant.USER, AppConstant.USER_ID));
                    chatInfo.setType(V2TIMConversation.V2TIM_C2C);*/
                    Intent intent = new Intent(MyApplication.getInstance(), FriendProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(TUIKitConstants.ProfileType.CONTENT, contactItemBean);
                    MyApplication.getInstance().startActivity(intent);

                }
            });

        } else {
            final int circlePosition = position - HEADVIEW_SIZE;
            final FriendsData.DataDTO circleItem = (FriendsData.DataDTO) datas.get(circlePosition);
            final CircleViewHolder holder = (CircleViewHolder) viewHolder;
            final String circleId = circleItem.getId();
            String im_id = circleItem.getMemberId();
            String name = circleItem.getMemberInfoResultVO().getNickName();
            String headImg = circleItem.getMemberInfoResultVO().getAvator();
            final String content = circleItem.getArticle();
//            String createTime = TimeUtil.getTimeShowString(TimeUtil.dateToString(circleItem.getCreatedTime()), false);
//            Log.d(TAG, "onBindViewHolder: time==="+createTime+"---------"+circleItem.getCreatedTime());
            if (circleItem.getThumbList() != null && circleItem.getThumbList().size() > 0) {
                favortDatas = circleItem.getThumbList();
            }
            if (circleItem.getCommentAndReplyList() != null && circleItem.getCommentAndReplyList().size() > 0) {
                commentsDatas = circleItem.getCommentAndReplyList();
            }
            boolean hasFavort = circleItem.hasFavort();
            boolean hasComment = circleItem.hasComment();

            Glide.with(context)
                    .load(headImg)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.icon)
                    .transform(new GlideCircleTransform())
                    .into(holder.headIv);

            holder.nameTv.setText(name);
            holder.timeTv.setText(circleItem.getCreatedTime());
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (SPUtils.getValue(AppConstant.USER,AppConstant.USER_ID).equals(im_id)) {
                        //是自己
                        /*ChatInfo chatInfo = new ChatInfo();
                        chatInfo.setId(im_id);
                        chatInfo.setType(V2TIMConversation.V2TIM_C2C);*/
                        ContactItemBean contactItemBean = new ContactItemBean();
                        contactItemBean.setId(im_id);
                        contactItemBean.setAvatarurl(headImg);
                        contactItemBean.setNickname(name);
                        Intent intent = new Intent(MyApplication.getInstance(), FriendProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(TUIKitConstants.ProfileType.CONTENT, contactItemBean);
                        MyApplication.getInstance().startActivity(intent);
                    } else {
                        final ContactItemBean bean = new ContactItemBean();
                        bean.setFriend(true);
                        bean.setId(im_id);
                        bean.setAvatarurl(headImg);
                        bean.setNickname(name);
                        Intent intent = new Intent(MyApplication.getInstance(), FriendProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(TUIKitConstants.ProfileType.CONTENT, bean);
                        MyApplication.getInstance().startActivity(intent);
                       /* V2TIMManager.getFriendshipManager().getFriendsInfo(list, new V2TIMValueCallback<List<V2TIMFriendInfoResult>>() {
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
                                    Intent intent = new Intent(MyApplication.instance(), FriendProfileActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra(TUIKitConstants.ProfileType.CONTENT, bean);
                                    MyApplication.instance().startActivity(intent);
                                }
                            }
                        });*/
                    }
                }
            };
            holder.nameTv.setOnClickListener(listener);
            holder.headIv.setOnClickListener(listener);

            if (!TextUtils.isEmpty(content)) {
                holder.contentTv.setExpand(circleItem.isExpand());
                holder.contentTv.setExpandStatusListener(new ExpandTextView.ExpandStatusListener() {
                    @Override
                    public void statusChange(boolean isExpand) {
                        circleItem.setExpand(isExpand);
                    }
                });

                holder.contentTv.setText(UrlUtils.formatUrlString(content));
            }
            holder.contentTv.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);

            if (SPUtils.getValue(AppConstant.USER, AppConstant.USER_ID).equals(circleItem.getMemberInfoResultVO().getId())) {
                holder.deleteBtn.setVisibility(View.VISIBLE);
            } else {
                holder.deleteBtn.setVisibility(View.GONE);
            }
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new TUIKitDialog(context)
                            .builder()
                            .setCancelable(true)
                            .setCancelOutside(true)
                            .setTitle("删除该朋友圈？")
                            .setDialogWidth(0.75f)
                            .setSureButtonColor(context.getResources().getColor(R.color.text_negative))
                            .setPositiveButton("删除", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mDeleteFriendsClickListener != null) {
                                        mDeleteFriendsClickListener.mDeleteListener(circleItem, 0);
                                    }
                                }
                            })
                            .setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .show();
                }
            });
            if (hasFavort || hasComment) {
                if (hasFavort) {//处理点赞列表
                    holder.praiseListView.setOnItemClickListener(new PraiseListView.OnItemClickListener() {
                        @Override
                        public void onClick(int position) {
//                            String userName = favortDatas.get(position).getUser().getName();
                            String im_id = favortDatas.get(position).getFromMemberInfo().getId();//im_id
                            //跳转详细资料
                            ArrayList<String> list = new ArrayList<>();
                            list.add(im_id);
                            if (UserInfo.getInstance().getUserId().equals(im_id)) {
                                //是自己
                                ChatInfo chatInfo = new ChatInfo();
                                chatInfo.setId(im_id);
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
                    });
                    if (favortDatas != null && favortDatas.size() > 0) {
                        holder.praiseListView.setDatas(favortDatas);
                        holder.praiseListView.setVisibility(View.VISIBLE);
                    }
                } else {
                    holder.praiseListView.setVisibility(View.GONE);
                }

                if (hasComment) {//处理评论列表
                    holder.commentList.setOnItemClickListener(new CommentListView.OnItemClickListener() {
                        @Override
                        public void onItemClick(int commentPosition) {
                            FriendsData.DataDTO.CommentAndReplyListDTO commentItem = commentsDatas.get(commentPosition);
                            if (UserInfo.getInstance().getUserId().equals(commentItem.getFromMemberInfo().getId())) {//复制或者删除自己的评论

                                CommentDialog dialog = new CommentDialog(context, /*presenter,*/ commentItem, circlePosition);
                                dialog.show();
                            } else {//回复别人的评论
//                                if (presenter != null) {
                                CommentConfig config = new CommentConfig();
                                config.msgId = circleId;
                                config.commentId = commentItem.getId();
                                config.circlePosition = circlePosition;
                                config.commentPosition = commentPosition;
                                config.commentType = CommentConfig.Type.REPLY;
                                config.replyUser = commentItem.getFromMemberInfo();
                                mReplyCommentListener.replyComment(config);
//                                    presenter.showEditTextBody(config);
//                                }
                            }
                        }
                    });
                    holder.commentList.setOnItemLongClickListener(new CommentListView.OnItemLongClickListener() {
                        @Override
                        public void onItemLongClick(int commentPosition) {
                            //长按进行复制或者删除
                            FriendsData.DataDTO.CommentAndReplyListDTO commentItem = commentsDatas.get(commentPosition);
                            CommentDialog dialog = new CommentDialog(context,/* presenter,*/ commentItem, circlePosition);
                            dialog.setDeleteCommentListener(new CommentDialog.IDeleteCommentListener() {
                                @Override
                                public void mDeleteComment(FriendsData.DataDTO.CommentAndReplyListDTO data) {
                                    if (mDeleteCommentListener != null) {
                                        mDeleteCommentListener.deleteComment(data.getId());
                                    }
                                }
                            });
                            dialog.show();
                        }
                    });
                    if (commentsDatas != null && commentsDatas.size() > 0) {
                        holder.commentList.setDatas(commentsDatas);
                        holder.commentList.setVisibility(View.VISIBLE);
                    }
                } else {
                    holder.commentList.setVisibility(View.GONE);
                }
                holder.digCommentBody.setVisibility(View.VISIBLE);
            } else {
                holder.digCommentBody.setVisibility(View.GONE);
            }

            holder.digLine.setVisibility(hasFavort && hasComment ? View.VISIBLE : View.GONE);

            final SnsPopupWindow snsPopupWindow = holder.snsPopupWindow;
            //判断是否已点赞
            String curUserFavortId = circleItem.getCurUserFavortId(SPUtils.getValue(AppConstant.USER, AppConstant.USER_ID));
            if (!TextUtils.isEmpty(curUserFavortId)) {
                snsPopupWindow.getmActionItems().get(0).mTitle = "取消";
            } else {
                snsPopupWindow.getmActionItems().get(0).mTitle = "赞";
            }
            snsPopupWindow.update();
            snsPopupWindow.setmItemClickListener(new PopupItemClickListener(circlePosition, circleItem, curUserFavortId));
            holder.snsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //弹出popupwindow
                    snsPopupWindow.showPopupWindow(view);
                }
            });

            holder.urlTipTv.setVisibility(View.GONE);
            switch (holder.viewType) {
                case CircleViewHolder.TYPE_URL:// 处理链接动态的链接内容和和图片

                    break;
                case CircleViewHolder.TYPE_IMAGE:// 处理图片
                    if (holder instanceof ImageViewHolder) {
                        final List<PhotoInfo> photos = new ArrayList<>();
                        List<String> imgUrl = circleItem.getImgUrl();
                        for (int i = 0; i < imgUrl.size(); i++) {
                            PhotoInfo photoInfo = new PhotoInfo();
                            photoInfo.url = imgUrl.get(i);
                            photos.add(photoInfo);
                        }
                        if (photos != null && photos.size() > 0) {
                            ((ImageViewHolder) holder).multiImageView.setVisibility(View.VISIBLE);
                            ((ImageViewHolder) holder).multiImageView.setList(photos);
                            ((ImageViewHolder) holder).multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    //imagesize是作为loading时的图片size
                                    List<String> photoUrls = new ArrayList<String>();
//                                    List<LocalMedia> selectMedia = new ArrayList<>();
                                    for (PhotoInfo photoInfo : photos) {
//                                        LocalMedia localMedia = new LocalMedia();
//                                        localMedia.setPath(photoInfo.url);
//                                        selectMedia.add(localMedia);
                                        photoUrls.add(photoInfo.url);
                                    }

                                    // 预览图片可以保存图片到本地备用
//                                    PictureSelector.create((Activity) context)
//                                            .themeStyle(R.style.picture_default_style)
//                                            .isNotPreviewDownload(true)
//                                            .loadImageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
//                                            .openExternalPreview(position, selectMedia);
                                    //新的图片浏览器
                                    TransferConfig transfer = TransferConfig.build()
                                            .setSourceUrlList(photoUrls)
//                                            .setSourceImageList(photoUrls)
                                            .setProgressIndicator(new ProgressBarIndicator())
                                            .setImageLoader(GlideImageLoader.with(context))
                                            .setIndexIndicator(new NumberIndexIndicator())
                                            .setOffscreenPageLimit(1)
                                            .setNowThumbnailIndex(position)
                                            .create();
                                    ((FriendsActivity) context).transferee.apply(transfer).show();
                                }
                            });
                        } else {
                            ((ImageViewHolder) holder).multiImageView.setVisibility(View.GONE);
                        }
                    }

                    break;
                case CircleViewHolder.TYPE_VIDEO:
                    if (holder instanceof VideoPlayerViewHolder) {
                        ((VideoPlayerViewHolder) holder).videoView.setUpLazy(circleItem.getVideoUrl(), true, null, null, "");
//                        ((VideoPlayerViewHolder) holder).videoView.setUp(circleItem.getVideoUrl(), true, "");
                        //增加title
                        ((VideoPlayerViewHolder) holder).videoView.getTitleTextView().setVisibility(View.GONE);
                        //设置返回键
                        ((VideoPlayerViewHolder) holder).videoView.getBackButton().setVisibility(View.GONE);
                        //增加封面
                        ImageView imageView = new ImageView(context);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        Glide.with(context)
                                .setDefaultRequestOptions(
                                        new RequestOptions()
                                                .frame(500000)
                                                .centerCrop()
                                                .error(R.drawable.nim_default_img_failed)
                                                .placeholder(R.drawable.nim_default_img_failed))
                                .load(circleItem.getVideoUrl())
                                .into(imageView);
                        Glide.with(context)
                                .setDefaultRequestOptions(
                                        new RequestOptions()
                                                .frame(500000)
                                                .centerCrop()
                                                .error(R.drawable.nim_default_img_failed)
                                                .placeholder(R.drawable.nim_default_img_failed))
                                .load(circleItem.getVideoUrl())
                                .into(((VideoPlayerViewHolder) holder).ThumbImage);
                        ((VideoPlayerViewHolder) holder).videoStart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ((VideoPlayerViewHolder) holder).ThumbImage.setVisibility(View.GONE);
                                ((VideoPlayerViewHolder) holder).videoStart.setVisibility(View.GONE);
                                ((VideoPlayerViewHolder) holder).videoView.startPlayLogic();
                            }
                        });
//                        imageView.setImageResource(R.drawable.nim_default_img_failed);
                        ((VideoPlayerViewHolder) holder).videoView.setThumbImageView(imageView);

                        //设置旋转
//                        OrientationUtils orientationUtils = new OrientationUtils((Activity) context, ((VideoPlayerViewHolder) holder).videoView);
                        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
                        ((VideoPlayerViewHolder) holder).videoView.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((VideoPlayerViewHolder) holder).videoView.startWindowFullscreen(context, false, true);

//                                orientationUtils.resolveByClick();
                            }
                        });
                        //防止错位设置
                        ((VideoPlayerViewHolder) holder).videoView.setPlayTag("PlayTag");
                        ((VideoPlayerViewHolder) holder).videoView.setPlayPosition(position);
                        //是否可以滑动调整
                        ((VideoPlayerViewHolder) holder).videoView.setIsTouchWiget(true);
                        //设置返回按键功能
                        ((VideoPlayerViewHolder) holder).videoView.getBackButton().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                onBackPressed();
                            }
                        });
                        ((VideoPlayerViewHolder) holder).videoView.setAutoFullWithSize(true);
                        //音频焦点冲突时是否释放
                        ((VideoPlayerViewHolder) holder).videoView.setReleaseWhenLossAudio(false);
                        //全屏动画
                        ((VideoPlayerViewHolder) holder).videoView.setShowFullAnimation(true);
                        //小屏时不触摸滑动
                        ((VideoPlayerViewHolder) holder).videoView.setIsTouchWiget(false);

                    }

                    break;

                case CircleViewHolder.TYPE_MUSIC:

                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return datas.size() + 1;//有head需要加1
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        ImageView mcircleBg;
        ImageView circleHead;
        TextView circleName;
        TextView remindText;
        ImageView remindImg;
        LinearLayout newRemind;
        String currentFaceUrl = "";

        public HeaderViewHolder(View itemView) {
            super(itemView);
            mcircleBg = itemView.findViewById(R.id.circle_bg);
            circleHead = itemView.findViewById(R.id.circle_head);
            circleName = itemView.findViewById(R.id.circle_name);
            remindText = itemView.findViewById(R.id.remind_text);
            remindImg = itemView.findViewById(R.id.remind_img);
            newRemind = itemView.findViewById(R.id.new_remind);
            EventBus.getDefault().register(this);
        }

        @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
        public void onBaseBean(NewMsgCount event) {
            if (event != null) {
                int count = event.getCount();
                String faceUrl = event.getFaceUrl();
                if (!TextUtils.isEmpty(faceUrl) || count > 0) {
                    newRemind.setVisibility(View.VISIBLE);
                    remindText.setText(count + "条新消息");
                    if (!TextUtils.equals(currentFaceUrl, faceUrl)) {
                        //避免重复刷新图片
                        GlideEngine.createGlideEngine().loadCornerImage(remindImg, faceUrl, null, 5);
                        currentFaceUrl = faceUrl;
                    }
                } else {
                    newRemind.setVisibility(View.GONE);
                }
            }
        }
    }

    public IDeleteCommentListener mDeleteCommentListener;

    public void setDeleteCommentListener(IDeleteCommentListener deleteCommentListener) {
        mDeleteCommentListener = deleteCommentListener;
    }

    public interface IDeleteCommentListener {
        void deleteComment(String commentId);
    }

    public IDeleteFriendsClickListener mDeleteFriendsClickListener;

    public void setDeleteFriendsClickListener(IDeleteFriendsClickListener deleteFriendsClickListener) {
        mDeleteFriendsClickListener = deleteFriendsClickListener;
    }

    public interface IDeleteFriendsClickListener {
        void mDeleteListener(FriendsData.DataDTO data, int type);
    }

    public IReplyCommentListener mReplyCommentListener;

    public void setReplyCommentListener(IReplyCommentListener replyCommentListener) {
        mReplyCommentListener = replyCommentListener;
    }

    public interface IReplyCommentListener {
        void replyComment(CommentConfig data);
    }

    private class PopupItemClickListener implements SnsPopupWindow.OnItemClickListener {
        private String mFavorId;
        //动态在列表中的位置
        private int mCirclePosition;
        private long mLasttime = 0;
        private FriendsData.DataDTO mCircleItem;

        public PopupItemClickListener(int circlePosition, FriendsData.DataDTO circleItem, String favorId) {
            this.mFavorId = favorId;
            this.mCirclePosition = circlePosition;
            this.mCircleItem = circleItem;
        }

        @Override
        public void onItemClick(ActionItem actionitem, int position) {
            switch (position) {
                case 0://点赞、取消点赞
                    if (System.currentTimeMillis() - mLasttime < 700)//防止快速点击操作
                        return;
                    mLasttime = System.currentTimeMillis();
                    if (mDeleteFriendsClickListener != null) {
                        if ("赞".equals(actionitem.mTitle.toString())) {
                            mDeleteFriendsClickListener.mDeleteListener(mCircleItem, 1);
                        } else {//取消点赞
                            mDeleteCommentListener.deleteComment(mFavorId);
                        }
                    }

                    /*if (presenter != null) {
                        if ("赞".equals(actionitem.mTitle.toString())) {
                            presenter.addFavort(mCirclePosition,mCircleItem.getId());
                        } else {//取消点赞
                            presenter.deleteFavort(mCirclePosition, mFavorId,mCircleItem.getId());
                        }
                    }*/
                    break;
                case 1://发布评论

                    if (mDeleteFriendsClickListener != null) {
                        mDeleteFriendsClickListener.mDeleteListener(mCircleItem, 3);
                        /*CommentConfig config = new CommentConfig();
                        config.circlePosition = mCirclePosition;
                        config.commentType = CommentConfig.Type.PUBLIC;*/

//                        presenter.showEditTextBody(config);
                    }

                    /*if (presenter != null) {
                        CommentConfig config = new CommentConfig();
                        config.circlePosition = mCirclePosition;
                        config.commentType = CommentConfig.Type.PUBLIC;
                        presenter.showEditTextBody(config);
                    }*/
                    break;
                default:
                    break;
            }
        }
    }
}
