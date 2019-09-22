package com.vboss.okline.ui.app.search;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.AppEntity;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.ui.app.AppHelper;
import com.vboss.okline.ui.app.adapter.CommonAdapter;
import com.vboss.okline.ui.app.adapter.OnItemClickListener;
import com.vboss.okline.ui.app.adapter.ViewHolder;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.scanner.QRCodeActivity;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.DensityUtil;
import com.vboss.okline.utils.StringUtils;
import com.vboss.okline.utils.TextUtils;
import com.vboss.okline.view.widget.ClearEditText;
import com.vboss.okline.view.widget.OKCardView;
import com.vboss.okline.view.widget.shadow.ListViewShadowViewHelper;
import com.vboss.okline.view.widget.shadow.ShadowProperty;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static android.R.attr.gravity;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: yuanshaoyu <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/6/3 10:09 <br/>
 * Summary  : 在这里描述Class的主要功能
 */

public class AppSearchFragment extends DialogFragment implements AppSearchContract.View ,View.OnClickListener,TextView.OnEditorActionListener{
    private static final String TAG = AppSearchFragment.class.getSimpleName();
    private Context mContext;
    private InputMethodManager imm;

    @BindView(R.id.edt_card)
    ClearEditText edt_card;
    @BindView(R.id.empty)
    View empty;
    @BindView(R.id.search_app_RecyclerView)
    RecyclerView search_app_RecyclerView;
    @BindView(R.id.fl_data)
    FrameLayout frameLayout;

    @BindView(R.id.no_result)
    RelativeLayout no_result;

    @BindView(R.id.search_content)
    TextView search_content;

    @BindView(R.id.tv_cancel)
    TextView tv_cancel;

    @BindView(R.id.action_back_layout)
    RelativeLayout action_back_layout;

    @BindView(R.id.action_back)
    ImageButton action_back;

    @BindView(R.id.iv)
    ImageView logo;

    //modify by yuanshaoyu 2017-06-21
//    @BindView(R.id.logoView)
//    LogoView logoView;
//    //Added by yuanshaoyu 2017-06-21
//    @BindView(R.id.ocardView)
//    OKCardView okCardView;

    /*@BindView(R.id.ib_more)
    ImageButton ib_more;*/

    //private CardAdapter adapter;
    CommonAdapter<AppEntity> adapter;
    List<AppEntity> searchMyAppList = new ArrayList<>();
    private AppSearchPresenter presenter;
    IntentFilter intentFilter;
    String searchKey;
    String previousFragment;
    String getselectTag;
    List<CardEntity> searchCardList = new ArrayList<>();
    CommonAdapter<CardEntity> cardAdapter;
    MainActivity activity;
    private PopupWindow morePopupWindow;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
        presenter = new AppSearchPresenter(context, this);
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addDataScheme("package");

