package com.vboss.okline.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.io.File;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Jiang Zhongyuan <br/>
 * Email   : jiangzhongyuan@okline.cn <br/>
 * Date    : 17/5/13 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class InitApkBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("InitApkBroadCastReceiver onReceive : " + intent);
        if (Intent.ACTION_MY_PACKAGE_REPLACED.equals(intent.getAction())) {
            deleteApkFile();
        }
        if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())
                || Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
            if (intent.getData().getSchemeSpecificPart().equals(context.getPackageName())) {  //Restart services.
                deleteApkFile();
            }
        }
    }

    private void deleteApkFile() {
        String filepath = DownloadActivity.filepath;
        if (!TextUtils.isEmpty(filepath)) {
            File file = new File(filepath);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
