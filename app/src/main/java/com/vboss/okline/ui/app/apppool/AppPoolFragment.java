package com.vboss.okline.ui.app.apppool;


import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.AppAds;
import com.vboss.okline.data.entities.AppEntity;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.ui.app.App;
import com.vboss.okline.ui.app.AppHelper;
import com.vboss.okline.ui.app.MyInstalledReceiver;
import com.vboss.okline.ui.app.adapter.CommonAdapter;
import com.vboss.okline.ui.app.adapter.ViewHolder;
import com.vboss.okline.ui.app.apppool.apppooltype.AppPoolTypeListFragment;
import com.vboss.okline.ui.app.search.AppSearchFragment;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.utils.TextUtils;

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
 * Summary  : App Pool主界面
 */

public class AppPoolFragment extends BaseFragment implements AppPoolContract.View {//,ViewPager.OnPageChangeListener
    private static final String TAG = "AppPoolFragment";

    @BindView(R.id.apppool_recyclerview)
    RecyclerView appPoolRecyclerView;

//    @BindView(R.id.adViewPager)
//    ViewPager viewPager;
//
//    @BindView(R.id.dotViewGroup)
//    LinearLayout dotViewGroup;

    @BindView(R.id.apppool_header)
    RecyclerViewHeader header;

    @BindView(R.id.new_app_name1)
    TextView new_app_name1;

    @BindView(R.id.new_app_name2)
    TextView new_app_name2;

    @BindView(R.id.new_app_name3)
    TextView new_app_name3;

    @BindView(R.id.new_app_name4)
    TextView new_app_name4;

    @BindView(R.id.new_app_name5)
    TextView new_app_name5;

    @BindView(R.id.new_app_name6)
    TextView new_app_name6;

    @BindView(R.id.new_app_name7)
    TextView new_app_name7;

    @BindView(R.id.new_app_name8)
    TextView new_app_name8;

    @BindView(R.id.new_app_icon1)
    SimpleDraweeView new_app_icon1;

    @BindView(R.id.new_app_icon2)
    SimpleDraweeView new_app_icon2;

    @BindView(R.id.new_app_icon3)
    SimpleDraweeView new_app_icon3;

    @BindView(R.id.new_app_icon4)
    SimpleDraweeView new_app_icon4;

    @BindView(R.id.new_app_icon5)
    SimpleDraweeView new_app_icon5;

    @BindView(R.id.new_app_icon6)
    SimpleDraweeView new_app_icon6;

    @BindView(R.id.new_app_icon7)
    SimpleDraweeView new_app_icon7;

    @BindView(R.id.new_app_icon8)
    SimpleDraweeView new_app_icon8;

    @BindView(R.id.new_app_installed_icon1)
    ImageView installed_icon1;

    @BindView(R.id.new_app_installed_icon2)
    ImageView installed_icon2;

    @BindView(R.id.new_app_installed_icon3)
    ImageView installed_icon3;

    @BindView(R.id.new_app_installed_icon4)
    ImageView installed_icon4;

    @BindView(R.id.new_app_installed_icon5)
    ImageView installed_icon5;

    @BindView(R.id.new_app_installed_icon6)
    ImageView installed_icon6;

    @BindView(R.id.new_app_installed_icon7)
    ImageView installed_icon7;

    @BindView(R.id.new_app_installed_icon8)
    ImageView installed_icon8;

    @BindView(R.id.new_app_update_icon1)
    ImageView update_icon1;

    @BindView(R.id.new_app_update_icon2)
    ImageView update_icon2;

    @BindView(R.id.new_app_update_icon3)
    ImageView update_icon3;

    @BindView(R.id.new_app_update_icon4)
    ImageView update_icon4;

    @BindView(R.id.new_app_update_icon5)
    ImageView update_icon5;

    @BindView(R.id.new_app_update_icon6)
    ImageView update_icon6;

    @BindView(R.id.new_app_update_icon7)
    ImageView update_icon7;

    @BindView(R.id.new_app_update_icon8)
    ImageView update_icon8;

    @BindView(R.id.fragment_toolbar)
    FragmentToolbar toolbar;

    MainActivity activity;
    AppPoolPresenter appPoolPresenter;
    List<AppEntity> appPoolList;
    List<App> appTypeList;
    CommonAdapter<App> appAdapter;

