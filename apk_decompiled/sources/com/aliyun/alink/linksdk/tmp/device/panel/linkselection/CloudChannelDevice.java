package com.aliyun.alink.linksdk.tmp.device.panel.linkselection;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.cmp.api.ConnectSDK;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.api.DeviceManager;
import com.aliyun.alink.linksdk.tmp.connect.entity.cmp.CmpNotifyManager;
import com.aliyun.alink.linksdk.tmp.device.panel.data.InvokeServiceData;
import com.aliyun.alink.linksdk.tmp.device.panel.data.PanelMethodExtraData;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.AsyncConnectListenerWrapper;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.CloudStatusSubListenerWrapper;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.ConnectListenerWrapper;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.DoubleDataListenerWrapper;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.EventNotifyListener;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelEventCallback;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.SubsListenerWrapper;
import com.aliyun.alink.linksdk.tmp.service.TmpUTPointEx;
import com.aliyun.alink.linksdk.tmp.service.UTPoint;
import com.aliyun.alink.linksdk.tmp.storage.TmpStorage;
import com.aliyun.alink.linksdk.tmp.utils.CloudUtils;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tmp.utils.TmpEnum;
import com.aliyun.alink.linksdk.tools.ALog;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/* JADX INFO: loaded from: classes2.dex */
public class CloudChannelDevice {
    private static final String TAG = "[Tmp]CloudChannelDevice";
    private static Map<String, ScheduledFuture> scheduledFutureMap = new HashMap();
    private DeviceBasicData mBasicData;
    private String mIotId;
    private WeakReference<MultipleChannelDevice> mMultipleChannelDeviceWeakReference;
    private IPanelCallback panelCallback;

    public boolean isInit() {
        return true;
    }

    public boolean isIniting() {
        return false;
    }

    public CloudChannelDevice(String str, MultipleChannelDevice multipleChannelDevice, TmpEnum.ChannelStrategy channelStrategy) {
        this.mIotId = str;
        this.mMultipleChannelDeviceWeakReference = new WeakReference<>(multipleChannelDevice);
        TmpStorage.DeviceInfo deviceInfo = TmpStorage.getInstance().getDeviceInfo(this.mIotId);
        if (deviceInfo != null) {
            ALog.i(TAG, "clound mBasicData init");
            this.mBasicData = new DeviceBasicData(false);
            this.mBasicData.setIotId(this.mIotId);
            this.mBasicData.setProductKey(deviceInfo.mProductKey);
            this.mBasicData.setDeviceName(deviceInfo.mDeviceName);
            String macByDn = TmpStorage.getInstance().getMacByDn(deviceInfo.mDeviceName);
            if (TextUtils.isEmpty(macByDn)) {
                return;
            }
            DeviceManager.getInstance().updateDeviceInfo(deviceInfo.mProductKey, macByDn, deviceInfo.mProductKey, deviceInfo.mDeviceName);
        }
    }

    public void init(IPanelCallback iPanelCallback) {
        if (iPanelCallback != null) {
            iPanelCallback.onComplete(true, null);
        }
    }

    public void uninit() {
        CmpNotifyManager.getInstance().removeHandler(hashCode(), ConnectSDK.getInstance().getPersistentConnectId(), TmpConstant.MQTT_TOPIC_EVENTS);
        CmpNotifyManager.getInstance().removeHandler(hashCode(), ConnectSDK.getInstance().getPersistentConnectId(), TmpConstant.MQTT_TOPIC_PROPERTIES);
        CmpNotifyManager.getInstance().removeHandler(hashCode(), ConnectSDK.getInstance().getPersistentConnectId(), TmpConstant.MQTT_TOPIC_STATUS);
        CmpNotifyManager.getInstance().removeHandler(hashCode(), ConnectSDK.getInstance().getPersistentConnectId(), TmpConstant.MQTT_TOPIC_NOTIFY);
        CmpNotifyManager.getInstance().removeHandler(hashCode(), TmpConstant.LINK_DEFAULT_CONNECT_ID, TmpConstant.THING_WIFI_STATUS_NOTIFY);
    }

    public void getProperties(IPanelCallback iPanelCallback) {
        ALog.d(TAG, "getProperties callback:" + iPanelCallback);
        CloudUtils.getProperties(this.mIotId, new ConnectListenerWrapper(iPanelCallback));
    }

    public void setProperties(String str, PanelMethodExtraData panelMethodExtraData, IPanelCallback iPanelCallback) {
        ALog.d(TAG, "setProperties params:" + str + " callback:" + iPanelCallback);
        JSONObject object = JSON.parseObject(str);
        UTPoint uTPointCreateUTPoint = UTPoint.createUTPoint(object, "/living/thing/properties/set");
        TmpUTPointEx tmpUTPointEx = new TmpUTPointEx(this.mIotId, uTPointCreateUTPoint == null ? "" : uTPointCreateUTPoint.getPerformanceId(), "cloud");
        if (object != null && object.containsKey("expand") && TextUtils.equals(object.get("expand").toString(), "Scene")) {
            object.remove("expand");
            CloudUtils.executeScene(this.mIotId, object, new ConnectListenerWrapper(uTPointCreateUTPoint, tmpUTPointEx, iPanelCallback));
        } else {
            CloudUtils.setProperties(object, new ConnectListenerWrapper(uTPointCreateUTPoint, tmpUTPointEx, iPanelCallback));
        }
    }

