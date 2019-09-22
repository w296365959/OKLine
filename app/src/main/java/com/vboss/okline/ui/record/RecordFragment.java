package com.vboss.okline.ui.record;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cosw.sdkblecard.DeviceInfo;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.record.date.RecordWithDateFragment;
import com.vboss.okline.ui.record.organization.RecordWithOrganizationFragment;
import com.vboss.okline.ui.record.person.RecordWithPersonFragment;
import com.vboss.okline.ui.record.search.RecordSearchFragment;
import com.vboss.okline.ui.user.OCardAttachFragment;
import com.vboss.okline.ui.user.OcardFragment;
import com.vboss.okline.ui.user.UserFragment;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.ui.user.customized.NonScrollableViewPager;
import com.vboss.okline.view.widget.MonthDatePickerDialog;
import com.vboss.okline.view.widget.OKCardView;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/3/31
 * Summary : 记录模块UI
 */
public class RecordFragment extends BaseFragment {

    public static final String UNREAD_TAG_INSTANT_MESSAGE = "instant_msg";
    public static final String UNREAD_TAG_INSTITUTION = "institution";
    @BindView(R.id.sliding_tabs)
    TabLayout slidingTabs;
    @BindView(R.id.vp_record)
    NonScrollableViewPager vpRecord;
    @BindView(R.id.action_back)
    ImageButton action_back;
    @BindView(R.id.action_menu_button)
    ImageButton action_menu;
    @BindView(R.id.action_title)
    TextView action_title;
    @BindView(R.id.sdv_logo)
    SimpleDraweeView action_logo;
    Unbinder unbinder;
    @BindView(R.id.action_menu_layout)
    RelativeLayout actionMenuLayout;
    @BindView(R.id.iv_ocard_state)
    LogoView ivOcardState;
    @BindView(R.id.okcard_view)
    OKCardView okcardView;
    @BindView(R.id.action_back_layout)
    RelativeLayout actionBackLayout;
    private View view;
    private ArrayList<Fragment> fragments;
    private MainActivity activity;
    private RecordWithDateFragment recordWithDateFragment;
    private RecordWithPersonFragment recordWithPersonFragment;
    private RecordWithOrganizationFragment recordWithOrganizationFragment;
    private String[] strings = {"日期", "个人", "机构"};
    private boolean isSearchButtonClickable = true;
    private static final String TAG = "RecordFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("RecordFragment.onCreateView");
        view = inflater.inflate(R.layout.fragment_record, container, false);
        unbinder = ButterKnife.bind(this, view);
        initFragment();
        slidingTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab != null && tab.getCustomView() != null) {
                    int position = tab.getPosition();
                    ImageView unread_tag = (ImageView) tab.getCustomView().findViewById(R.id.unread_tag);
                    unread_tag.setVisibility(View.INVISIBLE);
                    if (position == 1) {
                        activity.sharedPreferences.edit().putBoolean(UNREAD_TAG_INSTANT_MESSAGE,false).apply();
                        activity.setUnreadDotVisibility();
                        //add by luoxx 0621 更新聊天记录里的用户数据
                        recordWithPersonFragment.loadConversationList();
                    }
                    if (position == 2) {
                        activity.sharedPreferences.edit().putBoolean(UNREAD_TAG_INSTITUTION,false).apply();
                        activity.setUnreadDotVisibility();
                    }
                    TextView viewById = (TextView) tab.getCustomView().findViewById(R.id.text);
                    viewById.setTextColor(getResources().getColor(R.color.black));

                    vpRecord.setCurrentItem(position);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView viewById = (TextView) tab.getCustomView().findViewById(R.id.text);
                viewById.setTextColor(getResources().getColor(R.color.transparency_65_2));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Added by wangshuai logoView click
        ivOcardState.setOnClickListener(activity);

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Utils.showLog(TAG,"onHiddenChanged:"+hidden);
        if (!hidden) {
            updateRedDotState();
        }
    }

    private void updateRedDotState() {
        if (slidingTabs != null) {
            switch (vpRecord.getCurrentItem()) {
                case 1:
                    activity.sharedPreferences.edit().putBoolean(UNREAD_TAG_INSTANT_MESSAGE,false).apply();
                    TabLayout.Tab tabAt1 = slidingTabs.getTabAt(1);
                    ImageView view1 = (ImageView) tabAt1.getCustomView().findViewById(R.id.unread_tag);
                    view1.setVisibility(View.INVISIBLE);
                    activity.setUnreadDotVisibility();
                    //add by luoxx 0621 更新聊天记录里的用户数据
                    recordWithPersonFragment.loadConversationList();
                    break;
                case 2:
                    activity.sharedPreferences.edit().putBoolean(UNREAD_TAG_INSTITUTION,false).apply();
                    TabLayout.Tab tabAt2= slidingTabs.getTabAt(2);
                    ImageView view2 = (ImageView) tabAt2.getCustomView().findViewById(R.id.unread_tag);
                    view2.setVisibility(View.INVISIBLE);
                    activity.setUnreadDotVisibility();
                    break;
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Utils.showLog(TAG,"setUserVisibleHint:"+isVisibleToUser);
    }

    public int getSubFragmentIndex(){
        int currentItem = -1;
        if (vpRecord != null) {
            currentItem = vpRecord.getCurrentItem();
        }
        return currentItem;
    }

    public void enlightenUnreadDot(int index, boolean flag){
        Utils.showLog(TAG,"enlightenUnreadDot设置记录界面红点");
        if (slidingTabs != null) {
            ImageView tag = (ImageView) slidingTabs.getTabAt(index).getCustomView().findViewById(R.id.unread_tag);
            tag.setVisibility(flag ?View.VISIBLE:View.INVISIBLE);
        }
    }

    public void initFragment() {
        //判断fragment是否添加进activity
        if (isAdded()) {
            recordWithDateFragment = new RecordWithDateFragment();
            recordWithPersonFragment = new RecordWithPersonFragment();
            recordWithOrganizationFragment = new RecordWithOrganizationFragment();
            fragments = new ArrayList<>();
            fragments.add(recordWithDateFragment);
            fragments.add(recordWithPersonFragment);
            fragments.add(recordWithOrganizationFragment);
            activity = (MainActivity) getActivity();
            //设置viewpager的adapter
            vpRecord.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    return fragments.get(position);
                }

                @Override
                public int getCount() {
                    return fragments.size();
                }
            });
            //Tablayout与viewpager联动 getPageTitle
            slidingTabs.setupWithViewPager(vpRecord, false);
            for (int i = 0; i < fragments.size(); i++) {
                TabLayout.Tab tab = slidingTabs.getTabAt(i);
                tab.setCustomView(R.layout.view_record_tablayout_tab);
                //tablayout上的悬浮小圆点
                TextView text = (TextView) tab.getCustomView().findViewById(R.id.text);
                ImageView unread_tag = (ImageView) tab.getCustomView().findViewById(R.id.unread_tag);
                switch (i) {//小红点的显示
                    case 0://日期

                        break;
                    case 1://个人
                        if (activity.sharedPreferences.getBoolean(UNREAD_TAG_INSTANT_MESSAGE,false)) {
                            unread_tag.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 2://机构
                        if (activity.sharedPreferences.getBoolean(UNREAD_TAG_INSTITUTION,false)) {
                            unread_tag.setVisibility(View.VISIBLE);
                        }
                        break;
                }
                text.setText(strings[i]);
                text.setTextColor(i==0?getResources().getColor(R.color.black):getResources().getColor(R.color.transparency_65_2));
            }
            slidingTabs.setTabMode(TabLayout.MODE_FIXED);
            //缓存页面数
            vpRecord.setOffscreenPageLimit(fragments.size() - 1);
            //viewpager页面改变监听
            vpRecord.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }
                /**
                 * 根据viewpager选择的fragment(tablayout)显示不同的的toolbar右上角图标
                 * @param position
                 */
                @Override
                public void onPageSelected(int position) {
                    switch (position) {
                        case 0:
                            Utils.showLog(TAG, "RecordFragment.onPageSelected:0");
                            action_menu.setImageResource(R.drawable.calendar0);
                            break;
                        case 1:
                            Utils.showLog(TAG, "RecordFragment.onPageSelected1");
                            action_menu.setImageResource(R.color.transparent);
                            break;
                        case 2:
                            Utils.showLog(TAG, "RecordFragment.onPageSelected2");
                            action_menu.setImageResource(R.mipmap.ic_search);
                            break;
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
    }

    /***
     * modify by wangshuai 2017-04-10
     * repair fragment add change ActionBar change
     ****/
    private void initToolbar() {
        action_back.setImageResource(R.mipmap.ic_navigation_me);
        action_title.setText(getResources().getString(R.string.tab_record));
        action_menu.setVisibility(View.VISIBLE);
        action_menu.setImageResource(R.drawable.calendar0);
        action_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击跳转到个人中心(我的)的页面
                activity.addSecondFragment(new UserFragment());
            }
        });
        //toolbar右上角图标点击
        actionMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSearchButtonClickable) {
                    isSearchButtonClickable = false;
                    switch (vpRecord.getCurrentItem()) {
                        case 0:
                            //显示日期下 日历dialog
                            showDatePickDialog();
                            break;
                        case 1:
                            isSearchButtonClickable = true;
                            break;
                        case 2://显示机构下 搜索fragment
                            //activity.addSecondFragment(RecordSearchFragment.newInstance(RecordSearchFragment.SEARCH_STATE_CARD, null, null, CardType.ALL, null));
                            //<editor-fold desc="2017-06-05 15:28:14 郑军 搜索流程重构">
                            //                            startActivity(new Intent(getContext(), RecordSearchActivity.class));
                            RecordSearchFragment.newInstance(0, CardType.ALL, null).show(getFragmentManager(), RecordSearchFragment.class.getName());
                            //</editor-fold>
                            isSearchButtonClickable = true;
                            break;
                    }
                }
            }
        });
    }
    /**
     * 显示日历
     */
    private void showDatePickDialog() {
        try {
            Calendar calendar = Calendar.getInstance();
            final MonthDatePickerDialog dialog = new MonthDatePickerDialog(activity, null,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            final DatePicker datePicker = dialog.getDatePicker();
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int year = datePicker.getYear();
                    int month = datePicker.getMonth();
                    boolean month0 = (month < 9);
                    int day = datePicker.getDayOfMonth();
                    boolean day0 = (day <= 9);
                    String text = year + "-" + (month0 ? "0" : "") + (month + 1) + "-" + (day0 ? "0" : "") + day;
                    recordWithDateFragment.setDate(text);
                }
            });
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    isSearchButtonClickable = true;
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("RecordFragment.onDestroyView");
        unbinder.unbind();
    }
}
