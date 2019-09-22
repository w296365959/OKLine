package com.vboss.okline.ui.opay;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentSuccessfulActivity extends BaseActivity {

    @BindView(R.id.anchor)
    View anchor;
    @BindView(R.id.ll_settings_return)
    ImageView llSettingsReturn;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.tv_header_save)
    TextView tvHeaderSave;
    @BindView(R.id.iv_ol_logo)
    ImageView ivOlLogo;
    @BindView(R.id.tv_ol_payment_successful)
    TextView tvOlPaymentSuccessful;
    @BindView(R.id.lv_paymentinfo)
    ListView lvPaymentinfo;
    @BindView(R.id.tv_ol_payment_successful_amout)
    TextView tvOlPaymentSuccessfulAmout;
    private String[] items;
    private String[] contents;

    private String orderAmount;
    private String orderNumber;
    private String orderDesc;
    private String cardName;
    private String orderDate;
    private String tn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_successful);
        ButterKnife.bind(PaymentSuccessfulActivity.this);
//        setSystemBarColor(R.color.colorThemeBackground);

        BankDebitInfo serializableExtra = (BankDebitInfo) getIntent().getSerializableExtra(BankDebitInfo.BANK_DEBIT_INFO);
        orderAmount = serializableExtra.getOrderAmount();
        String substring = orderAmount.substring(0, orderAmount.length() - 2);
        String substring1 = orderAmount.substring(orderAmount.length() - 2);
        String text = "¥ " + substring + "." + substring1;
        tvOlPaymentSuccessfulAmout.setText(text);

        items = new String[]{"商品详情", "当前状态", "付款信息", "创建时间", "交易单号", "商户订单号"};
        orderNumber = serializableExtra.getOrderNumber();
        orderDesc = serializableExtra.getOrderDesc();
        cardName = serializableExtra.getCardName();
        orderDate = serializableExtra.getOrderDate();
        tn = serializableExtra.getTn();
        contents = new String[]{orderDesc, "支付成功", cardName, orderDate, tn, orderNumber};
        llSettingsReturn.setVisibility(View.GONE);
        tvHeaderTitle.setText(R.string.pay_status_success);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lvPaymentinfo.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return items.length;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = View.inflate(getApplicationContext(), R.layout.view_payment_successful_info_item, null);
                TextView tv_opaysdk_result_info_item = (TextView) view.findViewById(R.id.tv_opaysdk_result_info_item);
                tv_opaysdk_result_info_item.setText(items[position]);
                TextView tv_opaysdk_result_info_content = (TextView) view.findViewById(R.id.tv_opaysdk_result_info_content);
                tv_opaysdk_result_info_content.setText(contents[position]);
                return view;
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setResult(Activity.RESULT_OK);
                finish();
            }
        }, 3000);
    }

    @Override
    public void onBackPressed() {
        //禁用返回键
    }
}
