package com.alibaba.sdk.android.emas;

import com.alibaba.sdk.android.tbrest.utils.LogUtil;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: compiled from: CacheManager.java */
/* JADX INFO: loaded from: classes.dex */
public class c implements Cache<g> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final int f2879a;

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    private List<g> f9a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final int f2880b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f2881c = 0;
    private final j mSendManager;

    @Override // com.alibaba.sdk.android.emas.Cache
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public g get() {
        return null;
    }

    @Override // com.alibaba.sdk.android.emas.Cache
    /* JADX INFO: renamed from: a, reason: collision with other method in class and merged with bridge method [inline-methods] */
    public boolean remove(g gVar) {
        return false;
    }

    @Override // com.alibaba.sdk.android.emas.Cache
    public void clear() {
    }

    public c(j jVar, int i, int i2) {
        this.f2879a = i;
        this.f2880b = i2;
        this.mSendManager = jVar;
    }

    @Override // com.alibaba.sdk.android.emas.Cache
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public synchronized void add(g gVar) {
        if (this.f9a == null) {
            this.f9a = new ArrayList();
        }
        this.f9a.add(gVar);
        this.f2881c += gVar.length();
        if (this.f9a.size() >= this.f2879a || this.f2881c >= this.f2880b) {
            LogUtil.d("CacheManager satisfy limit. immediately send. size: " + this.f9a.size() + ", current capacity: " + this.f2881c);
            b();
        }
    }

    public synchronized void flush() {
        if (this.f9a != null && !this.f9a.isEmpty()) {
            LogUtil.d("CacheManager flush. immediately send.");
            b();
        }
    }

    private void b() {
        this.mSendManager.a(this.f9a);
        this.f9a = null;
        this.f2881c = 0;
    }
}
