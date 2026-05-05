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
import androidx.annotation.NonNull;
import anetwork.channel.util.RequestConstant;
import com.alibaba.sdk.android.openaccount.model.CheckCodeResult;
import com.alibaba.sdk.android.openaccount.model.Result;
import com.alibaba.sdk.android.openaccount.rpc.RpcServerBizConstants;
import com.alibaba.sdk.android.openaccount.task.TaskWithDialog;
import com.alibaba.sdk.android.openaccount.ui.R;
import com.alibaba.sdk.android.openaccount.ui.RequestCode;
import com.alibaba.sdk.android.openaccount.ui.model.CheckAccountExistResult;
import com.alibaba.sdk.android.openaccount.ui.ui.LoginDoubleCheckWebActivity;
import com.alibaba.sdk.android.openaccount.ui.ui.LoginIVWebActivity;
import com.alibaba.sdk.android.openaccount.ui.ui.SpecialLoginActivity;
import com.alibaba.sdk.android.openaccount.ui.ui.fragment.BaseFragment;
import com.alibaba.sdk.android.openaccount.ui.util.ToastUtils;
import com.alibaba.sdk.android.openaccount.ui.widget.NetworkCheckOnClickListener;
import com.alibaba.sdk.android.openaccount.util.JSONUtils;
import com.alibaba.sdk.android.openaccount.util.RpcUtils;
import com.facebook.internal.NativeProtocol;
import com.taobao.accs.utl.UtilityImpl;
import java.util.HashMap;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class MobileFragment extends BaseFragment {
    protected String mAccountExist;
    protected EditText mMobileInputBox;
    protected Button mNextButton;
    protected int mScene = 0;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        if (this.mAttachedActivity != null && this.mAttachedActivity.getSupportActionBar() != null) {
            this.mAttachedActivity.getSupportActionBar().setTitle(R.string.ali_sdk_openaccount_text_enter_mobile);
        }
        View viewInflate = layoutInflater.inflate(getLayout(), viewGroup, false);
        this.mMobileInputBox = (EditText) viewInflate.findViewById(R.id.ali_user_mobile_input_box);
        this.mNextButton = (Button) viewInflate.findViewById(R.id.ali_user_mobile_next);
        initParams();
        afterViews();
        return viewInflate;
    }

    protected int getLayout() {
        return R.layout.ali_sdk_openaccount_fragment_mobile;
    }

    private void initParams() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            try {
                this.mScene = arguments.getInt("scene");
            } catch (Exception unused) {
            }
        }
    }

    private void afterViews() {
        Button button = this.mNextButton;
        if (button != null) {
            button.setOnClickListener(new NetworkCheckOnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.fragment.MobileFragment.1
                @Override // com.alibaba.sdk.android.openaccount.ui.widget.NetworkCheckOnClickListener
                public void afterCheck(View view2) {
                    MobileFragment.this.goNext();
                }
            });
        }
    }

    protected void goNext() {
        CheckAccountExistTask checkAccountExistTaskCreateCheckAccountExistTask;
        if (TextUtils.isEmpty(getMobile()) || this.mScene != 0 || (checkAccountExistTaskCreateCheckAccountExistTask = createCheckAccountExistTask(null, null, null)) == null) {
            return;
        }
        checkAccountExistTaskCreateCheckAccountExistTask.execute(new Void[0]);
    }

    protected CheckAccountExistTask createCheckAccountExistTask(String str, String str2, String str3) {
        String mobile;
        if (this.mMobileInputBox == null || (mobile = getMobile()) == null || mobile.length() <= 0) {
            return null;
        }
        return new CheckAccountExistTask(this.mActivity, mobile, str, str2, str3);
    }

    private void goCheckCode(String str) {
        if (this.mAttachedActivity == null || !(this.mAttachedActivity instanceof SpecialLoginActivity) || this.mAttachedActivity.isFinishing()) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(UtilityImpl.NET_TYPE_MOBILE, getMobile());
        bundle.putString("accountExist", str);
        bundle.putInt("scene", this.mScene);
        ((SpecialLoginActivity) this.mAttachedActivity).jumpToCheckCode(bundle);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void goPassword() {
        if (this.mAttachedActivity == null || !(this.mAttachedActivity instanceof SpecialLoginActivity) || this.mAttachedActivity.isFinishing()) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(UtilityImpl.NET_TYPE_MOBILE, getMobile());
        bundle.putInt("scene", this.mScene);
        ((SpecialLoginActivity) this.mAttachedActivity).goPwd(bundle);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.fragment.BaseFragment
    @NonNull
    protected String getMobile() {
        EditText editText = this.mMobileInputBox;
        return editText != null ? editText.getText().toString() : "";
    }

    protected void sendSmsCode(String str) {
        this.mAccountExist = str;
        if (TextUtils.equals(str, RequestConstant.FALSE)) {
            new BaseFragment.SendSmsCodeForRegisterTask(this.mAttachedActivity).execute(new Void[0]);
        } else {
            new BaseFragment.SendSMSForResetPwdTask(this.mAttachedActivity).execute(new Void[0]);
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.fragment.BaseFragment
    protected void onSendSMSForResetPwdSuccess(Result<Void> result) {
        goCheckCode(this.mAccountExist);
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.fragment.BaseFragment
    protected void onSendSMSForRegisterSuccess(Result<CheckCodeResult> result) {
        goCheckCode(this.mAccountExist);
    }

    protected class CheckAccountExistTask extends TaskWithDialog<Void, Void, Result<CheckAccountExistResult>> {
        private String cSessionId;
        private String loginId;
        private String nocToken;
        private String sig;

        public CheckAccountExistTask(Activity activity2, String str, String str2, String str3, String str4) {
            super(activity2);
            this.loginId = str;
            this.sig = str2;
            this.nocToken = str3;
            this.cSessionId = str4;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        public Result<CheckAccountExistResult> asyncExecute(Void... voidArr) {
            HashMap map = new HashMap();
            map.put("loginId", this.loginId);
            String str = this.sig;
            if (str != null) {
                map.put("sig", str);
            }
            if (!TextUtils.isEmpty(this.cSessionId)) {
                map.put("csessionid", this.cSessionId);
            }
            if (!TextUtils.isEmpty(this.nocToken)) {
                map.put("nctoken", this.nocToken);
            }
            return parseJsonResult(RpcUtils.invokeWithRiskControlInfo("checkAccountExistRequest", map, "checkaccountexist"));
        }

        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        protected void doWhenException(Throwable th) {
            MobileFragment.this.onFail(th);
        }

        protected Result<CheckAccountExistResult> parseJsonResult(Result<JSONObject> result) {
            if (result.data == null) {
                return Result.result(result.code, result.message);
            }
            JSONObject jSONObject = result.data;
            CheckAccountExistResult checkAccountExistResult = new CheckAccountExistResult();
            checkAccountExistResult.accountExist = jSONObject.optString("accountExist");
            checkAccountExistResult.accountHasPassword = jSONObject.optString("accountHasPassword");
            checkAccountExistResult.havanaExist = jSONObject.optString("havanaExist");
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("checkCodeResult");
            if (jSONObjectOptJSONObject != null) {
                CheckCodeResult checkCodeResult = new CheckCodeResult();
                checkAccountExistResult.checkCodeResult = checkCodeResult;
                checkCodeResult.checkCodeId = JSONUtils.optString(jSONObjectOptJSONObject, "checkCodeId");
                checkCodeResult.checkCodeUrl = JSONUtils.optString(jSONObjectOptJSONObject, "checkCodeUrl");
                checkCodeResult.clientVerifyData = JSONUtils.optString(jSONObjectOptJSONObject, "clientVerifyData");
            }
            return Result.result(result.code, result.message, checkAccountExistResult);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Result<CheckAccountExistResult> result) {
            if (result == null) {
                ToastUtils.toastSystemError(this.context);
                return;
            }
            int i = result.code;
            if (i == 1) {
                if (TextUtils.equals(result.data.accountHasPassword, "true")) {
                    MobileFragment.this.goPassword();
                    return;
                } else {
                    MobileFragment.this.sendSmsCode(result.data != null ? result.data.accountExist : "");
                    return;
                }
            }
            if (i == 26053) {
                if (result.data == null || result.data.checkCodeResult == null || TextUtils.isEmpty(result.data.checkCodeResult.clientVerifyData)) {
                    return;
                }
                Uri.Builder builderBuildUpon = Uri.parse(result.data.checkCodeResult.clientVerifyData).buildUpon();
                builderBuildUpon.appendQueryParameter("callback", "https://www.alipay.com/webviewbridge");
                Intent intent = new Intent(MobileFragment.this.getActivity(), (Class<?>) LoginDoubleCheckWebActivity.class);
                intent.putExtra("url", builderBuildUpon.toString());
                intent.putExtra("title", result.message);
                intent.putExtra("callback", "https://www.alipay.com/webviewbridge");
                MobileFragment.this.startActivityForResult(intent, RequestCode.NO_CAPTCHA_REQUEST_CODE);
                return;
            }
            if (i == 26152) {
                if (result.data == null || result.data.checkCodeResult == null || TextUtils.isEmpty(result.data.checkCodeResult.clientVerifyData)) {
                    return;
                }
                Uri.Builder builderBuildUpon2 = Uri.parse(result.data.checkCodeResult.clientVerifyData).buildUpon();
                builderBuildUpon2.appendQueryParameter("callback", "https://www.alipay.com/webviewbridge");
                Intent intent2 = new Intent(MobileFragment.this.getActivity(), (Class<?>) LoginIVWebActivity.class);
                intent2.putExtra("url", builderBuildUpon2.toString());
                intent2.putExtra("title", result.message);
                intent2.putExtra("callback", "https://www.alipay.com/webviewbridge");
                MobileFragment.this.startActivityForResult(intent2, RequestCode.RISK_IV_REQUEST_CODE);
                return;
            }
            if (TextUtils.equals(result.type, RpcServerBizConstants.ACTION_TYPE_CALLBACK) && MobileFragment.this.getLoginCallback() != null) {
                MobileFragment.this.getLoginCallback().onFailure(result.code, result.message);
            } else if (TextUtils.isEmpty(result.message)) {
                ToastUtils.toastSystemError(this.context);
            } else {
                ToastUtils.toast(this.context, result.message, result.code);
            }
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        CheckAccountExistTask checkAccountExistTaskCreateCheckAccountExistTask;
        super.onActivityResult(i, i2, intent);
        if (i == RequestCode.NO_CAPTCHA_REQUEST_CODE && i2 == -1) {
            if (intent == null || !"nocaptcha".equals(intent.getStringExtra(NativeProtocol.WEB_DIALOG_ACTION))) {
                return;
            }
            CheckAccountExistTask checkAccountExistTaskCreateCheckAccountExistTask2 = createCheckAccountExistTask(intent.getStringExtra("sig"), intent.getStringExtra("nocToken"), intent.getStringExtra("cSessionId"));
            if (checkAccountExistTaskCreateCheckAccountExistTask2 != null) {
                checkAccountExistTaskCreateCheckAccountExistTask2.execute(new Void[0]);
                return;
            }
            return;
        }
        if (i == RequestCode.RISK_IV_REQUEST_CODE && i2 == -1 && (checkAccountExistTaskCreateCheckAccountExistTask = createCheckAccountExistTask(null, null, null)) != null) {
            checkAccountExistTaskCreateCheckAccountExistTask.execute(new Void[0]);
        }
    }
}
