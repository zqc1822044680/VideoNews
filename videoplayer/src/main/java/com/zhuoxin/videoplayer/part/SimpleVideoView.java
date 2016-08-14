package com.zhuoxin.videoplayer.part;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zhuoxin.videoplayer.R;
import com.zhuoxin.videoplayer.full.VideoViewActivity;

import java.io.IOException;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;

/**
 * Created by Able on 2016/8/11.
 */
public class SimpleVideoView extends FrameLayout implements View.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnInfoListener, MediaPlayer.OnVideoSizeChangedListener {

    private String videoPath;

    private ImageView ivPreview;

    private ImageButton btnToggle;

    private ProgressBar progressBar;

    private ImageButton btnFullScreen;

    private SurfaceView surfaceView;

    private SurfaceHolder mSurfaceHolder;

    private MediaPlayer mMediaPlayer;

    private boolean isPrepared; // 当前视频是否已准备好

    private boolean isPlaying; // 当前视频是否正在播放

    private long curr_progress; // 当前进度

    private long curr_duration; // 总时长

    private int percentage; // 百分比

    private static final int PROGRESS_MAX = 1000;

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            if (isPlaying){

                curr_progress = mMediaPlayer.getCurrentPosition();

                curr_duration = mMediaPlayer.getDuration();

                percentage = (int)(curr_progress / curr_duration * 100);

                progressBar.setProgress(percentage);

                handler.sendEmptyMessageDelayed(0, 200);

            }

        }

    };

    public SimpleVideoView(Context context) {

        super(context, null);

        init();

    }

    public SimpleVideoView(Context context, AttributeSet attrs) {

        super(context, attrs, 0);

        init();

    }

    public SimpleVideoView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        init();

    }

    public ImageView getIvPreview() {

        return ivPreview;

    }

    private void init() {

        Vitamio.isInitialized(getContext());

        LayoutInflater.from(getContext()).inflate(R.layout.view_simple_video_player, this, true);

        initSurfaceView();  // 控制视图的初始化

        initControllerViews(); // 初始化自定义控件视图

    }

    public void onResume(){

        initMediaPlayer(); // 初始化mediaPlayer及监听

        prepareMediaPlayer(); // 设置资源进行准备

    }

    /**
     * 暂停mediaPlayer，释放MediaPlayer（和activity的onPause同事执行）
     */
    public void onPause(){

        pauseMediaPlayer();

        releaseMediaPlayer();

    }

    /**
     * 释放 mediaPlayer,同时更新UI状态
     */
    private void releaseMediaPlayer() {

        mMediaPlayer.release();

        mMediaPlayer = null;

        isPlaying = false;

        isPrepared = false;

    }

    /**
     * 暂停 MediaPlayer，同时更新UI状态
     */
    private void pauseMediaPlayer() {

        if (isPlaying) {

            mMediaPlayer.pause();

        }

        isPlaying = false;

        btnToggle.setImageResource(R.drawable.ic_pause);

    }

    /**
     * 开始 MediaPlayer，同时更新UI状态
     */
    private void startMediaPlayer() {

        if (isPrepared) {

            mMediaPlayer.start();

        }

        isPlaying = true;

        btnToggle.setImageResource(R.drawable.ic_play_arrow); // 播放/暂停 按钮图像更新

//        updateProgress();

    }

//    private void updateProgress() {
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                while (mMediaPlayer.getCurrentPosition() < mMediaPlayer.getDuration() && isPlaying){
//
//                    try {
//
//                        Thread.sleep(100);
//
//                        curr_progress = mMediaPlayer.getCurrentPosition();
//
//                        curr_duration = mMediaPlayer.getDuration();
//
//                        percentage = (int)(curr_progress / curr_duration * 100);
//
//                        progressBar.setProgress(percentage);
//
//                        progressBar.postInvalidate();
//
//                    } catch (InterruptedException e) {
//
//                        e.printStackTrace();
//
//                    }
//
//                }
//
//            }
//
//        }).start();
//
//    }

    /**
     * 初始化SurfaceView
     */
    private void initSurfaceView() {

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

        mSurfaceHolder = surfaceView.getHolder();

        mSurfaceHolder.setFormat(PixelFormat.RGBA_8888);

    }

    /**
     * 初始化自定义控件视图
     */
    private void initControllerViews(){

        ivPreview = (ImageView) findViewById(R.id.ivPreview); // 预览图片

        btnToggle = (ImageButton) findViewById(R.id.btnToggle); // 播放/暂停 按钮

        btnToggle.setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar); // 进度条

        progressBar.setMax(PROGRESS_MAX);

        btnFullScreen = (ImageButton) findViewById(R.id.btnFullScreen); // 全屏播放按钮

        btnFullScreen.setOnClickListener(this);



    }

    /**
     * 设置播放文件路径（一般在onResume方法前调用）
     * @param videoPath
     */
    public void setVideoPath(String videoPath){

        this.videoPath = videoPath;

    }

    /**
     * 初始化MediaPlayer，设置监听
     */
    private void initMediaPlayer() {

        mMediaPlayer = new MediaPlayer(getContext());

        mMediaPlayer.setDisplay(mSurfaceHolder);

        mMediaPlayer.setOnPreparedListener(this);

        mMediaPlayer.setOnInfoListener(this);

        mMediaPlayer.setOnVideoSizeChangedListener(this); // 设置视频宽高发生变化时的监听

    }

    /**
     * 准备MediaPlayer，同时更新UI状态
     */
    private void prepareMediaPlayer() {

        try {

            mMediaPlayer.reset();

            mMediaPlayer.setDataSource(videoPath);

            mMediaPlayer.setLooping(true);

            mMediaPlayer.prepareAsync();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.btnToggle){

            if (mMediaPlayer.isPlaying()){

                pauseMediaPlayer();

            } else if (isPrepared) {

                startMediaPlayer();

            } else {

                Toast.makeText(getContext(), "Can't play now!", Toast.LENGTH_SHORT).show();

            }

        } else if (id == R.id.btnFullScreen){

            VideoViewActivity.open(getContext(), videoPath);

        }


    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        isPrepared = true;

        startMediaPlayer(); // 准备好后自动播放

    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {

        if (what == MediaPlayer.MEDIA_INFO_FILE_OPEN_OK){

            long bufferSize = mMediaPlayer.audioTrackInit();

            mMediaPlayer.audioInitedOk(bufferSize);

            return true;

        }

        return false;

    }

    /**
     * 根据视频的原宽高比设置视频高度
     * @param mp     the MediaPlayer associated with this callback
     * @param width  the width of the video
     * @param height the height of the video
     */
    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

        int videoWidth = surfaceView.getWidth();

        int videoHeight = videoWidth * height / width;

        ViewGroup.LayoutParams params = surfaceView.getLayoutParams();

        params.width = videoWidth;

        params.height = videoHeight;

        surfaceView.setLayoutParams(params);

    }

}
