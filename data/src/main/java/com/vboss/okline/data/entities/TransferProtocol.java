package com.vboss.okline.data.entities;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/5/7 <br/>
 * Summary : 转账交易实体
 */
public class TransferProtocol {

    /**
     * 提醒主键
     */
    private int tipsId;
    /**
     * 接受状态 <br/>
     * <p>
     * 0： 接收方已选择转账卡号
     * 1：接收方未选择转账卡号
     */
    private int returnState;

    /**
     * 转账交易角色<br/>
     * <p>
     * 0：己方
     * 1：对方
     */
    private int payObj;
    /**
     * 己方卡号
     */
    private String cardNo;
    /**
     * 对方卡号
     */
    private String fCardNo;
    /**
     * 己方卡片名称
     */
    private String cardName;
    /**
     * 对方卡片名称
     */
    private String fCardName;
    /**
     * 己方卡片url
     */
    private String imgUrl;
    /**
     * 对方卡片url
     */
    private String fImgUrl;
    /**
     * 交易金额
     */
    private long amount;

    public int getTipsId() {
        return tipsId;
    }

    public int getReturnState() {
        return returnState;
    }

    public int getPayObj() {
        return payObj;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getfCardNo() {
        return fCardNo;
    }

    public String getCardName() {
        return cardName;
    }

    public String getfCardName() {
        return fCardName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getfImgUrl() {
        return fImgUrl;
    }

    public long getAmount() {
        return amount;
    }
}
