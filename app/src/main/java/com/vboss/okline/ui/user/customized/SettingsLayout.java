package com.vboss.okline.ui.user.customized;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
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
 * Date    : 2017/3/28
 * Summary : 设定项目自定义控件
 */

public class SettingsLayout extends LinearLayout {
    public SettingsLayout(Context context) {
        super(context);
    }

    public SettingsLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = View.inflate(context, R.layout.view_settings_layout,null);
        ImageView pic = (ImageView) view.findViewById(R.id.view_settings_layout_pic);
        ImageView arrow = (ImageView) view.findViewById(R.id.view_settings_layout_arrow);
        TextView text = (TextView) view.findViewById(R.id.view_settings_layout_text);

        addView(view);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingsLayout);
        int resourceId = typedArray.getResourceId(R.styleable.SettingsLayout_pic, R.drawable.nav_app);
        pic.setImageResource(resourceId);
        int string = typedArray.getResourceId(R.styleable.SettingsLayout_text,R.string.app_name);
        text.setText(string);
    }
}