        View convertView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_app_search, null);
        ButterKnife.bind(this, convertView);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialog);
        builder.setView(convertView);
        initView();
        changeHint();
        initAdapter();
        return builder.create();
    }
    //改变editText的hint
    private void changeHint() {
        if(previousFragment.equals("appPoolType") && getselectTag.equals("card")){
            edt_card.setHint("输入卡名称的关键字");
        }else {
            edt_card.setHint("输入应用名称的关键字");
        }
    }

    private void initAdapter() {
//        searchMyAppList = new ArrayList<>();
        adapter = new CommonAdapter<AppEntity>(getActivity(),R.layout.app_item_layout,searchMyAppList) {
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
                        //modified by yuanshaoyu 2017-6-29 :增加卡助理传参
                        try{
                            //modified by yuanshaoyu 2017-6-13: 卡助理写死为已安装
                            UserRepository userRepository = UserRepository.getInstance(getActivity());
                            User user = userRepository.getUser();
                            if (app.appName().equals("卡助理")) {

                                if (user == null) {
                                    Timber.tag(TAG).i("user is null");
                                    return;

                                }
                                AppHelper.startAssistant(getActivity(),user);
                            }else {
                                if (componentName != null && app.isDownload() == 1) {
                                    Intent intent = activity.getPackageManager().getLaunchIntentForPackage(app.openUrl());
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

//                        if (componentName != null && app.isDownload() == 1) {
//                            Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage(app.openUrl());
//                            getActivity().startActivity(intent);
//                        }else {
//                            if (!TextUtils.isEmpty(apkUrl)) {
//                                // add by yuanshaoyu 2017-5-24 :判断自己应用下载的app
//                                presenter.poolAppDownload(app.openUrl());
//                                Uri uri = Uri.parse(apkUrl);
//                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                                getActivity().startActivity(intent);
//                            }
//
//                        }
                    }
                });
            }
        };
        search_app_RecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),4));
        search_app_RecyclerView.setAdapter(adapter);
    }

    private void initView() {
//        //Added by yuanshaoyu 2017-06-08 registerReceiver broadcast
//        IntentFilter filter = new IntentFilter(BaseActivity.ACTION_OCARD_STATE_CHANGED);
//        getActivity().registerReceiver(receiver, filter);
//        switchLogo();
        edt_card.setOnEditorActionListener(this);
        if (previousFragment.equals("myApp")) {
            action_back.setImageResource(R.mipmap.ic_contacts_add);
            textChangedListener();
        }else if(previousFragment.equals("AppPool")){
            action_back.setImageResource(R.mipmap.ic_toolbar_back);
            textChangedListener();
        }else if(previousFragment.equals("appPoolType") && getselectTag.equals("app")){
            action_back.setImageResource(R.mipmap.ic_toolbar_back);
            textChangedListener();
        }else if(previousFragment.equals("appPoolType") && getselectTag.equals("card")){
            action_back.setImageResource(R.mipmap.ic_toolbar_back);
        }

        //add by yuanshaoyu 2017-6-6 :修改不间断快速\点击“取消”时键盘拉起的异常
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideSoftInput();
                        dismiss();
                    }
                });
            }
        },200);

    }

    private void textChangedListener() {
        edt_card.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    searchKey = s.toString();
                    empty.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    presenter.getSearchAppPool(CardType.ALL,s.toString(),1,50);
//                     else if(previousFragment.equals("appPoolType") && getselectTag.equals("card")){
//                        initCardListAdapter();
//                        presenter.getSearchAppPoolCard(CardType.ALL,s.toString(),1,50);
//                    }
                } else {
                    searchMyAppList.clear();
                    empty.setVisibility(View.VISIBLE);
                    frameLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.tag(TAG).i(" onResume ");

        if (!TextUtils.isEmpty(searchKey)) {
            presenter.getSearchAppPool(CardType.ALL,searchKey,1,50);
        }

        Window mWindow = getDialog().getWindow();
        if (mWindow != null) {
            Timber.tag(TAG).i(" onResume ");
            WindowManager.LayoutParams mLayoutParams = mWindow.getAttributes();
            mLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            mLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mLayoutParams.gravity = gravity;
            //modify by yuanshaoyu 2017-06-06 solve show soft input
            mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
        //modify by yuanshaoyu 2017-06-06 solve show soft input
        edt_card.setFocusable(true);
        edt_card.setFocusableInTouchMode(true);
        edt_card.requestFocus();
        //modify by yuanshaoyu 2017-06-06 solve show soft input
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        },50);


    }

    @OnClick({R.id.empty,R.id.action_back_layout,R.id.ib_more})
    public void onActionBarClick(View v) {
        switch (v.getId()) {
            case R.id.empty:
                hideSoftInput();
                dismiss();
                break;

            case R.id.action_back_layout:
                if (previousFragment.equals("myApp")) {
                    View convertView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_card_search_pop, null);
                    morePopupWindow = new PopupWindow(convertView, DensityUtil.dip2px(getActivity(), 120),
                            DensityUtil.dip2px(getActivity(), 100));
                    morePopupWindow.setBackgroundDrawable(new ColorDrawable());
                    morePopupWindow.setOutsideTouchable(true);
                    convertView.findViewById(R.id.tv_pop_receivables).setOnClickListener(this);
                    convertView.findViewById(R.id.tv_pop_scanner).setOnClickListener(this);
                    morePopupWindow.showAsDropDown(v, DensityUtil.dip2px(getActivity(), 5), 0);
                }else {
                    hideSoftInput();
                    dismiss();
                }

                break;
            case R.id.ib_more:

            default:
                break;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pop_scanner:
                dismiss();
                activity.startActivity(new Intent(activity, QRCodeActivity.class));

                break;
            case R.id.tv_pop_receivables:

                break;
        }
        morePopupWindow.dismiss();
    }
    private void hideSoftInput() {
        imm.hideSoftInputFromWindow(edt_card.getWindowToken(), 0);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        hideSoftInput();
    }

    @Override
    public void showSearchMyApp(List<AppEntity> list) {

    }

    @Override
    public void showSearchAppPool(List<AppEntity> list) {
        Timber.tag(TAG).i("query app data %s", list.size());
        if (list != null && list.size() > 0) {
            no_result.setVisibility(View.GONE);
            searchMyAppList.clear();
            searchMyAppList.addAll(list);
            adapter.setmDatas(searchMyAppList);
            adapter.notifyDataSetChanged();
        }else {
            searchMyAppList.clear();
            adapter.notifyDataSetChanged();
            no_result.setVisibility(View.VISIBLE);
            search_content.setText(searchKey);
        }

    }

    @Override
    public void showSearchAppPoolCard(List<CardEntity> list) {
        if (list != null && list.size() > 0) {
            no_result.setVisibility(View.GONE);
            searchCardList.clear();
            searchCardList.addAll(list);
            cardAdapter.setmDatas(searchCardList);
            cardAdapter.notifyDataSetChanged();
        }else {
            searchCardList.clear();
            adapter.notifyDataSetChanged();
            no_result.setVisibility(View.VISIBLE);
            search_content.setText(searchKey);
        }
        Timber.tag(TAG).i("query card data %s", list.size());

    }

    @Override
    public void showHotApp(List<AppEntity> list) {

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
                int r2 = StringUtils.dip2px(getActivity(), 7.5f);
                ListViewShadowViewHelper.bindShadowHelper(
                        new ShadowProperty()
                                .setShadowColor(ActivityCompat.getColor(mContext, R.color.colorShadow))
                                .setShadowDy(StringUtils.dip2px(getActivity(), 0.25f))
                                .setShadowRadius(StringUtils.dip2px(getActivity(), 2f))
                        , card_img, r2, r2);
            }
        };
        search_app_RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        search_app_RecyclerView.setAdapter(cardAdapter);
    }
    /**
     * 用于判断上级页面
     *
     * @param selectTag  页面标识
     * @param myApp  fragment标识
     * @return
     */
    public static AppSearchFragment newInstance(String selectTag, String myApp) {
        AppSearchFragment instance = new AppSearchFragment();
        Bundle args = new Bundle();
        args.putString("previousFragment", myApp);
        args.putString("selectTag",selectTag);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        Timber.tag(TAG).i("actionId %s", actionId);
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            hideSoftInput();
            String key = edt_card.getText().toString().trim();
            Timber.tag(TAG).i("key %s", key);
            if (!TextUtils.isEmpty(key)) {
                searchKey = key;
                query(key);
            }else {
                Toast.makeText(activity,"请输入关键字",Toast.LENGTH_LONG).show();
            }
        }
        return false;
    }
    /**
     * card or card's log query
     * key is query keyword
     *
     * @param key search keyword
     */
    private void query(String key) {
        if (TextUtils.isEmpty(key)) {
            Utils.customToast(activity, activity.getResources().getString(R.string.card_query_key_empty),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (previousFragment.equals("myApp")) {
            presenter.getSearchAppPool(CardType.ALL,key,1,50);
            frameLayout.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
        }else if(previousFragment.equals("AppPool")){
            presenter.getSearchAppPool(CardType.ALL,key,1,50);
            frameLayout.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
        }else if(previousFragment.equals("appPoolType") && getselectTag.equals("app")){
            presenter.getSearchAppPool(CardType.ALL,key,1,50);
            frameLayout.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
        }else if(previousFragment.equals("appPoolType") && getselectTag.equals("card")){
            initCardListAdapter();
            frameLayout.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
            presenter.getSearchAppPoolCard(CardType.ALL,key,1,50);
        }

    }

    //add by yuanshaoyu 2017-6-7:修复获取不到selectTag的闪退问题
    public void setTag(String previousFrag,String selectTag) {
        previousFragment = previousFrag;
        getselectTag = selectTag;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Added by yuanshaoyu 2017-06-08 unregisterReceiver broadcast
//        try {
//            getActivity().unregisterReceiver(receiver);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
//    BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            //Added by yuanshaoyu 2017-06-09 according to OCard state update logo
//            switchLogo();
//        }
//    };
//
//    private void switchLogo() {
//        okCardView.setBatteryVolume(BaseActivity.getOcardBattery(), false);
//        switch (BaseActivity.getOcardState()) {
//            case BaseActivity.OCARD_STATE_BOND:
//                //modify by yuanshaoyu 2017-06-21
//                logoView.oCardContacted();
//                logoView.setOCardState(LogoView.OCARD_BIND);
//                okCardView.setVisibility(View.VISIBLE);
//                break;
//            case BaseActivity.OCARD_STATE_NOT_BOND:
//                //modify by yuanshaoyu 2017-06-21
//                logoView.setOCardState(LogoView.OCARD_NO_BIND);
//                okCardView.setVisibility(View.GONE);
//                break;
//            case BaseActivity.OCARD_STATE_IPSS_INVALID:
//
//                break;
//            case BaseActivity.OCARD_STATE_NOT_CONNECTED:
//                okCardView.setVisibility(View.GONE);
//                logoView.setOCardState(LogoView.OCARD_BIND);
//                logoView.oCardOutContacted();
//                break;
//        }
//    }
}
