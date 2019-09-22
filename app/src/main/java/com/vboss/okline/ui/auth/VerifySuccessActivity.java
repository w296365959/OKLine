package com.vboss.okline.ui.auth;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.easeui.utils.DialogUtil;
import com.vboss.okline.R;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.ui.home.MainActivity;

/**
 * OKLine(luoxiuxiu) co.,Ltd.<br/>
 * Author  : luoxiuxiu <br/>
 * Email   : show@okline.cn <br/>
 * Date    : 2017/4/11 <br/>
 * Summary :  注册成功
 */

public class VerifySuccessActivity extends AppCompatActivity implements View.OnClickListener {


    private static final int[] TITLE = {R.string.register_call_title,
            R.string.register_file_title,
            R.string.register_email_title};
    private static final int[] AUTHORITY_TITLE = {R.string.register_ol_title,
            R.string.register_exist_title,
            R.string.register_new_title,
            R.string.register_key_title};
    private static final int[] success_card = { R.string.success_card_bank,
            R.string.success_card_bank,
            R.string.success_card_bank,
            R.string.success_card_bank};

    private static final int[] DRAWABLE = {R.drawable.call_item,
            R.drawable.info_item,
            R.drawable.email_item};
    private static final int[] approve_icon = {R.drawable.approve_icon,
            R.drawable.approve_bank_icon,
            R.drawable.approve_bus_icon,
            R.drawable.approve_door_icon};

    private TextView tvOlNumber;
    private int count;//当前的欧乐会员的排名

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_success_layout);
        ((TextView) findViewById(R.id.tv_title)).setText(R.string.ocr_title_success);

        tvOlNumber = (TextView) findViewById(R.id.tv_ol_number);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        LinearLayout authorityLayout = (LinearLayout) findViewById(R.id.ll_layout);
        User user = UserRepository.getInstance(this).getUser();
        if(user!=null) {
            count = user.getCount();
            if(count == 0)
                tvOlNumber.setVisibility(View.GONE);
            else{
                tvOlNumber.setVisibility(View.VISIBLE);
                tvOlNumber.setText(String.format(getString(R.string.register_ol_count), String.valueOf(count)));
            }
        }
        addLayout(layout);
        addAuthorityLayout(authorityLayout);

        findViewById(R.id.start_ol).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VerifySuccessActivity.this, MainActivity.class));
                finish();
            }
        });

    }

    /**
     * 用户可以开启的权限
     *
     * @param authorityLayout
     */
    private void addAuthorityLayout(LinearLayout authorityLayout) {
        for (int i = 0; i < AUTHORITY_TITLE.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.register_success_card_item, null);
            TextView btnTitle = (TextView) view.findViewById(R.id.title_item_btn);
            TextView tvCardImg = (TextView) view.findViewById(R.id.tv_card_img);
            Button btnDetail = (Button) view.findViewById(R.id.detail_btn);
            TextView tvSuccessCard = (TextView) view.findViewById(R.id.tv_success_card);
            btnTitle.setTextColor(Color.parseColor("#FF6500"));
            btnTitle.setText(getString(AUTHORITY_TITLE[i]));
            tvCardImg.setBackgroundResource(approve_icon[i]);
            if(i==1 || i==2) {
                tvSuccessCard.setVisibility(View.VISIBLE);
                tvSuccessCard.setText(getString(success_card[i]));
            }else {
                tvSuccessCard.setVisibility(View.GONE);
            }
//            Drawable drawable = getResources().getDrawable(AUTHORITY_DEAWABLE[i]);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            btnTitle.setCompoundDrawables(drawable, null, null, null);
            btnDetail.setOnClickListener(this);
            btnDetail.setTag(AUTHORITY_TITLE[i]);
            authorityLayout.addView(view);
        }
    }

    /**
     * 添加用户当前的权限
     *
     * @param layout
     */
    private void addLayout(LinearLayout layout) {
        for (int i = 0; i < TITLE.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.register_success_item, null);
            TextView btnTitle = (TextView) view.findViewById(R.id.title_item_btn);
            Button btnDetail = (Button) view.findViewById(R.id.detail_btn);
            btnTitle.setText(getString(TITLE[i]));
            Drawable drawable = getResources().getDrawable(DRAWABLE[i]);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            btnTitle.setCompoundDrawables(drawable, null, null, null);
            btnDetail.setOnClickListener(this);
            btnDetail.setTag(TITLE[i]);
            layout.addView(view);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        int tag = (Integer) v.getTag();
        if (tag == TITLE[0]) {
            DialogUtil dialogUtil = new DialogUtil(this, getString(R.string.register_call_title), getString(R.string.dialog_call_info));
            dialogUtil.show();
            dialogUtil.setSubmitLeft(getString(R.string.dialog_btn));
            dialogUtil.setScreen();
        } else if (tag == TITLE[1]) {
            DialogUtil dialogUtil = new DialogUtil(this, getString(R.string.register_file_title), getString(R.string.dialog_file_info));
            dialogUtil.show();
            dialogUtil.setSubmitLeft(getString(R.string.dialog_btn));
            dialogUtil.setScreen();
        } else if (tag == TITLE[2]) {
            DialogUtil dialogUtil = new DialogUtil(this, getString(R.string.register_email_title), getString(R.string.dialog_email_info));
            dialogUtil.show();
            dialogUtil.setSubmitLeft(getString(R.string.dialog_btn));
            dialogUtil.setScreen();
        } else if (tag == AUTHORITY_TITLE[0]) {
            DialogUtil dialogUtil = new DialogUtil(this, getString(R.string.register_ol_title), getString(R.string.dialog_ol_info));
            dialogUtil.show();
            dialogUtil.setSubmitLeft(getString(R.string.dialog_btn));
            dialogUtil.setScreen();
        } else if (tag == AUTHORITY_TITLE[1]) {
            DialogUtil dialogUtil = new DialogUtil(this, getString(R.string.register_exist_title), getString(R.string.dialog_exist_info));
            dialogUtil.show();
            dialogUtil.setSubmitLeft(getString(R.string.dialog_btn));
            dialogUtil.setScreen();
        } else if (tag == AUTHORITY_TITLE[2]) {
            DialogUtil dialogUtil = new DialogUtil(this, getString(R.string.register_new_title), getString(R.string.dialog_new_info));
            dialogUtil.show();
            dialogUtil.setSubmitLeft(getString(R.string.dialog_btn));
            dialogUtil.setScreen();
        } else if (tag == AUTHORITY_TITLE[3]) {
            DialogUtil dialogUtil = new DialogUtil(this, getString(R.string.register_key_title), getString(R.string.dialog_key_info));
            dialogUtil.show();
            dialogUtil.setSubmitLeft(getString(R.string.dialog_btn));
            dialogUtil.setScreen();
        } else return;

    }

}
