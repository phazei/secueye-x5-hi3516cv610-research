package com.alibaba.sdk.android.openaccount.session.impl;

import android.text.TextUtils;
import com.alibaba.sdk.android.openaccount.Environment;
import com.alibaba.sdk.android.openaccount.OpenAccountConfigs;
import com.alibaba.sdk.android.openaccount.OpenAccountConstants;
import com.alibaba.sdk.android.openaccount.config.ConfigService;
import com.alibaba.sdk.android.openaccount.config.EnvironmentChangeListener;
import com.alibaba.sdk.android.openaccount.executor.ExecutorService;
import com.alibaba.sdk.android.openaccount.initialization.InitializationServiceClient;
import com.alibaba.sdk.android.openaccount.message.MessageConstants;
import com.alibaba.sdk.android.openaccount.message.MessageUtils;
import com.alibaba.sdk.android.openaccount.model.OpenAccountSession;
import com.alibaba.sdk.android.openaccount.model.RefreshToken;
import com.alibaba.sdk.android.openaccount.model.Result;
import com.alibaba.sdk.android.openaccount.model.ResultCode;
import com.alibaba.sdk.android.openaccount.model.SessionData;
import com.alibaba.sdk.android.openaccount.model.User;
import com.alibaba.sdk.android.openaccount.rpc.RpcService;
import com.alibaba.sdk.android.openaccount.security.SecurityGuardService;
import com.alibaba.sdk.android.openaccount.session.SessionListener;
import com.alibaba.sdk.android.openaccount.session.SessionManagerService;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.alibaba.sdk.android.openaccount.util.JSONUtils;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.taobao.accs.utl.UtilityImpl;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class DefaultSessionManagerServiceImpl implements SessionManagerService, EnvironmentChangeListener {
    public static final DefaultSessionManagerServiceImpl INSTANCE = new DefaultSessionManagerServiceImpl();
    private static final String INTERNAL_SESSION_STORE_KEY = "openaccount_internal_session";
    private static final String REFRESH_TOKEN_STORE_KEY = "openaccount_refresh_token";
    private static final String TAG = "DefaultSessionManagerServiceImpl";
    private volatile String authorizationCode;

    @Autowired
    private ConfigService configService;

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private InitializationServiceClient initializationServiceClient;
    private volatile InternalSession internalSession;
    private volatile RefreshToken refreshToken;

    @Autowired
    private RpcService rpcService;

    @Autowired
    private SecurityGuardService securityGuardService;
    private List<SessionListener> sessionListeners = new CopyOnWriteArrayList();

    public static class InternalSession {
        public Map<String, String[]> cookies;
        public Long createTime;
        public Integer expireIn;
        public String sid;
        public User user;
    }

    public ResultCode init() {
        String valueFromDynamicDataStore = this.securityGuardService.getValueFromDynamicDataStore(getEnvironmentAwarePropertyName(INTERNAL_SESSION_STORE_KEY));
        if (TextUtils.isEmpty(valueFromDynamicDataStore)) {
            valueFromDynamicDataStore = this.securityGuardService.getValueFromDynamicDataStore(INTERNAL_SESSION_STORE_KEY);
            if (!TextUtils.isEmpty(valueFromDynamicDataStore)) {
                this.securityGuardService.putValueInDynamicDataStore(getEnvironmentAwarePropertyName(INTERNAL_SESSION_STORE_KEY), valueFromDynamicDataStore);
                this.securityGuardService.removeValueFromDynamicDataStore(INTERNAL_SESSION_STORE_KEY);
            }
        }
        if (!TextUtils.isEmpty(valueFromDynamicDataStore)) {
            this.internalSession = toInternalSession(valueFromDynamicDataStore);
        }
        recoverRefreshToken();
        if (OpenAccountConfigs.enableOpenAccountMtopSession && this.internalSession != null) {
            this.rpcService.registerSessionInfo(this.internalSession.sid);
        }
        return new Result(MessageUtils.createMessage(100, new Object[0]));
    }

    private void recoverRefreshToken() {
        String valueFromDynamicDataStore = this.securityGuardService.getValueFromDynamicDataStore(getEnvironmentAwarePropertyName(REFRESH_TOKEN_STORE_KEY));
        if (TextUtils.isEmpty(valueFromDynamicDataStore)) {
            valueFromDynamicDataStore = this.securityGuardService.getValueFromDynamicDataStore(REFRESH_TOKEN_STORE_KEY);
            if (!TextUtils.isEmpty(valueFromDynamicDataStore)) {
                this.securityGuardService.putValueInDynamicDataStore(getEnvironmentAwarePropertyName(REFRESH_TOKEN_STORE_KEY), valueFromDynamicDataStore);
                this.securityGuardService.removeValueFromDynamicDataStore(REFRESH_TOKEN_STORE_KEY);
            }
        }
        if (TextUtils.isEmpty(valueFromDynamicDataStore)) {
            return;
        }
        this.refreshToken = toRefreshToken(valueFromDynamicDataStore);
    }

    @Override // com.alibaba.sdk.android.openaccount.config.EnvironmentChangeListener
    public void onEnvironmentChange(Environment environment, Environment environment2) {
        this.internalSession = null;
        this.refreshToken = null;
        this.authorizationCode = null;
        init();
    }

    @Override // com.alibaba.sdk.android.openaccount.session.SessionManagerService
    public synchronized ResultCode removeSession() {
        if (!getSession().isLogin()) {
            return ResultCode.create(10011, new Object[0]);
        }
        removeLocalSession();
        removeLocalRefreshToken();
        postSessionStateChangedMsg();
        return ResultCode.create(MessageUtils.createMessage(100, new Object[0]));
    }

    @Override // com.alibaba.sdk.android.openaccount.session.SessionManagerService
    public synchronized RefreshToken getRefreshToken() {
        if (this.refreshToken == null) {
            recoverRefreshToken();
        }
        return this.refreshToken;
    }

    @Override // com.alibaba.sdk.android.openaccount.session.SessionManagerService
    public void registerSessionListener(SessionListener sessionListener) {
        if (sessionListener == null) {
            return;
        }
        this.sessionListeners.add(sessionListener);
    }

    @Override // com.alibaba.sdk.android.openaccount.session.SessionManagerService
    public void unRegisterSessionListener(SessionListener sessionListener) {
        if (sessionListener == null) {
            return;
        }
        this.sessionListeners.remove(sessionListener);
    }

    @Override // com.alibaba.sdk.android.openaccount.session.SessionManagerService
    public String getSessionId() {
        InternalSession internalSession = this.internalSession;
        if (internalSession == null) {
            return null;
        }
        return internalSession.sid;
    }

    @Override // com.alibaba.sdk.android.openaccount.session.SessionManagerService
    public Integer getSessionExpiredIn() {
        InternalSession internalSession = this.internalSession;
        if (internalSession == null) {
            return null;
        }
        return internalSession.expireIn;
    }

    @Override // com.alibaba.sdk.android.openaccount.session.SessionManagerService
    public Long getSessionCreationTime() {
        InternalSession internalSession = this.internalSession;
        if (internalSession == null) {
            return null;
        }
        return internalSession.createTime;
    }

    @Override // com.alibaba.sdk.android.openaccount.session.SessionManagerService
    public OpenAccountSession getSession() {
        return new OpenAccountSession() { // from class: com.alibaba.sdk.android.openaccount.session.impl.DefaultSessionManagerServiceImpl.1
            @Override // com.alibaba.sdk.android.openaccount.model.OpenAccountSession
            public int getScenario() {
                RefreshToken refreshToken = DefaultSessionManagerServiceImpl.this.refreshToken;
                if (refreshToken == null || refreshToken.scenario == null) {
                    return 0;
                }
                return refreshToken.scenario.intValue();
            }

            @Override // com.alibaba.sdk.android.openaccount.model.OpenAccountSession
            public boolean isLogin() {
                return !DefaultSessionManagerServiceImpl.this.isRefreshTokenExpired();
            }

            @Override // com.alibaba.sdk.android.openaccount.model.OpenAccountSession
            public Long getLoginTime() {
                RefreshToken refreshToken = DefaultSessionManagerServiceImpl.this.refreshToken;
                if (refreshToken == null) {
                    return null;
                }
                return refreshToken.createTime;
            }

            @Override // com.alibaba.sdk.android.openaccount.model.OpenAccountSession
            public String getUserId() {
                InternalSession internalSession = DefaultSessionManagerServiceImpl.this.internalSession;
                if (internalSession == null || internalSession.user == null) {
                    return null;
                }
                return internalSession.user.id;
            }

            @Override // com.alibaba.sdk.android.openaccount.model.OpenAccountSession
            public String getMobile() {
                InternalSession internalSession = DefaultSessionManagerServiceImpl.this.internalSession;
                if (internalSession == null || internalSession.user == null) {
                    return null;
                }
                return internalSession.user.mobile;
            }

            @Override // com.alibaba.sdk.android.openaccount.model.OpenAccountSession
            public String getNick() {
                InternalSession internalSession = DefaultSessionManagerServiceImpl.this.internalSession;
                if (internalSession == null || internalSession.user == null) {
                    return null;
                }
                return internalSession.user.nick;
            }

            @Override // com.alibaba.sdk.android.openaccount.model.OpenAccountSession
            public String getEmail() {
                InternalSession internalSession = DefaultSessionManagerServiceImpl.this.internalSession;
                if (internalSession == null || internalSession.user == null) {
                    return null;
                }
                return internalSession.user.email;
            }

            @Override // com.alibaba.sdk.android.openaccount.model.OpenAccountSession
            public String getLoginId() {
                InternalSession internalSession = DefaultSessionManagerServiceImpl.this.internalSession;
                if (internalSession == null || internalSession.user == null) {
                    return null;
                }
                return internalSession.user.loginId;
            }

            @Override // com.alibaba.sdk.android.openaccount.model.OpenAccountSession
            public User getUser() {
                InternalSession internalSession = DefaultSessionManagerServiceImpl.this.internalSession;
                if (internalSession == null) {
                    return null;
                }
                return internalSession.user;
            }

            @Override // com.alibaba.sdk.android.openaccount.model.OpenAccountSession
            public String getAuthorizationCode() {
                return DefaultSessionManagerServiceImpl.this.authorizationCode;
            }

            @Override // com.alibaba.sdk.android.openaccount.model.OpenAccountSession
            public Map<String, Object> getOtherInfo() {
                InternalSession internalSession = DefaultSessionManagerServiceImpl.this.internalSession;
                if (internalSession == null || internalSession.user == null) {
                    return null;
                }
                return internalSession.user.otherInfo;
            }
        };
    }

    @Override // com.alibaba.sdk.android.openaccount.session.SessionManagerService
    public synchronized boolean isRefreshTokenExpired() {
        RefreshToken refreshToken = this.refreshToken;
        if (refreshToken != null && refreshToken.token != null) {
            if (System.currentTimeMillis() <= refreshToken.createTime.longValue()) {
                return true;
            }
            return (System.currentTimeMillis() - refreshToken.createTime.longValue()) / 1000 > ((long) refreshToken.expireIn.intValue());
        }
        return true;
    }

    @Override // com.alibaba.sdk.android.openaccount.session.SessionManagerService
    public synchronized boolean isSessionExpired() {
        InternalSession internalSession = this.internalSession;
        if (internalSession != null && internalSession.sid != null && internalSession.createTime != null && internalSession.expireIn != null) {
            if (System.currentTimeMillis() <= internalSession.createTime.longValue()) {
                return true;
            }
            return (System.currentTimeMillis() - internalSession.createTime.longValue()) / 1000 > ((long) internalSession.expireIn.intValue());
        }
        return true;
    }

    private void removeLocalSession() {
        this.internalSession = null;
        try {
            this.securityGuardService.removeValueFromDynamicDataStore(getEnvironmentAwarePropertyName(INTERNAL_SESSION_STORE_KEY));
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void removeLocalRefreshToken() {
        this.refreshToken = null;
        try {
            this.securityGuardService.removeValueFromDynamicDataStore(getEnvironmentAwarePropertyName(REFRESH_TOKEN_STORE_KEY));
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void postSessionStateChangedMsg() {
        for (final SessionListener sessionListener : this.sessionListeners) {
            this.executorService.postUITask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.session.impl.DefaultSessionManagerServiceImpl.2
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        sessionListener.onStateChanged(DefaultSessionManagerServiceImpl.this.getSession());
                    } catch (Exception e) {
                        AliSDKLogger.e(DefaultSessionManagerServiceImpl.TAG, "Fail to post the session changes to the registered listener", e);
                    }
                }
            });
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.session.SessionManagerService
    public synchronized ResultCode updateSession(SessionData sessionData) {
        if (sessionData == null) {
            return ResultCode.create(MessageUtils.createMessage(MessageConstants.GENERIC_SYSTEM_ERROR, new Object[0]));
        }
        updateSessionOnly(sessionData);
        if (sessionData.refreshToken != null) {
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.createTime = sessionData.refreshTokenCreationTime;
            refreshToken.expireIn = Integer.valueOf(sessionData.refreshTokenExpireTime.intValue() - 120);
            refreshToken.token = sessionData.refreshToken;
            refreshToken.scenario = sessionData.scenario;
            this.refreshToken = refreshToken;
            this.securityGuardService.putValueInDynamicDataStore(getEnvironmentAwarePropertyName(REFRESH_TOKEN_STORE_KEY), toRefreshTokenJSON(refreshToken));
        }
        postSessionStateChangedMsg();
        if (OpenAccountConfigs.enableOpenAccountMtopSession) {
            this.rpcService.registerSessionInfo(this.internalSession.sid);
        }
        return ResultCode.create(MessageUtils.createMessage(100, new Object[0]));
    }

    @Override // com.alibaba.sdk.android.openaccount.session.SessionManagerService
    public void updateUser(User user) {
        InternalSession internalSession = this.internalSession;
        internalSession.user = user;
        if (getSession().isLogin()) {
            removeLocalSession();
            this.internalSession = internalSession;
            this.securityGuardService.putValueInDynamicDataStore(getEnvironmentAwarePropertyName(INTERNAL_SESSION_STORE_KEY), toInternalSessionJSON(internalSession));
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.session.SessionManagerService
    public void updateSessionOnly(SessionData sessionData) {
        this.authorizationCode = sessionData.authCode;
        InternalSession internalSession = new InternalSession();
        internalSession.cookies = sessionData.cookiesMap;
        if (TextUtils.isEmpty(sessionData.id)) {
            if (this.internalSession != null) {
                internalSession.user = this.internalSession.user;
            }
        } else {
            User user = new User();
            user.id = sessionData.id;
            user.openId = sessionData.openId;
            user.loginId = sessionData.loginId;
            user.nick = sessionData.nick;
            user.email = sessionData.email;
            user.avatarUrl = sessionData.avatarUrl;
            user.displayName = sessionData.displayName;
            user.mobile = sessionData.mobile;
            user.mobileLocationCode = sessionData.mobileLocationCode;
            user.hasPassword = sessionData.hasPassword;
            user.otherInfo = sessionData.otherInfo;
            internalSession.user = user;
        }
        internalSession.createTime = sessionData.sessionCreationTime;
        if (sessionData.sessionExpireTime != null) {
            internalSession.expireIn = Integer.valueOf(sessionData.sessionExpireTime.intValue() - 120);
        }
        internalSession.sid = sessionData.sessionId;
        removeSession();
        this.internalSession = internalSession;
        this.securityGuardService.putValueInDynamicDataStore(getEnvironmentAwarePropertyName(INTERNAL_SESSION_STORE_KEY), toInternalSessionJSON(internalSession));
    }

    private InternalSession toInternalSession(String str) {
        try {
            InternalSession internalSession = new InternalSession();
            JSONObject jSONObject = new JSONObject(str);
            internalSession.createTime = JSONUtils.optLong(jSONObject, "createTime");
            internalSession.expireIn = JSONUtils.optInteger(jSONObject, "expireIn");
            internalSession.sid = JSONUtils.optString(jSONObject, "sid");
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("user");
            if (jSONObjectOptJSONObject != null) {
                User user = new User();
                internalSession.user = user;
                user.avatarUrl = JSONUtils.optString(jSONObjectOptJSONObject, "avatarUrl");
                user.id = JSONUtils.optString(jSONObjectOptJSONObject, "id");
                user.nick = JSONUtils.optString(jSONObjectOptJSONObject, "nick");
                user.email = JSONUtils.optString(jSONObjectOptJSONObject, "email");
                user.loginId = JSONUtils.optString(jSONObjectOptJSONObject, "loginId");
                user.mobile = JSONUtils.optString(jSONObjectOptJSONObject, UtilityImpl.NET_TYPE_MOBILE);
                user.displayName = JSONUtils.optString(jSONObjectOptJSONObject, "displayName");
                user.mobileLocationCode = JSONUtils.optString(jSONObjectOptJSONObject, AlinkConstants.KEY_MOBILE_LOCATION_CODE);
                user.openId = JSONUtils.optString(jSONObjectOptJSONObject, "openId");
                user.hasPassword = JSONUtils.optBoolean(jSONObjectOptJSONObject, "hasPassword").booleanValue();
                JSONObject jSONObjectOptJSONObject2 = jSONObject.optJSONObject("otherInfo");
                if (jSONObjectOptJSONObject2 != null) {
                    user.otherInfo = JSONUtils.toMap(jSONObjectOptJSONObject2);
                }
            }
            JSONObject jSONObjectOptJSONObject3 = jSONObject.optJSONObject("cookies");
            if (jSONObjectOptJSONObject3 != null) {
                internalSession.cookies = new HashMap();
                Iterator<String> itKeys = jSONObjectOptJSONObject3.keys();
                while (itKeys.hasNext()) {
                    String next = itKeys.next();
                    JSONArray jSONArrayOptJSONArray = jSONObjectOptJSONObject3.optJSONArray(next);
                    if (jSONArrayOptJSONArray != null) {
                        internalSession.cookies.put(next, JSONUtils.toStringArray(jSONArrayOptJSONArray));
                    }
                }
            }
            return internalSession;
        } catch (JSONException e) {
            AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "fail to recover the internal session from local file " + str, e);
            return null;
        }
    }

    private String toInternalSessionJSON(InternalSession internalSession) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("createTime", internalSession.createTime);
            jSONObject.put("expireIn", internalSession.expireIn);
            jSONObject.put("sid", internalSession.sid);
            User user = internalSession.user;
            if (user != null) {
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("avatarUrl", user.avatarUrl);
                jSONObject2.put("id", user.id);
                jSONObject2.put("nick", user.nick);
                jSONObject2.put("email", user.email);
                jSONObject2.put("loginId", user.loginId);
                jSONObject2.put("openId", user.openId);
                jSONObject2.put(UtilityImpl.NET_TYPE_MOBILE, user.mobile);
                jSONObject2.put(AlinkConstants.KEY_MOBILE_LOCATION_CODE, user.mobileLocationCode);
                jSONObject2.put("hasPassword", user.hasPassword);
                jSONObject2.put("displayName", user.displayName);
                jSONObject.put("user", jSONObject2);
                if (user.otherInfo != null) {
                    jSONObject.put("otherInfo", JSONUtils.toJsonObject(user.otherInfo));
                }
            }
            if (internalSession.cookies != null) {
                jSONObject.put("cookies", JSONUtils.toJsonObject(internalSession.cookies));
            }
            return jSONObject.toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private RefreshToken toRefreshToken(String str) {
        RefreshToken refreshToken = new RefreshToken();
        try {
            JSONObject jSONObject = new JSONObject(str);
            refreshToken.createTime = JSONUtils.optLong(jSONObject, "createTime");
            refreshToken.expireIn = JSONUtils.optInteger(jSONObject, "expireIn");
            refreshToken.token = JSONUtils.optString(jSONObject, "token");
            refreshToken.scenario = JSONUtils.optInteger(jSONObject, "scenario");
            return refreshToken;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private String toRefreshTokenJSON(RefreshToken refreshToken) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("createTime", refreshToken.createTime);
            jSONObject.put("expireIn", refreshToken.expireIn);
            jSONObject.put("token", refreshToken.token);
            jSONObject.put("scenario", refreshToken.scenario);
            return jSONObject.toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private String getEnvironmentAwarePropertyName(String str) {
        return this.configService.getEnvironment() + OpenAccountUIConstants.UNDER_LINE + str;
    }
}
