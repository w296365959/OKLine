package com.vboss.okline.ui.user.customized;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vboss.okline.R;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/3/29
 * Summary : 进度显示自定义控件
 */

public class LoadingLayout extends LinearLayout {

    private TextView text;
    private LoadingView loadingview;
    private View view;
    private ImageView oklinelogo;
    private TextView textna;

    public LoadingLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        view = View.inflate(context, R.layout.view_loading_layout, null);
        text = (TextView) view.findViewById(R.id.text);
        loadingview = (LoadingView) view.findViewById(R.id.loadingview);
        oklinelogo = (ImageView) view.findViewById(R.id.oklinelogo);
        textna = (TextView) view.findViewById(R.id.text_nonavailable);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingLayout);
        int resourceId = typedArray.getResourceId(R.styleable.LoadingLayout_inner_text, R.string.nonavailable);
        text.setText(resourceId);
        oklinelogo.setVisibility(INVISIBLE);
        textna.setVisibility(INVISIBLE);
        addView(view);
    }

    public void startLoading(){
        oklinelogo.setVisibility(INVISIBLE);
        textna.setVisibility(INVISIBLE);
        loadingview.startLoading();
    }

    public void endLoadingOnSuccess(){
        loadingview.endLoading();
        oklinelogo.setVisibility(VISIBLE);
    }

    public void endLoadingOnFail(boolean visible, String text){
        loadingview.endLoading();
        if (!TextUtils.isEmpty(text) && visible) {
            textna.setVisibility(VISIBLE);
            textna.setText(text);
        }
    }

    public void setText(String s) {
        text.setText(s);
    }
}
