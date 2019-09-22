package com.vboss.okline.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ICE.VOIP.ui.PhoneListener;
import com.ICE.VOIP.ui.PhoneManager;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseHelper;
import com.hyphenate.easeui.db.InviteMessgeDao;
import com.hyphenate.easeui.model.EaseContactModel;
import com.okline.vboss.http.EncryptUtils;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.base.EventToken;
import com.vboss.okline.data.AppRepository;
import com.vboss.okline.data.CardRepository;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.Cert;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.data.local.AppConfig;
import com.vboss.okline.data.local.SPUtils;
import com.vboss.okline.jpush.JPushEntity;
import com.vboss.okline.jpush.JPushHelper;
import com.vboss.okline.nfc.ui.NfcPage;
import com.vboss.okline.ui.app.AppFragment;
import com.vboss.okline.ui.app.MyInstalledReceiver;
import com.vboss.okline.ui.app.apppool.apppooltype.AppPoolTypeListFragment;
import com.vboss.okline.ui.card.CardConstant;
import com.vboss.okline.ui.card.RechargeSynchronousHelper;
import com.vboss.okline.ui.card.log.CardLogFragment;
import com.vboss.okline.ui.card.main.CardFragment;
import com.vboss.okline.ui.card.recharge.CardRechargeMainFragment;
import com.vboss.okline.ui.contact.ChatSearchActivity;
import com.vboss.okline.ui.contact.ContactDetail.SendEmailActivity;
import com.vboss.okline.ui.contact.ContactsFragment;
import com.vboss.okline.ui.contact.callPhone.CallingActivity;
import com.vboss.okline.ui.contact.callPhone.DialActivity;
import com.vboss.okline.ui.im.EaseRegisterCache;
import com.vboss.okline.ui.record.RecordFragment;
import com.vboss.okline.ui.user.OCardAttachFragment;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.StringUtils;
import com.vboss.okline.utils.ToastUtil;
import com.vboss.okline.view.widget.ArcView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.okline.icm.libary.UserTools;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static cn.okline.icm.libary.UserTools.DBFILE;
import static com.vboss.okline.base.OKLineApp.context;

public class MainActivity extends BaseActivity {
    public static final int REQUEST_CODE_OPEN_CARD = 237;
    public static final int REQUEST_CODE_ADD_NEWFRIEND = 301;
    public static final String CONFIG_XML = "config.xml";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TAG1 = "PopOff";
    private static final String ACTION_CONNECT_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    private static final int REQUEST_CODE_RECORD_AUDIO = 405;
    private static final int TAB_SIZE = 4;
    public static boolean inOCardBinding;
    private static String stringProperty;
    private static Fragment currentFragment;   //当前显示的fragment
    public SharedPreferences sharedPreferences;
    //add by linzhangbin 卫星菜单移到主界面
    @BindView(R.id.arc_view)
    //add by linzhangbin 卫星菜单移到主界面 end
            ArcView arcView;
    @BindView(R.id.imageView)
    ImageView imageView;

    //add by yuanshaoyu 2017-6-20 :app Receiver
    IntentFilter intentFilter;
    MyInstalledReceiver myInstalledReceiver;

