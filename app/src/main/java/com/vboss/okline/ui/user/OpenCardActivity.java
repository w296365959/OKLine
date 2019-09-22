package com.vboss.okline.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.CardCondition;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.ui.card.CardConstant;
import com.vboss.okline.ui.card.recharge.CardRechargeMainFragment;
import com.vboss.okline.ui.opay.OPaySDKActivity;
import com.vboss.okline.ui.opay.PaymentRunningDialog;
import com.vboss.okline.ui.user.callback.OpenCardConditionsListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OpenCardActivity extends BaseActivity implements OpenCardContract.View {

    public static final int REQUEST_CODE_OPEN_RECHARGE = 387;
    public String failMessage;
    public User user;
    public String orderNum;//商户订单号
    @BindView(R.id.vp_open_card)
    com.vboss.okline.ui.opay.NonScrollableViewPager vpOpenCard;
    OpenCardPresenter openCardPresenter;
    CardCondition cardCondition;
    CardEntity initialCardEntity;
    String orderNo;
    String cardNumber;
    ArrayList<CardEntity> bankCards;
    int paymentResult;
    private CardOpenRechargeFragment cardOpenRechargeFragment;
    private static final String TAG = "OpenCardActivity";
    private ArrayList<Fragment> fragments;
    private CardOpenFragment cardOpenFragment;
    private CardOpenInfoFragment cardOpenInfoFragment;
    private CardOpenPaymentFragment cardOpenPaymentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_card);
        ButterKnife.bind(this);

        openCardPresenter = new OpenCardPresenter(new OpenCardModel(this), this);

        try {
            user = UserRepository.getInstance(this).getUser();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        获取intent中调用页面所传信息
        Intent intent = getIntent();
        initialCardEntity = (CardEntity) intent.getSerializableExtra(CardConstant.CARD_INSTANCE);
        cardCondition = (CardCondition) intent.getSerializableExtra(CardConstant.CARD_CONDITION);
        bankCards = (ArrayList<CardEntity>) intent.getSerializableExtra(CardConstant.BANK_CARDS);

        if (cardCondition == null) {
//        设定启动画面与动作
//        获取开卡条件
            openCardPresenter.getOpenCardConditions(initialCardEntity.cardMainType(), initialCardEntity.aid(), new OpenCardConditionsListener() {
                @Override
                public void onFetch(CardCondition cardCondition) {
                    if (cardCondition != null) {
                        OpenCardActivity.this.cardCondition = cardCondition;
                        initUI();
                    } else {
                        onFail("");
                    }
                }

                @Override
                public void onFail(String message) {
                    showToast("无法获取开卡信息，请稍后再试");
                }
            });
        } else {
            initUI();
        }
    }

    private void initUI() {
        //        配置界面
        fragments = new ArrayList<>();
        cardOpenInfoFragment = new CardOpenInfoFragment();
        cardOpenFragment = new CardOpenFragment();
        cardOpenRechargeFragment = new CardOpenRechargeFragment();
        cardOpenPaymentFragment = new CardOpenPaymentFragment();
        fragments.add(cardOpenInfoFragment);
        fragments.add(cardOpenFragment);
        fragments.add(cardOpenRechargeFragment);
        fragments.add(cardOpenPaymentFragment);

        vpOpenCard.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });

        vpOpenCard.setOffscreenPageLimit(fragments.size());

        if (cardCondition.needToAuth() == 1) {
            vpOpenCard.setCurrentItem(0);
        } else if (cardCondition.needToAuth() == 0 && cardCondition.needPrestoreToOpen() == 0 && cardCondition.needFeeToOpen() == 0) {
            vpOpenCard.setCurrentItem(1);
        } else {
            vpOpenCard.setCurrentItem(2);
        }

        vpOpenCard.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 3) {
//                    cardOpenRechargeFragment.initUI();
                }
                if (position == 4) {
                    cardOpenPaymentFragment.initUI(paymentResult == 1, failMessage);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        Utils.showLog(TAG, "懒加载页数：" + vpOpenCard.getOffscreenPageLimit());
    }

    @Override
    public void showPersonalInfo(User user) {
        if (vpOpenCard.getCurrentItem() == 0 && user != null && cardOpenInfoFragment != null) {
            cardOpenInfoFragment.showPersonalInfo(user);
        }
    }

    @Override
    public void showToast(String message) {
        Utils.customToast(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.showLog(TAG, "requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        if (requestCode == CardOpenRechargeFragment.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                paymentResult = 1;
                orderNo = data.getStringExtra(OPaySDKActivity.PAYMENT_ARGUMENT_TRIALNUMBER);
                Utils.showLog(TAG, "支付模块返回的流水号：" + orderNo);
                vpOpenCard.setCurrentItem(3, true);
            } else if (resultCode == RESULT_CANCELED) {
                paymentResult = 2;
                failMessage = data != null ? data.getStringExtra(PaymentRunningDialog.FAIL_MESSAGE) : "支付已取消";
                vpOpenCard.setCurrentItem(3, true);
            }
        } else if (requestCode == CardOpenPaymentFragment.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                paymentResult = 1;
                orderNo = data.getStringExtra(OPaySDKActivity.PAYMENT_ARGUMENT_TRIALNUMBER);
                Utils.showLog(TAG, "支付模块返回的流水号：" + orderNo);
            } else if (resultCode == RESULT_CANCELED) {
                paymentResult = 2;
                failMessage = data != null ? data.getStringExtra(PaymentRunningDialog.FAIL_MESSAGE) : "支付已取消";
            }
            if (vpOpenCard.getCurrentItem() == 3) {
                cardOpenPaymentFragment.initUI(paymentResult == 1, failMessage);
            }
        }

        if (requestCode == REQUEST_CODE_OPEN_RECHARGE) {
            Utils.showLog(TAG, "★ 开卡成功后充值成功返回数据！");
            if (data != null && data.hasExtra(CardConstant.CARD_INSTANCE)) {
                setResult(RESULT_OK, data);
                finish();
            }
        }


        if (data != null
                && data.hasExtra(CardRechargeMainFragment.INTENT_TAG)
                && data.getStringExtra(CardRechargeMainFragment.INTENT_TAG).equals(CardRechargeMainFragment.INTENT_OPEN_BANK_CARD)) {
            Utils.showLog(TAG, "onActivityResult跳转到银行卡开卡界面");
            setResult(RESULT_CANCELED,data);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        int currentItem = vpOpenCard.getCurrentItem();
        switch (currentItem) {
            case 0:
                finish();
                break;
            case 1:
                break;
            case 2:
                if (cardCondition.needToAuth() == 1) {
                    vpOpenCard.setCurrentItem(0);
                } else {
                    finish();
                }
                break;
            case 3:
                break;
            default:
                super.onBackPressed();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        openCardPresenter.onViewDestroy();
    }
}
