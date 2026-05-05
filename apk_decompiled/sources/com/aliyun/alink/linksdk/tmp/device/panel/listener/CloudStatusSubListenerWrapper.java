package com.aliyun.alink.linksdk.tmp.device.panel.listener;

import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.tmp.device.deviceshadow.DeviceShadowFetcher;
import com.aliyun.alink.linksdk.tmp.device.deviceshadow.DeviceShadowMgr;
import com.aliyun.alink.linksdk.tmp.device.panel.data.PanelMethodExtraData;
import com.aliyun.alink.linksdk.tmp.device.panel.linkselection.MultipleChannelDevice;
import com.aliyun.alink.linksdk.tmp.listener.IProcessListener;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.TmpEnum;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes2.dex */
public class CloudStatusSubListenerWrapper extends SubsListenerWrapper {
    protected WeakReference<MultipleChannelDevice> mMultipleChannelDeviceWeakReference;

    public CloudStatusSubListenerWrapper(String str, MultipleChannelDevice multipleChannelDevice, IPanelEventCallback iPanelEventCallback) {
        super(str, iPanelEventCallback);
        this.mMultipleChannelDeviceWeakReference = new WeakReference<>(multipleChannelDevice);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.SubsListenerWrapper, com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener
    public void onNotify(String str, String str2, AMessage aMessage) {
        super.onNotify(str, str2, aMessage);
        if (DeviceShadowMgr.getInstance().isPropertyCached(this.mIotId)) {
            return;
        }
        DeviceShadowMgr.getInstance().getProps(this.mIotId, true, new DeviceShadowFetcher(this.mMultipleChannelDeviceWeakReference.get(), new PanelMethodExtraData(TmpEnum.ChannelStrategy.CLOUD_CHANNEL_ONLY)), new IProcessListener() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.listener.CloudStatusSubListenerWrapper.1
            @Override // com.aliyun.alink.linksdk.tmp.listener.IProcessListener
            public void onFail(ErrorInfo errorInfo) {
            }

            @Override // com.aliyun.alink.linksdk.tmp.listener.IProcessListener
            public void onSuccess(Object obj) {
            }
        });
    }
}
