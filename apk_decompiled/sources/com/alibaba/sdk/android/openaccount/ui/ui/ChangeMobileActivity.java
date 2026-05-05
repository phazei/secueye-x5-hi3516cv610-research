package com.alibaba.sdk.android.openaccount.ui.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.message.MessageUtils;
import com.alibaba.sdk.android.openaccount.model.Result;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConfigs;
import com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountUIServiceImpl;
import com.alibaba.sdk.android.openaccount.ui.message.UIMessageConstants;
import com.alibaba.sdk.android.openaccount.ui.model.CheckSmsCodeForRegisterResult;
import com.alibaba.sdk.android.openaccount.ui.model.SendSmsCodeForChangeMobileResult;
import com.alibaba.sdk.android.openaccount.ui.model.SmsActionType;
import com.alibaba.sdk.android.openaccount.ui.model.TrustTokenType;
import com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage;
import com.alibaba.sdk.android.openaccount.ui.widget.NetworkCheckOnClickListener;
import com.alibaba.sdk.android.openaccount.util.OpenAccountUtils;
import com.alibaba.sdk.android.openaccount.util.RpcUtils;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.taobao.accs.utl.UtilityImpl;
import java.util.HashMap;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class ChangeMobileActivity extends SendSmsCodeActivity {
    public static String TAG = "oa.ChangeMobile";
    protected String clientVerifyData;

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.ActivityTemplate
    protected String getLayoutName() {
        return "ali_sdk_openaccount_change_mobile";
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.SendSmsCodeActivity, com.alibaba.sdk.android.openaccount.ui.ui.NextStepActivityTemplate, com.alibaba.sdk.android.openaccount.ui.ui.ActivityTemplate, com.alibaba.sdk.android.openaccount.ui.ui.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.clientVerifyData = (String) bundle.get("clientVerifyData");
        }
        this.next.setOnClickListener(new NetworkCheckOnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.ChangeMobileActivity.1
            @Override // com.alibaba.sdk.android.openaccount.ui.widget.NetworkCheckOnClickListener
            public void afterCheck(View view2) {
                ChangeMobileActivity changeMobileActivity = ChangeMobileActivity.this;
                changeMobileActivity.new CheckSmsCodeForChangeMobileTask(changeMobileActivity, "").execute(new Void[0]);
            }
        });
        this.smsCodeInputBox.addSendClickListener(new NetworkCheckOnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.ChangeMobileActivity.2
            @Override // com.alibaba.sdk.android.openaccount.ui.widget.NetworkCheckOnClickListener
            public void afterCheck(View view2) {
                ChangeMobileActivity changeMobileActivity = ChangeMobileActivity.this;
                changeMobileActivity.new SendSmsCodeForChangeMobileTask(changeMobileActivity).execute(new Void[0]);
            }
        });
        this.mobileInputBox.setSupportForeignMobile(this, OpenAccountUIConfigs.ChangeMobileFlow.mobileCountrySelectorActvityClazz, OpenAccountUIConfigs.ChangeMobileFlow.supportForeignMobileNumbers);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.SendSmsCodeActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("clientVerifyData", this.clientVerifyData);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.SendSmsCodeActivity
    protected LoginCallback getLoginCallback() {
        return OpenAccountUIServiceImpl._changeMobileCallback;
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.SendSmsCodeActivity
    protected void onUserCancel() {
        LoginCallback loginCallback = getLoginCallback();
        if (loginCallback != null) {
            loginCallback.onFailure(UIMessageConstants.OPEN_ACCOUNT_CHANGE_MOBILE_CANCEL, MessageUtils.getMessageContent(UIMessageConstants.OPEN_ACCOUNT_CHANGE_MOBILE_CANCEL, new Object[0]));
        }
    }

    protected class SendSmsCodeForChangeMobileTask extends TaskWithToastMessage<SendSmsCodeForChangeMobileResult> {
        public SendSmsCodeForChangeMobileTask(Activity activity2) {
            super(activity2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        public Result<SendSmsCodeForChangeMobileResult> asyncExecute(Void... voidArr) {
            ChangeMobileActivity.this.clientVerifyData = null;
            HashMap map = new HashMap();
            map.put(UtilityImpl.NET_TYPE_MOBILE, ChangeMobileActivity.this.mobileInputBox.getEditTextContent());
            map.put(AlinkConstants.KEY_MOBILE_LOCATION_CODE, ChangeMobileActivity.this.mobileInputBox.getMobileLocationCode());
            map.put("smsActionType", SmsActionType.SDK_MOBILE_UPDATE);
            return parseJsonResult(RpcUtils.invokeWithRiskControlInfo("sendSmsCodeRequest", map, "sendsmscode"));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        public SendSmsCodeForChangeMobileResult parseData(JSONObject jSONObject) {
            SendSmsCodeForChangeMobileResult sendSmsCodeForChangeMobileResult = new SendSmsCodeForChangeMobileResult();
            sendSmsCodeForChangeMobileResult.checkCodeId = jSONObject.optString("checkCodeId");
            sendSmsCodeForChangeMobileResult.checkCodeUrl = jSONObject.optString("checkCodeUrl");
            sendSmsCodeForChangeMobileResult.clientVerifyData = jSONObject.optString("clientVerifyData");
            return sendSmsCodeForChangeMobileResult;
        }

        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected void doSuccessAfterToast(Result<SendSmsCodeForChangeMobileResult> result) {
            ChangeMobileActivity.this.smsCodeInputBox.startTimer(ChangeMobileActivity.this);
            ChangeMobileActivity.this.smsCodeInputBox.requestFocus();
        }

        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected void doFailAfterToast(Result<SendSmsCodeForChangeMobileResult> result) {
            if (result.code == 26053) {
                ChangeMobileActivity.this.clientVerifyData = result.data.clientVerifyData;
                ChangeMobileActivity.this.smsCodeInputBox.startTimer(ChangeMobileActivity.this);
            }
        }

        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected boolean toastMessageRequired(Result<SendSmsCodeForChangeMobileResult> result) {
            return result.code != 26053;
        }
    }

    protected class CheckSmsCodeForChangeMobileTask extends TaskWithToastMessage<CheckSmsCodeForRegisterResult> {
        private String serverVerifyData;

        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected void doFailAfterToast(Result<CheckSmsCodeForRegisterResult> result) {
        }

        public CheckSmsCodeForChangeMobileTask(Activity activity2, String str) {
            super(activity2);
            this.serverVerifyData = str;
        }

        public CheckSmsCodeForChangeMobileTask(Activity activity2) {
            super(activity2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        public Result<CheckSmsCodeForRegisterResult> asyncExecute(Void... voidArr) {
            HashMap map = new HashMap();
            map.put(AlinkConstants.KEY_MOBILE_LOCATION_CODE, ChangeMobileActivity.this.mobileInputBox.getMobileLocationCode());
            map.put(UtilityImpl.NET_TYPE_MOBILE, ChangeMobileActivity.this.mobileInputBox.getEditTextContent());
            map.put("smsCode", ChangeMobileActivity.this.smsCodeInputBox.getInputBoxWithClear().getEditTextContent());
            map.put("trustTokenType", TrustTokenType.UPDATE_MOBILE);
            return parseJsonResult(RpcUtils.invokeWithRiskControlInfo("checkSmsCodeRequest", map, "checksmscode"));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        public CheckSmsCodeForRegisterResult parseData(JSONObject jSONObject) {
            CheckSmsCodeForRegisterResult checkSmsCodeForRegisterResult = new CheckSmsCodeForRegisterResult();
            checkSmsCodeForRegisterResult.token = jSONObject.optString("token");
            checkSmsCodeForRegisterResult.clientVerifyData = jSONObject.optString("clientVerifyData");
            return checkSmsCodeForRegisterResult;
        }

        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected void doSuccessAfterToast(Result<CheckSmsCodeForRegisterResult> result) {
            if (result == null || !result.isSuccess() || result.data == null || TextUtils.isEmpty(result.data.token)) {
                return;
            }
            ChangeMobileActivity changeMobileActivity = ChangeMobileActivity.this;
            changeMobileActivity.new ChangeMobileTask(changeMobileActivity, result.data.token).execute(new Void[0]);
        }
    }

    protected class ChangeMobileTask extends TaskWithToastMessage<JSONObject> {
        private String token;

        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage, com.alibaba.sdk.android.openaccount.task.TaskWithDialog
        protected boolean needProgressDialog() {
            return false;
        }

        public ChangeMobileTask(Activity activity2, String str) {
            super(activity2);
            this.token = str;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        public Result<JSONObject> asyncExecute(Void... voidArr) {
            HashMap map = new HashMap();
            map.put("trustToken", this.token);
            return parseJsonResult(RpcUtils.invokeWithRiskControlInfo("updateMobileRequest", map, "updatemobileandkicksession"));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        public JSONObject parseData(JSONObject jSONObject) {
            return jSONObject.optJSONObject("openAccount");
        }

        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected void doSuccessAfterToast(Result<JSONObject> result) {
            ChangeMobileActivity.this.sessionManagerService.updateUser(OpenAccountUtils.parseUserFromJSONObject(result.data));
            ChangeMobileActivity.this.finishWithoutCallback();
            LoginCallback loginCallback = ChangeMobileActivity.this.getLoginCallback();
            if (loginCallback != null) {
                loginCallback.onSuccess(ChangeMobileActivity.this.sessionManagerService.getSession());
            }
        }

        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected void doFailAfterToast(Result<JSONObject> result) {
            if (ChangeMobileActivity.this.getLoginCallback() != null) {
                ChangeMobileActivity.this.getLoginCallback().onFailure(result.code, result.message);
            }
        }
    }
}
