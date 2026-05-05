package com.aliyun.alink.linksdk.tmp.utils;

import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/* JADX INFO: loaded from: classes2.dex */
public class ResHelper {
    protected static final String TAG = "[Tmp]ResHelper";

    public static String getRawDeviceModel(Context context, int i) {
        InputStream inputStreamOpenRawResource = null;
        try {
            try {
                inputStreamOpenRawResource = context.getResources().openRawResource(i);
                int iAvailable = inputStreamOpenRawResource.available();
                byte[] bArr = new byte[iAvailable];
                inputStreamOpenRawResource.read(bArr);
                return new String(bArr, 0, iAvailable, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
                if (inputStreamOpenRawResource == null) {
                    return "";
                }
                try {
                    inputStreamOpenRawResource.close();
                    return "";
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return "";
                }
            }
        } finally {
            if (inputStreamOpenRawResource != null) {
                try {
                    inputStreamOpenRawResource.close();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0074 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r5v10 */
    /* JADX WARN: Type inference failed for: r5v16 */
    /* JADX WARN: Type inference failed for: r5v17 */
    /* JADX WARN: Type inference failed for: r5v18 */
    /* JADX WARN: Type inference failed for: r5v19 */
    /* JADX WARN: Type inference failed for: r5v2, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r5v3 */
    /* JADX WARN: Type inference failed for: r5v5, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r5v7 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String getDataFileData(android.content.Context r5, java.lang.String r6) throws java.lang.Throwable {
        /*
            r0 = 0
            if (r5 != 0) goto Lb
            java.lang.String r5 = "[Tmp]ResHelper"
            java.lang.String r6 = "getDataFileData context empty"
            com.aliyun.alink.linksdk.tools.ALog.d(r5, r6)
            return r0
        Lb:
            java.io.File r5 = r5.getExternalCacheDir()
            if (r5 != 0) goto L19
            java.lang.String r5 = "[Tmp]ResHelper"
            java.lang.String r6 = "getDataFileData cacheDir empty"
            com.aliyun.alink.linksdk.tools.ALog.d(r5, r6)
            return r0
        L19:
            java.lang.String r1 = "[Tmp]ResHelper"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "getDataFileData cacheDir:"
            r2.append(r3)
            java.lang.String r3 = r5.getAbsolutePath()
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            com.aliyun.alink.linksdk.tools.ALog.d(r1, r2)
            java.io.File r1 = new java.io.File
            java.lang.String r5 = r5.getAbsolutePath()
            r1.<init>(r5, r6)
            java.io.FileInputStream r5 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L61
            r5.<init>(r1)     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L61
            int r6 = r5.available()     // Catch: java.lang.Exception -> L5c java.lang.Throwable -> L71
            byte[] r1 = new byte[r6]     // Catch: java.lang.Exception -> L5c java.lang.Throwable -> L71
            r2 = 0
            r5.read(r1, r2, r6)     // Catch: java.lang.Exception -> L5c java.lang.Throwable -> L71
            java.lang.String r3 = new java.lang.String     // Catch: java.lang.Exception -> L5c java.lang.Throwable -> L71
            java.lang.String r4 = "UTF-8"
            r3.<init>(r1, r2, r6, r4)     // Catch: java.lang.Exception -> L5c java.lang.Throwable -> L71
            r5.close()     // Catch: java.lang.Exception -> L56
            goto L5a
        L56:
            r5 = move-exception
            r5.printStackTrace()
        L5a:
            r0 = r3
            goto L70
        L5c:
            r6 = move-exception
            goto L63
        L5e:
            r6 = move-exception
            r5 = r0
            goto L72
        L61:
            r6 = move-exception
            r5 = r0
        L63:
            r6.printStackTrace()     // Catch: java.lang.Throwable -> L71
            if (r5 == 0) goto L70
            r5.close()     // Catch: java.lang.Exception -> L6c
            goto L70
        L6c:
            r5 = move-exception
            r5.printStackTrace()
        L70:
            return r0
        L71:
            r6 = move-exception
        L72:
            if (r5 == 0) goto L7c
            r5.close()     // Catch: java.lang.Exception -> L78
            goto L7c
        L78:
            r5 = move-exception
            r5.printStackTrace()
        L7c:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.aliyun.alink.linksdk.tmp.utils.ResHelper.getDataFileData(android.content.Context, java.lang.String):java.lang.String");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0 */
    /* JADX WARN: Type inference failed for: r0v1 */
    /* JADX WARN: Type inference failed for: r0v13 */
    /* JADX WARN: Type inference failed for: r0v14 */
    /* JADX WARN: Type inference failed for: r0v2, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r0v5 */
    /* JADX WARN: Type inference failed for: r0v6, types: [byte[]] */
    /* JADX WARN: Type inference failed for: r0v7 */
    public static byte[] getFileByte(File file) throws Throwable {
        byte[] bArr;
        FileInputStream fileInputStream;
        ?? r0 = 0;
        byte[] bArr2 = null;
        FileInputStream fileInputStream2 = null;
        try {
            try {
                fileInputStream = new FileInputStream(file);
            } catch (Throwable th) {
                th = th;
            }
        } catch (Exception e) {
            e = e;
            bArr = null;
        }
        try {
            int iAvailable = fileInputStream.available();
            bArr2 = new byte[iAvailable];
            fileInputStream.read(bArr2, 0, iAvailable);
            try {
                fileInputStream.close();
                r0 = bArr2;
            } catch (Exception e2) {
                e2.printStackTrace();
                r0 = bArr2;
            }
        } catch (Exception e3) {
            e = e3;
            bArr = bArr2;
            fileInputStream2 = fileInputStream;
            e.printStackTrace();
            if (fileInputStream2 != null) {
                try {
                    fileInputStream2.close();
                } catch (Exception e4) {
                    e4.printStackTrace();
                }
            }
            r0 = bArr;
        } catch (Throwable th2) {
            th = th2;
            r0 = fileInputStream;
            if (r0 != 0) {
                try {
                    r0.close();
                } catch (Exception e5) {
                    e5.printStackTrace();
                }
            }
            throw th;
        }
        return r0;
    }

    public static String getFileStr(File file) {
        try {
            return new String(getFileByte(file), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
