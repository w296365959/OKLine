package com.vboss.okline.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.vboss.okline.BuildConfig;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.entities.AppVersion;
import com.vboss.okline.data.entities.CheckInfo;
import com.vboss.okline.data.remote.AppRemoteDataSource;
import com.vboss.okline.ui.auth.present.RzContact;

import java.io.File;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Jiang Zhongyuan <br/>
 * Email   : jiangzhongyuan@okline.cn <br/>
 * Date    : 17/5/12 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class AppUpgradeHelper {

    private RzContact.IResultUpdate iResult;
    private Activity activity;

    public AppUpgradeHelper(Activity activity, RzContact.IResultUpdate update){
        this.activity = activity;
        this.iResult = update;
    }

    public AppUpgradeHelper(){

    }

    public void checkAppVersion(final Activity activity) {
        CheckInfo[] checkInfos = new CheckInfo[]{new CheckInfo(AppVersion.TYPE_OL_APP, BuildConfig.VERSION_NAME)};
        AppRemoteDataSource.getInstance()
                .checkVersionRequest("1", checkInfos)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<List<AppVersion>>("AppUpgradeHelper") {
                    @Override
                    public void onNext(List<AppVersion> appVersions) {
                        for (AppVersion appVersion : appVersions) {
                            if (appVersion.getVersionType() == AppVersion.TYPE_OL_APP) {
                                if (appVersion.getUpdateFlag() == AppVersion.FLAG_CHOOSE
                                        || appVersion.getUpdateFlag() == AppVersion.FLAG_MUST) {
                                    iResult.getResultUpdate(appVersion);
//                                    showUpgradeDialog(activity, appVersion);
                                }else{
                                    iResult.getResultUpdate(null);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

//    public void showUpgradeDialog(Context context, AppVersion appVersion) {
//        UpgradeDialog dialog = new UpgradeDialog(context, appVersion);
//        dialog.show();
//    }

    public void installApk(DownloadActivity context, String filePath) {

        File apkFile = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        context.finish();
    }

}
