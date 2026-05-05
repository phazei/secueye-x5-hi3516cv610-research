package com.aliyun.alink.business.devicecenter.biz;

import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.api.add.ICacheModel;
import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class SAPProvisionedICacheModel implements ICacheModel, Serializable {
    public String productKey = null;
    public String deviceName = null;
    public String apSsid = null;
    public String apBssid = null;
    public long aliveTime = -1;

    @Override // com.aliyun.alink.business.devicecenter.api.add.ICacheModel
    public String getKey() {
        return this.apSsid;
    }

    @Override // com.aliyun.alink.business.devicecenter.api.add.ICacheModel
    public boolean isValid() {
        return TextUtils.isEmpty(this.apBssid);
    }
}
