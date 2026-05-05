package com.taobao.accs.net;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import anet.channel.DataFrameCb;
import anet.channel.IAuth;
import anet.channel.ISessionListener;
import anet.channel.Session;
import anet.channel.SessionCenter;
import anet.channel.SessionInfo;
import anet.channel.request.Request;
import anet.channel.session.TnetSpdySession;
import com.alibaba.sdk.android.error.ErrorCode;
import com.alibaba.sdk.android.logger.ILog;
import com.aliyun.alink.linksdk.alcs.coap.resources.LinkFormat;
import com.heytap.mcssdk.constant.Constants;
import com.taobao.accs.ACCSClient;
import com.taobao.accs.AccsClientConfig;
import com.taobao.accs.AccsErrorCode;
import com.taobao.accs.AccsException;
import com.taobao.accs.AccsState;
import com.taobao.accs.ConnectionListener;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import com.taobao.accs.data.Message;
import com.taobao.accs.ut.monitor.NetPerformanceMonitor;
import com.taobao.accs.utl.AccsLogger;
import com.taobao.accs.utl.BaseMonitor;
import com.taobao.accs.utl.OrangeAdapter;
import com.taobao.accs.utl.UtilityImpl;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class j extends b implements DataFrameCb, ISessionListener {
    private boolean n;
    private long o;
    private ScheduledFuture p;
    private ScheduledFuture q;
    private boolean r;
    private ErrorCode s;
    private ILog t;
    private Runnable u;
    private Runnable v;
    private Runnable w;
    private Set<String> x;

    @Override // com.taobao.accs.net.b
    public void a(boolean z, boolean z2) {
    }

    @Override // com.taobao.accs.net.b
    public com.taobao.accs.ut.a.c c() {
        return null;
    }

    public j(Context context, int i, String str) {
        super(context, i, str);
        this.n = true;
        this.o = Constants.MILLS_OF_HOUR;
        this.r = false;
        this.s = null;
        this.u = new k(this);
        this.v = new l(this);
        this.w = new q(this);
        this.x = Collections.synchronizedSet(new HashSet());
        this.t = AccsLogger.getLogger(d());
        if (!OrangeAdapter.isTnetLogOff(true)) {
            String strD = UtilityImpl.d(this.f6366d, "inapp");
            this.t.d("config tnet log path:" + strD);
            if (!TextUtils.isEmpty(strD)) {
                Session.configTnetALog(context, strD, 5242880, 5);
            }
        }
        AccsClientConfig configByTag = AccsClientConfig.getConfigByTag(str);
        if (configByTag != null && configByTag.isChannelLoopStart()) {
            this.t.d("channel loop start");
            ThreadPoolExecutorFactory.getScheduledExecutor().scheduleWithFixedDelay(this.w, 120000L, configByTag.getLoopInterval(), TimeUnit.MILLISECONDS);
        } else {
            this.t.d("channel delay start");
            ThreadPoolExecutorFactory.getScheduledExecutor().schedule(this.w, 120000L, TimeUnit.MILLISECONDS);
        }
    }

    @Override // com.taobao.accs.net.b
    public synchronized void a() {
        this.t.d("start");
        this.n = true;
        a(this.f6366d);
    }

    @Override // com.taobao.accs.net.b
    protected void a(Message message, boolean z) {
        if (!this.n || message == null) {
            this.t.w("not running or msg null! " + this.n);
            return;
        }
        try {
            if (ThreadPoolExecutorFactory.getSendScheduledExecutor().getQueue().size() > 1000) {
                throw new RejectedExecutionException("accs");
            }
            long j = message.Q;
            if (j <= 0) {
                j = 1;
            }
            ScheduledFuture<?> scheduledFutureSchedule = ThreadPoolExecutorFactory.getSendScheduledExecutor().schedule(new m(this, message), j, TimeUnit.MILLISECONDS);
            if (message.a() == 1 && message.O != null) {
                if (message.c() && a(message.O)) {
                    this.e.b(message);
                }
                this.e.f6312a.put(message.O, scheduledFutureSchedule);
            }
            NetPerformanceMonitor netPerformanceMonitorE = message.e();
            if (netPerformanceMonitorE != null) {
                netPerformanceMonitorE.setDeviceId(UtilityImpl.getDeviceId(this.f6366d));
                netPerformanceMonitorE.setConnType(this.f6365c);
                netPerformanceMonitorE.onEnterQueueData();
            }
        } catch (RejectedExecutionException unused) {
            int size = ThreadPoolExecutorFactory.getSendScheduledExecutor().getQueue().size();
            ErrorCode errorCodeBuild = AccsErrorCode.MESSAGE_QUEUE_FULL.copy().detail("inapp " + size).build();
            this.e.a(message, errorCodeBuild);
            this.t.e("send queue full", NotificationCompat.CATEGORY_ERROR, errorCodeBuild);
        } catch (Throwable th) {
            this.t.e("send error", th);
            this.e.a(message, AccsErrorCode.SEND_LOCAL_EXCEPTION.copy().detail(AccsErrorCode.getExceptionInfo(th)).build());
        }
    }

    @Override // com.taobao.accs.net.b
    protected void a(String str, boolean z, long j) {
        ThreadPoolExecutorFactory.getScheduledExecutor().schedule(new n(this, str, j, z), j, TimeUnit.MILLISECONDS);
    }

    @Override // com.taobao.accs.net.b
    public void e() {
        this.t.e("shut down");
        this.n = false;
    }

    @Override // com.taobao.accs.net.b
    public void b() {
        this.f = 0;
    }

    @Override // com.taobao.accs.net.b
    protected void a(String str, boolean z, String str2) {
        Session session;
        try {
            Message messageB = this.e.b(str);
            if (messageB != null && messageB.f != null && (session = SessionCenter.getInstance(this.i.getAppKey()).get(messageB.f.toString(), 0L)) != null) {
                if (z) {
                    session.close(true);
                } else {
                    session.ping(true);
                }
            }
        } catch (Exception e) {
            this.t.e("onTimeOut", e);
        }
    }

    @Override // anet.channel.DataFrameCb
    public void onDataReceive(TnetSpdySession tnetSpdySession, byte[] bArr, int i, int i2) {
        ThreadPoolExecutorFactory.getScheduledExecutor().execute(new o(this, i2, i, bArr, tnetSpdySession));
    }

    @Override // anet.channel.DataFrameCb
    public void onException(int i, int i2, boolean z, String str) {
        this.t.e("onException", com.taobao.accs.common.Constants.KEY_DATA_ID, Integer.valueOf(i), "errorId", Integer.valueOf(i2), "needRetry", Boolean.valueOf(z), "detail", str);
        ThreadPoolExecutorFactory.getScheduledExecutor().execute(new p(this, i2, str, i, z));
    }

    @Override // com.taobao.accs.net.b
    public boolean a(String str) {
        if (str == null) {
            return false;
        }
        ScheduledFuture<?> scheduledFuture = this.e.f6312a.get(str);
        boolean zCancel = scheduledFuture != null ? scheduledFuture.cancel(false) : false;
        if (zCancel) {
            this.t.i("cancel", "customDataId", str);
        }
        return zCancel;
    }

    public void a(JSONObject jSONObject) {
        if (jSONObject == null) {
            this.t.e("onReceiveAccsHeartbeatResp response data is null");
            return;
        }
        this.t.i("onReceiveAccsHeartbeatResp", "data", jSONObject);
        try {
            int i = jSONObject.getInt("timeInterval");
            if (i != -1) {
                long j = i * 1000;
                if (this.o != j) {
                    if (i == 0) {
                        j = Constants.MILLS_OF_HOUR;
                    }
                    this.o = j;
                    this.p = ThreadPoolExecutorFactory.getScheduledExecutor().scheduleAtFixedRate(this.v, this.o, this.o, TimeUnit.MILLISECONDS);
                }
            } else if (this.p != null) {
                this.p.cancel(true);
            }
        } catch (JSONException e) {
            this.t.e("onReceiveAccsHeartbeatResp", e);
        }
    }

    @Override // com.taobao.accs.net.b
    protected String d() {
        return "InAppConn_" + this.m;
    }

    @Override // com.taobao.accs.net.b
    protected void a(Context context) {
        try {
            if (this.g) {
                return;
            }
            super.a(context);
            String inappHost = this.i.getInappHost();
            boolean z = false;
            if (h() && this.i.isKeepalive()) {
                z = true;
            } else {
                this.t.d("initAwcn close keepalive");
            }
            a(SessionCenter.getInstance(this.i.getAppKey()), inappHost, z);
            this.g = true;
            this.t.i("initAwcn success!");
        } catch (Throwable th) {
            this.t.e("initAwcn", th);
        }
    }

    public void a(SessionCenter sessionCenter, String str, boolean z) {
        if (this.x.contains(str)) {
            return;
        }
        SessionInfo sessionInfoCreate = SessionInfo.create(str, z, true, new a(this, str), null, this);
        sessionCenter.registerAccsSessionListener(this);
        sessionCenter.registerPublicKey(str, this.i.getInappPubKey());
        sessionCenter.registerSessionInfo(sessionInfoCreate);
        this.x.add(str);
        this.t.i("registerSessionInfo", "host", str);
    }

    public void a(AccsClientConfig accsClientConfig) {
        if (accsClientConfig == null) {
            this.t.i("updateConfig null");
            return;
        }
        if (accsClientConfig.equals(this.i)) {
            this.t.i("updateConfig not any changed");
            return;
        }
        try {
            boolean z = false;
            this.t.i("updateConfig", "old", this.i, "new", accsClientConfig);
            String inappHost = this.i.getInappHost();
            String inappHost2 = accsClientConfig.getInappHost();
            SessionCenter sessionCenter = SessionCenter.getInstance(this.i.getAppKey());
            if (sessionCenter == null) {
                this.t.d("old session not exist, no need update");
                return;
            }
            sessionCenter.unregisterSessionInfo(inappHost);
            if (this.x.contains(inappHost)) {
                this.x.remove(inappHost);
            }
            String appKey = this.i.getAppKey();
            this.i = accsClientConfig;
            this.f6364b = this.i.getAppKey();
            this.m = this.i.getTag();
            if (!appKey.equals(this.f6364b)) {
                sessionCenter = SessionCenter.getInstance(this.f6364b);
            }
            if (h() && this.i.isKeepalive()) {
                z = true;
            } else {
                this.t.i("updateConfig close keep alive");
            }
            a(sessionCenter, inappHost2, z);
            this.t.i("updateConfig done");
        } catch (Throwable th) {
            this.t.e("updateConfig", th);
        }
    }

    @Override // anet.channel.ISessionListener
    public void onConnectionChanged(Intent intent) {
        boolean booleanExtra = intent.getBooleanExtra(com.taobao.accs.common.Constants.KEY_CONNECT_AVAILABLE, false);
        String stringExtra = intent.getStringExtra("host");
        ErrorCode errorCode = com.taobao.accs.common.Constants.getErrorCode(intent);
        if (TextUtils.isEmpty(stringExtra)) {
            return;
        }
        if (!booleanExtra) {
            if (errorCode.getCodeInt() == AccsErrorCode.SUCCESS.getCodeInt()) {
                if (UtilityImpl.g(this.f6366d)) {
                    errorCode = AccsErrorCode.INAPP_CON_DISCONNECTED.copy().detail(AccsErrorCode.getAllDetails("lost connect")).build();
                } else {
                    errorCode = AccsErrorCode.NO_NETWORK.copy().detail(AccsErrorCode.getAllDetails("lost connect")).build();
                }
            }
            this.t.e("InAppConnection Not Available ", "error", errorCode);
            r();
        } else {
            this.t.i("InAppConnection Available. last status", Boolean.valueOf(this.r));
            s();
            if (!this.r) {
                b(this.f6366d);
            }
        }
        this.r = booleanExtra;
        this.s = errorCode;
        if (errorCode != null && errorCode.getCodeInt() != AccsErrorCode.SUCCESS.getCodeInt()) {
            AccsState.getInstance().b(AccsState.CONNECTION_CHANGE, LinkFormat.HOST + stringExtra + "a" + booleanExtra + "c" + errorCode.getCodeInt());
            AccsState.getInstance().b(AccsState.RECENT_ERRORS, Integer.valueOf(errorCode.getCodeInt()));
        } else {
            AccsState.getInstance().b(AccsState.CONNECTION_CHANGE, LinkFormat.HOST + stringExtra + "a" + booleanExtra);
        }
        a(booleanExtra, errorCode);
    }

    private void a(boolean z, ErrorCode errorCode) {
        try {
            for (ConnectionListener connectionListener : ACCSClient.getAccsClient(this.m).getConnectionListeners()) {
                if (z) {
                    connectionListener.onConnect();
                } else {
                    connectionListener.onDisconnect(errorCode.getCodeInt(), errorCode.getMsg());
                }
            }
        } catch (AccsException e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: compiled from: Taobao */
    public static class a implements IAuth {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private String f6382a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private int f6383b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private String f6384c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private b f6385d;
        private ILog e;

        public a(b bVar, String str) {
            this.f6384c = bVar.d();
            this.f6382a = bVar.c("https://" + str + "/accs/");
            this.f6383b = bVar.f6365c;
            this.f6385d = bVar;
            this.e = AccsLogger.getLogger(bVar.d());
        }

        @Override // anet.channel.IAuth
        public void auth(Session session, IAuth.AuthCallback authCallback) {
            this.e.i(BaseMonitor.ALARM_POINT_AUTH, config.Constants.URL, this.f6382a);
            session.request(new Request.Builder().setUrl(this.f6382a).build(), new s(this, authCallback));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void q() {
        if (this.i.isAccsHeartbeatEnable()) {
            this.t.e("startAccsHeartBeat");
            ScheduledThreadPoolExecutor scheduledExecutor = ThreadPoolExecutorFactory.getScheduledExecutor();
            Runnable runnable = this.v;
            long j = this.o;
            this.p = scheduledExecutor.scheduleAtFixedRate(runnable, j, j, TimeUnit.MILLISECONDS);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void r() {
        s();
        this.t.i("startReconnectTask");
        this.q = ThreadPoolExecutorFactory.getScheduledExecutor().scheduleWithFixedDelay(this.u, 120L, 120L, TimeUnit.SECONDS);
    }

    private void s() {
        ScheduledFuture scheduledFuture = this.q;
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            this.q = null;
        }
    }

    @Override // com.taobao.accs.net.b
    public boolean l() {
        return this.r;
    }

    @Override // com.taobao.accs.net.b
    public int m() {
        ErrorCode errorCode = this.s;
        if (errorCode != null) {
            return errorCode.getCodeInt();
        }
        return 0;
    }

    @Override // com.taobao.accs.net.b
    public void n() {
        SessionCenter sessionCenter = SessionCenter.getInstance(this.i.getAppKey());
        if (sessionCenter == null) {
            return;
        }
        String inappHost = this.i.getInappHost();
        sessionCenter.unregisterSessionInfo(inappHost);
        if (this.x.contains(inappHost)) {
            this.x.remove(inappHost);
        }
    }

    @Override // com.taobao.accs.net.b
    public void o() {
        this.t.i("reconnect begin");
        this.g = false;
        a(this.f6366d);
        ThreadPoolExecutorFactory.getSendScheduledExecutor().execute(new r(this));
    }
}
