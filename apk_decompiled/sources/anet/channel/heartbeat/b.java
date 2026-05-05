package anet.channel.heartbeat;

import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.Session;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import java.util.concurrent.TimeUnit;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class b implements IHeartbeat, Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Session f1761a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private volatile long f1762b = 0;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private volatile boolean f1763c = false;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private long f1764d = 0;

    b() {
    }

    @Override // anet.channel.heartbeat.IHeartbeat
    public void start(Session session) {
        if (session == null) {
            throw new NullPointerException("session is null");
        }
        this.f1761a = session;
        this.f1764d = session.getConnStrategy().getHeartbeat();
        if (this.f1764d <= 0) {
            this.f1764d = 45000L;
        }
        ALog.i("awcn.DefaultHeartbeatImpl", "heartbeat start", session.p, UTConstants.E_SDK_CONNECT_SESSION_ACTION, session, "interval", Long.valueOf(this.f1764d));
        a(this.f1764d);
    }

    @Override // anet.channel.heartbeat.IHeartbeat
    public void stop() {
        Session session = this.f1761a;
        if (session == null) {
            return;
        }
        ALog.i("awcn.DefaultHeartbeatImpl", "heartbeat stop", session.p, UTConstants.E_SDK_CONNECT_SESSION_ACTION, this.f1761a);
        this.f1763c = true;
    }

    @Override // anet.channel.heartbeat.IHeartbeat
    public void reSchedule() {
        this.f1762b = System.currentTimeMillis() + this.f1764d;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f1763c) {
            return;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (jCurrentTimeMillis < this.f1762b - 1000) {
            a(this.f1762b - jCurrentTimeMillis);
            return;
        }
        if (!GlobalAppRuntimeInfo.isAppBackground()) {
            if (ALog.isPrintLog(1)) {
                ALog.d("awcn.DefaultHeartbeatImpl", "heartbeat", this.f1761a.p, UTConstants.E_SDK_CONNECT_SESSION_ACTION, this.f1761a);
            }
            this.f1761a.ping(true);
            a(this.f1764d);
            return;
        }
        ALog.e("awcn.DefaultHeartbeatImpl", "close session in background", this.f1761a.p, UTConstants.E_SDK_CONNECT_SESSION_ACTION, this.f1761a);
        this.f1761a.close(false);
    }

    private void a(long j) {
        try {
            this.f1762b = System.currentTimeMillis() + j;
            ThreadPoolExecutorFactory.submitScheduledTask(this, j + 50, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            ALog.e("awcn.DefaultHeartbeatImpl", "Submit heartbeat task failed.", this.f1761a.p, e, new Object[0]);
        }
    }
}
