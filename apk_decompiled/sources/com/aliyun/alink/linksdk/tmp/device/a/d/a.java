package com.aliyun.alink.linksdk.tmp.device.a.d;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tmp.TmpSdk;
import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.api.DeviceManager;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.component.cloud.ICloudProxyListener;
import com.aliyun.alink.linksdk.tmp.config.DefaultClientConfig;
import com.aliyun.alink.linksdk.tmp.config.DeviceConfig;
import com.aliyun.alink.linksdk.tmp.data.auth.AccessInfo;
import com.aliyun.alink.linksdk.tmp.device.panel.data.AccessInfoPayload;
import com.aliyun.alink.linksdk.tmp.device.panel.data.ProductInfoPayload;
import com.aliyun.alink.linksdk.tmp.device.request.GateWayRequest;
import com.aliyun.alink.linksdk.tmp.device.request.IGateWayRequestListener;
import com.aliyun.alink.linksdk.tmp.device.request.auth.GetComboAccessInfoRequest;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.service.DevService;
import com.aliyun.alink.linksdk.tmp.storage.TmpStorage;
import com.aliyun.alink.linksdk.tmp.utils.ErrorCode;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import com.google.gson.reflect.TypeToken;

/* JADX INFO: compiled from: CreateClientConnectTask.java */
/* JADX INFO: loaded from: classes2.dex */
public class a extends b {
    public final int n;
    protected Runnable o;

