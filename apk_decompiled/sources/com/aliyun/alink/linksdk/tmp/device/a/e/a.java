package com.aliyun.alink.linksdk.tmp.device.a.e;

import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.connect.e;
import com.aliyun.alink.linksdk.tmp.device.a.d;
import com.aliyun.alink.linksdk.tmp.device.payload.CommonResponsePayload;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;
import java.util.List;

/* JADX INFO: compiled from: DeleteAuthUserTask.java */
/* JADX INFO: loaded from: classes2.dex */
public class a extends d<a> implements com.aliyun.alink.linksdk.tmp.connect.c {
    protected static final String n = "[Tmp]DeleteAuthUserTask";
    protected List<String> o;

    public a(com.aliyun.alink.linksdk.tmp.device.a aVar, IDevListener iDevListener, DeviceBasicData deviceBasicData) {
        super(aVar, iDevListener);
        a(deviceBasicData);
    }

    public a a(List<String> list) {
        this.o = list;
        return this;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.c
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, e eVar) {
        CommonResponsePayload commonResponsePayload;
        if (eVar != null && eVar.b() && (commonResponsePayload = (CommonResponsePayload) GsonUtils.fromJson(eVar.e(), new TypeToken<CommonResponsePayload>() { // from class: com.aliyun.alink.linksdk.tmp.device.a.e.a.1
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

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d, com.aliyun.alink.linksdk.tmp.device.a.a
    public boolean a() {
        super.a();
        this.i.a(com.aliyun.alink.linksdk.tmp.connect.a.b.a(this.j.getProductKey(), this.j.getDeviceName()).a(this.o).a(true).c(), this);
        return true;
    }
}
