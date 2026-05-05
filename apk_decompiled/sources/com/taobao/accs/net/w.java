package com.taobao.accs.net;

import android.content.Context;
import android.text.TextUtils;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.strategy.IConnStrategy;
import com.alibaba.sdk.android.error.ErrorCode;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.huawei.hms.support.hianalytics.HiAnalyticsConstant;
import com.taobao.accs.AccsClientConfig;
import com.taobao.accs.AccsErrorCode;
import com.taobao.accs.common.Constants;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import com.taobao.accs.data.Message;
import com.taobao.accs.ut.monitor.SessionMonitor;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.AppMonitorAdapter;
import com.taobao.accs.utl.BaseMonitor;
import com.taobao.accs.utl.OrangeAdapter;
import com.taobao.accs.utl.UTMini;
import com.taobao.accs.utl.UtilityImpl;
import com.taobao.accs.utl.Utils;
import io.netty.handler.codec.rtsp.RtspHeaders;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.android.spdy.RequestPriority;
import org.android.spdy.SessionCb;
import org.android.spdy.SessionInfo;
import org.android.spdy.SpdyAgent;
import org.android.spdy.SpdyByteArray;
import org.android.spdy.SpdyDataProvider;
import org.android.spdy.SpdyRequest;
import org.android.spdy.SpdySession;
import org.android.spdy.SpdySessionKind;
import org.android.spdy.SpdyVersion;
import org.android.spdy.Spdycb;
import org.android.spdy.SuperviseConnectInfo;
import org.android.spdy.SuperviseData;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class w extends b implements SessionCb, Spdycb {
    private Object A;
    private long B;
    private long C;
    private long D;
    private long E;
    private int F;
    private String G;
    private SessionMonitor H;
    private com.taobao.accs.ut.a.c I;
    private boolean J;
    private String K;
    private boolean L;
    private g M;
    private String N;
    protected ScheduledFuture<?> n;
    protected String o;
    protected int p;
    protected String q;
    protected int r;
    private int s;
    private LinkedList<Message> t;
    private a u;
    private boolean v;
    private String w;
    private String x;
    private SpdyAgent y;
    private SpdySession z;

    @Override // com.taobao.accs.net.b
    protected boolean h() {
        return false;
    }

    @Override // com.taobao.accs.net.b
    public int m() {
        return 0;
    }

    public w(Context context, int i, String str) {
        super(context, i, str);
        this.s = 3;
        this.t = new LinkedList<>();
        this.v = true;
        this.y = null;
        this.z = null;
        this.A = new Object();
        this.F = -1;
        this.G = null;
        this.J = false;
        this.K = "";
        this.L = false;
        this.M = new g(r());
        w();
    }

    @Override // com.taobao.accs.net.b
    public void a() {
        this.v = true;
        ALog.d(d(), "start", new Object[0]);
        a(this.f6366d);
        if (this.u == null) {
            ALog.i(d(), "start thread", new Object[0]);
            this.u = new a("NetworkThread_" + this.m);
            this.u.setPriority(2);
            this.u.start();
        }
        a(false, false);
    }

    @Override // com.taobao.accs.net.b
    protected void a(Message message, boolean z) {
        if (!this.v || message == null) {
            ALog.e(d(), "not running or msg null! " + this.v, new Object[0]);
            return;
        }
        try {
            if (ThreadPoolExecutorFactory.getScheduledExecutor().getQueue().size() > 1000) {
                throw new RejectedExecutionException("accs");
            }
            ScheduledFuture<?> scheduledFutureSchedule = ThreadPoolExecutorFactory.getScheduledExecutor().schedule(new x(this, message, z), message.Q, TimeUnit.MILLISECONDS);
            if (message.a() == 1 && message.O != null) {
                if (message.c()) {
                    a(message.O);
                }
                this.e.f6312a.put(message.O, scheduledFutureSchedule);
            }
            if (message.e() != null) {
                message.e().setDeviceId(UtilityImpl.getDeviceId(this.f6366d));
                message.e().setConnType(this.f6365c);
                message.e().onEnterQueueData();
            }
        } catch (RejectedExecutionException unused) {
            int size = ThreadPoolExecutorFactory.getScheduledExecutor().getQueue().size();
            this.e.a(message, AccsErrorCode.MESSAGE_QUEUE_FULL.copy().detail("channel " + size).build());
            ALog.e(d(), "send queue full count:" + size, new Object[0]);
        } catch (Throwable th) {
            ALog.e(d(), "send error", th, new Object[0]);
            this.e.a(message, AccsErrorCode.SEND_LOCAL_EXCEPTION.copy().detail(AccsErrorCode.getExceptionInfo(th)).build());
        }
    }

    @Override // com.taobao.accs.net.b
    public void e() {
        super.e();
        this.v = false;
        ThreadPoolExecutorFactory.getScheduledExecutor().execute(new y(this));
        ALog.e(d(), "shut down", new Object[0]);
    }

    @Override // com.taobao.accs.net.b
    public void a(boolean z, boolean z2) {
        ALog.d(d(), "try ping, force:" + z, new Object[0]);
        if (this.f6365c == 1) {
            ALog.d(d(), "INAPP, skip", new Object[0]);
        } else {
            b(Message.a(z, (int) (z2 ? Math.random() * 10.0d * 1000.0d : 0.0d)), z);
        }
    }

    public void q() {
        ALog.e(d(), " force close!", new Object[0]);
        try {
            this.z.closeSession();
            this.H.setCloseType(1);
        } catch (Exception unused) {
        }
        c(3);
    }

    @Override // com.taobao.accs.net.b
    public com.taobao.accs.ut.a.c c() {
        if (this.I == null) {
            this.I = new com.taobao.accs.ut.a.c();
        }
        this.I.f6427b = this.f6365c;
        this.I.f6429d = this.t.size();
        this.I.i = UtilityImpl.g(this.f6366d);
        com.taobao.accs.ut.a.c cVar = this.I;
        cVar.f = this.K;
        cVar.f6426a = this.s;
        SessionMonitor sessionMonitor = this.H;
        cVar.f6428c = sessionMonitor != null && sessionMonitor.getRet();
        this.I.j = s();
        this.I.e = this.e != null ? this.e.d() : 0;
        com.taobao.accs.ut.a.c cVar2 = this.I;
        cVar2.g = this.x;
        return cVar2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Message message) {
        if (message.t == null || this.t.size() == 0) {
            return;
        }
        for (int size = this.t.size() - 1; size >= 0; size--) {
            Message message2 = this.t.get(size);
            if (message2 != null && message2.t != null && message2.f().equals(message.f())) {
                switch (message.t.intValue()) {
                    case 1:
                    case 2:
                        if (message2.t.intValue() == 1 || message2.t.intValue() == 2) {
                            this.t.remove(size);
                        }
                        break;
                    case 3:
                    case 4:
                        if (message2.t.intValue() == 3 || message2.t.intValue() == 4) {
                            this.t.remove(size);
                        }
                        break;
                    case 5:
                    case 6:
                        if (message2.t.intValue() == 5 || message2.t.intValue() == 6) {
                            this.t.remove(size);
                        }
                        break;
                }
                ALog.d(d(), "clearRepeatControlCommand message:" + message2.t + "/" + message2.f(), new Object[0]);
            }
        }
        if (this.e != null) {
            this.e.b(message);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d(String str) {
        SessionInfo sessionInfo;
        int i = this.s;
        if (i == 2 || i == 1) {
            return;
        }
        if (this.M == null) {
            this.M = new g(r());
        }
        List<IConnStrategy> listA = this.M.a(r());
        int port = Constants.PORT;
        if (listA != null && listA.size() > 0) {
            for (IConnStrategy iConnStrategy : listA) {
                if (iConnStrategy != null) {
                    ALog.i(d(), BaseMonitor.ALARM_POINT_CONNECT, "ip", iConnStrategy.getIp(), RtspHeaders.Values.PORT, Integer.valueOf(iConnStrategy.getPort()));
                }
            }
            if (this.L) {
                this.M.b();
                this.L = false;
            }
            IConnStrategy iConnStrategyA = this.M.a();
            this.o = iConnStrategyA == null ? r() : iConnStrategyA.getIp();
            if (iConnStrategyA != null) {
                port = iConnStrategyA.getPort();
            }
            this.p = port;
            AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_POINT_DNS, "httpdns", 0.0d);
            ALog.i(d(), "connect from amdc succ", "ip", this.o, RtspHeaders.Values.PORT, Integer.valueOf(this.p), "originPos", Integer.valueOf(this.M.c()));
        } else {
            if (str != null) {
                this.o = str;
            } else {
                this.o = r();
            }
            if (System.currentTimeMillis() % 2 == 0) {
                port = 80;
            }
            this.p = port;
            AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_POINT_DNS, "localdns", 0.0d);
            ALog.i(d(), "connect get ip from amdc fail!!", new Object[0]);
        }
        if (Utils.isIPV6Address(this.o)) {
            this.w = "https://[" + this.o + "]:" + this.p + "/accs/";
        } else {
            this.w = "https://" + this.o + ":" + this.p + "/accs/";
        }
        ALog.i(d(), BaseMonitor.ALARM_POINT_CONNECT, config.Constants.URL, this.w);
        this.N = String.valueOf(System.currentTimeMillis());
        if (this.H != null) {
            AppMonitor.getInstance().commitStat(this.H);
        }
        this.H = new SessionMonitor();
        this.H.setConnectType(this.f6365c == 0 ? "service" : "inapp");
        if (this.y != null) {
            try {
                this.D = System.currentTimeMillis();
                this.E = System.nanoTime();
                this.q = UtilityImpl.a(this.f6366d);
                this.r = UtilityImpl.b(this.f6366d);
                this.B = System.currentTimeMillis();
                this.H.onStartConnect();
                c(2);
                synchronized (this.A) {
                    try {
                        try {
                            if (!TextUtils.isEmpty(this.q) && this.r >= 0 && this.J) {
                                ALog.i(d(), BaseMonitor.ALARM_POINT_CONNECT, "proxy", this.q, RtspHeaders.Values.PORT, Integer.valueOf(this.r));
                                sessionInfo = new SessionInfo(this.o, this.p, r() + OpenAccountUIConstants.UNDER_LINE + this.f6364b, this.q, this.r, this.N, this, 4226);
                                this.K = this.q + ":" + this.r;
                            } else {
                                ALog.i(d(), "connect normal", new Object[0]);
                                sessionInfo = new SessionInfo(this.o, this.p, r() + OpenAccountUIConstants.UNDER_LINE + this.f6364b, null, 0, this.N, this, 4226);
                                this.K = "";
                            }
                            sessionInfo.setPubKeySeqNum(t());
                            sessionInfo.setConnectionTimeoutMs(b.ACCS_RECEIVE_TIMEOUT);
                            this.z = this.y.createSession(sessionInfo);
                            this.H.connection_stop_date = 0L;
                            this.A.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        this.J = false;
                    }
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
    }

    private int t() {
        boolean zK = k();
        if (AccsClientConfig.mEnv == 2) {
            return 0;
        }
        int channelPubKey = this.i.getChannelPubKey();
        if (channelPubKey <= 0) {
            return zK ? 4 : 3;
        }
        ALog.i(d(), "getPublicKeyType use custom pub key", "pubKey", Integer.valueOf(channelPubKey));
        return channelPubKey;
    }

    private void u() {
        if (this.z == null) {
            c(3);
            return;
        }
        try {
            String strEncode = URLEncoder.encode(UtilityImpl.getDeviceId(this.f6366d));
            String strA = UtilityImpl.a(this.f6366d, i(), this.i.getAppSecret(), UtilityImpl.getDeviceId(this.f6366d), this.m);
            String strC = c(this.w);
            ALog.i(d(), BaseMonitor.ALARM_POINT_AUTH, "url", strC);
            this.x = strC;
            if (!a(strEncode, i(), strA)) {
                ErrorCode errorCodeBuild = AccsErrorCode.SPDY_AUTH_PARAM_ERROR.copy().detail("device " + strEncode + " key " + i() + " sign " + strA).build();
                ALog.e(d(), "auth param error!", "code", errorCodeBuild);
                a(errorCodeBuild);
                return;
            }
            new URL(strC);
            SpdyRequest spdyRequest = new SpdyRequest(new URL(strC), "GET", RequestPriority.DEFAULT_PRIORITY, 80000, b.ACCS_RECEIVE_TIMEOUT);
            spdyRequest.setDomain(r());
            this.z.submitRequest(spdyRequest, new SpdyDataProvider((byte[]) null), r(), this);
        } catch (Throwable th) {
            ALog.e(d(), "auth exception ", th, new Object[0]);
            a(AccsErrorCode.SPDY_AUTH_EXCEPTION.copy().detail(AccsErrorCode.getExceptionInfo(th)).build());
        }
    }

    private boolean a(String str, String str2, String str3) {
        if (Utils.getMode(this.f6366d) == 2) {
            return true;
        }
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str3)) {
            return true;
        }
        int i = 3;
        c(3);
        if (TextUtils.isEmpty(str)) {
            i = 1;
        } else if (TextUtils.isEmpty(str2)) {
            i = 2;
        } else if (!TextUtils.isEmpty(str3)) {
            i = 1;
        }
        this.H.setFailReason(i);
        this.H.onConnectStop();
        String str4 = this.f6365c == 0 ? "service" : "inapp";
        a aVar = this.u;
        int i2 = aVar != null ? aVar.f6408a : 0;
        UTMini.getInstance().commitEvent(66001, "DISCONNECT " + str4, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(Constants.SDK_VERSION_CODE), this.x, this.K);
        AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_CONNECT, "retrytimes:" + i2, i + "", "");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void v() {
        if (this.f6365c == 1) {
            return;
        }
        this.B = System.currentTimeMillis();
        this.C = System.nanoTime();
        f.a(this.f6366d).a();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private synchronized void c(int i) {
        ALog.i(d(), "notifyStatus start", "status", a(i));
        if (i == this.s) {
            ALog.d(d(), "ignore notifyStatus", new Object[0]);
            return;
        }
        this.s = i;
        switch (i) {
            case 1:
                f.a(this.f6366d).f();
                v();
                if (this.n != null) {
                    this.n.cancel(true);
                }
                synchronized (this.A) {
                    try {
                        this.A.notifyAll();
                        break;
                    } catch (Exception unused) {
                    }
                    break;
                }
                synchronized (this.t) {
                    try {
                        this.t.notifyAll();
                        break;
                    } catch (Exception unused2) {
                    }
                    break;
                }
                ALog.i(d(), "notifyStatus end", "status", a(i));
                return;
            case 2:
                if (this.n != null) {
                    this.n.cancel(true);
                }
                ThreadPoolExecutorFactory.getScheduledExecutor().schedule(new z(this, this.N), 120000L, TimeUnit.MILLISECONDS);
                ALog.i(d(), "notifyStatus end", "status", a(i));
                return;
            case 3:
                ALog.w(d(), "notifyStatus", "status", a(i));
                v();
                f.a(this.f6366d).d();
                synchronized (this.A) {
                    try {
                        this.A.notifyAll();
                        break;
                    } catch (Exception unused3) {
                    }
                    break;
                }
                this.e.a(AccsErrorCode.SPDY_CON_DISCONNECTED.copy().detail(com.taobao.accs.utl.g.a().b()).build());
                a(false, true);
                ALog.i(d(), "notifyStatus end", "status", a(i));
                return;
            case 4:
                ALog.i(d(), "notifyStatus end", "status", a(i));
                return;
            default:
                ALog.i(d(), "notifyStatus end", "status", a(i));
                return;
        }
    }

    public String r() {
        String channelHost = this.i.getChannelHost();
        ALog.i(d(), "getChannelHost", "host", channelHost);
        return channelHost == null ? "" : channelHost;
    }

    private void w() {
        try {
            SpdyAgent.enableDebug = true;
            this.y = SpdyAgent.getInstance(this.f6366d, SpdyVersion.SPDY3, SpdySessionKind.NONE_SESSION);
            if (SpdyAgent.checkLoadSucc()) {
                com.taobao.accs.utl.d.a();
                if (!k()) {
                    this.y.setAccsSslCallback(new aa(this));
                }
                if (OrangeAdapter.isTnetLogOff(false)) {
                    return;
                }
                String str = this.f6365c == 0 ? "service" : "inapp";
                ALog.d(d(), "into--[setTnetLogPath]", new Object[0]);
                String strD = UtilityImpl.d(this.f6366d, str);
                ALog.d(d(), "config tnet log path:" + strD, new Object[0]);
                if (TextUtils.isEmpty(strD)) {
                    return;
                }
                this.y.configLogFile(strD, 5242880, 5);
                return;
            }
            ALog.e(d(), "initClient", new Object[0]);
            com.taobao.accs.utl.d.b();
        } catch (Throwable th) {
            ALog.e(d(), "initClient", th, new Object[0]);
        }
    }

    /* JADX INFO: compiled from: Taobao */
    private class a extends Thread {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public int f6408a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        long f6409b;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private final String f6411d;

        public a(String str) {
            super(str);
            this.f6411d = getName();
            this.f6408a = 0;
        }

        private void a(boolean z) {
            if (w.this.s == 1) {
                if (w.this.s != 1 || System.currentTimeMillis() - this.f6409b <= 5000) {
                    return;
                }
                this.f6408a = 0;
                return;
            }
            ALog.d(w.this.d(), "tryConnect", "force", Boolean.valueOf(z));
            if (z) {
                this.f6408a = 0;
            }
            ALog.i(this.f6411d, "tryConnect", "force", Boolean.valueOf(z), "failTimes", Integer.valueOf(this.f6408a));
            if (w.this.s == 1 || this.f6408a < 4) {
                if (w.this.s != 1) {
                    if (w.this.f6365c == 1 && this.f6408a == 0) {
                        ALog.i(this.f6411d, "tryConnect in app, no sleep", new Object[0]);
                    } else {
                        ALog.i(this.f6411d, "tryConnect, need sleep", new Object[0]);
                        try {
                            sleep(5000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    w.this.K = "";
                    if (this.f6408a == 3) {
                        w.this.M.b(w.this.r());
                    }
                    w.this.d((String) null);
                    w.this.H.setRetryTimes(this.f6408a);
                    if (w.this.s != 1) {
                        this.f6408a++;
                        ALog.e(this.f6411d, "try connect fail, ready for reconnect", new Object[0]);
                        a(false);
                        return;
                    }
                    this.f6409b = System.currentTimeMillis();
                    return;
                }
                return;
            }
            w.this.J = true;
            ALog.e(this.f6411d, "tryConnect fail", "maxTimes", 4);
        }

        /* JADX WARN: Removed duplicated region for block: B:173:0x0475 A[Catch: Throwable -> 0x050f, TRY_ENTER, TryCatch #13 {Throwable -> 0x050f, blocks: (B:173:0x0475, B:175:0x0482, B:176:0x048d, B:177:0x0493, B:196:0x04ef, B:197:0x04f0, B:198:0x04ff, B:205:0x050e, B:199:0x0500, B:200:0x0509, B:178:0x0494, B:180:0x04a1, B:182:0x04af, B:184:0x04b3, B:186:0x04bb, B:188:0x04c3, B:189:0x04d5, B:190:0x04d8, B:191:0x04ea), top: B:234:0x0473, inners: #6, #8 }] */
        /* JADX WARN: Removed duplicated region for block: B:18:0x0064 A[Catch: all -> 0x051b, TryCatch #16 {, blocks: (B:7:0x0027, B:9:0x0033, B:16:0x004e, B:18:0x0064, B:20:0x0076, B:21:0x007d, B:13:0x0048, B:14:0x004b), top: B:237:0x0027, inners: #2 }] */
        /* JADX WARN: Removed duplicated region for block: B:197:0x04f0 A[Catch: Throwable -> 0x050f, TryCatch #13 {Throwable -> 0x050f, blocks: (B:173:0x0475, B:175:0x0482, B:176:0x048d, B:177:0x0493, B:196:0x04ef, B:197:0x04f0, B:198:0x04ff, B:205:0x050e, B:199:0x0500, B:200:0x0509, B:178:0x0494, B:180:0x04a1, B:182:0x04af, B:184:0x04b3, B:186:0x04bb, B:188:0x04c3, B:189:0x04d5, B:190:0x04d8, B:191:0x04ea), top: B:234:0x0473, inners: #6, #8 }] */
        /* JADX WARN: Removed duplicated region for block: B:227:0x02ec A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:255:0x0368 A[SYNTHETIC] */
        @Override // java.lang.Thread, java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void run() throws java.lang.Throwable {
            /*
                Method dump skipped, instruction units count: 1317
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.net.w.a.run():void");
        }
    }

    public boolean s() {
        return this.v;
    }

    @Override // org.android.spdy.SessionCb
    public void spdySessionFailedError(SpdySession spdySession, int i, Object obj) {
        if (spdySession != null) {
            try {
                spdySession.cleanUp();
            } catch (Exception e) {
                ALog.e(d(), "session cleanUp has exception: " + e, new Object[0]);
            }
        }
        a aVar = this.u;
        int i2 = aVar != null ? aVar.f6408a : 0;
        ALog.e(d(), "spdySessionFailedError", "retryTimes", Integer.valueOf(i2), "errorId", Integer.valueOf(i));
        this.J = false;
        this.L = true;
        c(3);
        this.H.setFailReason(i);
        this.H.onConnectStop();
        String str = this.f6365c == 0 ? "service" : "inapp";
        UTMini.getInstance().commitEvent(66001, "DISCONNECT " + str, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(Constants.SDK_VERSION_CODE), this.x, this.K);
        AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_CONNECT, "retrytimes:" + i2, i + "", "");
    }

    @Override // org.android.spdy.SessionCb
    public void spdySessionConnectCB(SpdySession spdySession, SuperviseConnectInfo superviseConnectInfo) {
        this.F = superviseConnectInfo.connectTime;
        int i = superviseConnectInfo.handshakeTime;
        ALog.i(d(), "spdySessionConnectCB", "sessionConnectInterval", Integer.valueOf(this.F), "sslTime", Integer.valueOf(i), "reuse", Integer.valueOf(superviseConnectInfo.sessionTicketReused));
        u();
        this.H.setRet(true);
        this.H.onConnectStop();
        SessionMonitor sessionMonitor = this.H;
        sessionMonitor.tcp_time = this.F;
        sessionMonitor.ssl_time = i;
        String str = this.f6365c == 0 ? "service" : "inapp";
        UTMini.getInstance().commitEvent(66001, "CONNECTED " + str + " " + superviseConnectInfo.sessionTicketReused, String.valueOf(this.F), String.valueOf(i), Integer.valueOf(Constants.SDK_VERSION_CODE), String.valueOf(superviseConnectInfo.sessionTicketReused), this.x, this.K);
        AppMonitorAdapter.commitAlarmSuccess("accs", BaseMonitor.ALARM_POINT_CONNECT, "");
    }

    @Override // org.android.spdy.SessionCb
    public void spdySessionCloseCallback(SpdySession spdySession, Object obj, SuperviseConnectInfo superviseConnectInfo, int i) {
        ALog.e(d(), "spdySessionCloseCallback", "errorCode", Integer.valueOf(i));
        if (spdySession != null) {
            try {
                spdySession.cleanUp();
            } catch (Exception e) {
                ALog.e(d(), "session cleanUp has exception: " + e, new Object[0]);
            }
        }
        c(3);
        this.H.onCloseConnect();
        if (this.H.getConCloseDate() > 0 && this.H.getConStopDate() > 0) {
            this.H.getConCloseDate();
            this.H.getConStopDate();
        }
        this.H.setCloseReason(this.H.getCloseReason() + "tnet error:" + i);
        if (superviseConnectInfo != null) {
            this.H.live_time = superviseConnectInfo.keepalive_period_second;
        }
        AppMonitor.getInstance().commitStat(this.H);
        for (Message message : this.e.e()) {
            if (message.e() != null) {
                message.e().setFailReason("session close");
                AppMonitor.getInstance().commitStat(message.e());
            }
        }
        String str = this.f6365c == 0 ? "service" : "inapp";
        ALog.d(d(), "spdySessionCloseCallback, conKeepTime:" + this.H.live_time + " connectType:" + str, new Object[0]);
        UTMini uTMini = UTMini.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append("DISCONNECT CLOSE ");
        sb.append(str);
        uTMini.commitEvent(66001, sb.toString(), Integer.valueOf(i), Long.valueOf(this.H.live_time), Integer.valueOf(Constants.SDK_VERSION_CODE), this.x, this.K);
    }

    @Override // org.android.spdy.SessionCb
    public void spdyPingRecvCallback(SpdySession spdySession, long j, Object obj) {
        ALog.d(d(), "spdyPingRecvCallback uniId:" + j, new Object[0]);
        if (j < 0) {
            return;
        }
        this.e.b();
        f.a(this.f6366d).e();
        f.a(this.f6366d).a();
        this.H.onPingCBReceive();
        if (this.H.ping_rec_times % 2 == 0) {
            UtilityImpl.a(this.f6366d, Constants.SP_KEY_SERVICE_END, System.currentTimeMillis());
        }
    }

    @Override // org.android.spdy.SessionCb
    public void spdyCustomControlFrameRecvCallback(SpdySession spdySession, Object obj, int i, int i2, int i3, int i4, byte[] bArr) {
        v();
        ALog.i(d(), "onFrame", "type", Integer.valueOf(i2), "len", Integer.valueOf(bArr.length));
        StringBuilder sb = new StringBuilder();
        if (ALog.isPrintLog(ALog.Level.D) && bArr.length < 512) {
            long jCurrentTimeMillis = System.currentTimeMillis();
            for (byte b2 : bArr) {
                sb.append(Integer.toHexString(b2 & 255));
                sb.append(" ");
            }
            ALog.d(d(), ((Object) sb) + " log time:" + (System.currentTimeMillis() - jCurrentTimeMillis), new Object[0]);
        }
        if (i2 == 200) {
            try {
                long jCurrentTimeMillis2 = System.currentTimeMillis();
                this.e.a(bArr);
                com.taobao.accs.ut.a.d dVarG = this.e.g();
                if (dVarG != null) {
                    dVarG.f6432c = String.valueOf(jCurrentTimeMillis2);
                    dVarG.g = this.f6365c == 0 ? "service" : "inapp";
                    dVarG.a();
                }
            } catch (Throwable th) {
                ALog.e(d(), "onDataReceive ", th, new Object[0]);
                UTMini.getInstance().commitEvent(66001, "SERVICE_DATA_RECEIVE", UtilityImpl.a(th));
            }
            ALog.d(d(), "try handle msg", new Object[0]);
            g();
        } else {
            ALog.e(d(), "drop frame", "len", Integer.valueOf(bArr.length));
        }
        ALog.d(d(), "spdyCustomControlFrameRecvCallback", new Object[0]);
    }

    @Override // org.android.spdy.Spdycb
    public void spdyStreamCloseCallback(SpdySession spdySession, long j, int i, Object obj, SuperviseData superviseData) {
        ALog.d(d(), "spdyStreamCloseCallback", new Object[0]);
        if (i != 0) {
            ALog.e(d(), "spdyStreamCloseCallback", HiAnalyticsConstant.HaKey.BI_KEY_RESULT, Integer.valueOf(i));
            a(AccsErrorCode.NETWORKSDK_SPDY_CLOSE_ERROR.copy().detail("channel code " + i).build());
        }
    }

    @Override // org.android.spdy.Spdycb
    public void spdyRequestRecvCallback(SpdySession spdySession, long j, Object obj) {
        ALog.d(d(), "spdyRequestRecvCallback", new Object[0]);
    }

    @Override // org.android.spdy.Spdycb
    public void spdyOnStreamResponse(SpdySession spdySession, long j, Map<String, List<String>> map, Object obj) {
        this.B = System.currentTimeMillis();
        this.C = System.nanoTime();
        try {
            Map<String, String> mapA = UtilityImpl.a(map);
            ALog.d("SilenceConn_", "spdyOnStreamResponse", "header", map);
            int i = Integer.parseInt(mapA.get(":status"));
            if (i == 200) {
                ALog.i(d(), "spdyOnStreamResponse", "httpStatusCode", Integer.valueOf(i));
                c(1);
                String str = mapA.get("x-at");
                if (!TextUtils.isEmpty(str)) {
                    this.k = str;
                }
                this.H.auth_time = this.H.connection_stop_date > 0 ? System.currentTimeMillis() - this.H.connection_stop_date : 0L;
                String str2 = this.f6365c == 0 ? "service" : "inapp";
                UTMini.getInstance().commitEvent(66001, "CONNECTED 200 " + str2, this.x, this.K, Integer.valueOf(Constants.SDK_VERSION_CODE), "0");
                AppMonitorAdapter.commitAlarmSuccess("accs", BaseMonitor.ALARM_POINT_AUTH, "");
            } else {
                ALog.e(d(), "spdyOnStreamResponse", "httpStatusCode", Integer.valueOf(i));
                a(AccsErrorCode.NETWORKSDK_SPDY_RES_ERROR.copy().detail("channel code " + i).build());
            }
        } catch (Exception e) {
            ALog.e(d(), e.toString(), new Object[0]);
            q();
            this.H.setCloseReason("exception");
        }
        ALog.d(d(), "spdyOnStreamResponse", new Object[0]);
    }

    private void a(ErrorCode errorCode) {
        this.k = null;
        q();
        a aVar = this.u;
        int i = aVar != null ? aVar.f6408a : 0;
        this.H.setCloseReason("code not 200 is" + errorCode.getCodeInt());
        this.L = true;
        String str = this.f6365c == 0 ? "service" : "inapp";
        UTMini.getInstance().commitEvent(66001, "CONNECTED NO 200 " + str, errorCode, Integer.valueOf(i), Integer.valueOf(Constants.SDK_VERSION_CODE), this.x, this.K);
        AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_AUTH, "", errorCode + "", "");
    }

    @Override // org.android.spdy.Spdycb
    public void spdyDataSendCallback(SpdySession spdySession, boolean z, long j, int i, Object obj) {
        ALog.d(d(), "spdyDataSendCallback", new Object[0]);
    }

    @Override // org.android.spdy.Spdycb
    public void spdyDataRecvCallback(SpdySession spdySession, boolean z, long j, int i, Object obj) {
        ALog.d(d(), "spdyDataRecvCallback", new Object[0]);
    }

    @Override // com.taobao.accs.net.b
    public void b() {
        this.J = false;
        this.f = 0;
    }

    @Override // org.android.spdy.SessionCb
    public void bioPingRecvCallback(SpdySession spdySession, int i) {
        ALog.w(d(), "bioPingRecvCallback uniId:" + i, new Object[0]);
    }

    @Override // com.taobao.accs.net.b
    protected void a(String str, boolean z, String str2) {
        try {
            c(4);
            q();
            this.H.setCloseReason(str2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.taobao.accs.net.b
    public boolean a(String str) {
        boolean z;
        synchronized (this.t) {
            z = true;
            int size = this.t.size() - 1;
            while (true) {
                if (size >= 0) {
                    Message message = this.t.get(size);
                    if (message != null && message.a() == 1 && message.O != null && message.O.equals(str)) {
                        this.t.remove(size);
                        break;
                    }
                    size--;
                } else {
                    z = false;
                    break;
                }
            }
        }
        return z;
    }

    @Override // org.android.spdy.SessionCb
    public byte[] getSSLMeta(SpdySession spdySession) {
        return UtilityImpl.a(this.f6366d, this.m, this.f6364b, spdySession.getDomain());
    }

    @Override // org.android.spdy.SessionCb
    public int putSSLMeta(SpdySession spdySession, byte[] bArr) {
        return UtilityImpl.a(this.f6366d, this.m, this.f6364b, spdySession.getDomain(), bArr);
    }

    @Override // org.android.spdy.Spdycb
    public void spdyDataChunkRecvCB(SpdySession spdySession, boolean z, long j, SpdyByteArray spdyByteArray, Object obj) {
        ALog.d(d(), "spdyDataChunkRecvCB", new Object[0]);
    }

    @Override // com.taobao.accs.net.b
    protected String d() {
        return "SilenceConn_" + this.m;
    }

    @Override // org.android.spdy.SessionCb
    public void spdyCustomControlFrameFailCallback(SpdySession spdySession, Object obj, int i, int i2) {
        b(i);
    }

    @Override // com.taobao.accs.net.b
    protected void a(Context context) {
        if (this.g) {
            return;
        }
        super.a(context);
        GlobalAppRuntimeInfo.setBackground(false);
        this.g = true;
        ALog.i(d(), "init awcn success!", new Object[0]);
    }

    @Override // com.taobao.accs.net.b
    public String b(String str) {
        return "https://" + this.i.getChannelHost();
    }

    @Override // com.taobao.accs.net.b
    public boolean l() {
        return this.s == 1;
    }

    @Override // com.taobao.accs.net.b
    public void n() {
        q();
    }

    @Override // com.taobao.accs.net.b
    public void o() {
        a(true, false);
    }
}
