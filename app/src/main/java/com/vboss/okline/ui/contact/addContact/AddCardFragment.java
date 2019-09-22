package com.vboss.okline.ui.contact.addContact;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.vboss.okline.data.ContactRepository;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.data.local.ContactLocalDataSource;
import com.vboss.okline.data.local.UserLocalDataSource;
import com.vboss.okline.ui.contact.adapter.AccountAdapter;
import com.vboss.okline.ui.contact.adapter.ContactCardListAdapter;
import com.vboss.okline.ui.contact.bean.AccountItem;
import com.vboss.okline.ui.contact.bean.ContactItem;
import com.vboss.okline.ui.contact.myCard.EditDeliveryInfoFragment;
import com.vboss.okline.ui.contact.myCard.EditMyWorkCardFragment;
import com.vboss.okline.ui.contact.myCard.SelectAccountFragment;
import com.vboss.okline.ui.contact.myCard.SerializableObject;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.utils.StringUtils;
import com.vboss.okline.utils.TextUtils;
import com.vboss.okline.utils.ToastUtil;
import com.vboss.okline.view.widget.CommonDialog;
import com.vboss.okline.view.widget.ListViewForScrollView;

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

import static com.vboss.okline.R.id.tv_save_card;
import static com.vboss.okline.base.OKLineApp.context;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/6/8 18:33
 * Desc :
 */

public class AddCardFragment extends BaseFragment {
    private static final String TAG = "AddCardFragment";

