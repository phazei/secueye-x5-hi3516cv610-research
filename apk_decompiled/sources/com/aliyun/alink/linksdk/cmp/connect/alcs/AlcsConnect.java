package com.aliyun.alink.linksdk.cmp.connect.alcs;

import android.content.Context;
import android.text.TextUtils;
import com.aliyun.alink.linksdk.alcs.api.client.IDeviceHandler;
import com.aliyun.alink.linksdk.alcs.api.utils.ErrorInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalRspMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener;
import com.aliyun.alink.linksdk.cmp.core.base.AConnect;
import com.aliyun.alink.linksdk.cmp.core.base.AConnectConfig;
import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.CmpError;
import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectAuth;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectInitListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSubscribeListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectUnscribeListener;
import com.aliyun.alink.linksdk.cmp.core.util.CallbackHelper;
import com.aliyun.alink.linksdk.cmp.core.util.ClassSwitchHelper;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.linksdk.alcs.AlcsCmpSDK;
import com.aliyun.linksdk.alcs.IAlcsClient;
import com.aliyun.linksdk.alcs.IClientNotify;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes2.dex */
public class AlcsConnect extends AConnect implements IConnectAuth<Map<String, String>> {
    public static final String PerformanceTag = "PerformanceTag";
    private static final String TAG = "AlcsConnect";
    private IAlcsClient alcsClient = null;
    private List<CacheAction> cacheActionList = null;

    /* JADX INFO: renamed from: config, reason: collision with root package name */
    private AlcsConnectConfig f4196config;
    private Context context;
    private IConnectInitListener initListener;

