package com.vboss.okline.ui.card.notice;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardLog;
import com.vboss.okline.jpush.JPushActivity;
import com.vboss.okline.jpush.JPushEntity;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.FrescoUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/3/31 14:19 <br/>
 * Summary  : 刷卡提醒
 */

public class CardNoticeFragment extends BaseFragment implements CardNoticeContact.ICardNoticeView {
    private static final String TAG = CardNoticeFragment.class.getSimpleName();
    public static final String KEY_MODE = "mode";
    public static final String KEY_CARD = "card";
    public static final int MODE_BANK_PAY = 0x01;
    public static final int MODE_BANK_PAY_SUCCESS = 0x02;
    public static final int MODE_MEMBER_PAY = 0x03;
    public static final int MODE_MEMBER_PAY_SUCCESS = 0x04;
    public static final int MODE_MEMBER_PAY_FAILED = 0x05;
    public static final int RECHAREGE_REQUEST_CODE = 0x011;
    @BindView(R.id.tv_pay_tip)
    TextView tv_card_tip;
    @BindView(R.id.sdv_card_pay)
    SimpleDraweeView draweeView;

    @BindView(R.id.tv_card_pay_tip)
    TextView tv_card_pay_tip;
    @BindView(R.id.iv_card_logo)
    ImageView iv_card_logo;

    /**
     * 会员卡积分，余额显示
     */
    @BindView(R.id.layout_card_member)
    LinearLayout layout_card_balance;
    @BindView(R.id.tv_card_member_balance)
    TextView tv_card_balance;
    @BindView(R.id.tv_card_member_integral)
    TextView tv_card_integral;

    /**
     * 交易成功界面
     */
    @BindView(R.id.layout_card_success)
    LinearLayout layout_success;
    @BindView(R.id.tv_card_pay_money)
    TextView tv_card_pay_money;
    @BindView(R.id.tv_card_pay_discount)
    TextView tv_card_pay_discount;

    /**
     * pos ticket  layout
     */
    @BindView(R.id.layout_card_ticket)
    LinearLayout layout_card_ticket;
    @BindView(R.id.tv_card_pay_pos)
    TextView tv_card_pos;
    @BindView(R.id.tv_card_pay_ticket)
    TextView tv_card_ticket;
    @BindView(R.id.tv_card_trade)
    TextView tv_card_trade;

    /**
     * 余额不足
     */
    @BindView(R.id.layout_card_balance)
    LinearLayout layout_balance;
    @BindView(R.id.tv_card_recharge_1)
    TextView tv_recharge_1;
    @BindView(R.id.tv_card_recharge_2)
    TextView tv_recharge_2;
    @BindView(R.id.tv_card_recharge_3)
    TextView tv_recharge_3;
    @BindView(R.id.tv_card_no_recharge)
    TextView tv_no_recharge;
    @BindView(R.id.tv_card_now_recharge)
    TextView tv_now_recharge;
    @BindView(R.id.tv_card_member_no_balance)
    TextView tv_no_enough_balance;

    @BindView(R.id.iv_card_pay_close)
    ImageView layout_back;

    @BindView(R.id.tv_card_no)
    TextView tv_card_no;

    private List<TextView> textViews;
    int mode;

    private CardNoticePresenter presenter;
    private Context mContext;
    private JPushActivity jPushActivity;

    private long[] recharge_amounts = new long[]{100, 200, 300};
    private long recharge_money = 0;
    private CardEntity mCardEntity;


    public static Fragment newInstance(JPushEntity entity) {
        CardNoticeFragment instance = new CardNoticeFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_CARD, entity);
        instance.setArguments(args);
        return instance;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        jPushActivity = (JPushActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.activity_card_pay, container, false);
        ButterKnife.bind(this, convertView);
        return convertView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();

