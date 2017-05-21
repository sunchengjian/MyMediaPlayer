package com.sunchengjian.mymediaplayer.pager;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.sunchengjian.mymediaplayer.R;
import com.sunchengjian.mymediaplayer.adapter.LocalVideoAdapter;
import com.sunchengjian.mymediaplayer.domain.MediaItem;
import com.sunchengjian.mymediaplayer.framgment.BaseFragment;

import java.util.ArrayList;

/**
 * Created by 0..0 on 2017/5/21.
 */

public class LocalVideoPager extends BaseFragment {
    private ListView lv;
    private TextView tv_nodata;
    private ArrayList<MediaItem> mediaItems;
    private LocalVideoAdapter adapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mediaItems != null & mediaItems.size() > 0) {
                tv_nodata.setVisibility(View.GONE);

                adapter = new LocalVideoAdapter(context, mediaItems);
                lv.setAdapter(adapter);
            } else {
                tv_nodata.setVisibility(View.VISIBLE);
            }
        }
    };
    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.fragment_local_video_pager, null);
        lv = (ListView) view.findViewById(R.id.lv);
        tv_nodata = (TextView) view.findViewById(R.id.tv_nodata);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        //加载本地的所有的视屏
        getData();
    }

    private void getData() {
        new Thread() {
            public void run() {
                mediaItems = new ArrayList<MediaItem>();

                ContentResolver resolver = context.getContentResolver();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        //视频的名字
                        MediaStore.Video.Media.DISPLAY_NAME,
                        //视频的时长
                        MediaStore.Video.Media.DURATION,
                        //大小
                        MediaStore.Video.Media.SIZE,
                        //地址
                        MediaStore.Video.Media.DATA
                };
                Cursor cursor = resolver.query(uri, objs, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {

                        String name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                        long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                        long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                        String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));

                        mediaItems.add(new MediaItem(name, duration, size, data));
                        //子线程不能更新UI 用handler实现
                        handler.sendEmptyMessage(0);

                    }
                    cursor.close();
                }
            }
        }.start();
    }
}
