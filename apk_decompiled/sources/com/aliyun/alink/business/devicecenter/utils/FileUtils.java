package com.aliyun.alink.business.devicecenter.utils;

/* JADX INFO: loaded from: classes2.dex */
public class FileUtils {
    /* JADX WARN: Removed duplicated region for block: B:40:0x0044 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String ReadFile(java.io.InputStream r5) throws java.lang.Throwable {
        /*
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r1 = 0
            java.io.InputStreamReader r2 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> L2b java.io.IOException -> L2d
            java.lang.String r3 = "UTF-8"
            r2.<init>(r5, r3)     // Catch: java.lang.Throwable -> L2b java.io.IOException -> L2d
            java.io.BufferedReader r5 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L2b java.io.IOException -> L2d
            r5.<init>(r2)     // Catch: java.lang.Throwable -> L2b java.io.IOException -> L2d
        L12:
            java.lang.String r1 = r5.readLine()     // Catch: java.lang.Throwable -> L23 java.io.IOException -> L26
            if (r1 == 0) goto L1c
            r0.append(r1)     // Catch: java.lang.Throwable -> L23 java.io.IOException -> L26
            goto L12
        L1c:
            r5.close()     // Catch: java.lang.Throwable -> L23 java.io.IOException -> L26
            r5.close()     // Catch: java.io.IOException -> L37
            goto L3b
        L23:
            r0 = move-exception
            r1 = r5
            goto L41
        L26:
            r1 = move-exception
            r4 = r1
            r1 = r5
            r5 = r4
            goto L2e
        L2b:
            r5 = move-exception
            goto L42
        L2d:
            r5 = move-exception
        L2e:
            r5.printStackTrace()     // Catch: java.lang.Throwable -> L40
            if (r1 == 0) goto L3b
            r1.close()     // Catch: java.io.IOException -> L37
            goto L3b
        L37:
            r5 = move-exception
            r5.printStackTrace()
        L3b:
            java.lang.String r5 = r0.toString()
            return r5
        L40:
            r0 = move-exception
        L41:
            r5 = r0
        L42:
            if (r1 == 0) goto L4c
            r1.close()     // Catch: java.io.IOException -> L48
            goto L4c
        L48:
            r0 = move-exception
            r0.printStackTrace()
        L4c:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.aliyun.alink.business.devicecenter.utils.FileUtils.ReadFile(java.io.InputStream):java.lang.String");
    }
}
