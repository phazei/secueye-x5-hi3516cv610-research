package com.aliyun.alink.linksdk.tmp.device.a.d;

import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.config.DefaultServerConfig;
import com.aliyun.alink.linksdk.tmp.config.DeviceConfig;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.listener.ITResRequestHandler;
import com.aliyun.alink.linksdk.tmp.utils.TmpEnum;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: RegDefaultResTask.java */
/* JADX INFO: loaded from: classes2.dex */
public class h extends com.aliyun.alink.linksdk.tmp.device.a.d<h> {
    protected static com.aliyun.alink.linksdk.tmp.resource.a n;
    protected static Object o = new Object();

    public h(com.aliyun.alink.linksdk.tmp.device.a aVar, DeviceBasicData deviceBasicData, DeviceConfig deviceConfig, IDevListener iDevListener) {
        super(aVar, iDevListener);
        a(aVar);
        a(deviceBasicData);
        a(deviceConfig);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d, com.aliyun.alink.linksdk.tmp.device.a.a
    public synchronized boolean a() {
        com.aliyun.alink.linksdk.tmp.device.a aVar = this.h;
        try {
            synchronized (o) {
                if (n == null) {
                    n = new com.aliyun.alink.linksdk.tmp.resource.a(this.m.getBasicData().getProductKey(), this.m.getBasicData().getDeviceName(), aVar == null ? null : aVar.d());
                } else {
                    n.a(this.m.getBasicData().getProductKey(), this.m.getBasicData().getDeviceName(), aVar == null ? null : aVar.d());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ((DeviceConfig.DeviceType.SERVER == this.m.getDeviceType() || DeviceConfig.DeviceType.PROVISION_RECEIVER == this.m.getDeviceType()) && DefaultServerConfig.ConnectType.isConnectContainCoap(((DefaultServerConfig) this.m).getConnectType())) {
            com.aliyun.alink.linksdk.tmp.connect.b bVarA = aVar.a();
            ALog.d("[Tmp]DeviceAsyncTask", "regRes METHOD_IDENTIFIER_DEV connect+:" + bVarA);
            if (bVarA != null) {
                aVar.a(bVarA.a(TmpEnum.ConnectType.CONNECT_TYPE_COAP), "dev", false, (ITResRequestHandler) n);
            }
        }
        a((Object) null, (Object) null);
        return false;
    }
}