    BroadcastReceiver noNetWorkBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_CONNECT_CHANGE.equals(intent.getAction())) {
                ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                /* **** no network toast *****/
                if (networkInfo == null) {
                    noNetWorkToast();
                } else {
                    if (!networkInfo.isAvailable()) {
                        noNetWorkToast();
                    }
                }
                if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    int netWorkState = getNetWorkState();
                    Log.d(TAG, "onReceived netWorkState = " + netWorkState);
                    //网络从新连接
                    if (lastState < 0 && netWorkState >= 0) {
                        PhoneManager.getInstance().onNetWorkReconnect(netWorkState);
                    }
                }
            }
        }
    };
    @BindView(R.id.root)
    LinearLayout root;
    /*private int[] drawableNormalIds = new int[]{
            R.mipmap.ic_nav_card_normal, R.mipmap.ic_nav_record_normal,
            R.mipmap.ic_nav_contact_normal, R.mipmap.ic_nav_app_normal};
    private int[] drawableHoverIds = new int[]{
            R.mipmap.ic_nav_card_press, R.mipmap.ic_nav_record_press,
            R.mipmap.ic_nav_contact_press, R.mipmap.ic_nav_app_press};*/
    //luoxiuxiu 记录设置全局
    private RecordFragment recordFragment = new RecordFragment();
    private ContactsFragment contactsFragment = new ContactsFragment();
    //    private List<TextView> tabTexts = new ArrayList<>();
    private List<ImageButton> tabImages;
    //modify by wangshuai 2017-05-24 update tab icon
    private int[] drawableNormalIds = new int[]{
            R.mipmap.ic_tab_card_normal, R.mipmap.ic_tab_record_normal,
            R.mipmap.ic_tab_contact_normal, R.mipmap.ic_tab_app_normal};
    private int[] drawableHoverIds = new int[]{
            R.mipmap.ic_tab_card_press, R.mipmap.ic_tab_record_press,
            R.mipmap.ic_tab_contact_press, R.mipmap.ic_tab_app_press};
    private LinearLayout layout;
    private View progressBar;
    /**
     * Fragment manager
     */
    // init FragmentManager instance
    private FragmentManager fm = getSupportFragmentManager();
    private List<Fragment> mainFragments = new ArrayList<>();     //存储首页的4个fragment
    private SparseArray<String> mainFragmentTags = new SparseArray<>();
    private SparseArray<String> secondFragmentTags = new SparseArray<>();
    private List<Fragment> secondFragments = new ArrayList<>();    //存储二级界面fragment
    private int mTabIndex = -1;       //首页选中位置, 默认指定无效位置
    private ImageView iv_msg_unread_tag;
    public ImageView iv_contact_unread_tag;//通讯录未读消息红点
//    private LocalBroadcastManager broadcastManager;
//    private BroadcastReceiver broadcastReceiver;
    private InviteMessgeDao inviteMessgeDao;

    public void setUnreadDotVisibility() {
        boolean flag1 = sharedPreferences.getBoolean(RecordFragment.UNREAD_TAG_INSTANT_MESSAGE, false);
        boolean flag2 = sharedPreferences.getBoolean(RecordFragment.UNREAD_TAG_INSTITUTION, false);
        final boolean visible = flag1 || flag2;
        Utils.showLog(TAG, "设置主界面小红点：" + (visible ? "可见" : "不可见"));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (visible) {
                    iv_msg_unread_tag.setVisibility(View.VISIBLE);
                } else {
                    iv_msg_unread_tag.setVisibility(View.INVISIBLE);
                }
            }
        });


    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        easeCache();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // add by luoxx 2017-6-22 :
        inviteMessgeDao = new InviteMessgeDao(this);

        // add by yuanshaoyu 2017-6-20 :
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addDataScheme("package");
        myInstalledReceiver = new MyInstalledReceiver();
        registerReceiver(myInstalledReceiver, intentFilter);

        initFragment();
        initTabs();
        sharedPreferences = getSharedPreferences(CONFIG_XML, Context.MODE_PRIVATE);
        registerNetWorkBroadcastReceiver();
        //Added by wangshuai 2017-04-28 recharge synchronous
        RechargeSynchronousHelper.getInstance().synchronousRecord();
        layout = (LinearLayout) findViewById(R.id.ll_tabs);
        iv_msg_unread_tag = (ImageView) findViewById(R.id.iv_msg_unread_tag);
        iv_contact_unread_tag = (ImageView) findViewById(R.id.iv_contact_unread_tag);
        setUnreadDotVisibility();

        //Added by shihaijun to prepare data
        prepareOKLineData();

        Intent intent = getIntent();
        Utils.showLog(TAG, "MainActivity onCreate-- intent" + intent.toString());
        if (intent.hasExtra(CardRechargeMainFragment.INTENT_TAG) &&
                intent.getStringExtra(CardRechargeMainFragment.INTENT_TAG).equals(CardRechargeMainFragment.INTENT_OPEN_BANK_CARD)) {
            Utils.showLog(TAG, "onCreate跳转到银行卡开卡界面");
            addSecondFragment(new AppPoolTypeListFragment());
        }
        initIPSS8();
        arcViewControl();
        //add luuxiuxiu 170628 register broadcast receiver to receive the change of group from EaseHelper
