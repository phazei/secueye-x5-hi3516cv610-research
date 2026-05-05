package com.alibaba.sdk.android.push.b;

import com.alibaba.sdk.android.error.ErrorCode;

/* JADX INFO: loaded from: classes.dex */
public class f extends Exception {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private ErrorCode f3036a;

    public f(ErrorCode errorCode) {
        super(errorCode.toShortString());
        this.f3036a = errorCode;
    }

    public ErrorCode a() {
        return this.f3036a;
    }
}
