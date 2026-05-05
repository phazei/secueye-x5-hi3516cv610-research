package com.aliyun.alink.h2.api;

/* JADX INFO: loaded from: classes2.dex */
public interface CompletableListener<T> {
    void complete(T t);

    void completeExceptionally(Throwable th);
}
