package anet.channel.detect;

import android.text.TextUtils;
import android.util.Pair;
import anet.channel.AwcnConfig;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.statist.RequestStatistic;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.IConnStrategy;
import anet.channel.strategy.StrategyCenter;
import anet.channel.strategy.dispatch.DispatchConstants;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import config.Constants;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;
import org.android.netutil.NetUtils;
import org.android.netutil.PingEntry;
import org.android.netutil.PingResponse;
import org.android.netutil.PingTask;
import org.android.spdy.SpdyAgent;
import org.android.spdy.SpdySessionKind;
import org.android.spdy.SpdyVersion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class ExceptionDetector {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    long f1684a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    String f1685b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    String f1686c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    String f1687d;
    LimitedQueue<Pair<String, Integer>> e = new LimitedQueue<>(10);

    ExceptionDetector() {
    }

    public void a() {
        NetworkStatusHelper.addStatusChangeListener(new anet.channel.detect.a(this));
    }

    public void a(RequestStatistic requestStatistic) {
        if (!AwcnConfig.isNetworkDetectEnable()) {
            ALog.i("anet.ExceptionDetector", "network detect closed.", null, new Object[0]);
        } else {
            ThreadPoolExecutorFactory.submitDetectTask(new c(this, requestStatistic));
        }
    }

    void b() throws JSONException {
        ALog.e("anet.ExceptionDetector", "network detect start.", null, new Object[0]);
        SpdyAgent.getInstance(GlobalAppRuntimeInfo.getContext(), SpdyVersion.SPDY3, SpdySessionKind.NONE_SESSION);
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        NetworkStatusHelper.NetworkStatus status = NetworkStatusHelper.getStatus();
        jSONObject2.put("status", status.getType());
        jSONObject2.put("subType", NetworkStatusHelper.getNetworkSubType());
        if (status != NetworkStatusHelper.NetworkStatus.NO) {
            if (status.isMobile()) {
                jSONObject2.put("apn", NetworkStatusHelper.getApn());
                jSONObject2.put(DispatchConstants.CARRIER, NetworkStatusHelper.getCarrier());
            } else {
                jSONObject2.put("bssid", NetworkStatusHelper.getWifiBSSID());
                jSONObject2.put("ssid", NetworkStatusHelper.getWifiSSID());
            }
            jSONObject2.put("proxy", NetworkStatusHelper.getProxyType());
        }
        jSONObject.put(Constants.NETWORK_INFO, jSONObject2);
        String defaultGateway = status.isWifi() ? NetUtils.getDefaultGateway("114.114.114.114") : NetUtils.getPreferNextHop("114.114.114.114", 2);
        Future<PingResponse> futureLaunch = !TextUtils.isEmpty(defaultGateway) ? new PingTask(defaultGateway, 1000, 3, 0, 0).launch() : null;
        a aVarA = a("guide-acs.m.taobao.com", this.f1685b);
        a aVarA2 = a("gw.alicdn.com", this.f1687d);
        a aVarA3 = a("msgacs.m.taobao.com", this.f1686c);
        JSONObject jSONObject3 = new JSONObject();
        jSONObject3.put("nextHop", defaultGateway);
        jSONObject3.put("ping", a(futureLaunch));
        jSONObject.put("LocalDetect", jSONObject3);
        JSONArray jSONArray = new JSONArray();
        jSONArray.put(a(aVarA));
        jSONArray.put(a(aVarA2));
        jSONArray.put(a(aVarA3));
        jSONObject.put("InternetDetect", jSONArray);
        JSONObject jSONObject4 = new JSONObject();
        Iterator it = this.e.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            jSONObject4.put((String) pair.first, pair.second);
        }
        jSONObject.put("BizDetect", jSONObject4);
        this.e.clear();
        ALog.e("anet.ExceptionDetector", "network detect result: " + jSONObject.toString(), null, new Object[0]);
    }

    boolean c() {
        if (this.e.size() != 10) {
            return false;
        }
        if (NetworkStatusHelper.getStatus() == NetworkStatusHelper.NetworkStatus.NO) {
            ALog.e("anet.ExceptionDetector", "no network", null, new Object[0]);
            return false;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (jCurrentTimeMillis < this.f1684a) {
            return false;
        }
        Iterator it = this.e.iterator();
        int i = 0;
        while (it.hasNext()) {
            int iIntValue = ((Integer) ((Pair) it.next()).second).intValue();
            if (iIntValue == -202 || iIntValue == -400 || iIntValue == -401 || iIntValue == -405 || iIntValue == -406) {
                i++;
            }
        }
        boolean z = i * 2 > 10;
        if (z) {
            this.f1684a = jCurrentTimeMillis + 1800000;
        }
        return z;
    }

    private ArrayList<String> a(String str, int i) {
        PingResponse pingResponse;
        ArrayList<String> arrayList = new ArrayList<>();
        if (TextUtils.isEmpty(str)) {
            return arrayList;
        }
        int i2 = 0;
        while (i2 < i) {
            i2++;
            try {
                pingResponse = (PingResponse) new PingTask(str, 0, 1, 0, i2).launch().get();
            } catch (Exception unused) {
                pingResponse = null;
            }
            StringBuilder sb = new StringBuilder();
            if (pingResponse != null) {
                String lastHopIPStr = pingResponse.getLastHopIPStr();
                double d2 = pingResponse.getResults()[0].rtt;
                int errcode = pingResponse.getErrcode();
                if (TextUtils.isEmpty(lastHopIPStr)) {
                    lastHopIPStr = WebSocketServerHandshaker.SUB_PROTOCOL_WILDCARD;
                }
                sb.append("hop=");
                sb.append(lastHopIPStr);
                sb.append(",rtt=");
                sb.append(d2);
                sb.append(",errCode=");
                sb.append(errcode);
            }
            arrayList.add(sb.toString());
        }
        return arrayList;
    }

    private a a(String str, String str2) {
        a aVar = new a(this, null);
        aVar.f1690a = str;
        try {
            aVar.f1691b = InetAddress.getByName(str).getHostAddress();
        } catch (UnknownHostException unused) {
        }
        if (!TextUtils.isEmpty(str2)) {
            aVar.f1692c = str2;
        } else {
            List<IConnStrategy> connStrategyListByHost = StrategyCenter.getInstance().getConnStrategyListByHost(str);
            if (connStrategyListByHost != null && !connStrategyListByHost.isEmpty()) {
                aVar.f1692c = connStrategyListByHost.get(0).getIp();
            }
        }
        String str3 = !TextUtils.isEmpty(aVar.f1692c) ? aVar.f1692c : aVar.f1691b;
        if (!TextUtils.isEmpty(str3)) {
            String str4 = str3;
            aVar.f1693d = new PingTask(str4, 1000, 3, 0, 0).launch();
            aVar.e = new PingTask(str4, 1000, 3, 1172, 0).launch();
            aVar.f = new PingTask(str4, 1000, 3, 1432, 0).launch();
        }
        return aVar;
    }

    private JSONObject a(a aVar) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        if (aVar == null || aVar.f1693d == null) {
            return jSONObject;
        }
        jSONObject.put("host", aVar.f1690a);
        jSONObject.put("currentIp", aVar.f1692c);
        jSONObject.put("localIp", aVar.f1691b);
        jSONObject.put("ping", a(aVar.f1693d));
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put("1200", a(aVar.e));
        jSONObject2.put("1460", a(aVar.f));
        jSONObject.put("MTU", jSONObject2);
        if ("guide-acs.m.taobao.com".equals(aVar.f1690a)) {
            ArrayList<String> arrayListA = a(!TextUtils.isEmpty(aVar.f1692c) ? aVar.f1692c : aVar.f1691b, 5);
            JSONObject jSONObject3 = new JSONObject();
            int i = 0;
            while (i < arrayListA.size()) {
                int i2 = i + 1;
                jSONObject3.put(String.valueOf(i2), arrayListA.get(i));
                i = i2;
            }
            jSONObject.put("traceRoute", jSONObject3);
        }
        return jSONObject;
    }

    private JSONObject a(Future<PingResponse> future) throws JSONException {
        PingResponse pingResponse;
        JSONObject jSONObject = new JSONObject();
        if (future == null) {
            return jSONObject;
        }
        try {
            pingResponse = future.get();
        } catch (Exception unused) {
            pingResponse = null;
        }
        if (pingResponse == null) {
            return jSONObject;
        }
        jSONObject.put("errCode", pingResponse.getErrcode());
        JSONArray jSONArray = new JSONArray();
        for (PingEntry pingEntry : pingResponse.getResults()) {
            jSONArray.put("seq=" + pingEntry.seq + ",hop=" + pingEntry.hop + ",rtt=" + pingEntry.rtt);
        }
        jSONObject.put("response", jSONArray);
        return jSONObject;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: Taobao */
    class LimitedQueue<E> extends LinkedList<E> {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private int f1689b;

        public LimitedQueue(int i) {
            this.f1689b = i;
        }

        @Override // java.util.LinkedList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List, java.util.Deque, java.util.Queue
        public boolean add(E e) {
            boolean zAdd = super.add(e);
            while (zAdd && size() > this.f1689b) {
                super.remove();
            }
            return zAdd;
        }
    }

    /* JADX INFO: compiled from: Taobao */
    private class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        String f1690a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        String f1691b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        String f1692c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        Future<PingResponse> f1693d;
        Future<PingResponse> e;
        Future<PingResponse> f;

        private a() {
        }

        /* synthetic */ a(ExceptionDetector exceptionDetector, anet.channel.detect.a aVar) {
            this();
        }
    }
}
