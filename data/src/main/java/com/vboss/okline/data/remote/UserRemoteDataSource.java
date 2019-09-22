package com.vboss.okline.data.remote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.PhoneNumberUtils;

import com.google.common.base.Preconditions;
import com.google.gson.reflect.TypeToken;
import com.okline.vboss.http.OKLineClient;
import com.okline.vboss.http.OLException;
import com.vboss.okline.data.DeviceFlavor;
import com.vboss.okline.data.UserDataSource;
import com.vboss.okline.data.WorkerThreadScheduler;
import com.vboss.okline.data.entities.AddableBankInfo;
import com.vboss.okline.data.entities.CardCondition;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.Cert;
import com.vboss.okline.data.entities.OKCard;
import com.vboss.okline.data.entities.PaymentsOrder;
import com.vboss.okline.data.entities.TransferProtocol;
import com.vboss.okline.data.entities.User;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/1 <br/>
 * Summary : 用户服务器数据操作入口
 */
public class UserRemoteDataSource implements UserDataSource {
    /**
     * 用户注册
     */
    private static final String USER_REGISTER = "userRegisterRequest";

    /**
     * 用户登陆
     */
    private static final String USER_LOGIN = "userLoginRequest";

    /**
     * 用户重置注册
     */
    private static final String USER_RESET_REGISTER = "userResetLoginRequest";
    /**
     * 获取绑定的欧卡
     */
    private static final String GET_OCARD = "qryDeviceInfoRequest";
    /**
     * 绑定欧卡
     */
    private static final String BIND_OCARD = "addDeviceInfoRequest";
    /**
     * 欧卡身份验证
     */
    private static final String AUTHENTICATE = "idcardValidRequest";
    /**
     * 挂失欧卡
     */
    private static final String LOSS_OCARD = "lossDeviceInfoRequest";
    /**
     * 重启欧卡
     */
    private static final String RESUME_OCARD = "restartDeviceInfoRequest";
    /**
     * 补新卡
     */
    private static final String MAKEUP_OCARD = "replaceDeviceInfoRequest";

    /**
     * 开卡条件
     */
    private static final String PRE_CONDITION_TO_OPEN = "qryPayFeeListRequest";

    /**
     * 同步卡片
     */
    private static final String SYNC_OR_CREATE_CARD = "syncCardInfoRequest";
    /**
     * C2C转账
     */
    private static final String C2C_CASH = "userTransFeeRequest";
    /**
     * 获取CA证书
     */
    private static final String GET_CA = "qryUserCertRequest";

    private static final String SET_JPUSH_ID = "setJPushIdRequest";

    /**
     * 设置用户头像
     */
    private static final String SET_USER_AVATAR = "setUserImgByOlNoRequest";

    private static final String REQUEST_TRANSFER_LIST = "setTransTipsRequest";

    private static final String RESPONSE_TRANSFER = "qryTransTipsRequest";

    private static final String QUERY_TRANSFER_LIST = "qryTransBankCardRequest";

    private static final String GET_TRANSFER_HISTORY = "qryC2CTransListRequest";

    private static final String GET_OL_MEMBER_INFO = "qryBusinessCardRequest";

    private static final String GET_CONTACT_VISITING_CARD = "qryAddressListRequest";

    private static final String CREATE_PAYMENT_ORDER = "createQRCodeRequest";

    private static final String QUERY_PAYMENT_ORDER = "qryQRCodeInfoRequest";

    private static final String PAYMENT_TO_PAY = "userTransferRequest";

    private static final String GET_PHONE_FOR_SMS = "getSmsPhoneRequest";

    private static final String SAVE_USER_INFO = "updateBusinessCardRequest";

    private static final String GET_BANK_ACCOUNT_INFO = "getMerListRequest";

    private static final String QUERY_BANK_MERCHANT_BY_BIN = "qryBankBinRequest";

    private static UserRemoteDataSource instance;
    private final OKLineClient client;

    public static UserRemoteDataSource getInstance() {
        if (instance == null) {
            instance = new UserRemoteDataSource();
        }
        return instance;
    }

    private UserRemoteDataSource() {
        client = OKLineClient.getInstance();
    }

