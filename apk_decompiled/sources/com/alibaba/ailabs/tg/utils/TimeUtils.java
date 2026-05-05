package com.alibaba.ailabs.tg.utils;

import android.annotation.SuppressLint;
import com.alibaba.ailabs.tg.utils.constant.TimeConstants;
import com.heytap.mcssdk.constant.Constants;
import com.huawei.agconnect.exception.AGCServerException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public final class TimeUtils {

    @SuppressLint({"SimpleDateFormat"})
    private static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String[] CHINESE_ZODIAC = {"猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊"};
    private static final int[] ZODIAC_FLAGS = {20, 19, 21, 21, 21, 22, 23, 23, 23, 24, 23, 22};
    private static final String[] ZODIAC = {"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"};

    private static long timeSpan2Millis(long j, int i) {
        return j * ((long) i);
    }

    private TimeUtils() {
    }

    public static String millis2String(long j) {
        return millis2String(j, DEFAULT_FORMAT);
    }

    public static String millis2String(long j, DateFormat dateFormat) {
        return dateFormat.format(new Date(j));
    }

    public static long string2Millis(String str) {
        return string2Millis(str, DEFAULT_FORMAT);
    }

    public static long string2Millis(String str, DateFormat dateFormat) {
        try {
            return dateFormat.parse(str).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1L;
        }
    }

    public static Date string2Date(String str) {
        return string2Date(str, DEFAULT_FORMAT);
    }

    public static Date string2Date(String str, DateFormat dateFormat) {
        try {
            return dateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String date2String(Date date) {
        return date2String(date, DEFAULT_FORMAT);
    }

    public static String date2String(Date date, DateFormat dateFormat) {
        return dateFormat.format(date);
    }

    public static long date2Millis(Date date) {
        return date.getTime();
    }

    public static Date millis2Date(long j) {
        return new Date(j);
    }

    public static long getTimeSpan(String str, String str2, int i) {
        return getTimeSpan(str, str2, DEFAULT_FORMAT, i);
    }

    public static long getTimeSpan(String str, String str2, DateFormat dateFormat, int i) {
        return millis2TimeSpan(Math.abs(string2Millis(str, dateFormat) - string2Millis(str2, dateFormat)), i);
    }

    public static long getTimeSpan(Date date, Date date2, int i) {
        return millis2TimeSpan(Math.abs(date2Millis(date) - date2Millis(date2)), i);
    }

    public static long getTimeSpan(long j, long j2, int i) {
        return millis2TimeSpan(Math.abs(j - j2), i);
    }

    public static String getFitTimeSpan(String str, String str2, int i) {
        return millis2FitTimeSpan(Math.abs(string2Millis(str, DEFAULT_FORMAT) - string2Millis(str2, DEFAULT_FORMAT)), i);
    }

    public static String getFitTimeSpan(String str, String str2, DateFormat dateFormat, int i) {
        return millis2FitTimeSpan(Math.abs(string2Millis(str, dateFormat) - string2Millis(str2, dateFormat)), i);
    }

    public static String getFitTimeSpan(Date date, Date date2, int i) {
        return millis2FitTimeSpan(Math.abs(date2Millis(date) - date2Millis(date2)), i);
    }

    public static String getFitTimeSpan(long j, long j2, int i) {
        return millis2FitTimeSpan(Math.abs(j - j2), i);
    }

    public static long getNowMills() {
        return System.currentTimeMillis();
    }

    public static String getNowString() {
        return millis2String(System.currentTimeMillis(), DEFAULT_FORMAT);
    }

    public static String getNowString(DateFormat dateFormat) {
        return millis2String(System.currentTimeMillis(), dateFormat);
    }

    public static Date getNowDate() {
        return new Date();
    }

    public static long getTimeSpanByNow(String str, int i) {
        return getTimeSpan(getNowString(), str, DEFAULT_FORMAT, i);
    }

    public static long getTimeSpanByNow(String str, DateFormat dateFormat, int i) {
        return getTimeSpan(getNowString(dateFormat), str, dateFormat, i);
    }

    public static long getTimeSpanByNow(Date date, int i) {
        return getTimeSpan(new Date(), date, i);
    }

    public static long getTimeSpanByNow(long j, int i) {
        return getTimeSpan(System.currentTimeMillis(), j, i);
    }

    public static String getFitTimeSpanByNow(String str, int i) {
        return getFitTimeSpan(getNowString(), str, DEFAULT_FORMAT, i);
    }

    public static String getFitTimeSpanByNow(String str, DateFormat dateFormat, int i) {
        return getFitTimeSpan(getNowString(dateFormat), str, dateFormat, i);
    }

    public static String getFitTimeSpanByNow(Date date, int i) {
        return getFitTimeSpan(getNowDate(), date, i);
    }

    public static String getFitTimeSpanByNow(long j, int i) {
        return getFitTimeSpan(System.currentTimeMillis(), j, i);
    }

    public static String getFriendlyTimeSpanByNow(String str) {
        return getFriendlyTimeSpanByNow(str, DEFAULT_FORMAT);
    }

    public static String getFriendlyTimeSpanByNow(String str, DateFormat dateFormat) {
        return getFriendlyTimeSpanByNow(string2Millis(str, dateFormat));
    }

    public static String getFriendlyTimeSpanByNow(Date date) {
        return getFriendlyTimeSpanByNow(date.getTime());
    }

    public static String getFriendlyTimeSpanByNow(long j) {
        long jCurrentTimeMillis = System.currentTimeMillis() - j;
        if (jCurrentTimeMillis < 0) {
            return String.format("%tc", Long.valueOf(j));
        }
        if (jCurrentTimeMillis < 1000) {
            return "刚刚";
        }
        if (jCurrentTimeMillis < 60000) {
            return String.format(Locale.getDefault(), "%d秒前", Long.valueOf(jCurrentTimeMillis / 1000));
        }
        if (jCurrentTimeMillis < Constants.MILLS_OF_HOUR) {
            return String.format(Locale.getDefault(), "%d分钟前", Long.valueOf(jCurrentTimeMillis / 60000));
        }
        long weeOfToday = getWeeOfToday();
        return j >= weeOfToday ? String.format("今天%tR", Long.valueOf(j)) : j >= weeOfToday - 86400000 ? String.format("昨天%tR", Long.valueOf(j)) : String.format("%tF", Long.valueOf(j));
    }

    private static long getWeeOfToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, 0);
        calendar.set(13, 0);
        calendar.set(12, 0);
        calendar.set(14, 0);
        return calendar.getTimeInMillis();
    }

    public static long getMillis(long j, long j2, int i) {
        return j + timeSpan2Millis(j2, i);
    }

    public static long getMillis(String str, long j, int i) {
        return getMillis(str, DEFAULT_FORMAT, j, i);
    }

    public static long getMillis(String str, DateFormat dateFormat, long j, int i) {
        return string2Millis(str, dateFormat) + timeSpan2Millis(j, i);
    }

    public static long getMillis(Date date, long j, int i) {
        return date2Millis(date) + timeSpan2Millis(j, i);
    }

    public static String getString(long j, long j2, int i) {
        return getString(j, DEFAULT_FORMAT, j2, i);
    }

    public static String getString(long j, DateFormat dateFormat, long j2, int i) {
        return millis2String(j + timeSpan2Millis(j2, i), dateFormat);
    }

    public static String getString(String str, long j, int i) {
        return getString(str, DEFAULT_FORMAT, j, i);
    }

    public static String getString(String str, DateFormat dateFormat, long j, int i) {
        return millis2String(string2Millis(str, dateFormat) + timeSpan2Millis(j, i), dateFormat);
    }

    public static String getString(Date date, long j, int i) {
        return getString(date, DEFAULT_FORMAT, j, i);
    }

    public static String getString(Date date, DateFormat dateFormat, long j, int i) {
        return millis2String(date2Millis(date) + timeSpan2Millis(j, i), dateFormat);
    }

    public static Date getDate(long j, long j2, int i) {
        return millis2Date(j + timeSpan2Millis(j2, i));
    }

    public static Date getDate(String str, long j, int i) {
        return getDate(str, DEFAULT_FORMAT, j, i);
    }

    public static Date getDate(String str, DateFormat dateFormat, long j, int i) {
        return millis2Date(string2Millis(str, dateFormat) + timeSpan2Millis(j, i));
    }

    public static Date getDate(Date date, long j, int i) {
        return millis2Date(date2Millis(date) + timeSpan2Millis(j, i));
    }

    public static long getMillisByNow(long j, int i) {
        return getMillis(getNowMills(), j, i);
    }

    public static String getStringByNow(long j, int i) {
        return getStringByNow(j, DEFAULT_FORMAT, i);
    }

    public static String getStringByNow(long j, DateFormat dateFormat, int i) {
        return getString(getNowMills(), dateFormat, j, i);
    }

    public static Date getDateByNow(long j, int i) {
        return getDate(getNowMills(), j, i);
    }

    public static boolean isToday(String str) {
        return isToday(string2Millis(str, DEFAULT_FORMAT));
    }

    public static boolean isToday(String str, DateFormat dateFormat) {
        return isToday(string2Millis(str, dateFormat));
    }

    public static boolean isToday(Date date) {
        return isToday(date.getTime());
    }

    public static boolean isToday(long j) {
        long weeOfToday = getWeeOfToday();
        return j >= weeOfToday && j < weeOfToday + 86400000;
    }

    public static boolean isLeapYear(String str) {
        return isLeapYear(string2Date(str, DEFAULT_FORMAT));
    }

    public static boolean isLeapYear(String str, DateFormat dateFormat) {
        return isLeapYear(string2Date(str, dateFormat));
    }

    public static boolean isLeapYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return isLeapYear(calendar.get(1));
    }

    public static boolean isLeapYear(long j) {
        return isLeapYear(millis2Date(j));
    }

    public static boolean isLeapYear(int i) {
        return (i % 4 == 0 && i % 100 != 0) || i % AGCServerException.AUTHENTICATION_INVALID == 0;
    }

    public static String getChineseWeek(String str) {
        return getChineseWeek(string2Date(str, DEFAULT_FORMAT));
    }

    public static String getChineseWeek(String str, DateFormat dateFormat) {
        return getChineseWeek(string2Date(str, dateFormat));
    }

    public static String getChineseWeek(Date date) {
        return new SimpleDateFormat("E", Locale.CHINA).format(date);
    }

    public static String getChineseWeek(long j) {
        return getChineseWeek(new Date(j));
    }

    public static String getUSWeek(String str) {
        return getUSWeek(string2Date(str, DEFAULT_FORMAT));
    }

    public static String getUSWeek(String str, DateFormat dateFormat) {
        return getUSWeek(string2Date(str, dateFormat));
    }

    public static String getUSWeek(Date date) {
        return new SimpleDateFormat("EEEE", Locale.US).format(date);
    }

    public static String getUSWeek(long j) {
        return getUSWeek(new Date(j));
    }

    public static int getWeekIndex(String str) {
        return getWeekIndex(string2Date(str, DEFAULT_FORMAT));
    }

    public static int getWeekIndex(String str, DateFormat dateFormat) {
        return getWeekIndex(string2Date(str, dateFormat));
    }

    public static int getWeekIndex(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(7);
    }

    public static int getWeekIndex(long j) {
        return getWeekIndex(millis2Date(j));
    }

    public static int getWeekOfMonth(String str) {
        return getWeekOfMonth(string2Date(str, DEFAULT_FORMAT));
    }

    public static int getWeekOfMonth(String str, DateFormat dateFormat) {
        return getWeekOfMonth(string2Date(str, dateFormat));
    }

    public static int getWeekOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(4);
    }

    public static int getWeekOfMonth(long j) {
        return getWeekOfMonth(millis2Date(j));
    }

    public static int getWeekOfYear(String str) {
        return getWeekOfYear(string2Date(str, DEFAULT_FORMAT));
    }

    public static int getWeekOfYear(String str, DateFormat dateFormat) {
        return getWeekOfYear(string2Date(str, dateFormat));
    }

    public static int getWeekOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(3);
    }

    public static int getWeekOfYear(long j) {
        return getWeekOfYear(millis2Date(j));
    }

    public static String getChineseZodiac(String str) {
        return getChineseZodiac(string2Date(str, DEFAULT_FORMAT));
    }

    public static String getChineseZodiac(String str, DateFormat dateFormat) {
        return getChineseZodiac(string2Date(str, dateFormat));
    }

    public static String getChineseZodiac(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return CHINESE_ZODIAC[calendar.get(1) % 12];
    }

    public static String getChineseZodiac(long j) {
        return getChineseZodiac(millis2Date(j));
    }

    public static String getChineseZodiac(int i) {
        return CHINESE_ZODIAC[i % 12];
    }

    public static String getZodiac(String str) {
        return getZodiac(string2Date(str, DEFAULT_FORMAT));
    }

    public static String getZodiac(String str, DateFormat dateFormat) {
        return getZodiac(string2Date(str, dateFormat));
    }

    public static String getZodiac(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getZodiac(calendar.get(2) + 1, calendar.get(5));
    }

    public static String getZodiac(long j) {
        return getZodiac(millis2Date(j));
    }

    public static String getZodiac(int i, int i2) {
        String[] strArr = ZODIAC;
        int i3 = i - 1;
        if (i2 < ZODIAC_FLAGS[i3]) {
            i3 = (i + 10) % 12;
        }
        return strArr[i3];
    }

    private static long millis2TimeSpan(long j, int i) {
        return j / ((long) i);
    }

    private static String millis2FitTimeSpan(long j, int i) {
        if (j < 0 || i <= 0) {
            return null;
        }
        int iMin = Math.min(i, 5);
        String[] strArr = {"天", "小时", "分钟", "秒", "毫秒"};
        if (j == 0) {
            return 0 + strArr[iMin - 1];
        }
        StringBuilder sb = new StringBuilder();
        int[] iArr = {TimeConstants.DAY, TimeConstants.HOUR, 60000, 1000, 1};
        for (int i2 = 0; i2 < iMin; i2++) {
            if (j >= iArr[i2]) {
                long j2 = j / ((long) iArr[i2]);
                j -= ((long) iArr[i2]) * j2;
                sb.append(j2);
                sb.append(strArr[i2]);
            }
        }
        return sb.toString();
    }
}
