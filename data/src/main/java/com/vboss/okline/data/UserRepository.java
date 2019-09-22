package com.vboss.okline.data;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.telephony.SmsManager;
import android.text.TextUtils;

import com.cosw.sdkblecard.DeviceInfo;
import com.google.common.base.Preconditions;
import com.okline.vboss.http.OLException;
import com.vboss.okline.data.entities.AddableBankInfo;
import com.vboss.okline.data.entities.CardCondition;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.data.entities.Cert;
import com.vboss.okline.data.entities.OKCard;
import com.vboss.okline.data.entities.PaymentsOrder;
import com.vboss.okline.data.entities.TransferProtocol;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.data.local.AppConfig;
import com.vboss.okline.data.local.DBService;
import com.vboss.okline.data.local.SPUtils;
import com.vboss.okline.data.local.UserLocalDataSource;
import com.vboss.okline.data.remote.AliOSSClient;
import com.vboss.okline.data.remote.UserRemoteDataSource;
import com.vboss.okline.tsm.OCardService;
import com.vboss.okline.tsm.TsmCardFlavor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/1 <br/>
 * Summary : 用户数据操作入口
 */
public class UserRepository implements UserDataSource {
    private static final String TAG = UserRepository.class.getSimpleName();
    private UserRemoteDataSource remote;
    private UserLocalDataSource local;
    private AliOSSClient ossClient;
    private DBService dbService;
    private static UserRepository instance;
    private final OCardService oCardService;

    public static UserRepository getInstance(Context context) {
        if (instance == null) {
            instance = new UserRepository(context);
        }
        return instance;
    }

    private UserRepository(Context context) {
        remote = UserRemoteDataSource.getInstance();
        local = UserLocalDataSource.getInstance();
        ossClient = AliOSSClient.getInstance(context);
        dbService = DBService.getInstance(context);
        oCardService = OCardService.getInstance(context);
    }

    /**
     * 获取本地用户
     */
    public User getUser() {
        return local.getUser();
    }

    /**
     * 获取欧乐号。
     *
     * @return 如果用户已注册，返回新注册的olno；否则返回默认的欧乐号。
     */
    public String getOlNo() {
        return local.getOlNo();
    }

