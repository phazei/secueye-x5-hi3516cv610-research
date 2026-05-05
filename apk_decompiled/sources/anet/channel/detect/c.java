package anet.channel.detect;

import android.text.TextUtils;
import android.util.Pair;
import anet.channel.statist.RequestStatistic;
import anet.channel.util.ALog;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class c implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ RequestStatistic f1696a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ ExceptionDetector f1697b;

    c(ExceptionDetector exceptionDetector, RequestStatistic requestStatistic) {
        this.f1697b = exceptionDetector;
        this.f1696a = requestStatistic;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            if (this.f1696a == null) {
                return;
            }
            if (!TextUtils.isEmpty(this.f1696a.ip) && this.f1696a.ret == 0) {
                if ("guide-acs.m.taobao.com".equalsIgnoreCase(this.f1696a.host)) {
                    this.f1697b.f1685b = this.f1696a.ip;
                } else if ("msgacs.m.taobao.com".equalsIgnoreCase(this.f1696a.host)) {
                    this.f1697b.f1686c = this.f1696a.ip;
                } else if ("gw.alicdn.com".equalsIgnoreCase(this.f1696a.host)) {
                    this.f1697b.f1687d = this.f1696a.ip;
                }
            }
            if (!TextUtils.isEmpty(this.f1696a.url)) {
                this.f1697b.e.add(Pair.create(this.f1696a.url, Integer.valueOf(this.f1696a.statusCode)));
            }
            if (this.f1697b.c()) {
                this.f1697b.b();
            }
        } catch (Throwable th) {
            ALog.e("anet.ExceptionDetector", "network detect fail.", null, th, new Object[0]);
        }
    }
}
