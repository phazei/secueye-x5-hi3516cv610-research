package com.aliyun.alink.business.devicecenter.api.diagnose;

import android.content.Context;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.biz.SilenceWorker;
import com.aliyun.alink.business.devicecenter.biz.worker.DeviceErrorUploadWorker;
import com.aliyun.alink.business.devicecenter.channel.coap.CoAPClient;
import com.aliyun.alink.business.devicecenter.channel.coap.request.CoapRequestPayload;
import com.aliyun.alink.business.devicecenter.config.DeviceCenterBiz;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.utils.NetworkConnectiveManager;
import com.aliyun.alink.business.devicecenter.utils.ThreadPool;
import com.aliyun.alink.business.devicecenter.utils.TimerUtils;
import com.aliyun.alink.business.devicecenter.utils.WiFiUtils;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPConstant;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPContext;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPRequest;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPResponse;
import com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPReqHandler;
import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public class ProvisionDiagnose {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public WifiManager f3298a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public NetworkConnectiveManager f3299b;
    public Context g;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public TimerUtils f3300c = null;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public DiagnoseCallback f3301d = null;
    public AlcsCoAPRequest e = null;
    public long f = -1;
    public DiagnoseParams h = null;
    public ScheduledFuture<?> i = null;
    public AtomicBoolean j = new AtomicBoolean(false);
    public AtomicBoolean k = new AtomicBoolean(false);
    public AtomicBoolean l = new AtomicBoolean(false);
    public AtomicInteger m = new AtomicInteger(0);
    public NetworkConnectiveManager.INetworkChangeListener n = new NetworkConnectiveManager.INetworkChangeListener() { // from class: com.aliyun.alink.business.devicecenter.api.diagnose.ProvisionDiagnose.1
        @Override // com.aliyun.alink.business.devicecenter.utils.NetworkConnectiveManager.INetworkChangeListener
        public void onNetworkStateChange(NetworkInfo networkInfo, Network network) {
            WifiInfo connectionInfo;
            ALog.d("ProvisionDiagnose", "onWiFiStateChange() called with: networkInfo = [" + networkInfo + "]");
            if (networkInfo == null) {
                return;
            }
            try {
                if (networkInfo.getState() != NetworkInfo.State.CONNECTED) {
                    return;
                }
                if (ProvisionDiagnose.this.k.get()) {
                    ALog.d("ProvisionDiagnose", "diagnose stopped, return.");
                    return;
                }
                if (networkInfo.getType() != 1) {
                    ALog.d("ProvisionDiagnose", "scheduleGetErrorCode when device ap connected. ");
                    return;
                }
                if (ProvisionDiagnose.this.f3298a == null || ProvisionDiagnose.this.f3298a.getConnectionInfo() == null || ProvisionDiagnose.this.h == null || TextUtils.isEmpty(ProvisionDiagnose.this.h.deviceSSID) || (connectionInfo = ProvisionDiagnose.this.f3298a.getConnectionInfo()) == null) {
                    return;
                }
                String ssid = connectionInfo.getSSID();
                if (ssid != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("\"");
                    sb.append(ProvisionDiagnose.this.h.deviceSSID);
                    sb.append("\"");
                    if (ssid.equals(sb.toString())) {
                        if (ProvisionDiagnose.this.l.get()) {
                            return;
                        }
                        ProvisionDiagnose.this.c();
                        return;
                    }
                }
                ProvisionDiagnose.this.a(ProvisionDiagnose.this.h.deviceSSID);
            } catch (Exception e) {
                e.printStackTrace();
                ALog.w("ProvisionDiagnose", "handleWiFiStateChange exception=" + e);
            }
        }
    };
    public TimerUtils.ITimerCallback o = new TimerUtils.ITimerCallback() { // from class: com.aliyun.alink.business.devicecenter.api.diagnose.ProvisionDiagnose.4
        @Override // com.aliyun.alink.business.devicecenter.utils.TimerUtils.ITimerCallback
        public void onTimeout() {
            if (ProvisionDiagnose.this.f3301d != null) {
                DiagnoseResult diagnoseResult = new DiagnoseResult();
                diagnoseResult.code = String.valueOf(DCErrorCode.ERROR_CODE_DIAGNOSE_TIMEOUT);
                diagnoseResult.errMsg = "diagnose timeout";
                if (ProvisionDiagnose.this.k.get()) {
                    return;
                }
                ProvisionDiagnose.this.k.set(true);
                if (ProvisionDiagnose.this.f3301d != null) {
                    ProvisionDiagnose.this.f3301d.onResult(diagnoseResult);
                }
                ProvisionDiagnose.this.stopDiagnose();
            }
        }
    };

    public ProvisionDiagnose(Context context) {
        this.f3299b = null;
        this.g = null;
        if (context == null) {
            throw new RuntimeException("context cannot be null.");
        }
        this.g = context;
        DeviceCenterBiz.getInstance().setAppContext(context);
        this.f3299b = NetworkConnectiveManager.getInstance();
        this.f3298a = (WifiManager) context.getApplicationContext().getSystemService("wifi");
    }

    public void startDiagnose(DiagnoseParams diagnoseParams, DiagnoseCallback diagnoseCallback) {
        ALog.d("ProvisionDiagnose", "startDiagnose() called with: params = [" + diagnoseParams + "], callback = [" + diagnoseCallback + "]");
        if (diagnoseParams == null || TextUtils.isEmpty(diagnoseParams.deviceSSID)) {
            throw new IllegalArgumentException("params error");
        }
        if (diagnoseParams.timeout < 3) {
            return;
        }
        if (this.j.get()) {
            throw new IllegalStateException("diagnose has already started.");
        }
        this.j.set(true);
        this.k.set(false);
        this.l.set(false);
        this.m.set(1);
        this.h = diagnoseParams;
        this.f3301d = diagnoseCallback;
        a(this.f3300c);
        this.f3300c = new TimerUtils((this.h.timeout - 2) * 1000);
        this.f3300c.setCallback(this.o);
        this.f3300c.start(TimerUtils.MSG_DIAGNOSE);
        a();
        a(this.h.deviceSSID);
    }

    public void stopDiagnose() {
        ALog.d("ProvisionDiagnose", "stopDiagnose() called");
        this.h = null;
        this.f3301d = null;
        this.m.set(0);
        this.j.set(false);
        this.k.set(true);
        this.l.set(false);
        a(this.f3300c);
        a(this.i);
        d();
    }

    public final void b() {
        ALog.d("ProvisionDiagnose", "getDeviceErrorCode() called");
        try {
            CoapRequestPayload coapRequestPayload = new CoapRequestPayload();
            coapRequestPayload.getClass();
            CoapRequestPayload coapRequestPayloadBuild = new CoapRequestPayload.Builder().version("2.0").params(new HashMap()).method("awss.device.errcode.get").build();
            a(this.e);
            this.e = new AlcsCoAPRequest(AlcsCoAPConstant.Code.GET, AlcsCoAPConstant.Type.NON);
            String broadcastIp = WiFiUtils.getBroadcastIp();
            StringBuilder sb = new StringBuilder();
            sb.append(broadcastIp);
            sb.append(":");
            sb.append(5683);
            sb.append("/sys/awss/device/errcode/get");
            String string = sb.toString();
            this.e.setPayload(coapRequestPayloadBuild.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("getDeviceErrorCode setPayload=");
            sb2.append(coapRequestPayloadBuild.toString());
            sb2.append(",getPayload=");
            sb2.append(this.e.getPayloadString());
            ALog.llog((byte) 3, "ProvisionDiagnose", sb2.toString());
            this.e.setMulticast(1);
            this.e.setURI(string);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("coapUri=");
            sb3.append(string);
            ALog.d("ProvisionDiagnose", sb3.toString());
        } catch (Exception e) {
            ALog.w("ProvisionDiagnose", "pre getDeviceErrorCode sendRequest params exception=" + e);
        }
        this.f = CoAPClient.getInstance().sendRequest(this.e, new IAlcsCoAPReqHandler() { // from class: com.aliyun.alink.business.devicecenter.api.diagnose.ProvisionDiagnose.3
            @Override // com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPReqHandler
            public void onReqComplete(AlcsCoAPContext alcsCoAPContext, int i, AlcsCoAPResponse alcsCoAPResponse) {
                CoAPClient.getInstance().printResponse(alcsCoAPContext, alcsCoAPResponse);
                if (alcsCoAPResponse == null || TextUtils.isEmpty(alcsCoAPResponse.getPayloadString())) {
                    return;
                }
                ALog.llog((byte) 3, "ProvisionDiagnose", "getDeviceErrorCode responseString=" + alcsCoAPResponse.getPayloadString());
                if (ProvisionDiagnose.this.k.get()) {
                    ALog.d("ProvisionDiagnose", "diagnose has stopped, return.");
                    return;
                }
                try {
                    JSONObject jSONObject = JSONObject.parseObject(alcsCoAPResponse.getPayloadString()).getJSONObject("data");
                    DiagnoseResult diagnoseResult = new DiagnoseResult();
                    diagnoseResult.code = jSONObject.getString("code");
                    diagnoseResult.codeVer = jSONObject.getString(AlinkConstants.KEY_CODE_VER);
                    diagnoseResult.state = jSONObject.getString("state");
                    diagnoseResult.errMsg = jSONObject.getString(AlinkConstants.KEY_ERR_MSG);
                    diagnoseResult.sign = jSONObject.getString("sign");
                    diagnoseResult.signSecretType = jSONObject.getString(AlinkConstants.KEY_SIGN_SECRET_TYPE);
                    ProvisionDiagnose.this.k.set(true);
                    if (TextUtils.isEmpty(diagnoseResult.sign) || TextUtils.isEmpty(diagnoseResult.signSecretType)) {
                        ALog.i("ProvisionDiagnose", "sign or signSecretType is empty -> old device.");
                    } else {
                        ProvisionDiagnose.this.a(jSONObject);
                    }
                    if (ProvisionDiagnose.this.f3301d != null) {
                        ProvisionDiagnose.this.f3301d.onResult(diagnoseResult);
                    }
                    ProvisionDiagnose.this.stopDiagnose();
                } catch (Exception e2) {
                    ALog.w("ProvisionDiagnose", "getDeviceErrorCode device.errcode.get parsePayloadException= " + e2);
                }
            }
        });
    }

    public final void c() {
        a(this.i);
        this.l.set(true);
        this.i = ThreadPool.scheduleAtFixedRate(new Runnable() { // from class: com.aliyun.alink.business.devicecenter.api.diagnose.ProvisionDiagnose.2
            @Override // java.lang.Runnable
            public void run() {
                if (ProvisionDiagnose.this.k.get()) {
                    ALog.d("ProvisionDiagnose", "diagnose stopped, ignore getDeviceErrorCode and return.");
                } else {
                    ProvisionDiagnose.this.b();
                }
            }
        }, 0L, 5L, TimeUnit.SECONDS);
    }

    public final void d() {
        try {
            if (this.f3299b != null) {
                this.f3299b.unregisterConnectiveListener(this.n);
            }
        } catch (Exception e) {
            ALog.w("ProvisionDiagnose", "unregisterAPBroadcast exception=" + e);
        }
    }

    public final void a(String str) {
        if (this.k.get()) {
            return;
        }
        WiFiUtils.connect(this.g, str, null, "", "", -1);
    }

    public final void a() {
        NetworkConnectiveManager networkConnectiveManager = this.f3299b;
        if (networkConnectiveManager != null) {
            networkConnectiveManager.registerConnectiveListener(this.n);
        }
    }

    public final void a(JSONObject jSONObject) {
        DiagnoseParams diagnoseParams = this.h;
        if (diagnoseParams != null) {
            jSONObject.put("productKey", (Object) diagnoseParams.productKey);
            jSONObject.put("deviceName", (Object) this.h.deviceName);
        }
        ALog.d("ProvisionDiagnose", "uploadDeviceError() called with: jsonObject = [" + jSONObject + "]");
        SilenceWorker.getInstance().registerWorker(new DeviceErrorUploadWorker(), jSONObject);
    }

    public final void a(ScheduledFuture scheduledFuture) {
        if (scheduledFuture != null) {
            try {
                scheduledFuture.cancel(true);
            } catch (Exception unused) {
            }
        }
    }

    public final void a(TimerUtils timerUtils) {
        if (timerUtils != null) {
            timerUtils.stop(TimerUtils.MSG_DIAGNOSE);
        }
    }

    public final void a(AlcsCoAPRequest alcsCoAPRequest) {
        if (alcsCoAPRequest != null) {
            alcsCoAPRequest.cancel();
        }
        if (this.f != -1) {
            CoAPClient.getInstance().cancelMessage(this.f);
        }
    }
}
