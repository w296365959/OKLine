package com.vboss.okline.ui.app.search;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vboss.okline.R;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.base.helper.ToolbarSearchHelper;
import com.vboss.okline.data.CardRepository;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.AppEntity;
import com.vboss.okline.data.entities.CardCondition;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.ui.app.adapter.CommonAdapter;
import com.vboss.okline.ui.app.adapter.OnItemClickListener;
import com.vboss.okline.ui.app.adapter.ViewHolder;
import com.vboss.okline.ui.app.apppool.apppooltype.AppPoolTypeListFragment;
import com.vboss.okline.ui.card.CardConstant;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.user.OpenCardActivity;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.ui.user.customized.LoadingDialog;
import com.vboss.okline.utils.StringUtils;
import com.vboss.okline.utils.TextUtils;
import com.vboss.okline.view.widget.shadow.ListViewShadowViewHelper;
import com.vboss.okline.view.widget.shadow.ShadowProperty;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: Yuan shaoyu <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/3/28 9:51 <br/>
 * Summary  : 我的App搜索界面
 */

public class MyAppSearchFragment extends Fragment implements AppSearchContract.View{
    public static final String TAG = "MyAppSearchFragment";

    @BindView(R.id.app_search_RecyclerView)
    RecyclerView app_search_RecyclerView;

    @BindView(R.id.search_result_num)
    TextView searchResultNum;

    @BindView(R.id.hot_app_title)
    TextView hot_app_title;

    @BindView(R.id.search_app_title)
    LinearLayout search_app_title;

    ToolbarSearchHelper searchHelper;
    MainActivity activity;
    //热门应用列表
    List<AppEntity> hotAppList ;

    List<AppEntity> searchMyAppList;
    List<CardEntity> searchCardList;
    CommonAdapter<AppEntity> adapter;
    CommonAdapter<CardEntity> cardAdapter;
    String previousFragment;
    String getselectTag;
    AppSearchPresenter appSearchPresenter;
    IntentFilter intentFilter;
    MyInstallAppReceiver myInstallAppReceiver;
    private boolean isPreparingCardOpen;
    private CardCondition cardCondition;
    private ArrayList<Object> cardEntityArrayList;

    int searchButtonTag = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_my_app_search, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addDataScheme("package");
        myInstallAppReceiver = new MyInstallAppReceiver();
        getActivity().registerReceiver(myInstallAppReceiver, intentFilter);

        //获取上级页面
        Bundle bundle = getArguments();
        previousFragment = bundle.getString("previousFragment");
        getselectTag = bundle.getString("selectTag");
        initAdapter();
        initSearchBar();
        appSearchPresenter = new AppSearchPresenter(activity,this);

