package com.aliyun.alink.business.devicecenter.biz.worker;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.biz.ISilenceWorker;
import com.aliyun.alink.business.devicecenter.biz.ProvisionRepository;
import com.aliyun.alink.business.devicecenter.biz.SilenceWorker;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
public class DeviceErrorUploadWorker implements ISilenceWorker {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final Object f3403a = new Object();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public AtomicBoolean f3404b = new AtomicBoolean(false);

    @Override // com.aliyun.alink.business.devicecenter.biz.ISilenceWorker
    public void work(Object obj) {
        ALog.d("DeviceErrorUploadWorker", "work() called with: data = [" + obj + "], workStarted = [" + this.f3404b + "]");
        if (this.f3404b.get()) {
            ALog.d("DeviceErrorUploadWorker", "DeviceErrorUploadWorker has started, return.");
            return;
        }
        if (obj instanceof JSONObject) {
            JSONObject jSONObject = new JSONObject();
            JSONObject jSONObject2 = new JSONObject();
            JSONObject jSONObject3 = (JSONObject) obj;
            String string = jSONObject3.getString("productKey");
            String string2 = jSONObject3.getString("deviceName");
            jSONObject2.put(AlinkConstants.KEY_CODE_VER, (Object) jSONObject3.getString(AlinkConstants.KEY_CODE_VER));
            jSONObject2.put("code", (Object) Integer.valueOf(jSONObject3.getIntValue("code")));
            jSONObject2.put("state", (Object) Integer.valueOf(jSONObject3.getIntValue("state")));
            jSONObject2.put(AlinkConstants.KEY_ERR_MSG, (Object) jSONObject3.getString(AlinkConstants.KEY_ERR_MSG));
            jSONObject.put("error", (Object) jSONObject2);
            jSONObject.put("sign", (Object) jSONObject3.getString("sign"));
            jSONObject.put(AlinkConstants.KEY_SIGN_SECRET_TYPE, (Object) jSONObject3.getString(AlinkConstants.KEY_SIGN_SECRET_TYPE));
            ProvisionRepository.uploadDeviceError(string, string2, jSONObject, new IoTCallback() { // from class: com.aliyun.alink.business.devicecenter.biz.worker.DeviceErrorUploadWorker.1
                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onFailure(IoTRequest ioTRequest, Exception exc) {
                    DeviceErrorUploadWorker.this.f3404b.set(false);
                }

                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                    if (ioTResponse == null || ioTResponse.getCode() != 200) {
                        DeviceErrorUploadWorker.this.f3404b.set(false);
                        return;
                    }
                    synchronized (DeviceErrorUploadWorker.f3403a) {
                        DeviceErrorUploadWorker.this.f3404b.set(false);
                        SilenceWorker.getInstance().unregisterWorker(DeviceErrorUploadWorker.this);
                    }
                    ALog.i("DeviceErrorUploadWorker", "deviceErrorUpload success. requestId: " + ioTResponse.getId());
                }
            });
        }
    }
}
