package com.vboss.okline.ui.contact.myCard;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.vboss.okline.data.entities.AddableBankInfo;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.data.local.UserLocalDataSource;
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
 * Date : 2017/6/8 15:30
 * Desc :
 */

public class EditAccountFragment extends BaseFragment {
    private static final String TAG = "EditAccountFragment";
    @BindView(R.id.toolbar_contact_account)
    FragmentToolbar toolbar;
    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
    @BindView(R.id.ll_create_con_phone)
    RelativeLayout llCreateConPhone;
    @BindView(R.id.et_my_account)
    ClearEditText etMyAccount;
    @BindView(R.id.ll_account)
    LinearLayout llAccount;
    @BindView(R.id.ll_choose_bank)
    LinearLayout llChooseBank;
    @BindView(R.id.tv_save_bank)
    TextView tvSaveBank;
    Unbinder unbinder;
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;
    @BindView(R.id.et_account_name)
    EditText etAccountName;
    private MainActivity activity;
    private List<Subscription> subscriptionList;
    //原集合位置的数据
    private int position;
    //传过来的账号数据集合
    private ArrayList<User.BankAccount> list;
    private int editOrCreate;
    private boolean isError = false;
    private String bankIcon;
    private String icon;
    private User.BankAccount bankAccount;
    //用于记录用户点击选择银行次数,第一次自动匹配,之后的全部自行选择
    private int autoCompareAccount = 0;
    //记录第一次自动匹配的账号如果变动则取消之前匹配的银行
    private String firstAccount;
    //用于记录从新建名片,编辑名片,还是我的名片过来的
    private String tag;
    private String phone;

    public static Fragment newInstance(User.BankAccount bankAccount, int type, int position,
                                       SerializableObject<List<User.BankAccount>> list,String tag,String phone) {
        EditAccountFragment fragment = new EditAccountFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bankAccount", bankAccount);
        //type 0 为编辑 1 为新增
        bundle.putInt("editOrCreate", type);
        bundle.putInt("position", position);
        bundle.putString("bankIcon", bankAccount.getBankIcon());
        bundle.putString("classTag",tag);
        bundle.putString("phone",phone);
        bundle.putSerializable("serializableList", list);
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
        View view = inflater.inflate(R.layout.contact_my_account_information, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initToolbar();
        initContent();
//        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (null != bundle) {
            User.BankAccount bankAccount = (User.BankAccount) bundle.getSerializable("bankAccount");
            editOrCreate = bundle.getInt("editOrCreate");
            position = bundle.getInt("position");
            icon = bundle.getString("bankIcon");
            tag = bundle.getString("classTag");
            phone = bundle.getString("phone");
            Timber.tag(TAG).i("editOrCreate : %s , position : %s , icon : %s , tag : %s",editOrCreate,position,icon,tag);
            SerializableObject<ArrayList<User.BankAccount>> obj =
                    (SerializableObject<ArrayList<User.BankAccount>>) bundle.getSerializable("serializableList");
            list = obj.getObj();
            Timber.tag(TAG).i("initData:" + list.size());
            if (0 == editOrCreate) {
                //编辑 展示右上角删除按钮
                toolbar.setActionMenuIcon(R.mipmap.delete_account);
                toolbar.setActionMenuVisible(View.VISIBLE);
            } else {
                //新增 隐藏右上角删除按钮
                toolbar.setActionMenuVisible(View.GONE);
            }
            if (null != bankAccount) {
                String account = bankAccount.getCardNo();
                String bank = bankAccount.getBankName();
                String name = bankAccount.getCardName();
                etMyAccount.setText(account);
                etAccountName.setText(name);
                tvBankName.setText(bank);
            }
        }
        subscriptionList = new ArrayList<>();
    }

