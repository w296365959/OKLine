package com.vboss.okline.ui.contact.myCard;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.base.EventToken;
import com.vboss.okline.base.OKLineApp;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.data.CardRepository;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.ui.contact.adapter.AccountAdapter;
import com.vboss.okline.ui.contact.adapter.OtherPhoneAdapter;
import com.vboss.okline.ui.contact.bean.AccountItem;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.utils.StringUtils;
import com.vboss.okline.utils.TextUtils;
import com.vboss.okline.utils.ToastUtil;
import com.vboss.okline.view.widget.ListViewForScrollView;

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
 * Date : 2017/6/5 09:57
 * Desc :
 */

public class MyCardFragmentNew extends BaseFragment {
    private static final String TAG = "MyCardFragmentNew";
    @BindView(R.id.iv_contact_avatar_big)
    SimpleDraweeView ivContactAvatarBig;
    @BindView(R.id.tv_contact_name)
    TextView tvContactName;
    @BindView(R.id.tv_contact_phone)
    TextView tvContactPhone;
    @BindView(R.id.iv_pencil_otherPhone)
    ImageView ivPencilOtherPhone;
    @BindView(R.id.lv_other_phone)
    ListViewForScrollView lvOtherPhone;
    @BindView(R.id.iv_pencil_myWorkCard)
    ImageView ivPencilMyWorkCard;
    @BindView(R.id.lv_count_information)
    ListViewForScrollView lvCountInformation;
    @BindView(R.id.toolbar_contact_newcard)
    FragmentToolbar toolbar;
    Unbinder unbinder;
    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_pencil_account)
    ImageView ivPencilAccount;
    @BindView(R.id.lv_express_info)
    ListViewForScrollView lvExpressInfo;
    @BindView(R.id.iv_edit_delivery)
    ImageView ivEditDelivery;
    //add by linzhangbin 2017/6/12 我的快递信息
    @BindView(R.id.tv_my_delivery_info)
    TextView deliveryInfo;
    //add by linzhangbin 2017/6/12 我的快递信息 end

    private MainActivity activity;
    private AccountAdapter accountAdapter;
    private List<AccountItem> accountList = new ArrayList<>();
    private List<AccountItem> openedList = new ArrayList<>();
    private String area = "";
    private String detailLocation = "";
    private User.VisitingCard visitingCard = new User.VisitingCard("","","");
    private List<String> phoneList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contacts_my_card_new, null);
        ButterKnife.bind(this, view);
        activity.hideTabs(true);
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
        initContent();
    }

    /**
     * 初始化主界面
     */
    private void initContent() {
        User user = UserRepository.getInstance(activity).getUser();
        if (null != user) {
            Timber.tag(TAG).i("initContent:" + user.toString());
            if (!StringUtils.isNullString(user.getAvatar())) {
                ivContactAvatarBig.setImageURI(Uri.parse(user.getAvatar()));
            } else {
                ivContactAvatarBig.setImageResource(R.mipmap.default_avatar);
            }
            if (!StringUtils.isNullString(user.getRealName())) {
                tvContactName.setText(user.getRealName());
            } else {
                tvContactName.setText("");
            }
            if (!StringUtils.isNullString(user.getPhone())) {
                tvContactPhone.setText(user.getPhone());
            } else {
                tvContactPhone.setText("");
            }
        } else {
            Log.i("MyCardFragment", "initContent: user为null");
        }
        if (null != user.getOlNo()) {
            showCardList();
            //modify by linzhangbin 2017/6/21 获取用户个人信息
            UserRepository.getInstance(OKLineApp.context)
                    .getContactVisitingCard(null)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new DefaultSubscribe<User>(TAG) {

                        @Override
                        public void onNext(User user) {
                            Timber.tag(TAG).i("initContent: getOKLineMember:" + user.toString());
                            visitingCard = user.getVisitingCard();
                            phoneList = user.getSecondPhoneArray();
                            //modify by linzhangbin 2017/6/8 如果没有数据则不显示
                            showPhoneList(phoneList);
                            //modify by linzhangbin 2017/6/8 如果没有数据则不显示
                            showWorkCard(visitingCard);
                            showExpressList(user);

                            List<User.BankAccount> bankInfo = user.getBankInfo();
                            if (null != bankInfo){
                                Timber.tag(TAG).i("added account size : "+ bankInfo.size());
                                for (User.BankAccount bankAccount : bankInfo) {
                                    AccountItem item = new AccountItem();
                                    item.setBank(bankAccount.getBankName());
                                    item.setAccount(bankAccount.getCardNo());
                                    accountList.add(item);
                                }
                                Timber.tag(TAG).i("showBankInfoList : "+ accountList.size());
                                AccountAdapter accountAdapter = new AccountAdapter(activity,accountList,1);
                                lvCountInformation.setAdapter(accountAdapter);
                                if (accountList.isEmpty()){
                                    lvCountInformation.setVisibility(View.GONE);
                                }else{
                                    lvCountInformation.setVisibility(View.VISIBLE);
                                }
//                                MyCardFragmentNew.this.accountAdapter.refresh(accountList);

                            }
//                            if (accountList.isEmpty()){
//                                llAccountContent.setVisibility(View.GONE);
//                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            super.onError(throwable);
                            ToastUtil.show(activity,"获取数据失败!");
                        }
                    });
        }

    }

    /**
     * 我的工作名片
     * @param visitingCard
     */
    private void showWorkCard(User.VisitingCard visitingCard) {
        if (null != visitingCard) {
            Timber.tag(TAG).i("initContent: visitingCard:" + visitingCard.toString());
            String company = visitingCard.getCompany();
            String address = visitingCard.getAddress();
            String position = visitingCard.getPosition();
            String area = visitingCard.getArea();
            if (TextUtils.isEmpty(company)) {
                tvCompany.setVisibility(View.GONE);
            } else {
                tvCompany.setVisibility(View.VISIBLE);
                tvCompany.setText(company);
            }
            if (TextUtils.isEmpty(position)) {
                tvPosition.setVisibility(View.GONE);
            } else {
                tvPosition.setVisibility(View.VISIBLE);
                //modify by linzhangbin change color 2017/6/6
                tvPosition.setTextColor(activity.getResources().getColor(R.color.button_green));
                //modify by linzhangbin change color 2017/6/6
                tvPosition.setText(position);
            }
            if (TextUtils.isEmpty(address)) {
                tvAddress.setVisibility(View.GONE);
            } else {
                tvAddress.setVisibility(View.VISIBLE);
                tvAddress.setText(area + "" + address);
            }

            //add by linzhangbin 2017/6/13
            RxBus.get().post(EventToken.SHOW_USER_INFO,!TextUtils.isEmpty(company) && !TextUtils.isEmpty(address)
                        && !TextUtils.isEmpty(position));
            //add by linzhangbin 2017/6/13 end
        } else {
            tvCompany.setVisibility(View.GONE);
            tvPosition.setVisibility(View.GONE);
            tvAddress.setVisibility(View.GONE);
        }
    }

    /**
     * 我的快递地址信息
     */
    private void showExpressList(User user) {
        deliveryInfo.setVisibility(View.GONE);
        //TODO 这里用list显示 创建一个adapter lvExpressInfo
        List<String> express = user.getExpress();
        if (null != express && !express.isEmpty()){
            lvExpressInfo.setVisibility(View.VISIBLE);
            OtherPhoneAdapter adapter = new OtherPhoneAdapter(activity, express);
            lvExpressInfo.setAdapter(adapter);
        }else{
            lvOtherPhone.setVisibility(View.GONE);
        }


        /*if (null != express && !express.isEmpty()){
            for (String expres : express) {
                Timber.tag(TAG).i("快递地址:"+expres);
            }
            lvExpressInfo.setVisibility(View.GONE);
            String location = express.get(0);
            String[] split = location.split(",");
            String area = split[0];
            String detailLocation = split[1];
            deliveryInfo.setText(area+""+detailLocation);
            deliveryInfo.setVisibility(View.VISIBLE);


        }else{
            lvExpressInfo.setVisibility(View.GONE);
        }*/
    }

    /**
     * 我的帐号信息
     */
    private void showCardList() {
        CardRepository.getInstance(activity).cardList(CardType.BANK_CARD)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<List<CardEntity>>(TAG) {
                    @Override
                    public void onNext(List<CardEntity> cardEntities) {
                        //modify by linzhangbin 2017/6/8 如果没有数据则不显示
                        if (null != cardEntities && !cardEntities.isEmpty()) {
                            for (CardEntity cardEntity : cardEntities) {
                                AccountItem item = new AccountItem();
                                item.setBank(cardEntity.cardName());
                                item.setAccount(cardEntity.cardNo());
                                accountList.add(item);
                                openedList.add(item);

                            }
                            accountAdapter = new AccountAdapter(activity, accountList,1);
                            lvCountInformation.setAdapter(accountAdapter);
                            lvCountInformation.setVisibility(View.VISIBLE);
                        }
                        //modify by linzhangbin 2017/6/8 如果没有数据则不显示


                    }
                });
    }

    /**
     * 显示我的其他通信方式
     */
    private void showPhoneList(List<String> phoneList) {
        if (null != phoneList && !phoneList.isEmpty()){
            lvOtherPhone.setVisibility(View.VISIBLE);
            OtherPhoneAdapter adapter = new OtherPhoneAdapter(activity, phoneList);
            lvOtherPhone.setAdapter(adapter);
        }else{
            lvOtherPhone.setVisibility(View.GONE);
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        activity.hideTabs(false);

    }

    @OnClick({R.id.iv_pencil_otherPhone, R.id.iv_pencil_myWorkCard, R.id.iv_pencil_account,R.id.iv_edit_delivery})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_pencil_otherPhone:
                //跳转编辑其他电话页面
                activity.addSecondFragment(new EditOtherPhoneFragment());
                break;
            case R.id.iv_pencil_myWorkCard:
                //跳转编辑工作名片页面
                activity.addSecondFragment(EditMyWorkCardFragment.newInstance(MyCardFragmentNew.class.getSimpleName(),"",visitingCard));
                break;
            case R.id.iv_pencil_account:
                //跳转编辑帐号
//                activity.addSecondFragment(new EditAccountFragment());
                activity.addSecondFragment(SelectAccountFragment.newInstance(MyCardFragmentNew.class.getSimpleName(),"",null));
                break;
            //add by linzhangbin 2017/6/12 编辑我的快递信息
            case R.id.iv_edit_delivery:
                //TODO lvExpressInfo 这里把快递集合传过去
                activity.addSecondFragment(EditDeliveryInfoFragment.newInstance(MyCardFragmentNew.class.getSimpleName(),area,detailLocation,""));
                break;
            //add by linzhangbin 2017/6/12 编辑我的快递信息 end
        }
    }

    @Subscribe(tags = {
            @Tag(EventToken.SAVE_WORK_CARD_MYCARD)
    })
    public void onWorkCardSaved(User.VisitingCard visitingCard) {
        this.visitingCard = visitingCard;
        Timber.tag(TAG).i("onWorkCardSaved :" + visitingCard.toString());
        if (StringUtils.isNullString(visitingCard.getCompany())){
            tvCompany.setVisibility(View.GONE);
        }else{
            tvCompany.setVisibility(View.VISIBLE);
        }

        if (StringUtils.isNullString(visitingCard.getPosition())){
            tvPosition.setVisibility(View.GONE);
        }else{
            tvPosition.setVisibility(View.VISIBLE);
        }
        if (StringUtils.isNullString(visitingCard.getAddress()) && StringUtils.isNullString(visitingCard.getPosition())){
            tvAddress.setVisibility(View.GONE);
        }else{
            tvAddress.setVisibility(View.VISIBLE);
        }
        tvCompany.setText(visitingCard.getCompany());
        //modify by linzhangbin change color 2017/6/22
        tvPosition.setTextColor(getResources().getColor(R.color.button_green));
        //modify by linzhangbin change color 2017/6/22
        tvPosition.setText(visitingCard.getPosition());
        tvAddress.setText(visitingCard.getArea() + "" + visitingCard.getAddress());

    }

    @Subscribe(tags = {
            @Tag(EventToken.SAVE_OTHER_PHONE)
    })
    public void onOtherphoneSaved(ArrayList<String> list) {
        Timber.tag(TAG).i("onOtherphoneSaved :" + list.size());
        // modify by linzhangbin 2017/6/8 空保存之后界面显示一条白杠去除
        if (list.size() == 0) {
            lvOtherPhone.setVisibility(View.GONE);
        } else {
            showPhoneList(list);
        }
        // modify by linzhangbin 2017/6/8 空保存之后界面显示一条白杠去除 end
    }

    @Subscribe(tags = {
            @Tag(EventToken.SAVE_EXPRESS)
    })
    public void onSaveExpress(String location) {
        Timber.tag(TAG).i("onSaveExpress :" + location);
        String[] split = location.split(",");
        area = split[0];
        detailLocation = split[1];
        deliveryInfo.setText(area +""+ detailLocation);
        deliveryInfo.setVisibility(View.VISIBLE);
    }
    @Subscribe(tags = {
            @Tag(EventToken.SAVE_MY_ACCOUNT)
    })
    public void onSaveAccount(ArrayList<User.BankAccount> list) {
        //modify by linzhangbin 2017/6/22 帐号信息重复显示的问题
        accountList.clear();
        accountList.addAll(openedList);

        for (User.BankAccount bankAccount : list) {
            AccountItem item = new AccountItem();
            item.setBank(bankAccount.getBankName());
            item.setAccount(bankAccount.getCardNo());
            item.setIcon(bankAccount.getBankIcon());
            accountList.add(item);
        }

        AccountAdapter adapter = new AccountAdapter(activity, accountList,1);
        lvCountInformation.setAdapter(adapter);
        //modify by linzhangbin 2017/6/15  添加名片第一次不会显示的BUG
        if (accountList.size()>0){
            lvCountInformation.setVisibility(View.VISIBLE);
        }
        //modify by linzhangbin 2017/6/15  添加名片第一次不会显示的BUG end
        //modify by linzhangbin 2017/6/22 帐号信息重复显示的问题 end

    }


}
