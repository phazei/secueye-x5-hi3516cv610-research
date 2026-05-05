package anet.channel.strategy;

import anet.channel.strategy.dispatch.DispatchConstants;
import anet.channel.util.ALog;
import com.taobao.accs.utl.BaseMonitor;
import io.netty.handler.codec.rtsp.RtspHeaders;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class l {
    public static d a(JSONObject jSONObject) {
        try {
            return new d(jSONObject);
        } catch (Exception e2) {
            ALog.e("StrategyResultParser", "Parse HttpDns response failed.", null, e2, "JSON Content", jSONObject.toString());
            return null;
        }
    }

    /* JADX INFO: compiled from: Taobao */
    public static class e {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final String f1915a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final a f1916b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final String f1917c;

        public e(JSONObject jSONObject) {
            this.f1915a = jSONObject.optString("ip");
            this.f1917c = jSONObject.optString("path");
            this.f1916b = new a(jSONObject);
        }
    }

    /* JADX INFO: compiled from: Taobao */
    public static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final int f1901a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final String f1902b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final int f1903c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public final int f1904d;
        public final int e;
        public final int f;
        public final String g;
        public final String h;

        public a(JSONObject jSONObject) {
            this.f1901a = jSONObject.optInt(RtspHeaders.Values.PORT);
            this.f1902b = jSONObject.optString("protocol");
            this.f1903c = jSONObject.optInt("cto");
            this.f1904d = jSONObject.optInt("rto");
            this.e = jSONObject.optInt("retry");
            this.f = jSONObject.optInt("heartbeat");
            this.g = jSONObject.optString("rtt", "");
            this.h = jSONObject.optString("publickey");
        }
    }

    /* JADX INFO: compiled from: Taobao */
    public static class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final String f1909a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final e[] f1910b;

        public c(JSONObject jSONObject) {
            this.f1909a = jSONObject.optString("host");
            JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("strategies");
            if (jSONArrayOptJSONArray != null) {
                int length = jSONArrayOptJSONArray.length();
                this.f1910b = new e[length];
                for (int i = 0; i < length; i++) {
                    this.f1910b[i] = new e(jSONArrayOptJSONArray.optJSONObject(i));
                }
                return;
            }
            this.f1910b = null;
        }
    }

    /* JADX INFO: compiled from: Taobao */
    public static class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final String f1905a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final int f1906b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final String f1907c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public final String f1908d;
        public final String e;
        public final String[] f;
        public final String[] g;
        public final a[] h;
        public final e[] i;
        public final boolean j;
        public final boolean k;
        public final int l;

        public b(JSONObject jSONObject) {
            this.f1905a = jSONObject.optString("host");
            this.f1906b = jSONObject.optInt("ttl");
            this.f1907c = jSONObject.optString("safeAisles");
            this.f1908d = jSONObject.optString("cname", null);
            this.e = jSONObject.optString("unit", null);
            this.j = jSONObject.optInt("clear") == 1;
            this.k = jSONObject.optBoolean("effectNow");
            this.l = jSONObject.optInt("version");
            JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("ips");
            if (jSONArrayOptJSONArray != null) {
                int length = jSONArrayOptJSONArray.length();
                this.f = new String[length];
                for (int i = 0; i < length; i++) {
                    this.f[i] = jSONArrayOptJSONArray.optString(i);
                }
            } else {
                this.f = null;
            }
            JSONArray jSONArrayOptJSONArray2 = jSONObject.optJSONArray("sips");
            if (jSONArrayOptJSONArray2 != null && jSONArrayOptJSONArray2.length() > 0) {
                int length2 = jSONArrayOptJSONArray2.length();
                this.g = new String[length2];
                for (int i2 = 0; i2 < length2; i2++) {
                    this.g[i2] = jSONArrayOptJSONArray2.optString(i2);
                }
            } else {
                this.g = null;
            }
            JSONArray jSONArrayOptJSONArray3 = jSONObject.optJSONArray("aisles");
            if (jSONArrayOptJSONArray3 != null) {
                int length3 = jSONArrayOptJSONArray3.length();
                this.h = new a[length3];
                for (int i3 = 0; i3 < length3; i3++) {
                    this.h[i3] = new a(jSONArrayOptJSONArray3.optJSONObject(i3));
                }
            } else {
                this.h = null;
            }
            JSONArray jSONArrayOptJSONArray4 = jSONObject.optJSONArray("strategies");
            if (jSONArrayOptJSONArray4 != null && jSONArrayOptJSONArray4.length() > 0) {
                int length4 = jSONArrayOptJSONArray4.length();
                this.i = new e[length4];
                for (int i4 = 0; i4 < length4; i4++) {
                    this.i[i4] = new e(jSONArrayOptJSONArray4.optJSONObject(i4));
                }
                return;
            }
            this.i = null;
        }
    }

    /* JADX INFO: compiled from: Taobao */
    public static class d {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final String f1911a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final b[] f1912b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public final c[] f1913c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public final String f1914d;
        public final String e;
        public final int f;
        public final int g;
        public final int h;

        public d(JSONObject jSONObject) {
            this.f1911a = jSONObject.optString("ip");
            this.f1914d = jSONObject.optString("uid", null);
            this.e = jSONObject.optString("utdid", null);
            this.f = jSONObject.optInt(DispatchConstants.CONFIG_VERSION);
            this.g = jSONObject.optInt("fcl");
            this.h = jSONObject.optInt("fct");
            JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray(BaseMonitor.COUNT_POINT_DNS);
            if (jSONArrayOptJSONArray != null) {
                int length = jSONArrayOptJSONArray.length();
                this.f1912b = new b[length];
                for (int i = 0; i < length; i++) {
                    this.f1912b[i] = new b(jSONArrayOptJSONArray.optJSONObject(i));
                }
            } else {
                this.f1912b = null;
            }
            JSONArray jSONArrayOptJSONArray2 = jSONObject.optJSONArray("hrTask");
            if (jSONArrayOptJSONArray2 != null) {
                int length2 = jSONArrayOptJSONArray2.length();
                this.f1913c = new c[length2];
                for (int i2 = 0; i2 < length2; i2++) {
                    this.f1913c[i2] = new c(jSONArrayOptJSONArray2.optJSONObject(i2));
                }
                return;
            }
            this.f1913c = null;
        }
    }
}
