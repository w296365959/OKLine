package com.vboss.okline.ui.opay;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.vboss.okline.R;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.CardRepository;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.ui.user.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * OKLine(Hangzhou) Co.,ltd.
 * Author:Zheng Jun
 * Email:zhengjun@okline.cn
 * Date: 2016-6-16 11:28:11
 * Desc:
 */
public class OPaySDKActivity extends FragmentActivity {

    public static final String PAYMENT_ARGUMENT_TRIALNUMBER = "tn";
    public static final String PAYMENT_ARGUMENT_CARDLIST = "cardList";
    public NonScrollableViewPager viewPager;
    User user;
    ArrayList<Fragment> fragments;
    private OPayFragment fragment0;
    private OPayPasswordFragment fragment1;
    private PaymentResultFragment fragment2;
    private CardListFragment fragment3;
    String orderAmount,orderNumber,merNo,orderDesc,orderTn,cardId,paymentNote;
    int cardMainType,heightPixels;
    boolean showResultPage,isEmptyCards,cardSelected;
    View anchor;
    ArrayList<CardEntity> cardEntities;
    CardEntity selectedCard;
    BankDebitInfo bankDebitInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Fresco.hasBeenInitialized()) {
            Fresco.initialize(this);
        }
        setContentView(R.layout.activity_opaysdk);
        //获取屏幕高度宽度
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        heightPixels = outMetrics.heightPixels;
        final int widthPixels = outMetrics.widthPixels;
        int dpi = Utils.getDpi(OPaySDKActivity.this);
        final int height = (2 * dpi / 3) - (dpi - heightPixels);

        Utils.setActionBarColor(R.color.colorOPayBackground, this);
        viewPager = (NonScrollableViewPager) findViewById(R.id.vp);

        ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
        layoutParams.height = height;
        viewPager.setLayoutParams(layoutParams);

        anchor = findViewById(R.id.anchor);

        try {
            user = UserRepository.getInstance(OPaySDKActivity.this).getUser();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<CardEntity> serializableExtra =
                (ArrayList<CardEntity>) getIntent().getSerializableExtra(PAYMENT_ARGUMENT_CARDLIST);
        if (!serializableExtra.isEmpty()) {
            cardEntities = serializableExtra;
            for (CardEntity cardEntity : cardEntities) {
                Utils.showLog(TAG, "提前获取到的卡片：" + cardEntity.cardName());
            }
            selectedCard = cardEntities.get(0);
            isEmptyCards = cardEntities.isEmpty();
            processCardList();
        } else {
            cardEntities = new ArrayList<>();
            Utils.showLog(TAG, "★OPaySDKActivity开始申请卡片列表");
            CardRepository.getInstance(OPaySDKActivity.this).cardList(CardType.BANK_CARD)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultSubscribe<List<CardEntity>>(TAG) {
                        @Override
                        public void onCompleted() {
                            super.onCompleted();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            super.onError(throwable);
                            Utils.showLog(TAG, "请求卡片列表失败：" + throwable.getMessage());
                            Utils.customToast(OPaySDKActivity.this, getString(R.string.error_connection_lost), Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onNext(List<CardEntity> cardEntities) {
                            super.onNext(cardEntities);
                            Utils.showLog(TAG, "请求卡片列表成功");
                            if (cardEntities != null && !cardEntities.isEmpty()) {
                                for (CardEntity cardEntity : cardEntities) {
                                    if (cardEntity.isQuickPass() == 2) {
//                                    区分闪付卡
                                        Utils.showLog(TAG, cardEntity.toString());
                                        OPaySDKActivity.this.cardEntities.add(cardEntity);
                                    }
                                }
                                if (!OPaySDKActivity.this.cardEntities.isEmpty()) {
//                                闪付卡集合不为空
                                    Utils.showLog(TAG, "闪付卡集合不为空");
                                    selectedCard = OPaySDKActivity.this.cardEntities.get(0);
                                } else {
                                    isEmptyCards = true;
                                }
                            } else {
                                isEmptyCards = true;
                            }
                            processCardList();
                        }
                    });
        }
    }

    private void processCardList() {
        if (isEmptyCards) {
            Utils.customToast(OPaySDKActivity.this, getString(R.string.card_open_no_card_alert), Toast.LENGTH_SHORT).show();
            finish();
        }

        if (fragments == null) {
            showFragments();
        } else {
            if (fragment0 != null) {
                try {
                    fragment0.setAccountName();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        viewPager.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    void showFragments() {
        Intent intent = getIntent();
        BankDebitInfo info = (BankDebitInfo) intent.getSerializableExtra(BankDebitInfo.BANK_DEBIT_INFO);
        Utils.showLog(TAG,""+info);
        orderAmount = info.getOrderAmount();
        orderNumber = info.getOrderNumber();
        orderTn = info.getTn();
        merNo = info.getMerNo();
        orderDesc = info.getOrderDesc();
        showResultPage = info.isShowResultPage();
        cardId = info.getCardId();
        cardMainType = info.getCardType();
        cardSelected = info.isCardSelected();

        //初始化ViewPager
        fragments = new ArrayList<>();
        fragment0 = new OPayFragment();
        fragments.add(fragment0);
        fragment1 = new OPayPasswordFragment();
        fragments.add(fragment1);
        //由于银行提供的是免密付款接口,暂时隐藏银行密码输入界面
//        final CardPasswordFragment cardPasswordFragment = new CardPasswordFragment();
//        fragments.add(cardPasswordFragment);
        fragment2 = new PaymentResultFragment();
        fragments.add(fragment2);
        fragment3 = new CardListFragment();
        fragments.add(fragment3);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                return super.instantiateItem(container, position);
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });

        //进入页面的第一个Page
        viewPager.setCurrentItem(cardSelected ?1:0);
        viewPager.setOffscreenPageLimit(fragments.size() - 1);

        //页面监听
        viewPager.addOnPageChangeListener(new NonScrollableViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        try {
                            fragment0.setAccountName();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        if (fragment1.gvKeyboard != null) {
                            ViewGroup.LayoutParams aFragment3gvKeyboardLayoutParams = fragment1.gvKeyboard.getLayoutParams();
                            aFragment3gvKeyboardLayoutParams.height = heightPixels / 3;
                            fragment1.gvKeyboard.setLayoutParams(aFragment3gvKeyboardLayoutParams);
                        }

                        break;
                    case 2:
                        if (fragment2.ivLoading != null) {
                            fragment2.setPaymentExecutingAnimation();
                        }
                        break;
                    case 3:

                        break;
                    default:

                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        //禁用返回键
    }

    private static final String TAG = "OPaySDKActivity";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.showLog(TAG, "requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        Intent data1 = new Intent();
        data1.putExtra(PAYMENT_ARGUMENT_TRIALNUMBER, this.orderTn);
        data1.putExtra(BankDebitInfo.BANK_DEBIT_INFO,bankDebitInfo);
        setResult(RESULT_OK, data1);
        if (viewPager.getCurrentItem() == 0) {

        } else if (requestCode == 251 && resultCode == RESULT_OK) {
            finish();
        }
    }
}
