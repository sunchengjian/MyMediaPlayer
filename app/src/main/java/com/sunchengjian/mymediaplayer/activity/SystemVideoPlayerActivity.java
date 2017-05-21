package com.sunchengjian.mymediaplayer.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.sunchengjian.mymediaplayer.R;

public class SystemVideoPlayerActivity extends AppCompatActivity {
    private VideoView vv;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_video_player);

        vv = (VideoView) findViewById(R.id.vv);
        uri = getIntent().getData();

        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            //准备好时用到
            @Override
            public void onPrepared(MediaPlayer mp) {
                vv.start();
            }
        });
        vv.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            //播放错误时用
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(SystemVideoPlayerActivity.this, "播放错误", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            //播放完成
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(SystemVideoPlayerActivity.this, "播放完成", Toast.LENGTH_SHORT).show();
                //退出当前页面
                finish();
            }
        });
        //将视频地址传给VV
        vv.setVideoURI(uri);
        vv.setMediaController(new MediaController(this));
    }
}
