package com.alibaba.ailabs.iot.aisbase.spec;

/* JADX INFO: loaded from: classes.dex */
public enum BluetoothDeviceSubtype {
    UNKNOWN,
    BASIC,
    BEACON,
    GMA,
    BLE,
    COMBO,
    FEIYAN;

    public static BluetoothDeviceSubtype parseFromByte(byte b2) {
        switch (b2) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                return FEIYAN;
            case 5:
            case 6:
            case 7:
            default:
                return UNKNOWN;
            case 8:
                return BASIC;
            case 9:
                return BEACON;
            case 10:
                return GMA;
            case 11:
                return BLE;
            case 12:
                return COMBO;
        }
    }
}
