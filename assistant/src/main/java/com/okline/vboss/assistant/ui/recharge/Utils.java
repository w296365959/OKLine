package com.okline.vboss.assistant.ui.recharge;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.okline.vboss.assistant.R;

import java.lang.reflect.Method;

import rx.Subscription;
import timber.log.Timber;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/6/8
 * Summary : 在这里描述Class的主要功能
 */

public class Utils {

    public static int getDpi(Activity context) {
        int dpi = 0;
        Display display = context.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            dpi = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    public static void showLog(String TAG, String content) {
        Timber.tag(TAG).i("■■■" + content + "■■■");
    }

    public static String getPrintSize(long size) {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "GB";
        }
    }

    public static String getAmountText(int amout) {
        int i = amout / 100;
        int i1 = amout % 100;
        return "¥ "+i+"."+(i1==0?"00":i1);
    }

    /**
     * 根据项目内资源内容生成Uri对象
     *
     * @param context
     * @param res
     * @return
     */
    public static Uri getResourceUri(Context context, int res) {
        try {
            Context packageContext = context.createPackageContext(context.getPackageName(),
                    Context.CONTEXT_RESTRICTED);
            Resources resources = packageContext.getResources();
            String appPkg = packageContext.getPackageName();
            String resPkg = resources.getResourcePackageName(res);
            String type = resources.getResourceTypeName(res);
            String name = resources.getResourceEntryName(res);


            Uri.Builder uriBuilder = new Uri.Builder();
            uriBuilder.scheme(ContentResolver.SCHEME_ANDROID_RESOURCE);
            uriBuilder.encodedAuthority(appPkg);
            uriBuilder.appendEncodedPath(type);
            if (!appPkg.equals(resPkg)) {
                uriBuilder.appendEncodedPath(resPkg + ":" + name);
            } else {
                uriBuilder.appendEncodedPath(name);
            }
            return uriBuilder.build();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Toast customToast(Context context, CharSequence text, int duration) {
        Toast result = new Toast(context);

        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.transient_notification_assistant, null);
        TextView tv = (TextView) v.findViewById(R.id.tv_custom_toast);
        String string = "操作出错";
        if (text != null) {
            string = text.toString();
            if (string.contains("Failed to connect")) {
                string = "当前网络不可用，请检查您的网络";
            }
        }
        tv.setText(string);

        result.setView(v);
        result.setGravity(Gravity.CENTER, 0, 0);
        result.setDuration(duration);
        return result;
    }

    public static void setImageWithRoundCorner(String uriString, SimpleDraweeView simpleDraweeView) {
        //构建Controller
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                //设置需要下载的图片地址
                .setUri(Uri.parse(uriString))
                //设置点击重试是否开启
                .setTapToRetryEnabled(true)
                //构建
                .build();

        //设置Controller
        simpleDraweeView.setController(controller);

        RoundingParams roundingParams = RoundingParams.fromCornersRadius(2f);
        roundingParams.setCornersRadius(10f);
        simpleDraweeView.getHierarchy().setRoundingParams(roundingParams);
    }

    public static String transformatTime(long l, int style) {
        String string = "";
        switch (style) {
            case 0:
                string = DateFormat.format("yyyy年MM月dd日 kk:mm:ss", l).toString();
                break;
            case 1:
                string = DateFormat.format("MM-dd kk:mm:ss", l).toString();
                break;
            case 2:
                string = DateFormat.format("yyyy-MM-dd kk:mm:ss", l).toString();
                break;
            case 3:
                string = DateFormat.format("yyyy年MM月dd日 kk:mm:ss SSS", l).toString();
                break;
            case 4:
                string = DateFormat.format("yyyyMMddkkmmssSSS", l).toString();
                break;
        }
        return string;
    }

    public static void unsubscribeRxJava(Subscription defaultSubscribe) {
        if (defaultSubscribe != null) {
            defaultSubscribe.unsubscribe();
        }
    }

    public static boolean isNetWorkConnected(Context context) {
        boolean result;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public static void hideSoftKeyboard(Activity activity) {
        /**隐藏软键盘**/
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 显示缩略图
     *
     * @param draweeView     draweeView
     * @param url            url
     * @param resizeWidthDp  resizeWidth
     * @param resizeHeightDp resizeHeight
     * @param cornersRadius
     */
    public static void showThumb(SimpleDraweeView draweeView, String url, int resizeWidthDp, int resizeHeightDp, float cornersRadius) {
        if (url == null || "".equals(url))
            return;
        if (draweeView == null)
            return;
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setResizeOptions(new ResizeOptions(Utils.dip2px(draweeView.getContext(), resizeWidthDp), Utils.dip2px(draweeView.getContext(), resizeHeightDp)))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(draweeView.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        draweeView.setController(controller);

        RoundingParams roundingParams = RoundingParams.fromCornersRadius(2f);
        roundingParams.setCornersRadius(cornersRadius);
        draweeView.getHierarchy().setRoundingParams(roundingParams);
    }
}
