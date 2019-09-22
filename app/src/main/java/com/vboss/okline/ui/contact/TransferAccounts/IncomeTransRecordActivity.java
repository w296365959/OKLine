package com.vboss.okline.ui.contact.TransferAccounts;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.ui.app.jiugongge.AnimationDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: Yuan shaoyu <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/5/3 15:51 <br/>
 * Summary  : 收款方的转账记录界面
 */
public class IncomeTransRecordActivity extends Activity {
    private static final String TAG = "IncomeTransRecordActivity";
    @BindView(R.id.fragment_toolbar)
    FragmentToolbar toolbar;

    @BindView(R.id.incomeaccount_edit)
    TextView incomeaccount_edit;

    AnimationDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_trans_record);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initToolBar();
        incomeaccount_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = LayoutInflater.from(IncomeTransRecordActivity.this).inflate(R.layout.select_income_ccounts_dialog, null);
                dialog = new AnimationDialog(IncomeTransRecordActivity.this);
                dialog.showDialog(dialogView);
                TextView tv_dialog_ensure = (TextView) dialogView.findViewById(R.id.tv_dialog_ensure);
                tv_dialog_ensure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
//                dialogView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });

                dialog.show();
            }
        });
    }
    private void initToolBar() {
        toolbar.setActionTitle("转账记录");
        toolbar.setActionLogoIcon(R.mipmap.orange_logo);
        toolbar.setActionMenuVisible(View.GONE);
        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
            @Override
            public void onNavigation(View v) {
                // 返回一级界面
                finish();
            }
        });

    }
}
