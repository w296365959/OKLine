package com.vboss.okline.ui.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.vboss.okline.R;
import com.vboss.okline.ui.record.RecordFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/3/28
 * Summary : 测试用Activity
 */

public class UserActivity extends AppCompatActivity {

    @BindView(R.id.vp)
    ViewPager vp;
    private ArrayList<Fragment> fragments;
    private RecordFragment userFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        vp = (ViewPager) findViewById(R.id.vp);

        userFragment = new RecordFragment();

        fragments = new ArrayList<>();
        fragments.add(userFragment);

        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
    }
}
