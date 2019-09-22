package com.okline.vboss.assistant.ui.opay;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/4/14
 * Summary : 在这里描述Class的主要功能
 */

class OLConfig {

    /**
     * 默认空字串
     */
    String EMPTY_STRING = "";
    /**
     * 默认页码
     */
    int DEFAULT_PAGE_NUM = 1;

    /**
     * 网络请求成功
     */
    static int RESULT_OK = 0;


    /**
     * 验证网络返回数据的签名失败的状态码
     */
    int CODE_VERIFY_SIGN_FAILURE = 9527;
    //    String BASE_URL = "http://bss.test.okline.com.cn:8000/";
    String BASE_URL = "http://115.236.100.66:8800/vboss/";

    //    String BASE_URL = "http://192.168.2.109/vboss/";
//    String BASE_URL = "http://oss.test.okline.com.cn:8080/";
    //        String BASE_URL = "http://192.168.2.159:80/vboss/";
//    String BASE_URL = "http://192.168.2.109/vboss/";
    String PLATFORM = "olApp";

    String PID = "olApp";
    String PCODE = "OKLine";

    /**
     * 用户注册
     */
    String REGISTER = "userRegisterRequest";
    /**
     * 登录系统
     */
    String LOGIN = "userLoginRequest";
    /**
     * 绑定欧卡
     */
    String BIND_OCARD = "userBindingRequest";
    /**
     * 解绑欧卡
     */
    String UNBIND_OCARD = "userUnbindRequest";
    /**
     * 获取好友列表
     */
    String GET_BUDDY_LIST = "userContactsRequest";

    /**
     * 获取绑定的卡片
     */
    String GET_CARD_LIST = "cardListRequest";

    /**
     * 获取开卡支持的卡列表
     */
    String GET_SUPPORTED_CARD_LIST = "getCardListRequest";
    /**
     * 获取卡片记录
     */
    String GET_CARD_RECORDS = "cardTransRequest";
    /**
     * 交易详情 拿到线上支付记录
     */
    String GET_RECORD_DETAIL = "cardTransDtlRequest";

    /**
     * 验证短信验证码
     */
    String VERIFY_SMS_CODE = "checkVerfiyRequest";
    /**
     * 绑定银行卡
     */
    String BIND_BANK_CARD = "bandBankcardRequest";
    /**
     * 银行卡开户
     */
    String OPEN_ACCOUNT = "openAccountRequest";

    /**
     * 卡片开户
     * 如公交卡
     */
    String OPEN_CARD = "openCardRequest";

    String CARD_SYNC = "syncCardInfoRequest";

    /**
     * 请求验证
     */
    String REQUEST_SMS_CODE = "getVerifyRequest";
    /**
     * 获取卡片客户端列表
     */
    String GET_SUPPORTED_APP_LIST = "appMarketRequest";

    /**
     * 获取已绑定的卡的对应的手机客户端信息
     */
    String GET_CARD_APP_LIST = "myAppListRequest";

    /**
     * 获取绑定的所有卡片（银行卡、交通卡、会员卡等）列表
     */
    String GET_ALL_CARDS = "mainShowRequest";

    /**
     * 卡片置顶
     */
    String TOP_CARD = "cardDefaultRequest";

    /**
     * 手机充值金额列表
     */
    String GET_PHONE_BILL = "qryFeeListRequest";

    /**
     * 卡片充值金额列表
     */
    String GET_CARD_FEE_LIST = "qryPayFeeListRequest";

    /**
     * 手机充值
     */
    String RECHARGE_MOBILE_PHONE = "phoneRechargeRequest";
    /**
     * 获取手机充值结果
     */
    String GET_RESULT_OF_RECHARGE = "qryPayResultRequest";
    /**
     * 获取卡片详情
     */
    String GET_CARD_DETAIL = "cardDetailRequest";
    /**
     * 删除卡片
     */
    String DELETE_CARD = "cardDeleteRequest";

    /**
     * 绑银行卡时的银行卡短信验证
     */
    String REQUEST_SMS_CODE_FROM_BANK = "smsValidRequest";

    /**
     * 实名认证
     */
    String USER_CERTIFY = "userCertifyRequest";

    /**
     * 设置私密密码
     */
    String SET_PAY_PASSWORD = "setPwdRequest";

    /**
     * 验证私密密码
     */
    String VERIFY_PAY_PASSWORD = "checkPwdRequest";

    /**
     * 修改私密密码
     */
    String MODIFY_PAY_PASSWORD = "modifyPwdRequest";

    /**
     * 检查密码是否设置
     */
    String CHECK_PASSWORD_IS_SET = "qryPwdRequest";
    /**
     * 向服务器推送刷卡记录
     */
    String PUSH_RECORD = "payTransUploadRequest";

    String GET_USER_INFO = "qryUserInfoRequest";

    String UPDATE_USER_INFO = "modUserInfoRequest";
    /**
     * 导入手机通讯录
     */
    String ADD_CONTACTS = "getAddFriendListRequest";

    String DELETE_CONTACTS = "userDelFriendRequest";

    String GET_MERCHANT_LIST = "getMerListRequest";

    String GET_CONFIG_LIST = "getConfListRequest";

    String GET_BANK_CARD_TYPE = "qryBankBinRequest";


    String CHECK_VERSION = "checkVersionRequest";

    String GET_SHOPPING_RECEIPT = "cardTransDtlRequest";

    /**
     * 查询联系人信息
     */
    String GET_FRIEND_DETAIL = "qryUserFriendDetailRequest";
    /**
     * 保存联系人信息
     */
    String UPDATE_FRIEND_DETAIL = "updateUserFriendDetailRequest";

    /**
     * 之前保存联系人的接口
     */
    String MODIFY_CONTACTS_NICKNAME = "userModFriendRequest";

    /**
     * 获取单个联系人详情
     */
    String GET_CONTACTS_DETAIL = "userFriendRefreshRequest";

    String C2C_TRANSFER = "userTransFeeRequest";
    /**
     * 查询push信息
     */
    String QUERY_PUSH_INFO = "qryPushInfoRequest";
    /**
     * 创建联系人
     */
    String CREATE_CONTACT = "addUserFriendRequest";
}
