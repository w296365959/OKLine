package com.okline.vboss.assistant.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.okline.vboss.assistant.R;
import com.okline.vboss.assistant.R2;
import com.okline.vboss.assistant.adapter.CardAdapter;
import com.okline.vboss.assistant.base.Config;
import com.okline.vboss.assistant.base.DefaultSubscribe;
import com.okline.vboss.assistant.net.CardEntity;
import com.okline.vboss.assistant.net.CardType;
import com.okline.vboss.assistant.net.OLApiService;
import com.okline.vboss.assistant.ui.notice.CardHelper;
import com.okline.vboss.assistant.ui.recharge.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;

    private List<CardEntity> cardList;
    private CardAdapter cardAdapter;
    public static int ocardState;
    private BroadcastReceiver broadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_assistant);
        ButterKnife.bind(this);
        //Added by wangshuai 2017-06-14 register RxBus
        RxBus.get().register(this);
        getOlNoAndAddress();

        //Added by wangshuai test
//        OLApiService.initialize(this.getApplication(), Config.DEFAULT_OLNO, Config.DEFAULT_BLUE_ADDRESS);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.vboss.okline.action.OCARD_STATE_CHANGED");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int ocard_state = intent.getIntExtra("ocard_state", 0);
                Utils.showLog(TAG, "卡助理收到欧卡状态广播：" + ocard_state);
                onOcardStateChanged(ocard_state);
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);

    }

    private void initCardData() {
        cardList = new ArrayList<>();
        cardAdapter = new CardAdapter(this, cardList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cardAdapter);
        requestCard();
    }

    private void getOlNoAndAddress() {
        Intent intent = getIntent();
        try {
            if (intent != null) {
                //modify by wangshuai 2017-06-14
                String olNo = intent.getStringExtra(Config.KEY_OLNO);
                Config.OL_NUM = olNo;
                ocardState = intent.getIntExtra(Config.KEY_OCARD_STATE, 0);
                onOcardStateChanged(ocardState);
                String address = intent.getStringExtra(Config.KEY_ADDRESS);
                Config.ADDRESS = address;
                Config.USER_NAME = intent.getStringExtra(Config.KEY_USER_NAME);
                Config.MOBILE = intent.getStringExtra(Config.KEY_MOBILE);

                Timber.tag(TAG).i("bluetooth Address %s olNo %s ", Config.ADDRESS, Config.OL_NUM);

                //modify by wangshuai 2017-06-15 address maybe is empty
                /*if (TextUtils.isEmpty(olNo) || TextUtils.isEmpty(address)) {
                    throw new IllegalStateException("Fail to get olno & address");
                }*/

                //Initialize Oule Service & TsmService
                //modify by wangshuai 2017-06-06
                OLApiService.initialize(this.getApplication(), olNo);
                OLApiService.getInstance().setAddress(address);

                //Cache olNo & address in memory
                /*AssistantApplication application = (AssistantApplication) getApplication();
                application.setOlno(olNo);
                application.setAddress(address);*/

                //modify by wangshuai 2017-06-14
                initCardData();

            } else {
                throw new IllegalStateException("Without Bundle including olno & address");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onOcardStateChanged(int state) {
        ocardState = state;
        Utils.showLog(TAG, "卡助理欧卡状态更新：" + ocardState);
        switch (state) {
            case 1:
                Utils.showLog(TAG, "欧卡已连接");
                break;
            case 3:
                Utils.showLog(TAG, "安全电话不可用");
                break;
            case 0:
                Utils.showLog(TAG, "欧卡未绑定");
                break;
            case 2:
                Utils.showLog(TAG, "欧卡未连接");
                break;
        }
    }

    /**
     * request all card
     */
    public void requestCard() {
        OLApiService.getInstance().getDownloadableCardList(CardType.ALL)
                .flatMap(new Func1<List<CardEntity>, Observable<List<CardEntity>>>() {
                    @Override
                    public Observable<List<CardEntity>> call(List<CardEntity> cardEntities) {
                        Timber.tag(TAG).i("can download card list size %s", cardEntities.size());
                        cardList.clear();
                        cardList.addAll(cardEntities);
                        cardAdapter.setPreIndex(cardEntities.size());
                        return OLApiService.getInstance().getDownloadedCardList(CardType.ALL);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<List<CardEntity>>(TAG) {
                    @Override
                    public void onNext(List<CardEntity> cardEntities) {
                        super.onNext(cardEntities);
                        Timber.tag(TAG).i("downloaded cardEntities size %s", cardEntities.size());
                        //modify by wangshuai 2017-06-15 filter bank or common cards
                        filterBankOrCommonCard(cardEntities);
                    }
                });
    }

    /**
     * filter bank or common,vip cards
     *
     * @param cardEntities List<CardEntity>
     */
    private void filterBankOrCommonCard(List<CardEntity> cardEntities) {
        for (CardEntity entity : cardEntities) {
            if (CardType.BANK_CARD == entity.getCardMainType() ||
                    CardType.COMMON_CARD == entity.getCardMainType() ||
                    CardType.VIP_CARD == entity.getCardMainType()) {
                cardList.add(entity);
            }
        }
        cardAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Added by wangshuai 2017-06-14 unregister RxBus
        RxBus.get().unregister(this);
        unregisterReceiver(broadcastReceiver);
    }

    @Subscribe(tags = {@Tag(CardHelper.EVENT_REFRESH)})
    public void refresh(Boolean b) {
        Timber.tag(TAG).i("refresh data ");
        requestCard();
    }

    @OnClick(R2.id.ib_back)
    void onBack() {
        finish();
    }

}
