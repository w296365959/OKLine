package com.vboss.okline.ui.user.entity;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/4/13
 * Summary : 在这里描述Class的主要功能
 */

public class CardRechargeInfo {
    private int amount;
    private boolean selected;

    public CardRechargeInfo(int amount, boolean selected) {
        this.amount = amount;
        this.selected = selected;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
