package com.aliyun.alink.linksdk.tmp.device.panel;

import android.content.Context;
import com.aliyun.alink.linksdk.tmp.data.deviceshadow.UpdateParam;
import com.aliyun.alink.linksdk.tmp.device.deviceshadow.DeviceShadowFetcher;
import com.aliyun.alink.linksdk.tmp.device.deviceshadow.DeviceShadowMgr;
import com.aliyun.alink.linksdk.tmp.device.panel.data.PanelMethodExtraData;
import com.aliyun.alink.linksdk.tmp.device.panel.linkselection.MultipleChannelDevice;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelDeviceLocalInitListener;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelEventCallback;
import com.aliyun.alink.linksdk.tmp.error.CommonError;
import com.aliyun.alink.linksdk.tmp.listener.IProcessListener;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.TmpEnum;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class PanelDevice {
    private static final String TAG = "[Tmp]PanelDevice";
    private MultipleChannelDevice mMultipleChannelDevice;

    public PanelDevice(String str) {
        this(str, null);
    }

    public PanelDevice(String str, PanelMethodExtraData panelMethodExtraData) {
        ALog.d(TAG, "PanelDevice iotId:" + str);
        this.mMultipleChannelDevice = new MultipleChannelDevice(str, panelMethodExtraData);
    }

    public void uninit() {
        this.mMultipleChannelDevice.uninit();
    }

    public void init(Context context, IPanelCallback iPanelCallback) {
        init(context, iPanelCallback, null);
    }

    public void init(Context context, IPanelCallback iPanelCallback, IPanelDeviceLocalInitListener iPanelDeviceLocalInitListener) {
        this.mMultipleChannelDevice.init(iPanelCallback, iPanelDeviceLocalInitListener);
    }

    public boolean isInit() {
        return this.mMultipleChannelDevice.isInit();
    }

    public String getIotId() {
        return this.mMultipleChannelDevice.getIotId();
    }

    public void getPropertiesByCache(final IPanelCallback iPanelCallback, PanelMethodExtraData panelMethodExtraData) {
        ALog.d(TAG, "getPropertiesByCache() called with: callback = [" + iPanelCallback + "], extraData = [" + panelMethodExtraData + "]");
        if (iPanelCallback == null) {
            ALog.e(TAG, "getProperties callback empty");
        } else {
            DeviceShadowMgr.getInstance().getProps(this.mMultipleChannelDevice.getIotId(), new DeviceShadowFetcher(this.mMultipleChannelDevice, panelMethodExtraData), new IProcessListener() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.PanelDevice.1
                @Override // com.aliyun.alink.linksdk.tmp.listener.IProcessListener
                public void onSuccess(Object obj) {
                    ALog.d(PanelDevice.TAG, "getPropertiesByCache onSuccess() called with: returnValue = [" + obj + "]");
                    iPanelCallback.onComplete(true, String.valueOf(obj));
                }

                @Override // com.aliyun.alink.linksdk.tmp.listener.IProcessListener
                public void onFail(ErrorInfo errorInfo) {
                    ALog.d(PanelDevice.TAG, "getPropertiesByCache onFail() called with: errorInfo = [" + errorInfo + "]");
                    iPanelCallback.onComplete(false, new CommonError(errorInfo));
                }
            });
        }
    }

    public void getProperties(IPanelCallback iPanelCallback, PanelMethodExtraData panelMethodExtraData) {
        this.mMultipleChannelDevice.getProperties(iPanelCallback, panelMethodExtraData);
    }

    public void getProperties(IPanelCallback iPanelCallback) {
        getProperties(iPanelCallback, null);
    }

    public void getDeviceTSL(IPanelCallback iPanelCallback) {
        this.mMultipleChannelDevice.getDeviceTSL(iPanelCallback);
    }

    public void queryTimerDownlinkTask(IPanelCallback iPanelCallback) {
        this.mMultipleChannelDevice.queryTimerDownlinkTask(iPanelCallback);
    }

    public void cacheProperties(final IPanelCallback iPanelCallback, PanelMethodExtraData panelMethodExtraData) {
        ALog.d(TAG, "cacheProperties callback:" + iPanelCallback);
        UpdateParam updateParam = new UpdateParam();
        updateParam.updateType = TmpEnum.DeviceShadowUpdateType.UPDATE_OPTION_PROPERTIES;
        DeviceShadowMgr.getInstance().refreshDeviceShadow(this.mMultipleChannelDevice.getIotId(), updateParam, new IProcessListener() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.PanelDevice.2
            @Override // com.aliyun.alink.linksdk.tmp.listener.IProcessListener
            public void onSuccess(Object obj) {
                IPanelCallback iPanelCallback2 = iPanelCallback;
                if (iPanelCallback2 != null) {
                    iPanelCallback2.onComplete(true, "");
                }
            }

            @Override // com.aliyun.alink.linksdk.tmp.listener.IProcessListener
            public void onFail(ErrorInfo errorInfo) {
                IPanelCallback iPanelCallback2 = iPanelCallback;
                if (iPanelCallback2 != null) {
                    iPanelCallback2.onComplete(false, new CommonError(errorInfo));
                }
            }
        });
    }

    public void setProperties(String str, IPanelCallback iPanelCallback) {
        setProperties(str, iPanelCallback, null);
    }

    public void setProperties(String str, IPanelCallback iPanelCallback, PanelMethodExtraData panelMethodExtraData) {
        this.mMultipleChannelDevice.setProperties(str, iPanelCallback, panelMethodExtraData);
    }

    public void setPropertyAlias(String str, IPanelCallback iPanelCallback) {
        this.mMultipleChannelDevice.setPropertyAlias(str, iPanelCallback);
    }

    @Deprecated
    public void getEvents(IPanelCallback iPanelCallback) {
        getLastEvent(iPanelCallback);
    }

    public void getLastEvent(IPanelCallback iPanelCallback) {
        this.mMultipleChannelDevice.getLastEvent(iPanelCallback);
    }

    public void invokeService(String str, IPanelCallback iPanelCallback) {
        invokeService(str, iPanelCallback, null);
    }

    public void invokeService(String str, IPanelCallback iPanelCallback, PanelMethodExtraData panelMethodExtraData) {
        this.mMultipleChannelDevice.invokeService(str, iPanelCallback, panelMethodExtraData);
    }

    public void getStatusByCache(final IPanelCallback iPanelCallback) {
        if (iPanelCallback == null) {
            ALog.e(TAG, "getStatus callback empty");
            return;
        }
        MultipleChannelDevice multipleChannelDevice = this.mMultipleChannelDevice;
        if (multipleChannelDevice != null && multipleChannelDevice.isBleMeshDevice()) {
            this.mMultipleChannelDevice.getStatus(iPanelCallback);
        } else {
            DeviceShadowMgr.getInstance().getStatus(this.mMultipleChannelDevice.getIotId(), new DeviceShadowFetcher(this.mMultipleChannelDevice, null), new IProcessListener() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.PanelDevice.3
                @Override // com.aliyun.alink.linksdk.tmp.listener.IProcessListener
                public void onSuccess(Object obj) {
                    IPanelCallback iPanelCallback2 = iPanelCallback;
                    if (iPanelCallback2 != null) {
                        iPanelCallback2.onComplete(true, String.valueOf(obj));
                    }
                }

                @Override // com.aliyun.alink.linksdk.tmp.listener.IProcessListener
                public void onFail(ErrorInfo errorInfo) {
                    IPanelCallback iPanelCallback2 = iPanelCallback;
                    if (iPanelCallback2 != null) {
                        iPanelCallback2.onComplete(false, new CommonError(errorInfo));
                    }
                }
            });
        }
    }

    public void getStatus(IPanelCallback iPanelCallback) {
        this.mMultipleChannelDevice.getStatus(iPanelCallback);
    }

    @Deprecated
    public void subAllEvent(IPanelEventCallback iPanelEventCallback, IPanelCallback iPanelCallback) {
        subAllEvents(iPanelEventCallback, iPanelCallback);
    }

    public void subAllEvents(IPanelEventCallback iPanelEventCallback, IPanelCallback iPanelCallback) {
        this.mMultipleChannelDevice.subAllEvents(iPanelEventCallback, iPanelCallback);
    }

    public void subAllEvents(IPanelEventCallback iPanelEventCallback, IPanelCallback iPanelCallback, PanelMethodExtraData panelMethodExtraData) {
        this.mMultipleChannelDevice.subAllEvents(iPanelEventCallback, iPanelCallback, panelMethodExtraData);
    }

    public void getTslByCache(final IPanelCallback iPanelCallback) {
        ALog.d(TAG, "getTslByCache() called with: callback = [" + iPanelCallback + "]");
        if (iPanelCallback == null) {
            ALog.e(TAG, "getTsl callback empty");
        } else {
            DeviceShadowMgr.getInstance().getTsl(this.mMultipleChannelDevice.getIotId(), new IProcessListener() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.PanelDevice.4
                @Override // com.aliyun.alink.linksdk.tmp.listener.IProcessListener
                public void onSuccess(Object obj) {
                    ALog.d(PanelDevice.TAG, "onSuccess() called with: returnValue = [" + obj + "]");
                    IPanelCallback iPanelCallback2 = iPanelCallback;
                    if (iPanelCallback2 != null) {
                        iPanelCallback2.onComplete(true, String.valueOf(obj));
                    }
                }

                @Override // com.aliyun.alink.linksdk.tmp.listener.IProcessListener
                public void onFail(ErrorInfo errorInfo) {
                    IPanelCallback iPanelCallback2 = iPanelCallback;
                    if (iPanelCallback2 != null) {
                        iPanelCallback2.onComplete(false, new CommonError(errorInfo));
                    }
                }
            });
        }
    }

    public void getDetailInfoByCache(final IPanelCallback iPanelCallback) {
        if (iPanelCallback == null) {
            ALog.e(TAG, "getDetailInfo callback empty");
        } else {
            DeviceShadowMgr.getInstance().getDetailInfo(this.mMultipleChannelDevice.getIotId(), new IProcessListener() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.PanelDevice.5
                @Override // com.aliyun.alink.linksdk.tmp.listener.IProcessListener
                public void onSuccess(Object obj) {
                    IPanelCallback iPanelCallback2 = iPanelCallback;
                    if (iPanelCallback2 != null) {
                        iPanelCallback2.onComplete(true, String.valueOf(obj));
                    }
                }

                @Override // com.aliyun.alink.linksdk.tmp.listener.IProcessListener
                public void onFail(ErrorInfo errorInfo) {
                    IPanelCallback iPanelCallback2 = iPanelCallback;
                    if (iPanelCallback2 != null) {
                        iPanelCallback2.onComplete(false, new CommonError(errorInfo));
                    }
                }
            });
        }
    }

    public void startLocalConnect(IPanelCallback iPanelCallback) {
        this.mMultipleChannelDevice.startLocalConnect(iPanelCallback);
    }

    public void stopLocalConnect(IPanelCallback iPanelCallback) {
        this.mMultipleChannelDevice.stopLocalConnect(iPanelCallback);
    }

    public void getLocalConnectionState(IPanelCallback iPanelCallback) {
        this.mMultipleChannelDevice.getLocalConnectionState(iPanelCallback);
    }

    public void getWifiStatus(IPanelCallback iPanelCallback) {
        this.mMultipleChannelDevice.getWifiStatus(iPanelCallback);
    }

    public void getCachedWifiStatus(IPanelCallback iPanelCallback) {
        this.mMultipleChannelDevice.getWifiStatus(iPanelCallback);
    }

    public void getDeviceNetTypesSupported(IPanelCallback iPanelCallback) {
        this.mMultipleChannelDevice.getDeviceNetTypesSupported(iPanelCallback);
    }
}
