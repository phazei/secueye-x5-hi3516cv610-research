package anet.channel.request;

import anet.channel.util.ALog;
import org.android.spdy.SpdyErrorException;
import org.android.spdy.SpdySession;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class c implements Cancelable {
    public static final c NULL = new c(null, 0, null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f1804a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final SpdySession f1805b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final String f1806c;

    public c(SpdySession spdySession, int i, String str) {
        this.f1805b = spdySession;
        this.f1804a = i;
        this.f1806c = str;
    }

    @Override // anet.channel.request.Cancelable
    public void cancel() {
        try {
            if (this.f1805b == null || this.f1804a == 0) {
                return;
            }
            ALog.i("awcn.TnetCancelable", "cancel tnet request", this.f1806c, "streamId", Integer.valueOf(this.f1804a));
            this.f1805b.streamReset(this.f1804a, 5);
        } catch (SpdyErrorException e) {
            ALog.e("awcn.TnetCancelable", "request cancel failed.", this.f1806c, e, "errorCode", Integer.valueOf(e.SpdyErrorGetCode()));
        }
    }
}
