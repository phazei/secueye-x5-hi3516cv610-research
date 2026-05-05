package com.aliyun.alink.linksdk.tmp.device.a.g;

import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.connect.a.k;
import com.aliyun.alink.linksdk.tmp.connect.c;
import com.aliyun.alink.linksdk.tmp.connect.e;
import com.aliyun.alink.linksdk.tmp.device.a.d;
import com.aliyun.alink.linksdk.tmp.listener.IDevRawDataListener;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.LogCat;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: SendRawDataTask.java */
/* JADX INFO: loaded from: classes2.dex */
public class a extends d<com.aliyun.alink.linksdk.tmp.device.a.h.a> implements c {
    private static final String n = "[Tmp]SendRawDataTask";
    private byte[] o;

    public a(com.aliyun.alink.linksdk.tmp.device.a aVar, DeviceBasicData deviceBasicData, IDevRawDataListener iDevRawDataListener) {
        super(aVar, null);
        a(iDevRawDataListener);
        a(deviceBasicData);
    }

    public a a(byte[] bArr) {
        this.o = bArr;
        return this;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.c
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, e eVar) {
        if (eVar != null && eVar.b()) {
            a(dVar, eVar);
        } else {
            a(dVar, (ErrorInfo) null);
        }
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.c
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, ErrorInfo errorInfo) {
        b(dVar, errorInfo);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d, com.aliyun.alink.linksdk.tmp.device.a.a
    /* JADX INFO: renamed from: b */
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, e eVar, ErrorInfo errorInfo) {
        if (this.g == null) {
            LogCat.e(n, "onFlowComplete handler empty error");
            return;
        }
        IDevRawDataListener iDevRawDataListener = this.g;
        this.g = null;
        iDevRawDataListener.onSuccess(this.e, eVar.f());
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d, com.aliyun.alink.linksdk.tmp.device.a.a
    public boolean a() {
        super.a();
        if (this.j == null || this.i == null) {
            ALog.e(n, "mDeviceBasicData or mDeviceModel or mConnect null");
            b((Object) null, new ErrorInfo(300, "param is invalid"));
            return false;
        }
        boolean zA = this.i.a(k.a(this.j.getProductKey(), this.j.getDeviceName()).a(this.o).c(), this);
        ALog.d(n, "action bRet:" + zA);
        return zA;
    }
}
