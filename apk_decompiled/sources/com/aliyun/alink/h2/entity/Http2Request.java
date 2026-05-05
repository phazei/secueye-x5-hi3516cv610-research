package com.aliyun.alink.h2.entity;

/* JADX INFO: loaded from: classes2.dex */
public class Http2Request extends BaseHttpEntity {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private boolean f3882a = true;

    public boolean isEndOfStream() {
        return this.f3882a;
    }

    public void setEndOfStream(boolean z) {
        this.f3882a = z;
    }
}
