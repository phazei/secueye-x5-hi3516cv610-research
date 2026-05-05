package anet.channel.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.monitor.NetworkSpeed;
import anet.channel.statist.ExceptionStatistic;
import anet.channel.status.NetworkStatusHelper;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.aliyun.alink.linksdk.alcs.coap.resources.LinkFormat;
import com.ta.utdid2.device.UTDevice;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class Utils {
    private static final String TAG = "awcn.Utils";
    public static Context context;

    public static String getDeviceId(Context context2) {
        return UTDevice.getUtdid(context2);
    }

    public static String getMainProcessName(Context context2) {
        if (context2 == null) {
            return "";
        }
        try {
            return context2.getPackageManager().getPackageInfo(context2.getPackageName(), 0).applicationInfo.processName;
        } catch (PackageManager.NameNotFoundException unused) {
            return "";
        }
    }

    public static String getProcessName(Context context2, int i) {
        String str = "";
        try {
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context2.getSystemService(AgooConstants.OPEN_ACTIIVTY_NAME)).getRunningAppProcesses();
            if (runningAppProcesses != null && runningAppProcesses.size() > 0) {
                Iterator<ActivityManager.RunningAppProcessInfo> it = runningAppProcesses.iterator();
                while (true) {
                    if (it.hasNext()) {
                        ActivityManager.RunningAppProcessInfo next = it.next();
                        if (next.pid == i) {
                            str = next.processName;
                            break;
                        }
                    }
                }
            } else {
                AppMonitor.getInstance().commitStat(new ExceptionStatistic(ErrorConstant.ERROR_GET_PROCESS_NULL, ErrorConstant.formatMsg(ErrorConstant.ERROR_GET_PROCESS_NULL, "BuildVersion=" + String.valueOf(Build.VERSION.SDK_INT)), LinkFormat.RESOURCE_TYPE));
            }
            break;
        } catch (Exception e) {
            AppMonitor.getInstance().commitStat(new ExceptionStatistic(ErrorConstant.ERROR_GET_PROCESS_NULL, e.toString(), LinkFormat.RESOURCE_TYPE));
        }
        return TextUtils.isEmpty(str) ? getProcessNameNew(i) : str;
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0085, code lost:
    
        r7 = r0[8];
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0089, code lost:
    
        r4.close();
        r5.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0090, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0091, code lost:
    
        anet.channel.util.ALog.e(anet.channel.util.Utils.TAG, "getProcessNameNew ", null, r0, new java.lang.Object[0]);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.lang.String getProcessNameNew(int r7) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 237
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.util.Utils.getProcessNameNew(int):java.lang.String");
    }

    public static Context getAppContext() {
        Context context2 = context;
        if (context2 != null) {
            return context2;
        }
        synchronized (Utils.class) {
            if (context != null) {
                return context;
            }
            try {
                Class<?> cls = Class.forName("android.app.ActivityThread");
                Object objInvoke = cls.getMethod("currentActivityThread", new Class[0]).invoke(cls, new Object[0]);
                context = (Context) objInvoke.getClass().getMethod("getApplication", new Class[0]).invoke(objInvoke, new Object[0]);
            } catch (Exception e) {
                ALog.w(TAG, "getAppContext", null, e, new Object[0]);
            }
            return context;
        }
    }

    public static String getStackMsg(Throwable th) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            StackTraceElement[] stackTrace = th.getStackTrace();
            if (stackTrace != null && stackTrace.length > 0) {
                for (StackTraceElement stackTraceElement : stackTrace) {
                    stringBuffer.append(stackTraceElement.toString() + SdkConstant.CLOUDAPI_LF);
                }
            }
        } catch (Exception e) {
            ALog.e(TAG, "getStackMsg", null, e, new Object[0]);
        }
        return stringBuffer.toString();
    }

    public static Object invokeStaticMethodThrowException(String str, String str2, Class<?>[] clsArr, Object... objArr) throws Exception {
        Method declaredMethod;
        if (str == null || str2 == null) {
            return null;
        }
        Class<?> cls = Class.forName(str);
        if (clsArr != null) {
            declaredMethod = cls.getDeclaredMethod(str2, clsArr);
        } else {
            declaredMethod = cls.getDeclaredMethod(str2, new Class[0]);
        }
        if (declaredMethod == null) {
            return null;
        }
        declaredMethod.setAccessible(true);
        if (objArr != null) {
            return declaredMethod.invoke(cls, objArr);
        }
        return declaredMethod.invoke(cls, new Object[0]);
    }

    public static float getNetworkTimeFactor() {
        NetworkStatusHelper.NetworkStatus status = NetworkStatusHelper.getStatus();
        float f = (status == NetworkStatusHelper.NetworkStatus.G4 || status == NetworkStatusHelper.NetworkStatus.WIFI) ? 0.8f : 1.0f;
        return anet.channel.monitor.b.a().b() == NetworkSpeed.Fast.getCode() ? f * 0.75f : f;
    }
}
