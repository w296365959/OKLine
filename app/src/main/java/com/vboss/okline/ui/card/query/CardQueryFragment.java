package com.vboss.okline.ui.card.query;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.helper.ToolbarSearchHelper;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardLog;
import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.ui.card.CardBaseFragment;
import com.vboss.okline.ui.card.CardConstant;
import com.vboss.okline.ui.card.adapter.CardAdapter;
import com.vboss.okline.ui.card.adapter.CardLogAdapter;
import com.vboss.okline.ui.card.log.CardLogFragment;
import com.vboss.okline.ui.card.widget.SliderListView;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.user.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/4/8 11:53 <br/>
 * Summary  : 卡证搜索，卡记录搜索
 */

public class CardQueryFragment extends CardBaseFragment implements CardQueryContact.ICardQueryView {
    public static final String KEY_MODE = "query_mode";
    private static final String TAG = CardQueryFragment.class.getSimpleName();
    private static final int STATE_REFRESH = 0x003;
    private static final int STATE_LOAD_MORE = 0x004;

    @BindView(R.id.listView)
    SliderListView mListView;
    @BindView(R.id.rl_card_log_query)
    RecyclerView recyclerView;
    @BindView(R.id.refreshFrameLayout)
    PtrClassicFrameLayout refreshFrameLayout;


    @BindView(R.id.tv_search_count)
    TextView tv_search_count;
    @BindView(R.id.layout_count)
    LinearLayout layout_count;
    MainActivity activity;
    CardQueryPresenter presenter;
    ToolbarSearchHelper toolbarHelper;

    private int mode;
    private int cardType;
    private String cardNo;
    private String keyWord = "";
    private int state = STATE_REFRESH;
    private int page = 1;

    private List<CardEntity> cardModelList;
    private List<CardLog> cardLogList;
    private CardAdapter cardAdapter;
    private CardLogAdapter cardLogAdapter;


