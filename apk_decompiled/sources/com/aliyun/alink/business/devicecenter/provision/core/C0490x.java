package com.aliyun.alink.business.devicecenter.provision.core;

import com.alibaba.ailabs.iot.mesh.TgScanManager;
import com.alibaba.ailabs.iot.mesh.UnprovisionedBluetoothMeshDevice;
import com.alibaba.ailabs.tg.utils.ListUtils;
import com.alibaba.fastjson.JSON;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.core.mesh.AppMeshStrategy;

/* JADX INFO: renamed from: com.aliyun.alink.business.devicecenter.provision.core.x, reason: case insensitive filesystem */
/* JADX INFO: compiled from: AppMeshStrategy.java */
/* JADX INFO: loaded from: classes2.dex */
public class C0490x implements AppMeshStrategy.a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ UnprovisionedBluetoothMeshDevice f3723a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ AppMeshStrategy f3724b;

    public C0490x(AppMeshStrategy appMeshStrategy, UnprovisionedBluetoothMeshDevice unprovisionedBluetoothMeshDevice) {
        this.f3724b = appMeshStrategy;
        this.f3723a = unprovisionedBluetoothMeshDevice;
    }

    @Override // com.aliyun.alink.business.devicecenter.provision.core.mesh.AppMeshStrategy.a
    public void onFail(int i, String str) {
        ALog.w(AppMeshStrategy.TAG, "filter device fail code:" + i + ";msg:" + str);
    }

    @Override // com.aliyun.alink.business.devicecenter.provision.core.mesh.AppMeshStrategy.a
    public void onSuccess(Object obj) {
        ALog.d(AppMeshStrategy.TAG, "filter device success:" + obj);
        if (obj != null) {
            if (ListUtils.isEmpty(JSON.parseArray(obj.toString())) || this.f3724b.unprovisionedDeviceFound.get()) {
                ALog.e(AppMeshStrategy.TAG, "filter device is null");
                return;
            }
            TgScanManager.getInstance().stopGetRemoteSpecifiedPIDUnprovisionedSigMeshDeviceWithScan();
            this.f3724b.unprovisionedBluetoothMeshDevice = this.f3723a;
            this.f3724b.unprovisionedDeviceFound.set(true);
            this.f3724b.startProvision();
        }
    }
}
