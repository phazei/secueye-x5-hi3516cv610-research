package com.aliyun.alink.business.devicecenter.provision.core;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.core.mesh.AppMeshStrategy;

/* JADX INFO: compiled from: AppMeshStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class E implements AppMeshStrategy.a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ DeviceInfo f3660a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ AppMeshStrategy f3661b;

    public E(AppMeshStrategy appMeshStrategy, DeviceInfo deviceInfo) {
        this.f3661b = appMeshStrategy;
        this.f3660a = deviceInfo;
    }

    @Override // com.aliyun.alink.business.devicecenter.provision.core.mesh.AppMeshStrategy.a
    public void onFail(int i, String str) {
        ALog.w(AppMeshStrategy.TAG, "pid returnTo Pk is fail");
        this.f3661b.provisionResCallback(this.f3660a);
    }

    @Override // com.aliyun.alink.business.devicecenter.provision.core.mesh.AppMeshStrategy.a
    public void onSuccess(Object obj) {
        if (obj != null) {
            try {
                String strValueOf = String.valueOf(JSON.parseObject(obj.toString()).get("productKey"));
                String str = AppMeshStrategy.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("pidReturnToPk productkey:");
                sb.append(strValueOf);
                ALog.d(str, sb.toString());
                if (!TextUtils.isEmpty(strValueOf)) {
                    this.f3660a.productKey = strValueOf;
                }
                if (this.f3661b.mDeviceInfo != null) {
                    this.f3660a.deviceName = this.f3661b.mDeviceInfo.deviceName;
                    this.f3661b.mDeviceInfo = null;
                }
                this.f3661b.provisionResCallback(this.f3660a);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
