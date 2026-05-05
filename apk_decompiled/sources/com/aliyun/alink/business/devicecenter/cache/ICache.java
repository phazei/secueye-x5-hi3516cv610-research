package com.aliyun.alink.business.devicecenter.cache;

import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface ICache<T> {
    void clearCache();

    T getCache(String... strArr);

    void updateCache(List<T> list);
}
