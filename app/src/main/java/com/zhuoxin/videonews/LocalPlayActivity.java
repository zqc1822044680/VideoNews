package com.zhuoxin.videonews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zhuoxin.videoplayer.part.SimpleVideoView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Able on 2016/8/11.
 */
public class LocalPlayActivity extends AppCompatActivity {

    @BindView(R.id.simpleVideoView)
    SimpleVideoView simpleVideoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_local_play);

    }

    @Override

    public void onContentChanged() {

        super.onContentChanged();

        ButterKnife.bind(this);

        simpleVideoView.setVideoPath(getTestVideo1());

    }

    @Override

    protected void onResume() {

        super.onResume();

        simpleVideoView.onResume();

    }

    @Override
    protected void onPause() {

        super.onPause();

        simpleVideoView.onPause();

    }

    public String getTestVideo1() {
        return "http://o9ve1mre2.bkt.clouddn.com/raw_%E6%B8%A9%E7%BD%91%E7%94%B7%E5%8D%95%E5%86%B3%E8%B5%9B.mp4";
    }

}
