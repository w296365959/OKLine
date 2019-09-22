package com.vboss.okline.ui.record.search;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.data.RecordRepository;
import com.vboss.okline.data.entities.OrgzCard;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.record.organization.OrganizationDetailFragment;
import com.vboss.okline.ui.user.UserFragment;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.TextUtils;
import com.vboss.okline.view.widget.ClearEditText;
import com.vboss.okline.view.widget.OKCardView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/6/5
 * Summary : 搜索功能
 */

public class RecordSearchFragment extends DialogFragment {

    public static final int TYPE_ORGANIZATION = 0;
    public static final int TYPE_CARD = 1;
    public static final int TYPE_CONVERSATION = 2;
    @BindView(R.id.action_back)
    ImageButton actionBack;
    @BindView(R.id.action_back_layout)
    RelativeLayout actionBackLayout;
    @BindView(R.id.iv_okline_logo)
    LogoView ivOklineLogo;
    @BindView(R.id.okcard_view)
    OKCardView okcardView;
    @BindView(R.id.tv_search_content)
    TextView tvSearchContent;
    private int type = TYPE_ORGANIZATION;
    private static final String TAG = "RecordSearchFragment";
    private int index = 1;
    private static final int PAGE_SIZE = 15;
    private RecordSearchAdapter adapter;
    private String keyWord;
    //根据是否要加载更多来判断显示的数据,默认第一页开始
    private boolean loadMore = false;