        //bundle data get
        Bundle args = getArguments();
        presenter = new CardNoticePresenter(mContext, this);
        CardNoticeHelper helper = new CardNoticeHelper();
        if (args != null) {
            JPushEntity entity = args.getParcelable(KEY_CARD);
            if (entity != null) {
                Timber.tag(TAG).i("entity %s", entity);
                mode = helper.getMode(entity);
                presenter.queryCardDetail(entity.getCardMainType(), entity.getCardId());
                requestOrder(entity);
                updateTitle();
            } else {
                jPushActivity.finish();
            }
        }
    }

    /**
     * init
     */
    private void init() {
        textViews = new ArrayList<>();
        textViews.add(tv_recharge_1);
        textViews.add(tv_recharge_2);
        textViews.add(tv_recharge_3);
        initListener();
    }

    /**
     * request card order info
     *
     * @param entity JPushEntity
     */
    private void requestOrder(JPushEntity entity) {
        if (!TextUtils.isEmpty(entity.getOrderNo())) {
            presenter.queryOrderInfo(entity.getCardMainType(), entity.getCardId(),
                    entity.getOrderNo(), entity.getPayDate(), null);
        }
    }

    /**
     * update title
     */
    private void updateTitle() {
        switch (mode) {
            case MODE_BANK_PAY:
                initBankPay();
                break;
            case MODE_BANK_PAY_SUCCESS:
                tv_card_tip.setText(jPushActivity.getResources().getString(R.string.card_pay_title_success));
                iv_card_logo.setVisibility(View.VISIBLE);
                break;
            case MODE_MEMBER_PAY:
                tv_card_tip.setText(jPushActivity.getResources().getString(R.string.card_pay_title_tip));
                break;
            case MODE_MEMBER_PAY_FAILED:
                tv_card_tip.setText(jPushActivity.getResources().getString(R.string.card_pay_title_fail));
                break;
            case MODE_MEMBER_PAY_SUCCESS:
                tv_card_tip.setText(jPushActivity.getResources().getString(R.string.card_pay_title_success));
                iv_card_logo.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     * bank pay show
     */
    private void initBankPay() {
        tv_card_tip.setText(jPushActivity.getResources().getString(R.string.card_pay_title_tip));
        tv_card_pay_tip.setVisibility(View.VISIBLE);
    }


    /**
     * init view click listener
     */
    private void initListener() {
        tv_recharge_1.setOnClickListener(onValueClickListener);
        tv_recharge_2.setOnClickListener(onValueClickListener);
        tv_recharge_3.setOnClickListener(onValueClickListener);
        tv_no_recharge.setOnClickListener(onRechargeClickListener);
        tv_now_recharge.setOnClickListener(onRechargeClickListener);
        tv_card_pos.setOnClickListener(onPosTicketClickListener);
        tv_card_ticket.setOnClickListener(onPosTicketClickListener);
        layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jPushActivity.finish();
            }
        });
    }

    /**
     * the Order Ticket Click Listener
     */
    private View.OnClickListener onPosTicketClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_card_pay_pos:

                    break;
                case R.id.tv_card_pay_ticket:

                    break;
            }
        }
    };


    /**
     * recharge value select listener
     */
    private View.OnClickListener onValueClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_card_recharge_1:
                    recharge_money = recharge_amounts[0];
                    selectRecharge(0);
                    break;
                case R.id.tv_card_recharge_2:
                    recharge_money = recharge_amounts[1];
                    selectRecharge(1);
                    break;
                case R.id.tv_card_recharge_3:
                    recharge_money = recharge_amounts[2];
                    selectRecharge(2);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * add by wangshuai 2017-04-10
     * change recharge value state
     *
     * @param index select position
     */
    private void selectRecharge(int index) {
        for (int i = 0; i < textViews.size(); i++) {
            if (index == i) {
                textViews.get(i).setTextColor(ActivityCompat.getColor(mContext, R.color.white));
                textViews.get(i).setBackgroundResource(R.drawable.shape_card_recharge_1);
            } else {
                textViews.get(i).setTextColor(ActivityCompat.getColor(mContext, R.color.black));
                textViews.get(i).setBackgroundResource(R.drawable.shape_card_pay_bg);
            }
        }
    }

    /**
     * recharge click listener
     */
    private View.OnClickListener onRechargeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_card_no_recharge:
                    jPushActivity.finish();
                    break;
                case R.id.tv_card_now_recharge:
                    if (mCardEntity == null) {
                        Utils.customToast(jPushActivity, jPushActivity.getResources()
                                .getString(R.string.card_recharge_no_card), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (recharge_money == 0){
                        Utils.customToast(jPushActivity, jPushActivity.getResources()
                                .getString(R.string.card_recharge_select_money), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    presenter.recharge("", "6", recharge_money, mCardEntity);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void updateCardDetail(CardEntity cardEntity) {
        if (cardEntity != null) {
            update(cardEntity);
        }
    }

    /**
     * update card info
     *
     * @param cardEntity CardEntity
     */
    private void update(CardEntity cardEntity) {
        Log.i(TAG, "mode : " + mode);
        this.mCardEntity = cardEntity;
        String number = cardEntity.cardNo();
        // update card image
        if (TextUtils.isEmpty(cardEntity.imgUrl())) {
            draweeView.setController(FrescoUtil.getDefaultImage(mContext, R.mipmap.image_card_default));
        } else {
            draweeView.setController(FrescoUtil.loadImage(cardEntity.imgUrl(),
                    jPushActivity.getResources().getDimensionPixelSize(R.dimen.card_pay_img_width) / 2,
                    jPushActivity.getResources().getDimensionPixelSize(R.dimen.card_pay_img_height) / 2
            ));
        }
        // show card number
        if (!TextUtils.isEmpty(number)) {
            int length = number.length();
            String temp_no = number.length() > 4 ? number.substring(length - 4, length) : number;
            String temp = String.format(jPushActivity.getResources().getString(R.string.card_cardNo), temp_no);
            SpannableString ss = new SpannableString(temp);
            ss.setSpan(new StyleSpan(Typeface.BOLD), 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_card_no.setText(ss);
            tv_card_no.setVisibility(View.VISIBLE);
        }

        //update member card balance and integral
        switch (mode) {
            case MODE_MEMBER_PAY:
                updateMemberPay(cardEntity);
                break;
            case MODE_MEMBER_PAY_FAILED:
                updateNoEnoughBalance(cardEntity);
                break;
            case MODE_MEMBER_PAY_SUCCESS:
                updateMemberBalance(cardEntity);
                break;
        }
    }


    /**
     * member card pay tip
     *
     * @param cardEntity CardEntity
     */
    private void updateMemberPay(CardEntity cardEntity) {
        layout_card_balance.setVisibility(View.VISIBLE);
        tv_card_pay_tip.setVisibility(View.VISIBLE);
        String balance = String.format(jPushActivity.getResources().getString(R.string.card_pay_rmb),
                com.vboss.okline.utils.TextUtils.formatMoney(cardEntity.balance()));
        String integral = String.valueOf(cardEntity.integral());

        /* *** member card balance ***/
        SpannableString ss = new SpannableString(balance);
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(String.format(jPushActivity.getResources().getString(R.string.card_balance), ""));
        ss.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(mContext, R.color.colorCardPayMoney)),
                0, balance.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(ss);
        tv_card_balance.setText(ssb);

        /* ** member card integral **/
        SpannableStringBuilder ssb1 = new SpannableStringBuilder();
        ssb1.append(String.format(jPushActivity.getResources().getString(R.string.card_integral), ""));
        SpannableString ss1 = new SpannableString(integral);
        ss1.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(mContext, R.color.colorCardPayMoney)),
                0, integral.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb1.append(ss1);
        tv_card_integral.setText(ssb1);

    }


    /**
     * not enough balance
     *
     * @param cardEntity CardEntity
     */
    private void updateNoEnoughBalance(CardEntity cardEntity) {
        layout_balance.setVisibility(View.VISIBLE);
        String no_balance = String.format(jPushActivity.getResources().getString(R.string.card_pay_rmb),
                com.vboss.okline.utils.TextUtils.formatMoney(cardEntity.balance()));
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(String.format(jPushActivity.getResources().getString(R.string.card_balance), ""));
        SpannableString ss = new SpannableString(no_balance);
        ss.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(mContext, R.color.colorCardPayMoney)),
                0, no_balance.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(ss);
        tv_no_enough_balance.setText(ssb);
    }

    /**
     * update member card balance integral
     *
     * @param cardEntity card
     */
    private void updateMemberBalance(CardEntity cardEntity) {
        layout_card_balance.setVisibility(View.VISIBLE);
        String balance = String.format(jPushActivity.getResources().getString(R.string.card_pay_rmb),
                com.vboss.okline.utils.TextUtils.formatMoney(cardEntity.balance()));
        String integral = String.valueOf(cardEntity.integral());
        /* *** member card balance ***/
        SpannableString ss = new SpannableString(balance);
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(String.format(jPushActivity.getResources().getString(R.string.card_balance), ""));
        ss.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(mContext, R.color.colorCardPayMoney)),
                0, balance.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(ss);
        tv_card_balance.setText(ssb);

        /* ** member card integral **/
        SpannableStringBuilder ssb1 = new SpannableStringBuilder();
        ssb1.append(String.format(jPushActivity.getResources().getString(R.string.card_integral), ""));
        SpannableString ss1 = new SpannableString(integral);
        ss1.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(mContext, R.color.colorCardPayMoney)),
                0, integral.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb1.append(ss1);
        tv_card_integral.setText(ssb1);

    }


    @Override
    public void updateTradeInfo(CardLog cardLog) {
        if (cardLog != null) {
            switch (mode) {
                case MODE_BANK_PAY_SUCCESS:
                    updateBankPaySuccess(cardLog);
                    break;
                case MODE_MEMBER_PAY_SUCCESS:
                    updateMemberPaySuccess(cardLog);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * member card pay success
     *
     * @param cardEntity CardLog
     */
    private void updateMemberPaySuccess(CardLog cardEntity) {
        layout_success.setVisibility(View.VISIBLE);

        String pay_money = String.format(jPushActivity.getResources().getString(R.string.card_pay_rmb),
                com.vboss.okline.utils.TextUtils.formatMoney(Math.abs(cardEntity.amount())));
        String pay_discount = String.format(jPushActivity.getResources().getString(R.string.card_pay_rmb),
                com.vboss.okline.utils.TextUtils.formatMoney(Math.abs(0)));

        /* ****  余额支付 ******/
        SpannableStringBuilder ssb2 = new SpannableStringBuilder();
        ssb2.append(String.format(jPushActivity.getResources().getString(R.string.card_pay_balance), ""));
        SpannableString ss2 = new SpannableString(pay_money);
        ss2.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(mContext, R.color.colorCardPayTip)),
                0, pay_money.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss2.setSpan(new AbsoluteSizeSpan(50), 0, pay_money.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb2.append(ss2);
        tv_card_pay_money.setText(ssb2);

        /* ***  积分抵扣 ******/
        SpannableStringBuilder ssb3 = new SpannableStringBuilder();
        ssb3.append(String.format(jPushActivity.getResources().getString(R.string.card_pay_discount), ""));
        SpannableString ss3 = new SpannableString(pay_discount);
        ss3.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(mContext, R.color.colorCardPayTip)),
                0, pay_discount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss3.setSpan(new AbsoluteSizeSpan(50), 0, pay_discount.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb3.append(ss3);
        tv_card_pay_discount.setText(ssb3);

        /*  *** POS单 小票显示  * *****/
        if (TextUtils.isEmpty(cardEntity.receiptUrl()) || TextUtils.isEmpty(cardEntity.slipUrl())) {
            layout_card_ticket.setVisibility(View.GONE);
        } else {
            layout_card_ticket.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(cardEntity.receiptUrl())) {
                tv_card_ticket.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(cardEntity.slipUrl())) {
                tv_card_pos.setVisibility(View.VISIBLE);
            }
        }

    }

    /**
     * bank pay success
     *
     * @param cardEntity CardEntity
     */
    private void updateBankPaySuccess(CardLog cardEntity) {
        layout_success.setVisibility(View.VISIBLE);
        tv_card_pay_discount.setVisibility(View.GONE);
        tv_card_pay_money.setVisibility(View.GONE);
        String money = String.format(jPushActivity.getResources().getString(R.string.card_pay_rmb),
                com.vboss.okline.utils.TextUtils.formatMoney(Math.abs(cardEntity.amount())));
        String source = String.format(jPushActivity.getResources().getString(R.string.card_pay_bank_money), "");
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        SpannableString ss = new SpannableString(money);
        ssb.append(source);
        ss.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(mContext, R.color.colorCardPayTip)),
                0, money.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(50), 0, money.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(ss);
        tv_card_trade.setText(ssb);

        /*  *** POS单 小票显示  * *****/
        if (TextUtils.isEmpty(cardEntity.receiptUrl()) || TextUtils.isEmpty(cardEntity.slipUrl())) {
            layout_card_ticket.setVisibility(View.GONE);
        } else {
            layout_card_ticket.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(cardEntity.receiptUrl())) {
                tv_card_ticket.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(cardEntity.slipUrl())) {
                tv_card_pos.setVisibility(View.VISIBLE);
            }
        }
    }

}
