package com.vboss.okline.ui.user;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.ui.card.widget.LogoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardOpenAuthenticFragment extends BaseFragment {

    @BindView(R.id.iv_icon)
    SimpleDraweeView ivIcon;
    @BindView(R.id.btn_approve)
    TextView btnApprove;
    Unbinder unbinder;
    @BindView(R.id.tv_merName)
    TextView tvMerName;
    @BindView(R.id.tv_card_open_info_name)
    TextView tvCardOpenInfoName;
    @BindView(R.id.tv_card_open_info_mobile)
    TextView tvCardOpenInfoMobile;
    @BindView(R.id.tv_card_open_info_ID)
    TextView tvCardOpenInfoID;
    @BindView(R.id.action_back)
    ImageButton actionBack;
    @BindView(R.id.action_back_layout)
    RelativeLayout actionBackLayout;
    @BindView(R.id.iv_ocard_state)
    LogoView ivOcardState;
    @BindView(R.id.action_title)
    TextView actionTitle;
    @BindView(R.id.action_menu_button)
    ImageButton actionMenuButton;
    @BindView(R.id.action_menu_layout)
    RelativeLayout actionMenuLayout;
    private String cardIcon;
    private String merName;
    private FragmentActivity activity;
    private boolean inCallingMode;
    private UserRepository userRepository;
    private String olNo;
    private String bhtAddress;
    private User user;

    public CardOpenAuthenticFragment() {
        // Required empty public constructor
    }

    public static CardOpenAuthenticFragment newInstance(String cardIcon, String merName, boolean inCallingMode) {
        CardOpenAuthenticFragment cardOpenAuthenticFragment = new CardOpenAuthenticFragment();
        cardOpenAuthenticFragment.merName = merName;
        cardOpenAuthenticFragment.cardIcon = cardIcon;
        cardOpenAuthenticFragment.inCallingMode = inCallingMode;
        return cardOpenAuthenticFragment;
    }

    private static final String TAG = "CardOpenAuthenticFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card_open_authentic, container, false);
        unbinder = ButterKnife.bind(this, view);
        userRepository = UserRepository.getInstance(getContext());
        olNo = userRepository.getOlNo();
        User user = userRepository.getUser();
        if (user != null) {
            bhtAddress = user.getBhtAddress();
        }
        actionTitle.setText(getString(R.string.info_approved));
        actionMenuButton.setVisibility(View.GONE);
        actionBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setResult(Activity.RESULT_CANCELED);
                activity.finish();
            }
        });
        if (!TextUtils.isEmpty(cardIcon)) {
            Utils.showThumb(ivIcon, cardIcon, 120 * 2, 120 * 2, 15f);
        }
        tvMerName.setText(merName);
        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到相应的APP
                if (inCallingMode) {
                    Intent intent = new Intent();
                    intent.putExtra(PersonalInfoApprovalActivity.OL_NO, olNo);
                    intent.putExtra(PersonalInfoApprovalActivity.BLUETOOTH_ADDRESS, bhtAddress);
                    activity.setResult(Activity.RESULT_OK, intent);
                    activity.finish();
                }
            }
        });
        this.user = UserRepository.getInstance(getContext()).getUser();
        if (this.user == null) {
            this.user = new User("139****8868", "张三", "01990", "", "", "", "");
            this.user.setIdcardNo("110108198808028852");
        }
        showPersonalInfo(this.user);
        return view;
    }

    public void showPersonalInfo(User user) {
        String idcardNo = user.getIdcardNo();
        String text;
        text = idcardNo.substring(0, 3) + "************" + idcardNo.substring(idcardNo.length() - 3);
        tvCardOpenInfoID.setText(text);
        tvCardOpenInfoName.setText(user.getRealName());
        String phone = user.getPhone();
        tvCardOpenInfoMobile.setText(phone.substring(0, 3) + "****" + phone.substring(7));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
