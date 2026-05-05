package com.alibaba.sdk.android.openaccount.ui.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.OauthService;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.annotation.ExtensionPoint;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.executor.ExecutorService;
import com.alibaba.sdk.android.openaccount.message.Message;
import com.alibaba.sdk.android.openaccount.message.MessageConstants;
import com.alibaba.sdk.android.openaccount.message.MessageUtils;
import com.alibaba.sdk.android.openaccount.model.LoginResult;
import com.alibaba.sdk.android.openaccount.model.LoginSuccessResult;
import com.alibaba.sdk.android.openaccount.model.OpenAccountSession;
import com.alibaba.sdk.android.openaccount.model.Result;
import com.alibaba.sdk.android.openaccount.model.SessionData;
import com.alibaba.sdk.android.openaccount.model.UserContract;
import com.alibaba.sdk.android.openaccount.rpc.RpcServerBizConstants;
import com.alibaba.sdk.android.openaccount.session.SessionManagerService;
import com.alibaba.sdk.android.openaccount.task.TaskWithDialog;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConfigs;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService;
import com.alibaba.sdk.android.openaccount.ui.R;
import com.alibaba.sdk.android.openaccount.ui.RequestCode;
import com.alibaba.sdk.android.openaccount.ui.callback.EmailResetPasswordCallback;
import com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountUIServiceImpl;
import com.alibaba.sdk.android.openaccount.ui.model.SmsActionType;
import com.alibaba.sdk.android.openaccount.ui.task.LoginByIVTokenTask;
import com.alibaba.sdk.android.openaccount.ui.util.AttributeUtils;
import com.alibaba.sdk.android.openaccount.ui.util.StringUtils;
import com.alibaba.sdk.android.openaccount.ui.util.ToastUtils;
import com.alibaba.sdk.android.openaccount.ui.widget.InputBoxWithHistory;
import com.alibaba.sdk.android.openaccount.ui.widget.NetworkCheckOnClickListener;
import com.alibaba.sdk.android.openaccount.ui.widget.NextStepButtonWatcher;
import com.alibaba.sdk.android.openaccount.ui.widget.NonMultiClickListener;
import com.alibaba.sdk.android.openaccount.ui.widget.OauthWidget;
import com.alibaba.sdk.android.openaccount.ui.widget.PasswordInputBox;
import com.alibaba.sdk.android.openaccount.util.CommonUtils;
import com.alibaba.sdk.android.openaccount.util.Md5Utils;
import com.alibaba.sdk.android.openaccount.util.OpenAccountUtils;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;
import com.alibaba.sdk.android.openaccount.util.RpcUtils;
import com.alibaba.sdk.android.openaccount.util.safe.RSAKey;
import com.alibaba.sdk.android.openaccount.util.safe.Rsa;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import com.facebook.internal.NativeProtocol;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.taobao.accs.utl.UtilityImpl;
import com.xiaomi.mipush.sdk.Constants;
import java.util.HashMap;
import org.json.JSONObject;
import org.mozilla.javascript.ES6Iterator;
import tools.LocationUtil;

/* JADX INFO: loaded from: classes.dex */
@ExtensionPoint
public class LoginActivity extends NextStepActivityTemplate {
    protected static final String TAG = "oa";
    protected InputBoxWithHistory loginIdEdit;
    protected TextView loginWithSmsCodeTV;
    protected OauthWidget oauthWidget;
    protected PasswordInputBox passwordEdit;
    protected TextView registerTV;
    protected TextView resetPasswordTV;

