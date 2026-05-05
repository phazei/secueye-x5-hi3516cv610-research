package com.aliyun.alink.business.devicecenter.api.discovery;

/* JADX INFO: loaded from: classes.dex */
public enum DiscoveryType {
    LOCAL_ONLINE_DEVICE(0, "wifi or ethernet online device"),
    CLOUD_ENROLLEE_DEVICE(1, "enrollee device, like zero provision device"),
    BLE_ENROLLEE_DEVICE(2, "wifi with bluetooth enrollee device"),
    SOFT_AP_DEVICE(3, "soft ap provision device"),
    BEACON_DEVICE(4, "smart config provision device, mock soft ap"),
    COMBO_SUBTYPE_0X03_DEVICE(5, "combo device broadcast with subType = 0x03"),
    COMBO_SUBTYPE_0X04_DEVICE(6, "provisioned， combo device broadcast with subType = 0x04"),
    APP_FOUND_BLE_MESH_DEVICE(7, "enrollee ble mesh device from app, discovered by app mesh gateway."),
    CLOUD_BLE_MESH_DEVICE(8, "enrollee ble mesh device from cloud, discovered by gateway."),
    GENIE_SOUND_BOX(9, "enrollee genie sound box device from app, discovered by app"),
    CLOUD_GENIE_SILENT_DEVICE(10, "enrollee silent device from cloud, discovered by genie sound box"),
    APP_FOUND_COMBO_MESH_DEVICE(11, "enrollee combo mesh device from app");

    public String description;
    public int type;

    DiscoveryType(int i, String str) {
        this.type = 0;
        this.description = null;
        this.type = i;
        this.description = str;
    }

    public String getDescription() {
        return this.description;
    }

    public int getType() {
        return this.type;
    }
}
