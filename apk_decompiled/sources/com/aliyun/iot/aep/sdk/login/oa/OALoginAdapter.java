package com.aliyun.iot.aep.sdk.login.oa;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.Environment;
import com.alibaba.sdk.android.openaccount.OauthService;
import com.alibaba.sdk.android.openaccount.OpenAccountConstants;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.OpenAccountService;
import com.alibaba.sdk.android.openaccount.OpenAccountSessionService;
import com.alibaba.sdk.android.openaccount.callback.InitResultCallback;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.callback.LogoutCallback;
import com.alibaba.sdk.android.openaccount.model.LoginResult;
import com.alibaba.sdk.android.openaccount.model.OpenAccountSession;
import com.alibaba.sdk.android.openaccount.model.Result;
import com.alibaba.sdk.android.openaccount.model.SessionData;
import com.alibaba.sdk.android.openaccount.model.User;
import com.alibaba.sdk.android.openaccount.session.SessionListener;
import com.alibaba.sdk.android.openaccount.session.SessionManagerService;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConfigs;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService;
import com.alibaba.sdk.android.openaccount.ui.callback.EmailRegisterCallback;
import com.alibaba.sdk.android.openaccount.util.OpenAccountUtils;
import com.alibaba.sdk.android.openaccount.util.RpcUtils;
import com.alibaba.sdk.android.openaccount.util.safe.RSAKey;
import com.alibaba.sdk.android.openaccount.util.safe.Rsa;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iot.aep.sdk.login.ILoginAdapter;
import com.aliyun.iot.aep.sdk.login.ILoginCallback;
import com.aliyun.iot.aep.sdk.login.ILoginStatusChangeListener;
import com.aliyun.iot.aep.sdk.login.ILogoutCallback;
import com.aliyun.iot.aep.sdk.login.IRefreshSessionCallback;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.aliyun.iot.aep.sdk.login.data.SessionInfo;
import com.aliyun.iot.aep.sdk.login.data.UserInfo;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes2.dex */
public class OALoginAdapter implements ILoginAdapter {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Context f4779c;
    private SessionListener j;
    private Map<String, String> m;
    private Class<?> n;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final String f4777a = "OALoginAdapter";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private boolean f4778b = false;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f4780d = false;
    private List<ILoginStatusChangeListener> e = new ArrayList();
    private volatile List<IRefreshSessionCallback> f = new ArrayList();
    private List<OnBeforeLogoutListener> g = new ArrayList();
    private List<OnCompleteBeforeLogoutListener> h = new ArrayList();
    private volatile boolean i = false;
    private SessionInfo k = new SessionInfo();
    private String l = "";

    public interface ActionOnBeforeLogoutResultCallback {
        public static final int RESULT_FAILED = -1;
        public static final int RESULT_SUCC = 0;

        void onResult(int i);
    }

    public interface OALoginAdapterInitResultCallback {
        void onInitFailed(int i, String str);

        void onInitSuccess();
    }

    public interface OnBeforeLogoutListener {
        void doAction();
    }

    public interface OnCompleteBeforeLogoutListener {
        void doAction(ActionOnBeforeLogoutResultCallback actionOnBeforeLogoutResultCallback);
    }

    public OALoginAdapter(Context context) {
        this.f4779c = context;
    }

    public void setSupportAliYun(boolean z) {
        this.f4778b = z;
    }

    public void setDefaultOAHost(String str) {
        this.l = str;
    }

    public void setDefaultLoginParams(Map<String, String> map) {
        this.m = map;
    }

    public void setDefaultLoginClass(Class<?> cls) {
        this.n = cls;
    }

    @Override // com.aliyun.iot.aep.sdk.login.ILoginAdapter
    public void init(String str) {
        String securityImagePostfix = ConfigManager.getInstance().getSecurityImagePostfix();
        if (TextUtils.isEmpty(securityImagePostfix)) {
            securityImagePostfix = "114d";
        }
        init(str, securityImagePostfix);
    }

