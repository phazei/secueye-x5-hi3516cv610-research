package com.alibaba.cloudapi.sdk.util;

import android.util.Log;
import com.alibaba.cloudapi.sdk.exception.SdkException;
import com.alibaba.cloudapi.sdk.model.ApiContext;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes.dex */
public class CallbackManager implements Runnable {
    static ExecutorService fixThreadPool;
    int requestExpiredTime;
    final int CHECK_EXPIRE_INTERVAL = 500;
    CountDownLatch countDownLatch = new CountDownLatch(1);
    ConcurrentHashMap<Integer, ApiContext> callbacks = new ConcurrentHashMap<>();

    public CallbackManager(int i, int i2) {
        this.requestExpiredTime = 10000;
        this.requestExpiredTime = i2;
        fixThreadPool = Executors.newFixedThreadPool(i);
    }

    public void add(Integer num, ApiContext apiContext) {
        this.callbacks.put(num, apiContext);
        CountDownLatch countDownLatch = this.countDownLatch;
        if (countDownLatch == null || countDownLatch.getCount() != 1) {
            return;
        }
        this.countDownLatch.countDown();
    }

    public ApiContext getContext(Integer num) {
        return this.callbacks.get(num);
    }

    public void callback(int i, final ApiResponse apiResponse) {
        final ApiContext apiContextRemove = this.callbacks.remove(Integer.valueOf(i));
        if (apiContextRemove != null) {
            fixThreadPool.execute(new Runnable() { // from class: com.alibaba.cloudapi.sdk.util.CallbackManager.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        apiContextRemove.getCallback().onResponse(apiContextRemove.getRequest(), apiResponse);
                    } catch (Exception e) {
                        Log.e("SDK", "Callback failed", e);
                    }
                }
            });
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        while (true) {
            Long lValueOf = Long.valueOf(new Date().getTime());
            ArrayList arrayList = new ArrayList();
            for (Map.Entry<Integer, ApiContext> entry : this.callbacks.entrySet()) {
                ApiContext value = entry.getValue();
                if (lValueOf.longValue() - value.getStartTime() > this.requestExpiredTime) {
                    value.getCallback().onFailure(value.getRequest(), new SdkException("Get Response Timeout"));
                    arrayList.add(entry.getKey());
                }
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                this.callbacks.remove((Integer) it.next());
            }
            try {
                if (this.callbacks.size() == 0) {
                    this.countDownLatch = new CountDownLatch(1);
                    this.countDownLatch.await();
                }
                Thread.sleep(500L);
            } catch (Exception e) {
                Log.e("SDK", "Check callback expired", e);
            }
        }
    }
}
