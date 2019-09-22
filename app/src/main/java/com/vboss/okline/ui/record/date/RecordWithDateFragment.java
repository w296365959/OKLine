package com.vboss.okline.ui.record.date;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.RecordRepository;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.user.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Jiang Zhongyuan <br/>
 * Email   : jiangzhongyuan@okline.cn <br/>
 * Date    : 17/5/2 <br/>
 * Summary : 按日期记录
 */
public class RecordWithDateFragment extends BaseFragment {
    public static final String TAG = RecordWithDateFragment.class.getSimpleName();

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptrFrameLayout)
    PtrClassicFrameLayout ptrFrameLayout;
    private RecordDateAdapter adapter;

    private int index = 1;
    private static final int PAGE_SIZE = 15;
    private MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_record_with_date, container, false);
        ButterKnife.bind(this, view);
        initPtrFrameLayout();
        initRecyclerView();
        return view;
    }

    private void initPtrFrameLayout() {
        ptrFrameLayout.setLastUpdateTimeRelateObject(this);
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler2() {

            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                loadMore();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refresh();
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
        ptrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrFrameLayout.autoRefresh();
            }
        }, 100);
        ptrFrameLayout.setLoadingMinTime(300);
        ptrFrameLayout.setResistanceFooter(1.0f);
        ptrFrameLayout.setDurationToCloseFooter(0); // footer will hide immediately when completed
        ptrFrameLayout.setForceBackWhenComplete(true);
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
    }

    private void initRecyclerView() {
        adapter = new RecordDateAdapter(getContext(), new ArrayList<DateObj>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void refresh() {
        loadParent(true);
    }

    private void loadParent(final boolean refresh) {
        if (refresh) {
            index = 1;
        }
        recyclerView.setVisibility(View.VISIBLE);
        RecordRepository repository = RecordRepository.getInstance(getContext());
        repository.getTransDateList(index, PAGE_SIZE)
                .map(new Func1<List<String>, List<DateObj>>() {
                    @Override
                    public List<DateObj> call(List<String> strings) {
                        List<DateObj> dateList = new ArrayList<>();
                        for (String title : strings) {
                            DateObj dateObj = new DateObj(title);
                            dateList.add(dateObj);
                        }
                        return dateList;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<List<DateObj>>(TAG) {
                    @Override
                    public void onNext(List<DateObj> list) {
                        ptrFrameLayout.refreshComplete();
                        if (refresh) {
                            if (list.size() == 0) {
                                recyclerView.setVisibility(View.GONE);
                            }
                            adapter.refresh(list);
                        } else {
                            adapter.addDatas(list);
                        }
                        if (list.size() >= PAGE_SIZE) {
                            ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
                            index++;
                        } else {
                            ptrFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ptrFrameLayout.refreshComplete();
                        if (refresh) {
                            recyclerView.setVisibility(View.GONE);
                        }
                        Utils.customToast(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadMore() {
        loadParent(false);
    }

    public void setDate(String date) {
        List<DateObj> dateObjs = new ArrayList<>();
        dateObjs.add(new DateObj(date));
        if (adapter != null) {
            adapter.refresh(dateObjs, true);
        }
    }
}
