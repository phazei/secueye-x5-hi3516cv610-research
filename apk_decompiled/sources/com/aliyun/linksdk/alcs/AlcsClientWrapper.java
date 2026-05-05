package com.aliyun.linksdk.alcs;

import com.aliyun.alink.linksdk.alcs.api.client.AlcsClientConfig;
import com.aliyun.alink.linksdk.alcs.api.client.IDeviceHandler;
import com.aliyun.alink.linksdk.alcs.api.client.IDeviceStateListener;
import com.aliyun.alink.linksdk.alcs.api.utils.ErrorInfo;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPContext;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPResponse;
import com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPReqHandler;
import com.aliyun.alink.linksdk.alcs.data.ica.ICAAuthParams;
import com.aliyun.alink.linksdk.alcs.lpbs.api.IAlcsPal;
import com.aliyun.alink.linksdk.alcs.lpbs.api.PluginMgr;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalConnectParams;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalReqMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalRspMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalSubMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalConnectListener;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalDeviceStateListener;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class AlcsClientWrapper implements IAlcsClient {
    private static final String TAG = "AlcsClientWrapper";
    private IAlcsPal alcsClient = null;

    /* JADX INFO: renamed from: config, reason: collision with root package name */
    private AlcsClientConfig f5215config = null;
    private IClientNotify clientNotify = null;

    /* JADX WARN: Type inference failed for: r0v7, types: [ExtraConnectparams, ExtraParams] */
    @Override // com.aliyun.linksdk.alcs.IAlcsClient
    public void init(AlcsClientConfig alcsClientConfig, final IDeviceHandler iDeviceHandler) {
        ALog.d(TAG, "init()");
        if (this.alcsClient != null) {
            return;
        }
        this.alcsClient = PluginMgr.getInstance();
        this.f5215config = alcsClientConfig;
        this.alcsClient = PluginMgr.getInstance();
        ICAAuthParams iCAAuthParams = new ICAAuthParams();
        iCAAuthParams.accessKey = alcsClientConfig.getAccessKey();
        iCAAuthParams.accessToken = alcsClientConfig.getAccessToken();
        PalConnectParams palConnectParams = new PalConnectParams();
        palConnectParams.deviceInfo = new PalDeviceInfo(alcsClientConfig.getProductKey(), alcsClientConfig.getDeviceName());
        palConnectParams.authInfo = iCAAuthParams;
        palConnectParams.dataFormat = alcsClientConfig.mDataFormat;
        palConnectParams.connectKeepStrategy = alcsClientConfig.mConnectKeepType;
        palConnectParams.extraConnectParams = alcsClientConfig.mExtraParams;
        palConnectParams.pluginId = alcsClientConfig.mPluginId;
        this.alcsClient.startConnect(palConnectParams, new PalConnectListener() { // from class: com.aliyun.linksdk.alcs.AlcsClientWrapper.1
            @Override // com.aliyun.alink.linksdk.alcs.lpbs.listener.PalConnectListener
            public void onLoad(int i, Map<String, Object> map, PalDeviceInfo palDeviceInfo) {
                ALog.d(AlcsClientWrapper.TAG, "alcsClientwrapper connectDevice onLoad errorCode:" + i);
                if (i == 0) {
                    iDeviceHandler.onSuccess(null);
                    AlcsClientWrapper.this.alcsClient.regDeviceStateListener(new PalDeviceInfo(AlcsClientWrapper.this.f5215config.getProductKey(), AlcsClientWrapper.this.f5215config.getDeviceName()), new AlcsServerStateListener());
                } else {
                    iDeviceHandler.onFail(null, new ErrorInfo(i, ""));
                }
            }
        });
    }

    @Override // com.aliyun.linksdk.alcs.IAlcsClient
    public void destroy() {
        ALog.d(TAG, "destroy()");
        IAlcsPal iAlcsPal = this.alcsClient;
        if (iAlcsPal == null) {
            return;
        }
        iAlcsPal.stopConnect(new PalDeviceInfo(this.f5215config.getProductKey(), this.f5215config.getDeviceName()));
    }

    @Override // com.aliyun.linksdk.alcs.IAlcsClient
    public String getDstAddr() {
        AlcsClientConfig alcsClientConfig;
        ALog.d(TAG, "getDstAddr()");
        if (this.alcsClient == null || (alcsClientConfig = this.f5215config) == null) {
            return null;
        }
        return alcsClientConfig.getDstAddr();
    }

    @Override // com.aliyun.linksdk.alcs.IAlcsClient
    public boolean isServerOnline() {
        ALog.d(TAG, "isServerOnline()");
        if (this.alcsClient == null) {
            return false;
        }
        ALog.d(TAG, "isServerOnline(), call coap sdk");
        return this.alcsClient.isDeviceConnected(new PalDeviceInfo(this.f5215config.getProductKey(), this.f5215config.getDeviceName()));
    }

    @Override // com.aliyun.linksdk.alcs.IAlcsClient
    public boolean sendRequest(boolean z, PalReqMessage palReqMessage, PalMsgListener palMsgListener) {
        ALog.d(TAG, "sendRequest()");
        IAlcsPal iAlcsPal = this.alcsClient;
        if (iAlcsPal == null) {
            return false;
        }
        return iAlcsPal.asyncSendRequest(palReqMessage, palMsgListener);
    }

    @Override // com.aliyun.linksdk.alcs.IAlcsClient
    public boolean sendResponse(boolean z, AlcsCoAPResponse alcsCoAPResponse) {
        ALog.d(TAG, "sendResponse()");
        return false;
    }

    @Override // com.aliyun.linksdk.alcs.IAlcsClient
    public void subscribe(boolean z, PalSubMessage palSubMessage, PalMsgListener palMsgListener) {
        ALog.d(TAG, "subscribe()");
        IAlcsPal iAlcsPal = this.alcsClient;
        if (iAlcsPal == null) {
            return;
        }
        iAlcsPal.subscribe(palSubMessage, new AlcsSubScribleMsgHandler(palSubMessage.topic, palMsgListener), new AlcsMsgTriggerHandler(palSubMessage.topic, palMsgListener));
    }

    @Override // com.aliyun.linksdk.alcs.IAlcsClient
    public void unsubscribe(boolean z, PalSubMessage palSubMessage, PalMsgListener palMsgListener) {
        ALog.d(TAG, "unsubscribe()");
        IAlcsPal iAlcsPal = this.alcsClient;
        if (iAlcsPal == null) {
            return;
        }
        iAlcsPal.unsubscribe(palSubMessage, palMsgListener);
    }

    private class AlcsSubScribleMsgHandler implements PalMsgListener {
        private PalMsgListener handler;
        private String topic;

        public AlcsSubScribleMsgHandler(String str, PalMsgListener palMsgListener) {
            this.handler = null;
            this.topic = null;
            this.topic = str;
            this.handler = palMsgListener;
        }

        @Override // com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener
        public void onLoad(PalRspMessage palRspMessage) {
            PalMsgListener palMsgListener = this.handler;
            if (palMsgListener != null) {
                palMsgListener.onLoad(palRspMessage);
            }
        }
    }

    private class AlcsMsgTriggerHandler implements PalMsgListener {
        private PalMsgListener handler;
        private String topic;

        public AlcsMsgTriggerHandler(String str, PalMsgListener palMsgListener) {
            this.handler = null;
            this.topic = null;
            this.topic = str;
            this.handler = palMsgListener;
        }

        @Override // com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener
        public void onLoad(PalRspMessage palRspMessage) {
            AlcsClientWrapper.this.clientNotify.onNotify(this.topic, palRspMessage);
        }
    }

    private class AlcsCoapUnsubscribeHandle implements IAlcsCoAPReqHandler {
        private boolean flag = false;
        private IDeviceHandler handler;

        public AlcsCoapUnsubscribeHandle(IDeviceHandler iDeviceHandler) {
            this.handler = null;
            this.handler = iDeviceHandler;
        }

        @Override // com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPReqHandler
        public void onReqComplete(AlcsCoAPContext alcsCoAPContext, int i, AlcsCoAPResponse alcsCoAPResponse) {
            IDeviceHandler iDeviceHandler;
            if (this.flag || (iDeviceHandler = this.handler) == null) {
                return;
            }
            if (i == 0) {
                iDeviceHandler.onSuccess(null);
            } else {
                iDeviceHandler.onFail(null, null);
            }
            this.flag = true;
        }
    }

    private class AlcsServerStateListener implements IDeviceStateListener, PalDeviceStateListener {
        private AlcsServerStateListener() {
        }

        @Override // com.aliyun.alink.linksdk.alcs.api.client.IDeviceStateListener
        public void onDeviceStateChange(int i) {
            ALog.d(AlcsClientWrapper.TAG, "onDeviceStateChange(), state = " + i);
            boolean z = i == 1;
            if (AlcsClientWrapper.this.clientNotify != null) {
                AlcsClientWrapper.this.clientNotify.onServerStateChange(z);
            }
        }

        @Override // com.aliyun.alink.linksdk.alcs.lpbs.listener.PalDeviceStateListener
        public void onDeviceStateChange(PalDeviceInfo palDeviceInfo, int i) {
            ALog.d(AlcsClientWrapper.TAG, "onDeviceStateChange(), state = " + i);
            boolean z = i == 1;
            if (AlcsClientWrapper.this.clientNotify != null) {
                AlcsClientWrapper.this.clientNotify.onServerStateChange(z);
            }
        }
    }

    @Override // com.aliyun.linksdk.alcs.IAlcsClient
    public void setNotifyListener(IClientNotify iClientNotify) {
        this.clientNotify = iClientNotify;
    }
}
