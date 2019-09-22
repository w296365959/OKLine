package com.vboss.okline.ui.opay;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.vboss.okline.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * OKLine(Hangzhou) Co.,ltd.
 * Author:Zheng Jun
 * Email:zhengjun@okline.cn
 * Date: 2016-6-16 11:28:11
 * Desc:
 */
public class CardPasswordFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.iv_icon2)
    ImageView ivIcon2;
    @BindView(R.id.tv_paypophint2)
    TextView tvPaypophint2;
    @BindView(R.id.tv_paypopcancel2)
    TextView tvPaypopcancel2;
    @BindView(R.id.bankpsw_dot1)
    ImageView bankpswDot1;
    @BindView(R.id.bankpsw_dot2)
    ImageView bankpswDot2;
    @BindView(R.id.bankpsw_dot3)
    ImageView bankpswDot3;
    @BindView(R.id.bankpsw_dot4)
    ImageView bankpswDot4;
    @BindView(R.id.bankpsw_dot5)
    ImageView bankpswDot5;
    @BindView(R.id.bankpsw_dot6)
    ImageView bankpswDot6;
    @BindView(R.id.ll_pwdface2)
    RelativeLayout llPwdface2;
    @BindView(R.id.btn_bankpwdkey1)
    TextView btnBankpwdkey1;
    @BindView(R.id.btn_bankpwdkey2)
    TextView btnBankpwdkey2;
    @BindView(R.id.btn_bankpwdkey3)
    TextView btnBankpwdkey3;
    @BindView(R.id.btn_bankpwdkey4)
    TextView btnBankpwdkey4;
    @BindView(R.id.btn_bankpwdkey5)
    TextView btnBankpwdkey5;
    @BindView(R.id.btn_bankpwdkey6)
    TextView btnBankpwdkey6;
    @BindView(R.id.btn_bankpwdkey7)
    TextView btnBankpwdkey7;
    @BindView(R.id.btn_bankpwdkey8)
    TextView btnBankpwdkey8;
    @BindView(R.id.btn_bankpwdkey9)
    TextView btnBankpwdkey9;
    @BindView(R.id.btn_bankpwdkeyblank)
    TextView btnBankpwdkeyblank;
    @BindView(R.id.btn_bankpwdkey0)
    TextView btnBankpwdkey0;
    @BindView(R.id.btn_bankpwdkeydel)
    RelativeLayout btnBankpwdkeydel;
    @BindView(R.id.gv_bankkeyboard)
    GridLayout gvBankkeyboard;
    private StringBuffer pwd;
    private ArrayList<ImageView> list;
    private OPaySDKActivity activity;
    private View view;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (OPaySDKActivity) getActivity();
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_opaysdk4, null);
            unbinder = ButterKnife.bind(this, view);
            pwd = new StringBuffer(6);
            list = new ArrayList<>(6);
            list.add(bankpswDot1);
            list.add(bankpswDot2);
            list.add(bankpswDot3);
            list.add(bankpswDot4);
            list.add(bankpswDot5);
            list.add(bankpswDot6);
            btnBankpwdkey0.setOnClickListener(this);
            btnBankpwdkey1.setOnClickListener(this);
            btnBankpwdkey2.setOnClickListener(this);
            btnBankpwdkey3.setOnClickListener(this);
            btnBankpwdkey4.setOnClickListener(this);
            btnBankpwdkey5.setOnClickListener(this);
            btnBankpwdkey6.setOnClickListener(this);
            btnBankpwdkey7.setOnClickListener(this);
            btnBankpwdkey8.setOnClickListener(this);
            btnBankpwdkey9.setOnClickListener(this);
            btnBankpwdkeydel.setOnClickListener(this);
            tvPaypopcancel2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //返回的信息为用户中途取消!
                    getActivity().setResult(Activity.RESULT_CANCELED);
                    getActivity().finish();
                }
            });
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bankpwdkeydel:
                if (pwd.length() > 0) {
                    pwd.deleteCharAt(pwd.length() - 1);
                    setDotVisibility(list, pwd.length());
                }
                break;
            case R.id.btn_bankpwdkey0:
                pwd.append(0);
                checkPwd();
                break;
            case R.id.btn_bankpwdkey1:
                pwd.append(1);
                checkPwd();
                break;
            case R.id.btn_bankpwdkey2:
                pwd.append(2);
                checkPwd();
                break;
            case R.id.btn_bankpwdkey3:
                pwd.append(3);
                checkPwd();
                break;
            case R.id.btn_bankpwdkey4:
                pwd.append(4);
                checkPwd();
                break;
            case R.id.btn_bankpwdkey5:
                pwd.append(5);
                checkPwd();
                break;
            case R.id.btn_bankpwdkey6:
                pwd.append(6);
                checkPwd();
                break;
            case R.id.btn_bankpwdkey7:
                pwd.append(7);
                checkPwd();
                break;
            case R.id.btn_bankpwdkey8:
                pwd.append(8);
                checkPwd();
                break;
            case R.id.btn_bankpwdkey9:
                pwd.append(9);
                checkPwd();
                break;
            default:
                break;
        }
    }


    private void checkPwd() {
        final String string = pwd.length() > 6 ? pwd.substring(0, 6) : pwd.toString();
        setDotVisibility(list, string.length());
        if (string.length() == 6) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //TODO 银行卡接口暂时不用处理
                    if (string.equals("666666")) {
                        activity.viewPager.setCurrentItem(5, true);
                    }
                    pwd.setLength(0);
                    setDotVisibility(list, 0);
                }
            }, 500);
        }
    }


    private void setDotVisibility(ArrayList<ImageView> list, int length) {
        for (int i = 0; i < length; i++) {
            list.get(i).setVisibility(View.VISIBLE);
        }
        for (int i = length; i < list.size(); i++) {
            list.get(i).setVisibility(View.INVISIBLE);
        }
    }
}
