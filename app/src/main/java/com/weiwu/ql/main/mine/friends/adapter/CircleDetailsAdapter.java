package com.weiwu.ql.main.mine.friends.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.tencent.common.dialog.DialogMaker;
import com.tencent.common.http.BaseBean;
import com.tencent.common.http.HttpCallBack;
import com.tencent.common.http.YHttp;
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
import com.tencent.qcloud.tim.uikit.utils.TimeUtil;
import com.vansz.glideimageloader.GlideImageLoader;
import com.weiwu.ql.AppConstant;
import com.weiwu.ql.R;
import com.weiwu.ql.data.bean.FriendsData;
import com.weiwu.ql.main.contact.detail.FriendProfileActivity;
import com.weiwu.ql.main.mine.friends.FriendsDetailsActivity;
import com.weiwu.ql.main.mine.friends.data.ActionItem;
import com.weiwu.ql.main.mine.friends.data.CommentConfig;
import com.weiwu.ql.main.mine.friends.data.PhotoInfo;
import com.weiwu.ql.main.mine.friends.msg.MsgListActivity;
import com.weiwu.ql.main.mine.friends.utils.GlideCircleTransform;
import com.weiwu.ql.main.mine.friends.utils.UrlUtils;
import com.weiwu.ql.main.mine.friends.view.CommentDialog;
import com.weiwu.ql.main.mine.friends.view.CommentListView;
import com.weiwu.ql.main.mine.friends.view.ExpandTextView;
import com.weiwu.ql.main.mine.friends.view.MultiImageView;
import com.weiwu.ql.main.mine.friends.view.PraiseListView;
import com.weiwu.ql.main.mine.friends.view.SnsPopupWindow;
import com.weiwu.ql.utils.IntentUtil;
import com.weiwu.ql.utils.SPUtils;
import com.weiwu.ql.view.GlideEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by yiwei on 16/5/17.
 */
public class CircleDetailsAdapter extends BaseRecycleViewAdapter {

    public final static int TYPE_HEAD = 0;

    private static final int STATE_IDLE = 0;
    private static final int STATE_ACTIVED = 1;
    private static final int STATE_DEACTIVED = 2;
    private int videoState = STATE_IDLE;
    public static final int HEADVIEW_SIZE = 0;
    public static final String TAG = "";

    int curPlayIndex = -1;

    //    private CirclePresenter presenter;
    private Context context;
    private ScheduledExecutorService scheduExec = null;

    /*public void setCirclePresenter(CirclePresenter presenter) {
        this.presenter = presenter;
    }*/

    public CircleDetailsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
//        if (position == 0) {
//            return TYPE_HEAD;
//        }

