package anet.channel.detect;

import anet.channel.statist.RequestStatistic;
import anet.channel.util.ALog;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class n {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static d f1714a = new d();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static ExceptionDetector f1715b = new ExceptionDetector();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static k f1716c = new k();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static AtomicBoolean f1717d = new AtomicBoolean(false);

    public static void a() {
        try {
            if (f1717d.compareAndSet(false, true)) {
                ALog.i("awcn.NetworkDetector", "registerListener", null, new Object[0]);
                f1714a.b();
                f1715b.a();
                f1716c.a();
            }
        } catch (Exception e) {
            ALog.e("awcn.NetworkDetector", "[registerListener]error", null, e, new Object[0]);
        }
    }

    public static void a(RequestStatistic requestStatistic) {
        if (f1717d.get()) {
            f1715b.a(requestStatistic);
        }
    }
}
