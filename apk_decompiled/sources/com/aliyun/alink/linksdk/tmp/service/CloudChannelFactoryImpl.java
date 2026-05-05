package com.aliyun.alink.linksdk.tmp.service;

import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.ICloudChannelFactory;
import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IThingCloudChannel;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.tmp.service.CloudChannelImpl;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes2.dex */
public class CloudChannelFactoryImpl implements ICloudChannelFactory {
    private static final String TAG = "[Tmp]CloudChannelFactoryImpl";
    private Map<String, IThingCloudChannel> mChannelList = new ConcurrentHashMap();

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.ICloudChannelFactory
    public void createCloudChannel(final PalDeviceInfo palDeviceInfo, Map<String, Object> map, final ICloudChannelFactory.FactoryListener factoryListener) {
        if (palDeviceInfo == null || factoryListener == null) {
            ALog.e(TAG, "createCloudChannel deviceInfo null or listener null deviceInfo:" + palDeviceInfo + " listener:" + factoryListener);
            factoryListener.onCreate(palDeviceInfo, null);
            return;
        }
        IThingCloudChannel iThingCloudChannel = this.mChannelList.get(palDeviceInfo.getDevId());
        if (iThingCloudChannel != null) {
            ALog.d(TAG, "createCloudChannel exist id:" + palDeviceInfo.toString() + " channel:" + iThingCloudChannel);
            factoryListener.onCreate(palDeviceInfo, iThingCloudChannel);
            return;
        }
        final CloudChannelImpl cloudChannelImpl = new CloudChannelImpl(palDeviceInfo);
        cloudChannelImpl.initCloudChannel(map, new CloudChannelImpl.CloudChannelInitListener() { // from class: com.aliyun.alink.linksdk.tmp.service.CloudChannelFactoryImpl.1
            @Override // com.aliyun.alink.linksdk.tmp.service.CloudChannelImpl.CloudChannelInitListener
            public void onComplete(boolean z) {
                if (z) {
                    CloudChannelFactoryImpl.this.mChannelList.put(palDeviceInfo.getDevId(), cloudChannelImpl);
                    factoryListener.onCreate(palDeviceInfo, cloudChannelImpl);
                } else {
                    factoryListener.onCreate(palDeviceInfo, null);
                }
            }
        });
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.ICloudChannelFactory
    public void releaseCloudChannel(PalDeviceInfo palDeviceInfo) {
        if (palDeviceInfo == null) {
            ALog.e(TAG, "releaseCloudChannel deviceInfo null");
            return;
        }
        IThingCloudChannel iThingCloudChannelRemove = this.mChannelList.remove(palDeviceInfo.getDevId());
        ALog.d(TAG, "releaseCloudChannel deviceInfo:" + palDeviceInfo.getDevId() + " cloudChannel:" + iThingCloudChannelRemove);
        if (iThingCloudChannelRemove == null || !(iThingCloudChannelRemove instanceof CloudChannelImpl)) {
            return;
        }
        ((CloudChannelImpl) iThingCloudChannelRemove).uninitChannel();
    }
}