    public void setPropertiesMesh(final String str, PanelMethodExtraData panelMethodExtraData, final IPanelCallback iPanelCallback) {
        ALog.d(TAG, "setProperties params:" + str + " callback:" + iPanelCallback);
        JSONObject object = JSON.parseObject(str);
        UTPoint uTPointCreateUTPoint = UTPoint.createUTPoint(object, "/living/thing/properties/set");
        TmpUTPointEx tmpUTPointEx = new TmpUTPointEx(this.mIotId, uTPointCreateUTPoint == null ? "" : uTPointCreateUTPoint.getPerformanceId(), "cloud");
        if (object != null && object.containsKey("expand") && TextUtils.equals(object.get("expand").toString(), "Scene")) {
            object.remove("expand");
            CloudUtils.executeScene(this.mIotId, object, new ConnectListenerWrapper(uTPointCreateUTPoint, tmpUTPointEx, iPanelCallback));
        } else {
            this.panelCallback = new IPanelCallback() { // from class: com.aliyun.alink.linksdk.tmp.device.panel.linkselection.CloudChannelDevice.1
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                    ALog.d(CloudChannelDevice.TAG, "CloudUtils.setProperties isSuccess:" + z);
                    if (z) {
                    }
                    IPanelCallback iPanelCallback2 = iPanelCallback;
                    if (iPanelCallback2 != null) {
                        iPanelCallback2.onComplete(z, obj);
                    }
                }
            };
            CloudUtils.setProperties(object, new ConnectListenerWrapper(uTPointCreateUTPoint, tmpUTPointEx, this.panelCallback));
        }
    }

    public void getLastEvent(IPanelCallback iPanelCallback) {
        ALog.d(TAG, "getEvents callback:" + iPanelCallback);
        if (iPanelCallback == null) {
            ALog.e(TAG, "getEvents callback null");
        } else {
            CloudUtils.getEvents(this.mIotId, new ConnectListenerWrapper(iPanelCallback));
        }
    }

    public void invokeService(String str, PanelMethodExtraData panelMethodExtraData, IPanelCallback iPanelCallback) {
        Object objRemove;
        ALog.d(TAG, "invokeServiceCloud params:" + str + " callback:" + iPanelCallback);
        if (iPanelCallback == null) {
            ALog.e(TAG, "invokeService callback null");
            return;
        }
        JSONObject object = null;
        IConnectSendListener doubleDataListenerWrapper = new DoubleDataListenerWrapper(iPanelCallback);
        try {
            object = JSON.parseObject(str);
            if (object != null && (objRemove = object.remove(TmpConstant.SERVICE_CALLTYPE)) != null) {
                String strValueOf = String.valueOf(objRemove);
                if (!TextUtils.isEmpty(strValueOf) && InvokeServiceData.CALL_TYPE_ASYNC.equalsIgnoreCase(strValueOf)) {
                    doubleDataListenerWrapper = new AsyncConnectListenerWrapper(iPanelCallback);
                }
            }
        } catch (Exception e) {
            ALog.e(TAG, "invokeService error:" + e.toString());
        }
        CloudUtils.invokeService(object, doubleDataListenerWrapper);
    }

    public void getStatus(IPanelCallback iPanelCallback) {
        ALog.d(TAG, "getStatus callback:" + iPanelCallback);
        if (iPanelCallback == null) {
            ALog.e(TAG, "getStatus callback null");
        } else {
            CloudUtils.getStatus(this.mIotId, new ConnectListenerWrapper(iPanelCallback));
        }
    }

    public void subAllEvents(IPanelEventCallback iPanelEventCallback, IPanelCallback iPanelCallback) {
        if (iPanelEventCallback == null) {
            ALog.e(TAG, "subAllEvents callback null");
            return;
        }
        SubsListenerWrapper subsListenerWrapper = new SubsListenerWrapper(this.mIotId, iPanelEventCallback);
        SubsListenerWrapper subsListenerWrapper2 = new SubsListenerWrapper(this.mIotId, iPanelEventCallback);
        CloudStatusSubListenerWrapper cloudStatusSubListenerWrapper = new CloudStatusSubListenerWrapper(this.mIotId, this.mMultipleChannelDeviceWeakReference.get(), iPanelEventCallback);
        EventNotifyListener eventNotifyListener = new EventNotifyListener(this.mIotId, iPanelEventCallback);
        ALog.d(TAG, "subAllEvents callback:" + iPanelCallback + " listener:" + iPanelEventCallback + " subsListener:" + subsListenerWrapper + " cloudStatusSubListenerWrapper:" + cloudStatusSubListenerWrapper);
        CmpNotifyManager.getInstance().addHandler(hashCode(), TmpConstant.LINK_DEFAULT_CONNECT_ID, TmpConstant.THING_WIFI_STATUS_NOTIFY, subsListenerWrapper2);
        CmpNotifyManager.getInstance().addHandler(hashCode(), TmpConstant.LINK_DEFAULT_CONNECT_ID, TmpConstant.THING_WIFI_RECONNECT_NOTIFY, subsListenerWrapper2);
        CmpNotifyManager.getInstance().addHandler(hashCode(), ConnectSDK.getInstance().getPersistentConnectId(), TmpConstant.MQTT_TOPIC_PROPERTIES, subsListenerWrapper2);
        CmpNotifyManager.getInstance().addHandler(hashCode(), ConnectSDK.getInstance().getPersistentConnectId(), TmpConstant.MQTT_TOPIC_EVENTS, subsListenerWrapper);
        CmpNotifyManager.getInstance().addHandler(hashCode(), ConnectSDK.getInstance().getPersistentConnectId(), TmpConstant.MQTT_TOPIC_STATUS, cloudStatusSubListenerWrapper);
        CmpNotifyManager.getInstance().addHandler(hashCode(), ConnectSDK.getInstance().getPersistentConnectId(), TmpConstant.MQTT_TOPIC_NOTIFY, eventNotifyListener);
        if (iPanelCallback != null) {
            iPanelCallback.onComplete(true, null);
        }
    }
}
