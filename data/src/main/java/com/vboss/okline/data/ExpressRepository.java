package com.vboss.okline.data;

import android.support.annotation.NonNull;

import com.vboss.okline.data.model.ExpressModel;
import com.vboss.okline.data.remote.RemoteExpressDataSource;

import java.util.List;

import rx.Observable;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/7/5 <br/>
 * Summary : 快递业务实现
 */
public class ExpressRepository implements ExpressDataSource {
    private final ExpressDataSource remote;
    private static ExpressRepository INSTANCE;

    public static ExpressRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ExpressRepository();
        }
        return INSTANCE;
    }

    private ExpressRepository() {
        remote = new RemoteExpressDataSource();
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
        return remote.getExpressList(expressState);
    }

    /**
     * 获取快递详情
     *
     * @param expressId 快递Id
     * @return
     */
    @Override
    public Observable<ExpressModel> getExpressDetail(@NonNull String expressId) {
        return remote.getExpressDetail(expressId);
    }

    /**
     * 获取快递公司列表
     *
     * @return
     */
    @Override
    public Observable<List<ExpressModel.Company>> getCompanyList() {
        return remote.getCompanyList();
    }

    /**
     * 获取快递的物流信息
     *
     * @param expressId 快递Id
     * @return
     */
    @Override
    public Observable<List<ExpressModel.Logistics>> getLogistics(String expressId) {
        return remote.getLogistics(expressId);
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
    public Observable<String> buildExpress(String friendOlNo, @NonNull String expressNo, @NonNull String sAddress, @NonNull String dAddress, @NonNull String goodsName, @NonNull String weight) {
        return remote.buildExpress(friendOlNo, expressNo, sAddress, dAddress, goodsName, weight);
    }

    /**
     * 确认收货地址
     *
     * @param friendOlNo 对方欧乐号
     * @param expressId  快递Id
     * @param dAddress   收件地址
     * @return 返回快递单号
     */
    @Override
    public Observable<String> confirmAddress(@NonNull String friendOlNo, @NonNull String expressId, @NonNull String dAddress) {
        return remote.confirmAddress(friendOlNo, expressId, dAddress);
    }

    /**
     * 通知快递
     *
     * @param expressId 快递Id
     * @return 返回通知成功与否
     */
    @Override
    public Observable<Boolean> notifyExpress(@NonNull String expressId) {
        return remote.notifyExpress(expressId);
    }
}
