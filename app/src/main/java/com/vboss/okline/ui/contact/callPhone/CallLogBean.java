package com.vboss.okline.ui.contact.callPhone;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/5/4 20:09
 * Desc :
 */

public class CallLogBean {

    /**
     * 通话类型 0为,未接,1为 拨出,2,为 拨入
     */
    private int type;
    /**
     * 备注名称
     */
    private String remarkName;
    /**
     * 通话日期
     */
    private String dates;
    /**
     * 通话时间
     */
    private String duration;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * 通话类型 0为,未接,1为 拨出,2,为 拨入
     */
    public int getType() {
        return type;
    }
    /**
     * 通话类型 0为,未接,1为 拨出,2,为 拨入
     */
    public void setType(int type) {
        this.type = type;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }


    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    @Override
    public String toString() {
        return "CallLogBean{" +
                "type=" + type +
                ", remarkName='" + remarkName + '\'' +
                ", dates=" + dates +
                '}';
    }
}
