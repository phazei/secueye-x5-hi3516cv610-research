package anet.channel.strategy.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class SerialLruCache<K, V> extends LinkedHashMap<K, V> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private int f1921a;

    public boolean entryRemoved(Map.Entry<K, V> entry) {
        return true;
    }

    public SerialLruCache(LinkedHashMap<K, V> linkedHashMap, int i) {
        super(linkedHashMap);
        this.f1921a = i;
    }

    @Deprecated
    public SerialLruCache(LinkedHashMap<K, V> linkedHashMap) {
        this(linkedHashMap, 256);
    }

    public SerialLruCache(int i) {
        super(i + 1, 1.0f, true);
        this.f1921a = i;
    }

    @Override // java.util.LinkedHashMap
    protected boolean removeEldestEntry(Map.Entry<K, V> entry) {
        if (size() > this.f1921a) {
            return entryRemoved(entry);
        }
        return false;
    }
}
