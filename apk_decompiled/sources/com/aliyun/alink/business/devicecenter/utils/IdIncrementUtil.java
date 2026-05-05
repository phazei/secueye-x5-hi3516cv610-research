package com.aliyun.alink.business.devicecenter.utils;

import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes2.dex */
public class IdIncrementUtil {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static AtomicInteger f3765a = new AtomicInteger();

    public static int getId() {
        return f3765a.incrementAndGet();
    }
}
