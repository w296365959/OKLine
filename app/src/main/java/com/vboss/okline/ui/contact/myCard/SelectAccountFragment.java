package com.vboss.okline.ui.contact.myCard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.vboss.okline.data.local.UserLocalDataSource;
import com.vboss.okline.ui.app.adapter.CommonAdapter;
import com.vboss.okline.ui.app.adapter.ViewHolder;
import com.vboss.okline.ui.contact.TransferAccounts.FullyLinearLayoutManager;
import com.vboss.okline.ui.contact.addContact.AddCardFragment;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/6/14 09:35
 * Desc :
 */

public class SelectAccountFragment extends BaseFragment {
    private static final String TAG = "selectAccountFragment";
    @BindView(R.id.fragment_toolbar)
    FragmentToolbar toolbar;
    @BindView(R.id.select_bind_recyclerView)
    RecyclerView selectBindRecyclerView;
    @BindView(R.id.select_add_recyclerView)
    RecyclerView selectAddRecyclerView;
    @BindView(R.id.add_account_button)
    TextView addAccountButton;
    @BindView(R.id.activity_select_account)
    LinearLayout activitySelectAccount;
    @BindView(R.id.ll_bind_account)
    LinearLayout llBindAccount;
    Unbinder unbinder;
    CommonAdapter<CardEntity> bindAdapter;
    CommonAdapter<User.BankAccount> addAdapter;
    List<CardEntity> bindList;
    List<User.BankAccount> addList;
    private MainActivity activity;
    private List<Subscription> subscriptionList;
    private SerializableObject<List<User.BankAccount>> listSerializableObject
            = new SerializableObject<List<User.BankAccount>>(new ArrayList<User.BankAccount>());
    private String tag;
    //用于提交编辑好友名片的字段
    private String contactPhone;
    private SerializableObject<List<User.BankAccount>> listAddCard;


