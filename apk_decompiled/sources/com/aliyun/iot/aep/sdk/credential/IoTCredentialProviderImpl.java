package com.aliyun.iot.aep.sdk.credential;

import android.text.TextUtils;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponseImpl;
import com.aliyun.iot.aep.sdk.apiclient.hook.IoTAuthProvider;
import com.aliyun.iot.aep.sdk.apiclient.hook.IoTRequestPayloadCallback;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestPayload;
import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTAuthApiHook;
import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialListener;
import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManage;
import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManageError;
import com.aliyun.iot.aep.sdk.credential.data.IoTCredentialData;
import com.aliyun.iot.aep.sdk.credential.oa.OADepBiz;
import com.aliyun.iot.aep.sdk.credential.utils.ReflectUtils;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.huawei.agconnect.exception.AGCServerException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes2.dex */
public class IoTCredentialProviderImpl extends IoTAuthProvider {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private IoTCredentialManage f4637a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private volatile Set<IoTRequest> f4638b = Collections.synchronizedSet(new HashSet());

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private volatile long f4639c = 0;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private IoTAuthApiHook f4640d;

    public IoTCredentialProviderImpl(IoTCredentialManage ioTCredentialManage) {
        this.f4637a = ioTCredentialManage;
        if (ioTCredentialManage == null) {
            throw new IllegalArgumentException("credentialManage must not null");
        }
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.hook.IoTAPIHook
    public void onInterceptSend(final IoTRequest ioTRequest, final IoTRequestPayload ioTRequestPayload, final IoTRequestPayloadCallback ioTRequestPayloadCallback) {
        if (TextUtils.isEmpty(this.f4637a.getIoTToken())) {
            ALog.i("IoTCredentialProviderImpl", "onInterceptSend(): iottoken is empty");
            this.f4637a.asyncRefreshIoTCredential(new IoTCredentialListener() { // from class: com.aliyun.iot.aep.sdk.credential.IoTCredentialProviderImpl.1
                @Override // com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialListener
                public void onRefreshIoTCredentialSuccess(IoTCredentialData ioTCredentialData) {
                    if (ioTRequestPayload.getRequest() == null || ioTCredentialData == null) {
                        return;
                    }
                    ioTRequestPayload.getRequest().put("iotToken", ioTCredentialData.iotToken);
                    ioTRequestPayloadCallback.onIoTRequestPayloadReady();
                }

                @Override // com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialListener
                public void onRefreshIoTCredentialFailed(IoTCredentialManageError ioTCredentialManageError) {
                    IoTResponseImpl ioTResponseImpl = new IoTResponseImpl();
                    if (ioTCredentialManageError != null && ioTCredentialManageError.errorCode == 0) {
                        ioTResponseImpl.setCode(AGCServerException.TOKEN_INVALID);
                        ioTResponseImpl.setMessage("not login or refresh iotToken failure");
                        ioTResponseImpl.setLocalizedMsg("未登录或者登录状态过期");
                        ioTRequestPayloadCallback.onResponse(ioTRequest, ioTResponseImpl);
                        return;
                    }
                    if (ioTCredentialManageError != null) {
                        if (ioTCredentialManageError.errorCode == 5 && System.currentTimeMillis() - IoTCredentialProviderImpl.this.f4639c >= 300000) {
                            ALog.i("IoTCredentialProviderImpl", "SessionId check failed, refresh session force");
                            IoTCredentialProviderImpl.this.f4639c = System.currentTimeMillis();
                            if (ReflectUtils.hasOADep()) {
                                OADepBiz.refreshSession(true);
                            } else {
                                ALog.e("IoTCredentialProviderImpl", "hasOADep = false, no oa dep exist, onRefreshIoTCredentialFailed something wrong.");
                            }
                        }
                        IoTCredentialProviderImpl.this.onInterceptFailure(ioTRequest, ioTRequestPayload, new Exception("error code: " + ioTCredentialManageError.errorCode + " error msg:" + ioTCredentialManageError.detail), ioTRequestPayloadCallback);
                        return;
                    }
                    IoTCredentialProviderImpl.this.onInterceptFailure(ioTRequest, ioTRequestPayload, new Exception("refresh iotToken error"), ioTRequestPayloadCallback);
                }
            });
            return;
        }
        if (ioTRequestPayload.getRequest() != null) {
            ioTRequestPayload.getRequest().put("iotToken", this.f4637a.getIoTToken());
        }
        IoTAuthApiHook ioTAuthApiHook = this.f4640d;
        if (ioTAuthApiHook != null) {
            ioTAuthApiHook.onInterceptSend(ioTRequest, ioTRequestPayload);
        }
        ioTRequestPayloadCallback.onIoTRequestPayloadReady();
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.hook.IoTAPIHook
    public void onInterceptResponse(final IoTRequest ioTRequest, IoTRequestPayload ioTRequestPayload, final IoTResponse ioTResponse, final IoTCallback ioTCallback) {
        IoTAuthApiHook ioTAuthApiHook = this.f4640d;
        if (ioTAuthApiHook != null) {
            ioTAuthApiHook.onInterceptResponse(ioTRequest, ioTRequestPayload, ioTResponse);
        }
        if (ioTResponse != null && ioTResponse.getCode() != 401) {
            ioTCallback.onResponse(ioTRequest, ioTResponse);
        } else if (this.f4638b.contains(ioTRequest)) {
            ioTCallback.onResponse(ioTRequest, ioTResponse);
            this.f4638b.remove(ioTRequest);
        } else {
            this.f4638b.add(ioTRequest);
            this.f4637a.asyncRefreshIoTCredential(new IoTCredentialListener() { // from class: com.aliyun.iot.aep.sdk.credential.IoTCredentialProviderImpl.2
                @Override // com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialListener
                public void onRefreshIoTCredentialSuccess(IoTCredentialData ioTCredentialData) {
                    new IoTAPIClientFactory().getClient().send(ioTRequest, new IoTCallback() { // from class: com.aliyun.iot.aep.sdk.credential.IoTCredentialProviderImpl.2.1
                        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                        public void onFailure(IoTRequest ioTRequest2, Exception exc) {
                            ioTCallback.onFailure(ioTRequest2, exc);
                            IoTCredentialProviderImpl.this.a(ioTRequest2);
                        }

                        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                        public void onResponse(IoTRequest ioTRequest2, IoTResponse ioTResponse2) {
                            ioTCallback.onResponse(ioTRequest2, ioTResponse2);
                            IoTCredentialProviderImpl.this.a(ioTRequest2);
                        }
                    });
                }

                @Override // com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialListener
                public void onRefreshIoTCredentialFailed(IoTCredentialManageError ioTCredentialManageError) {
                    ioTCallback.onResponse(ioTRequest, ioTResponse);
                    IoTCredentialProviderImpl.this.a(ioTRequest);
                }
            });
        }
    }

    @Override // com.aliyun.iot.aep.sdk.apiclient.hook.IoTAuthProvider, com.aliyun.iot.aep.sdk.apiclient.hook.IoTAPIHook
    public void onInterceptFailure(IoTRequest ioTRequest, IoTRequestPayload ioTRequestPayload, Exception exc, IoTCallback ioTCallback) {
        super.onInterceptFailure(ioTRequest, ioTRequestPayload, exc, ioTCallback);
        a(ioTRequest);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(IoTRequest ioTRequest) {
        if (this.f4638b.contains(ioTRequest)) {
            this.f4638b.remove(ioTRequest);
        }
    }

    public void setApiHook(IoTAuthApiHook ioTAuthApiHook) {
        this.f4640d = ioTAuthApiHook;
    }
}
