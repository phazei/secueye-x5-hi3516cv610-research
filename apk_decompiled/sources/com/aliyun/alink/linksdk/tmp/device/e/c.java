package com.aliyun.alink.linksdk.tmp.device.e;

import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.config.DeviceConfig;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.listener.IDevRawDataListener;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tmp.listener.ITRawDataRequestHandler;
import com.aliyun.alink.linksdk.tmp.listener.ITResRequestHandler;
import java.util.Map;

/* JADX INFO: compiled from: ServerWrapper.java */
/* JADX INFO: loaded from: classes2.dex */
public class c extends b {
    public c(DeviceConfig deviceConfig) {
        super(deviceConfig);
    }

    protected c() {
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.e.b, com.aliyun.alink.linksdk.tmp.api.IDevice
    public String regRes(String str, boolean z, ITResRequestHandler iTResRequestHandler) {
        return this.mDeviceImpl.a(str, z, iTResRequestHandler);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.e.b, com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean unRegRes(String str, ITResRequestHandler iTResRequestHandler) {
        return this.mDeviceImpl.a(str, iTResRequestHandler);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.e.b, com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean triggerRes(String str, OutputParams outputParams, IPublishResourceListener iPublishResourceListener) {
        return this.mDeviceImpl.a(str, outputParams, iPublishResourceListener);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.e.b, com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean setPropertyValue(Map<String, ValueWrapper> map, boolean z, IPublishResourceListener iPublishResourceListener) {
        return this.mDeviceImpl.a(map, z, iPublishResourceListener);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.e.b, com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean sendRawData(byte[] bArr, IDevRawDataListener iDevRawDataListener) {
        return this.mDeviceImpl.b(bArr, iDevRawDataListener);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.e.b, com.aliyun.alink.linksdk.tmp.api.IDevice
    public boolean regRawRes(boolean z, ITRawDataRequestHandler iTRawDataRequestHandler) {
        return this.mDeviceImpl.a(z, iTRawDataRequestHandler);
    }
}
