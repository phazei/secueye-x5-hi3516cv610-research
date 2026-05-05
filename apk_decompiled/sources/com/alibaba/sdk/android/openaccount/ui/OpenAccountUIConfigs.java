package com.alibaba.sdk.android.openaccount.ui;

import android.app.Activity;
import com.alibaba.sdk.android.openaccount.ui.ui.EmailResetPasswordActivity;
import com.alibaba.sdk.android.openaccount.ui.ui.EmailResetPwdFillPwdActivity;
import com.alibaba.sdk.android.openaccount.ui.ui.LoginWithSmsCodeActivity;
import com.alibaba.sdk.android.openaccount.ui.ui.MobileCountrySelectorActivity;
import com.alibaba.sdk.android.openaccount.ui.ui.RegisterFillPasswordActivity;
import com.alibaba.sdk.android.openaccount.ui.ui.ResetPasswordActivity;
import com.alibaba.sdk.android.openaccount.ui.ui.ResetPasswordFillPasswordActivity;

/* JADX INFO: loaded from: classes.dex */
public class OpenAccountUIConfigs {
    public static boolean hideNavigationBar = false;
    public static boolean showToolbar = true;
    public static boolean statusBarDarkMode = true;
    public static boolean statusBarTranslucent = true;

    public static class AccountPasswordLoginFlow {
        public static int historyAccountNum = 3;
        public static Class<? extends Activity> mobileCountrySelectorActvityClazz = MobileCountrySelectorActivity.class;
        public static boolean showAlertForPwdErrorToManyTimes = true;
        public static boolean showTipAlertAfterLogin = false;
        public static boolean supportForeignMobileNumbers = false;
        public static boolean usePasswordMaskingForLogin = true;
    }

    public static class ChangeMobileFlow {
        public static Class<? extends Activity> mobileCountrySelectorActvityClazz = MobileCountrySelectorActivity.class;
        public static boolean supportForeignMobileNumbers = false;
    }

    public static class EmailRegisterFlow {
    }

    public static class EmailResetPasswordLoginFlow {
        public static Class<? extends Activity> resetPasswordFillActivityClazz = EmailResetPwdFillPwdActivity.class;
        public static Class<? extends Activity> resetPasswordActivityClazz = EmailResetPasswordActivity.class;
    }

    public static class LoginWithSmsCodeFlow {
        public static Class<? extends Activity> loginWithSmsCodeActivityClass = LoginWithSmsCodeActivity.class;
    }

    public static class MobileNoPasswordLoginFlow {
        public static Class<? extends Activity> mobileCountrySelectorActvityClazz = MobileCountrySelectorActivity.class;
        public static boolean supportForeignMobileNumbers = false;
    }

    public static class MobileRegisterFlow {
        public static boolean supportForeignMobileNumbers = false;
        public static boolean usePasswordMaskingForRegister = false;
        public static Class<? extends Activity> registerFillPasswordActivityClazz = RegisterFillPasswordActivity.class;
        public static Class<? extends Activity> mobileCountrySelectorActvityClazz = MobileCountrySelectorActivity.class;
    }

    public static class MobileResetPasswordLoginFlow {
        public static boolean supportForeignMobileNumbers = false;
        public static boolean supportResetPasswordWithNick = false;
        public static boolean usePasswordMaskingForReset = false;
        public static Class<? extends Activity> resetPasswordPasswordActivityClazz = ResetPasswordFillPasswordActivity.class;
        public static Class<? extends Activity> mobileCountrySelectorActvityClazz = MobileCountrySelectorActivity.class;
    }

    public static class OneStepMobileRegisterFlow {
        public static Class<? extends Activity> mobileCountrySelectorActvityClazz = MobileCountrySelectorActivity.class;
        public static boolean supportForeignMobileNumbers = false;
        public static boolean usePasswordMaskingForRegister = false;
    }

    public static class UnifyLoginFlow {
        public static Class<? extends Activity> resetPasswordActivityClass = ResetPasswordActivity.class;
    }
}
