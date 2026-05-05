package tools;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import com.huawei.hms.support.hianalytics.HiAnalyticsConstant;
import com.xiaomi.mipush.sdk.Constants;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: loaded from: classes4.dex */
public class DateUtil {
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm:ss");
    public static final SimpleDateFormat sdf4 = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat sdf5 = new SimpleDateFormat("yyyy-MM-dd");
    private static final Calendar cal = Calendar.getInstance();

    public static synchronized String getTodayZero() {
        cal.setTime(new Date());
        cal.set(11, 0);
        cal.set(12, 0);
        return sdf1.format(cal.getTime());
    }

    public static synchronized String getDateFormat(Date date, SimpleDateFormat simpleDateFormat) {
        return simpleDateFormat.format(date);
    }

    public static synchronized String getDateFormat(Date date) {
        return getDateFormat(date, sdf1);
    }

    public static synchronized String getToday() {
        return getToday(sdf1);
    }

    public static synchronized String getToday(SimpleDateFormat simpleDateFormat) {
        return simpleDateFormat.format(new Date());
    }

    public static synchronized String getDateFormat(String str) {
        return getDateFormat(str, sdf1);
    }

    public static synchronized int isTimeDesEquals(String str, String str2) {
        long j = Long.parseLong(getDateFormat(str));
        long j2 = Long.parseLong(getDateFormat(str2));
        if (j > j2) {
            return -1;
        }
        return j < j2 ? 1 : 0;
    }

    public static synchronized long getDateTimes(Date date) {
        cal.setTime(date);
        return cal.getTimeInMillis();
    }

    public static String getPhonrCurrentTimeZone() {
        TimeZone timeZone = TimeZone.getDefault();
        String str = timeZone.getDisplayName(false, 0) + " " + timeZone.getDisplayName(false, 1);
        return str.contains("EST") ? "GMT-05:00" : str.contains("EDT") ? "GMT-04:00" : str.contains("CET") ? "GMT+01:00" : str.contains("JST") ? "GMT+09:00" : str;
    }

    public static synchronized String getDateFormat(String str, SimpleDateFormat simpleDateFormat) {
        try {
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        return new SimpleDateFormat("yyyyMMddHHmm").format(simpleDateFormat.parse(str));
    }

    public static synchronized long getTodayZeroMillins() {
        return ((getTodayMillins() / 86400000) * 86400000) - ((long) TimeZone.getDefault().getRawOffset());
    }

    public static synchronized long getTodayMillins() {
        return System.currentTimeMillis();
    }

    public static synchronized long getTimeMillins(int i, int i2, int i3, int i4, int i5, int i6) {
        cal.set(1, i);
        cal.set(2, i2 - 1);
        cal.set(5, i3);
        cal.set(11, i4);
        cal.set(12, i5);
        cal.set(13, i6);
        cal.set(14, 0);
        return cal.getTime().getTime();
    }

    public static synchronized long getTimeMillins(int i, int i2, int i3) {
        return ((i * 3600) + (i2 * 60) + i3) * 1000;
    }

    public static synchronized Calendar getCalendar(long j) {
        cal.setTime(new Date(j));
        return cal;
    }

    public static synchronized int getHMSTimeMillins(long j) {
        cal.setTime(new Date(j));
        return ((cal.get(11) * 3600) + (cal.get(12) * 60) + cal.get(13)) * 1000;
    }

    public static synchronized int getHMSTimeSecs(long j) {
        cal.setTime(new Date(j * 1000));
        return (cal.get(11) * 3600) + (cal.get(12) * 60) + cal.get(13);
    }

    public static synchronized long getDateTime(String str, SimpleDateFormat simpleDateFormat) {
        if (str != null) {
            if (simpleDateFormat != null) {
                try {
                    return simpleDateFormat.parse(str).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0L;
                }
            }
        }
        return 0L;
    }

    private static synchronized Calendar getCalendar() {
        cal.setTime(new Date());
        return cal;
    }

    public static String getCurrntDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        TimeZone timeZone = TimeZone.getDefault();
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        String str = simpleDateFormat.format(new Date());
        boolean zInDaylightTime = false;
        try {
            if (Build.VERSION.SDK_INT >= 26 && !Build.BRAND.equalsIgnoreCase(LocationUtil.MANUFACTURER_SAMSUNG)) {
                zInDaylightTime = isDaylightTime(LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")), ZoneId.of(TimeZone.getDefault().getID()));
            } else {
                Date date = simpleDateFormat.parse(str);
                if (timeZone.useDaylightTime()) {
                    zInDaylightTime = timeZone.inDaylightTime(date);
                }
            }
            if (zInDaylightTime) {
                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(simpleDateFormat.parse(str));
                    calendar.add(11, 1);
                    return String.valueOf(calendar.getTime().getTime() / 1000);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return "";
                }
            }
            return String.valueOf(System.currentTimeMillis() / 1000);
        } catch (Exception e2) {
            e2.printStackTrace();
            return "";
        }
    }

