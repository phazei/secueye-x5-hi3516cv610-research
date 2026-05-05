package com.alibaba.sdk.android.utils.crashdefend;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import com.huawei.hms.support.hianalytics.HiAnalyticsConstant;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.android.agoo.common.AgooConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: compiled from: CrashDefendUtils.java */
/* JADX INFO: loaded from: classes.dex */
class e {
    static void a(Context context, a aVar, List<c> list) {
        String str;
        String str2;
        if (context == null) {
            return;
        }
        synchronized (list) {
            FileOutputStream fileOutputStreamOpenFileOutput = null;
            try {
                try {
                    JSONObject jSONObject = new JSONObject();
                    if (aVar != null) {
                        jSONObject.put("startSerialNumber", aVar.f3215a);
                    }
                    if (list != null) {
                        try {
                            JSONArray jSONArray = new JSONArray();
                            for (c cVar : list) {
                                if (cVar != null) {
                                    JSONObject jSONObject2 = new JSONObject();
                                    jSONObject2.put("sdkId", cVar.f44a);
                                    jSONObject2.put("sdkVersion", cVar.f46b);
                                    jSONObject2.put("crashLimit", cVar.f3220a);
                                    jSONObject2.put("crashCount", cVar.crashCount);
                                    jSONObject2.put(HiAnalyticsConstant.HaKey.BI_KEY_WAITTIME, cVar.f3221b);
                                    jSONObject2.put("registerSerialNumber", cVar.f45b);
                                    jSONObject2.put("startSerialNumber", cVar.f42a);
                                    jSONObject2.put("restoreCount", cVar.f3222c);
                                    jSONArray.put(jSONObject2);
                                }
                            }
                            jSONObject.put("sdkList", jSONArray);
                        } catch (JSONException e) {
                            Log.e("CrashUtils", "save sdk json fail:", e);
                        }
                    }
                    String string = jSONObject.toString();
                    fileOutputStreamOpenFileOutput = m31a(context) ? context.openFileOutput("com_alibaba_aliyun_crash_defend_sdk_info", 0) : context.openFileOutput("com_alibaba_aliyun_crash_defend_sdk_info_" + a(context), 0);
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
                } catch (IOException e3) {
                    Log.e("CrashUtils", "save sdk io fail:", e3);
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
                } catch (Exception e5) {
                    Log.e("CrashUtils", "save sdk exception:", e5);
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
    }

    /* JADX INFO: renamed from: a, reason: collision with other method in class */
    static boolean m32a(Context context, a aVar, List<c> list) {
        String str;
        String str2;
        if (context == null) {
            return false;
        }
        FileInputStream fileInputStreamOpenFileInput = null;
        StringBuilder sb = new StringBuilder();
        synchronized (list) {
            try {
                try {
                    try {
                        fileInputStreamOpenFileInput = m31a(context) ? context.openFileInput("com_alibaba_aliyun_crash_defend_sdk_info") : context.openFileInput("com_alibaba_aliyun_crash_defend_sdk_info_" + a(context));
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
                            } catch (IOException e) {
                                e = e;
                                str = "CrashUtils";
                                str2 = "load sdk io fail:";
                                Log.e(str, str2, e);
                            }
                        }
                    } catch (IOException e2) {
                        Log.e("CrashUtils", "load sdk io fail:", e2);
                        if (fileInputStreamOpenFileInput != null) {
                            try {
                                fileInputStreamOpenFileInput.close();
                            } catch (IOException e3) {
                                e = e3;
                                str = "CrashUtils";
                                str2 = "load sdk io fail:";
                                Log.e(str, str2, e);
                            }
                        }
                    }
                } catch (FileNotFoundException e4) {
                    Log.e("CrashUtils", "load sdk file fail:", e4);
                    if (fileInputStreamOpenFileInput != null) {
                        try {
                            fileInputStreamOpenFileInput.close();
                        } catch (IOException e5) {
                            e = e5;
                            str = "CrashUtils";
                            str2 = "load sdk io fail:";
                            Log.e(str, str2, e);
                        }
                    }
                } catch (Exception e6) {
                    Log.e("CrashUtils", "load sdk exception:", e6);
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
                }
                if (sb.length() == 0) {
                    return false;
                }
                try {
                    try {
                        JSONObject jSONObject = new JSONObject(sb.toString());
                        aVar.f3215a = jSONObject.optLong("startSerialNumber", 1L);
                        JSONArray jSONArray = jSONObject.getJSONArray("sdkList");
                        for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                            JSONObject jSONObject2 = jSONArray.getJSONObject(i2);
                            if (jSONObject2 != null) {
                                c cVar = new c();
                                cVar.f44a = jSONObject2.optString("sdkId", "");
                                cVar.f46b = jSONObject2.optString("sdkVersion", "");
                                cVar.f3220a = jSONObject2.optInt("crashLimit", -1);
                                cVar.crashCount = jSONObject2.optInt("crashCount", 0);
                                cVar.f3221b = jSONObject2.optInt(HiAnalyticsConstant.HaKey.BI_KEY_WAITTIME, 0);
                                cVar.f45b = jSONObject2.optLong("registerSerialNumber", 0L);
                                cVar.f42a = jSONObject2.optLong("startSerialNumber", 0L);
                                cVar.f3222c = jSONObject2.optInt("restoreCount", 0);
                                if (!TextUtils.isEmpty(cVar.f44a)) {
                                    list.add(cVar);
                                }
                            }
                        }
                    } catch (Exception e8) {
                        Log.e("CrashUtils", "load sdk exception:", e8);
                    }
                } catch (JSONException e9) {
                    Log.e("CrashUtils", "load sdk json fail:", e9);
                }
                return true;
            } finally {
            }
        }
    }

    /* JADX INFO: renamed from: a, reason: collision with other method in class */
    private static boolean m31a(Context context) {
        return context.getPackageName().equalsIgnoreCase(a(context));
    }

    private static String a(Context context) {
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
}
