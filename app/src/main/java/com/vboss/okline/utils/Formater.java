package com.vboss.okline.utils;

import java.text.DecimalFormat;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Jiang Zhongyuan <br/>
 * Email   : jiangzhongyuan@okline.cn <br/>
 * Date    : 17/5/7 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class Formater {
    public static final String MONEY_FORMAT_DOUBLE = "###############0.00";
    public static DecimalFormat moneyFormater = new DecimalFormat(MONEY_FORMAT_DOUBLE);

    public static String money(double money) {
        return moneyFormater.format(money);
    }

    public static String money(int money) {
        return money(money / 100.0);
    }
}
