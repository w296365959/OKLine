package com.vboss.okline.ui.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.data.entities.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/4/12
 * Summary : 在这里描述Class的主要功能
 */

public class CardOpenInfoFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.tv_card_open_info_name)
    TextView tvCardOpenInfoName;
    @BindView(R.id.tv_card_open_info_mobile)
    TextView tvCardOpenInfoMobile;
    @BindView(R.id.tv_card_open_info_ID)
    TextView tvCardOpenInfoID;
    @BindView(R.id.tv_card_open_info_ensure)
    TextView tvCardOpenInfoEnsure;
    @BindView(R.id.action_back)
    ImageButton actionBack;
    @BindView(R.id.action_back_layout)
    RelativeLayout actionBackLayout;
    @BindView(R.id.iv_ocard_state)
    ImageView ivOcardState;
    @BindView(R.id.action_title)
    TextView actionTitle;
    @BindView(R.id.action_menu_button)
    ImageButton actionMenuButton;
    @BindView(R.id.action_menu_layout)
    RelativeLayout actionMenuLayout;
    private OpenCardActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Utils.showLog(TAG, "CardOpenInfoFragment.onCreateView");
        activity = (OpenCardActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_card_open_info, container, false);
        unbinder = ButterKnife.bind(this, view);
//        状态栏内容设置
        actionBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        actionTitle.setText(R.string.card_open_info);
        tvCardOpenInfoEnsure.measure(0, 0);
        int measuredHeight = tvCardOpenInfoEnsure.getMeasuredHeight();
        Utils.showLog(TAG, "高度为：" + measuredHeight + " 转换成dp为：" + Utils.px2dip(getContext(), measuredHeight));
        return view;
    }

    private static final String TAG = "CardOpenInfoFragment";

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Utils.showLog(TAG, "CardOpenInfoFragment.onViewCreated");
        activity.openCardPresenter.getPersonalInfo();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void showPersonalInfo(User user) {
        String idcardNo = user.getIdcardNo();
        String text;
        text = idcardNo.substring(0, 3) + "************" + idcardNo.substring(idcardNo.length() - 3);
        tvCardOpenInfoID.setText(text);
        tvCardOpenInfoName.setText(user.getRealName());
        String phone = user.getPhone();
        tvCardOpenInfoMobile.setText(phone.substring(0, 3) + "****" + phone.substring(7));
        tvCardOpenInfoEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.cardCondition.needFeeToOpen() == 1) {
                    activity.vpOpenCard.setCurrentItem(2);
                } else {
                    activity.vpOpenCard.setCurrentItem(1);
                }
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Utils.showLog(TAG, "CardOpenInfoFragment.setUserVisibleHint:" + isVisibleToUser);
        Utils.showLog(TAG, "getUserVisibleHint:" + getUserVisibleHint());
    }
}
