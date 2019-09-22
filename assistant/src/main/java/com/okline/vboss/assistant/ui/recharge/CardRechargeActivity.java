package com.okline.vboss.assistant.ui.recharge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.okline.vboss.assistant.R;
import com.okline.vboss.assistant.R2;
import com.okline.vboss.assistant.base.Config;
import com.okline.vboss.assistant.net.CardEntity;
import com.okline.vboss.assistant.net.OLApiService;
import com.okline.vboss.assistant.ui.MainActivity;
import com.okline.vboss.assistant.ui.opay.BankDebitInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class CardRechargeActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    public static final String EXTRA_CARD_ENTITY = "card_entity";
    @BindView(R2.id.iv_return)
    ImageView ivReturn;
    @BindView(R2.id.vp_card_recharge)
    NonScrollableViewPager vpCardRecharge;
    private ArrayList<Fragment> fragments;
    private MainFragment mainFragment;
    CardEntity cardEntity;
    ArrayList<CardEntity> bankCards;
    private static final String TAG = "CardRechargeActivity";
    CardRechargeInfo cardRechargeInfo;
    public String orderDesc;
    public String orderNum;
    public BankDebitInfo bankDebitInfo;
    private RechargeFragment rechargeFragment;
    public ArrayList<CardEntity> availableBanks;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_recharge_assistant);
        ButterKnife.bind(this);
        ivReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.tag(TAG).i("finish");
                finish();
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.vboss.okline.action.OCARD_STATE_CHANGED");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                MainActivity.ocardState = intent.getIntExtra("ocard_state", 0);
                Utils.showLog(TAG, "卡助理收到欧卡状态广播：" + MainActivity.ocardState);
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);

        if (TextUtils.isEmpty(Config.ADDRESS)) {
            Intent intent = getIntent();
            try {
                if (intent != null) {
                    String olNo = intent.getStringExtra(Config.KEY_OLNO);
                    Config.OL_NUM = olNo;
                    MainActivity.ocardState = intent.getIntExtra(Config.KEY_OCARD_STATE, 0);
                    Config.ADDRESS = intent.getStringExtra(Config.KEY_ADDRESS);
                    Config.USER_NAME = intent.getStringExtra(Config.KEY_USER_NAME);
                    Config.MOBILE = intent.getStringExtra(Config.KEY_MOBILE);
                    Timber.tag(TAG).i("bluetooth Address %s olNo %s ", Config.ADDRESS, Config.OL_NUM);
                    OLApiService.initialize(this.getApplication(), olNo);
                    OLApiService.getInstance().setAddress(Config.ADDRESS);
                } else {
                    throw new IllegalStateException("Without Bundle including olno & address");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        cardEntity = (CardEntity) getIntent().getSerializableExtra(EXTRA_CARD_ENTITY);
        Utils.showLog(TAG, "卡片信息：" + cardEntity.toString());
        fragments = new ArrayList<>();
        mainFragment = new MainFragment();
        fragments.add(mainFragment);
        rechargeFragment = new RechargeFragment();
        fragments.add(rechargeFragment);
        vpCardRecharge.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        vpCardRecharge.setOffscreenPageLimit(fragments.size() - 1);
        vpCardRecharge.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_return) {
            finish();

        }
    }

    @Override
    public void onBackPressed() {
        //禁用返回键
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Utils.showLog(TAG, "onPageSelected position = [" + position + "]");
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
