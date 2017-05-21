package com.sunchengjian.mymediaplayer.pager;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.sunchengjian.mymediaplayer.framgment.BaseFragment;

/**
 * Created by 0..0 on 2017/5/21.
 */

public class LocalAudioPager extends BaseFragment {
    private TextView tv;

    @Override
    public View initView() {
        tv = new TextView(context);
        tv.setTextSize(30);
        tv.setGravity(Gravity.CENTER);
        return tv;
    }

    @Override
    public void initData() {
        super.initData();
        tv.setText("这是本地音乐");
    }
}
