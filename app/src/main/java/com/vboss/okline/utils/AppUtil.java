package com.vboss.okline.utils;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;

import com.vboss.okline.data.local.AppConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import timber.log.Timber;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Okline(Hangzhou)co,Ltd<br/>
 * Author: Mengyupeng<br/>
 * Email:  mengyupeng@okline.cn</br>
 * Date : $(DATE) </br>
 * Summary:
 */

public class AppUtil {


//    private static final int REQUEST_EXTERNAL_STORAGE = 1;
//    private static String[] PERMISSIONS_STORAGE = {
//            Manifest.permission.READ_PHONE_STATE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE};
//
//    public static void verifyStoragePermissions(Activity activity) {
//        // Check if we have write permission
//        int permission = ActivityCompat.checkSelfPermission(activity,
//                Manifest.permission.ACCESS_FINE_LOCATION);
//
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            // We don't have permission so prompt the user
//            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
//                    REQUEST_EXTERNAL_STORAGE);
//        }
//    }

    public static void saveBmpToLoalSdCard(Bitmap bmp, String dirPath, String imageName) {

        FileOutputStream out = null;
        // 判断是否可以对SDcard进行操作
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {    // 获取SDCard指定目录下

            creatDir(dirPath);
            File file = new File(dirPath, imageName);
            try {
                out = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }


    public static void creatDir(String path) {
        try {
            File dirFile = new File(path);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File createFile(String dirPath, String fileName) {
        File file = new File(dirPath, fileName);
        return file;

    }


    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        ;
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 发送与接收的广播
     **/
    public static String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    public static String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";

    public static void sendSMS(Activity mContext, String phoneNumber, String message) {
        Intent sentIntent = new Intent(SENT_SMS_ACTION);
        PendingIntent sentPI = PendingIntent.getBroadcast(mContext, 0, sentIntent,
                0);
        Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
        PendingIntent deliverPI = PendingIntent.getBroadcast(mContext, 0,
                deliverIntent, 0);
        //获取短信管理器
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        //拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(message);
        for (String text : divideContents) {
            smsManager.sendTextMessage(phoneNumber, null, text, sentPI, deliverPI);
        }
    }


    public static String getIMEI(Activity mContext) {
        String result = "";
        result = ((TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE))
                .getDeviceId() + "";
        return result;

    }

    public static String getIMSI(Activity mContext) {
        String result = "";
        result = ((TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE))
                .getSubscriberId() + "";
        return result;

    }

    /**
     * 调起系统发短信功能
     *
     * @param phoneNumber 手机号
     * @param message     短信内容
     */
    public static void doSendSMSTo(Context context, String phoneNumber, String message) {
        Timber.tag(AppUtil.class.getSimpleName()).i(" doSendSMSTo ");
        if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
            //获取短信管理器
            android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
            //拆分短信内容（手机短信长度限制）
            List<String> divideContents = smsManager.divideMessage(message);
            for (String text : divideContents) {
                smsManager.sendTextMessage(phoneNumber, null, text, null, null);
            }
        }
    }

    /**
     * check SIM Operator
     *
     * @param context Context
     * @return 1 移动  2 联通    3 电信
     */
    public static int checkSIMOperator(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMSI = telephonyManager.getSubscriberId();
        if (IMSI == null)
            return 0;
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
            return 1;
        } else if (IMSI.startsWith("46001") || IMSI.startsWith("46006")) {
            return 2;
        } else if (IMSI.startsWith("46003") ||IMSI.startsWith("46011") || IMSI.startsWith("46005") ||IMSI.startsWith("46020")) {
            return 3;
        }
        return 1;
    }

    public static void call(final Activity activity, final String phoneNumber) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, 100);
        } else {
            Uri uri = Uri.parse("tel:" + phoneNumber);
            Intent intent = new Intent(Intent.ACTION_CALL, uri);
            activity.startActivity(intent);
        }
    }

}