    List<TextView> textViewList;
    List<SimpleDraweeView> simpleDraweeViewList;
    List<ImageView> installedImgList;
    List<ImageView> pengdingUpdateImgList;
    List<String> componentNames;
    List<String> apkUrls;
    List<String> openUrls;
    List<Short> isDownLoads;
    List<String> appNames ;
    String isDownLoad;
    String componentName;
    String apkUrl;
    List<AppAds> adsList;
    /**
     * 装点点的ImageView数组
     */
    private ImageView[] tips;

    /**
     * 装ImageView数组
     */
    private SimpleDraweeView[] simpleDraweeViews;
    AppSearchFragment appSearchFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_pool, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initNewApp();

        initToolBar();
        initAppPoolAdapter();
        getAppTypeList();
        adsList = new ArrayList<>();
        appPoolPresenter = new AppPoolPresenter(activity,this);
        appSearchFragment = new AppSearchFragment();//AppSearchFragment.newInstance("app", "AppPool");
        //appPoolPresenter.getAdsImage();
    }

    @Override
    public void onResume() {
        super.onResume();
        appPoolPresenter.getNewAppList();
    }

    //初始化最新上架的控件
    private void initNewApp() {
        appPoolList = new ArrayList<>();
        textViewList = new ArrayList<>();
        textViewList.add(new_app_name1);
        textViewList.add(new_app_name2);
        textViewList.add(new_app_name3);
        textViewList.add(new_app_name4);
        textViewList.add(new_app_name5);
        textViewList.add(new_app_name6);
        textViewList.add(new_app_name7);
        textViewList.add(new_app_name8);
        simpleDraweeViewList = new ArrayList<>();
        simpleDraweeViewList.add(new_app_icon1);
        simpleDraweeViewList.add(new_app_icon2);
        simpleDraweeViewList.add(new_app_icon3);
        simpleDraweeViewList.add(new_app_icon4);
        simpleDraweeViewList.add(new_app_icon5);
        simpleDraweeViewList.add(new_app_icon6);
        simpleDraweeViewList.add(new_app_icon7);
        simpleDraweeViewList.add(new_app_icon8);
        componentNames = new ArrayList<>();
        apkUrls = new ArrayList<>();
        installedImgList = new ArrayList<>();
        installedImgList.add(installed_icon1);
        installedImgList.add(installed_icon2);
        installedImgList.add(installed_icon3);
        installedImgList.add(installed_icon4);
        installedImgList.add(installed_icon5);
        installedImgList.add(installed_icon6);
        installedImgList.add(installed_icon7);
        installedImgList.add(installed_icon8);
        pengdingUpdateImgList = new ArrayList<>();
        pengdingUpdateImgList.add(update_icon1);
        pengdingUpdateImgList.add(update_icon2);
        pengdingUpdateImgList.add(update_icon3);
        pengdingUpdateImgList.add(update_icon4);
        pengdingUpdateImgList.add(update_icon5);
        pengdingUpdateImgList.add(update_icon6);
        pengdingUpdateImgList.add(update_icon7);
        pengdingUpdateImgList.add(update_icon8);
        openUrls = new ArrayList<>();
        isDownLoads = new ArrayList<>();
        appNames = new ArrayList<>();

    }

    @OnClick({R.id.new_app_icon1,R.id.new_app_icon2,R.id.new_app_icon3,
            R.id.new_app_icon4,R.id.new_app_icon5,R.id.new_app_icon6
            ,R.id.new_app_icon7,R.id.new_app_icon8})
    public void ViewOnClick(View view){
        switch (view.getId()){
            case R.id.new_app_icon1:
                openOrUpload(0);
                break;
            case R.id.new_app_icon2:
                openOrUpload(1);
                break;
            case R.id.new_app_icon3:
                openOrUpload(2);
                break;
            case R.id.new_app_icon4:
                openOrUpload(3);
                break;
            case R.id.new_app_icon5:
                openOrUpload(4);
                break;
            case R.id.new_app_icon6:
                openOrUpload(5);
                break;
            case R.id.new_app_icon7:
                openOrUpload(6);
                break;
            case R.id.new_app_icon8:
                openOrUpload(7);
                break;
            default:
                break;
        }
    }

    public void openOrUpload(int index){
        componentName = componentNames.get(index);
        apkUrl = apkUrls.get(index);
        try{
            //modified by yuanshaoyu 2017-6-13: 卡助理写死为已安装
            UserRepository userRepository = UserRepository.getInstance(getActivity());
            User user = userRepository.getUser();
            if (appNames.get(index).equals("卡助理")) {
                if (user == null) {
                    Timber.tag(TAG).i("user is null");
                    return;
                }
                AppHelper.startAssistant(getActivity(),user);
            }else {
                //modified by yuanshaoyu 2017-5-24: 增加isDownload判断 是否从app pool中下载安装的app
                if (!TextUtils.isEmpty(componentName) && isDownLoads.get(index) == 1) {
                    Intent intent = activity.getPackageManager().getLaunchIntentForPackage(openUrls.get(index));
                    //modified by yuanshaoyu 2017-6-29 :增加欧乐社区穿欧乐号
                    intent.putExtra("olNo",user.getOlNo());
                    activity.startActivity(intent);
                }else {
                    if (!TextUtils.isEmpty(apkUrl)) {
                        Uri uri = Uri.parse(apkUrl);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        getActivity().startActivity(intent);
                    }

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void initToolBar() {
        activity = (MainActivity) getActivity();
        if (activity == null) {
            throw new NullPointerException("activity is null");
        }
        toolbar.setActionTitle(R.string.apppool);
        toolbar.setActionMenuVisible(View.VISIBLE);
        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
            @Override
            public void onNavigation(View v) {
                // 返回一级界面
                activity.removeSecondFragment();
            }
        });

        //搜索按钮点击
        toolbar.setOnActionMenuClickListener(new FragmentToolbar.OnActionMenuClickListener() {
            @Override
            public void onActionMenu(View v) {
                //modified by yuanshaoyu 2017-6-5:按照新需求修改搜索UI
//                //activity.addSecondFragment(MyAppSearchFragment.newInstance("app", "AppPool"));
                //modified by yuanshaoyu 2017-6-7:解决快速点击两次搜索按钮的重叠问题
                if (!appSearchFragment.isResumed()) {
                    //add by yuanshaoyu 2017-6-7:修复获取不到selectTag的闪退问题
                    appSearchFragment.setTag("AppPool","app");
                    appSearchFragment.show(getChildFragmentManager(), appSearchFragment.getClass().getSimpleName());
                }

            }
        });
    }

    private void initAppPoolAdapter() {
        appTypeList = new ArrayList<>();

        appAdapter = new CommonAdapter<App>(getActivity(),R.layout.apppool_item_layout) {
            @Override
            public void convert(final ViewHolder holder, final App app, int position) {
                if (position == 0) {
                    holder.setVisible(R.id.apppool_title_type,true);
                }else {
                    holder.setVisible(R.id.apppool_title_type,false);
                }
                //最下面一条不显示line
                if (appTypeList.size() - 1  == position) {
                    holder.setVisible(R.id.listview_line,false);
                }else {
                    holder.setVisible(R.id.listview_line,true);
                }
                holder.setText(R.id.apptype_Name,app.getAppName());
                holder.setImageDrawable(R.id.apptype_icon,app.getDrawable());
                holder.setOnClickListener(R.id.type_app, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.addSecondFragment(AppPoolTypeListFragment.newInstance(app.getAppName(), false));
                    }
                });
            }
        };
        appPoolRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        header.attachTo(appPoolRecyclerView,true);
        appPoolRecyclerView.setAdapter(appAdapter);
    }

    /**
     * 最新上架列表
     * @param list 最新上架列表数据集合
     */
    @Override
    public void showNewApp(List<AppEntity> list) {
        appPoolPresenter.unSubscribe();
        appPoolList.clear();
        componentNames.clear();
        //add by yuanshaoyu 2017-6-16 :clear isDownLoads
        isDownLoads.clear();
        openUrls.clear();
        if (list!=null && list.size()>0) {
            appPoolList.addAll(list);
            appAdapter.notifyDataSetChanged();

            int lenght = Math.min(list.size(),textViewList.size());
            for (int i = 0; i < lenght; i++) {
                textViewList.get(i).setText(list.get(i).appName());
                simpleDraweeViewList.get(i).setImageURI(list.get(i).appIcon());
                //modified by yuanshaoyu 2017-6-13: 卡助理在客户端写死为已安装
                if (list.get(i).appName().equals("卡助理")) {
                    installedImgList.get(i).setVisibility(View.VISIBLE);
                }else {
                    //modified by yuanshaoyu 2017-5-24: 增加isDownload判断 是否从app pool中下载安装的app
                    if (!TextUtils.isEmpty(list.get(i).componentName()) && list.get(i).isDownload() == 1) {
                        installedImgList.get(i).setVisibility(View.VISIBLE);
                        //localVersionCode< versionCode 则表示需要更新
                        if (list.get(i).localVersionCode()< list.get(i).versionCode()) {
                            pengdingUpdateImgList.get(i).setVisibility(View.VISIBLE);
                        }else {
                            pengdingUpdateImgList.get(i).setVisibility(View.GONE);
                        }
                    }else {
                        installedImgList.get(i).setVisibility(View.GONE);
                    }
                }

                componentNames.add(list.get(i).componentName());
                apkUrls.add(list.get(i).apkUrl());
                openUrls.add(list.get(i).openUrl());
                isDownLoads.add(list.get(i).isDownload());
                appNames.add(list.get(i).appName());
            }
        }
    }

    @Override
    public void showAdsImage(List<AppAds> list) {
       adsList.clear();
        if (list!=null && list.size()>0) {
            adsList.addAll(list);
        }
        //showAds(adsList);

    }

    /**
     * 显示头部广告栏
     //* @param list 广告栏数据集合
     */
