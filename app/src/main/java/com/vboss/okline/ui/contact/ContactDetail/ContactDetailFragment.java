package com.vboss.okline.ui.contact.ContactDetail;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hyphenate.easeui.EaseConstant;
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
import com.vboss.okline.ui.contact.ContactsUtils;
import com.vboss.okline.ui.contact.HistoryChatFragment;
import com.vboss.okline.ui.contact.adapter.AccountAdapter;
import com.vboss.okline.ui.contact.adapter.ContactCardListAdapter;
import com.vboss.okline.ui.contact.adapter.OtherPhoneAdapter;
import com.vboss.okline.ui.contact.addContact.EditAddPhoneFragment;
import com.vboss.okline.ui.contact.bean.AccountItem;
import com.vboss.okline.ui.contact.bean.ContactItem;
import com.vboss.okline.ui.contact.callPhone.CallingActivity;
import com.vboss.okline.ui.contact.editRemark.EditRemarkFragment;
import com.vboss.okline.ui.contact.myCard.EditDeliveryInfoFragment;
import com.vboss.okline.ui.contact.myCard.EditMyWorkCardFragment;
import com.vboss.okline.ui.contact.myCard.SelectAccountFragment;
import com.vboss.okline.ui.contact.myCard.SerializableObject;
import com.vboss.okline.ui.express.ExpressActivity;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.utils.AppUtil;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static com.vboss.okline.ui.contact.ContactsFragment.isRefresh;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/3/31 09:23
 * Desc :
 */

public class ContactDetailFragment extends BaseFragment implements ContactDetailContract.View {
    private static final String TAG = "ContactDetailFragment";

    private static final int PERMISSION_CALLPHONE = 200;
    @BindView(R.id.sdv_detail_avatar)
    SimpleDraweeView sdvDetailAvatar;
    @BindView(R.id.tv_detail_remark)
    TextView tvDetailRemark;
    @BindView(R.id.iv_pencil)
    ImageView ivPencil;
    @BindView(R.id.iv_detail_phone)
    ImageView ivDetailPhone;
    @BindView(R.id.iv_detail_chat)
    ImageView ivDetailChat;
    @BindView(R.id.iv_detail_email)
    ImageView ivDetailEmail;
    @BindView(R.id.iv_detail_delivery)
    ImageView ivDetailDelivery;
    @BindView(R.id.iv_detail_transfer)
    ImageView ivDetailTransfer;
    @BindView(R.id.ll_card_detail)
    LinearLayout llCardDetail;
    @BindView(R.id.iv_upwards)
    ImageView ivUpwards;
    @BindView(R.id.ll_detail_show)
    LinearLayout llDetailShow;
    @BindView(R.id.tv_detail_realName)
    TextView tvDetailRealName;
    Unbinder unbinder;
    @BindView(R.id.tv_detail_realName2)
    TextView tvDetailRealName2;
    @BindView(R.id.tv_detail_phoneNum)
    TextView tvDetailPhoneNum;
    @BindView(R.id.toolbar_contact)
    FragmentToolbar toolbar;
    @BindView(R.id.iv_contact_state)
    ImageView ivContactState;
    @BindView(R.id.contact_transfer_account_num)
    TextView contactTransferAccountNum;
    @BindView(R.id.transfer_accounts_dot)
    RelativeLayout transferAccountsDot;
    @BindView(R.id.fl_chat_content)
    FrameLayout flChatContent;
    @BindView(R.id.ll_name_layout)
    LinearLayout llNameLayout;
    @BindView(R.id.iv_pencil1)
    ImageView ivPencil1;
    @BindView(R.id.lv_other_phone)
    ListViewForScrollView lvOtherPhone;
    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.tv_position)
    TextView tvPosition;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.lv_count_information)
    ListViewForScrollView lvCountInformation;
    @BindView(R.id.lv_express_info)
    ListViewForScrollView lvExpressInfo;
    //如果非可信好友这三项隐藏
    @BindView(R.id.tv_identity_info)
    TextView identityInfo;
    @BindView(R.id.ll_identity_name)
    LinearLayout identityName;
    @BindView(R.id.ll_identity_phone)
    LinearLayout identityPhone;
    @BindView(R.id.iv_pencil_otherPhone)
    ImageView ivPencilOtherPhone;
    @BindView(R.id.iv_pencil_workCard)
    ImageView ivPencilWorkCard;
    @BindView(R.id.iv_pencil_account)
    ImageView ivPencilAccount;
    @BindView(R.id.iv_pencil_delivery)
    ImageView ivPencilDelivery;
    @BindView(R.id.tv_my_delivery_info)
    TextView deliveryInfo;

    private MainActivity activity;
    private ContactDetailPresenter presenter;
    //    private Contact contact;
    private int contactID;
    private boolean upwards = true;
    private boolean star;
    private String remark;
    private String phone;
    private String avatar;
    private ContactEntity getEntity;
    private int friendId;
