package com.alibaba.ailabs.iot.aisbase.utils.ut;

import com.alibaba.ailabs.iot.aisbase.Ma;
import com.alibaba.ailabs.iot.aisbase.Na;
import com.alibaba.ailabs.iot.aisbase.env.AppEnv;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;

/* JADX INFO: loaded from: classes.dex */
public class UTUtil {
    public static void updateBusInfo(String str, BluetoothDeviceWrapper bluetoothDeviceWrapper, String str2, int i, String str3) {
        if (AppEnv.IS_GENIE_ENV) {
            Na.a(str, bluetoothDeviceWrapper, str2, i, str3);
        } else {
            Ma.a(str, bluetoothDeviceWrapper, str2, i, str3);
        }
    }

    public static void updateBusInfo(String str, String str2, String str3) {
        if (AppEnv.IS_GENIE_ENV) {
            Na.a(str, str2, str3);
        } else {
            Ma.a(str, str2, str3);
        }
    }
}
