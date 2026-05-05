package tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/* JADX INFO: loaded from: classes4.dex */
public class TimeUtil {
    public static String TimeStamp2Date(String str, String str2) {
        if (StringUtil.isNullOrEmpty(str2)) {
            str2 = "yyyy-MM-dd HH:mm:ss";
        }
        return new SimpleDateFormat(str2, Locale.CHINA).format(new Date(Long.valueOf(Long.parseLong(str) * 1000).longValue()));
    }

    public static String TimeStamp2Date(String str) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date(Long.valueOf(Long.parseLong(str)).longValue()));
    }

    public static String getFormatDay(Date date) {
        return date == null ? "" : new SimpleDateFormat("yyyy年MM月dd日").format(Long.valueOf(date.getTime()));
    }

    public static String getFormatDayWithTime(Date date) {
        return date == null ? "" : new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss").format(Long.valueOf(date.getTime()));
    }

    public static String Date2TimeStamp(String str, String str2) {
        try {
            return String.valueOf(new SimpleDateFormat(str2).parse(str).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Date transformTime(Date date, TimeZone timeZone, TimeZone timeZone2) {
        if (date == null) {
            return null;
        }
        return new Date(date.getTime() - ((long) (timeZone.getOffset(date.getTime()) - timeZone2.getOffset(date.getTime()))));
    }

    public static int getHours(long j) {
        return (int) (j > 3600 ? j / 3600 : 0L);
    }

    public static int getMins(long j) {
        long j2 = j % 3600;
        long j3 = 0;
        if (j <= 3600) {
            j3 = j / 60;
        } else if (j2 != 0 && j2 > 60) {
            j3 = j2 / 60;
        }
        return (int) j3;
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0021  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String getSeconds(long r8) {
        /*
            r0 = 3600(0xe10, double:1.7786E-320)
            long r2 = r8 % r0
            int r0 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1))
            r4 = 60
            r6 = 0
            if (r0 <= 0) goto L1a
            int r8 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r8 == 0) goto L21
            int r8 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r8 <= 0) goto L22
            long r2 = r2 % r4
            int r8 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r8 == 0) goto L21
            goto L22
        L1a:
            long r2 = r8 % r4
            int r8 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r8 == 0) goto L21
            goto L22
        L21:
            r2 = r6
        L22:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            r8.append(r2)
            java.lang.String r9 = ""
            r8.append(r9)
            java.lang.String r8 = r8.toString()
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: tools.TimeUtil.getSeconds(long):java.lang.String");
    }
}