    @Override
    public Observable<User> registerUser(Context context, @NonNull String idCardNo, @NonNull String realName, int creditType, @NonNull String imei, @NonNull String imsi) {

        return client.requestAsyncForData(USER_REGISTER, RequestData.newBuilder()
                .idCardNo(idCardNo)
                .realName(realName)
                .creditType(creditType)
                .imei(imei)
                .imsi(imsi).build(), User.class);
    }

    /**
     * 重置或注册用户
     *
     * @param creditType 证件类型
     * @param idCardNo   证件号
     * @param realName   真实姓名
     * @param imei       手机imei号
     * @param imsi       sim卡imsi号
     * @return 返回用户信息
     */
    public Observable<User> resetOrRegisterUser(int creditType, String idCardNo, String realName, String imei, String imsi) {
        return client.requestAsyncForData(USER_RESET_REGISTER, RequestData.newBuilder()
                .olNo(null)
                .creditType(creditType)
                .idCardNo(idCardNo)
                .realName(realName)
                .imei(imei)
                .imsi(imsi).build(), User.class);
    }

    /**
     * 用户登陆
     *
     * @param imei 手机imei号
     * @param imsi sim卡imsi号
     * @return 返回相关用户对象
     */
    public Observable<User> loginUser(@NonNull String imei, @NonNull String imsi) {
        Preconditions.checkNotNull(imei);
        Preconditions.checkNotNull(imsi);
        return client.requestAsyncForData(USER_LOGIN, RequestData.newBuilder()
                .olNo(null)
                .imei(imei)
                .imsi(imsi).build(), User.class);
    }

    /**
     * 查询接收短信的手机号码
     */
    public Observable<String> getCellPhoneForSms() {
        return client.<User>requestAsyncForData(GET_PHONE_FOR_SMS, RequestData.newBuilder().build(), User.class).map(new Func1<User, String>() {
            @Override
            public String call(User user) {
                return user.getPhone();
            }
        });
    }

    /**
     * 保存名片信息
     *
     * @param phone    手机号码
     * @param infoType 信息类型
     * @param content  信息内容，json格式
     * @return
     */
    public Observable<Boolean> saveUserInfo(@Nullable String phone, int infoType, @NonNull String content) {
        return client.requestAsyncForBoolean(SAVE_USER_INFO, RequestData.newBuilder()
                .dataType(infoType)
                .content(content)
                .phone(phone).build());
    }

    /**
     * 获取银行账户信息
     *
     * @param cardMainType 卡片类型
     * @return 返回银行账户信息列表
     */
    public Observable<List<AddableBankInfo>> getAddableBankList(int cardMainType) {
        return client.requestAsyncForData(GET_BANK_ACCOUNT_INFO, RequestData.newBuilder()
                .cardMainType(cardMainType).build(), new TypeToken<List<AddableBankInfo>>() {
        }.getType());
    }

    /**
     * 根据卡号获取卡片所属商户名。
     *
     * @param cardNo 完整卡号或者卡号前6位
     * @return 返回包含merName和cardLen字段的{@code AddablaeBankInfo}对象
     */
    public Observable<AddableBankInfo> queryBankMerchantName(@NonNull String cardNo) {
        return client.requestAsyncForData(QUERY_BANK_MERCHANT_BY_BIN, RequestData.newBuilder()
                .cardNo(cardNo).build(), AddableBankInfo.class);
    }

    /**
     * 通过手机号码或对方欧乐号查询欧乐会员资料
     *
     * @param keyOfSearch 欧乐号／手机号码
     * @return
     */
    @Override
    public Observable<User> getOKLineMember(@Nullable String keyOfSearch) {

        RequestData.Builder builder = RequestData.newBuilder();
//Modified by shihaijun on 2017-06-20 : 查询对方个人信息 start
        if (keyOfSearch != null) {
            if (PhoneNumberUtils.isGlobalPhoneNumber(keyOfSearch)) {
                //根据手机号码查询个人信息
                builder.phone(keyOfSearch);
            } else if (keyOfSearch.toUpperCase().startsWith("OLHZ")) {
                //根据欧乐号查询好友个人信息
                builder.friendOlNo(keyOfSearch);
            } else {
                return Observable.error(new OLException("Can not get member information by " + keyOfSearch));
            }
        }
//Modified by shihaijun on 2017-06-20 : 查询对方个人信息 end

        return client.requestAsyncForData(GET_OL_MEMBER_INFO, builder.build(), User.class);
    }