        int itemType = 0;
        FriendsData.DataDTO.MessageDTO item = (FriendsData.DataDTO.MessageDTO) datas.get(position);
        if (item.getPics().size() == 0 && item.getMediaUrl() == null) {
            itemType = CircleViewHolder.TYPE_TEXT;
        } else if (item.getPics() != null && item.getPics().size() > 0) {
            itemType = CircleViewHolder.TYPE_IMAGE;
        } else if (item.getMediaUrl() != null) {
            itemType = CircleViewHolder.TYPE_VIDEO;
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

    //???????????????item
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
                    //???????????????????????????????????????
                    CustomAlertDialog alertDialog = new CustomAlertDialog(context);
                    alertDialog.setTitle("");
                    String title = "????????????";
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
                                        DialogMaker.showProgressDialog(context, null, "????????????", true, new DialogInterface.OnCancelListener() {
                                            @Override
                                            public void onCancel(DialogInterface dialog) {

                                            }
                                        }).setCanceledOnTouchOutside(false);
                                        // ????????????
                                        /*Map map = new HashMap();
                                        map.put("image", new File(result.get(0).getCutPath()));
                                        YHttp.obtain().post(Constant.URL_UPLOAD, map, new HttpCallBack<BaseBean>() {
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
                                        // ??????
                                    }
                                });

                    });
//                    title = "???????????????";
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
                    //???????????????
                    IntentUtil.redirectToNextActivity(context, MsgListActivity.class);

                }
            });
            mHeaderHolder.circleHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //????????????????????????
                    ChatInfo chatInfo = new ChatInfo();
                    chatInfo.setId(UserInfo.getInstance().getUserId());
                    chatInfo.setType(V2TIMConversation.V2TIM_C2C);
                    Intent intent = new Intent(mHeaderHolder.itemView.getContext(), FriendProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(TUIKitConstants.ProfileType.CONTENT, chatInfo);
                    mHeaderHolder.itemView.getContext().startActivity(intent);

                }
            });

        } else {
            final int circlePosition = position;
            final FriendsData.DataDTO.MessageDTO circleItem = (FriendsData.DataDTO.MessageDTO) datas.get(position);
            final CircleViewHolder holder = (CircleViewHolder) viewHolder;
            final int circleId = circleItem.getId();
            String im_id = circleItem.getIm_id();
            String name = circleItem.getNick_name();
            String headImg = circleItem.getFace_url();
            final String content = circleItem.getText_content();
            String createTime = TimeUtil.getTimeShowString(TimeUtil.dateToString(circleItem.getCreate_time()), false);
            final List<FriendsData.DataDTO.MessageDTO.LikesDTO> favortDatas = circleItem.getLikes();
            final List<FriendsData.DataDTO.MessageDTO.CommentAndRepliesDTO> commentsDatas = circleItem.getCommentAndReplies();
            boolean hasFavort = circleItem.hasFavort();
            boolean hasComment = circleItem.hasComment();

            Glide.with(context)
                    .load(headImg)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.color.bg_no_photo)
                    .transform(new GlideCircleTransform())
                    .into(holder.headIv);

            holder.nameTv.setText(name);
            holder.timeTv.setText(createTime);
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //??????????????????
                    ArrayList<String> list = new ArrayList<>();
                    list.add(im_id);
                    if (UserInfo.getInstance().getUserId().equals(im_id)) {
                        //?????????
                        ChatInfo chatInfo = new ChatInfo();
                        chatInfo.setId(im_id);
                        chatInfo.setType(V2TIMConversation.V2TIM_C2C);
                        Intent intent = new Intent(holder.itemView.getContext(), FriendProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(TUIKitConstants.ProfileType.CONTENT, chatInfo);
                        holder.itemView.getContext().startActivity(intent);
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
                                    Intent intent = new Intent(holder.itemView.getContext(), FriendProfileActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra(TUIKitConstants.ProfileType.CONTENT, bean);
                                    holder.itemView.getContext().startActivity(intent);
                                }
                            }
                        });
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

            if (UserInfo.getInstance().getUserId().equals(circleItem.getIm_id())) {
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
                            .setTitle("?????????????????????")
                            .setDialogWidth(0.75f)
                            .setSureButtonColor(context.getResources().getColor(R.color.text_negative))
                            .setPositiveButton("??????", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    /*//??????
                                    if (presenter != null) {
                                        presenter.deleteCircle(circleId);
                                    }*/
                                }
                            })
                            .setNegativeButton("??????", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .show();
                }
            });
            if (hasFavort || hasComment) {
                if (hasFavort) {//??????????????????
                    holder.praiseListView.setOnItemClickListener(new PraiseListView.OnItemClickListener() {
                        @Override
                        public void onClick(int position) {
//                            String userName = favortDatas.get(position).getUser().getName();
                            String im_id = favortDatas.get(position).getIm_id();//im_id
                            //??????????????????
                            ArrayList<String> list = new ArrayList<>();
                            list.add(im_id);
                            if (UserInfo.getInstance().getUserId().equals(im_id)) {
                                //?????????
                                ChatInfo chatInfo = new ChatInfo();
                                chatInfo.setId(im_id);
                                chatInfo.setType(V2TIMConversation.V2TIM_C2C);
                                Intent intent = new Intent(holder.itemView.getContext(), FriendProfileActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(TUIKitConstants.ProfileType.CONTENT, chatInfo);
                                holder.itemView.getContext().startActivity(intent);
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
                                            Intent intent = new Intent(holder.itemView.getContext(), FriendProfileActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.putExtra(TUIKitConstants.ProfileType.CONTENT, bean);
                                            holder.itemView.getContext().startActivity(intent);

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

                if (hasComment) {//??????????????????
                    holder.commentList.setOnItemClickListener(new CommentListView.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position, FriendsData.DataDTO.MessageDTO.CommentAndRepliesDTO commentPosition) {
//                            FriendsData.DataDTO.MessageDTO.CommentAndRepliesDTO commentItem = commentsDatas.get(commentPosition);
                            if (UserInfo.getInstance().getUserId().equals(commentPosition.getIm_id())) {//?????????????????????????????????

                                CommentDialog dialog = new CommentDialog(context, commentPosition, circlePosition);
                                dialog.show();
                            } else {//?????????????????????
                                CommentConfig config = new CommentConfig();
//                                config.msgId = circleId;
                                config.commentId = commentPosition.getId()+"";
                                config.circlePosition = circlePosition;
                                config.commentPosition = position;
                                config.commentType = CommentConfig.Type.REPLY;
//                                config.commentatorId = commentPosition.getCommentatorId();
//                                    presenter.showEditTextBody(config);
                            }
                        }
                    });
                    holder.commentList.setOnItemLongClickListener(new CommentListView.OnItemLongClickListener() {
                        @Override
                        public void onItemLongClick(int commentPosition) {
                            //??????????????????????????????
                            FriendsData.DataDTO.MessageDTO.CommentAndRepliesDTO commentItem = commentsDatas.get(commentPosition);
                            CommentDialog dialog = new CommentDialog(context, commentItem, circlePosition);
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
            //?????????????????????
            int curUserFavortId = circleItem.getCurUserFavortId(SPUtils.getValue(AppConstant.USER, AppConstant.USER_ID));

//            String curUserFavortId = circleItem.getCurUserFavortId(DatasUtil.curUser.getIm_id());
            if (curUserFavortId != 0) {
                snsPopupWindow.getmActionItems().get(0).mTitle = "??????";
            } else {
                snsPopupWindow.getmActionItems().get(0).mTitle = "???";
            }
            snsPopupWindow.update();
            snsPopupWindow.setmItemClickListener(new PopupItemClickListener(circlePosition, circleItem, curUserFavortId));
            holder.snsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //??????popupwindow
                    snsPopupWindow.showPopupWindow(view);
                }
            });

            holder.urlTipTv.setVisibility(View.GONE);
            switch (holder.viewType) {
                case CircleViewHolder.TYPE_URL:// ?????????????????????????????????????????????

                    break;
                case CircleViewHolder.TYPE_IMAGE:// ????????????
                    if (holder instanceof ImageViewHolder) {
                        final List<PhotoInfo> photos = new ArrayList<>();
                        List<String> imgUrl = circleItem.getPics();
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
                                    //imagesize?????????loading????????????size
                                    List<String> photoUrls = new ArrayList<String>();
//                                    List<LocalMedia> selectMedia = new ArrayList<>();
                                    for (PhotoInfo photoInfo : photos) {
//                                        LocalMedia localMedia = new LocalMedia();
//                                        localMedia.setPath(photoInfo.url);
//                                        selectMedia.add(localMedia);
                                        photoUrls.add(photoInfo.url);
                                    }

                                    // ?????????????????????????????????????????????
//                                    PictureSelector.create((Activity) context)
//                                            .themeStyle(R.style.picture_default_style)
//                                            .isNotPreviewDownload(true)
//                                            .loadImageEngine(GlideEngine.createGlideEngine()) // ?????????Demo GlideEngine.java
//                                            .openExternalPreview(position, selectMedia);
                                    //?????????????????????
                                    ((FriendsDetailsActivity) context).config = TransferConfig.build()
                                            .setSourceUrlList(photoUrls)
                                            .setProgressIndicator(new ProgressBarIndicator())
                                            .setImageLoader(GlideImageLoader.with(context))
                                            .setIndexIndicator(new NumberIndexIndicator())
                                            .setOffscreenPageLimit(1)
                                            .create();
                                    ((FriendsDetailsActivity) context).config.setNowThumbnailIndex(position);
                                    ((FriendsDetailsActivity) context).transferee.apply(((FriendsDetailsActivity) context).config).show();
                                }
                            });
                        } else {
                            ((ImageViewHolder) holder).multiImageView.setVisibility(View.GONE);
                        }
                    }

                    break;
                case CircleViewHolder.TYPE_VIDEO:
                    if (holder instanceof VideoPlayerViewHolder) {
                        ((VideoPlayerViewHolder) holder).videoView.setUpLazy(circleItem.getMediaUrl(), true, null, null, "");
//                        ((VideoPlayerViewHolder) holder).videoView.setUp(circleItem.getVideoUrl(), true, "");
                        //??????title
                        ((VideoPlayerViewHolder) holder).videoView.getTitleTextView().setVisibility(View.GONE);
                        //???????????????
                        ((VideoPlayerViewHolder) holder).videoView.getBackButton().setVisibility(View.GONE);
                        //????????????
                        ImageView imageView = new ImageView(context);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        Glide.with(context)
                                .setDefaultRequestOptions(
                                        new RequestOptions()
                                                .frame(500000)
                                                .centerCrop()
                                                .error(R.drawable.nim_default_img_failed)
                                                .placeholder(R.drawable.nim_default_img_failed))
                                .load(circleItem.getMediaUrl())
                                .into(imageView);
                        Glide.with(context)
                                .setDefaultRequestOptions(
                                        new RequestOptions()
                                                .frame(500000)
                                                .centerCrop()
                                                .error(R.drawable.nim_default_img_failed)
                                                .placeholder(R.drawable.nim_default_img_failed))
                                .load(circleItem.getMediaUrl())
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

                        //????????????
//                        OrientationUtils orientationUtils = new OrientationUtils((Activity) context, ((VideoPlayerViewHolder) holder).videoView);
                        //????????????????????????,????????????????????????????????????????????????
                        ((VideoPlayerViewHolder) holder).videoView.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((VideoPlayerViewHolder) holder).videoView.startWindowFullscreen(context, false, true);

//                                orientationUtils.resolveByClick();
                            }
                        });
                        //??????????????????
                        ((VideoPlayerViewHolder) holder).videoView.setPlayTag("PlayTag");
                        ((VideoPlayerViewHolder) holder).videoView.setPlayPosition(position);
                        //????????????????????????
                        ((VideoPlayerViewHolder) holder).videoView.setIsTouchWiget(true);
                        //????????????????????????
                        ((VideoPlayerViewHolder) holder).videoView.getBackButton().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                onBackPressed();
                            }
                        });
                        ((VideoPlayerViewHolder) holder).videoView.setAutoFullWithSize(true);
                        //?????????????????????????????????
                        ((VideoPlayerViewHolder) holder).videoView.setReleaseWhenLossAudio(false);
                        //????????????
                        ((VideoPlayerViewHolder) holder).videoView.setShowFullAnimation(true);
                        //????????????????????????
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
        return datas.size();//???head?????????1
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    public void stopRind() {
        if (scheduExec != null && !scheduExec.isShutdown()) {
            scheduExec.shutdown();
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        ImageView mcircleBg;
        ImageView circleHead;
        TextView circleName;
        TextView remindText;
        ImageView remindImg;
        LinearLayout newRemind;
        String currentFaceUrl;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            mcircleBg = itemView.findViewById(R.id.circle_bg);
            circleHead = itemView.findViewById(R.id.circle_head);
            circleName = itemView.findViewById(R.id.circle_name);
            remindText = itemView.findViewById(R.id.remind_text);
            remindImg = itemView.findViewById(R.id.remind_img);
            newRemind = itemView.findViewById(R.id.new_remind);
            scheduExec = Executors.newScheduledThreadPool(1);
            scheduExec.scheduleWithFixedDelay(new Runnable() {
                public void run() {
                    YHttp.obtain().post(Constant.URL_REMIND, null, new HttpCallBack<BaseBean>() {
                        @Override
                        public void onSuccess(BaseBean bean) {
                            if (bean.getData() == null) {
                                return;
                            }
                            JSONObject object = JSON.parseObject(bean.getData().toString());
                            if (object != null) {
                                String faceUrl = object.getString("face_url");
                                Integer count = object.getInteger("count");
                                if (!TextUtils.isEmpty(faceUrl) || count > 0) {
                                    newRemind.setVisibility(View.VISIBLE);
                                    remindText.setText(count + "????????????");
                                    if (!TextUtils.equals(currentFaceUrl, faceUrl)) {
                                        //????????????????????????
                                        GlideEngine.createGlideEngine().loadCornerImage(remindImg, faceUrl, null, 5);
                                        currentFaceUrl = faceUrl;
                                    }
                                } else {
                                    newRemind.setVisibility(View.GONE);
                                }
                            } else {
                                newRemind.setVisibility(View.GONE);
                            }


                        }

                        @Override
                        public void onFailed(String error) {

                        }
                    });

                }
            }, 0, 5, TimeUnit.SECONDS);
        }
    }

    private class PopupItemClickListener implements SnsPopupWindow.OnItemClickListener {
        private int mFavorId;
        //???????????????????????????
        private int mCirclePosition;
        private long mLasttime = 0;
        private FriendsData.DataDTO.MessageDTO mCircleItem;

        public PopupItemClickListener(int circlePosition, FriendsData.DataDTO.MessageDTO circleItem, int favorId) {
            this.mFavorId = favorId;
            this.mCirclePosition = circlePosition;
            this.mCircleItem = circleItem;
        }

        @Override
        public void onItemClick(ActionItem actionitem, int position) {
            switch (position) {
                case 0://?????????????????????
                    if (System.currentTimeMillis() - mLasttime < 700)//????????????????????????
                        return;
                    mLasttime = System.currentTimeMillis();
                    /*if (presenter != null) {
                        if ("???".equals(actionitem.mTitle.toString())) {
                            presenter.addFavort(mCirclePosition, mCircleItem.getId());
                        } else {//????????????
                            presenter.deleteFavort(mCirclePosition, mFavorId, mCircleItem.getId());
                        }
                    }*/
                    break;
                case 1://????????????
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
