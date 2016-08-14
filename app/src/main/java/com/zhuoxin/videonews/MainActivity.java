package com.zhuoxin.videonews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.zhuoxin.videonews.adapter.MainViewPagerAdapter;
import com.zhuoxin.videonews.fragment.LikesFragment;
import com.zhuoxin.videonews.fragment.LocalFragment;
import com.zhuoxin.videonews.fragment.NewsFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.btnLocal)
    Button btnLocal;

    @BindView(R.id.btnLikes)
    Button btnLikes;

    @BindView(R.id.btnNews)
    Button btnNews;

    private MainViewPagerAdapter adapter;

    private ArrayList<Fragment> fragmentAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

    }

    @Override
    public void onContentChanged() {

        super.onContentChanged();

        ButterKnife.bind(this);

        initFragment();

        adapter = new MainViewPagerAdapter(getSupportFragmentManager(), fragmentAll);

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(this);

        btnNews.setSelected(true);

    }

    private void initFragment() {

        fragmentAll = new ArrayList<>();

        fragmentAll.add(new NewsFragment());

        fragmentAll.add(new LocalFragment());

        fragmentAll.add(new LikesFragment());

    }

    @OnClick({R.id.btnNews, R.id.btnLikes, R.id.btnLocal})
    public void chooseFragment(View view){

        switch (view.getId()){

            case R.id.btnNews:

                viewPager.setCurrentItem(0, false);

                break;

            case R.id.btnLocal:

                viewPager.setCurrentItem(1, false);

                break;

            case R.id.btnLikes:

                viewPager.setCurrentItem(2, false);

                break;

        }

    }

    /**
     * 当页面滑动时触发
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 当页面选中时触发
     * @param position
     */
    @Override
    public void onPageSelected(int position) {

        btnNews.setSelected(position == 0);

        btnLocal.setSelected(position == 1);

        btnLikes.setSelected(position == 2);

    }

    /**
     * 滑动状态发生改变时触发
     * @param state
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
