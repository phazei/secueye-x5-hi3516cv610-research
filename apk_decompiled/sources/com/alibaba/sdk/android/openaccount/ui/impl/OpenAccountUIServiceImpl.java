package com.alibaba.sdk.android.openaccount.ui.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import anetwork.channel.util.RequestConstant;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.config.ConfigService;
import com.alibaba.sdk.android.openaccount.message.Message;
import com.alibaba.sdk.android.openaccount.message.MessageUtils;
import com.alibaba.sdk.android.openaccount.model.OpenAccountSession;
import com.alibaba.sdk.android.openaccount.session.SessionManagerService;
import com.alibaba.sdk.android.openaccount.task.AbsRunnable;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService;
import com.alibaba.sdk.android.openaccount.ui.callback.AccountDeviceCallback;
import com.alibaba.sdk.android.openaccount.ui.callback.EmailRegisterCallback;
import com.alibaba.sdk.android.openaccount.ui.callback.EmailResetPasswordCallback;
import com.alibaba.sdk.android.openaccount.ui.callback.QrConfirmLoginCallback;
import com.alibaba.sdk.android.openaccount.ui.message.UIMessageConstants;
import com.alibaba.sdk.android.openaccount.ui.task.UpdateLoginIdTask;
import com.alibaba.sdk.android.openaccount.ui.task.UpdateProfileTask;
import com.alibaba.sdk.android.openaccount.ui.ui.AccountDeviceActivity;
import com.alibaba.sdk.android.openaccount.ui.ui.ChangeMobileActivity;
import com.alibaba.sdk.android.openaccount.ui.ui.EmailRegisterActivity;
import com.alibaba.sdk.android.openaccount.ui.ui.EmailResetPasswordActivity;
import com.alibaba.sdk.android.openaccount.ui.ui.LoginActivity;
import com.alibaba.sdk.android.openaccount.ui.ui.NoPasswordLoginActivity;
import com.alibaba.sdk.android.openaccount.ui.ui.OneStepRegisterActivity;
import com.alibaba.sdk.android.openaccount.ui.ui.QrLoginActivity;
import com.alibaba.sdk.android.openaccount.ui.ui.QrLoginConfirmActivity;
import com.alibaba.sdk.android.openaccount.ui.ui.RegisterActivity;
import com.alibaba.sdk.android.openaccount.ui.ui.ResetPasswordActivity;
import com.alibaba.sdk.android.openaccount.ui.ui.SpecialLoginActivity;
import com.alibaba.sdk.android.openaccount.ui.ut.UTConstants;
import com.alibaba.sdk.android.openaccount.ui.util.OpenAccountUIUtils;
import com.alibaba.sdk.android.openaccount.util.CommonUtils;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class OpenAccountUIServiceImpl implements OpenAccountUIService {
    public static AccountDeviceCallback _accountDeviceCallback;
    public static LoginCallback _changeMobileCallback;
    public static EmailRegisterCallback _emailRegisterCallback;
    public static EmailResetPasswordCallback _emailResetPasswordCallback;
    public static LoginCallback _loginCallback;
    public static LoginCallback _loginWithSmsCodeCallback;
    public static LoginCallback _noPasswordLoginCallback;
    public static LoginCallback _qrLoginCallback;
    public static QrConfirmLoginCallback _qrLoginConfirmCallback;
    public static LoginCallback _registerCallback;
    public static LoginCallback _resetPasswordCallback;
    public static LoginCallback _specialLoginCallback;
    public static LoginCallback _updateLoginIdCallback;
    public static LoginCallback _updateProfileCallback;

    @Autowired
    private ConfigService configService;

    @Autowired
    private SessionManagerService sessionManagerService;

    public void init() {
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showLogin(Context context, LoginCallback loginCallback) {
        showLogin(context, LoginActivity.class, null, loginCallback);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showLogin(Context context, Map<String, String> map, LoginCallback loginCallback) {
        showLogin(context, LoginActivity.class, map, loginCallback);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showSpecialLogin(Context context, LoginCallback loginCallback) {
        showSpecialLogin(context, SpecialLoginActivity.class, loginCallback);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showSpecialLogin(final Context context, final Class<?> cls, LoginCallback loginCallback) {
        ConfigManager.getInstance().putExtBizParam("createIfNotExist", RequestConstant.FALSE);
        _specialLoginCallback = loginCallback;
        CommonUtils.startInitWaitTask(context, loginCallback, new AbsRunnable() { // from class: com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountUIServiceImpl.1
            @Override // com.alibaba.sdk.android.openaccount.task.AbsRunnable
            public void runWithoutException() {
                Intent intent = new Intent(context, (Class<?>) cls);
                intent.setFlags(67108864);
                if (!(context instanceof Activity)) {
                    intent.addFlags(268435456);
                }
                intent.putExtra("scene", 0);
                context.startActivity(intent);
            }
        }, UTConstants.E_SHOW_SPECIAL_LOGIN);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showSpecialRegister(final Context context, LoginCallback loginCallback) {
        ConfigManager.getInstance().putExtBizParam("createIfNotExist", RequestConstant.FALSE);
        _specialLoginCallback = loginCallback;
        CommonUtils.startInitWaitTask(context, loginCallback, new AbsRunnable() { // from class: com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountUIServiceImpl.2
            @Override // com.alibaba.sdk.android.openaccount.task.AbsRunnable
            public void runWithoutException() {
                Intent intent = new Intent(context, (Class<?>) SpecialLoginActivity.class);
                intent.setFlags(67108864);
                if (!(context instanceof Activity)) {
                    intent.addFlags(268435456);
                }
                intent.putExtra("scene", 2);
                context.startActivity(intent);
            }
        }, UTConstants.E_SHOW_SPECIAL_LOGIN);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showSpecialRegister(final Context context, final Class<?> cls, LoginCallback loginCallback) {
        ConfigManager.getInstance().putExtBizParam("createIfNotExist", RequestConstant.FALSE);
        _specialLoginCallback = loginCallback;
        CommonUtils.startInitWaitTask(context, loginCallback, new AbsRunnable() { // from class: com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountUIServiceImpl.3
            @Override // com.alibaba.sdk.android.openaccount.task.AbsRunnable
            public void runWithoutException() {
                Intent intent = new Intent(context, (Class<?>) cls);
                intent.setFlags(67108864);
                if (!(context instanceof Activity)) {
                    intent.addFlags(268435456);
                }
                intent.putExtra("scene", 2);
                context.startActivity(intent);
            }
        }, UTConstants.E_SHOW_SPECIAL_LOGIN);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showNoPasswordLogin(Context context, Map<String, String> map, LoginCallback loginCallback) {
        showNoPasswordLogin(context, NoPasswordLoginActivity.class, map, loginCallback);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showNoPasswordLogin(final Context context, final Class<?> cls, final Map<String, String> map, LoginCallback loginCallback) {
        _noPasswordLoginCallback = loginCallback;
        CommonUtils.startInitWaitTask(context, loginCallback, new AbsRunnable() { // from class: com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountUIServiceImpl.4
            @Override // com.alibaba.sdk.android.openaccount.task.AbsRunnable
            public void runWithoutException() {
                Intent intent = new Intent(context, (Class<?>) cls);
                Map map2 = map;
                if (map2 != null) {
                    for (Map.Entry entry : map2.entrySet()) {
                        intent.putExtra((String) entry.getKey(), (String) entry.getValue());
                    }
                }
                if (!(context instanceof Activity)) {
                    intent.addFlags(268435456);
                }
                context.startActivity(intent);
            }
        }, UTConstants.E_SHOW_NO_PASSWORD_LOGIN);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showRegister(Context context, LoginCallback loginCallback) {
        showRegister(context, RegisterActivity.class, loginCallback);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showOneStepRegister(Context context, LoginCallback loginCallback) {
        showRegister(context, OneStepRegisterActivity.class, loginCallback);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showResetPassword(Context context, LoginCallback loginCallback) {
        showResetPassword(context, ResetPasswordActivity.class, loginCallback);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showLoginWithSmsCode(Context context, LoginCallback loginCallback) {
        showLoginWithSmsCode(context, ResetPasswordActivity.class, loginCallback);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showChangeMobile(Context context, LoginCallback loginCallback) {
        showChangeMobile(context, ChangeMobileActivity.class, loginCallback);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showNoPasswordLogin(Context context, LoginCallback loginCallback) {
        showNoPasswordLogin(context, NoPasswordLoginActivity.class, loginCallback);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showEmailRegister(Context context, EmailRegisterCallback emailRegisterCallback) {
        showEmailRegister(context, EmailRegisterActivity.class, emailRegisterCallback);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showLogin(final Context context, final Class<?> cls, final Map<String, String> map, LoginCallback loginCallback) {
        _loginCallback = loginCallback;
        CommonUtils.startInitWaitTask(context, loginCallback, new AbsRunnable() { // from class: com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountUIServiceImpl.5
            @Override // com.alibaba.sdk.android.openaccount.task.AbsRunnable
            public void runWithoutException() {
                Intent intent = new Intent(context, (Class<?>) cls);
                Map map2 = map;
                if (map2 != null) {
                    for (Map.Entry entry : map2.entrySet()) {
                        intent.putExtra((String) entry.getKey(), (String) entry.getValue());
                    }
                }
                intent.setFlags(67108864);
                if (!(context instanceof Activity)) {
                    intent.addFlags(268435456);
                }
                context.startActivity(intent);
            }
        }, UTConstants.E_SHOWLOGIN);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showLogin(Context context, Class<?> cls, LoginCallback loginCallback) {
        showLogin(context, cls, null, loginCallback);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showNoPasswordLogin(Context context, Class<?> cls, LoginCallback loginCallback) {
        showNoPasswordLogin(context, cls, null, loginCallback);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showChangeMobile(final Context context, final Class<?> cls, LoginCallback loginCallback) {
        _changeMobileCallback = loginCallback;
        if (CommonUtils.sessionFail(loginCallback)) {
            return;
        }
        CommonUtils.startInitWaitTask(context, loginCallback, new AbsRunnable() { // from class: com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountUIServiceImpl.6
            @Override // com.alibaba.sdk.android.openaccount.task.AbsRunnable
            public void runWithoutException() {
                Intent intent = new Intent(context, (Class<?>) cls);
                if (!(context instanceof Activity)) {
                    intent.addFlags(268435456);
                }
                context.startActivity(intent);
            }
        }, UTConstants.E_SHOW_CHANGE_MOBILE);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showEmailRegister(final Context context, final Class<?> cls, EmailRegisterCallback emailRegisterCallback) {
        _emailRegisterCallback = emailRegisterCallback;
        CommonUtils.startInitWaitTask(context, emailRegisterCallback, new AbsRunnable() { // from class: com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountUIServiceImpl.7
            @Override // com.alibaba.sdk.android.openaccount.task.AbsRunnable
            public void runWithoutException() {
                Intent intent = new Intent(context, (Class<?>) cls);
                if (!(context instanceof Activity)) {
                    intent.addFlags(268435456);
                }
                context.startActivity(intent);
            }
        }, UTConstants.E_SHOW_EMAIL_REGISTER);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showRegister(final Context context, final Class<?> cls, LoginCallback loginCallback) {
        _registerCallback = loginCallback;
        CommonUtils.startInitWaitTask(context, loginCallback, new AbsRunnable() { // from class: com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountUIServiceImpl.8
            @Override // com.alibaba.sdk.android.openaccount.task.AbsRunnable
            public void runWithoutException() {
                Intent intent = new Intent(context, (Class<?>) cls);
                if (!(context instanceof Activity)) {
                    intent.addFlags(268435456);
                }
                context.startActivity(intent);
            }
        }, UTConstants.E_SHOW_REGISTER);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showResetPassword(Context context, Class<?> cls, LoginCallback loginCallback) {
        showResetPassword(context, null, cls, loginCallback);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showResetPassword(final Context context, final Map<String, String> map, final Class<?> cls, LoginCallback loginCallback) {
        _resetPasswordCallback = loginCallback;
        CommonUtils.startInitWaitTask(context, loginCallback, new AbsRunnable() { // from class: com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountUIServiceImpl.9
            @Override // com.alibaba.sdk.android.openaccount.task.AbsRunnable
            public void runWithoutException() {
                Intent intent = new Intent(context, (Class<?>) cls);
                if (!(context instanceof Activity)) {
                    intent.addFlags(268435456);
                }
                Map map2 = map;
                if (map2 != null) {
                    for (Map.Entry entry : map2.entrySet()) {
                        intent.putExtra((String) entry.getKey(), (String) entry.getValue());
                    }
                }
                context.startActivity(intent);
            }
        }, UTConstants.E_SHOW_RESET_PASSWORD);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showLoginWithSmsCode(Context context, Class<?> cls, LoginCallback loginCallback) {
        showLoginWithSmsCode(context, null, cls, loginCallback);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showLoginWithSmsCode(final Context context, final Map<String, String> map, final Class<?> cls, LoginCallback loginCallback) {
        _loginWithSmsCodeCallback = loginCallback;
        CommonUtils.startInitWaitTask(context, loginCallback, new AbsRunnable() { // from class: com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountUIServiceImpl.10
            @Override // com.alibaba.sdk.android.openaccount.task.AbsRunnable
            public void runWithoutException() {
                Intent intent = new Intent(context, (Class<?>) cls);
                if (!(context instanceof Activity)) {
                    intent.addFlags(268435456);
                }
                Map map2 = map;
                if (map2 != null) {
                    for (Map.Entry entry : map2.entrySet()) {
                        intent.putExtra((String) entry.getKey(), (String) entry.getValue());
                    }
                }
                context.startActivity(intent);
            }
        }, UTConstants.E_SHOW_RESET_PASSWORD);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showQrCodeLogin(Context context, Map<String, String> map, LoginCallback loginCallback) {
        showQrCodeLogin(context, QrLoginActivity.class, map, loginCallback);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showQrCodeLogin(Context context, Class<?> cls, Map<String, String> map, LoginCallback loginCallback) {
        Intent intent = new Intent(context, cls);
        if (map != null) {
            intent.putExtra(OpenAccountUIConstants.QR_LOGIN_REQUEST_PARAMETERS_KEY, new HashMap(map));
        }
        _qrLoginCallback = loginCallback;
        if (!(context instanceof Activity)) {
            intent.addFlags(268435456);
        }
        context.startActivity(intent);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showQrLoginConfirm(Context context, Map<String, String> map, QrConfirmLoginCallback qrConfirmLoginCallback) {
        OpenAccountSession session;
        if (map == null || TextUtils.isEmpty(map.get(OpenAccountUIConstants.SHORTURL))) {
            return;
        }
        SessionManagerService sessionManagerService = this.sessionManagerService;
        if (sessionManagerService != null && ((session = sessionManagerService.getSession()) == null || !session.isLogin())) {
            Message messageCreateMessage = MessageUtils.createMessage(UIMessageConstants.QR_CONFIRM_USER_NOT_LOGIN, new Object[0]);
            AliSDKLogger.log(OpenAccountUIConstants.LOG_TAG, messageCreateMessage);
            qrConfirmLoginCallback.onFailure(messageCreateMessage.code, messageCreateMessage.message);
            return;
        }
        ConfigService configService = this.configService;
        Intent intent = new Intent(context, (Class<?>) QrLoginConfirmActivity.class);
        intent.putExtra("url", String.format(configService.getStringProperty(this.configService.getEnvironment().name() + "_QR_LOGIN_CONFIRM_URL", "http://login-openaccount.taobao.com/login/qrcodeLoginV2.htm?shortURL=%s"), map.get(OpenAccountUIConstants.SHORTURL)) + "&from=" + OpenAccountUIUtils.getQrFromParameter(map));
        intent.putExtra("title", ResourceUtils.getString(context, "ali_sdk_openaccount_dynamic_text_qr_confirm_title_bar"));
        _qrLoginConfirmCallback = qrConfirmLoginCallback;
        if (!(context instanceof Activity)) {
            intent.addFlags(268435456);
        }
        context.startActivity(intent);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showEmailResetPassword(Context context, EmailResetPasswordCallback emailResetPasswordCallback) {
        showEmailResetPassword(context, EmailResetPasswordActivity.class, emailResetPasswordCallback);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showEmailResetPassword(final Context context, final Class<?> cls, EmailResetPasswordCallback emailResetPasswordCallback) {
        _emailResetPasswordCallback = emailResetPasswordCallback;
        CommonUtils.startInitWaitTask(context, emailResetPasswordCallback, new AbsRunnable() { // from class: com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountUIServiceImpl.11
            @Override // com.alibaba.sdk.android.openaccount.task.AbsRunnable
            public void runWithoutException() {
                Intent intent = new Intent(context, (Class<?>) cls);
                if (!(context instanceof Activity)) {
                    intent.addFlags(268435456);
                }
                context.startActivity(intent);
            }
        }, UTConstants.E_SHOW_EMAIL_REGISTER);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showDeviceManager(Context context, AccountDeviceCallback accountDeviceCallback) {
        showDeviceManager(context, AccountDeviceActivity.class, accountDeviceCallback);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void showDeviceManager(Context context, Class<?> cls, AccountDeviceCallback accountDeviceCallback) {
        if (CommonUtils.sessionFail(accountDeviceCallback)) {
            return;
        }
        _accountDeviceCallback = accountDeviceCallback;
        Intent intent = new Intent(context, cls);
        if (!(context instanceof Activity)) {
            intent.addFlags(268435456);
        }
        context.startActivity(intent);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void updateProfile(Context context, Map<String, Object> map, LoginCallback loginCallback) {
        _updateProfileCallback = loginCallback;
        if (CommonUtils.sessionFail(loginCallback)) {
            return;
        }
        new UpdateProfileTask(context, map).execute(new Void[0]);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void updateLoginId(Context context, String str, boolean z, LoginCallback loginCallback) {
        SessionManagerService sessionManagerService;
        _updateLoginIdCallback = loginCallback;
        if (CommonUtils.sessionFail(loginCallback)) {
            return;
        }
        if (z && (sessionManagerService = this.sessionManagerService) != null && !TextUtils.isEmpty(sessionManagerService.getSession().getUser().nick)) {
            Message messageCreateMessage = MessageUtils.createMessage(10013, new Object[0]);
            AliSDKLogger.log(OpenAccountUIConstants.LOG_TAG, messageCreateMessage);
            if (loginCallback != null) {
                loginCallback.onFailure(messageCreateMessage.code, messageCreateMessage.message);
                return;
            }
            return;
        }
        new UpdateLoginIdTask(context, str, z).execute(new Void[0]);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService
    public void updateLoginId(Context context, String str, LoginCallback loginCallback) {
        updateLoginId(context, str, false, loginCallback);
    }
}
