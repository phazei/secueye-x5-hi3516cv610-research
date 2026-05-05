package com.alibaba.sdk.android.openaccount.task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.alibaba.sdk.android.oauth.OauthModule;
import com.alibaba.sdk.android.oauth.helper.LoginUtil;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.OpenAccountConstants;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.callback.CheckBindedHidCallback;
import com.alibaba.sdk.android.openaccount.callback.SSOResultCallback;
import com.alibaba.sdk.android.openaccount.config.ConfigService;
import com.alibaba.sdk.android.openaccount.message.MessageConstants;
import com.alibaba.sdk.android.openaccount.message.MessageUtils;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.util.CommonUtils;
import com.taobao.accs.common.Constants;
import com.taobao.login4android.Login;
import com.taobao.login4android.broadcast.LoginAction;
import com.taobao.login4android.broadcast.LoginBroadcastHelper;

/* JADX INFO: loaded from: classes.dex */
public class SSOTaoProvider {
    public static CheckBindedHidCallback _checkBindedHidCallback;
    private boolean listenToLoginBroadcast;
    private ConfigService mConfigService = (ConfigService) OpenAccountSDK.getService(ConfigService.class);
    private Long mHavanaId;
    private SSOResultCallback ssoResultCallback;

    public SSOTaoProvider(Context context, Long l, SSOResultCallback sSOResultCallback) {
        this.listenToLoginBroadcast = true;
        this.mHavanaId = l;
        this.ssoResultCallback = sSOResultCallback;
        if (OauthModule.isSecondPartTaobaoSdkAvailable()) {
            ConfigManager.getInstance().setRegisterLoginBroadcast(false);
            this.listenToLoginBroadcast = true;
            init(context);
            _checkBindedHidCallback = new CheckBindedHidCallback() { // from class: com.alibaba.sdk.android.openaccount.task.SSOTaoProvider.1
                @Override // com.alibaba.sdk.android.openaccount.callback.CheckBindedHidCallback
                public void onSuccess(String str) {
                    Bundle bundle = new Bundle();
                    bundle.putString("unifySsoToken", str);
                    Login.login(false, bundle);
                    SSOTaoProvider.this.listenToLoginBroadcast = true;
                }

                @Override // com.alibaba.sdk.android.openaccount.callback.FailureCallback
                public void onFailure(int i, String str) {
                    SSOTaoProvider.this.listenToLoginBroadcast = false;
                    CommonUtils.onFailure(SSOTaoProvider.this.ssoResultCallback, i, str);
                }
            };
            new CheckHidBindedTask(context, this.mHavanaId, null, null, _checkBindedHidCallback).execute(new Void[0]);
            return;
        }
        this.listenToLoginBroadcast = false;
        CommonUtils.onFailure(this.ssoResultCallback, MessageUtils.createMessage(MessageConstants.NOT_SUPPORT_LOGIN_SDK, new Object[0]));
    }

    public void init(Context context) {
        Login.init(OpenAccountSDK.getAndroidContext(), OpenAccountSDK.getProperty(Constants.KEY_TTID), OpenAccountSDK.getProperty("productVersion"), LoginUtil.getEnv(this.mConfigService.getEnvironment()));
        LoginBroadcastHelper.registerLoginReceiver(context, new BroadcastReceiver() { // from class: com.alibaba.sdk.android.openaccount.task.SSOTaoProvider.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (intent != null) {
                    if (!SSOTaoProvider.this.listenToLoginBroadcast) {
                        AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "source is not tokenLogin. shutdown login broadcast");
                        return;
                    }
                    LoginAction loginActionValueOf = LoginAction.valueOf(intent.getAction());
                    if (loginActionValueOf == null) {
                        return;
                    }
                    switch (AnonymousClass3.$SwitchMap$com$taobao$login4android$broadcast$LoginAction[loginActionValueOf.ordinal()]) {
                        case 1:
                            SSOTaoProvider.this.listenToLoginBroadcast = false;
                            if (SSOTaoProvider.this.ssoResultCallback != null) {
                                SSOTaoProvider.this.ssoResultCallback.onSuccess();
                            }
                            LoginBroadcastHelper.unregisterLoginReceiver(OpenAccountSDK.getAndroidContext(), this);
                            break;
                        case 2:
                            SSOTaoProvider.this.listenToLoginBroadcast = false;
                            CommonUtils.onFailure(SSOTaoProvider.this.ssoResultCallback, MessageUtils.createMessage(10003, new Object[0]));
                            LoginBroadcastHelper.unregisterLoginReceiver(OpenAccountSDK.getAndroidContext(), this);
                            break;
                        case 3:
                            SSOTaoProvider.this.listenToLoginBroadcast = false;
                            CommonUtils.onFailure(SSOTaoProvider.this.ssoResultCallback, MessageUtils.createMessage(MessageConstants.USER_TOKEN_LOGIN_FAIL, new Object[0]));
                            LoginBroadcastHelper.unregisterLoginReceiver(OpenAccountSDK.getAndroidContext(), this);
                            break;
                    }
                }
                AliSDKLogger.e(OpenAccountConstants.LOG_TAG, "null intent in 2nd Taobao SDK onReceive method");
                CommonUtils.onFailure(SSOTaoProvider.this.ssoResultCallback, MessageUtils.createMessage(MessageConstants.GENERIC_SYSTEM_ERROR, new Object[0]));
            }
        });
    }

    /* JADX INFO: renamed from: com.alibaba.sdk.android.openaccount.task.SSOTaoProvider$3, reason: invalid class name */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$taobao$login4android$broadcast$LoginAction = new int[LoginAction.values().length];

        static {
            try {
                $SwitchMap$com$taobao$login4android$broadcast$LoginAction[LoginAction.NOTIFY_LOGIN_SUCCESS.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$taobao$login4android$broadcast$LoginAction[LoginAction.NOTIFY_LOGIN_CANCEL.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$taobao$login4android$broadcast$LoginAction[LoginAction.NOTIFY_LOGIN_FAILED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }
}
