package com.okline.vboss.assistant.ui.notice;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.okline.vboss.assistant.R;
import com.okline.vboss.assistant.R2;
import com.okline.vboss.assistant.net.CardEntity;
import com.okline.vboss.assistant.net.RechargeEntity;
import com.okline.vboss.assistant.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/6/13 9:54 <br/>
 * Summary  : 在这里描述Class的主要功能
 */

public class DetailFragment extends DialogFragment {
    private static final String TAG = DetailFragment.class.getSimpleName();
    @BindView(R2.id.tv_title)
    TextView tv_title;
    @BindView(R2.id.tv_row_1)
    TextView tv_row_1;
    @BindView(R2.id.tv_row_2)
    TextView tv_row_2;
    @BindView(R2.id.tv_row_3)
    TextView tv_row_3;
    @BindView(R2.id.tv_row_4)
    TextView tv_row_4;
    @BindView(R2.id.tv_row_5)
    TextView tv_row_5;
    @BindView(R2.id.tv_row_6)
    TextView tv_row_6;
    @BindView(R2.id.tv_row_7)
    TextView tv_row_7;

    private int mode;
    private RechargeEntity rechargeEntity;
    private CardEntity cardEntity;

    public static DetailFragment newInstance(int mode) {
        DetailFragment instance = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(CardHelper.KEY_OPERATE, mode);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag(TAG).i(" onCreate ");
        /*Bundle args = getArguments();
        if (args != null) {
            mode = args.getInt(CardHelper.KEY_OPERATE, CardHelper.CARD_INFO);
        }*/
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Timber.tag(TAG).i(" onCreateDialog ");
        View convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_detail_assistant, null);
        ButterKnife.bind(this, convertView);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.style_dialog);
        builder.setView(convertView);
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.tag(TAG).i(" onResume ");
        Timber.tag(TAG).i("mode %s", mode);
        initView();
    }

    private void initView() {
        try {
            resetView();
            switch (mode) {
                case CardHelper.CARD_INFO:
                    showCardDetail();
                    break;
                case CardHelper.RECHARGE_INFO:
                    showCardRechargeDetail();
                    break;
                case CardHelper.RECHARGE_DISCOUNT_ACTIVE:
                    showRechargeDiscountActive();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showRechargeDiscountActive() {
        tv_row_1.setVisibility(View.VISIBLE);
        tv_row_2.setVisibility(View.VISIBLE);

        String activeDate = "2017年12月31日";
        String activeDetail = "充100送10、充200送20、充300送30";

        tv_title.setText(getResources().getString(R.string.card_notice_discount_active));

        tv_row_1.setTextColor(ActivityCompat.getColor(getContext(), R.color.color_gray_7f));
        tv_row_1.setText(activeDetail);

        String recharge_discount_date = String.format(getResources().getString(R.string.recharge_discount_date), activeDate);
        SpannableString ss1 = new SpannableString(recharge_discount_date);
        ss1.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(getContext(), R.color.color_gray_7f)),
                recharge_discount_date.indexOf(getResources().getString(R.string.recharge_index)) + 1,
                recharge_discount_date.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_row_2.setText(ss1);
    }

    private void showCardRechargeDetail() {
        tv_row_1.setVisibility(View.VISIBLE);
        tv_row_2.setVisibility(View.VISIBLE);
        tv_row_3.setVisibility(View.VISIBLE);
        tv_row_4.setVisibility(View.VISIBLE);
        tv_row_5.setVisibility(View.VISIBLE);
        tv_row_6.setVisibility(View.VISIBLE);
        tv_row_7.setVisibility(View.VISIBLE);

        if (rechargeEntity == null) {
            Timber.tag(TAG).w("rechargeEntity is null");
            dismiss();
            return;
        }

        //modify by wangshuai 2017-06-15 print cardEntity info
        Timber.tag(TAG).i("rechargeEntity %s", rechargeEntity.toString());

        String rechargeCard = rechargeEntity.getRecharge_card_name();
        String rechargeAmount = StringUtils.formatMoney(rechargeEntity.getRecharge_amount());
        String rechargeGiveAmount = StringUtils.formatMoney(rechargeEntity.getGive_amount());
        String rechargeRealAmount = StringUtils.formatMoney(rechargeEntity.getRecharge_real_amount());
        String rechargePayment = rechargeEntity.getRecharge_payment();
        String rechargePaymentAmount = StringUtils.formatMoney(rechargeEntity.getRecharge_pay_amount());
        String rechargeDate = "";

        tv_title.setText(getResources().getString(R.string.card_notice_recharge_info));

        String recharge_card = String.format(getResources().getString(R.string.recharge_card), rechargeCard);
        SpannableString ss1 = new SpannableString(recharge_card);
        ss1.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(getContext(), R.color.color_gray_7f)),
                recharge_card.indexOf(getResources().getString(R.string.recharge_index)) + 1,
                recharge_card.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_row_1.setText(ss1);

        String recharge_amount = String.format(getResources().getString(R.string.recharge_amount), rechargeAmount);
        SpannableString ss2 = new SpannableString(recharge_amount);
        ss2.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(getContext(), R.color.color_gray_7f)),
                recharge_amount.indexOf(getResources().getString(R.string.recharge_index)) + 1,
                recharge_amount.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_row_2.setText(ss2);

        String recharge_give = String.format(getResources().getString(R.string.recharge_give_amount), rechargeGiveAmount);
        SpannableString ss3 = new SpannableString(recharge_give);
        ss3.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(getContext(), R.color.color_gray_7f)),
                recharge_give.indexOf(getResources().getString(R.string.recharge_index)) + 1,
                recharge_give.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_row_3.setText(ss3);

        String recharge_real = String.format(getResources().getString(R.string.recharge_real_amount), rechargeRealAmount);
        SpannableString ss4 = new SpannableString(recharge_real);
        ss4.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(getContext(), R.color.color_red)),
                recharge_real.indexOf(getResources().getString(R.string.recharge_index)) + 1,
                recharge_real.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_row_4.setText(ss4);

        String recharge_payment = String.format(getResources().getString(R.string.recharge_payment), rechargePayment);
        SpannableString ss5 = new SpannableString(recharge_payment);
        ss5.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(getContext(), R.color.color_gray_7f)),
                recharge_payment.indexOf(getResources().getString(R.string.recharge_index)) + 1,
                recharge_payment.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_row_5.setText(ss5);

        String recharge_pay_amount = String.format(getResources().getString(R.string.recharge_payment_amount), rechargePaymentAmount);
        SpannableString ss6 = new SpannableString(recharge_pay_amount);
        ss6.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(getContext(), R.color.color_gray_7f)),
                recharge_pay_amount.indexOf(getResources().getString(R.string.recharge_index)) + 1,
                recharge_pay_amount.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_row_6.setText(ss6);

        //modify by wangshuai 2017-06-15 date format
        try {
            String pattern_1 = "yyyy-MM-dd HH:mm:ss";
            String pattern_2 = "yyyy/MM/dd HH:mm";
            Date date = new SimpleDateFormat(pattern_1, Locale.getDefault()).parse(rechargeEntity.getRecharge_date());
            rechargeDate = new SimpleDateFormat(pattern_2, Locale.getDefault()).format(date);
        } catch (Exception e) {
            e.printStackTrace();
            rechargeDate = rechargeEntity.getRecharge_date();
        } finally {
            String recharge_date = String.format(getResources().getString(R.string.recharge_date), rechargeDate);
            SpannableString ss7 = new SpannableString(recharge_date);
            ss7.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(getContext(), R.color.color_gray_7f)),
                    recharge_date.indexOf(getResources().getString(R.string.recharge_index)) + 1,
                    recharge_date.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_row_7.setText(ss7);
        }


    }


    private void showCardDetail() {
        tv_row_1.setVisibility(View.VISIBLE);
        tv_row_2.setVisibility(View.VISIBLE);
        //modify by wangshuai 2017-06-14 card valid not show
        tv_row_3.setVisibility(View.GONE);
        tv_row_4.setVisibility(View.VISIBLE);

        if (cardEntity == null) {
            Timber.tag(TAG).w("cardEntity is null");
            dismiss();
            return;
        }

        //modify by wangshuai 2017-06-15 print cardEntity info
        Timber.tag(TAG).i("cardEntity %s", cardEntity.toString());

        String cardName = cardEntity.getCardName();
        String cardNo = cardEntity.getCardNo();
        String cardValid = "2017年12月31日";
        String cardOrganization = cardEntity.getMerName();

        tv_title.setText(getResources().getString(R.string.card_notice_card_info));

        String card_name = String.format(getResources().getString(R.string.card_name_1), cardName);
        SpannableString ss1 = new SpannableString(card_name);
        ss1.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(getContext(), R.color.color_gray_7f)),
                card_name.indexOf(getResources().getString(R.string.recharge_index)) + 1,
                card_name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_row_1.setText(ss1);

        String card_no = String.format(getResources().getString(R.string.card_number_1), cardNo);
        SpannableString ss2 = new SpannableString(card_no);
        ss2.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(getContext(), R.color.color_gray_7f)),
                card_no.indexOf(getResources().getString(R.string.recharge_index)) + 1,
                card_no.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_row_2.setText(ss2);

        String card_valid = String.format(getResources().getString(R.string.card_valid), cardValid);
        SpannableString ss3 = new SpannableString(card_valid);
        ss3.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(getContext(), R.color.color_gray_7f)),
                card_valid.indexOf(getResources().getString(R.string.recharge_index)) + 1,
                card_valid.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_row_3.setText(ss3);

        String card_organization = String.format(getResources().getString(R.string.card_organization), cardOrganization);
        SpannableString ss4 = new SpannableString(card_organization);
        ss4.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(getContext(), R.color.color_gray_7f)),
                card_organization.indexOf(getResources().getString(R.string.recharge_index)) + 1,
                card_organization.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_row_4.setText(ss4);
    }

    private void resetView() {
        tv_row_1.setVisibility(View.GONE);
        tv_row_2.setVisibility(View.GONE);
        tv_row_3.setVisibility(View.GONE);
        tv_row_4.setVisibility(View.GONE);
        tv_row_5.setVisibility(View.GONE);
        tv_row_6.setVisibility(View.GONE);
        tv_row_7.setVisibility(View.GONE);
    }

    @OnClick(R2.id.ib_close)
    public void onClose(View v) {
        dismiss();
    }

    public void setMode(int mode) {
        Timber.tag(TAG).i("mode %s", mode);
        this.mode = mode;
    }

    public void setCardEntity(CardEntity cardEntity) {
        this.cardEntity = cardEntity;
    }

    public void setRechargeEntity(RechargeEntity rechargeEntity) {
        this.rechargeEntity = rechargeEntity;
    }
}
