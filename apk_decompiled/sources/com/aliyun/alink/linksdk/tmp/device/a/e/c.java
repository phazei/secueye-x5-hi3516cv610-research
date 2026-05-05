package com.aliyun.alink.linksdk.tmp.device.a.e;

import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.connect.a.h;
import com.aliyun.alink.linksdk.tmp.connect.e;
import com.aliyun.alink.linksdk.tmp.device.a.d;
import com.aliyun.alink.linksdk.tmp.device.payload.CommonResponsePayload;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;

/* JADX INFO: compiled from: PutAuthUserTask.java */
/* JADX INFO: loaded from: classes2.dex */
public class c extends d<c> implements com.aliyun.alink.linksdk.tmp.connect.c {
    protected static final String n = "[Tmp]PutAuthUserTask";
    protected String o;
    protected String p;

    public c(com.aliyun.alink.linksdk.tmp.device.a aVar, IDevListener iDevListener, DeviceBasicData deviceBasicData) {
        super(aVar, iDevListener);
        a(deviceBasicData);
    }

    public c a(String str) {
        this.o = str;
        return this;
    }

    public c b(String str) {
        this.p = str;
        return this;
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d, com.aliyun.alink.linksdk.tmp.device.a.a
    public boolean a() {
        super.a();
        this.i.a(h.a(this.j.getProductKey(), this.j.getDeviceName()).e(this.o).f(this.p).a(true).c(), this);
        return true;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.c
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, e eVar) {
        CommonResponsePayload commonResponsePayload;
        if (eVar != null && eVar.b() && (commonResponsePayload = (CommonResponsePayload) GsonUtils.fromJson(eVar.e(), new TypeToken<CommonResponsePayload>() { // from class: com.aliyun.alink.linksdk.tmp.device.a.e.c.1
        }.getType())) != null && commonResponsePayload.payloadSuccess()) {
            a(dVar, eVar);
        } else {
            a(dVar, new ErrorInfo(300, "response error"));
        }
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.c
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, ErrorInfo errorInfo) {
        b(dVar, errorInfo);
    }
}
