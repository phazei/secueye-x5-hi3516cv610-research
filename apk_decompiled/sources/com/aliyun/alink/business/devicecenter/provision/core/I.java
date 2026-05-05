package com.aliyun.alink.business.devicecenter.provision.core;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.core.mesh.ConcurrentAppMeshStrategy;

/* JADX INFO: compiled from: ConcurrentAppMeshStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class I implements ConcurrentAppMeshStrategy.a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ DeviceInfo f3665a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ ConcurrentAppMeshStrategy f3666b;

    public I(ConcurrentAppMeshStrategy concurrentAppMeshStrategy, DeviceInfo deviceInfo) {
        this.f3666b = concurrentAppMeshStrategy;
        this.f3665a = deviceInfo;
    }

    @Override // com.aliyun.alink.business.devicecenter.provision.core.mesh.ConcurrentAppMeshStrategy.a
    public void onFail(int i, String str) {
        ALog.w(ConcurrentAppMeshStrategy.TAG, "pid returnTo Pk is fail");
        this.f3666b.provisionResCallback(this.f3665a);
    }

    @Override // com.aliyun.alink.business.devicecenter.provision.core.mesh.ConcurrentAppMeshStrategy.a
    public void onSuccess(Object obj) {
        if (obj != null) {
            try {
                String strValueOf = String.valueOf(JSON.parseObject(obj.toString()).get("productKey"));
                String str = ConcurrentAppMeshStrategy.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("pidReturnToPk productkey:");
                sb.append(strValueOf);
                ALog.d(str, sb.toString());
                if (!TextUtils.isEmpty(strValueOf)) {
                    this.f3665a.productKey = strValueOf;
                }
                this.f3666b.provisionResCallback(this.f3665a);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
