package com.aliyun.alink.linksdk.tmp.device.a.i;

import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.connect.a.n;
import com.aliyun.alink.linksdk.tmp.connect.c;
import com.aliyun.alink.linksdk.tmp.connect.e;
import com.aliyun.alink.linksdk.tmp.data.auth.AccessInfo;
import com.aliyun.alink.linksdk.tmp.data.auth.SetupData;
import com.aliyun.alink.linksdk.tmp.device.a.d;
import com.aliyun.alink.linksdk.tmp.device.payload.setup.SetupRequestPayload;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.storage.TmpStorage;
import com.aliyun.alink.linksdk.tmp.utils.AuthInfoCreater;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tools.ALog;
import com.google.gson.reflect.TypeToken;

/* JADX INFO: compiled from: SetupTask.java */
/* JADX INFO: loaded from: classes2.dex */
public class b extends d<com.aliyun.alink.linksdk.tmp.device.a.h.a> implements c {
    private static final String o = "[Tmp]SetupTask";
    protected SetupData n;

    public b(Object obj, DeviceBasicData deviceBasicData, com.aliyun.alink.linksdk.tmp.device.a aVar, IDevListener iDevListener) {
        super(aVar, iDevListener);
        a(obj);
        a(deviceBasicData);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.aliyun.alink.linksdk.tmp.connect.c
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, e eVar) {
        if (eVar != null && eVar.b()) {
            ALog.d(o, "SetupTask onLoad taskSuccess");
            a(dVar, eVar);
        } else {
            ALog.d(o, "SetupTask onLoad onError");
            a(dVar, new ErrorInfo(300, "errror"));
        }
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.c
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, ErrorInfo errorInfo) {
        ALog.d(o, "SetupTask onError");
        b(dVar, errorInfo);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.a
    /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, e eVar) {
        SetupData setupData = this.n;
        if (setupData != null && "ServerAuthInfo".equalsIgnoreCase(setupData.configType)) {
            ALog.d(o, "setup success change provisioner key");
            for (int i = 0; i < this.n.configValue.size(); i++) {
                AccessInfo accessInfoCreateAccessInfo = AuthInfoCreater.getInstance().createAccessInfo(this.n.configValue.get(i).authCode, this.n.configValue.get(i).authSecret, "2");
                TmpStorage.getInstance().saveAccessInfo(this.j.getDevId(), accessInfoCreateAccessInfo.mAccessKey, accessInfoCreateAccessInfo.mAccessToken, true, TmpStorage.FLAG_PROVISIONER);
            }
        }
        super.a(dVar, eVar);
    }

    public void b(Object obj) {
        this.n = (SetupData) GsonUtils.fromJson(String.valueOf(obj), new TypeToken<SetupData>() { // from class: com.aliyun.alink.linksdk.tmp.device.a.i.b.1
        }.getType());
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d, com.aliyun.alink.linksdk.tmp.device.a.a
    public boolean a() {
        super.a();
        SetupRequestPayload setupRequestPayload = new SetupRequestPayload(this.j.getProductKey(), this.j.getDeviceName());
        setupRequestPayload.setParams(this.n);
        boolean zA = this.i.a(n.a(this.j.getProductKey(), this.j.getDeviceName()).a(this.j.getAddr()).a(this.j.getPort()).a(this.e).k(this.j.getProductKey()).l(this.j.getDeviceName()).a(true).b(setupRequestPayload).c(), this);
        ALog.d(o, "SetupTask action bRet：" + zA + " ConfigData:" + this.n.toString());
        return zA;
    }
}