//        registerBroadcastReceiver();
    }

    /**
     * 初始化arcview add by linzhangbin 2017/6/12
     */
    private void arcViewControl() {
        //add by linzhangbin 2017/6/12 arcView点击事件
        arcView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arcView.isOpen()) {
                    arcView.subItemAnim();
                }

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setVisibility(View.GONE);
                arcView.setVisibility(View.VISIBLE);
                arcView.subItemAnim();
            }
        });
        arcView.setOnFinishAnim(new ArcView.OnFinishAnim() {

            @Override
            public void onFinish() {
                arcView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            }
        });


        //add by linzhangbin 2017/6/12 arcView点击事件 end

        //add by linzhangbin 2017/6/7 卫星菜单点击事件
        /**
         * 旋转按钮被点击
         */
        arcView.setOnSubItemClickListener(new ArcView.onSubItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                ToastUtil.show(activity,"position:"+position);
                if (position == 3) { //拨号
                    Intent intent = new Intent(MainActivity.this, DialActivity.class);
                    startActivity(intent);
                } else if (position == 2) { //聊天查询
                    Intent intent = new Intent(MainActivity.this, ChatSearchActivity.class);
                    startActivity(intent);
                } else if (position == 1) { //发送邮件
                    Intent intent = new Intent(MainActivity.this, SendEmailActivity.class);
                    startActivity(intent);
                }
            }
        });
        //add by linzhangbin 2017/6/7 卫星菜单点击事件
