package com.okline.vboss.assistant.utils.shadow;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 5/25/15.
 */
public class ListViewShadowViewHelper {
    private ShadowProperty shadowProperty;
    private View view;
    private ShadowViewDrawable shadowViewDrawable;
    private int color;
    private float rx;
    private float ry;

    public static ListViewShadowViewHelper bindShadowHelper(ShadowProperty shadowProperty, View view) {
        return new ListViewShadowViewHelper(shadowProperty, view, Color.WHITE, 0, 0);
    }

    public static ListViewShadowViewHelper bindShadowHelper(ShadowProperty shadowProperty, View view, int color) {
        return new ListViewShadowViewHelper(shadowProperty, view, color, 0, 0);
    }

    public static ListViewShadowViewHelper bindShadowHelper(ShadowProperty shadowProperty, View view, float rx, float ry) {
        return new ListViewShadowViewHelper(shadowProperty, view, Color.WHITE, rx, ry);
    }

    public static ListViewShadowViewHelper bindShadowHelper(ShadowProperty shadowProperty, View view, int color, float rx, float ry) {
        return new ListViewShadowViewHelper(shadowProperty, view, color, rx, ry);
    }

    private ListViewShadowViewHelper(ShadowProperty shadowProperty, View view, int color, float rx, float ry) {
        this.shadowProperty = shadowProperty;
        this.view = view;
        this.color = color;
        this.rx = rx;
        this.ry = ry;
        init();
    }

    public ListViewShadowViewHelper(ShadowProperty shadowProperty, View view, float rx, float ry) {
        this.shadowProperty = shadowProperty;
        this.view = view;
        this.rx = rx;
        this.ry = ry;
        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        int shadowOffset = shadowProperty.getShadowOffset();
        //view.setPadding(view.getPaddingLeft() + shadowOffset, view.getPaddingTop() + shadowOffset, view.getPaddingRight() + shadowOffset, view.getPaddingBottom() + shadowOffset);

        shadowViewDrawable = new ShadowViewDrawable(shadowProperty, color, rx, ry);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                shadowViewDrawable.setBounds(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(shadowViewDrawable);
        } else {
            view.setBackground(shadowViewDrawable);
        }


    }

    public ShadowViewDrawable getShadowViewDrawable() {
        return shadowViewDrawable;
    }

    public View getView() {
        return view;
    }

    public ShadowProperty getShadowProperty() {
        return shadowProperty;
    }
}