    public void init(String str, String str2) {
        init(str, str2, null);
    }

    public void init(String str, String str2, final OALoginAdapterInitResultCallback oALoginAdapterInitResultCallback) {
        a("init() OALoginAdapter , env is:" + str + "  " + str2 + "    thread:" + Thread.currentThread().getId());
        if ("TEST".equalsIgnoreCase(str)) {
            ConfigManager.getInstance().setEnvironment(Environment.TEST);
        } else if ("PRE".equalsIgnoreCase(str)) {
            ConfigManager.getInstance().setEnvironment(Environment.PRE);
        } else {
            ConfigManager.getInstance().setEnvironment(Environment.ONLINE);
        }
        if (!TextUtils.isEmpty(str2)) {
            ConfigManager.getInstance().setSecGuardImagePostfix(str2);
        } else {
            ConfigManager.getInstance().setSecGuardImagePostfix("114d");
        }
        ConfigManager.getInstance().setUseSingleImage(true);
        ConfigManager.getInstance().setAPIGateway(true);
        if (!TextUtils.isEmpty(this.l)) {
            ConfigManager.getInstance().setApiGatewayHost(this.l);
        }
        if (this.f4780d) {
            OpenAccountSDK.turnOnDebug();
        }
        OpenAccountSDK.syncInit(this.f4779c, new InitResultCallback() { // from class: com.aliyun.iot.aep.sdk.login.oa.OALoginAdapter.1
            @Override // com.alibaba.sdk.android.openaccount.callback.InitResultCallback
            public void onSuccess() {
                OALoginAdapter.this.a("OpenAccountSDK init success");
                OALoginAdapter oALoginAdapter = OALoginAdapter.this;
                oALoginAdapter.j = new b();
                ((OpenAccountService) OpenAccountSDK.getService(OpenAccountService.class)).addSessionListener(OALoginAdapter.this.j);
                OALoginAdapter.this.a();
                OALoginAdapterInitResultCallback oALoginAdapterInitResultCallback2 = oALoginAdapterInitResultCallback;
                if (oALoginAdapterInitResultCallback2 != null) {
                    oALoginAdapterInitResultCallback2.onInitSuccess();
                }
            }

            @Override // com.alibaba.sdk.android.openaccount.callback.FailureCallback
            public void onFailure(int i, String str3) {
                OALoginAdapter.this.a("OpenAccountSDK init failed:" + str3);
                OALoginAdapterInitResultCallback oALoginAdapterInitResultCallback2 = oALoginAdapterInitResultCallback;
                if (oALoginAdapterInitResultCallback2 != null) {
                    oALoginAdapterInitResultCallback2.onInitFailed(i, str3);
                }
            }
        });
    }

    @Override // com.aliyun.iot.aep.sdk.login.ILoginAdapter
    public void setIsDebuggable(boolean z) {
        if (z) {
            OpenAccountSDK.turnOnDebug();
        }
    }

    @Override // com.aliyun.iot.aep.sdk.login.ILoginAdapter
    public void refreshSession(final boolean z, IRefreshSessionCallback iRefreshSessionCallback) {
        if (iRefreshSessionCallback != null) {
            this.f.add(iRefreshSessionCallback);
        }
        if (this.i) {
            return;
        }
        ThreadPool.DefaultThreadPool.getInstance().submit(new Runnable() { // from class: com.aliyun.iot.aep.sdk.login.oa.OALoginAdapter.2
            @Override // java.lang.Runnable
            public void run() {
                OALoginAdapter.this.a("refreshSession() is force:" + z);
                ((OpenAccountSessionService) OpenAccountSDK.getService(OpenAccountSessionService.class)).refreshSession(z);
            }
        });
    }

    @Override // com.aliyun.iot.aep.sdk.login.ILoginAdapter
    public void registerLoginListener(ILoginStatusChangeListener iLoginStatusChangeListener) {
        this.e.add(iLoginStatusChangeListener);
    }

