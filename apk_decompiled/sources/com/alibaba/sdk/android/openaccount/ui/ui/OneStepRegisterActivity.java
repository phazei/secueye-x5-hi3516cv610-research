package com.alibaba.sdk.android.openaccount.ui.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.annotation.ExtensionPoint;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.model.Result;
import com.alibaba.sdk.android.openaccount.model.SessionData;
import com.alibaba.sdk.android.openaccount.task.TaskWithDialog;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConfigs;
import com.alibaba.sdk.android.openaccount.ui.RequestCode;
import com.alibaba.sdk.android.openaccount.ui.model.RegisterResult;
import com.alibaba.sdk.android.openaccount.ui.util.ToastUtils;
import com.alibaba.sdk.android.openaccount.ui.widget.NetworkCheckOnClickListener;
import com.alibaba.sdk.android.openaccount.ui.widget.NextStepButtonWatcher;
import com.alibaba.sdk.android.openaccount.ui.widget.PasswordInputBox;
import com.alibaba.sdk.android.openaccount.util.OpenAccountUtils;
import com.alibaba.sdk.android.openaccount.util.RpcUtils;
import com.alibaba.sdk.android.openaccount.util.safe.RSAKey;
import com.alibaba.sdk.android.openaccount.util.safe.Rsa;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.facebook.internal.NativeProtocol;
import com.taobao.accs.utl.UtilityImpl;
import java.util.HashMap;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
@ExtensionPoint
public class OneStepRegisterActivity extends RegisterActivity {
    protected PasswordInputBox passwordInputBox;
    protected String smsCheckTrustToken;

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.RegisterActivity, com.alibaba.sdk.android.openaccount.ui.ui.ActivityTemplate
    protected String getLayoutName() {
        return "ali_sdk_openaccount_register_one_step";
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.RegisterActivity, com.alibaba.sdk.android.openaccount.ui.ui.SendSmsCodeActivity, com.alibaba.sdk.android.openaccount.ui.ui.NextStepActivityTemplate, com.alibaba.sdk.android.openaccount.ui.ui.ActivityTemplate, com.alibaba.sdk.android.openaccount.ui.ui.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.next.setOnClickListener(new NetworkCheckOnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.OneStepRegisterActivity.1
            @Override // com.alibaba.sdk.android.openaccount.ui.widget.NetworkCheckOnClickListener
            public void afterCheck(View view2) {
                OneStepRegisterActivity.this.doRegister(null, null, null);
            }
        });
        this.passwordInputBox = (PasswordInputBox) findViewById("password_input_box");
        this.passwordInputBox.setUsePasswordMasking(OpenAccountUIConfigs.OneStepMobileRegisterFlow.usePasswordMaskingForRegister);
        NextStepButtonWatcher nextStepButtonWatcher = getNextStepButtonWatcher();
        nextStepButtonWatcher.setNextStepButton(this.next);
        nextStepButtonWatcher.addEditText(this.passwordInputBox.getEditText());
        this.passwordInputBox.getInputBoxWithClear().addTextChangedListener(nextStepButtonWatcher);
        this.mobileInputBox.setSupportForeignMobile(this, OpenAccountUIConfigs.OneStepMobileRegisterFlow.mobileCountrySelectorActvityClazz, OpenAccountUIConfigs.OneStepMobileRegisterFlow.supportForeignMobileNumbers);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doRegister(String str, String str2, String str3) {
        new OneStepRegistrationTask(this, str, str2, str3).execute(new Void[0]);
    }

    protected class OneStepRegistrationTask extends TaskWithDialog<Void, Void, Result<RegisterResult>> {
        private String mCSessionId;
        private String mNocToken;
        private String mSig;
        private String serverVerifyData;

        public OneStepRegistrationTask(Activity activity2, String str) {
            super(activity2);
            this.serverVerifyData = str;
        }