    public String getIsIncompleteTimeZone() {
        char[] charArray = TimeZone.getDefault().getDisplayName(false, 0, Locale.CHINA).toCharArray();
        String str = "00";
        if (charArray.length > 6) {
            Character chValueOf = Character.valueOf(charArray[charArray.length - 1]);
            str = Character.valueOf(charArray[charArray.length - 2]).toString() + chValueOf.toString();
        }
        return str.equals(AgooConstants.ACK_PACK_ERROR) ? "1" : (str.equals("45") || str.equals("75")) ? "3" : str.equals("30") ? "2" : "0";
    }

    public String cmdSetDate(String str, String str2, boolean z) {
        if (Build.VERSION.SDK_INT >= 26) {
            Iterator<String> it = ZoneId.getAvailableZoneIds().iterator();
            while (it.hasNext()) {
                TimeZone.getTimeZone(it.next()).getDisplayName(false, 0, Locale.CHINA);
            }
        }
        return str + HiAnalyticsConstant.REPORT_VAL_SEPARATOR + str2 + HiAnalyticsConstant.REPORT_VAL_SEPARATOR + getCurrntDate() + HiAnalyticsConstant.REPORT_VAL_SEPARATOR + getIsIncompleteTimeZone() + HiAnalyticsConstant.REPORT_VAL_SEPARATOR + getIsDaylightTime(new String[]{"America/New_York", "Africa/Cairo", "Europe/London", "Europe/Moscow", "America/Toronto", "America/Los_Angeles", "America/Chicago", "Europe/Paris", "Europe/Rome", "Europe/Zurich", "Europe/Paris", "Europe/Berlin", "Australia/Sydney", "Brazil/East"});
    }

