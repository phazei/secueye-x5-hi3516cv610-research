package com.alibaba.sdk.android.openaccount.initialization.impl;

import android.content.Context;
import anet.channel.strategy.dispatch.DispatchConstants;
import com.alibaba.sdk.android.openaccount.Environment;
import com.alibaba.sdk.android.openaccount.OpenAccountConstants;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.config.EnvironmentChangeListener;
import com.alibaba.sdk.android.openaccount.device.DeviceManager;
import com.alibaba.sdk.android.openaccount.initialization.InitializationHandler;
import com.alibaba.sdk.android.openaccount.initialization.InitializationServiceClient;
import com.alibaba.sdk.android.openaccount.network.ConnectType;
import com.alibaba.sdk.android.openaccount.network.MobileNetworkType;
import com.alibaba.sdk.android.openaccount.rpc.RpcService;
import com.alibaba.sdk.android.openaccount.rpc.model.RpcRequest;
import com.alibaba.sdk.android.openaccount.rpc.model.RpcResponse;
import com.alibaba.sdk.android.openaccount.rpc.mtop.MtopMtopRpcServiceImpl;
import com.alibaba.sdk.android.openaccount.security.SecurityGuardService;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.util.NetworkUtils;
import com.alibaba.sdk.android.pluto.Pluto;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import com.facebook.share.internal.ShareConstants;
import com.taobao.accs.utl.BaseMonitor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class DefaultInitializationServiceClientImpl implements InitializationServiceClient, EnvironmentChangeListener {
    private static final boolean DEBUG = false;
    private static final String TAG = "DefaultInitializationServiceClientImpl";

    @Autowired
    private DeviceManager deviceManager;
    private long lastInitTime;

    @Autowired
    private RpcService rpcService;

    @Autowired
    private SecurityGuardService securityGuardService;

    public void setRpcService(RpcService rpcService) {
        this.rpcService = rpcService;
    }

    @Override // com.alibaba.sdk.android.openaccount.config.EnvironmentChangeListener
    public void onEnvironmentChange(Environment environment, Environment environment2) {
        request();
    }

    @Override // com.alibaba.sdk.android.openaccount.initialization.InitializationServiceClient
    public void request() {
        InitializationHandler[] initializationHandlerArr = (InitializationHandler[]) Pluto.DEFAULT_INSTANCE.getBeans(InitializationHandler.class);
        if (initializationHandlerArr.length == 0) {
            return;
        }
        request(initializationHandlerArr);
    }

    @Override // com.alibaba.sdk.android.openaccount.initialization.InitializationServiceClient
    public void request(InitializationHandler... initializationHandlerArr) {
        JSONObject jSONObject_request;
        int requestServiceType;
        ArrayList arrayList = new ArrayList(initializationHandlerArr.length);
        boolean z = false;
        int i = 0;
        for (InitializationHandler initializationHandler : initializationHandlerArr) {
            int requestRequirement = initializationHandler.getRequestRequirement();
            if (requestRequirement != 2 && (requestServiceType = initializationHandler.getRequestServiceType()) != 0) {
                if (!z && requestRequirement == 1) {
                    z = true;
                }
                i |= requestServiceType;
                arrayList.add(initializationHandler);
            }
        }
        if (arrayList.size() == 0 || !z || (jSONObject_request = _request(arrayList, i)) == null) {
            return;
        }
        for (InitializationHandler initializationHandler2 : arrayList) {
            try {
                initializationHandler2.handleResponseValue(jSONObject_request.optJSONObject(initializationHandler2.getResponseValueKey()));
            } catch (Exception e) {
                AliSDKLogger.e(TAG, "Fail to execute the handler " + initializationHandler2 + ", the error message is " + e.getMessage(), e);
            }
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.initialization.InitializationServiceClient
    public <T> T request(InitializationHandler<T> initializationHandler, Class<T> cls, InitializationHandler... initializationHandlerArr) {
        int requestServiceType;
        int requestServiceType2 = initializationHandler.getRequestServiceType();
        if (requestServiceType2 == 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList(initializationHandlerArr.length + 1);
        arrayList.add(initializationHandler);
        int i = requestServiceType2;
        for (InitializationHandler initializationHandler2 : initializationHandlerArr) {
            if (initializationHandler2 != null && (requestServiceType = initializationHandler2.getRequestServiceType()) != 0) {
                i |= requestServiceType;
                arrayList.add(initializationHandler2);
            }
        }
        JSONObject jSONObject_request = _request(arrayList, i);
        if (jSONObject_request == null) {
            return null;
        }
        for (InitializationHandler initializationHandler3 : initializationHandlerArr) {
            try {
                initializationHandler3.handleResponseValue(jSONObject_request.optJSONObject(initializationHandler3.getResponseValueKey()));
            } catch (Exception e) {
                AliSDKLogger.e(TAG, "Fail to execute the handler " + initializationHandler3 + ", the error message is " + e.getMessage(), e);
            }
        }
        try {
            return initializationHandler.handleResponseValue(jSONObject_request.optJSONObject(initializationHandler.getResponseValueKey()));
        } catch (Exception e2) {
            AliSDKLogger.e(TAG, "Fail to invoke the handler " + initializationHandler + ", the error message is " + e2.getMessage(), e2);
            return null;
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.initialization.InitializationServiceClient
    public <T> T request(InitializationHandler<T> initializationHandler, Class<T> cls) {
        JSONObject jSONObject_request;
        int requestServiceType = initializationHandler.getRequestServiceType();
        if (requestServiceType == 0 || (jSONObject_request = _request(Collections.singletonList(initializationHandler), requestServiceType)) == null) {
            return null;
        }
        try {
            return initializationHandler.handleResponseValue(jSONObject_request.optJSONObject(initializationHandler.getResponseValueKey()));
        } catch (Exception e) {
            AliSDKLogger.e(TAG, "Fail to invoke the handler " + initializationHandler + ", the error message is " + e.getMessage(), e);
            return null;
        }
    }

    private JSONObject _request(List<InitializationHandler> list, int i) {
        try {
            HashMap map = new HashMap();
            map.put("context", getSysInfo(i));
            for (InitializationHandler initializationHandler : list) {
                map.put(initializationHandler.getRequestParameterKey(), initializationHandler.createRequestParameters());
            }
            RpcRequest rpcRequest = new RpcRequest();
            rpcRequest.params = new HashMap();
            rpcRequest.params.put(ShareConstants.WEB_DIALOG_RESULT_PARAM_REQUEST_ID, map);
            rpcRequest.target = BaseMonitor.ALARM_POINT_CONNECT;
            if (this.rpcService == null) {
                this.rpcService = MtopMtopRpcServiceImpl.INSTANCE;
            }
            RpcResponse rpcResponseInvoke = this.rpcService.invoke(rpcRequest);
            if (rpcResponseInvoke != null && rpcResponseInvoke.data != null) {
                if (rpcResponseInvoke.code != 1) {
                    AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "fail to request init server, the error code is " + rpcResponseInvoke.code + " the error message is " + rpcResponseInvoke.message);
                    return null;
                }
                for (InitializationHandler initializationHandler2 : list) {
                    JSONObject jSONObjectOptJSONObject = rpcResponseInvoke.data.optJSONObject(initializationHandler2.getResponseValueKey());
                    if (jSONObjectOptJSONObject != null) {
                        initializationHandler2.handleResponseValue(jSONObjectOptJSONObject);
                    }
                }
            }
        } catch (Exception e) {
            AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "fail to process the current init request", e);
        }
        return null;
    }

    private void handleResponseError(int i, String str, List<InitializationHandler> list) {
        Iterator<InitializationHandler> it = list.iterator();
        while (it.hasNext()) {
            try {
                it.next().handleResponseError(i, str);
            } catch (Exception unused) {
            }
        }
    }

    private JSONObject getSysInfo(int i) {
        try {
            JSONObject jSONObject = new JSONObject();
            Context androidContext = OpenAccountSDK.getAndroidContext();
            jSONObject.put("sdkVersion", OpenAccountSDK.getVersion().toString());
            jSONObject.put("utDid", this.deviceManager.getUtdid());
            jSONObject.put("platformName", DispatchConstants.ANDROID);
            String str = "unknown";
            ConnectType connectType = NetworkUtils.getConnectType(androidContext);
            if (connectType == ConnectType.CONNECT_TYPE_WIFI) {
                str = "wifi";
            } else if (connectType == ConnectType.CONNECT_TYPE_MOBILE) {
                MobileNetworkType mobileNetworkType = NetworkUtils.getMobileNetworkType(androidContext);
                if (mobileNetworkType == MobileNetworkType.MOBILE_NETWORK_TYPE_2G) {
                    str = "2g";
                } else if (mobileNetworkType == MobileNetworkType.MOBILE_NETWORK_TYPE_3G) {
                    str = "3g";
                } else if (mobileNetworkType == MobileNetworkType.MOBILE_NETWORK_TYPE_4G) {
                    str = "4g";
                }
            }
            jSONObject.put("netType", str);
            jSONObject.put("appKey", this.securityGuardService.getAppKey());
            jSONObject.put("yunOSId", this.deviceManager.getYunOSDeviceId());
            String property = OpenAccountSDK.getProperty("appVersion");
            if (property == null) {
                try {
                    property = androidContext.getPackageManager().getPackageInfo(androidContext.getPackageName(), 0).versionName;
                } catch (Exception unused) {
                }
            }
            jSONObject.put("appVersion", property);
            jSONObject.put("appAuthToken", this.securityGuardService.getSecurityToken());
            jSONObject.put("securityToken", this.securityGuardService.getSecurityToken());
            return jSONObject;
        } catch (Throwable th) {
            AliSDKLogger.e(TAG, "Fail to get sys info, the error message is " + th.getMessage(), th);
            return null;
        }
    }
}
