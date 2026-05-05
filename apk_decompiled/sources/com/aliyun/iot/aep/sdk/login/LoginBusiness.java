package com.aliyun.iot.aep.sdk.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.aliyun.alink.sdk.jsbridge.BonePluginRegistry;
import com.aliyun.iot.aep.sdk.login.data.UserInfo;
import com.aliyun.iot.aep.sdk.login.plugin.BoneUserAccountPlugin;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes2.dex */
public class LoginBusiness {
    public static final String LOGIN_CHANGE_ACTION = "com.aliyun.iot.sdk.LoginStatusChange";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static ILoginAdapter f4773a = null;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static Context f4774b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static String f4775c = "";

    private LoginBusiness() {
    }

    public static void init(Context context, ILoginAdapter iLoginAdapter, String str) {
        if (iLoginAdapter == null) {
            throw new IllegalArgumentException("LoginAdapter must not be null");
        }
        f4773a = iLoginAdapter;
        f4775c = str;
        f4774b = context;
        f4773a.registerLoginListener(new a());
        if (isJsbridgeAvailable()) {
            BonePluginRegistry.register(BoneUserAccountPlugin.API_NAME, BoneUserAccountPlugin.class);
        }
    }

    @Deprecated
    public static void init(Context context, ILoginAdapter iLoginAdapter, boolean z, String str) {
        f4775c = str;
        f4774b = context;
        if (iLoginAdapter == null) {
            throw new IllegalArgumentException("LoginAdapter must not be null");
        }
        f4773a = iLoginAdapter;
        f4773a.init(str);
        f4773a.setIsDebuggable(z);
        f4773a.registerLoginListener(new a());
        if (isJsbridgeAvailable()) {
            BonePluginRegistry.register(BoneUserAccountPlugin.API_NAME, BoneUserAccountPlugin.class);
        }
    }

    public static boolean isLogin() {
        ILoginAdapter iLoginAdapter = f4773a;
        if (iLoginAdapter != null) {
            return iLoginAdapter.isLogin();
        }
        return false;
    }

    public static ILoginAdapter getLoginAdapter() {
        return f4773a;
    }

    public static void showRegister(Context context, Class<?> cls, ILoginCallback iLoginCallback) {
        ILoginAdapter iLoginAdapter = f4773a;
        if (iLoginAdapter != null) {
            iLoginAdapter.showRegister(context, cls, iLoginCallback);
        }
    }

    public static void showEmailRegister(Context context, Class<?> cls, ILoginCallback iLoginCallback) {
        ILoginAdapter iLoginAdapter = f4773a;
        if (iLoginAdapter != null) {
            iLoginAdapter.showEmailRegister(context, cls, iLoginCallback);
        }
    }

    public static void login(ILoginCallback iLoginCallback) {
        ILoginAdapter iLoginAdapter = f4773a;
        if (iLoginAdapter != null) {
            iLoginAdapter.login(iLoginCallback);
        }
    }

    @Deprecated
    public static void login(String str, String str2, ILoginCallback iLoginCallback) {
        ILoginAdapter iLoginAdapter = f4773a;
        if (iLoginAdapter != null) {
            iLoginAdapter.login(str, str2, iLoginCallback);
        }
    }

    public static void oauthLogin(Activity activity2, ILoginCallback iLoginCallback) {
        ILoginAdapter iLoginAdapter = f4773a;
        if (iLoginAdapter != null) {
            iLoginAdapter.oauthLogin(activity2, iLoginCallback);
        }
    }

    public static void oauthLogin(Activity activity2, int i, ILoginCallback iLoginCallback) {
        ILoginAdapter iLoginAdapter = f4773a;
        if (iLoginAdapter != null) {
            iLoginAdapter.oauthLogin(activity2, i, iLoginCallback);
        }
    }

    public static void authCodeLogin(String str, ILoginCallback iLoginCallback) {
        ILoginAdapter iLoginAdapter = f4773a;
        if (iLoginAdapter != null) {
            iLoginAdapter.authCodeLogin(str, iLoginCallback);
        }
    }

    public static void logout(final ILogoutCallback iLogoutCallback) {
        Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.aliyun.iot.aep.sdk.login.LoginBusiness.1
            @Override // java.lang.Runnable
            public void run() {
                if (LoginBusiness.f4773a != null) {
                    LoginBusiness.f4773a.logout(iLogoutCallback);
                }
            }
        });
    }

    public static UserInfo getUserInfo() {
        ILoginAdapter iLoginAdapter = f4773a;
        if (iLoginAdapter != null) {
            return iLoginAdapter.getUserData();
        }
        return null;
    }

    public static Object getSessionInfo() {
        ILoginAdapter iLoginAdapter = f4773a;
        if (iLoginAdapter != null) {
            return iLoginAdapter.getSessionData();
        }
        return null;
    }

    public static String getSessionId() {
        ILoginAdapter iLoginAdapter = f4773a;
        if (iLoginAdapter != null) {
            return iLoginAdapter.getSessionId();
        }
        return null;
    }

    public static void refreshSession(boolean z, IRefreshSessionCallback iRefreshSessionCallback) {
        ILoginAdapter iLoginAdapter = f4773a;
        if (iLoginAdapter != null) {
            iLoginAdapter.refreshSession(z, iRefreshSessionCallback);
        }
    }

    public static String getEnv() {
        return f4775c;
    }

    private static class a implements ILoginStatusChangeListener {
        private a() {
        }

        @Override // com.aliyun.iot.aep.sdk.login.ILoginStatusChangeListener
        public void onLoginStatusChange() {
            Intent intent = new Intent(LoginBusiness.LOGIN_CHANGE_ACTION);
            intent.addFlags(32);
            LocalBroadcastManager.getInstance(LoginBusiness.f4774b).sendBroadcast(intent);
        }
    }

    public static boolean isJsbridgeAvailable() {
        return a("com.aliyun.alink.sdk.jsbridge.BonePluginRegistry");
    }

    private static boolean a(String str) {
        try {
            return Class.forName(str) != null;
        } catch (Throwable unused) {
            return false;
        }
    }
}