    public String getIsDaylightTime(String[] strArr) {
        boolean zInDaylightTime;
        String str = "";
        for (String str2 : strArr) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
            TimeZone timeZone = TimeZone.getTimeZone(str2);
            simpleDateFormat.setTimeZone(timeZone);
            String str3 = simpleDateFormat.format(new Date());
            timeZone.getDisplayName(false, 0, Locale.CHINA);
            try {
                if (Build.VERSION.SDK_INT >= 26 && !Build.BRAND.equalsIgnoreCase(LocationUtil.MANUFACTURER_SAMSUNG)) {
                    zInDaylightTime = isDaylightTime(LocalDateTime.parse(str3, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")), ZoneId.of(str2));
                } else {
                    zInDaylightTime = timeZone.useDaylightTime() ? timeZone.inDaylightTime(simpleDateFormat.parse(str3)) : false;
                }
                str = zInDaylightTime ? str + "1" : str + "0";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    /* JADX WARN: Type inference failed for: r2v3, types: [java.time.ZonedDateTime] */
    public static boolean isDaylightTime(LocalDateTime localDateTime, ZoneId zoneId) {
        boolean zIsDaylightSavings;
        if (Build.VERSION.SDK_INT >= 26) {
            zIsDaylightSavings = zoneId.getRules().isDaylightSavings(localDateTime.atZone(zoneId).toInstant());
        } else {
            zIsDaylightSavings = false;
        }
        Log.e("是否夏令时", "" + zIsDaylightSavings);
        return zIsDaylightSavings;
    }

    public boolean is24(Context context) {
        String string = Settings.System.getString(context.getContentResolver(), "time_12_24");
        return string != null && string.equals(AgooConstants.REPORT_NOT_ENCRYPT);
    }

    public String getCurrentTimeZone() {
        String displayName = TimeZone.getDefault().getDisplayName(false, 0, Locale.CHINA);
        char[] charArray = displayName.toCharArray();
        return charArray.length > 6 ? getTZTimeZone(displayName, charArray) : "未知时区格式";
    }

    public String getTZTimeZone(String str, char[] cArr) {
        Character chValueOf = Character.valueOf(cArr[cArr.length - 5]);
        Character chValueOf2 = Character.valueOf(cArr[cArr.length - 4]);
        String string = chValueOf.toString();
        String string2 = chValueOf2.toString();
        try {
            Integer.parseInt(string2);
            if (string.equals("1")) {
                string2 = string + string2;
            }
        } catch (Exception e) {
            e.printStackTrace();
            string2 = "8";
        }
        if (str.indexOf(Constants.ACCEPT_TIME_SEPARATOR_SERVER) == -1) {
            return string2;
        }
        return Constants.ACCEPT_TIME_SEPARATOR_SERVER + string2;
    }

    public static synchronized String getTimeDes(String str) {
        cal.setTime(new Date());
        return cal.get(1) + str + String.format("%02d", Integer.valueOf(cal.get(2) + 1)) + str + String.format("%02d", Integer.valueOf(cal.get(5)));
    }

    public static synchronized String getTimeDes(long j, String str) {
        cal.setTime(new Date(j * 1000));
        return cal.get(1) + str + String.format("%02d", Integer.valueOf(cal.get(2) + 1)) + str + String.format("%02d", Integer.valueOf(cal.get(5)));
    }

    public static synchronized String getTimeDes(long j) {
        return sdf2.format(new Date(j));
    }

    public static synchronized String getTimeDes(long j, SimpleDateFormat simpleDateFormat) {
        return simpleDateFormat.format(new Date(j));
    }

    public static synchronized String getTimeDes(int i, SimpleDateFormat simpleDateFormat) {
        return simpleDateFormat.format(new Date(((long) i) * 1000));
    }

    public static synchronized String getSimpleTimeDes(long j, SimpleDateFormat simpleDateFormat) {
        return simpleDateFormat.format(Long.valueOf(j));
    }

    public static synchronized Calendar getCalendar(String str) {
        return getCalendar(str, sdf1);
    }

    public static synchronized Calendar getCalendar(String str, SimpleDateFormat simpleDateFormat) {
        try {
            cal.setTime(simpleDateFormat.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    public static synchronized int getYear() {
        return getCalendar().get(1);
    }

    public static synchronized int getYear(String str) {
        return getCalendar(str).get(1);
    }

    public static synchronized int getMonth() {
        return getCalendar().get(2) + 1;
    }

    public static synchronized int getMonth(String str) {
        return getCalendar(str).get(2) + 1;
    }

    public static synchronized int getDay() {
        return getCalendar().get(5);
    }

    public static synchronized int getDay(String str) {
        return getCalendar(str).get(5);
    }

    public static synchronized int getHour() {
        return getCalendar().get(11);
    }

    public static synchronized int getHour(String str) {
        return getCalendar(str).get(11);
    }

    public static synchronized int getMinute() {
        return getCalendar().get(12);
    }

    public static synchronized int getMinute(String str) {
        return getCalendar(str).get(12);
    }

    public static synchronized int getSecond() {
        return getCalendar().get(13);
    }

    public static synchronized int getSecond(String str) {
        return getCalendar(str).get(13);
    }

    public static synchronized int[] getCalendarDate(Date date) {
        cal.setTime(date);
        return new int[]{cal.get(1), cal.get(2) + 1, cal.get(5), cal.get(11), cal.get(12), cal.get(13)};
    }

    public static synchronized long getTimeMillins(int i) {
        return new Date().getTime() + ((long) i);
    }

    public static synchronized int[] getCalendarDate(int i) {
        return getCalendarDate(new Date(new Date().getTime() + ((long) i)));
    }

    public static synchronized String getCompleteTime(Context context) {
        return new SimpleDateFormat("yyyy-MM-dd EEEE HH:mm:ss", getCurrentLocale(context)).format(new Date(System.currentTimeMillis()));
    }

    public static synchronized Locale getCurrentLocale(Context context) {
        Locale locale;
        if (SystemUtil.isZhJianTi()) {
            locale = Locale.SIMPLIFIED_CHINESE;
        } else {
            locale = Locale.ENGLISH;
        }
        return locale;
    }

    public static synchronized String getCompleteTime(Context context, int i, int i2, int i3, int i4, int i5, int i6) {
        return new SimpleDateFormat("yyyy-MM-dd EEEE HH:mm:ss", getCurrentLocale(context)).format(new Date(getTimeMillins(i, i2, i3, i4, i5, i6)));
    }

    public static synchronized String getCompleteTime(Context context, int i, int i2, int i3, int i4) {
        return getCompleteTime(context, i, i2, i3, i4 / 3600, (i4 % 3600) / 60, (i4 % 3600) % 60);
    }

    public static synchronized String chinaToTimeZome(String str) {
        return getTimeDes((getDateTime(str, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")) - ((long) TimeZone.getTimeZone("GMT+08:00").getRawOffset())) + ((long) getTimeZome()), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public static int getTimeZome() {
        TimeZone timeZone = TimeZone.getDefault();
        timeZone.getID();
        timeZone.getDisplayName();
        timeZone.getDisplayName(false, 0);
        return timeZone.getRawOffset();
    }

    public static String getFormattedLocalTimeFromUtc(String str, String str2) {
        String string;
        Date date = null;
        if (TextUtils.isEmpty(str) || !str.contains("T")) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            string = simpleDateFormat.parse(str).toString();
        } catch (ParseException e) {
            e.printStackTrace();
            string = null;
        }
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        simpleDateFormat2.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            date = simpleDateFormat2.parse(string);
        } catch (ParseException e2) {
            e2.printStackTrace();
        }
        return new SimpleDateFormat(str2).format(date);
    }

    public static String utc2Local(String str) {
        Date date;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            date = simpleDateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            date = null;
        }
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat2.setTimeZone(TimeZone.getDefault());
        return simpleDateFormat2.format(Long.valueOf(date.getTime()));
    }
}
