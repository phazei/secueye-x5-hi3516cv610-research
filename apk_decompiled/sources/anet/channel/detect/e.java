package anet.channel.detect;

import anet.channel.AwcnConfig;
import anet.channel.strategy.IStrategyListener;
import anet.channel.strategy.l;
import anet.channel.util.ALog;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class e implements IStrategyListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ d f1700a;

    e(d dVar) {
        this.f1700a = dVar;
    }

    @Override // anet.channel.strategy.IStrategyListener
    public void onStrategyUpdated(l.d dVar) {
        ALog.i("anet.HorseRaceDetector", "onStrategyUpdated", null, new Object[0]);
        if (!AwcnConfig.isHorseRaceEnable() || dVar.f1913c == null || dVar.f1913c.length == 0) {
            return;
        }
        synchronized (this.f1700a.f1698a) {
            for (int i = 0; i < dVar.f1913c.length; i++) {
                l.c cVar = dVar.f1913c[i];
                this.f1700a.f1698a.put(cVar.f1909a, cVar);
            }
        }
    }
}