        public OneStepRegistrationTask(Activity activity2, String str, String str2, String str3) {
            super(activity2);
            this.mSig = str;
            this.mCSessionId = str3;
            this.mNocToken = str2;
        }

        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        protected void doWhenException(Throwable th) {
            this.executorService.postUITask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.OneStepRegisterActivity.OneStepRegistrationTask.1
                @Override // java.lang.Runnable
                public void run() {
                    ToastUtils.toastSystemError(OneStepRegistrationTask.this.context);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        public Result<RegisterResult> asyncExecute(Void... voidArr) {
            OneStepRegisterActivity.this.clientVerifyData = null;
            HashMap map = new HashMap();
            HashMap map2 = new HashMap();
            map.put("checkCodeRequest", map2);
            map2.put("serverVerifyData", this.serverVerifyData);
            HashMap map3 = new HashMap();
            map3.put(UtilityImpl.NET_TYPE_MOBILE, OneStepRegisterActivity.this.mobileInputBox.getEditTextContent());
            map3.put(AlinkConstants.KEY_MOBILE_LOCATION_CODE, OneStepRegisterActivity.this.mobileInputBox.getMobileLocationCode());
            map3.put("smsCode", OneStepRegisterActivity.this.smsCodeInputBox.getInputBoxWithClear().getEditTextContent());
            if (!TextUtils.isEmpty(this.mSig)) {
                map2.put("sig", this.mSig);
            }
            if (!TextUtils.isEmpty(this.mCSessionId)) {
                map2.put("csessionid", this.mCSessionId);
            }
            if (!TextUtils.isEmpty(this.mNocToken)) {
                map2.put("nctoken", this.mNocToken);
                map3.put("smsToken", OneStepRegisterActivity.this.smsCheckTrustToken);
            }
            OneStepRegisterActivity.this.smsCheckTrustToken = "";
            map.put("checkSmsCodeRequest", map3);
            try {
                String rsaPubkey = RSAKey.getRsaPubkey();
                if (TextUtils.isEmpty(rsaPubkey)) {
                    return null;
                }
                map.put("password", Rsa.encrypt(OneStepRegisterActivity.this.passwordInputBox.getInputBoxWithClear().getEditTextContent(), rsaPubkey));
                return parseJsonResult(RpcUtils.invokeWithRiskControlInfo("registerRequest", map, "onestepregister"));
            } catch (Exception unused) {
                return null;
            }
        }

        protected Result<RegisterResult> parseJsonResult(Result<JSONObject> result) {
            if (result.data == null) {
                return Result.result(result.code, result.message);
            }
            return Result.result(result.code, result.message, parseData(result.data));
        }

        protected RegisterResult parseData(JSONObject jSONObject) {
            RegisterResult registerResult = new RegisterResult();
            registerResult.clientVerifyData = jSONObject.optString("clientVerifyData");
            registerResult.smsCheckTrustToken = jSONObject.optString("smsCheckTrustToken");
            if (jSONObject.has("loginSuccessResult")) {
                registerResult.loginSuccessResult = OpenAccountUtils.parseLoginSuccessResult(jSONObject);
            }
            return registerResult;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Result<RegisterResult> result) {
            super.onPostExecute((Object) result);
            try {
                if (result == null) {
                    ToastUtils.toastSystemError(this.context);
                    return;
                }
                int i = result.code;
                if (i == 1) {
                    if (result.data == null || result.data.loginSuccessResult == null) {
                        return;
                    }
                    SessionData sessionDataCreateSessionDataFromLoginSuccessResult = OpenAccountUtils.createSessionDataFromLoginSuccessResult(result.data.loginSuccessResult);
                    if (sessionDataCreateSessionDataFromLoginSuccessResult.scenario == null) {
                        sessionDataCreateSessionDataFromLoginSuccessResult.scenario = 2;
                    }
                    OneStepRegisterActivity.this.sessionManagerService.updateSession(sessionDataCreateSessionDataFromLoginSuccessResult);
                    OpenAccountSDK.getSqliteUtil().saveToSqlite(OneStepRegisterActivity.this.mobileInputBox.getEditTextContent(), OneStepRegisterActivity.this.passwordInputBox.getInputBoxWithClear().getEditTextContent());
                    LoginCallback loginCallback = OneStepRegisterActivity.this.getLoginCallback();
                    if (loginCallback != null) {
                        loginCallback.onSuccess(OneStepRegisterActivity.this.sessionManagerService.getSession());
                    }
                    OneStepRegisterActivity.this.setResult(-1);
                    OneStepRegisterActivity.this.finishWithoutCancel();
                    return;
                }
                if (i == 26053) {
                    if (result.data == null || TextUtils.isEmpty(result.data.clientVerifyData)) {
                        return;
                    }
                    OneStepRegisterActivity.this.smsCheckTrustToken = result.data.smsCheckTrustToken;
                    Uri.Builder builderBuildUpon = Uri.parse(result.data.clientVerifyData).buildUpon();
                    builderBuildUpon.appendQueryParameter("callback", "https://www.alipay.com/webviewbridge");
                    Intent intent = new Intent(OneStepRegisterActivity.this, (Class<?>) LoginDoubleCheckWebActivity.class);
                    intent.putExtra("url", builderBuildUpon.toString());
                    intent.putExtra("title", result.message);
                    intent.putExtra("callback", "https://www.alipay.com/webviewbridge");
                    OneStepRegisterActivity.this.startActivityForResult(intent, RequestCode.NO_CAPTCHA_REQUEST_CODE);
                    return;
                }
                if (TextUtils.isEmpty(result.message)) {
                    ToastUtils.toastSystemError(this.context);
                } else {
                    ToastUtils.toast(this.context, result.message, result.code);
                }
            } catch (Throwable th) {
                th.printStackTrace();
                ToastUtils.toastSystemError(this.context);
            }
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.RegisterActivity, com.alibaba.sdk.android.openaccount.ui.ui.SendSmsCodeActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == RequestCode.NO_CAPTCHA_REQUEST_CODE) {
            if (i2 == -1) {
                if (intent == null || !"nocaptcha".equals(intent.getStringExtra(NativeProtocol.WEB_DIALOG_ACTION))) {
                    return;
                }
                String stringExtra = intent.getStringExtra("cSessionId");
                doRegister(intent.getStringExtra("sig"), intent.getStringExtra("nocToken"), stringExtra);
                return;
            }
            this.smsCheckTrustToken = "";
        }
    }
}
