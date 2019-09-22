package com.vboss.okline.ui.contact.callPhone;

import java.io.Serializable;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/5/4 20:47
 * Desc :
 */

public class CallLogNeedAllBean implements Comparable<CallLogNeedAllBean>,Serializable {
    private final long noLoseAccuracy = 14000000000L;
    private boolean isFriend;
    /**
     * 电话号码
     */
    private String number;
    /**
     * 备注名称
     */
    private String remarkName;
    /**
     * 通话次数
     */
    private int count;
    /**
     * 通话类型 0,未接 1.拨出 2,拨入
     */
    private int type;
    /**
     * 归属地
     */
    private String location;
    /**
     * 最后通话日期
     */
    private String lastDate;
    /**
     * 最后通话日期的Long值
     */
    private Long lastDateLong;

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public Long getLastDateLong() {
        return lastDateLong;
    }

    public void setLastDateLong(Long lastDateLong) {
        this.lastDateLong = lastDateLong;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "CallLogNeedAllBean{" +
                "number='" + number + '\'' +
                ", remarkName='" + remarkName + '\'' +
                ", count=" + count +
                ", type=" + type +
                ", location='" + location + '\'' +
                '}';
    }

    @Override
    public int compareTo(CallLogNeedAllBean callLogNeedAllBean) {
        return callLogNeedAllBean.lastDateLong.compareTo(lastDateLong);
    }
}
