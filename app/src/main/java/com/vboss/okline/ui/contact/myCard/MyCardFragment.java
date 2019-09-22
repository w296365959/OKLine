package com.vboss.okline.ui.contact.myCard;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.base.helper.ToolbarHelper;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.ContentValues.TAG;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/3/31 16:17
 * Desc :
 */

public class MyCardFragment extends BaseFragment {

    @BindView(R.id.sdv_user_avatar)
    SimpleDraweeView sdvUserAvatar;
    @BindView(R.id.tv_user_remark)
    TextView tvUserRemark;
    @BindView(R.id.tv_user_phone)
    TextView tvUserPhone;
    Unbinder unbinder;
    @BindView(R.id.toolbar_contact)
    FragmentToolbar toolbar;
    private MainActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_card, null);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setActionTitle(getResources().getString(R.string.title_my_card));
        toolbar.setActionMenuVisible(View.GONE);
        toolbar.setActionMenuClickable(false);
        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
            @Override
            public void onNavigation(View v) {
                activity.removeSecondFragment();
            }
        });
        //lzb test
        initContent();
    }

    private void initContent() {
        //edit 2017/5/7 拿不到身份证的头像 就暂时用默认头像
        User user = UserRepository.getInstance(activity).getUser();

        if (null != user) {
            Log.i("MyCardFragment", "initContent: user:" + user.toString());
            if (!StringUtils.isNullString(user.getAvatar())){
                sdvUserAvatar.setImageURI(Uri.parse(user.getAvatar()));
            }else{
            sdvUserAvatar.setImageResource(R.mipmap.default_avatar);
            }
            if (!StringUtils.isNullString(user.getRealName())) {
                tvUserRemark.setText(user.getRealName());
            } else {
                tvUserRemark.setText("");
            }
            if (!StringUtils.isNullString(user.getPhone())) {
                tvUserPhone.setText(user.getPhone());
            } else {
                tvUserPhone.setText("");
            }
        } else {
            Log.i("MyCardFragment", "initContent: user为null");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
