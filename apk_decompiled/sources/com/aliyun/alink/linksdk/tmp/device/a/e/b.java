package com.aliyun.alink.linksdk.tmp.device.a.e;

import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.connect.a.f;
import com.aliyun.alink.linksdk.tmp.connect.e;
import com.aliyun.alink.linksdk.tmp.device.a.d;
import com.aliyun.alink.linksdk.tmp.device.payload.CommonResponsePayload;
import com.aliyun.alink.linksdk.tmp.device.payload.cloud.EncryptGroupAuthInfo;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.ALog;
import com.google.gson.reflect.TypeToken;

/* JADX INFO: compiled from: GroupAuthTask.java */
/* JADX INFO: loaded from: classes2.dex */
public class b extends d<b> implements com.aliyun.alink.linksdk.tmp.connect.c {
    protected static final String n = "[Tmp]GroupAuthTask";
    protected String o;
    protected String p;
    protected String q;
    protected String r;
    protected String s;

    public b(com.aliyun.alink.linksdk.tmp.device.a aVar, DeviceBasicData deviceBasicData, IDevListener iDevListener) {
        super(aVar, iDevListener);
        a(aVar);
        a(deviceBasicData);
    }

    public b a(String str) {
        this.p = str;
        return this;
    }

    public b a(String str, EncryptGroupAuthInfo encryptGroupAuthInfo) {
        this.q = str;
        if (encryptGroupAuthInfo != null) {
            if (TmpConstant.GROUP_ROLE_DEVICE.equalsIgnoreCase(this.q)) {
                this.r = encryptGroupAuthInfo.encryptGroupKeyPrefix;
                this.s = encryptGroupAuthInfo.encryptGroupSecret;
            } else if (TmpConstant.GROUP_ROLE_CONTROLLER.equalsIgnoreCase(this.q)) {
                this.r = encryptGroupAuthInfo.encryptAccessKey;
                this.s = encryptGroupAuthInfo.encryptAccessToken;
            }
        }
        return this;
    }

    public b b(String str) {
        this.o = str;
        return this;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.c
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, e eVar) {
        CommonResponsePayload commonResponsePayload;
        if (eVar != null && eVar.b() && (commonResponsePayload = (CommonResponsePayload) GsonUtils.fromJson(eVar.e(), new TypeToken<CommonResponsePayload>() { // from class: com.aliyun.alink.linksdk.tmp.device.a.e.b.1
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
        ALog.d(n, "dealGroupAUthInfo groupId:" + this.o + " groupAuthType:" + this.q + " mOp:" + this.p + " mParam1:" + this.r + " mParam2" + this.s);
        this.i.a(f.a(this.j.getProductKey(), this.j.getDeviceName()).a(this.j.getAddr()).a(this.j.getPort()).g(this.o).e(this.p).f(this.q).h(this.r).i(this.s).a(true).c(), this);
        return true;
    }
}
