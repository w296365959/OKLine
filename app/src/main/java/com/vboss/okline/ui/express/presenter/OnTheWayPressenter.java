package com.vboss.okline.ui.express.presenter;

import android.content.Context;
import android.widget.TextView;

import com.vboss.okline.ui.express.ExpressContact;

/**
 * Okline(Hangzhou)co,Ltd<br/>
 * Author: wangzhongming<br/>
 * Email:  wangzhongming@okline.cn</br>
 * Date :  2017/7/7 14:16 </br>
 * Summary:OnTheWayFragment对应Presenter
 */

public class OnTheWayPressenter extends ExpressPresenter implements ExpressContact.OnTheWayPresenter{
    private  ExpressContact.ExpressView expressView;

    public OnTheWayPressenter(Context context, ExpressContact.ExpressView expressView) {
        super(context, expressView);

        this.expressView=expressView;
    }


}
