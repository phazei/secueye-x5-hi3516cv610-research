package anetwork.channel.cache;

import anet.channel.util.HttpHelper;
import anetwork.channel.cache.Cache;
import io.netty.handler.codec.http.HttpHeaders;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final TimeZone f1997a = TimeZone.getTimeZone("GMT");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final ThreadLocal<SimpleDateFormat> f1998b = new ThreadLocal<>();

    public static String a(long j) {
        return a().format(new Date(j));
    }

    private static long a(String str) {
        if (str.length() == 0) {
            return 0L;
        }
        try {
            ParsePosition parsePosition = new ParsePosition(0);
            Date date = a().parse(str, parsePosition);
            if (parsePosition.getIndex() == str.length()) {
                return date.getTime();
            }
        } catch (Exception unused) {
        }
        return 0L;
    }

    public static Cache.Entry a(Map<String, List<String>> map) {
        long j;
        long j2;
        long jCurrentTimeMillis = System.currentTimeMillis();
        String singleHeaderFieldByKey = HttpHelper.getSingleHeaderFieldByKey(map, "Cache-Control");
        boolean z = true;
        int i = 0;
        if (singleHeaderFieldByKey != null) {
            String[] strArrSplit = singleHeaderFieldByKey.split(",");
            j = 0;
            while (true) {
                if (i >= strArrSplit.length) {
                    break;
                }
                String strTrim = strArrSplit[i].trim();
                if (strTrim.equals(HttpHeaders.Values.NO_STORE)) {
                    return null;
                }
                if (strTrim.equals("no-cache")) {
                    j = 0;
                    break;
                }
                if (strTrim.startsWith("max-age=")) {
                    try {
                        j = Long.parseLong(strTrim.substring(8));
                    } catch (Exception unused) {
                    }
                }
                i++;
            }
        } else {
            z = false;
            j = 0;
        }
        String singleHeaderFieldByKey2 = HttpHelper.getSingleHeaderFieldByKey(map, "Date");
        long jA = singleHeaderFieldByKey2 != null ? a(singleHeaderFieldByKey2) : 0L;
        String singleHeaderFieldByKey3 = HttpHelper.getSingleHeaderFieldByKey(map, "Expires");
        long jA2 = singleHeaderFieldByKey3 != null ? a(singleHeaderFieldByKey3) : 0L;
        String singleHeaderFieldByKey4 = HttpHelper.getSingleHeaderFieldByKey(map, "Last-Modified");
        long jA3 = singleHeaderFieldByKey4 != null ? a(singleHeaderFieldByKey4) : 0L;
        String singleHeaderFieldByKey5 = HttpHelper.getSingleHeaderFieldByKey(map, "ETag");
        if (z) {
            jCurrentTimeMillis += j * 1000;
            j2 = jA3;
        } else if (jA <= 0 || jA2 < jA) {
            j2 = jA3;
            if (j2 <= 0) {
                jCurrentTimeMillis = 0;
            }
        } else {
            jCurrentTimeMillis += jA2 - jA;
            j2 = jA3;
        }
        if (jCurrentTimeMillis == 0 && singleHeaderFieldByKey5 == null) {
            return null;
        }
        Cache.Entry entry = new Cache.Entry();
        entry.etag = singleHeaderFieldByKey5;
        entry.ttl = jCurrentTimeMillis;
        entry.serverDate = jA;
        entry.lastModified = j2;
        entry.responseHeaders = map;
        return entry;
    }

    private static SimpleDateFormat a() {
        SimpleDateFormat simpleDateFormat = f1998b.get();
        if (simpleDateFormat != null) {
            return simpleDateFormat;
        }
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        simpleDateFormat2.setTimeZone(f1997a);
        f1998b.set(simpleDateFormat2);
        return simpleDateFormat2;
    }
}
