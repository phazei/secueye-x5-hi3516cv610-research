package com.alibaba.sdk.android.openaccount.ui.ui;

import android.os.Bundle;
import com.alibaba.sdk.android.openaccount.annotation.ExtensionPoint;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConfigs;
import com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountUIServiceImpl;

/* JADX INFO: loaded from: classes.dex */
@ExtensionPoint
public class RegisterFillPasswordActivity extends FillPasswordActivity {
    @Override // com.alibaba.sdk.android.openaccount.ui.ui.ActivityTemplate
    protected String getLayoutName() {
        return "ali_sdk_openaccount_register_fill_password";
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.FillPasswordActivity
    protected String getRequestKey() {
        return "registerRequest";
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.FillPasswordActivity
    protected int getScenario() {
        return 2;
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.FillPasswordActivity
    protected String getTarget() {
        return "register";
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.FillPasswordActivity, com.alibaba.sdk.android.openaccount.ui.ui.NextStepActivityTemplate, com.alibaba.sdk.android.openaccount.ui.ui.ActivityTemplate, com.alibaba.sdk.android.openaccount.ui.ui.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.passwordInputBox.setUsePasswordMasking(OpenAccountUIConfigs.MobileRegisterFlow.usePasswordMaskingForRegister);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.FillPasswordActivity
    protected LoginCallback getLoginCallback() {
        return OpenAccountUIServiceImpl._registerCallback;
    }
}
