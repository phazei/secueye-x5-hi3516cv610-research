package com.aliyun.alink.business.devicecenter.api.add;

import android.text.TextUtils;
import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class LKDeviceInfo implements ICacheModel, Serializable {
    public String productKey = null;
    public String productId = null;
    public String deviceName = null;

    @Override // com.aliyun.alink.business.devicecenter.api.add.ICacheModel
    public String getKey() {
        return this.productKey + "&&" + this.deviceName;
    }

    @Override // com.aliyun.alink.business.devicecenter.api.add.ICacheModel
    public boolean isValid() {
        return (TextUtils.isEmpty(this.productKey) && TextUtils.isEmpty(this.productId)) ? false : true;
    }
}