    @Autowired
    protected SessionManagerService sessionManagerService;
    protected String token;

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.ActivityTemplate
    protected String getLayoutName() {
        return "ali_sdk_openaccount_login";
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.NextStepActivityTemplate, com.alibaba.sdk.android.openaccount.ui.ui.ActivityTemplate, com.alibaba.sdk.android.openaccount.ui.ui.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.loginIdEdit = (InputBoxWithHistory) findViewById("login_id");
        this.loginIdEdit.getInputBoxWithClear().setSupportForeignMobile(this, OpenAccountUIConfigs.AccountPasswordLoginFlow.mobileCountrySelectorActvityClazz, OpenAccountUIConfigs.AccountPasswordLoginFlow.supportForeignMobileNumbers);
        this.passwordEdit = (PasswordInputBox) findViewById("password");
        this.loginIdEdit.setHistoryView((ListView) findViewById("input_history"));
        Button button = (Button) findViewById(ES6Iterator.NEXT_METHOD);
        if (ConfigManager.getInstance().isSupportOfflineLogin()) {
            button.setOnClickListener(new NonMultiClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.LoginActivity.1
                @Override // com.alibaba.sdk.android.openaccount.ui.widget.NonMultiClickListener
                public void onMonMultiClick(View view2) {
                    LoginActivity.this.login(view2);
                }
            });
        } else {
            button.setOnClickListener(new NetworkCheckOnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.LoginActivity.2
                @Override // com.alibaba.sdk.android.openaccount.ui.widget.NetworkCheckOnClickListener
                public void afterCheck(View view2) {
                    LoginActivity.this.login(view2);
                }
            });
        }
        NextStepButtonWatcher nextStepButtonWatcher = getNextStepButtonWatcher();
        nextStepButtonWatcher.addEditTexts(this.loginIdEdit.getEditText(), this.passwordEdit.getEditText());
        this.passwordEdit.getInputBoxWithClear().getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.LoginActivity.3
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i != 6) {
                    return false;
                }
                LoginActivity.this.login(null);
                return true;
            }
        });
        this.passwordEdit.getInputBoxWithClear().addTextChangedListener(nextStepButtonWatcher);
        this.loginIdEdit.getInputBoxWithClear().addTextChangedListener(nextStepButtonWatcher);
        this.loginIdEdit.getEditText().addTextChangedListener(new TextWatcher() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.LoginActivity.4
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                LoginActivity.this.loginIdEdit.updateHistoryView(editable.toString());
            }
        });
        if (bundle != null) {
            String string = bundle.getString("login_id");
            if (!TextUtils.isEmpty(string)) {
                this.loginIdEdit.getInputBoxWithClear().getEditText().setText(string);
            }
        }
        this.registerTV = (TextView) findViewById(ResourceUtils.getRId(this, "register"));
        TextView textView = this.registerTV;
        if (textView != null) {
            textView.setOnClickListener(new View.OnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.LoginActivity.5
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    LoginActivity.this.registerUser(view2);
                }
            });
        }
        this.resetPasswordTV = (TextView) findViewById(ResourceUtils.getRId(this, SmsActionType.RESET_PASSWORD));
        TextView textView2 = this.resetPasswordTV;
        if (textView2 != null) {
            textView2.setOnClickListener(new View.OnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.LoginActivity.6
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    LoginActivity.this.forgetPassword(view2);
                }
            });
        }
        this.loginWithSmsCodeTV = (TextView) findViewById(ResourceUtils.getRId(this, "login_with_sms_code"));
        TextView textView3 = this.loginWithSmsCodeTV;
        if (textView3 != null) {
            textView3.setOnClickListener(new View.OnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.LoginActivity.7
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    LoginActivity.this.loginWithSmsCode(view2);
                }
            });
        }
        this.oauthWidget = (OauthWidget) findViewById(ResourceUtils.getRId(this, "oauth"));
        if (this.oauthWidget != null) {
            if (OpenAccountSDK.getService(OauthService.class) == null) {
                this.oauthWidget.setVisibility(8);
            } else {
                this.oauthWidget.setOauthOnClickListener(new LoginCallback() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.LoginActivity.8
                    @Override // com.alibaba.sdk.android.openaccount.callback.FailureCallback
                    public void onFailure(int i, String str) {
                        LoginCallback loginCallback = LoginActivity.this.getLoginCallback();
                        if (loginCallback != null) {
                            loginCallback.onFailure(i, str);
                        }
                    }

                    @Override // com.alibaba.sdk.android.openaccount.callback.LoginCallback
                    public void onSuccess(OpenAccountSession openAccountSession) {
                        LoginCallback loginCallback = LoginActivity.this.getLoginCallback();
                        if (loginCallback != null) {
                            loginCallback.onSuccess(openAccountSession);
                        }
                        LoginActivity.this.finishWithoutCallback();
                    }
                });
            }
        }
        useCustomAttrs(this, this.attrs);
    }

    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("token", this.token);
        Editable text = this.loginIdEdit.getInputBoxWithClear().getEditText().getText();
        if (text != null) {
            bundle.putString("login_id", text.toString());
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.ActivityTemplate
    protected void doUseCustomAttrs(Context context, TypedArray typedArray) {
        super.doUseCustomAttrs(context, typedArray);
        TextView textView = this.registerTV;
        if (textView != null) {
            textView.setTextColor(AttributeUtils.getColor(context, typedArray, "ali_sdk_openaccount_attrs_login_register_text_color"));
        }
        TextView textView2 = this.resetPasswordTV;
        if (textView2 != null) {
            textView2.setTextColor(AttributeUtils.getColor(context, typedArray, "ali_sdk_openaccount_attrs_login_reset_password_text_color"));
        }
    }

    public void login(View view2) {
        login(view2, null, null, null);
    }

    public void login(View view2, String str, String str2, String str3) {
        LoginTask loginTaskCreateLoginTask = createLoginTask(str, str2, str3);
        if (loginTaskCreateLoginTask != null) {
            loginTaskCreateLoginTask.execute(new Void[0]);
        }
    }

    protected String getLoginId() {
        InputBoxWithHistory inputBoxWithHistory = this.loginIdEdit;
        if (inputBoxWithHistory == null) {
            return "";
        }
        String mobileLocationCode = inputBoxWithHistory.getInputBoxWithClear().getMobileLocationCode();
        if (!TextUtils.isEmpty(mobileLocationCode) && !TextUtils.equals("86", mobileLocationCode)) {
            return mobileLocationCode + Constants.ACCEPT_TIME_SEPARATOR_SERVER + this.loginIdEdit.getEditText().getText().toString();
        }
        return this.loginIdEdit.getEditText().getText().toString();
    }

    protected LoginTask createLoginTask(String str, String str2, String str3) {
        String string;
        String loginId = getLoginId();
        if (loginId == null || loginId.length() <= 0 || (string = this.passwordEdit.getEditText().getText().toString()) == null || loginId.length() <= 0) {
            return null;
        }
        return new LoginTask(this, loginId, string, str, str2, str3);
    }

    protected class LoginTask extends TaskWithDialog<Void, Void, Result<LoginResult>> {
        private String cSessionId;
        private String loginId;
        private String nocToken;
        private String password;
        private String sig;

        public LoginTask(Activity activity2, String str, String str2, String str3, String str4, String str5) {
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
            LoginActivity.this.hideSoftInputForHw();
            if (!CommonUtils.isNetworkAvailable()) {
                if (ConfigManager.getInstance().isSupportOfflineLogin()) {
                    return LoginActivity.this.tryOfflineLogin(this.loginId, this.password);
                }
                Result<LoginResult> result = new Result<>();
                result.code = MessageConstants.NETWORK_NOT_AVAILABLE;
                result.message = MessageUtils.getMessageContent(MessageConstants.NETWORK_NOT_AVAILABLE, new Object[0]);
                return result;
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
            Result<LoginResult> loginResult = OpenAccountUtils.toLoginResult(RpcUtils.pureInvokeWithRiskControlInfo("loginRequest", map, FirebaseAnalytics.Event.LOGIN));
            return (ConfigManager.getInstance().isSupportOfflineLogin() && loginResult.code == 10019) ? LoginActivity.this.tryOfflineLogin(this.loginId, this.password) : loginResult;
        }

        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        protected void doWhenException(Throwable th) {
            this.executorService.postUITask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.LoginActivity.LoginTask.1
                @Override // java.lang.Runnable
                public void run() {
                    ToastUtils.toastSystemError(LoginTask.this.context);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Result<LoginResult> result) {
            dismissProgressDialog();
            super.onPostExecute((Object) result);
            try {
            } catch (Throwable th) {
                AliSDKLogger.e("oa", "after post execute error", th);
                ToastUtils.toastSystemError(this.context);
                return;
            }
            if (result == null) {
                if (ConfigManager.getInstance().isSupportOfflineLogin()) {
                    ToastUtils.toastNetworkError(this.context);
                    return;
                } else {
                    ToastUtils.toastSystemError(this.context);
                    return;
                }
            }
            int i = result.code;
            if (i == 4037) {
                if (OpenAccountUIConfigs.AccountPasswordLoginFlow.showAlertForPwdErrorToManyTimes) {
                    String string = LoginActivity.this.getResources().getString(R.string.ali_sdk_openaccount_text_confirm);
                    String string2 = LoginActivity.this.getResources().getString(R.string.ali_sdk_openaccount_text_reset_password);
                    final ToastUtils toastUtils = new ToastUtils();
                    toastUtils.alert(LoginActivity.this, "", result.message, string, new DialogInterface.OnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.LoginActivity.LoginTask.2
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i2) {
                            toastUtils.dismissAlertDialog(LoginActivity.this);
                        }
                    }, string2, new DialogInterface.OnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.LoginActivity.LoginTask.3
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i2) {
                            LoginActivity.this.forgetPassword(null);
                        }
                    });
                    return;
                }
                ToastUtils.toast(this.context, result.message + result.code, result.code);
                return;
            }
            if (i == 26053) {
                if (result.data == null || result.data.checkCodeResult == null || TextUtils.isEmpty(result.data.checkCodeResult.clientVerifyData)) {
                    return;
                }
                Uri.Builder builderBuildUpon = Uri.parse(result.data.checkCodeResult.clientVerifyData).buildUpon();
                builderBuildUpon.appendQueryParameter("callback", "https://www.alipay.com/webviewbridge");
                Intent intent = new Intent(LoginActivity.this, (Class<?>) LoginDoubleCheckWebActivity.class);
                intent.putExtra("url", builderBuildUpon.toString());
                intent.putExtra("title", result.message);
                intent.putExtra("callback", "https://www.alipay.com/webviewbridge");
                LoginActivity.this.startActivityForResult(intent, RequestCode.NO_CAPTCHA_REQUEST_CODE);
                return;
            }
            if (i == 26152) {
                if (result.data == null || result.data.checkCodeResult == null || TextUtils.isEmpty(result.data.checkCodeResult.clientVerifyData)) {
                    return;
                }
                Uri.Builder builderBuildUpon2 = Uri.parse(result.data.checkCodeResult.clientVerifyData).buildUpon();
                builderBuildUpon2.appendQueryParameter("callback", "https://www.alipay.com/webviewbridge");
                Intent intent2 = new Intent(LoginActivity.this, (Class<?>) LoginIVWebActivity.class);
                intent2.putExtra("url", builderBuildUpon2.toString());
                intent2.putExtra("title", result.message);
                intent2.putExtra("callback", "https://www.alipay.com/webviewbridge");
                LoginActivity.this.startActivityForResult(intent2, RequestCode.RISK_IV_REQUEST_CODE);
                return;
            }
            if (i != 40399) {
                switch (i) {
                    case 1:
                        if (result.data != null && result.data.loginSuccessResult != null) {
                            SessionData sessionDataCreateSessionDataFromLoginSuccessResult = OpenAccountUtils.createSessionDataFromLoginSuccessResult(result.data.loginSuccessResult);
                            if (sessionDataCreateSessionDataFromLoginSuccessResult.scenario == null) {
                                sessionDataCreateSessionDataFromLoginSuccessResult.scenario = 1;
                            }
                            LoginActivity.this.sessionManagerService.updateSession(sessionDataCreateSessionDataFromLoginSuccessResult);
                            String str = result.data.userInputName;
                            if (TextUtils.isEmpty(str)) {
                                str = this.loginId;
                            }
                            if (ConfigManager.getInstance().isSupportOfflineLogin()) {
                                OpenAccountSDK.getSqliteUtil().saveToSqlite(this.loginId, this.password);
                            }
                            boolean zSaveInputHistory = LoginActivity.this.loginIdEdit.saveInputHistory(str);
                            if (!OpenAccountUIConfigs.AccountPasswordLoginFlow.showTipAlertAfterLogin || zSaveInputHistory) {
                                LoginActivity.this.loginSuccess();
                            } else {
                                LoginActivity.this.showTipDialog(String.format(ResourceUtils.getString(LoginActivity.this.getApplicationContext(), "ali_sdk_openaccount_dynamic_text_alert_msg_after_login"), this.loginId));
                            }
                            break;
                        }
                        break;
                    case 2:
                        SessionData sessionDataCreateSessionDataFromLoginSuccessResult2 = OpenAccountUtils.createSessionDataFromLoginSuccessResult(result.data.loginSuccessResult);
                        if (sessionDataCreateSessionDataFromLoginSuccessResult2.scenario == null) {
                            sessionDataCreateSessionDataFromLoginSuccessResult2.scenario = 1;
                        }
                        LoginActivity.this.sessionManagerService.updateSession(sessionDataCreateSessionDataFromLoginSuccessResult2);
                        LoginActivity.this.loginSuccess();
                        break;
                    default:
                        if (TextUtils.equals(result.type, RpcServerBizConstants.ACTION_TYPE_CALLBACK) && LoginActivity.this.getLoginCallback() != null) {
                            LoginActivity.this.getLoginCallback().onFailure(result.code, result.message);
                        } else {
                            LoginActivity.this.onPwdLoginFail(result.code, result.message);
                        }
                        break;
                }
                return;
            }
            ToastUtils.toast(this.context, this.context.getString(R.string.please_check_phone_time), RpcServerBizConstants.PWD_ERROR_TO_TIME_EXPIRED);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideSoftInputForHw() {
        InputMethodManager inputMethodManager;
        if (!LocationUtil.MANUFACTURER_HUAWEI.equalsIgnoreCase(Build.MANUFACTURER) || Build.VERSION.SDK_INT < 27 || (inputMethodManager = (InputMethodManager) getSystemService("input_method")) == null) {
            return;
        }
        inputMethodManager.hideSoftInputFromWindow(this.passwordEdit.getEditText().getWindowToken(), 2);
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [T, com.alibaba.sdk.android.openaccount.model.LoginResult] */
    public Result<LoginResult> tryOfflineLogin(String str, String str2) {
        String sha256 = Md5Utils.getSHA256(str + "&" + str2);
        UserContract account = OpenAccountSDK.getSqliteUtil().getAccount(sha256);
        if (account != null && TextUtils.equals(account.getHash(), sha256)) {
            Result<LoginResult> result = new Result<>();
            result.code = 2;
            ?? loginResult = new LoginResult();
            LoginSuccessResult loginSuccessResult = new LoginSuccessResult();
            loginSuccessResult.openAccount = new JSONObject();
            try {
                loginSuccessResult.openAccount.put("id", account.getUserid());
                loginSuccessResult.openAccount.put(UtilityImpl.NET_TYPE_MOBILE, account.getMobile());
                loginSuccessResult.openAccount.put("nick", account.getNick());
                loginSuccessResult.openAccount.put("loginId", account.getLoginId());
                loginSuccessResult.openAccount.put("email", account.getEmail());
            } catch (Exception e) {
                e.printStackTrace();
            }
            loginResult.loginSuccessResult = loginSuccessResult;
            result.data = loginResult;
            return result;
        }
        Result<LoginResult> result2 = new Result<>();
        result2.code = 3;
        result2.message = ResourceUtils.getString(this, "ali_sdk_openaccount_dynamic_text_offline_exception");
        return result2;
    }

    public void showTipDialog(String str) {
        if (isFinishing()) {
            finishWithoutCallback();
        } else {
            new AlertDialog.Builder(this).setMessage(str).setPositiveButton(ResourceUtils.getString(getApplicationContext(), "ali_sdk_openaccount_dynamic_text_iknow"), new DialogInterface.OnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.LoginActivity.10
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    LoginActivity.this.loginSuccess();
                }
            }).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.LoginActivity.9
                @Override // android.content.DialogInterface.OnCancelListener
                public void onCancel(DialogInterface dialogInterface) {
                    LoginActivity.this.loginSuccess();
                }
            }).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loginSuccess() {
        AliSDKLogger.i("oa", "loginSuccess");
        ((ExecutorService) OpenAccountSDK.getService(ExecutorService.class)).postUITask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.LoginActivity.11
            @Override // java.lang.Runnable
            public void run() {
                LoginCallback loginCallback = LoginActivity.this.getLoginCallback();
                if (loginCallback != null) {
                    AliSDKLogger.i("oa", "loginCallback != null");
                    loginCallback.onSuccess(LoginActivity.this.sessionManagerService.getSession());
                }
                ((ExecutorService) OpenAccountSDK.getService(ExecutorService.class)).postUITask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.LoginActivity.11.1
                    @Override // java.lang.Runnable
                    public void run() {
                        LoginActivity.this.finishWithoutCallback();
                    }
                });
            }
        });
    }

    protected void onPwdLoginFail(int i, String str) {
        if (TextUtils.isEmpty(str)) {
            ToastUtils.toastSystemError(getApplicationContext());
            return;
        }
        ToastUtils.toast(getApplicationContext(), str + i, i);
    }

    public void forgetPassword(View view2) {
        HashMap map = new HashMap();
        if (!TextUtils.isEmpty(this.loginIdEdit.getInputBoxWithClear().getMobileLocationCode())) {
            map.put("LocationCode", this.loginIdEdit.getInputBoxWithClear().getMobileLocationCode());
        }
        String string = this.loginIdEdit.getEditText().getText().toString();
        map.put(UtilityImpl.NET_TYPE_MOBILE, string);
        OpenAccountUIService openAccountUIService = (OpenAccountUIService) OpenAccountSDK.getService(OpenAccountUIService.class);
        if (StringUtils.isEmail(string)) {
            openAccountUIService.showEmailResetPassword(this, OpenAccountUIConfigs.EmailResetPasswordLoginFlow.resetPasswordActivityClazz, getResetPasswordCallback4Email());
        } else {
            openAccountUIService.showResetPassword(this, map, OpenAccountUIConfigs.UnifyLoginFlow.resetPasswordActivityClass, getResetPasswordLoginCallback());
        }
    }

    public void loginWithSmsCode(View view2) {
        HashMap map = new HashMap();
        if (!TextUtils.isEmpty(this.loginIdEdit.getInputBoxWithClear().getMobileLocationCode())) {
            map.put("LocationCode", this.loginIdEdit.getInputBoxWithClear().getMobileLocationCode());
        }
        map.put(UtilityImpl.NET_TYPE_MOBILE, this.loginIdEdit.getEditText().getText().toString());
        ((OpenAccountUIService) OpenAccountSDK.getService(OpenAccountUIService.class)).showLoginWithSmsCode(this, map, OpenAccountUIConfigs.LoginWithSmsCodeFlow.loginWithSmsCodeActivityClass, getResetPasswordLoginCallback());
    }

    protected EmailResetPasswordCallback getResetPasswordCallback4Email() {
        return new EmailResetPasswordCallback() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.LoginActivity.12
            @Override // com.alibaba.sdk.android.openaccount.ui.callback.EmailResetPasswordCallback
            public void onEmailSent(String str) {
                String string = ResourceUtils.getString(LoginActivity.this, "alisdk_openaccount_message_email_already_send");
                Toast.makeText(LoginActivity.this.getApplicationContext(), str + string, 1).show();
            }

            @Override // com.alibaba.sdk.android.openaccount.callback.FailureCallback
            public void onFailure(int i, String str) {
                LoginCallback loginCallback = LoginActivity.this.getLoginCallback();
                if (loginCallback != null) {
                    loginCallback.onFailure(i, str);
                }
            }

            @Override // com.alibaba.sdk.android.openaccount.callback.LoginCallback
            public void onSuccess(OpenAccountSession openAccountSession) {
                LoginCallback loginCallback = LoginActivity.this.getLoginCallback();
                if (loginCallback != null) {
                    loginCallback.onSuccess(openAccountSession);
                }
                LoginActivity.this.finishWithoutCallback();
            }
        };
    }

    protected LoginCallback getResetPasswordLoginCallback() {
        return new LoginCallback() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.LoginActivity.13
            @Override // com.alibaba.sdk.android.openaccount.callback.FailureCallback
            public void onFailure(int i, String str) {
                LoginCallback loginCallback = LoginActivity.this.getLoginCallback();
                if (loginCallback != null) {
                    loginCallback.onFailure(i, str);
                }
            }

            @Override // com.alibaba.sdk.android.openaccount.callback.LoginCallback
            public void onSuccess(OpenAccountSession openAccountSession) {
                LoginCallback loginCallback = LoginActivity.this.getLoginCallback();
                if (loginCallback != null) {
                    loginCallback.onSuccess(openAccountSession);
                }
                LoginActivity.this.finishWithoutCallback();
            }
        };
    }

    public void registerUser(View view2) {
        ((OpenAccountUIService) OpenAccountSDK.getService(OpenAccountUIService.class)).showRegister(this, getRegisterLoginCallback());
    }

    protected LoginCallback getRegisterLoginCallback() {
        return new LoginCallback() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.LoginActivity.14
            @Override // com.alibaba.sdk.android.openaccount.callback.FailureCallback
            public void onFailure(int i, String str) {
                LoginCallback loginCallback = LoginActivity.this.getLoginCallback();
                if (loginCallback != null) {
                    loginCallback.onFailure(i, str);
                }
            }

            @Override // com.alibaba.sdk.android.openaccount.callback.LoginCallback
            public void onSuccess(OpenAccountSession openAccountSession) {
                LoginCallback loginCallback = LoginActivity.this.getLoginCallback();
                if (loginCallback != null) {
                    loginCallback.onSuccess(openAccountSession);
                }
                LoginActivity.this.finishWithoutCallback();
            }
        };
    }

    @Override // android.app.Activity
    public void finish() {
        super.finish();
        LoginCallback loginCallback = getLoginCallback();
        if (loginCallback != null) {
            Message messageCreateMessage = MessageUtils.createMessage(10003, new Object[0]);
            loginCallback.onFailure(messageCreateMessage.code, messageCreateMessage.message);
        }
    }

    public void finishWithoutCallback() {
        super.finish();
    }

    protected LoginCallback getLoginCallback() {
        if (OpenAccountUIServiceImpl._loginCallback != null) {
            return OpenAccountUIServiceImpl._loginCallback;
        }
        return null;
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == RequestCode.NO_CAPTCHA_REQUEST_CODE && i2 == -1) {
            if (intent != null && "nocaptcha".equals(intent.getStringExtra(NativeProtocol.WEB_DIALOG_ACTION))) {
                String stringExtra = intent.getStringExtra("cSessionId");
                login(null, intent.getStringExtra("sig"), intent.getStringExtra("nocToken"), stringExtra);
            }
        } else if (i == RequestCode.RISK_IV_REQUEST_CODE && i2 == -1) {
            if (handleIVResult(intent)) {
                return;
            }
        } else {
            InputBoxWithHistory inputBoxWithHistory = this.loginIdEdit;
            if (inputBoxWithHistory != null && inputBoxWithHistory.getInputBoxWithClear().onActivityResult(i, i2, intent)) {
                return;
            }
        }
        OauthWidget oauthWidget = this.oauthWidget;
        if (oauthWidget == null || oauthWidget.getVisibility() != 0) {
            return;
        }
        this.oauthWidget.authorizeCallback(i, i2, intent);
    }

    protected boolean handleIVResult(Intent intent) {
        if (intent != null) {
            String stringExtra = intent.getStringExtra("havana_iv_token");
            String stringExtra2 = intent.getStringExtra("actionType");
            InputBoxWithHistory inputBoxWithHistory = this.loginIdEdit;
            if (inputBoxWithHistory != null && inputBoxWithHistory.getEditText() != null && this.loginIdEdit.getEditText().getText() != null) {
                String string = this.loginIdEdit.getEditText().getText().toString();
                if (string == null || string.length() <= 0 || TextUtils.isEmpty(stringExtra)) {
                    return true;
                }
                new IVTask(this, stringExtra, string, stringExtra2).execute(new Void[0]);
                return true;
            }
        }
        return false;
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        this.passwordEdit.getEditText().setText("");
        OpenAccountUIServiceImpl._loginCallback = null;
        if (OpenAccountSDK.getService(OauthService.class) != null) {
            ((OauthService) OpenAccountSDK.getService(OauthService.class)).cleanUp();
        }
    }

    protected class IVTask extends LoginByIVTokenTask {
        public IVTask(Activity activity2, String str, String str2, String str3) {
            super(activity2, str, str2, str3);
        }

        public IVTask(Activity activity2, String str, String str2, String str3, boolean z, LoginCallback loginCallback) {
            super(activity2, str, str2, str3, z, loginCallback);
        }

        @Override // com.alibaba.sdk.android.openaccount.ui.task.LoginByIVTokenTask
        protected void loginSuccess() {
            String loginId = LoginActivity.this.getLoginId();
            String string = LoginActivity.this.passwordEdit.getEditText().getText().toString();
            if (ConfigManager.getInstance().isSupportOfflineLogin()) {
                OpenAccountSDK.getSqliteUtil().saveToSqlite(loginId, string);
            }
            super.loginSuccess();
        }
    }
}
