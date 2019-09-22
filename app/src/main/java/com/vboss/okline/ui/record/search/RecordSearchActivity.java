package com.vboss.okline.ui.record.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.data.RecordRepository;
import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.ui.user.Utils;

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

public class RecordSearchActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptrFrameLayout)
    PtrClassicFrameLayout ptrFrameLayout;
    @BindView(R.id.searchContentView)
    EditText searchContentView;
    @BindView(R.id.searchBtn)
    TextView searchBtn;
    @BindView(R.id.countTextView)
    TextView countTextView;
    @BindView(R.id.itemCountLL)
    LinearLayout itemCountLL;

    public static final int TYPE_ORGANIZATION = 0;
    public static final int TYPE_CARD = 1;
    public static final int TYPE_CONVERSATION = 2;
    private int type = TYPE_ORGANIZATION;
    public static final String TAG = RecordSearchActivity.class.getSimpleName();
    private int index = 1;
    private static final int PAGE_SIZE = 15;
    private RecordSearchAdapter adapter;
    private String keyWord;
    private boolean loadMore = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", TYPE_ORGANIZATION);
        initPtrFrameLayout();
        initRecyclerView();
    }

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

    private void initRecyclerView() {
        adapter = new RecordSearchAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void search() {
        if (!loadMore) {
            keyWord = searchContentView.getText().toString();
            index = 1;
        }
        switch (type) {
            case TYPE_ORGANIZATION:
                organization();
                break;
            case TYPE_CARD:
                cardLog();
                break;
            case TYPE_CONVERSATION:
                break;
        }
    }

    private void organization() {
        RecordRepository.getInstance(this)
                .orgzCardList(keyWord, index, PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SearchSubscriber());
    }

    private void cardLog() {
        //<editor-fold desc="2017-06-05 15:26:16">
        int cardMainType = getIntent().getIntExtra("cardMainType", CardType.ALL);
        //</editor-fold>
        String cardNo = getIntent().getStringExtra("cardNo");
        RecordRepository.getInstance(this)
                .orgzCardLogList(cardMainType, cardNo, keyWord, index, PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SearchSubscriber());
    }

    class SearchSubscriber extends Subscriber {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            ptrFrameLayout.refreshComplete();
            Utils.customToast(RecordSearchActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(Object o) {
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
                itemCountLL.setVisibility(View.VISIBLE);
                countTextView.setText("搜索结果(" + adapter.getItemCount() + ")");
            }
        }
    }

    //<editor-fold desc="2017-06-05 15:26:47">
    @OnClick({R.id.action_back, R.id.searchBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_back:
                hidekeyboard(searchContentView);
                finish();
                break;
            case R.id.searchBtn:
                loadMore = false;
                itemCountLL.setVisibility(View.GONE);
                search();
                break;
        }
    }
    //</editor-fold>

    public void hidekeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
