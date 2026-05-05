package com.aliyun.alink.linksdk.cmp.core.base;

import java.util.LinkedHashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class AMessage {
    public Object data;
    private volatile Map<String, Object> mCache;

    public Object getData() {
        return this.data;
    }

    public void setData(Object obj) {
        this.data = obj;
    }

    public Object getCachedItem(String str) {
        if (this.mCache == null) {
            return null;
        }
        return this.mCache.get(str);
    }

    public void putCachedItem(String str, Object obj) {
        if (this.mCache == null) {
            synchronized (this) {
                if (this.mCache == null) {
                    this.mCache = new LinkedHashMap();
                }
            }
        }
        this.mCache.put(str, obj);
    }
}
