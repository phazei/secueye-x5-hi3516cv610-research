package com.alibaba.sdk.android.ams.common.util;

import android.content.Context;
import android.util.Base64;
import com.alibaba.sdk.android.ams.common.logger.AmsLogger;
import com.huawei.hms.framework.common.ContainerUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final AmsLogger f2837a = AmsLogger.getLogger("MPS:httpClient");

    private static class a extends IOException {
        public a(IOException iOException, int i) {
            super(iOException.getMessage() + " code " + i, iOException);
        }
    }

    public static HttpURLConnection a(String str, Map<String, String> map, String str2) {
        return "POST".equals(str2) ? a(str, map, str2, 0, null) : b(str, map, str2, 0, null);
    }

    private static HttpURLConnection a(String str, Map<String, String> map, String str2, int i, Context context) throws IOException {
        if (i >= 3) {
            throw new a(new IOException("redirectCount > 3"), 300);
        }
        StringBuilder sb = new StringBuilder();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getValue() == null) {
                    f2837a.w("skip empty entry " + entry.getKey());
                } else {
                    sb.append(entry.getKey());
                    sb.append(ContainerUtils.KEY_VALUE_DELIMITER);
                    sb.append(Base64.encodeToString(entry.getValue().getBytes(), 8));
                    sb.append("&");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setReadTimeout(6000);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setConnectTimeout(6000);
        OutputStream outputStream = httpURLConnection.getOutputStream();
        outputStream.write(sb.toString().getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
        try {
            int responseCode = httpURLConnection.getResponseCode();
            f2837a.d("responseCode: " + responseCode);
            return (responseCode != 200 && responseCode / 3 == 100) ? a(httpURLConnection.getHeaderField("Location"), map, str2, i, context) : httpURLConnection;
        } catch (IOException e) {
            f2837a.d("openConnection: ", e);
            throw new a(e, httpURLConnection.getResponseCode());
        }
    }

    @Deprecated
    private static HttpURLConnection b(String str, Map<String, String> map, String str2, int i, Context context) throws ProtocolException, a {
        if (i >= 3) {
            throw new a(new IOException("redirectCount > 3"), 300);
        }
        StringBuilder sb = new StringBuilder();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getValue() == null) {
                    f2837a.w("skip empty entry " + entry.getKey());
                } else {
                    sb.append(entry.getKey());
                    sb.append(ContainerUtils.KEY_VALUE_DELIMITER);
                    sb.append(URLEncoder.encode(entry.getValue(), "utf-8"));
                    sb.append("&");
                }
            }
        }
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str + "?" + ((Object) sb)).openConnection();
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setRequestMethod(str2 == null ? "GET" : str2);
        httpURLConnection.setReadTimeout(6000);
        httpURLConnection.setConnectTimeout(6000);
        httpURLConnection.connect();
        try {
            int responseCode = httpURLConnection.getResponseCode();
            f2837a.d("responseCode: " + responseCode);
            return (responseCode != 200 && responseCode / 3 == 100) ? b(httpURLConnection.getHeaderField("Location"), map, str2, i, context) : httpURLConnection;
        } catch (IOException e) {
            f2837a.d("openConnection: ", e);
            throw new a(e, httpURLConnection.getResponseCode());
        }
    }
}
