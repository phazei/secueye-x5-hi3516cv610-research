package com.aliyun.alink.linksdk.tmp.device.deviceshadow;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.tmp.device.panel.linkselection.MultipleChannelDevice;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelEventCallback;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.ALog;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes2.dex */
public class DeviceShadowNotifier {
    private static final String TAG = "[Tmp]DeviceShadowNotifier";
    private WeakReference<MultipleChannelDevice> mMultipleChannelDeviceRef;

    public DeviceShadowNotifier(MultipleChannelDevice multipleChannelDevice) {
        this.mMultipleChannelDeviceRef = new WeakReference<>(multipleChannelDevice);
    }

    public void notifyPropertyChange(JSONObject jSONObject) {
        MultipleChannelDevice multipleChannelDevice = this.mMultipleChannelDeviceRef.get();
        ALog.d(TAG, "notifyPropertyChange multipleChannelDevice:" + multipleChannelDevice + " data:" + jSONObject);
        if (jSONObject == null || multipleChannelDevice == null) {
            ALog.e(TAG, "notifyPropertyChange data empty or multipleChannelDevice null");
            return;
        }
        try {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("iotId", (Object) multipleChannelDevice.getIotId());
            jSONObject2.put("productKey", (Object) multipleChannelDevice.getProductKey());
            jSONObject2.put("deviceName", (Object) multipleChannelDevice.getDeviceName());
            jSONObject2.put("items", (Object) jSONObject);
            notifyChange(jSONObject2, TmpConstant.MQTT_TOPIC_PROPERTIES, multipleChannelDevice.getIotId(), multipleChannelDevice.getPanelEventCallback());
        } catch (Exception e) {
            ALog.e(TAG, "notifyPropertyChange error:" + e.toString());
        }
    }

    public void notifyStatusChange(JSONObject jSONObject) {
        MultipleChannelDevice multipleChannelDevice = this.mMultipleChannelDeviceRef.get();
        ALog.d(TAG, "notifyStatusChange multipleChannelDevice:" + multipleChannelDevice + " data:" + jSONObject);
        if (jSONObject == null) {
            ALog.e(TAG, "notifyStatusChange data empty");
            return;
        }
        try {
            Object objRemove = jSONObject.remove("status");
            if (objRemove != null) {
                jSONObject.put("value", objRemove);
            }
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("iotId", (Object) multipleChannelDevice.getIotId());
            jSONObject2.put("productKey", (Object) multipleChannelDevice.getProductKey());
            jSONObject2.put("deviceName", (Object) multipleChannelDevice.getDeviceName());
            jSONObject2.put("status", (Object) jSONObject);
            notifyChange(jSONObject2, TmpConstant.MQTT_TOPIC_STATUS, multipleChannelDevice.getIotId(), multipleChannelDevice.getPanelEventCallback());
        } catch (Exception e) {
            ALog.e(TAG, "notifyStatusChange error:" + e.toString());
        }
    }

    protected void notifyChange(JSONObject jSONObject, String str, String str2, IPanelEventCallback iPanelEventCallback) {
        ALog.d(TAG, "notifyChange topic:" + str + " data:" + jSONObject + " eventCallback:" + iPanelEventCallback);
        if (iPanelEventCallback == null || jSONObject == null) {
            ALog.e(TAG, "notifyChange eventCallback empty or data empty");
        } else {
            iPanelEventCallback.onNotify(str2, str, jSONObject.toString());
        }
    }
}
