package com.alibaba.sdk.android.openaccount.ui.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.model.LoginSuccessResult;
import com.alibaba.sdk.android.openaccount.model.Result;
import com.alibaba.sdk.android.openaccount.model.SessionData;
import com.alibaba.sdk.android.openaccount.session.SessionManagerService;
import com.alibaba.sdk.android.openaccount.ui.R;
import com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage;
import com.alibaba.sdk.android.openaccount.ui.widget.NetworkCheckOnClickListener;
import com.alibaba.sdk.android.openaccount.util.OpenAccountUtils;
import com.alibaba.sdk.android.openaccount.util.RpcUtils;
import com.alibaba.sdk.android.openaccount.util.safe.RSAKey;
import com.alibaba.sdk.android.openaccount.util.safe.Rsa;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class ResetPwdFragment extends BaseFragment {
    protected EditText mConfirmPwdInputBox;
    protected Button mNextButton;
    protected EditText mPwdInputBox;
    protected int mScene = 0;
    protected String mToken;

    protected int getScenario() {
        return 3;
    }

    protected Map<String, Object> getUpdatedOpenAccountProfile() {
        return null;
    }

    protected void onPwdInvalid() {
    }

    protected void onPwdNotEqual() {
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(getLayout(), viewGroup, false);
        this.mPwdInputBox = (EditText) viewInflate.findViewById(R.id.ali_user_pwd_input_box);
        this.mConfirmPwdInputBox = (EditText) viewInflate.findViewById(R.id.ali_user_pwd_confirm_input_box);
        this.mNextButton = (Button) viewInflate.findViewById(R.id.ali_user_reset_pwd_next);
        initParams();
        afterViews();
        return viewInflate;
    }

    protected void initParams() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            try {
                this.mScene = arguments.getInt("scene");
                this.mToken = arguments.getString("token");
            } catch (Exception unused) {
            }
        }
        if (this.mScene == 1) {
            EditText editText = this.mPwdInputBox;
            if (editText != null) {
                editText.setHint(getResources().getString(R.string.ali_sdk_openaccount_text_new_pwd));
            }
            if (this.mAttachedActivity == null || this.mAttachedActivity.getSupportActionBar() == null) {
                return;
            }
            this.mAttachedActivity.getSupportActionBar().setTitle(getResources().getString(R.string.ali_sdk_openaccount_text_reset_password));
            return;
        }
        EditText editText2 = this.mPwdInputBox;
        if (editText2 != null) {
            editText2.setHint(getResources().getString(R.string.ali_sdk_openaccount_text_set_pwd));
        }
        if (this.mAttachedActivity != null && this.mAttachedActivity.getSupportActionBar() != null) {
            this.mAttachedActivity.getSupportActionBar().setTitle(getResources().getString(R.string.ali_sdk_openaccount_text_set_password));
        }
        int i = this.mScene;
    }

    protected void afterViews() {
        Button button = this.mNextButton;
        if (button != null) {
            button.setOnClickListener(new NetworkCheckOnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.fragment.ResetPwdFragment.1
                @Override // com.alibaba.sdk.android.openaccount.ui.widget.NetworkCheckOnClickListener
                public void afterCheck(View view2) {
                    ResetPwdFragment.this.goNext();
                }
            });
        }
    }

    protected void goNext() {
        if (!TextUtils.isEmpty(getPwdConfirm()) && !TextUtils.isEmpty(getPwd())) {
            if (getPwd().equals(getPwdConfirm())) {
                int i = this.mScene;
                if (i == 1 || i == 0) {
                    new FillPasswordTask(this.mAttachedActivity).execute(new Void[0]);
                    return;
                } else {
                    if (i == 2) {
                        new RegisterTask(this.mAttachedActivity).execute(new Void[0]);
                        return;
                    }
                    return;
                }
            }
            onPwdNotEqual();
            return;
        }
        onPwdInvalid();
    }

    protected String getPwdConfirm() {
        EditText editText = this.mConfirmPwdInputBox;
        return editText != null ? editText.getText().toString() : "";
    }

    protected String getPwd() {
        EditText editText = this.mPwdInputBox;
        return editText != null ? editText.getText().toString() : "";
    }

    protected int getLayout() {
        return R.layout.ali_sdk_openaccount_fragment_resetpwd;
    }

    protected class FillPasswordTask extends TaskWithToastMessage<LoginSuccessResult> {
        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected void doFailAfterToast(Result<LoginSuccessResult> result) {
        }

        public FillPasswordTask(Activity activity2) {
            super(activity2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        public Result<LoginSuccessResult> asyncExecute(Void... voidArr) {
            HashMap map = new HashMap();
            map.put("token", ResetPwdFragment.this.mToken);
            try {
                String rsaPubkey = RSAKey.getRsaPubkey();
                if (TextUtils.isEmpty(rsaPubkey)) {
                    return null;
                }
                map.put("password", Rsa.encrypt(ResetPwdFragment.this.getPwd(), rsaPubkey));
                map.put("updatedProfile", ResetPwdFragment.this.getUpdatedOpenAccountProfile());
                return parseJsonResult(RpcUtils.invokeWithRiskControlInfo("resetPasswordRequest", map, "resetpassword"));
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
                sessionDataCreateSessionDataFromLoginSuccessResult.scenario = Integer.valueOf(ResetPwdFragment.this.getScenario());
            }
            ((SessionManagerService) OpenAccountSDK.getService(SessionManagerService.class)).updateSession(sessionDataCreateSessionDataFromLoginSuccessResult);
            ResetPwdFragment.this.successCallback();
        }
    }

    protected class RegisterTask extends TaskWithToastMessage<LoginSuccessResult> {
        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected void doFailAfterToast(Result<LoginSuccessResult> result) {
        }

        public RegisterTask(Activity activity2) {
            super(activity2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        public Result<LoginSuccessResult> asyncExecute(Void... voidArr) {
            HashMap map = new HashMap();
            map.put("token", ResetPwdFragment.this.mToken);
            try {
                String rsaPubkey = RSAKey.getRsaPubkey();
                if (TextUtils.isEmpty(rsaPubkey)) {
                    return null;
                }
                map.put("password", Rsa.encrypt(ResetPwdFragment.this.getPwd(), rsaPubkey));
                map.put("updatedProfile", ResetPwdFragment.this.getUpdatedOpenAccountProfile());
                return parseJsonResult(RpcUtils.invokeWithRiskControlInfo("registerRequest", map, "register"));
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
                sessionDataCreateSessionDataFromLoginSuccessResult.scenario = Integer.valueOf(ResetPwdFragment.this.getScenario());
            }
            ((SessionManagerService) OpenAccountSDK.getService(SessionManagerService.class)).updateSession(sessionDataCreateSessionDataFromLoginSuccessResult);
            ResetPwdFragment.this.successCallback();
        }
    }
}
