package com.okline.vboss.assistant.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by admin on 15-8-10.
 */
public class StringUtils {

    // 随机字符集
    private static final char[] RANDOM_CHAR_ARRAY =
            ("0123456789"
                    + "abcdefghijklmnopqrstuvwxyz"
                    + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    + "~!@#$%^&*()_+-=[]{}<>/?|,.;:'`"
            ).toCharArray();

    //SD卡路径
    public static String sdPath = Environment.getExternalStorageDirectory().getPath() + "/";

    /**
     * 判斷字符是否為空
     *
     * @param text
     * @return
     */
    public static boolean isNullString(String text) {
        if (text == null || "".equals(text.trim()) || text.equalsIgnoreCase("null") || text.equals("\"null\""))
            return true;

        return false;
    }

    /**
     * 比较两个字符串是否相等
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean compare(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        } else if (str1 != null && str2 != null) {
            return str1.equals(str2);
        } else {
            return false;
        }
    }

    /**
     * 收起软键盘并设置提示文字
     */
    public static void collapseSoftInputMethod(Context context, EditText inputText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputText.getWindowToken(), 0);
//        if (imm.isActive()) { // 如果开启
//            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
//        }
    }

    public static String getJabber(String from) {
        if (isNullString(from)) return null;
        String[] res = from.split("@");
        return res[0].toLowerCase();
    }

    public static String getJabberID(String from) {
        if (isNullString(from)) return null;
        String[] res = from.split("/");
        return res[0].toLowerCase();
    }

    /**
     * 当前适配width
     *
     * @return
     */
    public static int getScreenWidth(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 当前适配height
     *
     * @return
     */
    public static int getScreenHeight(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }


    /**
     * 新建文件路径
     *
     * @param path
     * @return
     */
    public static String creatFile(String path) {
        path = sdPath + path;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 删除文件
     *
     * @param path
     */
    public static void deleteFile(String path) {
        if (path != null) {
            File dirFile = new File(path);
            if (!dirFile.exists()) {
                return;
            }

            if (dirFile.isDirectory()) {
                String[] children = dirFile.list();

                for (int i = 0; i < children.length; ++i) {
                    (new File(dirFile, children[i])).delete();
                }
            }

            dirFile.delete();
        }
    }

    /**
     * 实现中英文混时得到长度 过长字符替换
     *
     * @param origin 原始字符串
     * @param len    截取长度(一个汉字长度按2算的)
     * @param c      后缀     例如：标题字数显示判断,大于10个字点点点  那么：String c="..."
     * @return 返回的字符串
     */
    public static String tosubstring(String origin, int len, String c) {
        if (origin == null || origin.equals("") || len < 1)
            return "";
        byte[] strByte = new byte[len];
        if (len > strlength(origin)) {
            return origin + c;
        }
        try {
            System.arraycopy(origin.getBytes("GBK"), 0, strByte, 0, len);
            int count = 0;
            for (int i = 0; i < len; i++) {
                int value = (int) strByte[i];
                if (value < 0) {
                    count++;
                }
            }
            if (count % 2 != 0) {
                len = (len == 1) ? ++len : --len;
            }
            origin = "";
            return new String(strByte, 0, len, "GBK") + c;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
     *
     * @param s 需要得到长度的字符串
     * @return i得到的字符串长度
     */
    public static int strlength(String s) {
        if (s == null)
            return 0;
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }

    public static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }

    /**
     * 输入的中英文总的字符数（中文--2个字符，英文--1个字符）
     *
     * @param validateStr
     * @return
     */
    public static int getChineseLength(String validateStr) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        //  获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
        for (int i = 0; i < validateStr.length(); i++) {
            //  获取一个字符
            String temp = validateStr.substring(i, i + 1);
            //  判断是否为中文字符
            if (temp.matches(chinese)) {
                //  中文字符长度为2
                valueLength += 2;
            } else {
                //   其他字符长度为1
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * 获取字符串字符数
     */
    public static int getBytesLength(String string) {
        int length = 0;
        try {
            byte[] gbks = string.getBytes("GBK");
            length = gbks.length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return length;
    }


    /**
     * 根据时间获取字符+两位随机字符组成唯一的随机字符
     *
     * @return
     */
    public static String getRandomString() {
        int charSize = RANDOM_CHAR_ARRAY.length;
        long time = System.currentTimeMillis();
        String result = "";

        // 将当前时间转为字符串
        while (time > 0) {
            int charNum = (int) (time % charSize);
            result = RANDOM_CHAR_ARRAY[charNum] + result;
            time = time / charSize;
        }

        // 添加两位随机字符
        int random = (int) (Math.random() * 10000);
        result = result + RANDOM_CHAR_ARRAY[random % charSize];
        result = result + RANDOM_CHAR_ARRAY[(random / charSize) % charSize];

        return result;
    }

    public static String getDate(long date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(date));
    }

    /**
     * 将px值转换为dip或dp值,保证尺寸大小不变
     * (DisplayMetrics类中属性density)
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值,保证尺寸大小不变
     * (DisplayMetrics类中属性density)
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值, 保证文字大小不变
     * (DisplayMetrics类中属性density)
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值, 保证文字大小不变
     * (DisplayMetrics类中属性density)
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * Convert Dp to Pixel
     */
    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }


    /**
     * format amount
     *
     * @param amountInFen amount
     * @return String
     */
    public static String formatMoney(int amountInFen) {
        float amount = amountInFen / 100f;
        return String.format(Locale.getDefault(), "%.2f", amount);
    }


}
