package com.vboss.okline.ui.user.customized;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vboss.okline.R;

import java.util.ArrayList;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/3/29
 * Summary : 加载UI自定义控件
 */

public class LoadingView extends LinearLayout {

    public static final int DELAY_MILLIS = 600;
    private final Handler handler;
    private final Runnable runnable;
    private ArrayList<TextView> textViews;
    private int indicator = -1;

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = View.inflate(context, R.layout.view_loading,null);
        TextView point0 = (TextView) view.findViewById(R.id.point0);
        TextView point1 = (TextView) view.findViewById(R.id.point1);
        TextView point2 = (TextView) view.findViewById(R.id.point2);
        TextView point3 = (TextView) view.findViewById(R.id.point3);
        TextView point4 = (TextView) view.findViewById(R.id.point4);
        TextView point5 = (TextView) view.findViewById(R.id.point5);
        textViews = new ArrayList<>();
        textViews.add(point0);
        textViews.add(point1);
        textViews.add(point2);
        textViews.add(point3);
        textViews.add(point4);
        textViews.add(point5);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                shuffleUI(indicator);
                if (indicator < 5) {
                    indicator++;
                } else {
                    indicator = -1;
                }
                handler.postDelayed(this, DELAY_MILLIS);
            }
        };
        shuffleUI(-1);
        addView(view);
    }

    private void shuffleUI(int i) {
        if (i == -1) {
            for (TextView textView : textViews) {
                textView.setVisibility(INVISIBLE);
            }
        } else {
            textViews.get(indicator).setVisibility(VISIBLE);
        }
    }

    public void startLoading(){
        handler.postDelayed(runnable, DELAY_MILLIS);
    }

    public void endLoading(){
        handler.removeCallbacks(runnable);
        shuffleUI(-1);
    }
}
