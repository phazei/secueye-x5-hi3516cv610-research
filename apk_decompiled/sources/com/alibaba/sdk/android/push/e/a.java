package com.alibaba.sdk.android.push.e;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: com.alibaba.sdk.android.push.e.a$a, reason: collision with other inner class name */
    public interface InterfaceC0201a {
        void a(String str);

        void b(String str);
    }

    public static Bitmap a(Context context, String str, String str2) throws Throwable {
        File file;
        File file2 = new File(context.getCacheDir(), "aliyun_push_images");
        String strA = a(str);
        if (new File(file2, strA).exists()) {
            file = new File(file2, strA);
        } else {
            try {
                a(str, file2, strA, str2);
                file = new File(file2, strA);
            } catch (Exception unused) {
                return null;
            }
        }
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    public static String a(String str) {
        String strSubstring = str.substring(str.lastIndexOf("/") + 1);
        return strSubstring.contains("?") ? strSubstring.substring(0, strSubstring.indexOf("?")) : strSubstring;
    }

    public static StringBuilder a(BufferedReader bufferedReader) throws IOException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) {
                return sb;
            }
            sb.append(line);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:49:0x0081 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0086 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:62:? A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static void a(java.io.InputStream r4, java.io.File r5, java.lang.String r6) throws java.lang.Throwable {
        /*
            r0 = 0
            java.io.BufferedInputStream r1 = new java.io.BufferedInputStream     // Catch: java.lang.Throwable -> L2e java.lang.Exception -> L31
            r1.<init>(r4)     // Catch: java.lang.Throwable -> L2e java.lang.Exception -> L31
            r4 = 10240(0x2800, float:1.4349E-41)
            byte[] r4 = new byte[r4]     // Catch: java.lang.Throwable -> L26 java.lang.Exception -> L2a
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L26 java.lang.Exception -> L2a
            r2.<init>(r5)     // Catch: java.lang.Throwable -> L26 java.lang.Exception -> L2a
        Lf:
            int r5 = r1.read(r4)     // Catch: java.lang.Throwable -> L22 java.lang.Exception -> L24
            r0 = -1
            if (r5 == r0) goto L1b
            r0 = 0
            r2.write(r4, r0, r5)     // Catch: java.lang.Throwable -> L22 java.lang.Exception -> L24
            goto Lf
        L1b:
            r1.close()     // Catch: java.lang.Throwable -> L1e
        L1e:
            r2.close()     // Catch: java.lang.Throwable -> L21
        L21:
            return
        L22:
            r4 = move-exception
            goto L28
        L24:
            r4 = move-exception
            goto L2c
        L26:
            r4 = move-exception
            r2 = r0
        L28:
            r0 = r1
            goto L7f
        L2a:
            r4 = move-exception
            r2 = r0
        L2c:
            r0 = r1
            goto L33
        L2e:
            r4 = move-exception
            r2 = r0
            goto L7f
        L31:
            r4 = move-exception
            r2 = r0
        L33:
            com.alibaba.sdk.android.push.b.b r5 = com.alibaba.sdk.android.push.b.b.a()     // Catch: java.lang.Throwable -> L7e
            com.alibaba.sdk.android.push.e.a$a r5 = r5.b()     // Catch: java.lang.Throwable -> L7e
            java.lang.String r1 = "image"
            boolean r1 = r1.equals(r6)     // Catch: java.lang.Throwable -> L7e
            if (r1 == 0) goto L4d
            if (r5 == 0) goto L56
            java.lang.String r1 = r4.getMessage()     // Catch: java.lang.Throwable -> L7e
            r5.a(r1)     // Catch: java.lang.Throwable -> L7e
            goto L56
        L4d:
            if (r5 == 0) goto L56
            java.lang.String r1 = r4.getMessage()     // Catch: java.lang.Throwable -> L7e
            r5.b(r1)     // Catch: java.lang.Throwable -> L7e
        L56:
            java.lang.String r5 = "MPS:Download"
            com.alibaba.sdk.android.ams.common.logger.AmsLogger r5 = com.alibaba.sdk.android.ams.common.logger.AmsLogger.getLogger(r5)     // Catch: java.lang.Throwable -> L7e
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L7e
            r1.<init>()     // Catch: java.lang.Throwable -> L7e
            java.lang.String r3 = "writeToFile failed: "
            r1.append(r3)     // Catch: java.lang.Throwable -> L7e
            r1.append(r6)     // Catch: java.lang.Throwable -> L7e
            java.lang.String r6 = ", error: "
            r1.append(r6)     // Catch: java.lang.Throwable -> L7e
            java.lang.String r6 = android.util.Log.getStackTraceString(r4)     // Catch: java.lang.Throwable -> L7e
            r1.append(r6)     // Catch: java.lang.Throwable -> L7e
            java.lang.String r6 = r1.toString()     // Catch: java.lang.Throwable -> L7e
            r5.d(r6)     // Catch: java.lang.Throwable -> L7e
            throw r4     // Catch: java.lang.Throwable -> L7e
        L7e:
            r4 = move-exception
        L7f:
            if (r0 == 0) goto L84
            r0.close()     // Catch: java.lang.Throwable -> L84
        L84:
            if (r2 == 0) goto L89
            r2.close()     // Catch: java.lang.Throwable -> L89
        L89:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.sdk.android.push.e.a.a(java.io.InputStream, java.io.File, java.lang.String):void");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:106:? A[ADDED_TO_REGION, REMOVE, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x013b  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x014a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0145 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0140 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r10v1 */
    /* JADX WARN: Type inference failed for: r10v10 */
    /* JADX WARN: Type inference failed for: r10v11 */
    /* JADX WARN: Type inference failed for: r10v12 */
    /* JADX WARN: Type inference failed for: r10v13 */
    /* JADX WARN: Type inference failed for: r10v15, types: [java.io.BufferedReader] */
    /* JADX WARN: Type inference failed for: r10v16 */
    /* JADX WARN: Type inference failed for: r10v17 */
    /* JADX WARN: Type inference failed for: r10v18 */
    /* JADX WARN: Type inference failed for: r10v19 */
    /* JADX WARN: Type inference failed for: r10v2 */
    /* JADX WARN: Type inference failed for: r10v3, types: [java.io.BufferedReader] */
    /* JADX WARN: Type inference failed for: r10v4 */
    /* JADX WARN: Type inference failed for: r10v5 */
    /* JADX WARN: Type inference failed for: r10v8 */
    /* JADX WARN: Type inference failed for: r10v9 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void a(java.lang.String r8, java.io.File r9, java.lang.String r10, java.lang.String r11) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 342
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.sdk.android.push.e.a.a(java.lang.String, java.io.File, java.lang.String, java.lang.String):void");
    }
}
