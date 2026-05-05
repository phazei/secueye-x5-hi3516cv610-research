package com.aliyun.alink.linksdk.cmp.connect.hubapi;

import android.content.Context;
import android.text.TextUtils;
import com.aliyun.alink.apiclient.CommonRequest;
import com.aliyun.alink.apiclient.CommonResponse;
import com.aliyun.alink.apiclient.InitializeConfig;
import com.aliyun.alink.apiclient.IoTAPIClientFactory;
import com.aliyun.alink.apiclient.IoTApiClient;
import com.aliyun.alink.apiclient.IoTCallback;
import com.aliyun.alink.linksdk.cmp.core.base.AConnect;
import com.aliyun.alink.linksdk.cmp.core.base.AConnectConfig;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.CmpError;
import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectInitListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSubscribeListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectUnscribeListener;
import com.aliyun.alink.linksdk.cmp.core.util.CallbackHelper;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class HubApiClientConnect extends AConnect {
    public static final String CONNECT_ID = "LINK_HUB_API_CLIENT";
    private static final String TAG = "HubApiClientConnect";
    private static IoTApiClient ioTAPIClient;

    /* JADX INFO: renamed from: config, reason: collision with root package name */
    private HubApiClientConnectConfig f4200config;
    private Context context;

    @Override // com.aliyun.alink.linksdk.cmp.core.base.AConnect
    public void init(Context context, AConnectConfig aConnectConfig, IConnectInitListener iConnectInitListener) {
        ALog.d(TAG, "init()");
        if (context == null || aConnectConfig == null || !(aConnectConfig instanceof HubApiClientConnectConfig) || !aConnectConfig.checkVaild()) {
            ALog.d(TAG, "init()，params error");
            CallbackHelper.paramError(iConnectInitListener, "init, cxt or config is invalid");
            return;
        }
        this.connectId = CONNECT_ID;
        this.context = context;
        this.f4200config = (HubApiClientConnectConfig) aConnectConfig;
        ioTAPIClient = new IoTAPIClientFactory().getClient();
        InitializeConfig initializeConfig = new InitializeConfig();
        initializeConfig.productKey = this.f4200config.productKey;
        initializeConfig.deviceName = this.f4200config.deviceName;
        if (!TextUtils.isEmpty(this.f4200config.domain)) {
            initializeConfig.domain = this.f4200config.domain;
        }
        if (!TextUtils.isEmpty(this.f4200config.productSecret)) {
            initializeConfig.productSecret = this.f4200config.productSecret;
        }
        if (!TextUtils.isEmpty(this.f4200config.deviceSecret)) {
            initializeConfig.deviceSecret = this.f4200config.deviceSecret;
        }
        ioTAPIClient.init(initializeConfig);
        this.connectState = ConnectState.CONNECTED;
        updateConnectState(this.connectState);
        if (iConnectInitListener != null) {
            iConnectInitListener.onSuccess();
        }
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.base.AConnect
    public void onDestroy() {
        ALog.d(TAG, "onDestory()");
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.base.AConnect
    public void send(final ARequest aRequest, final IConnectSendListener iConnectSendListener) {
        ALog.d(TAG, "send()");
        if (aRequest == null || !(aRequest instanceof HubApiRequest)) {
            ALog.d(TAG, "request is invalid ");
            if (iConnectSendListener == null) {
                return;
            }
            CmpError cmpErrorPARAMS_ERROR = CmpError.PARAMS_ERROR();
            cmpErrorPARAMS_ERROR.setSubMsg("send, request is invalid");
            iConnectSendListener.onFailure(aRequest, cmpErrorPARAMS_ERROR);
            return;
        }
        ioTAPIClient.send(((HubApiRequest) aRequest).toChannelRequest(), new IoTCallback() { // from class: com.aliyun.alink.linksdk.cmp.connect.hubapi.HubApiClientConnect.1
            @Override // com.aliyun.alink.apiclient.IoTCallback
            public void onFailure(CommonRequest commonRequest, Exception exc) {
                if (iConnectSendListener == null) {
                    return;
                }
                CmpError cmpErrorHUB_API_SEND_FAIL = CmpError.HUB_API_SEND_FAIL();
                cmpErrorHUB_API_SEND_FAIL.setSubMsg(exc.toString());
                iConnectSendListener.onFailure(aRequest, cmpErrorHUB_API_SEND_FAIL);
            }

            @Override // com.aliyun.alink.apiclient.IoTCallback
            public void onResponse(CommonRequest commonRequest, CommonResponse commonResponse) {
                StringBuilder sb = new StringBuilder();
                sb.append("onResponse(),rsp = ");
                sb.append((commonResponse == null || commonResponse.getData() == null) ? "" : commonResponse.getData());
                ALog.d(HubApiClientConnect.TAG, sb.toString());
                if (iConnectSendListener == null) {
                    return;
                }
                if (commonResponse == null || commonResponse.getData() == null) {
                    CmpError cmpErrorHUB_API_SEND_FAIL = CmpError.HUB_API_SEND_FAIL();
                    cmpErrorHUB_API_SEND_FAIL.setSubMsg("empty response or fail status");
                    cmpErrorHUB_API_SEND_FAIL.setSubCode(717);
                    iConnectSendListener.onFailure(aRequest, cmpErrorHUB_API_SEND_FAIL);
                    return;
                }
                HubApiResponse hubApiResponse = new HubApiResponse();
                hubApiResponse.data = commonResponse.getData();
                iConnectSendListener.onResponse(aRequest, hubApiResponse);
            }
        });
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.base.AConnect
    public void subscribe(ARequest aRequest, IConnectSubscribeListener iConnectSubscribeListener) {
        ALog.d(TAG, "subscribe,unsupport");
        if (iConnectSubscribeListener != null) {
            iConnectSubscribeListener.onFailure(CmpError.UNSUPPORT());
        }
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.base.AConnect
    public void unsubscribe(ARequest aRequest, IConnectUnscribeListener iConnectUnscribeListener) {
        ALog.d(TAG, "unsubscribe,unsupport");
        if (iConnectUnscribeListener != null) {
            iConnectUnscribeListener.onFailure(CmpError.UNSUPPORT());
        }
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.base.AConnect
    public void setNotifyListener(IConnectNotifyListener iConnectNotifyListener) {
        ALog.d(TAG, "setNotifyListener,unsupport");
    }
}
