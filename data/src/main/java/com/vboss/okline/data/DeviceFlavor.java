package com.vboss.okline.data;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.vboss.okline.data.entities.User.DEV_CLOUD_SE;
import static com.vboss.okline.data.entities.User.DEV_OCARD;
import static com.vboss.okline.data.entities.User.DEV_POS;
import static com.vboss.okline.data.entities.User.DEV_RING;
import static com.vboss.okline.data.entities.User.DEV_WATCH;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/31 <br/>
 * Summary : 用户设备类型
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({DEV_OCARD, DEV_CLOUD_SE, DEV_RING, DEV_POS, DEV_WATCH})
public  @interface DeviceFlavor {
}
