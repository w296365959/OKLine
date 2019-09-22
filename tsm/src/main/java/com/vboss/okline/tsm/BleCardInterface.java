package com.vboss.okline.tsm;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cosw.sdkblecard.DeviceInfo;

import rx.Observable;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/5/4 <br/>
 * Summary : 蓝牙卡操作接口
 */
interface BleCardInterface {



    /**
     * 连接欧卡
     *
     *
     * @param context
     * @param address 欧卡蓝牙地址
     * @param timeout 连接超时时间，单位毫秒
     * @return
     */
    Observable<Boolean> connectOCardAndInstantiateTsm(Context context, @NonNull String address, int timeout);

    /**
     * 设置配对密码。
     *
     * @param pin 密码
     * @param timeout   设置超时。毫秒。
     * @return
     */
    boolean setPinToPair(String pin, int timeout);

    /**
     * 请求密码配对
     *
     * @param pin pin码
     * @return
     */
    boolean pairByPin(@NonNull String pin);

    /**
     * 扫描蓝牙卡。
     *
     * @param timeout 超时时间，单位毫秒。 >0，超时结束扫描；=0，手动结束扫描。
     * @return
     */
    Observable<DeviceInfo> scanOCard(int timeout);

    /**
    * 设置蓝牙关机时间。
     *
     * @param time 关机时间
    *
    * */
    boolean setTimeToPowerOff(short time);

    /**
     * 手动停止扫描
     */
    void stopScanning();

    /**
     * 断开蓝牙卡连接
     */
    void disconnectDevice();

    /**
     * 检测蓝牙连接状态
     */
    boolean isConnected();

    /**
     * 获取设备的产品序列号
     *
     * @param scanRecord
     * @return
     */
    String getProductId(byte[] scanRecord);

    /**
     * 释放蓝牙卡相关资源
     */
    void destroy();

    /**
     * 查询蓝牙卡电压状态。
     * <p>
     * 03 电压正常； <br/>
     * 02 电压低告警状态,此时读卡器还可以读卡几 十次,黄灯亮； <br/>
     * 01 电压极低状态,此时读卡器不能读卡,而且会关机；<br/>
     * Others:查询失败
     */
    byte getVoltageState();

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
    byte getChargeState();

    /**
     * 获取已连接的蓝牙卡电量剩余百分比
     */
    int getBatteryPercent();

    /**
     * 获取已连接的蓝牙卡设备信息
     */
    DeviceInfo getBleDeviceInfo();


}
