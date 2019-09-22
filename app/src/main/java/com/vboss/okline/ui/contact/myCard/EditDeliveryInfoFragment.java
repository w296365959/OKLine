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
import com.vboss.okline.ui.contact.ContactDetail.ContactDetailFragment;
import com.vboss.okline.ui.contact.addContact.AddCardFragment;
import com.vboss.okline.ui.express.ExpressChooseAddressActivity;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.utils.TextUtils;
import com.vboss.okline.utils.ToastUtil;

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
 * Date : 2017/6/12 09:34
 * Desc :
 */

public class EditDeliveryInfoFragment extends BaseFragment {
    private static final String TAG = "EditDeliveryInfo";
    @BindView(R.id.toolbar_edit_delivery)
    FragmentToolbar toolbar;
    @BindView(R.id.tv_delivery_location)
    TextView tvDeliveryLocation;
    @BindView(R.id.ll_location_delivery)
    LinearLayout llLocationDelivery;
    @BindView(R.id.et_detail_location)
    EditText etDetailLocation;
    @BindView(R.id.tv_save_delivery)
    TextView tvSaveDelivery;
    Unbinder unbinder;
    private String tag;
    private String area;
    private String location;
    private String phone;

    public static Fragment newInstance(String tag, String area, String location, String phone) {
        EditDeliveryInfoFragment fragment = new EditDeliveryInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("classTag", tag);
        bundle.putString("area", area);
        bundle.putString("location", location);
        bundle.putString("phone", phone);
        fragment.setArguments(bundle);
        return fragment;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_delivery_info, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setActionTitle(getResources().getString(R.string.title_my_deliveryinfo));
        toolbar.setActionMenuVisible(View.GONE);
        toolbar.setActionMenuClickable(false);
        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
            @Override
            public void onNavigation(View v) {
                //关闭软键盘
                TextUtils.showOrHideSoftIM(toolbar, false);
                finishFragment();
            }
        });

        initData();
        initContent();
    }

    /**
     * Added by wangshuai 2017-07-06 close current page
     */
    private void finishFragment() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).removeSecondFragment();
        } else {
            getActivity().finish();
        }
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            tag = bundle.getString("classTag");
            area = bundle.getString("area");
            location = bundle.getString("location");
            phone = bundle.getString("phone");
        }

    }

    private void initContent() {
        //modify by linzhangbin 2017/6/20 默认弹出软键盘
        etDetailLocation.setFocusable(true);
        etDetailLocation.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) etDetailLocation.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(etDetailLocation, 0);
        if (tag.equals(MyCardFragmentNew.class.getSimpleName())) {
            //这里要拿到缓存中user的VisitingCard信息
            //modify by linzhangbin 2017/7/6 接口更改
            UserRepository.getInstance(OKLineApp.context)
                    .getContactVisitingCard(null)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new DefaultSubscribe<User>(TAG) {
                        @Override
                        public void onError(Throwable throwable) {
                            super.onError(throwable);
                        }

                        @Override
                        public void onNext(User user) {
                            List<String> express = user.getExpress();
                            //TODO 暂时只取第一条
                            if (null != express && !express.isEmpty()) {
                                String expressInfo = express.get(0);
                                String[] split = expressInfo.split(",");
                                String area = split[0];
                                String location = split[1];
                                tvDeliveryLocation.setText(area);
                                etDetailLocation.setText(location);
                                etDetailLocation.setSelection(location.length());
                            }
                        }
                    });
            //modify by linzhangbin 2017/6/20 默认弹出软键盘 end
        } else {
            toolbar.setActionTitle(getResources().getString(R.string.title_deliveryinfo));
            if (!TextUtils.isEmpty(area)) {
                tvDeliveryLocation.setText(area);
            }
            etDetailLocation.setText(location);
            etDetailLocation.setSelection(location.length());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_delivery_location, R.id.tv_save_delivery})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_delivery_location:
                Intent intent = new Intent(getActivity(), ChooseAddressActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("chooseAddress", "EditDeliveryInfo");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.tv_save_delivery:
                saveDeliveryAddress();

                break;
        }
    }

    private void saveDeliveryAddress() {
        tvSaveDelivery.setClickable(false);
        final String area = tvDeliveryLocation.getText().toString().trim();
        final String location = etDetailLocation.getText().toString().trim();
        final String info = area + "," + location;
        final List<String> express = new ArrayList<>();
        express.add(info);
        if (area.equals("请选择")) {
            ToastUtil.show(getActivity(), "请选择所在地区");
            tvSaveDelivery.setClickable(true);
        } else if (location.length() < 5) {
            ToastUtil.show(getActivity(), "详细地址不可低于五个字");
            tvSaveDelivery.setClickable(true);
        } else {
            Timber.tag(TAG).i("tag : " + tag);
            if (tag.equals(MyCardFragmentNew.class.getSimpleName())) {
                //此处调提交保存接口 将area和location上传
                //modify by linzhangbin 2017/6.29 修改自己的名片不用传手机号 start
                UserRepository.getInstance(getActivity()).saveExpress(null, express)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new DefaultSubscribe<Boolean>(TAG) {
                            @Override
                            public void onNext(Boolean aBoolean) {
                                tvSaveDelivery.setClickable(true);
                                if (aBoolean) {
                                    Timber.tag(TAG).i("express address : " + info);
                                    ToastUtil.show(getActivity(), "保存成功!");
                                    RxBus.get().post(EventToken.SAVE_EXPRESS, info);
                                    //add by linzhangbin 2017/6/7 保存名片成功返回 2017/6/8收起软键盘
                                    TextUtils.showOrHideSoftIM(tvSaveDelivery, false);
                                    finishFragment();
                                }
                            }
                            @Override
                            public void onError(Throwable throwable) {
                                super.onError(throwable);
                                tvSaveDelivery.setClickable(true);
                            }
                        });
                //modify by linzhangbin 2017/6.29 修改自己的名片不用传手机号 end
            } else if (tag.equals(AddCardFragment.class.getSimpleName())) {
                RxBus.get().post(EventToken.SAVE_ADD_EXPRESS, info);
                TextUtils.showOrHideSoftIM(tvSaveDelivery, false);
                tvSaveDelivery.setClickable(true);
                finishFragment();

            } else if (tag.equals(ContactDetailFragment.class.getSimpleName())) {
                saveExpressAddress(express, info, EventToken.SAVE_DETAIL_EXPRESS);
            }
            //modify by wangshuai 2017-07-06 add or edit send express address
            if (ExpressChooseAddressActivity.class.getSimpleName().equals(tag)) {
                saveExpressAddress(express, info, EventToken.EDIT_EXPRESS);
            }
        }
    }

    /**
     * Added by wangshuai 2017-07-06
     * save express address to server
     */
    private void saveExpressAddress(List<String> express, final String info, final String event) {
        UserRepository.getInstance(getActivity()).saveExpress(phone, express)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<Boolean>(TAG) {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        tvSaveDelivery.setClickable(true);
                        if (aBoolean) {
                            Timber.tag(TAG).i("express address : " + info);
                            ToastUtil.show(getActivity(), "保存成功!");
                            RxBus.get().post(event, info);
                            //add by linzhangbin 2017/6/7 保存名片成功返回 2017/6/8收起软键盘
                            TextUtils.showOrHideSoftIM(tvSaveDelivery, false);
                            finishFragment();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        tvSaveDelivery.setClickable(true);
                    }
                });
    }

    @Subscribe(tags = {@Tag(EventToken.CHOOSEAREA_DELIVERY)})
    public void chooseDeliveryAddress(String area) {
        Timber.tag(TAG).i("chooseAreaComplete :" + area);
        tvDeliveryLocation.setText(area);
    }
}
