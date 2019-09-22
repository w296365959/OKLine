package com.vboss.okline.data;

import java.util.List;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/20 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class LogUtils {
    public static void println(Object object) {
        if (object instanceof List) {
            System.out.println("Size = " + ((List) object).size());
            for (Object ob : (List) object) {
                System.out.println(ob);
            }
        } else {
            System.out.println(object);
        }
    }
}
