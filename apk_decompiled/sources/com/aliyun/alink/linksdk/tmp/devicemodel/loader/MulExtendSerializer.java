package com.aliyun.alink.linksdk.tmp.devicemodel.loader;

import com.aliyun.alink.linksdk.tmp.devicemodel.DeviceModel;

/* JADX INFO: loaded from: classes2.dex */
public class MulExtendSerializer extends SingleExtendSerializer {
    public MulExtendSerializer() {
        super(RootDeviceModelSerializer.MULEXTEND_DEVICEMODELSERIALIZER_ID);
    }

    @Override // com.aliyun.alink.linksdk.tmp.devicemodel.loader.SingleExtendSerializer, com.aliyun.alink.linksdk.tmp.devicemodel.loader.DeviceModelSerializer
    public String serialize(String str, DeviceModel deviceModel) {
        if (deviceModel.getExtend() != null && !deviceModel.getExtend().isEmpty()) {
            for (int i = 0; i < deviceModel.getExtend().size(); i++) {
                deviceModel.getExtend().get(0);
            }
        }
        return serializeInner(deviceModel);
    }

    @Override // com.aliyun.alink.linksdk.tmp.devicemodel.loader.SingleExtendSerializer, com.aliyun.alink.linksdk.tmp.devicemodel.loader.DeviceModelSerializer
    public boolean deserialize(String str, String str2, ILoaderHandler iLoaderHandler) {
        return super.deserialize(str, str2, iLoaderHandler);
    }
}
