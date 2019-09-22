package com.vboss.okline.utils;

import android.content.Context;


import com.vboss.okline.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * OkLine(ShenZhen) co., Ltd.<br/>
 * Author: Shi Haijun<br/>
 * Email : haijun@okline.cn<br/>
 * Date  : 2016/4/11 11:55<br/>
 * Desc  :
 */
public class TimeUtils {

    /**
     * 获取当前系统时间
     *
     * @param template 输出的时间格式
     * @return  返回{@code Template}格式的时间字串
     */
    public static String getCurrentTime(String template){
        SimpleDateFormat format = new SimpleDateFormat(template, Locale.getDefault());

        return format.format(new Date(System.currentTimeMillis()));
    }

    public static Long format2long(String template, String time){
        SimpleDateFormat sdf = new SimpleDateFormat(template);
        // 此处会抛异常
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 获取毫秒数
         long longDate = date.getTime();
        return longDate;
    }

    /**
     * 获取当前系统时间的毫秒数
     */
    public static long getCurrentTimeInMS() {
        return System.currentTimeMillis();
    }

    /**
     * 将毫秒数解析成{@code template}格式的时间
     *
     * @param template  时间格式
     * @param timeInMs  毫秒数表示的时间
     * @return  返回{@code template}格式的时间字串
     */
    public static String parseTime(String template, long timeInMs) {
        SimpleDateFormat format = new SimpleDateFormat(template, Locale.CHINA);
        return format.format(new Date(timeInMs));
    }

    /**
     * 把日期时间字串转化为targetTemplate形式的时间字串。
     *
     * @param sourceTemplate timestamp的日期格式
     * @param targetTemplate 目标日期格式
     * @param timestamp      日期字串
     * @return 返回targetTemplate格式的时间字串
     */
    public static String parseTime(String sourceTemplate, String targetTemplate, String timestamp) {
        SimpleDateFormat format = new SimpleDateFormat(targetTemplate, Locale.CHINA);
        format.applyLocalizedPattern(sourceTemplate);
        try {
            Date date = format.parse(timestamp);
            format.applyPattern(targetTemplate);
            return format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }
    /**
     * 返回unix时间戳 (1970年至今的秒数)
     * @return
     */
    public static long getUnixStamp(){
        return System.currentTimeMillis()/1000;
    }

    /**
     * 得到昨天的日期
     * @return
     */
    public static String getYestoryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String yestoday = sdf.format(calendar.getTime());
        return yestoday;
    }

    /**
     * 得到今天的日期
     * @return
     */
    public static String getTodayDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        return date;
    }

    /**
     * 时间戳转化为时间格式
     * @param timeStamp
     * @return
     */
    public static String timeStampToStr(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(timeStamp * 1000);
        return date;
    }

    /**
     * 得到日期   yyyy-MM-dd
     * @param timeStamp  时间戳
     * @return
     */
    public static String formatDate(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(timeStamp*1000);
        return date;
    }

    /**
     * 得到时间  HH:mm:ss
     * @param timeStamp   时间戳
     * @return
     */
    public static String getTime(long timeStamp) {
        String time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(timeStamp * 1000);
        String[] split = date.split("\\s");
        if ( split.length > 1 ){
            time = split[1];
        }
        return time;
    }

    /**
     * 将一个时间戳转换成提示性时间字符串，如刚刚，1秒前
     *
     * @param timeStamp
     * @return
     */
    public static String convertTimeToFormat(long timeStamp) {
        long curTime = System.currentTimeMillis() / (long) 1000 ;
        long time = curTime - timeStamp;

        if (time < 60 && time >= 0) {
            return "刚刚";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "小时前";
        } else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {
            return time / 3600 / 24 + "天前";
        } else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 + "个月前";
        } else if (time >= 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 / 12 + "年前";
        } else {
            return "刚刚";
        }
    }
    public static String formatTime(long timeStamp) {
        long curTime = System.currentTimeMillis();
        long time = (curTime - timeStamp)/(long)1000;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.CHINA);
        SimpleDateFormat format1 = new SimpleDateFormat("yy/MM/dd", Locale.CHINA);
        if (time < 3600 * 24 && time >= 0){
            return format.format(new Date(timeStamp));
        }else if(time > 3600 * 24 && time < 3600 * 24 * 2){
            return "昨天";
        }else{
            return format1.format(new Date(timeStamp));
        }
    }

    /**
     * 将一个时间戳转换成提示性时间字符串，(多少分钟)
     *
     * @param timeStamp
     * @return
     */
    public static String timeStampToFormat(long timeStamp) {
        long curTime = System.currentTimeMillis() / (long) 1000 ;
        long time = curTime - timeStamp;
        return time/60 + "";
    }

    public static String getTodayOrYesterday2(long date, Context context) {//date 是存储的时间戳
        //所在时区时8，系统初始时间是1970-01-01 80:00:00，注意是从八点开始，计算的时候要加回去
        int offSet = Calendar.getInstance().getTimeZone().getRawOffset();
        long today = (System.currentTimeMillis()+offSet)/86400000;
        long start = (date+offSet)/86400000;
        long intervalTime = start - today;
        //-2:前天,-1：昨天,0：今天,1：明天,2：后天
        String strDes="";
        if(intervalTime==0){
            //strDes= context.getResources().getString(R.string.today);//今天
            strDes = getFormatDate("HH:mm",date);
        }else if(intervalTime==-1){
            strDes= context.getResources().getString(R.string.yesterday)+" "+getFormatDate("HH:mm",date);//昨天
        }else{
            strDes=getFormatDate("MM月dd日",date)+" "+getFormatDate("HH:mm",date);//直接显示时间
        }
        return strDes;
    }

    public static String getTodayOrYesterday(long date, Context context) {//date 是存储的时间戳
        //所在时区时8，系统初始时间是1970-01-01 80:00:00，注意是从八点开始，计算的时候要加回去
        int offSet = Calendar.getInstance().getTimeZone().getRawOffset();
        long today = (System.currentTimeMillis()+offSet)/86400000;
        long start = (date+offSet)/86400000;
        long intervalTime = start - today;
        //-2:前天,-1：昨天,0：今天,1：明天,2：后天
        String strDes="";
        if(intervalTime==0){
            //strDes= context.getResources().getString(R.string.today);//今天
            strDes = getFormatDate("HH:mm",date);
        }else if(intervalTime==-1){
            strDes= context.getResources().getString(R.string.yesterday);//昨天
        }else{
            strDes=getFormatDate("yyyy/MM/dd",date);//直接显示时间
        }
        return strDes;
    }

    private static String getFormatDate(String template, long date) {
        //长整型的毫秒值数据
        Date date1 = new Date(date);
        //标准日历系统类
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date1);
        //java.text.SimpleDateFormat，设置时间格式
        SimpleDateFormat format = new SimpleDateFormat(template);
        //得到毫秒值转化的时间
        String time = format.format(gc.getTime());
        return time;
    }


}
