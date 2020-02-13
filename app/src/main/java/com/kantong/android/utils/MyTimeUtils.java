package com.kantong.android.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyTimeUtils {

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *
     * @param start
     * @param end
     * @return
     */
    public static int differentDaysByMillisecond(Date start, Date end) {
        int days = (int) ((end.getTime() - start.getTime()) / (1000 * 3600 * 24));
        return days;
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔天数
     *
     * @param start
     * @param end
     * @return
     */
    public static int countDaysByMillisecond(long start, long end) {
        int days = (int) ((end - start) / (1000 * 3600 * 24));
        return days;
    }

    public static String getCurrentData() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-d", Locale.CHINA);
        return format.format(new Date());
    }

    public static void test() {
//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


        Date d1 = null;
        try {
            d1 = sdf.parse("2019-09-27");
            System.out.println(d1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date d2 = null;
        try {
            d2 = sdf.parse("2019-09-28");
            System.out.println(d2);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            System.out.println(daysBetween(d1, d2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */


    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 字符串的日期格式的计算
     */

    public static int daysBetween(String startDate, String endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(startDate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(endDate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));

    }

    /**
     * 指定日期加上天数后的日期
     *
     * @param num     为增加的天数
     * @return
     * @throws ParseException
     */
    public static String plusDay(int num, String date) throws ParseException {
        if(TextUtils.isEmpty(date)) return "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // 日期格式
        Date getDate = dateFormat.parse(date); // 指定日期
        Date newDate = addDate(getDate, num); // 指定日期加上num天
        return dateFormat.format(newDate);
    }

    public static Date addDate(Date date, long day) throws ParseException {
        long time = date.getTime(); // 得到指定日期的毫秒数
        day = day * 24 * 60 * 60 * 1000; // 要加上的天数转换成毫秒数
        time += day; // 相加得到新的毫秒数
        return new Date(time); // 将毫秒数转换成日期
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @return
     */
    public static String timeStamp2Date(String seconds) {
        return timeStamp2Date(seconds, null);
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @return
     */
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) {
//            format = "yyyy-MM-dd HH:mm:ss";
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }
}
