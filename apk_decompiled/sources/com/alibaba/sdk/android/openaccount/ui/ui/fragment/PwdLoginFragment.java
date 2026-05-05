package com.alibaba.sdk.android.openaccount.ui.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.executor.ExecutorService;
import com.alibaba.sdk.android.openaccount.model.LoginResult;
import com.alibaba.sdk.android.openaccount.model.Result;
import com.alibaba.sdk.android.openaccount.model.SessionData;
import com.alibaba.sdk.android.openaccount.session.SessionManagerService;
import com.alibaba.sdk.android.openaccount.ui.R;
import com.alibaba.sdk.android.openaccount.ui.RequestCode;
import com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountUIServiceImpl;
import com.alibaba.sdk.android.openaccount.ui.task.LoginByIVTokenTask;
import com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage;
import com.alibaba.sdk.android.openaccount.ui.ui.LoginDoubleCheckWebActivity;
import com.alibaba.sdk.android.openaccount.ui.ui.LoginIVWebActivity;
import com.alibaba.sdk.android.openaccount.ui.ui.SpecialLoginActivity;
import com.alibaba.sdk.android.openaccount.ui.ui.fragment.BaseFragment;
import com.alibaba.sdk.android.openaccount.ui.widget.NetworkCheckOnClickListener;
import com.alibaba.sdk.android.openaccount.util.OpenAccountUtils;
import com.alibaba.sdk.android.openaccount.util.RpcUtils;
import com.alibaba.sdk.android.openaccount.util.safe.RSAKey;
import com.alibaba.sdk.android.openaccount.util.safe.Rsa;
import com.facebook.internal.NativeProtocol;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.taobao.accs.utl.UtilityImpl;
import java.util.HashMap;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class PwdLoginFragment extends BaseFragment {
    protected TextView mFindPwdTextView;
    protected String mMobile;
    protected Button mNextButton;
    protected EditText mPwdInputBox;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        if (this.mAttachedActivity != null && this.mAttachedActivity.getSupportActionBar() != null) {
            this.mAttachedActivity.getSupportActionBar().setTitle(R.string.ali_sdk_openaccount_text_login);
        }
        View viewInflate = layoutInflater.inflate(getLayout(), viewGroup, false);
        this.mPwdInputBox = (EditText) viewInflate.findViewById(R.id.ali_user_pwd_input_box);
        this.mNextButton = (Button) viewInflate.findViewById(R.id.ali_user_mobile_next);
        this.mFindPwdTextView = (TextView) viewInflate.findViewById(R.id.ali_user_find_pwd);
        initParams();
        afterViews();
        return viewInflate;
    }

    protected void initParams() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            try {
                this.mMobile = arguments.getString(UtilityImpl.NET_TYPE_MOBILE);
            } catch (Exception unused) {
            }
        }
    }

    protected void afterViews() {
        Button button = this.mNextButton;
        if (button != null) {
            button.setOnClickListener(new NetworkCheckOnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.fragment.PwdLoginFragment.1
                @Override // com.alibaba.sdk.android.openaccount.ui.widget.NetworkCheckOnClickListener
                public void afterCheck(View view2) {
                    PwdLoginFragment.this.goNext();
                }
            });
        }
        TextView textView = this.mFindPwdTextView;
        if (textView != null) {
            textView.setOnClickListener(new NetworkCheckOnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.fragment.PwdLoginFragment.2
                @Override // com.alibaba.sdk.android.openaccount.ui.widget.NetworkCheckOnClickListener
                public void afterCheck(View view2) {
                    PwdLoginFragment pwdLoginFragment = PwdLoginFragment.this;
                    new BaseFragment.SendSMSForResetPwdTask(pwdLoginFragment.mAttachedActivity).execute(new Void[0]);
                }
            });
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.fragment.BaseFragment
    protected void onSendSMSForResetPwdSuccess(Result<Void> result) {
        goMobileScene(1);
    }

    protected void goMobileScene(int i) {
        if (this.mAttachedActivity == null || !(this.mAttachedActivity instanceof SpecialLoginActivity) || this.mAttachedActivity.isFinishing()) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(UtilityImpl.NET_TYPE_MOBILE, getMobile());
        bundle.putInt("scene", i);
        ((SpecialLoginActivity) this.mAttachedActivity).jumpToCheckCode(bundle);
    }

    protected String getPwd() {
        EditText editText = this.mPwdInputBox;
        return editText != null ? editText.getText().toString() : "";
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.fragment.BaseFragment
    @NonNull
    protected String getMobile() {
        return this.mMobile;
    }

    protected int getLayout() {
        return R.layout.ali_sdk_openaccount_fragment_pwd;
    }

    protected void goNext() {
        if (TextUtils.isEmpty(getMobile()) || TextUtils.isEmpty(getPwd())) {
            return;
        }
        new PwdLoginTask(this.mAttachedActivity, getMobile(), getPwd(), null, null, null).execute(new Void[0]);
    }

    protected class PwdLoginTask extends TaskWithToastMessage<LoginResult> {
        private String cSessionId;
        private String loginId;
        private String nocToken;
        private String password;
        private String sig;

        public PwdLoginTask(Activity activity2, String str, String str2, String str3, String str4, String str5) {
            super(activity2);
            this.loginId = str;
            this.password = str2;
            this.sig = str3;
            this.nocToken = str4;
            this.cSessionId = str5;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        public Result<LoginResult> asyncExecute(Void... voidArr) {
            HashMap map = new HashMap();
            String str = this.loginId;
            if (str != null) {
                map.put("loginId", str);
            }
            if (this.password != null) {
                try {
                    String rsaPubkey = RSAKey.getRsaPubkey();
                    if (TextUtils.isEmpty(rsaPubkey)) {
                        return null;
                    }
                    map.put("password", Rsa.encrypt(this.password, rsaPubkey));
                } catch (Exception unused) {
                    return null;
                }
            }
            String str2 = this.sig;
            if (str2 != null) {
                map.put("sig", str2);
            }
            if (!TextUtils.isEmpty(this.cSessionId)) {
                map.put("csessionid", this.cSessionId);
            }
            if (!TextUtils.isEmpty(this.nocToken)) {
                map.put("nctoken", this.nocToken);
            }
            return OpenAccountUtils.toLoginResult(RpcUtils.pureInvokeWithRiskControlInfo("loginRequest", map, FirebaseAnalytics.Event.LOGIN));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        public LoginResult parseData(JSONObject jSONObject) {
            return OpenAccountUtils.parseLoginResult(jSONObject);
        }

        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected void doSuccessAfterToast(Result<LoginResult> result) {
            SessionData sessionDataCreateSessionDataFromLoginSuccessResult = OpenAccountUtils.createSessionDataFromLoginSuccessResult(result.data.loginSuccessResult);
            if (sessionDataCreateSessionDataFromLoginSuccessResult.scenario == null) {
                sessionDataCreateSessionDataFromLoginSuccessResult.scenario = 1;
            }
            ((SessionManagerService) OpenAccountSDK.getService(SessionManagerService.class)).updateSession(sessionDataCreateSessionDataFromLoginSuccessResult);
            PwdLoginFragment.this.successCallback();
        }

        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected void doFailAfterToast(Result<LoginResult> result) {
            PwdLoginFragment.this.handleFailCase(result);
        }

        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected boolean toastMessageRequired(Result<LoginResult> result) {
            return PwdLoginFragment.this.needToast(result);
        }
    }

    protected void handleFailCase(Result<LoginResult> result) {
        if (result != null) {
            if (result.code == 26053) {
                if (result.data == null || result.data.checkCodeResult == null || TextUtils.isEmpty(result.data.checkCodeResult.clientVerifyData)) {
                    return;
                }
                Uri.Builder builderBuildUpon = Uri.parse(result.data.checkCodeResult.clientVerifyData).buildUpon();
                builderBuildUpon.appendQueryParameter("callback", "https://www.alipay.com/webviewbridge");
                Intent intent = new Intent(this.mAttachedActivity, (Class<?>) LoginDoubleCheckWebActivity.class);
                intent.putExtra("url", builderBuildUpon.toString());
                intent.putExtra("title", result.message);
                intent.putExtra("callback", "https://www.alipay.com/webviewbridge");
                startActivityForResult(intent, RequestCode.NO_CAPTCHA_REQUEST_CODE);
                return;
            }
            if (result.code != 26152 || result.data == null || result.data.checkCodeResult == null || TextUtils.isEmpty(result.data.checkCodeResult.clientVerifyData)) {
                return;
            }
            Uri.Builder builderBuildUpon2 = Uri.parse(result.data.checkCodeResult.clientVerifyData).buildUpon();
            builderBuildUpon2.appendQueryParameter("callback", "https://www.alipay.com/webviewbridge");
            Intent intent2 = new Intent(this.mAttachedActivity, (Class<?>) LoginIVWebActivity.class);
            intent2.putExtra("url", builderBuildUpon2.toString());
            intent2.putExtra("title", result.message);
            intent2.putExtra("callback", "https://www.alipay.com/webviewbridge");
            startActivityForResult(intent2, RequestCode.RISK_IV_REQUEST_CODE);
        }
    }

    protected boolean needToast(Result<LoginResult> result) {
        if (result != null) {
            return (result.code == 26053 || result.code == 26152) ? false : true;
        }
        return true;
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.fragment.BaseFragment
    protected void successCallback() {
        ((ExecutorService) OpenAccountSDK.getService(ExecutorService.class)).postUITask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.fragment.PwdLoginFragment.3
            @Override // java.lang.Runnable
            public void run() {
                LoginCallback loginCallback = PwdLoginFragment.this.getLoginCallback();
                if (loginCallback != null) {
                    loginCallback.onSuccess(((SessionManagerService) OpenAccountSDK.getService(SessionManagerService.class)).getSession());
                    if (PwdLoginFragment.this.mAttachedActivity != null) {
                        PwdLoginFragment.this.mAttachedActivity.finish();
                    }
                }
            }
        });
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.fragment.BaseFragment
    protected LoginCallback getLoginCallback() {
        if (OpenAccountUIServiceImpl._specialLoginCallback != null) {
            return OpenAccountUIServiceImpl._specialLoginCallback;
        }
        return null;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == RequestCode.NO_CAPTCHA_REQUEST_CODE && i2 == -1) {
            if (intent == null || !"nocaptcha".equals(intent.getStringExtra(NativeProtocol.WEB_DIALOG_ACTION))) {
                return;
            }
            String stringExtra = intent.getStringExtra("cSessionId");
            String stringExtra2 = intent.getStringExtra("nocToken");
            new PwdLoginTask(this.mAttachedActivity, getMobile(), getPwd(), intent.getStringExtra("sig"), stringExtra2, stringExtra).execute(new Void[0]);
            return;
        }
        if (i == RequestCode.RISK_IV_REQUEST_CODE && i2 == -1 && intent != null) {
            String stringExtra3 = intent.getStringExtra("havana_iv_token");
            String stringExtra4 = intent.getStringExtra("actionType");
            new LoginByIVTokenTask(this.mAttachedActivity, stringExtra3, getMobile(), stringExtra4, true, getLoginCallback()).execute(new Void[0]);
        }
    }
}
