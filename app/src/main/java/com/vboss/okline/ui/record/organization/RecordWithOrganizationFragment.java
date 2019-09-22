package com.vboss.okline.ui.record.organization;

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
import com.vboss.okline.data.entities.OrgzCard;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.user.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Jiang Zhongyuan <br/>
 * Email   : jiangzhongyuan@okline.cn <br/>
 * Date    : 17/5/5 <br/>
 * Summary : 记录信息(机构)
 */
public class RecordWithOrganizationFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptrFrameLayout)
    PtrClassicFrameLayout ptrFrameLayout;
    private int index = 1;
    private static final int PAGE_SIZE = 15;
    private RecordOrganizationAdapter adapter;
    private MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_with_organization, container, false);
        activity = (MainActivity) getActivity();
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

            /**
             * 刷新回调函数
             * @param frame
             * @param content
             * @param header
             * @return  返回true表示可以下拉刷新
             */
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
        adapter = new RecordOrganizationAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void refresh() {
        loadDatas(true);
    }

    private void loadMore() {
        loadDatas(false);
    }

    /**
     * 请求数据
     * @param refresh  是否请求第一页数据
     */
    private void loadDatas(final boolean refresh) {
        if (refresh) {
            //页码
            index = 1;
        }
        recyclerView.setVisibility(View.VISIBLE);
        RecordRepository.getInstance(getContext())
                .orgzCardList(null, index, PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<List<OrgzCard>>("RecordWithOrganizationFragment") {

                    @Override
                    public void onNext(List<OrgzCard> cardLogs) {
                        super.onNext(cardLogs);
                        //停止刷新
                        ptrFrameLayout.refreshComplete();
                        if (refresh) {
                            if (cardLogs.size() == 0) {
                                recyclerView.setVisibility(View.GONE);
                            }
                            adapter.refresh(cardLogs);
                        } else {
                            adapter.addDatas(cardLogs);
                        }
                        if (cardLogs.size() >= PAGE_SIZE) {
                            //即可刷新又可以加载更多
                            ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
                            index++;
                        } else {
                            //只有刷新
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

    private static final String TAG = "RecordWithOrganizationFragment";
}
