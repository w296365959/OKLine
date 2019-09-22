package com.okline.vboss.assistant.utils.shadow;

import android.content.Context;
import android.support.v4.app.ActivityCompat;

import com.okline.vboss.assistant.R;
import com.okline.vboss.assistant.utils.StringUtils;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/4/11 19:07 <br/>
 * Summary  : 在这里描述Class的主要功能
 */

public class ShadowHelper {
    private Context mContext;
    static ShadowHelper instance;

    private ShadowHelper(Context context) {
        this.mContext = context;
    }

    public static ShadowHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (ShadowHelper.class) {
                if (instance == null) {
                    instance = new ShadowHelper(context);
                }
            }
        }
        return instance;
    }

    /**
     * view layer
     *
     * @param color        resId color
     * @param shadowDy     float shadow dy
     * @param shadowRadius float shadow radius
     * @return ShadowProperty
     */
    public ShadowProperty getShadowProperty(int color, float shadowDy, float shadowRadius) {
        return new ShadowProperty().setShadowColor(color)
                .setShadowDy(StringUtils.dip2px(mContext, shadowDy))
                .setShadowRadius(StringUtils.dip2px(mContext, shadowRadius));
    }


    /**
     * view layer
     *
     * @return ShadowProperty
     */
    public ShadowProperty getShadowProperty() {
        return getShadowProperty(ActivityCompat.getColor(mContext, R.color.colorShadow), 0.25f, 2f);
    }

    /**
     * get shadow radius
     *
     * @return float
     */
    public float getShadowRadius() {
        return getShadowRadius(7.5f);
    }

    /**
     * get shadow radius
     *
     * @param rx float
     * @return float
     */
    public float getShadowRadius(float rx) {
        return StringUtils.dip2px(mContext, rx);
    }

}
