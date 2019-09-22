package com.okline.vboss.assistant.net;

import com.google.gson.annotations.SerializedName;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/6/7 <br/>
 * Summary : 请求数据实体类
 */
class RequestData {

    private String olNo;
    private Integer cardMainType;
    private String cardNo;
    private String aid;
    private String order;
    private Integer sysBelong;
    @SerializedName("pageNum")
    private Integer pageIndex;
    private Integer pageSize;

    private RequestData(Builder builder) {
        olNo = builder.olNo;
        cardMainType = builder.cardMainType;
        cardNo = builder.cardNo;
        aid = builder.aid;
        order = builder.order;
        sysBelong = builder.sysBelong;
        pageIndex = builder.pageIndex;
        pageSize = builder.pageSize;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {
        private String olNo;
        private Integer cardMainType;
        private String cardNo;
        private String aid;
        private String order;
        private Integer sysBelong;
        private Integer pageIndex;
        private Integer pageSize;

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

        public Builder cardNo(String val) {
            cardNo = val;
            return this;
        }

        public Builder aid(String val) {
            aid = val;
            return this;
        }

        public Builder order(String val) {
            order = val;
            return this;
        }

        public Builder sysBelong(Integer val) {
            sysBelong = val;
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

        public RequestData build() {
            return new RequestData(this);
        }
    }
}
