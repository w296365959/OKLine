package com.vboss.okline.ui.contact.addContact;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.base.EventToken;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.ui.contact.bean.ContactItem;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.utils.TextUtils;
import com.vboss.okline.utils.ToastUtil;
import com.vboss.okline.view.widget.ClearEditText;
import com.vboss.okline.view.widget.CommonDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/6/9 10:05
 * Desc :
 */

public class EditAddInfoFragment extends BaseFragment {
    private static final String TAG = "EditAddInfoFragment";

    @BindView(R.id.toolbar_contact)
    FragmentToolbar toolbar;
    @BindView(R.id.et_add_name)
    ClearEditText etAddName;
    @BindView(R.id.ll_edit_name)
    LinearLayout llEditName;
    @BindView(R.id.et_card_phone)
    ClearEditText etCardPhone;
    @BindView(R.id.ll_edit_phone)
    LinearLayout llEditPhone;
    @BindView(R.id.tv_save_info)
    TextView tvSaveInfo;
    Unbinder unbinder;
    //add by linzhangbin 2017/6/26 增加关系一栏 start
    @BindView(R.id.et_card_relationship)
    ClearEditText etCardRelationship;
    @BindView(R.id.ll_edit_relationship)
    LinearLayout llEditRelationship;
    //add by linzhangbin 2017/6/26 增加关系一栏 end
    private MainActivity activity;

    //add by linzhangbin 2017/6/13 回显之前编辑的姓名和号码
    public static Fragment newInstance(String name, String phone, String relationship) {
        EditAddInfoFragment fragment = new EditAddInfoFragment();
        Bundle bundle = new Bundle();
        if (!phone.equals("编辑号码")) {
            bundle.putString("addName", name);
            bundle.putString("addPhone", phone);
            bundle.putString("relationship", relationship);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_add_info_fragment, null);
        unbinder = ButterKnife.bind(this, view);
        activity = (MainActivity) getActivity();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        etAddName.setFocusable(true);
        etAddName.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) etAddName.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(etAddName, 0);
        //add by linzhangbin 2017/6/13 回显之前编辑的姓名和号码
        Bundle bundle = getArguments();
        if (null != bundle) {
            String addName = bundle.getString("addName");
            String addPhone = bundle.getString("addPhone");
            String relationship = bundle.getString("relationship");
            etAddName.setText(addName);
            etCardPhone.setText(addPhone);
            etCardRelationship.setText(relationship);
            if (null != addName) {
                etAddName.setSelection(addName.length());
            }

        }
        //add by linzhangbin 2017/6/13 回显之前编辑的姓名和号码 end
    }

    /**
     * init Toolbar
     */
    private void initToolbar() {
        toolbar.setActionTitle(getResources().getString(R.string.title_add_card));
        toolbar.setNavigationVisible(View.VISIBLE);
        toolbar.setActionMenuVisible(View.GONE);
        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
            @Override
            public void onNavigation(View v) {
                new CommonDialog(activity)
                        .setTitleVisible(View.GONE)
                        .setContent(getString(R.string.abandon_save_card))
                        .setNegativeButton(getString(R.string.dialog_negative))
                        .setPositiveButton(getString(R.string.dialog_positive))
                        .setOnDialogClickListener(new CommonDialog.OnDialogInterface() {
                            @Override
                            public void cancel(View view, CommonDialog dialog) {
                                dialog.dismiss();
                            }

                            @Override
                            public void ensure(View view, CommonDialog dialog) {
                                dialog.dismiss();
                                TextUtils.showOrHideSoftIM(toolbar, false);
                                activity.removeSecondFragment();
                            }
                        }).show();
            }
        });
    }
    //add by linzhangbin 2017/6/13 回显之前编辑的姓名和号码 end

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //modify by linzhangbin 2017/6/27
    @OnClick(R.id.tv_save_info)
    public void onViewClicked() {
        //modify by linzhangbin 2017/7/6 创建联系人加入nickName(备注)字段 start
        String name = etAddName.getText().toString().trim();
        String phone = etCardPhone.getText().toString().trim();
        String remark = etCardRelationship.getText().toString().trim();
        //modify by linzhangbin 2017/7/6 创建联系人加入nickName(备注)字段 end
        ContactItem item = new ContactItem();
        item.setRemark(name);
        item.setPhoneNum(phone);
        item.setNickName(remark);
        if (TextUtils.isEmpty(name)){
            ToastUtil.show(activity,"姓名不能为空");
        }else if(TextUtils.isEmpty(phone)){
            ToastUtil.show(activity,"电话不能为空");
        }else if(TextUtils.isEmpty(remark)){
            ToastUtil.show(activity,"关系不能为空");
        }else{
            RxBus.get().post(EventToken.SAVE_PHONE_NAME, item);
            TextUtils.showOrHideSoftIM(toolbar, false);
            activity.removeSecondFragment();
            Timber.tag(TAG).i("name : %s, phone : %s, relationship : %s", name, phone, remark);
        }

    }
}
