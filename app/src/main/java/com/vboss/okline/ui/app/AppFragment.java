package com.vboss.okline.ui.app;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cosw.sdkblecard.DeviceInfo;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.okline.vboss.assistant.base.Config;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.AppEntity;
import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.ui.app.adapter.CommonAdapter;
import com.vboss.okline.ui.app.adapter.DialogAdapter;
import com.vboss.okline.ui.app.adapter.JiugonggeAdapter;
import com.vboss.okline.ui.app.adapter.ViewHolder;
import com.vboss.okline.ui.app.apppool.AppPoolFragment;
import com.vboss.okline.ui.app.jiugongge.AnimationDialog;
import com.vboss.okline.ui.app.jiugongge.DraggableGridViewPager;
import com.vboss.okline.ui.app.search.AppSearchFragment;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.scanner.QRCodeActivity;
import com.vboss.okline.ui.user.OCardAttachFragment;
import com.vboss.okline.ui.user.OcardFragment;
import com.vboss.okline.ui.user.UserFragment;
import com.vboss.okline.utils.DensityUtil;
import com.vboss.okline.utils.TextUtils;
import com.vboss.okline.view.widget.OKCardView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: Yuan shaoyu <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/3/28 9:51 <br/>
 * Summary  : App模块主界面
 */

public class AppFragment extends BaseFragment implements AppContract.View ,View.OnClickListener{
    private static final String TAG = "AppFragment";
    @BindView(R.id.app_recyclerView)
    RecyclerView appRecyclerView;

    @BindView(R.id.enter_apppool_button)
    TextView enterApppoolButton;

    @BindView(R.id.no_app)
    TextView noApp;

    @BindView(R.id.app_update_RL)
    RelativeLayout appUpdateRL;

    @BindView(R.id.jiugongge_viewpager)
    DraggableGridViewPager jiugongge_viewpager;

    @BindView(R.id.dot_container)
    LinearLayout dot_container;

    //modify by yuanshaoyu 2017-06-20  logoView
    @BindView(R.id.logoView)
    LogoView logoView;

    //Added by yuanshaoyu 2017-06-21 add ocard battery capacity view
    @BindView(R.id.ocardView)
    OKCardView okCardView;

//    @BindView(R.id.app_update_num)
//    TextView app_update_num;
//
//    @BindView(R.id.update_dot)
//    RelativeLayout update_dot;

    @BindView(R.id.jiugongge_bg)
    FrameLayout jiugongge_bg;

    DraggableGridViewPager mDraggableGridViewPager;
    LinearLayout indicatorContainer;
    CommonAdapter<AppEntity> adapter;
    DialogAdapter dialogAdapter;
    JiugonggeAdapter jiugonggeAdapter;
    AnimationDialog dialog;
    List<AppEntity> starAppList;
    //所有已安装的APP
    List<AppEntity> allInstalledList;
    //九宫格列表项
    List<App> jiugonggeAllList;
    //九宫格弹窗列表的圆点集合
    List<ImageView> dotList;
    //底部九宫格列表项的圆点集合
    List<ImageView> jiugonggeDotList;
    List<AppEntity> commonList;
    List<AppEntity> bankList;
    List<AppEntity> vipList;
    List<AppEntity> doorList;
    List<AppEntity> workList;
    List<AppEntity> cerList;
    List<AppEntity> transList;
    AppPresenter appPresenter;
    PopupWindow popupWindow;
    MainActivity activity;
    int appType;
    private SparseArray<App> hashMap;
    String dataType = "";

    String[] typeNames ;
    String packageName;
    private PopupWindow morePopupWindow;
    AppSearchFragment appSearchFragment;
    private boolean isBluetoothConnecting;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         try{

             //appRepository = AppRepository.getInstance(activity);
             initAppAdapter();
             dialogAdapter = new DialogAdapter(activity);
             dialogAdapter.setFragment(AppFragment.this);

             typeNames = new String[]{
                     getResources().getString(R.string.card_tab_common),
                     getResources().getString(R.string.card_tab_member),
                     getResources().getString(R.string.card_tab_bank),
                     getResources().getString(R.string.card_tab_work),
                     getResources().getString(R.string.card_tab_certificates),
                     getResources().getString(R.string.card_tab_travel),
                     getResources().getString(R.string.card_tab_access)
             };

             hashMap = new SparseArray<>();
         }catch (Exception e){
             e.printStackTrace();
         }

