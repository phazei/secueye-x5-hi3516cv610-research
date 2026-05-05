package anet.channel.status;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Pair;
import anet.channel.AwcnConfig;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.hjq.permissions.Permission;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class b {
    private static Method t;
    private static String[] m = {"net.dns1", "net.dns2", "net.dns3", "net.dns4"};

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    static volatile Context f1833a = null;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    static volatile boolean f1834b = false;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    static volatile NetworkStatusHelper.NetworkStatus f1835c = NetworkStatusHelper.NetworkStatus.NONE;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    static volatile String f1836d = "unknown";
    static volatile String e = "";
    static volatile String f = "";
    static volatile String g = "";
    static volatile String h = "unknown";
    static volatile String i = "";
    static volatile Pair<String, Integer> j = null;
    static volatile boolean k = false;
    static volatile List<InetAddress> l = Collections.EMPTY_LIST;
    private static volatile boolean n = false;
    private static volatile boolean o = false;
    private static ConnectivityManager p = null;
    private static TelephonyManager q = null;
    private static WifiManager r = null;
    private static SubscriptionManager s = null;
    private static BroadcastReceiver u = new BroadcastReceiver() { // from class: anet.channel.status.NetworkStatusMonitor$2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (ALog.isPrintLog(1)) {
                ALog.d("awcn.NetworkStatusMonitor", "receiver:" + intent.getAction(), null, new Object[0]);
            }
            ThreadPoolExecutorFactory.submitScheduledTask(new d(this));
        }
    };

    b() {
    }

    static void a() {
        if (n || f1833a == null) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        try {
            f1833a.registerReceiver(u, intentFilter);
        } catch (Exception unused) {
            ALog.e("awcn.NetworkStatusMonitor", "registerReceiver failed", null, new Object[0]);
        }
        d();
        n = true;
    }

    static void b() {
        if (f1833a != null) {
            f1833a.unregisterReceiver(u);
        }
    }

    static void c() {
        if (Build.VERSION.SDK_INT < 24 || o) {
            return;
        }
        NetworkInfo networkInfoE = e();
        f1834b = networkInfoE != null && networkInfoE.isConnected();
        p.registerDefaultNetworkCallback(new c());
        o = true;
    }

    static void d() {
        NetworkInfo networkInfoE;
        boolean z;
        WifiInfo wifiInfoI;
        ALog.d("awcn.NetworkStatusMonitor", "[checkNetworkStatus]", null, new Object[0]);
        NetworkStatusHelper.NetworkStatus networkStatus = f1835c;
        String str = e;
        String str2 = f;
        try {
            try {
                networkInfoE = e();
                z = false;
            } catch (Exception e2) {
                ALog.e("awcn.NetworkStatusMonitor", "getNetworkInfo exception", null, e2, new Object[0]);
                a(NetworkStatusHelper.NetworkStatus.NONE, "unknown");
                networkInfoE = null;
                z = true;
            }
            if (!z) {
                if (networkInfoE == null || !networkInfoE.isConnected()) {
                    a(NetworkStatusHelper.NetworkStatus.NO, "no network");
                    ALog.i("awcn.NetworkStatusMonitor", "checkNetworkStatus", null, "no network");
                } else {
                    ALog.i("awcn.NetworkStatusMonitor", "checkNetworkStatus", null, "info.isConnected", Boolean.valueOf(networkInfoE.isConnected()), "info.isAvailable", Boolean.valueOf(networkInfoE.isAvailable()), "info.getType", Integer.valueOf(networkInfoE.getType()));
                    if (networkInfoE.getType() == 0) {
                        String subtypeName = networkInfoE.getSubtypeName();
                        String strReplace = !TextUtils.isEmpty(subtypeName) ? subtypeName.replace(" ", "") : "";
                        a(a(networkInfoE.getSubtype(), strReplace), strReplace);
                        e = a(networkInfoE.getExtraInfo());
                        h();
                    } else if (networkInfoE.getType() == 1) {
                        a(NetworkStatusHelper.NetworkStatus.WIFI, "wifi");
                        if (AwcnConfig.isWifiInfoEnable() && (wifiInfoI = i()) != null && b(Permission.ACCESS_FINE_LOCATION)) {
                            g = wifiInfoI.getBSSID();
                            f = wifiInfoI.getSSID();
                        }
                        h = "wifi";
                        i = "wifi";
                        j = j();
                    } else {
                        a(NetworkStatusHelper.NetworkStatus.NONE, "unknown");
                    }
                    k = networkInfoE.isRoaming();
                    anet.channel.util.c.e();
                }
            }
            if (f1835c == networkStatus && e.equalsIgnoreCase(str) && f.equalsIgnoreCase(str2)) {
                return;
            }
            if (ALog.isPrintLog(2)) {
                NetworkStatusHelper.printNetworkDetail();
            }
            NetworkStatusHelper.notifyStatusChanged(f1835c);
        } catch (Exception e3) {
            ALog.e("awcn.NetworkStatusMonitor", "checkNetworkStatus", null, e3, new Object[0]);
        }
    }

    private static void a(NetworkStatusHelper.NetworkStatus networkStatus, String str) {
        f1835c = networkStatus;
        f1836d = str;
        e = "";
        f = "";
        g = "";
        j = null;
        h = "";
        i = "";
    }

    private static NetworkStatusHelper.NetworkStatus a(int i2, String str) {
        switch (i2) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
            case 16:
                return NetworkStatusHelper.NetworkStatus.G2;
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
            case 17:
                return NetworkStatusHelper.NetworkStatus.G3;
            case 13:
            case 18:
            case 19:
                return NetworkStatusHelper.NetworkStatus.G4;
            case 20:
                return NetworkStatusHelper.NetworkStatus.G5;
            default:
                if (str.equalsIgnoreCase("TD-SCDMA") || str.equalsIgnoreCase("WCDMA") || str.equalsIgnoreCase("CDMA2000")) {
                    return NetworkStatusHelper.NetworkStatus.G3;
                }
                return NetworkStatusHelper.NetworkStatus.NONE;
        }
    }

    private static String a(String str) {
        if (TextUtils.isEmpty(str)) {
            return "unknown";
        }
        String lowerCase = str.toLowerCase(Locale.US);
        return lowerCase.contains("cmwap") ? "cmwap" : lowerCase.contains("uniwap") ? "uniwap" : lowerCase.contains("3gwap") ? "3gwap" : lowerCase.contains("ctwap") ? "ctwap" : lowerCase.contains("cmnet") ? "cmnet" : lowerCase.contains("uninet") ? "uninet" : lowerCase.contains("3gnet") ? "3gnet" : lowerCase.contains("ctnet") ? "ctnet" : "unknown";
    }

    private static void h() {
        try {
            if (AwcnConfig.isCarrierInfoEnable() && b(Permission.READ_PHONE_STATE)) {
                if (q == null) {
                    q = (TelephonyManager) f1833a.getSystemService("phone");
                }
                i = q.getSimOperator();
                if (Build.VERSION.SDK_INT >= 22) {
                    if (s == null) {
                        s = SubscriptionManager.from(f1833a);
                        t = s.getClass().getDeclaredMethod("getDefaultDataSubscriptionInfo", new Class[0]);
                    }
                    if (t != null) {
                        h = ((SubscriptionInfo) t.invoke(s, new Object[0])).getCarrierName().toString();
                    }
                }
            }
        } catch (Exception unused) {
        }
    }

    static NetworkInfo e() {
        if (p == null) {
            p = (ConnectivityManager) f1833a.getSystemService("connectivity");
        }
        return p.getActiveNetworkInfo();
    }

    private static WifiInfo i() {
        try {
            if (r == null) {
                r = (WifiManager) f1833a.getSystemService("wifi");
            }
            return r.getConnectionInfo();
        } catch (Throwable th) {
            ALog.e("awcn.NetworkStatusMonitor", "getWifiInfo", null, th, new Object[0]);
            return null;
        }
    }

    private static Pair<String, Integer> j() {
        try {
            String property = System.getProperty("http.proxyHost");
            if (TextUtils.isEmpty(property)) {
                return null;
            }
            return Pair.create(property, Integer.valueOf(Integer.parseInt(System.getProperty("http.proxyPort"))));
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    static String f() {
        try {
            Method method = Class.forName("android.os.SystemProperties").getMethod(TmpConstant.PROPERTY_IDENTIFIER_GET, String.class);
            for (String str : m) {
                String str2 = (String) method.invoke(null, str);
                if (!TextUtils.isEmpty(str2)) {
                    return str2;
                }
            }
        } catch (Exception unused) {
        }
        return null;
    }

    static int g() {
        if (p == null || Build.VERSION.SDK_INT < 24) {
            return -1;
        }
        return p.getRestrictBackgroundStatus();
    }

    private static boolean b(String str) {
        return Build.VERSION.SDK_INT >= 23 && f1833a.checkSelfPermission(str) == 0;
    }
}
