package com.alibaba.sdk.android.openaccount.session.impl;

import com.alibaba.sdk.android.openaccount.OpenAccountConstants;
import com.alibaba.sdk.android.openaccount.OpenAccountSessionService;
import com.alibaba.sdk.android.openaccount.initialization.InitializationHandler;
import com.alibaba.sdk.android.openaccount.initialization.InitializationServiceClient;
import com.alibaba.sdk.android.openaccount.message.Message;
import com.alibaba.sdk.android.openaccount.message.MessageConstants;
import com.alibaba.sdk.android.openaccount.message.MessageUtils;
import com.alibaba.sdk.android.openaccount.model.OpenAccountSession;
import com.alibaba.sdk.android.openaccount.model.RefreshToken;
import com.alibaba.sdk.android.openaccount.model.Result;
import com.alibaba.sdk.android.openaccount.model.ResultCode;
import com.alibaba.sdk.android.openaccount.model.SessionData;
import com.alibaba.sdk.android.openaccount.session.SessionManagerService;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.alibaba.sdk.android.openaccount.ut.UserTrackerService;
import com.alibaba.sdk.android.openaccount.util.OpenAccountUtils;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class SessionServiceImpl implements OpenAccountSessionService, InitializationHandler<ResultCode> {

    @Autowired
    private ExecutorService executorService;
    private volatile boolean forceRefreshOnce;

    @Autowired
    private InitializationServiceClient initializationServiceClient;

    @Autowired
    private SessionManagerService sessionManagerService;

    @Autowired
    private UserTrackerService userTrackerService;

    @Override // com.alibaba.sdk.android.openaccount.initialization.InitializationHandler
    public String getRequestParameterKey() {
        return "refreshSid";
    }

    @Override // com.alibaba.sdk.android.openaccount.initialization.InitializationHandler
    public int getRequestServiceType() {
        return 2;
    }

    @Override // com.alibaba.sdk.android.openaccount.initialization.InitializationHandler
    public String getResponseValueKey() {
        return "refreshSid";
    }

    @Override // com.alibaba.sdk.android.openaccount.initialization.InitializationHandler
    public void handleResponseError(int i, String str) {
    }

    @Override // com.alibaba.sdk.android.openaccount.OpenAccountSessionService
    public Result<String> getSessionId() {
        try {
            String sessionId = this.sessionManagerService.getSessionId();
            if (AliSDKLogger.isDebugEnabled()) {
                AliSDKLogger.d(OpenAccountConstants.LOG_TAG, "get sid: " + sessionId);
            }
            if (sessionId == null) {
                return Result.result(MessageUtils.createMessage(10011, new Object[0]));
            }
            return Result.result(sessionId);
        } catch (Exception e) {
            AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "fail to get session Id", e);
            return Result.result(MessageUtils.createMessage(MessageConstants.GENERIC_SYSTEM_ERROR, new Object[0]));
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0023 A[Catch: Throwable -> 0x0074, all -> 0x0092, TryCatch #1 {Throwable -> 0x0074, blocks: (B:10:0x001b, B:17:0x004a, B:19:0x0050, B:20:0x006e, B:12:0x0023, B:14:0x0030), top: B:32:0x001b, outer: #0 }] */
    @Override // com.alibaba.sdk.android.openaccount.OpenAccountSessionService
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized com.alibaba.sdk.android.openaccount.model.Result<java.lang.String> refreshSession(boolean r5) {
        /*
            r4 = this;
            monitor-enter(r4)
            com.alibaba.sdk.android.openaccount.session.SessionManagerService r0 = r4.sessionManagerService     // Catch: java.lang.Throwable -> L92
            boolean r0 = r0.isRefreshTokenExpired()     // Catch: java.lang.Throwable -> L92
            r1 = 0
            if (r0 == 0) goto L18
            r5 = 10011(0x271b, float:1.4028E-41)
            java.lang.Object[] r0 = new java.lang.Object[r1]     // Catch: java.lang.Throwable -> L92
            com.alibaba.sdk.android.openaccount.message.Message r5 = com.alibaba.sdk.android.openaccount.message.MessageUtils.createMessage(r5, r0)     // Catch: java.lang.Throwable -> L92
            com.alibaba.sdk.android.openaccount.model.Result r5 = com.alibaba.sdk.android.openaccount.model.Result.result(r5)     // Catch: java.lang.Throwable -> L92
            monitor-exit(r4)
            return r5
        L18:
            r0 = 1
            if (r5 != 0) goto L23
            com.alibaba.sdk.android.openaccount.session.SessionManagerService r5 = r4.sessionManagerService     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L92
            boolean r5 = r5.isSessionExpired()     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L92
            if (r5 == 0) goto L4a
        L23:
            r4.forceRefreshOnce = r0     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L92
            com.alibaba.sdk.android.openaccount.initialization.InitializationServiceClient r5 = r4.initializationServiceClient     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L92
            r5.request()     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L92
            boolean r5 = com.alibaba.sdk.android.openaccount.util.CommonUtils.isNetworkAvailable()     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L92
            if (r5 != 0) goto L4a
            java.lang.Object[] r5 = new java.lang.Object[r0]     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L92
            java.lang.Object[] r2 = new java.lang.Object[r1]     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L92
            r3 = 10014(0x271e, float:1.4033E-41)
            java.lang.String r2 = com.alibaba.sdk.android.openaccount.message.MessageUtils.getMessageContent(r3, r2)     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L92
            r5[r1] = r2     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L92
            com.alibaba.sdk.android.openaccount.message.Message r5 = com.alibaba.sdk.android.openaccount.message.MessageUtils.createMessage(r3, r5)     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L92
            int r2 = r5.code     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L92
            java.lang.String r5 = r5.message     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L92
            com.alibaba.sdk.android.openaccount.model.Result r5 = com.alibaba.sdk.android.openaccount.model.Result.result(r2, r5)     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L92
            monitor-exit(r4)
            return r5
        L4a:
            boolean r5 = com.alibaba.sdk.android.openaccount.trace.AliSDKLogger.isDebugEnabled()     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L92
            if (r5 == 0) goto L6e
            java.lang.String r5 = "oa"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L92
            r2.<init>()     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L92
            java.lang.String r3 = "refresh sid: "
            r2.append(r3)     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L92
            com.alibaba.sdk.android.openaccount.model.Result r3 = r4.getSessionId()     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L92
            T r3 = r3.data     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L92
            java.lang.String r3 = (java.lang.String) r3     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L92
            r2.append(r3)     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L92
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L92
            com.alibaba.sdk.android.openaccount.trace.AliSDKLogger.d(r5, r2)     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L92
        L6e:
            com.alibaba.sdk.android.openaccount.model.Result r5 = r4.getSessionId()     // Catch: java.lang.Throwable -> L74 java.lang.Throwable -> L92
            monitor-exit(r4)
            return r5
        L74:
            r5 = move-exception
            r2 = 10010(0x271a, float:1.4027E-41)
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch: java.lang.Throwable -> L92
            java.lang.String r5 = r5.getMessage()     // Catch: java.lang.Throwable -> L92
            r0[r1] = r5     // Catch: java.lang.Throwable -> L92
            com.alibaba.sdk.android.openaccount.message.Message r5 = com.alibaba.sdk.android.openaccount.message.MessageUtils.createMessage(r2, r0)     // Catch: java.lang.Throwable -> L92
            java.lang.String r0 = "oa"
            com.alibaba.sdk.android.openaccount.trace.AliSDKLogger.log(r0, r5)     // Catch: java.lang.Throwable -> L92
            int r0 = r5.code     // Catch: java.lang.Throwable -> L92
            java.lang.String r5 = r5.message     // Catch: java.lang.Throwable -> L92
            com.alibaba.sdk.android.openaccount.model.Result r5 = com.alibaba.sdk.android.openaccount.model.Result.result(r0, r5)     // Catch: java.lang.Throwable -> L92
            monitor-exit(r4)
            return r5
        L92:
            r5 = move-exception
            monitor-exit(r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.sdk.android.openaccount.session.impl.SessionServiceImpl.refreshSession(boolean):com.alibaba.sdk.android.openaccount.model.Result");
    }

    @Override // com.alibaba.sdk.android.openaccount.initialization.InitializationHandler
    public Object createRequestParameters() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.putOpt("refreshToken", this.sessionManagerService.getRefreshToken().token);
            jSONObject.putOpt("sid", this.sessionManagerService.getSessionId());
        } catch (JSONException e) {
            AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "fail to create session refresh requirement", e);
        }
        return jSONObject;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.alibaba.sdk.android.openaccount.initialization.InitializationHandler
    public ResultCode handleResponseValue(JSONObject jSONObject) {
        if (AliSDKLogger.isDebugEnabled()) {
            AliSDKLogger.d(OpenAccountConstants.LOG_TAG, "handleInitSession response " + jSONObject);
        }
        return processRefreshSessionResult(jSONObject);
    }

    @Override // com.alibaba.sdk.android.openaccount.initialization.InitializationHandler
    public int getRequestRequirement() {
        if (this.forceRefreshOnce) {
            this.forceRefreshOnce = false;
            return 1;
        }
        boolean zIsRefreshTokenExpired = this.sessionManagerService.isRefreshTokenExpired();
        if (!zIsRefreshTokenExpired && this.sessionManagerService.isSessionExpired()) {
            return 1;
        }
        if (!AliSDKLogger.isDebugEnabled()) {
            return 2;
        }
        AliSDKLogger.d(OpenAccountConstants.LOG_TAG, "refreshTokenExpired = " + zIsRefreshTokenExpired + "isSessionExpired = " + this.sessionManagerService.isSessionExpired());
        return 2;
    }

    private ResultCode processRefreshSessionResult(JSONObject jSONObject) {
        int iOptInt = jSONObject.optInt("code", -1);
        String strOptString = jSONObject.optString("message");
        try {
            if (iOptInt != 1) {
                if (iOptInt == 26107) {
                    this.sessionManagerService.removeSession();
                    AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "refresh token expired, local session will be removed");
                    sendSessionUTHint(jSONObject, false, null);
                    return ResultCode.create(100, strOptString);
                }
                sendSessionUTHint(jSONObject, false, null);
                Message messageCreateMessage = MessageUtils.createMessage(MessageConstants.GENERIC_SYSTEM_ERROR, "refresh session error code = " + iOptInt + " message = " + strOptString);
                AliSDKLogger.log(OpenAccountConstants.LOG_TAG, messageCreateMessage);
                return ResultCode.create(messageCreateMessage);
            }
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("data");
            if (jSONObjectOptJSONObject == null) {
                sendSessionUTHint(jSONObject, false, null);
                AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "Null data from refresh sid response with code = 1");
                return ResultCode.create(MessageUtils.createMessage(MessageConstants.GENERIC_SYSTEM_ERROR, new Object[0]));
            }
            SessionData sessionDataCreateSessionDataFromRefreshSidResponse = OpenAccountUtils.createSessionDataFromRefreshSidResponse(jSONObjectOptJSONObject);
            if (sessionDataCreateSessionDataFromRefreshSidResponse != null) {
                if (sessionDataCreateSessionDataFromRefreshSidResponse.scenario == null) {
                    OpenAccountSession session = this.sessionManagerService.getSession();
                    sessionDataCreateSessionDataFromRefreshSidResponse.scenario = Integer.valueOf(session != null ? session.getScenario() : 0);
                }
                sendSessionUTHint(jSONObject, isRefreshTokenUpgraded(sessionDataCreateSessionDataFromRefreshSidResponse), null);
                return this.sessionManagerService.updateSession(sessionDataCreateSessionDataFromRefreshSidResponse);
            }
            AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "null session data is created from the refreshSid response " + jSONObject);
            sendSessionUTHint(jSONObject, false, null);
            return ResultCode.create(MessageUtils.createMessage(MessageConstants.GENERIC_SYSTEM_ERROR, new Object[0]));
        } catch (Exception e) {
            Message messageCreateMessage2 = MessageUtils.createMessage(MessageConstants.GENERIC_SYSTEM_ERROR, " code = " + iOptInt + " message = " + strOptString + " " + e.getMessage());
            AliSDKLogger.log(OpenAccountConstants.LOG_TAG, messageCreateMessage2, e);
            sendSessionUTHint(jSONObject, false, messageCreateMessage2.message);
            return ResultCode.create(messageCreateMessage2);
        }
    }

    private boolean isRefreshTokenUpgraded(SessionData sessionData) {
        RefreshToken refreshToken = this.sessionManagerService.getRefreshToken();
        return (refreshToken == null || refreshToken.token == null || refreshToken.token.startsWith("OA") || sessionData.refreshToken == null || !sessionData.refreshToken.startsWith("OA")) ? false : true;
    }

    private void sendSessionUTHint(JSONObject jSONObject, boolean z, String str) {
        String str2;
        HashMap map = new HashMap();
        map.put("code", jSONObject.optString("code"));
        map.put("traceId", jSONObject.optString("traceId"));
        if (str == null) {
            str = jSONObject.optString("message");
        }
        map.put("msg", str);
        if (z) {
            str2 = UTConstants.E_SDK_CONNECT_SESSION_UPGRADED;
        } else {
            str2 = jSONObject.optInt("code", -1) == 1 ? UTConstants.E_SDK_CONNECT_SESSION_SUCCESS : UTConstants.E_SDK_CONNECT_SESSION_FAILED;
        }
        this.userTrackerService.sendCustomHit(UTConstants.E_SDK_CONNECT_RESULT, 19999, UTConstants.E_SDK_CONNECT_SESSION_ACTION, 0L, str2, map);
    }
}