    @BindView(R.id.searchContentView)
    ClearEditText searchContentView;
    @BindView(R.id.searchBtn)
    TextView searchBtn;
    @BindView(R.id.countTextView)
    TextView countTextView;
    @BindView(R.id.itemCountLL)
    LinearLayout itemCountLL;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptrFrameLayout)
    PtrClassicFrameLayout ptrFrameLayout;
    @BindView(R.id.empty)
    View empty;
    private MainActivity activity;
    private int cardMainType;
    private String cardNo;
    private View.OnClickListener onClickListener;
    private View convertView;

    private void onOcardStateChanged() {
        int ocardState = BaseActivity.getOcardState();
        switch (ocardState) {
            case BaseActivity.OCARD_STATE_BOND:
                Utils.showLog(TAG, "欧卡已连接");
                ivOklineLogo.setOCardState(LogoView.OCARD_BIND);
                ivOklineLogo.oCardContacted();
                break;
            case BaseActivity.OCARD_STATE_IPSS_INVALID:
                Utils.showLog(TAG, "安全电话不可用");
                break;
            case BaseActivity.OCARD_STATE_NOT_BOND:
                Utils.showLog(TAG, "欧卡未绑定");
                ivOklineLogo.setOCardState(LogoView.OCARD_NO_BIND);
                break;
            case BaseActivity.OCARD_STATE_NOT_CONNECTED:
                Utils.showLog(TAG, "欧卡未连接");
                ivOklineLogo.setOCardState(LogoView.OCARD_BIND);
                ivOklineLogo.oCardOutContacted();
                break;
        }
    }

    public static RecordSearchFragment newInstance(int type, int cardMainType, String cardNo) {
        RecordSearchFragment recordSearchFragment = new RecordSearchFragment();
        recordSearchFragment.type = type;
        recordSearchFragment.cardMainType = cardMainType;
        recordSearchFragment.cardNo = cardNo;
        return recordSearchFragment;
    }

    @Override
    public void onAttach(Context context) {
        System.out.println("RecordSearchFragment.onAttach");
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("RecordSearchFragment.onStart");
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        searchContentView.setFocusable(true);
        searchContentView.setFocusableInTouchMode(true);
        searchContentView.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        onClickListener.onClick(searchContentView);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        System.out.println("RecordSearchFragment.onCreateDialog");
        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_search, null);
        ButterKnife.bind(this, convertView);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialog);
        builder.setView(convertView);
        initView();
        if (type == RecordSearchActivity.TYPE_CARD) {
            actionBack.setImageResource(R.drawable.ic_toolbar_back);
            searchContentView.setHint("输入交易商户名的关键字");
        }
        onOcardStateChanged();
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
        searchBtn.setText("取消");
        searchContentView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString();
                if (TextUtils.isEmpty(s1)) {
                    ptrFrameLayout.setVisibility(View.GONE);
                    itemCountLL.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                }
            }
        });
        searchContentView.setOnClickListener(onClickListener);
        searchContentView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //软键盘点击搜索
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    loadMore = false;
                    itemCountLL.setVisibility(View.GONE);
                    search();
                    return true;
                }
                return false;
            }
        });
        return builder.create();
    }

    private void initView() {
        initPtrFrameLayout();
        initRecyclerView();
    }

    private void initRecyclerView() {
        adapter = new RecordSearchAdapter(getContext());
        //条目点击监听
        adapter.setListener(new RecordSearchAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position, OrgzCard card) {
                                    Utils.showLog(TAG, "context instanceof MainActivity: TRUE");
                                    OrganizationDetailFragment fragment = new OrganizationDetailFragment();
                                    activity.addSecondFragment(fragment);
                                    fragment.setOrgzCard(card);
                                    //移除RecordSearchFragment
                                    dismiss();
                                }
                            }
        );
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    /**
     * 搜索功能
     */
    private void search() {
        if (!loadMore) {
            keyWord = searchContentView.getText().toString();
            index = 1;
        }
        switch (type) {
            case TYPE_ORGANIZATION://获取或搜索机构卡列表
                Utils.showLog(TAG, "搜素类型:TYPE_ORGANIZATION");
                organization();
                break;
            case TYPE_CARD://获取某机构下(即某张卡)交易记录
                Utils.showLog(TAG, "搜素类型:TYPE_CARD");
                //显示该卡该搜索条件下的有关记录信息
                cardLog();
                break;
            case TYPE_CONVERSATION:
                break;
        }
    }
    /**
     * 获取某机构下(即某张卡)交易记录,
     * 根据搜索关键字,显示有关记录信息
     */
    private void cardLog() {
//        int cardMainType = getIntent().getIntExtra("cardMainType", 0);
//        String cardNo = getIntent().getStringExtra("cardNo");
        RecordRepository.getInstance(getContext())
                .orgzCardLogList(cardMainType, cardNo, keyWord, index, PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SearchSubscriber());
    }
    /**
     * 获取或搜索机构卡列表
     */
    private void organization() {
        RecordRepository.getInstance(getContext())
                .orgzCardList(keyWord, index, PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SearchSubscriber());
    }
    /**
     * 下拉刷新
     */
    private void initPtrFrameLayout() {
        ptrFrameLayout.setLastUpdateTimeRelateObject(this);
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler2() {

            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                loadMore = true;
                search();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
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
        ptrFrameLayout.setLoadingMinTime(300);
        ptrFrameLayout.setResistanceFooter(1.0f);
        ptrFrameLayout.setDurationToCloseFooter(0); // footer will hide immediately when completed
        ptrFrameLayout.setForceBackWhenComplete(true);
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.NONE);
    }

    class SearchSubscriber extends Subscriber {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            ptrFrameLayout.refreshComplete();
            Utils.customToast(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(Object o) {
            //填充的灰色View隐藏
            empty.setVisibility(View.GONE);
            //显示recyclerView的布局
            ptrFrameLayout.setVisibility(View.VISIBLE);
            ptrFrameLayout.refreshComplete();
            if (o instanceof List) {
                List list = (List) o;
                if (loadMore) {
                    adapter.addDatas(list);
                } else {
                    adapter.refresh(list);
                }
                if (list.size() >= PAGE_SIZE) {
                    ptrFrameLayout.setMode(PtrFrameLayout.Mode.LOAD_MORE);
                    index++;
                } else {
                    ptrFrameLayout.setMode(PtrFrameLayout.Mode.NONE);
                }
                int itemCount = adapter.getItemCount();
                if (itemCount == 0) {
                    itemCountLL.setVisibility(View.VISIBLE);
                    tvSearchContent.setText(searchContentView.getText().toString().trim());
                    convertView.findViewById(R.id.iv_itemCountLL).setVisibility(View.VISIBLE);
                    convertView.findViewById(R.id.ll_itemCountLL).setVisibility(View.VISIBLE);
                    countTextView.setText("无相关结果");
                }
            }
        }
    }


    @OnClick({R.id.action_back, R.id.empty, R.id.searchBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_back:
                hidekeyboard(searchContentView);
                dismiss();
                if (type == RecordSearchActivity.TYPE_ORGANIZATION) {
                    activity.addSecondFragment(new UserFragment());
                }
                break;
            case R.id.empty:
                hidekeyboard(searchContentView);
                dismiss();
                break;
            case R.id.searchBtn:
                hidekeyboard(searchContentView);
                dismiss();
                break;
        }
    }

    public void hidekeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

  /*  @Override
    public void dismiss() {
        hidekeyboard(searchContentView);
        super.dismiss();
    }*/
}
