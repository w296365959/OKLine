package com.vboss.okline.ui.contact;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.vboss.okline.R;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.base.EventToken;
import com.vboss.okline.base.OKLineApp;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.data.local.UserLocalDataSource;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.ui.contact.ContactDetail.ContactDetailFragment;
import com.vboss.okline.ui.contact.adapter.ContactMainAdapter;
import com.vboss.okline.ui.contact.addContact.AddCardFragment;
import com.vboss.okline.ui.contact.bean.Contact;
import com.vboss.okline.ui.contact.bean.ContactItem;
import com.vboss.okline.ui.contact.dialog.ContactAddDialog;
import com.vboss.okline.ui.contact.group.CreateGroupActivity;
import com.vboss.okline.ui.contact.myCard.MyCardFragmentNew;
import com.vboss.okline.ui.contact.search.ContactsSearchFragment;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.scanner.QRCodeActivity;
import com.vboss.okline.ui.user.UserFragment;
import com.vboss.okline.utils.StringUtils;
import com.vboss.okline.utils.TextUtils;
import com.vboss.okline.utils.ToastUtil;
import com.vboss.okline.view.widget.CommonDialog;
import com.vboss.okline.view.widget.OKCardView;
import com.vboss.okline.view.widget.SideBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;
import static com.vboss.okline.ui.home.MainActivity.REQUEST_CODE_ADD_NEWFRIEND;

//import static com.baidu.location.d.j.R;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/3/28 15:00
 * Desc :
 */

