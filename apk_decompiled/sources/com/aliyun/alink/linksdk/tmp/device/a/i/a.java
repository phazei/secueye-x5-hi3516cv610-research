package com.aliyun.alink.linksdk.tmp.device.a.i;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.cmp.api.ConnectSDK;
import com.aliyun.alink.linksdk.cmp.connect.channel.PersistentConnectInfo;
import com.aliyun.alink.linksdk.cmp.core.base.AConnectInfo;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.config.DefaultProvisionConfig;
import com.aliyun.alink.linksdk.tmp.config.DeviceConfig;
import com.aliyun.alink.linksdk.tmp.connect.d;
import com.aliyun.alink.linksdk.tmp.connect.e;
import com.aliyun.alink.linksdk.tmp.data.auth.AuthPairData;
import com.aliyun.alink.linksdk.tmp.data.auth.ProvisionAuthData;
import com.aliyun.alink.linksdk.tmp.data.auth.SetupData;
import com.aliyun.alink.linksdk.tmp.device.payload.cloud.PrefixGetPayload;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.utils.AuthInfoCreater;
import com.aliyun.alink.linksdk.tmp.utils.CloudUtils;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import com.google.gson.reflect.TypeToken;

/* JADX INFO: compiled from: ChgPrvsRcerAuthTask.java */
/* JADX INFO: loaded from: classes2.dex */
public class a extends b {
    private static final String o = "[Tmp]ChgPrvsRcerAuthTask";
    private AuthPairData p;
    private DefaultProvisionConfig q;

    public a(com.aliyun.alink.linksdk.tmp.device.a aVar, Object obj, DeviceBasicData deviceBasicData, DeviceConfig deviceConfig, IDevListener iDevListener) {
        super(obj, deviceBasicData, aVar, iDevListener);
        a(aVar);
        if (DeviceConfig.DeviceType.PROVISION == deviceConfig.getDeviceType()) {
            this.q = (DefaultProvisionConfig) deviceConfig;
        }
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.i.b, com.aliyun.alink.linksdk.tmp.device.a.d, com.aliyun.alink.linksdk.tmp.device.a.a
    public boolean a() {
        ALog.d(o, "ChgPrvsRcerAuthTask action");
        DefaultProvisionConfig defaultProvisionConfig = this.q;
        if (defaultProvisionConfig == null || !defaultProvisionConfig.IsAutoChangeAuth()) {
            ALog.d(o, "action config not IsAutoChangeAuth or not default");
            a((d) null, (e) null);
            return true;
        }
        if (DefaultProvisionConfig.AuthChangeType.LocalAuth == this.q.getAuthChangeType()) {
            this.p = AuthInfoCreater.getInstance().createAuthInfo(this.j.getProductKey(), this.j.getDeviceName(), "0");
            if (this.p == null) {
                ALog.d(o, "action mAuthInfo null");
                a((d) null, (e) null);
                return true;
            }
            return b();
        }
        if (DefaultProvisionConfig.AuthChangeType.CloudAuth == this.q.getAuthChangeType()) {
            CloudUtils.registerPersistentConnect(null, null, null, null);
            AConnectInfo connectInfo = ConnectSDK.getInstance().getConnectInfo(ConnectSDK.getInstance().getPersistentConnectId());
            if (connectInfo == null || !(connectInfo instanceof PersistentConnectInfo)) {
                ALog.e(o, "PersistentConnectInfo empty");
                b((Object) null, new ErrorInfo(301, "param is invalid"));
                return false;
            }
            PersistentConnectInfo persistentConnectInfo = (PersistentConnectInfo) connectInfo;
            CloudUtils.queryPrefixSecret(persistentConnectInfo.productKey, persistentConnectInfo.deviceName, this.q.getBasicData().getProductKey(), this.q.getBasicData().getDeviceName(), new IConnectSendListener() { // from class: com.aliyun.alink.linksdk.tmp.device.a.i.a.1
                @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
                public void onResponse(ARequest aRequest, AResponse aResponse) {
                    ALog.d(a.o, "registerPersistentConnect queryPrefixSecret send onResponse:" + aResponse);
                    if (aResponse == null || aResponse.data == null) {
                        a.this.b((Object) null, new ErrorInfo(300, "param is invalid"));
                        return;
                    }
                    PrefixGetPayload prefixGetPayload = (PrefixGetPayload) GsonUtils.fromJson(aResponse.data.toString(), new TypeToken<PrefixGetPayload>() { // from class: com.aliyun.alink.linksdk.tmp.device.a.i.a.1.1
                    }.getType());
                    if (prefixGetPayload == null || prefixGetPayload.data == null || TextUtils.isEmpty(prefixGetPayload.data.prefix) || TextUtils.isEmpty(prefixGetPayload.data.deviceSecret)) {
                        a.this.b((Object) null, new ErrorInfo(300, "param is invalid"));
                        return;
                    }
                    a.this.p = new AuthPairData(null, null, prefixGetPayload.data.prefix, prefixGetPayload.data.deviceSecret);
                    a.this.b();
                }

                @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
                public void onFailure(ARequest aRequest, AError aError) {
                    ALog.e(a.o, "queryPrefx onResponse  error:" + aError);
                    a.this.b((Object) null, aError == null ? null : new ErrorInfo(aError.getCode(), aError.getMsg()));
                }
            });
        } else {
            ALog.e(o, "ChgPrvsRcerAuthTask action getAuthChangeType error :" + this.q.getAuthChangeType());
            a((d) null, (e) null);
        }
        return true;
    }

    public boolean b() {
        this.n = new SetupData();
        this.n.configType = "ServerAuthInfo";
        ProvisionAuthData provisionAuthData = new ProvisionAuthData();
        provisionAuthData.authCode = this.p.authCode;
        provisionAuthData.authSecret = this.p.authSecret;
        provisionAuthData.productKey = this.j.getProductKey();
        provisionAuthData.deviceName = this.j.getDeviceName();
        this.n.configValue.add(provisionAuthData);
        return super.a();
    }
}
