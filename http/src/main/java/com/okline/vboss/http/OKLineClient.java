package com.okline.vboss.http;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.R.attr.data;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/29 <br/>
 * Summary : 欧乐服务请求客户端
 */
public class OKLineClient {
    private static final String TAG = OKLineClient.class.getSimpleName();
    /**
     * 欧乐App的网络请求方法
     */
    private static final String METHOD_FOR_APP = "APIForApp";
    /**
     * 欧乐TOKEN的请求方法
     */
    private static final String METHOD_FOR_TOKEN = "APIForAccessToken";

//    private static final String URL_REQUEST_TOKEN = "http://bss.test.okline.com.cn:8000/APIForAeccessToken";

    /**
     * 阿里聚安全的请求方法
     */
    private static final String METHOD_FOR_SECURITY = "APIForSecurity";

    private static final String BASE_URL = "http://115.236.100.66:8800/vboss/";
    //    private static final String BASE_URL = "http://192.168.100.8:80/vboss/";
    private static final String PLATFORM = "olApp";

    static final String P_ID_FOR_APP = "OlApp";
    static final String P_CODE_FOR_APP = "OkLine";

    static final String P_ID_FOR_TOKEN = "OlAccess";
    static final String P_CODE_FOR_TOKEN = "dSQ8Sswsiw28WP1i";

    static final String P_ID_FOR_SECURITY = "sec10000";
//    private static final String P_CODE_FOR_SECURITY = "OkLine";


    private final OLServiceApi serviceApi;

    private static OKLineClient instance;

    interface OLServiceApi {
        /*@POST("APIForApp")
        @FormUrlEncoded
        Call<OLPackage> post(@Field("param") String param, @Field("pid") String pId,
                             @Field("date") String date, @Field("sign") String sign);*/
        @POST
        @FormUrlEncoded
        Call<ResponseBody> post2(@Url String url, @FieldMap Map<String, String> fieldMap);

        @POST
        @FormUrlEncoded
        Call<OLPackage> post(@Url String url, @FieldMap Map<String, String> map);
    }

    public static OKLineClient getInstance() {
        if (instance == null) {
            instance = new OKLineClient();
        }
        return instance;
    }


