package com.vboss.okline.ui.card.log;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.AppEntity;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardLog;
import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.ui.card.CardBaseFragment;
import com.vboss.okline.ui.card.CardConstant;
import com.vboss.okline.ui.card.adapter.CardLogAdapter;
import com.vboss.okline.ui.card.query.CardQueryContact;
import com.vboss.okline.ui.card.query.CardSearchFragment;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.DensityUtil;
import com.vboss.okline.utils.FrescoUtil;
import com.vboss.okline.view.widget.CommonDialog;
import com.vboss.okline.view.widget.MonthDatePickerDialog;
import com.vboss.okline.view.widget.shadow.ShadowProperty;
import com.vboss.okline.view.widget.shadow.ShadowViewHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import timber.log.Timber;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/3/29 20:33 <br/>
 * Summary  : 卡片交易记录(消费清单)
 */

public class CardLogFragment extends CardBaseFragment implements CardLogContact.ICardLogView {
    public static final String TAG = CardLogFragment.class.getSimpleName();
    MainActivity activity;

    @BindView(R.id.recycleView)
    RecyclerView recyclerView;

    @BindView(R.id.iv_card_date)
    ImageView iv_card_date;

    @BindView(R.id.tv_card_name)
    TextView tv_card_name;
    @BindView(R.id.tv_card_number)
    TextView tv_card_number;
    @BindView(R.id.tv_card_balance)
    TextView tv_card_balance;
    @BindView(R.id.tv_card_integral)
    TextView tv_card_integral;

    @BindView(R.id.sdv_card)
    SimpleDraweeView simple_card;
    @BindView(R.id.sdv_card_app)
    SimpleDraweeView simple_card_logo;

    @BindView(R.id.fragment_toolbar)
    FragmentToolbar toolbar;
    @BindView(R.id.trans_ptrFrameLayout)
    PtrClassicFrameLayout classicFrameLayout;

    String queryDate = "";
    String cardNo = "";
    int cardId = 0;
    int cardType = CardType.COMMON_CARD;

    private static final int STATE_REFRESH = 0x003;
    private static final int STATE_LOAD_MORE = 0x004;
    private int state = STATE_REFRESH;
    private int page = 1;

    MonthDatePickerDialog dialog;

    AppEntity appEntity;
    CardLogPresenter presenter;
    List<CardLog> mData;
    CardLogAdapter cardLogAdapter;
    CardSearchFragment cardSearchFragment;