    /**
     * 用户注册
     *
     * @param context
     * @param idCardNo   身份证号码
     * @param realName   身份证姓名
     * @param creditType 证件类型
     * @param imei       手机IMEI号
     * @param imsi       SIM卡IMSI号
     * @return
     */
    @Override
    public Observable<User> registerUser(final Context context, @NonNull String idCardNo, @NonNull String realName, int creditType, @NonNull final String imei, @NonNull final String imsi) {
        return remote.registerUser(null, idCardNo, realName, creditType, imei, imsi)
                .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Throwable> observable) {
                        return retryToSendSms(observable, context, imsi);
                    }
                }).map(new Func1<User, User>() {
                    @Override
                    public User call(User user) {
                        Timber.tag(TAG).i("++++++++> Fresh user : %s", user);
                        refresh(context, user);
                        return user;
                    }
                });
    }

    /**
     * 重置或注册用户。
     *
     * @param context    上下文
     * @param creditType 证件类型
     * @param idCardNo   证件号
     * @param realName   用户姓名
     * @param imei       手机imei号
     * @param imsi       sim卡imsi号
     * @return 返回用户对象
     */
    public Observable<User> resetOrRegisterUser(final Context context, int creditType, String idCardNo, String realName, String imei, String imsi) {
        return remote.resetOrRegisterUser(creditType, idCardNo, realName, imei, imsi).doOnNext(new Action1<User>() {
            @Override
            public void call(User user) {
                refresh(context, user);
            }
        });

    }

    /**
     * 用户登陆， 如果短信发送失败，重试3次。
     *
     * @param context 上下文
     * @param imei    手机imei号
     * @param imsi    SIM卡imsi号
     * @return
     */
    public Observable<User> login(final Context context, String imei, final String imsi) {
        return remote.loginUser(imei, imsi).retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
            @Override
            public Observable<?> call(Observable<? extends Throwable> error) {
                return retryToSendSms(error, context, imsi);
            }
        }).doOnNext(new Action1<User>() {
            @Override
            public void call(User user) {
                refresh(context, user);
            }
        });
    }

    /**
     * 重新发送短信以获取手机号码.
     * <p>
     * 如果需要重发，则每隔20秒发送一次短信；如果不需要，则直接返回{@code error} Observable.
     *
     * @param error   服务器返回的Exception Observable对象
     * @param context 上下文
     * @param imsi    手机imsi号
     * @return 返回一个无意义的数据
     */
    private Observable<?> retryToSendSms(Observable<? extends Throwable> error, final Context context, final String imsi) {
        return error.zipWith(Observable.range(1, 3), new Func2<Throwable, Integer, Object>() {
            @Override
            public Object call(Throwable throwable, Integer count) {
                //Fail to parse phone number on server and retry.
                boolean shouldRetry = (throwable instanceof OLException) && ((OLException) throwable).getCode() == 2000
                        && count != 3;
                if (shouldRetry) {
                    return count;
                } else {
                    return throwable;
                }
            }
        }).flatMap(new Func1<Object, Observable<?>>() {
            @Override
            public Observable<?> call(final Object object) {
                if (object instanceof Integer) {
                    return Observable.timer(15, TimeUnit.SECONDS).doOnNext(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            Timber.tag(TAG).w(" ===> Retry to send sms at %d time(s) ", (Integer) object);
                            sendASms(context, imsi);
                        }
                    });
                } else {
                    return Observable.error((Throwable) object);
                }
            }
        });
    }

    private void sendASms(Context context, String imsi) {
        //modify by 2017-06-05,11:48 luoxiuxiu 本地获取发送短信的手机
        String sendPhone = SPUtils.getSp((Activity) context, AppConfig.SP_PHONE_SMS);
        Timber.tag(TAG).d("sendSES : msg " + sendPhone);
        Timber.i("发送短信内容：" + imsi);
        SmsManager.getDefault().sendTextMessage(sendPhone, null, imsi, null, null);

    }

    /**
     * 获取接收短信的手机号码
     */
    public Observable<String> getCellPhoneForSms() {
        return remote.getCellPhoneForSms();
    }

    /**
     * 保存备用电话号码
     *
     * @param phoneList 电话号码数组
     * @return
     */
    public Observable<Boolean> saveSecondPhone(@Nullable String friendPhone, List<String> phoneList) {
        return remote.saveUserInfo(friendPhone, 1, GsonUtils.toJson(phoneList));
    }

    /**
     * 保存工作名片信息
     *
     * @param card 工作名片对象
     * @return
     */
    public Observable<Boolean> saveVisitingCard(@Nullable String friendPhone, @NonNull User.VisitingCard card) {
        Preconditions.checkNotNull(card);
        return remote.saveUserInfo(friendPhone, 2, GsonUtils.toJson(card));
    }

    /**
     * 保存快递地址
     *
     * @param list 快递地址列表
     * @return 返回保存成功与否
     */
    public Observable<Boolean> saveExpress(@Nullable String friendPhone, List<String> list) {
        Preconditions.checkNotNull(list);
        return remote.saveUserInfo(friendPhone, 4, GsonUtils.toJson(list));
    }

    /**
     * 保存银行账户信息
     *
     * @param accountList 账户信息列表
     * @return
     */
    public Observable<Boolean> saveBankInfo(@Nullable String friendPhone, @NonNull List<User.BankAccount> accountList) {
        Preconditions.checkNotNull(accountList);
        return remote.saveUserInfo(friendPhone, 3, GsonUtils.toJson(accountList));
    }

    /**
     * 获取用户自己的个人信息
     */
    public Observable<User> getSelfInfo() {
        return getOKLineMember(null).doOnNext(new Action1<User>() {
            @Override
            public void call(User user) {
                // TODO: 2017/6/20  cache user's express , bankAccountInfo and VisitingCard.
            }
        });
    }

    /**
     * 获取欧乐会员资料。 返回realName、phone、imgUrl、userNo。
     *
     * @param keyOfSearch 欧乐号
     * @return
     */
    @Override
    public Observable<User> getOKLineMember(String keyOfSearch) {
        return remote.getOKLineMember(keyOfSearch);
    }

    /**
     * 获取联系人名片详情
     *
     * @param keyOfSearch 手机号码或者欧乐号
     * @return
     */
    public Observable<User> getContactVisitingCard(String keyOfSearch) {
        return remote.getContactVisitingCard(keyOfSearch);
    }

    /**
     * 获取可添加的银行列表；
     */
    public Observable<List<AddableBankInfo>> getAddableBankList() {
        return remote.getAddableBankList(CardType.BANK_CARD);
    }

    /**
     * 根据卡号获取卡片所属商户名称
     *
     * @param cardNo 卡号获取卡号前六位
     * @return 返回包含商户民merName和卡号长度cardLen的{@code AddableBankInfo}实例。
     */
    public Observable<AddableBankInfo> queryBankMerchantName(@NonNull String cardNo) {
        Preconditions.checkNotNull(cardNo);
        return remote.queryBankMerchantName(cardNo);
    }

    /**
     * 用户数据初始化
     *
     * @param context 上下文
     * @param user    新注册的用户
     */
    private void refresh(Context context, User user) {
        if (!user.getOlNo().equals(local.getOlNo())) {
            //delete previous user's tables
            DBService.getInstance(context).deleteData();
        }
        local.setUser(user);
        local.setOlNo(user.getOlNo());
    }

    /**
     * 更新用户头像
     *
     * @param filePath 头像文件的本地Uri
     * @return
     */
    @Override
    public Observable<String> updateAvatar(@NonNull final String filePath) {
//        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1)+"_"+ System.currentTimeMillis();
        String fileName = UserLocalDataSource.getInstance().getOlNo();
        if (TextUtils.isEmpty(fileName)) {
            fileName = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date(System.currentTimeMillis()));
        }
        return ossClient.uploadAvatar(fileName, filePath).flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String avatarUrl) {
                Timber.tag(TAG).i("Uploaded File url : %s", avatarUrl);
                local.updateAvatar(avatarUrl);
                return remote.updateAvatar(avatarUrl);
            }
        }).subscribeOn(Schedulers.io());
    }


    /**
     * 获取绑定的欧卡
     */
    @Override
    public Observable<OKCard> getOCard() {

        return Observable.fromCallable(new Callable<OKCard>() {
            @Override
            public OKCard call() throws Exception {
                return local.getOCard();
            }
        }).flatMap(new Func1<OKCard, Observable<OKCard>>() {
            @Override
            public Observable<OKCard> call(OKCard okCard) {
                if (okCard != null) {
                    return Observable.just(okCard);
                } else {
                    return remote.getOCard().doOnNext(new Action1<OKCard>() {
                        @Override
                        public void call(OKCard okCard) {
                            local.setOKCard(okCard);
                        }
                    });
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 获取蓝牙信息。
     * <p>
     * 蓝牙连接成功后，返回连接的设备信息，否则返回null。
     */
    public DeviceInfo getBleDeviceInfo() {
        return oCardService.getBleDeviceInfo();
    }

    /**
     * 操作欧卡前的验证操作
     *
     * @param creditType 证件类型
     * @param realName   真实姓名
     * @param idCardNo   证件号
     * @return
     */
    @Override
    public Observable<Boolean> authenticateToHandleOCard(int creditType, @NonNull String realName, @NonNull String idCardNo) {
        return remote.authenticateToHandleOCard(creditType, realName, idCardNo);
    }

    /**
     * 扫描BLE设备
     *
     * @param duration 扫描时长
     * @param filter   用来过滤符合条件的BLE设备。过滤方式为：蓝牙地址包含filter或者蓝牙名称包含filter，如果为空则不过滤。
     * @return 返回扫描到的且符合条件的BLE设备。
     */
    public Observable<DeviceInfo> scanOCard(int duration, @Nullable final String filter) {
        return oCardService.scanOCard(duration).filter(new Func1<DeviceInfo, Boolean>() {
            @Override
            public Boolean call(DeviceInfo deviceInfo) {
                if (TextUtils.isEmpty(filter)) {
                    return true;
                } else {
                    String devName = deviceInfo.getDeviceName();
                    String devAddr = deviceInfo.getDeviceMac();
                    return devAddr.contains(filter) || (devName != null && devName.contains(filter));
                }

            }
        }).subscribeOn(Schedulers.computation());
    }

    /**
     * 检测欧卡连接状态
     */
    public Observable<Boolean> monitorOCardConnection() {
        return oCardService.getConnection().map(new Func1<OCardService.Connection, Boolean>() {
            @Override
            public Boolean call(OCardService.Connection connection) {
                return connection.isConnected();
            }
        });
    }

    /**
     * 监听欧卡电量变化。
     *
     * @param intervalInMs 查询的间隔时间
     * @return 在指定的时间间隔内查询电量，并返回变化的值。如果返回0，表示蓝牙未连接。
     */
    public Observable<Integer> monitorOCardBattery(final long intervalInMs) {

        Preconditions.checkArgument(intervalInMs > 0, "Parameter intervalInMs must be greater than 0");

        return Observable.interval(intervalInMs, TimeUnit.MILLISECONDS).map(new Func1<Long, Integer>() {
            @Override
            public Integer call(Long aLong) {
                return oCardService.getBatteryPercent();
            }
        }).distinctUntilChanged();
    }

    /**
     * 监听欧卡电压状态
     *
     * @param intervalInMs 监听的间隔时间
     * @return -
     * 3 电压正常； <br/>
     * 2 电压低告警状态,此时读卡器还可以读卡几 十次,黄灯亮； <br/>
     * 1 电压极低状态,此时读卡器不能读卡,而且会关机；<br/>
     * -1
     * -2 蓝牙未连接
     */
    public Observable<Integer> monitorOCadVoltage(long intervalInMs) {
        Preconditions.checkArgument(intervalInMs > 0, "Parameter intervalInMs must be greater than 0");

        return Observable.interval(intervalInMs, TimeUnit.MILLISECONDS).map(new Func1<Long, Integer>() {
            @Override
            public Integer call(Long aLong) {
                return (int) oCardService.getVoltageState();
            }
        }).distinctUntilChanged();
    }

    /**
     * 检测欧卡。默认在Computation线程中执行。
     *
     * @param deviceId 设备id
     * @param timeout  超时时间。单位毫秒。
     * @return 返回对应deviceId的设备信息
     */

    @RequiresPermission(anyOf = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION})
    public Observable<DeviceInfo> detectOCard(final String deviceId, int timeout) {
        return oCardService.scanOCard(timeout).filter(new Func1<DeviceInfo, Boolean>() {
            @Override
            public Boolean call(DeviceInfo deviceInfo) {
                //取蓝牙mac地址后6位作为deviceId。
                String mac = deviceInfo.getDeviceMac().replaceAll(":", "");
                String scannedDeviceId = mac.substring(mac.length() - 6);
                Timber.tag(TAG).i("Scanned device Id : %s", scannedDeviceId);

                return scannedDeviceId.equalsIgnoreCase(deviceId);
            }
        }).subscribeOn(Schedulers.computation());
    }

    /**
     * 绑定蓝牙卡.绑定成功后，保存设备信息到本地。
     *
     * @param context    上下文
     * @param deviceType 蓝牙设备类型
     * @param btName     设备名称
     * @param btAddress  设备蓝牙地址
     * @param btDeviceId 设备Id
     * @param timeout    蓝牙连接超时时间
     * @return 绑定的欧卡
     */
    public Observable<OKCard> bindBTCard(final Context context, final int deviceType, final String btName, @NonNull final String btAddress, final String btDeviceId, int timeout) {

        //If connected , disconnect it.
        if (oCardService.isConnected()) {
            oCardService.disconnectDevice();
        }
//Modified by Shihaijun on 2017-06-12 : 修改欧卡绑定逻辑 start
        return oCardService.connectAndInitialize(context, btAddress, timeout).flatMap(new Func1<Boolean, Observable<OKCard>>() {
            @Override
            public Observable<OKCard> call(Boolean success) {
                //Bind device to server
                return attachDevice(deviceType, btDeviceId, btName, btAddress).doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        //Disconnect if fail to bind device to server.
                        oCardService.disconnectDevice();
                    }
                });
            }
        });
//Modified by Shihaijun on 2017-06-12 : 修改欧卡绑定逻辑 end
    }

    /**
     * 释放欧卡资源
     */
    public void releaseOCard() {
        oCardService.destroy();
    }

    /**
     * 向服务器绑定欧卡
     *
     * @param deviceType 设备类型
     * @param deviceNo   设备号
     * @param bhtName    设备蓝牙名称
     * @param bhtAddress 设备蓝牙地址
     * @return 返回绑定的欧卡对象
     */
    @Override
    public Observable<OKCard> attachDevice(@DeviceFlavor int deviceType, @NonNull final String deviceNo, @NonNull final String bhtName, @NonNull final String bhtAddress) {
        return remote.attachDevice(deviceType, deviceNo, bhtName, bhtAddress).doOnNext(new Action1<OKCard>() {
            @Override
            public void call(OKCard okCard) {
                //cache device info to local
                local.setDeviceInfo(deviceNo, bhtName, bhtAddress);
                //Set deviceInfo to OKCard object.
                okCard.setInfo(oCardService.getBleDeviceInfo());
                //Cache OKCard
                local.setOKCard(okCard);
            }
        });
    }

    /**
     * 挂失欧卡
     *
     * @param deviceType 设备类型
     * @param deviceNo   设备号
     * @return
     */
    @Override
    public Observable<OKCard> lossOCard(@DeviceFlavor int deviceType, @NonNull String deviceNo) {
        return remote.lossOCard(deviceType, deviceNo);
    }

    /**
     * 重启欧卡
     *
     * @param deviceType 设备类型
     * @param deviceNo   设备号
     * @return
     */
    @Override
    public Observable<OKCard> resumeOCard(@DeviceFlavor int deviceType, @NonNull String deviceNo) {
        return remote.resumeOCard(deviceType, deviceNo);
    }

    /**
     * 补新欧卡
     *
     * @param deviceType 设备类型
     * @param deviceNo   设备号
     * @param bhtName    设备的蓝牙名称
     * @param bhtAddress 设备的蓝牙地址
     * @return
     */
    @Override
    public Observable<OKCard> makeupOCard(@DeviceFlavor int deviceType, @NonNull final String deviceNo, @NonNull final String bhtName, @NonNull final String bhtAddress) {
        return remote.makeupOCard(deviceType, deviceNo, bhtName, bhtAddress).doOnNext(new Action1<OKCard>() {
            @Override
            public void call(OKCard okCard) {
//                local.setDeviceInfo(deviceNo, bhtName, bhtAddress);
            }
        });
    }

    /**
     * 获取开卡条件
     *
     * @param cardMainType 卡片类型
     * @param aid          该类型卡片应用id
     * @return
     */
    @Override
    public Observable<CardCondition> getCardOpenCondition(int cardMainType, String aid) {
        return remote.getCardOpenCondition(cardMainType, aid);
    }

    /**
     * 安装卡应用，即开卡。
     *
     * @param context
     * @param appAid  卡应用aid
     * @param orderNo 开卡支付订单号
     * @return
     */
    public Observable<CardEntity> installCardApp(Context context, @TsmCardFlavor final int tsmCardType, final String appAid, final String orderNo) {
        final long startAt = System.currentTimeMillis();
        return checkBluetoothState(context).concatMap(new Func1<Boolean, Observable<? extends Boolean>>() {
            @Override
            public Observable<? extends Boolean> call(Boolean aBoolean) {
                return oCardService.appDelete(appAid);
            }
        }).concatMap(new Func1<Boolean, Observable<CardEntity>>() {
            @Override
            public Observable<CardEntity> call(Boolean notInstalled) {
                return oCardService.appInstall(tsmCardType, appAid)
                        .doOnNext(new Action1<String>() {
                            @Override
                            public void call(String cardNo) {
                                Timber.tag(TAG).i("Consume %s 秒 to install card app[%s]", (System.currentTimeMillis() - startAt) / 1000, appAid);
                            }
                        }).concatMap(new Func1<String, Observable<CardEntity>>() {
                            @Override
                            public Observable<CardEntity> call(String cardNo) {
                                Timber.tag(TAG).i(" Success to open card which number is %s。", cardNo);
                                return syncCard(cardNo, appAid, orderNo).doOnNext(new Action1<CardEntity>() {
                                    @Override
                                    public void call(CardEntity cardEntity) {
                                        Timber.tag(TAG).i("Success to sync card. CardNo is %s", cardEntity.cardNo());
                                    }
                                });
                            }
                        });
            }
        });
    }

    /**
     * 绑定欧卡之后，查看欧卡信息时调用。
     * <p>
     * 请求蓝牙连接，并返回连接设备信息。
     *
     * @param context 上下文
     * @param address 蓝牙地址
     * @param timeout 连接超时时间
     * @return
     */
    public Observable<DeviceInfo> requestOCardConnection(Context context, String address, int timeout) {
        return oCardService.connectOCard(context, address, timeout);
    }

    /**
     * 检查蓝牙连接状态，如果未连接，则重连。
     */
    private Observable<Boolean> checkBluetoothState(Context context) {
        User user = getUser();
        if (user == null) {
            return Observable.error(new Throwable(context.getString(R.string.user_not_registered)));
        } else {
            String address = user.getBhtAddress();
            return oCardService.connectAndInitialize(context, address, 20 * 1000);
        }
    }

    /**
     * 同步卡片
     *
     * @param cardNo  卡号
     * @param aid     卡片应用id
     * @param orderNo 订单号
     * @return
     */
    @Override
    public Observable<CardEntity> syncCard(String cardNo, String aid, String orderNo) {
        return remote.syncCard(cardNo, aid, orderNo).doOnNext(new Action1<CardEntity>() {
            @Override
            public void call(CardEntity entity) {
                int rowId = dbService.insert(CardEntity.TABLE_NAME, CardEntity.buildMarshal(entity).asContentValues());
                Timber.tag(TAG).i("Sync card , %s to insert card[%d]", (rowId != 0 ? "and success" : "but fail"), entity.cardId());
            }
        });
    }

    /**
     * 卡片充值
     *
     * @param context
     * @param tsmCardType Tsm卡片类型
     * @param appAid      卡应用aid
     * @param amount      充值金额，单位分。
     * @return 返回卡片余额
     */
    public Observable<String> rechargeCard(Context context, @TsmCardFlavor final int tsmCardType, final String appAid, final int amount) {
        return checkBluetoothState(context).flatMap(new Func1<Boolean, Observable<String>>() {
            @Override
            public Observable<String> call(Boolean aBoolean) {
                return oCardService.recharge2(tsmCardType, appAid, amount);
            }
        });
    }

    /**
     * C2C收款
     *
     * @param amount     金额
     * @param targetOlNo 目标用户欧乐好
     * @param tipsId
     * @return
     */
    @Override
    public Observable<Boolean> c2cCash(int amount, String targetOlNo, int tipsId) {
        return remote.c2cCash(amount, targetOlNo, tipsId);
    }

    /**
     * 设置Jpush RegistrationId
     *
     * @param olNo
     * @param registrationId id
     */
    @Override
    public void setJPushRegistrationId(String olNo, String registrationId) {
        remote.setJPushRegistrationId(olNo, registrationId);
    }

    /**
     * 获取历史交易卡列表
     *
     * @param friendOlNo 接收方欧乐号
     * @return
     */
    public Observable<List<CardEntity>> queryTransferCardList(String friendOlNo) {
        return remote.queryTransferCardList(friendOlNo);
    }

    /**
     * @param cardNo       己方卡号
     * @param friendOlNo   对方olno
     * @param friendCardNo 对方卡号
     * @param tipsId
     * @return
     */
    @Override
    public Observable<Integer> setTransferTips(String cardNo, String friendOlNo, String friendCardNo, int tipsId) {
        return remote.setTransferTips(cardNo, friendOlNo, friendCardNo, tipsId);
    }

    /**
     * @param friendOlNo 对方欧乐号
     * @param tipsId     提醒表主键
     * @return
     */
    @Override
    public Observable<TransferProtocol> queryTransferTips(String friendOlNo, int tipsId) {
        return remote.queryTransferTips(friendOlNo, tipsId);
    }

    /**
     * 获取跟某一好友的转账历史
     *
     * @param friendOlNo 好友的欧乐号
     * @return
     */
    @Override
    public Observable<List<TransferProtocol>> getTransferHistory(String friendOlNo) {
        return remote.getTransferHistory(friendOlNo);
    }


    @Override
    public Observable<Cert> getCA() {
        return remote.getCA();
    }

    /**
     * 创建收付款业务的订单号
     *
     * @param cardMainType 卡片主类型
     * @param cardNo       卡号
     * @param amount       交易金额
     * @return 返回交易订单号
     */
    @Override
    public Observable<String> createPaymentsOrder(int cardMainType, String cardNo, int amount) {
        return remote.createPaymentsOrder(cardMainType, cardNo, amount);
    }

    /**
     * 查询收付款订单
     *
     * @param orderNo 订单号
     * @return
     */
    @Override
    public Observable<PaymentsOrder> queryPaymentOrder(String orderNo) {
        return remote.queryPaymentOrder(orderNo);
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
        return remote.payPayment(cardMainType, cardNo, orderNo);
    }
}
