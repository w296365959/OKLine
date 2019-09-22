package com.vboss.okline.data;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import timber.log.Timber;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/7 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class RepoUtils {
    /**
     * 检查页码和页数据条数
     *
     * @param pageIndex
     * @param pageSize
     */
    public static void checkPageIndexAndSize(int pageIndex, int pageSize) {
        Preconditions.checkArgument(pageIndex >= 0, "PageIndex must >= 0");
        if (pageIndex != 0) {
            Preconditions.checkArgument(pageSize > 0, " PageSize must > 0");
        }
    }

    public static String buildLikeStatement(String field, Object value) {
        return field + " LIKE '%" + value + "%'";
    }

    /**
     * 获取启动已安装应用的主Activity信息
     *
     * @param context
     * @return
     */
    public static Map<String, String> getMainIntentActivities(Context context) {
        List<ResolveInfo> list = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        list.addAll(context.getPackageManager().queryIntentActivities(intent, 0));
        Timber.i("MainIntentActivities Size =  %d", list.size());
        intent.removeCategory(Intent.CATEGORY_LAUNCHER);
        intent.addCategory(Intent.CATEGORY_INFO);
        list.addAll(context.getPackageManager().queryIntentActivities(intent, 0));
        Timber.i("MainIntentActivities Size =  %d", list.size());
        Map<String, String> pkgActMap = new HashMap<>(list.size());
        for (ResolveInfo info : list) {
            pkgActMap.put(info.activityInfo.packageName,
                    new ComponentName(info.activityInfo.packageName, info.activityInfo.name).flattenToString());
        }
        if (BuildConfig.DEBUG) {
            Set<String> keySet = pkgActMap.keySet();
            for (String pkgName : keySet) {
                Timber.i("[%s, %s]", pkgName, pkgActMap.get(pkgName));
            }
        }
        return pkgActMap;
    }
}
