package com.vboss.okline.ui.express.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.ExpressRepository;
import com.vboss.okline.data.model.ExpressModel;
import com.vboss.okline.ui.express.ExpressContact;
import com.vboss.okline.ui.user.Utils;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Okline(Hangzhou)co,Ltd<br/>
 * Author: wangzhongming<br/>
 * Email:  wangzhongming@okline.cn</br>
 * Date :  2017/7/5 14:37 </br>
 * Summary:快递业务逻辑
 */

public class ExpressPresenter implements ExpressContact.ExpressPresenter {
    private static final String TAG = ExpressPresenter.class.getSimpleName();
    private ExpressRepository expressRepository;
    private ExpressContact.ExpressView expressView;
    private Context context;

    public ExpressPresenter(Context context, ExpressContact.ExpressView expressView) {
        expressRepository = ExpressRepository.getInstance();
        this.expressView = expressView;
        this.context = context;
    }


    @Override
    public void getExpressLists(@ExpressModel.StateFlavour int expressState) {
        Utils.showLog("TAG", "---------------------------");
        //获取快递列表
        expressRepository.getExpressList(expressState)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<List<ExpressModel>>("ExpressPresenter") {
                    @Override
                    public void onNext(List<ExpressModel> expressModels) {
                        super.onNext(expressModels);
                        Timber.tag(TAG).i("onNext: %d ", expressModels.size());
                        Log.i(TAG, expressModels.toString());
                        expressView.setMyExpressList(expressModels);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }


    @Override
    public void getExpressDetails(@NonNull String expressId) {

    }

    @Override
    public void getCompanyLists() {
        //得到快递公司列表
        expressRepository.getCompanyList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<List<ExpressModel.Company>>("ExpressPresenter") {
                    @Override
                    public void onNext(List<ExpressModel.Company> companies) {
                        super.onNext(companies);
                        Timber.tag(TAG).i("onNext companies: %d ", companies.size());
                        Log.i(TAG, companies.toString());
                        expressView.setExpressCompany(companies);
                    }
                });

    }

    @Override
    public void getExpressLogistics(@NonNull String expressId) {

    }

    @Override
    public void sendExpress(String friendOlNo, @NonNull String expressNo, @NonNull String sAddress, @NonNull String dAddress, @NonNull String goodsName, @NonNull String weight) {
        //发送快递
        expressRepository.buildExpress(friendOlNo,expressNo,sAddress,dAddress,goodsName,weight)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<String>(TAG){
                    @Override
                    public void onNext(String s) {
                        super.onNext(s);
                        //返回创建的快递id
                    }
                });
    }

    @Override
    public void confirmAddress(@NonNull String friendOlNo, @NonNull String expressId, @NonNull String dAddress) {

    }

    @Override
    public void notifyExpress(@NonNull String expressId) {

    }

    @Override
    public void acceptExpressMessage(String goodsName, String weight, String sAddress) {

    }



    /**
     * 设置View的选择状态
     *
     * @param view
     * @param b
     */
    public void setSelectedView(View view, boolean b) {
        view.setSelected(b);
    }


}
