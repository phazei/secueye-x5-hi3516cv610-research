package anet.channel.monitor;

import anet.channel.util.ALog;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static volatile a f1773a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Map<INetworkQualityChangeListener, f> f1774b = new ConcurrentHashMap();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private f f1775c = new f();

    private a() {
    }

    public static a a() {
        if (f1773a == null) {
            synchronized (a.class) {
                if (f1773a == null) {
                    f1773a = new a();
                }
            }
        }
        return f1773a;
    }

    public void a(INetworkQualityChangeListener iNetworkQualityChangeListener, f fVar) {
        if (iNetworkQualityChangeListener == null) {
            ALog.e("BandWidthListenerHelp", "listener is null", null, new Object[0]);
            return;
        }
        if (fVar == null) {
            this.f1775c.f1791b = System.currentTimeMillis();
            this.f1774b.put(iNetworkQualityChangeListener, this.f1775c);
        } else {
            fVar.f1791b = System.currentTimeMillis();
            this.f1774b.put(iNetworkQualityChangeListener, fVar);
        }
    }

    public void a(INetworkQualityChangeListener iNetworkQualityChangeListener) {
        this.f1774b.remove(iNetworkQualityChangeListener);
    }

    public void a(double d2) {
        boolean zA;
        for (Map.Entry<INetworkQualityChangeListener, f> entry : this.f1774b.entrySet()) {
            INetworkQualityChangeListener key = entry.getKey();
            f value = entry.getValue();
            if (key != null && value != null && !value.b() && value.f1790a != (zA = value.a(d2))) {
                value.f1790a = zA;
                key.onNetworkQualityChanged(zA ? NetworkSpeed.Slow : NetworkSpeed.Fast);
            }
        }
    }
}
