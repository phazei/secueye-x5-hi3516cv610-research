package com.alibaba.sdk.android.openaccount.ui.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.annotation.ExtensionPoint;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.model.CheckCodeResult;
import com.alibaba.sdk.android.openaccount.model.Result;
import com.alibaba.sdk.android.openaccount.model.SessionData;
import com.alibaba.sdk.android.openaccount.session.SessionManagerService;
import com.alibaba.sdk.android.openaccount.task.TaskWithDialog;
import com.alibaba.sdk.android.openaccount.ui.R;
import com.alibaba.sdk.android.openaccount.ui.RequestCode;
import com.alibaba.sdk.android.openaccount.ui.callback.EmailRegisterCallback;
import com.alibaba.sdk.android.openaccount.ui.helper.IPassWordCheckListener;
import com.alibaba.sdk.android.openaccount.ui.helper.ITextChangeListner;
import com.alibaba.sdk.android.openaccount.ui.helper.PasswordCheckHelper;
import com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountUIServiceImpl;
import com.alibaba.sdk.android.openaccount.ui.model.EmailRegisterResult;
import com.alibaba.sdk.android.openaccount.ui.model.SendEmailResult;
import com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage;
import com.alibaba.sdk.android.openaccount.ui.util.ToastUtils;
import com.alibaba.sdk.android.openaccount.ui.widget.InputBoxWithClear;
import com.alibaba.sdk.android.openaccount.ui.widget.NetworkCheckOnClickListener;
import com.alibaba.sdk.android.openaccount.ui.widget.NextStepButtonWatcher;
import com.alibaba.sdk.android.openaccount.ui.widget.PasswordInputBox;
import com.alibaba.sdk.android.openaccount.ui.widget.PasswordLevelView;
import com.alibaba.sdk.android.openaccount.ui.widget.SmsCodeInputBox;
import com.alibaba.sdk.android.openaccount.util.OpenAccountUtils;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;
import com.alibaba.sdk.android.openaccount.util.RpcUtils;
import com.alibaba.sdk.android.openaccount.util.safe.RSAKey;
import com.alibaba.sdk.android.openaccount.util.safe.Rsa;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import com.facebook.internal.NativeProtocol;
import java.util.HashMap;
import org.json.JSONObject;
import org.mozilla.javascript.ES6Iterator;

/* JADX INFO: loaded from: classes.dex */
@ExtensionPoint
public class EmailRegisterActivity extends NextStepActivityTemplate {
    protected SmsCodeInputBox emailCodeInputBox;
    protected String emailUrl;
    protected TextView mHintTextView;
    protected PasswordLevelView mPasswordLevelView;
    protected InputBoxWithClear mailInputBox;
    protected PasswordCheckHelper passwordCheckHelper;
    protected PasswordInputBox passwordInputBox;
    protected Button sendEmailButton;

