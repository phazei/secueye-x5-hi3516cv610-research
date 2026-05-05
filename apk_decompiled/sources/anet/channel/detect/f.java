package anet.channel.detect;

import anet.channel.AwcnConfig;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import anet.channel.util.AppLifecycle;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class f implements AppLifecycle.AppLifecycleListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ d f1701a;

    @Override // anet.channel.util.AppLifecycle.AppLifecycleListener
    public void forground() {
    }

    f(d dVar) {
        this.f1701a = dVar;
    }

    @Override // anet.channel.util.AppLifecycle.AppLifecycleListener
    public void background() {
        ALog.i("anet.HorseRaceDetector", "background", null, new Object[0]);
        if (AwcnConfig.isHorseRaceEnable()) {
            ThreadPoolExecutorFactory.submitHRTask(new g(this));
        }
    }
}
