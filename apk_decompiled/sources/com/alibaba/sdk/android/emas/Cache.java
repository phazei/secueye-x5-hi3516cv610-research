package com.alibaba.sdk.android.emas;

/* JADX INFO: loaded from: classes.dex */
public interface Cache<T> {
    void add(T t);

    void clear();

    T get();

    boolean remove(T t);
}