    @Autowired
    protected SessionManagerService sessionManagerService;
    protected String smsCheckTrustToken;

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.ActivityTemplate
    protected String getLayoutName() {
        return "ali_sdk_openaccount_mail_register";
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.NextStepActivityTemplate, com.alibaba.sdk.android.openaccount.ui.ui.ActivityTemplate, com.alibaba.sdk.android.openaccount.ui.ui.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mailInputBox = (InputBoxWithClear) findViewById("mail_input_box");
        this.sendEmailButton = (Button) findViewById(ES6Iterator.NEXT_METHOD);
        this.emailCodeInputBox = (SmsCodeInputBox) findViewById("check_code_input_box");
        this.emailCodeInputBox.getSend().setText(ResourceUtils.getString("ali_sdk_openaccount_text_sendmail"));
        this.emailCodeInputBox.setSendText("ali_sdk_openaccount_text_sendmail");
        this.passwordInputBox = (PasswordInputBox) findViewById("password_input_box");
        this.mHintTextView = (TextView) findViewById(R.id.ali_oa_send_email_success_tip);
        this.mHintTextView.setText(getString(R.string.ali_sdk_openaccount_text_register_password_rule, new Object[]{Integer.valueOf(ConfigManager.getInstance().getMinPasswordLength()), Integer.valueOf(ConfigManager.getInstance().getMaxPasswordLength())}));
        this.mPasswordLevelView = (PasswordLevelView) findViewById(R.id.ali_oa_password_stronger);
        NextStepButtonWatcher nextStepButtonWatcher = getNextStepButtonWatcher();
        getNextStepButtonWatcher().addEditTexts(this.mailInputBox.getEditText(), this.passwordInputBox.getEditText(), this.emailCodeInputBox.getEditText());
        if (ConfigManager.getInstance().isShowPasswordStrengthHint()) {
            getNextStepButtonWatcher().setPasswordInputBox(this.passwordInputBox.getEditText());
            nextStepButtonWatcher.setPassWordChangeListner(new ITextChangeListner() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.EmailRegisterActivity.1
                @Override // com.alibaba.sdk.android.openaccount.ui.helper.ITextChangeListner
                public void onTextChange(String str) {
                    if (EmailRegisterActivity.this.passwordCheckHelper == null) {
                        EmailRegisterActivity.this.passwordCheckHelper = new PasswordCheckHelper(str);
                    } else {
                        EmailRegisterActivity.this.passwordCheckHelper.setPassword(str);
                    }
                    EmailRegisterActivity.this.passwordCheckHelper.check(new IPassWordCheckListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.EmailRegisterActivity.1.1
                        @Override // com.alibaba.sdk.android.openaccount.ui.helper.IPassWordCheckListener
                        public void onCheckPassword(int i) {
                            EmailRegisterActivity.this.mPasswordLevelView.setPasswordLevel(i);
                        }
                    });
                }
            });
        } else {
            this.mPasswordLevelView.setVisibility(8);
        }
        this.mailInputBox.addTextChangedListener(nextStepButtonWatcher);
        this.emailCodeInputBox.getInputBoxWithClear().addTextChangedListener(nextStepButtonWatcher);
        this.passwordInputBox.getInputBoxWithClear().addTextChangedListener(nextStepButtonWatcher);
        this.emailCodeInputBox.addSendClickListener(new NetworkCheckOnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.EmailRegisterActivity.2
            @Override // com.alibaba.sdk.android.openaccount.ui.widget.NetworkCheckOnClickListener
            public void afterCheck(View view2) {
                EmailRegisterActivity.this.checkEmail(null, null, null);
            }
        });
        this.sendEmailButton.setOnClickListener(new NetworkCheckOnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.EmailRegisterActivity.3
            @Override // com.alibaba.sdk.android.openaccount.ui.widget.NetworkCheckOnClickListener
            public void afterCheck(View view2) {
                EmailRegisterActivity emailRegisterActivity = EmailRegisterActivity.this;
                emailRegisterActivity.new CheckEmailTask(emailRegisterActivity).execute(new Void[0]);
            }
        });
        useCustomAttrs(this, this.attrs);
    }

    protected void checkEmail(String str, String str2, String str3) {
        new SendEmailTask(this, str, str2, str3).execute(new Void[0]);
    }

    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    protected EmailRegisterCallback getEmailRegisterCallback() {
        return OpenAccountUIServiceImpl._emailRegisterCallback;
    }

    class SendEmailTask extends TaskWithToastMessage<SendEmailResult> {
        private String mCSessionId;
        private String mNocToken;
        private String mSig;

        public SendEmailTask(Activity activity2, String str, String str2, String str3) {
            super(activity2);
            this.mCSessionId = str;
            this.mNocToken = str2;
            this.mSig = str3;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        public SendEmailResult parseData(JSONObject jSONObject) {
            SendEmailResult sendEmailResult = new SendEmailResult();
            sendEmailResult.email = jSONObject.optString("email");
            sendEmailResult.smsCheckTrustToken = jSONObject.optString("smsCheckTrustToken");
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("checkCodeResult");
            if (jSONObjectOptJSONObject != null) {
                sendEmailResult.checkCodeResult = new CheckCodeResult();
                sendEmailResult.checkCodeResult.clientVerifyData = jSONObjectOptJSONObject.optString("clientVerifyData");
                sendEmailResult.checkCodeResult.checkCodeId = jSONObjectOptJSONObject.optString("checkCodeId");
                sendEmailResult.checkCodeResult.checkCodeUrl = jSONObjectOptJSONObject.optString("checkCodeUrl");
            }
            return sendEmailResult;
        }

        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected void doSuccessAfterToast(Result<SendEmailResult> result) {
            if (result.data != null) {
                EmailRegisterActivity.this.emailUrl = result.data.email;
            }
            EmailRegisterActivity.this.emailCodeInputBox.startTimer(this.context);
            EmailRegisterActivity.this.emailCodeInputBox.requestFocus();
        }

        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected void doFailAfterToast(Result<SendEmailResult> result) {
            if (result.code == 26053) {
                EmailRegisterActivity.this.smsCheckTrustToken = result.data.smsCheckTrustToken;
                if (result.data.checkCodeResult != null) {
                    Uri.Builder builderBuildUpon = Uri.parse(result.data.checkCodeResult.clientVerifyData).buildUpon();
                    builderBuildUpon.appendQueryParameter("callback", "https://www.alipay.com/webviewbridge");
                    Intent intent = new Intent(EmailRegisterActivity.this, (Class<?>) LoginDoubleCheckWebActivity.class);
                    intent.putExtra("url", builderBuildUpon.toString());
                    intent.putExtra("title", result.message);
                    intent.putExtra("callback", "https://www.alipay.com/webviewbridge");
                    EmailRegisterActivity.this.startActivityForResult(intent, RequestCode.NO_CAPTCHA_REQUEST_CODE);
                    return;
                }
                return;
            }
            if (result != null && !TextUtils.isEmpty(result.message)) {
                ToastUtils.toast(EmailRegisterActivity.this, result.message, result.code);
            } else {
                ToastUtils.toastSystemError(EmailRegisterActivity.this);
            }
        }

        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected boolean toastMessageRequired(Result<SendEmailResult> result) {
            return result.code != 26053;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        public Result<SendEmailResult> asyncExecute(Void... voidArr) {
            HashMap map = new HashMap();
            map.put("email", EmailRegisterActivity.this.mailInputBox.getEditText().getText());
            map.put("actionType", "sdk_email_activation");
            if (!TextUtils.isEmpty(this.mSig)) {
                map.put("sig", this.mSig);
            }
            if (!TextUtils.isEmpty(this.mCSessionId)) {
                map.put("csessionid", this.mCSessionId);
            }
            if (!TextUtils.isEmpty(this.mNocToken)) {
                map.put("nctoken", this.mNocToken);
            }
            EmailRegisterActivity.this.smsCheckTrustToken = "";
            return parseJsonResult(RpcUtils.invokeWithRiskControlInfo("sendEmailTokenRequest", map, "emailsendtoken"));
        }
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
            map.put("email", EmailRegisterActivity.this.mailInputBox.getEditText().getText());
            map.put("actionType", "sdk_email_activation");
            map.put("emailToken", EmailRegisterActivity.this.emailCodeInputBox.getInputBoxWithClear().getEditTextContent());
            try {
                String rsaPubkey = RSAKey.getRsaPubkey();
                if (TextUtils.isEmpty(rsaPubkey)) {
                    return null;
                }
                map.put("password", Rsa.encrypt(EmailRegisterActivity.this.passwordInputBox.getInputBoxWithClear().getEditTextContent(), rsaPubkey));
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
                EmailRegisterActivity.this.sessionManagerService.updateSession(sessionDataCreateSessionDataFromLoginSuccessResult);
                if (ConfigManager.getInstance().isSupportOfflineLogin()) {
                    OpenAccountSDK.getSqliteUtil().saveToSqlite(EmailRegisterActivity.this.mailInputBox.getEditText().getText().toString(), EmailRegisterActivity.this.passwordInputBox.getInputBoxWithClear().getEditTextContent());
                }
                LoginCallback loginCallback = EmailRegisterActivity.this.getLoginCallback();
                if (loginCallback != null) {
                    loginCallback.onSuccess(EmailRegisterActivity.this.sessionManagerService.getSession());
                }
                EmailRegisterActivity.this.setResult(-1);
                EmailRegisterActivity.this.finishWithoutCancel();
                return;
            }
            if (TextUtils.isEmpty(result.message)) {
                ToastUtils.toastSystemError(this.context);
            } else {
                ToastUtils.toast(this.context, result.message, result.code);
            }
        }
    }

    public LoginCallback getLoginCallback() {
        return OpenAccountUIServiceImpl._emailRegisterCallback;
    }

    protected void finishWithoutCancel() {
        super.finish();
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == RequestCode.NO_CAPTCHA_REQUEST_CODE) {
            if (i2 == -1) {
                if (intent == null || !"nocaptcha".equals(intent.getStringExtra(NativeProtocol.WEB_DIALOG_ACTION))) {
                    return;
                }
                checkEmail(intent.getStringExtra("cSessionId"), intent.getStringExtra("nocToken"), intent.getStringExtra("sig"));
                return;
            }
            this.smsCheckTrustToken = "";
        }
    }

    protected void turnPasswordLevelHint(boolean z) {
        this.mPasswordLevelView.setVisibility(z ? 0 : 8);
    }
}
