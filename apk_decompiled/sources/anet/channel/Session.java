package anet.channel;

import android.content.Context;
import android.text.TextUtils;
import anet.channel.entity.ConnType;
import anet.channel.entity.EventCb;
import anet.channel.request.Cancelable;
import anet.channel.request.Request;
import anet.channel.statist.SessionStatistic;
import anet.channel.strategy.IConnStrategy;
import anet.channel.strategy.StrategyCenter;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import anet.channel.util.HttpConstant;
import anet.channel.util.HttpHelper;
import anet.channel.util.StringUtils;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.android.spdy.SpdyAgent;
import org.android.spdy.SpdySessionKind;
import org.android.spdy.SpdyVersion;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public abstract class Session implements Comparable<Session> {
    static ExecutorService v = Executors.newSingleThreadExecutor();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Context f1632a;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public String f1634c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public String f1635d;
    public String e;
    public String f;
    public int g;
    public String h;
    public int i;
    public ConnType j;
    public IConnStrategy k;
    public boolean m;
    protected Runnable o;
    public final String p;
    public final SessionStatistic q;
    public int r;
    public int s;
    private Future<?> x;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    Map<EventCb, Integer> f1633b = new LinkedHashMap();
    private boolean w = false;
    public String l = null;
    public int n = 6;
    public boolean t = false;
    protected boolean u = true;
    private List<Long> y = null;
    private long z = 0;

    public abstract void close();

    public void connect() {
    }

    public abstract Runnable getRecvTimeOutRunnable();

    public abstract boolean isAvailable();

    public void onDisconnect() {
    }

    public void ping(boolean z) {
    }

    public void ping(boolean z, int i) {
    }

    public abstract Cancelable request(Request request, RequestCb requestCb);

    public void sendCustomFrame(int i, byte[] bArr, int i2) {
    }

    /* JADX INFO: compiled from: Taobao */
    public static class a {
        public static final int AUTHING = 3;
        public static final int AUTH_FAIL = 5;
        public static final int AUTH_SUCC = 4;
        public static final int CONNECTED = 0;
        public static final int CONNECTING = 1;
        public static final int CONNETFAIL = 2;
        public static final int DISCONNECTED = 6;
        public static final int DISCONNECTING = 7;

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        static final String[] f1636a = {"CONNECTED", "CONNECTING", "CONNETFAIL", "AUTHING", "AUTH_SUCC", "AUTH_FAIL", "DISCONNECTED", "DISCONNECTING"};

        static String a(int i) {
            return f1636a[i];
        }
    }

    @Override // java.lang.Comparable
    public int compareTo(Session session) {
        return ConnType.compare(this.j, session.j);
    }

    public Session(Context context, anet.channel.entity.a aVar) {
        boolean z = false;
        this.m = false;
        this.f1632a = context;
        this.e = aVar.a();
        this.f = this.e;
        this.g = aVar.b();
        this.j = aVar.c();
        this.f1634c = aVar.f();
        String str = this.f1634c;
        this.f1635d = str.substring(str.indexOf(HttpConstant.SCHEME_SPLIT) + 3);
        this.s = aVar.e();
        this.r = aVar.d();
        this.k = aVar.f1734a;
        IConnStrategy iConnStrategy = this.k;
        if (iConnStrategy != null && iConnStrategy.getIpType() == -1) {
            z = true;
        }
        this.m = z;
        this.p = aVar.h();
        this.q = new SessionStatistic(aVar);
        this.q.host = this.f1635d;
    }

    public void checkAvailable() {
        ping(true);
    }

    public static void configTnetALog(Context context, String str, int i, int i2) {
        SpdyAgent spdyAgent = SpdyAgent.getInstance(context, SpdyVersion.SPDY3, SpdySessionKind.NONE_SESSION);
        if (spdyAgent != null && SpdyAgent.checkLoadSucc()) {
            spdyAgent.configLogFile(str, i, i2);
        } else {
            ALog.e("agent null or configTnetALog load so fail!!!", null, "loadso", Boolean.valueOf(SpdyAgent.checkLoadSucc()));
        }
    }

    public void close(boolean z) {
        this.t = z;
        close();
    }

    public void registerEventcb(int i, EventCb eventCb) {
        Map<EventCb, Integer> map = this.f1633b;
        if (map != null) {
            map.put(eventCb, Integer.valueOf(i));
        }
    }

    public String getIp() {
        return this.e;
    }

    public int getPort() {
        return this.g;
    }

    public ConnType getConnType() {
        return this.j;
    }

    public String getHost() {
        return this.f1634c;
    }

    public String getRealHost() {
        return this.f1635d;
    }

    public IConnStrategy getConnStrategy() {
        return this.k;
    }

    public String getUnit() {
        return this.l;
    }

    public void handleCallbacks(int i, anet.channel.entity.b bVar) {
        v.submit(new b(this, i, bVar));
    }

    public synchronized void notifyStatus(int i, anet.channel.entity.b bVar) {
        ALog.e("awcn.Session", "notifyStatus", this.p, "status", a.a(i));
        if (i == this.n) {
            ALog.i("awcn.Session", "ignore notifyStatus", this.p, new Object[0]);
            return;
        }
        this.n = i;
        switch (this.n) {
            case 0:
                handleCallbacks(1, bVar);
                break;
            case 2:
                handleCallbacks(256, bVar);
                break;
            case 4:
                this.l = StrategyCenter.getInstance().getUnitByHost(this.f1635d);
                handleCallbacks(512, bVar);
                break;
            case 5:
                handleCallbacks(1024, bVar);
                break;
            case 6:
                onDisconnect();
                if (!this.w) {
                    handleCallbacks(2, bVar);
                }
                break;
        }
    }

    public void setPingTimeout(int i) {
        if (this.o == null) {
            this.o = getRecvTimeOutRunnable();
        }
        a();
        Runnable runnable = this.o;
        if (runnable != null) {
            this.x = ThreadPoolExecutorFactory.submitScheduledTask(runnable, i, TimeUnit.MILLISECONDS);
        }
    }

    protected void a() {
        Future<?> future;
        if (this.o == null || (future = this.x) == null) {
            return;
        }
        future.cancel(true);
    }

    public String toString() {
        return "Session@[" + this.p + '|' + this.j + ']';
    }

    public void handleResponseCode(Request request, int i) {
        if (request.getHeaders().containsKey(HttpConstant.X_PV) && i >= 500 && i < 600) {
            synchronized (this) {
                if (this.y == null) {
                    this.y = new LinkedList();
                }
                if (this.y.size() < 5) {
                    this.y.add(Long.valueOf(System.currentTimeMillis()));
                } else {
                    long jLongValue = this.y.remove(0).longValue();
                    long jCurrentTimeMillis = System.currentTimeMillis();
                    if (jCurrentTimeMillis - jLongValue <= 60000) {
                        StrategyCenter.getInstance().forceRefreshStrategy(request.getHost());
                        this.y.clear();
                    } else {
                        this.y.add(Long.valueOf(jCurrentTimeMillis));
                    }
                }
            }
        }
    }

    public void handleResponseHeaders(Request request, Map<String, List<String>> map) {
        try {
            if (map.containsKey(HttpConstant.X_SWITCH_UNIT)) {
                String singleHeaderFieldByKey = HttpHelper.getSingleHeaderFieldByKey(map, HttpConstant.X_SWITCH_UNIT);
                if (TextUtils.isEmpty(singleHeaderFieldByKey)) {
                    singleHeaderFieldByKey = null;
                }
                if (StringUtils.isStringEqual(this.l, singleHeaderFieldByKey)) {
                    return;
                }
                long jCurrentTimeMillis = System.currentTimeMillis();
                if (jCurrentTimeMillis - this.z > 60000) {
                    StrategyCenter.getInstance().forceRefreshStrategy(request.getHost());
                    this.z = jCurrentTimeMillis;
                }
            }
        } catch (Exception unused) {
        }
    }
}