    public a(com.aliyun.alink.linksdk.tmp.device.a aVar, DeviceBasicData deviceBasicData, DeviceConfig deviceConfig, IDevListener iDevListener) {
        super(aVar, deviceBasicData, deviceConfig, iDevListener);
        this.n = com.taobao.accs.net.b.ACCS_RECEIVE_TIMEOUT;
        this.o = new Runnable() { // from class: com.aliyun.alink.linksdk.tmp.device.a.d.a.1
            @Override // java.lang.Runnable
            public void run() {
                ALog.e("[Tmp]CreateConnectTask", "CreateClientConnectTask time out");
                a aVar2 = a.this;
                aVar2.onFail(aVar2.e, new ErrorInfo(ErrorCode.ERROR_CODE_TIMEOUT, "timeout"));
                com.aliyun.alink.linksdk.tmp.connect.a.a(a.this.q);
            }
        };
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d, com.aliyun.alink.linksdk.tmp.device.a.a
    public synchronized boolean a() {
        TmpSdk.mHandler.postDelayed(this.o, 40000L);
        ALog.d("[Tmp]CreateConnectTask", "action mDeviceBasicData:" + this.j + " mConfig:" + this.m);
        if (DevService.isDeviceWifiAndBleCombo(this.j.getSupportedNetType())) {
            final AccessInfo comboAccessInfo = TmpStorage.getInstance().getComboAccessInfo(this.m.getBasicData().getDevId());
            if (comboAccessInfo != null) {
                e();
            }
            GateWayRequest getComboAccessInfoRequest = new GetComboAccessInfoRequest(this.m.getBasicData().getProductKey(), this.m.getBasicData().getDeviceName(), null);
            getComboAccessInfoRequest.sendRequest(getComboAccessInfoRequest, new IGateWayRequestListener<GetComboAccessInfoRequest, GetComboAccessInfoRequest.GetComboAccessInfoResponse>() { // from class: com.aliyun.alink.linksdk.tmp.device.a.d.a.2
                /* JADX WARN: Multi-variable type inference failed */
                @Override // com.aliyun.alink.linksdk.tmp.device.request.IGateWayRequestListener
                /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
                public void onSuccess(GetComboAccessInfoRequest getComboAccessInfoRequest2, GetComboAccessInfoRequest.GetComboAccessInfoResponse getComboAccessInfoResponse) {
                    if (getComboAccessInfoResponse != null && getComboAccessInfoResponse.data != 0 && ((GetComboAccessInfoRequest.GetComboAccessInfoData) getComboAccessInfoResponse.data).accessInfo != null) {
                        TmpStorage.getInstance().saveComboAccessInfo(a.this.m.getBasicData().getDevId(), ((GetComboAccessInfoRequest.GetComboAccessInfoData) getComboAccessInfoResponse.data).accessInfo.accessKey, ((GetComboAccessInfoRequest.GetComboAccessInfoData) getComboAccessInfoResponse.data).accessInfo.accessToken);
                    }
                    if (getComboAccessInfoResponse != null && getComboAccessInfoResponse.data != 0 && ((GetComboAccessInfoRequest.GetComboAccessInfoData) getComboAccessInfoResponse.data).syncAccessInfo != null) {
                        TmpStorage.getInstance().saveSyncAccessInfo(a.this.m.getBasicData().getDevId(), ((GetComboAccessInfoRequest.GetComboAccessInfoData) getComboAccessInfoResponse.data).syncAccessInfo.accessKey, ((GetComboAccessInfoRequest.GetComboAccessInfoData) getComboAccessInfoResponse.data).syncAccessInfo.accessToken, ((GetComboAccessInfoRequest.GetComboAccessInfoData) getComboAccessInfoResponse.data).syncAccessInfo.authCode);
                    }
                    if (comboAccessInfo == null) {
                        a.this.e();
                    }
                }

                @Override // com.aliyun.alink.linksdk.tmp.device.request.IGateWayRequestListener
                /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
                public void onFail(GetComboAccessInfoRequest getComboAccessInfoRequest2, AError aError) {
                    ALog.e("[Tmp]CreateConnectTask", "GetComboAccessInfoRequest onFail:" + aError);
                    if (comboAccessInfo == null) {
                        a.this.e();
                    }
                }
            });
        } else {
            e();
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        DefaultClientConfig defaultClientConfig = (DefaultClientConfig) this.m;
        if (!TextUtils.isEmpty(this.m.getBasicData().getIotId()) && (TextUtils.isEmpty(((DefaultClientConfig) this.m).getAccessKey()) || TextUtils.isEmpty(((DefaultClientConfig) this.m).getAccessToken()))) {
            d();
        } else if (!TextUtils.isEmpty(this.m.getBasicData().getIotId()) && TextUtils.isEmpty(defaultClientConfig.mDateFormat)) {
            b();
        } else {
            c();
        }
    }

    protected void b() {
        final DefaultClientConfig defaultClientConfig = (DefaultClientConfig) this.m;
        if (!TextUtils.isEmpty(defaultClientConfig.mDateFormat)) {
            c();
            return;
        }
        ProductInfoPayload.ProductInfo productInfo = TmpStorage.getInstance().getProductInfo(this.m.getBasicData().getIotId());
        if (productInfo != null && !TextUtils.isEmpty(productInfo.dataFormat)) {
            defaultClientConfig.mDateFormat = productInfo.dataFormat;
            c();
        } else {
            TmpSdk.getCloudProxy().queryProductInfo(this.m.getBasicData().getIotId(), new ICloudProxyListener() { // from class: com.aliyun.alink.linksdk.tmp.device.a.d.a.3
                @Override // com.aliyun.alink.linksdk.tmp.component.cloud.ICloudProxyListener
                public void onResponse(String str, Object obj) {
                    if (obj == null) {
                        ALog.e("[Tmp]CreateConnectTask", "queryProductInfo aResponse error null");
                        a.this.c();
                        return;
                    }
                    ProductInfoPayload productInfoPayload = (ProductInfoPayload) GsonUtils.fromJson(obj.toString(), new TypeToken<ProductInfoPayload>() { // from class: com.aliyun.alink.linksdk.tmp.device.a.d.a.3.1
                    }.getType());
                    if (productInfoPayload == null || productInfoPayload.data == null || TextUtils.isEmpty(productInfoPayload.data.dataFormat)) {
                        ALog.e("[Tmp]CreateConnectTask", "queryProductInfo payload error ");
                        a.this.c();
                        return;
                    }
                    defaultClientConfig.mDateFormat = productInfoPayload.data.dataFormat;
                    TmpStorage.getInstance().saveProductInfo(a.this.m.getBasicData().getIotId(), productInfoPayload.data);
                    ALog.d("[Tmp]CreateConnectTask", "queryProductInfo onResponse dataFormat:" + defaultClientConfig.mDateFormat + " payload:" + productInfoPayload);
                    a.this.c();
                }

                @Override // com.aliyun.alink.linksdk.tmp.component.cloud.ICloudProxyListener
                public void onFailure(String str, AError aError) {
                    a.this.c();
                }
            });
        }
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d.b, com.aliyun.alink.linksdk.tmp.listener.IDevListener
    public void onSuccess(Object obj, OutputParams outputParams) {
        TmpSdk.mHandler.removeCallbacks(this.o);
        super.onSuccess(obj, outputParams);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d.b, com.aliyun.alink.linksdk.tmp.listener.IDevListener
    public void onFail(Object obj, ErrorInfo errorInfo) {
        TmpSdk.mHandler.removeCallbacks(this.o);
        super.onFail(obj, errorInfo);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d.b
    protected void c() {
        DefaultClientConfig defaultClientConfig = (DefaultClientConfig) this.m;
        DeviceManager.getInstance().addDevIotId(defaultClientConfig.getDevId(), defaultClientConfig.getBasicData().getIotId());
        super.c();
    }

    protected void d() {
        AccessInfo accessInfo;
        ALog.d("[Tmp]CreateConnectTask", "queryAccessInfo start");
        if (TextUtils.isEmpty(this.m.getBasicData().getDevId())) {
            TmpStorage.getInstance().getDeviceInfo(this.m.getBasicData().getIotId());
            accessInfo = null;
        } else {
            accessInfo = TmpStorage.getInstance().getAccessInfo(this.m.getBasicData().getDevId());
            a(this.m.getBasicData().getProductKey(), this.m.getBasicData().getDeviceName());
        }
        if (accessInfo != null) {
            DefaultClientConfig defaultClientConfig = (DefaultClientConfig) this.m;
            defaultClientConfig.setAccessKey(accessInfo.mAccessKey);
            defaultClientConfig.setAccessToken(accessInfo.mAccessToken);
            b();
            return;
        }
        TmpSdk.getCloudProxy().queryAccessInfo(this.m.getBasicData().getIotId(), new ICloudProxyListener() { // from class: com.aliyun.alink.linksdk.tmp.device.a.d.a.4
            @Override // com.aliyun.alink.linksdk.tmp.component.cloud.ICloudProxyListener
            public void onResponse(String str, Object obj) {
                AccessInfoPayload accessInfoPayload = (AccessInfoPayload) GsonUtils.fromJson(obj.toString(), new TypeToken<AccessInfoPayload>() { // from class: com.aliyun.alink.linksdk.tmp.device.a.d.a.4.1
                }.getType());
                ALog.d("[Tmp]CreateConnectTask", "queryAccessInfo onResponse payload:" + accessInfoPayload);
                if (accessInfoPayload != null && accessInfoPayload.data != null && !accessInfoPayload.data.isEmpty()) {
                    AccessInfoPayload.AlcsDeviceInfo alcsDeviceInfo = accessInfoPayload.data.get(0);
                    DefaultClientConfig defaultClientConfig2 = (DefaultClientConfig) a.this.m;
                    defaultClientConfig2.setAccessKey(alcsDeviceInfo.accessKey);
                    defaultClientConfig2.setAccessToken(alcsDeviceInfo.accessToken);
                    a.this.a(alcsDeviceInfo.productKey, alcsDeviceInfo.deviceName);
                    TmpStorage.DeviceInfo deviceInfo = new TmpStorage.DeviceInfo(alcsDeviceInfo.productKey, alcsDeviceInfo.deviceName);
                    TmpStorage.getInstance().saveDeviceInfo(a.this.m.getBasicData().getIotId(), alcsDeviceInfo.productKey, alcsDeviceInfo.deviceName);
                    TmpStorage.getInstance().saveIotId(alcsDeviceInfo.productKey, alcsDeviceInfo.deviceName, a.this.m.getBasicData().getIotId());
                    TmpStorage.getInstance().saveAccessInfo(deviceInfo.getId(), alcsDeviceInfo.accessKey, alcsDeviceInfo.accessToken);
                } else {
                    ALog.e("[Tmp]CreateConnectTask", "queryAccessInfo onResponse payload null");
                    AccessInfo accessInfo2 = TmpStorage.getInstance().getAccessInfo(a.this.m.getBasicData().getDevId(), "local");
                    if (accessInfo2 != null && !TextUtils.isEmpty(accessInfo2.mAccessKey) && !TextUtils.isEmpty(accessInfo2.mAccessToken)) {
                        DefaultClientConfig defaultClientConfig3 = (DefaultClientConfig) a.this.m;
                        defaultClientConfig3.setAccessKey(accessInfo2.mAccessKey);
                        defaultClientConfig3.setAccessToken(accessInfo2.mAccessToken);
                    }
                }
                a.this.b();
            }

            @Override // com.aliyun.alink.linksdk.tmp.component.cloud.ICloudProxyListener
            public void onFailure(String str, AError aError) {
                ALog.e("[Tmp]CreateConnectTask", "queryAccessInfo onResponse  error:" + aError);
                AccessInfo accessInfo2 = TmpStorage.getInstance().getAccessInfo(a.this.m.getBasicData().getDevId(), "local");
                if (accessInfo2 != null && !TextUtils.isEmpty(accessInfo2.mAccessKey) && !TextUtils.isEmpty(accessInfo2.mAccessToken)) {
                    DefaultClientConfig defaultClientConfig2 = (DefaultClientConfig) a.this.m;
                    defaultClientConfig2.setAccessKey(accessInfo2.mAccessKey);
                    defaultClientConfig2.setAccessToken(accessInfo2.mAccessToken);
                }
                a.this.b();
            }
        });
    }
}
