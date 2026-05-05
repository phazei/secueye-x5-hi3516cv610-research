package anetwork.channel.config;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import anet.channel.AwcnConfig;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.statist.RequestStatistic;
import anet.channel.strategy.dispatch.HttpDispatcher;
import anet.channel.strategy.utils.c;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import anet.channel.util.HttpUrl;
import anetwork.channel.cache.CacheManager;
import anetwork.channel.http.NetworkSdkSetting;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class NetworkConfigCenter {
    public static final String SERVICE_OPTIMIZE = "SERVICE_OPTIMIZE";
    public static final String SESSION_ASYNC_OPTIMIZE = "SESSION_ASYNC_OPTIMIZE";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static volatile boolean f1999a = true;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static volatile boolean f2000b = true;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static volatile boolean f2001c = true;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static volatile int f2002d = 5;
    private static volatile boolean e = true;
    private static volatile boolean f = true;
    private static volatile boolean g = false;
    private static volatile long h = 0;
    private static volatile boolean i = false;
    private static volatile ConcurrentHashMap<String, List<String>> j;
    private static volatile CopyOnWriteArrayList<String> k;
    private static final List<String> l = new ArrayList();
    private static volatile int m = 10000;
    private static volatile boolean n = true;
    private static volatile boolean o = false;
    private static volatile int p = 60000;
    private static volatile CopyOnWriteArrayList<String> q = null;
    private static volatile ConcurrentHashMap<String, List<String>> r = null;
    private static volatile boolean s = true;
    private static volatile boolean t = false;
    private static volatile boolean u = false;
    private static volatile boolean v = true;
    private static volatile boolean w = true;
    private static volatile IRemoteConfig x;

    @Deprecated
    public static void setHttpsValidationEnabled(boolean z) {
    }

    public static void init() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(NetworkSdkSetting.getContext());
        h = defaultSharedPreferences.getLong("Cache.Flag", 0L);
        u = defaultSharedPreferences.getBoolean("CHANNEL_LOCAL_INSTANCE_ENABLE", false);
        v = defaultSharedPreferences.getBoolean("ALLOW_SPDY_WHEN_BIND_SERVICE_FAILED", true);
    }

    public static void setSSLEnabled(boolean z) {
        ALog.i("anet.NetworkConfigCenter", "[setSSLEnabled]", null, "enable", Boolean.valueOf(z));
        f1999a = z;
    }

    public static boolean isSSLEnabled() {
        return f1999a;
    }

    public static void setSpdyEnabled(boolean z) {
        ALog.i("anet.NetworkConfigCenter", "[setSpdyEnabled]", null, "enable", Boolean.valueOf(z));
        f2000b = z;
    }

    public static boolean isSpdyEnabled() {
        return f2000b;
    }

    public static void setServiceBindWaitTime(int i2) {
        f2002d = i2;
    }

    public static int getServiceBindWaitTime() {
        return f2002d;
    }

    public static void setRemoteNetworkServiceEnable(boolean z) {
        f2001c = z;
    }

    public static boolean isRemoteNetworkServiceEnable() {
        return f2001c;
    }

    public static void setRemoteConfig(IRemoteConfig iRemoteConfig) {
        if (x != null) {
            x.unRegister();
        }
        if (iRemoteConfig != null) {
            iRemoteConfig.register();
        }
        x = iRemoteConfig;
    }

    public static boolean isHttpSessionEnable() {
        return e;
    }

    public static void setHttpSessionEnable(boolean z) {
        e = z;
    }

    public static boolean isAllowHttpIpRetry() {
        return e && g;
    }

    public static void setAllowHttpIpRetry(boolean z) {
        g = z;
    }

    public static boolean isHttpCacheEnable() {
        return f;
    }

    public static void setHttpCacheEnable(boolean z) {
        f = z;
    }

    public static void setCacheFlag(long j2) {
        if (j2 != h) {
            ALog.i("anet.NetworkConfigCenter", "set cache flag", null, "old", Long.valueOf(h), "new", Long.valueOf(j2));
            h = j2;
            SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(NetworkSdkSetting.getContext()).edit();
            editorEdit.putLong("Cache.Flag", h);
            editorEdit.apply();
            CacheManager.clearAllCache();
        }
    }

    public static void updateWhiteListMap(String str) {
        if (ALog.isPrintLog(2)) {
            ALog.i("anet.NetworkConfigCenter", "updateWhiteUrlList", null, "White List", str);
        }
        if (TextUtils.isEmpty(str)) {
            j = null;
            return;
        }
        ConcurrentHashMap<String, List<String>> concurrentHashMap = new ConcurrentHashMap<>();
        try {
            JSONObject jSONObject = new JSONObject(str);
            Iterator<String> itKeys = jSONObject.keys();
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                Object obj = jSONObject.get(next);
                try {
                    if (WebSocketServerHandshaker.SUB_PROTOCOL_WILDCARD.equals(obj)) {
                        concurrentHashMap.put(next, l);
                    } else if (obj instanceof JSONArray) {
                        JSONArray jSONArray = (JSONArray) obj;
                        int length = jSONArray.length();
                        ArrayList arrayList = new ArrayList(length);
                        for (int i2 = 0; i2 < length; i2++) {
                            Object obj2 = jSONArray.get(i2);
                            if (obj2 instanceof String) {
                                arrayList.add((String) obj2);
                            }
                        }
                        if (!arrayList.isEmpty()) {
                            concurrentHashMap.put(next, arrayList);
                        }
                    }
                } catch (JSONException unused) {
                }
            }
        } catch (JSONException e2) {
            ALog.e("anet.NetworkConfigCenter", "parse jsonObject failed", null, e2, new Object[0]);
        }
        j = concurrentHashMap;
    }

    public static void updateBizWhiteList(String str) {
        if (ALog.isPrintLog(2)) {
            ALog.i("anet.NetworkConfigCenter", "updateRequestWhiteList", null, "White List", str);
        }
        if (TextUtils.isEmpty(str)) {
            k = null;
            return;
        }
        try {
            JSONArray jSONArray = new JSONArray(str);
            int length = jSONArray.length();
            CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
            for (int i2 = 0; i2 < length; i2++) {
                String string = jSONArray.getString(i2);
                if (!string.isEmpty()) {
                    copyOnWriteArrayList.add(string);
                }
            }
            k = copyOnWriteArrayList;
        } catch (JSONException e2) {
            ALog.e("anet.NetworkConfigCenter", "parse bizId failed", null, e2, new Object[0]);
        }
    }

    public static boolean isUrlInWhiteList(HttpUrl httpUrl) {
        ConcurrentHashMap<String, List<String>> concurrentHashMap;
        List<String> list;
        if (httpUrl == null || (concurrentHashMap = j) == null || (list = concurrentHashMap.get(httpUrl.host())) == null) {
            return false;
        }
        if (list == l) {
            return true;
        }
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            if (httpUrl.path().startsWith(it.next())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBizInWhiteList(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        CopyOnWriteArrayList<String> copyOnWriteArrayList = k;
        if (k == null) {
            return false;
        }
        Iterator<String> it = copyOnWriteArrayList.iterator();
        while (it.hasNext()) {
            if (str.equalsIgnoreCase(it.next())) {
                return true;
            }
        }
        return false;
    }

    public static int getRequestStatisticSampleRate() {
        return m;
    }

    public static void setRequestStatisticSampleRate(int i2) {
        m = i2;
    }

    public static boolean isBgRequestForbidden() {
        return i;
    }

    public static void setBgRequestForbidden(boolean z) {
        i = z;
    }

    public static void setAmdcPresetHosts(String str) {
        if (GlobalAppRuntimeInfo.isTargetProcess()) {
            try {
                JSONArray jSONArray = new JSONArray(str);
                int length = jSONArray.length();
                ArrayList arrayList = new ArrayList(length);
                for (int i2 = 0; i2 < length; i2++) {
                    String string = jSONArray.getString(i2);
                    if (c.c(string)) {
                        arrayList.add(string);
                    }
                }
                HttpDispatcher.getInstance().addHosts(arrayList);
            } catch (JSONException e2) {
                ALog.e("anet.NetworkConfigCenter", "parse hosts failed", null, e2, new Object[0]);
            }
        }
    }

    public static boolean isResponseBufferEnable() {
        return n;
    }

    public static void setResponseBufferEnable(boolean z) {
        n = z;
    }

    public static boolean isGetSessionAsyncEnable() {
        return o;
    }

    public static void setGetSessionAsyncEnable(boolean z) {
        o = z;
    }

    public static int getBgForbidRequestThreshold() {
        return p;
    }

    public static void setBgForbidRequestThreshold(int i2) {
        p = i2;
    }

    public static void setMonitorRequestList(String str) {
        if (TextUtils.isEmpty(str)) {
            q = null;
        }
        try {
            JSONArray jSONArray = new JSONObject(str).getJSONArray("host");
            int length = jSONArray.length();
            CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
            for (int i2 = 0; i2 < length; i2++) {
                String string = jSONArray.getString(i2);
                if (c.c(string)) {
                    copyOnWriteArrayList.add(string);
                }
            }
            q = copyOnWriteArrayList;
        } catch (JSONException e2) {
            ALog.e("anet.NetworkConfigCenter", "parse hosts failed", null, e2, new Object[0]);
        }
    }

    public static boolean isRequestInMonitorList(RequestStatistic requestStatistic) {
        CopyOnWriteArrayList<String> copyOnWriteArrayList;
        if (requestStatistic == null || (copyOnWriteArrayList = q) == null || TextUtils.isEmpty(requestStatistic.host)) {
            return false;
        }
        Iterator<String> it = copyOnWriteArrayList.iterator();
        while (it.hasNext()) {
            if (requestStatistic.host.equalsIgnoreCase(it.next())) {
                return true;
            }
        }
        return false;
    }

    public static void setDegradeRequestList(String str) {
        if (ALog.isPrintLog(2)) {
            ALog.i("anet.NetworkConfigCenter", "setDegradeRequestList", null, "Degrade List", str);
        }
        if (TextUtils.isEmpty(str)) {
            r = null;
            return;
        }
        ConcurrentHashMap<String, List<String>> concurrentHashMap = new ConcurrentHashMap<>();
        try {
            JSONObject jSONObject = new JSONObject(str);
            Iterator<String> itKeys = jSONObject.keys();
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                Object obj = jSONObject.get(next);
                try {
                    if (WebSocketServerHandshaker.SUB_PROTOCOL_WILDCARD.equals(obj)) {
                        concurrentHashMap.put(next, l);
                    } else if (obj instanceof JSONArray) {
                        JSONArray jSONArray = (JSONArray) obj;
                        int length = jSONArray.length();
                        ArrayList arrayList = new ArrayList(length);
                        for (int i2 = 0; i2 < length; i2++) {
                            Object obj2 = jSONArray.get(i2);
                            if (obj2 instanceof String) {
                                arrayList.add((String) obj2);
                            }
                        }
                        if (!arrayList.isEmpty()) {
                            concurrentHashMap.put(next, arrayList);
                        }
                    }
                } catch (JSONException unused) {
                }
            }
        } catch (JSONException e2) {
            ALog.e("anet.NetworkConfigCenter", "parse jsonObject failed", null, e2, new Object[0]);
        }
        r = concurrentHashMap;
    }

    public static boolean isUrlInDegradeList(HttpUrl httpUrl) {
        ConcurrentHashMap<String, List<String>> concurrentHashMap;
        List<String> list;
        if (httpUrl == null || (concurrentHashMap = r) == null || (list = concurrentHashMap.get(httpUrl.host())) == null) {
            return false;
        }
        if (list == l) {
            return true;
        }
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            if (httpUrl.path().startsWith(it.next())) {
                return true;
            }
        }
        return false;
    }

    public static void enableNetworkSdkOptimizeTest(boolean z) {
        if (z) {
            setGetSessionAsyncEnable(true);
            ThreadPoolExecutorFactory.setNormalExecutorPoolSize(16);
            AwcnConfig.registerPresetSessions("[{\"host\":\"trade-acs.m.taobao.com\", \"protocol\":\"http2\", \"rtt\":\"0rtt\", \"publicKey\": \"acs\", \"isKeepAlive\":true}]");
        } else {
            setGetSessionAsyncEnable(false);
            ThreadPoolExecutorFactory.setNormalExecutorPoolSize(6);
        }
    }

    public static boolean isRequestDelayRetryForNoNetwork() {
        return s;
    }

    public static void setRequestDelayRetryForNoNetwork(boolean z) {
        s = z;
    }

    public static boolean isBindServiceOptimize() {
        return t;
    }

    public static void setBindServiceOptimize(boolean z) {
        t = z;
    }

    public static void setChannelLocalInstanceEnable(boolean z) {
        u = z;
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(NetworkSdkSetting.getContext()).edit();
        editorEdit.putBoolean("CHANNEL_LOCAL_INSTANCE_ENABLE", u);
        editorEdit.apply();
    }

    public static boolean isChannelLocalInstanceEnable() {
        return u;
    }

    public static void setAllowSpdyWhenBindServiceFailed(boolean z) {
        v = z;
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(NetworkSdkSetting.getContext()).edit();
        editorEdit.putBoolean("ALLOW_SPDY_WHEN_BIND_SERVICE_FAILED", v);
        editorEdit.apply();
    }

    public static boolean isAllowSpdyWhenBindServiceFailed() {
        return v;
    }

    public static void setCookieEnable(boolean z) {
        w = z;
    }

    public static boolean isCookieEnable() {
        return w;
    }
}