    private void initToolbar() {
        //modify by linzhangbin 2017/6/28 区分页面显示不同标题 start
        if (tag.equals(MyCardFragmentNew.class.getSimpleName())){
            toolbar.setActionTitle(getResources().getString(R.string.title_my_account_information));
        }else{
            toolbar.setActionTitle(getResources().getString(R.string.title_add_account_information));
        }
        //modify by linzhangbin 2017/6/28 区分页面显示不同标题 end
        toolbar.setActionMenuClickable(false);
        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
            @Override
            public void onNavigation(View v) {
                TextUtils.showOrHideSoftIM(toolbar, false);
                activity.removeSecondFragment();
            }
        });
        toolbar.setOnActionMenuClickListener(new FragmentToolbar.OnActionMenuClickListener() {
            @Override
            public void onActionMenu(View v) {
                toolbar.setActionMenuClickable(false);
                //删除账户
//                list.remove(position);
                final ArrayList<User.BankAccount> bankAccounts = list;
                bankAccounts.remove(position);
                if (tag.equals(MyCardFragmentNew.class.getSimpleName())){
                    //modify by linzhangbin 2017/6.29 修改自己的名片不用传手机号 start
                    deleteAccount(null,EventToken.SAVE_MY_ACCOUNT,bankAccounts);
                    //modify by linzhangbin 2017/6.29 修改自己的名片不用传手机号 end
                }else if (tag.equals(AddCardFragment.class.getSimpleName())){ //添加名片
//                    list.remove(position);
                    RxBus.get().post(EventToken.SAVE_ADD_ACCOUNT,list);
                    activity.removeSecondFragment();

                }else{ //编辑联系人名片
                    deleteAccount(phone,EventToken.DELETE_DETAIL_ACCOUNT,bankAccounts);
                }


            }
        });
    }

    /**
     * 删除帐号
     * @param phone 电话号码
     * @param eventToken 标识
     * @param bankAccounts 账户的集合(用于提交)
     */
    private void deleteAccount(@Nullable String phone, final String eventToken, ArrayList<User.BankAccount> bankAccounts){
        UserRepository.getInstance(OKLineApp.context)
                .saveBankInfo(phone,bankAccounts)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<Boolean>(TAG) {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            ToastUtil.show(activity, "删除成功!");
                            Timber.tag(TAG).i("initData1111:" + list.size());
                            RxBus.get().post(eventToken, list);
                            TextUtils.showOrHideSoftIM(tvSaveBank, false);
                            activity.removeSecondFragment();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtil.show(activity,"删除失败!");
                        toolbar.setActionMenuClickable(true);
                    }
                });


    }

    private void initContent() {
        //modify by linzhangbin 2017/6/28 区分不同页面第一行户名的显示不同 start
        if (tag.equals(MyCardFragmentNew.class.getSimpleName())){
            etAccountName.setVisibility(View.GONE);
            tvCompanyName.setText(UserLocalDataSource.getInstance().getUser().getRealName());
            etMyAccount.setFocusable(true);
            etMyAccount.setFocusableInTouchMode(true);
            etMyAccount.requestFocus();
            InputMethodManager inputManager = (InputMethodManager) etMyAccount.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(etMyAccount, 0);
        }else{
            etAccountName.setVisibility(View.VISIBLE);
            etAccountName.setFocusable(true);
            etAccountName.setFocusableInTouchMode(true);
            etAccountName.requestFocus();
            InputMethodManager inputManager = (InputMethodManager) etAccountName.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(etAccountName, 0);
        }
        //modify by linzhangbin 2017/6/28 区分不同页面第一行户名的显示不同 end

        //add by linzhangbin 2017/6/20 改变银行卡号把之前选择的银行置空
        //账号的监听
        etMyAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (autoCompareAccount!=0 && !firstAccount.equals(editable.toString())){
                    tvBankName.setText("");
                }
            }
        });
        //add by linzhangbin 2017/6/20 改变银行卡号把之前选择的银行置空 end
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        for (Subscription subscription : subscriptionList) {
            if (null != subscription && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
       }
        unbinder.unbind();
    }

    @OnClick({R.id.ll_choose_bank, R.id.tv_save_bank})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_choose_bank:
                //modify by linzhangbin 2017/6/20 自动匹配
                TextUtils.showOrHideSoftIM(llChooseBank, false);
                Timber.tag(TAG).i("choose bank clicked : " + etMyAccount.getText().toString());
                if (autoCompareAccount == 0 && !StringUtils.isNullString(etMyAccount.getText().toString())) {

                    firstAccount = etMyAccount.getText().toString();
                    //第一次自动匹配银行卡商户名
                    UserRepository.getInstance(OKLineApp.context)
                            .queryBankMerchantName(firstAccount)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new DefaultSubscribe<AddableBankInfo>(TAG){
                                @Override
                                public void onNext(AddableBankInfo addableBankInfo) {
                                    Timber.tag(TAG).i("queryBankMerchantName 自动匹配中.... : " + addableBankInfo.toString());
                                    if (!TextUtils.isEmpty(addableBankInfo.getMerName())){
                                        tvBankName.setText(addableBankInfo.getMerName());
                                        Timber.tag(TAG).i("设置银行名称");
                                    }else{
                                        activity.addSecondFragment(new ChooseBankFragment());
                                        Timber.tag(TAG).i("进入选择银行界面");
                                    }
                                    icon = addableBankInfo.getMerIcon();

                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    super.onError(throwable);
                                    //匹配失败直接进入自动选择银行
                                    activity.addSecondFragment(new ChooseBankFragment());
                                }
                            });
                    autoCompareAccount++;

                }else{
                    //之后不再自动匹配,除了第一次,之后全部由用户自己选择银行
                    activity.addSecondFragment(new ChooseBankFragment());
                }

                break;
            case R.id.tv_save_bank:
                tvSaveBank.setClickable(false);
                //modify by linzhangbin 2017/7/5 此处应取edittext的内容 start
