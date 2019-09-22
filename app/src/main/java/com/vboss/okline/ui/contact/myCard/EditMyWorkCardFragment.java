package com.vboss.okline.ui.contact.myCard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.base.EventToken;
import com.vboss.okline.base.OKLineApp;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.ui.contact.addContact.AddCardFragment;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.utils.StringUtils;
import com.vboss.okline.utils.TextUtils;
import com.vboss.okline.utils.ToastUtil;
import com.vboss.okline.view.widget.ClearEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;


/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/6/5 10:35
 * Desc :
 */

public class EditMyWorkCardFragment extends BaseFragment {
    private static final String TAG = "EditMyWorkCard";
    @BindView(R.id.toolbar_contact_workcard)
    FragmentToolbar toolbar;
    @BindView(R.id.et_company_name)
    ClearEditText etCompanyName;
    @BindView(R.id.ll_create_con_phone)
    LinearLayout llCreateConPhone;
    @BindView(R.id.et_job_position)
    ClearEditText etJobPosition;
    @BindView(R.id.ll_create_con_name)
    LinearLayout llCreateConName;
    @BindView(R.id.tv_my_location)
    TextView tvMyLocation;
    @BindView(R.id.ll_location)
    LinearLayout llLocation;
    @BindView(R.id.et_detail_location)
    EditText etDetailLocation;
    @BindView(R.id.tv_save_address)
    TextView tvSaveAddress;
    Unbinder unbinder;
    private MainActivity activity;
    private List<Subscription> sbpList;
    //用来区分从不同的界面进入的这里
    private String tag;
    //联系人电话
    private String contactPhone;
    private User.VisitingCard workCard;