    @Override // com.aliyun.iot.aep.sdk.login.ILoginAdapter
    public void unRegisterLoginListener(ILoginStatusChangeListener iLoginStatusChangeListener) {
        this.e.remove(iLoginStatusChangeListener);
    }

    @Override // com.aliyun.iot.aep.sdk.login.ILoginAdapter
    public void showRegister(Context context, Class<?> cls, ILoginCallback iLoginCallback) {
        ((OpenAccountUIService) OpenAccountSDK.getService(OpenAccountUIService.class)).showRegister(context, cls, new OALoginCallback(iLoginCallback));
    }

    @Override // com.aliyun.iot.aep.sdk.login.ILoginAdapter
    public void showEmailRegister(Context context, Class<?> cls, ILoginCallback iLoginCallback) {
        ((OpenAccountUIService) OpenAccountSDK.getService(OpenAccountUIService.class)).showEmailRegister(context, cls, new OALoginCallback(iLoginCallback));
    }

    @Override // com.aliyun.iot.aep.sdk.login.ILoginAdapter
    public void login(ILoginCallback iLoginCallback) {
        login(iLoginCallback, null);
    }

    public void login(ILoginCallback iLoginCallback, Map<String, String> map) {
        OpenAccountUIService openAccountUIService = (OpenAccountUIService) OpenAccountSDK.getService(OpenAccountUIService.class);
        if (openAccountUIService == null) {
            return;
        }
        if (this.m != null) {
            if (map == null) {
                map = new HashMap<>();
            }
            map.putAll(this.m);
        }
        try {
            if (this.n != null) {
                if (map != null) {
                    openAccountUIService.showLogin(this.f4779c, this.n, map, new OALoginCallback(iLoginCallback));
                    return;
                } else {
                    openAccountUIService.showLogin(this.f4779c, this.n, new OALoginCallback(iLoginCallback));
                    return;
                }
            }
            if (map != null) {
                openAccountUIService.showLogin(this.f4779c, map, new OALoginCallback(iLoginCallback));
            } else {
                openAccountUIService.showLogin(this.f4779c, new OALoginCallback(iLoginCallback));
            }
        } catch (Exception e) {
            a("login failed:" + e.toString());
        }
    }

