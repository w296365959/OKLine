package com.okline.vboss.assistant.net;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.cosw.sdkblecard.DeviceInfo;
import com.google.gson.reflect.TypeToken;
import com.okline.vboss.http.OKLineClient;
import com.vboss.okline.tsm.OCardService;
import com.vboss.okline.tsm.TsmCardFlavor;
import com.vboss.okline.tsm.TsmCardType;

import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.functions.Func1;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/6/7 <br/>
 * Summary : 欧乐服务接口
 */
public class OLApiService {
    /**
     * 可下载的卡片列表
     */
    private static final String POOL_CARD_REQUEST = "qryAppListByCardRequest";
    /**
     * 已下载的卡片列表
     */
    private static final String CARD_LIST = "qryCardListByCardTypeRequest";
    /**
     * 同步卡片
     */
    private static final String SYNC_OR_CREATE_CARD = "syncCardInfoRequest";

    /**
     * 开卡条件
     */
    private static final String PRE_CONDITION_TO_OPEN = "qryPayFeeListRequest";

    private final OKLineClient mOlClient;
    private static OLApiService INSTANCE;
    private final String mOlNo;
    private String mAddress;
    private final OCardService cardService;

    /**
     * Get {@link OLApiService} instance, invoke after do {@link #initialize(Context, String)}
     *
     * @return OLApiService instance
     */
    public static OLApiService getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Invoke OLApiService#initialize(Context,String, String) first");
        }

        return INSTANCE;

    }

    /**
     * Initialize {@link OLApiService}
     *
     * @param context Application对象
     * @param olNo    欧乐号
     */
    public static void initialize(@NonNull Context context, @NonNull String olNo) {
        if (INSTANCE == null) {
            INSTANCE = new OLApiService(context.getApplicationContext(), olNo);
        }
    }

    private OLApiService(@NonNull Context context, @NonNull String olNo) {
        mOlClient = OKLineClient.getInstance();
        cardService = OCardService.getInstance(context);
        mOlNo = olNo;
    }

    /**
     * 配置蓝牙地址
     *
     * @param address 蓝牙地址
     */
    public void setAddress(@NonNull String address) {
        this.mAddress = address;
    }

    /**
     * 监听蓝牙连接状态
     *
     * @return 已连接返回true，否则返回false。
     */
    public Observable<Boolean> monitorBTState() {
        return cardService.getConnection().map(new Func1<OCardService.Connection, Boolean>() {
            @Override
            public Boolean call(OCardService.Connection connection) {
                return connection.isConnected();
            }
        });
    }

    /**
     * 获取可下载卡的卡列表
     *
     * @param cardMainType
     * @return
     */
    public Observable<List<CardEntity>> getDownloadableCardList(int cardMainType) {
        return mOlClient.requestAsyncForData(POOL_CARD_REQUEST, RequestData.newBuilder()
                        .olNo(mOlNo)
                        .sysBelong(1)
                        .cardMainType(cardMainType)
                        .pageIndex(1)
                        .pageSize(Integer.MAX_VALUE)
                        .build(),
                new TypeToken<List<CardEntity>>() {
                }.getType());
    }

    /**
     * 获取已下载的卡列表
     *
     * @return
     */
    public Observable<List<CardEntity>> getDownloadedCardList(int cardMainType) {
        return mOlClient.requestAsyncForData(CARD_LIST, RequestData.newBuilder()
                .olNo(mOlNo)
                .cardMainType(cardMainType)
                .build(), new TypeToken<List<CardEntity>>() {
        }.getType());
    }

    /**
     * 获取开卡条件
     *
     * @param cardMainType 卡片类型
     * @param aid          卡片应用Id
     * @return 返回开卡条件对象
     */
    public Observable<CardCondition> getCardOpenCondition(int cardMainType, String aid) {
        return mOlClient.requestAsyncForData(PRE_CONDITION_TO_OPEN, RequestData.newBuilder()
                .olNo(mOlNo)
                .cardMainType(cardMainType)
                .aid(aid).build(), CardCondition.class);
    }

    /**
     * 下载卡片，并将卡片同步到服务器
     *
     * @param context      上下文
     * @param cardMainType 卡片主类型
     * @param aid          卡片应用Id
     * @param orderNo      订单号
     * @return 返回下载的卡片详情
     */
    public Observable<CardEntity> downloadCard(@NonNull Context context, int cardMainType, @NonNull final String aid, @NonNull final String orderNo) {
        final int tsmType = parseTsmCardType(cardMainType);
        return prepareTsm(context).flatMap(new Func1<Boolean, Observable<String>>() {
            @Override
            public Observable<String> call(Boolean aBoolean) {
                return tsmDownloadCard(tsmType, aid);
            }
        }).flatMap(new Func1<String, Observable<CardEntity>>() {
            @Override
            public Observable<CardEntity> call(String cardNo) {
                return syncCard(cardNo, aid, orderNo);
            }
        });
    }

    /**
     * 连接蓝牙卡，并返回连接的蓝牙卡设备对象
     *
     * @param context 上下文
     * @param address 蓝牙地址
     * @param timeout 连接超时时间
     * @return
     */
    public Observable<DeviceInfo> requestOCardConnection(Context context, String address, int timeout) {
        return cardService.connectOCard(context, address, timeout);
    }


    /**
     * 解析TSM卡片类型
     *
     * @param cardMainType 卡片类型
     * @return Tsm卡片类型
     * @see TsmCardType
     */
    private int parseTsmCardType(int cardMainType) {

        switch (cardMainType) {
            case CardType.BANK_CARD:
                return TsmCardType.CARD_TYPE_PBOC;
            case CardType.TRANS_CARD:
            case CardType.COMMON_CARD:
                return TsmCardType.CARD_TYPE_TRANSPORT;
            case CardType.VIP_CARD:
                return TsmCardType.CARD_TYPE_VIP;
            default:
                throw new UnsupportedOperationException(String.format(Locale.getDefault(), "Unsupported card type [%d]", cardMainType));
        }
    }

    /**
     * 下载卡片到蓝牙卡。 先通过获取卡号来判断是否已安装应用，如果卡号获取失败，则安装应用，否则直接返回卡号。
     *
     * @param tsmCardType
     * @param aid
     * @return
     */
    private Observable<String> tsmDownloadCard(final int tsmCardType, final String aid) {
        //Modified by shihaijun on 2017-06-14: 先删除已安装的card app再下载card app。 start
        return cardService.appDelete(aid).flatMap(new Func1<Boolean, Observable<String>>() {
            @Override
            public Observable<String> call(Boolean shouldInstall) {
                return cardService.appInstall(tsmCardType, aid);
            }
        });
        //Modified by shihaijun on 2017-06-14: 先删除已安装的card app再下载card app。 end
    }

    /**
     * 同步卡片
     *
     * @param cardNo 卡号
     * @param aid    卡片应用id
     * @param order  支付订单号
     * @return 返回对应卡片详情
     */
    private Observable<CardEntity> syncCard(String cardNo, String aid, String order) {
        return mOlClient.requestAsyncForData(SYNC_OR_CREATE_CARD, RequestData.newBuilder()
                .olNo(mOlNo)
                .cardNo(cardNo)
                .aid(aid)
                .order(order).build(), CardEntity.class);
    }

    /**
     * 卡片TSM充值，未同步到欧乐服务器
     *
     * @param context     上下文
     * @param tsmCardType TSM卡片类型
     * @param aid         卡片应用Aid
     * @param amount      充值金额，单位分
     * @return 充值成功后返回卡片余额，单位元。
     * @see com.vboss.okline.tsm.TsmCardType
     */
    public Observable<String> tsmRechargeCard(@NonNull Context context, @TsmCardFlavor final int tsmCardType, @NonNull final String aid, @IntRange(from = 1) final int amount) {
        return prepareTsm(context).flatMap(new Func1<Boolean, Observable<String>>() {
            @Override
            public Observable<String> call(Boolean prepared) {
                return cardService.recharge2(tsmCardType, aid, amount);
            }
        });
    }

    /**
     * 就绪蓝牙和TSM服务
     *
     * @param context
     * @return 准备好返回true，否则返回false。
     */
    private Observable<Boolean> prepareTsm(Context context) {
        return cardService.connectAndInitialize(context, mAddress, 20 * 1000).first();
    }

}
