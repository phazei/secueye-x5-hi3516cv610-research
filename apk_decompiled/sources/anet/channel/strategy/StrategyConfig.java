package anet.channel.strategy;

import android.text.TextUtils;
import anet.channel.strategy.l;
import anet.channel.strategy.utils.SerialLruCache;
import anet.channel.util.ALog;
import anet.channel.util.HttpConstant;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class StrategyConfig implements Serializable {
    public static final String NO_RESULT = "No_Result";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private SerialLruCache<String, String> f1848a = null;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Map<String, String> f1849b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private transient StrategyInfoHolder f1850c = null;

    StrategyConfig() {
    }

    StrategyConfig a() {
        StrategyConfig strategyConfig = new StrategyConfig();
        synchronized (this) {
            strategyConfig.f1848a = new SerialLruCache<>(this.f1848a, 256);
            strategyConfig.f1849b = new ConcurrentHashMap(this.f1849b);
            strategyConfig.f1850c = this.f1850c;
        }
        return strategyConfig;
    }

    void a(StrategyInfoHolder strategyInfoHolder) {
        this.f1850c = strategyInfoHolder;
    }

    void b() {
        if (this.f1848a == null) {
            this.f1848a = new SerialLruCache<>(256);
        }
        if (this.f1849b == null) {
            this.f1849b = new ConcurrentHashMap();
        }
    }

    void a(l.d dVar) {
        if (dVar.f1912b == null) {
            return;
        }
        synchronized (this) {
            TreeMap treeMap = null;
            for (int i = 0; i < dVar.f1912b.length; i++) {
                l.b bVar = dVar.f1912b[i];
                if (bVar.j) {
                    this.f1848a.remove(bVar.f1905a);
                } else if (bVar.f1908d != null) {
                    if (treeMap == null) {
                        treeMap = new TreeMap();
                    }
                    treeMap.put(bVar.f1905a, bVar.f1908d);
                } else {
                    if (!HttpConstant.HTTP.equalsIgnoreCase(bVar.f1907c) && !HttpConstant.HTTPS.equalsIgnoreCase(bVar.f1907c)) {
                        this.f1848a.put(bVar.f1905a, NO_RESULT);
                    } else {
                        this.f1848a.put(bVar.f1905a, bVar.f1907c);
                    }
                    if (!TextUtils.isEmpty(bVar.e)) {
                        this.f1849b.put(bVar.f1905a, bVar.e);
                    } else {
                        this.f1849b.remove(bVar.f1905a);
                    }
                }
            }
            if (treeMap != null) {
                for (Map.Entry entry : treeMap.entrySet()) {
                    String str = (String) entry.getValue();
                    if (this.f1848a.containsKey(str)) {
                        this.f1848a.put(entry.getKey(), this.f1848a.get(str));
                    } else {
                        this.f1848a.put(entry.getKey(), NO_RESULT);
                    }
                }
            }
        }
        if (ALog.isPrintLog(1)) {
            ALog.d("awcn.StrategyConfig", "", null, "SchemeMap", this.f1848a.toString());
            ALog.d("awcn.StrategyConfig", "", null, "UnitMap", this.f1849b.toString());
        }
    }

    String a(String str) {
        String str2;
        if (TextUtils.isEmpty(str) || !anet.channel.strategy.utils.c.c(str)) {
            return null;
        }
        synchronized (this) {
            str2 = this.f1848a.get(str);
            if (str2 == null) {
                this.f1848a.put(str, NO_RESULT);
            }
        }
        if (str2 == null) {
            this.f1850c.d().a(str, false);
            return str2;
        }
        if (NO_RESULT.equals(str2)) {
            return null;
        }
        return str2;
    }

    String b(String str) {
        String str2;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        synchronized (this) {
            str2 = this.f1849b.get(str);
        }
        return str2;
    }
}
