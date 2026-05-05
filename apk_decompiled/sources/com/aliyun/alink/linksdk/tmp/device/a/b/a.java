package com.aliyun.alink.linksdk.tmp.device.a.b;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.connect.CommonRequestBuilder;
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

/* JADX INFO: compiled from: CancelSubEventTask.java */
/* JADX INFO: loaded from: classes2.dex */
public class a extends d<a> implements com.aliyun.alink.linksdk.tmp.connect.c {
    protected String n;
    protected INotifyHandler o;

    public a(com.aliyun.alink.linksdk.tmp.device.a aVar, DeviceBasicData deviceBasicData, IDevListener iDevListener) {
        super(aVar, iDevListener);
        a(deviceBasicData);
        a(aVar);
    }

    public a a(String str) {
        this.n = str;
        return this;
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d, com.aliyun.alink.linksdk.tmp.device.a.a
    public boolean a() {
        com.aliyun.alink.linksdk.tmp.device.a aVar;
        super.a();
        EventRequestPayload eventRequestPayload = new EventRequestPayload(this.j.getProductKey(), this.j.getDeviceName());
        if (this.k == null && this.h != null && (aVar = this.h) != null) {
            this.k = aVar.d();
        }
        if (this.k != null) {
            eventRequestPayload.setMethod(this.k.getEventMethod(this.n));
        }
        com.aliyun.alink.linksdk.tmp.connect.d dVarC = com.aliyun.alink.linksdk.tmp.connect.a.a.d().a(this.j.getAddr()).a(this.j.getPort()).k(this.j.getProductKey()).l(this.j.getDeviceName()).a(this.e).a(true).c(false).m(eventRequestPayload.getMethod()).c(CommonRequestBuilder.a(this.j.getProductKey(), this.j.getDeviceName(), eventRequestPayload.getMethod(), "sys")).b(eventRequestPayload).c();
        if (this.i != null) {
            this.i.b(dVarC, this);
        }
        LogCat.d("[Tmp]DeviceAsyncTask", "action mEventNameList:" + this.n + " devId:" + this.j.getDeviceName());
        return true;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.aliyun.alink.linksdk.tmp.connect.c
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, e eVar) {
        if (eVar != null && eVar.b() && ((CommonResponsePayload) GsonUtils.fromJson(eVar.e(), new TypeToken<CommonResponsePayload>() { // from class: com.aliyun.alink.linksdk.tmp.device.a.b.a.1
        }.getType())).getCode() == 200) {
            LogCat.d("[Tmp]DeviceAsyncTask", "onLoad normal success");
            a(dVar, eVar);
        } else {
            LogCat.d("[Tmp]DeviceAsyncTask", "onLoad normal error");
            b(dVar, new ErrorInfo(300, "response error"));
        }
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.c
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, ErrorInfo errorInfo) {
        b(dVar, errorInfo);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.a
    /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, e eVar) {
        super.a(dVar, eVar);
    }

    protected boolean b() {
        com.aliyun.alink.linksdk.tmp.device.a aVar = this.h;
        if (aVar == null || TextUtils.isEmpty(this.n)) {
            return false;
        }
        aVar.c(this.n);
        return true;
    }
}
