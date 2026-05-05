package com.aliyun.alink.linksdk.tmp.device.a.h;

import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.connect.a.l;
import com.aliyun.alink.linksdk.tmp.connect.c;
import com.aliyun.alink.linksdk.tmp.connect.e;
import com.aliyun.alink.linksdk.tmp.data.device.Option;
import com.aliyun.alink.linksdk.tmp.data.ut.ExtraData;
import com.aliyun.alink.linksdk.tmp.device.a.d;
import com.aliyun.alink.linksdk.tmp.device.payload.KeyValuePair;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.device.payload.service.ServiceRequestPayload;
import com.aliyun.alink.linksdk.tmp.device.payload.service.ServiceResponsePayload;
import com.aliyun.alink.linksdk.tmp.devicemodel.DeviceModel;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tmp.utils.LogCat;
import com.aliyun.alink.linksdk.tmp.utils.TextHelper;
import com.aliyun.alink.linksdk.tools.ALog;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: compiled from: InvokeServiceTask.java */
/* JADX INFO: loaded from: classes2.dex */
public class a extends d<a> implements c {
    protected static final String n = "[Tmp]InvokeServiceTask";
    protected String o;
    protected List<KeyValuePair> p;
    protected Option q;

    public a(com.aliyun.alink.linksdk.tmp.device.a aVar, DeviceBasicData deviceBasicData, IDevListener iDevListener) {
        super(aVar, iDevListener);
        a(deviceBasicData);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d
    /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
    public a a(DeviceModel deviceModel) {
        this.k = deviceModel;
        return this;
    }

    public a a(String str) {
        this.o = str;
        return this;
    }

    public a a(List<KeyValuePair> list) {
        this.p = list;
        return this;
    }

    public a a(ExtraData extraData) {
        if (extraData != null) {
            this.q = extraData.option;
        }
        return this;
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d, com.aliyun.alink.linksdk.tmp.device.a.a
    public boolean a() {
        super.a();
        HashMap map = new HashMap();
        List<KeyValuePair> list = this.p;
        if (list != null && !list.isEmpty()) {
            for (KeyValuePair keyValuePair : this.p) {
                map.put(keyValuePair.getKey(), keyValuePair.getValueWrapper());
            }
        }
        if (this.j == null || this.k == null || this.i == null) {
            ALog.e(n, "mDeviceBasicData or mDeviceModel or mConnect null");
            b((Object) null, new ErrorInfo(300, "param is invalid"));
            return false;
        }
        ServiceRequestPayload serviceRequestPayload = new ServiceRequestPayload(this.j.getProductKey(), this.j.getDeviceName());
        serviceRequestPayload.setMethod(TextHelper.foramtMethod(this.o));
        serviceRequestPayload.setParams((Map<String, ValueWrapper>) map);
        this.i.a(l.d().a(this.j.getAddr()).a(this.j.getPort()).a(this.e).k(this.k.getProfile().getProdKey()).l(this.k.getProfile().getName()).m(TextHelper.foramtMethod(this.o)).a(true).a(this.q).b(serviceRequestPayload).c(), this);
        LogCat.d(n, "mServiceIdentifier:" + this.o + " mServiceArgs:" + this.p);
        return true;
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d, com.aliyun.alink.linksdk.tmp.device.a.a
    /* JADX INFO: renamed from: b */
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, e eVar, ErrorInfo errorInfo) {
        if (this.f == null) {
            LogCat.e(n, "onFlowComplete handler empty error");
        } else if (dVar != null) {
            this.f.onSuccess(dVar.b(), new OutputParams(((ServiceResponsePayload) GsonUtils.fromJson(eVar.e(), new TypeToken<ServiceResponsePayload>() { // from class: com.aliyun.alink.linksdk.tmp.device.a.h.a.1
            }.getType())).getData()));
        } else {
            super.a(dVar, eVar, errorInfo);
        }
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.c
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, e eVar) {
        ServiceResponsePayload serviceResponsePayload;
        if (eVar != null && eVar.b() && (serviceResponsePayload = (ServiceResponsePayload) GsonUtils.fromJson(eVar.e(), new TypeToken<ServiceResponsePayload>() { // from class: com.aliyun.alink.linksdk.tmp.device.a.h.a.2
        }.getType())) != null && serviceResponsePayload.getCode() == 200) {
            a(dVar, eVar);
            return;
        }
        LogCat.e(n, "onLoad error response:" + eVar);
        b(dVar, new ErrorInfo(300, "response error"));
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.c
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, ErrorInfo errorInfo) {
        b(dVar, errorInfo);
    }
}
