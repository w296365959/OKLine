package com.okline.vboss.http;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.okline.vboss.http.OKLineClient.P_ID_FOR_APP;
import static com.okline.vboss.http.OKLineClient.P_ID_FOR_SECURITY;
import static com.okline.vboss.http.OKLineClient.P_ID_FOR_TOKEN;


/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/29 <br/>
 * Summary : 与服务器数据交换实体类
 */
final class OLPackage {
    private static final String TAG = OLPackage.class.getSimpleName();

    /**
     * 请求成功的code
     */
    private static final int CODE_SUCCESS = 0;

    /**
     * 参数字段
     */
    private String param;
    /**
     * p_id
     */
    private String pid;
    /**
     * 请求时间
     */
    private String date;
    /**
     * 参数签名
     */
    private String sign;

    private transient Param mParamObj;

    private int grantType;
    private String deviceNo;
    private String imei;

    /**
     * 构造器
     *
     * @param platform  系统归属，如android，iphone等。
     * @param pid       pid
     * @param bodyType  业务类型
     * @param paramData 参数数据
     */
    OLPackage(@NonNull String platform, @NonNull String pid, @NonNull String bodyType, @Nullable Object paramData) {
        this.date = getCurrentTime();
        this.pid = pid;
        this.mParamObj = new Param(platform, bodyType, paramData, date);
    }

    public void setGrantType(int grantType) {
        this.grantType = grantType;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    /**
     * @param param
     * @param pid
     * @param pCode
     * @return
     */
    private String encode(Param param, String pid, String pCode) {
        String json = GsonUtils.toJson(param);
        if (BuildConfig.DEBUG) {
            System.out.println("Request param : " + json);
        }
        String encode = null;
        if (P_ID_FOR_APP.equals(pid)) {
            encode = EncryptUtils.encodeBase64(json);
        } else if (P_ID_FOR_TOKEN.equals(pid) || P_ID_FOR_SECURITY.equals(pid)) {
            encode = EncryptUtils.encodeAes(json, pCode);
        } else {
            throw new UnsupportedOperationException("Unsupported pId " + pid);
        }

        if (encode == null) {
            throw new IllegalStateException("Fail to encode");
        }

        return encode;
    }

    /**
     * 解码
     *
     * @param src
     * @return
     */
    private Result decode(String src, String pid, String pCode) {
        String decode = null;
        if (P_ID_FOR_APP.equals(pid)) {
            decode = EncryptUtils.decodeBase64(src);
        } else if (P_ID_FOR_TOKEN.equals(pid) || P_ID_FOR_SECURITY.equals(pid)) {
            decode = EncryptUtils.decodeAes(src, pCode);
        } else {
            throw new OLException("Unsupported pId " + pid);
        }

        if (BuildConfig.DEBUG) {
            System.out.println("Decoded result : \n" + decode);
        }

        if (decode == null) {
            throw new OLException("Fail to decode ");
        }
        return GsonUtils.fromJson(decode, Result.class);
    }

    /**
     * 签名
     *
     * @param pCode
     * @return
     */
    private String sign(String pCode) {
//        this.param = encode(mParamObj, pid, pCode);
        return EncryptUtils.sign(param, pid, pCode);
    }

    /**
     * 将RetroPackage的域生成{@code Map}
     *
     * @param pCode
     * @return
     */
    Map<String, String> createMap(String pCode) {

        this.param = encode(mParamObj, pid, pCode);

        Map<String, String> map = new HashMap<>(4);
        map.put("param", this.param);
        map.put("pid", this.pid);
        map.put("date", this.date);
        map.put("sign", sign(pCode));
        /*if (BuildConfig.DEBUG) {
            System.out.println("Request Param body : \n" + map.toString());
        }*/
        return map;
    }

    Map<String, String> createFieldMap(String pCode, int grantType, String deviceNo, String imei) {
        this.param = encode(mParamObj, pid, pCode);

        Map<String, String> map = new HashMap<>(4);
        map.put("param", this.param);
        map.put("pid", this.pid);
        map.put("date", this.date);
        map.put("sign", sign(pCode));
        map.put("deviceNo", deviceNo);
        map.put("grantType", String.valueOf(grantType));
        map.put("imei", EncryptUtils.md5Hex(imei));

        return map;

    }

    private static String getCurrentTime() {
        return new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
    }

    Result getResult(String pCode) throws OLException {
        /*if (BuildConfig.DEBUG) {
            System.out.println("Response Package : " + this);
        }*/

        boolean verified = EncryptUtils.verifySign(param, pid, pCode, sign);
        if (!verified) throw new OLException("Fail to verify");
        return decode(param, pid, pCode);
    }

    @Override
    public String toString() {
        return "OLPackage{" +
                "param='" + param + '\'' + "\n" +
                ", pid='" + pid + '\'' +
                ", date='" + date + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }

    private class Param {
        String platform;
        String bodyType;
        String createTime;
        Object data;

        Param(String platform, String bodyType, Object data, String date) {
            this.platform = platform;
            this.bodyType = bodyType;
            this.createTime = date;
            this.data = data;
        }

        @Override
        public String toString() {
            return "Param{" +
                    "platform='" + platform + '\'' +
                    ", bodyType='" + bodyType + '\'' +
                    ", createTime='" + createTime + '\'' + "\n" +
                    ", data=" + data +
                    '}';
        }
    }

    static class Result {
        @SerializedName("resultCode")
        private int code;
        @SerializedName("resultMsg")
        private String message;
        /**
         * 总数
         */
        @SerializedName("count")
        private int total;
        private Object data;

        boolean desireSuccess(String bodyType) throws OLException {
            if (code == CODE_SUCCESS) {
                return true;
            } else {
                throw new OLException(code, bodyType, message);
            }
        }

        <T> T getData(Type typeOfT, String bodyType) throws OLException {
            if (code == CODE_SUCCESS) {
                String json = GsonUtils.toJson(data);
                return GsonUtils.fromJson(json, typeOfT);
            } else {
                throw new OLException(code, bodyType, message);
            }
        }

        @Override
        public String toString() {
            return "Result{" +
                    "code=" + code +
                    ", message='" + message + '\'' +
                    ", total=" + total +
                    ", data=" + data +
                    '}';
        }
    }
}
