package com.aliyun.alink.linksdk.tmp.device.a.d;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.config.DefaultProvisionConfig;
import com.aliyun.alink.linksdk.tmp.config.DeviceConfig;
import com.aliyun.alink.linksdk.tmp.data.auth.AccessInfo;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.storage.TmpStorage;
import com.aliyun.alink.linksdk.tmp.utils.AuthInfoCreater;

/* JADX INFO: compiled from: CreateProvisionConnectTask.java */
/* JADX INFO: loaded from: classes2.dex */
public class d extends b {
    public d(com.aliyun.alink.linksdk.tmp.device.a aVar, DeviceBasicData deviceBasicData, DeviceConfig deviceConfig, IDevListener iDevListener) {
        super(aVar, deviceBasicData, deviceConfig, iDevListener);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d, com.aliyun.alink.linksdk.tmp.device.a.a
    public boolean a() {
        if (TextUtils.isEmpty(((DefaultProvisionConfig) this.m).getAccessKey()) || TextUtils.isEmpty(((DefaultProvisionConfig) this.m).getAccessToken())) {
            AccessInfo accessInfo = TmpStorage.getInstance().getAccessInfo(this.m.getDevId());
            AccessInfo accessInfo2 = TmpStorage.getInstance().getAccessInfo(this.m.getDevId(), TmpStorage.FLAG_PROVISIONER);
            DefaultProvisionConfig defaultProvisionConfig = (DefaultProvisionConfig) this.m;
            if (accessInfo == null || TextUtils.isEmpty(accessInfo.mAccessKey) || TextUtils.isEmpty(accessInfo.mAccessToken)) {
                if (accessInfo2 == null || TextUtils.isEmpty(accessInfo2.mAccessKey) || TextUtils.isEmpty(accessInfo2.mAccessToken)) {
                    accessInfo = AuthInfoCreater.getInstance().createAccessInfo("Xtau@iot", "Yx3DdsyetbSezlvc", "0");
                    com.aliyun.alink.linksdk.tmp.device.a aVar = this.h;
                    if (aVar != null) {
                        aVar.a(true);
                    }
                } else {
                    accessInfo = accessInfo2;
                }
            }
            defaultProvisionConfig.setAccessKey(accessInfo.mAccessKey);
            defaultProvisionConfig.setAccessToken(accessInfo.mAccessToken);
        }
        c();
        return true;
    }
}
