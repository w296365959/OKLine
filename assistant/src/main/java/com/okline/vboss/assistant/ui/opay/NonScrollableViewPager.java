package com.okline.vboss.assistant.ui.opay;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * OKLine(Hangzhou) Co.,ltd.
 * Author:Zheng Jun
 * Email:zhengjun@okline.cn
 * Date: 2016-6-16 11:28:11
 * Desc:
 */
public class NonScrollableViewPager extends ViewPager {

    public NonScrollableViewPager(Context context) {
        super(context);
    }

    public NonScrollableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item,Math.abs(getCurrentItem()-item)==1);
    }
}