    /**
     * init CardLogFragment object
     *
     * @param cardEntity@return CardLogFragment instance
     */
    public static Fragment newInstance(CardEntity cardEntity) {
        Utils.showLog(TAG, "卡片信息：" + cardEntity);
        CardLogFragment cardLogFragment = new CardLogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CardConstant.KEY_CARD_TYPE, cardEntity.cardMainType());
        bundle.putInt(CardConstant.KEY_CARD_ID, cardEntity.cardId());
        bundle.putString(CardConstant.KEY_CARD_NO, cardEntity.cardNo());
        cardLogFragment.setArguments(bundle);
        return cardLogFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_card_log, container, false);
        ButterKnife.bind(this, convertView);
        return convertView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDialog(activity);
        Bundle bundle = getArguments();
        if (bundle != null) {
            cardId = bundle.getInt(CardConstant.KEY_CARD_ID);
            cardNo = bundle.getString(CardConstant.KEY_CARD_NO);
            cardType = bundle.getInt(CardConstant.KEY_CARD_TYPE, CardType.COMMON_CARD);
        }
        cardSearchFragment = CardSearchFragment.newInstance(CardQueryContact.MODE_QUERY_CARD_LOG, cardNo, cardType);
        initToolbar();
        initListener();
        initData();
    }

    /**
     * init toolbar
     */
    private void initToolbar() {
        try {
            toolbar.setActionTitle(activity.getResources().getString(R.string.card_log_title));
            toolbar.setActionMenuVisible(View.VISIBLE);
            toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
                @Override
                public void onNavigation(View v) {
                    activity.removeSecondFragment();
                }
            });
            toolbar.setOnActionMenuClickListener(new FragmentToolbar.OnActionMenuClickListener() {
                @Override
                public void onActionMenu(View v) {
                    //modify by wangshuai 2017-06-05 update card log query UI, use DialogFragment
                    if (!cardSearchFragment.isResumed()) {
                        cardSearchFragment.show(getChildFragmentManager(), cardSearchFragment.getClass().getSimpleName());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * init view click listener
     */
    private void initListener() {
        iv_card_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
        simple_card_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == appEntity) {
                    Timber.tag(TAG).e("my god appEntity is null");
                    return;
                }
                String packageName = appEntity.openUrl();
                if (!isInstalled(packageName)) {
                    showOpenApp();
                } else {
                    try {
                        Log.i(TAG, "app is installed" + packageName);
                        Intent intent = activity.getPackageManager().getLaunchIntentForPackage(packageName);
                        //modify by wangshuai 2017-07-04 start ol community intent data olNo
                        intent.putExtra("olNo", UserRepository.getInstance(activity).getOlNo());
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Timber.tag(TAG).i(" onHiddenChanged %s", hidden);
        //modify by wangshuai 2017-06-05 when cardLogFragment is not hidden refresh data
        presenter.queryCardInfo(cardType, cardId);
        refresh(queryDate);
    }

    /**
     * init data
     */
    private void initData() {
        mData = new ArrayList<>();
        cardLogAdapter = new CardLogAdapter(activity, mData);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(cardLogAdapter);

        presenter = new CardLogPresenter(activity, this);
        presenter.queryCardInfo(cardType, cardId);
        refresh(queryDate);

        // set card image shadow
        //modify by wangshuai 2017-05-02 update card image shadow
        ShadowViewHelper.bindShadowHelper(
                new ShadowProperty()
                        .setShadowColor(ActivityCompat.getColor(activity, R.color.colorShadow))
                        .setShadowDy(DensityUtil.dip2px(activity, 0.2f))
                        .setShadowRadius(DensityUtil.dip2px(activity, 3f)),
                simple_card,
                DensityUtil.dip2px(activity, 3f),
                DensityUtil.dip2px(activity, 3f));

        classicFrameLayout.setLastUpdateTimeRelateObject(this);
        classicFrameLayout.setLoadingMinTime(300);
        classicFrameLayout.setResistanceFooter(1.0f);
        classicFrameLayout.setDurationToCloseFooter(0); // footer will hide immediately when completed
        classicFrameLayout.setForceBackWhenComplete(true);
        classicFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
        classicFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout ptrFrameLayout) {
                loadMore(queryDate);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                queryDate = "";
                Timber.tag(TAG).i("OnRefreshListener refresh");
                //modify by wangshuai 2017-06-05 refresh card detail info
                presenter.queryCardInfo(cardType, cardId);
                refresh(queryDate);
            }

            @Override
            public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
                return super.checkCanDoLoadMore(frame, recyclerView, footer);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, recyclerView, header);
            }
        });
    }

    /**
     * the Method recyclerView touch up call
     * load more data
     *
     * @param date date string
     */
    private void loadMore(String date) {
        state = STATE_LOAD_MORE;
        page++;
        presenter.queryCardLog(cardType, cardNo, date, page, 15);
        Timber.tag(TAG).i("it's will be load a large data show, let's begin");
    }

    /**
     * the Method recyclerView touch down call
     * refresh data
     *
     * @param date date string
     */
    private void refresh(String date) {
        state = STATE_REFRESH;
        page = 1;
        presenter.queryCardLog(cardType, cardNo, date, page, 15);
    }

    /***
     * check app is install by packageName
     * @param packageName package name
     * @return boolean
     */
    private boolean isInstalled(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        PackageManager packageManager = activity.getPackageManager();
        try {
            PackageInfo info = packageManager.getPackageInfo(packageName, 0);
            return info != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
//        return false;
    }

    /**
     * Add by wangshuai 2017-04-11
     * app icon click show tip dialog
     */
    private void showOpenApp() {
        CommonDialog dialog = new CommonDialog(activity);
        String appName = (appEntity == null ?
                activity.getResources().getString(R.string.card_log_null_appName) :
                appEntity.appName());
        dialog.setTilte(String.format(activity.getResources().getString(R.string.card_app_open_tip),
                appName));
        dialog.setOnDialogClickListener(new CommonDialog.OnDialogInterface() {
            @Override
            public void cancel(View view, CommonDialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void ensure(View view, CommonDialog dialog) {
                dialog.dismiss();
                // open download url for web and download apk, then install app
                String url = (appEntity == null ? "" : appEntity.apkUrl());
                Timber.tag(TAG).i("apk download url %s ", url);
                if (!TextUtils.isEmpty(url)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    Timber.tag(TAG).i("start action view web open url");
                }
            }
        });
        dialog.show();
    }

    /**
     * The Method date select dialog
     */
    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();
        if (dialog == null) {
            dialog = new MonthDatePickerDialog(activity, null,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            final DatePicker datePicker = dialog.getDatePicker();
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    int year = datePicker.getYear();
                    int month = datePicker.getMonth();
                    int day = datePicker.getDayOfMonth();

                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.set(year, month, day);

                    String format = "yyyy-MM-dd";
                    SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
                    queryDate = dateFormat.format(calendar1.getTime());
                    Timber.tag(TAG).i("date %s refresh %s", queryDate, "according date refresh");
                    refresh(queryDate);
                }
            });
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void updateData(List<CardLog> data) {
        /*
        |  List mData clear, then mData addAll data
        |  adapter notifyDataSetChanged()
        |  recyclerView refresh complete
         */
        Timber.tag(TAG).i(" refresh state %s data size %s ", state, data.size());
        if (data.size() >= 15) {
            classicFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
        } else {
            classicFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
        }

        if (state == STATE_REFRESH) {
            mData.clear();
            mData.addAll(data);
            recyclerView.setAdapter(cardLogAdapter);
            classicFrameLayout.refreshComplete();
        }
        /*
        | recyclerView load more complete
        | if data == null isLoadMore change false
        | else list mData addAll data
        | adapter notifyDataSetChanged()
         */
        if (state == STATE_LOAD_MORE) {
            mData.addAll(data);
            cardLogAdapter.notifyDataSetChanged();
            classicFrameLayout.refreshComplete();
        }
        Timber.tag(TAG).i("adapter notifyDataSetChanged %s ", cardLogAdapter.getItemCount());

    }

    @Override
    public void updateCardDetail(CardEntity cardEntity) {
        try {
            if (cardEntity != null) {
                Timber.tag(TAG).i("cardEntity %s", cardEntity.toString());
                appEntity = cardEntity.getAppEntity();

                //modify by wangshuai 2017-06-08 update the type of vip and common card show balance and integral
                if (CardType.COMMON_CARD == cardEntity.cardMainType() || CardType.VIP_CARD == cardEntity.cardMainType()) {
                    tv_card_balance.setVisibility(View.VISIBLE);
                } else {
                    tv_card_balance.setVisibility(View.GONE);
                }
                if (CardType.VIP_CARD == cardEntity.cardMainType()) {
                    tv_card_integral.setVisibility(View.VISIBLE);
                } else {
                    tv_card_integral.setVisibility(View.GONE);
                }

                tv_card_name.setText(cardEntity.cardName());
                tv_card_number.setText(String.format(getResources().getString(R.string.card_info_number), cardEntity.cardNo()));
                tv_card_balance.setText(String.format(getResources().getString(R.string.card_info_balance),
                        com.vboss.okline.utils.TextUtils.formatMoney(cardEntity.balance())));
                tv_card_integral.setText(String.format(getResources().getString(R.string.card_info_integral),
                        String.valueOf(cardEntity.integral())));
                if (TextUtils.isEmpty(cardEntity.imgUrl())) {
                    simple_card.setController(FrescoUtil.getDefaultImage(activity, R.mipmap.image_card_default));
                } else {
                    simple_card.setController(FrescoUtil.loadImage(cardEntity.imgUrl(),
                            activity.getResources().getDimensionPixelSize(R.dimen.card_log_image_width) / 3,
                            activity.getResources().getDimensionPixelSize(R.dimen.card_log_image_height) / 3
                    ));
                }
            /*
            *  appEntity is null simple_card_logo gone
            *  only this app is installed  simple_card_logo visible
             */
                if (appEntity != null) {
                    String packageName = appEntity.openUrl();
                    Log.i(TAG, "appEntity info :" + appEntity.toString());
                    if (!TextUtils.isEmpty(packageName) && isInstalled(packageName)) {
                        simple_card_logo.setVisibility(View.VISIBLE);
                        if (TextUtils.isEmpty(appEntity.appIcon())) {
                            simple_card_logo.setController(FrescoUtil.getDefaultImage(activity, R.mipmap.image_card_default));
                        } else {
                            simple_card_logo.setImageURI(Uri.parse(appEntity.appIcon()));
                        }
                    } else {
                        simple_card_logo.setVisibility(View.GONE);
                    }
                } else {
                    Timber.tag(TAG).e("appEntity is null");
                    simple_card_logo.setVisibility(View.GONE);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestTimeOut(Throwable throwable, int methodFlag) {
        handlerTimeOut(throwable, methodFlag);
    }

    @Override
    public void requestFailed(Throwable throwable) {
        Timber.tag(TAG).i(" refresh state %s", state);
        //refresh data
        if (state == STATE_REFRESH) {
            mData.clear();
            recyclerView.setAdapter(cardLogAdapter);
            classicFrameLayout.refreshComplete();
            classicFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
        }
        //load more data
        if (state == STATE_LOAD_MORE) {
            cardLogAdapter.notifyDataSetChanged();
            classicFrameLayout.refreshComplete();
            classicFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
        }
    }

    @Override
    protected void onRetry(int method) {
        presenter.queryCardInfo(cardType, cardId);
    }
}
