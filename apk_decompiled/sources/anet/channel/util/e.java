package anet.channel.util;

import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.status.NetworkStatusHelper;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class e implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ d f1945a;

    e(d dVar) {
        this.f1945a = dVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        f fVarK;
        try {
            if (this.f1945a.f1943a.equals(c.b(NetworkStatusHelper.getStatus()))) {
                ALog.e("awcn.Inet64Util", "startIpStackDetect double check", null, new Object[0]);
                int iJ = c.j();
                if (this.f1945a.f1944b.ipStackType != iJ) {
                    c.e.put(this.f1945a.f1943a, Integer.valueOf(iJ));
                    this.f1945a.f1944b.lastIpStackType = this.f1945a.f1944b.ipStackType;
                    this.f1945a.f1944b.ipStackType = iJ;
                }
                if ((iJ == 2 || iJ == 3) && (fVarK = c.k()) != null) {
                    c.f1942d.put(this.f1945a.f1943a, fVarK);
                    this.f1945a.f1944b.nat64Prefix = fVarK.toString();
                }
                if (GlobalAppRuntimeInfo.isTargetProcess()) {
                    AppMonitor.getInstance().commitStat(this.f1945a.f1944b);
                }
            }
        } catch (Exception unused) {
        }
    }
}
