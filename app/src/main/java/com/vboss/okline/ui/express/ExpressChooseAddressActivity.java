package com.vboss.okline.ui.express;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.ui.contact.myCard.EditDeliveryInfoFragment;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/7/6 14:06 <br/>
 * Summary  : choose express address
 */

public class ExpressChooseAddressActivity extends BaseActivity {
    private static final String TAG = ExpressChooseAddressActivity.class.getSimpleName();

    public static final String KEY_LOCATION_ADDRESS = "location_address";
    public static final String KEY_LOCATION_MOBILE = "mobile";
    public static final String KEY_LOCATION_AREA = "location_area";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jpush);

        Fragment instance = EditDeliveryInfoFragment.newInstance(TAG,
                getIntent().getStringExtra(KEY_LOCATION_AREA),
                getIntent().getStringExtra(KEY_LOCATION_ADDRESS),
                getIntent().getStringExtra(KEY_LOCATION_MOBILE));
        getSupportFragmentManager().beginTransaction().replace(R.id.container_notice, instance).commit();
    }
}
