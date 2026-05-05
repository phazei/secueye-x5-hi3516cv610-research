package anet.channel.session;

import anet.channel.RequestCb;
import anet.channel.bytes.ByteArray;
import anet.channel.statist.RequestStatistic;
import anet.channel.util.ALog;
import anet.channel.util.HttpHelper;
import java.util.List;
import java.util.Map;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class g implements RequestCb {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ f f1828a;

    g(f fVar) {
        this.f1828a = fVar;
    }

    @Override // anet.channel.RequestCb
    public void onResponseCode(int i, Map<String, List<String>> map) {
        ALog.i("awcn.HttpSession", "", this.f1828a.f1824a.getSeq(), "httpStatusCode", Integer.valueOf(i));
        ALog.i("awcn.HttpSession", "", this.f1828a.f1824a.getSeq(), "response headers", map);
        this.f1828a.f1825b.onResponseCode(i, map);
        this.f1828a.f1826c.serverRT = HttpHelper.parseServerRT(map);
        this.f1828a.f1827d.handleResponseCode(this.f1828a.f1824a, i);
        this.f1828a.f1827d.handleResponseHeaders(this.f1828a.f1824a, map);
    }

    @Override // anet.channel.RequestCb
    public void onDataReceive(ByteArray byteArray, boolean z) {
        this.f1828a.f1825b.onDataReceive(byteArray, z);
    }

    @Override // anet.channel.RequestCb
    public void onFinish(int i, String str, RequestStatistic requestStatistic) {
        if (i <= 0 && i != -204) {
            this.f1828a.f1827d.handleCallbacks(2, new anet.channel.entity.b(2, 0, "Http connect fail"));
        }
        this.f1828a.f1825b.onFinish(i, str, requestStatistic);
    }
}
