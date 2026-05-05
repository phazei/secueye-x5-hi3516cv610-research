package com.aliyun.alink.linksdk.tmp.device.a.a;

import com.aliyun.alink.linksdk.cmp.manager.discovery.DiscoveryMessage;
import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.api.DeviceManager;
import com.aliyun.alink.linksdk.tmp.connect.e;
import com.aliyun.alink.linksdk.tmp.connect.entity.cmp.i;
import com.aliyun.alink.linksdk.tmp.event.INotifyHandler;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.utils.LogCat;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: RecNotifyTask.java */
/* JADX INFO: loaded from: classes2.dex */
public class d extends com.aliyun.alink.linksdk.tmp.device.a.d<a> implements INotifyHandler {
    protected static final String n = "[Tmp]RecNotifyTask";

    public d(com.aliyun.alink.linksdk.tmp.connect.b bVar, IDevListener iDevListener) {
        super(null, iDevListener);
        a(bVar);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d, com.aliyun.alink.linksdk.tmp.device.a.a
    public boolean a() {
        super.a();
        ALog.d(n, "action startNotifyMonitor");
        if (this.i == null) {
            return true;
        }
        this.i.a(this);
        return true;
    }

    public boolean b() {
        if (this.i == null) {
            return true;
        }
        this.i.c();
        return true;
    }

    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, e eVar) {
        ALog.d(n, "onDeviceFound response:" + eVar + " mDeviceHandler:" + this.f);
        if (eVar == null || eVar.a() == null) {
            LogCat.e(n, "addDevice error response null or unsuccess");
            return;
        }
        DiscoveryMessage discoveryMessage = (DiscoveryMessage) ((i) eVar).a().data;
        if (discoveryMessage == null) {
            ALog.e(n, "onDeviceFound discoveryMessage or deviceInfo null");
        }
        DeviceBasicData deviceBasicData = new DeviceBasicData(true);
        deviceBasicData.setProductKey(discoveryMessage.productKey);
        deviceBasicData.setDeviceName(discoveryMessage.deviceName);
        deviceBasicData.setModelType(discoveryMessage.modelType);
        DeviceManager.getInstance().addDeviceBasicData(deviceBasicData);
    }

    @Override // com.aliyun.alink.linksdk.tmp.event.INotifyHandler
    public void onMessage(com.aliyun.alink.linksdk.tmp.connect.d dVar, e eVar) {
        a(dVar, eVar);
    }
}
