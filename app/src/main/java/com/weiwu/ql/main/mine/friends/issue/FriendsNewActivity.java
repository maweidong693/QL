package com.weiwu.ql.main.mine.friends.issue;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.tencent.common.dialog.DialogMaker;
import com.tencent.qcloud.tim.uikit.base.ITitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.dialog.TUIKitDialog;
import com.tencent.qcloud.tim.uikit.utils.ScreenUtil;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.repositories.FindRepository;
import com.weiwu.ql.data.request.IssueMessageRequestBody;
import com.weiwu.ql.main.mine.friends.FriendsContract;
import com.weiwu.ql.view.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FriendsNewActivity extends BaseActivity implements FriendsContract.IIssueView {

    private RecyclerView recyclerView;
    private GridImageAdapter adapter;
    private int maxSelectNum = 9;// 图片最大可选数量
    private List<LocalMedia> selectMedia = new ArrayList<>();
    private List<String> pics = new ArrayList<>();
    private int compressFlag = 2;// 1 系统自带压缩 2 luban压缩
    private TitleBarLayout mTitleBar;
    private static Bundle bundle;

    private Button btnSend;
    private EditText etMessage;

    private DefaultItemTouchHelpCallback defaultItemTouchHelpCallback;
    private DefaultItemTouchHelpCallback.OnItemTouchCallbackListener onItemTouchCallbackListener;

    LinearLayout llFriendsBox;

    private LinearLayout llDeleteBox;
    private TextView tvDeleteleTip;
    private ImageView ivDeleteTip;
    private boolean touchUp;
    private boolean toDelete;
    private boolean cleaning;
    private int pos;
    private FriendsContract.IIssuePresenter mPresenter;
    private int mUploadType = 0;

    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, FriendsNewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (extras != null) {
            intent.putExtras(extras);
            bundle = intent.getExtras();
        }
        ((Activity) context).startActivityForResult(intent, 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_new_message);
        setPresenter(new FriendsNewPresenter(FindRepository.getInstance()));
        mTitleBar = findViewById(R.id.friend_title_bar);
        mTitleBar.getRightGroup().removeAllViews();
        View inflate = LayoutInflater.from(this).inflate(R.layout.title_button, null);
        btnSend = inflate.findViewById(R.id.btn_send);
        mTitleBar.getRightGroup().addView(inflate);
        mTitleBar.setOnLeftClickListener(view ->
                new TUIKitDialog(this)
                        .builder()
                        .setCancelable(true)
                        .setCancelOutside(true)
                        .setTitle("退出此次编辑？")
                        .setDialogWidth(0.75f)
                        .setSureButtonColor(getResources().getColor(R.color.text_negative))
                        .setPositiveButton("退出", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show());
        etMessage = findViewById(R.id.etNewMessage);
        recyclerView = findViewById(R.id.recycler);

        llFriendsBox = findViewById(R.id.ll_friends_box);

        llDeleteBox = findViewById(R.id.ll_del_tip);
        tvDeleteleTip = findViewById(R.id.tv_del_tip);
        ivDeleteTip = findViewById(R.id.iv_del_tip);

        if (bundle != null) {
            initRecycleView();
            initLongTouchtoMove();//长按滑动
            selectMedia = (List<LocalMedia>) bundle.get("resultList");
            adapter.setList(selectMedia);
            adapter.notifyDataSetChanged();
            ((TouchHelpCallbackListenerImpl) onItemTouchCallbackListener).setSelectList(selectMedia);
            bundle.clear();
            bundle = null;
            refreshLayout();
        } else {
            mTitleBar.setTitle("发表文字", ITitleBarLayout.POSITION.LEFT);
        }

        initUI();
    }

    private void initRecycleView() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(FriendsNewActivity.this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(FriendsNewActivity.this, onAddPicClickListener);
        adapter.setSelectMax(maxSelectNum);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (!selectMedia.isEmpty() && selectMedia.size() > 0) {
                    int mimeType = PictureMimeType.getMimeType(selectMedia.get(0).getMimeType());
                    if (mimeType == PictureConfig.TYPE_VIDEO) {
                        //预览视频
                        PictureSelector.create(FriendsNewActivity.this).externalPictureVideo(selectMedia.get(position).getRealPath());
                    } else {
                        // 预览图片
                        PictureSelector.create(FriendsNewActivity.this)
                                .themeStyle(R.style.picture_default_style)
                                .isNotPreviewDownload(false)
                                .isGif(false)
                                .isCompress(true)//是否压缩
                                .maxVideoSelectNum(1)
                                .loadImageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
                                .openExternalPreview(position, selectMedia);
                    }
                }

            }

            @Override
            public boolean onItemLongClick(int position, View v) {
//                Log.d("wzt", "position:" + position + " ," + selectList.size());
                defaultItemTouchHelpCallback.setDragEnable(true);
                defaultItemTouchHelpCallback.setSwipeEnable(true);
                return false;
            }
        });
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if ((selectMedia != null && selectMedia.size() > 0) || !TextUtils.isEmpty(etMessage.getText().toString().trim())) {
                    btnSend.setEnabled(true);
                } else {
                    btnSend.setEnabled(false);
                }
            }
        });
    }

    private void initLongTouchtoMove() {
        onItemTouchCallbackListener = new TouchHelpCallbackListenerImpl(this, selectMedia, adapter,
                new TouchHelpCallbackListenerImpl.ITouchHelpCallback() {
                    @Override
                    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
//                        Log.d("wzt", "onSelectedChanged:" + actionState);
                        if (toDelete) {
                            touchUp = actionState == ItemTouchHelper.ACTION_STATE_IDLE;
                        }
                        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                            showDeleteBox();
                        } else {
                            hideDeleteBox();
                        }
                    }

                    @Override
                    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                        cleaning = true;
                        toDelete = false;
                        hideDeleteBox();
//                        Log.d("wzt", "clearView");
                    }

                    @Override
                    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                            float dX, float dY, int actionState, boolean isCurrentlyActive) {

                        toChangeViewByOnChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    }

                    @Override
                    public void onSwiped(int position) {

                    }

                });
        defaultItemTouchHelpCallback = new DefaultItemTouchHelpCallback(onItemTouchCallbackListener);
        defaultItemTouchHelpCallback.setDragEnable(true);
        defaultItemTouchHelpCallback.setSwipeEnable(true);
        DefaultItemTouchHelper defaultItemTouchHelper = new DefaultItemTouchHelper(defaultItemTouchHelpCallback);
        defaultItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private static final String TAG = "FriendsNewActivity";

    private void initUI() {
        etMessage.addTextChangedListener(new MessageChange());
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectMedia.isEmpty() && selectMedia.size() > 0) {
                    Log.d(TAG, "onClick: a====" + selectMedia.size());

                    LocalMedia media = selectMedia.get(0);
                    String mimeType = media.getMimeType();
                    Log.d(TAG, "onClick: c===" + mimeType);
                    if (mimeType.indexOf("image", 0) != -1) {
                        uploadPics();
                        Log.d(TAG, "onClick: b===" + pics.size());

                    } else if (mimeType.indexOf("video", 0) != -1) {
                        uploadVideo();

                    }
                } else {
                    mPresenter.issueMessage(new IssueMessageRequestBody(etMessage.getText().toString()));

//                    addMessage("", "", "", null, etMessage.getText().toString());
                }
            }
        });
    }

    /**
     * 上传视频
     */
    private void uploadVideo() {
        DialogMaker.showProgressDialog(this, null, "上传中…", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                DialogMaker.dismissProgressDialog();
            }
        }).setCanceledOnTouchOutside(false);
        File file = new File(selectMedia.get(0).getRealPath());
        /*MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(videoPath);
        Bitmap bitmap = media.getFrameAtTime();*/
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        mUploadType = 2;
        mPresenter.uploadPic(body);
    }

    /**
     * 上传图片
     */
    private void uploadPics() {
        pics.clear();
        DialogMaker.showProgressDialog(this, null, "上传中…", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                DialogMaker.dismissProgressDialog();
            }
        }).setCanceledOnTouchOutside(false);
        for (int i = 0; i < selectMedia.size(); i++) {
            File file = new File(selectMedia.get(i).getCompressPath());
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            mUploadType = 1;
            mPresenter.uploadPic(body);

        }
    }

    public String listToString(List list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(separator);
        }
        return list.isEmpty() ? "" : sb.toString().substring(0, sb.toString().length() - 1);
    }

    /**
     * @param location     定位地址
     * @param mediaPreview 视频缩略图
     * @param mediaUrl     视频地址
     * @param pics         图片内容(数组格式)
     * @param textContent  文字内容
     */
    private void addMessage(String location, String mediaPreview, String mediaUrl, List<String> pics, String textContent) {
        mPresenter.issueMessage(new IssueMessageRequestBody(textContent, listToString(pics, ',')));


    }

    @Override
    public void uploadResult(LoginData data) {
        String mIconUrl = data.getData().getSrc();
        if (mUploadType == 1) {
            pics.add(mIconUrl);
            if (pics.size() == selectMedia.size()) {
                addMessage("", "", "", pics, etMessage.getText().toString());
            }
        } else {
            mPresenter.issueMessage(new IssueMessageRequestBody(etMessage.getText().toString(), data.getData().getSrc(), null));
        }
    }

    @Override
    public void issueResult(HttpResult data) {
//发表朋友圈成功
        setResult(RESULT_OK, null);
        finish();
    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
        if (code == 10001) {
            MyApplication.getInstance().loginAgain();
        }
    }

    @Override
    public void setPresenter(FriendsContract.IIssuePresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return this;
    }

    // EditText监听器
    class MessageChange implements TextWatcher {

        @Override
        public void afterTextChanged(Editable arg0) {
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence cs, int start, int before, int count) {
            if ((selectMedia != null && selectMedia.size() > 0) || !TextUtils.isEmpty(cs.toString().trim())) {
                btnSend.setEnabled(true);
            } else {
                btnSend.setEnabled(false);
            }
        }

    }

    /**
     * 增加按钮图片回调接口
     */
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick(int type, int position) {
            switch (type) {
                case 0:
                    PictureSelector.create(FriendsNewActivity.this)
                            .openGallery(PictureMimeType.ofAll())
                            .loadImageEngine(GlideEngine.createGlideEngine())
                            .isNotPreviewDownload(false)
                            .isGif(false)
                            .maxVideoSelectNum(1)
                            .isCompress(true)//是否压缩
                            .minimumCompressSize(1000)
                            .selectionData(selectMedia)
                            .forResult(new OnResultCallbackListener<LocalMedia>() {
                                @Override
                                public void onResult(List<LocalMedia> result) {

                                    // 结果回调
                                    selectMedia = result;
//            Log.i("wzt", "selectMedia回调个数:" + selectMedia.size());
                                    if (selectMedia != null) {
                                        adapter.setList(selectMedia);
                                        adapter.notifyDataSetChanged();
                                        ((TouchHelpCallbackListenerImpl) onItemTouchCallbackListener).setSelectList(selectMedia);
                                        refreshLayout();
                                    }
                                }

                                @Override
                                public void onCancel() {
                                    // 取消
                                }
                            });
                    // 只拍照
                    //PictureConfig.getPictureConfig().startOpenCamera(mContext, resultCallback);
                    break;
                case 1:
                    // 删除图片
                    selectMedia.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, selectMedia.size());
//                    adapter.notifyDataSetChanged();//teng 修正点击删除图标没有触发观察者但是没了删除动画效果
                    refreshLayout();
                    break;
            }
        }

        @Override
        public boolean onLongClick(View view) {
            defaultItemTouchHelpCallback.setDragEnable(false);
            defaultItemTouchHelpCallback.setSwipeEnable(false);
            return false;
        }
    };


    /**
     * 刷新地理位置等布局
     */
    private void refreshLayout() {
        touchUp = false;
        toDelete = false;
        //判断提醒谁布局看是否需要下移
        int row = (selectMedia.size() + 1) / 3;
        row = 0 == (selectMedia.size() + 1) % 3 ? row : row + 1;
        row = row > 3 ? 3 : row;//row最多为三行
        int marginTop = (getResources().getDimensionPixelSize(R.dimen.article_img_margin_top)
                + ScreenUtil.getScreenWidth(this) / 3) * row
                + getResources().getDimensionPixelSize(R.dimen.article_post_et_h)
                + 10;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) llFriendsBox.getLayoutParams();
        params.setMargins(0, marginTop, 0, 0);
        llFriendsBox.setLayoutParams(params);
    }

    private void showDeleteBox() {
        llDeleteBox.setVisibility(View.VISIBLE);
        llDeleteBox.setBackgroundColor(0xDDFF4444);
        tvDeleteleTip.setText("拖到此处删除");
        ivDeleteTip.setImageResource(R.drawable.friends_del_nor);
    }

    private void hideDeleteBox() {
        llDeleteBox.setVisibility(View.GONE);
    }

    private void sureToDeleteBox() {
        llDeleteBox.setVisibility(View.VISIBLE);
        llDeleteBox.setBackgroundColor(0xDDD7110A);
        tvDeleteleTip.setText("松手即可删除");
        ivDeleteTip.setImageResource(R.drawable.friends_del_sure);
    }

    private void toChangeViewByOnChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                           float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if (actionState == ItemTouchHelper.ACTION_STATE_IDLE && !toDelete) {
            return;
        }
        if (dY >= (recyclerView.getHeight()
                - viewHolder.itemView.getBottom()//item底部距离recyclerView顶部高度
                - getPixelById(R.dimen.article_post_delete))) {//拖到删除处

            toDelete = true;
            sureToDeleteBox();
            if (touchUp) {//在删除处放手，则删除item
                viewHolder.itemView.setVisibility(View.INVISIBLE);//先设置不可见，如果不设置的话，
                // 会看到viewHolder返回到原位置时才消失，因为remove会在viewHolder动画执行完成后才将viewHolder删除
                selectMedia.remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                if ((selectMedia != null && selectMedia.size() > 0) || !TextUtils.isEmpty(etMessage.getText().toString().trim())) {
                    btnSend.setEnabled(true);
                } else {
                    btnSend.setEnabled(false);
                }
                hideDeleteBox();
                refreshLayout();
                cleaning = true;
            }
        } else {//没有到删除处
            if (toDelete) {
                showDeleteBox();
            }
            toDelete = false;
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK)
            new TUIKitDialog(this)
                    .builder()
                    .setCancelable(true)
                    .setCancelOutside(true)
                    .setTitle("退出此次编辑？")
                    .setDialogWidth(0.75f)
                    .setSureButtonColor(getResources().getColor(R.color.text_negative))
                    .setPositiveButton("退出", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    })
                    .setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .show();

        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK)
            return super.onKeyDown(keyCode, event);
        // 不拦截，如果这里拦截了，也不会走到onBackPressed方法了
        return false;
    }

    private int getPixelById(int dimensionId) {
        return this.getResources().getDimensionPixelSize(dimensionId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bundle != null) {
            bundle.clear();
            bundle = null;
        }
        if (adapter != null) {
            adapter = null;
        }
        if (recyclerView != null) {
            recyclerView = null;
        }
        if (selectMedia != null) {
            selectMedia = null;
        }
    }
}
