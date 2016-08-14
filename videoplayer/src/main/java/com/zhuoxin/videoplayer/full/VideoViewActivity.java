package com.zhuoxin.videoplayer.full;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuoxin.videoplayer.R;

import java.util.Locale;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by Able on 2016/8/10.
 */
public class VideoViewActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener {

    private static final String KEY_VIDEO_PATH = "KEY_VIDEO_PATH";


    private VideoView videoView;

    private MediaPlayer mediaPlayer;


    private ImageView ivLoding; // 缓冲信息（图像）

    private TextView tvBufferInfo; // 缓冲信息（文本信息，显示00kb/s, 00%）

    private int downloadSpeed; // 当前缓冲速度

    private int bufferPercent; // 当前缓冲百分比

    /* 开启当前activity，传入视频播放路径 */
    public static void open(Context context, String videoPath){

        Intent intent = new Intent(context, VideoViewActivity.class);

        intent.putExtra(KEY_VIDEO_PATH, videoPath);

        context.startActivity(intent);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // 取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 设置窗口背景色
        getWindow().setBackgroundDrawableResource(R.color.black);

        setContentView(R.layout.activity_video_view);

    }

    @Override
    public void onContentChanged() {

        super.onContentChanged();

        Vitamio.isInitialized(this);

        // 1.初始化视图
        // 2.初始化VideoView
        initVideoView();

        initBufferViews();

    }

    @Override
    protected void onResume() {

        super.onResume();

        videoView.setVideoPath(getIntent().getStringExtra(KEY_VIDEO_PATH));

    }

    @Override
    protected void onPause() {

        super.onPause();

        videoView.stopPlayback();

    }

    private void initBufferViews(){

        tvBufferInfo = (TextView) findViewById(R.id.tvBufferInfo);

        ivLoding = (ImageView) findViewById(R.id.ivLoading);

        tvBufferInfo.setVisibility(View.INVISIBLE);

        ivLoding.setVisibility(View.INVISIBLE);

    }

    private void initVideoView() {

        videoView = (VideoView) findViewById(R.id.videoView);

        videoView.setMediaController(new CustomMediaController(this));

        videoView.setKeepScreenOn(true);

        videoView.requestFocus();

        videoView.setOnPreparedListener(this); // 资源准备监听

        videoView.setOnInfoListener(this); // 状态监听

        videoView.setOnBufferingUpdateListener(this);

    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        mediaPlayer = mp;

        mediaPlayer.setBufferSize(512*1024);

    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {

        switch (what){

            case MediaPlayer.MEDIA_INFO_BUFFERING_START: // 开始缓冲

                if (videoView.isPlaying()){

                    videoView.pause();

                }

                showBufferViews();

                break;

            case MediaPlayer.MEDIA_INFO_BUFFERING_END: // 结束缓冲

                videoView.start(); // 开始播放

                hideBufferViews();

                break;

            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED: // 缓冲速率发生变化

                downloadSpeed = extra;

                updateBufferViewInfo();

                break;

        }

        return false;

    }

    /**
     * 缓冲更新监听
     * @param mp      the MediaPlayer the update pertains to
     * @param percent the percentage (0-100) of the buffer that has been filled thus
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

        bufferPercent = percent;

        updateBufferViewInfo();

    }

    // 开始缓冲时调用
    private void showBufferViews() {

        tvBufferInfo.setVisibility(View.VISIBLE);

        ivLoding.setVisibility(View.VISIBLE);

        downloadSpeed = 0;

        bufferPercent = 0;

    }


    //结束缓冲时调用
    private void hideBufferViews() {

        tvBufferInfo.setVisibility(View.INVISIBLE);

        ivLoding.setVisibility(View.INVISIBLE);

    }

    // 缓冲速度发生变化时调用
    private void updateBufferViewInfo() {

        String info = String.format(Locale.CHINA, "%d%%, %dkb/s", bufferPercent, downloadSpeed);

        tvBufferInfo.setText(info);

    }

}
