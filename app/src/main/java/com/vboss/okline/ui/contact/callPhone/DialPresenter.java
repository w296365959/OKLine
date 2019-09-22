package com.vboss.okline.ui.contact.callPhone;

import android.content.Context;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/5/4 19:54
 * Desc :
 */

public class DialPresenter extends DialContract.DialPresenter {
    DialContract.Model mModel;
    DialContract.View mView;
    Context mContext;

    public DialPresenter(DialContract.Model model, DialContract.View view, Context context){
        onAttached();
        mModel = model;
        mView = view;
        mContext = context;

    }
    @Override
    public void onAttached() {

    }


}
