package com.vboss.okline.ui.record.organization;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.RecordRepository;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardLog;
import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.data.entities.OrgzCard;
import com.vboss.okline.data.remote.CardRemoteDataSource;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.record.search.RecordSearchActivity;
import com.vboss.okline.ui.record.search.RecordSearchFragment;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.TextUtils;
import com.vboss.okline.view.widget.OKCardView;

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
 * Summary : 单卡的详细记录信息(机构)
 */
public class OrganizationDetailFragment extends BaseFragment {

    @BindView(R.id.action_back)
    ImageButton actionBack;
    @BindView(R.id.action_back_layout)
    RelativeLayout actionBackLayout;
    @BindView(R.id.sdv_logo)
    SimpleDraweeView sdvLogo;
    @BindView(R.id.action_title)
    TextView actionTitle;
    @BindView(R.id.action_menu_button)
    ImageButton actionMenuButton;
    @BindView(R.id.action_menu_layout)
    RelativeLayout actionMenuLayout;
    @BindView(R.id.sdv_card)
    SimpleDraweeView sdvCard;
    @BindView(R.id.tv_card_num)
    TextView tvCardNum;
    @BindView(R.id.tv_card_balance)
    TextView tvCardBalance;
    @BindView(R.id.tv_card_credits)
    TextView tvCardCredits;
    @BindView(R.id.iv_card_date)
    ImageView ivCardDate;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptrFrameLayout)
    PtrClassicFrameLayout ptrFrameLayout;
    @BindView(R.id.iv_ocard_state)
    LogoView ivOcardState;
    @BindView(R.id.okcard_view)
    OKCardView okcardView;
    private int index = 1;
    private static final int PAGE_SIZE = 15;
    private OrganizationDetailAdapter adapter;
    private OrgzCard orgzCard;
    private MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_organization_detail, container, false);
        activity = (MainActivity) getActivity();
        ButterKnife.bind(this, view);
        initTop();
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
        ptrFrameLayout.setLoadingMinTime(300);
        ptrFrameLayout.setResistanceFooter(1.0f);
        ptrFrameLayout.setDurationToCloseFooter(0); // footer will hide immediately when completed
        ptrFrameLayout.setForceBackWhenComplete(true);
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
        ptrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrFrameLayout.autoRefresh();
            }
        }, 100);
    }

    private void initRecyclerView() {
        adapter = new OrganizationDetailAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initTop() {
        if (orgzCard == null) {
            return;
        }
        actionTitle.setText(orgzCard.cardName());
        if (getContext() instanceof MainActivity) {
            final MainActivity activity = (MainActivity) getContext();
            //右上角搜索按钮 点击监听
            actionMenuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //<editor-fold desc="郑军 2017-06-05 15:42:56 重构搜索流程">
                    //获取某机构下(即某张卡)有关(搜索关键字---某商户)的交易记录
                    RecordSearchFragment.newInstance(RecordSearchActivity.TYPE_CARD, orgzCard.cardMainType(), orgzCard.cardNo())
                            .show(getFragmentManager(), RecordSearchFragment.class.getName());
                    //</editor-fold>
                }
            });
            actionBackLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.removeSecondFragment();
                }
            });
        }

    }

    public void setOrgzCard(OrgzCard orgzCard) {
        this.orgzCard = orgzCard;
    }

    private void refresh() {
        loadDatas(true);
        loadOrgzInfo();
    }

    private void loadMore() {
        loadDatas(false);
    }

    private void loadDatas(final boolean refresh) {
        if (orgzCard == null) {
            return;
        }
        if (refresh) {
            index = 1;
        }
        recyclerView.setVisibility(View.VISIBLE);
        RecordRepository.getInstance(getContext())
                .orgzCardLogList(orgzCard.cardMainType(), orgzCard.cardNo(), null, index, PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<List<CardLog>>("OrganizationDetailFragment") {

                    @Override
                    public void onNext(List<CardLog> cardLogs) {
                        super.onNext(cardLogs);
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

    private void loadOrgzInfo() {
        CardRemoteDataSource.getInstance()
                .cardDetailByNum(orgzCard.cardMainType(), orgzCard.cardNo())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<CardEntity>("OrganizationDetailFragment") {
                    @Override
                    public void onNext(CardEntity cardEntity) {
                        sdvCard.setImageURI(cardEntity.imgUrl());
                        Utils.showLog(TAG,"卡片类型："+cardEntity.cardMainType());
                        tvCardNum.setVisibility(View.VISIBLE);
                        tvCardNum.setText("卡号：" + cardEntity.cardNo());
                        if (orgzCard.cardMainType() != CardType.BANK_CARD) {
                            Utils.showLog(TAG,"显示余额");
                            tvCardBalance.setVisibility(View.VISIBLE);
                            tvCardBalance.setText("余额：" + TextUtils.formatMoney(cardEntity.balance()));
                        }
                        if (orgzCard.cardMainType() != CardType.COMMON_CARD && orgzCard.cardMainType() != CardType.BANK_CARD) {
                            tvCardCredits.setVisibility(View.VISIBLE);
                            Utils.showLog(TAG,"显示积分");
                            tvCardCredits.setText("积分：" + cardEntity.integral());
                        }
                    }
                });
    }

    private static final String TAG = "OrganizationDetailFragment";

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
