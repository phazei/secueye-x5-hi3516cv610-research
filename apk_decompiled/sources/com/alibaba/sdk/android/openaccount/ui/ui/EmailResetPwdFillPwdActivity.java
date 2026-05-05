package com.alibaba.sdk.android.openaccount.ui.ui;

import android.app.Activity;
import android.text.TextUtils;
import com.alibaba.sdk.android.openaccount.annotation.ExtensionPoint;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.model.Result;
import com.alibaba.sdk.android.openaccount.model.SessionData;
import com.alibaba.sdk.android.openaccount.task.TaskWithDialog;
import com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountUIServiceImpl;
import com.alibaba.sdk.android.openaccount.ui.model.EmailRegisterResult;
import com.alibaba.sdk.android.openaccount.ui.util.ToastUtils;
import com.alibaba.sdk.android.openaccount.util.OpenAccountUtils;
import com.alibaba.sdk.android.openaccount.util.RpcUtils;
import com.alibaba.sdk.android.openaccount.util.safe.RSAKey;
import com.alibaba.sdk.android.openaccount.util.safe.Rsa;
import java.util.HashMap;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
@ExtensionPoint
public class EmailResetPwdFillPwdActivity extends FillPasswordActivity {
    protected String email;
    protected String emailToken;

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.ActivityTemplate
    protected String getLayoutName() {
        return "ali_sdk_openaccount_register_fill_password";
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.FillPasswordActivity
    protected String getRequestKey() {
        return "checkEmailTokenRequest";
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.FillPasswordActivity
    protected int getScenario() {
        return 0;
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.FillPasswordActivity
    protected String getTarget() {
        return "emailvalidatetoken";
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.FillPasswordActivity
    protected void initParams() {
        try {
            this.email = getIntent().getStringExtra("email");
            this.emailToken = getIntent().getStringExtra("emailToken");
        } catch (Exception unused) {
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.FillPasswordActivity
    protected LoginCallback getLoginCallback() {
        return OpenAccountUIServiceImpl._emailResetPasswordCallback;
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.FillPasswordActivity
    protected void resetPassword() {
        new CheckEmailTask(this).execute(new Void[0]);
    }

    class CheckEmailTask extends TaskWithDialog<Void, Void, Result<EmailRegisterResult>> {
        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        protected void doWhenException(Throwable th) {
        }

        public CheckEmailTask(Activity activity2) {
            super(activity2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        public Result<EmailRegisterResult> asyncExecute(Void... voidArr) {
            HashMap map = new HashMap();
            map.put("email", EmailResetPwdFillPwdActivity.this.email);
            map.put("actionType", "sdk_email_reset_password");
            map.put("emailToken", EmailResetPwdFillPwdActivity.this.emailToken);
            try {
                String rsaPubkey = RSAKey.getRsaPubkey();
                if (TextUtils.isEmpty(rsaPubkey)) {
                    return null;
                }
                map.put("password", Rsa.encrypt(EmailResetPwdFillPwdActivity.this.passwordInputBox.getInputBoxWithClear().getEditTextContent(), rsaPubkey));
                return parseJsonResult(RpcUtils.invokeWithRiskControlInfo("checkEmailTokenRequest", map, "emailvalidatetoken"));
            } catch (Exception unused) {
                return null;
            }
        }

        protected Result<EmailRegisterResult> parseJsonResult(Result<JSONObject> result) {
            if (result.data == null) {
                return Result.result(result.code, result.message);
            }
            return Result.result(result.code, result.message, parseData(result.data));
        }

        protected EmailRegisterResult parseData(JSONObject jSONObject) {
            EmailRegisterResult emailRegisterResult = new EmailRegisterResult();
            if (jSONObject.has("loginSuccessResult")) {
                emailRegisterResult.loginSuccessResult = OpenAccountUtils.parseLoginSuccessResult(jSONObject);
            }
            return emailRegisterResult;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Result<EmailRegisterResult> result) {
            super.onPostExecute((Object) result);
            if (result == null) {
                ToastUtils.toastSystemError(this.context);
                return;
            }
            if (result.code == 1) {
                if (result.data == null || result.data.loginSuccessResult == null) {
                    return;
                }
                SessionData sessionDataCreateSessionDataFromLoginSuccessResult = OpenAccountUtils.createSessionDataFromLoginSuccessResult(result.data.loginSuccessResult);
                Integer num = sessionDataCreateSessionDataFromLoginSuccessResult.scenario;
                EmailResetPwdFillPwdActivity.this.sessionManagerService.updateSession(sessionDataCreateSessionDataFromLoginSuccessResult);
                EmailResetPwdFillPwdActivity.this.setResult(-1);
                EmailResetPwdFillPwdActivity.this.finishWithoutCancel();
                return;
            }
            if (TextUtils.isEmpty(result.message)) {
                ToastUtils.toastSystemError(this.context);
            } else {
                ToastUtils.toast(this.context, result.message, result.code);
            }
        }
    }

    protected void finishWithoutCancel() {
        super.finish();
    }
}
