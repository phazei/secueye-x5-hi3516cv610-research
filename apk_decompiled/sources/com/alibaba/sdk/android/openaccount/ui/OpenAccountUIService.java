package com.alibaba.sdk.android.openaccount.ui;

import android.content.Context;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.ui.callback.AccountDeviceCallback;
import com.alibaba.sdk.android.openaccount.ui.callback.EmailRegisterCallback;
import com.alibaba.sdk.android.openaccount.ui.callback.EmailResetPasswordCallback;
import com.alibaba.sdk.android.openaccount.ui.callback.QrConfirmLoginCallback;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface OpenAccountUIService {
    void showChangeMobile(Context context, LoginCallback loginCallback);

    void showChangeMobile(Context context, Class<?> cls, LoginCallback loginCallback);

    void showDeviceManager(Context context, AccountDeviceCallback accountDeviceCallback);

    void showDeviceManager(Context context, Class<?> cls, AccountDeviceCallback accountDeviceCallback);

    void showEmailRegister(Context context, EmailRegisterCallback emailRegisterCallback);

    void showEmailRegister(Context context, Class<?> cls, EmailRegisterCallback emailRegisterCallback);

    void showEmailResetPassword(Context context, EmailResetPasswordCallback emailResetPasswordCallback);

    void showEmailResetPassword(Context context, Class<?> cls, EmailResetPasswordCallback emailResetPasswordCallback);

    void showLogin(Context context, LoginCallback loginCallback);

    void showLogin(Context context, Class<?> cls, LoginCallback loginCallback);

    void showLogin(Context context, Class<?> cls, Map<String, String> map, LoginCallback loginCallback);

    void showLogin(Context context, Map<String, String> map, LoginCallback loginCallback);

    void showLoginWithSmsCode(Context context, LoginCallback loginCallback);

    void showLoginWithSmsCode(Context context, Class<?> cls, LoginCallback loginCallback);

    void showLoginWithSmsCode(Context context, Map<String, String> map, Class<?> cls, LoginCallback loginCallback);

    void showNoPasswordLogin(Context context, LoginCallback loginCallback);

    void showNoPasswordLogin(Context context, Class<?> cls, LoginCallback loginCallback);

    void showNoPasswordLogin(Context context, Class<?> cls, Map<String, String> map, LoginCallback loginCallback);

    void showNoPasswordLogin(Context context, Map<String, String> map, LoginCallback loginCallback);

    void showOneStepRegister(Context context, LoginCallback loginCallback);

    void showQrCodeLogin(Context context, Class<?> cls, Map<String, String> map, LoginCallback loginCallback);

    void showQrCodeLogin(Context context, Map<String, String> map, LoginCallback loginCallback);

    void showQrLoginConfirm(Context context, Map<String, String> map, QrConfirmLoginCallback qrConfirmLoginCallback);

    void showRegister(Context context, LoginCallback loginCallback);

    void showRegister(Context context, Class<?> cls, LoginCallback loginCallback);

    void showResetPassword(Context context, LoginCallback loginCallback);

    void showResetPassword(Context context, Class<?> cls, LoginCallback loginCallback);

    void showResetPassword(Context context, Map<String, String> map, Class<?> cls, LoginCallback loginCallback);

    void showSpecialLogin(Context context, LoginCallback loginCallback);

    void showSpecialLogin(Context context, Class<?> cls, LoginCallback loginCallback);

    void showSpecialRegister(Context context, LoginCallback loginCallback);

    void showSpecialRegister(Context context, Class<?> cls, LoginCallback loginCallback);

    void updateLoginId(Context context, String str, LoginCallback loginCallback);

    void updateLoginId(Context context, String str, boolean z, LoginCallback loginCallback);

    void updateProfile(Context context, Map<String, Object> map, LoginCallback loginCallback);
}
