package anet.channel.strategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class StrategyTemplate {
    Map<String, ConnProtocol> templateMap = new ConcurrentHashMap();

    public static StrategyTemplate getInstance() {
        return a.f1863a;
    }

    /* JADX INFO: compiled from: Taobao */
    static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        static StrategyTemplate f1863a = new StrategyTemplate();

        a() {
        }
    }

    public void registerConnProtocol(String str, ConnProtocol connProtocol) {
        if (connProtocol != null) {
            this.templateMap.put(str, connProtocol);
            try {
                IStrategyInstance strategyCenter = StrategyCenter.getInstance();
                if (strategyCenter instanceof g) {
                    ((g) strategyCenter).f1891b.f1853c.a(str, connProtocol);
                }
            } catch (Exception unused) {
            }
        }
    }

    public ConnProtocol getConnProtocol(String str) {
        return this.templateMap.get(str);
    }
}
