package com.alibaba.sdk.android.openaccount.ui.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.model.LoginSuccessResult;
import com.alibaba.sdk.android.openaccount.model.Result;
import com.alibaba.sdk.android.openaccount.model.SessionData;
import com.alibaba.sdk.android.openaccount.session.SessionManagerService;
import com.alibaba.sdk.android.openaccount.ui.R;
import com.alibaba.sdk.android.openaccount.ui.helper.IPassWordCheckListener;
import com.alibaba.sdk.android.openaccount.ui.helper.ITextChangeListner;
import com.alibaba.sdk.android.openaccount.ui.helper.PasswordCheckHelper;
import com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage;
import com.alibaba.sdk.android.openaccount.ui.util.AttributeUtils;
import com.alibaba.sdk.android.openaccount.ui.widget.InputBoxWithClear;
import com.alibaba.sdk.android.openaccount.ui.widget.NetworkCheckOnClickListener;
import com.alibaba.sdk.android.openaccount.ui.widget.NextStepButtonWatcher;
import com.alibaba.sdk.android.openaccount.ui.widget.PasswordInputBox;
import com.alibaba.sdk.android.openaccount.ui.widget.PasswordLevelView;
import com.alibaba.sdk.android.openaccount.util.OpenAccountUtils;
import com.alibaba.sdk.android.openaccount.util.RpcUtils;
import com.alibaba.sdk.android.openaccount.util.safe.RSAKey;
import com.alibaba.sdk.android.openaccount.util.safe.Rsa;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import com.aliyun.iot.ilop.KeyboardHelper;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class FillPasswordActivity extends NextStepActivityTemplate {
    protected InputBoxWithClear inputBoxWithClear;
    protected String loginId;
    protected PasswordCheckHelper passwordCheckHelper;
    protected PasswordInputBox passwordInputBox;
    protected PasswordLevelView passwordLevelView;

    @Autowired
    protected SessionManagerService sessionManagerService;
    protected TextView text;
    protected String token;

    protected abstract LoginCallback getLoginCallback();

    protected abstract String getRequestKey();

    protected abstract int getScenario();

    protected abstract String getTarget();

    protected Map<String, Object> getUpdatedOpenAccountProfile() {
        return null;
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.NextStepActivityTemplate, com.alibaba.sdk.android.openaccount.ui.ui.ActivityTemplate, com.alibaba.sdk.android.openaccount.ui.ui.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.inputBoxWithClear = (InputBoxWithClear) findViewById("input_box");
        this.passwordLevelView = (PasswordLevelView) findViewById("password_level");
        NextStepButtonWatcher nextStepButtonWatcher = getNextStepButtonWatcher();
        nextStepButtonWatcher.addEditText(this.inputBoxWithClear.getEditText());
        if (ConfigManager.getInstance().isShowPasswordStrengthHint()) {
            nextStepButtonWatcher.setPasswordInputBox(this.inputBoxWithClear.getEditText());
            nextStepButtonWatcher.setPassWordChangeListner(new ITextChangeListner() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.FillPasswordActivity.1
                @Override // com.alibaba.sdk.android.openaccount.ui.helper.ITextChangeListner
                public void onTextChange(String str) {
                    if (FillPasswordActivity.this.passwordCheckHelper == null) {
                        FillPasswordActivity.this.passwordCheckHelper = new PasswordCheckHelper(str);
                    } else {
                        FillPasswordActivity.this.passwordCheckHelper.setPassword(str);
                    }
                    FillPasswordActivity.this.passwordCheckHelper.check(new IPassWordCheckListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.FillPasswordActivity.1.1
                        @Override // com.alibaba.sdk.android.openaccount.ui.helper.IPassWordCheckListener
                        public void onCheckPassword(int i) {
                            FillPasswordActivity.this.passwordLevelView.setPasswordLevel(i);
                        }
                    });
                }
            });
        } else {
            this.passwordLevelView.setVisibility(8);
        }
        this.inputBoxWithClear.addTextChangedListener(nextStepButtonWatcher);
        this.passwordInputBox = (PasswordInputBox) findViewById("password_input_box");
        initParams();
        if (this.next != null) {
            this.next.setOnClickListener(new NetworkCheckOnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.FillPasswordActivity.2
                @Override // com.alibaba.sdk.android.openaccount.ui.widget.NetworkCheckOnClickListener
                public void afterCheck(View view2) {
                    FillPasswordActivity.this.resetPassword();
                }
            });
        }
        this.text = (TextView) findViewById("text");
        useCustomAttrs(this, this.attrs);
    }

    protected void initParams() {
        try {
            this.token = getIntent().getStringExtra("token");
            this.loginId = getIntent().getStringExtra("loginId");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void resetPassword() {
        new FillPasswordTask(this).execute(new Void[0]);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.ActivityTemplate
    protected void doUseCustomAttrs(Context context, TypedArray typedArray) {
        super.doUseCustomAttrs(context, typedArray);
        setPasswordHintMinAndMaxLength(ConfigManager.getInstance().getMinPasswordLength(), ConfigManager.getInstance().getMaxPasswordLength());
        this.text.setTextColor(AttributeUtils.getColor(context, typedArray, "ali_sdk_openaccount_attrs_fill_password_text_color"));
    }

    protected class FillPasswordTask extends TaskWithToastMessage<LoginSuccessResult> {

        /* JADX INFO: renamed from: activity, reason: collision with root package name */
        Activity f2927activity;

        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected void doFailAfterToast(Result<LoginSuccessResult> result) {
        }

        public FillPasswordTask(Activity activity2) {
            super(activity2);
            this.f2927activity = activity2;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        public Result<LoginSuccessResult> asyncExecute(Void... voidArr) {
            KeyboardHelper.newInstance().hideSoftInputForHw(this.f2927activity, FillPasswordActivity.this.passwordInputBox.getEditText());
            HashMap map = new HashMap();
            map.put("token", FillPasswordActivity.this.token);
            try {
                String rsaPubkey = RSAKey.getRsaPubkey();
                if (TextUtils.isEmpty(rsaPubkey)) {
                    return null;
                }
                map.put("password", Rsa.encrypt(FillPasswordActivity.this.inputBoxWithClear.getEditTextContent(), rsaPubkey));
                map.put("updatedProfile", FillPasswordActivity.this.getUpdatedOpenAccountProfile());
                return parseJsonResult(RpcUtils.invokeWithRiskControlInfo(FillPasswordActivity.this.getRequestKey(), map, FillPasswordActivity.this.getTarget()));
            } catch (Exception unused) {
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        public LoginSuccessResult parseData(JSONObject jSONObject) {
            return OpenAccountUtils.parseLoginSuccessResult(jSONObject);
        }

        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected void doSuccessAfterToast(Result<LoginSuccessResult> result) {
            SessionData sessionDataCreateSessionDataFromLoginSuccessResult = OpenAccountUtils.createSessionDataFromLoginSuccessResult(result.data);
            if (sessionDataCreateSessionDataFromLoginSuccessResult.scenario == null) {
                sessionDataCreateSessionDataFromLoginSuccessResult.scenario = Integer.valueOf(FillPasswordActivity.this.getScenario());
            }
            FillPasswordActivity.this.sessionManagerService.updateSession(sessionDataCreateSessionDataFromLoginSuccessResult);
            String editTextContent = FillPasswordActivity.this.inputBoxWithClear.getEditTextContent();
            if (!TextUtils.isEmpty(FillPasswordActivity.this.loginId)) {
                OpenAccountSDK.getSqliteUtil().saveToSqlite(FillPasswordActivity.this.loginId, editTextContent);
            }
            FillPasswordActivity.this.setResult(-1);
            FillPasswordActivity.this.finish();
        }
    }

    protected void setPasswordHintMinAndMaxLength(int i, int i2) {
        this.text.setText(getString(R.string.ali_sdk_openaccount_text_reset_password_rule, new Object[]{Integer.valueOf(i), Integer.valueOf(i2)}));
    }
}
