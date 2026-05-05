package com.alibaba.sdk.android.push.common.util.a;

import com.alibaba.sdk.android.error.ErrorCode;

/* JADX INFO: loaded from: classes.dex */
public class a extends Exception {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private ErrorCode f3054a;

    public a(ErrorCode errorCode) {
        super(errorCode.toShortString());
        this.f3054a = errorCode;
    }

    public ErrorCode a() {
        return this.f3054a;
    }
}
