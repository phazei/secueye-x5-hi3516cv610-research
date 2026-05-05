package com.aliyun.alink.linksdk.alcs.lpbs.component.cloud;

import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public interface ICloudChannelFactory {

    public interface FactoryListener {
        void onCreate(PalDeviceInfo palDeviceInfo, IThingCloudChannel iThingCloudChannel);
    }

    void createCloudChannel(PalDeviceInfo palDeviceInfo, Map<String, Object> map, FactoryListener factoryListener);

    void releaseCloudChannel(PalDeviceInfo palDeviceInfo);
}