    private enum ActionEnum {
        Send,
        Subscribe,
        Unsubscribe
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.base.AConnect
    public void init(Context context, AConnectConfig aConnectConfig, IConnectInitListener iConnectInitListener) {
        ALog.d(TAG, "init()");
        if (context == null || aConnectConfig == null || !(aConnectConfig instanceof AlcsConnectConfig) || !aConnectConfig.checkVaild()) {
            ALog.d(TAG, "init()，params error");
            CallbackHelper.paramError(iConnectInitListener, "init, cxt or config is invalid");
            return;
        }
        this.context = context;
        AlcsConnectConfig alcsConnectConfig = (AlcsConnectConfig) aConnectConfig;
        this.f4196config = alcsConnectConfig;
        this.initListener = iConnectInitListener;
        updateConnectState(ConnectState.CONNECTING);
        if (alcsConnectConfig.isNeedAuthInfo()) {
            iConnectInitListener.onPrepareAuth(this);
        } else {
            initClientConnect();
        }
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.base.AConnect
    public void onDestroy() {
        ALog.d(TAG, "onDestroy()");
        IAlcsClient iAlcsClient = this.alcsClient;
        if (iAlcsClient != null) {
            iAlcsClient.destroy();
            this.alcsClient = null;
        }
        updateConnectState(ConnectState.DISCONNECTED);
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.base.AConnect
    public ConnectState getConnectState() {
        if (this.alcsClient != null && this.connectState != ConnectState.CONNECTING) {
            if (this.alcsClient.isServerOnline()) {
                updateConnectState(ConnectState.CONNECTED);
            } else {
                updateConnectState(ConnectState.DISCONNECTED);
            }
        }
        return this.connectState;
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectAuth
    public void onPrepareAuthFail(AError aError) {
        ALog.d(TAG, "onPrepareFail()");
        IConnectInitListener iConnectInitListener = this.initListener;
        if (iConnectInitListener != null) {
            iConnectInitListener.onFailure(aError);
        }
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectAuth
    public void onAuth(Map<String, String> map) {
        ALog.d(TAG, "auth()");
        if (map == null || !map.containsKey("PK") || !map.containsKey("DN") || !map.containsKey("TOKEN") || !map.containsKey("KEY")) {
            IConnectInitListener iConnectInitListener = this.initListener;
            if (iConnectInitListener != null) {
                iConnectInitListener.onFailure(CmpError.CONNECT_AUTH_INFO_ERROR());
                return;
            }
            return;
        }
        this.f4196config.setProductKey(map.get("PK"));
        this.f4196config.setDeviceName(map.get("DN"));
        this.f4196config.setAccessKey(map.get("KEY"));
        this.f4196config.setAccessToken(map.get("TOKEN"));
        initClientConnect();
    }

    private void initClientConnect() {
        ALog.d(TAG, "initClientConnect()");
        this.alcsClient = AlcsCmpSDK.initClientConnect(ClassSwitchHelper.alcsConfigTransfer(this.f4196config), new IDeviceHandler() { // from class: com.aliyun.alink.linksdk.cmp.connect.alcs.AlcsConnect.1
            @Override // com.aliyun.alink.linksdk.alcs.api.client.IDeviceHandler
            public void onSuccess(Object obj) {
                ALog.d(AlcsConnect.TAG, "initClientConnect(), onSuccess connectId:" + AlcsConnect.this.connectId);
                AlcsConnect.this.updateConnectState(ConnectState.CONNECTED);
                if (AlcsConnect.this.initListener != null) {
                    AlcsConnect.this.initListener.onSuccess();
                }
                AlcsConnect.this.handleCacheActions(true);
            }

            @Override // com.aliyun.alink.linksdk.alcs.api.client.IDeviceHandler
            public void onFail(Object obj, ErrorInfo errorInfo) {
                String str;
                ALog.d(AlcsConnect.TAG, "initClientConnect(), onFail connectId:" + AlcsConnect.this.connectId);
                AlcsConnect.this.updateConnectState(ConnectState.CONNECTFAIL);
                if (AlcsConnect.this.initListener != null) {
                    CmpError cmpErrorALCS_INIT_ERROR = CmpError.ALCS_INIT_ERROR();
                    cmpErrorALCS_INIT_ERROR.setSubCode(errorInfo != null ? errorInfo.getErrorCode() : 0);
                    if (errorInfo != null) {
                        str = errorInfo.getErrorCode() + "," + errorInfo.getErrorMsg();
                    } else {
                        str = "";
                    }
                    cmpErrorALCS_INIT_ERROR.setSubMsg(str);
                    AlcsConnect.this.initListener.onFailure(cmpErrorALCS_INIT_ERROR);
                }
                AlcsConnect.this.handleCacheActions(false);
            }
        });
        this.alcsClient.setNotifyListener(new IClientNotify() { // from class: com.aliyun.alink.linksdk.cmp.connect.alcs.AlcsConnect.2
            @Override // com.aliyun.linksdk.alcs.IClientNotify
            public void onServerStateChange(boolean z) {
                ALog.d(AlcsConnect.TAG, "onServerStateChange(), isConnected = " + z);
                if (z) {
                    AlcsConnect.this.updateConnectState(ConnectState.CONNECTED);
                } else {
                    AlcsConnect.this.updateConnectState(ConnectState.DISCONNECTED);
                }
            }

            @Override // com.aliyun.linksdk.alcs.IClientNotify
            public void onNotify(String str, PalRspMessage palRspMessage) {
                ALog.d(AlcsConnect.TAG, "onNotify(), topic  = " + str);
                if (AlcsConnect.this.notifyListener == null) {
                    return;
                }
                try {
                    str = new URI(str).getPath();
                } catch (Exception e) {
                    ALog.d(AlcsConnect.TAG, "onNotify(), e = " + e.toString());
                }
                ALog.d(AlcsConnect.TAG, "onNotify(), path = " + str);
                if (AlcsConnect.this.notifyListener.shouldHandle(AlcsConnect.this.getConnectId(), str)) {
                    ALog.d(AlcsConnect.TAG, "onNotify(), notify");
                    AMessage aMessage = new AMessage();
                    aMessage.setData(palRspMessage.payload);
                    AlcsConnect.this.notifyListener.onNotify(AlcsConnect.this.getConnectId(), str, aMessage);
                }
            }
        });
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.base.AConnect
    public void send(final ARequest aRequest, final IConnectSendListener iConnectSendListener) {
        ALog.d(TAG, "send()");
        if (this.alcsClient == null || !(this.connectState == ConnectState.CONNECTED || this.connectState == ConnectState.CONNECTING)) {
            if (iConnectSendListener != null) {
                iConnectSendListener.onFailure(aRequest, CmpError.CONNECT_FAIL_DISCONNECT());
            }
        } else {
            if (this.connectState == ConnectState.CONNECTING) {
                cacheAction(ActionEnum.Send, aRequest, iConnectSendListener);
                return;
            }
            CoAPRequest coAPRequest = (CoAPRequest) aRequest;
            final String str = coAPRequest.traceId != null ? coAPRequest.traceId : "";
            String str2 = coAPRequest.alinkIdForTracker != null ? coAPRequest.alinkIdForTracker : "";
            if (!TextUtils.isEmpty(str)) {
                ALog.d("PerformanceTag", "{\"mod\":\"cmp\",\"tunnel\":\"alcs\",\"id\":\"_id_\",\"alinkid\":\"_alinkid_\",\"event\":\"req\"}".replace("_id_", str).replace("_alinkid_", str2));
            }
            this.alcsClient.sendRequest(coAPRequest.isSecurity, ClassSwitchHelper.alcsRequestToIotReqMsg(this.f4196config.getProductKey(), this.f4196config.getDeviceName(), coAPRequest), new PalMsgListener() { // from class: com.aliyun.alink.linksdk.cmp.connect.alcs.AlcsConnect.3
                @Override // com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener
                public void onLoad(PalRspMessage palRspMessage) {
                    if (iConnectSendListener == null) {
                        return;
                    }
                    if (palRspMessage == null || palRspMessage.code != 0) {
                        if (!TextUtils.isEmpty(str)) {
                            ALog.d("PerformanceTag", "{\"mod\":\"cmp\",\"tunnel\":\"alcs\",\"id\":\"_id_\",\"event\":\"fail\"}".replace("_id_", str));
                        }
                        iConnectSendListener.onFailure(aRequest, CmpError.ALCS_SEND_FAIL(palRspMessage.code));
                    } else {
                        if (!TextUtils.isEmpty(str)) {
                            ALog.d("PerformanceTag", "{\"mod\":\"cmp\",\"tunnel\":\"alcs\",\"id\":\"_id_\",\"event\":\"res\"}".replace("_id_", str));
                        }
                        iConnectSendListener.onResponse(aRequest, ClassSwitchHelper.IotResTransfer(palRspMessage));
                    }
                }
            });
        }
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.base.AConnect
    public void subscribe(ARequest aRequest, final IConnectSubscribeListener iConnectSubscribeListener) {
        ALog.d(TAG, "subscribe()");
        if (this.alcsClient == null || !(this.connectState == ConnectState.CONNECTED || this.connectState == ConnectState.CONNECTING)) {
            if (iConnectSubscribeListener != null) {
                iConnectSubscribeListener.onFailure(CmpError.CONNECT_FAIL_DISCONNECT());
            }
        } else {
            if (this.connectState == ConnectState.CONNECTING) {
                cacheAction(ActionEnum.Subscribe, aRequest, iConnectSubscribeListener);
                return;
            }
            CoAPRequest coAPRequest = (CoAPRequest) aRequest;
            this.alcsClient.subscribe(coAPRequest.isSecurity, ClassSwitchHelper.alcsRequestToIotSubMsg(this.f4196config.getProductKey(), this.f4196config.getDeviceName(), coAPRequest), new PalMsgListener() { // from class: com.aliyun.alink.linksdk.cmp.connect.alcs.AlcsConnect.4
                @Override // com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener
                public void onLoad(PalRspMessage palRspMessage) {
                    if (iConnectSubscribeListener != null) {
                        if (palRspMessage.code == 0) {
                            iConnectSubscribeListener.onSuccess();
                            return;
                        }
                        CmpError cmpErrorALCS_SUBSCRIBE_FAIL = CmpError.ALCS_SUBSCRIBE_FAIL();
                        cmpErrorALCS_SUBSCRIBE_FAIL.setSubMsg(String.valueOf(palRspMessage.code));
                        iConnectSubscribeListener.onFailure(cmpErrorALCS_SUBSCRIBE_FAIL);
                    }
                }
            });
        }
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.base.AConnect
    public void unsubscribe(ARequest aRequest, final IConnectUnscribeListener iConnectUnscribeListener) {
        ALog.d(TAG, "unsubscribe()");
        if (aRequest == null || this.alcsClient == null || !(this.connectState == ConnectState.CONNECTED || this.connectState == ConnectState.CONNECTING)) {
            if (iConnectUnscribeListener != null) {
                iConnectUnscribeListener.onFailure(CmpError.CONNECT_FAIL_DISCONNECT());
            }
        } else {
            if (this.connectState == ConnectState.CONNECTING) {
                cacheAction(ActionEnum.Unsubscribe, aRequest, iConnectUnscribeListener);
                return;
            }
            try {
                this.alcsClient.unsubscribe(((CoAPRequest) aRequest).isSecurity, ClassSwitchHelper.alcsRequestToIotSubMsg(this.f4196config.getProductKey(), this.f4196config.getDeviceName(), (CoAPRequest) aRequest), new PalMsgListener() { // from class: com.aliyun.alink.linksdk.cmp.connect.alcs.AlcsConnect.5
                    @Override // com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener
                    public void onLoad(PalRspMessage palRspMessage) {
                        if (palRspMessage != null && palRspMessage.code == 0) {
                            ALog.d(AlcsConnect.TAG, "unsubscribe(),onSuccess");
                            IConnectUnscribeListener iConnectUnscribeListener2 = iConnectUnscribeListener;
                            if (iConnectUnscribeListener2 != null) {
                                iConnectUnscribeListener2.onSuccess();
                                return;
                            }
                            return;
                        }
                        ALog.d(AlcsConnect.TAG, "unsubscribe(),onFail");
                        if (iConnectUnscribeListener == null) {
                            return;
                        }
                        CmpError cmpErrorALCS_UNSUBSCRIBE_FAIL = CmpError.ALCS_UNSUBSCRIBE_FAIL();
                        cmpErrorALCS_UNSUBSCRIBE_FAIL.setSubMsg(palRspMessage != null ? String.valueOf(palRspMessage.code) : "1");
                        iConnectUnscribeListener.onFailure(cmpErrorALCS_UNSUBSCRIBE_FAIL);
                    }
                });
            } catch (Exception e) {
                ALog.d(TAG, "unsubscribe(), error" + e.toString());
                e.printStackTrace();
            }
        }
    }

    private class CacheAction {
        public ActionEnum action;
        public Object listener;
        public ARequest request;

        private CacheAction() {
        }
    }

    private void cacheAction(ActionEnum actionEnum, ARequest aRequest, Object obj) {
        ALog.d(TAG, "cacheAction");
        CacheAction cacheAction = new CacheAction();
        cacheAction.action = actionEnum;
        cacheAction.request = aRequest;
        cacheAction.listener = obj;
        if (this.cacheActionList == null) {
            this.cacheActionList = new CopyOnWriteArrayList();
        }
        this.cacheActionList.add(cacheAction);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCacheActions(boolean z) {
        ALog.d(TAG, "handleCacheActions(),isConnect = " + z);
        List<CacheAction> list = this.cacheActionList;
        if (list == null || list.size() == 0) {
            return;
        }
        for (CacheAction cacheAction : this.cacheActionList) {
            ALog.d(TAG, "handleCacheActions(),item");
            if (cacheAction == null) {
                ALog.e(TAG, "handleCacheActions(),action null");
            } else {
                switch (cacheAction.action) {
                    case Send:
                        if (!z && cacheAction.listener != null) {
                            ((IConnectSendListener) cacheAction.listener).onFailure(cacheAction.request, CmpError.CONNECT_FAIL_DISCONNECT());
                        } else {
                            send(cacheAction.request, (IConnectSendListener) cacheAction.listener);
                        }
                        break;
                    case Subscribe:
                        if (!z && cacheAction.listener != null) {
                            ((IConnectSubscribeListener) cacheAction.listener).onFailure(CmpError.CONNECT_FAIL_DISCONNECT());
                        } else {
                            subscribe(cacheAction.request, (IConnectSubscribeListener) cacheAction.listener);
                        }
                        break;
                    case Unsubscribe:
                        if (!z && cacheAction.listener != null) {
                            ((IConnectUnscribeListener) cacheAction.listener).onFailure(CmpError.CONNECT_FAIL_DISCONNECT());
                        } else {
                            unsubscribe(cacheAction.request, (IConnectUnscribeListener) cacheAction.listener);
                        }
                        break;
                }
            }
        }
        this.cacheActionList.clear();
        this.cacheActionList = null;
    }
}
