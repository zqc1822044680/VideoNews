package com.zhuoxin.videonews.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.zhuoxin.videonews.view.LocalView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Able on 2016/8/14.
 */
public class LocalAdapter extends CursorAdapter {

    // 用来加载生成视频预览图的线程池
    private final ExecutorService service = Executors.newFixedThreadPool(5);

    // 用来缓存视频预览图
    private LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(5 * 1024 * 1024){

        @Override
        protected int sizeOf(String key, Bitmap value) {

            return value.getByteCount();

        }

    };

    public LocalAdapter(Context context) {

        super(context, null, true);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        return new LocalView(context);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final LocalView localView = (LocalView) view;

        localView.bind(cursor); // 将当前数据设置到视图上

        final String filePath = localView.getFilePath(); // 当前视频路径

        Bitmap bitmap = lruCache.get(filePath); // 从缓存中获取预览图

        if (bitmap != null){

            localView.setPreview(bitmap); // 设置当前视图预览图

        } else {

            service.submit(new Runnable() {

                @Override
                public void run() {

                    Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MINI_KIND); // 获取设置视频预览图(此方法很费时)

                    lruCache.put(filePath, bitmap);

                    localView.setPreview(bitmap);

                }

            });

        }

    }

    public void release(){

        service.shutdown();

    }

}
