package anet.channel.strategy.dispatch;

import android.content.Context;
import anet.channel.util.ALog;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class AmdcRuntimeInfo {
    private static final String TAG = "awcn.AmdcRuntimeInfo";
    private static volatile int amdcLimitLevel = 0;
    private static volatile long amdcLimitTime = 0;
    public static volatile String appChannel = null;
    public static volatile String appName = null;
    public static volatile String appVersion = null;
    private static volatile Context context = null;
    private static volatile boolean forceHttps = false;
    private static IAmdcSign iSign;
    public static volatile double latitude;
    public static volatile double longitude;
    private static Map<String, String> params;

    public static void updateAmdcLimit(int i, int i2) {
        ALog.i(TAG, "set amdc limit", null, FirebaseAnalytics.Param.LEVEL, Integer.valueOf(i), "time", Integer.valueOf(i2));
        if (i < 0 || i > 3) {
            return;
        }
        amdcLimitLevel = i;
        amdcLimitTime = System.currentTimeMillis() + (((long) i2) * 1000);
    }

    public static int getAmdcLimitLevel() {
        if (amdcLimitLevel > 0 && System.currentTimeMillis() - amdcLimitTime > 0) {
            amdcLimitTime = 0L;
            amdcLimitLevel = 0;
        }
        return amdcLimitLevel;
    }

    public static void setContext(Context context2) {
        context = context2;
    }

    public static Context getContext() {
        return context;
    }

    public static void setSign(IAmdcSign iAmdcSign) {
        iSign = iAmdcSign;
    }

    public static IAmdcSign getSign() {
        return iSign;
    }

    public static void updateLocation(double d2, double d3) {
        latitude = d2;
        longitude = d3;
    }

    public static void setAppInfo(String str, String str2, String str3) {
        appName = str;
        appVersion = str2;
        appChannel = str3;
    }

    public static synchronized void setParam(String str, String str2) {
        if (params == null) {
            params = new HashMap();
        }
        params.put(str, str2);
    }

    public static synchronized Map<String, String> getParams() {
        if (params == null) {
            return Collections.EMPTY_MAP;
        }
        return new HashMap(params);
    }

    public static boolean isForceHttps() {
        return forceHttps;
    }

    public static void setForceHttps(boolean z) {
        forceHttps = z;
    }
}
