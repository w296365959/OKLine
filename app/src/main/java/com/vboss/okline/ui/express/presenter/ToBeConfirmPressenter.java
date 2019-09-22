package com.vboss.okline.ui.express.presenter;

import android.content.Context;

import com.vboss.okline.ui.express.ExpressContact;

/**
 * Okline(Hangzhou)co,Ltd<br/>
 * Author: wangzhongming<br/>
 * Email:  wangzhongming@okline.cn</br>
 * Date :  2017/7/7 14:16 </br>
 * Summary:ToBeConfirmFragment对应Presenter
 */

public class ToBeConfirmPressenter extends ExpressPresenter implements ExpressContact.ToBeConfirmPresenter{

    public ToBeConfirmPressenter(Context context, ExpressContact.ExpressView expressView) {
        super(context, expressView);
    }
}
