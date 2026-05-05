package com.aliyun.alink.linksdk.tmp.device.panel.listener;

import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.device.panel.linkselection.LocalChannelDevice;

/* JADX INFO: loaded from: classes2.dex */
public class AlcsMulChannelEventListenerWrapper extends AlcsEventListenerWrapper {
    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.AlcsEventListenerWrapper
    public void stopLocalConnect() {
    }

    public AlcsMulChannelEventListenerWrapper(LocalChannelDevice localChannelDevice, DeviceBasicData deviceBasicData, IPanelCallback iPanelCallback, IPanelEventCallback iPanelEventCallback) {
        super(localChannelDevice, deviceBasicData, iPanelCallback, iPanelEventCallback);
    }
}