//        root.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
//            @Override
//            public void onGlobalFocusChanged(View view, View view1) {
//
//            }
//        });

    }

    //im登录入口
    private void easeCache() {
        String easeLoginNumber = SPUtils.getSp(this, AppConfig.SP_EASE_NUMBER);
        User user = UserRepository.getInstance(this).getUser();
        if (user == null) return;
        String olNumber = user.getOlNo();
//        String olNumber = "OLHZ310571000000000436";
//        String olNumber = "OLHZ310571000000000501";
        if (!(StringUtils.isNullString(olNumber))) {
            if (StringUtils.isNullString(easeLoginNumber)) {
                new EaseRegisterCache(this, String.valueOf(olNumber), EncryptUtils.password(String.valueOf(olNumber)));
            } else {
                String currentUser = EMClient.getInstance().getCurrentUser();
                if (!currentUser.equalsIgnoreCase(easeLoginNumber))
                    new EaseRegisterCache(this, String.valueOf(olNumber), EncryptUtils.password(String.valueOf(olNumber)));
                else {
                    if (!(EMClient.getInstance().isLoggedInBefore())) {
                        new EaseRegisterCache(this, String.valueOf(olNumber), EncryptUtils.password(String.valueOf(olNumber)));
                    }
                }
            }
        }
    }

    /**
     * 预取应用数据
     *
     * @Author shihaijun
     */
    private void prepareOKLineData() {
        CardRepository.getInstance(this).prepareCardData();
        AppRepository.getInstance(this).preparePoolApp(this);
        //add by luoxx 170614 添加联系人数据对象缓存
        EaseContactModel.getInstance().localContact(this);
    }

    /**
     * Add by 2017-04-17 wangshuai
     * <p>
     * the Method register NetWork change BroadcastReceiver
     */
    private void registerNetWorkBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter(ACTION_CONNECT_CHANGE);
        registerReceiver(noNetWorkBroadcastReceiver, intentFilter);
    }

    private void initFragment() {
            /* ** clear **/
        mainFragments.clear();
        mainFragmentTags.clear();
        secondFragments.clear();
        secondFragmentTags.clear();

            /* 顺序添加首页fragment ****/
        mainFragments.add(new CardFragment());
        mainFragments.add(recordFragment);
        mainFragments.add(contactsFragment);
        mainFragments.add(new AppFragment());
        //init fragment tag

        mainFragmentTags.put(0, mainFragments.get(0).getClass().getSimpleName());
        mainFragmentTags.put(1, mainFragments.get(1).getClass().getSimpleName());
        mainFragmentTags.put(2, mainFragments.get(2).getClass().getSimpleName());
        mainFragmentTags.put(3, mainFragments.get(3).getClass().getSimpleName());

    }

    /**
     * init tabs
     */
    private void initTabs() {
        if (tabImages == null) {
            tabImages = new ArrayList<>(TAB_SIZE);
        } else {
            tabImages.clear();
        }
        tabImages.add((ImageButton) findViewById(R.id.iv_tab_card));
        tabImages.add((ImageButton) findViewById(R.id.iv_tab_record));
        tabImages.add((ImageButton) findViewById(R.id.iv_tab_contact));
        tabImages.add((ImageButton) findViewById(R.id.iv_tab_app));
        for (int i = 0; i < TAB_SIZE; i++) {
            tabImages.get(i).setOnClickListener(new OnTabChangeListener(i));
        }
        tabImages.get(0).performClick();
    }

    /**
     * 底部导航切换
     *
     * @param position 位置
     */
    private void switchTab(int position) {
        for (int i = 0; i < TAB_SIZE; i++) {
            if (position == i) {
                tabImages.get(i).setImageResource(drawableHoverIds[i]);
            } else {
                tabImages.get(i).setImageResource(drawableNormalIds[i]);
            }
        }
        switchMainFragment(position);
    }

    /**
     * 加载二级页面的Fragment
     *
     * @param fragment 二级页面Fragment
     */
    public void addSecondFragment(Fragment fragment) {
        //add by linzhangbin 2017/6/12 隐藏arcview
        arcView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        //add by linzhangbin 2017/6/12 隐藏arcview end

        goNext(fragment);
    }

    /**
     * 进入下一级
     *
     * @param fragment 目标Fragment对象
     */
    public void goNext(Fragment fragment) {
        String tag = fragment.getClass().getSimpleName();
        Fragment target = fm.findFragmentByTag(tag);
        if (target != null) {
            fm.beginTransaction().hide(currentFragment).show(target).commitAllowingStateLoss();
            Timber.tag(TAG).w("====Just show %s", target.getClass().getSimpleName());
        } else {
            target = fragment;
            fm.beginTransaction().hide(currentFragment)
                    .add(R.id.container, target, tag)
                    .addToBackStack(tag).commitAllowingStateLoss();
            secondFragments.add(target);
            int index = secondFragmentTags.size();
            secondFragmentTags.put(index, tag);
            Timber.tag(TAG).w("Add %s to back stack， secondFragmentCount = %d ", target.getClass().getSimpleName(), secondFragments.size());
        }
        currentFragment = target;
    }

    /**
     * 返回上一级
     */
    @Override
    public void goBack() {
        int count = fm.getBackStackEntryCount();
        if (count != 0) {
            popBackStack(count - 1);
        }
    }

    /**
     * 根据TAG 移除fragment
     */
    public void removeSecondFragment() {
//        Timber.tag(TAG).i("CurrentTab = %d, when removeSecondFragment", mTabIndex);
        goBack();
        //lzb edit 2017/05/12 点击物理返回键显示底部导航栏
        hideTabs(false);
        //add by linzhangbin 2017/6/13 显示arcview
        if (secondFragments.size() < 1) {
            imageView.setVisibility(View.VISIBLE);
            arcView.setVisibility(View.GONE);
        }
    }

    /***
     * change fragment and remove second level fragment
     *
     * @param position mTabIndex tab
     */
    private void switchMainFragment(int position) {
//Added by shihaijun on 2017-06-21 : 优化Fragment切换 start
        boolean isSameTab = position == mTabIndex
                && currentFragment != null && currentFragment.getClass().getSimpleName().equals(mainFragmentTags.get(position));

        if (!isSameTab) {
            clearBackStack();
            Fragment target = fm.findFragmentByTag(mainFragmentTags.get(position));
            if (target == null) {
                target = mainFragments.get(position);
                String tag = mainFragmentTags.get(position);
                if (currentFragment == null) {
                    fm.beginTransaction().add(R.id.container, target, tag).commitAllowingStateLoss();
                } else {
                    fm.beginTransaction().hide(currentFragment).add(R.id.container, target, tag).commitAllowingStateLoss();
                }
            } else {
                if (currentFragment != null) {
                    fm.beginTransaction().hide(currentFragment).show(target).commitAllowingStateLoss();
                } else {
                    fm.beginTransaction().show(target).commitAllowingStateLoss();
                }
            }
            currentFragment = target;
            mTabIndex = position;

        }
//Added by shihaijun on 2017-06-21 : 优化Fragment切换 end

        //add by linzhangbin 2017/6/13 多级界面直接切一级界面的显示问题
        if (arcView.isOpen()) {
            arcView.subItemAnim();
        }
        arcView.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
        //add by linzhangbin 2017/6/13 多级界面直接切一级界面的显示问题 end
    }

    /**
     * 只保留Fragment回退栈的栈顶元素
     *
     * @since Shihaiun
     */
    private void topBackStack() {
        popBackStack(1);
    }

    /**
     * 清空Fragment回退栈
     *
     * @since Shihaiun
     */
    private void clearBackStack() {
        popBackStack(0);
    }

    /**
     * 将Fragment弹出回退栈
     *
     * @param retain 留存的Fragment个数
     * @since Shihaiun
     */
    private void popBackStack(int retain) {
        int count = fm.getBackStackEntryCount();
        Timber.tag(TAG).i("BackStackEntryCount = %d, Retain = %d", count, retain);
        while (count > retain) {
            fm.popBackStack();
            count--;
            secondFragmentTags.remove(count);
            Fragment fragment = secondFragments.remove(count);

            if (count != 0) {
                currentFragment = secondFragments.get(count - 1);
            } else {
                currentFragment = mainFragments.get(mTabIndex);
            }
            Timber.tag(TAG).w("Pop %s from back stack , retain = %d, CurrentFragment = %s",
                    fragment.getClass().getSimpleName(), count, currentFragment == null ? "NULL" : currentFragment.getClass().getSimpleName());
        }
    }


    @Override
    public void onBackPressed() {
        if (!inOCardBinding) {
            super.onBackPressed();
            Timber.tag(TAG).i("+++++++ onBackPressed ++++++++");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!inOCardBinding) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                onBack();
                return false;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        } else {
            return false;
        }
    }

    /**
     * back 键监听
     */
    private void onBack() {
        Timber.tag(TAG).i(" ++++++++ Do onBack() ++++++++++");
        if (secondFragments.size() > 0) {
            removeSecondFragment();
        } else {
            /*
            * Add by wangshuai 2017-04-22
            * if you press back code
            * the app will goto running background
            * **/
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
        }
    }

    private void initIPSS8() {

        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_CODE_RECORD_AUDIO);
            } else {
                initConfigFile();
            }
        } else {
            //上面已经写好的拨号方法
            initConfigFile();
        }


    }

    private void initConfigFile() {
        if (UserTools.isUserFileExist(this)) {
            initPhone();
        } else {
            getCA();
        }
    }

    // TODO for test
    private void createTestCA(byte[] data) {
        String filePath2 = "/mnt/sdcard" + File.separator + DBFILE;
        UserTools.writeUserData(context, data, filePath2);
    }

    private void getCA() {
        UserRepository userRepository = UserRepository.getInstance(this);
        userRepository.getCA()
                .map(new Func1<Cert, Boolean>() {
                    @Override
                    public Boolean call(Cert cert) {
                        createTestCA(cert.getCert());
                        return UserTools.writeUserData(getApplicationContext(), cert.getCert());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<Boolean>(TAG) {
                    @Override
                    public void onNext(Boolean result) {
                        if (result) {
                            initConfigFile();
                        } else {
                            Timber.tag(TAG).d("初始化失败，读取不到认证文件");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Timber.tag(TAG).d("初始化失败，读取不到认证文件");
                    }
                });
    }

    private void initPhone() {
        PhoneManager.getInstance().addPhoneListener(new PhoneListener() {
            @Override
            public void onLoginResult(boolean isSuccess) {
                if (isSuccess) {
                    Timber.tag(TAG).i("onLoginResult: " + isSuccess);
                }
            }

            @Override
            public void onCallOut(String phoneNumber) {
                Timber.tag(TAG).i("oncallout: " + phoneNumber);
            }

            @Override
            public void onRinging(boolean active, String phoneNumber) {
                Timber.tag(TAG).i("onRinging: " + active + "," + phoneNumber);
                if (!active) {
                    startActivity(new Intent(MainActivity.this, CallingActivity.class)
                            .putExtra("state", CallingActivity.STATE_RINGING)
                            .putExtra("phoneNum", phoneNumber));
                }
            }

            @Override
            public void onHangUp(boolean active, String phoneNumber) {
                Timber.tag(TAG).i("onHangUp: " + active + "," + phoneNumber);
            }

            @Override
            public void onCalling(boolean active, String phoneNumber) {
                Timber.tag(TAG).i("oncalling: " + active + "," + phoneNumber);
            }
        });
        PhoneManager.getInstance().initConfigFile(getApplicationContext());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_RECORD_AUDIO:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initConfigFile();
                } else {
                    ToastUtil.show(this, "安全通话功能初始化失败");
                }
                break;
            case OCardAttachFragment.REQUEST_CODE_PERMISSION:
                Utils.showLog(TAG, "当前fragment：" + currentFragment);
                if (currentFragment instanceof OCardAttachFragment) {
                    Utils.showLog(TAG, "当前fragment为OCardAttachFragment实例");
                    Utils.showLog(TAG,"权限申请：requestCode = [" + requestCode + "], permissions = [" + Arrays.asList(permissions).toString() + "], grantResults = [" + Arrays.asList(grantResults).toString() + "]");
                    currentFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }
    }

    @Override
    public void setIntent(Intent newIntent) {
        Utils.showLog(TAG, "newIntent = [" + newIntent + "]");
        if (NfcPage.isSendByMe(newIntent)) {
            super.setIntent(newIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //add by linzhangbin 2017/6/13 显示arcview
        if (secondFragments.size() < 1) {
            arcView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
        }
        //add by linzhangbin 2017/6/13 显示arcview
//        testAppUpgrade();

//        AppUpgradeHelper.checkAppVersion(this);
        EaseHelper sdkHelper = EaseHelper.getInstance();
        sdkHelper.pushActivity(this);

        EMClient.getInstance().chatManager().addMessageListener(messageListener);

    }

    @Override
    protected void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        EaseHelper sdkHelper = EaseHelper.getInstance();
        sdkHelper.popActivity(this);
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 控制底部导航栏的显示与隐藏
     *
     * @param hide show or hide
     */
    public void hideTabs(boolean hide) {
        if (hide) {
            layout.setVisibility(View.GONE);
        } else {
            layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(noNetWorkBroadcastReceiver);
//        broadcastManager.unregisterReceiver(broadcastReceiver);
        PhoneManager.getInstance().destory();
        if (myInstalledReceiver != null) {
            unregisterReceiver(myInstalledReceiver);
        }
    }

    //luoxx 监控新消息
    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {
                EaseHelper.getInstance().getNotifier().onNewMsg(message);
            }
            RxBus.get().post(EventToken.CONVERSATION_CHANGED, true);
            System.out.println("■收到信息时mTabIndex = " + mTabIndex);
            int subFragmentIndex;
            Utils.showLog(TAG, "recordFragment:" + recordFragment + " recordFragment.isAdded():" + recordFragment.isAdded());
            if (recordFragment != null && recordFragment.isAdded()) {
                subFragmentIndex = recordFragment.getSubFragmentIndex();
            } else {
                subFragmentIndex = -1;
            }
            System.out.println("■收到信息时recordFragment = " + subFragmentIndex);
            if (mTabIndex == 1 && subFragmentIndex == 1) {
                //正在IM记录界面，不处理
                Utils.showLog(TAG, "正在IM记录界面，不处理");
            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(RecordFragment.UNREAD_TAG_INSTANT_MESSAGE, true);
                editor.apply();
                setUnreadDotVisibility();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recordFragment.enlightenUnreadDot(1, true);
                    }
                });
            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            RxBus.get().post(EventToken.CONVERSATION_CHANGED, true);
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };

    /**
     * Add by 2017-04-17 wangshuai
     * Toast No NetWork
     */
    private void noNetWorkToast() {
        Timber.tag(TAG).w("unlucky! your's network may be error");
        Utils.customToast(MainActivity.this, getResources().getString(R.string.no_network),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.showLog(TAG, "MainActivity的onActivityResult：requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
        Fragment fragment = null;
        if (!secondFragments.isEmpty()) {
            fragment = secondFragments.get(secondFragments.size() - 1);
        }
        Utils.showLog(TAG, "★MainActivity当前的fragment：" + (fragment == null ? "null" : fragment));
        if (requestCode == REQUEST_CODE_OPEN_CARD) {
            AppPoolTypeListFragment.inOpenCardMode = false;
        }
        if (resultCode == RESULT_OK && data != null && data.hasExtra(CardConstant.CARD_INSTANCE)) {
            gotoCardDetail(data);
        }
        mainFragments.get(2).onActivityResult(requestCode, resultCode, data);


        if (data != null && data.hasExtra(CardRechargeMainFragment.INTENT_TAG) && data.getStringExtra(CardRechargeMainFragment.INTENT_TAG).equals(CardRechargeMainFragment.INTENT_OPEN_BANK_CARD)) {
            Utils.showLog(TAG, "onActivityResult跳转到银行卡开卡界面");
            if (fragment != null && fragment instanceof AppPoolTypeListFragment) {
                Utils.showLog(TAG, "当前页面在APP模块之中");
                removeSecondFragment();
            }
            addSecondFragment(AppPoolTypeListFragment.newInstance(getString(R.string.card_tab_bank), true));
        }
    }

    /**
     * go to card detail info
     *
     * @param data intent extras data
     */
    private void gotoCardDetail(Intent data) {
        CardEntity serializableExtra = (CardEntity) data.getSerializableExtra(CardConstant.CARD_INSTANCE);
        if (serializableExtra != null) {
            Utils.showLog(TAG, "跳转到卡片详情页面:" + serializableExtra.toString());
//Added by shihaijun on 2017-06-21 : 优化Fragment切换 start
            switchTab(0);
//Added by shihaijun on 2017-06-21 : 优化Fragment切换 end
            addSecondFragment(CardLogFragment.newInstance(serializableExtra));
        }
    }

    /**
     * receiver record unread message
     */
    @Subscribe(tags = {@Tag(JPushHelper.EVENT_JPUSH)})
    public void unreadRecord(JPushEntity entity) {
        Timber.tag(TAG).i("receiver unread message %s", entity == null ? "" : entity.toString());
        Utils.showLog(TAG, "收到信息推送：" + entity);
        Utils.showLog(TAG, "有推送：mTabIndex=" + mTabIndex + " recordFragment.getSubFragmentIndex()=" + recordFragment.getSubFragmentIndex());
        if (mTabIndex == 1 && recordFragment.getSubFragmentIndex() == 2) {
            //不处理
            Utils.showLog(TAG, "正在该界面，不处理");
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(RecordFragment.UNREAD_TAG_INSTITUTION, true).apply();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recordFragment.enlightenUnreadDot(2, true);
                }
            });
            setUnreadDotVisibility();
        }
    }

    private class OnTabChangeListener implements View.OnClickListener {
        private int position;

        OnTabChangeListener(int index) {
            this.position = index;
        }

        @Override
        public void onClick(View v) {
            if (!inOCardBinding) {
                Log.i(TAG, "Clicked position ：" + position);
                switchTab(position);
            }
        }
    }

    //add luoxx 170630 好友变化监控 start
//    private void registerBroadcastReceiver() {
//        broadcastManager = LocalBroadcastManager.getInstance(this);
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
//        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
//        broadcastReceiver = new BroadcastReceiver() {
//
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                if(intent!=null){
//                    String type = intent.getType();
//                    if(type == AppConfig.SP_add_contact_unread){//添加好友广播监控
//                        if(contactsFragment!=null){
//                            String toFriend = intent.getStringExtra(AppConfig.FRIEND_ID);
//                            contactsFragment.addEaseContact(toFriend);
//                        }
//                    }else if(type == AppConfig.SP_del_contact_unread){//删除好友广播监控
//
//                    }
//
//
//                }
//                List<InviteMessage> inviteMsgList = inviteMessgeDao.getMessagesList();
//                int count = 0;
//                for (InviteMessage invite:inviteMsgList){
//                    if(invite.getStatus() == InviteMessage.InviteMesageStatus.BEINVITEED){
//                        count++;
//                    }
//                }
//                if(count<=0)return;
//                if (mTabIndex == 2) {
//                    //正在通讯录界面，不处理
//                    iv_contact_unread_tag.setVisibility(View.INVISIBLE);
//                    contactsFragment.updateUnread(count);
//                } else {
//                    iv_contact_unread_tag.setVisibility(View.VISIBLE);
//                }
//                SPUtils.saveSpInt(getApplicationContext(),AppConfig.SP_add_contact_unread,count);
//            }
//        };
//        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
//    }
        //add luoxx 170630 好友变化监控 end

}
