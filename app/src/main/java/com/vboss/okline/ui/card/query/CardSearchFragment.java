package com.vboss.okline.ui.card.query;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vboss.okline.R;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardLog;
import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.ui.card.CardConstant;
import com.vboss.okline.ui.card.adapter.CardAdapter;
import com.vboss.okline.ui.card.adapter.CardLogAdapter;
import com.vboss.okline.ui.card.log.CardLogFragment;
import com.vboss.okline.ui.card.widget.SliderListView;
import com.vboss.okline.ui.card.widget.SliderView;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.scanner.QRCodeActivity;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import timber.log.Timber;

import static android.R.attr.gravity;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/6/3 10:09 <br/>
 * Summary  : 在这里描述Class的主要功能
 */

public class CardSearchFragment extends DialogFragment implements CardQueryContact.ICardQueryView,
        View.OnClickListener, TextView.OnEditorActionListener {
    private static final String TAG = CardSearchFragment.class.getSimpleName();
    public static final String KEY_MODE = "query_mode";
    private InputMethodManager imm;

    @BindView(R.id.edt_card)
    EditText edt_card;
    //empty view
    @BindView(R.id.empty)
    View empty;
    //list card view
    @BindView(R.id.listView)
    SliderListView listView;
    @BindView(R.id.fl_data)
    FrameLayout frameLayout;
    //list card log view
    @BindView(R.id.refreshFrameLayout)
    PtrClassicFrameLayout refreshFrameLayout;
    @BindView(R.id.rl_card_log_query)
    RecyclerView recyclerView;
    @BindView(R.id.fl_log_data)
    FrameLayout frameLayout1;

    @BindView(R.id.ib_more)
    ImageButton ib_more;
    @BindView(R.id.action_back)
    ImageButton action_back;

    //Added by 2017-06-07 search empty view
    @BindView(R.id.fl_no_data)
    RelativeLayout emptyLayout;
    @BindView(R.id.tv_search_key)
    TextView tv_search_key;

    @BindView(R.id.action_back_layout)
    RelativeLayout action_back_layout;

    private CardAdapter adapter;
    private List<CardEntity> cardList = new ArrayList<>();

    private CardLogAdapter logAdapter;
    private List<CardLog> cardLogList = new ArrayList<>();

    private static final int STATE_REFRESH = 0x003;
    private static final int STATE_LOAD_MORE = 0x004;
    private int state = STATE_REFRESH;
    private int page = 1;
    private int cardType;
    private String cardNo;

    private PopupWindow popupWindow;
    private CardQueryPresenter presenter;
    private MainActivity act;
    private int mode;
    private String keyWord;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        act = (MainActivity) getActivity();
        presenter = new CardQueryPresenter(context, this);
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Timber.tag(TAG).i("onCreateDialog ");
        View convertView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_card_search, null);
        ButterKnife.bind(this, convertView);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialog);
        builder.setView(convertView);
        initView();
        return builder.create();
    }

    private void initView() {
        mode = getArguments().getInt(KEY_MODE, CardQueryContact.MODE_QUERY_CARD);
        cardNo = getArguments().getString(CardConstant.KEY_CARD_NO);
        cardType = getArguments().getInt(CardConstant.KEY_CARD_TYPE, CardType.ALL);
        if (mode == CardQueryContact.MODE_QUERY_CARD_LOG) {
            edt_card.setHint(act.getResources().getString(R.string.card_query_logs));
            ib_more.setVisibility(View.GONE);
            action_back_layout.setVisibility(View.VISIBLE);
        }
        //modify by wangshuai 2017-06-06
//        ib_more.setVisibility(mode == CardQueryContact.MODE_QUERY_CARD ? View.VISIBLE : View.GONE);

        initPopWindow();
        initQueryCards();
        initQueryCardLogs();
        initQueryEdit();
    }

    /**
     * init editText
     */
    private void initQueryEdit() {
        edt_card.setOnEditorActionListener(this);
        edt_card.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    keyWord = s.toString();
                    empty.setVisibility(View.GONE);
                    if (mode == CardQueryContact.MODE_QUERY_CARD) {
                        presenter.queryCards(keyWord);
                    }
                } else {
                    empty.setVisibility(View.VISIBLE);
                    emptyLayout.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.GONE);
                    frameLayout1.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * card or card's log query
     * key is query keyword
     *
     * @param key search keyword
     */
    private void query(String key) {
        if (TextUtils.isEmpty(key)) {
            Utils.customToast(act, act.getResources().getString(R.string.card_query_key_empty),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (mode == CardQueryContact.MODE_QUERY_CARD) {
            Log.i(TAG, "query card from key ");
            frameLayout1.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
            presenter.queryCards(key);
        } else if (mode == CardQueryContact.MODE_QUERY_CARD_LOG) {
            Log.i(TAG, "query card's log from key ");
            frameLayout1.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            empty.setVisibility(View.GONE);
            refresh(key);
        }
    }

    private void initQueryCardLogs() {
        cardLogList = new ArrayList<>();
        logAdapter = new CardLogAdapter(act, cardLogList);
        recyclerView.setLayoutManager(new LinearLayoutManager(act));
        recyclerView.setAdapter(logAdapter);
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
     * init query card config
     */
    private void initQueryCards() {
        cardList.clear();
        adapter = new CardAdapter(act, cardList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    // ListView onItemClick if sliderView if open ,should be first close
                    SliderView sliderView = (SliderView) listView.getChildAt(position - listView.getFirstVisiblePosition());
                    if (sliderView != null && sliderView.open()) {
                        sliderView.reset();
                        return;
                    }
                    if (position < cardList.size()) {
                        CardEntity cardEntity = cardList.get(position);
                        //modify by wangshuai 2017-05-25 CREDENTIALS type don't click
                        if (cardEntity != null && cardEntity.cardMainType() != CardType.CREDENTIALS) {
                            act.addSecondFragment(CardLogFragment.newInstance(cardEntity));
                            hideSoftInput();
                            dismiss();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * init popWindow
     */
    private void initPopWindow() {
        View convertView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_card_search_pop, null);
        popupWindow = new PopupWindow(convertView, DensityUtil.dip2px(getActivity(), 120),
                DensityUtil.dip2px(getActivity(), 100));
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setOutsideTouchable(true);
        convertView.findViewById(R.id.tv_pop_receivables).setOnClickListener(this);
        convertView.findViewById(R.id.tv_pop_scanner).setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.tag(TAG).i(" onResume ");
        Window mWindow = getDialog().getWindow();
        if (mWindow != null) {
            Timber.tag(TAG).i(" onResume ");
            WindowManager.LayoutParams mLayoutParams = mWindow.getAttributes();
            mLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            mLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mLayoutParams.gravity = gravity;
            mWindow.setAttributes(mLayoutParams);
            //modify by wangshuai 2017-06-06 solve show soft input
            mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
        //modify by wangshuai 2017-06-06 solve show soft input
        edt_card.setFocusable(true);
        edt_card.setFocusableInTouchMode(true);
        edt_card.requestFocus();
        //modify by wangshuai 2017-06-06 solve show soft input
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @OnClick({R.id.tv_cancel, R.id.empty, R.id.ib_more, R.id.action_back_layout})
    public void onActionBarClick(View v) {
        switch (v.getId()) {
            case R.id.empty:
                hideSoftInput();
                break;
            case R.id.tv_cancel:
                hideSoftInput();
                break;
            case R.id.ib_more:
                Timber.tag(TAG).i(" popWindow show ");
                popupWindow.showAsDropDown(v, DensityUtil.dip2px(getActivity(), 5), 0);
                break;
            //modify by wangshuai 2017-06-06 update icon back
            case R.id.action_back_layout:
                hideSoftInput();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pop_scanner:
                act.startActivity(new Intent(act, QRCodeActivity.class));
                break;
            case R.id.tv_pop_receivables:

                break;
        }
        popupWindow.dismiss();
        hideSoftInput();
    }

    private void hideSoftInput() {
        imm.hideSoftInputFromWindow(edt_card.getWindowToken(), 0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 100);
    }

    @Override
    public void onStop() {
        super.onStop();
        popupWindow.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void updateCards(List<CardEntity> data) {
        Timber.tag(TAG).i("query card data %s", data.size());
        //modify by wangshuai 2017-06-07 update search result empty
        if (data.size() == 0) {
            showEmpty();
        } else {
            //modify by wangshuai 2017-06-06 update View visible or gone
            frameLayout.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            cardList.clear();
            cardList.addAll(data);
            adapter.notifyDataSetChanged();
        }

    }

    /**
     * search data is empty
     */
    private void showEmpty() {
        emptyLayout.setVisibility(View.VISIBLE);
        String str = String.format(getResources().getString(R.string.card_search_key), keyWord);
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(getActivity(), R.color.color_card_search_blue)),
                str.indexOf("：") + 1, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_search_key.setText(ss);
    }

    @Override
    public void updateCardLogs(List<CardLog> data) {
        Timber.tag(TAG).i("query card data %s", data.size());
        if (data.size() == 0) {
            showEmpty();
        } else {
            refreshFrameLayout.refreshComplete();
            //modify by wangshuai 2017-06-06 update View visible or gone
            frameLayout1.setVisibility(View.VISIBLE);
            if (data.size() >= 20) {
                refreshFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
            } else {
                refreshFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
            }
            if (state == STATE_REFRESH) {
                cardLogList.clear();
                cardLogList.addAll(data);
                recyclerView.setAdapter(logAdapter);
            }
            if (state == STATE_LOAD_MORE) {
                cardLogList.addAll(data);
                logAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void requestTimeOut(Throwable throwable, int methodFlag) {

    }

    @Override
    public void updateCardLogsFailed(Throwable throwable) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        Timber.tag(TAG).i("actionId %s", actionId);
        //modify by wangshuai 2017-06-06 before search clear list and adapter notify changed
        cardLogList.clear();
        logAdapter.notifyDataSetChanged();
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            imm.hideSoftInputFromWindow(edt_card.getWindowToken(), 0);
            String key = edt_card.getText().toString().trim();
            Timber.tag(TAG).i("key %s", key);
            if (!TextUtils.isEmpty(key)) {
                keyWord = key;
                query(key);
            }
        }
        return false;
    }

    public static CardSearchFragment newInstance(int mode, String cardNo, int cardMainType) {
        CardSearchFragment instance = new CardSearchFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_MODE, mode);
        args.putString(CardConstant.KEY_CARD_NO, cardNo);
        args.putInt(CardConstant.KEY_CARD_TYPE, cardMainType);
        instance.setArguments(args);
        return instance;
    }
}
