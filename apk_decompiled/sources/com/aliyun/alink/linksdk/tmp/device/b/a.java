package com.aliyun.alink.linksdk.tmp.device.b;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.api.DeviceManager;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.config.DefaultClientConfig;
import com.aliyun.alink.linksdk.tmp.data.auth.AccessInfo;
import com.aliyun.alink.linksdk.tmp.device.a.c;
import com.aliyun.alink.linksdk.tmp.device.a.d.g;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.storage.TmpStorage;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: ALCSAutoConnector.java */
/* JADX INFO: loaded from: classes2.dex */
public class a {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final String f4379b = "[Tmp]ALCSAutoConnector";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected DeviceBasicData f4380a;

    public a(DeviceBasicData deviceBasicData) {
        this.f4380a = deviceBasicData;
    }

    public void a() {
        ALog.d(f4379b, "startConnect mBasicData:" + this.f4380a);
        if (!"1".equalsIgnoreCase(this.f4380a.getModelType())) {
            ALog.d(f4379b, "startConnect not MODEL_TYPE_ALI_WIFI  auto connect return");
            return;
        }
        this.f4380a.setLocal(true);
        DeviceBasicData deviceBasicData = DeviceManager.getInstance().getDeviceBasicData(this.f4380a.getDevId());
        if (deviceBasicData == null) {
            ALog.w(f4379b, "startConnect local not found");
            return;
        }
        this.f4380a.setPort(deviceBasicData.getPort());
        this.f4380a.setAddr(deviceBasicData.getAddr());
        if (TextUtils.isEmpty(this.f4380a.getIotId())) {
            this.f4380a.setIotId(TmpStorage.getInstance().getIotId(this.f4380a.getProductKey(), this.f4380a.getDeviceName()));
        }
        AccessInfo accessInfo = TmpStorage.getInstance().getAccessInfo(this.f4380a.getDevId());
        c cVar = new c();
        DefaultClientConfig defaultClientConfig = new DefaultClientConfig(this.f4380a);
        if (accessInfo != null) {
            defaultClientConfig.mAccessKey = accessInfo.mAccessKey;
            defaultClientConfig.mAccessToken = accessInfo.mAccessToken;
        }
        IDevListener iDevListener = new IDevListener() { // from class: com.aliyun.alink.linksdk.tmp.device.b.a.1
            @Override // com.aliyun.alink.linksdk.tmp.listener.IDevListener
            public void onSuccess(Object obj, OutputParams outputParams) {
                ALog.d(a.f4379b, "onSuccess returnValue:" + outputParams);
            }

            @Override // com.aliyun.alink.linksdk.tmp.listener.IDevListener
            public void onFail(Object obj, ErrorInfo errorInfo) {
                ALog.e(a.f4379b, "onFail errorInfo:" + errorInfo + " mBasicData:" + a.this.f4380a + " errorInfo:" + errorInfo);
            }
        };
        cVar.b(new g(null, this.f4380a, defaultClientConfig, iDevListener).a((Object) null)).b(new com.aliyun.alink.linksdk.tmp.device.a.d.a(null, this.f4380a, defaultClientConfig, iDevListener).a((Object) null)).a();
    }
}
