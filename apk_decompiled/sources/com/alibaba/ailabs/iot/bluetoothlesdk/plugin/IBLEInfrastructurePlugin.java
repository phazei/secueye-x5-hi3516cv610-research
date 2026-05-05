package com.alibaba.ailabs.iot.bluetoothlesdk.plugin;

import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.plugin.IPlugin;
import com.alibaba.ailabs.iot.bluetoothlesdk.interfaces.OnNotifyListener;
import com.alibaba.fastjson.JSONObject;
import datasource.implemention.data.DeviceVersionInfo;

/* JADX INFO: loaded from: classes.dex */
public interface IBLEInfrastructurePlugin extends IPlugin {
    void bindDevice(IActionListener<Boolean> iActionListener);

    DeviceVersionInfo getDeviceVersionInfo();

    void reportDeviceStatus(byte[] bArr, IActionListener<Boolean> iActionListener);

    void reportOnlineStatus(boolean z, String str, IActionListener<Boolean> iActionListener);

    void sendMessage(byte b2, byte[] bArr, boolean z, IActionListener<byte[]> iActionListener);

    void sendMessage(JSONObject jSONObject, boolean z, IActionListener iActionListener);

    void setOnNotifyListener(OnNotifyListener onNotifyListener);

    void unbindDevice(IActionListener<Boolean> iActionListener);
}
