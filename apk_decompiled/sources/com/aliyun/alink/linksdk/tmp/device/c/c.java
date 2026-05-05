package com.aliyun.alink.linksdk.tmp.device.c;

import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.api.DeviceManager;
import com.aliyun.alink.linksdk.tmp.api.IProvision;
import com.aliyun.alink.linksdk.tmp.config.DeviceConfig;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: Provision.java */
/* JADX INFO: loaded from: classes2.dex */
public class c implements IProvision {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f4395a = "[Tmp]Provision";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private com.aliyun.alink.linksdk.tmp.device.a f4396b;

    public c(DeviceConfig deviceConfig) {
        DeviceBasicData deviceBasicData = DeviceManager.getInstance().getDeviceBasicData(deviceConfig.getBasicData().getDevId());
        this.f4396b = new com.aliyun.alink.linksdk.tmp.device.a(deviceConfig, deviceBasicData == null ? new DeviceBasicData(deviceConfig.getBasicData()) : deviceBasicData);
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IProvision
    public void provisionInit(Object obj, IDevListener iDevListener) {
        ALog.d(f4395a, "init tag");
        this.f4396b.a(obj, iDevListener);
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IProvision
    public void unInit() {
        ALog.d(f4395a, "unInit");
        com.aliyun.alink.linksdk.tmp.device.a aVar = this.f4396b;
        if (aVar != null) {
            aVar.f();
        }
    }

    @Override // com.aliyun.alink.linksdk.tmp.api.IProvision
    public boolean setConfiData(Object obj, Object obj2, IDevListener iDevListener) {
        ALog.d(f4395a, "setup configData:" + obj);
        return this.f4396b.a(obj, obj2, iDevListener);
    }
}
