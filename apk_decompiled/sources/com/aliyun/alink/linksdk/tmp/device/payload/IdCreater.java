package com.aliyun.alink.linksdk.tmp.device.payload;

import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes2.dex */
public class IdCreater {
    private static volatile IdCreater mInstance;
    private AtomicInteger mId = new AtomicInteger(0);

    private IdCreater() {
    }

    public static IdCreater getInstance() {
        if (mInstance == null) {
            synchronized (IdCreater.class) {
                if (mInstance == null) {
                    mInstance = new IdCreater();
                }
            }
        }
        return mInstance;
    }

    public int getNextId() {
        int iIncrementAndGet = this.mId.incrementAndGet();
        if (iIncrementAndGet > 1000000000) {
            this.mId.set(0);
        }
        return iIncrementAndGet;
    }
}
