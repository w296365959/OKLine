package com.vboss.okline.data.entities;

import com.cosw.sdkblecard.DeviceInfo;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/1 <br/>
 * Summary : 物理欧卡实体类
 */
public class OKCard {
    /**
     * 欧卡绑定状态<br/>
     * <p>
     * 0：未绑定 <br/>
     * 1：已绑定
     */
    private int isBind;

    /**
     * 欧卡设备好
     */
    private String deviceNo;
    /**
     * 绑定日期。时间格式：yyyy-MM-dd HH:mm:ss
     */
    private String bindDate;
    /**
     * 挂失日期
     */
    private String lossTime;
    /**
     * 绑定状态 <br/>
     * <p>
     * 1：正常 <br/>
     * 2：已挂失 <br/>
     * 3：已失效 <br/>
     */
    private int bindState;
    /**
     * 欧卡蓝牙名称
     */
    private String bhtName;
    /**
     * 欧卡蓝牙地址
     */
    private String bhtAddress;

    private DeviceInfo info;

    //<editor-fold desc="郑军 编辑日期2017-04-10 18:39:03">
    /**
     * 欧卡总容量
     */
    private String totalVolume;
    /**
     * 欧卡可用容量
     */
    private String availableVolume;
    /**
     * 电量
     */
    private String batteryVolume;
    /**
     * 欧卡版本
     */
    private String versionInfo;

    public OKCard(int isBind, String deviceNo, String bindDate, String lossTime, int bindState, String bhtName, String bhtAddress, String totalVolume, String availableVolume, String batteryVolume, String versionInfo) {
        this.isBind = isBind;
        this.deviceNo = deviceNo;
        this.bindDate = bindDate;
        this.lossTime = lossTime;
        this.bindState = bindState;
        this.bhtName = bhtName;
        this.bhtAddress = bhtAddress;
        this.totalVolume = totalVolume;
        this.availableVolume = availableVolume;
        this.batteryVolume = batteryVolume;
        this.versionInfo = versionInfo;
    }

    public OKCard() {
    }

    public String getBatteryVolume() {
        return batteryVolume;
    }

    public int getIsBind() {
        return isBind;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public String getBindDate() {
        return bindDate;
    }

    public String getLossTime() {
        return lossTime;
    }

    public int getBindState() {
        return bindState;
    }

    public String getBhtName() {
        return bhtName;
    }

    public String getBhtAddress() {
        return bhtAddress;
    }

    public String getTotalVolume() {
        return totalVolume;
    }

    public String getAvailableVolume() {
        return availableVolume;
    }

    public String getVersionInfo() {
        return versionInfo;
    }

    public void setTotalVolume(String totalVolume) {
        this.totalVolume = totalVolume;
    }

    public void setAvailableVolume(String availableVolume) {
        this.availableVolume = availableVolume;
    }

    public void setBatteryVolume(String batteryVolume) {
        this.batteryVolume = batteryVolume;
    }

    public void setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
    }

    //</editor-fold>


    public DeviceInfo getInfo() {
        return info;
    }

    public void setInfo(DeviceInfo info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "OKCard{" +
                "isBind=" + isBind +
                ", deviceNo='" + deviceNo + '\'' +
                ", bindDate='" + bindDate + '\'' +
                ", lossTime='" + lossTime + '\'' +
                ", bindState=" + bindState +
                ", bhtName='" + bhtName + '\'' +
                ", bhtAddress='" + bhtAddress + '\'' +
                ", totalVolume='" + totalVolume + '\'' +
                ", availableVolume='" + availableVolume + '\'' +
                ", batteryVolume='" + batteryVolume + '\'' +
                ", versionInfo='" + versionInfo + '\'' +
                '}';
    }
}
