package com.vboss.okline.ui.app;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


import java.util.ArrayList;
import java.util.List;

/**
 * 获取本地所有App
 */
public class LocalApp {
    private Context context;
    private List<App> allLocalApp;

    public LocalApp(Context context) {
        this.context = context;
    }

    public List<App> getAppPackage() {
        allLocalApp = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packages = packageManager.getInstalledPackages(0);

        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            App cardApp = new App();
            cardApp.setAppName(packageInfo.applicationInfo.loadLabel(packageManager).toString());//App名字
            cardApp.setVersion(packageInfo.versionCode);
            cardApp.setComponent(packageInfo.packageName);//应用包名
            // 过滤系统程序
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                allLocalApp.add(cardApp);
            }else if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0){
                // 表示是系统程序，但用户更新过，也算是用户安装的程序
                allLocalApp.add(cardApp);
            }
        }
        return allLocalApp;

    }


    public String isDownload(List<App> list, String appName) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getAppName().equals(appName)) {
                String component=list.get(i).getComponent();
                return component;
            }
        }
        return null;
    }

}
