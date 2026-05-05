package com.aliyun.alink.linksdk.tmp.device.a.c;

import com.aliyun.alink.linksdk.tmp.connect.d;
import com.aliyun.alink.linksdk.tmp.data.device.Option;
import com.aliyun.alink.linksdk.tmp.data.ut.ExtraData;
import com.aliyun.alink.linksdk.tmp.device.a.e;
import com.aliyun.alink.linksdk.tmp.device.payload.KeyValuePair;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.device.payload.service.ServiceRequestPayload;
import com.aliyun.alink.linksdk.tmp.device.payload.service.ServiceResponsePayload;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tmp.utils.TextHelper;
import com.aliyun.alink.linksdk.tools.ALog;
import com.google.gson.reflect.TypeToken;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: compiled from: InvokeGroupService.java */
/* JADX INFO: loaded from: classes2.dex */
public class c extends e<c> implements com.aliyun.alink.linksdk.tmp.connect.c {
    private static final String i = "[Tmp]InvokeGroupService";
    protected String f;
    protected List<KeyValuePair> g;
    protected Option h;

    public c(com.aliyun.alink.linksdk.tmp.device.b bVar, IDevListener iDevListener) {
        super(bVar, iDevListener);
    }

    public c a(String str) {
        this.f = str;
        return this;
    }

    public c a(List<KeyValuePair> list) {
        this.g = list;
        return this;
    }

    public c a(ExtraData extraData) {
        if (extraData != null) {
            this.h = extraData.option;
        }
        return this;
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.a
    public boolean a() {
        HashMap map = new HashMap();
        List<KeyValuePair> list = this.g;
        if (list != null && !list.isEmpty()) {
            for (KeyValuePair keyValuePair : this.g) {
                map.put(keyValuePair.getKey(), keyValuePair.getValueWrapper());
            }
        }
        com.aliyun.alink.linksdk.tmp.device.b bVar = this.f4360c.get();
        if (bVar == null) {
            ALog.e(i, "groupImpl null");
            b((Object) null, new ErrorInfo(300, "param is invalid"));
            return false;
        }
        com.aliyun.alink.linksdk.tmp.connect.b.b bVarB = bVar.b();
        if (bVarB == null) {
            ALog.e(i, "groupConnect null");
            b((Object) null, new ErrorInfo(300, "param is invalid"));
            return false;
        }
        ServiceRequestPayload serviceRequestPayload = new ServiceRequestPayload(null, null);
        serviceRequestPayload.setMethod(TextHelper.foramtMethod(this.f));
        serviceRequestPayload.setParams((Map<String, ValueWrapper>) map);
        bVarB.a(com.aliyun.alink.linksdk.tmp.connect.a.a.b.a(this.f, bVar.a().localGroupId).b(bVar.a().accessInfo.mAccessKey, bVar.a().accessInfo.mAccessToken).a(bVar.a().localGroupDeviceData).b(serviceRequestPayload).c(), this);
        ALog.d(i, "InvokeGroupService mServiceIdentifier:" + this.f + " mServiceArgs:" + this.g);
        return false;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.c
    public void a(d dVar, com.aliyun.alink.linksdk.tmp.connect.e eVar) {
        ServiceResponsePayload serviceResponsePayload;
        ALog.e(i, "onLoad response:" + eVar);
        if (eVar != null && eVar.b() && (serviceResponsePayload = (ServiceResponsePayload) GsonUtils.fromJson(eVar.e(), new TypeToken<ServiceResponsePayload>() { // from class: com.aliyun.alink.linksdk.tmp.device.a.c.c.1
        }.getType())) != null && serviceResponsePayload.getCode() == 200) {
            a(dVar, eVar);
            return;
        }
        ALog.e(i, "onLoad error response:" + eVar);
        b(dVar, new ErrorInfo(300, "response error"));
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.c
    public void a(d dVar, ErrorInfo errorInfo) {
        ALog.e(i, "onError errorInfo:" + errorInfo);
        b((Object) null, errorInfo);
    }
}