//    public void showAds(List<AppAds> list){
//        Log.e(TAG," start  showAds");
//        //将广告位切换的点点加入到dotViewGroup中
//        tips = new ImageView[list.size()];
//        for(int i=0; i<tips.length; i++){
//            ImageView imageView = new ImageView(getActivity());
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            layoutParams.leftMargin = 5;
//            imageView.setLayoutParams(layoutParams);
//            tips[i] = imageView;
//            if(i == 0){
//                tips[i].setBackgroundResource(R.mipmap.select_dot);
//            }else{
//                tips[i].setBackgroundResource(R.mipmap.no_select_dot);
//            }
//
//            dotViewGroup.addView(imageView);
//        }
//
//        //将图片装载到数组中
//        if (list.size() == 1) {
//            Log.e(TAG,"list.size() == 1");
//            simpleDraweeViews = new SimpleDraweeView[2];
//            for (int i = 0; i < (simpleDraweeViews.length); i++) {
//                SimpleDraweeView simpleDraweeView = new SimpleDraweeView(getActivity());
//                simpleDraweeView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                simpleDraweeViews[i] = simpleDraweeView;
//                dotViewGroup.setVisibility(View.GONE);
//                viewPager.setOnTouchListener(new View.OnTouchListener() {
//
//                    @Override
//                    public boolean onTouch(View arg0, MotionEvent arg1) {
//                        // TODO Auto-generated method stub
//                    return true;
//                    }
//                });
//            }
//        }else if (list.size() == 2 || list.size() == 3) {
//            simpleDraweeViews = new SimpleDraweeView[list.size() * 2];
//            for (int i = 0; i < (simpleDraweeViews.length); i++) {
//                SimpleDraweeView simpleDraweeView = new SimpleDraweeView(getActivity());
//                simpleDraweeViews[i] = simpleDraweeView;
//                simpleDraweeView.setImageURI(Uri.parse(list.get((i > (list.size()-1)) ? i -list.size()  : i).imgUrl()));
//            }
//        } else {
//            simpleDraweeViews = new SimpleDraweeView[list.size()];
//            for(int i=0; i<simpleDraweeViews.length; i++){
//                SimpleDraweeView simpleDraweeView = new SimpleDraweeView(getActivity());
//                simpleDraweeViews[i] = simpleDraweeView;
//                simpleDraweeView.setImageURI(Uri.parse(list.get(i).imgUrl()));
//            }
//        }
//        //设置Adapter
//        viewPager.setAdapter(new MyAdapter());
//        //设置监听，主要是设置点点的背景
//        viewPager.setOnPageChangeListener(this);
//        viewPager.setCurrentItem(0);
//        dotViewGroup.setVisibility(View.VISIBLE);
//
//
//    }

