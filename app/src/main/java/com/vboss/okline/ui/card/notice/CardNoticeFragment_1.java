package com.vboss.okline.ui.card.notice;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.data.entities.CardCondition;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardLog;
import com.vboss.okline.jpush.JPushActivity;
import com.vboss.okline.jpush.JPushEntity;
import com.vboss.okline.ui.card.CardConstant;
import com.vboss.okline.ui.card.recharge.CardRechargeActivity;
import com.vboss.okline.ui.card.ticket.CardTicketActivity;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.DensityUtil;
import com.vboss.okline.view.widget.shadow.ShadowProperty;
import com.vboss.okline.view.widget.shadow.ShadowViewHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/5/13 16:41 <br/>
 * Summary  : 在这里描述Class的主要功能
 */

public class CardNoticeFragment_1 extends BaseFragment implements CardNoticeContact.ICardNoticeView,
        CardNoticeContact.IQuickPassView {
    private static final String TAG = CardNoticeFragment_1.class.getSimpleName();
    public static final String KEY_MODE = "mode";
    public static final String KEY_CARD = "card";
    public static final int MODE_BANK_PAY = 0x01;
    public static final int MODE_BANK_PAY_SUCCESS = 0x02;
    public static final int MODE_BANK_PAY_FAILED = 0x06;
    public static final int MODE_MEMBER_PAY_SUCCESS = 0x04;
    public static final int MODE_MEMBER_PAY_FAILED = 0x05;
    public static final int MODE_BUS_PAY_SUCCESS = 0x07;
    public static final int MODE_BUS_PAY_FAILED = 0x08;

    public static final int RECHARGE_REQUEST_CODE = 0x011;


    @BindView(R.id.tv_amount)
    TextView tv_amount;     // pay amount
    @BindView(R.id.tv_discount)
    TextView tv_discount;    //pay integral discount
    @BindView(R.id.iv_card_notice_state)
    ImageView iv_state_logo;   //pay state logo
    @BindView(R.id.simple_card_image)
    SimpleDraweeView simple_card_image;  // card image
    @BindView(R.id.tv_bus_card_balance)
    TextView tv_bus_card_balance;   // bus card balance

    @BindView(R.id.layout_print)
    RelativeLayout layout_print;   //print layout
    @BindView(R.id.ib_print_pos)
    ImageButton ib_print_pos;   // pos order
    @BindView(R.id.ib_print_ticket)
    ImageButton ib_print_ticket;   // ticket
    @BindView(R.id.ib_print_invoice)
    ImageButton ib_print_invoice;  // invoice

    @BindView(R.id.layout_no_enough_layout)
    LinearLayout layout_no_enough_balance;   // card no enough balance

    @BindView(R.id.layout_member_pay_fail)
    LinearLayout layout_member_pay_fail;   // member card pay fail
    @BindView(R.id.tv_card_member_balance)
    TextView tv_card_member_balance;    //member card balance
    @BindView(R.id.tv_card_member_integral)
    TextView tv_card_member_integral;   //member card integral

    @BindView(R.id.tv_exit)
    TextView tv_exit;

    private CardLog mCardLog;
    private CardEntity mCardEntity;
    private int mode;

    private JPushActivity act;
    private CardNoticePresenter presenter;
    private JPushEntity entity;
    private TimeCount timeCount;


    public static Fragment newInstance(JPushEntity entity) {
        CardNoticeFragment_1 instance = new CardNoticeFragment_1();
        Bundle args = new Bundle();
        args.putParcelable(KEY_CARD, entity);
        instance.setArguments(args);
        Timber.tag(TAG).i("newInstance jpush content %s", entity.toString());
        return instance;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        act = (JPushActivity) getActivity();
        presenter = new CardNoticePresenter(context, this, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_card_notice, container, false);
        ButterKnife.bind(this, convertView);
        return convertView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        timeCount = new TimeCount(10 * 1000, 1000);
        Bundle args = getArguments();
        if (args == null) {
            Timber.tag(TAG).w("bundle is null ");
            return;
        }
        entity = args.getParcelable(KEY_CARD);
        if (entity == null) {
            Timber.tag(TAG).w("jpush entity is null");
            return;
        }
        CardNoticeHelper helper = new CardNoticeHelper();
        mode = helper.getPayMode(entity);
        Timber.tag(TAG).i("pay state : %s", mode);
        updateLogo();
        // request card detail info
        presenter.queryCardDetail(entity.getCardMainType(), entity.getCardId());

        //request pay order detail info

        ShadowViewHelper.bindShadowHelper(
                new ShadowProperty()
                        .setShadowColor(ActivityCompat.getColor(act, R.color.colorShadow))
                        .setShadowDy(DensityUtil.dip2px(act, 1f))
                        .setShadowRadius(DensityUtil.dip2px(act, 1f)),
                simple_card_image,
                DensityUtil.dip2px(act, 6.5f),
                DensityUtil.dip2px(act, 6.5f));
    }

    /**
     * update pay logo
     */
    private void updateLogo() {
        if (mode == MODE_BANK_PAY_FAILED || mode == MODE_BUS_PAY_FAILED || mode == MODE_MEMBER_PAY_FAILED) {
            iv_state_logo.setImageResource(R.mipmap.ic_card_notice_fail);
        } else {
            iv_state_logo.setImageResource(R.mipmap.ic_card_notice_success);
        }
        showView();
    }

    private void showView() {
        switch (mode) {
            case MODE_BANK_PAY:
                tv_amount.setVisibility(View.VISIBLE);
                tv_discount.setVisibility(View.GONE);
                iv_state_logo.setVisibility(View.INVISIBLE);
                tv_bus_card_balance.setVisibility(View.INVISIBLE);
                layout_print.setVisibility(View.GONE);
                layout_member_pay_fail.setVisibility(View.GONE);
                layout_no_enough_balance.setVisibility(View.GONE);
                tv_exit.setVisibility(View.VISIBLE);
                break;
            case MODE_BANK_PAY_SUCCESS:
                tv_amount.setVisibility(View.VISIBLE);
                iv_state_logo.setVisibility(View.VISIBLE);
                tv_discount.setVisibility(View.GONE);
                tv_bus_card_balance.setVisibility(View.INVISIBLE);
                layout_print.setVisibility(View.VISIBLE);
                layout_member_pay_fail.setVisibility(View.GONE);
                layout_no_enough_balance.setVisibility(View.GONE);
                tv_exit.setVisibility(View.VISIBLE);
                break;
            case MODE_BANK_PAY_FAILED:
                tv_discount.setVisibility(View.GONE);
                tv_amount.setVisibility(View.INVISIBLE);
                iv_state_logo.setVisibility(View.VISIBLE);
                tv_bus_card_balance.setVisibility(View.GONE);
                layout_print.setVisibility(View.GONE);
                layout_member_pay_fail.setVisibility(View.GONE);
                layout_no_enough_balance.setVisibility(View.GONE);
                tv_exit.setVisibility(View.VISIBLE);
                break;
            case MODE_BUS_PAY_SUCCESS:
                tv_amount.setVisibility(View.VISIBLE);
                tv_discount.setVisibility(View.GONE);
                iv_state_logo.setVisibility(View.VISIBLE);
                tv_bus_card_balance.setVisibility(View.VISIBLE);
                layout_print.setVisibility(View.VISIBLE);
                layout_member_pay_fail.setVisibility(View.GONE);
                layout_no_enough_balance.setVisibility(View.GONE);
                tv_exit.setVisibility(View.VISIBLE);
                break;
            case MODE_BUS_PAY_FAILED:
                tv_amount.setVisibility(View.INVISIBLE);
                tv_discount.setVisibility(View.GONE);
                iv_state_logo.setVisibility(View.VISIBLE);
                tv_bus_card_balance.setVisibility(View.VISIBLE);
                layout_no_enough_balance.setVisibility(View.VISIBLE);
                layout_member_pay_fail.setVisibility(View.GONE);
                layout_print.setVisibility(View.GONE);
                tv_exit.setVisibility(View.GONE);
                break;
            case MODE_MEMBER_PAY_SUCCESS:
                tv_amount.setVisibility(View.VISIBLE);
                iv_state_logo.setVisibility(View.VISIBLE);
                tv_discount.setVisibility(View.VISIBLE);
                tv_bus_card_balance.setVisibility(View.GONE);
                layout_no_enough_balance.setVisibility(View.GONE);
                layout_member_pay_fail.setVisibility(View.GONE);
                layout_print.setVisibility(View.VISIBLE);
                tv_exit.setVisibility(View.VISIBLE);
                break;
            case MODE_MEMBER_PAY_FAILED:
                tv_amount.setVisibility(View.INVISIBLE);
                iv_state_logo.setVisibility(View.VISIBLE);
                tv_discount.setVisibility(View.GONE);
                tv_bus_card_balance.setVisibility(View.GONE);
                layout_no_enough_balance.setVisibility(View.GONE);
                layout_member_pay_fail.setVisibility(View.VISIBLE);
                layout_print.setVisibility(View.GONE);
                tv_exit.setVisibility(View.VISIBLE);
                break;
            default:
                act.finish();
                break;
        }
    }

    /**
     * the method pos or ticket or invoice print
     *
     * @param v {@link android.widget.ImageButton}
     */
    @OnClick({R.id.ib_print_invoice, R.id.ib_print_pos, R.id.ib_print_ticket})
    public void onPrint(View v) {
        if (entity == null) {
            Timber.tag(TAG).w("card log si null");
            return;
        }
        int position = 0;
        String url = "";
        switch (v.getId()) {
            case R.id.ib_print_invoice:
                position = 2;
                url = entity.getInvoiceUrl();
                break;
            case R.id.ib_print_pos:
                position = 0;
                url = entity.getSlipUrl();
                break;
            case R.id.ib_print_ticket:
                position = 1;
                url = entity.getReceiptUrl();
                break;
            default:
                break;
        }
        if (TextUtils.isEmpty(url)) {
            Timber.tag(TAG).w("url is null");
            return;
        }
        Intent intent = new Intent(act, CardTicketActivity.class);
        intent.putExtra(CardTicketActivity.KEY_ACTION_MODE, position);
        intent.putExtra(CardTicketActivity.KEY_ACTION_URL, url);
        act.startActivity(intent);
    }

    /**
     * the method card no enough and recharge card
     *
     * @param v {@link TextView}
     */
    @OnClick({R.id.tv_card_no_recharge, R.id.tv_card_now_recharge})
    public void onRecharge(View v) {
        switch (v.getId()) {
            case R.id.tv_card_no_recharge:
                act.finish();
                break;
            case R.id.tv_card_now_recharge:
                recharge();
                break;
            default:
                break;
        }
    }

    /**
     * card recharge
     */
    private void recharge() {
        presenter.queryQuickPassCards();
    }

    /****
     * click card image finish
     * @param v {@link SimpleDraweeView}
     */
    @OnClick(R.id.simple_card_image)
    public void onCardImage(View v) {
        act.finish();
    }

    @Override
    public void updateCardDetail(CardEntity cardEntity) {
        timeCount.start();
        if (cardEntity == null) {
            return;
        }
        this.mCardEntity = cardEntity;
        Timber.tag(TAG).i("card detail info %s", cardEntity.toString());
        if (!TextUtils.isEmpty(cardEntity.imgUrl())) {
            simple_card_image.setImageURI(Uri.parse(cardEntity.imgUrl()));
        }
        if (TextUtils.isEmpty(entity.getOrderNo())) {
            tv_amount.setText(act.getResources().getString(R.string.card_pay_title_tip));
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(act.getResources().getString(R.string.card_rmb));
        sb.append(com.vboss.okline.utils.TextUtils.formatMoney(cardEntity.balance()));
        String balanceStr = String.format(act.getResources().getString(R.string.card_balance), sb);
        SpannableString ss = new SpannableString(balanceStr);
        ss.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(act, R.color.colorCardNotice)),
                balanceStr.lastIndexOf(act.getResources().getString(R.string.card_rmb)), balanceStr.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_bus_card_balance.setText(ss);
        tv_card_member_balance.setText(ss);

        String integralStr = String.format(act.getResources().getString(R.string.card_integral), cardEntity.integral());
        SpannableString ss1 = new SpannableString(integralStr);
        ss1.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(act, R.color.colorCardNotice)),
                integralStr.length() - String.valueOf(cardEntity.integral()).length(), integralStr.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_card_member_integral.setText(ss1);

        //amount
        StringBuilder sb1 = new StringBuilder();
        sb1.append(act.getResources().getString(R.string.card_rmb));
        sb1.append(com.vboss.okline.utils.TextUtils.formatMoney(Math.abs(entity.getAmount())));
        tv_amount.setText(sb1);

        //discount integral
        Timber.tag(TAG).i("integral %s", entity.getIntegral());
        StringBuilder builder = new StringBuilder();
        builder.append(String.valueOf(entity.getIntegral())).append("(");
        builder.append(com.vboss.okline.utils.TextUtils.formatMoney(entity.getIntegral())).append(")");
        tv_discount.setText(builder);
    }

    @Override
    public void updateTradeInfo(CardLog cardLog) {
        Timber.tag(TAG).i("card log %s", cardLog);
        this.mCardLog = cardLog;
    }

    @Override
    public void showQuickPassCard(ArrayList<CardEntity> quickPass) {
        List<Integer> amountList = new ArrayList<>();
        amountList.add(5000);
        amountList.add(10000);
        amountList.add(15000);
        amountList.add(20000);
        amountList.add(30000);
        amountList.add(50000);
        CardCondition cardCondition = CardCondition.newBuilder()
                .amountList(amountList).build();
        if (quickPass.size() == 0) {
            Utils.customToast(act, act.getResources().getString(R.string.card_recharge_tip), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(getContext(), CardRechargeActivity.class);
        intent.putExtra(CardConstant.CARD_INSTANCE, mCardEntity);
        intent.putExtra(CardConstant.BANK_CARDS, quickPass);
        intent.putExtra(CardConstant.CARD_CONDITION, cardCondition);
        startActivityForResult(intent, RECHARGE_REQUEST_CODE);
    }

    @Override
    public void onStop() {
        super.onStop();
        Timber.tag(TAG).i(" onStop ");
        timeCount.cancel();
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

        }

        @Override
        public void onFinish() {
            act.finish();
        }
    }
}
