package com.vboss.okline.ui.user.customized;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
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
 * Date    : 2017/4/1
 * Summary : 自定义控件
 */

public class OcardButton extends LinearLayout {

    private final LinearLayout ll_ocard_button;
    private int resourceId2;
    private int resourceId1;
    private int resourceId0;
    private TextView text;
    private ImageView button_image;
    private int status;

    public OcardButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = View.inflate(context, R.layout.view_ocard_button, null);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.OcardButton);
        button_image = (ImageView) view.findViewById(R.id.button_image);
        ll_ocard_button = (LinearLayout) view.findViewById(R.id.ll_ocard_button);
        resourceId0 = typedArray.getResourceId(R.styleable.OcardButton_button_img0, R.drawable.lost1);
        resourceId1 = typedArray.getResourceId(R.styleable.OcardButton_button_img1, R.drawable.lost1);
        resourceId2 = typedArray.getResourceId(R.styleable.OcardButton_button_img2, R.drawable.lost1);
        text = (TextView) view.findViewById(R.id.text);
        text.setText(typedArray.getResourceId(R.styleable.OcardButton_button_text,R.string.absence_declaration));
        addView(view);
    }

    private void setTextColor(@ColorInt int color) {
        text.setTextColor(color);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        switch (status) {
            case 0:
                ll_ocard_button.setBackgroundResource(R.drawable.background_fff3);
                button_image.setImageResource(resourceId0);
                setTextColor(Color.parseColor("#f4f4f4"));
                setClickable(false);
                break;
            case 1:
                ll_ocard_button.setBackgroundResource(R.drawable.background_fff3);
                button_image.setImageResource(resourceId1);
                setTextColor(getResources().getColor(R.color.brick_red));
                setClickable(true);
                break;
            case 2:
                ll_ocard_button.setBackgroundResource(R.drawable.background_brick);
                button_image.setImageResource(resourceId2);
                setTextColor(Color.parseColor("#ffffff"));
                setClickable(false);
                break;
        }
    }
}