    public static Fragment newInstance(int mode, int cardType, String cardNo) {
        Log.i(TAG, "mode : " + mode);
        CardQueryFragment instance = new CardQueryFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_MODE, mode);
        args.putString(CardConstant.KEY_CARD_NO, cardNo);
        args.putInt(CardConstant.KEY_CARD_TYPE, cardType);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //modify wangshuai by 2017-05-12
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_card_search, container, false);
        ButterKnife.bind(this, convertView);
        return convertView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDialog(activity);
        Bundle args = getArguments();
        if (args != null) {
            mode = args.getInt(KEY_MODE, CardQueryContact.MODE_QUERY_CARD);
            cardNo = args.getString(CardConstant.KEY_CARD_NO);
            cardType = args.getInt(CardConstant.KEY_CARD_TYPE, CardType.ALL);
        }
        initToolbar();
        //modify by wangshuai 2017-05-12 hide bottom tab
        activity.hideTabs(true);

    }

    /**
     * init toolBar
     */
    private void initToolbar() {
        toolbarHelper = new ToolbarSearchHelper(activity);
        toolbarHelper.enableHomeAsUp(true);
        toolbarHelper.setOnActionSearchListener(new ToolbarSearchHelper.OnActionSearchListener() {
            @Override
            public void onSearch(String key) {
                Log.i(TAG, "key: " + key);
                keyWord = key;
                query(keyWord);
                hideSoftInput(toolbarHelper.getSearchText());
            }
        });
        toolbarHelper.setNavigationListener(new ToolbarSearchHelper.OnNavigationClickListener() {
            @Override
            public void onNavigationClick() {
                hideSoftInput(toolbarHelper.getSearchText());
                activity.removeSecondFragment();
                //modify by wangshuai 2017-05-12 show bottom tab
                activity.hideTabs(false);
            }
        });
        init();
    }

    /**
     * init view configuration
     */
    private void init() {
        try {
            presenter = new CardQueryPresenter(activity, this);
            if (CardQueryContact.MODE_QUERY_CARD == mode) {
                mListView.setVisibility(View.VISIBLE);
                cardModelList = new ArrayList<>();
                cardAdapter = new CardAdapter(activity, cardModelList);
                cardAdapter.setCardNoVisible(true);
                mListView.setAdapter(cardAdapter);
                mListView.setOnItemClickListener(onItemClickListener);
                toolbarHelper.setSearchHintText(activity.getResources().getString(R.string.card_query_cards));
            } else if (CardQueryContact.MODE_QUERY_CARD_LOG == mode) {
                initPtrFrameLayout();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Added by wangshuai 2017-05-22 update card log refresh style
     * init refresh recyclerView
     */
    private void initPtrFrameLayout() {
        recyclerView.setVisibility(View.VISIBLE);
        refreshFrameLayout.setVisibility(View.VISIBLE);
        cardLogList = new ArrayList<>();
        cardLogAdapter = new CardLogAdapter(activity, cardLogList);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(cardLogAdapter);
        toolbarHelper.setSearchHintText(activity.getResources().getString(R.string.card_query_logs));

        refreshFrameLayout.setLastUpdateTimeRelateObject(this);
        refreshFrameLayout.setLoadingMinTime(300);
        refreshFrameLayout.setResistanceFooter(1.0f);
        refreshFrameLayout.setDurationToCloseFooter(0); // footer will hide immediately when completed
        refreshFrameLayout.setForceBackWhenComplete(true);
        refreshFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
        refreshFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout ptrFrameLayout) {
                loadMore(keyWord);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                if (!TextUtils.isEmpty(keyWord)) {
                    refresh(keyWord);
                } else {
                    refreshFrameLayout.refreshComplete();
                    refreshFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
                }
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
     * the Method RecyclerView onRefresh call refresh data
     *
     * @param key merchant name
     */
    private void refresh(String key) {
        state = STATE_REFRESH;
        page = 1;
        if (mode == CardQueryContact.MODE_QUERY_CARD_LOG) {
            Log.i(TAG, "query card's log from key ");
            presenter.queryCardLogs(cardType, cardNo, key, page, 20);
        }
    }

    /**
     * the Method RecyclerView onLoadMore call load more data
     *
     * @param key merchant name
     */
    private void loadMore(String key) {
        state = STATE_LOAD_MORE;
        page++;
        if (mode == CardQueryContact.MODE_QUERY_CARD_LOG) {
            Log.i(TAG, "query card's log from key ");
            presenter.queryCardLogs(cardType, cardNo, key, page, 20);
        }
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
        if (mode == CardQueryContact.MODE_QUERY_CARD) {
            Log.i(TAG, "query card from key ");
            presenter.queryCards(key);
        } else if (mode == CardQueryContact.MODE_QUERY_CARD_LOG) {
            Log.i(TAG, "query card's log from key ");
            refresh(key);
        }
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
    public void updateCards(List<CardEntity> data) {
        cardModelList.clear();
        if (data != null) {
            cardModelList.addAll(data);
        }
        cardAdapter.notifyDataSetChanged();
        layout_count.setVisibility(View.VISIBLE);
        tv_search_count.setText(String.format(activity.getResources().getString(R.string.card_search),
                String.valueOf(cardModelList.size())));
    }

    @Override
    public void updateCardLogs(List<CardLog> data) {
        refreshFrameLayout.refreshComplete();
        if (data.size() >= 20) {
            refreshFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
        } else {
            refreshFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
        }
        if (state == STATE_REFRESH) {
            cardLogList.clear();
            cardLogList.addAll(data);
            recyclerView.setAdapter(cardLogAdapter);
        }
        if (state == STATE_LOAD_MORE) {
            cardLogList.addAll(data);
            cardLogAdapter.notifyDataSetChanged();
        }
        layout_count.setVisibility(View.VISIBLE);
        tv_search_count.setText(String.format(activity.getResources().getString(R.string.card_search),
                String.valueOf(cardLogList.size())));
    }

    @Override
    public void requestTimeOut(Throwable throwable, int methodFlag) {
        handlerTimeOut(throwable, methodFlag);
    }

    @Override
    public void updateCardLogsFailed(Throwable throwable) {
        /* *********** Added by wangshuai 2017-04-26 *********** */
        refreshFrameLayout.refreshComplete();
        //refresh data
        if (state == STATE_REFRESH) {
            cardLogList.clear();
            recyclerView.setAdapter(cardLogAdapter);
        }
        //load more data
        if (state == STATE_LOAD_MORE) {
            cardLogAdapter.notifyDataSetChanged();
        }
        layout_count.setVisibility(View.VISIBLE);
        tv_search_count.setText(String.format(activity.getResources().getString(R.string.card_search),
                String.valueOf(cardLogList.size())));
    }


    /**
     * listView item click listener
     */
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (mode == CardQueryContact.MODE_QUERY_CARD) {
                if (position < cardModelList.size()) {
                    CardEntity cardEntity = cardModelList.get(position);
                    if (cardEntity != null) {
                        activity.addSecondFragment(CardLogFragment.newInstance(
                                cardEntity));
                    }
                }
            }
        }
    };

    @Override
    protected void onRetry(int method) {
        if (TextUtils.isEmpty(keyWord)) {
            keyWord = toolbarHelper.getSearchText().getText().toString().trim();
        }
        query(keyWord);
    }
}
