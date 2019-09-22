package com.okline.vboss.assistant.ui.notice;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hwangjr.rxbus.RxBus;
import com.okline.ocp.CustomRevoke;
import com.okline.vboss.assistant.R;
import com.okline.vboss.assistant.R2;
import com.okline.vboss.assistant.base.Config;
import com.okline.vboss.assistant.net.CardEntity;
import com.okline.vboss.assistant.net.RechargeEntity;
import com.okline.vboss.assistant.utils.DensityUtil;
import com.okline.vboss.assistant.utils.StringUtils;
import com.okline.vboss.assistant.widget.FailRechargeDialog;
import com.okline.vboss.assistant.widget.NotRechargeDialog;
import com.okline.vboss.assistant.widget.shadow.ListViewShadowViewHelper;
import com.okline.vboss.assistant.widget.shadow.ShadowProperty;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/6/8 19:34 <br/>
 * Summary  : 在这里描述Class的主要功能
 */

public class CardNoticeActivity extends AppCompatActivity {
    private static final String TAG = CardNoticeActivity.class.getSimpleName();
    // findView by id
    @BindView(R2.id.tv_amount)
    TextView tv_amount;
    @BindView(R2.id.tv_balance)
    TextView tv_balance;
    @BindView(R2.id.iv_card_notice_state)
    ImageView iv_card_state;
    @BindView(R2.id.ib_voice)
    ImageButton ib_voice;
    @BindView(R2.id.tv_btn)
    TextView tv_btn;
    @BindView(R2.id.tv_btn_give_up)
    TextView tv_give_up;

    //card download or card consume
    @BindView(R2.id.layout_open_card)
    LinearLayout layout_card_consume;
    @BindView(R2.id.layout_print)
    LinearLayout layout_print;
    @BindView(R2.id.layout_download_success)
    LinearLayout layout_download_success;
    @BindView(R2.id.tv_download_card_fail)
    TextView tv_download_card_fail;
    @BindView(R2.id.simple_card_image)
    SimpleDraweeView simple_card_image;

    //card recharge layout
    @BindView(R2.id.layout_card_recharge)
    LinearLayout layout_card_recharge;
    @BindView(R2.id.layout_recharge_success)
    LinearLayout layout_recharge_success;
    @BindView(R2.id.layout_recharge_fail)
    LinearLayout layout_recharge_fail;
    @BindView(R2.id.simple_card_image_from)
    SimpleDraweeView simple_card_image_from;
    @BindView(R2.id.simple_card_image_to)
    SimpleDraweeView simple_card_image_to;

    private int mode;
    private static final int count = 10;
    private TimeCount timeCount;
    private CardEntity cardEntity;
    private RechargeEntity rechargeEntity;
    private CardHelper cardHelper;