//    @Override
//    public void onPageScrollStateChanged(int arg0) {
//
//    }
//
//    @Override
//    public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//    }
//
//    @Override
//    public void onPageSelected(int arg0) {
//        //这些判断为了解决当图片小于4张的时候切换点点的异常
//        if (adsList.size() == 2) {
//            if (arg0 %simpleDraweeViews.length == 2) {
//                setImageBackground(0);
//            }else if(arg0 %simpleDraweeViews.length == 3){
//                setImageBackground(1);
//            }else {
//                setImageBackground(arg0 % simpleDraweeViews.length);
//            }
//        }else if(adsList.size() == 3){
//            if (arg0 %simpleDraweeViews.length == 3) {
//                setImageBackground(0);
//            }else if(arg0 %simpleDraweeViews.length == 4){
//                setImageBackground(1);
//            }
//            else if(arg0 %simpleDraweeViews.length == 5){
//                setImageBackground(2);
//            }else {
//                setImageBackground(arg0 % simpleDraweeViews.length);
//            }
//        } else {
//            setImageBackground(arg0 % simpleDraweeViews.length);
//        }
//    }
//
//    /**
//     * 设置选中的tip的背景
//     * @param selectItems 选中项
//     */
//    private void setImageBackground(int selectItems){
//        for(int i=0; i<tips.length; i++){
//            if(i == selectItems){
//                tips[i].setBackgroundResource(R.mipmap.select_dot);
//            }else{
//                tips[i].setBackgroundResource(R.mipmap.no_select_dot);
//            }
//        }
//    }

    /**
     * app分类列表
     * @return  返回app分类列表
     */
    private List<App> getAppTypeList() {

        String[] typeName = getResources().getStringArray(R.array.app_pool_type_lables);
        int[] icons = getIconIds();
        boolean hasIcon = typeName.length == icons.length;
        appTypeList = new ArrayList<>();
        App app ;
        for (int i = 0; i < typeName.length; i++) {
            app = new App();
            app.setAppName(typeName[i]);
            if (hasIcon) {
                app.setDrawable(ActivityCompat.getDrawable(activity,icons[i]));
            }
            appTypeList.add(app);
        }
        appAdapter.setmDatas(appTypeList);
        return appTypeList;
    }

    //分类图片
    private int[] getIconIds() {
        TypedArray ta = getResources().obtainTypedArray(R.array.app_pool_type_drawables);
        int len = ta.length();

        if (len == 0) return new int[0];
        int[] ids = new int[len];
        for (int i = 0; i < len; i++) {
            ids[i] = ta.getResourceId(i, 0);
        }
        ta.recycle();
        return ids;
    }

