package com.vboss.okline.ui.card.recharge;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.CardCondition;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.ui.card.CardConstant;
import com.vboss.okline.ui.opay.BankDebitInfo;
import com.vboss.okline.ui.opay.NonScrollableViewPager;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.ui.user.entity.CardRechargeInfo;
import com.vboss.okline.utils.TextUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/5/3
 * Summary : 在这里描述Class的主要功能
 */

public class CardRechargeActivity extends BaseActivity {

    @BindView(R.id.vp_open_card)
    NonScrollableViewPager vpOpenCard;
    CardEntity cardEntity;
    CardCondition cardCondition;
    ArrayList<CardEntity> bankCards;
    User user;
    private ArrayList<Fragment> fragments;
    int paymentResult;
    String orderNo;
    String failMessage;
    private CardRechargeMainFragment cardRechargeMainFragment;
    private CardRechargeResultFragment cardRechargeResultFragment;
    private CardRechargeOperationFragment cardRechargeOperationFragment;
    CardRechargeInfo cardRechargeInfo;
    BankDebitInfo bankDebitInfo;
    public String orderNum;//商户订单号

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_recharge);
        ButterKnife.bind(this);
        user = UserRepository.getInstance(this).getUser();

        Intent intent = getIntent();
        cardEntity = (CardEntity) intent.getSerializableExtra(CardConstant.CARD_INSTANCE);
        Utils.showLog(TAG,"★卡片信息："+cardEntity);
        cardCondition = (CardCondition) intent.getSerializableExtra(CardConstant.CARD_CONDITION);
        bankCards = (ArrayList<CardEntity>) intent.getSerializableExtra(CardConstant.BANK_CARDS);

        fragments = new ArrayList<>();
        cardRechargeMainFragment = new CardRechargeMainFragment();
        cardRechargeResultFragment = new CardRechargeResultFragment();
        cardRechargeOperationFragment = new CardRechargeOperationFragment();
        fragments.add(cardRechargeMainFragment);
        fragments.add(cardRechargeResultFragment);
        fragments.add(cardRechargeOperationFragment);

        vpOpenCard.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }
        });
        vpOpenCard.setOffscreenPageLimit(fragments.size());
        vpOpenCard.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 1:
                        if (!TextUtils.isEmpty(orderNo)) {
                            cardRechargeResultFragment.initUI(paymentResult == 1, failMessage);
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Utils.showLog(TAG,"★懒加载页数："+vpOpenCard.getOffscreenPageLimit());
    }

    private static final String TAG = "CardRechargeActivity";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CardRechargeMainFragment.REQUEST_CODE_RECHARGE) {
            cardRechargeMainFragment.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == CardRechargeResultFragment.REQUEST_CODE) {
            cardRechargeResultFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        //禁用返回键
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }
}
