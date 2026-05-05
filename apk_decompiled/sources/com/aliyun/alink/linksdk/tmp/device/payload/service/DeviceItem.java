package com.aliyun.alink.linksdk.tmp.device.payload.service;

import androidx.annotation.Nullable;

/* JADX INFO: loaded from: classes2.dex */
public class DeviceItem {
    public String iotId;

    public DeviceItem(String str) {
        this.iotId = str;
    }

    public boolean equals(@Nullable Object obj) {
        if (obj instanceof String) {
            return obj.equals(this.iotId);
        }
        return super.equals(obj);
    }
}
