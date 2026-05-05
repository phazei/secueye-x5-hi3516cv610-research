package com.aliyun.alink.linksdk.tmp.utils;

/* JADX INFO: loaded from: classes2.dex */
public abstract class SingleInstance<T> {
    protected volatile T mInstance;

    public abstract T create();

    public final T getInstance() {
        if (this.mInstance == null) {
            synchronized (this) {
                if (this.mInstance == null) {
                    this.mInstance = create();
                }
            }
        }
        return this.mInstance;
    }
}
