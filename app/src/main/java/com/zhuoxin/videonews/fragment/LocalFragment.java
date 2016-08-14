package com.zhuoxin.videonews.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.zhuoxin.videonews.R;
import com.zhuoxin.videonews.adapter.LocalAdapter;

/**
 * Created by Able on 2016/8/14.
 */
public class LocalFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private GridView gridView;

    private LocalAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_local_video, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        getLoaderManager().initLoader(0, null, this);

        gridView = (GridView) view.findViewById(R.id.gridView);

        adapter = new LocalAdapter(getContext());

        gridView.setAdapter(adapter);

    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        adapter.release();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                MediaStore.Video.Media._ID, // 视频ID
                MediaStore.Video.Media.DATA, // 视频文件路径
                MediaStore.Video.Media.DISPLAY_NAME // 视频名称
        };

        return new CursorLoader(getContext(), MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        adapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        adapter.swapCursor(null);

    }

}
