package com.vboss.okline.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * OKLine(ShenZhen) co.,Ltd.
 * 作者 : Shi Haijun
 * 邮箱 : haijun@okline.cn
 * 日期 : 2016/3/3 14:43
 * 描述 : Text方面的工具类
 */
public class TextUtils {
    /*
     * 检测是否是电话号码
     */
    public static boolean isPhoneNumber(String phoneNumber) {
        final String regex = "^((13[0-9])|(15[^4,\\D])|(18[0-1,5-9]))\\d{8}$";
        return Pattern.compile(regex).matcher(phoneNumber).matches();
    }

    public static boolean isChineseName(String name) {
        final String regx = "^[\u4e00-\u9fa5]{2,4}$";
        return Pattern.compile(regx).matcher(name).matches();
    }

    /**
     * 检测是否是短信验证码
     */
    public static boolean isSMSCode(String smsCode) {
        return isSixDigits(smsCode);
    }

    /**
     * 判断是不是6位数字
     */
    protected static boolean isSixDigits(String text) {
        final String regex = "^\\d{6}$";
        return Pattern.compile(regex).matcher(text).matches();
    }

    /**
     * 检测是否是有效的密码格式
     *
     * @param password
     * @return
     */
    public static boolean isValidPassword(String password) {
        return true;
    }

    /**
     * 判断是不是6位数字密码
     *
     * @param password
     * @return
     */
    public static boolean isNumberPassword(String password) {
        return isSixDigits(password);
    }

    /**
     * 检测是否是身证份号
     */
    public static boolean isIDNo(String idNo) {
        String len15="^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
        String len18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}[0-9Xx]$";

        return Pattern.compile(len15).matcher(idNo).matches() ||
                Pattern.compile(len18).matcher(idNo).matches();
    }

    /**
     * 检测是否是邮件地址
     */
    public static boolean isEmail(String email) {
        final String regex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

        return Pattern.compile(regex).matcher(email).matches();
    }

    /**
     * 将以分为单位的金额转化为以元为单位
     *
     * @param amountInFen 金额字串
     * @return 以元为单位的金额，如 12300 -> 123.00
     */
    public static String formatMoney(String amountInFen) {
        try {
            int amount = Integer.valueOf(amountInFen);
            return formatMoney(amount);
        } catch (NumberFormatException e) {
//            e.printStackTrace();

        }

        return "Error";
    }

    public static String formatMoney(int amountInFen) {
        float amount = amountInFen / 100f;
        return String.format(Locale.getDefault(),"%.2f", amount);
    }

    public static String formatMoney(float amountInFen) {
        float amount = amountInFen / 100f;
        return String.format(Locale.getDefault(),"%.2f", amount);
    }

    /**
     * 检测字串是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 显示或隐藏软键盘
     *
     * @param view
     * @param show
     */
    public static void showOrHideSoftIM(View view, boolean show) {
        if (view != null) {
            Context context = view.getContext();
            InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (show) {
                im.showSoftInputFromInputMethod(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            } else {
                im.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    /**
     * 显示或隐藏软键盘；当前已显示，则隐藏；否则显示。
     *
     * @param view
     */
    public static void toggleInputMethod(View view) {
        if (view != null) {
            Context context = view.getContext();
            InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            im.toggleSoftInputFromWindow(view.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
