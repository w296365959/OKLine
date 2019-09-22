package com.ICE.VOIP.ui;

import android.util.Log;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Jiang Zhongyuan <br/>
 * Email   : jiangzhongyuan@okline.cn <br/>
 * Date    : 17/5/4 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class Util {

    public static void log(Object o, String text) {
        Log.d("VOIPPhone", o.getClass().getSimpleName() + " ===> " + text);
    }

    public static void log(String text) {
        Log.d("VOIPPhone", text);
    }
}
