package com.zhuoxin.videonews.view;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuoxin.videonews.R;
import com.zhuoxin.videoplayer.full.VideoViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Able on 2016/8/14.
 */
public class LocalView extends FrameLayout {

    @BindView(R.id.ivPreview)
    ImageView ivPreview; // 视频预览图

    @BindView(R.id.tvVideoName)
    TextView tvVideoName; // 视频名称

    private String filePath; // 本地视频文件路径

    public LocalView(Context context) {

        super(context, null);

        init();

    }

    public LocalView(Context context, AttributeSet attrs) {

        super(context, attrs, 0);

        init();

    }

    public LocalView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        init();

    }

    private void init() {

        LayoutInflater.from(getContext()).inflate(R.layout.item_local_video, this, true);

        ButterKnife.bind(this);

    }

    public void bind(Cursor cursor){

        filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));

        String videoName =  cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));

        tvVideoName.setText(videoName);

        ivPreview.setImageBitmap(null);

    }

    @OnClick
    public void onClick(){

        VideoViewActivity.open(getContext(), filePath); // 全屏播放

    }

    @UiThread
    public void setPreview(@NonNull Bitmap bitmap) {

        ivPreview.setImageBitmap(bitmap);

    }

    public void setPreView(final String filePath, final Bitmap bitmap){

        if (filePath.equals(this.filePath)){

            return;

        }

        post(new Runnable(){

            @Override
            public void run() {

                // 二次确认
                if (filePath.equals(LocalView.this.filePath)){

                    return;

                }

                ivPreview.setImageBitmap(bitmap);

            }

        });

    }

    public String getFilePath() {

        return filePath;

    }

}
