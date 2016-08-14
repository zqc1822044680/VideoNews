package com.zhuoxin.videoplayer.full;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.zhuoxin.videoplayer.R;

import io.vov.vitamio.widget.MediaController;

/**
 * Created by Able on 2016/8/10.
 */
public class CustomMediaController extends MediaController implements View.OnClickListener, View.OnTouchListener {

    private MediaPlayerControl mControl;

    private final AudioManager mAudioManager;   // 用来调整音量

    private Window mWindow; // 用来调整亮度

    private final int maxVolume;    // 最大音量

    private int currentVolume;    // 当前音量

    private float currentBrightness;    //当前亮度

    private GestureDetector mDetector;

    public CustomMediaController(Context context) {

        super(context);

        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        mWindow = ((Activity)context).getWindow();



    }

    /**
     * 实现自定义MediaController
     * @return
     */
    @Override
    protected View makeControllerView() {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_custom_video_controller, this);

        initView(view);

        return view;

    }

    @Override
    public void setMediaPlayer(MediaPlayerControl player) {

        super.setMediaPlayer(player);

    }

    private void initView(View view) {

        // 设置快进
        ImageButton btnFastForward = (ImageButton) view.findViewById(R.id.btnFastForward);

        btnFastForward.setOnClickListener(this);

        // 设置快退
        ImageButton btnFastRewind = (ImageButton) view.findViewById(R.id.btnFastRewind);

        btnFastRewind.setOnClickListener(this);

        final View adjustView = view.findViewById(R.id.adjustView);

        mDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

                float startX = e1.getX();

                float startY = e1.getY();

                float endY = e2.getY();

                float width = adjustView.getWidth();

                float height = adjustView.getHeight();

                float percentage = (startY - endY) / height;    //垂直移动距离所占控件垂直距离的百分比

                if (startX < width / 5){

                    adjustBrightness(percentage);

                    return true;

                } else if (startX > width / 5 * 4){

                    adjustVolume(percentage);

                }

                return false;

            }
            
        });

        adjustView.setOnTouchListener(this);    // 对View进行touch监听

    }

    @Override
    public void onClick(View view) {

        long position = 0;

        int i = view.getId();

        if (i == R.id.btnFastForward) {

            position = mControl.getCurrentPosition();

            position += 10000;

            mControl.seekTo(position);


        } else if (i == R.id.btnFastRewind) {

            position = mControl.getCurrentPosition();

            position += 10000;

            mControl.seekTo(position);

        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        // 判断手势 MotionEvent.ACTION_MASK过滤多点多点触屏 为当前音量和亮度设置
        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN){

            currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

            currentBrightness = mWindow.getAttributes().screenBrightness;

        }

        mDetector.onTouchEvent(event);

        CustomMediaController.this.show(); // 为了在调整过程中，不消失

        return true;

    }


    /**
     * 设置音量
     * @param percentage
     */
    private void adjustVolume(float percentage) {

        int targetVolume = (int) ((percentage * maxVolume) + currentVolume);    // 计算音量

        targetVolume = targetVolume > maxVolume ? maxVolume : targetVolume; // 判断音量是否超过范围

        targetVolume = targetVolume < 0 ? 0 : targetVolume;

        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, targetVolume, AudioManager.FLAG_SHOW_UI);  // 设置

    }


    /**
     * 设置亮度
     * @param percentage
     */
    private void adjustBrightness(float percentage) {

        float targetBrightness = percentage + currentBrightness;

        targetBrightness = targetBrightness > 1.0f ? 1.0f : targetBrightness;

        targetBrightness = targetBrightness < 0 ? 0 : targetBrightness;

        WindowManager.LayoutParams params = mWindow.getAttributes();    // 设置

        params.screenBrightness = targetBrightness;

        mWindow.setAttributes(params);

    }

}
