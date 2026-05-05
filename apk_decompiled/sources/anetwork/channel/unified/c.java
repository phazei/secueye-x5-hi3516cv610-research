package anetwork.channel.unified;

import anet.channel.RequestCb;
import anet.channel.bytes.ByteArray;
import anet.channel.statist.RequestStatistic;
import anet.channel.util.ALog;
import anet.channel.util.HttpHelper;
import anetwork.channel.aidl.DefaultFinishEvent;
import anetwork.channel.cookie.CookieManager;
import java.util.List;
import java.util.Map;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class c implements RequestCb {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ b f2056a;

    c(b bVar) {
        this.f2056a = bVar;
    }

    @Override // anet.channel.RequestCb
    public void onResponseCode(int i, Map<String, List<String>> map) {
        if (this.f2056a.f2054c.f2081d.get()) {
            return;
        }
        this.f2056a.f2054c.a();
        CookieManager.setCookie(this.f2056a.f2054c.f2078a.g(), map);
        this.f2056a.f2055d = HttpHelper.parseContentLength(map);
        if (this.f2056a.f2054c.f2079b != null) {
            this.f2056a.f2054c.f2079b.onResponseCode(i, map);
        }
    }

    @Override // anet.channel.RequestCb
    public void onDataReceive(ByteArray byteArray, boolean z) {
        if (this.f2056a.f2054c.f2081d.get()) {
            return;
        }
        b.b(this.f2056a);
        if (this.f2056a.f2054c.f2079b != null) {
            this.f2056a.f2054c.f2079b.onDataReceiveSize(this.f2056a.e, this.f2056a.f2055d, byteArray);
        }
    }

    @Override // anet.channel.RequestCb
    public void onFinish(int i, String str, RequestStatistic requestStatistic) {
        if (this.f2056a.f2054c.f2081d.getAndSet(true)) {
            return;
        }
        if (ALog.isPrintLog(2)) {
            ALog.i("anet.DegradeTask", "[onFinish]", this.f2056a.f2054c.f2080c, "code", Integer.valueOf(i), "msg", str);
        }
        this.f2056a.f2054c.a();
        requestStatistic.isDone.set(true);
        if (this.f2056a.f2054c.f2079b != null) {
            this.f2056a.f2054c.f2079b.onFinish(new DefaultFinishEvent(i, str, this.f2056a.f));
        }
    }
}
