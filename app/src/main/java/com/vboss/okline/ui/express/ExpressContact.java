package com.vboss.okline.ui.express;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.vboss.okline.data.model.ExpressModel;

import java.util.List;

/**
 * Okline(Hangzhou)co,Ltd<br/>
 * Author: wangzhongming<br/>
 * Email:  wangzhongming@okline.cn</br>
 * Date :  2017/7/4 14:04 </br>
 * Summary:快递契约类
 */

public interface ExpressContact {
    interface ExpressView {
        /**
         * 快递列表
         *
         * @param list 快递的集合
         */
        void setMyExpressList(List<ExpressModel> list);


        /**
         * 快递公司列表
         * @param list
         */
        void setExpressCompany( List<ExpressModel.Company> list);


        void hideSelectCompany();
    }


    interface ExpressPresenter {
        /**
         * 获取快递列表 集合 ,1为收件,2为发件
         * @param expressState 快递状态,在ExpressModel里有
         */
        void getExpressLists(@ExpressModel.StateFlavour int expressState);

        /**
         * 获取快递详情
         *
         * @param expressId 快递ID
         */
        void getExpressDetails(@NonNull String expressId);

        /**
         * 获取快递公司列表  集合
         */
        void getCompanyLists();

        /**
         * 获取快递物流信息即快递记录
         * @param expressId 快递单号
         */
        void getExpressLogistics(@NonNull String expressId);

        /**
         * 发送快递
         *
         * @param friendOlNo 对方 欧乐号
         * @param expressNo  快递公司编号
         * @param sAddress   发件人地址
         * @param dAddress   收件人地址
         * @param goodsName  物品名称
         * @param weight     物品重量
         * @return 返回创建的快递Id
         */
        void sendExpress(String friendOlNo, @NonNull String expressNo, @NonNull String sAddress, @NonNull String dAddress, @NonNull String goodsName, @NonNull String weight);

        /**
         * 确认收货地址
         *
         * @param friendOlNo 对方欧乐号
         * @param expressId  快递Id
         * @param dAddress   收件地址
         * @return 返回快递单号
         */
        void confirmAddress(@NonNull String friendOlNo, @NonNull String expressId, @NonNull String dAddress);

        /**
         * 快递揽件成功的通知
         *
         * @param expressId 快递Id
         */
        void notifyExpress(@NonNull String expressId);

        /**
         * 货物信息
         *
         * @param goodsName 货物名称
         * @param weight    货物重量
         * @param sAddress  发货地址
         */
        void acceptExpressMessage(String goodsName, String weight, String sAddress);



    }

    /**
     * OnTheWayFragment对应Presenter
     */
    interface OnTheWayPresenter{
       /* *//**
         * 选择快递公司后
         * @param name  快递公司名
         * @param textView
         *//*
        void selectedCompany(String name, TextView textView);*/


    }
    /**
     * ToBeConfirmFragment对应Presenter
     */
    interface ToBeConfirmPresenter{

    }
    /**
     * CompletedFragment对应Presenter
     */
    interface CompletedPresenter{

    }
}
