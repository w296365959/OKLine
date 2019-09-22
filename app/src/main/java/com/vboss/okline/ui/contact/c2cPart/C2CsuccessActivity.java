package com.vboss.okline.ui.contact.c2cPart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.helper.ToolbarHelper;
import com.vboss.okline.utils.TextUtils;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/4/11 11:12
 * Desc :
 */

public class C2CsuccessActivity extends BaseActivity {

    @BindView(R.id.tv_gather_total_money)
    TextView tvGatherTotalMoney;
    @BindView(R.id.tv_gather_money)
    TextView tvGatherMoney;
    @BindView(R.id.tv_gather_account)
    TextView tvGatherAccount;
    @BindView(R.id.tv_gather_from_account)
    TextView tvGatherFromAccount;
    @BindView(R.id.tv_gather_date)
    TextView tvGatherDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.c2c_success_activity);
        ButterKnife.bind(this);

        initToolbar();
        initData();
    }

    private void initData() {
        Bundle extras = getIntent().getExtras();
        String myCount = extras.getString("myCount");
//        int amount = extras.getInt("amount");
        String amount = extras.getString("amount");
        String time = extras.getString("time");
        DecimalFormat format = new DecimalFormat("##0.00");
        String formatAmount = format.format(Long.parseLong(amount) / 100.00);
        tvGatherTotalMoney.setText("¥ "+formatAmount);
        tvGatherMoney.setText(String.format(getString(R.string.item_gather_money),/*String.valueOf(amount)*/formatAmount));
        tvGatherAccount.setText(String.format(getString(R.string.item_gather_account),myCount));
        //TODO 暂未拿到数据 对方账户
        tvGatherFromAccount.setText(String.format(getString(R.string.item_gather_from_account),""));
        tvGatherDate.setText(String.format(getString(R.string.item_gather_date),time));
    }

    private void initToolbar() {
        ToolbarHelper toolbarHelper = new ToolbarHelper(this);
        toolbarHelper.showToolbar(R.string.title_gathering, true);
        toolbarHelper.setNavigationListener(new ToolbarHelper.OnNavigationClickListener() {
            @Override
            public void onNavigationClick() {
                onBackPressed();
                TextUtils.showOrHideSoftIM(tvGatherTotalMoney,false);
            }

        });
        toolbarHelper.showMenuButton(false);
    }
}
