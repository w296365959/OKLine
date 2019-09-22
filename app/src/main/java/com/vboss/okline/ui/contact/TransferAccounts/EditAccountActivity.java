package com.vboss.okline.ui.contact.TransferAccounts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: Yuan shaoyu <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/5/7 10:51 <br/>
 * Summary  : 编辑（新增）收款账户界面
 */
public class EditAccountActivity extends Activity {
    private static final String TAG = "SelectAccountActivity";

    @BindView(R.id.fragment_toolbar)
    FragmentToolbar toolbar;

    @BindView(R.id.account_no)
    EditText account_no;

    @BindView(R.id.account_name)
    EditText account_name;

    @BindView(R.id.to_select_bank)
    TextView toSelectBank;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initToolBar();
    }
    @OnClick({R.id.to_select_bank,R.id.save_account_button})
    public void ViewOnClick(View view) {
        switch (view.getId()){
            case R.id.to_select_bank:
                Intent intent = new Intent(EditAccountActivity.this,SelectBankActivity.class);
                startActivity(intent);
                break;
            case R.id.save_account_button:

                break;
            default:
                break;
        }
    }
    private void initToolBar() {
        toolbar.setActionTitle("收款账户");
        toolbar.setActionLogoIcon(R.mipmap.orange_logo);
        //取得从上一个Activity当中传递过来的Intent对象
        Intent intent = getIntent();
        //从Intent当中根据key取得value
        String value = intent.getStringExtra("activityTag");
        String accountNo = intent.getStringExtra("accountNo");
        String accountName = intent.getStringExtra("accountName");
        if (value.equals("add")) {
            toolbar.setActionMenuVisible(View.GONE);
        }else if (value.equals("edit")){
            account_name.setCursorVisible(false);
            account_no.setCursorVisible(false);
            account_name.setText(accountName);
            account_no.setText(accountNo);
            toolbar.setActionMenuVisible(View.VISIBLE);
            toolbar.setActionMenuIcon(R.mipmap.delete_account);
            toolbar.setOnActionMenuClickListener(new FragmentToolbar.OnActionMenuClickListener() {
                @Override
                public void onActionMenu(View v) {
                    // TODO: 2017/5/7 删除账户 
                }
            });
        }

        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
            @Override
            public void onNavigation(View v) {
                // 返回一级界面
                finish();
            }
        });

    }
}
