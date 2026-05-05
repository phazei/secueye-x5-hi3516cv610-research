package com.aliyun.alink.linksdk.alcs.lpbs.bridge.a;

import com.aliyun.alink.linksdk.alcs.api.ICAConnectListener;
import com.aliyun.alink.linksdk.alcs.data.ica.ICADeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalConnectListener;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: ICAConnectListenerWrapper.java */
/* JADX INFO: loaded from: classes2.dex */
public class h implements ICAConnectListener {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final String f4070b = "[AlcsLPBS]ICAConnectListenerWrapper";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected PalConnectListener f4071a;

    public h(PalConnectListener palConnectListener) {
        this.f4071a = palConnectListener;
    }

    @Override // com.aliyun.alink.linksdk.alcs.api.ICAConnectListener
    public void onLoad(int i, String str, ICADeviceInfo iCADeviceInfo) {
        ALog.d(f4070b, "onLoad errorCode:" + i + " deviceInfo:" + iCADeviceInfo);
        if (i == 200) {
            i = 0;
        }
        PalConnectListener palConnectListener = this.f4071a;
        if (palConnectListener != null) {
            palConnectListener.onLoad(i, null, new PalDeviceInfo(iCADeviceInfo.productKey, iCADeviceInfo.deviceName));
        }
    }
}
