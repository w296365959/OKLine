package com.vboss.okline.ui.card.notice;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.okline.vboss.assistant.base.Config;
import com.okline.vboss.assistant.ui.recharge.CardRechargeActivity;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardLog;
import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.jpush.JPushEntity;
import com.vboss.okline.ui.card.ticket.CardTicketActivity;
import com.vboss.okline.utils.DensityUtil;
import com.vboss.okline.utils.Formater;
import com.vboss.okline.view.widget.shadow.ShadowProperty;
import com.vboss.okline.view.widget.shadow.ShadowViewHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static com.vboss.okline.ui.card.notice.CardNoticeHelper.RECHARGE_REQUEST_CODE;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/6/13 17:28 <br/>
 * Summary  : 在这里描述Class的主要功能
 */
public class CardNoticeFragment_2 extends Fragment implements CardNoticeContact.ICardNoticeView {
    private static final String TAG = CardNoticeFragment_2.class.getSimpleName();

    private static final int COUNT = 10;

    /**
     * The Layout amount.
     */
// layout amount
    @BindView(R.id.layout_amount)
    LinearLayout layout_amount;
    /**
     * The Tv amount.
     */
    @BindView(R.id.tv_amount)
    TextView tv_amount;
    /**
     * The Tv balance.
     */
    @BindView(R.id.tv_balance)
    TextView tv_balance;

    /**
     * The Layout vip amount.
     */
//layout vip card amount
    @BindView(R.id.layout_vip_amount)
    LinearLayout layout_vip_amount;
    /**
     * The Tv vip consume amount.
     */
    @BindView(R.id.tv_vip_amount)
    TextView tv_vip_consume_amount;
    /**
     * The Tv vip consume integral.
     */
    @BindView(R.id.tv_vip_integral)
    TextView tv_vip_consume_integral;
    /**
     * The Tv vip balance.
     */
    @BindView(R.id.tv_vip_balance)
    TextView tv_vip_balance;
    /**
     * The Tv vip integral.
     */
    @BindView(R.id.tv_vip_remainder_integral)
    TextView tv_vip_integral;

    /**
     * The Simple card image.
     */
    @BindView(R.id.simple_card_image)
    SimpleDraweeView simple_card_image;
    /**
     * The Iv card notice state.
     */
    @BindView(R.id.iv_card_notice_state)
    ImageView iv_card_notice_state;

    /**
     * The Layout print.
     */
//print layout
    @BindView(R.id.layout_print)
    LinearLayout layout_print;
    /**
     * The Ib print invoice.
     */
    @BindView(R.id.ib_print_invoice)
    ImageButton ib_print_invoice;
    /**
     * The Ib print ticket.
     */
    @BindView(R.id.ib_print_ticket)
    ImageButton ib_print_ticket;
    /**
     * The Ib print pos.
     */
    @BindView(R.id.ib_print_pos)
    ImageButton ib_print_pos;

    /**
     * The Ib voice.
     */
    @BindView(R.id.ib_voice)
    ImageButton ib_voice;

    /**
     * The Tv give up.
     */
    @BindView(R.id.tv_give_up)
    TextView tv_give_up;
    /**
     * The Tv ensure.
     */
    @BindView(R.id.tv_ensure)
    TextView tv_ensure;

    /**
     * The Tv no balance.
     */
    @BindView(R.id.tv_no_balance)
    TextView tv_no_balance;

    private int mode;
    private JPushEntity jPushEntity;
    private CardEntity mCardEntity;
    private CardNoticePresenter presenter;
    private TimeCount timeCount;
    private MediaPlayer mediaPlayer;

    /**
     * New instance card notice fragment 2.
     *
     * @param entity the entity
     * @return the card notice fragment 2
     */
    public static CardNoticeFragment_2 newInstance(JPushEntity entity) {
        CardNoticeFragment_2 instance = new CardNoticeFragment_2();
        Bundle args = new Bundle();
        args.putParcelable(CardNoticeHelper.KEY_CARD, entity);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        presenter = new CardNoticePresenter(context, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_card_notice_1, container, false);
        ButterKnife.bind(this, convertView);
        return convertView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args == null) {
            Timber.tag(TAG).e("bundle args is null");
            getActivity().finish();
            return;
        }
        //get jpush message form bundle
        jPushEntity = args.getParcelable(CardNoticeHelper.KEY_CARD);
        if (jPushEntity == null) {
            Timber.tag(TAG).e("jpushEntity is null ");
            getActivity().finish();
        }
        initMediaPlayer();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //get payment mode
        mode = getMode(jPushEntity);
        Timber.tag(TAG).i("mode %s *****  jpushEntity %s", mode, jPushEntity.toString());
        //query card info detail
        presenter.queryCardDetail(jPushEntity.getCardMainType(), jPushEntity.getCardId());

