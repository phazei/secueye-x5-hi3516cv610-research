package com.aliyun.alink.linksdk.alcs.lpbs.data.ica;

import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDiscoveryDeviceInfo;

/* JADX INFO: loaded from: classes2.dex */
public class ICAPalDiscoveryDevInfo extends PalDiscoveryDeviceInfo {
    private boolean isPkDnNeedConvert;

    public ICAPalDiscoveryDevInfo(PalDeviceInfo palDeviceInfo, boolean z) {
        super(palDeviceInfo);
        this.isPkDnNeedConvert = z;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.data.PalDiscoveryDeviceInfo
    public boolean isPkDnNeedConvert() {
        return this.isPkDnNeedConvert;
    }
}
