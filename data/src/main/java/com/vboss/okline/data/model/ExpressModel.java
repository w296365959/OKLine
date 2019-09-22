package com.vboss.okline.data.model;

import android.support.annotation.IntDef;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/7/4 <br/>
 * Summary : 快递业务数据模型
 */
public class ExpressModel {


    /**
     * 所有类型
     */
    public static final int ALL = -1;

    /**
     * 待确认的快递
     */
    public static final int STATE_UNCONFIRMED = 1;

    /**
     * 在路上的快递
     */
    public static final int STATE_UNDERWAY = 2;

    /**
     * 已完成的快递
     */
    public static final int STATE_FINISH = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ALL, STATE_FINISH, STATE_UNCONFIRMED, STATE_UNDERWAY})
    public @interface StateFlavour {
    }

    /**
     * 快递Id
     */
    private String expressId;
    /**
     * 收件人／发件人地区
     */
    private String areaName;
    /**
     * 收件人／发件人姓名
     */
    private String name;
    /**
     * 快递类型
     */
    private int expressType;
    /**
     * 发件人姓名
     */
    private String sName;
    /**
     * 收件人姓名
     */
    private String dName;
    /**
     * 发件人联系方式
     */
    private String sPhone;
    /**
     * 收件人联系人方式
     */
    private String dPhone;
    /**
     * 物件名称
     */
    private String goodsName;
    /**
     * 物件重量
     */
    private String weight;
    /**
     * 发件人地址
     */
    private String sAddress;
    /**
     * 收件人地址
     */
    private String dAddress;
    /**
     * 快递公司名称
     */
    private String expressName;
    /**
     * 快递状态
     */
    private int state;

    public String getExpressId() {
        return expressId;
    }

    public String getAreaName() {
        return areaName;
    }

    public String getName() {
        return name;
    }

    public int getExpressType() {
        return expressType;
    }

    public String getsName() {
        return sName;
    }

    public String getdName() {
        return dName;
    }

    public String getsPhone() {
        return sPhone;
    }

    public String getdPhone() {
        return dPhone;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public String getWeight() {
        return weight;
    }

    public String getsAddress() {
        return sAddress;
    }

    public String getdAddress() {
        return dAddress;
    }

    public String getExpressName() {
        return expressName;
    }

    public int getState() {
        return state;
    }

    @Override
    public String toString() {
        return "ExpressModel{" +
                "expressId='" + expressId + '\'' +
                ", areaName='" + areaName + '\'' +
                ", name='" + name + '\'' +
                ", expressType=" + expressType +
                ", sName='" + sName + '\'' +
                ", dName='" + dName + '\'' +
                ", sPhone='" + sPhone + '\'' +
                ", dPhone='" + dPhone + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", weight='" + weight + '\'' +
                ", sAddress='" + sAddress + '\'' +
                ", dAddress='" + dAddress + '\'' +
                ", expressName='" + expressName + '\'' +
                ", state=" + state +
                '}';
    }

    /**
     * 物流公司
     */
    public static class Company {
        /**
         * 公司名称
         */
        @SerializedName("expressName")
        private String name;
        /**
         * 公司编号
         */
        @SerializedName("expressNo")
        private int number;

        public String getName() {
            return name;
        }

        public int getNumber() {
            return number;
        }

        @Override
        public String toString() {
            return "Company{" +
                    "name='" + name + '\'' +
                    ", number=" + number +
                    '}';
        }
    }

    /**
     * 物流信息
     */
    public static class Logistics {
        /**
         * 更新时间
         */
        @SerializedName("createDate")
        private String date;
        /**
         * 物流信息
         */
        @SerializedName("remark")
        private String desc;

        public String getDate() {
            return date;
        }

        public String getDesc() {
            return desc;
        }

        @Override
        public String toString() {
            return "Logistics{" +
                    "date='" + date + '\'' +
                    ", desc='" + desc + '\'' +
                    '}';
        }
    }
}