    /**
     * 获取好友名片详情
     *
     * @param keyOfSearch 搜索关键字
     * @return
     */
    public Observable<User> getContactVisitingCard(String keyOfSearch) {
        RequestData.Builder builder = RequestData.newBuilder();
        if (keyOfSearch != null) {
            if (PhoneNumberUtils.isGlobalPhoneNumber(keyOfSearch)) {
                builder.phone(keyOfSearch);
            } else if (keyOfSearch.toUpperCase().startsWith("OLHZ")) {
                builder.friendOlNo(keyOfSearch);
            } else {
                return Observable.error(new OLException("Can not get member information by " + keyOfSearch));
            }
        }
        return client.requestAsyncForData(GET_CONTACT_VISITING_CARD, builder.build(), User.class);
    }

    @Override
    public Observable<String> updateAvatar(@NonNull final String filePath) {
        return client.requestAsyncForBoolean(SET_USER_AVATAR, RequestData.newBuilder()
                .imgUrl(filePath).build()).flatMap(new Func1<Boolean, Observable<String>>() {
            @Override
            public Observable<String> call(Boolean aBoolean) {
                return Observable.just(filePath);
            }
        });
    }

    @Override
    public Observable<OKCard> getOCard() {
        return client.requestAsyncForData(GET_OCARD, RequestData.newBuilder().build(), OKCard.class);
    }

    @Override
    public Observable<Boolean> authenticateToHandleOCard(int creditType, @NonNull String realName, @NonNull String idCardNo) {
        return client.requestAsyncForBoolean(AUTHENTICATE, RequestData.newBuilder()
                .creditType(creditType)
                .idCardNo(idCardNo)
                .realName(realName).build());
    }

    @Override
    public Observable<OKCard> attachDevice(@DeviceFlavor int deviceType, @NonNull String deviceNo, @NonNull String bhtName, @NonNull String bhtAddress) {
        return client.requestAsyncForData(BIND_OCARD, RequestData.newBuilder()
                .deviceType(deviceType)
                .deviceNo(deviceNo)
                .bhtName(bhtName)
                .bhtAddress(bhtAddress).build(), OKCard.class);
    }

    @Override
    public Observable<OKCard> lossOCard(@DeviceFlavor int deviceType, @NonNull String deviceNo) {
        return client.requestAsyncForData(LOSS_OCARD, RequestData.newBuilder()
                .deviceType(deviceType)
                .deviceNo(deviceNo).build(), OKCard.class);
    }

    @Override
    public Observable<OKCard> resumeOCard(@DeviceFlavor int deviceType, @NonNull String deviceNo) {
        return client.requestAsyncForData(RESUME_OCARD, RequestData.newBuilder()
                .deviceType(deviceType)
                .deviceNo(deviceNo).build(), OKCard.class);
    }

    @Override
    public Observable<OKCard> makeupOCard(@DeviceFlavor int deviceType, @NonNull String deviceNo, String bhtName, String bhtAddress) {
        return client.requestAsyncForData(MAKEUP_OCARD, RequestData.newBuilder()
                .deviceType(deviceType)
                .deviceNo(deviceNo)
                .bhtName(bhtName)
                .bhtAddress(bhtAddress).build(), OKCard.class);
    }

    /**
     * 获取开卡条件
     *
     * @param cardMainType 卡片主类型
     * @param aid          appletId
     * @return
     */
    @Override
    public Observable<CardCondition> getCardOpenCondition(int cardMainType, String aid) {
        return client.requestAsyncForData(PRE_CONDITION_TO_OPEN, RequestData.newBuilder()
                .cardMainType(cardMainType)
                .aid(aid).build(), CardCondition.class);
    }

    /**
     * 同步卡片
     *
     * @param cardNo  卡号
     * @param aid     appletId
     * @param orderNo 订单号
     * @return
     */
    @Override
    public Observable<CardEntity> syncCard(String cardNo, String aid, String orderNo) {
        return client.requestAsyncForData(SYNC_OR_CREATE_CARD, RequestData.newBuilder()
                .cardNo(cardNo)
                .aid(aid)
                .orderNo(orderNo).build(), CardEntity.class);
    }

    /**
     * C2C收款
     *
     * @param amount     转账金额
     * @param targetOlNo 目标欧乐号
     * @param tipsId
     * @return
     */
    @Override
    public Observable<Boolean> c2cCash(int amount, String targetOlNo, int tipsId) {
        return client.requestAsyncForBoolean(C2C_CASH, RequestData.newBuilder()
                .amount(amount)
                .tipsId(tipsId)
                .destOlNo(targetOlNo).build());
    }

