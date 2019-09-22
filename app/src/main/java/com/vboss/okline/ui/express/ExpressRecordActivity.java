package com.vboss.okline.ui.express;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.data.model.ExpressModel;

import java.util.List;

/**
 * Okline(Hangzhou)co,Ltd<br/>
 * Author: wangzhongming<br/>
 * Email:  wangzhongming@okline.cn</br>
 * Date :  2017/7/6 15:17 </br>
 * Summary: 快递记录
 */

public class ExpressRecordActivity extends BaseActivity implements ExpressContact.ExpressView {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_record);
    }

    @Override
    public void setMyExpressList(List<ExpressModel> list) {

    }

    @Override
    public void setExpressCompany(List<ExpressModel.Company> list) {

    }

    @Override
    public void hideSelectCompany() {

    }

}
