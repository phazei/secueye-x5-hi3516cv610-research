package com.alibaba.sdk.android.openaccount.ui.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.alibaba.sdk.android.openaccount.annotation.ExtensionPoint;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.session.SessionManagerService;
import com.alibaba.sdk.android.openaccount.ui.widget.MobileInputBoxWithClear;
import com.alibaba.sdk.android.openaccount.ui.widget.NextStepButtonWatcher;
import com.alibaba.sdk.android.openaccount.ui.widget.SmsCodeInputBox;
import com.alibaba.sdk.android.pluto.annotation.Autowired;

/* JADX INFO: loaded from: classes.dex */
@ExtensionPoint
public abstract class SendSmsCodeActivity extends NextStepActivityTemplate {
    protected MobileInputBoxWithClear mobileInputBox;

    @Autowired
    protected SessionManagerService sessionManagerService;
    protected SmsCodeInputBox smsCodeInputBox;

    protected abstract LoginCallback getLoginCallback();

    protected abstract void onUserCancel();

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.NextStepActivityTemplate, com.alibaba.sdk.android.openaccount.ui.ui.ActivityTemplate, com.alibaba.sdk.android.openaccount.ui.ui.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mobileInputBox = (MobileInputBoxWithClear) findViewById("mobile_input_box");
        this.smsCodeInputBox = (SmsCodeInputBox) findViewById("sms_code_input_box");
        NextStepButtonWatcher nextStepButtonWatcher = getNextStepButtonWatcher();
        nextStepButtonWatcher.addEditTexts(this.smsCodeInputBox.getEditText(), this.mobileInputBox.getEditText());
        this.smsCodeInputBox.getInputBoxWithClear().addTextChangedListener(nextStepButtonWatcher);
        this.mobileInputBox.addTextChangedListener(nextStepButtonWatcher);
        useCustomAttrs(this, this.attrs);
        if (bundle != null) {
            String string = bundle.getString("loginId");
            if (!TextUtils.isEmpty(string)) {
                this.mobileInputBox.getEditText().setText(string);
            }
        }
        if (bundle != null) {
            if (bundle.getString("mobileInputBox") != null) {
                this.mobileInputBox.getEditText().setText(bundle.getString("mobileInputBox"));
            }
            if (bundle.getString("smsCodeInputBox") != null) {
                this.smsCodeInputBox.getEditText().setText(bundle.getString("smsCodeInputBox"));
            }
        }
    }

    @Override // android.app.Activity
    public void finish() {
        super.finish();
        onUserCancel();
    }

    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("mobileInputBox", this.mobileInputBox.getEditTextContent());
        bundle.putString("smsCodeInputBox", String.valueOf(this.smsCodeInputBox.getEditText().getText()));
    }

    protected void finishWithoutCancel() {
        super.finish();
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (this.mobileInputBox.onActivityResult(i, i2, intent)) {
        }
    }

    public void finishWithoutCallback() {
        super.finish();
    }
}
