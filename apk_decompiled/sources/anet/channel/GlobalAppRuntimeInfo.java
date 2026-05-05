package anet.channel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Process;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import anet.channel.entity.ENV;
import anet.channel.strategy.StrategyCenter;
import anet.channel.strategy.dispatch.AmdcRuntimeInfo;
import anet.channel.strategy.dispatch.DispatchConstants;
import anet.channel.util.ALog;
import anet.channel.util.Utils;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class GlobalAppRuntimeInfo {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static Context f1628a;
    private static String e;
    private static String f;
    private static String g;
    private static volatile long k;
    private static String l;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static ENV f1629b = ENV.ONLINE;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static String f1630c = "";

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static String f1631d = "";
    private static volatile boolean h = true;
    private static SharedPreferences i = null;
    private static volatile CopyOnWriteArrayList<String> j = null;

    public static void setContext(Context context) {
        f1628a = context;
        if (context != null) {
            if (TextUtils.isEmpty(f1631d)) {
                f1631d = Utils.getProcessName(context, Process.myPid());
            }
            if (TextUtils.isEmpty(f1630c)) {
                f1630c = Utils.getMainProcessName(context);
            }
            if (i == null) {
                i = PreferenceManager.getDefaultSharedPreferences(context);
                f = i.getString("UserId", null);
            }
            ALog.e("awcn.GlobalAppRuntimeInfo", "", null, "CurrentProcess", f1631d, "TargetProcess", f1630c);
        }
    }

    public static Context getContext() {
        return f1628a;
    }

    public static void setTargetProcess(String str) {
        f1630c = str;
    }

    public static boolean isTargetProcess() {
        if (TextUtils.isEmpty(f1630c) || TextUtils.isEmpty(f1631d)) {
            return true;
        }
        return f1630c.equalsIgnoreCase(f1631d);
    }

    public static boolean isTargetProcess(String str) {
        if (TextUtils.isEmpty(f1630c) || TextUtils.isEmpty(str)) {
            return true;
        }
        return f1630c.equalsIgnoreCase(str);
    }

    public static String getCurrentProcess() {
        return f1631d;
    }

    public static void setCurrentProcess(String str) {
        f1631d = str;
    }

    public static void setEnv(ENV env) {
        f1629b = env;
    }

    public static ENV getEnv() {
        return f1629b;
    }

    public static void setTtid(String str) {
        e = str;
        try {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            int iIndexOf = str.indexOf("@");
            String strSubstring = null;
            String strSubstring2 = iIndexOf != -1 ? str.substring(0, iIndexOf) : null;
            String strSubstring3 = str.substring(iIndexOf + 1);
            int iLastIndexOf = strSubstring3.lastIndexOf(OpenAccountUIConstants.UNDER_LINE);
            if (iLastIndexOf != -1) {
                String strSubstring4 = strSubstring3.substring(0, iLastIndexOf);
                strSubstring = strSubstring3.substring(iLastIndexOf + 1);
                strSubstring3 = strSubstring4;
            }
            l = strSubstring;
            AmdcRuntimeInfo.setAppInfo(strSubstring3, strSubstring, strSubstring2);
        } catch (Exception unused) {
        }
    }

    public static String getTtid() {
        return e;
    }

    public static void setUserId(String str) {
        String str2 = f;
        if (str2 == null || !str2.equals(str)) {
            f = str;
            StrategyCenter.getInstance().forceRefreshStrategy(DispatchConstants.getAmdcServerDomain());
            SharedPreferences sharedPreferences = i;
            if (sharedPreferences != null) {
                sharedPreferences.edit().putString("UserId", str).apply();
            }
        }
    }

    public static String getUserId() {
        return f;
    }

    public static void setUtdid(String str) {
        String str2 = g;
        if (str2 == null || !str2.equals(str)) {
            g = str;
        }
    }

    public static String getUtdid() {
        Context context;
        if (g == null && (context = f1628a) != null) {
            g = Utils.getDeviceId(context);
        }
        return g;
    }

    public static void setBackground(boolean z) {
        h = z;
    }

    public static boolean isAppBackground() {
        if (f1628a == null) {
            return true;
        }
        return h;
    }

    public static void addBucketInfo(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || str.length() > 32 || str2.length() > 32) {
            return;
        }
        synchronized (GlobalAppRuntimeInfo.class) {
            if (j == null) {
                j = new CopyOnWriteArrayList<>();
            }
            j.add(str);
            j.add(str2);
        }
    }

    public static CopyOnWriteArrayList<String> getBucketInfo() {
        return j;
    }

    @Deprecated
    public static void setInitTime(long j2) {
        k = j2;
    }

    @Deprecated
    public static long getInitTime() {
        return k;
    }

    @Deprecated
    public static int getStartType() {
        anet.channel.fulltrace.b sceneInfo = anet.channel.fulltrace.a.a().getSceneInfo();
        if (sceneInfo != null) {
            return sceneInfo.f1750a;
        }
        return -1;
    }
}
