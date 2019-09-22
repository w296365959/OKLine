package com.vboss.okline.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.vboss.okline.data.entities.CardCondition;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.Cert;
import com.vboss.okline.data.entities.OKCard;
import com.vboss.okline.data.entities.PaymentsOrder;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.data.entities.TransferProtocol;

import java.util.List;

import rx.Observable;


/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/31 <br/>
 * Summary : 用户数据入口
 */
public interface UserDataSource {

    /**
     * 注册用户
     *
     * @param context
     * @param idCardNo   身份证号码
     * @param realName   身份证姓名
     * @param creditType
     * @param imei       手机IMEI号
     * @param imsi       SIM卡IMSI号
     * @return 返回欧乐号
     */
    Observable<User> registerUser(Context context, @NonNull String idCardNo, @NonNull String realName, int creditType, @NonNull String imei, @NonNull String imsi);

    /**
     * 获取欧乐会员资料
     *
     * @param keyOfSearch 欧乐号
     * @return
     */
    Observable<User> getOKLineMember(String keyOfSearch);


    /**
     * 更新用户头像
     *
     * @param filePath 头像文件的本地Uri
     * @return
     */
    Observable<String> updateAvatar(@NonNull String filePath);

    /**
     * 获取已绑定的欧卡。
     *
     * @return
     */
    Observable<OKCard> getOCard();

    /**
     * 操作欧卡前的身份验证
     *
     * @param creditType
     * @param realName
     * @param idCardNo
     * @return
     */
    Observable<Boolean> authenticateToHandleOCard(int creditType, @NonNull String realName, @NonNull String idCardNo);

    /**
     * 绑定欧卡
     *
     * @param deviceType
     * @param deviceNo
     * @param bhtName
     * @param bhtAddress
     * @return
     */
    Observable<OKCard> attachDevice(@DeviceFlavor int deviceType, @NonNull String deviceNo, @NonNull String bhtName, @NonNull String bhtAddress);

    /**
     * 挂失欧卡
     */
    Observable<OKCard> lossOCard(@DeviceFlavor int deviceType, @NonNull String deviceNo);

    /**
     * 重新启用欧卡
     */
    Observable<OKCard> resumeOCard(@DeviceFlavor int deviceType, @NonNull String deviceNo);

    /**
     * 补办新卡
     *
     * @param deviceType 设备类型
     * @param deviceNo   新卡片设备号
     * @param bhtName    新卡蓝牙蓝牙
     * @param bhtAddress 新卡蓝牙地址
     * @return
     */
    Observable<OKCard> makeupOCard(@DeviceFlavor int deviceType, @NonNull String deviceNo, @NonNull String bhtName, @NonNull String bhtAddress);

    /**
     * 获取开卡条件
     *
     * @param cardMainType
     * @param aid
     * @return
     */
    Observable<CardCondition> getCardOpenCondition(int cardMainType, String aid);

    /**
     * 同步卡片
     *
     * @param cardNo
     * @param aid
     * @param orderNo
     * @return
     */
    Observable<CardEntity> syncCard(String cardNo, String aid, String orderNo);

    /**
     * C2C收款
     *
     * @param amount
     * @param targetOlNo
     * @param tipsId
     * @return
     */
    Observable<Boolean> c2cCash(int amount, String targetOlNo, int tipsId);

    /**
     * 设置JPush registrationId
     *
     * @param olNo
     * @param registrationId id
     */
    void setJPushRegistrationId(String olNo, String registrationId);

    /**
     * 获取对方的银行卡列表
     *
     * @param cardNo       己方卡号
     * @param friendOlNo   对方olno
     * @param friendCardNo 对方卡号
     * @param tipsId
     * @return
     */
    Observable<Integer> setTransferTips(String cardNo, String friendOlNo, String friendCardNo, int tipsId);

    /**
     * 响应转账方的索取银行卡列表请求
     *
     * @param friendOlNo 对方欧乐号
     * @param tipsId     提醒表主键
     * @return
     */
    Observable<TransferProtocol> queryTransferTips(String friendOlNo, int tipsId);


    /**
     * 获取同某好友的转账交易历史
     *
     * @param friendOlNo 好友的欧乐号
     * @return
     */
    Observable<List<TransferProtocol>> getTransferHistory(String friendOlNo);

    /**
     * 获取CA证书
     *
     * @return
     */
    public Observable<Cert> getCA();

    /**
     * 创建收付款业务的订单号
     *
     * @param cardMainType 卡片主类型
     * @param cardNo       卡号
     * @param amount       交易金额
     * @return
     */
    Observable<String> createPaymentsOrder(int cardMainType, String cardNo, int amount);

    /**
     * 查询收付款订单
     *
     * @param orderNo 订单号
     * @return
     */
    Observable<PaymentsOrder> queryPaymentOrder(String orderNo);

    /**
     * 收付款支付
     *
     * @param cardMainType 卡片主类型
     * @param cardNo       支付卡号
     * @param orderNo      支付订单号
     * @return
     */
    Observable<Boolean> payPayment(int cardMainType, String cardNo, String orderNo);

}
