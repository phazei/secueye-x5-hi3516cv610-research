package com.aliyun.alink.business.devicecenter.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class FilterData implements Serializable {
    public String deviceName;
    public String productId;
    public String productKey;
    public Boolean reset;

    public boolean equals(@Nullable Object obj) {
        if (obj instanceof FilterData) {
            return obj.toString().equals(toString());
        }
        return false;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public String getProductId() {
        return this.productId;
    }

    public String getProductKey() {
        return this.productKey;
    }

    public Boolean getReset() {
        return this.reset;
    }

    public void setDeviceName(String str) {
        this.deviceName = str;
    }

    public void setProductId(String str) {
        this.productId = str;
    }

    public void setProductKey(String str) {
        this.productKey = str;
    }

    public void setReset(Boolean bool) {
        this.reset = bool;
    }

    @NonNull
    public String toString() {
        return "productKey:" + this.productKey + ";deviceName:" + this.deviceName + ";reset:" + this.reset + ";pid:" + this.productId;
    }
}
