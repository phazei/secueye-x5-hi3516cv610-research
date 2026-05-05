package anetwork.channel.unified;

import anet.channel.appmonitor.AppMonitor;
import anet.channel.statist.ExceptionStatistic;
import anet.channel.statist.RequestStatistic;
import anet.channel.util.ALog;
import anet.channel.util.ErrorConstant;
import anetwork.channel.aidl.DefaultFinishEvent;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class n implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ k f2089a;

    n(k kVar) {
        this.f2089a = kVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f2089a.f2082a.f2081d.compareAndSet(false, true)) {
            RequestStatistic requestStatistic = this.f2089a.f2082a.f2078a.f2045b;
            if (requestStatistic.isDone.compareAndSet(false, true)) {
                requestStatistic.statusCode = -202;
                requestStatistic.msg = ErrorConstant.getErrMsg(-202);
                requestStatistic.rspEnd = System.currentTimeMillis();
                ALog.e("anet.UnifiedRequestTask", "task time out", this.f2089a.f2082a.f2080c, "rs", requestStatistic);
                AppMonitor.getInstance().commitStat(new ExceptionStatistic(-202, null, requestStatistic, null));
            }
            this.f2089a.f2082a.b();
            this.f2089a.f2082a.f2079b.onFinish(new DefaultFinishEvent(-202, (String) null, this.f2089a.f2082a.f2078a.a()));
        }
    }
}
