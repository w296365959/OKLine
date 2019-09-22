package com.vboss.okline.ui.app.apppool.apppooltype;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: yuan shaoyu  <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/3/30 <br/>
 * summary  :在这里描述Class的主要功能
 */

public class Card {

    /**
     * 卡片ID
     */
    private long cardId;

    /**
     * 卡片名称
     */
    private String cardName;

    /**
     * 卡片图片url
     */
    private String imgUrl;

    /**
     * 发卡商名称
     */
    private String merName;

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getMerName() {
        return merName;
    }

    public void setMerName(String merName) {
        this.merName = merName;
    }
}
