package anet.channel.strategy;

import android.text.TextUtils;
import anet.channel.AwcnConfig;
import anet.channel.util.ALog;
import java.io.File;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class d implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f1872a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ StrategyInfoHolder f1873b;

    d(StrategyInfoHolder strategyInfoHolder, String str) {
        this.f1873b = strategyInfoHolder;
        this.f1872a = str;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            ALog.i("awcn.StrategyInfoHolder", "start loading strategy files", null, new Object[0]);
            long jCurrentTimeMillis = System.currentTimeMillis();
            if (AwcnConfig.isAsyncLoadStrategyEnable()) {
                ALog.i("awcn.StrategyInfoHolder", "load strategy async", null, new Object[0]);
                if (!TextUtils.isEmpty(this.f1872a)) {
                    this.f1873b.a(this.f1872a, true);
                }
                StrategyConfig strategyConfig = (StrategyConfig) m.a("StrategyConfig", null);
                if (strategyConfig != null) {
                    strategyConfig.b();
                    strategyConfig.a(this.f1873b);
                    synchronized (this.f1873b) {
                        this.f1873b.f1852b = strategyConfig;
                    }
                }
            }
            File[] fileArrB = m.b();
            if (fileArrB == null) {
                return;
            }
            int i = 0;
            for (int i2 = 0; i2 < fileArrB.length && i < 2; i2++) {
                File file = fileArrB[i2];
                if (!file.isDirectory()) {
                    String name = file.getName();
                    if (!name.equals(this.f1872a) && !name.startsWith("StrategyConfig")) {
                        this.f1873b.a(name, false);
                        i++;
                    }
                }
            }
            ALog.i("awcn.StrategyInfoHolder", "end loading strategy files", null, "total cost", Long.valueOf(System.currentTimeMillis() - jCurrentTimeMillis));
        } catch (Exception unused) {
        }
    }
}
