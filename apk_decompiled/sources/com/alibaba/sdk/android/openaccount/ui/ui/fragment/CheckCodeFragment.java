package com.alibaba.sdk.android.openaccount.ui.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import anetwork.channel.util.RequestConstant;
import com.alibaba.sdk.android.openaccount.model.CheckCodeResult;
import com.alibaba.sdk.android.openaccount.model.Result;
import com.alibaba.sdk.android.openaccount.ui.R;
import com.alibaba.sdk.android.openaccount.ui.RequestCode;
import com.alibaba.sdk.android.openaccount.ui.model.CheckRegisterAccountResult;
import com.alibaba.sdk.android.openaccount.ui.model.CheckSmsCodeForResetPasswordResult;
import com.alibaba.sdk.android.openaccount.ui.model.SendSmsCodeForChangeMobileResult;
import com.alibaba.sdk.android.openaccount.ui.model.SmsActionType;
import com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage;
import com.alibaba.sdk.android.openaccount.ui.ui.LoginDoubleCheckWebActivity;
import com.alibaba.sdk.android.openaccount.ui.ui.SpecialLoginActivity;
import com.alibaba.sdk.android.openaccount.ui.ui.fragment.BaseFragment;
import com.alibaba.sdk.android.openaccount.ui.util.ToastUtils;
import com.alibaba.sdk.android.openaccount.ui.widget.NetworkCheckOnClickListener;
import com.alibaba.sdk.android.openaccount.util.JSONUtils;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;
import com.alibaba.sdk.android.openaccount.util.RpcUtils;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.facebook.internal.NativeProtocol;
import com.facebook.share.internal.ShareConstants;
import com.taobao.accs.utl.UtilityImpl;
import java.util.HashMap;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class CheckCodeFragment extends BaseFragment {
    public static final String TAG = "oa.CheckCodeFragment";
    protected String mAccountExist;
    protected String mMobile;
    protected Button mNextButton;
    protected TextView mPhoneTextView;
    protected Button mResendCheckCodeButton;
    protected int mScene = 0;
    protected String smsCheckTrustToken;

    protected String getCheckCode() {
        return "888888";
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.fragment.BaseFragment
    protected String getLocationCode() {
        return "86";
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        if (this.mAttachedActivity != null && this.mAttachedActivity.getSupportActionBar() != null) {
            this.mAttachedActivity.getSupportActionBar().setTitle(R.string.ali_sdk_openaccount_text_enter_check_code);
        }
        View viewInflate = layoutInflater.inflate(getLayout(), viewGroup, false);
        this.mPhoneTextView = (TextView) viewInflate.findViewById(R.id.ali_user_phone_textview);
        this.mNextButton = (Button) viewInflate.findViewById(R.id.ali_user_checkcode_next);
        this.mResendCheckCodeButton = (Button) viewInflate.findViewById(R.id.ali_user_resend_checkcode);
        initParams();
        afterViews();
        return viewInflate;
    }

    protected int getLayout() {
        return R.layout.ali_sdk_openaccount_fragment_checkcode;
    }

    private void initParams() {
        try {
            String str = (String) getArguments().get(UtilityImpl.NET_TYPE_MOBILE);
            this.mMobile = str;
            if (!TextUtils.isEmpty(str) && this.mPhoneTextView != null) {
                this.mPhoneTextView.setText(str);
            }
            this.mScene = getArguments().getInt("scene");
            this.mAccountExist = getArguments().getString("accountExist");
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void afterViews() {
        Button button = this.mNextButton;
        if (button != null) {
            button.setOnClickListener(new NetworkCheckOnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.fragment.CheckCodeFragment.1
                @Override // com.alibaba.sdk.android.openaccount.ui.widget.NetworkCheckOnClickListener
                public void afterCheck(View view2) {
                    CheckCodeFragment.this.goNext();
                }
            });
        }
        Button button2 = this.mResendCheckCodeButton;
        if (button2 != null) {
            button2.setOnClickListener(new View.OnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.fragment.CheckCodeFragment.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    CheckCodeFragment.this.sendSmsCode();
                    CheckCodeFragment.this.countDownTime();
                }
            });
        }
        countDownTime();
    }

    protected void sendSmsCode() {
        if (this.mScene == 2 || TextUtils.equals(this.mAccountExist, RequestConstant.FALSE)) {
            new SendSmsCodeForRegisterTask(this.mAttachedActivity).execute(new Void[0]);
        } else {
            new BaseFragment.SendSMSForResetPwdTask(this.mAttachedActivity).execute(new Void[0]);
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.fragment.BaseFragment
    protected String getMobile() {
        return this.mMobile;
    }

    /* JADX WARN: Type inference failed for: r6v0, types: [com.alibaba.sdk.android.openaccount.ui.ui.fragment.CheckCodeFragment$3] */
    protected void countDownTime() {
        new CountDownTimer(60000L, 1000L) { // from class: com.alibaba.sdk.android.openaccount.ui.ui.fragment.CheckCodeFragment.3
            @Override // android.os.CountDownTimer
            public void onTick(long j) {
                int i = (int) (j / 1000);
                if (CheckCodeFragment.this.mResendCheckCodeButton != null) {
                    CheckCodeFragment.this.mResendCheckCodeButton.setEnabled(false);
                    CheckCodeFragment.this.mResendCheckCodeButton.setText(String.format(ResourceUtils.getString(CheckCodeFragment.this.getContext(), "ali_sdk_openaccount_text_count_down"), Integer.valueOf(i)));
                }
            }

            @Override // android.os.CountDownTimer
            public void onFinish() {
                if (CheckCodeFragment.this.mResendCheckCodeButton != null) {
                    CheckCodeFragment.this.mResendCheckCodeButton.setEnabled(true);
                    CheckCodeFragment.this.mResendCheckCodeButton.setText(ResourceUtils.getString(CheckCodeFragment.this.getContext(), "ali_sdk_openaccount_text_send_sms_code"));
                }
            }
        }.start();
    }

    protected void goNext() {
        if (this.mScene == 2 || TextUtils.equals(this.mAccountExist, RequestConstant.FALSE)) {
            new CheckCodeTask(this.mAttachedActivity).execute(new Void[0]);
            return;
        }
        int i = this.mScene;
        if (i == 0 || i == 1) {
            new CheckSmsCodeForResetPasswordTask(this.mAttachedActivity, null, null, null).execute(new Void[0]);
        }
    }

    protected class SendSmsCodeForRegisterTask extends TaskWithToastMessage<SendSmsCodeForChangeMobileResult> {
        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected void doSuccessAfterToast(Result<SendSmsCodeForChangeMobileResult> result) {
        }

        public SendSmsCodeForRegisterTask(Activity activity2) {
            super(activity2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        public Result<SendSmsCodeForChangeMobileResult> asyncExecute(Void... voidArr) {
            HashMap map = new HashMap();
            map.put(UtilityImpl.NET_TYPE_MOBILE, CheckCodeFragment.this.getMobile());
            map.put(AlinkConstants.KEY_MOBILE_LOCATION_CODE, CheckCodeFragment.this.getLocationCode());
            map.put("smsActionType", SmsActionType.SDK_ACCOUNT_REGISTER);
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
        protected void doFailAfterToast(Result<SendSmsCodeForChangeMobileResult> result) {
            int i = result.code;
        }

        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected boolean toastMessageRequired(Result<SendSmsCodeForChangeMobileResult> result) {
            return result.code != 26053;
        }
    }

    protected class CheckCodeTask extends TaskWithToastMessage<CheckRegisterAccountResult> {
        public CheckCodeTask(Activity activity2) {
            super(activity2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        public Result<CheckRegisterAccountResult> asyncExecute(Void... voidArr) {
            HashMap map = new HashMap();
            map.put("smsCode", CheckCodeFragment.this.getCheckCode());
            map.put(UtilityImpl.NET_TYPE_MOBILE, CheckCodeFragment.this.getMobile());
            map.put("trustTokenType", "openId_server_register");
            return parseJsonResult(RpcUtils.invokeWithRiskControlInfo("checksmscoderequest", map, "checkregisteraccount"));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        public CheckRegisterAccountResult parseData(JSONObject jSONObject) {
            CheckRegisterAccountResult checkRegisterAccountResult = new CheckRegisterAccountResult();
            checkRegisterAccountResult.accountExist = jSONObject.optString("accountExist");
            checkRegisterAccountResult.accountHasPassword = jSONObject.optString("accountHasPassword");
            checkRegisterAccountResult.token = jSONObject.optString("token");
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("checkCodeResult");
            if (jSONObjectOptJSONObject != null) {
                CheckCodeResult checkCodeResult = new CheckCodeResult();
                checkRegisterAccountResult.checkCodeResult = checkCodeResult;
                checkCodeResult.checkCodeId = JSONUtils.optString(jSONObjectOptJSONObject, "checkCodeId");
                checkCodeResult.checkCodeUrl = JSONUtils.optString(jSONObjectOptJSONObject, "checkCodeUrl");
                checkCodeResult.clientVerifyData = JSONUtils.optString(jSONObjectOptJSONObject, "clientVerifyData");
            }
            return checkRegisterAccountResult;
        }

        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected void doSuccessAfterToast(Result<CheckRegisterAccountResult> result) {
            final CheckRegisterAccountResult checkRegisterAccountResult;
            if (result == null) {
                ToastUtils.toastSystemError(this.context);
                return;
            }
            if (result.code == 1 && (checkRegisterAccountResult = result.data) != null) {
                if (TextUtils.equals(checkRegisterAccountResult.accountExist, RequestConstant.FALSE)) {
                    if (CheckCodeFragment.this.mScene == 0) {
                        CheckCodeFragment checkCodeFragment = CheckCodeFragment.this;
                        checkCodeFragment.jumpToRegisterConfirm(checkRegisterAccountResult, checkCodeFragment.mScene);
                        return;
                    } else {
                        if (CheckCodeFragment.this.mScene == 2) {
                            CheckCodeFragment.this.beforeJumpToPwd(new BaseFragment.BeforeJumpToPwdCallback() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.fragment.CheckCodeFragment.CheckCodeTask.1
                                @Override // com.alibaba.sdk.android.openaccount.ui.ui.fragment.BaseFragment.BeforeJumpToPwdCallback
                                public void onFinish() {
                                    if (CheckCodeFragment.this.mAttachedActivity == null || !(CheckCodeFragment.this.mAttachedActivity instanceof SpecialLoginActivity) || CheckCodeFragment.this.mAttachedActivity.isFinishing()) {
                                        return;
                                    }
                                    Bundle bundle = new Bundle();
                                    bundle.putString("token", checkRegisterAccountResult.token);
                                    bundle.putInt("scene", CheckCodeFragment.this.mScene);
                                    ((SpecialLoginActivity) CheckCodeFragment.this.mAttachedActivity).goFindPwd(bundle);
                                }
                            });
                            return;
                        }
                        return;
                    }
                }
                if (CheckCodeFragment.this.mScene == 2) {
                    CheckCodeFragment.this.jumpToRegisterConfirm(checkRegisterAccountResult, 1);
                }
            }
        }

        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected void doFailAfterToast(Result<CheckRegisterAccountResult> result) {
            CheckCodeFragment.this.handleFailCase(result);
        }

        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected boolean toastMessageRequired(Result<CheckRegisterAccountResult> result) {
            return CheckCodeFragment.this.needToast(result);
        }
    }

    protected void jumpToRegisterConfirm(CheckRegisterAccountResult checkRegisterAccountResult, int i) {
        Bundle bundle = new Bundle();
        bundle.putString(UtilityImpl.NET_TYPE_MOBILE, this.mMobile);
        bundle.putString("trustToken", checkRegisterAccountResult.token);
        bundle.putInt("scene", i);
        bundle.putString("accountExist", checkRegisterAccountResult.accountExist);
        bundle.putString("accountHasPassword", checkRegisterAccountResult.accountHasPassword);
        ((SpecialLoginActivity) this.mAttachedActivity).jumpToRegisterConfirm(bundle);
    }

    protected void beforeJumpToPwd(BaseFragment.BeforeJumpToPwdCallback beforeJumpToPwdCallback) {
        if (beforeJumpToPwdCallback != null) {
            beforeJumpToPwdCallback.onFinish();
        }
    }

    protected boolean needToast(Result<CheckRegisterAccountResult> result) {
        if (result != null) {
            return (result.code == 20152 || result.code == 4020) ? false : true;
        }
        return true;
    }

    protected void handleFailCase(Result<CheckRegisterAccountResult> result) {
        if (result != null) {
            if (result.code == 20152 && result.data != null && !TextUtils.isEmpty(result.data.smsCheckTrustToken)) {
                if (this.mAttachedActivity == null || !(this.mAttachedActivity instanceof SpecialLoginActivity)) {
                    return;
                }
                jumpToRegisterConfirm(result.data, this.mScene);
                return;
            }
            if (result.code == 4020) {
                processErrorCode();
            } else if (result.code == 4021) {
                processErrorCode();
            }
        }
    }

    protected void processErrorCode() {
        Log.e(TAG, "codeError");
    }

    protected class CheckSmsCodeForResetPasswordTask extends TaskWithToastMessage<CheckSmsCodeForResetPasswordResult> {
        private String cSessionId;
        private String nocToken;
        private String sig;

        public CheckSmsCodeForResetPasswordTask(Activity activity2, String str, String str2, String str3) {
            super(activity2);
            this.cSessionId = str;
            this.nocToken = str2;
            this.sig = str3;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        public Result<CheckSmsCodeForResetPasswordResult> asyncExecute(Void... voidArr) {
            HashMap map = new HashMap();
            HashMap map2 = new HashMap();
            map.put("checkCodeRequest", map2);
            HashMap map3 = new HashMap();
            if (!TextUtils.isEmpty(this.sig)) {
                map2.put("sig", this.sig);
            }
            if (!TextUtils.isEmpty(this.nocToken)) {
                map2.put("nctoken", this.nocToken);
                map3.put("smsToken", CheckCodeFragment.this.smsCheckTrustToken);
            }
            CheckCodeFragment.this.smsCheckTrustToken = "";
            if (!TextUtils.isEmpty(this.cSessionId)) {
                map2.put("csessionid", this.cSessionId);
            }
            map3.put("userId", CheckCodeFragment.this.mMobile);
            map3.put("version", 1);
            map3.put("smsCode", CheckCodeFragment.this.getCheckCode());
            map.put("checkSmsCodeRequest", map3);
            return parseJsonResult(RpcUtils.invokeWithRiskControlInfo(ShareConstants.WEB_DIALOG_RESULT_PARAM_REQUEST_ID, map, "checksmscodeforresetpassword"));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        public CheckSmsCodeForResetPasswordResult parseData(JSONObject jSONObject) {
            CheckSmsCodeForResetPasswordResult checkSmsCodeForResetPasswordResult = new CheckSmsCodeForResetPasswordResult();
            checkSmsCodeForResetPasswordResult.token = jSONObject.optString("token");
            checkSmsCodeForResetPasswordResult.clientVerifyData = jSONObject.optString("clientVerifyData");
            checkSmsCodeForResetPasswordResult.smsCheckTrustToken = jSONObject.optString("smsCheckTrustToken");
            return checkSmsCodeForResetPasswordResult;
        }

        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected void doSuccessAfterToast(Result<CheckSmsCodeForResetPasswordResult> result) {
            if (result == null || result.data == null || CheckCodeFragment.this.mAttachedActivity == null || !(CheckCodeFragment.this.mAttachedActivity instanceof SpecialLoginActivity) || CheckCodeFragment.this.mAttachedActivity.isFinishing()) {
                return;
            }
            final String str = result.data.token;
            CheckCodeFragment.this.beforeJumpToPwd(new BaseFragment.BeforeJumpToPwdCallback() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.fragment.CheckCodeFragment.CheckSmsCodeForResetPasswordTask.1
                @Override // com.alibaba.sdk.android.openaccount.ui.ui.fragment.BaseFragment.BeforeJumpToPwdCallback
                public void onFinish() {
                    if (CheckCodeFragment.this.mAttachedActivity == null || !(CheckCodeFragment.this.mAttachedActivity instanceof SpecialLoginActivity) || CheckCodeFragment.this.mAttachedActivity.isFinishing()) {
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("token", str);
                    bundle.putInt("scene", CheckCodeFragment.this.mScene);
                    ((SpecialLoginActivity) CheckCodeFragment.this.mAttachedActivity).goFindPwd(bundle);
                }
            });
        }

        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected void doFailAfterToast(Result<CheckSmsCodeForResetPasswordResult> result) {
            if (result.code != 26053 || result.data == null || TextUtils.isEmpty(result.data.clientVerifyData)) {
                return;
            }
            CheckCodeFragment.this.smsCheckTrustToken = result.data.smsCheckTrustToken;
            Uri.Builder builderBuildUpon = Uri.parse(result.data.clientVerifyData).buildUpon();
            builderBuildUpon.appendQueryParameter("callback", "https://www.alipay.com/webviewbridge");
            Intent intent = new Intent(CheckCodeFragment.this.mAttachedActivity, (Class<?>) LoginDoubleCheckWebActivity.class);
            intent.putExtra("url", builderBuildUpon.toString());
            intent.putExtra("title", result.message);
            intent.putExtra("callback", "https://www.alipay.com/webviewbridge");
            CheckCodeFragment.this.startActivityForResult(intent, RequestCode.NO_CAPTCHA_REQUEST_CODE);
        }

        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected boolean toastMessageRequired(Result<CheckSmsCodeForResetPasswordResult> result) {
            return result.code != 26053;
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == RequestCode.NO_CAPTCHA_REQUEST_CODE) {
            if (i2 == -1) {
                if (intent == null || !"nocaptcha".equals(intent.getStringExtra(NativeProtocol.WEB_DIALOG_ACTION))) {
                    return;
                }
                new CheckSmsCodeForResetPasswordTask(this.mAttachedActivity, intent.getStringExtra("cSessionId"), intent.getStringExtra("nocToken"), intent.getStringExtra("sig")).execute(new Void[0]);
                return;
            }
            this.smsCheckTrustToken = "";
        }
    }
}