    private OKLineClient() {
        OkHttpClient okHttp = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String msg) {
//                        Timber.tag("OKHttp").i(msg);
                        if (BuildConfig.DEBUG) {
                            System.out.println(msg);
                        }
                    }
                }).setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        serviceApi = new Retrofit.Builder()
                .client(okHttp)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OLServiceApi.class);
    }

    /**
     * 请求数据
     *
     * @param bodyType
     * @param paramData
     * @param typeOfT
     * @param <T>
     * @return
     */
    public <T> Observable<T> requestAsyncForData(final String bodyType, Object paramData, final Type typeOfT) {
        return requestAsync(METHOD_FOR_APP, P_ID_FOR_APP, P_CODE_FOR_APP, bodyType, paramData).map(new Func1<OLPackage.Result, T>() {
            @Override
            public T call(OLPackage.Result result) {
                return result.getData(typeOfT, bodyType);
            }
        });
    }

    /**
     * 执行成功与否
     *
     * @param bodyType
     * @param paramData
     * @return
     */
    public Observable<Boolean> requestAsyncForBoolean(final String bodyType, Object paramData) {
        return requestAsync(METHOD_FOR_APP, P_ID_FOR_APP, P_CODE_FOR_APP, bodyType, paramData).map(new Func1<OLPackage.Result, Boolean>() {
            @Override
            public Boolean call(OLPackage.Result result) {
                return result.desireSuccess(bodyType);
            }
        });
    }

    /**
     * App的同步请求
     *
     * @param bodyType
     * @param data
     * @param typeOfT
     * @param <T>
     * @return
     * @throws OLException
     */
    public <T> T requestForData(String bodyType, Object data, Type typeOfT) throws OLException {
        OLPackage.Result result = request(METHOD_FOR_APP, P_ID_FOR_APP, P_CODE_FOR_APP, bodyType, data);
        return result.getData(typeOfT, bodyType);
    }

    /**
     * App的同步请求
     *
     * @param bodyType
     * @param data
     * @return
     * @throws OLException
     */
    public boolean requestForBoolean(String bodyType, Object data) throws OLException {
        OLPackage.Result result = request(METHOD_FOR_APP, P_ID_FOR_APP, P_CODE_FOR_APP, bodyType, data);
        return result.desireSuccess(bodyType);
    }

    public <T> Observable<T> aliSecurityRequest(final String deviceNo, final String imei, final String bodyType, final Type typeOfT) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                String olToken = retrieveOLToken(OLToken.GRANT_TYPE_ALI_SECURITY, deviceNo, deviceNo);
                System.out.println("OLToken : " + olToken);
                try {
                    OLPackage.Result result = requestForToken(METHOD_FOR_SECURITY, P_ID_FOR_SECURITY, olToken, bodyType, deviceNo, imei, OLToken.GRANT_TYPE_ALI_SECURITY);
                    subscriber.onNext((T) result.getData(typeOfT, bodyType));
                    subscriber.onCompleted();
                } catch (OLException e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 获取Token
     *
     * @param grantType Token用途类型
     * @param deviceNo  设备号
     * @param imei      imei号
     * @return 返回访问TOKEN
     */
    public String retrieveOLToken(int grantType, String deviceNo, String imei) {
        OLToken request = new OLToken(grantType, deviceNo, EncryptUtils.md5Hex(imei));
        OLToken response = request(METHOD_FOR_TOKEN, P_ID_FOR_TOKEN, P_CODE_FOR_TOKEN, null, request).getData(OLToken.class, "RetrieveToken");
        if (response.getUseRule() == 2) {
            // TODO: 2017/6/2  Cache token
        }
        return response.getToken();
    }

    /**
     * 同步请求
     *
     * @param url
     * @param pId
     * @param pCode
     * @param bodyType
     * @param data
     * @return
     * @throws OLException
     */
    private OLPackage.Result request2(@Nullable String url, String pId, String pCode, String bodyType, Object data) throws OLException {
        OLPackage rp = new OLPackage(PLATFORM, pId, bodyType, data);
        Response<ResponseBody> response = null;
        try {
            response = serviceApi.post2(url, rp.createMap(pCode)).execute();
            if (response.isSuccessful()) {
                System.out.println("Response.Body : " + response.body().string());
            } else {
                System.out.println("Response.ErrorBody : " + response.errorBody());
            }
        } catch (IOException e) {
            throw new OLException(OLException.CODE_HTTP_EXCEPTION, e);
        }

        if (response.isSuccessful()) {
            return null  /*response.body().getResult(pCode)*/;
        } else {
            throw new OLException(response.code(), bodyType, response.message());
        }

    }

    /**
     * 同步请求
     *
     * @param url
     * @param pId
     * @param pCode
     * @param bodyType
     * @param data
     * @return
     * @throws OLException
     */
    private OLPackage.Result request(@Nullable String url, String pId, String pCode, String bodyType, Object data) throws OLException {
        OLPackage rp = new OLPackage(PLATFORM, pId, bodyType, data);
        Response<OLPackage> response = null;
        try {
            response = serviceApi.post(url, rp.createMap(pCode)).execute();
            System.out.println("Response : " + response.message());
        } catch (IOException e) {
            throw new OLException(OLException.CODE_HTTP_EXCEPTION, e);
        }

        if (response.isSuccessful()) {
            return response.body().getResult(pCode);
        } else {
            throw new OLException(response.code(), bodyType, response.message());
        }

    }

    /**
     * 同步请求
     *
     * @param url
     * @param pId
     * @param pCode
     * @param bodyType
     * @param deviceNo
     * @param imei
     * @param grantType
     * @return
     * @throws OLException
     */
    private OLPackage.Result requestForToken(@Nullable String url, String pId, String pCode, String bodyType, String deviceNo, String imei, int grantType) throws OLException {
        OLPackage rp = new OLPackage(PLATFORM, pId, bodyType, data);
        rp.setDeviceNo(deviceNo);
        rp.setGrantType(grantType);
        rp.setImei(EncryptUtils.md5Hex(imei));
        Response<OLPackage> response = null;
        try {
            response = serviceApi.post(url, rp.createFieldMap(pCode, grantType, deviceNo, imei)).execute();
            System.out.println("Response : " + response.message());
        } catch (IOException e) {
            throw new OLException(OLException.CODE_HTTP_EXCEPTION, e);
        }

        if (response.isSuccessful()) {
            return response.body().getResult(pCode);
        } else {
            throw new OLException(response.code(), bodyType, response.message());
        }

    }

    /**
     * 网络异步请求
     *
     * @param url       动态Url
     * @param pId       签名Id
     * @param pCode     签名Code
     * @param bodyType  请求的业务类型
     * @param paramData 请求参数
     * @return 返回网络请求响应结果
     */
    private Observable<OLPackage.Result> requestAsync(@Nullable final String url, @NonNull final String pId,
                                                      @NonNull final String pCode, @NonNull final String bodyType, @Nullable final Object paramData) {
        return Observable.create(new Observable.OnSubscribe<OLPackage.Result>() {
            @Override
            public void call(Subscriber<? super OLPackage.Result> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    OLPackage rp = new OLPackage(PLATFORM, pId, bodyType, paramData);
                    try {
                        Response<OLPackage> response = serviceApi.post(url, rp.createMap(pCode)).execute();

                        if (response.isSuccessful()) {
                            try {
                                subscriber.onNext(response.body().getResult(pCode));
                                subscriber.onCompleted();
                            } catch (OLException e) {
                                subscriber.onError(e);
                            }
                        } else {
                            subscriber.onError(new OLException(response.code(), bodyType, response.message()));
                        }
                    } catch (IOException e) {
                        subscriber.onError(e);
                    }
                }
            }
        }).subscribeOn(Schedulers.io());
    }


}
