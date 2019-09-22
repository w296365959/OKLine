package com.vboss.okline.ui.app.apppool.apppooltype;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.data.CardRepository;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.AppEntity;
import com.vboss.okline.data.entities.CardCondition;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.ui.app.AppHelper;
import com.vboss.okline.ui.app.MyInstalledReceiver;
import com.vboss.okline.ui.app.adapter.CommonAdapter;
import com.vboss.okline.ui.app.adapter.OnItemClickListener;
import com.vboss.okline.ui.app.adapter.ViewHolder;
import com.vboss.okline.ui.app.search.AppSearchFragment;
import com.vboss.okline.ui.card.CardConstant;
import com.vboss.okline.ui.card.widget.LogoView;
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
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: Yuan shaoyu <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/3/28 9:51 <br/>
 * Summary  : App Pool类型列表界面
 */

public class AppPoolTypeListFragment extends BaseFragment implements AppPoolTypeContract.View {
    private static final String TAG = "AppPoolTypeListFragment";
    //modified by yuanshaoyu 2017-5-26: solved app and card change problem
    private static final int APP_STATE_REFRESH = 0x003;
    private static final int APP_STATE_LOAD_MORE = 0x004;
    private static final int CARD_STATE_REFRESH = 0x005;
    private static final int CARD_STATE_LOAD_MORE = 0x006;
    private int state = APP_STATE_REFRESH;
    private int page = 1;
    private boolean intentToOpenCard;

    @BindView(R.id.apptype_list_recyclerview)
    RecyclerView apptTypeListRV;

    @BindView(R.id.app_pool_button)
    RadioButton appPoolButton;

    @BindView(R.id.card_button)
    RadioButton cardButton;

    @BindView(R.id.layout)
    TextView layout;

    @BindView(R.id.apptype_no_app_txt)
    TextView apptype_no_app_txt;

    @BindView(R.id.apptype_no_app_img)
    ImageView apptype_no_app_img;

    @BindView(R.id.apptype_no_app)
    LinearLayout apptype_no_app;

    MainActivity activity;
    @BindView(R.id.fragment_toolbar)
    FragmentToolbar toolbar;

    @BindView(R.id.ptrFrameLayout)
    PtrClassicFrameLayout ptrFrameLayout;

    CommonAdapter<AppEntity> appAdapter;
    CommonAdapter<CardEntity> cardAdapter;
    List<AppEntity> appList;
    List<CardEntity> cardList;
    AppPoolTypePresenter presenter;
    //标题栏名称
    String type_title;
    int appType;
    //每页显示数目
    int appPageSize = 20;

    private String selectTag;//用于判断点击我的APP还是所有APP
    private ArrayList<CardEntity> cardEntityArrayList;
    private boolean isPreparingCardOpen = false;
    public static boolean inOpenCardMode;
    private CardCondition cardCondition;
    AppSearchFragment appSearchFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_pool_type_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //获取标题栏标题
        Bundle bundle = getArguments();
        type_title = bundle.getString("title");
        initToolBar();
        initAppPoolAdapter();
        presenter = new AppPoolTypePresenter(activity, this);
        //refresh();

        if (intentToOpenCard) {
            cardButton.setChecked(true);
            ViewOnClick(cardButton);
        } else {
            appPoolButton.setChecked(true);
            ViewOnClick(appPoolButton);
        }

