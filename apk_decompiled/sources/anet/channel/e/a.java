package anet.channel.e;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import anet.channel.AwcnConfig;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.IConnStrategy;
import anet.channel.strategy.IStrategyFilter;
import anet.channel.strategy.IStrategyListener;
import anet.channel.strategy.StrategyCenter;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.android.spdy.SpdyAgent;
import org.android.spdy.SpdySessionKind;
import org.android.spdy.SpdyVersion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static b f1722a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static String f1723b;
    private static SharedPreferences f;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static AtomicBoolean f1724c = new AtomicBoolean(false);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static AtomicBoolean f1725d = new AtomicBoolean(false);
    private static long e = 21600000;
    private static IStrategyFilter g = new anet.channel.e.b();
    private static AtomicInteger h = new AtomicInteger(1);
    private static IStrategyListener i = new c();
    private static NetworkStatusHelper.INetworkStatusChangeListener j = new d();

    public static void a(NetworkStatusHelper.NetworkStatus networkStatus) {
        if (!AwcnConfig.isHttp3Enable()) {
            ALog.i("awcn.Http3ConnDetector", "startDetect", null, "http3 global config close.");
            return;
        }
        if (f1725d.get()) {
            ALog.e("awcn.Http3ConnDetector", "tnet exception.", null, new Object[0]);
            return;
        }
        if (NetworkStatusHelper.isConnected()) {
            if (TextUtils.isEmpty(f1723b)) {
                ALog.e("awcn.Http3ConnDetector", "startDetect", null, "host is null");
                return;
            }
            List<IConnStrategy> connStrategyListByHost = StrategyCenter.getInstance().getConnStrategyListByHost(f1723b, g);
            if (connStrategyListByHost.isEmpty()) {
                ALog.e("awcn.Http3ConnDetector", "startDetect", null, "http3 strategy is null.");
                return;
            }
            if (f1724c.compareAndSet(false, true)) {
                try {
                    long jCurrentTimeMillis = System.currentTimeMillis();
                    SpdyAgent.getInstance(GlobalAppRuntimeInfo.getContext(), SpdyVersion.SPDY3, SpdySessionKind.NONE_SESSION).InitializeSecurityStuff();
                    ALog.e("awcn.Http3ConnDetector", "tnet init http3.", null, "cost", Long.valueOf(System.currentTimeMillis() - jCurrentTimeMillis));
                } catch (Throwable th) {
                    ALog.e("awcn.Http3ConnDetector", "tnet init http3 error.", null, th, new Object[0]);
                    f1725d.set(true);
                    return;
                }
            }
            if (f1722a == null) {
                f1722a = new b();
            }
            if (f1722a.a(NetworkStatusHelper.getUniqueId(networkStatus))) {
                ThreadPoolExecutorFactory.submitDetectTask(new e(connStrategyListByHost, networkStatus));
            }
        }
    }

    public static void a() {
        try {
            ALog.e("awcn.Http3ConnDetector", "registerListener", null, "http3Enable", Boolean.valueOf(AwcnConfig.isHttp3Enable()));
            f = PreferenceManager.getDefaultSharedPreferences(GlobalAppRuntimeInfo.getContext());
            f1723b = f.getString("http3_detector_host", "");
            a(NetworkStatusHelper.getStatus());
            NetworkStatusHelper.addStatusChangeListener(j);
            StrategyCenter.getInstance().registerListener(i);
        } catch (Exception e2) {
            ALog.e("awcn.Http3ConnDetector", "[registerListener]error", null, e2, new Object[0]);
        }
    }

    public static void a(long j2) {
        if (j2 < 0) {
            return;
        }
        e = j2;
    }

    public static boolean b() {
        b bVar = f1722a;
        if (bVar != null) {
            return bVar.b(NetworkStatusHelper.getUniqueId(NetworkStatusHelper.getStatus()));
        }
        return false;
    }

    public static void a(boolean z) {
        b bVar = f1722a;
        if (bVar != null) {
            bVar.a(NetworkStatusHelper.getUniqueId(NetworkStatusHelper.getStatus()), z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static IConnStrategy b(IConnStrategy iConnStrategy) {
        return new g(iConnStrategy);
    }

    /* JADX INFO: renamed from: anet.channel.e.a$a, reason: collision with other inner class name */
    /* JADX INFO: compiled from: Taobao */
    private static class C0171a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        long f1726a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        boolean f1727b;

        private C0171a() {
        }

        /* synthetic */ C0171a(anet.channel.e.b bVar) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: Taobao */
    static class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private Map<String, C0171a> f1728a = new ConcurrentHashMap();

        b() {
            a();
        }

        private void a() {
            anet.channel.e.b bVar = null;
            String string = a.f.getString("networksdk_http3_history_records", null);
            if (TextUtils.isEmpty(string)) {
                return;
            }
            try {
                JSONArray jSONArray = new JSONArray(string);
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject jSONObject = (JSONObject) jSONArray.get(i);
                    C0171a c0171a = new C0171a(bVar);
                    String string2 = jSONObject.getString("networkUniqueId");
                    c0171a.f1726a = jSONObject.getLong("time");
                    c0171a.f1727b = jSONObject.getBoolean("enable");
                    if (a(c0171a.f1726a)) {
                        synchronized (this.f1728a) {
                            this.f1728a.put(string2, c0171a);
                        }
                    }
                }
            } catch (JSONException unused) {
            }
        }

        boolean a(String str) {
            synchronized (this.f1728a) {
                C0171a c0171a = this.f1728a.get(str);
                boolean z = true;
                if (c0171a == null) {
                    return true;
                }
                if (a(c0171a.f1726a)) {
                    z = false;
                }
                return z;
            }
        }

        private boolean a(long j) {
            return System.currentTimeMillis() - j < a.e;
        }

        void a(String str, boolean z) {
            C0171a c0171a = new C0171a(null);
            c0171a.f1727b = z;
            c0171a.f1726a = System.currentTimeMillis();
            JSONArray jSONArray = new JSONArray();
            synchronized (this.f1728a) {
                this.f1728a.put(str, c0171a);
                for (Map.Entry<String, C0171a> entry : this.f1728a.entrySet()) {
                    String key = entry.getKey();
                    C0171a value = entry.getValue();
                    try {
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("networkUniqueId", key);
                        jSONObject.put("time", value.f1726a);
                        jSONObject.put("enable", value.f1727b);
                        jSONArray.put(jSONObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            a.f.edit().putString("networksdk_http3_history_records", jSONArray.toString()).apply();
        }

        boolean b(String str) {
            synchronized (this.f1728a) {
                C0171a c0171a = this.f1728a.get(str);
                if (c0171a == null) {
                    return false;
                }
                return c0171a.f1727b;
            }
        }
    }
}