//                String name = tvCompanyName.getText().toString().trim();
                String name = etAccountName.getText().toString().trim();
                //modify by linzhangbin 2017/7/5 此处应取edittext的内容 end
                String account = etMyAccount.getText().toString().trim();
                final String bank = tvBankName.getText().toString();

                if (StringUtils.isNullString(bankIcon)){
                    if (StringUtils.isNullString(icon)){
                        bankIcon = "";
                    }else{
                        bankIcon = icon;
                    }
                }
                bankAccount = new User.BankAccount(name, account, bank,bankIcon);
//                Timber.tag(TAG).i("bankIcon:"+bankIcon);
                //modify by linzhangbin 2017/6/15 未保存成功第二次不可点击保存的bug
                if (TextUtils.isEmpty(account)) {
                    ToastUtil.show(activity, "账号不可为空");
                    tvSaveBank.setClickable(true);
                } else if (TextUtils.isEmpty(bank)) {
                    ToastUtil.show(activity, "请选择开户银行");
                    tvSaveBank.setClickable(true);
                    //modify by linzhangbin 2017/6/15 未保存成功第二次不可点击保存的bug end
                } else {

                    if (0 == editOrCreate) { // 0为编辑 1为新增
                        list.set(position, bankAccount);
//                        Timber.tag(TAG).i("set list 0:" + list.size());

                    } else {
                        if (!isError){
                            list.add(bankAccount);
                        }
//                        Timber.tag(TAG).i("set list 1:" + list.size());
                    }

                    if (tag.equals(MyCardFragmentNew.class.getSimpleName())){
                        //modify by linzhangbin 2017/6.29 修改自己的名片不用传手机号 start
                        UserRepository.getInstance(OKLineApp.context)
                                .saveBankInfo(null,list)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new DefaultSubscribe<Boolean>(TAG) {
                                    @Override
                                    public void onNext(Boolean aBoolean) {
                                        tvSaveBank.setClickable(true);
                                        if (aBoolean) {
                                            ToastUtil.show(activity, "保存成功!");
                                            for (User.BankAccount bankAccount1 : list) {
                                                Timber.tag(TAG).i("onSaveAccount:!!!" + bankAccount1.toString());
                                            }
                                            RxBus.get().post(EventToken.SAVE_MY_ACCOUNT, list);
                                            TextUtils.showOrHideSoftIM(tvSaveBank, false);
                                            activity.removeSecondFragment();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        super.onError(throwable);
                                        tvSaveBank.setClickable(true);
                                        isError = true;
                                    }
                                });
                        //modify by linzhangbin 2017/6.29 修改自己的名片不用传手机号 end
                    }else if (tag.equals(AddCardFragment.class.getSimpleName())){
                        RxBus.get().post(EventToken.SAVE_ADD_ACCOUNT,list);
                        tvSaveBank.setClickable(true);
                        activity.removeSecondFragment();
                    }else{
                        //modify by linzhangbin 2017/7/4 修改好友帐号信息 start
                        UserRepository.getInstance(OKLineApp.context)
                                .saveBankInfo(phone,list)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new DefaultSubscribe<Boolean>(TAG) {
                                    @Override
                                    public void onNext(Boolean aBoolean) {
                                        tvSaveBank.setClickable(true);
                                        if (aBoolean) {
                                            ToastUtil.show(activity, "保存成功!");
                                            RxBus.get().post(EventToken.SAVE_DETAIL_ACCOUNT, list);
                                            TextUtils.showOrHideSoftIM(tvSaveBank, false);
                                            activity.removeSecondFragment();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        super.onError(throwable);
                                        tvSaveBank.setClickable(true);
                                        isError = true;
                                    }
                                });

                        //modify by linzhangbin 2017/7/4 修改好友帐号信息 end
                    }

                }
                break;
        }
    }


    @Subscribe(tags = {
            @Tag(EventToken.SELECT_BANK)
    })
    public void onSelectBank(AddableBankInfo item) {
        Timber.tag(TAG).i(item.toString());
        if (!TextUtils.isEmpty(item.getMerIcon())){
            bankIcon = item.getMerIcon();
        }else{
            bankIcon = icon;
        }

        tvBankName.setText(item.getMerName());
    }
}