    @BindView(R.id.toolbar_contact_addcard)
    FragmentToolbar toolbar;
    @BindView(R.id.iv_contact_avatar_big)
    SimpleDraweeView ivContactAvatarBig;
    @BindView(R.id.tv_edit_name)
    TextView tvEditName;
    @BindView(R.id.tv_edit_phone)
    TextView tvEditPhone;
    @BindView(R.id.iv_edit_card)
    ImageView ivEditCard;
    @BindView(R.id.iv_other_connection)
    ImageView ivOtherConnection;
    @BindView(R.id.lv_phone)
    ListViewForScrollView lvPhone;
    Unbinder unbinder;
    @BindView(R.id.iv_pencil_workCard)
    ImageView ivPencilWorkCard;
    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_pencil_account)
    ImageView ivPencilAccount;
    @BindView(R.id.lv_count_information)
    ListViewForScrollView lvCountInformation;
    @BindView(R.id.iv_edit_delivery)
    ImageView ivEditDelivery;
    @BindView(R.id.lv_express_info)
    ListViewForScrollView lvExpressInfo;
    @BindView(R.id.tv_my_delivery_info)
    TextView tvMyDeliveryInfo;
    @BindView(tv_save_card)
    TextView tvSaveCard;
    private List<Subscription> subscriberList;
    private MainActivity activity;
    private List<ContactEntity> allContact = new ArrayList<>();
    //其他电话的集合
    private ArrayList<String> phoneList = new ArrayList<>();
    private ContactItem contactItem = new ContactItem();
    private User.VisitingCard workCard = new User.VisitingCard("","","");
    private User.BankAccount bankAccount = new User.BankAccount("","","","");
    private SerializableObject<List<User.BankAccount>> list
            = new SerializableObject<List<User.BankAccount>>(new ArrayList<User.BankAccount>());
    private ContactEntity.Addition otherConnections = new ContactEntity.Addition();
    private ContactEntity.Addition workInfo = new ContactEntity.Addition();
    private ContactEntity.Addition accountInfo = new ContactEntity.Addition();
    private ContactEntity.Addition deliveryAddress = new ContactEntity.Addition();
    //快递地址的集合
    private ArrayList<String> deliveryList = new ArrayList<>();


    //帐号的集合
    private List<AccountItem> accountList = new ArrayList<>();
    //地区
    private String area = "";
    //详细地址
    private String location = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_card_activity, null);
        unbinder = ButterKnife.bind(this, view);
        activity = (MainActivity) getActivity();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initToolbar();
    }

    private void initData() {
        subscriberList = new ArrayList<>();
        Subscription sbp = ContactLocalDataSource.getInstance(activity).getAllContact()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<List<ContactEntity>>(TAG) {
                    @Override
                    public void onNext(List<ContactEntity> list) {
                        allContact = list;
                    }
                });
        //TODO 这里直接用contactEntity? 另外如果用contactItem要加工作名片等字段或者用user里面的传过去
        contactItem.setRemark("");
        contactItem.setPhoneNum("");
        contactItem.setRelationship("");
        contactItem.setNickName("");
        subscriberList.add(sbp);
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
                                activity.removeSecondFragment();
                            }
                        }).show();

            }
        });

    }

    @OnClick({R.id.iv_edit_card, R.id.iv_other_connection, R.id.tv_save_card,R.id.iv_pencil_workCard,
            R.id.iv_pencil_account, R.id.iv_edit_delivery})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_edit_card: //编辑电话姓名关系
                activity.addSecondFragment(EditAddInfoFragment.newInstance(contactItem.getRemark(),
                        contactItem.getPhoneNum(),contactItem.getRelationship()));

                break;
            case R.id.iv_other_connection: //其他联系方式
                activity.addSecondFragment(EditAddPhoneFragment.newInstance(TAG,phoneList,""));

                break;

            case R.id.iv_pencil_workCard: //编辑工作名片
                activity.addSecondFragment(EditMyWorkCardFragment.newInstance(AddCardFragment.class.getSimpleName(),"", workCard));

                break;
            case R.id.iv_pencil_account:  //编辑帐号信息
                Timber.tag(TAG).i("list size :" + list.getObj().size());
                activity.addSecondFragment(SelectAccountFragment.newInstance(AddCardFragment.class.getSimpleName(),"",list));
                break;
            case R.id.iv_edit_delivery:  //编辑快递信息

                activity.addSecondFragment(EditDeliveryInfoFragment.newInstance(AddCardFragment.class.getSimpleName(),area,location,""));


                break;


            case tv_save_card: //保存名片
                //第一步拿到姓名和输入的手机号码
                final String remarks = tvEditName.getText().toString().trim();
                final String phoneNum = tvEditPhone.getText().toString().trim();
                String phone = UserLocalDataSource.getInstance().getUser().getPhone();
                boolean isChineseName = TextUtils.isChineseName(remarks);
                final boolean isPhoneNumber = TextUtils.isPhoneNumber(phoneNum);
                final boolean nullName = StringUtils.isNullString(remarks);
                final boolean nullPhoneNum = StringUtils.isNullString(phoneNum);
                final boolean isYourSelf = phoneNum.equals(phone);
                ContactEntity entity = ContactEntity.newBuilder()
                        .remarkName(remarks)
                        .operatType(3)
                        .phone(phoneNum)
                        .build();
                boolean sameFriend = allContact.contains(entity);


                if (nullName || nullPhoneNum) {
                    ToastUtil.show(activity, R.string.name_phone_isnull);
                } else if (isYourSelf) {
                    ToastUtil.show(activity, R.string.cannot_add_yourself);
                } else if (sameFriend) {
                    ToastUtil.show(activity, R.string.existed_contact);
                    //add by linzhangbin   2017/6/9 空保存名片问题
                } else if (tvEditPhone.getText().toString().trim().equals("编辑号码")) {
                    ToastUtil.show(activity, R.string.name_phone_isnull);
                    //add by linzhangbin   2017/6/9 空保存名片问题
                } else {
                    tvSaveCard.setClickable(false);
                    commit(entity);
                }

                break;
        }
    }

    /**
     * 向服务器提交
     */
    private void commit(ContactEntity entity) {
//        Subscription sbp = ContactRepository.getInstance(activity).addOrUpdateContact(entity)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new DefaultSubscribe<ContactEntity>(TAG) {
//                    @Override
//                    public void onNext(ContactEntity entity) {
//                        Timber.tag(TAG).i("创建联系人成功");
//                        tvSaveCard.setClickable(true);
//                        if (entity.relationState() > 1) {
//                            ToastUtil.show(activity, R.string.notice_addCon_success_forVip);
//                        } else {
//                            ToastUtil.show(activity, R.string.notice_addCon_success);
//                        }
//                        RxBus.get().post(EventToken.ADD_NEW_CONTACT, true);
//                        activity.removeSecondFragment();
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        super.onError(throwable);
//                        tvSaveCard.setClickable(true);
//                        ToastUtil.show(context, "创建好友失败");
//                    }
//                });

        //add by linzhangbin 2017/7/4 添加联系人提交所有编辑信息 start
        List<ContactEntity.Addition> list = new ArrayList<>();
        list.add(otherConnections);
        list.add(workInfo);
        list.add(accountInfo);
        list.add(deliveryAddress);
        //modify by linzhangbin 2017/7/6 创建联系人加入nickName(备注)字段 start
        Subscription sbp = ContactRepository.getInstance(OKLineApp.context)
                .addContact(contactItem.getPhoneNum(),contactItem.getRemark(),contactItem.getNickName(),list)
        //modify by linzhangbin 2017/7/6 创建联系人加入nickName(备注)字段 end
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<Boolean>(TAG){

                    @Override
                    public void onNext(Boolean aBoolean) {
                        super.onNext(aBoolean);
                        tvSaveCard.setClickable(true);
                        if (aBoolean) {
                            Timber.tag(TAG).i("创建联系人成功");
                            ToastUtil.show(activity,R.string.notice_addCon_success);
                        }
                        RxBus.get().post(EventToken.ADD_NEW_CONTACT, true);
                        activity.removeSecondFragment();

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        tvSaveCard.setClickable(true);
                        ToastUtil.show(context, "创建好友失败");
                    }
                });
        subscriberList.add(sbp);
        //add by linzhangbin 2017/7/4 添加联系人提交所有编辑信息 end
    }


    @Subscribe(tags = {@Tag(EventToken.SAVE_ADDED_PHONE)})
    public void saveAddedPhone(ArrayList<String> list) {
        phoneList = list;
        if (list.isEmpty()) {
            lvPhone.setVisibility(View.GONE);
        } else {
            ContactCardListAdapter adapter = new ContactCardListAdapter(activity, list);
            otherConnections.setDataType(ContactEntity.Addition.DATA_TYPE_PHONE);
            otherConnections.setContent(list);
            lvPhone.setAdapter(adapter);
            lvPhone.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe(tags = {@Tag(EventToken.SAVE_PHONE_NAME)})
    public void savePhoneName(ContactItem item) {
        contactItem = item;
        tvEditName.setText(item.getRemark()+"("+item.getNickName()+")");
        tvEditPhone.setText(item.getPhoneNum());
    }

    @Subscribe(tags = {
            @Tag(EventToken.SAVE_WORK_CARD_ADDNEW)
    })
    public void onWorkCardSaved(User.VisitingCard workCard) {
        this.workCard = workCard;
        Timber.tag(TAG).i("onWorkCardSaved :" + workCard.toString());
        String company = workCard.getCompany();
        String position = workCard.getPosition();
        String area = workCard.getArea();
        String location = workCard.getAddress();
        if (StringUtils.isNullString(company)){
            tvCompany.setVisibility(View.GONE);
        }else{
            tvCompany.setVisibility(View.VISIBLE);
        }

        if (StringUtils.isNullString(position)){
            tvPosition.setVisibility(View.GONE);
        }else{
            tvPosition.setVisibility(View.VISIBLE);
        }
        if (StringUtils.isNullString(area) && StringUtils.isNullString(location)){
            tvAddress.setVisibility(View.GONE);
        }else{
            tvAddress.setVisibility(View.VISIBLE);
        }
        //add by linzhangbin 2017/7/4 记录工作名片内容用于提交给服务器 start
        workInfo.setDataType(ContactEntity.Addition.DATA_TYPE_CARD);
        workInfo.setContent(workCard);
        //add by linzhangbin 2017/7/4 记录工作名片内容用于提交给服务器 end
        tvCompany.setText(company);
        //modify by linzhangbin change color 2017/6/22
        tvPosition.setTextColor(getResources().getColor(R.color.button_green));
        //modify by linzhangbin change color 2017/6/22
        tvPosition.setText(position);
        tvAddress.setText(area + "" + location);
    }

    @Subscribe(tags = {
            @Tag(EventToken.SAVE_ADD_ACCOUNT)
    })
    public void onSaveAddAccount(ArrayList<User.BankAccount> bankList) {
        accountList.clear();
        for (User.BankAccount bankAccount : bankList) {
            AccountItem item = new AccountItem();
            item.setBank(bankAccount.getBankName());
            item.setAccount(bankAccount.getCardNo());
            item.setIcon(bankAccount.getBankIcon());
            accountList.add(item);
        }
        //add by linzhangbin 2017/7/4 保存其他帐号信息用于提交服务器 start
        accountInfo.setDataType(ContactEntity.Addition.DATA_TYPE_BANK);
        accountInfo.setContent(accountList);
        //add by linzhangbin 2017/7/4 保存其他帐号信息用于提交服务器 end
        list.setObj(bankList);
        Timber.tag(TAG).i("list size :~~~~" + list.getObj().size());
        AccountAdapter adapter = new AccountAdapter(activity, accountList,1);
        lvCountInformation.setAdapter(adapter);
        if (accountList.size()>0){
            lvCountInformation.setVisibility(View.VISIBLE);
        }

    }

    @Subscribe(tags = {
            @Tag(EventToken.SAVE_ADD_EXPRESS)
    })
    public void onSaveAddExpress(String location) {
        Timber.tag(TAG).i("onSaveAddExpress :" + location);
        String[] split = location.split(",");
        String area = split[0];
        String detailLocation = split[1];
        this.area = area;
        this.location = detailLocation;
        //add by linzhangbin 2017/7/4 保存快递信息用于提交服务器 start
        deliveryList.add(location);
        deliveryAddress.setDataType(ContactEntity.Addition.DATA_TYPE_EXPRESS);
        deliveryAddress.setContent(deliveryList);
        //add by linzhangbin 2017/7/4 保存快递信息用于提交服务器 end
        tvMyDeliveryInfo.setText(area+""+detailLocation);
        tvMyDeliveryInfo.setVisibility(View.VISIBLE);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
