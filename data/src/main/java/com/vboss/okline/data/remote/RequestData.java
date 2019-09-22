package com.vboss.okline.data.remote;

import com.google.gson.annotations.SerializedName;
import com.vboss.okline.data.entities.CheckInfo;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.data.local.UserLocalDataSource;

import java.util.List;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/30 <br/>
 * Summary : 服务器请求的参数构造器类
 */
class RequestData {
    private String olNo;
    private Integer cardMainType;
    private Integer cardId;
    private String merName;
    @SerializedName("pageNum")
    private Integer pageIndex;
    private Integer pageSize;
    private String cardNo;
    private List<ContactEntity> phoneList;
    private Integer friendId;
    private List<String> friendOlNoList;
    private String groupName;
    private Integer groupId;
    private String friendOlNo;
    private String appName;
    private Integer sysBelong;
    private String cardName;
    private String remark;
    private Integer isNote;
    private String transDate;
    private Integer deviceType;
    private String deviceNo;
    private String bhtName;
    private String bhtAddress;
    private String idCardNo;
    private Integer creditType;
    private String realName;
    private String imei;
    private String imsi;
    private String month;
    @SerializedName("srcOlNo")
    private String destOlNo;
    private String aid;
    private Integer amount;
    private String bindId;
    private String orderNo;
    private String jPushId;
    private String tags;
    private String payDate;
    private String easeGroupId;
    private Integer appId;
    private Integer isFollow;
    private String imgUrl;
    private Integer tipsId;
    private String friendCardNo;
    private String tipsType;
    private String baseVersion;
    private CheckInfo[] checkInfo;
    private String phone;
    private Integer dataType;
    private String content;
    @SerializedName("key")
    private Integer updateType;
    @SerializedName("value")
    private String versionName;
    private Integer phase;
    private Integer platform;
    private Integer operatType;
    private String remarkName;
    private List<ContactEntity.Addition> dataList;
    @SerializedName("state")
    private Integer expressState;
    private String expressId;
    private String nickName;


    private RequestData(Builder builder) {
        olNo = builder.olNo;
        cardMainType = builder.cardMainType;
        cardId = builder.cardId;
        merName = builder.merName;
        pageIndex = builder.pageIndex;
        pageSize = builder.pageSize;
        cardNo = builder.cardNo;
        phoneList = builder.phoneList;
        friendId = builder.friendId;
        friendOlNoList = builder.friendOlNoList;
        groupName = builder.groupName;
        groupId = builder.groupId;
        friendOlNo = builder.friendOlNo;
        appName = builder.appName;
        sysBelong = builder.sysBelong;
        cardName = builder.cardName;
        remark = builder.remark;
        isNote = builder.isNote;
        transDate = builder.transDate;
        deviceType = builder.deviceType;
        deviceNo = builder.deviceNo;
        bhtName = builder.bhtName;
        bhtAddress = builder.bhtAddress;
        idCardNo = builder.idCardNo;
        creditType = builder.creditType;
        realName = builder.realName;
        imei = builder.imei;
        imsi = builder.imsi;
        month = builder.month;
        destOlNo = builder.destOlNo;
        aid = builder.aid;
        amount = builder.amount;
        bindId = builder.bindId;
        orderNo = builder.orderNo;
        jPushId = builder.jPushId;
        tags = builder.tags;
        payDate = builder.payDate;
        easeGroupId = builder.easeGroupId;
        appId = builder.appId;
        isFollow = builder.isFollow;
        imgUrl = builder.imgUrl;
        tipsId = builder.tipsId;
        friendCardNo = builder.friendCardNo;
        tipsType = builder.tipsType;
        baseVersion = builder.baseVersion;
        checkInfo = builder.checkInfo;
        phone = builder.phone;
        dataType = builder.dataType;
        content = builder.content;
        updateType = builder.updateType;
        versionName = builder.versionName;
        phase = builder.phase;
        platform = builder.platform;
        operatType = builder.operatType;
        remarkName = builder.remarkName;
        dataList = builder.dataList;
        expressState = builder.expressState;
        expressId = builder.expressId;
        nickName = builder.nickName;
    }

    /**
     * 返回RequestData构造器，并预设欧乐号。
     */
    static Builder newBuilder() {
        return new Builder().olNo(UserLocalDataSource.getInstance().getOlNo());
    }

    static final class Builder {
        private String olNo;
        private Integer cardMainType;
        private Integer cardId;
        private String merName;
        private Integer pageIndex;
        private Integer pageSize;
        private String cardNo;
        private List<ContactEntity> phoneList;
        private Integer friendId;
        private List<String> friendOlNoList;
        private String groupName;
        private Integer groupId;
        private String friendOlNo;
        private String appName;
        private Integer sysBelong;
        private String cardName;
        private String remark;
        private Integer isNote;
        private String transDate;
        private Integer deviceType;
        private String deviceNo;
        private String bhtName;
        private String bhtAddress;
        private String idCardNo;
        private Integer creditType;
        private String realName;
        private String imei;
        private String imsi;
        private String month;
        private String destOlNo;
        private String aid;
        private Integer amount;
        private String bindId;
        private String orderNo;
        private String jPushId;
        private String tags;
        private String payDate;
        private String easeGroupId;
        private Integer appId;
        private Integer isFollow;
        private String imgUrl;
        private Integer tipsId;
        private String friendCardNo;
        private String tipsType;
        private String srcOlNo;
        private String baseVersion;
        private CheckInfo[] checkInfo;
        private String phone;
        private Integer dataType;
        private String content;
        private Integer updateType;
        private String versionName;
        private Integer phase;
        private Integer platform;
        private Integer operatType;
        private String remarkName;
        private List<ContactEntity.Addition> dataList;
        private Integer expressState;
        private String expressId;
        private String nickName;

