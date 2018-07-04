package com.ejiahe.jeservice.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeFormat {

    public static final String ENG_DATE_FROMAT = "EEE, d MMM yyyy HH:mm:ss z";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_M_D = "yyyy-M-d";
    public static final String YYYY = "yyyy";
    public static final String MM = "MM";
    public static final String DD = "dd";
    public static final String MM_DD_HH_MM = "MM-dd HH:mm";

    private static XMPPDateTimeFormat xmppDateTimeFormat = new XMPPDateTimeFormat();

    public static String format(Date date){
        return XMPPDateTimeFormat.format(date);
    }

    /**
     * 转换UTC日期
     * @param dateString
     * @return
     * @throws ParseException
     */
    public static Date parseUTC(String dateString) throws ParseException{
        return xmppDateTimeFormat.parseString(dateString);
    }
    /**
     * 转换UTC日期
     * @param date
     * @return
     * @throws ParseException
     */
    public static String formatUTC(Date date){
        return xmppDateTimeFormat.format(date);
    }

    public static Date date2Date(Date date, String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        String str = sdf.format(date);
        try {
            date = sdf.parse(str);
        } catch (Exception e) {
            return null;
        }
        return date;
    }


    public static String date2String(Date date, String formatStr) {
        String strDate = "";
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        strDate = sdf.format(date);
        return strDate;
    }


    public static String timestamp2String(Timestamp timestamp, String formatStr) {
        String strDate = "";
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        strDate = sdf.format(timestamp);
        return strDate;
    }


    public static Date string2Date(String dateString, String formatStr) {
        Date formateDate = null;
        DateFormat format = new SimpleDateFormat(formatStr);
        try {
            formateDate = format.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
        return formateDate;
    }


    public static Timestamp date2Timestamp(Date date) {
        if (date == null)
            return null;
        return new Timestamp(date.getTime());
    }


    public static String getNowYear() {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY);
        return sdf.format(new Date());
    }


    public static String getNowMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat(MM);
        return sdf.format(new Date());
    }


    public static String getNowDay() {
        SimpleDateFormat sdf = new SimpleDateFormat(DD);
        return sdf.format(new Date());
    }


    /**
     * 输出指定时间与当前时间的间隔(分钟/小时/天)
     *
     * @param time
     * @return
     */
    public static String getTimeIntervalWithNowAsChinese(long time) {
        Calendar cal = Calendar.getInstance();
        long timel = cal.getTimeInMillis() - time;
        if (timel / 1000 < 60) {
            return "1分钟以内";
        } else if (timel / 1000 / 60 < 60) {
            return timel / 1000 / 60 + "分钟前";
        } else if (timel / 1000 / 60 / 60 < 24) {
            return timel / 1000 / 60 / 60 + "小时前";
        } else {
            return timel / 1000 / 60 / 60 / 24 + "天前";
        }
    }

}
