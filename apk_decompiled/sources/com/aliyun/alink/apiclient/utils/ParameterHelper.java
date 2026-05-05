package com.aliyun.alink.apiclient.utils;

import com.huawei.hms.framework.common.ContainerUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public class ParameterHelper {
    private static final String FORMAT_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String FORMAT_RFC2616 = "EEE, dd MMM yyyy HH:mm:ss zzz";
    private static final String TIME_ZONE = "GMT";

    public static String getUniqueNonce() {
        return UUID.randomUUID().toString();
    }

    public static String getISO8601Time(Date date) {
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        simpleDateFormat.setTimeZone(new SimpleTimeZone(0, TIME_ZONE));
        return simpleDateFormat.format(date);
    }

    public static String getRFC2616Date(Date date) {
        if (date == null) {
            date = new Date();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_RFC2616, Locale.ENGLISH);
        simpleDateFormat.setTimeZone(new SimpleTimeZone(0, TIME_ZONE));
        return simpleDateFormat.format(date);
    }

    public static Date parse(String str) throws ParseException {
        if (str == null || "".equals(str)) {
            return null;
        }
        try {
            return parseISO8601(str);
        } catch (ParseException unused) {
            return parseRFC2616(str);
        }
    }

    public static Date parseISO8601(String str) throws ParseException {
        if (str == null || "".equals(str)) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        simpleDateFormat.setTimeZone(new SimpleTimeZone(0, TIME_ZONE));
        return simpleDateFormat.parse(str);
    }

    public static Date parseRFC2616(String str) throws ParseException {
        if (str == null || "".equals(str) || str.length() != 29) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_RFC2616, Locale.ENGLISH);
        simpleDateFormat.setTimeZone(new SimpleTimeZone(0, TIME_ZONE));
        return simpleDateFormat.parse(str);
    }

    public static String md5Sum(byte[] bArr) {
        try {
            return Base64Helper.encode(MessageDigest.getInstance("MD5").digest(bArr));
        } catch (Exception unused) {
            return null;
        }
    }

    public static byte[] getFormData(Map<String, String> map) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (z) {
                z = false;
            } else {
                sb.append("&");
            }
            sb.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            sb.append(ContainerUtils.KEY_VALUE_DELIMITER);
            sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return sb.toString().getBytes("UTF-8");
    }
}
