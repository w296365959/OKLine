package com.vboss.okline.ui.contact.TransferAccounts;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.runtimepermissions.PermissionsManager;
import com.vboss.okline.runtimepermissions.PermissionsResultAction;
import com.vboss.okline.ui.app.App;
import com.vboss.okline.ui.app.adapter.CommonAdapter;
import com.vboss.okline.ui.app.adapter.ViewHolder;
import com.vboss.okline.ui.auth.VerifySuccessActivity;
import com.vboss.okline.ui.contact.ContactsUtils;
import com.vboss.okline.view.widget.CommonDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: Yuan shaoyu <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/5/7 10:51 <br/>
 * Summary  : 转账支付完成后的记录界面
 */

public class TransRecordActivity extends BaseActivity{
    private static final String TAG = "TransRecordActivity";

    @BindView(R.id.fragment_toolbar)
    FragmentToolbar toolbar;

    @BindView(R.id.trans_record_recyclerView)
    RecyclerView trans_record_recyclerView;
    CommonAdapter<App> adapter;
    List<App> transRecordList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_trans_record);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initToolBar();
        initAdapter();
        initData();
    }
    private void initData() {
        for (int i = 0; i <3 ; i++) {
            transRecordList.add(new App());
        }
        adapter.setmDatas(transRecordList);
        adapter.notifyDataSetChanged();
    }

    private void initAdapter() {
        transRecordList = new ArrayList<>();
        adapter = new CommonAdapter<App>(TransRecordActivity.this,R.layout.trans_recordtime_item_layout,transRecordList) {
            @Override
            public void convert(ViewHolder holder, App app, int position) {

            }
        };
        trans_record_recyclerView.setLayoutManager(new LinearLayoutManager(TransRecordActivity.this));
        trans_record_recyclerView.setAdapter(adapter);
    }


    private void initToolBar() {
        toolbar.setActionTitle("转账记录");
        toolbar.setActionLogoIcon(R.mipmap.orange_logo);
        toolbar.setActionMenuVisible(View.GONE);
        toolbar.setNavigationVisible(View.GONE);
    }
}