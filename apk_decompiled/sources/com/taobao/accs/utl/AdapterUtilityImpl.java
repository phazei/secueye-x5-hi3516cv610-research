package com.taobao.accs.utl;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Process;
import android.os.StatFs;
import android.text.TextUtils;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.huawei.hms.push.constant.RemoteMessageConst;
import com.ta.utdid2.device.UTDevice;
import com.taobao.accs.ChannelService;
import com.taobao.accs.client.AdapterGlobalClientInfo;
import com.taobao.accs.data.MsgDistributeService;
import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class AdapterUtilityImpl {
    public static String BACK_APP_KEY = "";
    private static final String TAG = "AdapterUtilityImpl";
    private static boolean channelProcessChecked = false;
    private static String channelProcessName = "";
    private static boolean isChannelProcess = true;
    private static boolean isTargetProcess = true;
    public static String mAgooAppSecret = null;
    private static boolean mChecked = false;
    private static boolean mIsMainProc = true;
    private static boolean targetProcessChecked = false;
    private static String targetProcessName = "";
    public static final String channelService = ChannelService.class.getName();
    public static final String msgService = MsgDistributeService.class.getName();

    public static boolean isMainProcess(Context context) throws Throwable {
        if (mChecked) {
            return mIsMainProc;
        }
        mIsMainProc = context.getPackageName().equalsIgnoreCase(getProcessName(context));
        mChecked = true;
        return mIsMainProc;
    }

    public static boolean isChannelProcess(Context context) throws Throwable {
        if (channelProcessChecked) {
            return isChannelProcess;
        }
        String processName = getProcessName(context);
        if (TextUtils.isEmpty(channelProcessName)) {
            channelProcessName = getServiceProcess(context, channelService);
        }
        isChannelProcess = processName.equalsIgnoreCase(channelProcessName);
        channelProcessChecked = true;
        return isChannelProcess;
    }

    public static boolean isTargetProcess(Context context) throws Throwable {
        if (targetProcessChecked) {
            return isTargetProcess;
        }
        String processName = getProcessName(context);
        if (TextUtils.isEmpty(targetProcessName)) {
            targetProcessName = getServiceProcess(context, msgService);
        }
        isTargetProcess = processName.equalsIgnoreCase(targetProcessName);
        targetProcessChecked = true;
        return isTargetProcess;
    }

    public static String getTargetProcess(Context context) {
        if (TextUtils.isEmpty(targetProcessName)) {
            targetProcessName = getServiceProcess(context, msgService);
        }
        return targetProcessName;
    }

    public static String getChannelProcess(Context context) {
        if (TextUtils.isEmpty(channelProcessName)) {
            channelProcessName = getServiceProcess(context, channelService);
        }
        return channelProcessName;
    }

    public static String getServiceProcess(Context context, String str) {
        try {
            ServiceInfo serviceInfo = context.getPackageManager().getServiceInfo(new ComponentName(context, str), 131584);
            if (serviceInfo == null) {
                return null;
            }
            if (serviceInfo.processName == null) {
                return context.getPackageName();
            }
            if (serviceInfo.processName.startsWith(":")) {
                return context.getPackageName() + serviceInfo.processName;
            }
            return serviceInfo.processName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x001a A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:11:0x001b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String getProcessName(android.content.Context r2) throws java.lang.Throwable {
        /*
            java.lang.String r2 = com.alibaba.sdk.android.tool.ProcessUtils.getProcessName(r2)     // Catch: java.lang.Throwable -> L5
            return r2
        L5:
            int r0 = android.os.Build.VERSION.SDK_INT     // Catch: java.lang.Exception -> L10
            r1 = 28
            if (r0 < r1) goto L10
            java.lang.String r2 = android.app.Application.getProcessName()     // Catch: java.lang.Exception -> L10
            return r2
        L10:
            java.lang.String r0 = getProcessNameByActivityThread(r2)
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 != 0) goto L1b
            return r0
        L1b:
            java.lang.String r0 = getProcessNameByPid()
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 != 0) goto L26
            return r0
        L26:
            java.lang.String r2 = getProcessNameByAm(r2)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.utl.AdapterUtilityImpl.getProcessName(android.content.Context):java.lang.String");
    }

    private static String getProcessNameByAm(Context context) {
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

    /* JADX WARN: Removed duplicated region for block: B:32:0x0061 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.lang.String getProcessNameByPid() throws java.lang.Throwable {
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
            java.lang.String r2 = r0.readLine()     // Catch: java.lang.Exception -> L39 java.lang.Throwable -> L5b
            java.lang.String r1 = r2.trim()     // Catch: java.lang.Exception -> L39 java.lang.Throwable -> L5b
            goto L3c
        L39:
            r2 = move-exception
            goto L4b
        L3b:
            r0 = r1
        L3c:
            if (r0 == 0) goto L5a
            r0.close()     // Catch: java.io.IOException -> L42
            goto L5a
        L42:
            r0 = move-exception
            r0.printStackTrace()
            goto L5a
        L47:
            r0 = move-exception
            goto L5f
        L49:
            r2 = move-exception
            r0 = r1
        L4b:
            java.lang.String r3 = "AdapterUtilityImpl"
            java.lang.String r4 = "getProcessNameByPid error: "
            r5 = 0
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch: java.lang.Throwable -> L5b
            com.taobao.accs.utl.ALog.w(r3, r4, r2, r5)     // Catch: java.lang.Throwable -> L5b
            if (r0 == 0) goto L5a
            r0.close()     // Catch: java.io.IOException -> L42
        L5a:
            return r1
        L5b:
            r1 = move-exception
            r6 = r1
            r1 = r0
            r0 = r6
        L5f:
            if (r1 == 0) goto L69
            r1.close()     // Catch: java.io.IOException -> L65
            goto L69
        L65:
            r1 = move-exception
            r1.printStackTrace()
        L69:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.utl.AdapterUtilityImpl.getProcessNameByPid():java.lang.String");
    }

    private static String getProcessNameByActivityThread(Context context) {
        try {
            Method declaredMethod = Class.forName("android.app.ActivityThread", false, context.getClassLoader()).getDeclaredMethod("currentProcessName", new Class[0]);
            declaredMethod.setAccessible(true);
            return (String) declaredMethod.invoke(null, new Object[0]);
        } catch (Exception e) {
            ALog.w(TAG, "getProcessNameByActivityThread error: ", e, new Object[0]);
            return null;
        }
    }

    public static long getUsableSpace() {
        try {
            File dataDirectory = Environment.getDataDirectory();
            if (dataDirectory == null) {
                return -1L;
            }
            return dataDirectory.getUsableSpace();
        } catch (Throwable th) {
            ALog.e(TAG, "getUsableSpace", th, new Object[0]);
            return -1L;
        }
    }

    public static String getStackMsg(Throwable th) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            StackTraceElement[] stackTrace = th.getStackTrace();
            if (stackTrace != null && stackTrace.length > 0) {
                for (StackTraceElement stackTraceElement : stackTrace) {
                    stringBuffer.append(stackTraceElement.toString());
                    stringBuffer.append(SdkConstant.CLOUDAPI_LF);
                }
            }
        } catch (Exception unused) {
        }
        return stringBuffer.toString();
    }

    public static String getDeviceId(Context context) {
        return UTDevice.getUtdid(context);
    }

    public static boolean isNetworkConnected(Context context) {
        if (context == null) {
            return false;
        }
        try {
            NetworkInfo activeNetworkInfo = AdapterGlobalClientInfo.getInstance(context).getConnectivityManager().getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                return activeNetworkInfo.isConnected();
            }
            return false;
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    public static final boolean checkIsWritable(String str, int i) {
        if (str == null) {
            return false;
        }
        StatFs statFs = new StatFs(str);
        int blockSize = statFs.getBlockSize();
        boolean z = statFs.getAvailableBlocks() > 10 && ((long) statFs.getAvailableBlocks()) * ((long) blockSize) > ((long) i);
        if (!z) {
            ALog.w("FileCheckUtils", "target : " + i + " st.getAvailableBlocks()=" + statFs.getAvailableBlocks() + ",st.getAvailableBlocks() * blockSize=" + (((long) statFs.getAvailableBlocks()) * ((long) blockSize)), new Object[0]);
        }
        return z;
    }

    public static String isNotificationEnabled(Context context) {
        boolean z = true;
        if (Utils.isTarget26(context)) {
            ApplicationInfo applicationInfo = context.getApplicationInfo();
            String packageName = context.getApplicationContext().getPackageName();
            int i = applicationInfo.uid;
            try {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(RemoteMessageConst.NOTIFICATION);
                Method declaredMethod = notificationManager.getClass().getDeclaredMethod("getService", new Class[0]);
                declaredMethod.setAccessible(true);
                Object objInvoke = declaredMethod.invoke(notificationManager, new Object[0]);
                Method declaredMethod2 = objInvoke.getClass().getDeclaredMethod("areNotificationsEnabledForPackage", String.class, Integer.TYPE);
                declaredMethod2.setAccessible(true);
                return String.valueOf(declaredMethod2.invoke(objInvoke, packageName, Integer.valueOf(i)));
            } catch (Throwable th) {
                ALog.e(TAG, "Android O isNotificationEnabled", th, new Object[0]);
                return "unknown";
            }
        }
        try {
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService("appops");
            ApplicationInfo applicationInfo2 = context.getApplicationInfo();
            String packageName2 = context.getApplicationContext().getPackageName();
            int i2 = applicationInfo2.uid;
            Class<?> cls = Class.forName(AppOpsManager.class.getName());
            if (((Integer) cls.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class).invoke(appOpsManager, Integer.valueOf(((Integer) cls.getDeclaredField("OP_POST_NOTIFICATION").get(appOpsManager)).intValue()), Integer.valueOf(i2), packageName2)).intValue() != 0) {
                z = false;
            }
            return String.valueOf(z);
        } catch (Throwable th2) {
            ALog.e(TAG, "isNotificationEnabled", th2, new Object[0]);
            return "unknown";
        }
    }
}
