package anet.channel.session;

import android.content.Context;
import android.text.TextUtils;
import anet.channel.AwcnConfig;
import anet.channel.Config;
import anet.channel.DataFrameCb;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.IAuth;
import anet.channel.RequestCb;
import anet.channel.Session;
import anet.channel.SessionInfo;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.bytes.ByteArray;
import anet.channel.bytes.a;
import anet.channel.heartbeat.HeartbeatManager;
import anet.channel.heartbeat.IHeartbeat;
import anet.channel.request.Cancelable;
import anet.channel.request.Request;
import anet.channel.security.ISecurity;
import anet.channel.statist.ExceptionStatistic;
import anet.channel.statist.RequestStatistic;
import anet.channel.statist.SessionMonitor;
import anet.channel.statist.SessionStatistic;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.ConnEvent;
import anet.channel.strategy.StrategyCenter;
import anet.channel.util.ALog;
import anet.channel.util.ErrorConstant;
import anet.channel.util.HttpConstant;
import anet.channel.util.HttpHelper;
import anet.channel.util.Utils;
import anetwork.channel.util.RequestConstant;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.linksdk.alcs.coap.resources.LinkFormat;
import com.huawei.hms.support.hianalytics.HiAnalyticsConstant;
import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.BaseMonitor;
import io.netty.handler.codec.rtsp.RtspHeaders;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.android.spdy.RequestPriority;
import org.android.spdy.SessionCb;
import org.android.spdy.SpdyAgent;
import org.android.spdy.SpdyByteArray;
import org.android.spdy.SpdyDataProvider;
import org.android.spdy.SpdyErrorException;
import org.android.spdy.SpdyRequest;
import org.android.spdy.SpdySession;
import org.android.spdy.SpdySessionKind;
import org.android.spdy.SpdyVersion;
import org.android.spdy.SuperviseConnectInfo;
import org.android.spdy.SuperviseData;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class TnetSpdySession extends Session implements SessionCb {
    protected long A;
    protected int B;
    protected DataFrameCb C;
    protected IHeartbeat D;
    protected IAuth E;
    protected String F;
    protected ISecurity G;
    private int H;
    private boolean I;
    protected SpdyAgent w;
    protected SpdySession x;
    protected volatile boolean y;
    protected long z;

    @Override // org.android.spdy.SessionCb
    public void bioPingRecvCallback(SpdySession spdySession, int i) {
    }

    static /* synthetic */ int e(TnetSpdySession tnetSpdySession) {
        int i = tnetSpdySession.H + 1;
        tnetSpdySession.H = i;
        return i;
    }

    public TnetSpdySession(Context context, anet.channel.entity.a aVar) {
        super(context, aVar);
        this.y = false;
        this.A = 0L;
        this.H = 0;
        this.B = -1;
        this.C = null;
        this.D = null;
        this.E = null;
        this.F = null;
        this.G = null;
        this.I = false;
    }

    public void initConfig(Config config2) {
        if (config2 != null) {
            this.F = config2.getAppkey();
            this.G = config2.getSecurity();
        }
    }

    public void initSessionInfo(SessionInfo sessionInfo) {
        if (sessionInfo != null) {
            this.C = sessionInfo.dataFrameCb;
            this.E = sessionInfo.auth;
            if (sessionInfo.isKeepAlive) {
                this.q.isKL = 1L;
                this.t = true;
                this.D = sessionInfo.heartbeat;
                this.I = sessionInfo.isAccs;
                if (this.D == null) {
                    if (sessionInfo.isAccs && !AwcnConfig.isAccsSessionCreateForbiddenInBg()) {
                        this.D = HeartbeatManager.getDefaultBackgroundAccsHeartbeat();
                    } else {
                        this.D = HeartbeatManager.getDefaultHeartbeat();
                    }
                }
            }
        }
        if (AwcnConfig.isIdleSessionCloseEnable() && this.D == null) {
            this.D = new anet.channel.heartbeat.c();
        }
    }

    public void setTnetPublicKey(int i) {
        this.B = i;
    }

    @Override // anet.channel.Session
    public Cancelable request(Request request, RequestCb requestCb) {
        SpdyRequest spdyRequest;
        anet.channel.request.c cVar = anet.channel.request.c.NULL;
        RequestStatistic requestStatistic = request != null ? request.f1794a : new RequestStatistic(this.f1635d, null);
        requestStatistic.setConnType(this.j);
        if (requestStatistic.start == 0) {
            long jCurrentTimeMillis = System.currentTimeMillis();
            requestStatistic.reqStart = jCurrentTimeMillis;
            requestStatistic.start = jCurrentTimeMillis;
        }
        requestStatistic.setIPAndPort(this.f, this.g);
        requestStatistic.ipRefer = this.k.getIpSource();
        requestStatistic.ipType = this.k.getIpType();
        requestStatistic.unit = this.l;
        if (request == null || requestCb == null) {
            if (requestCb != null) {
                requestCb.onFinish(-102, ErrorConstant.getErrMsg(-102), requestStatistic);
            }
            return cVar;
        }
        try {
            if (this.x != null && (this.n == 0 || this.n == 4)) {
                if (this.m) {
                    request.setDnsOptimize(this.e, this.g);
                }
                request.setUrlScheme(this.j.isSSL());
                URL url = request.getUrl();
                if (ALog.isPrintLog(2)) {
                    ALog.i("awcn.TnetSpdySession", "", request.getSeq(), "request URL", url.toString());
                    ALog.i("awcn.TnetSpdySession", "", request.getSeq(), "request Method", request.getMethod());
                    ALog.i("awcn.TnetSpdySession", "", request.getSeq(), "request headers", request.getHeaders());
                }
                if (TextUtils.isEmpty(this.h) || this.i <= 0) {
                    spdyRequest = new SpdyRequest(url, request.getMethod(), RequestPriority.DEFAULT_PRIORITY, -1, request.getConnectTimeout());
                } else {
                    spdyRequest = new SpdyRequest(url, url.getHost(), url.getPort(), this.h, this.i, request.getMethod(), RequestPriority.DEFAULT_PRIORITY, -1, request.getConnectTimeout(), 0);
                }
                spdyRequest.setRequestRdTimeoutMs(request.getReadTimeout());
                Map<String, String> headers = request.getHeaders();
                if (!headers.containsKey("Host")) {
                    spdyRequest.addHeaders(headers);
                    spdyRequest.addHeader(":host", this.m ? this.e : request.getHost());
                } else {
                    HashMap map = new HashMap(request.getHeaders());
                    String strRemove = map.remove("Host");
                    if (this.m) {
                        strRemove = this.e;
                    }
                    map.put(":host", strRemove);
                    spdyRequest.addHeaders(map);
                }
                SpdyDataProvider spdyDataProvider = new SpdyDataProvider(request.getBodyBytes());
                request.f1794a.sendStart = System.currentTimeMillis();
                request.f1794a.processTime = request.f1794a.sendStart - request.f1794a.start;
                int iSubmitRequest = this.x.submitRequest(spdyRequest, spdyDataProvider, this, new a(request, requestCb));
                if (ALog.isPrintLog(1)) {
                    ALog.d("awcn.TnetSpdySession", "", request.getSeq(), "streamId", Integer.valueOf(iSubmitRequest));
                }
                anet.channel.request.c cVar2 = new anet.channel.request.c(this.x, iSubmitRequest, request.getSeq());
                try {
                    this.q.requestCount++;
                    this.q.stdRCount++;
                    this.z = System.currentTimeMillis();
                    if (this.D != null) {
                        this.D.reSchedule();
                    }
                    return cVar2;
                } catch (SpdyErrorException e) {
                    e = e;
                    cVar = cVar2;
                    if (e.SpdyErrorGetCode() == -1104 || e.SpdyErrorGetCode() == -1103) {
                        ALog.e("awcn.TnetSpdySession", "Send request on closed session!!!", this.p, new Object[0]);
                        notifyStatus(6, new anet.channel.entity.b(2));
                    }
                    requestCb.onFinish(-300, ErrorConstant.formatMsg(-300, String.valueOf(e.SpdyErrorGetCode())), requestStatistic);
                    return cVar;
                } catch (Exception unused) {
                    cVar = cVar2;
                    requestCb.onFinish(-101, ErrorConstant.getErrMsg(-101), requestStatistic);
                    return cVar;
                }
            }
            requestCb.onFinish(-301, ErrorConstant.getErrMsg(-301), request.f1794a);
            return cVar;
        } catch (SpdyErrorException e2) {
            e = e2;
        } catch (Exception unused2) {
        }
    }

    @Override // anet.channel.Session
    public void sendCustomFrame(int i, byte[] bArr, int i2) {
        try {
            if (this.C == null) {
                return;
            }
            ALog.e("awcn.TnetSpdySession", "sendCustomFrame", this.p, Constants.KEY_DATA_ID, Integer.valueOf(i), "type", Integer.valueOf(i2));
            if (this.n == 4 && this.x != null) {
                if (bArr != null && bArr.length > 16384) {
                    a(i, -303, false, null);
                    return;
                }
                this.x.sendCustomControlFrame(i, i2, 0, bArr == null ? 0 : bArr.length, bArr);
                this.q.requestCount++;
                this.q.cfRCount++;
                this.z = System.currentTimeMillis();
                if (this.D != null) {
                    this.D.reSchedule();
                    return;
                }
                return;
            }
            ALog.e("awcn.TnetSpdySession", "sendCustomFrame", this.p, "sendCustomFrame con invalid mStatus:" + this.n);
            a(i, -301, true, "session invalid");
        } catch (SpdyErrorException e) {
            ALog.e("awcn.TnetSpdySession", "sendCustomFrame error", this.p, e, new Object[0]);
            a(i, -300, true, "SpdyErrorException: " + e.toString());
        } catch (Exception e2) {
            ALog.e("awcn.TnetSpdySession", "sendCustomFrame error", this.p, e2, new Object[0]);
            a(i, -101, true, e2.toString());
        }
    }

    private void a(int i, int i2, boolean z, String str) {
        DataFrameCb dataFrameCb = this.C;
        if (dataFrameCb != null) {
            dataFrameCb.onException(i, i2, z, str);
        }
    }

    @Override // anet.channel.Session
    public void connect() {
        int xquicCongControl;
        int i = 1;
        if (this.n == 1 || this.n == 0 || this.n == 4) {
            return;
        }
        try {
            if (this.w == null) {
                c();
            }
            if (anet.channel.util.c.a() && anet.channel.strategy.utils.c.a(this.e)) {
                try {
                    this.f = anet.channel.util.c.a(this.e);
                } catch (Exception unused) {
                }
            }
            String strValueOf = String.valueOf(System.currentTimeMillis());
            ALog.e("awcn.TnetSpdySession", BaseMonitor.ALARM_POINT_CONNECT, this.p, "host", this.f1634c, "ip", this.f, RtspHeaders.Values.PORT, Integer.valueOf(this.g), "sessionId", strValueOf, "SpdyProtocol,", this.j, "proxyIp,", this.h, "proxyPort,", Integer.valueOf(this.i));
            org.android.spdy.SessionInfo sessionInfo = new org.android.spdy.SessionInfo(this.f, this.g, this.f1634c + OpenAccountUIConstants.UNDER_LINE + this.F, this.h, this.i, strValueOf, this, this.j.getTnetConType());
            sessionInfo.setConnectionTimeoutMs((int) (((float) this.r) * Utils.getNetworkTimeFactor()));
            if (this.j.isPublicKeyAuto() || this.j.isH2S() || this.j.isHTTP3()) {
                sessionInfo.setCertHost(this.m ? this.e : this.f1635d);
            } else if (this.B >= 0) {
                sessionInfo.setPubKeySeqNum(this.B);
            } else {
                this.B = this.j.getTnetPublicKey(this.G != null ? this.G.isSecOff() : true);
                sessionInfo.setPubKeySeqNum(this.B);
            }
            if (this.j.isHTTP3() && (xquicCongControl = AwcnConfig.getXquicCongControl()) >= 0) {
                sessionInfo.setXquicCongControl(xquicCongControl);
            }
            this.x = this.w.createSession(sessionInfo);
            if (this.x.getRefCount() > 1) {
                ALog.e("awcn.TnetSpdySession", "get session ref count > 1!!!", this.p, new Object[0]);
                notifyStatus(0, new anet.channel.entity.b(1));
                b();
                return;
            }
            notifyStatus(1, null);
            this.z = System.currentTimeMillis();
            SessionStatistic sessionStatistic = this.q;
            if (TextUtils.isEmpty(this.h)) {
                i = 0;
            }
            sessionStatistic.isProxy = i;
            this.q.isTunnel = RequestConstant.FALSE;
            this.q.isBackground = GlobalAppRuntimeInfo.isAppBackground();
            this.A = 0L;
        } catch (Throwable th) {
            notifyStatus(2, null);
            ALog.e("awcn.TnetSpdySession", "connect exception ", this.p, th, new Object[0]);
        }
    }

    @Override // anet.channel.Session
    public void close() {
        ALog.e("awcn.TnetSpdySession", "force close!", this.p, UTConstants.E_SDK_CONNECT_SESSION_ACTION, this);
        notifyStatus(7, null);
        try {
            if (this.D != null) {
                this.D.stop();
                this.D = null;
            }
            if (this.x != null) {
                this.x.closeSession();
            }
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // anet.channel.Session
    public void onDisconnect() {
        this.y = false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // anet.channel.Session
    public Runnable getRecvTimeOutRunnable() {
        return new h(this);
    }

    @Override // anet.channel.Session
    public void ping(boolean z) {
        ping(z, this.s);
    }

    @Override // anet.channel.Session
    public void ping(boolean z, int i) {
        if (ALog.isPrintLog(1)) {
            ALog.d("awcn.TnetSpdySession", "ping", this.p, "host", this.f1634c, "thread", Thread.currentThread().getName());
        }
        if (z) {
            try {
                if (this.x != null) {
                    if (this.n == 0 || this.n == 4) {
                        handleCallbacks(64, null);
                        if (this.y) {
                            return;
                        }
                        this.y = true;
                        this.q.ppkgCount++;
                        this.x.submitPing();
                        if (ALog.isPrintLog(1)) {
                            ALog.d("awcn.TnetSpdySession", this.f1634c + " submit ping ms:" + (System.currentTimeMillis() - this.z) + " force:" + z, this.p, new Object[0]);
                        }
                        setPingTimeout(i);
                        this.z = System.currentTimeMillis();
                        if (this.D != null) {
                            this.D.reSchedule();
                            return;
                        }
                        return;
                    }
                    return;
                }
                if (this.q != null) {
                    this.q.closeReason = "session null";
                }
                ALog.e("awcn.TnetSpdySession", this.f1634c + " session null", this.p, new Object[0]);
                close();
            } catch (SpdyErrorException e) {
                if (e.SpdyErrorGetCode() == -1104 || e.SpdyErrorGetCode() == -1103) {
                    ALog.e("awcn.TnetSpdySession", "Send request on closed session!!!", this.p, new Object[0]);
                    notifyStatus(6, new anet.channel.entity.b(2));
                }
                ALog.e("awcn.TnetSpdySession", "ping", this.p, e, new Object[0]);
            } catch (Exception e2) {
                ALog.e("awcn.TnetSpdySession", "ping", this.p, e2, new Object[0]);
            }
        }
    }

    protected void b() {
        IAuth iAuth = this.E;
        if (iAuth != null) {
            iAuth.auth(this, new i(this));
            return;
        }
        notifyStatus(4, null);
        this.q.ret = 1;
        IHeartbeat iHeartbeat = this.D;
        if (iHeartbeat != null) {
            iHeartbeat.start(this);
        }
    }

    @Override // anet.channel.Session
    public boolean isAvailable() {
        return this.n == 4;
    }

    private void c() {
        SpdyAgent.enableDebug = false;
        this.w = SpdyAgent.getInstance(this.f1632a, SpdyVersion.SPDY3, SpdySessionKind.NONE_SESSION);
        ISecurity iSecurity = this.G;
        if (iSecurity != null && !iSecurity.isSecOff()) {
            this.w.setAccsSslCallback(new j(this));
        }
        if (AwcnConfig.isTnetHeaderCacheEnable()) {
            return;
        }
        try {
            this.w.getClass().getDeclaredMethod("disableHeaderCache", new Class[0]).invoke(this.w, new Object[0]);
            ALog.i("awcn.TnetSpdySession", "tnet disableHeaderCache", null, new Object[0]);
        } catch (Exception e) {
            ALog.e("awcn.TnetSpdySession", "tnet disableHeaderCache", null, e, new Object[0]);
        }
    }

    @Override // org.android.spdy.SessionCb
    public void spdySessionConnectCB(SpdySession spdySession, SuperviseConnectInfo superviseConnectInfo) {
        this.q.connectionTime = superviseConnectInfo.connectTime;
        this.q.sslTime = superviseConnectInfo.handshakeTime;
        this.q.sslCalTime = superviseConnectInfo.doHandshakeTime;
        this.q.netType = NetworkStatusHelper.getNetworkSubType();
        this.A = System.currentTimeMillis();
        notifyStatus(0, new anet.channel.entity.b(1));
        b();
        ALog.e("awcn.TnetSpdySession", "spdySessionConnectCB connect", this.p, "connectTime", Integer.valueOf(superviseConnectInfo.connectTime), "sslTime", Integer.valueOf(superviseConnectInfo.handshakeTime));
        if (this.j.isHTTP3()) {
            this.q.scid = superviseConnectInfo.scid;
            this.q.dcid = superviseConnectInfo.dcid;
            this.q.congControlKind = superviseConnectInfo.congControlKind;
            ALog.e("awcn.TnetSpdySession", "[HTTP3 spdySessionConnectCB]", this.p, "connectInfo", spdySession.getConnectInfoOnConnected());
        }
    }

    @Override // org.android.spdy.SessionCb
    public void spdyPingRecvCallback(SpdySession spdySession, long j, Object obj) {
        if (ALog.isPrintLog(2)) {
            ALog.i("awcn.TnetSpdySession", "ping receive", this.p, "Host", this.f1634c, "id", Long.valueOf(j));
        }
        if (j < 0) {
            return;
        }
        this.y = false;
        this.H = 0;
        IHeartbeat iHeartbeat = this.D;
        if (iHeartbeat != null) {
            iHeartbeat.reSchedule();
        }
        handleCallbacks(128, null);
    }

    @Override // org.android.spdy.SessionCb
    public void spdyCustomControlFrameRecvCallback(SpdySession spdySession, Object obj, int i, int i2, int i3, int i4, byte[] bArr) {
        ALog.e("awcn.TnetSpdySession", "[spdyCustomControlFrameRecvCallback]", this.p, "len", Integer.valueOf(i4), "frameCb", this.C);
        if (ALog.isPrintLog(1) && i4 < 512) {
            String str = "";
            for (byte b2 : bArr) {
                str = str + Integer.toHexString(b2 & 255) + " ";
            }
            ALog.e("awcn.TnetSpdySession", null, this.p, "str", str);
        }
        DataFrameCb dataFrameCb = this.C;
        if (dataFrameCb != null) {
            dataFrameCb.onDataReceive(this, bArr, i, i2);
        } else {
            ALog.e("awcn.TnetSpdySession", "AccsFrameCb is null", this.p, new Object[0]);
            AppMonitor.getInstance().commitStat(new ExceptionStatistic(-105, null, LinkFormat.RESOURCE_TYPE));
        }
        this.q.inceptCount++;
        IHeartbeat iHeartbeat = this.D;
        if (iHeartbeat != null) {
            iHeartbeat.reSchedule();
        }
    }

    @Override // org.android.spdy.SessionCb
    public void spdySessionFailedError(SpdySession spdySession, int i, Object obj) {
        if (spdySession != null) {
            try {
                spdySession.cleanUp();
            } catch (Exception e) {
                ALog.e("awcn.TnetSpdySession", "[spdySessionFailedError]session clean up failed!", null, e, new Object[0]);
            }
        }
        notifyStatus(2, new anet.channel.entity.b(256, i, "tnet connect fail"));
        ALog.e("awcn.TnetSpdySession", null, this.p, " errorId:", Integer.valueOf(i));
        this.q.errorCode = i;
        this.q.ret = 0;
        this.q.netType = NetworkStatusHelper.getNetworkSubType();
        AppMonitor.getInstance().commitStat(this.q);
        if (anet.channel.strategy.utils.c.b(this.q.ip)) {
            AppMonitor.getInstance().commitStat(new SessionMonitor(this.q));
        }
        AppMonitor.getInstance().commitAlarm(this.q.getAlarmObject());
    }

    @Override // org.android.spdy.SessionCb
    public void spdySessionCloseCallback(SpdySession spdySession, Object obj, SuperviseConnectInfo superviseConnectInfo, int i) {
        ALog.e("awcn.TnetSpdySession", "spdySessionCloseCallback", this.p, " errorCode:", Integer.valueOf(i));
        IHeartbeat iHeartbeat = this.D;
        if (iHeartbeat != null) {
            iHeartbeat.stop();
            this.D = null;
        }
        if (spdySession != null) {
            try {
                spdySession.cleanUp();
            } catch (Exception e) {
                ALog.e("awcn.TnetSpdySession", "session clean up failed!", null, e, new Object[0]);
            }
        }
        if (i == -3516) {
            ConnEvent connEvent = new ConnEvent();
            connEvent.isSuccess = false;
            StrategyCenter.getInstance().notifyConnEvent(this.f1635d, this.k, connEvent);
        }
        notifyStatus(6, new anet.channel.entity.b(2));
        if (superviseConnectInfo != null) {
            this.q.requestCount = superviseConnectInfo.reused_counter;
            this.q.liveTime = superviseConnectInfo.keepalive_period_second;
            try {
                if (this.j.isHTTP3()) {
                    if (spdySession != null) {
                        ALog.e("awcn.TnetSpdySession", "[HTTP3 spdySessionCloseCallback]", this.p, "connectInfo", spdySession.getConnectInfoOnDisConnected());
                    }
                    this.q.xqc0RttStatus = superviseConnectInfo.xqc0RttStatus;
                    this.q.retransmissionRate = superviseConnectInfo.retransmissionRate;
                    this.q.lossRate = superviseConnectInfo.lossRate;
                    this.q.tlpCount = superviseConnectInfo.tlpCount;
                    this.q.rtoCount = superviseConnectInfo.rtoCount;
                    this.q.srtt = superviseConnectInfo.srtt;
                }
            } catch (Exception unused) {
            }
        }
        if (this.q.errorCode == 0) {
            this.q.errorCode = i;
        }
        this.q.lastPingInterval = (int) (System.currentTimeMillis() - this.z);
        AppMonitor.getInstance().commitStat(this.q);
        if (anet.channel.strategy.utils.c.b(this.q.ip)) {
            AppMonitor.getInstance().commitStat(new SessionMonitor(this.q));
        }
        AppMonitor.getInstance().commitAlarm(this.q.getAlarmObject());
    }

    @Override // org.android.spdy.SessionCb
    public void spdyCustomControlFrameFailCallback(SpdySession spdySession, Object obj, int i, int i2) {
        ALog.e("awcn.TnetSpdySession", "spdyCustomControlFrameFailCallback", this.p, Constants.KEY_DATA_ID, Integer.valueOf(i));
        a(i, i2, true, "tnet error");
    }

    @Override // org.android.spdy.SessionCb
    public byte[] getSSLMeta(SpdySession spdySession) {
        String domain = spdySession.getDomain();
        if (TextUtils.isEmpty(domain)) {
            ALog.i("awcn.TnetSpdySession", "get sslticket host is null", null, new Object[0]);
            return null;
        }
        try {
            if (this.G == null) {
                return null;
            }
            return this.G.getBytes(this.f1632a, "accs_ssl_key2_" + domain);
        } catch (Throwable th) {
            ALog.e("awcn.TnetSpdySession", "getSSLMeta", null, th, new Object[0]);
            return null;
        }
    }

    @Override // org.android.spdy.SessionCb
    public int putSSLMeta(SpdySession spdySession, byte[] bArr) {
        String domain = spdySession.getDomain();
        if (TextUtils.isEmpty(domain)) {
            return -1;
        }
        try {
            if (this.G == null) {
                return -1;
            }
            ISecurity iSecurity = this.G;
            Context context = this.f1632a;
            StringBuilder sb = new StringBuilder();
            sb.append("accs_ssl_key2_");
            sb.append(domain);
            return iSecurity.saveBytes(context, sb.toString(), bArr) ? 0 : -1;
        } catch (Throwable th) {
            ALog.e("awcn.TnetSpdySession", "putSSLMeta", null, th, new Object[0]);
            return -1;
        }
    }

    /* JADX INFO: compiled from: Taobao */
    private class a extends anet.channel.session.a {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private Request f1814b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private RequestCb f1815c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private int f1816d = 0;
        private long e = 0;

        public a(Request request, RequestCb requestCb) {
            this.f1814b = request;
            this.f1815c = requestCb;
        }

        @Override // anet.channel.session.a, org.android.spdy.Spdycb
        public void spdyDataChunkRecvCB(SpdySession spdySession, boolean z, long j, SpdyByteArray spdyByteArray, Object obj) {
            if (ALog.isPrintLog(1)) {
                ALog.d("awcn.TnetSpdySession", "spdyDataChunkRecvCB", this.f1814b.getSeq(), "len", Integer.valueOf(spdyByteArray.getDataLength()), "fin", Boolean.valueOf(z));
            }
            this.e += (long) spdyByteArray.getDataLength();
            this.f1814b.f1794a.recDataSize += (long) spdyByteArray.getDataLength();
            if (TnetSpdySession.this.D != null) {
                TnetSpdySession.this.D.reSchedule();
            }
            if (this.f1815c != null) {
                ByteArray byteArrayA = a.C0170a.f1676a.a(spdyByteArray.getByteArray(), spdyByteArray.getDataLength());
                spdyByteArray.recycle();
                this.f1815c.onDataReceive(byteArrayA, z);
            }
            TnetSpdySession.this.handleCallbacks(32, null);
        }

        @Override // anet.channel.session.a, org.android.spdy.Spdycb
        public void spdyStreamCloseCallback(SpdySession spdySession, long j, int i, Object obj, SuperviseData superviseData) {
            if (ALog.isPrintLog(1)) {
                ALog.d("awcn.TnetSpdySession", "spdyStreamCloseCallback", this.f1814b.getSeq(), "streamId", Long.valueOf(j), "errorCode", Integer.valueOf(i));
            }
            String msg = HttpConstant.SUCCESS;
            if (i != 0) {
                this.f1816d = ErrorConstant.ERROR_TNET_REQUEST_FAIL;
                msg = ErrorConstant.formatMsg(ErrorConstant.ERROR_TNET_REQUEST_FAIL, String.valueOf(i));
                if (i != -2005) {
                    AppMonitor.getInstance().commitStat(new ExceptionStatistic(-300, msg, this.f1814b.f1794a, null));
                }
                ALog.e("awcn.TnetSpdySession", "spdyStreamCloseCallback error", this.f1814b.getSeq(), UTConstants.E_SDK_CONNECT_SESSION_ACTION, TnetSpdySession.this.p, "status code", Integer.valueOf(i), config.Constants.URL, this.f1814b.getHttpUrl().simpleUrlString());
            }
            this.f1814b.f1794a.tnetErrorCode = i;
            a(superviseData, this.f1816d, msg);
            RequestCb requestCb = this.f1815c;
            if (requestCb != null) {
                requestCb.onFinish(this.f1816d, msg, this.f1814b.f1794a);
            }
            if (i == -2004) {
                if (!TnetSpdySession.this.y) {
                    TnetSpdySession.this.ping(true);
                }
                if (TnetSpdySession.e(TnetSpdySession.this) >= 2) {
                    ConnEvent connEvent = new ConnEvent();
                    connEvent.isSuccess = false;
                    connEvent.isAccs = TnetSpdySession.this.I;
                    StrategyCenter.getInstance().notifyConnEvent(TnetSpdySession.this.f1635d, TnetSpdySession.this.k, connEvent);
                    TnetSpdySession.this.close(true);
                }
            }
        }

        private void a(SuperviseData superviseData, int i, String str) {
            try {
                this.f1814b.f1794a.rspEnd = System.currentTimeMillis();
                if (this.f1814b.f1794a.isDone.get()) {
                    return;
                }
                if (i > 0) {
                    this.f1814b.f1794a.ret = 1;
                }
                this.f1814b.f1794a.statusCode = i;
                this.f1814b.f1794a.msg = str;
                if (superviseData != null) {
                    this.f1814b.f1794a.rspEnd = superviseData.responseEnd;
                    this.f1814b.f1794a.sendBeforeTime = superviseData.sendStart - superviseData.requestStart;
                    this.f1814b.f1794a.sendDataTime = superviseData.sendEnd - this.f1814b.f1794a.sendStart;
                    this.f1814b.f1794a.firstDataTime = superviseData.responseStart - superviseData.sendEnd;
                    this.f1814b.f1794a.recDataTime = superviseData.responseEnd - superviseData.responseStart;
                    this.f1814b.f1794a.sendDataSize = superviseData.bodySize + superviseData.compressSize;
                    this.f1814b.f1794a.recDataSize = this.e + ((long) superviseData.recvUncompressSize);
                    this.f1814b.f1794a.reqHeadInflateSize = superviseData.uncompressSize;
                    this.f1814b.f1794a.reqHeadDeflateSize = superviseData.compressSize;
                    this.f1814b.f1794a.reqBodyInflateSize = superviseData.bodySize;
                    this.f1814b.f1794a.reqBodyDeflateSize = superviseData.bodySize;
                    this.f1814b.f1794a.rspHeadDeflateSize = superviseData.recvCompressSize;
                    this.f1814b.f1794a.rspHeadInflateSize = superviseData.recvUncompressSize;
                    this.f1814b.f1794a.rspBodyDeflateSize = superviseData.recvBodySize;
                    this.f1814b.f1794a.rspBodyInflateSize = this.e;
                    if (this.f1814b.f1794a.contentLength == 0) {
                        this.f1814b.f1794a.contentLength = superviseData.originContentLength;
                    }
                    TnetSpdySession.this.q.recvSizeCount += (long) (superviseData.recvBodySize + superviseData.recvCompressSize);
                    TnetSpdySession.this.q.sendSizeCount += (long) (superviseData.bodySize + superviseData.compressSize);
                }
            } catch (Exception unused) {
            }
        }

        @Override // anet.channel.session.a, org.android.spdy.Spdycb
        public void spdyOnStreamResponse(SpdySession spdySession, long j, Map<String, List<String>> map, Object obj) {
            this.f1814b.f1794a.firstDataTime = System.currentTimeMillis() - this.f1814b.f1794a.sendStart;
            this.f1816d = HttpHelper.parseStatusCode(map);
            TnetSpdySession.this.H = 0;
            ALog.i("awcn.TnetSpdySession", "", this.f1814b.getSeq(), HiAnalyticsConstant.HaKey.BI_KEY_RESULT, Integer.valueOf(this.f1816d));
            ALog.i("awcn.TnetSpdySession", "", this.f1814b.getSeq(), "response headers", map);
            RequestCb requestCb = this.f1815c;
            if (requestCb != null) {
                requestCb.onResponseCode(this.f1816d, HttpHelper.cloneMap(map));
            }
            TnetSpdySession.this.handleCallbacks(16, null);
            this.f1814b.f1794a.contentEncoding = HttpHelper.getSingleHeaderFieldByKey(map, "Content-Encoding");
            this.f1814b.f1794a.contentType = HttpHelper.getSingleHeaderFieldByKey(map, "Content-Type");
            this.f1814b.f1794a.contentLength = HttpHelper.parseContentLength(map);
            this.f1814b.f1794a.serverRT = HttpHelper.parseServerRT(map);
            TnetSpdySession.this.handleResponseCode(this.f1814b, this.f1816d);
            TnetSpdySession.this.handleResponseHeaders(this.f1814b, map);
            if (TnetSpdySession.this.D != null) {
                TnetSpdySession.this.D.reSchedule();
            }
        }
    }
}
