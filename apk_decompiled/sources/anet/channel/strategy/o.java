package anet.channel.strategy;

import java.util.Comparator;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
final class o implements Comparator<StrategyCollection> {
    o() {
    }

    @Override // java.util.Comparator
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(StrategyCollection strategyCollection, StrategyCollection strategyCollection2) {
        if (strategyCollection.f1845b != strategyCollection2.f1845b) {
            return (int) (strategyCollection.f1845b - strategyCollection2.f1845b);
        }
        return strategyCollection.f1844a.compareTo(strategyCollection2.f1844a);
    }
}
