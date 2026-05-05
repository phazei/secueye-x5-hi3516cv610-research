package anet.channel.heartbeat;

import anet.channel.Session;
import anet.channel.thread.ThreadPoolExecutorFactory;
import java.util.concurrent.TimeUnit;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class c implements IHeartbeat, Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Session f1765a = null;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private volatile boolean f1766b = false;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private volatile long f1767c = System.currentTimeMillis();

    @Override // anet.channel.heartbeat.IHeartbeat
    public void start(Session session) {
        if (session == null) {
            throw new NullPointerException("session is null");
        }
        this.f1765a = session;
        this.f1767c = System.currentTimeMillis() + 45000;
        ThreadPoolExecutorFactory.submitScheduledTask(this, 45000L, TimeUnit.MILLISECONDS);
    }

    @Override // anet.channel.heartbeat.IHeartbeat
    public void stop() {
        this.f1766b = true;
    }

    @Override // anet.channel.heartbeat.IHeartbeat
    public void reSchedule() {
        this.f1767c = System.currentTimeMillis() + 45000;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f1766b) {
            return;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (jCurrentTimeMillis < this.f1767c - 1000) {
            ThreadPoolExecutorFactory.submitScheduledTask(this, this.f1767c - jCurrentTimeMillis, TimeUnit.MILLISECONDS);
        } else {
            this.f1765a.close(false);
        }
    }
}
