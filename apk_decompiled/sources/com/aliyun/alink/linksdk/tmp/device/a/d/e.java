package com.aliyun.alink.linksdk.tmp.device.a.d;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.config.DefaultProvisionReceiverConfig;
import com.aliyun.alink.linksdk.tmp.config.DefaultServerConfig;
import com.aliyun.alink.linksdk.tmp.config.DeviceConfig;
import com.aliyun.alink.linksdk.tmp.data.auth.ServerEncryptInfo;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.storage.TmpStorage;

/* JADX INFO: compiled from: CreateProvisionReceiverConnectTask.java */
/* JADX INFO: loaded from: classes2.dex */
public class e extends b {
    public e(com.aliyun.alink.linksdk.tmp.device.a aVar, DeviceBasicData deviceBasicData, DeviceConfig deviceConfig, IDevListener iDevListener) {
        super(aVar, deviceBasicData, deviceConfig, iDevListener);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d, com.aliyun.alink.linksdk.tmp.device.a.a
    public boolean a() {
        String str;
        String str2;
        if (TextUtils.isEmpty(((DefaultServerConfig) this.m).getPrefix()) || TextUtils.isEmpty(((DefaultServerConfig) this.m).getSecret())) {
            DefaultProvisionReceiverConfig defaultProvisionReceiverConfig = (DefaultProvisionReceiverConfig) this.m;
            ServerEncryptInfo serverEnptInfo = TmpStorage.getInstance().getServerEnptInfo(defaultProvisionReceiverConfig.getDevId());
            ServerEncryptInfo serverEnptInfo2 = TmpStorage.getInstance().getServerEnptInfo(defaultProvisionReceiverConfig.getDevId(), "local");
            if (serverEnptInfo != null && !TextUtils.isEmpty(serverEnptInfo.mPrefix) && !TextUtils.isEmpty(serverEnptInfo.mSecret)) {
                str = serverEnptInfo.mPrefix;
                str2 = serverEnptInfo.mSecret;
            } else if (serverEnptInfo2 == null || TextUtils.isEmpty(serverEnptInfo2.mPrefix) || TextUtils.isEmpty(serverEnptInfo2.mSecret)) {
                str = "Xtau@iot";
                str2 = "Yx3DdsyetbSezlvc";
            } else {
                String str3 = serverEnptInfo2.mPrefix;
                String str4 = serverEnptInfo2.mSecret;
                str = str3;
                str2 = str4;
            }
            defaultProvisionReceiverConfig.setPrefix(str);
            defaultProvisionReceiverConfig.setSecret(str2);
        }
        c();
        return true;
    }
}
