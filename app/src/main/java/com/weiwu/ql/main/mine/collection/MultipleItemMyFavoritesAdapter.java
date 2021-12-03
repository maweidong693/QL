package com.weiwu.ql.main.mine.collection;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hitomi.tilibrary.transfer.TransferConfig;
import com.hitomi.tilibrary.transfer.Transferee;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.tencent.common.Constant;
import com.tencent.common.http.HttpCallBack;
import com.tencent.common.http.YHttp;
import com.tencent.qcloud.tim.uikit.component.AudioPlayer;
import com.tencent.qcloud.tim.uikit.component.face.FaceManager;
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.GlideEngine;
import com.tencent.qcloud.tim.uikit.utils.PopWindowUtil;
import com.tencent.qcloud.tim.uikit.utils.TimeUtil;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.vansz.glideimageloader.GlideImageLoader;
import com.weiwu.ql.R;
import com.weiwu.ql.utils.IntentUtil;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Joseph_Yan
 * on 2020/11/5
 */
public class MultipleItemMyFavoritesAdapter extends BaseMultiItemQuickAdapter<FavoritesData.FavoritesItem, BaseViewHolder> implements LoadMoreModule {

    protected static final int WRITE_EXTERNAL_STORAGE = 101;
    protected Transferee transferee;
    private PopupWindow mConversationPopWindow;

    public MultipleItemMyFavoritesAdapter(List<FavoritesData.FavoritesItem> data) {
        super(data);
        // 绑定 layout 对应的 type
        addItemType(FavoritesData.FavoritesItem.TYPE_TEXT, R.layout.favorites_item_text_view);
        addItemType(FavoritesData.FavoritesItem.TYPE_IMG, R.layout.favorites_item_img_view);
        addItemType(FavoritesData.FavoritesItem.TYPE_VIDEO, R.layout.favorites_item_img_view);
        addItemType(FavoritesData.FavoritesItem.TYPE_SOUND, R.layout.favorites_item_sound_view);

    }

    private ICollectionDelListener mCollectionDelListener;

    public void setCollectionDelListener(ICollectionDelListener collectionDelListener) {
        mCollectionDelListener = collectionDelListener;
    }

    public interface ICollectionDelListener {
        void delCollection(FavoritesData.FavoritesItem data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, FavoritesData.FavoritesItem item) {
        // 根据返回的 type 分别设置数据
        String createTime = TimeUtil.getTimeShowString(TimeUtil.dateToString(item.getCreate_time()), false);
        helper.setText(R.id.tv_data, createTime);
        helper.setText(R.id.tv_name, item.getNick());
        helper.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //弹出一个
                View itemPop = LayoutInflater.from(getContext()).inflate(R.layout.pop_dialog_delete, null);
                itemPop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mCollectionDelListener!=null){
                            mCollectionDelListener.delCollection(item);
                            /*getData().remove(item);
                            notifyDataSetChanged();*/
                            mConversationPopWindow.dismiss();
                        }
                        /*Map map = new HashMap();
                        map.put("id",item.getId());
                        YHttp.obtain().post(Constant.URL_DEL_COLLECTION, map, new HttpCallBack<BaseBean>() {
                            @Override
                            public void onSuccess(BaseBean o) {
                                getData().remove(item);
                                notifyDataSetChanged();
                                mConversationPopWindow.dismiss();
                            }

                            @Override
                            public void onFailed(String error) {

                            }
                        });*/


                    }
                });
                mConversationPopWindow = PopWindowUtil.popupWindow(itemPop, getRecyclerView(), (int) view.getX(), (int) (view.getY() + view.getHeight() / 2));
                getRecyclerView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mConversationPopWindow.dismiss();
                    }
                }, 10000); // 10s后无操作自动消失


                return false;
            }
        });
        switch (helper.getItemViewType()) {
            case FavoritesData.FavoritesItem.TYPE_TEXT:
                FaceManager.handlerEmojiText(helper.getView(R.id.tv_content), item.getContent(), false);
//                helper.setText(R.id.tv_content,item.getContent());
                helper.itemView.setOnClickListener(v -> {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", item);
                    IntentUtil.redirectToNextActivity(getContext(), MyTextFavoritesActivity.class, bundle);

                });
                break;
            case FavoritesData.FavoritesItem.TYPE_IMG:
                GlideEngine.loadImage(helper.getView(R.id.content_image_iv), item.getFile_url());
                helper.setVisible(R.id.video_play_btn, false);
                helper.itemView.setOnClickListener(v -> {
                    List<String> photoUrls = new ArrayList<String>();
                    photoUrls.add(item.getFile_url());
                    transferee = Transferee.getDefault(getContext()).apply(TransferConfig.build().setSourceUrlList(photoUrls).
                            setImageLoader(GlideImageLoader.with(getContext())).setOnLongClickListener(new Transferee.OnTransfereeLongClickListener() {
                        @Override
                        public void onLongClick(ImageView imageView, String imageUri, int pos) {
//                            saveImageFile(imageUri);

                        }
                    }).create());
                    transferee.show();
                });
                break;
            case FavoritesData.FavoritesItem.TYPE_VIDEO:
                GlideEngine.loadImage(helper.getView(R.id.content_image_iv), item.getThumb_image());
                helper.setVisible(R.id.video_play_btn, true);
                helper.itemView.setOnClickListener(v -> {

                    Bundle bundle = new Bundle();
                    bundle.putString("url", item.getFile_url());
                    bundle.putString("thumb_image", item.getThumb_image());
                    IntentUtil.redirectToNextActivity(getContext(), SimplePlayer.class, bundle);

                });

                break;
            case FavoritesData.FavoritesItem.TYPE_SOUND:
                helper.itemView.setOnClickListener(v -> {
                    if (AudioPlayer.getInstance().isPlaying()) {
                        AudioPlayer.getInstance().stopPlay();
                        return;
                    }
                    ImageView audioPlayImage = helper.getView(R.id.audio_play_iv);
                    audioPlayImage.setImageResource(R.drawable.play_voice_message);
                    final AnimationDrawable animationDrawable = (AnimationDrawable) audioPlayImage.getDrawable();
                    animationDrawable.start();
                    AudioPlayer.getInstance().startPlay(item.getFile_url(), success -> audioPlayImage.post(new Runnable() {
                        @Override
                        public void run() {
                            animationDrawable.stop();
                            audioPlayImage.setImageResource(com.tencent.qcloud.tim.uikit.R.drawable.voice_msg_playing_3);
                        }
                    }));
                });
                break;
            default:
                break;
        }
    }

    /**
     * 保存图片到相册使用的方法
     */
    protected void saveImageFile(String imageUri) {
        String[] uriArray = imageUri.split("\\.");
        String imageName = String.format("%s.%s", String.valueOf(System.currentTimeMillis()), uriArray[uriArray.length - 1]);
        if (checkWriteStoragePermission()) {
            File rootFile = new File("/storage/emulated/0/qiaoliao/");
            boolean mkFlag = true;
            if (!rootFile.exists()) {
                mkFlag = rootFile.mkdirs();
            }
            if (mkFlag) {
                File file = new File(rootFile, imageName);
                File imageFile = transferee.getImageFile(imageUri);
                try {
                    PictureFileUtils.copyFile(imageFile.getAbsolutePath(), file.getAbsolutePath());
                    ToastUtil.toastShortMessage("保存成功");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean checkWriteStoragePermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE);
            return false;
        }
        return true;
    }
}