    private FailRechargeDialog failRechargeDialog;
    private NotRechargeDialog notRechargeDialog;
    private DetailFragment detailFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_card_notice_assistant);
        ButterKnife.bind(this);
        //Added by wangshuai 2017-06-14  RxBus register
        RxBus.get().unregister(this);
        Timber.tag(TAG).i(" onCreate ");

        cardHelper = CardHelper.getInstance(this);
        Bundle args = getIntent().getExtras();
        if (args != null) {
            mode = args.getInt(CardHelper.KEY_OPERATE, CardHelper.CARD_CONSUME);
            cardEntity = (CardEntity) args.getSerializable(CardHelper.KEY_CARD);
            rechargeEntity = (RechargeEntity) args.getSerializable(CardHelper.KEY_RECHARGE);
        }
        Timber.tag(TAG).i("mode %s", mode);
        notRechargeDialog = new NotRechargeDialog(this);
        failRechargeDialog = new FailRechargeDialog(this);
        //modify by wangshuai 2017-06-14 onResume show UI
        initView();
        //player music
        initVoiceIcon();
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    private void initView() {
        switch (mode) {
            case CardHelper.CARD_CONSUME:
                ib_voice.setVisibility(View.VISIBLE);
                layout_card_consume.setVisibility(View.VISIBLE);
                layout_print.setVisibility(View.VISIBLE);
                iv_card_state.setImageResource(R.mipmap.ic_card_notice_success);
                break;
            case CardHelper.CARD_RECHARGE_SUCCESS:
                ib_voice.setVisibility(View.VISIBLE);
                layout_card_recharge.setVisibility(View.VISIBLE);
                layout_recharge_success.setVisibility(View.VISIBLE);
                iv_card_state.setImageResource(R.mipmap.ic_card_notice_success);
                break;
            case CardHelper.CARD_RECHARGE_FAIL:
                layout_card_recharge.setVisibility(View.VISIBLE);
                tv_give_up.setVisibility(View.VISIBLE);
                layout_recharge_fail.setVisibility(View.VISIBLE);
                iv_card_state.setImageResource(R.mipmap.ic_card_notice_fail);
                break;
            case CardHelper.CARD_DOWNLOAD_SUCCESS:
                ib_voice.setVisibility(View.VISIBLE);
                layout_card_consume.setVisibility(View.VISIBLE);
                layout_download_success.setVisibility(View.VISIBLE);
                iv_card_state.setImageResource(R.mipmap.ic_card_notice_success);
                break;
            case CardHelper.CARD_DOWNLOAD_FAIL_BLUETOOTH:
            case CardHelper.CARD_DOWNLOAD_FAIL_OCARD:
                layout_card_consume.setVisibility(View.VISIBLE);
                tv_give_up.setVisibility(View.VISIBLE);
                tv_download_card_fail.setVisibility(View.VISIBLE);
                tv_balance.setVisibility(View.INVISIBLE);
                iv_card_state.setImageResource(R.mipmap.ic_card_notice_fail);
                break;
        }
        initData();
    }

    private void initData() {
        try {
            timeCount = new TimeCount(count * 1000, 1000);
            //card consume
            if (mode == CardHelper.CARD_CONSUME) {
                tv_amount.setText(String.format(getResources().getString(R.string.card_notice_consume_amount), "100.00"));
                StringBuilder builder = new StringBuilder();
                builder.append(getResources().getString(R.string.rmb)).append("119.50");
                String balance = String.format(getResources().getString(R.string.card_balance), builder);
                SpannableString ss = new SpannableString(balance);
                ss.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(this, R.color.color_orange)),
                        balance.indexOf(getResources().getString(R.string.rmb)), balance.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_balance.setText(ss);
                tv_btn.setText(String.format(getResources().getString(R.string.card_notice_ensure), count));
                timeCount.start();
            }
            //card recharge success
            if (mode == CardHelper.CARD_RECHARGE_SUCCESS) {
                if (rechargeEntity == null || cardEntity == null) {
                    Timber.tag(TAG).w("rechargeEntity or cardEntity is null");
                    finish();
                    return;
                }
                //Added by wangshuai 2017-06-14 post message refresh data
                postMessage();

                tv_amount.setText(String.format(getResources().getString(R.string.card_notice_recharge_amount), StringUtils.formatMoney(rechargeEntity.getRecharge_amount())));
                StringBuilder builder = new StringBuilder();
                builder.append(getResources().getString(R.string.rmb)).append(StringUtils.formatMoney(cardEntity.getBalance()));
                String balance = String.format(getResources().getString(R.string.card_balance), builder);
                SpannableString ss = new SpannableString(balance);
                ss.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(this, R.color.color_orange)),
                        balance.indexOf(getResources().getString(R.string.rmb)), balance.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_balance.setText(ss);
                tv_btn.setText(String.format(getResources().getString(R.string.card_notice_ensure), count));
                timeCount.start();
            }
            //card recharge fail
            if (mode == CardHelper.CARD_RECHARGE_FAIL) {
                if (rechargeEntity == null || cardEntity == null) {
                    Timber.tag(TAG).w("rechargeEntity or cardEntity is null");
                    finish();
                    return;
                }
                tv_amount.setText(String.format(getResources().getString(R.string.card_notice_recharge_amount), StringUtils.formatMoney(rechargeEntity.getRecharge_amount())));
                StringBuilder builder = new StringBuilder();
                builder.append(getResources().getString(R.string.rmb)).append(getResources().getString(R.string.quarter_empty)).append(StringUtils.formatMoney(cardEntity.getBalance()));
                String balance = String.format(getResources().getString(R.string.card_balance), builder);
                SpannableString ss = new SpannableString(balance);
                ss.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(this, R.color.color_orange)),
                        balance.indexOf(getResources().getString(R.string.rmb)), balance.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_balance.setText(ss);
                tv_btn.setText(getResources().getString(R.string.card_notice_recharge_again));
                tv_give_up.setText(getResources().getString(R.string.card_notice_no_recharge));
            }
            // card download success
            if (mode == CardHelper.CARD_DOWNLOAD_SUCCESS) {
                if (cardEntity == null) {
                    Timber.tag(TAG).w("cardEntity is null");
                    finish();
                    return;
                }
                postMessage();
                //Added by wangshuai 2017-06-14 post message refresh data
                tv_amount.setText(String.format(getResources().getString(R.string.card_notice_download_success), cardEntity.getCardName()));
                String cardNo = cardEntity.getCardNo();
                String balance = String.format(getResources().getString(R.string.card_no), cardNo);
                SpannableString ss = new SpannableString(balance);
                ss.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(this, R.color.color_orange)),
                        balance.length() - cardNo.length(), balance.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_balance.setText(ss);
                tv_btn.setText(String.format(getResources().getString(R.string.card_notice_ensure), count));
                timeCount.start();
            }
            // card download fail bluetooth connected fail
            if (mode == CardHelper.CARD_DOWNLOAD_FAIL_BLUETOOTH) {
                if (cardEntity == null) {
                    Timber.tag(TAG).w("cardEntity is null");
                    finish();
                    return;
                }
                tv_amount.setText(String.format(getResources().getString(R.string.card_notice_download_fail), cardEntity.getCardName()));
                tv_download_card_fail.setText(getResources().getString(R.string.card_notice_check_bluetooth));
                tv_btn.setText(getResources().getString(R.string.card_notice_download_again));
                tv_give_up.setText(getResources().getString(R.string.card_notice_no_down));
                tv_give_up.setVisibility(View.VISIBLE);
            }

            // card download fail OCard not enough memory
            if (mode == CardHelper.CARD_DOWNLOAD_FAIL_OCARD) {
                if (cardEntity == null) {
                    Timber.tag(TAG).w("cardEntity is null");
                    finish();
                    return;
                }
                tv_amount.setText(String.format(getResources().getString(R.string.card_notice_download_fail), cardEntity.getCardName()));
                tv_download_card_fail.setText(getResources().getString(R.string.card_notice_check_OCard));
                tv_btn.setText(getResources().getString(R.string.card_notice_no_down));
                tv_give_up.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initCardShadow();
    }

    /**
     * post refresh message
     */
    private void postMessage() {
        RxBus.get().post(CardHelper.EVENT_REFRESH, true);
        Timber.tag(TAG).i(" post message ");
    }

    private void initCardShadow() {
        if (cardEntity != null) {
            //show card image shadow
            simple_card_image.setImageURI(Uri.parse(cardEntity.getImgUrl()));
            ListViewShadowViewHelper.bindShadowHelper(
                    new ShadowProperty().setShadowColor(ActivityCompat.getColor(this, R.color.color_shadow))
                            .setShadowDy(DensityUtil.dip2px(this, 0.5f)).setShadowRadius(DensityUtil.dip2px(this, 3f)),
                    simple_card_image,
                    DensityUtil.dip2px(this, 5.5f),
                    DensityUtil.dip2px(this, 5.5f)
            );
        }
        if (rechargeEntity != null) {
            simple_card_image_from.setImageURI(Uri.parse(rechargeEntity.getBank_card_url()));
            ListViewShadowViewHelper.bindShadowHelper(
                    new ShadowProperty().setShadowColor(ActivityCompat.getColor(this, R.color.color_shadow))
                            .setShadowDy(DensityUtil.dip2px(this, 0.5f)).setShadowRadius(DensityUtil.dip2px(this, 3f)),
                    simple_card_image_from,
                    DensityUtil.dip2px(this, 3f),
                    DensityUtil.dip2px(this, 3f)
            );
            simple_card_image_to.setImageURI(Uri.parse(rechargeEntity.getCard_url()));
            ListViewShadowViewHelper.bindShadowHelper(
                    new ShadowProperty().setShadowColor(ActivityCompat.getColor(this, R.color.color_shadow))
                            .setShadowDy(DensityUtil.dip2px(this, 0.5f)).setShadowRadius(DensityUtil.dip2px(this, 3f)),
                    simple_card_image_to,
                    DensityUtil.dip2px(this, 3f),
                    DensityUtil.dip2px(this, 3f)
            );
        }
    }

    private void initVoiceIcon() {
        cardHelper.initMediaPlayer(this);
        if (cardHelper.getSilentMode()) {
            ib_voice.setImageResource(R.mipmap.ic_card_notice_silent);
        } else {
            ib_voice.setImageResource(R.mipmap.ic_card_notice_voice);
        }
        if (cardHelper.getSilentMode()) {
            Timber.tag(TAG).i("media is silent mode ");
            return;
        }
        if (mode == CardHelper.CARD_CONSUME || mode == CardHelper.CARD_DOWNLOAD_SUCCESS ||
                mode == CardHelper.CARD_RECHARGE_SUCCESS) {
            // TODO: 2017/6/12  播放音频
            Timber.tag(TAG).i("player media ");
            cardHelper.playMedia();
        }
    }

    /**
     * card info,card valid and card recharge info, discount active
     * click listener
     */
    @OnClick({R2.id.tv_card_info, R2.id.tv_card_valid, R2.id.tv_recharge_info, R2.id.tv_discount_active})
    public void onClick(View v) {
        //modify by wangshuai 2017-06-15 update view click listener
        int id = v.getId();
        if (id == R.id.tv_card_info) {
            // TODO: 2017/6/12  卡信息
            countFinish();
            showDetail(CardHelper.CARD_INFO);
        } else if (id == R.id.tv_card_valid) {
            // TODO: 2017/6/12  有效期

        } else if (id == R.id.tv_recharge_info) {
            // TODO: 2017/6/12  充值详情
            countFinish();
            showDetail(CardHelper.RECHARGE_INFO);

        } else if (id == R.id.tv_discount_active) {
            // TODO: 2017/6/12  优惠活动
            countFinish();
            showDetail(CardHelper.RECHARGE_DISCOUNT_ACTIVE);
        }
    }

    private void showDetail(int mode) {
        if (detailFragment == null) {
            detailFragment = DetailFragment.newInstance(mode);
        }
        detailFragment.setMode(mode);
        detailFragment.setCardEntity(cardEntity);
        detailFragment.setRechargeEntity(rechargeEntity);
        detailFragment.show(getSupportFragmentManager(), DetailFragment.class.getSimpleName());

    }

    /**
     * card consume look invoice or pos or ticket,
     * and play voice click listener
     */
    @OnClick({R2.id.ib_print_invoice, R2.id.ib_print_pos, R2.id.ib_print_ticket, R2.id.ib_voice})
    public void onPrint(View v) {
        //modify by wangshuai 2017-06-15 update view click listener
        int id = v.getId();
        if (id == R.id.ib_print_invoice) {
        } else if (id == R.id.ib_print_ticket) {
        } else if (id == R.id.ib_print_pos) {
        } else if (id == R.id.ib_voice) {
            cardHelper.setSilentMode(!cardHelper.getSilentMode());
            initVoiceIcon();
        }
    }

    @OnClick({R2.id.tv_btn, R2.id.tv_btn_give_up})
    public void onEnsure(View v) {
        //modify by wangshuai 2017-06-15 update view click listener
        int id = v.getId();
        if (id == R.id.tv_btn) {
            ensure();
        } else if (id == R.id.tv_btn_give_up) {
            giveUp();
        }
    }

    /**
     * ensure
     */
    private void ensure() {
        // TODO: 2017/6/9  确定按钮
        switch (mode) {
            case CardHelper.CARD_CONSUME:
                // TODO: 2017/6/9  刷卡
                finish();
                break;
            case CardHelper.CARD_RECHARGE_FAIL:
                // TODO: 2017/6/9  充值失败
                setResult(RESULT_OK);
                finish();
                break;
            case CardHelper.CARD_RECHARGE_SUCCESS:
                // TODO: 2017/6/9  充值成功
                //通知关闭充值页面
                setResult(RESULT_CANCELED);
                finish();
                break;
            case CardHelper.CARD_DOWNLOAD_FAIL_BLUETOOTH:
                // TODO: 2017/6/9  蓝牙连接失败，导致下载卡失败
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                //modified by yuanshaoyu 2017-6-13:关闭页面的切换动画
                overridePendingTransition(0, 0);
                break;
            case CardHelper.CARD_DOWNLOAD_FAIL_OCARD:
                // TODO: 2017/6/9  欧卡空间不足，导致下载卡失败
                finish();
                break;
            case CardHelper.CARD_DOWNLOAD_SUCCESS:
                // TODO: 2017/6/9  下载卡成功
                Intent intentSuccess = new Intent();
                setResult(RESULT_CANCELED, intentSuccess);
                finish();
                //modified by yuanshaoyu 2017-6-13:关闭页面的切换动画
                overridePendingTransition(0, 0);
                break;
        }
    }

    /**
     * give up
     */
    private void giveUp() {
        if (mode == CardHelper.CARD_RECHARGE_FAIL) {
            notRechargeDialog.setOnDialogClickListener(new NotRechargeDialog.OnDialogInterface() {
                @Override
                public void cancel(View view, NotRechargeDialog dialog) {
                    dialog.dismiss();
                }

                @Override
                public void ensure(View view, NotRechargeDialog dialog) {
                    dialog.dismiss();
                    showReturnAmount();
                }
            });
            notRechargeDialog.show();
        } else if (mode == CardHelper.CARD_DOWNLOAD_FAIL_BLUETOOTH) {
            //add by yuanshaoyu 2017-6-12:下载卡失败的放弃下载
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
            //modified by yuanshaoyu 2017-6-13:关闭页面的切换动画
            overridePendingTransition(0, 0);
        }
    }

    private void showReturnAmount() {
        failRechargeDialog.setOnAuthDialogButton(new FailRechargeDialog.OnAuthDialogInterface() {
            @Override
            public void onEnsure(View v, DialogInterface dialog) {
                revoke();
                dialog.dismiss();
                //通知关闭充值页面
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        failRechargeDialog.show();
    }

    /**
     * order revoke
     */
    private void revoke() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CustomRevoke customRevoke = new CustomRevoke();
                try {
                    String result = customRevoke.customRevoke(Config.OL_NUM,
                            rechargeEntity.getTn(), rechargeEntity.getRecharge_amount(),
                            Config.MERCHANT_NO, "1", Config.P_ID, Config.P_CODE);
                    Timber.tag(TAG).i("revoke order result %s", result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
        timeCount.cancel();
        if (cardHelper != null) {
            cardHelper.destroy();
            cardHelper = null;
        }

    }

    /**
     * count down 10 s
     */
    private class TimeCount extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Timber.tag(TAG).i(" millisUntilFinished second %s", millisUntilFinished / 1000);
            tv_btn.setText(String.format(getResources().getString(R.string.card_notice_ensure), millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            Timber.tag(TAG).i(" onFinish ");
            tv_btn.setText(getResources().getString(R.string.card_ensure));
            //modify by wangshuai 2017-06-14 finish activity
            finish();
        }
    }

    private void countFinish() {
        tv_btn.setText(getResources().getString(R.string.card_ensure));
        timeCount.cancel();
    }

}
