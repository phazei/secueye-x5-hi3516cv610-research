package com.alibaba.sdk.android.openaccount.util;

import android.text.TextUtils;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.huawei.hms.framework.common.ContainerUtils;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class HttpHelper {
    private static final String TAG = "HttpHelper";

    public static String get(String str, Map<String, String> map) {
        return toString(get(str + "?" + encodeRequest(map)), "utf-8");
    }

    public static String toString(InputStream inputStream, String str) {
        ByteArrayOutputStream byteArrayOutputStream;
        byte[] bArr;
        try {
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                bArr = new byte[1024];
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } finally {
            closeQuietly(inputStream);
        }
        while (true) {
            int i = inputStream.read(bArr, 0, 1024);
            if (i != -1) {
                byteArrayOutputStream.write(bArr, 0, i);
            } else {
                return new String(byteArrayOutputStream.toByteArray(), "utf-8");
            }
            closeQuietly(inputStream);
        }
    }

    public static InputStream get(String str) {
        try {
            HttpURLConnection httpURLConnectionOpenConnection = openConnection(str);
            filterResponseCode(httpURLConnectionOpenConnection.getResponseCode());
            return httpURLConnectionOpenConnection.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getConnectionTimeout(int i) {
        String property = OpenAccountSDK.getProperty("httpConnectionTimeout");
        if (property != null) {
            try {
                return Integer.parseInt(property);
            } catch (Exception unused) {
            }
        }
        return i;
    }

    public static int getReadTimeout(int i) {
        String property = OpenAccountSDK.getProperty("httpReadTimeout");
        if (property != null) {
            try {
                return Integer.parseInt(property);
            } catch (Exception unused) {
            }
        }
        return i;
    }

    private static void filterResponseCode(int i) {
        if (i == 200) {
            return;
        }
        throw new RuntimeException("http request exception, response code: " + i);
    }

    private static HttpURLConnection openConnection(String str) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            httpURLConnection.setConnectTimeout(getConnectionTimeout(5000));
            httpURLConnection.setReadTimeout(getReadTimeout(5000));
            return httpURLConnection;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void log(Map<String, String> map, String str) {
        if (AliSDKLogger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("request ");
            sb.append(str);
            sb.append('\n');
            if (map == null || map.size() == 0) {
                sb.append("with no param");
            } else {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    sb.append(entry.getKey());
                    sb.append('=');
                    sb.append(entry.getValue());
                    sb.append('\n');
                }
            }
            AliSDKLogger.d(TAG, sb.toString());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0067 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r4v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r4v2 */
    /* JADX WARN: Type inference failed for: r4v3, types: [java.net.HttpURLConnection] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String post(java.util.Map<java.lang.String, java.lang.String> r3, java.lang.String r4) throws java.lang.Throwable {
        /*
            log(r3, r4)
            r0 = 0
            java.lang.String r3 = encodeRequest(r3)     // Catch: java.lang.Throwable -> L56 java.lang.Throwable -> L59
            java.lang.String r1 = "utf-8"
            byte[] r3 = r3.getBytes(r1)     // Catch: java.lang.Throwable -> L56 java.lang.Throwable -> L59
            java.net.HttpURLConnection r4 = openConnection(r4)     // Catch: java.lang.Throwable -> L56 java.lang.Throwable -> L59
            r1 = 1
            r4.setDoInput(r1)     // Catch: java.lang.Throwable -> L54 java.lang.Throwable -> L61
            r4.setDoOutput(r1)     // Catch: java.lang.Throwable -> L54 java.lang.Throwable -> L61
            java.lang.String r1 = "POST"
            r4.setRequestMethod(r1)     // Catch: java.lang.Throwable -> L54 java.lang.Throwable -> L61
            r1 = 0
            r4.setUseCaches(r1)     // Catch: java.lang.Throwable -> L54 java.lang.Throwable -> L61
            java.lang.String r1 = "Content-Type"
            java.lang.String r2 = "application/x-www-form-urlencoded"
            r4.setRequestProperty(r1, r2)     // Catch: java.lang.Throwable -> L54 java.lang.Throwable -> L61
            java.io.OutputStream r0 = r4.getOutputStream()     // Catch: java.lang.Throwable -> L54 java.lang.Throwable -> L61
            r0.write(r3)     // Catch: java.lang.Throwable -> L54 java.lang.Throwable -> L61
            r0.flush()     // Catch: java.lang.Throwable -> L54 java.lang.Throwable -> L61
            int r3 = r4.getResponseCode()     // Catch: java.lang.Throwable -> L54 java.lang.Throwable -> L61
            filterResponseCode(r3)     // Catch: java.lang.Throwable -> L54 java.lang.Throwable -> L61
            java.io.InputStream r3 = r4.getInputStream()     // Catch: java.lang.Throwable -> L54 java.lang.Throwable -> L61
            java.lang.String r1 = r4.getContentType()     // Catch: java.lang.Throwable -> L54 java.lang.Throwable -> L61
            java.lang.String r1 = getCharset(r1)     // Catch: java.lang.Throwable -> L54 java.lang.Throwable -> L61
            java.lang.String r3 = decodeResponse(r3, r1)     // Catch: java.lang.Throwable -> L54 java.lang.Throwable -> L61
            closeQuietly(r0)
            if (r4 == 0) goto L53
            r4.disconnect()     // Catch: java.lang.Exception -> L53
        L53:
            return r3
        L54:
            r3 = move-exception
            goto L5b
        L56:
            r3 = move-exception
            r4 = r0
            goto L62
        L59:
            r3 = move-exception
            r4 = r0
        L5b:
            java.lang.RuntimeException r1 = new java.lang.RuntimeException     // Catch: java.lang.Throwable -> L61
            r1.<init>(r3)     // Catch: java.lang.Throwable -> L61
            throw r1     // Catch: java.lang.Throwable -> L61
        L61:
            r3 = move-exception
        L62:
            closeQuietly(r0)
            if (r4 == 0) goto L6a
            r4.disconnect()     // Catch: java.lang.Exception -> L6a
        L6a:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.sdk.android.openaccount.util.HttpHelper.post(java.util.Map, java.lang.String):java.lang.String");
    }

    public static String encodeRequest(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        boolean z = false;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (z) {
                try {
                    sb.append("&");
                } catch (UnsupportedEncodingException e) {
                    AliSDKLogger.e(TAG, e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            } else {
                z = true;
            }
            sb.append(entry.getKey());
            sb.append(ContainerUtils.KEY_VALUE_DELIMITER);
            sb.append(URLEncoder.encode(entry.getValue(), "utf-8"));
        }
        return sb.toString();
    }

    private static String decodeResponse(InputStream inputStream, String str) throws UnsupportedEncodingException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[1024];
        while (true) {
            try {
                int i = inputStream.read(bArr);
                if (i != -1) {
                    byteArrayOutputStream.write(bArr, 0, i);
                } else {
                    return new String(byteArrayOutputStream.toByteArray(), str);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String getCharset(String str) {
        if (TextUtils.isEmpty(str)) {
            return "utf-8";
        }
        for (String str2 : str.split("[;]]")) {
            if (!TextUtils.isEmpty(str2) && str2.startsWith("charset")) {
                String[] strArrSplit = str2.split(ContainerUtils.KEY_VALUE_DELIMITER);
                if (strArrSplit.length == 2) {
                    return strArrSplit[1];
                }
            }
        }
        return "utf-8";
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (Exception unused) {
        }
    }
}