        appSearchFragment = new AppSearchFragment();
        //add by yuanshoayu 2017-5-22 : refresh or load more data
        ptrFrameLayout.setLastUpdateTimeRelateObject(this);
        ptrFrameLayout.setLoadingMinTime(500);
        ptrFrameLayout.setResistanceFooter(1.0f);
        ptrFrameLayout.setDurationToCloseFooter(0); // footer will hide immediately when completed
        ptrFrameLayout.setForceBackWhenComplete(true);
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout ptrFrameLayout) {
                loadMore();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                refresh();
            }

            @Override
            public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
                return super.checkCanDoLoadMore(frame, apptTypeListRV, footer);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, apptTypeListRV, header);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //add by yuanshaoyu 2017-5-13 to refresh cardList
        if (selectTag.equals("card")) {
            cardButton.setChecked(true);
            ViewOnClick(cardButton);
        }
        refresh();
    }

    /**
     * 获取app pool 分类列表数据
     *
     * @param type_title 类型名称
     */
    public void getTypeList(String type_title) {
        if (type_title.equals(getResources().getString(R.string.card_tab_common))) {
            appType = CardType.COMMON_CARD;
        } else if (type_title.equals(getResources().getString(R.string.card_tab_member))) {
            appType = CardType.VIP_CARD;
        } else if (type_title.equals(getResources().getString(R.string.card_tab_bank))) {
            appType = CardType.BANK_CARD;
        } else if (type_title.equals(getResources().getString(R.string.card_tab_work))) {
            appType = CardType.EMPLOYEE_CARD;
        } else if (type_title.equals(getResources().getString(R.string.card_tab_certificates))) {
            appType = CardType.CREDENTIALS;
        } else if (type_title.equals(getResources().getString(R.string.card_tab_travel))) {
            //modified by yuanshaoyu 2017-5-26 :把交通类改为票务类
            appType = CardType.TICKET;
        } else if (type_title.equals(getResources().getString(R.string.card_tab_access))) {
            appType = CardType.DOOR_CARD;
        }
    }


    @OnClick({R.id.app_pool_button, R.id.card_button})
    public void ViewOnClick(View view) {
        switch (view.getId()) {
            case R.id.app_pool_button:
                selectTag = "app";
                initAppPoolAdapter();
                refresh();
                //presenter.getAppPoolTypeList(appType, null, 1, appPageSize);
                break;
            case R.id.card_button:
                selectTag = "card";
                initCardListAdapter();
                refresh();
                break;
            default:
                break;
        }
    }

    private void initCardListAdapter() {
        cardList = new ArrayList<>();
        layout.setVisibility(View.GONE);
        cardAdapter = new CommonAdapter<CardEntity>(getActivity(), R.layout.cardlist_item_layout, cardList) {
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
        //<editor-fold desc="郑军  编辑日期：2017年04月13日14:25:22">
        cardAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, final int position) {
                final CardEntity card = cardList.get(position);
                if (card.isOpen()!=0) {
                    Utils.customToast(getContext(), "不能重复开卡", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isPreparingCardOpen) {
                    isPreparingCardOpen = true;
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
                                            AppPoolTypeListFragment.this.cardCondition = cardCondition;
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
        //</editor-fold>
        apptTypeListRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        apptTypeListRV.setAdapter(cardAdapter);
    }

    public void switchToOpenCardPage(CardCondition cardCondition, CardEntity card) {
        if (!inOpenCardMode) {
            inOpenCardMode = true;
            Intent intent = new Intent(getContext(), OpenCardActivity.class);
            intent.putExtra(CardConstant.CARD_CONDITION, cardCondition);
            intent.putExtra(CardConstant.CARD_INSTANCE, card);
            intent.putExtra(CardConstant.BANK_CARDS, cardEntityArrayList);
            activity.startActivityForResult(intent, MainActivity.REQUEST_CODE_OPEN_CARD);
        }
    }

    private void initAppPoolAdapter() {
        appList = new ArrayList<>();
        layout.setVisibility(View.VISIBLE);
        appAdapter = new CommonAdapter<AppEntity>(getActivity(), R.layout.app_item_layout, appList) {
            @Override
            public void convert(ViewHolder holder, final AppEntity appEntity, int position) {
                holder.setText(R.id.app_name, appEntity.appName());
                if (!TextUtils.isEmpty(appEntity.appIcon())) {
                    holder.setImageByUrl(R.id.app_icon, appEntity.appIcon());
                }

                final String componentName = appEntity.componentName();
                final String apkUrl = appEntity.apkUrl();
                //modified by yuanshaoyu 2017-6-13: 卡助理写死为已安装
                if (appEntity.appName().equals("卡助理")) {
                    holder.setVisible(R.id.app_installed_icon, true);
                }else {
                    //modified by yuanshaoyu 2017-5-24: 增加isDownload判断 是否从app pool中下载安装的app
                    if (!TextUtils.isEmpty(componentName) && appEntity.isDownload() == 1) {
                        holder.setVisible(R.id.app_installed_icon, true);
                        //localVersionCode< versionCode 则表示需要更新
                        if (appEntity.localVersionCode() < appEntity.versionCode()) {
                            holder.setVisible(R.id.app_pendingupdate_icon, true);
                        } else {
                            holder.setVisible(R.id.app_pendingupdate_icon, false);
                        }
                    } else {
                        holder.setVisible(R.id.app_installed_icon, false);
                    }
                }


                //modified by yuanshaoyu 2017-5-24: 增加isDownload判断 是否从app pool中下载安装的app
                holder.setOnClickListener(R.id.app_icon, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{
                            //modified by yuanshaoyu 2017-6-13: 卡助理写死为已安装
                            UserRepository userRepository = UserRepository.getInstance(getActivity());
                            User user = userRepository.getUser();
                            if (appEntity.appName().equals("卡助理")) {

                                if (user == null) {
                                    Timber.tag(TAG).i("user is null");
                                    return;

                                }
                                AppHelper.startAssistant(getActivity(),user);
                            }else {
                                if (componentName != null && appEntity.isDownload() == 1) {
                                    Intent intent = activity.getPackageManager().getLaunchIntentForPackage(appEntity.openUrl());
                                    //modified by yuanshaoyu 2017-6-29 :增加欧乐社区穿欧乐号
                                    intent.putExtra("olNo",user.getOlNo());
                                    activity.startActivity(intent);
                                }else {
                                    Uri uri = Uri.parse(apkUrl);
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    getActivity().startActivity(intent);
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                });
            }
        };
        apptTypeListRV.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        apptTypeListRV.setAdapter(appAdapter);
    }
    /**
     * the Method recyclerView touch up call
     * load more data
     */
    private void loadMore() {

        page++;
        if (selectTag .equals("app")) {
            state = APP_STATE_LOAD_MORE;
            getTypeList(type_title);
            //modified by yuanshaoyu 2017-6-1:解决添加银行卡跳转到银行卡列表界面时获取不到类型的错误
            presenter.getAppPoolTypeList(appType, null, page, appPageSize);
        }
        if (selectTag.equals("card")) {
            state = CARD_STATE_LOAD_MORE;
            //modified by yuanshaoyu 2017-6-1:解决添加银行卡跳转到银行卡列表界面时获取不到类型的错误
            getTypeList(type_title);
            presenter.getAppPoolCardList(appType, null, page, appPageSize);
        }
    }

    /**
     * the Method recyclerView touch down call
     * refresh data
     */
    private void refresh() {
        page = 1;
        if (selectTag .equals("app")) {
            state = APP_STATE_REFRESH;
            getTypeList(type_title);
            presenter.getAppPoolTypeList(appType, null, page, appPageSize);
        }
        if (selectTag.equals("card")) {
            state = CARD_STATE_REFRESH;
            getTypeList(type_title);
            presenter.getAppPoolCardList(appType, null, page, appPageSize);
        }
    }

    private void initToolBar() {
        activity = (MainActivity) getActivity();
        if (activity == null) {
            throw new NullPointerException("activity is null");
        }
        toolbar.setActionTitle(type_title);
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
                //activity.addSecondFragment(MyAppSearchFragment.newInstance(selectTag, "appPoolType"));
                //modified by yuanshaoyu 2017-6-7:解决快速点击两次搜索按钮的重叠问题
                if (!appSearchFragment.isResumed()) {
                    //add by yuanshaoyu 2017-6-7:修复获取不到selectTag的闪退问题
                    appSearchFragment.setTag("appPoolType",selectTag);
                    appSearchFragment.show(getChildFragmentManager(), appSearchFragment.getClass().getSimpleName());
                }
            }
        });

    }

    @Override
    public void showAppPoolTypeList(List<AppEntity> list) {
        //appList.clear();
        if (list != null && list.size() > 0) {
            apptype_no_app.setVisibility(View.GONE);
            apptTypeListRV.setVisibility(View.VISIBLE);
            //appList.addAll(list);
            //appAdapter.notifyDataSetChanged();
        }else {
            if (state != APP_STATE_LOAD_MORE){
                apptype_no_app.setVisibility(View.VISIBLE);
                apptype_no_app_txt.setText("暂无APP");
                apptype_no_app_img.setImageResource(R.mipmap.no_app);
                apptTypeListRV.setVisibility(View.GONE);
            }
        }
        //add by yuanshoayu 2017-5-22 : refresh or load more data
        if (list.size() < appPageSize) {
            ptrFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
        } else {
            ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
        }
        if (state == APP_STATE_REFRESH) {
            appList.clear();
            appList.addAll(list);
            Timber.tag(TAG).i("APP type list 已安装列表 :%s",appList.toString());
            apptTypeListRV.setLayoutManager(new GridLayoutManager(getActivity(), 4));
            apptTypeListRV.setAdapter(appAdapter);
            ptrFrameLayout.refreshComplete();
            ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
        }
        if (state == APP_STATE_LOAD_MORE) {
            appList.addAll(list);
            appAdapter.notifyDataSetChanged();
            ptrFrameLayout.refreshComplete();
            ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
        }
    }

    @Override
    public void showAppPoolCardList(List<CardEntity> list) {
        //cardList.clear();
        if (list != null && list.size() > 0) {
            apptype_no_app.setVisibility(View.GONE);
            apptTypeListRV.setVisibility(View.VISIBLE);
            //cardList.addAll(list);
            //Log.i(TAG,"refresh openCard list");
            //cardAdapter.notifyDataSetChanged();
        }else {
            if (state != CARD_STATE_LOAD_MORE){
                apptype_no_app.setVisibility(View.VISIBLE);
                apptype_no_app_txt.setText("无可开卡片");
                apptype_no_app_img.setImageResource(R.mipmap.no_open_card);
                apptTypeListRV.setVisibility(View.GONE);
            }
        }
        //add by yuanshoayu 2017-5-22 : refresh or load more data
        if (list.size() < appPageSize) {
            ptrFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
        } else {
            ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
        }
        if (state == CARD_STATE_REFRESH) {
            cardList.clear();
            cardList.addAll(list);
            apptTypeListRV.setLayoutManager(new LinearLayoutManager(getActivity()));
            apptTypeListRV.setAdapter(cardAdapter);
            ptrFrameLayout.refreshComplete();
            ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
        }
        if (state == CARD_STATE_LOAD_MORE) {
            cardList.addAll(list);
            cardAdapter.notifyDataSetChanged();
            ptrFrameLayout.refreshComplete();
            ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
        }
    }

    @Override
    public void requestAppFailed() {
        //refresh data
        if (selectTag.equals("app")) {
            if (state == APP_STATE_REFRESH) {
                cardList.clear();
                apptTypeListRV.setAdapter(cardAdapter);
                ptrFrameLayout.refreshComplete();
                ptrFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
            }
            //load more data
            if (state == APP_STATE_LOAD_MORE) {
                cardAdapter.notifyDataSetChanged();
                ptrFrameLayout.refreshComplete();
                ptrFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
            }
        }

    }

    @Override
    public void requestCardFailed() {

        //refresh data
        if (selectTag.equals("card")) {
            if (state == CARD_STATE_REFRESH) {
                appList.clear();
                apptTypeListRV.setAdapter(appAdapter);
                ptrFrameLayout.refreshComplete();
                ptrFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
            }
            //load more data
            if (state == CARD_STATE_LOAD_MORE) {
                appAdapter.notifyDataSetChanged();
                ptrFrameLayout.refreshComplete();
                ptrFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
            }
        }
    }

    /**
     * 用于传输数据
     *
     * @param appName 分类类型
     * @param intentToOpenCard
     * @return
     */
    public static Fragment newInstance(String appName, boolean intentToOpenCard) {
        AppPoolTypeListFragment instance = new AppPoolTypeListFragment();
        Bundle args = new Bundle();
        args.putString("title", appName);
        instance.setArguments(args);
        instance.intentToOpenCard = intentToOpenCard;
        return instance;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //add by yuanshaoyu 2017-6-19 :enent refresh
    @Subscribe(tags = {@Tag(AppHelper.EVENT_APP_REFRESH)})
    public void onRefresh(Boolean b){
        Timber.tag(TAG).i("appPool type list refresh data %s",b);
        refresh();
    }
}
