package com.vboss.okline.ui.opay;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PaymentFailedActivity extends BaseActivity {

    @BindView(R.id.anchor)
    View anchor;
    @BindView(R.id.ll_settings_return)
    ImageView llSettingsReturn;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.tv_header_save)
    TextView tvHeaderSave;
    @BindView(R.id.tv_opaysdk_resultfailed_reason)
    TextView tvOpaysdkResultfailedReason;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_failed);
        unbinder = ButterKnife.bind(this);
//        setSystemBarColor(R.color.colorThemeBackground);
        llSettingsReturn.setVisibility(View.GONE);
        tvHeaderSave.setText(R.string.edit_completed);
        tvHeaderSave.setVisibility(View.VISIBLE);
        tvHeaderSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
        tvHeaderTitle.setText(R.string.pay_status_failure);
        String resultMsg = getIntent().getStringExtra("resultMsg");
        String s = getResources().getString(R.string.tv_opaysdk_resultfailed_reason_prefix) + resultMsg + getString(R.string.tv_opaysdk_resultfailed_reason_postfix);
        tvOpaysdkResultfailedReason.setText(s);
    }

    @Override
    public void onBackPressed() {
        //禁用返回键
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
