package com.vboss.okline.ui.contact.addContact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.base.EventToken;
import com.vboss.okline.base.OKLineApp;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.ui.contact.ContactsUtils;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.utils.TextUtils;
import com.vboss.okline.utils.ToastUtil;
import com.vboss.okline.view.widget.CommonDialog;
import com.vboss.okline.view.widget.ItemView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/6/9 11:24
 * Desc : 添加联系人编辑其他通信方式页面
 */

public class EditAddPhoneFragment extends BaseFragment {
    private static final String TAG = "EditAddPhoneFragment";
    MainActivity activity;
    @BindView(R.id.toolbar_contact_newcard)
    FragmentToolbar toolbar;
    @BindView(R.id.ll_item_content)
    LinearLayout llItemContent;
    @BindView(R.id.tv_add_phone)
    TextView tvAddPhone;
    @BindView(R.id.tv_save_otherphone)
    TextView tvSaveOtherphone;
    Unbinder unbinder;
    private String tag = "";
    private String phone = "";

    public static Fragment newInstance(String tag, ArrayList<String> list,String phone){
        EditAddPhoneFragment fragment = new EditAddPhoneFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("phoneList",list);
        bundle.putString("classTag",tag);
        bundle.putString("phone",phone);
        fragment.setArguments(bundle);


        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contacts_my_otherphone, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setActionTitle(getResources().getString(R.string.title_other_connection));
        toolbar.setActionMenuVisible(View.GONE);
        toolbar.setActionMenuClickable(false);
        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
            @Override
            public void onNavigation(View v) {
                //modify by linzhangbin 2017/6/8 返回关闭键盘
                TextUtils.showOrHideSoftIM(toolbar, false);
                activity.removeSecondFragment();
                //end
            }
        });
        Bundle bundle = getArguments();
        if (null != bundle){
            ArrayList<String> phoneList = bundle.getStringArrayList("phoneList");
            tag = bundle.getString("classTag");
            phone = bundle.getString("phone");
            for (int i = 0; i <phoneList.size(); i++) {
//                ItemView itemView = new ItemView(activity);
//                itemView.setLabelContent("通信方式"+(i+1));
//                itemView.setEtContent(phoneList.get(i));
//                llItemContent.addView(itemView);
                addView(phoneList.get(i),"通信方式"+(llItemContent.getChildCount()+1));
            }
        }

        tvAddPhone.setText("添加通信方式");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_add_phone, R.id.tv_save_otherphone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_add_phone:
                addView("","通信方式"+(llItemContent.getChildCount()+1));

                break;
            case R.id.tv_save_otherphone:
                final ArrayList<String> list = new ArrayList<>();
                //modify by linzhangbin 空保存闪退的问题 2017/6/8
                if (0 == llItemContent.getChildCount()){

                }else{
                    //遍历装电话条目的线性布局
                    for (int i = 0; i < llItemContent.getChildCount(); i++) {
                        ItemView itemView = (ItemView)llItemContent.getChildAt(i);
                        list.add(itemView.getEtContent());
                        Timber.tag(TAG).i("savePhone:"+itemView.getEtContent());
                    }
                }
                //modify by linzhangbin 2017/7/5 编辑名片和新增联系人点击保存有不同的事件 start
                if (tag.equals(AddCardFragment.class.getSimpleName())) {
                    //新增联系人
                    RxBus.get().post(EventToken.SAVE_ADDED_PHONE, ContactsUtils.filterList(list));
                    TextUtils.showOrHideSoftIM(toolbar, false);
                    activity.removeSecondFragment();
                }else{
                    //编辑名片
                    UserRepository.getInstance(OKLineApp.context).saveSecondPhone(phone,ContactsUtils.filterList(list))
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new DefaultSubscribe<Boolean>(TAG){
                                @Override
                                public void onNext(Boolean aBoolean) {
                                    ToastUtil.show(activity,"保存成功");
                                    RxBus.get().post(EventToken.SAVE_DETAIL_PHONE, ContactsUtils.filterList(list));
                                    TextUtils.showOrHideSoftIM(toolbar, false);
                                    activity.removeSecondFragment();
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    super.onError(throwable);
                                    ToastUtil.show(activity,"保存失败,请稍后再试");
                                }
                            });
                }
                //modify by linzhangbin 2017/7/5 编辑名片和新增联系人点击保存有不同的事件 end


                break;
        }
    }

    /**
     * 添加电话条目
     */
    private void addView(String value,String label) {
        Timber.tag(TAG).i("addView~~~~~~~~~~~``");
        final ItemView itemView = new ItemView(activity);
        itemView.setLabelContent(label);
        llItemContent.addView(itemView);
        itemView.setEtContent(value);
        itemView.setDeleteCallBack(new ItemView.DeleteCallBack() {
            @Override
            public void deleteLayout(final ItemView itemView) {
                //modify by linzhangbin 点击删除弹出对话框
                new CommonDialog(activity)
                        .setTitleVisible(View.GONE)
                        .setContent(getString(R.string.text_confirm_delete))
                        .setNegativeButton(getString(R.string.dialog_negative))
                        .setPositiveButton(getString(R.string.dialog_positive))
                        .setOnDialogClickListener(new CommonDialog.OnDialogInterface() {
                            @Override
                            public void cancel(View view, CommonDialog dialog) {
                                dialog.dismiss();
                            }

                            @Override
                            public void ensure(View view, CommonDialog dialog) {
                                llItemContent.removeView(itemView);
                                //add by linzhangbin 修复删除后的"电话1"显示问题
                                for (int i = 0; i < llItemContent.getChildCount(); i++){
                                    if (llItemContent.getChildCount()>0){
                                        ItemView item =(ItemView) llItemContent.getChildAt(i);
                                        item.setLabelContent("通信方式"+(i+1));
                                    }
                                }
                                dialog.dismiss();
                                //add by linzhangbin 修复删除后的"电话1"显示问题

                            }
                        }).show();
                //modify by linzhangbin 点击删除弹出对话框
            }
        });
    }
}
