package com.aliyun.alink.linksdk.tmp.device.a.b;

import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.connect.CommonRequestBuilder;
import com.aliyun.alink.linksdk.tmp.connect.a.o;
import com.aliyun.alink.linksdk.tmp.connect.e;
import com.aliyun.alink.linksdk.tmp.device.a.d;
import com.aliyun.alink.linksdk.tmp.device.payload.CommonResponsePayload;
import com.aliyun.alink.linksdk.tmp.device.payload.event.EventRequestPayload;
import com.aliyun.alink.linksdk.tmp.event.INotifyHandler;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tmp.utils.LogCat;
import com.google.gson.reflect.TypeToken;

/* JADX INFO: compiled from: PropertySubEventTask.java */
/* JADX INFO: loaded from: classes2.dex */
public class b extends d<b> implements com.aliyun.alink.linksdk.tmp.connect.c {
    protected static final String n = "PropertySubEventTask";
    protected INotifyHandler o;
    protected String p;

    public b(com.aliyun.alink.linksdk.tmp.device.a aVar, DeviceBasicData deviceBasicData, IDevListener iDevListener) {
        super(aVar, iDevListener);
        a(deviceBasicData);
        a(aVar);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public b a(INotifyHandler iNotifyHandler) {
        this.o = iNotifyHandler;
        return (b) this.f4346d;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public b a(String str) {
        this.p = str;
        return (b) this.f4346d;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.c
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, e eVar) {
        if (eVar != null && eVar.b()) {
            CommonResponsePayload commonResponsePayload = (CommonResponsePayload) GsonUtils.fromJson(eVar.e(), new TypeToken<CommonResponsePayload>() { // from class: com.aliyun.alink.linksdk.tmp.device.a.b.b.1
            }.getType());
            if (commonResponsePayload != null && commonResponsePayload.getCode() == 200) {
                LogCat.d(n, "onLoad normal success");
                a(dVar, eVar);
                return;
            } else {
                LogCat.d(n, "onLoad normal error");
                return;
            }
        }
        LogCat.d(n, "onLoad error");
        b(dVar, new ErrorInfo(300, "response error"));
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.c
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, ErrorInfo errorInfo) {
        b(dVar, errorInfo);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d, com.aliyun.alink.linksdk.tmp.device.a.a
    public boolean a() {
        com.aliyun.alink.linksdk.tmp.device.a aVar;
        super.a();
        EventRequestPayload eventRequestPayload = new EventRequestPayload(this.j.getProductKey(), this.j.getDeviceName());
        if (this.k == null && this.h != null && (aVar = this.h) != null) {
            this.k = aVar.d();
        }
        eventRequestPayload.setMethod(this.k.getEventMethod(this.p));
        com.aliyun.alink.linksdk.tmp.connect.d dVarC = o.d().a(this.e).a(this.j.getPort()).a(this.j.getAddr()).k(this.j.getProductKey()).l(this.j.getDeviceName()).c(CommonRequestBuilder.a(this.j.getProductKey(), this.j.getDeviceName(), eventRequestPayload.getMethod(), "sys")).m(eventRequestPayload.getMethod()).c(true).a(true).b(eventRequestPayload).c();
        if (this.i != null && this.o != null) {
            this.i.a(dVarC, this, this.o);
        }
        LogCat.d(n, "action mEventNameList:" + this.p + " devId:" + this.j.getDeviceName());
        return true;
    }
}