    @Override // com.aliyun.iot.aep.sdk.login.ILoginAdapter
    public void login(final String str, final String str2, final ILoginCallback iLoginCallback) {
        ThreadPool.DefaultThreadPool.getInstance().submit(new Runnable() { // from class: com.aliyun.iot.aep.sdk.login.oa.OALoginAdapter.3
            @Override // java.lang.Runnable
            public void run() {
                OALoginAdapter.this.a(str, str2, iLoginCallback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str, String str2, ILoginCallback iLoginCallback) {
        if (iLoginCallback == null) {
            ALog.e("OALoginAdapter", "callback is null");
            return;
        }
        OALoginCallback oALoginCallback = new OALoginCallback(iLoginCallback);
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            ALog.e("OALoginAdapter", "error parameter");
            oALoginCallback.onFailure(-1, "error parameter");
            return;
        }
        HashMap map = new HashMap();
        map.put("loginId", str);
        try {
            String rsaPubkey = RSAKey.getRsaPubkey();
            if (TextUtils.isEmpty(rsaPubkey)) {
                oALoginCallback.onFailure(-1, "error when encrypt password");
                return;
            }
            map.put("password", Rsa.encrypt(str2, rsaPubkey));
            Result<LoginResult> loginResult = OpenAccountUtils.toLoginResult(RpcUtils.pureInvokeWithRiskControlInfo("loginRequest", map, FirebaseAnalytics.Event.LOGIN));
            try {
                if (loginResult == null) {
                    oALoginCallback.onFailure(-1, "login failed");
                } else {
                    switch (loginResult.code) {
                        case 1:
                        case 2:
                            a(loginResult);
                            OpenAccountService openAccountService = (OpenAccountService) OpenAccountSDK.getService(OpenAccountService.class);
                            if (openAccountService != null) {
                                oALoginCallback.onSuccess(openAccountService.getSession());
                            } else {
                                iLoginCallback.onLoginSuccess();
                            }
                            break;
                        default:
                            oALoginCallback.onFailure(loginResult.code, loginResult.message);
                            break;
                    }
                }
            } catch (Throwable th) {
                ALog.e("OALoginAdapter", "Login execute error" + th.toString());
                oALoginCallback.onFailure(-1, "Login get result error");
            }
        } catch (Exception unused) {
            oALoginCallback.onFailure(-1, "error when encrypt password");
        }
    }

    private void a(Result<LoginResult> result) {
        if (result.data == null || result.data.loginSuccessResult == null) {
            return;
        }
        SessionManagerService sessionManagerService = (SessionManagerService) OpenAccountSDK.getService(SessionManagerService.class);
        SessionData sessionDataCreateSessionDataFromLoginSuccessResult = OpenAccountUtils.createSessionDataFromLoginSuccessResult(result.data.loginSuccessResult);
        if (sessionDataCreateSessionDataFromLoginSuccessResult.scenario == null) {
            sessionDataCreateSessionDataFromLoginSuccessResult.scenario = 1;
        }
        sessionManagerService.updateSession(sessionDataCreateSessionDataFromLoginSuccessResult);
    }

    @Override // com.aliyun.iot.aep.sdk.login.ILoginAdapter
    public void oauthLogin(Activity activity2, ILoginCallback iLoginCallback) {
        ((OauthService) OpenAccountSDK.getService(OauthService.class)).oauth(activity2, 32, new OALoginCallback(iLoginCallback));
    }

    @Override // com.aliyun.iot.aep.sdk.login.ILoginAdapter
    public void oauthLogin(Activity activity2, int i, ILoginCallback iLoginCallback) {
        ((OauthService) OpenAccountSDK.getService(OauthService.class)).oauth(activity2, i, new OALoginCallback(iLoginCallback));
    }

    @Override // com.aliyun.iot.aep.sdk.login.ILoginAdapter
    public void authCodeLogin(String str, ILoginCallback iLoginCallback) {
        ((OpenAccountService) OpenAccountSDK.getService(OpenAccountService.class)).authCodeLogin(this.f4779c, str, new OALoginCallback(iLoginCallback));
    }

    public void showChangePwd(ILoginCallback iLoginCallback) {
        OpenAccountUIService openAccountUIService = (OpenAccountUIService) OpenAccountSDK.getService(OpenAccountUIService.class);
        if (OpenAccountUIConfigs.MobileResetPasswordLoginFlow.resetPasswordPasswordActivityClazz != null) {
            openAccountUIService.showRegister(this.f4779c, OpenAccountUIConfigs.UnifyLoginFlow.resetPasswordActivityClass, new OALoginCallback(iLoginCallback));
        } else {
            openAccountUIService.showRegister(this.f4779c, new OALoginCallback(iLoginCallback));
        }
    }

    @Override // com.aliyun.iot.aep.sdk.login.ILoginAdapter
    public void logout(ILogoutCallback iLogoutCallback) {
        List<OnBeforeLogoutListener> list = this.g;
        if (list != null && !list.isEmpty()) {
            Iterator<OnBeforeLogoutListener> it = this.g.iterator();
            while (it.hasNext()) {
                it.next().doAction();
            }
        }
        List<OnCompleteBeforeLogoutListener> list2 = this.h;
        if (list2 != null && !list2.isEmpty()) {
            int size = this.h.size();
            final CountDownLatch countDownLatch = new CountDownLatch(size);
            ALog.i("OALoginAdapter", "action nums : " + size);
            Iterator<OnCompleteBeforeLogoutListener> it2 = this.h.iterator();
            while (it2.hasNext()) {
                it2.next().doAction(new ActionOnBeforeLogoutResultCallback() { // from class: com.aliyun.iot.aep.sdk.login.oa.OALoginAdapter.4
                    @Override // com.aliyun.iot.aep.sdk.login.oa.OALoginAdapter.ActionOnBeforeLogoutResultCallback
                    public void onResult(int i) {
                        ALog.i("OALoginAdapter", "doAction onResult : " + i);
                        countDownLatch.countDown();
                    }
                });
            }
            try {
                countDownLatch.await(10L, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                ALog.i("OALoginAdapter", "OnCompleteBeforeLogoutListener doAction await timeout!");
                e.printStackTrace();
            }
        }
        try {
            ((OpenAccountService) OpenAccountSDK.getService(OpenAccountService.class)).logout(this.f4779c, new a(iLogoutCallback));
        } catch (Exception e2) {
            a("logout failure" + e2.toString());
        }
        try {
            CookieManager cookieManager = CookieManager.getInstance();
            if (Build.VERSION.SDK_INT >= 21) {
                cookieManager.removeAllCookies(new ValueCallback<Boolean>() { // from class: com.aliyun.iot.aep.sdk.login.oa.OALoginAdapter.5
                    @Override // android.webkit.ValueCallback
                    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
                    public void onReceiveValue(Boolean bool) {
                        Log.d("OALoginAdapter", "Cookie removed: " + bool);
                    }
                });
            } else {
                cookieManager.removeAllCookie();
            }
        } catch (Exception unused) {
        }
    }

    @Override // com.aliyun.iot.aep.sdk.login.ILoginAdapter
    public boolean isLogin() {
        OpenAccountSession session = ((OpenAccountService) OpenAccountSDK.getService(OpenAccountService.class)).getSession();
        return session != null && session.isLogin();
    }

    @Override // com.aliyun.iot.aep.sdk.login.ILoginAdapter
    public UserInfo getUserData() {
        OpenAccountSession session = ((OpenAccountService) OpenAccountSDK.getService(OpenAccountService.class)).getSession();
        User user = session == null ? null : session.getUser();
        UserInfo userInfo = new UserInfo();
        if (user == null) {
            return null;
        }
        Log.d("OALoginAdapter", "getUserData:" + JSON.toJSONString(user));
        Map<String, Object> otherInfo = session.getOtherInfo();
        if (otherInfo != null) {
            try {
                if (otherInfo.get(OpenAccountConstants.OPEN_ACCOUNT_OTHER_INFO) != null) {
                    Map map = (Map) otherInfo.get(OpenAccountConstants.OPEN_ACCOUNT_OTHER_INFO);
                    if (map.get("avatarUrl") != null) {
                        userInfo.userAvatarUrl = map.get("avatarUrl").toString();
                    }
                    if (map.get("id") != null) {
                        userInfo.userId = map.get("id").toString();
                    }
                    if (map.get("displayName") != null) {
                        userInfo.userNick = map.get("displayName").toString();
                    }
                    if (map.get("country") != null) {
                        userInfo.country = map.get("country").toString();
                    }
                }
                if (!TextUtils.isEmpty(user.openId) && otherInfo.get(OpenAccountConstants.OAUTH_OTHER_INFO) != null) {
                    Map map2 = (Map) otherInfo.get(OpenAccountConstants.OAUTH_OTHER_INFO);
                    if (map2.get("picture") != null) {
                        userInfo.userAvatarUrl = map2.get("picture").toString();
                    }
                    if (map2.get("name") != null) {
                        userInfo.userNick = map2.get("name").toString();
                    }
                    if (map2.get("country") != null) {
                        userInfo.country = map2.get("country").toString();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("OALoginAdapter", "getUserData: otherInfo error:" + e.getMessage());
            }
        }
        userInfo.openId = user.openId;
        userInfo.userId = user.id;
        if (!TextUtils.isEmpty(user.avatarUrl)) {
            userInfo.userAvatarUrl = user.avatarUrl;
        }
        if (TextUtils.isEmpty(userInfo.userNick)) {
            if (!TextUtils.isEmpty(user.nick)) {
                userInfo.userNick = user.nick;
            } else if (!TextUtils.isEmpty(user.displayName)) {
                userInfo.userNick = user.displayName;
            }
        }
        if (user.mobile != null) {
            userInfo.userPhone = user.mobile;
        }
        if (user.email != null) {
            userInfo.userEmail = user.email;
        }
        if (user.mobileLocationCode != null) {
            userInfo.mobileLocationCode = user.mobileLocationCode;
        }
        return userInfo;
    }

    @Override // com.aliyun.iot.aep.sdk.login.ILoginAdapter
    public Object getSessionData() {
        return this.k;
    }

    @Override // com.aliyun.iot.aep.sdk.login.ILoginAdapter
    public String getSessionId() {
        return this.k.sessionId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        SessionManagerService sessionManagerService = (SessionManagerService) OpenAccountSDK.getService(SessionManagerService.class);
        if (sessionManagerService == null) {
            return;
        }
        if (this.k == null) {
            this.k = new SessionInfo();
        }
        try {
            this.k.sessionCreateTime = sessionManagerService.getSessionCreationTime().longValue();
            this.k.sessionId = sessionManagerService.getSessionId();
            this.k.sessionExpire = sessionManagerService.getSessionExpiredIn().intValue();
        } catch (Exception unused) {
        }
        a("updateSession() sessionInfo:" + this.k.toString());
    }

    public void registerBeforeLogoutListener(OnBeforeLogoutListener onBeforeLogoutListener) {
        if (onBeforeLogoutListener != null) {
            this.g.add(onBeforeLogoutListener);
        }
    }

    public void unRegisterBeforeLogoutListener(OnBeforeLogoutListener onBeforeLogoutListener) {
        if (onBeforeLogoutListener == null) {
            return;
        }
        try {
            this.g.remove(onBeforeLogoutListener);
        } catch (Exception unused) {
        }
    }

    public void registerCompleteBeforeLogoutListener(OnCompleteBeforeLogoutListener onCompleteBeforeLogoutListener) {
        ALog.i("OALoginAdapter", "registerCompleteBeforeLogoutListener ~");
        if (onCompleteBeforeLogoutListener != null) {
            this.h.add(onCompleteBeforeLogoutListener);
        }
    }

    public void unRegisterCompleteBeforeLogoutListener(OnCompleteBeforeLogoutListener onCompleteBeforeLogoutListener) {
        if (onCompleteBeforeLogoutListener == null) {
            return;
        }
        try {
            this.h.remove(onCompleteBeforeLogoutListener);
        } catch (Exception unused) {
        }
    }

    private class b implements SessionListener {
        private b() {
        }

        @Override // com.alibaba.sdk.android.openaccount.session.SessionListener
        public void onStateChanged(OpenAccountSession openAccountSession) {
            OALoginAdapter.this.a("onStateChanged() refreshCacheList size:" + OALoginAdapter.this.f.size());
            OALoginAdapter.this.a();
            if (!OALoginAdapter.this.f.isEmpty()) {
                OALoginAdapter.this.b();
            }
            OALoginAdapter.this.i = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void b() {
        a("dealCacheRefreshListeners() Deal cache listener size:" + this.f.size());
        for (IRefreshSessionCallback iRefreshSessionCallback : this.f) {
            if (iRefreshSessionCallback != null) {
                iRefreshSessionCallback.onRefreshSuccess();
            }
        }
        this.f.clear();
    }

    public class OALoginCallback implements LoginCallback, EmailRegisterCallback {
        public EmailRegisterCallback emailRegisterCallback;
        public ILoginCallback loginCallback;

        @Override // com.alibaba.sdk.android.openaccount.ui.callback.EmailRegisterCallback
        public void onEmailSent(String str) {
        }

        public OALoginCallback(ILoginCallback iLoginCallback) {
            this.loginCallback = iLoginCallback;
        }

        @Override // com.alibaba.sdk.android.openaccount.callback.LoginCallback
        public void onSuccess(OpenAccountSession openAccountSession) {
            OALoginAdapter.this.a("login Success" + OALoginAdapter.this.a(openAccountSession));
            OALoginAdapter.this.a();
            Iterator it = OALoginAdapter.this.e.iterator();
            while (it.hasNext()) {
                ((ILoginStatusChangeListener) it.next()).onLoginStatusChange();
            }
            ILoginCallback iLoginCallback = this.loginCallback;
            if (iLoginCallback != null) {
                iLoginCallback.onLoginSuccess();
            }
            UserInfo userInfo = LoginBusiness.getUserInfo();
            if (userInfo != null) {
                OALoginAdapter.this.a("onSuccess: " + JSON.toJSONString(userInfo));
                if (TextUtils.isEmpty(userInfo.openId)) {
                    return;
                }
                HashMap map = new HashMap();
                User user = openAccountSession == null ? null : openAccountSession.getUser();
                if (!TextUtils.isEmpty(userInfo.userNick) && user != null && !TextUtils.isEmpty(user.displayName)) {
                    map.put("displayName", userInfo.userNick);
                }
                if (!TextUtils.isEmpty(userInfo.userAvatarUrl) && user != null && !TextUtils.isEmpty(user.avatarUrl)) {
                    map.put("avatarUrl", userInfo.userAvatarUrl);
                }
                if (map.isEmpty()) {
                    return;
                }
                ((OpenAccountUIService) OpenAccountSDK.getService(OpenAccountUIService.class)).updateProfile(OALoginAdapter.this.f4779c, map, new c());
            }
        }

        @Override // com.alibaba.sdk.android.openaccount.callback.FailureCallback
        public void onFailure(int i, String str) {
            OALoginAdapter.this.a("login failed  code:" + i + " msg:" + str);
            ILoginCallback iLoginCallback = this.loginCallback;
            if (iLoginCallback != null) {
                iLoginCallback.onLoginFailed(i, str);
            }
        }
    }

    private class c implements LoginCallback {
        private c() {
        }

        @Override // com.alibaba.sdk.android.openaccount.callback.LoginCallback
        public void onSuccess(OpenAccountSession openAccountSession) {
            OALoginAdapter.this.a("updateProfile Success" + OALoginAdapter.this.a(openAccountSession));
        }

        @Override // com.alibaba.sdk.android.openaccount.callback.FailureCallback
        public void onFailure(int i, String str) {
            OALoginAdapter.this.a("updateProfile onFailure" + i + " " + str);
        }
    }

    private class a implements LogoutCallback {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private ILogoutCallback f4794b;

        public a(ILogoutCallback iLogoutCallback) {
            this.f4794b = iLogoutCallback;
        }

        @Override // com.alibaba.sdk.android.openaccount.callback.FailureCallback
        public void onFailure(int i, String str) {
            OALoginAdapter.this.a("logout failurecode:" + i + " msg:" + str);
            ILogoutCallback iLogoutCallback = this.f4794b;
            if (iLogoutCallback != null) {
                iLogoutCallback.onLogoutFailed(i, str);
            }
        }

        @Override // com.alibaba.sdk.android.openaccount.callback.LogoutCallback
        public void onSuccess() {
            OALoginAdapter.this.a("logout Success");
            Iterator it = OALoginAdapter.this.e.iterator();
            while (it.hasNext()) {
                ((ILoginStatusChangeListener) it.next()).onLoginStatusChange();
            }
            ILogoutCallback iLogoutCallback = this.f4794b;
            if (iLogoutCallback != null) {
                iLogoutCallback.onLogoutSuccess();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String a(OpenAccountSession openAccountSession) {
        if (openAccountSession == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("userid:");
        sb.append(openAccountSession.getUserId());
        sb.append(" authorizationCode:");
        sb.append(openAccountSession.getAuthorizationCode());
        sb.append(" loginTime:");
        sb.append(openAccountSession.getLoginTime());
        sb.append(" user:");
        sb.append(openAccountSession.getUser() == null ? "" : openAccountSession.getUser().toString());
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str) {
        ALog.i("OALoginAdapter", str);
    }
}
