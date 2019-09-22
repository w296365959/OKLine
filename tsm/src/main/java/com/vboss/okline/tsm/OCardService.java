package com.vboss.okline.tsm;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cosw.android.sdk.TsmService;
import com.cosw.sdkblecard.BleDeviceStatusListener;
import com.cosw.sdkblecard.BleServiceProvider;
import com.cosw.sdkblecard.DeviceInfo;
import com.cosw.sdkblecard.PlkBleDeviceInfo;
import com.cosw.sdkblecard.PlkBleScanCallback;
import com.cosw.tsm.trans.protocol.vo.AppInfo;
import com.cosw.tsm.trans.protocol.vo.ClientInfo;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Locale;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;
import timber.log.Timber;

import static com.vboss.okline.tsm.TsmCardType.CARD_TYPE_PBOC;
import static com.vboss.okline.tsm.TsmCardType.CARD_TYPE_TRANSPORT;
import static com.vboss.okline.tsm.TsmCardType.CARD_TYPE_VIP;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/5/4 <br/>
 * Summary : 蓝牙卡服务
 */
public class OCardService implements BleCardInterface, TsmInterface {
    private static final String TAG = OCardService.class.getSimpleName();
    private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());
    /**
     * 状态：卡应用已安装。
     */
    private static final String APP_STATUS_INSTALLED = "已安装";
    private final BleServiceProvider bleService;
    private TsmService mTsmService;
    private static OCardService instance;
    private final Connection mConnection;
    private DeviceInfo mDeviceInfo;

    /**
     * 蓝牙连接状态
     */
    public class Connection {
        /**
         * 连接状态码
         * <p>
         * 0：已连接
         * 1：连接失败
         * 2：连接断开
         * 3：连接超时
         */
        int state = 2;
        /**
         * 状态描述
         */
        String message = "连接断开";

        public boolean isConnected() {
            return state == 0;
        }

        public String getMessage() {
            return this.message;
        }

        @Override
        public String toString() {
            return "Connection{" +
                    "state=" + state +
                    ", message='" + message + '\'' +
                    '}';
        }
    }

    public static OCardService getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (OCardService.class) {
                OCardService service = instance;
                if (service == null) {
                    service = new OCardService(context.getApplicationContext());
                }
                instance = service;
            }
        }
        return instance;
    }

    private OCardService(Context context) {
        bleService = BleServiceProvider.getInstance(context);
        mConnection = new Connection();
        bleService.registBleDeviceStatusListener(new BleDeviceStatusListener() {
            @Override
            public void connStatus(int state, String message) {
                Timber.tag(TAG).i("Connection CallBack:  state = %d , message = %s", state, message);
                mConnection.state = state;
                mConnection.message = message;

                //Added by shihaijun on 2017-07-04 : 蓝牙断开后mDeviceInfo置空 start
                if (!mConnection.isConnected()) {
                    setBleDeviceInfo(null);
                }
                //Added by shihaijun on 2017-07-04 : 蓝牙断开后mDeviceInfo置空 end
                if (_bus.hasObservers()) {
                    WorkerThreadScheduler.getInstance().execute(new Runnable() {
                        @Override
                        public void run() {
                            Timber.tag(TAG).w("====Emit bluetooth connection state on %s", Thread.currentThread().getName());
                            _bus.onNext(mConnection);
                        }
                    });
                }
            }
        });
    }

    /**
     * @return 获取蓝牙连接状态
     */
    public Observable<Connection> getConnection() {
        return _bus.ofType(Connection.class).share();
    }

    /**
     * 连接欧卡，并初始化TSM
     *
     * @param address 欧卡蓝牙地址
     * @param timeout 连接超时时间，单位毫秒
     * @return
     */
    public Observable<Boolean> connectAndInitialize(final Context context, final String address, final int timeout) {
        return connectOCard(context, address, timeout).flatMap(new Func1<DeviceInfo, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(final DeviceInfo deviceInfo) {
                return instantiateTsm(context);
            }
        });
    }

    /**
     * 实例化Tsm服务。若实例化失败，则断开蓝牙。
     *
     * @param context 上下文
     * @return 返回Tsm服务实例。
     */
    private Observable<Boolean> instantiateTsm(final Context context) {
        if (mTsmService != null) {
            return Observable.just(true);
        } else {
            return Observable.create(new Observable.OnSubscribe<Boolean>() {
                @Override
                public void call(final Subscriber<? super Boolean> subscriber) {
                    if (subscriber.isUnsubscribed()) return;
//Modified by Shihaijun on 2017/6/7 for 传入application的Context

                    Timber.tag(TAG).w("==Instantiate TsmService on %s==", Thread.currentThread().getName());
                    new TsmService(context.getApplicationContext(), TsmService.SeTypeEnum.BLE, new TsmService.IServiceConnected() {
                        //End of modification
                        @Override
                        public void onServiceConnected(TsmService service, ClientInfo clientInfo) {
                            Timber.tag(TAG).w("====Success to instantiate TsmService====");
                            mTsmService = service;
                            subscriber.onNext(true);
                            subscriber.onCompleted();
                        }

                        @Override
                        public void exceptionCaught(int i, String message, Throwable throwable) {
                            Timber.tag(TAG).e("Fail to instantiate TsmService , state = %d, message = %s", i, message);
                            disconnectDevice();
                            subscriber.onError(new TsmException(i, message));
                        }
                    });
                }
            }).subscribeOn(AndroidSchedulers.mainThread());
        }

    }

    /**
     * 连接欧卡，并返回欧卡设备信息。
     * <p>
     * 默认在Computation线程执行。
     *
     * @param address 设备蓝牙地址
     * @param timeout 连接超时间，单位毫秒。
     * @return 返回欧卡设备信息， 有可能为空。
     */
    public Observable<DeviceInfo> connectOCard(final Context context, final String address, final int timeout) {
        Timber.tag(TAG).i("connectOCard : mac = %s, timeout = %d", address, timeout);
        if (!BluetoothAdapter.checkBluetoothAddress(address)) {
            return Observable.error(new TsmException("Invalid bluetooth address"));
        }

        return getConnection().concatMap(new Func1<Connection, Observable<? extends DeviceInfo>>() {
            @Override
            public Observable<? extends DeviceInfo> call(Connection connection) {
                Timber.i("!! Handle connection result !!");
                if (connection.isConnected()) {
                    DeviceInfo device = getBleDeviceInfo();
                    if (device != null) {
                        Timber.tag(TAG).i("====>Re-Use connected device[%s]", device.getDeviceMac());
                        return Observable.just(device);
                    }

                    //Added by shihaijun on 2017-06-27 : 蓝牙连接成功之后，休眠50ms再验证密码 start
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //Added by shihaijun on 2017-06-27 : 蓝牙连接成功之后，休眠50ms再验证密码 end
                    if (verifyPin(context, address, timeout)) {
                        //Modified by shihaijun on 2017-07-04 : 蓝牙连接成功后，设置mDeviceInfo。 start
                        device = bleService.getBleDeviceInfo();
                        Timber.tag(TAG).i("====>Cache connected device[%s]", device.getDeviceMac());
                        setBleDeviceInfo(device);
                        //Modified by shihaijun on 2017-07-04 : 蓝牙连接成功后，设置mDeviceInfo。 end
                        return Observable.just(device);
                    } else {
                        disconnectDevice();
                        return Observable.error(new Exception("Fail to set or pair pin"));
                    }
                } else {
                    return Observable.error(new TsmException(connection.state, connection.message));
                }
            }
        }).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                WorkerThreadScheduler.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        //Sleep to make sure finished subscription.
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        DeviceInfo device = new DeviceInfo();
                        device.setDeviceMac(address);
                        bleService.connectDevice(device, timeout);
                    }
                });


            }
        });
    }

    /**
     * 连接蓝牙卡,验证密码，并初始化TsmService 。
     * <p>
     * 若验密码或实例化Tsm，则断开蓝牙连接。
     *
     * @param context
     * @param address 欧卡蓝牙地址
     * @param timeout 连接超时时间，单位毫秒
     * @return 蓝牙连接成功与否
     * @throws TsmException 有连接失败、超时等异常。
     * @see TsmException#BLE_CONNECTION_DISCONNECT
     * @see TsmException#BLE_CONNECTION_FAILURE
     * @see TsmException#BLE_CONNECTION_TIMEOUT
     */
    @Override
    public Observable<Boolean> connectOCardAndInstantiateTsm(final Context context, @NonNull final String address, final int timeout) {
        if (!BluetoothAdapter.checkBluetoothAddress(address)) {
            return Observable.error(new Throwable(context.getString(R.string.invalid_bt_address)));
        } else {
//Modified by shihaijun on 2017-06-05 start
            return connectAndInitialize(context, address, timeout);
        }
//Modified by shihaijun on 2017-06-05 end
    }

    /**
     * 验证蓝牙密码。 目前，设置默认密码为123456。
     *
     * @param context 上下文
     * @param address 蓝牙地址
     * @param timeout 超时时间
     * @return 返回验证成功与否
     */
    private boolean verifyPin(Context context, String address, int timeout) {

        Timber.tag(TAG).w("====Verify Pin on %s  ", Thread.currentThread().getName());
        long startAt = System.currentTimeMillis();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String setPin = preferences.getString(address, null);
        Timber.tag(TAG).i("Cached pin : %s", setPin);
        boolean successToPin;
        if (TextUtils.isEmpty(setPin)) {
            successToPin = setPinToPair("123456", timeout);
            if (successToPin) {
                Timber.tag(TAG).w("Success to set pin! Consume %d (s)", (System.currentTimeMillis() - startAt) / 1000);
                preferences.edit().putString(address, "123456").apply();
                //验证Pin码成功后，将蓝牙卡设置为用不关机.
                if (setTimeToPowerOff((short) 0)) {
                    Timber.tag(TAG).w("Success to set to never power off");
                } else {
                    Timber.tag(TAG).w("Fail to set to never power off");
                }
            } else {
                Timber.tag(TAG).w("Fail to set pin!  Consume %d (s)", (System.currentTimeMillis() - startAt) / 1000);
            }
        } else {
            successToPin = pairByPin(setPin);
            Timber.tag(TAG).i("Pin had been cached, %s to pair by pin", successToPin);
        }

        return successToPin;
    }


    /**
     * 设置Pin
     *
     * @param pin     密码
     * @param timeout 设置超时。毫秒。
     * @return
     */
    @Override
    public boolean setPinToPair(String pin, int timeout) {
        return bleService.setPinToPair(pin, (short) (timeout / 1000));
    }

    /**
     * 配对Pin
     *
     * @param pin pin码
     * @return
     */
    @Override
    public boolean pairByPin(@NonNull String pin) {
        return bleService.pairByPin(pin);
    }

    /**
     * 扫描蓝牙卡。
     *
     * @param timeout 超时时间，单位毫秒。 >0，超时结束扫描；=0，手动结束扫描。
     * @return 返回扫描到的蓝牙设备
     * @throws TsmException 扫描失败异常
     * @see TsmException#BLE_SCANNING_ERROR
     */
    @Override
    public Observable<DeviceInfo> scanOCard(final int timeout) {
        return Observable.create(new Observable.OnSubscribe<DeviceInfo>() {
            @Override
            public void call(final Subscriber<? super DeviceInfo> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                bleService.scanBleDevice(new PlkBleScanCallback() {
                    @Override
                    public void onScannedWithDevice(PlkBleDeviceInfo plkBleDeviceInfo) {
                        DeviceInfo deviceInfo = new DeviceInfo();
                        deviceInfo.setDeviceMac(plkBleDeviceInfo.getDevice().getAddress());
                        deviceInfo.setDeviceName(plkBleDeviceInfo.getDevice().getName());
                        Timber.tag(TAG).i("Scanned device : %s", plkBleDeviceInfo.getDevice());
                        subscriber.onNext(deviceInfo);
                    }

                    @Override
                    public void onScannedFinish(ArrayList<PlkBleDeviceInfo> arrayList) {
                        subscriber.onCompleted();
                        Timber.tag(TAG).i("Scan finish");
                    }

                    @Override
                    public void onScannedFailed(String errMsg) {
                        Timber.tag(TAG).e("Scan error");
                        subscriber.onError(new TsmException(TsmException.BLE_SCANNING_ERROR, errMsg));
                    }
                }, timeout);
            }
        }).subscribeOn(Schedulers.computation());
    }

    @Override
    public boolean setTimeToPowerOff(short time) {
        return bleService.setTimeToPowerOff(time);
    }

    /**
     * 手动停止扫描
     */
    @Override
    public void stopScanning() {
        bleService.stopScanning();
    }

    /**
     * 断开蓝牙卡连接
     */
    @Override
    public void disconnectDevice() {
        if (bleService.queryDeviceState()) {
            Timber.tag(TAG).w(" >>>>>>>> Attempt to disconnect. %s", bleService);
            bleService.disconnectDevice();
        }
    }

    /**
     * 检测蓝牙连接状态。
     * <p>
     * PS：该接口状态不可靠。
     */
    @Override
    public boolean isConnected() {
        return bleService.queryDeviceState();
    }

    /**
     * 获取设备的产品序列号
     *
     * @param scanRecord
     * @return
     */
    @Override
    public String getProductId(byte[] scanRecord) {
        return bleService.getProductId(scanRecord);
    }

    /**
     * 释放蓝牙卡相关资源
     */
    @Override
    public void destroy() {
        Timber.tag(TAG).w("====Do destroy()====");
        if (mTsmService != null) {
            mTsmService.destroy();
        }
    }

    /**
     * 查询蓝牙卡电压状态。
     * <p>
     * 03 电压正常； <br/>
     * 02 电压低告警状态,此时读卡器还可以读卡几 十次,黄灯亮； <br/>
     * 01 电压极低状态,此时读卡器不能读卡,而且会关机；<br/>
     */
    @Override
    public byte getVoltageState() {
        return bleService.getValtageState();
    }

    /**
     * 查询已连接的蓝牙卡的充电状态。
     * <p>
     * 00 未在充电中；<br/>
     * 01 充电进行中；<br/>
     * 02 充电已完成（连着充电器，但充电完成）；<br/>
     * Others:查询失败
     *
     * @return
     */
    @Override
    public byte getChargeState() {
        return bleService.getChargeState();
    }

    /**
     * 获取已连接的蓝牙卡电量剩余百分比
     */
    @Override
    public int getBatteryPercent() {
        return bleService.getDeviceEnegyPercentage();
    }

    /**
     * Cache connected bluetooth device
     *
     * @param deviceInfo
     */
    private synchronized void setBleDeviceInfo(DeviceInfo deviceInfo) {
        this.mDeviceInfo = deviceInfo;
    }

    /**
     * 获取已连接的蓝牙卡设备信息
     */
    @Override
    public synchronized DeviceInfo getBleDeviceInfo() {
        return mDeviceInfo;
    }

    /**
     * 下载卡应用前需先像TSM平台注册。
     *
     * @param appAid   卡应用aid
     * @param name     姓名
     * @param phone    手机号码
     * @param idCardNo 身份证号
     * @param address  联系地址
     * @return
     */
    @Override
    public Observable<Boolean> appRegister(@NonNull final String appAid, @NonNull final String name, @Nullable final String phone, @Nullable final String idCardNo, @Nullable final String address) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(final Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                Looper.prepare();
                Handler handler = new BooleanHandler(subscriber, TsmException.TSM_REGISTER_ERROR);
                Looper.loop();
                mTsmService.uploadInfo(handler, name, idCardNo, phone, address, appAid);
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 安装卡应用。 失败重试三次。
     *
     * @param tsmCardType Tsm平台卡片类型
     * @param appAid      卡应用Aid
     * @return 返回卡应用的卡号
     */
    @Override
    public Observable<String> appInstall(final int tsmCardType, @NonNull final String appAid) {

        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(final Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                Timber.tag(TAG).i("Start to install %s", appAid);
                mTsmService.appInstall(appAid, new TsmService.IExecApdusListener() {
                    @Override
                    public void onSuccess() {
                        subscriber.onNext(true);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onFailure(int i, String s, Throwable throwable) {
                        subscriber.onError(new TsmException(i, s));
                    }
                });
            }
//Modified by shihaijun on 2017-0608 to remove retry action start
        }).map(new Func1<Boolean, String>() {
            @Override
            public String call(Boolean aBoolean) {
                try {
                    Timber.tag(TAG).i("Go to get cardNo");
                    return getCardNo(tsmCardType, appAid);
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
        });
//Modified by shihaijun on 2017-0608 to remove retry action end
    }

    /**
     * 获取卡号
     *
     * @param cardType Tsm平台卡类型
     * @param appAid   卡应用aid
     * @return 返回卡号
     * @throws Exception
     */
    public String getCardNo(@TsmCardFlavor int cardType, String appAid) throws Exception {
        String cardNo = null;
        switch (cardType) {
            case CARD_TYPE_PBOC:
                cardNo = mTsmService.getCardNoPboc(appAid);
                break;
            case CARD_TYPE_TRANSPORT:
                cardNo = mTsmService.getCardNoHz(appAid);
                break;
            case CARD_TYPE_VIP:
                cardNo = mTsmService.getCardNoMsShip(appAid);
                break;
            default:
                break;
        }
        return cardNo;
    }

    /**
     * 删除卡应用
     *
     * @param appAid 卡应用aid
     * @return 返回删除成功与否
     */
    @Override
    public Observable<Boolean> appDelete(@NonNull final String appAid) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(final Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                if (isAppInstall(appAid)) {
                    Timber.tag(TAG).w(" +++++++ %s had been installed. Delete it now ++++++++", appAid);
                    mTsmService.appDelete(appAid, new TsmService.IExecApdusListener() {
                        @Override
                        public void onSuccess() {
                            subscriber.onNext(true);
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onFailure(int i, String s, Throwable throwable) {
                            subscriber.onError(new TsmException(i, throwable));
                        }
                    });
                } else {
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                }

            }
        });
    }

    /**
     * 根据aid获取卡应用信息
     *
     * @param appAid 卡应用aid
     * @return
     */
    @Override
    public Observable<AppInfo> getAppInfo(@NonNull final String appAid) {

        return Observable.create(new Observable.OnSubscribe<AppInfo>() {
            @Override
            public void call(final Subscriber<? super AppInfo> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                mTsmService.getInfo(appAid, new TsmService.InfoListener() {
                    @Override
                    public void onSuccess(Object o) {
                        subscriber.onNext((AppInfo) o);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        subscriber.onError(throwable);
                    }
                });
            }
        }).subscribeOn(Schedulers.io());
    }


    /**
     * 卡应用是否已安装
     *
     * @param appAid 卡应用aid
     * @return
     */
    @Override
    public boolean isAppInstall(@NonNull String appAid) {
        try {
            AppInfo info = getAppInfo(appAid).toBlocking().single();
            if (info != null && APP_STATUS_INSTALLED.equals(info.getAppStatus())) {
                return mTsmService.isInstall(appAid);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 安装安全域
     *
     * @param sdAid 安全域aid
     * @return
     */
    @Override
    public Observable<Boolean> sdInstall(@NonNull String sdAid) {
        return null;
    }

    /**
     * 删除安全域
     *
     * @param sdAid 安全域aid
     * @return
     */
    @Override
    public Observable<Boolean> sdDelete(@NonNull String sdAid) {
        return null;
    }

    /**
     * 充值并查询余额。
     * <p>
     * 需在AndroidSchedulers.MainThread中订阅.
     *
     * @param tsmCardType Tsm平台卡类型
     * @param appAid      卡应用aid
     * @param amount      充值金额
     * @return
     */
    public Observable<String> recharge2(final int tsmCardType, final String appAid, int amount) {
        return recharge(tsmCardType, appAid, amount).concatMap(new Func1<Boolean, Observable<? extends String>>() {
            @Override
            public Observable<? extends String> call(Boolean aBoolean) {
                return getBalance(tsmCardType, appAid);
            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 查询余额. 需在主线程调用。
     *
     * @param tsmCardType tsm平台的卡应用类型
     * @param appAid      卡应用aid
     * @return
     */
    @Override
    public Observable<String> getBalance(@TsmCardFlavor final int tsmCardType, @NonNull final String appAid) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (subscriber.isUnsubscribed()) return;
//                Looper.prepare();
                Handler handler = new StringHandler(subscriber, TsmException.TSM_REGISTER_ERROR);
//                Looper.loop();
                if (TsmCardType.CARD_TYPE_TRANSPORT == tsmCardType) {
                    mTsmService.getBalanceHz(handler, appAid);
                } else if (TsmCardType.CARD_TYPE_VIP == tsmCardType) {
                    mTsmService.getBalanceMsShip(handler, appAid);
                } else {
                    subscriber.onError(new Throwable(String.format(Locale.getDefault(), "getBalance()#Unsupported TsmCardType[%d]", tsmCardType)));
                }
                Timber.tag(TAG).i(" doGetBalance() -> AppAid = %s", appAid);
            }
        });
    }

    /**
     * 卡片圈存。需在主线程调用。
     *
     * @param tsmCardType tsm平台的卡应用类型
     * @param appAid      卡应用aid
     * @param amount      充值金额
     * @return
     */
    @Override
    public Observable<Boolean> recharge(@TsmCardFlavor final int tsmCardType, final String appAid, final int amount) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                Timber.tag(TAG).i(" doRecharge -> AppAid = %s, amount = %d", appAid, amount);
                if (subscriber.isUnsubscribed()) return;
//                Looper.prepare();
                Handler handler = new BooleanHandler(subscriber, TsmException.TSM_REGISTER_ERROR);
//                Looper.loop();
                if (TsmCardType.CARD_TYPE_TRANSPORT == tsmCardType) {
                    mTsmService.doLoadHz(handler, appAid, amount);
                } else if (TsmCardType.CARD_TYPE_VIP == tsmCardType) {
                    mTsmService.doLoadMsShip(handler, appAid, amount);
                } else {
                    subscriber.onError(new TsmException(String.format(Locale.getDefault(), "recharge()#Unsupported TsmCardType[%d]", tsmCardType)));
                }
            }
        });
    }

    private static class BooleanHandler extends Handler {
        private SoftReference<Subscriber<? super Boolean>> weakSubscriber;
        private int errCode;

        BooleanHandler(Subscriber<? super Boolean> subscriber, int errorCode) {
            weakSubscriber = new SoftReference<Subscriber<? super Boolean>>(subscriber);
            this.errCode = errorCode;
        }

        @Override
        public void handleMessage(Message msg) {
            Timber.tag(TAG).i("HandleMessage : %s", msg.obj);
            Subscriber<? super Boolean> subscriber = weakSubscriber.get();
            if (subscriber != null) {
                if (msg.what == 0) {
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new TsmException(errCode, String.valueOf(msg.obj)));
                }
            }
        }
    }

    private static class StringHandler extends Handler {
        private SoftReference<Subscriber<? super String>> weakSubscriber;
        private int errCode;

        StringHandler(Subscriber<? super String> subscriber, int errorCode) {
            weakSubscriber = new SoftReference<Subscriber<? super String>>(subscriber);
            this.errCode = errorCode;
        }

        @Override
        public void handleMessage(Message msg) {
            Timber.tag(TAG).i("HandleMessage : %s", msg.obj.toString());
            Subscriber<? super String> subscriber = weakSubscriber.get();
            if (subscriber != null) {
                if (msg.what == 0) {
                    subscriber.onNext(String.valueOf(msg.obj));
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new TsmException(errCode, String.valueOf(msg.obj)));
                }
            }
        }
    }
}
