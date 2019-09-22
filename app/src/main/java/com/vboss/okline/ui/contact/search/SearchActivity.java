package com.vboss.okline.ui.contact.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.helper.ToolbarSearchHelper;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/3/29 19:44
 * Desc :
 */
public class SearchActivity extends BaseActivity{
    private static final String TAG = "SearchActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_search);
        initToolbar();
    }

    private void initToolbar() {
        ToolbarSearchHelper toolbarHelper = new ToolbarSearchHelper(this);
        toolbarHelper.enableHomeAsUp(true);
        toolbarHelper.setOnActionSearchListener(new ToolbarSearchHelper.OnActionSearchListener() {
            @Override
            public void onSearch(String key) {
                Log.i(TAG, "key: " + key);
            }
        });
        toolbarHelper.setNavigationListener(new ToolbarSearchHelper.OnNavigationClickListener() {
            @Override
            public void onNavigationClick() {
                onBackPressed();
            }
        });
    }
}
