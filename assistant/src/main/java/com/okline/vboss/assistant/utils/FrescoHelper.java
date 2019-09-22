package com.okline.vboss.assistant.utils;

import android.content.Context;
import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/6/8 17:14 <br/>
 * Summary  : fresco helper class
 */

public class FrescoHelper {
    /**
     * according to image resource id get DraweeController
     *
     * @param context Context
     * @param resId   image resource id
     * @return DraweeController
     */
    public static DraweeController getDefaultImage(Context context, int resId) {
        Uri uri = Uri.parse("res://" + context.getPackageName() + "/" + resId);
        return Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)    //播放gif
                .setUri(uri)     //设置uri
                .build();
    }
}