        dialog = new AnimationDialog(activity);
        appSearchFragment = new AppSearchFragment();//AppSearchFragment.newInstance("app", "myApp");
        //Added by wangshuai logoView click
        logoView.setOnClickListener(activity);
    }

    @Override
    public void onResume() {
        super.onResume();

        jiugonggeAdapter = new JiugonggeAdapter(getActivity());
        jiugonggeAdapter.setFragment(AppFragment.this);
        appPresenter = new AppPresenter(activity, this);
        jiugonggeAllList = new ArrayList<>();
        jiugonggeAdapter.refresh(jiugonggeAllList);
        getJiugonggeMenuList();
        initJiugongge();
        getTypeData("");
        //modified by yuanshaoyu 2017/6/6 :按照新需求去掉更新 start
        //appPresenter.getUpdateApp(activity);

    }
    public void getJiugonggeMenuList() {
        String[] typeName = getResources().getStringArray(R.array.app_pool_type_lables);
        int[] type = new int[]{
                CardType.COMMON_CARD, CardType.VIP_CARD, CardType.BANK_CARD,
                //modified by yuanshaoyu 2017-5-26 :把交通类改为票务类
                CardType.EMPLOYEE_CARD, CardType.CREDENTIALS, CardType.TICKET,
                CardType.DOOR_CARD,
        };
        jiugonggeAllList = new ArrayList<>();
        App app ;
        for (int i = 0; i < typeName.length; i++) {
            app = new App();
            app.setAppName(typeName[i]);
            getTypeData(typeName[i]);
            jiugonggeAllList.add(app);
            hashMap.put(type[i], app);
        }
        jiugonggeAdapter.refresh(jiugonggeAllList);
    }

    //获取dialog分类数据
    public void getTypeData(String typeName) {
        if (typeName.equals(typeNames[0])) {
            appType = CardType.COMMON_CARD;
        } else if (typeName.equals(typeNames[1])) {
            appType = CardType.VIP_CARD;
        } else if (typeName.equals(typeNames[2])) {
            appType = CardType.BANK_CARD;
        } else if (typeName.equals(typeNames[3])) {
            appType = CardType.EMPLOYEE_CARD;
        } else if (typeName.equals(typeNames[4])) {
            appType = CardType.CREDENTIALS;
        } else if (typeName.equals(typeNames[5])) {
            appType = CardType.TICKET;
        } else if (typeName.equals(typeNames[6])) {
            appType = CardType.DOOR_CARD;
        } else {
            appType = CardType.ALL;
        }
        appPresenter.getJiugonggeDialogData(appType, null);
    }

    //九宫格列表中数据"删除"的监听
    public void deleteApp(List<AppEntity> list, int position, String pkgName) {
        uninstallApp(activity, pkgName);
        showDot(list);
        int curPageItem = (int) Math.ceil((float) (position + 1) / 9) - 1;
        changeDot(curPageItem);
    }

    //卸载APP
    public void uninstallApp(Context context, String appPackage) {
        Uri packageURI = Uri.parse("package:" + appPackage);
        //调用系统自带卸载操作进行卸载
        Intent uninstallIntent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        context.startActivity(uninstallIntent);
    }

    //九宫格列表中数据"添加星标"的监听
    public void addStar(int appId) {
        appPresenter.StarApp(appId, 1);
        dialog.dismiss();
        adapter.notifyDataSetChanged();
    }

    //初始化
    public void initJiugongge() {
        jiugongge_viewpager.setColCount(5);
        jiugongge_viewpager.setRowCount(1);
        jiugongge_viewpager.setAdapter(jiugonggeAdapter);
        jiugongge_viewpager.setOnPageChangeListener(new DraggableGridViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                jiugonggeChangeDot(position);
            }
        });
        showJiugonggeDot();
        jiugonggeChangeDot(0);
    }

    //显示九宫格翻页的圆点显示
    public void showJiugonggeDot() {
        jiugonggeDotList = new ArrayList<>();
        dot_container.removeAllViews();
        //九宫格列表页数
        int jiugonggePage = (int) Math.ceil((float) jiugonggeAllList.size() / 5);
        if (jiugonggePage != 1) {//只有一页不需要翻页
            for (int i = 0; i < jiugonggePage; i++) {
                ImageView imageView = new ImageView(getActivity());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.leftMargin = 10;
                layoutParams.rightMargin = 10;
                imageView.setLayoutParams(layoutParams);
                dot_container.addView(imageView);
                jiugonggeDotList.add(imageView);
            }
        }
    }

    @OnClick({R.id.app_update_RL, R.id.myapp_search, R.id.enter_apppool_button,R.id.ib_more})//
    public void ViewOnClick(View view) {
        switch (view.getId()) {
            case R.id.app_update_RL:
                //activity.addSecondFragment(new updateManageFragment());
                break;
            case R.id.myapp_search:
                //activity.addSecondFragment(MyAppSearchFragment.newInstance("app", "myApp"));
                //modified by yuanshaoyu 2017-6-7:解决快速点击两次搜索按钮的重叠问题
                if (!appSearchFragment.isResumed()) {
                    //add by yuanshaoyu 2017-6-7:修复获取不到selectTag的闪退问题
                    appSearchFragment.setTag("myApp","app");
                    appSearchFragment.show(getChildFragmentManager(), appSearchFragment.getClass().getSimpleName());
                }

                break;
            case R.id.enter_apppool_button:
                activity.addSecondFragment(new AppPoolFragment());
                break;
            //add by yuanshaoyu 2017/6/6 :按照新需求修改“TA”界面的标题样式 start
            case R.id.ib_more:
                View convertView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_card_search_pop, null);
                morePopupWindow = new PopupWindow(convertView, DensityUtil.dip2px(getActivity(), 120),
                        DensityUtil.dip2px(getActivity(), 100));
                morePopupWindow.setBackgroundDrawable(new ColorDrawable());
                morePopupWindow.setOutsideTouchable(true);
                convertView.findViewById(R.id.tv_pop_receivables).setOnClickListener(this);
                convertView.findViewById(R.id.tv_pop_scanner).setOnClickListener(this);
                morePopupWindow.showAsDropDown(view, DensityUtil.dip2px(getActivity(), 5), 0);
                //start
            default:
                break;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pop_scanner:
                activity.startActivity(new Intent(activity, QRCodeActivity.class));
                break;
            case R.id.tv_pop_receivables:

                break;
        }
        morePopupWindow.dismiss();
    }

    //显示九宫格
    public void showJiugonggeDialog(List<AppEntity> dataList,String type) {
        dataType = type;
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.jiugongge_dialog_layout, null);
        // moved by yuanshaoyu 2017-5-6-12:20 修改同时弹出两个弹框且无法关闭弹窗的问题
        if (!dialog.isShowing()) {
            dialog.showDialog(dialogView);
            dialog.show();
            dialogView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            // moved by yuanshaoyu 2017-5-3 :  九宫格增加类型标题   start
            TextView dialog_title = (TextView) dialogView.findViewById(R.id.app_dialog_title);
            dialog_title.setText(type);
            //moved by yuanshaoyu 2017-5-3 end

            mDraggableGridViewPager = (DraggableGridViewPager) dialogView.findViewById(R.id.draggable_grid_view_pager);
            indicatorContainer = (LinearLayout) dialogView.findViewById(R.id.indicator_container);
            mDraggableGridViewPager.setColCount(3);
            mDraggableGridViewPager.setRowCount(3);
            mDraggableGridViewPager.setAdapter(dialogAdapter);
            mDraggableGridViewPager.setOnPageChangeListener(new DraggableGridViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    changeDot(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }else {
            dialog.dismiss();
        }

        dialogAdapter.refresh(dataList);
        dotList = new ArrayList<>();
        showDot(dataList);
        changeDot(0);
    }

    public void showDot(List<AppEntity> dialogList) {

        dotList.clear();
        indicatorContainer.removeAllViews();
        //总页数
        double pageCount = Math.ceil((float) dialogList.size() / 9);
        //添加小点点
        if (pageCount != 1.0) {//一页时无需翻页
            for (int i = 0; i < pageCount; i++) {
                ImageView imageview = new ImageView(getActivity());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.leftMargin = 10;
                layoutParams.rightMargin = 10;
                imageview.setLayoutParams(layoutParams);
                indicatorContainer.addView(imageview);
                dotList.add(imageview);
            }
        }

    }

    //九宫格列表的翻页切换点点显示
    private void changeDot(int position) {
        for (int i = 0; i < dotList.size(); i++) {
            if (i == position) {
                dotList.get(i).setImageDrawable(ActivityCompat.getDrawable(activity,R.mipmap.select_dot));
            } else {
                dotList.get(i).setImageDrawable(ActivityCompat.getDrawable(activity,R.mipmap.no_select_dot));
            }
        }
    }

    //九宫格列表的翻页切换点点显示
    private void jiugonggeChangeDot(int position) {
        for (int i = 0; i < jiugonggeDotList.size(); i++) {
            if (i == position) {
                jiugonggeDotList.get(i).setImageDrawable(ActivityCompat.getDrawable(activity,R.mipmap.jiugongge_dot));
            } else {
                jiugonggeDotList.get(i).setImageDrawable(ActivityCompat.getDrawable(activity,R.mipmap.no_select_dot));
            }
        }
    }

    //九宫格数据列表
    @Override
    public void showJiugonggeDialogData(List<AppEntity> list) {
        appPresenter.unSubscribe();
        allInstalledList = new ArrayList<>();
        allInstalledList.clear();
        if (list != null && list.size() > 0) {
            noApp.setVisibility(View.GONE);

            appRecyclerView.setVisibility(View.VISIBLE);
            //modified by yuanshaoyu 2017-6-22 :app分类类型一直显示
            //jiugongge_bg.setVisibility(View.VISIBLE);
            allInstalledList.addAll(list);
            //dialogAdapter.refresh(allInstalledList);
            getStarAppList();
        }else {
            noApp.setVisibility(View.VISIBLE);
            appRecyclerView.setVisibility(View.GONE);
            //modified by yuanshaoyu 2017-6-22 :app分类类型一直显示
            //jiugongge_bg.setVisibility(View.GONE);
        }
    }

    /**
     * 常用类数据
     *
     * @param list  常用类数据集合
     */
    @Override
    public void showCommonData(List<AppEntity> list) {
        commonList = new ArrayList<>();
        commonList.clear();
        if (list != null && list.size() > 0) {
            jiugongge_bg.setVisibility(View.VISIBLE);
            commonList.addAll(list);
            jiugonggeAdapter.setCommonData(commonList);
            //解决弹窗显示的时候APP的增加或减少的刷新
            if (!TextUtils.isEmpty(dataType) && dataType.equals(typeNames[0])) {
                dialogAdapter.refresh(commonList);
            }
        } else {
            //modified by yuanshaoyu 2017-6-22 :app分类类型一直显示
//            jiugonggeAllList.remove(hashMap.get(CardType.COMMON_CARD));
            jiugonggeAdapter.notifyDataSetChanged();
            initJiugongge();

            //解决该类型中最后一个APP删除的时候弹窗数据的刷新
            if (!TextUtils.isEmpty(dataType) && dataType.equals(typeNames[0]) && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    /**
     * 会员类数据
     *
     * @param list 会员类数据集合
     */
    @Override
    public void showVipData(List<AppEntity> list) {
        vipList = new ArrayList<>();
        vipList.clear();
        if (list != null && list.size() > 0) {
            jiugongge_bg.setVisibility(View.VISIBLE);
            vipList.addAll(list);
            jiugonggeAdapter.setVipData(vipList);
            if (!TextUtils.isEmpty(dataType) && dataType.equals(typeNames[1])) {
                dialogAdapter.refresh(vipList);
            }
        } else {
            //modified by yuanshaoyu 2017-6-22 :app分类类型一直显示
//            jiugonggeAllList.remove(hashMap.get(CardType.VIP_CARD));
            jiugonggeAdapter.notifyDataSetChanged();
            initJiugongge();

            if (!TextUtils.isEmpty(dataType) && dataType.equals(typeNames[1]) && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    /**
     * 银行类数据
     *
     * @param list 银行类数据集合
     */
    @Override
    public void showBankData(List<AppEntity> list) {
        bankList = new ArrayList<>();
        bankList.clear();
        if (list != null && list.size() > 0) {
            jiugongge_bg.setVisibility(View.VISIBLE);
            bankList.addAll(list);
            jiugonggeAdapter.setBankData(bankList);
            if (!TextUtils.isEmpty(dataType) && dataType.equals(typeNames[2])) {
                dialogAdapter.refresh(bankList);
            }
        } else {
//modified by yuanshaoyu 2017-6-22 :app分类类型一直显示
//            jiugonggeAllList.remove(hashMap.get(CardType.BANK_CARD));
            jiugonggeAdapter.notifyDataSetChanged();
            initJiugongge();

            if (!TextUtils.isEmpty(dataType) && dataType.equals(typeNames[2]) && dialog.isShowing()) {
                dialog.dismiss();

            }
        }
    }

    /**
     * 工作类数据
     *
     * @param list 工作类数据集合
     */
    @Override
    public void showWorkData(List<AppEntity> list) {
        workList = new ArrayList<>();
        workList.clear();
        if (list != null && list.size() > 0) {
            jiugongge_bg.setVisibility(View.VISIBLE);
            workList.addAll(list);
            jiugonggeAdapter.setWorkData(workList);
            if (!TextUtils.isEmpty(dataType) && dataType.equals(typeNames[3])) {
                dialogAdapter.refresh(workList);
            }
        } else {
            //modified by yuanshaoyu 2017-6-22 :app分类类型一直显示
//            jiugonggeAllList.remove(hashMap.get(CardType.EMPLOYEE_CARD));
            jiugonggeAdapter.notifyDataSetChanged();
            initJiugongge();

            if (!TextUtils.isEmpty(dataType) && dataType.equals(typeNames[3]) && dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    }

    @Override
    public void showUpdateApp(List<AppEntity> list) {
//        if (list!=null &&list.size()>0) {
//            update_dot.setVisibility(View.VISIBLE);
//            app_update_num.setText(String.valueOf(list.size()));
//        }else {
//            update_dot.setVisibility(View.GONE);
//        }
    }

    //得到星标APP列表
    public void getStarAppList(){
        starAppList.clear();
        for (int i = 0; i < allInstalledList.size(); i++) {
            int star = allInstalledList.get(i).isFollow();
            if (star == 1) {
                starAppList.add(allInstalledList.get(i));
            }
            //add by yuanshaoyu 2017-6-14 :卡助理默认星标
//            if (allInstalledList.get(i).appName().equals("卡助理")) {
//                starAppList.add(allInstalledList.get(i));
//            }
        }
        adapter.setmDatas(starAppList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showStarApp(Boolean aBoolean) {
        if (aBoolean) {
            //modified by yuanshaoyu 2017-6-23 :解决加星标数据没有刷新
            appPresenter.getJiugonggeDialogData(CardType.ALL,null);
            //getStarAppList();
        }
    }

    /**
     * 证件类数据
     *
     * @param list 证件类数据集合
     */
    @Override
    public void showCredentialsData(List<AppEntity> list) {
        cerList = new ArrayList<>();
        cerList.clear();
        if (list != null && list.size() > 0) {
            cerList.addAll(list);
            jiugonggeAdapter.setCredentialsData(cerList);
            if (!TextUtils.isEmpty(dataType) && dataType.equals(typeNames[4])) {
                dialogAdapter.refresh(cerList);
            }
        } else {
            //modified by yuanshaoyu 2017-6-22 :app分类类型一直显示
//            jiugonggeAllList.remove(hashMap.get(CardType.CREDENTIALS));
            jiugonggeAdapter.notifyDataSetChanged();
            initJiugongge();

            if (!TextUtils.isEmpty(dataType) && dataType.equals(typeNames[4]) && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    /**
     * 交通类数据
     *
     * @param list 交通类数据集合
     */
    @Override
    public void showTransData(List<AppEntity> list) {
        transList = new ArrayList<>();
        transList.clear();
        if (list != null && list.size() > 0) {
            transList.addAll(list);
            jiugonggeAdapter.setTransData(transList);
            if (!TextUtils.isEmpty(dataType) && dataType.equals(typeNames[5])) {
                dialogAdapter.refresh(transList);
            }
        } else {
            //modified by yuanshaoyu 2017-6-22 :app分类类型一直显示
//            jiugonggeAllList.remove(hashMap.get(CardType.TICKET));
            jiugonggeAdapter.notifyDataSetChanged();
            initJiugongge();

            if (!TextUtils.isEmpty(dataType) && dataType.equals(typeNames[5]) && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    /**
     * 门禁类数据
     *
     * @param list 门禁类数据集合
     */
    @Override
    public void showDoorData(List<AppEntity> list) {
        doorList = new ArrayList<>();
        doorList.clear();
        if (list != null && list.size() > 0) {
            doorList.addAll(list);
            jiugonggeAdapter.setDoorData(doorList);
            if (!TextUtils.isEmpty(dataType) && dataType.equals(typeNames[6])) {
                dialogAdapter.refresh(doorList);
            }
        } else {
            //modified by yuanshaoyu 2017-6-22 :app分类类型一直显示
//            jiugonggeAllList.remove(hashMap.get(CardType.DOOR_CARD));
            jiugonggeAdapter.notifyDataSetChanged();
            initJiugongge();

            if (!TextUtils.isEmpty(dataType) && dataType.equals(typeNames[6]) && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    //加载适配器
    private void initAppAdapter() {
        starAppList = new ArrayList<>();
        adapter = new CommonAdapter<AppEntity>(getActivity(), R.layout.app_item_layout, starAppList) {
            @Override
            public void convert(ViewHolder holder, final AppEntity appEntity, final int position) {
                holder.setVisible(R.id.app_installed_icon,true);
                holder.setText(R.id.app_name, appEntity.appName());
                holder.setImageByUrl(R.id.app_icon, appEntity.appIcon());
                holder.setOnClickListener(R.id.app_icon, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: 2017/3/31  进入相应APP
                        ComponentName componentName = ComponentName.unflattenFromString(appEntity.componentName());
                        try {
                            if (!TextUtils.isEmpty(appEntity.openUrl())) {
                                //add by yuanshaoyu 2017-6-13 :卡助理传入olno与address
                                UserRepository userRepository = UserRepository.getInstance(getActivity());
                                User user = userRepository.getUser();
                                if (appEntity.appName().equals("卡助理")) {
                                    if (user == null) {
                                        Timber.tag(TAG).i("user is null");
                                        return;
                                    }
                                    AppHelper.startAssistant(getActivity(),user);
                                }else {
                                    Intent intent = activity.getPackageManager().getLaunchIntentForPackage(appEntity.openUrl());
                                    //modified by yuanshaoyu 2017-6-29 :增加欧乐社区穿欧乐号
                                    intent.putExtra("olNo",user.getOlNo());
                                    startActivity(intent);
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });
                //取消星标or卸载
                holder.setOnLongClickListener(R.id.app_icon, new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showPopUp(v, position);
                        return true;
                    }
                });
            }
        };
        appRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        appRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        appRecyclerView.setAdapter(adapter);
    }

    //取消星标or卸载的弹窗
    private void showPopUp(View v, final int position) {
        View popView = LayoutInflater.from(getActivity()).inflate(R.layout.star_popupwindow_layout, null);
        popupWindow = new PopupWindow(popView,
                (int) activity.getResources().getDimension(R.dimen.cancle_star_dialog_width),
                (int) activity.getResources().getDimension(R.dimen.add_star_dialog_height));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        backgroundAlpha(0.5f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        //所在列
        double colNum = position % 4;
        //popupwindow的显示超出dialog的时候，改变popupwindow的显示方向
        if (colNum == 3.0) {
            popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0] + v.getWidth() - popupWindow.getWidth(), location[1] + v.getHeight() + 10);
        } else {
            popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1] + v.getHeight() + 10);
        }

        String componentName = starAppList.get(position).componentName();
        //获取应用包名

        if (!TextUtils.isEmpty(componentName)) {
            packageName = componentName.substring(0, componentName.indexOf("/"));
        }
        LinearLayout add_star_button = (LinearLayout) popView.findViewById(R.id.star_add_star);
//        LinearLayout app_upload = (LinearLayout) popView.findViewById(R.id.app_upload);
        SimpleDraweeView pop_img = (SimpleDraweeView) popView.findViewById(R.id.star_pop_img);
        TextView pop_title = (TextView) popView.findViewById(R.id.star_pop_title);
        pop_img.setImageURI(starAppList.get(position).appIcon());
        pop_title.setText(starAppList.get(position).appName());
//        final TextView star_text = (TextView) popView.findViewById(R.id.star_text);
//        star_text.setText(getResources().getText(R.string.cancel_star));
        //取消星标
        add_star_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appPresenter.StarApp((int) starAppList.get(position).appId(), 0);
                getTypeData("");
                popupWindow.dismiss();
                adapter.notifyDataSetChanged();
            }
        });
        //卸载
//        app_upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                uninstallApp(activity, packageName);
//                popupWindow.dismiss();
//                adapter.notifyDataSetChanged();
//            }
//        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 背景透明度
     */

    public void backgroundAlpha(float bgAlpha)

    {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        appPresenter.unSubscribe();
    }

//    //add by yuanshaoyu 2017-6-19 :enent refresh
//    @Subscribe(tags = {@Tag(AppHelper.EVENT_APP_REFRESH)})
//    public void onRefresh(Boolean b){
//        Timber.tag(TAG).i("appPool refresh data %s",b);
//        appPoolPresenter.getNewAppList();
//    }
}