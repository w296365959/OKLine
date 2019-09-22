package com.vboss.okline.ui.app;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.okline.vboss.assistant.base.Config;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.data.entities.User;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: yuan shaoyu  <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/6/14 <br/>
 * summary  :在这里描述Class的主要功能
 */

public class AppHelper {
    //eventBus key
    public static final String EVENT_APP_REFRESH = "refresh_app";

    public static void startAssistant(Context context,User user){
        Log.i("App",user.toString());
        String olNo = user.getOlNo();
        String address = user.getBhtAddress();
        String phone = user.getPhone();
        String realName = user.getRealName();
//                            Intent intent = activity.getPackageManager().getLaunchIntentForPackage("com.okline.vboss.assiant");
//                            intent.addCategory(Intent.CATEGORY_LAUNCHER);
        Intent intent = new Intent(context, com.okline.vboss.assistant.ui.MainActivity.class);
        intent.putExtra(Config.KEY_OLNO,olNo);
        intent.putExtra(Config.KEY_ADDRESS,address);
        intent.putExtra(Config.KEY_MOBILE,phone);
        intent.putExtra(Config.KEY_USER_NAME,realName);
        intent.putExtra(Config.KEY_OCARD_STATE, BaseActivity.getOcardState());
        context.startActivity(intent);
    }
}
