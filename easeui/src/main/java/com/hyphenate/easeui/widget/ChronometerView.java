package com.hyphenate.easeui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Chronometer;

/**
 * OKLine(luoxiuxiu) co.,Ltd.<br/>
 * Author  : luoxiuxiu <br/>
 * Email   : show@okline.cn <br/>
 * Date    : 2017/4/19 <br/>
 * Summary :  语音计时
 */

public class ChronometerView extends Chronometer {
    public ChronometerView(Context context) {
        super(context);
    }

    public ChronometerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChronometerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        //continue when view is hidden
        visibility = View.VISIBLE;
        super.onWindowVisibilityChanged(visibility);
    }

}
