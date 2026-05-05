package com.aliyun.alink.linksdk.tmp.device.deviceshadow;

import com.aliyun.alink.linksdk.tmp.device.panel.data.PanelMethodExtraData;
import com.aliyun.alink.linksdk.tmp.device.panel.linkselection.MultipleChannelDevice;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.alink.linksdk.tmp.error.ParamsError;
import com.aliyun.alink.linksdk.tools.ALog;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes2.dex */
public class DeviceShadowFetcher {
    public static final String TAG = "[Tmp]DeviceShadowFetcher";
    protected PanelMethodExtraData mExtraData;
    protected WeakReference<MultipleChannelDevice> mMultipleChannelDeviceWeakReference;

    public DeviceShadowFetcher(MultipleChannelDevice multipleChannelDevice, PanelMethodExtraData panelMethodExtraData) {
        this.mMultipleChannelDeviceWeakReference = new WeakReference<>(multipleChannelDevice);
        this.mExtraData = panelMethodExtraData;
    }

    public MultipleChannelDevice getMultipleChannelDevice() {
        return this.mMultipleChannelDeviceWeakReference.get();
    }

    public void getStatus(IPanelCallback iPanelCallback) {
        MultipleChannelDevice multipleChannelDevice = this.mMultipleChannelDeviceWeakReference.get();
        ALog.d(TAG, "getStatus multipleChannelDevice:" + multipleChannelDevice + " panelCallback:" + iPanelCallback);
        if (multipleChannelDevice != null) {
            multipleChannelDevice.getStatus(iPanelCallback);
        } else if (iPanelCallback != null) {
            iPanelCallback.onComplete(false, new ParamsError());
        }
    }

    public void getProperties(IPanelCallback iPanelCallback) {
        MultipleChannelDevice multipleChannelDevice = this.mMultipleChannelDeviceWeakReference.get();
        ALog.d(TAG, "getProperty multipleChannelDevice:" + multipleChannelDevice + " panelCallback:" + iPanelCallback);
        if (multipleChannelDevice != null) {
            multipleChannelDevice.getProperties(iPanelCallback, this.mExtraData);
        } else if (iPanelCallback != null) {
            iPanelCallback.onComplete(false, new ParamsError());
        }
    }
}
