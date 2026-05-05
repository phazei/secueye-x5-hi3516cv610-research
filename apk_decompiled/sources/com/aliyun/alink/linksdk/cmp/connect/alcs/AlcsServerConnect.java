package com.aliyun.alink.linksdk.cmp.connect.alcs;

import android.content.Context;
import android.text.TextUtils;
import com.aliyun.alink.linksdk.alcs.api.server.AlcsServerConfig;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPConstant;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPContext;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPRequest;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPResponse;
import com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPResHandler;
import com.aliyun.alink.linksdk.alcs.coap.resources.AlcsCoAPResource;
import com.aliyun.alink.linksdk.cmp.api.CommonResource;
import com.aliyun.alink.linksdk.cmp.api.ResourceRequest;
import com.aliyun.alink.linksdk.cmp.core.base.AConnectConfig;
import com.aliyun.alink.linksdk.cmp.core.base.AConnectOption;
import com.aliyun.alink.linksdk.cmp.core.base.AMultiportConnect;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResource;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.base.CmpError;
import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;
import com.aliyun.alink.linksdk.cmp.core.listener.IBaseListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectAuth;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectInitListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectResourceRegister;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSubscribeListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectUnscribeListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IResourceRequestListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IResourceResponseListener;
import com.aliyun.alink.linksdk.cmp.core.util.CallbackHelper;
import com.aliyun.alink.linksdk.cmp.core.util.ClassSwitchHelper;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.linksdk.alcs.AlcsCmpSDK;
import com.aliyun.linksdk.alcs.IAlcsServer;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class AlcsServerConnect extends AMultiportConnect implements IConnectResourceRegister, IResourceResponseListener, IConnectAuth<Map<String, String>> {
    public static final String CONNECT_ID = "LINK_ALCS_MULTIPORT";
    private static final String TAG = "AlcsMultiportConnect";
    private IAlcsServer alcsServer = null;

    /* JADX INFO: renamed from: config, reason: collision with root package name */
    private AlcsServerConnectConfig f4197config;
    private Context context;
    private IConnectInitListener initListener;

    @Override // com.aliyun.alink.linksdk.cmp.core.base.AConnect
    public void init(Context context, AConnectConfig aConnectConfig, IConnectInitListener iConnectInitListener) {
        ALog.d(TAG, "init()");
        if (context == null || aConnectConfig == null || !(aConnectConfig instanceof AlcsServerConnectConfig) || !aConnectConfig.checkVaild()) {
            ALog.d(TAG, "init()，params error");
            CallbackHelper.paramError(iConnectInitListener, "init, cxt or config is invalid");
            return;
        }
        this.connectId = CONNECT_ID;
        this.context = context;
        this.initListener = iConnectInitListener;
        AlcsServerConnectConfig alcsServerConnectConfig = (AlcsServerConnectConfig) aConnectConfig;
        this.f4197config = alcsServerConnectConfig;
        updateConnectState(ConnectState.CONNECTING);
        if (alcsServerConnectConfig.isNeedAuthInfo()) {
            iConnectInitListener.onPrepareAuth(this);
        } else {
            initAndStart();
        }
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.base.AConnect
    public void updateConnectOption(AConnectOption aConnectOption) {
        ALog.d(TAG, "updateConnectOption()");
        if (aConnectOption == null || !(aConnectOption instanceof AlcsServerConnectOption)) {
            return;
        }
        super.updateConnectOption(aConnectOption);
        if (this.alcsServer == null) {
        }
        AlcsServerConnectOption alcsServerConnectOption = (AlcsServerConnectOption) aConnectOption;
        String prefix = alcsServerConnectOption.getPrefix();
        String blackClients = alcsServerConnectOption.getBlackClients();
        String secrect = alcsServerConnectOption.getSecrect();
        switch (alcsServerConnectOption.getOptionFlag()) {
            case ADD_PREFIX_SECRET:
                if (!TextUtils.isEmpty(prefix) && !TextUtils.isEmpty(secrect)) {
                    this.alcsServer.addSvrAccessKey(prefix, secrect);
                    break;
                }
                break;
            case DELETE_PREFIX:
                if (!TextUtils.isEmpty(prefix)) {
                    this.alcsServer.removeSvrKey(prefix);
                }
                break;
            case UPDATE_BLACK_LIST:
                this.alcsServer.updateBlackList(blackClients);
                break;
        }
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
        if (map == null || !map.containsKey("PREFIX") || !map.containsKey("SECRET")) {
            IConnectInitListener iConnectInitListener = this.initListener;
            if (iConnectInitListener != null) {
                iConnectInitListener.onFailure(CmpError.CONNECT_AUTH_INFO_ERROR());
                return;
            }
            return;
        }
        this.f4197config.setPrefix(map.get("PREFIX"));
        this.f4197config.setSecret(map.get("SECRET"));
        initAndStart();
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.base.AConnect
    public void onDestroy() {
        ALog.d(TAG, "onDestroy()");
        IAlcsServer iAlcsServer = this.alcsServer;
        if (iAlcsServer != null) {
            iAlcsServer.stopServer();
        }
    }

    private void initAndStart() {
        ALog.d(TAG, "initAndStart()");
        try {
            AlcsCmpSDK.init(this.context);
            AlcsServerConfig.Builder builder = new AlcsServerConfig.Builder();
            builder.setProdKey(this.f4197config.getProductKey());
            builder.setDevcieName(this.f4197config.getDeviceName());
            builder.setBlackList(this.f4197config.getBlackClients());
            builder.addPrefixSec(this.f4197config.getPrefix(), this.f4197config.getSecret());
            builder.setUnsafePort(5683);
            this.alcsServer = AlcsCmpSDK.initServer(builder.build());
            this.alcsServer.startServer();
            IConnectInitListener iConnectInitListener = this.initListener;
            if (iConnectInitListener != null) {
                iConnectInitListener.onSuccess();
            }
            updateConnectState(ConnectState.CONNECTED);
        } catch (Exception e) {
            ALog.d(TAG, "init(),error");
            e.printStackTrace();
            if (this.initListener != null) {
                CmpError cmpErrorALCS_INIT_MULTIPORT_FAIL = CmpError.ALCS_INIT_MULTIPORT_FAIL();
                cmpErrorALCS_INIT_MULTIPORT_FAIL.setSubMsg(e.toString());
                this.initListener.onFailure(cmpErrorALCS_INIT_MULTIPORT_FAIL);
            }
            updateConnectState(ConnectState.CONNECTFAIL);
        }
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectResourceRegister
    public void registerResource(AResource aResource, IResourceRequestListener iResourceRequestListener) {
        ALog.d(TAG, "registerResource()");
        if (this.alcsServer == null) {
            return;
        }
        boolean z = aResource instanceof CoAPResource;
        if (!z && !(aResource instanceof CommonResource)) {
            if (iResourceRequestListener != null) {
                iResourceRequestListener.onFailure(CmpError.UNSUPPORT());
                return;
            }
            return;
        }
        CoAPResource coAPResourceCommonResToCoapRes = null;
        if (z) {
            coAPResourceCommonResToCoapRes = (CoAPResource) aResource;
        } else if (aResource instanceof CommonResource) {
            coAPResourceCommonResToCoapRes = ClassSwitchHelper.commonResToCoapRes((CommonResource) aResource);
        }
        AlcsCoAPResource alcsCoAPResource = new AlcsCoAPResource(coAPResourceCommonResToCoapRes.topic);
        alcsCoAPResource.setPath(coAPResourceCommonResToCoapRes.topic);
        alcsCoAPResource.setPermission(3);
        alcsCoAPResource.setHandler(new AlcsCoAPResHandler(aResource, coAPResourceCommonResToCoapRes.topic, iResourceRequestListener));
        this.alcsServer.registerAllResource(coAPResourceCommonResToCoapRes.isNeedAuth, alcsCoAPResource);
        if (iResourceRequestListener != null) {
            iResourceRequestListener.onSuccess();
        }
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IResourceResponseListener
    public void onResponse(AResource aResource, ResourceRequest resourceRequest, Object obj) {
        boolean z;
        boolean z2;
        AlcsCoAPResponse alcsCoAPResponseCreateResponse;
        ALog.d(TAG, "onResponse()");
        if (this.alcsServer == null) {
            ALog.d(TAG, "onResponse(),params error,alcsServer null, return");
            return;
        }
        if (aResource == null || !(((z = aResource instanceof CoAPResource)) || (aResource instanceof CommonResource))) {
            ALog.d(TAG, "onResponse(),params error,resource error, return");
            return;
        }
        if (resourceRequest == null) {
            ALog.d(TAG, "onResponse(),params error, resoucreReq is null");
            return;
        }
        if (resourceRequest.context == null || !(resourceRequest.context instanceof AlcsCoAPRequest)) {
            ALog.d(TAG, "onResponse(),params error,resoucre request context error, return" + resourceRequest.context);
            return;
        }
        if (obj == null || (!((z2 = obj instanceof AlcsCoAPResponse)) && !(obj instanceof AResponse))) {
            ALog.d(TAG, "onResponse(),params error,responseerror, return");
            return;
        }
        if (z2) {
            alcsCoAPResponseCreateResponse = (AlcsCoAPResponse) obj;
        } else {
            alcsCoAPResponseCreateResponse = AlcsCoAPResponse.createResponse((AlcsCoAPRequest) resourceRequest.context, AlcsCoAPConstant.ResponseCode.CONTENT);
            Object data = ((AResponse) obj).getData();
            if (data instanceof String) {
                alcsCoAPResponseCreateResponse.setPayload((String) data);
            } else if (data instanceof byte[]) {
                alcsCoAPResponseCreateResponse.setPayload((byte[]) data);
            } else {
                try {
                    alcsCoAPResponseCreateResponse.setPayload(data.toString());
                } catch (Exception e) {
                    ALog.w(TAG, "onResponse(), send , toString error," + e.toString());
                    return;
                }
            }
        }
        boolean z3 = false;
        if (z) {
            z3 = ((CoAPResource) aResource).isNeedAuth;
        } else if (aResource instanceof CommonResource) {
            z3 = ((CommonResource) aResource).isNeedAuth;
        }
        ALog.w(TAG, "onResponse(), exe sendResponse, isNeedAuth = " + z3);
        this.alcsServer.sendResponse(z3, alcsCoAPResponseCreateResponse);
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectResourceRegister
    public void unregisterResource(AResource aResource, IBaseListener iBaseListener) {
        ALog.d(TAG, "unregisterResource()");
        if (iBaseListener != null) {
            iBaseListener.onFailure(CmpError.UNSUPPORT());
        }
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectResourceRegister
    public void publishResource(AResource aResource, IBaseListener iBaseListener) {
        ALog.d(TAG, "publishResource");
        if (this.alcsServer == null) {
            return;
        }
        boolean z = aResource instanceof CoAPResource;
        if (!z && !(aResource instanceof CommonResource)) {
            if (iBaseListener != null) {
                iBaseListener.onFailure(CmpError.UNSUPPORT());
                return;
            }
            return;
        }
        CoAPResource coAPResourceCommonResToCoapRes = null;
        if (z) {
            coAPResourceCommonResToCoapRes = (CoAPResource) aResource;
        } else if (aResource instanceof CommonResource) {
            coAPResourceCommonResToCoapRes = ClassSwitchHelper.commonResToCoapRes((CommonResource) aResource);
        }
        this.alcsServer.publishResoucre(coAPResourceCommonResToCoapRes.topic, coAPResourceCommonResToCoapRes.payload);
        if (iBaseListener != null) {
            iBaseListener.onSuccess();
        }
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.base.AConnect
    public void send(ARequest aRequest, IConnectSendListener iConnectSendListener) {
        ALog.d(TAG, "send()");
        if (iConnectSendListener != null) {
            iConnectSendListener.onFailure(aRequest, CmpError.UNSUPPORT());
        }
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.base.AConnect
    public void subscribe(ARequest aRequest, IConnectSubscribeListener iConnectSubscribeListener) {
        ALog.d(TAG, "subscribe()");
        if (iConnectSubscribeListener != null) {
            iConnectSubscribeListener.onFailure(CmpError.UNSUPPORT());
        }
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.base.AConnect
    public void unsubscribe(ARequest aRequest, IConnectUnscribeListener iConnectUnscribeListener) {
        ALog.d(TAG, "unsubscribe()");
        if (iConnectUnscribeListener != null) {
            iConnectUnscribeListener.onFailure(CmpError.UNSUPPORT());
        }
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.base.AConnect
    public void setNotifyListener(IConnectNotifyListener iConnectNotifyListener) {
        ALog.d(TAG, "setNotifyListener()");
    }

    private class AlcsCoAPResHandler implements IAlcsCoAPResHandler {
        private AResource resource;
        private IResourceRequestListener resourceRequestListener;
        private String topic;

        public AlcsCoAPResHandler(AResource aResource, String str, IResourceRequestListener iResourceRequestListener) {
            this.resource = null;
            this.topic = null;
            this.resourceRequestListener = null;
            this.resource = aResource;
            this.topic = str;
            this.resourceRequestListener = iResourceRequestListener;
        }

        @Override // com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPResHandler
        public void onRecRequest(AlcsCoAPContext alcsCoAPContext, AlcsCoAPRequest alcsCoAPRequest) {
            ALog.d(AlcsServerConnect.TAG, "onRecRequest(),topic = " + this.topic);
            if (this.resourceRequestListener == null) {
                return;
            }
            ResourceRequest resourceRequest = new ResourceRequest();
            resourceRequest.topic = this.topic;
            if (alcsCoAPContext != null) {
                resourceRequest.payloadObj = alcsCoAPRequest.getPayload();
            }
            resourceRequest.context = alcsCoAPRequest;
            this.resourceRequestListener.onHandleRequest(this.resource, resourceRequest, AlcsServerConnect.this);
        }
    }
}