//    private HistoryChatFragment chatFragment;
    private String olNo;
    private String realName;
    private Integer relationState;
    private User user;
    private List<AccountItem> accountList = new ArrayList<>();
    private ArrayList<String> phoneList = new ArrayList<>();
    private User.VisitingCard visitingCard;
    private List<User.BankAccount> bankInfo;
    private List<String> express;
    private String area = "";
    private String detailLocation = "";
    private SerializableObject<List<User.BankAccount>> list
            = new SerializableObject<List<User.BankAccount>>(new ArrayList<User.BankAccount>());
    private String nickName;
    private ContactItem contactItem;
    private List<User.BankAccount> friendBankList;

    public static Fragment newInstance(int contactID,ContactItem item) {
        ContactDetailFragment instance = new ContactDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("contactID", contactID);
        bundle.putParcelable("contactItem",item);
//        args.putSerializable("obj", contact);
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_detail, null);
        activity = (MainActivity) getActivity();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //lzb edit 2017/5/9 获取缓存中的user对象,进入详情界面返回主界面则刷新通讯录
        user = UserLocalDataSource.getInstance().getUser();
        Bundle bundle = getArguments();
        ivDetailPhone.setClickable(false);
        ivDetailChat.setClickable(false);
        ivDetailDelivery.setClickable(false);
        ivDetailTransfer.setClickable(false);
        ivDetailEmail.setClickable(false);
        if (bundle != null) {
            contactID = bundle.getInt("contactID");
            contactItem = (ContactItem) bundle.getParcelable("contactItem");
        }
        presenter = new ContactDetailPresenter(this, new ContactDetailModel(), activity);
        presenter.initDetail(contactID);
        toolbar.setActionTitle(getResources().getString(R.string.title_contact_detal));
        toolbar.setNavigationVisible(View.VISIBLE);
        toolbar.setActionMenuVisible(View.VISIBLE);
        toolbar.setActionMenuIcon(R.mipmap.detail_star_common);
        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
            @Override
            public void onNavigation(View v) {
                activity.removeSecondFragment();
                //add by linzhangbin 2017/6/6 关闭软键盘
                TextUtils.showOrHideSoftIM(toolbar, false);
                //add by linzhangbin 2017/6/6 关闭软键盘
            }
        });
        toolbar.setOnActionMenuClickListener(new FragmentToolbar.OnActionMenuClickListener() {
            @Override
            public void onActionMenu(View v) {
                presenter.addToStar(contactID, star);
                toolbar.setActionMenuClickable(false);
            }
        });
        //add by linzhangbin 联系人详情先加载缓存中的信息
        ContactLocalDataSource.getInstance(activity).getContact(contactID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<ContactEntity>(TAG) {
                    @Override
                    public void onNext(ContactEntity entity) {
                        String realName = entity.realName();
                        String remarkName = entity.remarkName();
                        Integer relationState = entity.relationState();
                        String phone = entity.phone();
                        String acatar = entity.imgUrl();
                    }
                });
        //add by linzhangbin 联系人详情先加载缓存中的信息 end
    }


    @Override
    public void showDetail(ContactEntity entity) {
        //modify by linzhangbin 2017/7/7 如果联系人没有发生变化 则返回主界面不刷新页面 start
        if (!contactItem.equals(ContactsUtils.contactEtity2contactItem(entity))){
            isRefresh = true;
        }
        //modify by linzhangbin 2017/7/7 如果联系人没有发生变化 则返回主界面不刷新页面 end
        //fix bug1268
        ivPencil.setVisibility(View.VISIBLE);
        ivDetailPhone.setClickable(true);
        ivDetailChat.setClickable(true);
        ivDetailEmail.setClickable(true);
        ivDetailDelivery.setClickable(true);
        ivDetailTransfer.setClickable(true);
        //fix bug1268
        Log.i("ContactDetailFragment", "showDetail: " + entity.toString());
        remark = entity.remarkName();
        friendId = entity.friendId();
        olNo = entity.friendOlNo();
        realName = entity.realName();
        nickName = entity.nickName();
        phone = entity.phone();
        avatar = entity.imgUrl();
        relationState = entity.relationState();
        //add by linzhangbin 2017/7/3 获取联系人名片详情 start
        presenter.getContactVisitingCard(phone);
        //add by linzhangbin 2017/7/3 获取联系人名片详情 end
        //modify by linzhangbin 2017/6/9 根据产品UI调整图标全为绿色逻辑不变
        //TODO 这里detail接口要改
//        tvDetailRealName.setText(ContactsUtils.realNameNremarkName(realName,entity.nickName()));
//        tvDetailRealName2.setText(realName);
        if (relationState == 3) {
            //信任的好友
            //add by linzhangbin 2017/6/29 信任好友显示实名信息 start
            identityInfo.setVisibility(View.VISIBLE);
            identityName.setVisibility(View.VISIBLE);
            identityPhone.setVisibility(View.VISIBLE);
            ivPencilOtherPhone.setVisibility(View.GONE);
            ivPencilWorkCard.setVisibility(View.GONE);
            ivPencilAccount.setVisibility(View.GONE);
            ivPencilDelivery.setVisibility(View.GONE);
            //add by linzhangbin 2017/6/29 信任好友显示实名信息 end
            tvDetailRealName.setText(ContactsUtils.realNameNremarkName(realName,nickName));
            tvDetailRealName2.setText(realName);
            ivContactState.setImageResource(R.mipmap.contact_detail_trust);
            if (StringUtils.isNullString(entity.imgUrl())) {
                sdvDetailAvatar.setImageResource(R.mipmap.default_avatar);
            } else {
                sdvDetailAvatar.setImageURI(Uri.parse(entity.imgUrl()));
            }

        } else if (relationState == 2) {
            //add by linzhangbin 2017/6/29 非信任好友不显示实名信息 start
            identityInfo.setVisibility(View.GONE);
            identityName.setVisibility(View.GONE);
            identityPhone.setVisibility(View.GONE);
            ivPencilOtherPhone.setVisibility(View.VISIBLE);
            ivPencilWorkCard.setVisibility(View.VISIBLE);
            ivPencilAccount.setVisibility(View.VISIBLE);
            ivPencilDelivery.setVisibility(View.VISIBLE);
            //add by linzhangbin 2017/6/29 非信任好友不显示实名信息 end
            tvDetailRealName.setText(ContactsUtils.realNameNremarkName(remark,nickName));
            tvDetailRealName2.setText(remark);
            ivContactState.setImageResource(R.mipmap.contact_detail_nottrust);
            sdvDetailAvatar.setImageResource(R.mipmap.default_avatar);

        } else {
            //非信任好友
            //add by linzhangbin 2017/6/29 非信任好友不显示实名信息 start
            identityInfo.setVisibility(View.GONE);
            identityName.setVisibility(View.GONE);
            identityPhone.setVisibility(View.GONE);
            ivPencilOtherPhone.setVisibility(View.VISIBLE);
            ivPencilWorkCard.setVisibility(View.VISIBLE);
            ivPencilAccount.setVisibility(View.VISIBLE);
            ivPencilDelivery.setVisibility(View.VISIBLE);
            //add by linzhangbin 2017/6/29 非信任好友不显示实名信息 end
            tvDetailRealName.setText(ContactsUtils.realNameNremarkName(remark,nickName));
            tvDetailRealName2.setText(remark);
            ivContactState.setVisibility(View.INVISIBLE);
            sdvDetailAvatar.setImageResource(R.mipmap.default_avatar);
        }
        //modify by linzhangbin 2017/6/9 根据产品UI调整图标全为绿色逻辑不变 end

        //modify by linzhangbin 2017/6/12 之前显示的是昵称 现在显示电话号码
        if (StringUtils.isNullString(phone)) {
            tvDetailRemark.setText("");
        } else {
            tvDetailRemark.setText(phone);
        }
        //modify by linzhangbin 2017/6/12 之前显示的是昵称 现在显示电话号码 end


        if (StringUtils.isNullString(phone)) {
            tvDetailPhoneNum.setText("");
        } else {
            tvDetailPhoneNum.setText(phone);
        }
        //数据暂时造假为true
        star = entity.isNote() == 1;
        if (1 == entity.isNote()) {
            toolbar.setActionMenuIcon(R.mipmap.detail_star);
        } else {
            toolbar.setActionMenuIcon(R.mipmap.detail_star_common);
        }
        getEntity = entity;

        if (!StringUtils.isNullString(olNo)) {
            HistoryChatFragment chatFragment = new HistoryChatFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
            bundle.putString(EaseConstant.EXTRA_USER_ID, olNo);
            chatFragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .add(R.id.fl_chat_content, chatFragment).commit();
        }
    }

    @Override
    public void showVisitingCard(User user) {
        Timber.tag(TAG).i(user.toString());
        if (null != user.getSecondPhoneArray()){
            phoneList.addAll(user.getSecondPhoneArray());
        }
        visitingCard = user.getVisitingCard();
        bankInfo = user.getBankInfo();
        friendBankList = user.getFriendBankList();
        express = user.getExpress();
        if (null != bankInfo){
            list.setObj(bankInfo);
        }
        showPhoneList(phoneList);
        showCardDetail(visitingCard);
        showBnankInfo(bankInfo,friendBankList);
        showDeliveryAddress(express);

    }

    @Override
    public void setStar() {
        if (star) {
            toolbar.setActionMenuIcon(R.mipmap.detail_star_common);
        } else {
            toolbar.setActionMenuIcon(R.mipmap.detail_star);
        }
        star = !star;

        isRefresh = true;
        toolbar.setActionMenuClickable(true);
    }


    /**
     * 展现快递地址
     * @param express
     */
    private void showDeliveryAddress(List<String> express) {
        if (null != express && !express.isEmpty()){
            for (String expres : express) {
                Timber.tag(TAG).i("快递地址:"+expres);
            }
            lvExpressInfo.setVisibility(View.GONE);
            String location = express.get(0);
            String[] split = location.split(",");
            area = split[0];
            detailLocation = split[1];
            deliveryInfo.setText(area +""+ detailLocation);
            deliveryInfo.setVisibility(View.VISIBLE);

        }else{
            lvExpressInfo.setVisibility(View.GONE);
        }
    }

    /**
     * 展现银行列表
     * @param bankInfo
     */
    private void showBnankInfo(List<User.BankAccount> bankInfo,List<User.BankAccount> friendBankList) {
        //modify by linzhangbin 2017/7/7 如果是可信好友展示其绑定银行卡和自己编辑的银行卡 start
        if (null != friendBankList){
            for (User.BankAccount bankAccount : friendBankList) {
                AccountItem item = new AccountItem();
                item.setBank(bankAccount.getBankName());
                item.setAccount(bankAccount.getCardNo());
                accountList.add(item);
            }
        }
        if (null != bankInfo){
            Timber.tag(TAG).i("added account size : "+ bankInfo.size());
            for (User.BankAccount bankAccount : bankInfo) {
                AccountItem item = new AccountItem();
                item.setBank(bankAccount.getBankName());
                item.setAccount(bankAccount.getCardNo());
                accountList.add(item);
            }
            AccountAdapter accountAdapter = new AccountAdapter(activity,accountList,0);
            lvCountInformation.setAdapter(accountAdapter);
            if (accountList.isEmpty()){
                lvCountInformation.setVisibility(View.GONE);
            }else{
                lvCountInformation.setVisibility(View.VISIBLE);
            }
        }
        //modify by linzhangbin 2017/7/7 如果是可信好友展示其绑定银行卡和自己编辑的银行卡 end
    }

    /**
     *
     * @param visitingCard
     */
    private void showCardDetail(User.VisitingCard visitingCard) {
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
//                if (3 == relationState){
//                    tvPosition.setTextColor(getResources().getColor(R.color.button_green));
//                }
                tvPosition.setVisibility(View.VISIBLE);
                tvPosition.setText(position);
            }
            if (TextUtils.isEmpty(address)) {
                tvAddress.setVisibility(View.GONE);
            } else {
                tvAddress.setVisibility(View.VISIBLE);
                tvAddress.setText(area + "" + address);
            }
        } else {
            tvCompany.setVisibility(View.GONE);
            tvPosition.setVisibility(View.GONE);
            tvAddress.setVisibility(View.GONE);
        }
    }

    /**
     * 其他电话展示
     * @param phoneList
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
    public boolean getUserVisibleHint() {
        Timber.tag(TAG).i("getUserVisibleHint");
        return super.getUserVisibleHint();
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.tag(TAG).i("onresume");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        //取消订阅
        presenter.unSubscribe();
    }


    @OnClick({R.id.iv_pencil, R.id.iv_detail_phone, R.id.iv_detail_chat, R.id.iv_detail_email,
            R.id.iv_detail_delivery, R.id.iv_detail_transfer, R.id.ll_card_detail,
            R.id.iv_pencil_otherPhone, R.id.iv_pencil_workCard, R.id.iv_pencil_account,
            R.id.iv_pencil_delivery})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_pencil:
                isRefresh = false;
                //add by linzhangbin 2017/6/29 可信好友只能编辑备注
                activity.addSecondFragment(EditRemarkFragment.newInstance(getEntity));

                //add by linzhangbin 2017/6/29 可信好友只能编辑备注

                break;
            case R.id.iv_detail_phone:  //打电话
                if (relationState == 3 || relationState == 2) {
                    Intent intent = new Intent(activity, CallingActivity.class);
                    intent.putExtra("phoneNum", phone);
                    startActivity(intent);
                } else {
                    CommonDialog commonDialog = new CommonDialog(activity);
                    commonDialog.setCanceledOnTouchOutside(true);
                    commonDialog.setTitleVisible(View.GONE);
                    commonDialog.setContent(getString(R.string.notVip_call_chat));
                    commonDialog.setNegativeButton(getString(R.string.notVip_negative_text));
                    commonDialog.setPositiveButton(getString(R.string.notVip_positive_call_text));
                    commonDialog.setOnDialogClickListener(new CommonDialog.OnDialogInterface() {
                        @Override
                        public void cancel(View view, CommonDialog dialog) {
                            dialog.dismiss();
                            ContactsUtils.normalChat(phone, activity, user.getRealName(), user.getPhone());
                        }

                        @Override
                        public void ensure(View view, CommonDialog dialog) {
                            //重试
                            ContactsUtils.requestCallPhonePermission(activity);
                            AppUtil.call(activity, phone);
                            dialog.dismiss();
                        }
                    });
                    commonDialog.show();

                }

                break;
            case R.id.iv_detail_chat:
                //聊天
                if (relationState == 3 || relationState == 2) {
                    ContactsUtils.personalChat(activity, realName, remark, olNo, phone, avatar);
                } else {
                    CommonDialog commonDialog = new CommonDialog(activity);
                    commonDialog.setTitleVisible(View.GONE);
                    commonDialog.setCanceledOnTouchOutside(true);
                    commonDialog.setContent(getString(R.string.notVip_call_chat));
                    commonDialog.setNegativeButton(getString(R.string.notVip_negative_text));
                    commonDialog.setPositiveButton(getString(R.string.notVip_positive_chat_text));
                    commonDialog.setOnDialogClickListener(new CommonDialog.OnDialogInterface() {
                        @Override
                        public void cancel(View view, CommonDialog dialog) {
                            ContactsUtils.normalChat(phone, activity, user.getRealName(), user.getPhone());
                            dialog.dismiss();
                        }

                        @Override
                        public void ensure(View view, CommonDialog dialog) {
                            //重试
                            ContactsUtils.normalChat(phone, activity);
                            dialog.dismiss();
                        }
                    });
                    commonDialog.show();

                }
                break;
            case R.id.iv_detail_email:


                if (relationState == 2) {
                    notTrustedEmailDeliverAndTransferDialog();
                } else if (relationState == 3) {
                    //发邮件
                    Intent intent = new Intent(activity, SendEmailActivity.class);
                    startActivity(intent);
                } else {
                    notVipEmailTransferAndDeliveryDialog();
                }

                break;
            case R.id.iv_detail_delivery:
                Intent chooseIntent=new Intent(getActivity(), ExpressActivity.class);
                chooseIntent.putExtra(ExpressActivity.KEY_OF_REAL_NAME, realName);
                chooseIntent.putExtra(ExpressActivity.KEY_OF_PHONE, phone);
                chooseIntent.putExtra(ExpressActivity.KEY_OF_AVATAR, avatar);
                chooseIntent.putExtra(ExpressActivity.KEY_OF_AREA, area);
                chooseIntent.putExtra(ExpressActivity.KEY_OF_DETAIL_LOCATION, detailLocation);
                startActivity(chooseIntent);
            /*    //查看物流
                if (relationState == 2) {
                    notTrustedEmailDeliverAndTransferDialog();
                } else if (relationState == 3) {
                    //发送好友请求
                  *//* wangzhongming 2017/7/4 进入物流界面*//*
                  Intent intent=new Intent(getActivity(), ExpressActivity.class);
                  startActivity(intent);

                } else {
                    notVipEmailTransferAndDeliveryDialog();
                }*/
                break;
            case R.id.iv_detail_transfer:
                //转账
                if (relationState == 2) {
                    notTrustedEmailDeliverAndTransferDialog();
                } else if (relationState == 3) { //信任用户转账
                    //转账  add by yuanshaoyu 2017-5-4
//                    String name = tvDetailRealName.getText().toString();
//                    String remark = tvDetailRemark.getText().toString();
//                    activity.addSecondFragment(TransferManagerFragment.newInstance(phone, olNo, name, remark, avatar));
                } else {
                    notVipEmailTransferAndDeliveryDialog();
                }
                break;
            case R.id.ll_card_detail:

                if (upwards) {//展开联系人详情,箭头向上
                    ivUpwards.setImageResource(R.mipmap.upwards);
                    llDetailShow.setVisibility(View.VISIBLE);
                } else {
                    ivUpwards.setImageResource(R.mipmap.downwards);
                    llDetailShow.setVisibility(View.GONE);
                }
                upwards = !upwards;
                break;

            case R.id.iv_pencil_otherPhone: //编辑其他电话
                activity.addSecondFragment(EditAddPhoneFragment.newInstance(TAG,phoneList,phone));
                break;
            case R.id.iv_pencil_workCard: //编辑工作名片
                activity.addSecondFragment(EditMyWorkCardFragment.newInstance(TAG,phone,visitingCard));
                break;
            case R.id.iv_pencil_account: //编辑帐号信息
                activity.addSecondFragment(SelectAccountFragment.newInstance(TAG,phone,list));
                break;
            case R.id.iv_pencil_delivery: //编辑快递地址
                activity.addSecondFragment(EditDeliveryInfoFragment.newInstance(TAG,area,detailLocation,phone));
                break;
        }
    }


    /**
     * 非欧乐会员用户点击发邮件转账和物流弹窗
     */
    private void notVipEmailTransferAndDeliveryDialog() {
        new CommonDialog(activity)
                .setTitleVisible(View.GONE)
                .setContent(getString(R.string.notVIP_email_delivery))
                .setNegativeButton(getString(R.string.dialog_negative))
                .setPositiveButton(getString(R.string.dialog_positive))
                .setOnDialogClickListener(new CommonDialog.OnDialogInterface() {
                    @Override
                    public void cancel(View view, CommonDialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void ensure(View view, CommonDialog dialog) {
                        //TODO 发短信
                        ContactsUtils.normalChat(phone, activity, user.getRealName(), user.getPhone());
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 非可信任欧乐用户点击邮箱转账和物流弹窗
     */
    private void notTrustedEmailDeliverAndTransferDialog() {
        new CommonDialog(activity)
                .setTitleVisible(View.GONE)
                .setContent(getString(R.string.notTrusted_email_deliver))
                .setNegativeButton(getString(R.string.dialog_negative))
                .setPositiveButton(getString(R.string.dialog_positive))
                .setOnDialogClickListener(new CommonDialog.OnDialogInterface() {
                    @Override
                    public void cancel(View view, CommonDialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void ensure(View view, CommonDialog dialog) {
//                        //add by luoxx 2017/6/22 发送好友请求(环信) start
//                        if(user!=null)
//                            EaseAddFriend.getInstance(getContext()).onFriendAdd(user.getOlNo());
//                        //add by luoxx 2017/6/29 发送好友请求(环信) end

                        //modify by linzhangbin 2017/7/4 发送好友请求接口更改 start
                        ContactRepository.getInstance(OKLineApp.context)
                                .requestFriend(phone,olNo)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new DefaultSubscribe<Boolean>(TAG){
                                    @Override
                                    public void onError(Throwable throwable) {
                                        super.onError(throwable);
                                        String message = throwable.getMessage();
                                        Timber.tag(TAG).i("onError : " + message);
                                        ToastUtil.show(activity, "发送好友失败,请稍候再试");

                                    }

                                    @Override
                                    public void onNext(Boolean aBoolean) {
                                        super.onNext(aBoolean);
                                        ToastUtil.show(activity, "已发送好友请求");
                                    }
                                });
                        //modify by linzhangbin 2017/7/4 发送好友请求接口更改 end

                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        try {
            switch (requestCode) {
                case PERMISSION_CALLPHONE:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                        ContactsUtils.safeCall(phone, activity);
                        AppUtil.call(activity, phone);
                    } else {
                        ToastUtil.show(activity, "没有拨打电话权限,将无法进行通话");
                    }
                    break;

                default:
                    break;
            }
        } catch (Exception e) {

        }
    }

    /**
     * 接收备注名更改信息 并更改UI
     *
     * @param remark 修改过后的备注名
     */
    @Subscribe(tags = {@Tag(EventToken.REMARK_CHANGED)})
    public void onRemarkChanged(String remark) {
        //modify by linzhangbin 2017/6/12 事件逻辑更改
        Timber.tag(TAG).d("onRemarkChanged " + remark);

        tvDetailRealName.setText(ContactsUtils.realNameNremarkName(realName,remark));
//        if (relationState == 3) {
//            tvRemarkName.setText("(" + remark + ")");
//        } else {
//            tvDetailRealName.setText(remark);
//            tvDetailRealName2.setText(remark);
//        }
        //modify by linzhangbin 2017/6/12 事件逻辑更改 end
    }

    /**
     * 保存工作名片成功
     * @param visitingCard
     */
    @Subscribe(tags = {
            @Tag(EventToken.SAVE_WORK_CARD_DETAIL)
    })
    public void onWorkCardSaved(User.VisitingCard visitingCard) {
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
        if (relationState == 3){
            //modify by linzhangbin change color 2017/6/22
            tvPosition.setTextColor(getResources().getColor(R.color.button_green));
            //modify by linzhangbin change color 2017/6/22
        }
        tvPosition.setText(visitingCard.getPosition());
        tvAddress.setText(visitingCard.getArea() + "" + visitingCard.getAddress());

    }

    /**
     * 保存帐号信息成功
     * @param bankList
     */
    @Subscribe(tags = {
            @Tag(EventToken.SAVE_DETAIL_ACCOUNT),
            @Tag(EventToken.DELETE_DETAIL_ACCOUNT)
    })
    public void onSaveDetailAccount(ArrayList<User.BankAccount> bankList) {
        accountList.clear();
        for (User.BankAccount bankAccount : bankList) {
            AccountItem item = new AccountItem();
            item.setBank(bankAccount.getBankName());
            item.setAccount(bankAccount.getCardNo());
            item.setIcon(bankAccount.getBankIcon());
            accountList.add(item);
        }
        list.setObj(bankList);
        Timber.tag(TAG).i("list size :~~~~" + list.getObj().size());
        AccountAdapter adapter = new AccountAdapter(activity, accountList,0);
        lvCountInformation.setAdapter(adapter);
        if (accountList.size()>0){
            lvCountInformation.setVisibility(View.VISIBLE);
        }else{
            lvCountInformation.setVisibility(View.GONE);
        }

    }

    /**
     * 保存快递地址成功
     */
    @Subscribe(tags = {
            @Tag(EventToken.SAVE_DETAIL_EXPRESS)
    })
    public void onSaveDetailExpress(String location) {
        Timber.tag(TAG).i("onSaveAddExpress :" + location);
        String[] split = location.split(",");
        String area = split[0];
        String detailLocation = split[1];
        this.area = area;
        this.detailLocation = detailLocation;
        deliveryInfo.setText(area+""+detailLocation);
        deliveryInfo.setVisibility(View.VISIBLE);
    }

    /**
     * 保存其他联系方式成功
     * @param list
     */
    @Subscribe(tags = {@Tag(EventToken.SAVE_DETAIL_PHONE)})
    public void saveDetailPhone(ArrayList<String> list) {
        phoneList = list;
        if (list.isEmpty()) {
            lvOtherPhone.setVisibility(View.GONE);
        } else {
            ContactCardListAdapter adapter = new ContactCardListAdapter(activity, list);
            lvOtherPhone.setAdapter(adapter);
            lvOtherPhone.setVisibility(View.VISIBLE);
        }
    }


}
