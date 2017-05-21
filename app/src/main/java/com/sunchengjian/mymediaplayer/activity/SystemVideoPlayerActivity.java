package com.sunchengjian.mymediaplayer.activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.sunchengjian.mymediaplayer.R;
import com.sunchengjian.mymediaplayer.domain.MediaItem;
import com.sunchengjian.mymediaplayer.utils.Utils;

import java.util.ArrayList;

public class SystemVideoPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    private VideoView vv;
    private Uri uri;

    private static final int PROGRESS = 0;

    private static final int HIDE_MEDIACONTROLLER = 1;

    private static final int DEFUALT_SCREEN = 0;

    private static final int FULL_SCREEN = 1;
    private ArrayList<MediaItem> mediaItems;

    private LinearLayout llTop;
    private TextView tvName;
    private ImageView ivBattery;
    private TextView tvSystemTime;
    private Button btnVoice;
    private SeekBar seekbarVoice;
    private Button btnSwitchPlayer;
    private LinearLayout llBottom;
    private TextView tvCurrentTime;
    private SeekBar seekbarVideo;
    private TextView tvDuration;
    private Button btnExit;
    private Button btnPre;
    private Button btnStartPause;
    private Button btnNext;
    private Button btnSwitchScreen;
    private Utils utils;
    //private MyBroadCastReceiver receiver;

    private int position;
    //手势识别器
    private GestureDetector detector;


    private boolean isFullScreen = false;

    private int screenHeight;
    private int screenWidth;

    private int videoWidth;
    private int videoHeight;


    private int currentVoice;
    private AudioManager am;

    private int maxVoice;

    private boolean isMute = false;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-05-20 11:01:51 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        setContentView(R.layout.activity_system_video_player);
        llTop = (LinearLayout) findViewById(R.id.ll_top);
        tvName = (TextView) findViewById(R.id.tv_name);
        ivBattery = (ImageView) findViewById(R.id.iv_battery);
        tvSystemTime = (TextView) findViewById(R.id.tv_system_time);
        btnVoice = (Button) findViewById(R.id.btn_voice);
        seekbarVoice = (SeekBar) findViewById(R.id.seekbar_voice);
        btnSwitchPlayer = (Button) findViewById(R.id.btn_switch_player);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        tvCurrentTime = (TextView) findViewById(R.id.tv_current_time);
        seekbarVideo = (SeekBar) findViewById(R.id.seekbar_video);
        tvDuration = (TextView) findViewById(R.id.tv_duration);
        btnExit = (Button) findViewById(R.id.btn_exit);
        btnPre = (Button) findViewById(R.id.btn_pre);
        btnStartPause = (Button) findViewById(R.id.btn_start_pause);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnSwitchScreen = (Button) findViewById(R.id.btn_switch_screen);
        vv = (VideoView) findViewById(R.id.vv);

        btnVoice.setOnClickListener(this);
        btnSwitchPlayer.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnPre.setOnClickListener(this);
        btnStartPause.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnSwitchScreen.setOnClickListener(this);

        //关联最大音量
        seekbarVoice.setMax(maxVoice);
        //设置当前进度
        seekbarVoice.setProgress(currentVoice);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_system_video_player);
        findViews();
        //  vv = (VideoView) findViewById(R.id.vv);
        uri = getIntent().getData();
        initdata();
        setVideoLister();
        //将视频地址传给VV

        vv.setVideoURI(uri);
        vv.setMediaController(new MediaController(this));
    }

    private void initdata() {
        utils = new Utils();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PROGRESS:
                    int currentPosition = vv.getCurrentPosition();
                    seekbarVideo.setProgress(currentPosition);

                    tvCurrentTime.setText(utils.stringForTime(currentPosition));
                    sendEmptyMessageDelayed(PROGRESS, 1000);
                    break;
            }
        }
    };

    private void setVideoLister() {
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            //准备好时用到
            @Override
            public void onPrepared(MediaPlayer mp) {
                int duration = vv.getDuration();
                seekbarVideo.setMax(duration);
                 tvDuration.setText(utils.stringForTime(duration));
                vv.start();
                handler.sendEmptyMessage(PROGRESS);
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
            //播放完成时
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(SystemVideoPlayerActivity.this, "播放完成", Toast.LENGTH_SHORT).show();
                //退出当前页面
                finish();
            }
        });
        seekbarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    vv.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btnVoice) {
            isMute = !isMute;

        } else if (v == btnSwitchPlayer) {

        } else if (v == btnExit) {

        } else if (v == btnPre) {


        } else if (v == btnStartPause) {
            if (vv.isPlaying()) {
                vv.pause();
                btnStartPause.setBackgroundResource(R.drawable.btn_start_selector);
            } else {
                vv.start();
                btnStartPause.setBackgroundResource(R.drawable.btn_pause_selector);
            }

        } else if (v == btnNext) {

        } else if (v == btnSwitchScreen) {

            if (isFullScreen) {


            } else {

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}