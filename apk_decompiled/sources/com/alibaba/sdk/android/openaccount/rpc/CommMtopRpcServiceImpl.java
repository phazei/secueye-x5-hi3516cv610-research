package com.alibaba.sdk.android.openaccount.rpc;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.Environment;
import com.alibaba.sdk.android.openaccount.OpenAccountConfigs;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.OpenAccountSessionService;
import com.alibaba.sdk.android.openaccount.callback.InitResultCallback;
import com.alibaba.sdk.android.openaccount.config.ConfigService;
import com.alibaba.sdk.android.openaccount.config.EnvironmentChangeListener;
import com.alibaba.sdk.android.openaccount.device.DeviceManager;
import com.alibaba.sdk.android.openaccount.message.MessageConstants;
import com.alibaba.sdk.android.openaccount.message.MessageUtils;
import com.alibaba.sdk.android.openaccount.model.Result;
import com.alibaba.sdk.android.openaccount.mtop.OpenAccountMtopLoginProvider;
import com.alibaba.sdk.android.openaccount.rpc.model.RpcRequest;
import com.alibaba.sdk.android.openaccount.rpc.model.RpcResponse;
import com.alibaba.sdk.android.openaccount.security.SecurityGuardService;
import com.alibaba.sdk.android.openaccount.session.SessionManagerService;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.alibaba.sdk.android.openaccount.ut.UserTrackerService;
import com.alibaba.sdk.android.openaccount.util.CommonUtils;
import com.alibaba.sdk.android.openaccount.util.JSONUtils;
import com.alibaba.sdk.android.openaccount.util.TraceHelper;
import com.alibaba.sdk.android.pluto.Pluto;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.domain.EnvModeEnum;
import mtopsdk.mtop.domain.MethodEnum;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.global.SDKConfig;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.intf.MtopBuilder;
import mtopsdk.mtop.intf.MtopSetting;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class CommMtopRpcServiceImpl implements RpcService, EnvironmentChangeListener, InitResultCallback {
    public static final CommMtopRpcServiceImpl INSTANCE = new CommMtopRpcServiceImpl();
    private static final String TAG = "oa_rpc";

    @Autowired
    protected ConfigService configService;

    @Autowired
    private DeviceManager deviceManager;
    private boolean initMtop;
    private Mtop mtop;

    @Autowired
    private SecurityGuardService securityGuardService;

    @Autowired
    private UserTrackerService userTrackerService;
    private String vid;

    @Override // com.alibaba.sdk.android.openaccount.rpc.RpcService
    public String apiPrefix() {
        return "mtop.alibaba.openaccount.sdk.";
    }

    @Override // com.alibaba.sdk.android.openaccount.rpc.RpcService
    public void degradeDaily() {
    }

    protected void enableLog() {
    }

    protected void enableSupportOpenAccount() {
    }

    @Override // com.alibaba.sdk.android.openaccount.rpc.RpcService
    public String getSource() {
        return null;
    }

    @Override // com.alibaba.sdk.android.openaccount.rpc.RpcService
    public void registerSessionInfo(String str) {
    }

    public void init(Context context) {
        if (this.mtop != null) {
            return;
        }
        this.initMtop = !this.configService.getBooleanProperty("disableMtopInit", false);
        if (this.initMtop) {
            configureMtopAppKeyIndex();
            MtopSetting.setAppVersion(OpenAccountSDK.getVersion().toString());
            MtopSetting.setAuthCode(ConfigManager.getInstance().getSecurityImagePostfix());
            if (OpenAccountConfigs.enableOpenAccountMtopSession) {
                enableSupportOpenAccount();
            }
        }
        this.mtop = Mtop.instance(context, TraceHelper.clientTTID);
        if (this.initMtop) {
            this.mtop.switchEnvMode(toMtopEnvironment(this.configService.getEnvironment()));
            TBSdkLog.setTLogEnabled(false);
            TBSdkLog.setPrintLog(true);
            if (this.configService.isDebugEnabled()) {
                enableLog();
                this.mtop.logSwitch(true);
            }
        }
        if (OpenAccountConfigs.enableOpenAccountMtopSession) {
            new OpenAccountMtopLoginProvider();
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.callback.FailureCallback
    public void onFailure(int i, String str) {
        configureMtopAppKeyIndex();
    }

    @Override // com.alibaba.sdk.android.openaccount.callback.InitResultCallback
    public void onSuccess() {
        configureMtopAppKeyIndex();
    }

    @Override // com.alibaba.sdk.android.openaccount.config.EnvironmentChangeListener
    public void onEnvironmentChange(Environment environment, Environment environment2) {
        configureMtopAppKeyIndex();
        this.mtop.switchEnvMode(toMtopEnvironment(environment2));
        this.vid = null;
    }

    private void configureMtopAppKeyIndex() {
        if (this.configService.getEnvironment() == Environment.TEST) {
            MtopSetting.setAppKeyIndex(this.configService.getAppKeyIndex(Environment.TEST), this.configService.getAppKeyIndex(Environment.TEST));
        } else {
            MtopSetting.setAppKeyIndex(this.configService.getAppKeyIndex(Environment.ONLINE), this.configService.getAppKeyIndex(Environment.TEST));
        }
    }

    private EnvModeEnum toMtopEnvironment(Environment environment) {
        if (environment == Environment.ONLINE) {
            return EnvModeEnum.ONLINE;
        }
        if (environment == Environment.PRE) {
            return EnvModeEnum.PREPARE;
        }
        if (environment == Environment.TEST) {
            return EnvModeEnum.TEST;
        }
        return EnvModeEnum.TEST_SANDBOX;
    }

    public void setMtop(Mtop mtop) {
        this.mtop = mtop;
    }

    @Override // com.alibaba.sdk.android.openaccount.rpc.RpcService
    public RpcResponse invoke(RpcRequest rpcRequest) {
        if (!ConfigManager.getInstance().isSupportOfflineLogin() && !CommonUtils.isNetworkAvailable()) {
            RpcResponse rpcResponse = new RpcResponse();
            rpcResponse.code = MessageConstants.NETWORK_NOT_AVAILABLE;
            rpcResponse.message = MessageUtils.getMessageContent(MessageConstants.NETWORK_NOT_AVAILABLE, new Object[0]);
            return rpcResponse;
        }
        MtopRequest mtopRequest = new MtopRequest();
        if (rpcRequest.target.contains(".")) {
            String[] strArrSplit = rpcRequest.target.split("\\.");
            try {
                rpcRequest.target = strArrSplit[strArrSplit.length - 1];
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mtopRequest.setApiName(apiPrefix() + rpcRequest.target);
        mtopRequest.setVersion(rpcRequest.version);
        mtopRequest.setNeedEcode(false);
        mtopRequest.setNeedSession(false);
        try {
            JSONObject jSONObject = new JSONObject();
            for (Map.Entry<String, Object> entry : rpcRequest.params.entrySet()) {
                if (entry.getValue() instanceof Map) {
                    jSONObject.put(entry.getKey(), JSONUtils.toJson((Map) entry.getValue()));
                } else {
                    jSONObject.put(entry.getKey(), entry.getValue());
                }
            }
            mtopRequest.setData(jSONObject.toString());
            RpcResponse rpcResponseProcessMtopResponse = processMtopResponse(_intervalRequest(mtopRequest), System.currentTimeMillis() - System.currentTimeMillis());
            if (rpcResponseProcessMtopResponse.code == 26101) {
                Result<String> resultRefreshSession = ((OpenAccountSessionService) Pluto.DEFAULT_INSTANCE.getBean(OpenAccountSessionService.class)).refreshSession(true);
                if (resultRefreshSession.isSuccess()) {
                    return processMtopResponse(_intervalRequest(mtopRequest), System.currentTimeMillis() - System.currentTimeMillis());
                }
                AliSDKLogger.e(TAG, "fail to refresh session code : " + resultRefreshSession.code + ", rpc retry is skipped");
                return rpcResponseProcessMtopResponse;
            }
            int i = rpcResponseProcessMtopResponse.code;
            return rpcResponseProcessMtopResponse;
        } catch (Exception e2) {
            AliSDKLogger.e(TAG, "fail to execute rpc", e2);
            return null;
        }
    }

    private MtopResponse _intervalRequest(MtopRequest mtopRequest) {
        MtopBuilder mtopBuilderBuild = this.mtop.build(mtopRequest, (String) null);
        if (ConfigManager.getInstance().isSupportOfflineLogin()) {
            mtopBuilderBuild.retryTime(0);
        }
        mtopBuilderBuild.setConnectionTimeoutMilliSecond(ConfigManager.getInstance().getConnectionTimeoutMills());
        mtopBuilderBuild.setSocketTimeoutMilliSecond(ConfigManager.getInstance().getSocketTimeoutMillis());
        HashMap map = new HashMap();
        if (OpenAccountConfigs.extraRpcHttpHeaders != null) {
            map.putAll(OpenAccountConfigs.extraRpcHttpHeaders);
        }
        String str = this.vid;
        if (str != null) {
            map.put("vid", str);
        }
        if (getSource() != null) {
            map.put("source", getSource());
        }
        String sessionId = ((SessionManagerService) Pluto.DEFAULT_INSTANCE.getBean(SessionManagerService.class)).getSessionId();
        if (sessionId != null) {
            map.put("sid", sessionId);
        }
        mtopBuilderBuild.headers(map).reqMethod(MethodEnum.POST);
        long jCurrentTimeMillis = System.currentTimeMillis();
        MtopResponse mtopResponseSyncRequest = mtopBuilderBuild.syncRequest();
        long jCurrentTimeMillis2 = System.currentTimeMillis();
        if (AliSDKLogger.isDebugEnabled()) {
            AliSDKLogger.d(TAG, "MTOP Request Headers : " + map);
            AliSDKLogger.d(TAG, "MTOP Use AppKey : " + SDKConfig.getInstance().getGlobalAppKey());
            AliSDKLogger.d(TAG, "time=" + (jCurrentTimeMillis2 - jCurrentTimeMillis) + " ,mtopResponse: " + mtopResponseSyncRequest.toString());
        }
        return mtopResponseSyncRequest;
    }

    private RpcResponse processMtopResponse(MtopResponse mtopResponse, long j) {
        RpcResponse rpcResponse = new RpcResponse();
        if (mtopResponse.isApiSuccess()) {
            JSONObject dataJsonObject = mtopResponse.getDataJsonObject();
            if (dataJsonObject != null) {
                rpcResponse.code = dataJsonObject.optInt("code");
                rpcResponse.message = dataJsonObject.optString("message");
                rpcResponse.type = dataJsonObject.optString("type");
                rpcResponse.data = dataJsonObject.optJSONObject("data");
                if (rpcResponse.data == null) {
                    rpcResponse.arrayData = dataJsonObject.optJSONArray("data");
                }
                rpcResponse.subCode = dataJsonObject.optInt("subCode");
                rpcResponse.traceId = dataJsonObject.optString("traceId");
                String strOptString = JSONUtils.optString(dataJsonObject, "vid");
                if (strOptString != null) {
                    this.vid = strOptString;
                }
                String strOptString2 = JSONUtils.optString(dataJsonObject, "deviceId");
                if (!TextUtils.isEmpty(strOptString2)) {
                    this.deviceManager.setSdkDeviceId(strOptString2);
                }
                HashMap map = new HashMap();
                map.put("code", String.valueOf(rpcResponse.code));
                map.put("traceId", String.valueOf(rpcResponse.traceId));
                map.put("msg", String.valueOf(rpcResponse.message));
                this.userTrackerService.sendCustomHit(UTConstants.E_RPC_INVOCATION_RESULT, j, UTConstants.E_RPC_INVOCATION_SUCCESS, map);
            } else {
                HashMap map2 = new HashMap();
                map2.put("msg", "null biz data");
                this.userTrackerService.sendCustomHit(UTConstants.E_RPC_INVOCATION_RESULT, j, UTConstants.E_RPC_INVOCATION_FAILED, map2);
            }
        } else {
            rpcResponse.code = MessageConstants.GENERIC_RPC_ERROR;
            rpcResponse.message = MessageUtils.getMessageContent(MessageConstants.GENERIC_RPC_ERROR, mtopResponse.getRetCode());
            HashMap map3 = new HashMap();
            map3.put("httpCode", String.valueOf(mtopResponse.getResponseCode()));
            map3.put("code", mtopResponse.getRetCode());
            map3.put("msg", mtopResponse.getRetMsg());
            this.userTrackerService.sendCustomHit(UTConstants.E_RPC_INVOCATION_RESULT, j, UTConstants.E_RPC_INVOCATION_FAILED, map3);
            AliSDKLogger.e(TAG, "Rpc error message : " + mtopResponse.getRetMsg() + "  retCode : " + mtopResponse.getRetCode() + " responseCode : " + mtopResponse.getResponseCode());
        }
        normalizeRpcResponse(rpcResponse);
        return rpcResponse;
    }

    private void normalizeRpcResponse(RpcResponse rpcResponse) {
        if (rpcResponse != null && rpcResponse.code == 26251) {
            rpcResponse.code = RpcServerBizConstants.SESSION_EXPIRED;
        }
    }
}