        //进页面加载数据会造成切换动画卡顿，故延迟0.2s加载数据会解决卡顿问题
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                appSearchPresenter.getHotApp();
            }
        },200);

        //modify by yuanshoayu 2017-05-12 hide bottom tab
        activity.hideTabs(true);
    }

    //added by yuanshaoyu 2017-5-26:解决重新进入界面热门应用与搜索结果显示错乱的问题
    @Override
    public void onResume() {
        super.onResume();
        String key = searchHelper.getSearchText().getText().toString();
        if (searchButtonTag == 1) {
            if (TextUtils.isEmpty(key)) {
                search_app_title.setVisibility(View.GONE);
                hot_app_title.setVisibility(View.VISIBLE);
                if (previousFragment.equals("appPoolType") && getselectTag.equals("card")) {
                    initAdapter();
                }
                appSearchPresenter.getHotApp();
                searchButtonTag = 0;
            }else {
                search_app_title.setVisibility(View.VISIBLE);
                hot_app_title.setVisibility(View.GONE);
                if (previousFragment.equals("myApp")) {
                    appSearchPresenter.getSearchAppPool(CardType.ALL,key,1,50);
                }else if(previousFragment.equals("AppPool")){
                    appSearchPresenter.getSearchAppPool(CardType.ALL,key,1,50);
                }else if(previousFragment.equals("appPoolType") && getselectTag.equals("app")){
                    appSearchPresenter.getSearchAppPool(CardType.ALL,key,1,50);
                }else if(previousFragment.equals("appPoolType") && getselectTag.equals("card")){
                    initCardListAdapter();
                    appSearchPresenter.getSearchAppPoolCard(CardType.ALL,key,1,50);
                }
            }

        }else {
            appSearchPresenter.getHotApp();
            if (!TextUtils.isEmpty(key)) {
                searchHelper.getSearchText().setText("");
            }
        }
    }

    private void initAdapter() {
        hotAppList = new ArrayList<>();
        searchMyAppList = new ArrayList<>();
        adapter = new CommonAdapter<AppEntity>(getActivity(),R.layout.app_item_layout,hotAppList) {
            @Override
            public void convert(ViewHolder holder, final AppEntity app, int position) {
                holder.setText(R.id.app_name,app.appName());
                holder.setImageByUrl(R.id.app_icon,app.appIcon());
                final String componentName = app.componentName();
                final String apkUrl = app.apkUrl();
                //modified by yuanshaoyu 2017-5-24: 增加isDownload判断 是否从app pool中下载安装的app
                if (!TextUtils.isEmpty(app.componentName()) && app.isDownload() == 1) {
                    holder.setVisible(R.id.app_installed_icon,true);
                    if (app.localVersionCode() < app.versionCode()) {
                        holder.setVisible(R.id.app_pendingupdate_icon,true);
                    }else {
                        holder.setVisible(R.id.app_pendingupdate_icon,false);
                    }
                }else {
                    holder.setVisible(R.id.app_installed_icon,false);
                }
                //下载APP或进入应用
                holder.setOnClickListener(R.id.app_icon, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (componentName != null && app.isDownload() == 1) {
                            Intent intent = activity.getPackageManager().getLaunchIntentForPackage(app.openUrl());
                            activity.startActivity(intent);
                        }else {
                            if (!TextUtils.isEmpty(apkUrl)) {
                                // add by yuanshaoyu 2017-5-24 :判断自己应用下载的app
                                appSearchPresenter.poolAppDownload(app.openUrl());
                                Uri uri = Uri.parse(apkUrl);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                getActivity().startActivity(intent);
                            }

                        }
                    }
                });
            }
        };
        app_search_RecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),4));
        app_search_RecyclerView.setAdapter(adapter);
    }

    private void initCardListAdapter() {
        searchCardList = new ArrayList<>();
        cardAdapter = new CommonAdapter<CardEntity>(getActivity(), R.layout.cardlist_item_layout,searchCardList) {
            @Override
            public void convert(ViewHolder holder, CardEntity cardEntity, int position) {

                //add by yuanshaoyu 2017-5-10 :判断是否已开卡
                if (cardEntity.isOpen() == 0) {
                    //未开卡
                    holder.setVisible(R.id.open_card_tip,false);
                }else if(cardEntity.isOpen() > 0){
                    //已开卡
                    holder.setVisible(R.id.open_card_tip,true);
                }

                holder.setText(R.id.app_cardName, cardEntity.cardName());
                holder.setText(R.id.app_cardMerName, cardEntity.merName());
                if (!TextUtils.isEmpty(cardEntity.imgUrl())) {
                    holder.setImageByUrl(R.id.app_cardImg, cardEntity.imgUrl());
                }

                View card_img = holder.getView(R.id.app_cardImg);
                //卡片阴影
                int r2 = StringUtils.dip2px(activity, 7.5f);
                ListViewShadowViewHelper.bindShadowHelper(
                        new ShadowProperty()
                                .setShadowColor(ActivityCompat.getColor(mContext, R.color.colorShadow))
                                .setShadowDy(StringUtils.dip2px(activity, 0.25f))
                                .setShadowRadius(StringUtils.dip2px(activity, 2f))
                        , card_img, r2, r2);
            }
        };
        app_search_RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        app_search_RecyclerView.setAdapter(cardAdapter);
        cardAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                final CardEntity card = searchCardList.get(position);
                if (card.isOpen()!=0) {
                    Utils.customToast(getContext(), "不能重复开卡", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isPreparingCardOpen) {
                    isPreparingCardOpen = true;
                    Utils.showLog(TAG,"开始获取开卡信息");
                    final LoadingDialog loadingDialog = LoadingDialog.getInstance();
                    loadingDialog.setLoadingDialogListener(new LoadingDialog.LoadingDialogListener() {
                        @Override
                        public void onDismiss() {
                            isPreparingCardOpen = false;
                        }

                        @Override
                        public void onShow() {
                            Observable.zip(
                                    UserRepository.getInstance(getContext()).getCardOpenCondition(card.cardMainType(), card.aid()),
                                    CardRepository.getInstance(getContext()).cardList(CardType.BANK_CARD),
                                    new Func2<CardCondition, List<CardEntity>, Boolean>() {
                                        @Override
                                        public Boolean call(CardCondition cardCondition, List<CardEntity> cardEntities) {
                                            Utils.showLog(TAG,"开卡条件："+cardCondition);
                                            Utils.showLog(TAG,"银行卡："+cardEntities);
                                            MyAppSearchFragment.this.cardCondition = cardCondition;
                                            cardEntityArrayList = new ArrayList<>();
                                            for (CardEntity cardEntity : cardEntities) {
                                                if (cardEntity.isQuickPass() == 2) {
                                                    cardEntityArrayList.add(cardEntity);
                                                }
                                            }
                                            return !cardEntityArrayList.isEmpty();
                                        }
                                    })
                                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new DefaultSubscribe<Boolean>(TAG){
                                        @Override
                                        public void onError(Throwable throwable) {
                                            super.onError(throwable);
                                            Utils.showLog(TAG, "开卡信息获取失败：" + throwable.getMessage());
                                            loadingDialog.onFinished("开卡信息获取失败", 1000);
                                        }

                                        @Override
                                        public void onNext(Boolean aBoolean) {
                                            super.onNext(aBoolean);
                                            loadingDialog.onFinished(null, 0);
                                            if (cardCondition.needFeeToOpen() == 1 || cardCondition.needPrestoreToOpen() == 1) {
                                                if (aBoolean) {
                                                    switchToOpenCardPage(cardCondition, card);
                                                } else {
                                                    Utils.customToast(getContext(), getString(R.string.card_open_no_card_alert), Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                switchToOpenCardPage(cardCondition, card);
                                            }
                                        }
                                    });
                        }
                    });
                    loadingDialog.show(getFragmentManager(), LoadingDialog.class.getName());
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
    }

    private void switchToOpenCardPage(CardCondition cardCondition, CardEntity card) {
        Utils.showLog(TAG,"准备跳转");
        if (!AppPoolTypeListFragment.inOpenCardMode) {
            AppPoolTypeListFragment.inOpenCardMode = true;
            Utils.showLog(TAG, "★跳转到开卡界面！");
            Intent intent = new Intent(getContext(), OpenCardActivity.class);
            intent.putExtra(CardConstant.CARD_CONDITION, cardCondition);
            intent.putExtra(CardConstant.CARD_INSTANCE, card);
            intent.putExtra(CardConstant.BANK_CARDS, cardEntityArrayList);
            Utils.showLog(TAG, "图标：" + card.iconUrl() + "  商户名：" + card.merName());
            activity.startActivityForResult(intent, MainActivity.REQUEST_CODE_OPEN_CARD);
        }
    }

    private void initSearchBar() {
        activity = (MainActivity)getActivity();
        if (activity == null) {
            throw new NullPointerException("activity is null");
        }
        searchHelper  = new ToolbarSearchHelper(activity);
        searchHelper.setSearchHintText(getString(R.string.edittext_hint));
        searchHelper.enableHomeAsUp(true);
        searchHelper.setNavigationListener(new ToolbarSearchHelper.OnNavigationClickListener() {
            @Override
            public void onNavigationClick() {
                activity.removeSecondFragment();
                //modify by yuanshaoyu 2017-05-12 show bottom tab
                activity.hideTabs(false);
            }
        });
        searchHelper.setOnActionSearchListener(new ToolbarSearchHelper.OnActionSearchListener() {
            @Override
            public void onSearch(String key) {

                if (TextUtils.isEmpty(key)) {
                    Toast.makeText(activity,"请输入关键字",Toast.LENGTH_LONG).show();
                    if (searchMyAppList.size()>0) {
                        searchMyAppList.clear();
                        searchResultNum.setText("0");
                        adapter.notifyDataSetChanged();
                    }
                }else {
                    searchButtonTag = 1;
                    search_app_title.setVisibility(View.VISIBLE);
                    hot_app_title.setVisibility(View.GONE);
                    if (previousFragment.equals("myApp")) {
                        appSearchPresenter.getSearchAppPool(CardType.ALL,key,1,50);
                    }else if(previousFragment.equals("AppPool")){
                        appSearchPresenter.getSearchAppPool(CardType.ALL,key,1,50);
                    }else if(previousFragment.equals("appPoolType") && getselectTag.equals("app")){
                        appSearchPresenter.getSearchAppPool(CardType.ALL,key,1,50);
                    }else if(previousFragment.equals("appPoolType") && getselectTag.equals("card")){
                        initCardListAdapter();
                        appSearchPresenter.getSearchAppPoolCard(CardType.ALL,key,1,50);
                    }
                }
                hideSoftInput(searchHelper.getSearchText());
            }
        });
    }
    /**
     * hide input keyboard
     *
     * @param searchText editText
     */
    private void hideSoftInput(EditText searchText) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
            searchHelper.CancelInput(activity);
    }

    /**
     * 用于判断上级页面
     *
     * @param selectTag  页面标识
     * @param myApp  fragment标识
     * @return
     */
    public static Fragment newInstance(String selectTag, String myApp) {
        MyAppSearchFragment instance = new MyAppSearchFragment();
        Bundle args = new Bundle();
        args.putString("previousFragment", myApp);
        args.putString("selectTag",selectTag);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void showSearchMyApp(List<AppEntity> list) {
        searchMyAppList.clear();
        if (list != null&&list.size()>0) {
            searchMyAppList.addAll(list);
            searchResultNum.setText(String.valueOf(searchMyAppList.size()));
            adapter.setmDatas(searchMyAppList);
            adapter.notifyDataSetChanged();
        }else {
            searchResultNum.setText("0");
        }
    }

    @Override
    public void showSearchAppPool(List<AppEntity> list) {
        Log.i(TAG,"list.size : " +list.size());
        searchMyAppList.clear();
        if (list != null&&list.size()>0) {
            searchMyAppList.addAll(list);
            searchResultNum.setText(String.valueOf(searchMyAppList.size()));
            adapter.setmDatas(searchMyAppList);
            adapter.notifyDataSetChanged();
        }else {
            searchResultNum.setText("0");
            hotAppList.clear();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showSearchAppPoolCard(List<CardEntity> list) {
        searchCardList.clear();
        if (list != null && list.size()>0) {
            searchCardList.addAll(list);
            searchResultNum.setText(String.valueOf(searchCardList.size()));
            cardAdapter.setmDatas(searchCardList);
            cardAdapter.notifyDataSetChanged();
        }else {
            searchResultNum.setText("0");
            hotAppList.clear();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showHotApp(List<AppEntity> list) {
        hotAppList.clear();
        if (list !=null && list.size()>0) {
            hotAppList.addAll(list);
            adapter.setmDatas(hotAppList);
            adapter.notifyDataSetChanged();
        }
    }

    public class MyInstallAppReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            String intentAction = intent.getAction();
            String packageName = intent.getDataString();//获取应用包名
            //截取包名用于比对
            String intentPackageName = packageName.substring(packageName.indexOf(":") + 1, packageName.length());
            if (intentAction.equals("android.intent.action.PACKAGE_REMOVED")) {
                //appSearchPresenter.deleteApp(intentPackageName);
//                appSearchPresenter.getHotApp();

            } else if (intentAction.equals("android.intent.action.PACKAGE_ADDED")) {
                Log.i(TAG,"add app previousFragment : "+previousFragment);
                //appSearchPresenter.addApp(context,intentPackageName);

//                appSearchPresenter.getHotApp();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myInstallAppReceiver != null) {
            getActivity().unregisterReceiver(myInstallAppReceiver);
        }
    }
}
