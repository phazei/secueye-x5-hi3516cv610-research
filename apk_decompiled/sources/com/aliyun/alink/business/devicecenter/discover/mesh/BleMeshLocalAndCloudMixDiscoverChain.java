package com.aliyun.alink.business.devicecenter.discover.mesh;

import android.content.Context;
import com.aliyun.alink.business.devicecenter.api.discovery.IDeviceDiscoveryListener;
import com.aliyun.alink.business.devicecenter.biz.ProvisionRepositoryV2;
import com.aliyun.alink.business.devicecenter.biz.model.response.MeshDeviceDiscoveryTriggerResponse;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.utils.PermissionCheckerUtils;
import com.aliyun.alink.business.devicecenter.utils.ThreadPool;
import com.aliyun.alink.linksdk.connectsdk.ApiCallBack;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public class BleMeshLocalAndCloudMixDiscoverChain extends BleMeshDiscoverChain {
    public BleMeshLocalAndCloudMixDiscoverChain(Context context) {
        super(context);
    }

    @Override // com.aliyun.alink.business.devicecenter.discover.mesh.BleMeshDiscoverChain, com.aliyun.alink.business.devicecenter.discover.IDiscoverChain
    public void startDiscover(IDeviceDiscoveryListener iDeviceDiscoveryListener) {
        ALog.d("BleMeshLocalAndCloudMixDiscoverChain", "startDiscover() called with: listener = [" + iDeviceDiscoveryListener + "]");
        ProvisionRepositoryV2.meshDeviceDiscoveryTrigger(null, false, new ApiCallBack<MeshDeviceDiscoveryTriggerResponse>() { // from class: com.aliyun.alink.business.devicecenter.discover.mesh.BleMeshLocalAndCloudMixDiscoverChain.1
            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onFail(int i, String str) {
                ALog.e("BleMeshLocalAndCloudMixDiscoverChain", "On Failed trigger cloud start discovery, error: " + str);
            }

            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onSuccess(MeshDeviceDiscoveryTriggerResponse meshDeviceDiscoveryTriggerResponse) {
                ALog.i("BleMeshLocalAndCloudMixDiscoverChain", "On successful trigger cloud start discovery");
            }
        });
        this.mDiscoveryViaGateway = true;
        super.startDiscover(iDeviceDiscoveryListener);
        if (PermissionCheckerUtils.isBleAvailable(this.f3624c)) {
            return;
        }
        resetFutureTask();
        this.h = ThreadPool.scheduleAtFixedRate(new Runnable() { // from class: com.aliyun.alink.business.devicecenter.discover.mesh.BleMeshLocalAndCloudMixDiscoverChain.2
            @Override // java.lang.Runnable
            public void run() {
                ALog.d("BleMeshLocalAndCloudMixDiscoverChain", "Prepare flush");
                BleMeshLocalAndCloudMixDiscoverChain bleMeshLocalAndCloudMixDiscoverChain = BleMeshLocalAndCloudMixDiscoverChain.this;
                bleMeshLocalAndCloudMixDiscoverChain.filterDispatchDevice(bleMeshLocalAndCloudMixDiscoverChain.g.size());
            }
        }, 0L, 3L, TimeUnit.SECONDS);
    }

    @Override // com.aliyun.alink.business.devicecenter.discover.mesh.BleMeshDiscoverChain, com.aliyun.alink.business.devicecenter.discover.IDiscoverChain
    public void stopDiscover() {
        ALog.d("BleMeshLocalAndCloudMixDiscoverChain", "stopDiscover() called");
        ProvisionRepositoryV2.meshDeviceDiscoveryTrigger(null, true, new ApiCallBack<MeshDeviceDiscoveryTriggerResponse>() { // from class: com.aliyun.alink.business.devicecenter.discover.mesh.BleMeshLocalAndCloudMixDiscoverChain.3
            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onFail(int i, String str) {
                ALog.e("BleMeshLocalAndCloudMixDiscoverChain", "On Failed trigger cloud stop discovery, error: " + str);
            }

            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onSuccess(MeshDeviceDiscoveryTriggerResponse meshDeviceDiscoveryTriggerResponse) {
                ALog.i("BleMeshLocalAndCloudMixDiscoverChain", "On successful trigger cloud stop discovery");
            }
        });
        super.stopDiscover();
    }
}
