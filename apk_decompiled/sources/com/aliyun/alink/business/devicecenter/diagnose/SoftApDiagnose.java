package com.aliyun.alink.business.devicecenter.diagnose;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.api.diagnose.DiagnoseResult;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.biz.SilenceWorker;
import com.aliyun.alink.business.devicecenter.biz.worker.DeviceErrorUploadWorker;
import com.aliyun.alink.business.devicecenter.channel.coap.CoAPClient;
import com.aliyun.alink.business.devicecenter.channel.coap.request.CoapRequestPayload;
import com.aliyun.alink.business.devicecenter.log.ALog;
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

/* JADX INFO: loaded from: classes.dex */
public class SoftApDiagnose {
    public String f;
    public String g;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public AlcsCoAPRequest f3571a = null;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public long f3572b = -1;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public ScheduledFuture<?> f3573c = null;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final Object f3574d = new Object();
    public TimerUtils e = null;
    public final TimerUtils.ITimerCallback h = new TimerUtils.ITimerCallback() { // from class: com.aliyun.alink.business.devicecenter.diagnose.SoftApDiagnose.1
        @Override // com.aliyun.alink.business.devicecenter.utils.TimerUtils.ITimerCallback
        public void onTimeout() {
            ALog.w("SoftApDiagnose", "diagnose timeout in " + SoftApDiagnose.this.e.getTimeout() + "s");
            SoftApDiagnose.this.stopDiagnose();
        }
    };

    private static class SingletonHolder {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final SoftApDiagnose f3578a = new SoftApDiagnose();
    }

    public static SoftApDiagnose getInstance() {
        return SingletonHolder.f3578a;
    }

    public void startDiagnose(String str, String str2, int i) {
        ALog.w("SoftApDiagnose", "start diagnose.");
        synchronized (this.f3574d) {
            if (this.f3573c != null) {
                ALog.w("SoftApDiagnose", "diagnose has started.");
                return;
            }
            this.f = str;
            this.g = str2;
            this.f3573c = ThreadPool.scheduleAtFixedRate(new Runnable() { // from class: com.aliyun.alink.business.devicecenter.diagnose.SoftApDiagnose.2
                @Override // java.lang.Runnable
                public void run() {
                    SoftApDiagnose.this.b();
                }
            }, 0L, 5L, TimeUnit.SECONDS);
            this.e = new TimerUtils(a(i) * 1000);
            this.e.setCallback(this.h);
            this.e.start(TimerUtils.MSG_DIAGNOSE);
        }
    }

    public void stopDiagnose() {
        ALog.d("SoftApDiagnose", "stop diagnose.");
        synchronized (this.f3574d) {
            if (this.f3573c != null) {
                this.f3573c.cancel(true);
                this.f3573c = null;
            }
            if (this.e != null) {
                this.e.stop(TimerUtils.MSG_DIAGNOSE);
            }
        }
    }

    public final void b() {
        ALog.d("SoftApDiagnose", "getDeviceErrorCode() called");
        try {
            CoapRequestPayload coapRequestPayload = new CoapRequestPayload();
            coapRequestPayload.getClass();
            CoapRequestPayload coapRequestPayloadBuild = new CoapRequestPayload.Builder().version("2.0").params(new HashMap()).method("awss.device.errcode.get").build();
            a();
            this.f3571a = new AlcsCoAPRequest(AlcsCoAPConstant.Code.GET, AlcsCoAPConstant.Type.NON);
            String broadcastIp = WiFiUtils.getBroadcastIp();
            StringBuilder sb = new StringBuilder();
            sb.append(broadcastIp);
            sb.append(":");
            sb.append(5683);
            sb.append("/sys/awss/device/errcode/get");
            String string = sb.toString();
            this.f3571a.setPayload(coapRequestPayloadBuild.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("getDeviceErrorCode setPayload=");
            sb2.append(coapRequestPayloadBuild.toString());
            sb2.append(",getPayload=");
            sb2.append(this.f3571a.getPayloadString());
            ALog.llog((byte) 3, "SoftApDiagnose", sb2.toString());
            this.f3571a.setMulticast(1);
            this.f3571a.setURI(string);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("coapUri=");
            sb3.append(string);
            ALog.d("SoftApDiagnose", sb3.toString());
        } catch (Exception e) {
            ALog.w("SoftApDiagnose", "pre getDeviceErrorCode sendRequest params exception=" + e);
        }
        this.f3572b = CoAPClient.getInstance().sendRequest(this.f3571a, new IAlcsCoAPReqHandler() { // from class: com.aliyun.alink.business.devicecenter.diagnose.SoftApDiagnose.3
            @Override // com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPReqHandler
            public void onReqComplete(AlcsCoAPContext alcsCoAPContext, int i, AlcsCoAPResponse alcsCoAPResponse) {
                CoAPClient.getInstance().printResponse(alcsCoAPContext, alcsCoAPResponse);
                if (alcsCoAPResponse == null || TextUtils.isEmpty(alcsCoAPResponse.getPayloadString())) {
                    return;
                }
                ALog.llog((byte) 3, "SoftApDiagnose", "getDeviceErrorCode responseString=" + alcsCoAPResponse.getPayloadString());
                if (alcsCoAPResponse.getMID() != SoftApDiagnose.this.f3572b) {
                    ALog.d("SoftApDiagnose", "old coap message id, return.");
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
                    if (TextUtils.isEmpty(diagnoseResult.sign) || TextUtils.isEmpty(diagnoseResult.signSecretType)) {
                        ALog.i("SoftApDiagnose", "sign or signSecretType is empty -> old device.");
                    } else if ("0".equals(diagnoseResult.code)) {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("not upload error code 0, errMsg: ");
                        sb4.append(diagnoseResult.errMsg);
                        ALog.i("SoftApDiagnose", sb4.toString());
                    } else {
                        SoftApDiagnose.this.a(jSONObject);
                    }
                    SoftApDiagnose.this.stopDiagnose();
                } catch (Exception e2) {
                    ALog.w("SoftApDiagnose", "getDeviceErrorCode device.errcode.get parsePayloadException= " + e2);
                }
            }
        });
    }

    public final int a(int i) {
        return Math.min(Math.max(i, 5), 30);
    }

    public final void a(JSONObject jSONObject) {
        if (this.f != null || this.g != null) {
            jSONObject.put("productKey", (Object) this.f);
            jSONObject.put("deviceName", (Object) this.g);
        }
        ALog.d("SoftApDiagnose", "uploadDeviceError() called with: jsonObject = [" + jSONObject + "]");
        SilenceWorker.getInstance().registerWorker(new DeviceErrorUploadWorker(), jSONObject);
    }

    public final void a() {
        AlcsCoAPRequest alcsCoAPRequest = this.f3571a;
        if (alcsCoAPRequest != null) {
            alcsCoAPRequest.cancel();
            this.f3571a = null;
        }
        if (this.f3572b != -1) {
            CoAPClient.getInstance().cancelMessage(this.f3572b);
        }
    }
}
