package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.config.genie.smartconfig.constants.WifiProvisionUtConst;

/* JADX INFO: compiled from: FeiyanUtUtil.java */
/* JADX INFO: loaded from: classes.dex */
public class Ma {
    public static void a(String str, BluetoothDeviceWrapper bluetoothDeviceWrapper, String str2, int i, String str3) {
        UTLogUtils.buildDeviceInfo(bluetoothDeviceWrapper);
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(WifiProvisionUtConst.KEY_STEP, (Object) str2);
        jSONObject.put("channel", (Object) "ble");
        if ("error".equals(str2)) {
            jSONObject.put("errorCode", (Object) Integer.valueOf(i));
            jSONObject.put("errorDesc", (Object) str3);
        }
    }

    public static void a(String str, String str2, String str3) {
    }
}