public class ContactsFragment extends ContactsBaseFragment implements ContentContract.ContentView, View.OnClickListener {
    private static final String TAG = "ContactsFragment";
    private static final int PERMISSION_CALLPHONE_CODE = 410;
    private static final int REQUEST_READCONTACT_PEMISSION = 2055;
    /**
     * 控制是否刷新页面
     */
    public static boolean isRefresh = false;
    public static boolean isImportCompleted = true;
    Unbinder unbinder;
    @BindView(R.id.lv_contacts)
    ListView lvContacts;
    @BindView(R.id.dialog)
    TextView dialog;
    @BindView(R.id.sidebar)
    SideBar sidebar;
    @BindView(R.id.pb_add_contacts)
    ProgressBar pbAddContacts;
    @BindView(R.id.iv_contact_avatar)
    SimpleDraweeView ivContactAvatar;
    @BindView(R.id.tv_me)
    TextView tvMe;
    @BindView(R.id.tv_my)
    TextView tvMy;
    @BindView(R.id.rl_headview)
    RelativeLayout rlHeadview;
    /**
     * ActionBar bind View
     */
//    @BindView(R.id.toolbar_contact)
//    FragmentToolbar toolbar;
    @BindView(R.id.trans_ptrFrameLayout)
    PtrClassicFrameLayout transPtrFrameLayout;
    /*@BindView(R.id.tv_call_phone)
    TextView tvCallPhone;*/
    @BindView(R.id.non_new_friend)
    TextView nonNewFriend;
    //请完善我的名片
    @BindView(R.id.tv_complete_information)
    TextView tvCompleteInformation;
    //add by linzhangbin for toolbar change 2017-6-6
    @BindView(R.id.btn_receivables)
    ImageButton btnReceivables;
    @BindView(R.id.ib_drop_add)
    ImageButton ibDropAdd;
//    @BindView(R.id.sdv_logo)
//    SimpleDraweeView sdvLogo;
    @BindView(R.id.action_title)
    TextView actionTitle;
    //add by linzhangbin for toolbar change 2017-6-6
    @BindView(R.id.action_menu_button)
    ImageButton actionMenuButton;
    //modify by linzhangbin 2017-06-21  logoView
    @BindView(R.id.logoView)
    LogoView logoView;
    //Added by linzhangbin 2017-06-21 add ocard battery capacity view
    @BindView(R.id.ocardView)
    OKCardView okCardView;
    private ContactsPresenter contactsPresenter;
    private List<Contact> conList;
    private View contentView;
    private View selfView;
    //    private ContactListAdapter adapter;
    private ContactMainAdapter adapter;
    //    private TextView tvMy;
    private SimpleDraweeView myAvatar;
    private MainActivity activity;
    private ContactAddDialog addDialog;
    private int clickedPosition = 0;
    private View newFriends;
    //新的朋友小红点
    private TextView redPoint;
    //用来展现分页listview的集合
    private List<ContactItem> contentList = new ArrayList<>();
    private ContactsSearchFragment fragment;
    private boolean isBluetoothConnecting;
    //星标用户数量
    private int starSize = 0;
    //群组数量
    private int groupSize = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.contacts_fragment, null);
        unbinder = ButterKnife.bind(this, contentView);
        activity = (MainActivity) getActivity();
        if (activity == null) {
            throw new NullPointerException("activity is null");
        }
        contactsPresenter = new ContactsPresenter(this, activity);

        return contentView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        newFriends = View.inflate(activity, R.layout.contacts_item_newfriend, null);
        lvContacts.addHeaderView(newFriends);
        redPoint = (TextView) newFriends.findViewById(R.id.new_friend_redpoint);
        fragment = new ContactsSearchFragment();

        //Added by wangshuai logoView click
        logoView.setOnClickListener(activity);

        //每次第一次启动刷新联系人
        refreshContact();
        initPtrFrameLayout();
        //add by linzhangbin 2017/6/6 查看是否资料编写完善
        checkInfomation();
        //add by linzhangbin 2017/6/6 查看是否资料编写完善
        lvContacts.setOverScrollMode(View.OVER_SCROLL_NEVER);
        sidebar.setTextView(dialog);
        sidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                if (adapter != null) {
                    int position = adapter.getPositionForSection(s.charAt(0));
                    if (position != -1 && position >= starSize+groupSize) {
                        lvContacts.setSelection(position);
                    }
                }
            }
        });
        lvContacts.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        tvMy.setOnClickListener(this);
        rlHeadview.setOnClickListener(this);
        /**
         * listView条目点击事件
         */
        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {//新的朋友
                    activity.addSecondFragment(new NewFriendFragment());
                    clickedPosition = 0;

                } else {
                    int newPosition = position - 1;
                    ContactItem item = (ContactItem) adapter.getItem(newPosition);
                    Log.i("ContactsFragment", "onItemClick: 条目点击:" + newPosition + "position type :" + item.getItemType()
                            + item.toString());
                    switch (item.getItemType()) {
                        case 1:
                        case 3:
                            //进名片详情页面
                            activity.addSecondFragment(ContactDetailFragment.newInstance(item.getContactID(),item));
                            clickedPosition = lvContacts.getFirstVisiblePosition();
                            break;
                        case 2:
                            //进群页面
                            String easeID = item.getEaseID();
                            if (!StringUtils.isNullString(easeID)) {
                                ContactsUtils.groupChat(activity, item);
                            } else {
                                ToastUtil.show(activity, "非IM群聊");
                            }

                            break;

                        default:
                            break;
//                    }
                    }
                }
            }
        });
    }

    //add by linzhangbin 2017/7/6 查看是否资料编写完善接口更改
    /**
     * 查看资料是否完善
     */
    private void checkInfomation() {
        //modify by linzhangbin 2017/6/21 获取用户个人信息
        UserRepository.getInstance(OKLineApp.context)
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

                            if (!TextUtils.isEmpty(company) && !TextUtils.isEmpty(address)
                                    && !TextUtils.isEmpty(position)) {
                                tvCompleteInformation.setVisibility(View.GONE);

                            } else {
                                tvCompleteInformation.setVisibility(View.VISIBLE);
                            }

                        } else {
                            tvCompleteInformation.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Timber.tag(TAG).i("get user card info failed");
                        tvCompleteInformation.setVisibility(View.VISIBLE);
                    }
                });

    }
    //add by linzhangbin 2017/6/6 查看是否资料编写完善

    /**
     * 初始化下拉刷新
     */
    private void initPtrFrameLayout() {
        transPtrFrameLayout.setLastUpdateTimeRelateObject(this);
        transPtrFrameLayout.setPtrHandler(new PtrDefaultHandler2() {

            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //lzb edit 2016/5/24 把头布局中"更新中..."换成"同步通讯录"
                TextView textView = (TextView) frame.getHeaderView().findViewById(in.srain.cube.views.
                        ptr.R.id.ptr_classic_header_rotate_view_header_title);
                textView.setText(R.string.text_sync_contact);
                //lzb edit 2016/5/24 把头布局中"更新中..."换成"同步通讯录"
                clickedPosition = 0;
                //lzb edit 2017/5/30 如果没同步完成则不给刷新
                Timber.tag(TAG).i("onRefreshBegin : " + isImportCompleted);
                refreshContact();

            }

            @Override
            public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
                return super.checkCanDoLoadMore(frame, lvContacts, footer);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, lvContacts, header);
            }

        });
        transPtrFrameLayout.setLoadingMinTime(300);
        transPtrFrameLayout.setResistanceFooter(1.0f);
        transPtrFrameLayout.setDurationToCloseFooter(0); // footer will hide immediately when completed
        transPtrFrameLayout.setForceBackWhenComplete(true);
        transPtrFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
