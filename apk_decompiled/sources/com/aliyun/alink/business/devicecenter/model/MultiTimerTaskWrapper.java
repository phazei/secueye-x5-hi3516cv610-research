package com.aliyun.alink.business.devicecenter.model;

import com.aliyun.alink.business.devicecenter.channel.http.RetryTransitoryClient;
import com.aliyun.alink.business.devicecenter.discover.DiscoverChainProcessor;
import com.aliyun.alink.business.devicecenter.discover.IDiscoverChain;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.utils.TimerUtils;
import java.util.concurrent.ScheduledFuture;

/* JADX INFO: loaded from: classes.dex */
public class MultiTimerTaskWrapper {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public DiscoverChainProcessor f3648a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public IDiscoverChain f3649b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public TimerUtils f3650c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public ScheduledFuture f3651d;
    public RetryTransitoryClient e;

    public MultiTimerTaskWrapper(DiscoverChainProcessor discoverChainProcessor, TimerUtils timerUtils, ScheduledFuture scheduledFuture) {
        this.f3648a = null;
        this.f3649b = null;
        this.f3650c = null;
        this.f3651d = null;
        this.e = null;
        this.f3648a = discoverChainProcessor;
        this.f3650c = timerUtils;
        this.f3651d = scheduledFuture;
    }

    public void cancelTimerTask(int i) {
        ALog.d("MultiTimerTaskWrapper", "cancelTimerTask() called with: messageId = [" + i + "]， timerTask=" + this.f3650c + ", chainProcessor=" + this.f3648a + ", scheduledFutureTask=" + this.f3651d + ", cloudDiscoverChain=" + this.f3649b + ", retryTransitoryClient=" + this.e);
        try {
            if (this.f3650c != null) {
                this.f3650c.stop(i);
                this.f3650c.setCallback(null);
            }
        } catch (Exception unused) {
        }
        try {
            if (this.f3648a != null) {
                this.f3648a.stopDiscover();
                this.f3648a = null;
            }
        } catch (Exception unused2) {
        }
        try {
            if (this.f3651d != null) {
                this.f3651d.cancel(true);
                this.f3651d = null;
            }
        } catch (Exception unused3) {
        }
        try {
            if (this.f3649b != null) {
                this.f3649b.stopDiscover();
                this.f3649b = null;
            }
        } catch (Exception unused4) {
        }
        try {
            if (this.e != null) {
                this.e.cancelRequest();
                this.e = null;
            }
        } catch (Exception unused5) {
        }
    }

    public String toString() {
        return "{\"chainProcessor\":\"" + this.f3648a + "\",\"timerTask\":\"" + this.f3650c + "\",\"scheduledFutureTask\":\"" + this.f3651d + "\",\"cloudDiscoverChain\":\"" + this.f3649b + "\",\"retryTransitoryClient\":\"" + this.e + "\"}";
    }

    public MultiTimerTaskWrapper(DiscoverChainProcessor discoverChainProcessor, TimerUtils timerUtils, ScheduledFuture scheduledFuture, IDiscoverChain iDiscoverChain, RetryTransitoryClient retryTransitoryClient) {
        this.f3648a = null;
        this.f3649b = null;
        this.f3650c = null;
        this.f3651d = null;
        this.e = null;
        this.f3648a = discoverChainProcessor;
        this.f3650c = timerUtils;
        this.f3651d = scheduledFuture;
        this.f3649b = iDiscoverChain;
        this.e = retryTransitoryClient;
    }
}
