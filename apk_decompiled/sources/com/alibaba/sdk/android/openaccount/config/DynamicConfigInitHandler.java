package com.alibaba.sdk.android.openaccount.config;

import android.content.Context;
import com.alibaba.sdk.android.openaccount.OpenAccountConstants;
import com.alibaba.sdk.android.openaccount.initialization.InitializationHandler;
import com.alibaba.sdk.android.openaccount.initialization.InitializationServiceClient;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.alibaba.sdk.android.openaccount.ut.UserTrackerService;
import com.alibaba.sdk.android.openaccount.util.CommonUtils;
import com.alibaba.sdk.android.pluto.Pluto;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class DynamicConfigInitHandler implements InitializationHandler<Void> {
    private static final String CONFIG_CURRENT_VERSION = "configCurrentVersion";
    private static final String CONFIG_EXPIRE_IN_SECONDS = "configExpireInSeconds";
    private static final String CONFIG_LAST_FETCH_TIMESTAMP = "configLastFetchTimestamp";
    private static final String CONFIG_POLLING_ENABLED = "configPollingEnabled";
    private static final String CONFIG_POLLING_INTERVAL_SECONDS = "configPollingIntervalSeconds";
    private static final String CONFIG_REQUEST_KEY = "config";
    private static final long DEFAULT_CONFIG_POLLING_INTERVAL_SECONDS = 86400;
    private static final String FETCH_CONFIGS_ON_INIT = "fetchConfigsOnInit";
    private static final String FETCH_CONFIGS_ON_RPC = "fetchConfigsOnRpc";
    private static final String TAG = "oa_config";
    private volatile Timer configPollTimer;

    @Autowired
    private ConfigService configurations;
    private volatile long currentConfigPollInterval;
    private volatile boolean forceQueryConfigOnce = false;
    private volatile boolean initRequest = true;

    @Autowired
    private UserTrackerService userTrackerService;

    @Override // com.alibaba.sdk.android.openaccount.initialization.InitializationHandler
    public String getRequestParameterKey() {
        return "config";
    }

    @Override // com.alibaba.sdk.android.openaccount.initialization.InitializationHandler
    public int getRequestServiceType() {
        return 8;
    }

    @Override // com.alibaba.sdk.android.openaccount.initialization.InitializationHandler
    public String getResponseValueKey() {
        return "config";
    }

    @Override // com.alibaba.sdk.android.openaccount.initialization.InitializationHandler
    public void handleResponseError(int i, String str) {
    }

    public void init(Context context) {
        startConfigFetchThread();
        this.configurations.registerPropertyChangeListener(new String[]{CONFIG_POLLING_INTERVAL_SECONDS, CONFIG_POLLING_ENABLED}, new PropertyChangeListener() { // from class: com.alibaba.sdk.android.openaccount.config.DynamicConfigInitHandler.1
            @Override // com.alibaba.sdk.android.openaccount.config.PropertyChangeListener
            public synchronized void propertyChanged(String str, String str2, String str3) {
                if (DynamicConfigInitHandler.CONFIG_POLLING_ENABLED.equals(str)) {
                    if (!"true".equalsIgnoreCase(str3)) {
                        if (DynamicConfigInitHandler.this.configPollTimer != null) {
                            if (AliSDKLogger.isDebugEnabled()) {
                                AliSDKLogger.d(DynamicConfigInitHandler.TAG, "config poll is disabled, will stop the timer");
                            }
                            DynamicConfigInitHandler.this.configPollTimer.cancel();
                            DynamicConfigInitHandler.this.configPollTimer = null;
                        }
                    } else {
                        DynamicConfigInitHandler.this.startConfigFetchThread();
                    }
                } else if (DynamicConfigInitHandler.CONFIG_POLLING_INTERVAL_SECONDS.equals(str) && str3 != null && !str3.equals(String.valueOf(DynamicConfigInitHandler.this.currentConfigPollInterval))) {
                    if (AliSDKLogger.isDebugEnabled()) {
                        AliSDKLogger.d(DynamicConfigInitHandler.TAG, "config poll interval changed, will restart the timer");
                    }
                    if (DynamicConfigInitHandler.this.configPollTimer != null) {
                        DynamicConfigInitHandler.this.configPollTimer.cancel();
                        DynamicConfigInitHandler.this.configPollTimer = null;
                    }
                    DynamicConfigInitHandler.this.startConfigFetchThread();
                }
            }
        });
    }

    @Override // com.alibaba.sdk.android.openaccount.initialization.InitializationHandler
    public JSONObject createRequestParameters() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("version", this.configurations.getIntProperty(CONFIG_CURRENT_VERSION, 0));
            jSONObject.put("lastModify", this.configurations.getLongProperty(CONFIG_LAST_FETCH_TIMESTAMP, 0L));
        } catch (JSONException e) {
            AliSDKLogger.e(TAG, "fail create config request parameters", e);
        }
        return jSONObject;
    }

    @Override // com.alibaba.sdk.android.openaccount.initialization.InitializationHandler
    public Void handleResponseValue(JSONObject jSONObject) {
        int iOptInt = jSONObject.optInt("code");
        if (iOptInt != 1) {
            if (iOptInt == 27552) {
                AliSDKLogger.d(OpenAccountConstants.LOG_TAG, "No Sdk configuration changed from server side");
                return null;
            }
            HashMap map = new HashMap();
            map.put("code", String.valueOf(iOptInt));
            map.put("traceId", jSONObject.optString("traceId"));
            map.put("msg", jSONObject.optString("message"));
            this.userTrackerService.sendCustomHit(UTConstants.E_SDK_CONNECT_RESULT, 19999, "config", 0L, UTConstants.E_SDK_CONNECT_CONFIG_FAILED, map);
            return null;
        }
        JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("data");
        if (jSONObjectOptJSONObject != null) {
            try {
                JSONObject jSONObjectOptJSONObject2 = jSONObjectOptJSONObject.optJSONObject("items");
                if (jSONObjectOptJSONObject2 == null) {
                    jSONObjectOptJSONObject2 = new JSONObject();
                }
                jSONObjectOptJSONObject2.put(CONFIG_CURRENT_VERSION, jSONObjectOptJSONObject.optInt("version"));
                jSONObjectOptJSONObject2.put(CONFIG_LAST_FETCH_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
                jSONObjectOptJSONObject2.put(CONFIG_EXPIRE_IN_SECONDS, String.valueOf(jSONObjectOptJSONObject.opt("expireInSeconds")));
                if (jSONObjectOptJSONObject2 != null) {
                    this.configurations.setDynamicProperties(jSONObjectOptJSONObject2.toString());
                }
            } catch (Exception e) {
                AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "fail to save the config response", e);
            }
        }
        HashMap map2 = new HashMap();
        map2.put("code", String.valueOf(iOptInt));
        map2.put("traceId", jSONObject.optString("traceId"));
        map2.put("msg", jSONObject.optString("message"));
        map2.put("version", jSONObjectOptJSONObject == null ? TmpConstant.GROUP_ROLE_UNKNOWN : jSONObjectOptJSONObject.optString("version"));
        this.userTrackerService.sendCustomHit(UTConstants.E_SDK_CONNECT_RESULT, 19999, "config", 0L, UTConstants.E_SDK_CONNECT_CONFIG_CHANGED, map2);
        return null;
    }

    @Override // com.alibaba.sdk.android.openaccount.initialization.InitializationHandler
    public int getRequestRequirement() {
        if (this.forceQueryConfigOnce) {
            this.forceQueryConfigOnce = false;
            return 1;
        }
        if (this.initRequest) {
            this.initRequest = false;
            if (this.configurations.getBooleanProperty(FETCH_CONFIGS_ON_INIT, false)) {
                return 1;
            }
        }
        return System.currentTimeMillis() - this.configurations.getLongProperty(CONFIG_LAST_FETCH_TIMESTAMP, 0L) > this.configurations.getLongProperty(CONFIG_EXPIRE_IN_SECONDS, 86400L) * 1000 ? 1 : 3;
    }

    public synchronized void startConfigFetchThread() {
        if (this.configPollTimer != null) {
            return;
        }
        if (CommonUtils.isApplicationDefaultProcess() == 1 && this.configurations.getBooleanProperty(CONFIG_POLLING_ENABLED, false)) {
            this.currentConfigPollInterval = this.configurations.getLongProperty(CONFIG_POLLING_INTERVAL_SECONDS, 86400L);
            this.configPollTimer = new Timer();
            this.configPollTimer.schedule(new ConfigPollTimerTask(), 5000L, this.currentConfigPollInterval * 1000);
        }
    }

    private class ConfigPollTimerTask extends TimerTask {
        private ConfigPollTimerTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            try {
                DynamicConfigInitHandler.this.forceQueryConfigOnce = true;
                InitializationServiceClient initializationServiceClient = (InitializationServiceClient) Pluto.DEFAULT_INSTANCE.getBean(InitializationServiceClient.class);
                if (initializationServiceClient == null) {
                    AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "No available Initialization client");
                    return;
                }
                if (AliSDKLogger.isDebugEnabled()) {
                    AliSDKLogger.d(DynamicConfigInitHandler.TAG, "config poll timer task is executing ... " + System.currentTimeMillis());
                }
                initializationServiceClient.request();
            } catch (Exception e) {
                AliSDKLogger.e("system", "Fail to post the config query, the error message is " + e.getMessage(), e);
            }
        }
    }
}
