package com.vboss.okline.ui.card.ticket;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.helper.FragmentToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/4/11 15:11 <br/>
 * Summary  : POS purchase order
 */

public class CardTicketActivity extends BaseActivity {
    private static final String TAG = CardTicketActivity.class.getSimpleName();
    public static final String KEY_ACTION_MODE = "action_mode";
    public static final String KEY_ACTION_URL = "action_url";
    private static final int[] titles = new int[]{
            R.string.card_pos_purchase_order,
            R.string.card_small_ticket,
            R.string.card_invoice
    };

    @BindView(R.id.tv_pos_ticket_title)
    TextView tv_pos_ticket_title;    //POS单标题

    @BindView(R.id.tv_merchant_name)
    TextView tv_merchant_name;     //商户名

    @BindView(R.id.tv_merchant_number)
    TextView tv_merchant_number;    //商户号

    @BindView(R.id.tv_terminal_number)
    TextView tv_terminal_number;    //终端号

    @BindView(R.id.tv_operator_number)
    TextView tv_operator_number;    //操作员

    @BindView(R.id.tv_card_issuing_bank)
    TextView tv_card_issuing_bank;    //发卡银行

    @BindView(R.id.tv_acquiring_bank)
    TextView tv_acquiring_bank;    //收单银行

    @BindView(R.id.tv_trans_type)
    TextView tv_trans_type;    //交易类型

    @BindView(R.id.tv_card_num)
    TextView tv_card_num;     //卡号

    @BindView(R.id.tv_serial_number)
    TextView tv_serial_number;    //中心流水

    @BindView(R.id.tv_trans_date)
    TextView tv_trans_date;     //交易日期

    @BindView(R.id.tv_period_validity)
    TextView tv_period_validity;    //有效日期

    @BindView(R.id.tv_batch_number)
    TextView tv_batch_number;    //批次号

    @BindView(R.id.tv_voucher_number)
    TextView tv_voucher_number;     //凭证号

    @BindView(R.id.tv_authorization_number)
    TextView tv_authorization_number;    //授权号

    @BindView(R.id.tv_amount_money)
    TextView tv_amount_money;    //金额

    @BindView(R.id.tv_pos_remark)
    TextView tv_pos_remark;    //备注

    @BindView(R.id.iv_signature)
    SimpleDraweeView iv_signature;

    @BindView(R.id.pos_line_1)
    View pos_line_1;
    @BindView(R.id.pos_line_2)
    View pos_line_2;
    @BindView(R.id.pos_line_3)
    View pos_line_3;
    @BindView(R.id.pos_line_4)
    View pos_line_4;


    @BindView(R.id.image_ticket)
    SimpleDraweeView simpleDraweeView;
    @BindView(R.id.toolbar)
    FragmentToolbar toolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_purchase_order);
        ButterKnife.bind(this);

        int mode = getIntent().getIntExtra(KEY_ACTION_MODE, 0);
        String imageUrl = getIntent().getStringExtra(KEY_ACTION_URL);

        Timber.tag(TAG).i("mode %s url %s", mode, imageUrl);
        toolbar.setActionMenuVisible(View.GONE);
        toolbar.setActionTitle(getResources().getString(titles[mode]));
        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
            @Override
            public void onNavigation(View v) {
                finish();
            }
        });

        simpleDraweeView.setImageURI(Uri.parse(imageUrl));
//        updateView();
    }


    private void updateView() {
        tv_pos_ticket_title.setText("银联POS签购单");

        tv_merchant_name.setText(String.format(getResources().getString(R.string.merchant_name), "鲜丰水果(瑞晶大厦店)(银行卡)"));
        tv_merchant_number.setText(String.format(getResources().getString(R.string.merchant_number), "898330160120021"));
        tv_terminal_number.setText(String.format(getResources().getString(R.string.terminal_number), "05315820"));
        tv_operator_number.setText(String.format(getResources().getString(R.string.operator_number), "01"));

        tv_card_issuing_bank.setText(String.format(getResources().getString(R.string.card_issuing_bank), "广发银行(03060000)"));
        tv_acquiring_bank.setText(String.format(getResources().getString(R.string.acquiring_bank), "48023310"));
        tv_trans_type.setText(String.format(getResources().getString(R.string.trans_type), "购物"));
        tv_card_num.setText("622602****5214");

        tv_serial_number.setText(String.format(getResources().getString(R.string.serial_number), "144929016225"));
        tv_trans_date.setText(String.format(getResources().getString(R.string.trans_date), "2016-12-05 09:45:26"));
        tv_period_validity.setText(String.format(getResources().getString(R.string.period_validity), "0000"));
        tv_batch_number.setText(String.format(getResources().getString(R.string.batch_number), "000035"));
        tv_voucher_number.setText(String.format(getResources().getString(R.string.voucher_number), "000229"));
        tv_authorization_number.setText(String.format(getResources().getString(R.string.authorization_number), "941457"));
        tv_amount_money.setText("RMB 137.00");
        tv_pos_remark.setText(String.format(getResources().getString(R.string.pos_remark), "25666366666666"));

        pos_line_1.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        pos_line_2.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        pos_line_3.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        pos_line_4.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }
}
