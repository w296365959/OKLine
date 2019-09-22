package com.vboss.okline.ui.auth.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.ImageView;

/**
 * OKLine(luoxiuxiu) co.,Ltd.<br/>
 * Author  : luoxiuxiu <br/>
 * Email   : show@okline.cn <br/>
 * Date    : 2017/4/24 <br/>
 * Summary : 定义一个ImageView,
 */

public class AutoImageView extends ImageView {
    public AutoImageView(Context context) {
        super (context);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
        } catch (Exception e) {
            System.out
                    .println("MyImageView  -> onDraw() Canvas: trying to use a recycled bitmap");
        }
    }
}
