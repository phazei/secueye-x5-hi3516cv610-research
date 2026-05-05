package com.aliyun.alink.linksdk.tmp.device.a.d;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.cmp.api.ConnectSDK;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.config.DefaultServerConfig;
import com.aliyun.alink.linksdk.tmp.config.DeviceConfig;
import com.aliyun.alink.linksdk.tmp.data.auth.ServerEncryptInfo;
import com.aliyun.alink.linksdk.tmp.device.payload.cloud.PrefixGetPayload;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.storage.TmpStorage;
import com.aliyun.alink.linksdk.tmp.utils.CloudUtils;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tmp.utils.TextHelper;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import com.google.gson.reflect.TypeToken;

/* JADX INFO: compiled from: CreateServerConnectTask.java */
/* JADX INFO: loaded from: classes2.dex */
public class f extends b {
    protected String n;

    public f(com.aliyun.alink.linksdk.tmp.device.a aVar, DeviceBasicData deviceBasicData, DeviceConfig deviceConfig, IDevListener iDevListener) {
        super(aVar, deviceBasicData, deviceConfig, iDevListener);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d, com.aliyun.alink.linksdk.tmp.device.a.a
    public boolean a() {
        DefaultServerConfig defaultServerConfig = (DefaultServerConfig) this.m;
        if (defaultServerConfig == null) {
            b((Object) null, (ErrorInfo) null);
            return true;
        }
        ALog.d("[Tmp]CreateConnectTask", "getConnectType:" + defaultServerConfig.getConnectType());
        if (defaultServerConfig.getConnectType() == DefaultServerConfig.ConnectType.COAP) {
            if (!TextUtils.isEmpty(((DefaultServerConfig) this.m).mIotProductKey) && ((TextUtils.isEmpty(((DefaultServerConfig) this.m).getPrefix()) || TextUtils.isEmpty(((DefaultServerConfig) this.m).getSecret())) && !TextUtils.isEmpty(((DefaultServerConfig) this.m).mIotDeviceName))) {
                b();
            } else {
                onFail(this.e, null);
            }
        } else if (defaultServerConfig.getConnectType() == DefaultServerConfig.ConnectType.MQTT) {
            this.q = ConnectSDK.getInstance().getPersistentConnectId();
            onSuccess(this.e, null);
        } else if (defaultServerConfig.getConnectType() == DefaultServerConfig.ConnectType.COAP_AND_MQTT) {
            this.q = ConnectSDK.getInstance().getPersistentConnectId();
            if (!TextUtils.isEmpty(((DefaultServerConfig) this.m).mIotProductKey) && ((TextUtils.isEmpty(((DefaultServerConfig) this.m).getPrefix()) || TextUtils.isEmpty(((DefaultServerConfig) this.m).getSecret())) && !TextUtils.isEmpty(((DefaultServerConfig) this.m).mIotDeviceName))) {
                b();
            } else {
                onFail(this.e, null);
            }
        } else {
            c();
        }
        return true;
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d.b
    protected void c() {
        if (this.i == null) {
            ALog.d("[Tmp]CreateConnectTask", "createConnect this :" + this);
            if (TextUtils.isEmpty(this.q)) {
                this.q = com.aliyun.alink.linksdk.tmp.connect.a.a(this.j, this.m, this);
                return;
            } else {
                this.n = com.aliyun.alink.linksdk.tmp.connect.a.a(this.j, this.m, this);
                return;
            }
        }
        onSuccess(null, null);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d.b, com.aliyun.alink.linksdk.tmp.listener.IDevListener
    public void onSuccess(Object obj, OutputParams outputParams) {
        ALog.d("[Tmp]CreateConnectTask", "onSuccess returnValue:" + outputParams + " this :" + this + " mConnectId:" + this.q);
        com.aliyun.alink.linksdk.tmp.device.a aVar = this.h;
        if (aVar != null) {
            if (outputParams != null) {
                if (TextUtils.isEmpty(this.q)) {
                    this.q = String.valueOf(outputParams.get(com.aliyun.alink.linksdk.tmp.connect.a.f4238b).getValue());
                } else {
                    this.n = String.valueOf(outputParams.get(com.aliyun.alink.linksdk.tmp.connect.a.f4238b).getValue());
                }
            } else if (TextUtils.isEmpty(this.q) && outputParams == null) {
                onFail(null, new ErrorInfo(300, "param is invalid"));
                ALog.e("[Tmp]CreateConnectTask", "create connect fail");
                return;
            }
            ALog.d("[Tmp]CreateConnectTask", "create connect connectId:" + this.q + " mSecondConnectId:" + this.n);
            aVar.a(com.aliyun.alink.linksdk.tmp.connect.a.a(this.q, this.n));
        }
        a((Object) null, (Object) null);
    }

    protected void b() {
        ALog.d("[Tmp]CreateConnectTask", "queryPrefx start");
        DefaultServerConfig defaultServerConfig = (DefaultServerConfig) this.m;
        final String strCombineStr = TextHelper.combineStr(defaultServerConfig.mIotProductKey, defaultServerConfig.mIotDeviceName);
        ServerEncryptInfo serverEnptInfo = TmpStorage.getInstance().getServerEnptInfo(strCombineStr);
        if (serverEnptInfo != null) {
            defaultServerConfig.setPrefix(serverEnptInfo.mPrefix);
            defaultServerConfig.setSecret(serverEnptInfo.mSecret);
            c();
            return;
        }
        CloudUtils.queryPrefixSecret(this.m.getBasicData().getProductKey(), this.m.getBasicData().getDeviceName(), new IConnectSendListener() { // from class: com.aliyun.alink.linksdk.tmp.device.a.d.f.1
            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onResponse(ARequest aRequest, AResponse aResponse) {
                ALog.d("[Tmp]CreateConnectTask", "registerPersistentConnect send onResponse:" + aResponse);
                if (aResponse != null && aResponse.data != null) {
                    PrefixGetPayload prefixGetPayload = (PrefixGetPayload) GsonUtils.fromJson(aResponse.data.toString(), new TypeToken<PrefixGetPayload>() { // from class: com.aliyun.alink.linksdk.tmp.device.a.d.f.1.1
                    }.getType());
                    if (prefixGetPayload != null && prefixGetPayload.data != null) {
                        DefaultServerConfig defaultServerConfig2 = (DefaultServerConfig) f.this.m;
                        defaultServerConfig2.setPrefix(prefixGetPayload.data.prefix);
                        defaultServerConfig2.setSecret(prefixGetPayload.data.deviceSecret);
                        TmpStorage.getInstance().saveServerEnptInfo(strCombineStr, prefixGetPayload.data.prefix, prefixGetPayload.data.deviceSecret);
                    }
                } else {
                    ALog.e("[Tmp]CreateConnectTask", "registerPersistentConnect send onResponse error");
                    ServerEncryptInfo serverEnptInfo2 = TmpStorage.getInstance().getServerEnptInfo(f.this.m.getDevId(), "local");
                    if (serverEnptInfo2 != null && !TextUtils.isEmpty(serverEnptInfo2.mPrefix) && !TextUtils.isEmpty(serverEnptInfo2.mSecret)) {
                        DefaultServerConfig defaultServerConfig3 = (DefaultServerConfig) f.this.m;
                        defaultServerConfig3.setPrefix(serverEnptInfo2.mPrefix);
                        defaultServerConfig3.setSecret(serverEnptInfo2.mSecret);
                    }
                }
                f.this.c();
            }

            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onFailure(ARequest aRequest, AError aError) {
                ALog.e("[Tmp]CreateConnectTask", "queryPrefx onResponse  error:" + aError);
                ServerEncryptInfo serverEnptInfo2 = TmpStorage.getInstance().getServerEnptInfo(f.this.m.getDevId(), "local");
                if (serverEnptInfo2 != null && !TextUtils.isEmpty(serverEnptInfo2.mPrefix) && !TextUtils.isEmpty(serverEnptInfo2.mSecret)) {
                    DefaultServerConfig defaultServerConfig2 = (DefaultServerConfig) f.this.m;
                    defaultServerConfig2.setPrefix(serverEnptInfo2.mPrefix);
                    defaultServerConfig2.setSecret(serverEnptInfo2.mSecret);
                }
                f.this.c();
            }
        });
    }
}