    //add by linzhangbin 2017/6/27 传tag区分不同页面 start
    public static Fragment newInstance(String tag, String contactPhone, SerializableObject<List<User.BankAccount>> list){
        SelectAccountFragment fragment = new SelectAccountFragment();
        Bundle bundle = new Bundle();
        bundle.putString("classTag",tag);
        bundle.putString("phone",contactPhone);
        bundle.putSerializable("list",list);
        fragment.setArguments(bundle);
        return fragment;
    }
    //add by linzhangbin 2017/6/27 传tag区分不同页面 end

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_select_account, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setActionTitle(getResources().getString(R.string.title_my_account_information));
        toolbar.setActionMenuVisible(View.GONE);
        toolbar.setActionMenuClickable(false);
        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
            @Override
            public void onNavigation(View v) {
                activity.removeSecondFragment();
                TextUtils.showOrHideSoftIM(toolbar, false);
            }
        });
        Bundle bundle = getArguments();
        if (null != bundle) {
            tag = bundle.getString("classTag");
            contactPhone = bundle.getString("phone");
            listAddCard = (SerializableObject<List<User.BankAccount>>) bundle.getSerializable("list");
            if (tag.equals(MyCardFragmentNew.class.getSimpleName())){
                toolbar.setActionTitle(getResources().getString(R.string.title_my_account_information));
            }else{
                toolbar.setActionTitle(getResources().getString(R.string.title_add_account_information));
            }
        }
        subscriptionList = new ArrayList<>();
        initAdapter();
        initBindData();
        initAddData();

        //新增账户
        addAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tag.equals(MyCardFragmentNew.class.getSimpleName())){
                    User.BankAccount bankAccount = new User.BankAccount("", "", "","");

                    activity.addSecondFragment(EditAccountFragment.newInstance(bankAccount,1,-1,listSerializableObject,tag,""));
//                activity.addSecondFragment(new EditAccountFragment());
                }else if(tag.equals(AddCardFragment.class.getSimpleName())){ //添加名片 编辑联系人
                    //TODO 此处从添加名片进来的显示问题 以及新增帐户或者编辑账户的显示问题
                    User.BankAccount bankAccount = new User.BankAccount("", "", "","");
                    activity.addSecondFragment(EditAccountFragment.newInstance(bankAccount,1,-1,listAddCard,tag,""));
                    Timber.tag(TAG).i("listAddCard size : %s",listAddCard.getObj().size());
                }else{
                    User.BankAccount bankAccount = new User.BankAccount("", "", "","");
                    activity.addSecondFragment(EditAccountFragment.newInstance(bankAccount,1,-1,listAddCard,tag,contactPhone));
                    Timber.tag(TAG).i("listAddCard size : %s",listAddCard.getObj().size());
                }

            }
        });
    }

    /**
     * 获取绑定账户数据
     */
    private void initBindData() {
        //modify by linzhangbin 2017/6/27 如果是编辑个人名片绑定卡片区域显示否则隐藏 start
        if (tag.equals(MyCardFragmentNew.class.getSimpleName())){
            llBindAccount.setVisibility(View.VISIBLE);
            Subscription sbpCardList = CardRepository.getInstance(activity).cardList(CardType.BANK_CARD)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new DefaultSubscribe<List<CardEntity>>(TAG) {
                        @Override
                        public void onNext(List<CardEntity> cardEntities) {
                            bindAdapter.setmDatas(cardEntities);
                            bindAdapter.notifyDataSetChanged();
                        }
                    });
            subscriptionList.add(sbpCardList);
        }else{
            llBindAccount.setVisibility(View.GONE);
        }
        //modify by linzhangbin 2017/6/27 如果是编辑个人名片绑定卡片区域显示否则隐藏 end
    }

    /**
     * 手动添加账户数据
     */
    private void initAddData() {
        if (tag.equals(MyCardFragmentNew.class.getSimpleName())){ //我的名片
            //modify by linzhangbin 2017/6/21 获取用户个人信息
            Subscription sbpSelfInfo = UserRepository.getInstance(OKLineApp.context)
                    .getContactVisitingCard(null)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new DefaultSubscribe<User>(TAG){

                        @Override
                        public void onNext(User user) {
                            List<User.BankAccount> bankInfo = user.getBankInfo();
                            if (null != bankInfo && !bankInfo.isEmpty()){
                                listSerializableObject = new SerializableObject<>(bankInfo);
                                addAdapter.setmDatas(bankInfo);
                                addAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            super.onError(throwable);
                            Timber.tag(TAG).i("获取手动添加账户失败..");
                        }

                    });
            subscriptionList.add(sbpSelfInfo);
            //modify by linzhangbin 查自己的信息不要传此参数 2017/6/21 end
        }else{ //新建联系人 联系人详情
            List<User.BankAccount> addBankList = listAddCard.getObj();
            Timber.tag(TAG).i("addBankList size : " + addBankList.size());
            if (null!= addBankList){
                addAdapter.setmDatas(addBankList);
                addAdapter.notifyDataSetChanged();
            }
        }

    }

    private void initAdapter() {
        // NestedScrollView嵌套RecyclerView时滑动不流畅问题的解决办法
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        selectBindRecyclerView.setLayoutManager(layoutManager);
        selectBindRecyclerView.setHasFixedSize(true);
        selectBindRecyclerView.setNestedScrollingEnabled(false);

//        selectAddRecyclerView.setLayoutManager(layoutManager);
//        selectAddRecyclerView.setHasFixedSize(true);
//        selectAddRecyclerView.setNestedScrollingEnabled(false);
        // NestedScrollView嵌套RecyclerView时滑动不流畅问题的解决办法

        bindList = new ArrayList<>();
        bindAdapter = new CommonAdapter<CardEntity>(activity, R.layout.bind_account_item_layout, bindList) {
            @Override
            public void convert(ViewHolder holder, final CardEntity item, int position) {
                holder.setText(R.id.bindAccount_cardName,item.cardName());
                holder.setText(R.id.bindAccount_name, UserLocalDataSource.getInstance().getUser().getRealName());
                if (!TextUtils.isEmpty(item.imgUrl())) {
                    holder.setImageByUrl(R.id.bindAccount_cardicon,item.imgUrl());
                }
                String substring = item.cardNo().substring(0,4)+"****"+item.cardNo().substring(item.cardNo().length() - 4);
                holder.setText(R.id.bindAccount_cardNo,substring);
            }
        };


        selectBindRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        selectBindRecyclerView.setAdapter(bindAdapter);

        addList = new ArrayList<>();


        addAdapter = new CommonAdapter<User.BankAccount>(activity, R.layout.add_account_item_layout, addList) {
            @Override
            public void convert(final ViewHolder holder, final User.BankAccount item, final int position) {
                Timber.tag(TAG).i("item ~~~~~~~: " + item.toString());
                //卡名称
                holder.setText(R.id.addAccount_cardName, item.getBankName());
                //TODO 这里根据不同的页面set不同的值 AddCardFragment的账号名没有获取到
                //账户名
                if (tag.equals(MyCardFragmentNew.class.getSimpleName())){
                    holder.setText(R.id.addAccount_name, UserLocalDataSource.getInstance().getUser().getRealName());
                }else{
//                    holder.setText(R.id.addAccount_name, listAddCard.getObj().get(position).getCardName());
                    holder.setText(R.id.addAccount_name, item.getCardName());
                }

                //卡号
                holder.setText(R.id.addAccount_cardNo, item.getCardNo());
                //卡图片
                if (!TextUtils.isEmpty(item.getBankIcon())) {
                    holder.setImageByUrl(R.id.addAccount_cardicon, item.getBankIcon());
                }else{
                    //add by linzhangbin 2017/6/22 如果没有银行logo则设置默认logo
                    holder.setImageResource(R.id.addAccount_cardicon,R.mipmap.bank_default_big);
                    //add by linzhangbin 2017/6/22 如果没有银行logo则设置默认logo
                }
                //手动编辑账户条目点击事件
                holder.setOnClickListener(R.id.edit_account, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(SelectAccountActivity.this,EditAccountActivity.class);
//                        intent.putExtra("activityTag","edit");
//                        intent.putExtra("accountName",holder.getText(R.id.addAccount_name));
//                        intent.putExtra("accountNo",holder.getText(R.id.addAccount_cardNo));
//                        startActivity(intent);
                        if (tag.equals(MyCardFragmentNew.class.getSimpleName())){
                            activity.addSecondFragment(EditAccountFragment.newInstance(item,0,position,listSerializableObject,tag,""));
                        }else if (tag.equals(AddCardFragment.class.getSimpleName())){
                            activity.addSecondFragment(EditAccountFragment.newInstance(item,0,position,listAddCard,tag,""));
                        }else{
                            activity.addSecondFragment(EditAccountFragment.newInstance(item,0,position,listAddCard,tag,contactPhone));
                        }

                    }
                });
            }
        };
        selectAddRecyclerView.setLayoutManager(new FullyLinearLayoutManager(activity));
        selectAddRecyclerView.setNestedScrollingEnabled(false);
        selectAddRecyclerView.setAdapter(addAdapter);
    }

    /**
     * 取消订阅
     */
    protected void unsubscribeAll() {
        if (subscriptionList != null && subscriptionList.size() != 0) {
            for (Subscription subscription : subscriptionList) {
                if (!subscription.isUnsubscribed()) {
                    subscription.unsubscribe();
                }
            }
            subscriptionList.clear();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        unsubscribeAll();
    }

    @Subscribe(tags = {
            @Tag(EventToken.SAVE_MY_ACCOUNT)
    })
    public void onSaveMyAccount(ArrayList<User.BankAccount> list) {
        addAdapter.setmDatas(list);
        addAdapter.notifyDataSetChanged();

    }

    @Subscribe(tags = {
            @Tag(EventToken.SAVE_ADD_ACCOUNT),
            @Tag(EventToken.SAVE_DETAIL_ACCOUNT)
    })
    public void onSaveAddAccount(ArrayList<User.BankAccount> list) {
        Timber.tag(TAG).i("list size : %s",list.size());
        listAddCard.setObj(list);
        Timber.tag(TAG).i("listAddCard size ~~~~~: %s",listAddCard.getObj().size());
        addAdapter.setmDatas(list);
        addAdapter.notifyDataSetChanged();
    }


}
