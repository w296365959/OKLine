package com.vboss.okline.ui.user.customized;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vboss.okline.R;
/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/5/25
 * Summary : 在这里描述Class的主要功能
 */

public class SettingsButton extends LinearLayout {

    private final ImageView pic;

    private final TextView text;
    private final RelativeLayout rl;

    public SettingsButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = View.inflate(context, R.layout.view_settings_button, null);
        pic = (ImageView) view.findViewById(R.id.pic);
        text = (TextView) view.findViewById(R.id.text);
        rl = (RelativeLayout) view.findViewById(R.id.rl_parent);
        addView(view);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingsButton);
        int bgId = typedArray.getResourceId(R.styleable.SettingsButton_bg1, 0);
        rl.setBackgroundResource(bgId != 0?bgId:R.drawable.background_empty_solid_with_stroke);
        int resourceId = typedArray.getResourceId(R.styleable.SettingsButton_pic1, 0);
        if (resourceId != 0) {
            pic.setImageResource(resourceId);
        }
        float scale = typedArray.getFloat(R.styleable.SettingsButton_scale, 1.0f);
        pic.setScaleX(scale);
        pic.setScaleY(scale);
        int string = typedArray.getResourceId(R.styleable.SettingsButton_text1,R.string.app_name);
        text.setText(string);
    }

    public void setRlBackground(int resId,int picId){
        rl.setBackgroundResource(resId);
        pic.setImageResource(picId);
    }

    private static final String TAG = "SettingsButton";
}
