package com.aliyun.alink.business.devicecenter.provision.core;

import android.util.Log;
import com.alibaba.fastjson.JSONArray;
import com.aliyun.alink.business.devicecenter.biz.ProvisionRepositoryV2;
import com.aliyun.alink.business.devicecenter.provision.core.mesh.ConcurrentGateMeshStrategy;

/* JADX INFO: compiled from: ConcurrentGateMeshStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class O implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ConcurrentGateMeshStrategy f3674a;

    public O(ConcurrentGateMeshStrategy concurrentGateMeshStrategy) {
        this.f3674a = concurrentGateMeshStrategy;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f3674a.provisionHasStopped.get() || this.f3674a.mTaskIds == null || this.f3674a.mTaskIds.size() == 0) {
            return;
        }
        JSONArray jSONArray = new JSONArray();
        for (String str : this.f3674a.mTaskIds) {
            Log.d(ConcurrentGateMeshStrategy.TAG, "run: s=" + str);
            jSONArray.add(str);
        }
        ProvisionRepositoryV2.getBatchMeshProvisionResult(jSONArray, new N(this));
    }
}
