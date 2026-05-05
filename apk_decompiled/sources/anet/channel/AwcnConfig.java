package anet.channel;

import android.text.TextUtils;
import anet.channel.strategy.ConnProtocol;
import anet.channel.strategy.StrategyTemplate;
import anet.channel.util.ALog;
import java.util.concurrent.CopyOnWriteArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class AwcnConfig {
    public static final String HTTP3_ENABLE = "HTTP3_ENABLE";
    public static final String NEXT_LAUNCH_FORBID = "NEXT_LAUNCH_FORBID";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static volatile boolean f1616a = false;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static volatile boolean f1617b = true;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static volatile boolean f1618c = true;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static volatile boolean f1619d = true;
    private static volatile boolean e = false;
    private static volatile boolean f = true;
    private static volatile long g = 43200000;
    private static volatile boolean h = true;
    private static volatile boolean i = true;
    private static boolean j = true;
    private static boolean k = false;
    private static volatile boolean l = false;
    private static volatile boolean m = true;
    private static volatile boolean n = false;
    private static volatile int o = 10000;
    private static volatile boolean p = false;
    private static volatile boolean q = true;
    private static volatile int r = -1;
    private static volatile boolean s = true;
    private static volatile boolean t = true;
    private static volatile boolean u = false;
    private static volatile boolean v = true;
    private static volatile CopyOnWriteArrayList<String> w = null;
    private static volatile boolean x = true;
    private static volatile boolean y = true;

    public static boolean isAccsSessionCreateForbiddenInBg() {
        return f1616a;
    }

    public static void setAccsSessionCreateForbiddenInBg(boolean z) {
        f1616a = z;
    }

    public static void setHttpsSniEnable(boolean z) {
        f1617b = z;
    }

    public static boolean isHttpsSniEnable() {
        return f1617b;
    }

    public static boolean isHorseRaceEnable() {
        return f1618c;
    }

    public static void setHorseRaceEnable(boolean z) {
        f1618c = z;
    }

    public static boolean isTnetHeaderCacheEnable() {
        return f1619d;
    }

    public static void setTnetHeaderCacheEnable(boolean z) {
        f1619d = z;
    }

    public static void setQuicEnable(boolean z) {
        e = z;
    }

    public static boolean isQuicEnable() {
        return e;
    }

    public static void setIdleSessionCloseEnable(boolean z) {
        f = z;
    }

    public static boolean isIdleSessionCloseEnable() {
        return f;
    }

    public static void registerPresetSessions(String str) {
        if (GlobalAppRuntimeInfo.isTargetProcess() && !TextUtils.isEmpty(str)) {
            try {
                JSONArray jSONArray = new JSONArray(str);
                int length = jSONArray.length();
                for (int i2 = 0; i2 < length; i2++) {
                    JSONObject jSONObject = jSONArray.getJSONObject(i2);
                    String string = jSONObject.getString("host");
                    if (!anet.channel.strategy.utils.c.c(string)) {
                        return;
                    }
                    StrategyTemplate.getInstance().registerConnProtocol(string, ConnProtocol.valueOf(jSONObject.getString("protocol"), jSONObject.getString("rtt"), jSONObject.getString("publicKey")));
                    if (jSONObject.getBoolean("isKeepAlive")) {
                        SessionCenter.getInstance().registerSessionInfo(SessionInfo.create(string, true, false, null, null, null));
                    }
                }
            } catch (Exception unused) {
            }
        }
    }

    public static boolean isIpv6Enable() {
        return h;
    }

    public static void setIpv6Enable(boolean z) {
        h = z;
    }

    public static boolean isIpv6BlackListEnable() {
        return i;
    }

    public static void setIpv6BlackListEnable(boolean z) {
        i = z;
    }

    public static long getIpv6BlackListTtl() {
        return g;
    }

    public static void setIpv6BlackListTtl(long j2) {
        g = j2;
    }

    public static boolean isAppLifeCycleListenerEnable() {
        return j;
    }

    public static void setAppLifeCycleListenerEnable(boolean z) {
        j = z;
    }

    public static boolean isAsyncLoadStrategyEnable() {
        return k;
    }

    public static void setAsyncLoadStrategyEnable(boolean z) {
        k = z;
    }

    public static boolean isTbNextLaunch() {
        return l;
    }

    public static void setTbNextLaunch(boolean z) {
        l = z;
    }

    public static boolean isPing6Enable() {
        return m;
    }

    public static void setPing6Enable(boolean z) {
        m = z;
    }

    public static boolean isNetworkDetectEnable() {
        return n;
    }

    public static void setNetworkDetectEnable(boolean z) {
        n = z;
    }

    public static int getAccsReconnectionDelayPeriod() {
        return o;
    }

    public static void setAccsReconnectionDelayPeriod(int i2) {
        if (i2 < 0) {
            i2 = 0;
        }
        if (i2 > 10000) {
            i2 = 10000;
        }
        o = i2;
    }

    public static void setHttp3Enable(boolean z) {
        p = z;
        ALog.e("awcn.AwcnConfig", "[setHttp3Enable]", null, "enable", Boolean.valueOf(z));
    }

    public static boolean isHttp3Enable() {
        return p;
    }

    public static void setHttp3OrangeEnable(boolean z) {
        q = z;
    }

    public static boolean isHttp3OrangeEnable() {
        return q;
    }

    public static void setXquicCongControl(int i2) {
        if (i2 < 0) {
            return;
        }
        r = i2;
    }

    public static int getXquicCongControl() {
        return r;
    }

    public static void setIpStackDetectByUdpConnect(boolean z) {
        s = z;
    }

    public static boolean isIpStackDetectByUdpConnect() {
        return s;
    }

    public static void setCookieHeaderRedundantFix(boolean z) {
        t = z;
    }

    public static boolean isCookieHeaderRedundantFix() {
        return t;
    }

    public static void setSendConnectInfoByBroadcast(boolean z) {
        u = z;
    }

    public static boolean isSendConnectInfoByBroadcast() {
        return u;
    }

    public static void setSendConnectInfoByService(boolean z) {
        v = z;
    }

    public static boolean isSendConnectInfoByService() {
        return v;
    }

    public static void setHttpDnsNotifyWhiteList(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        try {
            CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
            JSONArray jSONArray = new JSONArray(str);
            for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                String string = jSONArray.getString(i2);
                if (!TextUtils.isEmpty(string)) {
                    copyOnWriteArrayList.add(string);
                }
            }
            w = copyOnWriteArrayList;
        } catch (Exception e2) {
            ALog.e("awcn.AwcnConfig", "[setHttpDnsNotifyWhiteList] error", null, e2, new Object[0]);
        }
    }

    public static boolean isAllowHttpDnsNotify(String str) {
        if (w == null || TextUtils.isEmpty(str)) {
            return false;
        }
        return w.contains(str);
    }

    public static boolean isWifiInfoEnable() {
        return x;
    }

    public static void setWifiInfoEnable(boolean z) {
        x = z;
    }

    public static boolean isCarrierInfoEnable() {
        return y;
    }

    public static void setCarrierInfoEnable(boolean z) {
        y = z;
    }
}