        private Builder() {
        }

        public Builder olNo(String val) {
            olNo = val;
            return this;
        }

        public Builder cardMainType(Integer val) {
            cardMainType = val;
            return this;
        }

        public Builder cardId(Integer val) {
            cardId = val;
            return this;
        }

        public Builder merName(String val) {
            merName = val;
            return this;
        }

        public Builder pageIndex(Integer val) {
            pageIndex = val;
            return this;
        }

        public Builder pageSize(Integer val) {
            pageSize = val;
            return this;
        }

        public Builder cardNo(String val) {
            cardNo = val;
            return this;
        }

        public Builder phoneList(List<ContactEntity> val) {
            phoneList = val;
            return this;
        }

        public Builder friendId(Integer val) {
            friendId = val;
            return this;
        }

        public Builder friendOlNoList(List<String> val) {
            friendOlNoList = val;
            return this;
        }

        public Builder groupName(String val) {
            groupName = val;
            return this;
        }

        public Builder groupId(Integer val) {
            groupId = val;
            return this;
        }

        public Builder friendOlNo(String val) {
            friendOlNo = val;
            return this;
        }

        public Builder appName(String val) {
            appName = val;
            return this;
        }

        /**
         * 系统平台 <br/>
         * 1:android
         * 0:iphone
         *
         * @param val
         * @return
         */
        public Builder sysBelong(Integer val) {
            sysBelong = val;
            return this;
        }

        public Builder cardName(String val) {
            cardName = val;
            return this;
        }

        public Builder remark(String val) {
            remark = val;
            return this;
        }

        public Builder isNote(Integer val) {
            isNote = val;
            return this;
        }

        public Builder transDate(String val) {
            transDate = val;
            return this;
        }

        public Builder deviceType(Integer val) {
            deviceType = val;
            return this;
        }

        public Builder deviceType(int val) {
            deviceType = val;
            return this;
        }

        public Builder deviceNo(String val) {
            deviceNo = val;
            return this;
        }

        public Builder bhtName(String val) {
            bhtName = val;
            return this;
        }

        public Builder bhtAddress(String val) {
            bhtAddress = val;
            return this;
        }

        public Builder idCardNo(String val) {
            idCardNo = val;
            return this;
        }

        public Builder creditType(Integer val) {
            creditType = val;
            return this;
        }

        public Builder realName(String val) {
            realName = val;
            return this;
        }

        public Builder imei(String val) {
            imei = val;
            return this;
        }

        public Builder imsi(String val) {
            imsi = val;
            return this;
        }

        public Builder month(String val) {
            month = val;
            return this;
        }

        public Builder destOlNo(String val) {
            destOlNo = val;
            return this;
        }

        public Builder aid(String val) {
            aid = val;
            return this;
        }

        public Builder amount(Integer val) {
            amount = val;
            return this;
        }

        public Builder bindId(String val) {
            bindId = val;
            return this;
        }

        public Builder orderNo(String val) {
            orderNo = val;
            return this;
        }

        public Builder jPushId(String val) {
            jPushId = val;
            return this;
        }

        public Builder tags(String val) {
            tags = val;
            return this;
        }

        public Builder payDate(String val) {
            payDate = val;
            return this;
        }

        public Builder easeGroupId(String val) {
            easeGroupId = val;
            return this;
        }

        public Builder appId(Integer val) {
            appId = val;
            return this;
        }

        public Builder isFollow(Integer val) {
            isFollow = val;
            return this;
        }

        public Builder imgUrl(String val) {
            imgUrl = val;
            return this;
        }

        public Builder tipsId(Integer val) {
            tipsId = val;
            return this;
        }

        public Builder friendCardNo(String val) {
            friendCardNo = val;
            return this;
        }

        public Builder tipsType(String val) {
            tipsType = val;
            return this;
        }

        public Builder srcOlNo(String val) {
            srcOlNo = val;
            return this;
        }

        public Builder creditType(int val) {
            creditType = val;
            return this;
        }

        public Builder baseVersion(String val) {
            baseVersion = val;
            return this;
        }

        public Builder checkInfo(CheckInfo[] val) {
            checkInfo = val;
            return this;
        }

        public Builder phone(String val) {
            phone = val;
            return this;
        }

        public Builder dataType(Integer val) {
            dataType = val;
            return this;
        }

        public Builder content(String val) {
            content = val;
            return this;
        }

        public Builder updateType(Integer val) {
            updateType = val;
            return this;
        }

        public Builder versionName(String val) {
            versionName = val;
            return this;
        }

        public Builder phase(Integer val) {
            phase = val;
            return this;
        }

        public Builder platform(Integer val) {
            platform = val;
            return this;
        }

        public Builder operatType(Integer val) {
            operatType = val;
            return this;
        }

        public Builder remarkName(String val) {
            remarkName = val;
            return this;
        }

        public Builder dataList(List<ContactEntity.Addition> val) {
            dataList = val;
            return this;
        }

        public Builder expressState(Integer val) {
            expressState = val;
            return this;
        }

        public Builder expressId(String val) {
            expressId = val;
            return this;
        }

        public Builder nickName(String val) {
            nickName = val;
            return this;
        }

        public RequestData build() {
            return new RequestData(this);
        }


    }
}
