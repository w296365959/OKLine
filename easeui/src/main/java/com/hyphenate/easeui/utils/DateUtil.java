package com.hyphenate.easeui.utils;

/**
 * OKLine(luoxiuxiu) co.,Ltd.<br/>
 * Author  : luoxiuxiu <br/>
 * Email   : show@okline.cn <br/>
 * Date    : 2017/5/16 <br/>
 * Summary : 聊天时间公共类
 */

public class DateUtil {

    private static final long INTERVAL_IN_MILLISECONDS = 600000L;

    public DateUtil() {
    }

    public static boolean isCloseEnough(long var0, long var2) {
        long var4 = var0 - var2;
        if(var4 < 0L) {
            var4 = -var4;
        }

        return var4 < INTERVAL_IN_MILLISECONDS;
    }
}