//        transPtrFrameLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                transPtrFrameLayout.autoRefresh();
//            }
//        }, 100);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Timber.tag(TAG).i("onHiddenChanged: hidden");
            //取消订阅
            contactsPresenter.unSubscribe();

        } else {
            Timber.tag(TAG).i("onHiddenChanged: %s");
            refresh();
        }
    }

    /**
     * 刷新页面(订阅)
     */
    private void refresh() {
        Timber.tag(TAG).i("isRefresh:" + isRefresh);
        if (isRefresh) {
//            contactsPresenter.showContent();
            refreshContact();
            isRefresh = false;
        }
        if (clickedPosition != 0) {
            lvContacts.setSelection(clickedPosition);
//            clickedPosition = 0;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Timber.tag(TAG).i("requestCode:" + requestCode + ",resultCode:" + resultCode);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_ADD_NEWFRIEND) {
            //modify by linzhangbin 2017/6/1 方法变动 刷新通讯录
            contactsPresenter.showContactOnly();
            //modify by linzhangbin 方法变动 刷新通讯录
        }
    }

    /**
     * 进入app刷新通讯录
     */
    private void refreshContact() {

        //永远展现本地的联系人,如果有新增联系人再对比本地数据库筛选出来传给服务器
        contactsPresenter.showContactOnly();
        Timber.tag(TAG).i("refreshContact :" + isImportCompleted);
        if (isImportCompleted) {
            //这里如果没有权限要动态申请权限
            requestPermission();
            if (!contactsPresenter.hasReadContactPermission()){
                ToastUtil.show(activity, R.string.no_readcontact_permission);
            }
        }

    }

    //add by linzhangbin 2017/6/16 每次刷新通讯录先检测权限,动态获取
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            Timber.tag(TAG).i("requestPermission:>=23");
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                Timber.tag(TAG).i("requestPermission:没有系统权限");
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READCONTACT_PEMISSION);
            } else {
                toRefreshContact();
            }
        } else {
            toRefreshContact();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_READCONTACT_PEMISSION:
                Timber.tag(TAG).i("收到获取权限的通知");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Timber.tag(TAG).i("点击确定");
                    toRefreshContact();
                } else {
                    Timber.tag(TAG).i("点击取消");
                    CommonDialog commonDialog = new CommonDialog(activity);
                    commonDialog.setTilte(getString(R.string.cannot_import_contacts));
                    commonDialog.setNegativeButton(getString(R.string.cancel));
                    commonDialog.setPositiveButton(getString(R.string.confirm));
                    commonDialog.setOnDialogClickListener(new CommonDialog.OnDialogInterface() {
                        @Override
                        public void cancel(View view, CommonDialog dialog) {
                            dialog.dismiss();
//                            ToastUtil.show(activity, R.string.no_readcontact_permission);
                            closeRefresh();
                        }

                        @Override
                        public void ensure(View view, CommonDialog dialog) {
                            //重试
                            requestPermission();
                            dialog.dismiss();
                        }
                    });
                    commonDialog.show();
                }

                break;
            default:
                break;

        }
    }

    private void toRefreshContact(){
        LocalContact localContact = new LocalContact(activity);
        final List<ContactItem> localContactList = localContact.getContact();
        Timber.tag(TAG).i("localContactList size ; "+localContactList.size());
        //modify by linzhangbin 修复新添加的本机联系人刷不出来
        contactsPresenter.refreshContact(localContactList);
        //解决刷新效果不会被关闭的问题
        LocalContact.getInstance(activity).setOnErrorListener(new LocalContact.ErrorListener() {
            @Override
            public void error() {
                closeRefresh();
            }
        });

    }

    // add by linzhangbin 2017/6/16 end


    private void initToolbar() {
        //modify by linzhangbin 2017/6/6 for toolbar change
        ibDropAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                popupWindow.showAsDropDown(v, DensityUtil.dip2px(getActivity(), 5), 0);
                dialogShow();
            }
        });

        actionMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                activity.addSecondFragment(new ContactSearchFragment());
                //modify by linzhangbin 2017/6/6 更换搜索页面

                if (!fragment.isResumed()){
                    fragment.show(getChildFragmentManager(),ContactsSearchFragment.class.getSimpleName());
                }
                //modify by linzhangbin 2017/6/6 更换搜索页面

            }
        });
        //modify by linzhangbin 2017/6/6 for toolbar change
    }


    @Override
    public void dialogShow() {
        addDialog = new ContactAddDialog(getActivity(), R.style.dialog);
        //modify by linzhangbin 如果dialog没show则显示 BUG1576 start
        if (!addDialog.isShowing()){
            addDialog.show();
        }
        //modify by linzhangbin 如果dialog没show则显示 BUG1576 end
        addDialog.setClicklistener(new ContactAddDialog.ClickListenerInterface() {
            @Override
            public void addContact() {
                //modify by linzhangbin 2017/6/9 更换添加名片页面
//                Intent intent = new Intent(activity, AddContactActivity.class);
//                Intent intent = new Intent(activity, AddCardFragment.class);
//                startActivityForResult(intent, REQUEST_CODE_ADD_NEWFRIEND);


                activity.addSecondFragment(new AddCardFragment());
                addDialog.dismiss();
                //modify by linzhangbin 2017/6/9 更换添加名片页面 end
            }

            @Override
            public void createGroup() {
                Intent intent = new Intent(getActivity(), CreateGroupActivity.class);

                startActivity(intent);
                addDialog.dismiss();
            }

            @Override
            public void scaning() {
                startActivity(new Intent(activity, QRCodeActivity.class));
                addDialog.dismiss();
            }

//            @Override
//            public void gathering() {
//                Intent intent = new Intent(getActivity(), C2CmainActivity.class);
//                startActivity(intent);
//                addDialog.dismiss();
//            }
        });
    }

    @Override
    public void showConList(List<ContactItem> list) {
        /*if (list.isEmpty()) {
            nonNewFriend.setVisibility(View.VISIBLE);
        } else {
            nonNewFriend.setVisibility(View.GONE);
        }
        closeRefresh();
        contactsPresenter.refreshRedPoint();
//        adapter = new ContactListAdapter(list, activity, 2);
        adapter = new ContactMainAdapter(list, activity);
//        adapter = new ContactMainAdapter(testList, activity);
        lvContacts.setAdapter(adapter);
        lvContacts.setSelection(clickedPosition);
        adapter.notifyDataSetChanged();


        Timber.tag(TAG).i("showConList:" + list.size());*/
    }


    @Override
    public void showUser() {
        //lzb test
//        UserRepository repository = UserRepository.getInstance(activity);
        User user = UserLocalDataSource.getInstance().getUser();
        //lzb edit 2017/5/7 暂时用默认头像
        if (user != null) {
            if (StringUtils.isNullString(user.getAvatar())) {
                ivContactAvatar.setImageResource(R.mipmap.default_avatar);
            } else {
                //lzb edit 2017/5/24 用缓存user的头像
                ivContactAvatar.setImageURI(Uri.parse(user.getAvatar()));
                //lzb edit 2017/5/24 用缓存user的头像
            }
        }
    }

    /**
     * 关闭刷新
     */
    public void closeRefresh() {
        transPtrFrameLayout.refreshComplete();
        Timber.tag(TAG).i("closeRefresh");
    }

    @Override
    public void showRefreshToast(int size) {
//        ToastUtil.show(activity,"新增联系人%s个",size);
    }

    @Override
    public void requestTimeOut(Throwable throwable, int methodFlag) {
        handlerTimeOut(throwable, methodFlag);
    }

    @Override
    public void showRefreshError() {
        closeRefresh();
        ToastUtil.show(activity, R.string.request_data_failure);
    }

    @Override
    public void showRedPoint(int size) {
        Timber.tag(TAG).i("showRedPoint:" + size);
        if (size > 0) {
            redPoint.setVisibility(View.VISIBLE);
            redPoint.setText(String.valueOf(size));
        } else {
            redPoint.setVisibility(View.GONE);
        }

    }

    @Override
    public void showContactOnly(List<ContactItem> list,List<ContactItem> common,int starSize,int groupSize) {
        this.starSize = starSize;
        this.groupSize = groupSize;
        closeRefresh();
        //modify by lzb 2017/6/1 显示小红点
        contactsPresenter.refreshRedPoint();
        //modify by lzb 2017/6/1 显示小红点
        if (list.isEmpty()) {
            nonNewFriend.setVisibility(View.VISIBLE);
        } else {
            nonNewFriend.setVisibility(View.GONE);
        }
        //先展现所有数据库里的联系人,然后拿本地手机联系人同步给服务器,同时请求小红点和群组信息,得到群组信息后再刷新adapter
//        adapter = new ContactMainAdapter(testList, activity);
        adapter = new ContactMainAdapter(list, activity, starSize, groupSize);
        lvContacts.setAdapter(adapter);
        Timber.tag(TAG).i("showContactOnly:" + list.size());
        //lzb edit 2017/5/12 如果没好友显示"无联系人"

        //add by linzhangbin 根据数据源重绘右侧sidebar
        if (common.size()>0){
            ArrayList<String> firstLetterList = new ArrayList<>();
            for (ContactItem item : common) {
                String firstLetter = item.getSortLetters().substring(0, 1);
                //modify by linzhangbin 2017/6/23 筛选掉
                if (!firstLetterList.contains(firstLetter)){
                    firstLetterList.add(firstLetter);
                }
            }
            Timber.tag(TAG).i("firstLetterList size : " + firstLetterList.size());
            sidebar.setmSourceDatas(firstLetterList).invalidate();
            //add by linzhangbin 重绘之后重新测量
            sidebar.requestLayout();
            //add by linzhangbin 重绘之后重新测量
            sidebar.setVisibility(View.VISIBLE);
        }else{
            sidebar.setVisibility(View.GONE);
        }
        //add by linzhangbin 根据数据源重绘右侧sidebar end
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        Timber.tag(TAG).i("setUserVisibleHint : %s", isVisibleToUser);
    }


    @Override
    public void showSyncComplete() {
        closeRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPause() {
        super.onPause();
        Timber.tag(TAG).i("onPause");
    }

    /**
     * 点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_my:
                activity.addSecondFragment(new UserFragment());
                break;
            case R.id.rl_headview:
                //modify by linzhangbin 点击我跳转新的界面
                activity.addSecondFragment(new MyCardFragmentNew());
                break;
            default:
                break;

        }
    }

    @Subscribe(tags = {
            @Tag(EventToken.GROUP_CREATE),
            @Tag(EventToken.GROUP_ADD_MEMBERS),
            @Tag(EventToken.GROUP_LEAVE),
            @Tag(EventToken.CONTACT_CHANGED),
            @Tag(EventToken.REMARK_CHANGED),
            @Tag(EventToken.ADD_NEW_CONTACT)
    })
    public void onGroupChange(Boolean bool) {
        Timber.tag(TAG).d("onGroupChange " + bool);
        isRefresh = true;
        refresh();
    }
    @Subscribe(tags = {
            @Tag(EventToken.SHOW_USER_INFO)
    })
    public void showUserInfo(Boolean bool) {
        if (bool){
            //资料完善 不显示"编辑资料"提示
            tvCompleteInformation.setVisibility(View.GONE);
        }else{
            tvCompleteInformation.setVisibility(View.VISIBLE);
        }
    }
    @Subscribe(tags = {
            @Tag(EventToken.SAVE_WORK_CARD_MYCARD)
    })
    public void onWorkCardSaved(User.VisitingCard visitingCard) {
        Timber.tag(TAG).i("onWorkCardSaved :" + visitingCard.toString());
        String company = visitingCard.getCompany();
        String position = visitingCard.getPosition();
        String area = visitingCard.getArea();
        String address = visitingCard.getAddress();
        if (!TextUtils.isEmpty(company) && !TextUtils.isEmpty(position)
                && !TextUtils.isEmpty(area+address)){
            tvCompleteInformation.setVisibility(View.GONE);
        }else{
            tvCompleteInformation.setVisibility(View.VISIBLE);
        }

    }


}
