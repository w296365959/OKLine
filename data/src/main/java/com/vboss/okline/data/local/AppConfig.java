package com.vboss.okline.data.local;

import android.os.Environment;

import java.io.File;

/**
 * Okline(Hangzhou)co,Ltd<br/>
 * Author: Mengyupeng<br/>
 * Email:  mengyupeng@okline.cn</br>
 * Date : $(DATE) </br>
 * Summary:一些全部配置，文件路径
 */
public class AppConfig {


    //图片保存目录
    public static String ROOT_PATH= Environment.getExternalStorageDirectory().getAbsolutePath();
    public static String IMAGE_CACHE_DIR= ROOT_PATH+File.separator+"oklineimage"+File.separator;
    public static String IMAGE_ID_PIC="image_id_pic.jpg";//身份证图片保存目录
    public static String IMAGE_ID_AND_PERSON="image_and_person.jpg";//个人手持身份证照片

    //Sp保存目录

    public static String SP_DIR="ouline_sp";

    public static String SP_ID_NAME="id_name";
    public static String SP_ID_NUM="id_num";

    public static String SP_OL_NUMBER= "ol_number";
    public static String SP_EASE_NUMBER= "ease_login_number";
    public static String SP_PHONE_SMS = "phone_sms";//发送短信的手机号
    public static String SP_IN_FIRST = "is_inFirst";//是否第一次进入应用


}
