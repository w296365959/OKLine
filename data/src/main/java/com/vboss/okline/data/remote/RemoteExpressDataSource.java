package com.vboss.okline.data.remote;

import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.okline.vboss.http.OKLineClient;
import com.vboss.okline.data.ExpressDataSource;
import com.vboss.okline.data.local.UserLocalDataSource;
import com.vboss.okline.data.model.ExpressModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/7/5 <br/>
 * Summary : 快递业务远程接口实现
 */
public class RemoteExpressDataSource implements ExpressDataSource {
    private static final String QRY_EXPRESS_LIST = "qryExpressDetailRequest";
    private static final String GET_COMPANY_LIST = "qryExpressListRequest";
    private static final String GET_LOGISTICS_INFO = "qryExpressTimeRequest";
    private static final String BUILD_EXPRESS = "setExpressRequest";
    private static final String CONFIRM_ADDRESS = "setExpressConfirmedRequest";
    private static final String NOTIFY_EXPRESS = "sendExpressRequest";

    private final OKLineClient client;


    public RemoteExpressDataSource() {
        client = OKLineClient.getInstance();
    }

    /**
     * 获取快递列表
     *
     * @param expressState 快递状态
     * @return
     * @see ExpressModel#ALL
     * @see ExpressModel#STATE_UNDERWAY
     * @see ExpressModel#STATE_UNCONFIRMED
     * @see ExpressModel#STATE_FINISH
     */
    @Override
    public Observable<List<ExpressModel>> getExpressList(@ExpressModel.StateFlavour int expressState) {
        return queryExpress(expressState, null);
    }

    /**
     * 获取快递详情
     *
     * @param expressId 快递Id
     * @return 返回对应快递详情
     */
    @Override
    public Observable<ExpressModel> getExpressDetail(@NonNull final String expressId) {
        return queryExpress(ExpressModel.ALL, expressId).map(new Func1<List<ExpressModel>, ExpressModel>() {
            @Override
            public ExpressModel call(List<ExpressModel> expressModels) {
                if (expressModels.isEmpty()) {
                    return null;
                }
                return expressModels.get(0);
            }
        });
    }

    private Observable<List<ExpressModel>> queryExpress(int expressState, String expressId) {
        return client.requestAsyncForData(QRY_EXPRESS_LIST, RequestData.newBuilder()
                .expressState(expressState)
                .expressId(expressId).build(), new TypeToken<List<ExpressModel>>() {
        }.getType());
    }

    /**
     * 获取快递公司列表
     *
     * @return
     */
    @Override
    public Observable<List<ExpressModel.Company>> getCompanyList() {
        return client.requestAsyncForData(GET_COMPANY_LIST, RequestData.newBuilder().build(), new TypeToken<List<ExpressModel.Company>>() {
        }.getType());
    }

    /**
     * 获取快递的物流信息
     *
     * @param expressId 快递Id
     * @return
     */
    @Override
    public Observable<List<ExpressModel.Logistics>> getLogistics(String expressId) {
        return client.requestAsyncForData(GET_LOGISTICS_INFO, RequestData.newBuilder()
                .expressId(expressId).build(), new TypeToken<List<ExpressModel.Logistics>>() {
        }.getType());
    }

    /**
     * 发快递
     *
     * @param friendOlNo
     * @param expressNo  快递公司编号
     * @param sAddress   发件人地址
     * @param dAddress   收件人地址
     * @param goodsName  物品名称
     * @param weight     物品重量
     * @return 返回创建的快递Id
     */
    @Override
    public Observable<String> buildExpress(@NonNull String friendOlNo, @NonNull String expressNo, @NonNull String sAddress, @NonNull String dAddress, @NonNull String goodsName, @NonNull String weight) {
        Map<String, String> param = new HashMap<>();
        param.put("olNo", UserLocalDataSource.getInstance().getOlNo());
        param.put("friendOlNo", friendOlNo);
        param.put("expressNo", expressNo);
        param.put("sAddress", sAddress);
        param.put("dAddress", dAddress);
        param.put("goodsName", goodsName);
        param.put("weight", weight);

        return client.<HashMap<String, String>>requestAsyncForData(BUILD_EXPRESS, param, new TypeToken<HashMap<String, String>>() {
        }.getType()).map(new Func1<HashMap<String, String>, String>() {
            @Override
            public String call(HashMap<String, String> map) {
                if (map.isEmpty()) {
                    return null;
                }
                return map.get("expressId");
            }
        });
    }

    /**
     * 确认收货地址
     *
     * @param friendOlNo 对方欧乐号
     * @param expressId  快递Id
     * @param dAddress
     * @return 返回快递单号
     */
    @Override
    public Observable<String> confirmAddress(@NonNull String friendOlNo, @NonNull String expressId, @NonNull String dAddress) {
        Map<String, Object> param = new HashMap<>();
        param.put("olNo", UserLocalDataSource.getInstance().getOlNo());
        param.put("friendOlNo", friendOlNo);
        param.put("expressId", expressId);
        param.put("dAddress", dAddress);

        return client.<Map<String, String>>requestAsyncForData(CONFIRM_ADDRESS, param, new TypeToken<Map<String, String>>() {
        }.getType()).map(new Func1<Map<String, String>, String>() {
            @Override
            public String call(Map<String, String> map) {
                if (map.isEmpty()) {
                    return null;
                }
                return map.get("expressId");
            }
        });
    }

    /**
     * 通知快递
     *
     * @param expressId 快递Id
     * @return 返回通知成功与否
     */
    @Override
    public Observable<Boolean> notifyExpress(@NonNull String expressId) {
        return client.requestAsyncForBoolean(NOTIFY_EXPRESS, RequestData.newBuilder().expressId(expressId).build());
    }
}
