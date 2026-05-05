package com.aliyun.alink.linksdk.tmp.device.c;

import com.aliyun.alink.linksdk.tmp.listener.IProvisionResponser;
import com.aliyun.alink.linksdk.tmp.listener.ITResResponseCallback;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: compiled from: ProvisionResponser.java */
/* JADX INFO: loaded from: classes2.dex */
public class d implements IProvisionResponser {
    private static final String g = "[Tmp]ProvisionResponser";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected ITResResponseCallback f4397a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected int f4398b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected AtomicInteger f4399c = new AtomicInteger(0);

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    protected boolean f4400d = true;
    protected ErrorInfo e;
    protected Object f;

    public d(ITResResponseCallback iTResResponseCallback, int i) {
        this.f4397a = iTResResponseCallback;
        this.f4398b = i;
    }

    @Override // com.aliyun.alink.linksdk.tmp.listener.IProvisionResponser
    public void onComplete(String str, ErrorInfo errorInfo, Object obj) {
        boolean z;
        int iIncrementAndGet = this.f4399c.incrementAndGet();
        ALog.d(g, "onComplete identifer :" + str + " Ret:" + this.f4400d + " finishedCount:" + iIncrementAndGet + " mListenerCount:" + this.f4398b + " errorInfo:" + errorInfo + " data:" + obj);
        if (errorInfo == null || errorInfo.getErrorCode() == 200) {
            z = true;
        } else {
            z = false;
            this.e = errorInfo;
            this.f = obj;
        }
        this.f4400d |= z;
        if (iIncrementAndGet >= this.f4398b) {
            if (this.f4400d) {
                this.f = obj;
            }
            this.f4397a.onComplete(str, this.e, this.f);
        }
    }
}
