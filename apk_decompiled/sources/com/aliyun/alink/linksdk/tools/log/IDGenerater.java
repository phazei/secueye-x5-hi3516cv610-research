package com.aliyun.alink.linksdk.tools.log;

import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes2.dex */
public class IDGenerater {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static AtomicInteger f4450a = new AtomicInteger();

    public static int generateId() {
        return f4450a.incrementAndGet();
    }
}
