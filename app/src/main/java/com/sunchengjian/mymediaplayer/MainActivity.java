package com.sunchengjian.mymediaplayer;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.sunchengjian.mymediaplayer.framgment.BaseFragment;
import com.sunchengjian.mymediaplayer.pager.LocalAudioPager;
import com.sunchengjian.mymediaplayer.pager.LocalVideoPager;
import com.sunchengjian.mymediaplayer.pager.NetAudioPager;
import com.sunchengjian.mymediaplayer.pager.NetVideoPager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RadioGroup rg_main;
    private ArrayList<BaseFragment> fragments;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rg_main = (RadioGroup) findViewById(R.id.rg_main);
        initFragment();

        //RadioGroup 设置监听
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        rg_main.check(R.id.rb_local_video);

    }

    private void initFragment() {
        fragments = new ArrayList<>();
        fragments.add(new LocalVideoPager());
        fragments.add(new LocalAudioPager());
        fragments.add(new NetVideoPager());
        fragments.add(new NetAudioPager());
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId) {
                case R.id.rb_local_video:
                    position = 0;
                    break;
                case R.id.rb_local_audio:
                    position = 1;
                    break;
                case R.id.rb_net_video:
                    position = 2;
                    break;
                case R.id.rb_net_audio:
                    position = 3;
                    break;
            }
            BaseFragment baseFragment = fragments.get(position);
            addFragment(baseFragment);
        }
    }

    private Fragment tempFragment;

    private void addFragment(Fragment currentFragment) {
        if (tempFragment != currentFragment) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (!currentFragment.isAdded()) {
                if (tempFragment != null) {
                    ft.hide(tempFragment);
                }
                ft.add(R.id.fl_content, currentFragment);
            } else {
                if (tempFragment != null) {
                    ft.hide(tempFragment);
                }
                //显示当前的
                ft.show(currentFragment);
            }
            ft.commit();//提交事务
            //把当前的缓存起来
            tempFragment = currentFragment;
        }
    }

}
