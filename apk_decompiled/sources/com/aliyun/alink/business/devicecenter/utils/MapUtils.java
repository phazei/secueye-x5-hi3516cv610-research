package com.aliyun.alink.business.devicecenter.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes2.dex */
public class MapUtils<K, V> {
    public static final int MAP_TYPE_CON_HASH_MAP = 0;
    public static final int MAP_TYPE_HASH_MAP = 2;
    public static final int MAP_TYPE_TREE_MAP = 1;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Map<K, V> f3766a;

    public MapUtils() {
        this.f3766a = null;
        this.f3766a = new ConcurrentHashMap();
    }

    public MapUtils<K, V> addKV(K k, V v) {
        this.f3766a.put(k, v);
        return this;
    }

    public MapUtils<K, V> addKVNoN(K k, V v) {
        if (v != null) {
            this.f3766a.put(k, v);
        }
        return this;
    }

    public MapUtils<K, V> addKVs(Map<K, V> map) {
        if (map == null) {
            return this;
        }
        this.f3766a.putAll(map);
        return this;
    }

    public Map<K, V> build() {
        return this.f3766a;
    }

    public void clear() {
        Map<K, V> map = this.f3766a;
        if (map != null) {
            map.clear();
        }
    }

    public MapUtils(int i) {
        this.f3766a = null;
        if (i == 2) {
            this.f3766a = new HashMap();
        } else if (i == 1) {
            this.f3766a = new TreeMap();
        } else {
            this.f3766a = new ConcurrentHashMap();
        }
    }
}
