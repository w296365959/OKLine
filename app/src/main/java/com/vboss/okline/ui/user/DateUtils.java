package com.vboss.okline.ui.user;

import com.vboss.okline.ui.card.adapter.CardLogAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/4/21
 * Summary : 在这里描述Class的主要功能
 */

public class DateUtils {
    private static final String TAG = "DateUtils";
    public static final int INT = 24 * 60 * 60 * 1000;
    public static SimpleDateFormat format0 = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat format1 = new SimpleDateFormat(
            "yyyy年MM月dd日 HH:mm:ss");
    public static SimpleDateFormat format2 = new SimpleDateFormat(
            "H:mm:ss");
    public static SimpleDateFormat format3 = new SimpleDateFormat(
            "M月d日 HH:mm:ss");
    public static SimpleDateFormat format4 = new SimpleDateFormat(
            "yyyy年M月d日 HH:mm");
    public static SimpleDateFormat format5 = new SimpleDateFormat(
            "HH:mm");
    public static SimpleDateFormat format6 = new SimpleDateFormat(
            "yyyy年M月d日");

    public static long getTimeMillis(String date) {
        long timeMillis = 0;
        String[] strings = date.split("-");
        Date date1 = new Date(Integer.valueOf(strings[0]) - 0, Integer.valueOf(strings[1]) - 1, Integer.valueOf(strings[2]));
        timeMillis = date1.getTime();
        return timeMillis;
    }

    public static String processDate(String date) {
        String ret = null;
        long t = getTimeMillis(date) / INT;
        String transformatTime = Utils.transformatTime(System.currentTimeMillis(), 2);
        long l = getTimeMillis(transformatTime.substring(0, transformatTime.indexOf(" "))) / INT;
        long l1 = l - t;
        if (l1 == 0) {
            ret = "今天";
        } else if (l1 == 1) {
            ret = "昨天";
        } else {
            ret = CardLogAdapter.dateFormat(date);
        }
        return ret;
    }

    public static String getYearMonth(String date) {
        String year = date.substring(0, 4);
        String month = date.substring(5, 7);
        String result = year + "年" + month + "月";
        String day = null;
        if (date.length() > 8) {
            day = date.substring(8, date.length());
            result += day + "日";
        }
        return result;
    }

    public static String getMonthDay(String data) {
        String result = null;
        try {
            Date date = format0.parse(data);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            result = month + "月" + day + "日";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getHmmss(String data) {
        String result = null;
        try {
            Date date = format0.parse(data);
            result = format2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getHmm(String data) {
        String result = null;
        try {
            Date date = format0.parse(data);
            result = format5.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getDefaultDate(String data) {
        String result = data;
        try {
            Date date = format0.parse(data);
            result = format1.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getMdHHmmss(String data) {
        String result = data;
        try {
            Date date = format0.parse(data);
            result = format3.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getyyMdHHmmss(String data) {
        String result = data;
        try {
            Date date = format0.parse(data);
            result = format4.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean isTheSameDay(Calendar calendar, Calendar other) {
        if (calendar.get(Calendar.YEAR) != other.get(Calendar.YEAR)) {
            return false;
        }
        if (calendar.get(Calendar.MONTH) != other.get(Calendar.MONTH)) {
            return false;
        }
        if (calendar.get(Calendar.DAY_OF_MONTH) != other.get(Calendar.DAY_OF_MONTH)) {
            return false;
        }
        return true;
    }

    public static boolean isYesterday(Calendar today, Calendar other) {
        today.setTimeInMillis(today.getTimeInMillis() - 24 * 3600 * 1000);
        return isTheSameDay(today, other);
    }

    public static String getOrgaListDate(long timeStamp) {
        return getOrgaListDate(format0.format(new Date(timeStamp)));
    }

    public static String getOrgaListDate(String data) {
        data = data+":00";
        String result = data;
        try {
            Date date = format0.parse(data);
            Calendar calendarToday = Calendar.getInstance();
            calendarToday.setTime(new Date());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            if (isTheSameDay(calendarToday, calendar)) {
                return "今天 "+format5.format(date);
            } else if (isYesterday(calendarToday, calendar)) {
                return "昨天 "+format5.format(date);
            } else {
                return format6.format(date)+" "+format5.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
