package com.alibaba.sdk.android.crashdefend.c;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import com.huawei.hms.support.hianalytics.HiAnalyticsConstant;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import org.android.agoo.common.AgooConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class a {
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:35:0x006d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r0v0, types: [int] */
    /* JADX WARN: Type inference failed for: r1v12 */
    /* JADX WARN: Type inference failed for: r1v3, types: [java.io.BufferedReader] */
    /* JADX WARN: Type inference failed for: r1v6 */
    /* JADX WARN: Type inference failed for: r3v1, types: [java.lang.StringBuilder] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.lang.String a() throws java.lang.Throwable {
        /*
            int r0 = android.os.Process.myPid()
            r1 = 0
            java.io.File r2 = new java.io.File     // Catch: java.lang.Throwable -> L47 java.lang.Exception -> L49
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L47 java.lang.Exception -> L49
            r3.<init>()     // Catch: java.lang.Throwable -> L47 java.lang.Exception -> L49
            java.lang.String r4 = "/proc/"
            r3.append(r4)     // Catch: java.lang.Throwable -> L47 java.lang.Exception -> L49
            r3.append(r0)     // Catch: java.lang.Throwable -> L47 java.lang.Exception -> L49
            java.lang.String r0 = "/cmdline"
            r3.append(r0)     // Catch: java.lang.Throwable -> L47 java.lang.Exception -> L49
            java.lang.String r0 = r3.toString()     // Catch: java.lang.Throwable -> L47 java.lang.Exception -> L49
            r2.<init>(r0)     // Catch: java.lang.Throwable -> L47 java.lang.Exception -> L49
            boolean r0 = r2.exists()     // Catch: java.lang.Throwable -> L47 java.lang.Exception -> L49
            if (r0 == 0) goto L3b
            java.io.BufferedReader r0 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L47 java.lang.Exception -> L49
            java.io.FileReader r3 = new java.io.FileReader     // Catch: java.lang.Throwable -> L47 java.lang.Exception -> L49
            r3.<init>(r2)     // Catch: java.lang.Throwable -> L47 java.lang.Exception -> L49
            r0.<init>(r3)     // Catch: java.lang.Throwable -> L47 java.lang.Exception -> L49
            java.lang.String r2 = r0.readLine()     // Catch: java.lang.Exception -> L39 java.lang.Throwable -> L67
            java.lang.String r1 = r2.trim()     // Catch: java.lang.Exception -> L39 java.lang.Throwable -> L67
            goto L3c
        L39:
            r2 = move-exception
            goto L4b
        L3b:
            r0 = r1
        L3c:
            if (r0 == 0) goto L66
            r0.close()     // Catch: java.io.IOException -> L42
            goto L66
        L42:
            r0 = move-exception
            r0.printStackTrace()
            goto L66
        L47:
            r0 = move-exception
            goto L6b
        L49:
            r2 = move-exception
            r0 = r1
        L4b:
            java.lang.String r3 = "CrashUtils"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L67
            r4.<init>()     // Catch: java.lang.Throwable -> L67
            java.lang.String r5 = "getProcessNameByPid error: "
            r4.append(r5)     // Catch: java.lang.Throwable -> L67
            r4.append(r2)     // Catch: java.lang.Throwable -> L67
            java.lang.String r2 = r4.toString()     // Catch: java.lang.Throwable -> L67
            android.util.Log.d(r3, r2)     // Catch: java.lang.Throwable -> L67
            if (r0 == 0) goto L66
            r0.close()     // Catch: java.io.IOException -> L42
        L66:
            return r1
        L67:
            r1 = move-exception
            r6 = r1
            r1 = r0
            r0 = r6
        L6b:
            if (r1 == 0) goto L75
            r1.close()     // Catch: java.io.IOException -> L71
            goto L75
        L71:
            r1 = move-exception
            r1.printStackTrace()
        L75:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.sdk.android.crashdefend.c.a.a():java.lang.String");
    }

    public static synchronized void a(Context context, com.alibaba.sdk.android.crashdefend.a.a aVar, List<com.alibaba.sdk.android.crashdefend.a.b> list) {
        String str;
        String str2;
        if (context == null || list == null) {
            return;
        }
        FileOutputStream fileOutputStreamOpenFileOutput = null;
        try {
            try {
                try {
                    JSONObject jSONObject = new JSONObject();
                    if (aVar != null) {
                        jSONObject.put("startSerialNumber", aVar.f2862a);
                    }
                    try {
                        JSONArray jSONArray = new JSONArray();
                        for (com.alibaba.sdk.android.crashdefend.a.b bVar : list) {
                            if (bVar != null) {
                                JSONObject jSONObject2 = new JSONObject();
                                jSONObject2.put("sdkId", bVar.f2863a);
                                jSONObject2.put("sdkVersion", bVar.f2864b);
                                jSONObject2.put("crashLimit", bVar.f2865c);
                                jSONObject2.put("crashCount", bVar.f2866d);
                                jSONObject2.put(HiAnalyticsConstant.HaKey.BI_KEY_WAITTIME, bVar.e);
                                jSONObject2.put("registerSerialNumber", bVar.f);
                                jSONObject2.put("startSerialNumber", bVar.g);
                                jSONObject2.put("restoreCount", bVar.h);
                                jSONObject2.put("nextRestoreInterval", bVar.i);
                                jSONArray.put(jSONObject2);
                            }
                        }
                        jSONObject.put("sdkList", jSONArray);
                    } catch (JSONException e) {
                        Log.e("CrashUtils", "save sdk json fail:", e);
                    }
                    String string = jSONObject.toString();
                    fileOutputStreamOpenFileOutput = context.openFileOutput(a(context) ? "com_alibaba_aliyun_crash_defend_sdk_info" : "com_alibaba_aliyun_crash_defend_sdk_info_" + b(context), 0);
                    fileOutputStreamOpenFileOutput.write(string.getBytes());
                    if (fileOutputStreamOpenFileOutput != null) {
                        try {
                            fileOutputStreamOpenFileOutput.close();
                        } catch (IOException e2) {
                            e = e2;
                            str = "CrashUtils";
                            str2 = "save sdk io fail:";
                            Log.e(str, str2, e);
                        }
                    }
                } catch (Exception e3) {
                    b.a("CrashUtils", "save sdk exception:", e3);
                    if (fileOutputStreamOpenFileOutput != null) {
                        try {
                            fileOutputStreamOpenFileOutput.close();
                        } catch (IOException e4) {
                            e = e4;
                            str = "CrashUtils";
                            str2 = "save sdk io fail:";
                            Log.e(str, str2, e);
                        }
                    }
                }
            } catch (IOException e5) {
                b.a("CrashUtils", "save sdk io fail:", e5);
                if (fileOutputStreamOpenFileOutput != null) {
                    try {
                        fileOutputStreamOpenFileOutput.close();
                    } catch (IOException e6) {
                        e = e6;
                        str = "CrashUtils";
                        str2 = "save sdk io fail:";
                        Log.e(str, str2, e);
                    }
                }
            }
        } finally {
        }
    }

    private static boolean a(Context context) {
        return context.getPackageName().equalsIgnoreCase(b(context));
    }

    private static String b(Context context) throws Throwable {
        try {
            if (Build.VERSION.SDK_INT >= 28) {
                return Application.getProcessName();
            }
        } catch (Throwable th) {
            Log.e("CrashUtils", "Application gerProcessName error: " + Log.getStackTraceString(th));
        }
        String strD = d(context);
        if (!TextUtils.isEmpty(strD)) {
            return strD;
        }
        String strA = a();
        return !TextUtils.isEmpty(strA) ? strA : c(context);
    }

    public static synchronized boolean b(Context context, com.alibaba.sdk.android.crashdefend.a.a aVar, List<com.alibaba.sdk.android.crashdefend.a.b> list) {
        String str;
        String str2;
        String str3;
        String str4;
        File filesDir;
        if (context == null || list == null) {
            return false;
        }
        FileInputStream fileInputStreamOpenFileInput = null;
        StringBuilder sb = new StringBuilder();
        try {
            try {
                filesDir = context.getFilesDir();
            } finally {
            }
        } catch (FileNotFoundException e) {
            b.a("CrashUtils", "load sdk file fail:", e);
            if (fileInputStreamOpenFileInput != null) {
                try {
                    fileInputStreamOpenFileInput.close();
                } catch (IOException e2) {
                    e = e2;
                    str = "CrashUtils";
                    str2 = "load sdk io fail:";
                    Log.e(str, str2, e);
                }
            }
        } catch (IOException e3) {
            b.a("CrashUtils", "load sdk io fail:", e3);
            if (fileInputStreamOpenFileInput != null) {
                try {
                    fileInputStreamOpenFileInput.close();
                } catch (IOException e4) {
                    e = e4;
                    str = "CrashUtils";
                    str2 = "load sdk io fail:";
                    Log.e(str, str2, e);
                }
            }
        } catch (Exception e5) {
            b.a("CrashUtils", "load sdk exception:", e5);
            if (fileInputStreamOpenFileInput != null) {
                try {
                    fileInputStreamOpenFileInput.close();
                } catch (IOException e6) {
                    e = e6;
                    str = "CrashUtils";
                    str2 = "load sdk io fail:";
                    Log.e(str, str2, e);
                }
            }
        }
        if (!(a(context) ? new File(filesDir, "com_alibaba_aliyun_crash_defend_sdk_info") : new File(filesDir, "com_alibaba_aliyun_crash_defend_sdk_info_" + b(context))).exists()) {
            return false;
        }
        fileInputStreamOpenFileInput = context.openFileInput(a(context) ? "com_alibaba_aliyun_crash_defend_sdk_info" : "com_alibaba_aliyun_crash_defend_sdk_info_" + b(context));
        byte[] bArr = new byte[512];
        while (true) {
            int i = fileInputStreamOpenFileInput.read(bArr);
            if (i == -1) {
                break;
            }
            sb.append(new String(bArr, 0, i));
        }
        if (fileInputStreamOpenFileInput != null) {
            try {
                fileInputStreamOpenFileInput.close();
            } catch (IOException e7) {
                e = e7;
                str = "CrashUtils";
                str2 = "load sdk io fail:";
                Log.e(str, str2, e);
            }
        }
        if (sb.length() == 0) {
            return false;
        }
        try {
            JSONObject jSONObject = new JSONObject(sb.toString());
            aVar.f2862a = jSONObject.optLong("startSerialNumber", 1L);
            JSONArray jSONArray = jSONObject.getJSONArray("sdkList");
            for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                JSONObject jSONObject2 = jSONArray.getJSONObject(i2);
                if (jSONObject2 != null) {
                    com.alibaba.sdk.android.crashdefend.a.b bVar = new com.alibaba.sdk.android.crashdefend.a.b();
                    bVar.f2863a = jSONObject2.optString("sdkId", "");
                    bVar.f2864b = jSONObject2.optString("sdkVersion", "");
                    bVar.f2865c = jSONObject2.optInt("crashLimit", -1);
                    bVar.f2866d = jSONObject2.optInt("crashCount", 0);
                    bVar.e = jSONObject2.optInt(HiAnalyticsConstant.HaKey.BI_KEY_WAITTIME, 0);
                    bVar.f = jSONObject2.optLong("registerSerialNumber", 0L);
                    bVar.g = jSONObject2.optLong("startSerialNumber", 0L);
                    bVar.h = jSONObject2.optInt("restoreCount", 0);
                    bVar.i = jSONObject2.optInt("nextRestoreInterval", 0);
                    if (!TextUtils.isEmpty(bVar.f2863a)) {
                        list.add(bVar);
                    }
                }
            }
        } catch (JSONException e8) {
            e = e8;
            str3 = "CrashUtils";
            str4 = "load sdk json fail:";
            b.a(str3, str4, e);
        } catch (Exception e9) {
            e = e9;
            str3 = "CrashUtils";
            str4 = "load sdk exception:";
            b.a(str3, str4, e);
        }
        return true;
    }

    private static String c(Context context) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(AgooConstants.OPEN_ACTIIVTY_NAME);
        if (activityManager == null || (runningAppProcesses = activityManager.getRunningAppProcesses()) == null) {
            return "";
        }
        int iMyPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            if (runningAppProcessInfo.pid == iMyPid) {
                return runningAppProcessInfo.processName;
            }
        }
        return "";
    }

    private static String d(Context context) {
        try {
            Method declaredMethod = Class.forName("android.app.ActivityThread", false, context.getClassLoader()).getDeclaredMethod("currentProcessName", new Class[0]);
            declaredMethod.setAccessible(true);
            return (String) declaredMethod.invoke(null, new Object[0]);
        } catch (Exception e) {
            Log.d("CrashUtils", "getProcessNameByActivityThread error: " + e);
            return null;
        }
    }
}