//    public class MyAdapter extends PagerAdapter {
//
//        @Override
//        public int getCount() {
//            return Integer.MAX_VALUE;
//        }
//
//        @Override
//        public boolean isViewFromObject(View arg0, Object arg1) {
//            return arg0 == arg1;
//        }
//
//        @Override
//        public void destroyItem(View container, int position, Object object) {
//            ((ViewPager)container).removeView(simpleDraweeViews[position % simpleDraweeViews.length]);
//
//        }
//
//        /**
//         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
//         */
//        @Override
//        public Object instantiateItem(View container, int position) {
//            SimpleDraweeView simpleDraweeView = null;
//            try {
//                simpleDraweeView = simpleDraweeViews[position % simpleDraweeViews.length];
//                simpleDraweeView.setImageURI(Uri.parse(adsList.get(position).imgUrl()));
//                ((ViewPager)container).addView(simpleDraweeView);
//            }catch(Exception e){
//                //handler something
//            }
//            return simpleDraweeView == null ? new SimpleDraweeView(activity):simpleDraweeView;
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //add by yuanshaoyu 2017-6-19 :enent refresh
    @Subscribe(tags = {@Tag(AppHelper.EVENT_APP_REFRESH)})
    public void onRefresh(Boolean b){
        Timber.tag(TAG).i("appPool refresh data %s",b);
        appPoolPresenter.getNewAppList();
    }
}