    //add by linzhangbin 2017/6/27 设置tag判断从哪个页面进入的编辑工作名片界面 start
    public static Fragment newInstance(String tag, String phone, User.VisitingCard workCard) {
        EditMyWorkCardFragment fragment = new EditMyWorkCardFragment();
        Bundle bundle = new Bundle();
        bundle.putString("classTag", tag);
        bundle.putString("contactPhone", phone);
        if (null != workCard) {
            bundle.putSerializable("workCard", workCard);
        }
        fragment.setArguments(bundle);


        return fragment;
    }
    //add by linzhangbin 2017/6/27 设置tag判断从哪个页面进入的编辑工作名片界面 end


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_my_work_card, null);
        unbinder = ButterKnife.bind(this, view);
        sbpList = new ArrayList<>();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setActionTitle(getResources().getString(R.string.title_my_work_card));
        toolbar.setActionMenuVisible(View.GONE);
        toolbar.setActionMenuClickable(false);
        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
            @Override
            public void onNavigation(View v) {
                TextUtils.showOrHideSoftIM(toolbar, false);
                activity.removeSecondFragment();
            }
        });
        Bundle bundle = getArguments();
        if (null != bundle) {
            tag = bundle.getString("classTag");
            contactPhone = bundle.getString("contactPhone");
            workCard = (User.VisitingCard) bundle.getSerializable("workCard");
        }
        initContent();
    }

    //modify by linzhangbin 2017/6/27 从三个不同界面进入编辑工作名片的区分 start

    /**
     * 初始化界面
     */
    private void initContent() {
        //modify by linzhangbin 2017/6/20 默认弹出软键盘
        etCompanyName.setFocusable(true);
        etCompanyName.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) etCompanyName.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(etCompanyName, 0);
        //modify by linzhangbin 2017/6/20 默认弹出软键盘 end
        if (tag.equals(MyCardFragmentNew.class.getSimpleName())) { //从我的名片过来的
            //modify by linzhangbin 2017/6/21 获取用户个人信息
            Subscription sbp = UserRepository.getInstance(OKLineApp.context)
                    .getContactVisitingCard(null)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new DefaultSubscribe<User>(TAG) {
                        @Override
                        public void onNext(User user) {
                            User.VisitingCard visitingCard = user.getVisitingCard();
                            if (null != visitingCard) {
                                Timber.tag(TAG).i("initContent: visitingCard:" + visitingCard.toString());
                                String company = visitingCard.getCompany();
                                String address = visitingCard.getAddress();
                                String position = visitingCard.getPosition();
                                String area = visitingCard.getArea();
                                if (!StringUtils.isNullString(company)) {
                                    etCompanyName.setText(company);
                                    etCompanyName.setSelection(company.length());
                                }
                                if (!StringUtils.isNullString(position)) {
                                    etJobPosition.setText(position);
                                }
                                if (!StringUtils.isNullString(address)) {
                                    etDetailLocation.setText(address);
                                }
                                //add by linzhangbin 2017/6/7 显示省市县地区
                                if (!TextUtils.isEmpty(area)) {
                                    tvMyLocation.setText(area);
                                }
                                //add by linzhangbin 2017/6/7 显示省市县地区
                            }

                        }
                    });
            sbpList.add(sbp);
        } else if (tag.equals(AddCardFragment.class.getSimpleName())) { // 从创建联系人过来的
            toolbar.setActionTitle(getResources().getString(R.string.title_work_card));
            if (null != workCard) {
                etCompanyName.setText(workCard.getCompany());
                etJobPosition.setText(workCard.getPosition());
                etDetailLocation.setText(workCard.getAddress());
                if (!TextUtils.isEmpty(workCard.getArea())) {
                    tvMyLocation.setText(workCard.getArea());
                }
            }
        } else { //从个人名片过来的
            toolbar.setActionTitle(getResources().getString(R.string.title_work_card));
            if (null != workCard) {
                etCompanyName.setText(workCard.getCompany());
                etJobPosition.setText(workCard.getPosition());
                etDetailLocation.setText(workCard.getAddress());
                if (!TextUtils.isEmpty(workCard.getArea())) {
                    tvMyLocation.setText(workCard.getArea());
                }
            }

        }

    }
    //modify by linzhangbin 2017/6/27 从三个不同界面进入编辑工作名片的区分 end

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //add by linzhangbin 2017/6/12 取消订阅
        if (sbpList != null && sbpList.size() != 0) {
            for (Subscription subscription : sbpList) {
                if (!subscription.isUnsubscribed()) {
                    subscription.unsubscribe();
                }
            }
            sbpList.clear();
        }
        //add by linzhangbin 2017/6/12 取消订阅 end
        unbinder.unbind();
    }

    private void commit() {
        //add by linzhang bin 2017/6/7 省市区加上填写的地址一起提交
        tvSaveAddress.setClickable(false);
        String companyName = etCompanyName.getText().toString();
        String job = etJobPosition.getText().toString();
        String area = tvMyLocation.getText().toString();
        String address = etDetailLocation.getText().toString();
        final User.VisitingCard workCard = new User.VisitingCard(companyName,job,address);
        workCard.setArea(area);

        if (area.equals("请选择")) {
            //modify by linzhangbin 2017/6/8 保存按钮重置可被点击
            ToastUtil.show(activity, "请选择所在地区");
            tvSaveAddress.setClickable(true);
        } else if (address.length() < 5) {
            Timber.tag(TAG).i("详细地址字数小于5");
            ToastUtil.show(activity, "详细地址不可低于五个字");
            tvSaveAddress.setClickable(true);
            //modify by linzhangbin 2017/6/8 保存按钮重置可被点击
        } else {
            Timber.tag(TAG).i("commit~~~~~~");
            if (tag.equals(AddCardFragment.class.getSimpleName())) { //添加名片
                Timber.tag(TAG).i("commit~~~~~~添加名片~~~~");

                RxBus.get().post(EventToken.SAVE_WORK_CARD_ADDNEW, workCard);
                tvSaveAddress.setClickable(true);
                activity.removeSecondFragment();

            } else if (tag.equals(MyCardFragmentNew.class.getSimpleName())) {
                Timber.tag(TAG).i("commit~~~~~~我的名片~~~~");
                //add by linzhang bin 2017/6/7 省市区加上填写的地址一起提交
                Timber.tag(TAG).i("companyName : " + companyName + "/n" + "job : " + job + "/n" +
                        "address : " + address + "area" + workCard.getArea());
                //modify by linzhangbin 2017/6.29 修改自己的名片不用传手机号 start
                Subscription sbp = UserRepository.getInstance(activity).saveVisitingCard(null,workCard)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new DefaultSubscribe<Boolean>(TAG) {

                            @Override
                            public void onNext(Boolean aBoolean) {
                                if (aBoolean) {
                                    ToastUtil.show(activity, "保存成功!");
                                    tvSaveAddress.setClickable(true);
                                    RxBus.get().post(EventToken.SAVE_WORK_CARD_MYCARD, workCard);
                                    //add by linzhangbin 2017/6/7 保存名片成功返回 2017/6/8收起软键盘
                                    TextUtils.showOrHideSoftIM(tvSaveAddress, false);
                                    activity.removeSecondFragment();
                                    //add by linzhangbin 2017/6/7 保存名片成功返回 end
                                }
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                super.onError(throwable);
                                tvSaveAddress.setClickable(true);
                                ToastUtil.show(activity, "保存失败,请稍候再试");
                            }
                        });
                //modify by linzhangbin 2017/6.29 修改自己的名片不用传手机号 end
                sbpList.add(sbp);
            } else { //联系人详情
                Timber.tag(TAG).i("commit~~~~~~联系人详情~~~~");
                Subscription sbp = UserRepository.getInstance(activity).saveVisitingCard(contactPhone,workCard)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new DefaultSubscribe<Boolean>(TAG) {

                            @Override
                            public void onNext(Boolean aBoolean) {
                                if (aBoolean) {
                                    ToastUtil.show(activity, "保存成功!");
                                    tvSaveAddress.setClickable(true);
                                    RxBus.get().post(EventToken.SAVE_WORK_CARD_DETAIL, workCard);
                                    //add by linzhangbin 2017/6/7 保存名片成功返回 2017/6/8收起软键盘
                                    TextUtils.showOrHideSoftIM(tvSaveAddress, false);
                                    activity.removeSecondFragment();
                                    //add by linzhangbin 2017/6/7 保存名片成功返回 end
                                }
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                super.onError(throwable);
                                tvSaveAddress.setClickable(true);
                                ToastUtil.show(activity, "保存失败,请稍候再试");
                            }
                        });
                //modify by linzhangbin 2017/6.29 修改自己的名片不用传手机号 end
                sbpList.add(sbp);
            }

        }
    }


    @OnClick({R.id.tv_my_location, R.id.tv_save_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_my_location:
//                activity.addSecondFragment(new ProvinceFragment());
                Intent intent = new Intent(activity, ChooseAddressActivity.class);
                //moidify by linzhangbin 2017/6/12 区分两个页面进入选择地区界面
                Bundle bundle = new Bundle();
                bundle.putString("chooseAddress", "EditMyWorkCard");
                intent.putExtras(bundle);
                //moidify by linzhangbin 2017/6/12 区分两个页面进入选择地区界面 end
                startActivity(intent);

                break;
            case R.id.tv_save_address:
                commit();
        }
    }

    @Subscribe(tags = {
            @Tag(EventToken.CHOOSEAREA_WORKCARD)
    })
    public void chooseAreaComplete(String area) {
        Timber.tag(TAG).i("chooseAreaComplete :" + area);
        tvMyLocation.setText(area);
    }
}
