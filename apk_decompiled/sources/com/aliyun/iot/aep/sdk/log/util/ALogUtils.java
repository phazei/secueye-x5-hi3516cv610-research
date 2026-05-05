package com.aliyun.iot.aep.sdk.log.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import okhttp3.OkHttpClient;

/* JADX INFO: loaded from: classes2.dex */
public class ALogUtils {
    public static final String HOST = "iot-alog.aliyun.test:8080";
    private static final String ONLINE = "iot-alog.aliyun.test:8080";
    public static final String STSHOST = "iot-alog.aliyun.test:3000";
    private static final String STS_ONLINE = "iot-alog.aliyun.test:3000";
    private static final String STS_TEST = "30.6.52.56:3000";
    private static final String TEST = "30.6.52.56:8080";
    private static final int TIMEOUT_IN_MILLIONS = 5000;
    private static OkHttpClient sHttpClient;

    /* JADX WARN: Removed duplicated region for block: B:38:0x00ac A[Catch: IOException -> 0x008c, TRY_ENTER, TryCatch #8 {IOException -> 0x008c, blocks: (B:16:0x0088, B:19:0x008e, B:38:0x00ac, B:40:0x00b1), top: B:56:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00b1 A[Catch: IOException -> 0x008c, TRY_LEAVE, TryCatch #8 {IOException -> 0x008c, blocks: (B:16:0x0088, B:19:0x008e, B:38:0x00ac, B:40:0x00b1), top: B:56:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00c0 A[Catch: IOException -> 0x00bc, TRY_LEAVE, TryCatch #7 {IOException -> 0x00bc, blocks: (B:44:0x00b8, B:48:0x00c0), top: B:54:0x00b8 }] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00b8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String doPost(java.lang.String r4, java.lang.String r5) throws java.lang.Throwable {
        /*
            java.lang.String r0 = ""
            r1 = 0
            java.net.URL r2 = new java.net.URL     // Catch: java.lang.Throwable -> La2 java.lang.Exception -> La5
            r2.<init>(r4)     // Catch: java.lang.Throwable -> La2 java.lang.Exception -> La5
            java.net.URLConnection r4 = r2.openConnection()     // Catch: java.lang.Throwable -> La2 java.lang.Exception -> La5
            java.net.HttpURLConnection r4 = (java.net.HttpURLConnection) r4     // Catch: java.lang.Throwable -> La2 java.lang.Exception -> La5
            java.lang.String r2 = "accept"
        */
        //  java.lang.String r3 = "*/*"
        /*
            r4.setRequestProperty(r2, r3)     // Catch: java.lang.Throwable -> La2 java.lang.Exception -> La5
            java.lang.String r2 = "connection"
            java.lang.String r3 = "Keep-Alive"
            r4.setRequestProperty(r2, r3)     // Catch: java.lang.Throwable -> La2 java.lang.Exception -> La5
            java.lang.String r2 = "POST"
            r4.setRequestMethod(r2)     // Catch: java.lang.Throwable -> La2 java.lang.Exception -> La5
            java.lang.String r2 = "Content-Type"
            java.lang.String r3 = "application/x-www-form-urlencoded"
            r4.setRequestProperty(r2, r3)     // Catch: java.lang.Throwable -> La2 java.lang.Exception -> La5
            java.lang.String r2 = "charset"
            java.lang.String r3 = "utf-8"
            r4.setRequestProperty(r2, r3)     // Catch: java.lang.Throwable -> La2 java.lang.Exception -> La5
            r2 = 0
            r4.setUseCaches(r2)     // Catch: java.lang.Throwable -> La2 java.lang.Exception -> La5
            r2 = 1
            r4.setDoOutput(r2)     // Catch: java.lang.Throwable -> La2 java.lang.Exception -> La5
            r4.setDoInput(r2)     // Catch: java.lang.Throwable -> La2 java.lang.Exception -> La5
            r2 = 5000(0x1388, float:7.006E-42)
            r4.setReadTimeout(r2)     // Catch: java.lang.Throwable -> La2 java.lang.Exception -> La5
            r4.setConnectTimeout(r2)     // Catch: java.lang.Throwable -> La2 java.lang.Exception -> La5
            if (r5 == 0) goto L61
            java.lang.String r2 = r5.trim()     // Catch: java.lang.Throwable -> La2 java.lang.Exception -> La5
            java.lang.String r3 = ""
            boolean r2 = r2.equals(r3)     // Catch: java.lang.Throwable -> La2 java.lang.Exception -> La5
            if (r2 != 0) goto L61
            java.io.PrintWriter r2 = new java.io.PrintWriter     // Catch: java.lang.Throwable -> La2 java.lang.Exception -> La5
            java.io.OutputStream r3 = r4.getOutputStream()     // Catch: java.lang.Throwable -> La2 java.lang.Exception -> La5
            r2.<init>(r3)     // Catch: java.lang.Throwable -> La2 java.lang.Exception -> La5
            r2.print(r5)     // Catch: java.lang.Throwable -> L9a java.lang.Exception -> L9e
            r2.flush()     // Catch: java.lang.Throwable -> L9a java.lang.Exception -> L9e
            goto L62
        L61:
            r2 = r1
        L62:
            java.io.BufferedReader r5 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L9a java.lang.Exception -> L9e
            java.io.InputStreamReader r3 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> L9a java.lang.Exception -> L9e
            java.io.InputStream r4 = r4.getInputStream()     // Catch: java.lang.Throwable -> L9a java.lang.Exception -> L9e
            r3.<init>(r4)     // Catch: java.lang.Throwable -> L9a java.lang.Exception -> L9e
            r5.<init>(r3)     // Catch: java.lang.Throwable -> L9a java.lang.Exception -> L9e
        L70:
            java.lang.String r4 = r5.readLine()     // Catch: java.lang.Throwable -> L96 java.lang.Exception -> L98
            if (r4 == 0) goto L86
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L96 java.lang.Exception -> L98
            r1.<init>()     // Catch: java.lang.Throwable -> L96 java.lang.Exception -> L98
            r1.append(r0)     // Catch: java.lang.Throwable -> L96 java.lang.Exception -> L98
            r1.append(r4)     // Catch: java.lang.Throwable -> L96 java.lang.Exception -> L98
            java.lang.String r0 = r1.toString()     // Catch: java.lang.Throwable -> L96 java.lang.Exception -> L98
            goto L70
        L86:
            if (r2 == 0) goto L8e
            r2.close()     // Catch: java.io.IOException -> L8c
            goto L8e
        L8c:
            r4 = move-exception
            goto L92
        L8e:
            r5.close()     // Catch: java.io.IOException -> L8c
            goto Lb4
        L92:
            r4.printStackTrace()
            goto Lb4
        L96:
            r4 = move-exception
            goto L9c
        L98:
            r4 = move-exception
            goto La0
        L9a:
            r4 = move-exception
            r5 = r1
        L9c:
            r1 = r2
            goto Lb6
        L9e:
            r4 = move-exception
            r5 = r1
        La0:
            r1 = r2
            goto La7
        La2:
            r4 = move-exception
            r5 = r1
            goto Lb6
        La5:
            r4 = move-exception
            r5 = r1
        La7:
            r4.printStackTrace()     // Catch: java.lang.Throwable -> Lb5
            if (r1 == 0) goto Laf
            r1.close()     // Catch: java.io.IOException -> L8c
        Laf:
            if (r5 == 0) goto Lb4
            r5.close()     // Catch: java.io.IOException -> L8c
        Lb4:
            return r0
        Lb5:
            r4 = move-exception
        Lb6:
            if (r1 == 0) goto Lbe
            r1.close()     // Catch: java.io.IOException -> Lbc
            goto Lbe
        Lbc:
            r5 = move-exception
            goto Lc4
        Lbe:
            if (r5 == 0) goto Lc7
            r5.close()     // Catch: java.io.IOException -> Lbc
            goto Lc7
        Lc4:
            r5.printStackTrace()
        Lc7:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.aliyun.iot.aep.sdk.log.util.ALogUtils.doPost(java.lang.String, java.lang.String):java.lang.String");
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.aliyun.iot.aep.sdk.log.util.ALogUtils$1] */
    public static void doPostAsyn(final String str, final String str2) {
        new Thread() { // from class: com.aliyun.iot.aep.sdk.log.util.ALogUtils.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() throws Throwable {
                try {
                    ALogUtils.doPost(str, str2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static OkHttpClient getHttpClient() {
        if (sHttpClient == null) {
            sHttpClient = new OkHttpClient();
        }
        return sHttpClient;
    }

    public static String getPath(Context context, Uri uri) {
        if (!"content".equalsIgnoreCase(uri.getScheme())) {
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
            return null;
        }
        try {
            Cursor cursorQuery = context.getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
            int columnIndexOrThrow = cursorQuery.getColumnIndexOrThrow("_data");
            if (cursorQuery.moveToFirst()) {
                return cursorQuery.getString(columnIndexOrThrow);
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }
}
