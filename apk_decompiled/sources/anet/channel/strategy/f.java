package anet.channel.strategy;

import anet.channel.statist.StrategyStatObject;
import anet.channel.strategy.StrategyInfoHolder;
import java.io.Serializable;
import java.util.Map;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class f implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ Map.Entry f1888a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ StrategyInfoHolder.LruStrategyMap f1889b;

    f(StrategyInfoHolder.LruStrategyMap lruStrategyMap, Map.Entry entry) {
        this.f1889b = lruStrategyMap;
        this.f1888a = entry;
    }

    @Override // java.lang.Runnable
    public void run() {
        StrategyTable strategyTable = (StrategyTable) this.f1888a.getValue();
        if (strategyTable.f1862d) {
            StrategyStatObject strategyStatObject = new StrategyStatObject(1);
            strategyStatObject.writeStrategyFileId = strategyTable.f1859a;
            m.a((Serializable) this.f1888a.getValue(), strategyTable.f1859a, strategyStatObject);
            strategyTable.f1862d = false;
        }
    }
}
