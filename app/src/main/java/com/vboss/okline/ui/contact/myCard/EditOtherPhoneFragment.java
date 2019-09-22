package com.vboss.okline.ui.contact.myCard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.base.EventToken;
import com.vboss.okline.base.OKLineApp;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.data.local.UserLocalDataSource;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.ui.contact.ContactsUtils;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.TextUtils;
import com.vboss.okline.utils.ToastUtil;
import com.vboss.okline.view.widget.CommonDialog;
import com.vboss.okline.view.widget.ItemView;

import java.util.ArrayList;
import java.util.List;

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
 * Date : 2017/6/5 10:19
 * Desc :
 */

public class EditOtherPhoneFragment extends BaseFragment {
    private static final String TAG = "EditOtherPhoneFragment";
    @BindView(R.id.tv_add_phone)
    TextView tvAddPhone;
    @BindView(R.id.tv_save_otherphone)
    TextView tvSaveOtherphone;
    Unbinder unbinder;
    LinearLayout llItemContent;
    @BindView(R.id.toolbar_contact_newcard)
    FragmentToolbar toolbar;
    private MainActivity activity;
    private List<String> phoneList = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contacts_my_otherphone, null);
        llItemContent = (LinearLayout) view.findViewById(R.id.ll_item_content);
        unbinder = ButterKnife.bind(this, view);
        //modify by linzhangbin 2017/6/21 获取用户个人信息
        UserRepository.getInstance(OKLineApp.context)
                .getContactVisitingCard(null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<User>(TAG){
                    @Override
                    public void onNext(User user) {
                        phoneList = user.getSecondPhoneArray();
                        if (null == phoneList || phoneList.isEmpty()){
                            Timber.tag(TAG).i("添加第一个布局");
                            //开始先增加一个电话布局
//                            llItemContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                                @Override
//                                public void onGlobalLayout() {
//                                    addView("","电话1");
//                                    llItemContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                                }
//                            });
                        }else{
                            for (int i = 0; i <phoneList.size(); i++) {
                                addView(phoneList.get(i),"通信方式"+(llItemContent.getChildCount()+1));
                            }

                        }
                    }
                });


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setActionTitle(getResources().getString(R.string.title_my_other_phone));
        toolbar.setActionMenuVisible(View.GONE);
        toolbar.setActionMenuClickable(false);
        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
            @Override
            public void onNavigation(View v) {
                //modify by linzhangbin 2017/6/8 返回关闭键盘
                TextUtils.showOrHideSoftIM(toolbar,false);
                activity.removeSecondFragment();
                //end
            }
        });


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
                Timber.tag(TAG).i("addPhone~~~~~");
                addView("","通信方式"+(llItemContent.getChildCount()+1));
                break;
            case R.id.tv_save_otherphone:
                savePhone();

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

    /**
     * 保存我的其他电话号码
     */
    private void savePhone() {
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
        //modify by linzhangbin 空保存闪退的问题 2017/6/8

        //modify by linzhangbin 2017/6.29 修改自己的名片不用传手机号 start
        UserRepository.getInstance(activity).saveSecondPhone(null,ContactsUtils.filterList(list))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<Boolean>(TAG){
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean){
                            ToastUtil.show(activity,"保存成功!");
                            RxBus.get().post(EventToken.SAVE_OTHER_PHONE,list);
                            //add by linzhangbin 收起键盘 2017/6/8
                            TextUtils.showOrHideSoftIM(llItemContent,false);
                            //add by linzhangbin 收起键盘 2017/6/8
                            activity.removeSecondFragment();
                        }
                    }
                });
        //modify by linzhangbin 2017/6.29 修改自己的名片不用传手机号 end

    }

}