    @Override
    public void setJPushRegistrationId(final String olNo, final String registrationId) {
        WorkerThreadScheduler.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                boolean success = client.requestForBoolean(SET_JPUSH_ID, RequestData.newBuilder()
                        .olNo(olNo)
                        .sysBelong(1)
                        .jPushId(registrationId).build());
                Timber.tag("JIGUANG").w("%s to set jpushId[%s]", success, registrationId);
            }
        });

    }

    @Override
    public Observable<Cert> getCA() {
        return client.requestAsyncForData(GET_CA, RequestData.newBuilder()
                .build(), Cert.class);
    }

    /**
     * 创建收付款业务的订单号
     *
     * @param cardMainType 卡片主类型
     * @param cardNo       卡号
     * @param amount       交易金额
     * @return
     */
    @Override
    public Observable<String> createPaymentsOrder(int cardMainType, String cardNo, int amount) {
        return client.<PaymentsOrder>requestAsyncForData(CREATE_PAYMENT_ORDER, RequestData.newBuilder()
                .cardMainType(cardMainType)
                .cardNo(cardNo)
                .amount(amount).build(), PaymentsOrder.class)
                .map(new Func1<PaymentsOrder, String>() {
                    @Override
                    public String call(PaymentsOrder paymentsOrder) {
                        return paymentsOrder.getOrderNo();
                    }
                });
    }

    /**
     * 查询收付款订单
     *
     * @param orderNo 订单号
     * @return
     */
    @Override
    public Observable<PaymentsOrder> queryPaymentOrder(String orderNo) {
        return client.requestAsyncForData(QUERY_PAYMENT_ORDER, RequestData.newBuilder()
                .orderNo(orderNo).build(), PaymentsOrder.class);
    }

    /**
     * 收付款支付
     *
     * @param cardMainType 卡片主类型
     * @param cardNo       支付卡号
     * @param orderNo      支付订单号
     * @return
     */
    @Override
    public Observable<Boolean> payPayment(int cardMainType, String cardNo, String orderNo) {
        return client.requestAsyncForBoolean(PAYMENT_TO_PAY, RequestData.newBuilder()
                .cardMainType(cardMainType)
                .cardNo(cardNo)
                .orderNo(orderNo).build());
    }


    /**
     * 获取历史转账记录卡列表
     *
     * @param friendOlNo 接受方欧乐号
     * @return
     */
    public Observable<List<CardEntity>> queryTransferCardList(String friendOlNo) {
        return client.requestAsyncForData(QUERY_TRANSFER_LIST, RequestData.newBuilder()
                .friendOlNo(friendOlNo).build(), new TypeToken<List<CardEntity>>() {
        }.getType());
    }

    @Override
    public Observable<Integer> setTransferTips(String cardNo, final String friendOlNo, String friendCardNo, int tipsId) {
        return client.<TransferProtocol>requestAsyncForData(REQUEST_TRANSFER_LIST, RequestData.newBuilder()
                .cardNo(cardNo)
                .friendOlNo(friendOlNo)
                .tipsId(tipsId)
                .friendCardNo(friendCardNo).build(), TransferProtocol.class).map(new Func1<TransferProtocol, Integer>() {
            @Override
            public Integer call(TransferProtocol transferProtocol) {
                return transferProtocol.getTipsId();
            }
        });
    }

    @Override
    public Observable<TransferProtocol> queryTransferTips(String friendOlNo, int tipsId) {
        return client.<TransferProtocol>requestAsyncForData(RESPONSE_TRANSFER, RequestData.newBuilder()
                .friendOlNo(friendOlNo).build(), TransferProtocol.class).repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
            @Override
            public Observable<?> call(Observable<? extends Void> observable) {
                return Observable.timer(5, TimeUnit.SECONDS);
            }
        });
    }

    @Override
    public Observable<List<TransferProtocol>> getTransferHistory(String friendOlNo) {
        return client.requestAsyncForData(GET_TRANSFER_HISTORY,
                RequestData.newBuilder()
                        .friendOlNo(friendOlNo)
                        .build(), new TypeToken<List<TransferProtocol>>() {
                }.getType());
    }
}
