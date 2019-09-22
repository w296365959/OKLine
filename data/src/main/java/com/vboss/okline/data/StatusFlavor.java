package com.vboss.okline.data;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/31 <br/>
 * Summary : 数据库中数据记录的状态值取值范围。
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({0, 1})
public @interface StatusFlavor {
}
