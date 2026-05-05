package com.alibaba.sdk.android.push.e;

import com.alibaba.sdk.android.ams.common.logger.AmsLogger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: loaded from: classes.dex */
public class b {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final AmsLogger f3073c = AmsLogger.getLogger("MPS:SyncTool");

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    Lock f3074a = new ReentrantLock();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    Condition f3075b = this.f3074a.newCondition();

    public void a() {
        this.f3074a.lock();
        try {
            this.f3075b.signal();
        } finally {
            this.f3074a.unlock();
        }
    }

    public void a(int i) {
        this.f3074a.lock();
        try {
            try {
                this.f3075b.await(i, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                f3073c.e("await error:", e);
            }
        } finally {
            this.f3074a.unlock();
        }
    }
}
