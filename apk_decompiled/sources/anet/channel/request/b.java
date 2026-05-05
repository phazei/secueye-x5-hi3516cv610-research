package anet.channel.request;

import anet.channel.util.ALog;
import java.util.concurrent.Future;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class b implements Cancelable {
    public static final b NULL = new b(null, null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Future<?> f1802a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final String f1803b;

    public b(Future<?> future, String str) {
        this.f1802a = future;
        this.f1803b = str;
    }

    @Override // anet.channel.request.Cancelable
    public void cancel() {
        if (this.f1802a != null) {
            ALog.i("awcn.FutureCancelable", "cancel request", this.f1803b, new Object[0]);
            this.f1802a.cancel(true);
        }
    }
}
