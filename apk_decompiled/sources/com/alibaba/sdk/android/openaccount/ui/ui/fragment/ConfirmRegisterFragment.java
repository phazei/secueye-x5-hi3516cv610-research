package com.alibaba.sdk.android.openaccount.ui.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import anetwork.channel.util.RequestConstant;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.executor.ExecutorService;
import com.alibaba.sdk.android.openaccount.model.LoginResult;
import com.alibaba.sdk.android.openaccount.model.Result;
import com.alibaba.sdk.android.openaccount.model.SessionData;
import com.alibaba.sdk.android.openaccount.session.SessionManagerService;
import com.alibaba.sdk.android.openaccount.ui.R;
import com.alibaba.sdk.android.openaccount.ui.TokenWebViewActivity;
import com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountUIServiceImpl;
import com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage;
import com.alibaba.sdk.android.openaccount.ui.ui.SpecialLoginActivity;
import com.alibaba.sdk.android.openaccount.ui.ui.fragment.BaseFragment;
import com.alibaba.sdk.android.openaccount.ui.widget.NetworkCheckOnClickListener;
import com.alibaba.sdk.android.openaccount.util.OpenAccountUtils;
import com.alibaba.sdk.android.openaccount.util.RpcUtils;
import com.taobao.accs.utl.UtilityImpl;
import java.util.HashMap;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class ConfirmRegisterFragment extends BaseFragment implements View.OnClickListener {
    protected String mAccountExist;
    protected String mAccountHasPassword;
    protected Button mCancelButton;
    protected Button mConfirmButton;
    protected String mMobile;
    protected TextView mMobileTextView;
    protected TextView mMobileTextView2;
    protected TextView mProtocolView;
    protected String mSMSTrustToken;
    protected int mScene;

    @NonNull
    protected String getProtocol() {
        return "https://www.taobao.com";
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        if (this.mAttachedActivity != null && this.mAttachedActivity.getSupportActionBar() != null) {
            this.mAttachedActivity.getSupportActionBar().setTitle(R.string.ali_sdk_openaccount_text_register_confirm);
        }
        View viewInflate = layoutInflater.inflate(getLayout(), viewGroup, false);
        this.mProtocolView = (TextView) viewInflate.findViewById(R.id.ali_user_register_confirm_protocol);
        if (TextUtils.equals(this.mAccountExist, "true")) {
            this.mProtocolView.setVisibility(8);
        }
        this.mMobileTextView = (TextView) viewInflate.findViewById(R.id.ali_user_register_confirm_mobile_textview);
        this.mMobileTextView2 = (TextView) viewInflate.findViewById(R.id.ali_user_confirm_textview);
        this.mConfirmButton = (Button) viewInflate.findViewById(R.id.ali_user_register_confirm_next);
        this.mCancelButton = (Button) viewInflate.findViewById(R.id.ali_user_register_confirm_cancel);
        initParams();
        afterViews();
        return viewInflate;
    }

    protected int getLayout() {
        return R.layout.ali_sdk_openaccount_fragment_register_confirm;
    }

    private void initParams() {
        try {
            this.mMobile = getArguments().getString(UtilityImpl.NET_TYPE_MOBILE);
            this.mSMSTrustToken = getArguments().getString("trustToken");
            this.mScene = getArguments().getInt("scene", 0);
            this.mAccountHasPassword = getArguments().getString("accountHasPassword");
            this.mAccountExist = getArguments().getString("accountExist");
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void afterViews() {
        if (!TextUtils.isEmpty(this.mMobile) && this.mMobileTextView != null) {
            if (TextUtils.equals(this.mAccountExist, RequestConstant.FALSE)) {
                this.mMobileTextView.setText(this.mAttachedActivity.getString(R.string.ali_sdk_openaccount_text_not_register, new Object[]{this.mMobile}));
                this.mMobileTextView2.setText(R.string.ali_sdk_openaccount_text_whether_to_register);
                this.mConfirmButton.setText(R.string.ali_sdk_openaccount_text_confirm_register);
                this.mCancelButton.setText(R.string.ali_sdk_openaccount_text_exit_register);
                String string = this.mAttachedActivity.getString(R.string.ali_sdk_openaccount_text_protocol);
                SpannableString spannableString = new SpannableString(string);
                spannableString.setSpan(new ClickableSpan() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.fragment.ConfirmRegisterFragment.1
                    @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
                    public void updateDrawState(TextPaint textPaint) {
                        super.updateDrawState(textPaint);
                        try {
                            textPaint.setColor(ConfirmRegisterFragment.this.getResources().getColor(R.color.ali_sdk_openaccount_button_bg));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        textPaint.setUnderlineText(false);
                    }

                    @Override // android.text.style.ClickableSpan
                    public void onClick(View view2) {
                        if (view2 == null || view2.getContext() == null) {
                            return;
                        }
                        Context context = view2.getContext();
                        Intent intent = new Intent();
                        intent.setClass(context, ConfirmRegisterFragment.this.getWebViewClass());
                        intent.putExtra("url", ConfirmRegisterFragment.this.getProtocol());
                        context.startActivity(intent);
                    }
                }, 0, string.length(), 33);
                TextView textView = this.mProtocolView;
                if (textView != null) {
                    textView.setText(spannableString);
                    this.mProtocolView.setMovementMethod(LinkMovementMethod.getInstance());
                }
            } else {
                this.mMobileTextView.setText(this.mAttachedActivity.getString(R.string.ali_sdk_openaccount_text_already_register, new Object[]{this.mMobile}));
                this.mMobileTextView2.setText(R.string.ali_sdk_openaccount_text_whether_to_login);
                this.mConfirmButton.setText(R.string.ali_sdk_openaccount_text_confirm_login);
                this.mCancelButton.setText(R.string.ali_sdk_openaccount_text_exit);
            }
        }
        Button button = this.mConfirmButton;
        if (button != null) {
            button.setOnClickListener(new NetworkCheckOnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.fragment.ConfirmRegisterFragment.2
                @Override // com.alibaba.sdk.android.openaccount.ui.widget.NetworkCheckOnClickListener
                public void afterCheck(View view2) {
                    ConfirmRegisterFragment.this.toRegister();
                }
            });
        }
        Button button2 = this.mCancelButton;
        if (button2 != null) {
            button2.setOnClickListener(this);
        }
    }

    @NonNull
    protected Class getWebViewClass() {
        return TokenWebViewActivity.class;
    }

    protected void toRegister() {
        if (TextUtils.equals(this.mAccountExist, RequestConstant.FALSE)) {
            beforeJumpToPwd(new BaseFragment.BeforeJumpToPwdCallback() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.fragment.ConfirmRegisterFragment.3
                @Override // com.alibaba.sdk.android.openaccount.ui.ui.fragment.BaseFragment.BeforeJumpToPwdCallback
                public void onFinish() {
                    if (ConfirmRegisterFragment.this.mAttachedActivity == null || !(ConfirmRegisterFragment.this.mAttachedActivity instanceof SpecialLoginActivity) || ConfirmRegisterFragment.this.mAttachedActivity.isFinishing()) {
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("token", ConfirmRegisterFragment.this.mSMSTrustToken);
                    bundle.putInt("scene", 2);
                    ((SpecialLoginActivity) ConfirmRegisterFragment.this.mAttachedActivity).goFindPwd(bundle);
                }
            });
            return;
        }
        if (TextUtils.equals(this.mAccountHasPassword, "true")) {
            if (this.mAttachedActivity == null || !(this.mAttachedActivity instanceof SpecialLoginActivity) || this.mAttachedActivity.isFinishing()) {
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString(UtilityImpl.NET_TYPE_MOBILE, this.mMobile);
            bundle.putString("trustToken", this.mSMSTrustToken);
            bundle.putInt("scene", this.mScene);
            ((SpecialLoginActivity) this.mAttachedActivity).goPwd(bundle);
            return;
        }
        if (TextUtils.equals(this.mAccountHasPassword, RequestConstant.FALSE)) {
            beforeJumpToPwd(new BaseFragment.BeforeJumpToPwdCallback() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.fragment.ConfirmRegisterFragment.4
                @Override // com.alibaba.sdk.android.openaccount.ui.ui.fragment.BaseFragment.BeforeJumpToPwdCallback
                public void onFinish() {
                    if (ConfirmRegisterFragment.this.mAttachedActivity == null || !(ConfirmRegisterFragment.this.mAttachedActivity instanceof SpecialLoginActivity) || ConfirmRegisterFragment.this.mAttachedActivity.isFinishing()) {
                        return;
                    }
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("token", ConfirmRegisterFragment.this.mSMSTrustToken);
                    bundle2.putInt("scene", ConfirmRegisterFragment.this.mScene);
                    ((SpecialLoginActivity) ConfirmRegisterFragment.this.mAttachedActivity).goFindPwd(bundle2);
                }
            });
        }
    }

    protected void beforeJumpToPwd(BaseFragment.BeforeJumpToPwdCallback beforeJumpToPwdCallback) {
        if (beforeJumpToPwdCallback != null) {
            beforeJumpToPwdCallback.onFinish();
        }
    }

    protected class RegisterTask extends TaskWithToastMessage<LoginResult> {
        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected void doFailAfterToast(Result<LoginResult> result) {
        }

        public RegisterTask(Activity activity2) {
            super(activity2);
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
            ConfirmRegisterFragment.this.successCallback();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        public Result<LoginResult> asyncExecute(Void... voidArr) {
            HashMap map = new HashMap();
            map.put("token", ConfirmRegisterFragment.this.mSMSTrustToken);
            return parseJsonResult(RpcUtils.invokeWithRiskControlInfo("registerRequest", map, "register"));
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.fragment.BaseFragment
    protected void successCallback() {
        ((ExecutorService) OpenAccountSDK.getService(ExecutorService.class)).postUITask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.fragment.ConfirmRegisterFragment.5
            @Override // java.lang.Runnable
            public void run() {
                LoginCallback loginCallback = ConfirmRegisterFragment.this.getLoginCallback();
                if (loginCallback != null) {
                    loginCallback.onSuccess(((SessionManagerService) OpenAccountSDK.getService(SessionManagerService.class)).getSession());
                    if (ConfirmRegisterFragment.this.mAttachedActivity != null) {
                        ConfirmRegisterFragment.this.mAttachedActivity.finish();
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

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        if (view2.getId() == R.id.ali_user_register_confirm_cancel) {
            finishActivity();
        }
    }

    protected void finishActivity() {
        if (this.mAttachedActivity == null || !(this.mAttachedActivity instanceof SpecialLoginActivity)) {
            return;
        }
        ((SpecialLoginActivity) this.mAttachedActivity).finishCurrentActivity();
    }
}
