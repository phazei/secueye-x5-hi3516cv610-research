package com.aliyun.alink.apiclient.biz.a;

import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import com.aliyun.alink.apiclient.biz.IApiClientBiz;
import com.aliyun.alink.apiclient.biz.IApiClientBizCallback;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClient;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientImpl;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.facebook.internal.ServerProtocol;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: compiled from: ApiClientBizImpl.java */
/* JADX INFO: loaded from: classes.dex */
public class a implements IApiClientBiz {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private String f3232a = null;

    @Override // com.aliyun.alink.apiclient.biz.IApiClientBiz
    public void usageTrack(String str, String str2, String str3, Map map, final IApiClientBizCallback iApiClientBizCallback) {
        IoTAPIClient client = new IoTAPIClientFactory().getClient();
        boolean z = client instanceof IoTAPIClientImpl;
        if (z && !((IoTAPIClientImpl) client).hasInited()) {
            ALog.e("ApiClientBizImpl", "api client has not been inited.");
            if (iApiClientBizCallback != null) {
                iApiClientBizCallback.onFail(new IllegalStateException("ApiClientNotInit"));
                return;
            }
            return;
        }
        HashMap map2 = new HashMap();
        if (TextUtils.isEmpty(str)) {
            map2.put("type", "appSdkTrack");
        } else {
            map2.put("type", str);
        }
        if (TextUtils.isEmpty(str2)) {
            throw new IllegalArgumentException("sdkName cannot be empty.");
        }
        if (TextUtils.isEmpty(str3)) {
            throw new IllegalArgumentException("event cannot be empty.");
        }
        map2.put(ServerProtocol.DIALOG_PARAM_SDK_VERSION, str2);
        map2.put(NotificationCompat.CATEGORY_EVENT, str3);
        map2.put("system", "Android");
        map2.put("systemVer", com.aliyun.alink.apiclient.biz.b.a.b());
        if (TextUtils.isEmpty(this.f3232a)) {
            this.f3232a = com.aliyun.alink.apiclient.biz.b.a.a(z ? ((IoTAPIClientImpl) client).getAppContext() : null);
        }
        map2.put("phoneDescriptor", this.f3232a);
        if (map != null) {
            map2.putAll(map);
        }
        client.send(new IoTRequestBuilder().setApiVersion("1.0.0").setPath("/living/client/data/report/get").setScheme(Scheme.HTTPS).addParam("record", (Map) map2).build(), new IoTCallback() { // from class: com.aliyun.alink.apiclient.biz.a.a.1
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                ALog.w("ApiClientBizImpl", "onFailure() called with: ioTRequest = [" + a.this.a(ioTRequest) + "], e = [" + exc + "]");
                IApiClientBizCallback iApiClientBizCallback2 = iApiClientBizCallback;
                if (iApiClientBizCallback2 != null) {
                    iApiClientBizCallback2.onFail(exc);
                }
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                ALog.d("ApiClientBizImpl", "onResponse() called with: ioTRequest = [" + a.this.a(ioTRequest) + "], ioTResponse = [" + a.this.a(ioTResponse) + "]");
                if (iApiClientBizCallback != null) {
                    b bVar = null;
                    if (ioTResponse != null) {
                        bVar = new b();
                        b bVar2 = bVar;
                        bVar2.a(ioTResponse.getCode());
                        bVar2.a(ioTResponse.getId());
                        bVar2.b(ioTResponse.getMessage());
                        bVar2.c(ioTResponse.getLocalizedMsg());
                        bVar2.a(ioTResponse.getData());
                        bVar2.a(ioTResponse.getRawData());
                    }
                    iApiClientBizCallback.onResponse(bVar);
                }
            }
        });
    }

    public String a(IoTRequest ioTRequest) {
        if (ioTRequest == null) {
            return null;
        }
        return "[schema=" + ioTRequest.getScheme() + ",host=" + ioTRequest.getHost() + ",path=" + ioTRequest.getPath() + ",apiVersion=" + ioTRequest.getAPIVersion() + ",method=" + ioTRequest.getMethod() + ",authType=" + ioTRequest.getAuthType() + ",params=" + ioTRequest.getParams() + "]";
    }

    public String a(IoTResponse ioTResponse) {
        if (ioTResponse == null) {
            return null;
        }
        return "[requestId=" + ioTResponse.getId() + ",code=" + ioTResponse.getCode() + ",message=" + ioTResponse.getMessage() + ",localizedMsg=" + ioTResponse.getLocalizedMsg() + ",data=" + ioTResponse.getData() + "]";
    }
}
