package com.vboss.okline.utils;

import android.content.Context;
import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import timber.log.Timber;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/4/10 14:31 <br/>
 * Summary  : fresco 图片辅助操作类
 */

public class FrescoUtil {
    private static final String TAG = FrescoUtil.class.getSimpleName();

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

    /**
     * fresco image resize
     *
     * @param imageUrl string url
     * @param width    int imageView width
     * @param height   int imageView height
     * @return DraweeController
     */
    public static DraweeController loadImage(String imageUrl, int width, int height) {
        Timber.tag(TAG).i("imageUrl%s -- width %s -- height %s", imageUrl, width, height);
        Uri uri = Uri.parse(imageUrl);
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height))
                .setProgressiveRenderingEnabled(true)
                .build();
        return Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setAutoPlayAnimations(true)
                .build();
    }

}
