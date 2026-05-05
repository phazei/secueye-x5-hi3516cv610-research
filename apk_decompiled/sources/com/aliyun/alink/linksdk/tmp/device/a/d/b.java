package com.aliyun.alink.linksdk.tmp.device.a.d;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.config.DeviceConfig;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.storage.TmpStorage;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: CreateConnectTask.java */
/* JADX INFO: loaded from: classes2.dex */
public class b extends com.aliyun.alink.linksdk.tmp.device.a.d<b> implements IDevListener {
    protected static final String p = "[Tmp]CreateConnectTask";
    protected String q;

    public b(com.aliyun.alink.linksdk.tmp.device.a aVar, DeviceBasicData deviceBasicData, DeviceConfig deviceConfig, IDevListener iDevListener) {
        super(aVar, iDevListener);
        a(aVar);
        a(deviceBasicData);
        a(deviceConfig);
    }

    protected void c() {
        if (this.i == null) {
            this.q = com.aliyun.alink.linksdk.tmp.connect.a.a(this.j, this.m, this);
            com.aliyun.alink.linksdk.tmp.device.a aVar = this.h;
            ALog.d(p, "create connect connectId:" + this.q);
            if (aVar == null || TextUtils.isEmpty(this.q)) {
                return;
            }
            aVar.a(com.aliyun.alink.linksdk.tmp.connect.a.a(this.q, this.j == null ? null : this.j.productKey, this.j != null ? this.j.deviceName : null));
            return;
        }
        onSuccess(null, null);
    }

    public void a(String str, String str2) {
        com.aliyun.alink.linksdk.tmp.device.a aVar = this.h;
        if (aVar != null) {
            aVar.a(str, str2);
        }
    }

    public void onSuccess(Object obj, OutputParams outputParams) {
        ALog.d(p, "onSuccess returnValue:" + outputParams + " this :" + this + " mConnectId:" + this.q);
        com.aliyun.alink.linksdk.tmp.device.a aVar = this.h;
        if (aVar != null) {
            if (!TextUtils.isEmpty(this.q)) {
                ALog.d(p, "onSuccess mConnectId:" + this.q);
            } else if (TextUtils.isEmpty(this.q) && outputParams != null) {
                this.q = String.valueOf(outputParams.get(com.aliyun.alink.linksdk.tmp.connect.a.f4238b).getValue());
            } else {
                onFail(null, new ErrorInfo(300, "param is invalid"));
                ALog.e(p, "create connect fail");
                return;
            }
            ALog.d(p, "create connect connectId:" + this.q);
            aVar.a(com.aliyun.alink.linksdk.tmp.connect.a.a(this.q, this.j == null ? null : this.j.productKey, this.j == null ? null : this.j.deviceName));
        }
        a((Object) null, (Object) null);
    }

    public void onFail(Object obj, ErrorInfo errorInfo) {
        ALog.d(p, "onFail errorInfo:" + errorInfo);
        if (errorInfo != null && (errorInfo.getErrorCode() == 502 || errorInfo.getErrorCode() == 506 || errorInfo.getErrorCode() == 501)) {
            ALog.d(p, "onFail AUTH_ACCESS_TOKEN_INVALID clear storage");
            if (TextUtils.isEmpty(this.m.getBasicData().getDevId())) {
                TmpStorage.DeviceInfo deviceInfo = TmpStorage.getInstance().getDeviceInfo(this.m.getBasicData().getIotId());
                if (deviceInfo != null) {
                    TmpStorage.getInstance().saveAccessInfo(deviceInfo.getId(), "", "");
                }
            } else {
                TmpStorage.getInstance().saveAccessInfo(this.m.getBasicData().getDevId(), "", "");
            }
        }
        b((Object) null, errorInfo);
    }
}