        showView();
    }

    /**
     * init media
     */
    private void initMediaPlayer() {
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.beep);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setVolume(1.0f, 1.0f);
    }

    private int getMode(JPushEntity jPushEntity) {
        Timber.tag(TAG).i("errorCode %s", jPushEntity.getErrorCode());
        if (jPushEntity.getErrorCode() == 0) {
            //consume success
            if (jPushEntity.getCardMainType() == CardType.VIP_CARD) {
                Timber.tag(TAG).i("VIP card consume is success");
                return CardNoticeHelper.MODE_MEMBER_PAY_SUCCESS;
            }
            if (jPushEntity.getCardMainType() == CardType.BANK_CARD) {
                Timber.tag(TAG).i("bank card consume is success");
                return CardNoticeHelper.MODE_BANK_PAY_SUCCESS;
            }
            Timber.tag(TAG).i("common or trans card consume is success");
            return CardNoticeHelper.MODE_BUS_PAY_SUCCESS;
        }
        if (jPushEntity.getErrorCode() == CardNoticeHelper.CARD_NO_BALANCE) {
            Timber.tag(TAG).i("card payment balance no enough ");
            return CardNoticeHelper.MODE_BALANCE_FAILED;
        }
        return CardNoticeHelper.MODE_BANK_PAY;
    }

    private void showView() {
        timeCount = new TimeCount(1000 * COUNT, 1000);
        resetView();
        switch (mode) {
            case CardNoticeHelper.MODE_BANK_PAY:
                useCurrentCard();
                break;
            case CardNoticeHelper.MODE_BANK_PAY_FAILED:
                // bank card payment failed
                break;
            case CardNoticeHelper.MODE_BANK_PAY_SUCCESS:
                // bank card payment success
                showBankCardPaymentSuccess();
                break;
            case CardNoticeHelper.MODE_BUS_PAY_FAILED:
                // common card payment failed
                break;
            case CardNoticeHelper.MODE_BUS_PAY_SUCCESS:
                // common card payment success
                showCommonCardSuccess();
                break;
            case CardNoticeHelper.MODE_MEMBER_PAY_FAILED:
                // vip card payment failed
                break;
            case CardNoticeHelper.MODE_MEMBER_PAY_SUCCESS:
                // vip card payment success
                showVipPaymentSuccess();
                break;
            case CardNoticeHelper.MODE_BALANCE_FAILED:
                // card payment failed,because card no enough balance
                showCardNoEnoughBalance();
                break;
        }
    }

    private void useCurrentCard() {
        layout_amount.setVisibility(View.VISIBLE);
        tv_balance.setVisibility(View.INVISIBLE);
        iv_card_notice_state.setVisibility(View.INVISIBLE);
        tv_ensure.setText(getResources().getString(R.string.notify_ensure_1));
    }

    /**
     * bank card payment success
     */
    private void showBankCardPaymentSuccess() {
        layout_amount.setVisibility(View.VISIBLE);
        tv_balance.setVisibility(View.INVISIBLE);
        iv_card_notice_state.setImageResource(R.mipmap.ic_card_notice_success_1);
        layout_print.setVisibility(View.VISIBLE);
        ib_voice.setVisibility(View.VISIBLE);
        tv_ensure.setText(String.format(getResources().getString(R.string.notify_ensure), COUNT));
        timeCount.start();
    }

    /**
     * common card payment success
     */
    private void showCommonCardSuccess() {
        layout_amount.setVisibility(View.VISIBLE);
        ib_voice.setVisibility(View.VISIBLE);
        iv_card_notice_state.setImageResource(R.mipmap.ic_card_notice_success_1);
        tv_ensure.setText(String.format(getResources().getString(R.string.notify_ensure), COUNT));
        timeCount.start();
    }

    /**
     * vip card payment success
     */
    private void showVipPaymentSuccess() {
        layout_vip_amount.setVisibility(View.VISIBLE);
        layout_print.setVisibility(View.VISIBLE);
        ib_voice.setVisibility(View.VISIBLE);
        iv_card_notice_state.setImageResource(R.mipmap.ic_card_notice_success_1);
        tv_ensure.setText(String.format(getResources().getString(R.string.notify_ensure), COUNT));
        timeCount.start();
    }

    /**
     * card balance no enough
     */
    private void showCardNoEnoughBalance() {
        layout_amount.setVisibility(View.VISIBLE);
        tv_amount.setVisibility(View.INVISIBLE);
        iv_card_notice_state.setImageResource(R.mipmap.ic_card_notice_fail_1);
        tv_amount.setText(getResources().getString(R.string.app_name));
        tv_no_balance.setVisibility(View.VISIBLE);
        tv_ensure.setText(getResources().getString(R.string.notify_recharge));
        tv_give_up.setText(String.format(getResources().getString(R.string.notify_give_up), COUNT));
        tv_give_up.setVisibility(View.VISIBLE);
        timeCount.start();
    }


    /**
     * rest view
     */
    private void resetView() {
        ShadowViewHelper.bindShadowHelper(
                new ShadowProperty()
                        .setShadowColor(ActivityCompat.getColor(getActivity(), R.color.colorShadow))
                        .setShadowDy(DensityUtil.dip2px(getActivity(), 1f))
                        .setShadowRadius(DensityUtil.dip2px(getActivity(), 1f)),
                simple_card_image,
                DensityUtil.dip2px(getActivity(), 6.5f),
                DensityUtil.dip2px(getActivity(), 6.5f));

        layout_amount.setVisibility(View.GONE);
        layout_vip_amount.setVisibility(View.GONE);
        layout_print.setVisibility(View.GONE);
        ib_voice.setVisibility(View.INVISIBLE);
        tv_no_balance.setVisibility(View.GONE);
        ib_print_invoice.setVisibility(View.INVISIBLE);
        ib_print_pos.setVisibility(View.INVISIBLE);
        ib_print_ticket.setVisibility(View.INVISIBLE);

        showVoice();
    }

    /***
     * author wangshuai
     * date   2017/6/28 14:46
     *
     *    the method controller voice icon show, if CardNoticeHelper.isSilent(getActivity()) is
     *    true, the icon show otherwise the icon hide. and when card payment success and icon show
     *    play the audio frequency
     */
    private void showVoice() {
        if (CardNoticeHelper.isSilent(getActivity())) {
            ib_voice.setImageResource(R.mipmap.ic_card_notice_silent);
        } else {
            ib_voice.setImageResource(R.mipmap.ic_card_notice_voice);
        }
        if (CardNoticeHelper.isSilent(getActivity())) {
            Timber.tag(TAG).i("voice is silent");
            return;
        }
        if (mode == CardNoticeHelper.MODE_BANK_PAY_SUCCESS ||
                mode == CardNoticeHelper.MODE_BUS_PAY_SUCCESS ||
                mode == CardNoticeHelper.MODE_MEMBER_PAY_SUCCESS) {
            if (mediaPlayer != null) {
                playerMedia();
            }
        }
    }

    /**
     * On view click.
     *
     * @param v the v
     */
    @OnClick({R.id.ib_print_invoice, R.id.ib_print_ticket, R.id.ib_print_pos, R.id.ib_voice,
            R.id.tv_ensure, R.id.tv_give_up})
    void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.ib_print_invoice:
                lookTicket(2, jPushEntity.getInvoiceUrl());
                break;
            case R.id.ib_print_pos:
                lookTicket(0, jPushEntity.getSlipUrl());
                break;
            case R.id.ib_print_ticket:
                lookTicket(1, jPushEntity.getReceiptUrl());
                break;
            case R.id.ib_voice:
                CardNoticeHelper.setSilent(getActivity(), !CardNoticeHelper.isSilent(getActivity()));
                showVoice();
                break;
            case R.id.tv_ensure:
                ensure();
                break;
            case R.id.tv_give_up:
                countCancel();
                break;

        }
    }

    /**
     * look pos url
     *
     * @param url String
     */
    private void lookTicket(int position, String url) {
        if (TextUtils.isEmpty(url)) {
            Timber.tag(TAG).w("url is null");
            return;
        }
        Intent intent = new Intent(getContext(), CardTicketActivity.class);
        intent.putExtra(CardTicketActivity.KEY_ACTION_MODE, position);
        intent.putExtra(CardTicketActivity.KEY_ACTION_URL, url);
        getActivity().startActivity(intent);
    }

    /**
     * player media
     */
    private void playerMedia() {
        mediaPlayer.start();
    }

    /**
     * cancel timeCount
     */
    private void countCancel() {
        if (timeCount != null) {
            timeCount.cancel();
        }
        getActivity().finish();
    }

    private void ensure() {
        if (mode == CardNoticeHelper.MODE_BALANCE_FAILED) {
            recharge();
        } else {
            countCancel();
        }
    }

    /**
     * card recharge
     */
    private void recharge() {
//        presenter.queryQuickPassCards();
        //modify by wangshuai 2017-06-26 update card recharge step
        User user = UserRepository.getInstance(getActivity()).getUser();
        if (mCardEntity == null) {
            Timber.tag(TAG).w(" card entity is maybe null");
            return;
        }
        if (user == null) {
            Timber.tag(TAG).e("user is must be not null");
            return;
        }
        com.okline.vboss.assistant.net.CardEntity entity =
                new com.okline.vboss.assistant.net.CardEntity(mCardEntity.imgUrl(),
                        mCardEntity.cardId(), mCardEntity.cardNo(), mCardEntity.isQuickPass(),
                        mCardEntity.cardName(), null, mCardEntity.bindId(), mCardEntity.isDefault(),
                        mCardEntity.cardMainType(), mCardEntity.iconUrl(), mCardEntity.aid(),
                        mCardEntity.balance(), mCardEntity.isOpen(), mCardEntity.merName(), 0);

        Intent intent = new Intent(getActivity(), CardRechargeActivity.class);
        intent.putExtra(CardRechargeActivity.EXTRA_CARD_ENTITY, entity);
        intent.putExtra(Config.KEY_OLNO, user.getOlNo());
        intent.putExtra(Config.KEY_ADDRESS, user.getBhtAddress());
        intent.putExtra(Config.KEY_USER_NAME, user.getRealName());
        intent.putExtra(Config.KEY_MOBILE, user.getPhone());
        intent.putExtra(Config.KEY_OCARD_STATE, BaseActivity.getOcardState());
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intent, RECHARGE_REQUEST_CODE);
    }


    @Override
    public void updateCardDetail(CardEntity cardEntity) {
        Timber.tag(TAG).i("cardEntity %s ", cardEntity.toString());
        this.mCardEntity = cardEntity;
        if (!TextUtils.isEmpty(cardEntity.imgUrl())) {
            simple_card_image.setImageURI(Uri.parse(cardEntity.imgUrl()));
        }
        if (!TextUtils.isEmpty(jPushEntity.getInvoiceUrl())) {
            ib_print_invoice.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(jPushEntity.getReceiptUrl())) {
            ib_print_ticket.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(jPushEntity.getSlipUrl())) {
            ib_print_pos.setVisibility(View.VISIBLE);
        }

        //show balance
        String balance = String.format(getResources().getString(R.string.notify_balance),
                Formater.money(cardEntity.balance()));
        if (mode == CardNoticeHelper.MODE_BALANCE_FAILED) {
            SpannableString ssBalance = new SpannableString(balance);
            ssBalance.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(getActivity(), R.color.colorCardNotice)),
                    balance.indexOf(getResources().getString(R.string.notify_index)), balance.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_balance.setText(ssBalance);
        } else {
            tv_balance.setText(balance);
            tv_vip_balance.setText(balance);
        }

        if (mode == CardNoticeHelper.MODE_BANK_PAY) {
            tv_amount.setText(getResources().getString(R.string.notify_user_card));
        } else {
            //show consume amount
            String amount = String.format(getResources().getString(R.string.notify_consume_amount),
                    Formater.money(jPushEntity.getAmount()));
            SpannableString ssAmount = new SpannableString(amount);
            ssAmount.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(getActivity(), R.color.colorCardNotice)),
                    amount.indexOf(getResources().getString(R.string.notify_index)), amount.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_amount.setText(ssAmount);
            tv_vip_consume_amount.setText(ssAmount);
        }

        //show consume integral
        String consume_integral = String.format(getResources().getString(R.string.notify_consume_integral), jPushEntity.getIntegral());
        SpannableString ssConsumeIntegral = new SpannableString(consume_integral);
        ssConsumeIntegral.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(getActivity(), R.color.colorCardNotice)),
                consume_integral.indexOf(getResources().getString(R.string.notify_index)), consume_integral.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_vip_consume_integral.setText(ssConsumeIntegral);

        //show VIP card integral
        String vip_integral = String.format(getResources().getString(R.string.notify_remainder_integral), cardEntity.integral());
        tv_vip_integral.setText(vip_integral);

    }

    @Override
    public void updateTradeInfo(CardLog cardLog) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timeCount != null) {
            timeCount.cancel();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private class TimeCount extends CountDownTimer {

        /**
         * Instantiates a new Time count.
         *
         * @param millisInFuture    The number of millis in the future from the call                          to {@link #start()} until the countdown is done and {@link #onFinish()}                          is called.
         * @param countDownInterval The interval along the way to receive                          {@link #onTick(long)} callbacks.
         */
        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (mode == CardNoticeHelper.MODE_BALANCE_FAILED) {
                tv_give_up.setText(String.format(getResources().getString(R.string.notify_give_up), millisUntilFinished / 1000));
            } else {
                tv_ensure.setText(String.format(getResources().getString(R.string.notify_ensure), millisUntilFinished / 1000));
            }
        }

        @Override
        public void onFinish() {
            Timber.tag(TAG).i("finish activity ");
            if (mode != CardNoticeHelper.MODE_BALANCE_FAILED) {
                tv_ensure.setText(getResources().getString(R.string.notify_ensure_1));
            }
            tv_give_up.setText(getResources().getString(R.string.notify_give_up_1));
            getActivity().finish();
        }
    }
}
