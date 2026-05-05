package com.taobao.accs.data;

import anet.channel.appmonitor.AppMonitor;
import com.taobao.accs.common.Constants;
import com.taobao.accs.ut.monitor.AssembleMonitor;
import com.taobao.accs.utl.ALog;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
class c implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ a f6311a;

    c(a aVar) {
        this.f6311a = aVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        synchronized (this.f6311a) {
            if (this.f6311a.f == 0) {
                ALog.e("AssembleMessage", "timeout", Constants.KEY_DATA_ID, this.f6311a.f6307b);
                this.f6311a.f = 1;
                this.f6311a.h.clear();
                AppMonitor.getInstance().commitStat(new AssembleMonitor(this.f6311a.f6307b, String.valueOf(this.f6311a.f)));
            }
        }
    }
}
