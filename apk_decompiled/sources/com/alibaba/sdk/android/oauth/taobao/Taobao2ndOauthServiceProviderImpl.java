package com.alibaba.sdk.android.oauth.taobao;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.info.AppInfo;
import com.alibaba.sdk.android.oauth.AppCredential;
import com.alibaba.sdk.android.oauth.BindByOauthTask;
import com.alibaba.sdk.android.oauth.LoginByOauthRequest;
import com.alibaba.sdk.android.oauth.LoginByOauthTask;
import com.alibaba.sdk.android.oauth.OauthServiceProvider;
import com.alibaba.sdk.android.oauth.callback.OABindCallback;
import com.alibaba.sdk.android.oauth.helper.LoginUtil;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.Environment;
import com.alibaba.sdk.android.openaccount.OpenAccountConstants;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.OpenAccountService;
import com.alibaba.sdk.android.openaccount.callback.FailureCallback;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.callback.LogoutCallback;
import com.alibaba.sdk.android.openaccount.config.ConfigService;
import com.alibaba.sdk.android.openaccount.config.EnvironmentChangeListener;
import com.alibaba.sdk.android.openaccount.message.MessageConstants;
import com.alibaba.sdk.android.openaccount.message.MessageUtils;
import com.alibaba.sdk.android.openaccount.security.SecurityGuardService;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.util.CommonUtils;
import com.alibaba.sdk.android.pluto.Pluto;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.taobao.accs.common.Constants;
import com.taobao.android.sso.v2.launch.SsoLogin;
import com.taobao.android.sso.v2.launch.model.ISsoRemoteParam;
import com.taobao.login4android.Login;
import com.taobao.login4android.broadcast.LoginAction;
import com.taobao.login4android.broadcast.LoginBroadcastHelper;
import com.taobao.login4android.constants.LoginEnvType;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class Taobao2ndOauthServiceProviderImpl implements OauthServiceProvider, EnvironmentChangeListener {
    private static OABindCallback bindCallback;
    public static LoginCallback loginCallback;

    @Autowired
    private ConfigService configService;
    private OauthServiceProvider oauthServiceProvider;

    @Autowired
    private SecurityGuardService securityGuardService;
    private int oauthPlatform = -1;
    private boolean mLoginAfterOauth = true;

    @Override // com.alibaba.sdk.android.oauth.OauthServiceProvider
    public void authorizeCallback(int i, int i2, Intent intent) {
    }

    public static LoginCallback getLoginCallback() {
        return loginCallback;
    }

    public static void setLoginCallback(LoginCallback loginCallback2) {
        loginCallback = loginCallback2;
    }

    public void init(Context context) {
        this.oauthServiceProvider = this;
        if (this.configService.getDataProvider() == null) {
            Login.init(OpenAccountSDK.getAndroidContext(), OpenAccountSDK.getProperty(Constants.KEY_TTID), OpenAccountSDK.getProperty("productVersion"), getEnv(this.configService.getEnvironment()));
        } else {
            Login.init(OpenAccountSDK.getAndroidContext(), OpenAccountSDK.getProperty(Constants.KEY_TTID), OpenAccountSDK.getProperty("productVersion"), getEnv(this.configService.getEnvironment()), LoginUtil.convert(this.configService.getDataProvider()));
        }
        LoginBroadcastHelper.registerLoginReceiver(context, new BroadcastReceiver() { // from class: com.alibaba.sdk.android.oauth.taobao.Taobao2ndOauthServiceProviderImpl.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (!ConfigManager.getInstance().isRegisterLoginBroadcast()) {
                    AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "business deleberately shutdown login broadcast");
                }
                if (intent == null) {
                    AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "null intent in 2nd Taobao SDK onReceive method");
                    CommonUtils.onFailure(Taobao2ndOauthServiceProviderImpl.loginCallback, MessageUtils.createMessage(MessageConstants.GENERIC_SYSTEM_ERROR, new Object[0]));
                    return;
                }
                LoginAction loginActionValueOf = LoginAction.valueOf(intent.getAction());
                if (loginActionValueOf == null) {
                    return;
                }
                switch (AnonymousClass3.$SwitchMap$com$taobao$login4android$broadcast$LoginAction[loginActionValueOf.ordinal()]) {
                    case 1:
                        if (((OpenAccountService) Pluto.DEFAULT_INSTANCE.getBean(OpenAccountService.class)) != null) {
                            LoginByOauthRequest loginByOauthRequest = new LoginByOauthRequest();
                            loginByOauthRequest.accessToken = Login.getSid();
                            loginByOauthRequest.tokenType = "havana-original-sid";
                            loginByOauthRequest.openId = Login.getUserId();
                            loginByOauthRequest.oauthPlateform = 1;
                            loginByOauthRequest.oauthAppKey = Taobao2ndOauthServiceProviderImpl.this.securityGuardService.getAppKey();
                            HashMap map = new HashMap();
                            map.put("avatarUrl", Login.getHeadPicLink());
                            map.put("nick", Login.getNick());
                            map.put("openId", Login.getUserId());
                            if (Taobao2ndOauthServiceProviderImpl.this.mLoginAfterOauth) {
                                new LoginByOauthTask(context2, Taobao2ndOauthServiceProviderImpl.loginCallback, map, loginByOauthRequest, Taobao2ndOauthServiceProviderImpl.this.oauthServiceProvider).execute(new Void[0]);
                            } else {
                                new BindByOauthTask(context2, Taobao2ndOauthServiceProviderImpl.bindCallback, map, loginByOauthRequest, Taobao2ndOauthServiceProviderImpl.this.oauthServiceProvider).execute(new Void[0]);
                            }
                            break;
                        }
                        break;
                    case 2:
                        CommonUtils.onFailure(Taobao2ndOauthServiceProviderImpl.loginCallback, MessageUtils.createMessage(10003, new Object[0]));
                        break;
                    case 3:
                        CommonUtils.onFailure(Taobao2ndOauthServiceProviderImpl.loginCallback, MessageUtils.createMessage(MessageConstants.GENERIC_SYSTEM_ERROR, new Object[0]));
                        break;
                }
            }
        });
    }

    @Override // com.alibaba.sdk.android.oauth.OauthServiceProvider
    public void oauth(Activity activity2, int i, AppCredential appCredential, LoginCallback loginCallback2) {
        loginCallback = loginCallback2;
        this.oauthPlatform = i;
        this.mLoginAfterOauth = true;
        ConfigManager.getInstance().setRegisterLoginBroadcast(true);
        if (SsoLogin.isSupportTBSsoV2(activity2)) {
            taobaoSSO(activity2, loginCallback2);
        } else {
            Login.login(true);
        }
    }

    @Override // com.alibaba.sdk.android.oauth.OauthServiceProvider
    public void bind(Activity activity2, int i, AppCredential appCredential, OABindCallback oABindCallback) {
        bindCallback = oABindCallback;
        this.oauthPlatform = i;
        this.mLoginAfterOauth = false;
        if (SsoLogin.isSupportTBSsoV2(activity2)) {
            taobaoSSO(activity2, oABindCallback);
        } else {
            Login.login(true);
        }
    }

    private void taobaoSSO(Activity activity2, FailureCallback failureCallback) {
        try {
            SsoLogin.launchTao(activity2, new ISsoRemoteParam() { // from class: com.alibaba.sdk.android.oauth.taobao.Taobao2ndOauthServiceProviderImpl.2
                public String getServerTime() {
                    return TmpConstant.GROUP_ROLE_UNKNOWN;
                }

                public String getTtid() {
                    return DataProviderFactory.getDataProvider().getTTID();
                }

                public String getImei() {
                    return DataProviderFactory.getDataProvider().getImei();
                }

                public String getImsi() {
                    return DataProviderFactory.getDataProvider().getImsi();
                }

                public String getDeviceId() {
                    return DataProviderFactory.getDataProvider().getDeviceId();
                }

                public String getAppKey() {
                    return DataProviderFactory.getDataProvider().getAppkey();
                }

                public String getApdid() {
                    return AppInfo.getInstance().getApdid();
                }

                public String getUmidToken() {
                    return AppInfo.getInstance().getUmidToken();
                }

                public String getAtlas() {
                    return DataProviderFactory.getDataProvider().getEnvType() == LoginEnvType.DEV.getSdkEnvType() ? "daily" : "";
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            CommonUtils.onFailure(failureCallback, MessageUtils.createMessage(MessageConstants.GENERIC_SYSTEM_ERROR, new Object[0]));
        }
    }

    @Override // com.alibaba.sdk.android.oauth.OauthServiceProvider
    public void cleanUp() {
        loginCallback = null;
    }

    /* JADX INFO: renamed from: com.alibaba.sdk.android.oauth.taobao.Taobao2ndOauthServiceProviderImpl$3, reason: invalid class name */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$taobao$login4android$broadcast$LoginAction;

        static {
            try {
                $SwitchMap$com$alibaba$sdk$android$openaccount$Environment[Environment.TEST.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$alibaba$sdk$android$openaccount$Environment[Environment.PRE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$alibaba$sdk$android$openaccount$Environment[Environment.SANDBOX.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$alibaba$sdk$android$openaccount$Environment[Environment.ONLINE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            $SwitchMap$com$taobao$login4android$broadcast$LoginAction = new int[LoginAction.values().length];
            try {
                $SwitchMap$com$taobao$login4android$broadcast$LoginAction[LoginAction.NOTIFY_LOGIN_SUCCESS.ordinal()] = 1;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$taobao$login4android$broadcast$LoginAction[LoginAction.NOTIFY_LOGIN_CANCEL.ordinal()] = 2;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$taobao$login4android$broadcast$LoginAction[LoginAction.NOTIFY_LOGIN_FAILED.ordinal()] = 3;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    private LoginEnvType getEnv(Environment environment) {
        switch (environment) {
            case TEST:
                return LoginEnvType.DEV;
            case PRE:
                return LoginEnvType.PRE;
            case SANDBOX:
                return LoginEnvType.DEV;
            default:
                return LoginEnvType.ONLINE;
        }
    }

    @Override // com.alibaba.sdk.android.oauth.OauthServiceProvider
    public void logout(Context context, LogoutCallback logoutCallback) {
        Login.logout();
    }

    @Override // com.alibaba.sdk.android.openaccount.config.EnvironmentChangeListener
    public void onEnvironmentChange(Environment environment, Environment environment2) {
        switch (environment2) {
            case TEST:
                DataProviderFactory.getDataProvider().setEnvType(LoginEnvType.DEV.getSdkEnvType());
                break;
            case PRE:
                DataProviderFactory.getDataProvider().setEnvType(LoginEnvType.PRE.getSdkEnvType());
                break;
            case SANDBOX:
                DataProviderFactory.getDataProvider().setEnvType(LoginEnvType.DEV.getSdkEnvType());
                break;
            case ONLINE:
                DataProviderFactory.getDataProvider().setEnvType(LoginEnvType.ONLINE.getSdkEnvType());
                break;
            default:
                DataProviderFactory.getDataProvider().setEnvType(LoginEnvType.ONLINE.getSdkEnvType());
                break;
        }
    }
}
