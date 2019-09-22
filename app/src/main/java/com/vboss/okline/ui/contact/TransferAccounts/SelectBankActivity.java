package com.vboss.okline.ui.contact.TransferAccounts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.ui.contact.bean.Contact;
import com.vboss.okline.utils.FrescoUtil;
import com.vboss.okline.view.widget.SideBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: Yuan shaoyu <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/5/18 10:51 <br/>
 * Summary  : 选择银行卡界面
 */
public class SelectBankActivity extends Activity {

    @BindView(R.id.bank_listview)
    ListView bank_listview;

    @BindView(R.id.select_bank_dialog)
    TextView dialog;

    @BindView(R.id.select_bank_sidebar)
    SideBar sidebar;

    @BindView(R.id.sdv_logo)
    SimpleDraweeView actionLogo;

    @BindView(R.id.action_title)
    TextView actionTitle;

    @BindView(R.id.action_menu_layout)
    RelativeLayout action_menu_layout;

    @BindView(R.id.action_back_layout)
    RelativeLayout action_back_layout;

    AccountBankAdapter adapter;
    List<Contact> bankList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bank);
        ButterKnife.bind(this);
        initView();
        showContacts();
    }

    private void initView() {
        sidebar.setTextView(dialog);
        bankList = new ArrayList<>();
        adapter = new AccountBankAdapter(this,bankList);
        bank_listview.setAdapter(adapter);

        //设置右侧触摸监听
        sidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //TextUtils.showOrHideSoftIM(sideBar, false);
                //End to fix 187
                //该字母首次出现的位置
                if (adapter != null) {
                    int position = adapter.getPositionForSection(s.charAt(0));
                    if (position != -1) {
                        bank_listview.setSelection(position);
                    }
                }
            }
        });

        actionLogo.setController(FrescoUtil.getDefaultImage(this, R.mipmap.orange_logo));
        actionTitle.setText("选择银行");
        action_back_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        action_menu_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectBankActivity.this,SearchBankActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 去数据库查询联系人
     */
    private void showContacts() {
        for (int i = 0; i < 3; i++) {
            Contact contact = new Contact();
            contact.setName("工商银行");
            bankList.add(contact);
        }
        adapter.refresh(bankList);
    }
}
