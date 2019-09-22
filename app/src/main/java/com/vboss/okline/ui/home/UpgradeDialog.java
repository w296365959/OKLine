package com.vboss.okline.ui.home;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.data.entities.AppVersion;
import com.vboss.okline.ui.home.download.DownLoadManager;
import com.vboss.okline.ui.home.download.DownLoadService;

import butterknife.ButterKnife;
import timber.log.Timber;


/**
 * @author Mzc.wang
 * @version 2014年12月9日下午6:11:12
 */
public class UpgradeDialog extends Dialog {
    TextView titleView;
    TextView contentView;
    TextView rightBtn;
    TextView leftBtn;
    private AppVersion appVersion;

    public UpgradeDialog(Context context, AppVersion appVersion) {
        super(context);
        this.appVersion = appVersion;
        View view = LayoutInflater.from(context).inflate(R.layout.view_update_dialog, null);
        setContentView(view);
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ActionBar.LayoutParams.MATCH_PARENT;
        params.height = ActionBar.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        initView(view);
    }

    private void initView(View view) {
        titleView = ButterKnife.findById(view, R.id.titleView);
        contentView = ButterKnife.findById(view, R.id.contentView);
        rightBtn = ButterKnife.findById(view, R.id.rightBtn);
        leftBtn = ButterKnife.findById(view, R.id.leftBtn);
        String title;
        if (appVersion.getUpdateFlag() == AppVersion.FLAG_MUST) {
            title = getContext().getString(R.string.app_upgrade_dialog_title, appVersion.getVersionName());
            leftBtn.setVisibility(View.GONE);
            rightBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    download();
                }
            });
        } else {
            title = getContext().getString(R.string.app_upgrade_dialog_title_choose, appVersion.getVersionName());
            //update by luoxiuxiu 屏蔽左边的点击事件
//            leftBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dismiss();
//                }
//            });
            rightBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    download();
                }
            });
        }
        titleView.setText(title);
        String content = appVersion.getVersionMemo();
        Timber.tag("UpdateDialog").d("content = " + content);
        content = content.replaceAll("##", "\n");
        Timber.tag("UpdateDialog").d("content = " + content);
        if (TextUtils.isEmpty(content)) {
            contentView.setText("");
        } else {
            contentView.setText(content);
        }
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                }
                return true;
            }
        });
    }

    private void download() {
        DownloadActivity.intentIn(getContext(), "欧乐银联应用升级", appVersion.getVersionUrl(),appVersion.getVersionName());
    }


    public void setOnClick(View.OnClickListener onClick) {
        leftBtn.setOnClickListener(onClick);
    }
}
