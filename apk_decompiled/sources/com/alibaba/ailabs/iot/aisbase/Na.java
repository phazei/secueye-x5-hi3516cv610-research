package com.alibaba.ailabs.iot.aisbase;

import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.config.genie.smartconfig.constants.WifiProvisionUtConst;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;

/* JADX INFO: compiled from: GenieUtUtil.java */
/* JADX INFO: loaded from: classes.dex */
public class Na {
    public static void a(String str, BluetoothDeviceWrapper bluetoothDeviceWrapper, String str2, int i, String str3) {
        String strBuildDeviceInfo = UTLogUtils.buildDeviceInfo(bluetoothDeviceWrapper);
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(WifiProvisionUtConst.KEY_STEP, (Object) str2);
        jSONObject.put("channel", (Object) "ble");
        if ("error".equals(str2)) {
            jSONObject.put("errorCode", (Object) Integer.valueOf(i));
            jSONObject.put("errorDesc", (Object) str3);
        }
        a(str, strBuildDeviceInfo, jSONObject.toJSONString());
    }

    public static void a(String str, String str2, String str3) {
        UTHitBuilders.UTCustomHitBuilder uTCustomHitBuilder = new UTHitBuilders.UTCustomHitBuilder(str);
        uTCustomHitBuilder.setEventPage("ALSBluetooth");
        uTCustomHitBuilder.setProperty("user_id", RequestManage.getInstance().getUserId());
        uTCustomHitBuilder.setProperty("dev_info", str2);
        uTCustomHitBuilder.setProperty(WifiProvisionUtConst.KEY_BUSIZ_INFO, str3);
        UTAnalytics.getInstance().getDefaultTracker().send(uTCustomHitBuilder.build());
    }
}
