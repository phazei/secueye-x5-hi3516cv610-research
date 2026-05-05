package com.alibaba.sdk.android.openaccount.ui.task;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.config.ConfigService;
import com.alibaba.sdk.android.openaccount.model.LoginResult;
import com.alibaba.sdk.android.openaccount.model.Result;
import com.alibaba.sdk.android.openaccount.model.SessionData;
import com.alibaba.sdk.android.openaccount.session.SessionManagerService;
import com.alibaba.sdk.android.openaccount.task.TaskWithDialog;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConfigs;
import com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountTaobaoUIServiceImpl;
import com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountUIServiceImpl;
import com.alibaba.sdk.android.openaccount.ui.ui.LoginActivity;
import com.alibaba.sdk.android.openaccount.ui.ui.NoPasswordLoginActivity;
import com.alibaba.sdk.android.openaccount.ui.util.OpenAccountUIUtils;
import com.alibaba.sdk.android.openaccount.ui.util.ToastUtils;
import com.alibaba.sdk.android.openaccount.util.OpenAccountUtils;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;
import com.alibaba.sdk.android.openaccount.util.RpcUtils;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class LoginByIVTokenTask extends TaskWithDialog<Void, Void, Result<LoginResult>> {
    private String actionType;

    @Autowired
    private ConfigService configService;
    private String loginId;
    LoginCallback mLoginCallback;

    @Autowired
    private SessionManagerService sessionManagerService;
    private String token;

    public LoginByIVTokenTask(Activity activity2, String str, String str2, String str3) {
        super(activity2);
        this.token = str;
        this.loginId = str2;
        this.actionType = str3;
    }

    public LoginByIVTokenTask(Activity activity2, String str, String str2, String str3, boolean z, LoginCallback loginCallback) {
        super(activity2);
        this.token = str;
        this.loginId = str2;
        this.actionType = str3;
        this.showDialog = z;
        this.mLoginCallback = loginCallback;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
    public Result<LoginResult> asyncExecute(Void... voidArr) {
        HashMap map = new HashMap();
        if (!TextUtils.isEmpty(this.token) && !TextUtils.isEmpty(this.loginId) && !TextUtils.isEmpty(this.actionType)) {
            map.put("ivToken", this.token);
            map.put("loginId", this.loginId);
            map.put("actionType", this.actionType);
        }
        return OpenAccountUtils.toLoginResult(RpcUtils.pureInvokeWithRiskControlInfo("loginByIvTokenRequest", map, "loginbyivtoken"));
    }

    @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
    protected void doWhenException(Throwable th) {
        this.executorService.postUITask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.ui.task.LoginByIVTokenTask.1
            @Override // java.lang.Runnable
            public void run() {
                if (LoginByIVTokenTask.this.showDialog) {
                    ToastUtils.toastSystemError(LoginByIVTokenTask.this.context);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(final Result<LoginResult> result) {
        super.onPostExecute((Object) result);
        try {
            if (result == null) {
                if (this.showDialog) {
                    ToastUtils.toastSystemError(this.context);
                }
                callbackFailureTaobao();
                return;
            }
            if (result.isSuccess() && result.data != null && result.data.loginSuccessResult != null) {
                SessionData sessionDataCreateSessionDataFromLoginSuccessResult = OpenAccountUtils.createSessionDataFromLoginSuccessResult(result.data.loginSuccessResult);
                if (sessionDataCreateSessionDataFromLoginSuccessResult.scenario == null) {
                    sessionDataCreateSessionDataFromLoginSuccessResult.scenario = 1;
                }
                this.sessionManagerService.updateSession(sessionDataCreateSessionDataFromLoginSuccessResult);
                boolean zUpdateHistoryAccounts = OpenAccountUIUtils.updateHistoryAccounts(result.data.userInputName);
                if (this.context instanceof LoginActivity) {
                    if (!OpenAccountUIConfigs.AccountPasswordLoginFlow.showTipAlertAfterLogin) {
                        loginSuccessAction();
                        return;
                    } else if (!zUpdateHistoryAccounts) {
                        ((LoginActivity) this.context).runOnUiThread(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.ui.task.LoginByIVTokenTask.2
                            /* JADX WARN: Multi-variable type inference failed */
                            @Override // java.lang.Runnable
                            public void run() {
                                LoginByIVTokenTask.this.showTipDialog(String.format(ResourceUtils.getString(LoginByIVTokenTask.this.context.getApplicationContext(), "ali_sdk_openaccount_dynamic_text_alert_msg_after_login"), ((LoginResult) result.data).userInputName));
                            }
                        });
                        return;
                    } else {
                        loginSuccessAction();
                        return;
                    }
                }
                if (this.context instanceof NoPasswordLoginActivity) {
                    loginSuccess();
                    ((NoPasswordLoginActivity) this.context).finishWithoutCallback();
                    return;
                } else {
                    if (this.context instanceof Activity) {
                        loginSuccess();
                        callbackSuccessTaobao();
                        Activity activity2 = (Activity) this.context;
                        if (activity2 != null) {
                            activity2.finish();
                            return;
                        }
                        return;
                    }
                    loginSuccess();
                    return;
                }
            }
            callbackFailureTaobao();
            if (result.message != null && result.message.length() > 0) {
                if (this.showDialog) {
                    ToastUtils.toast(this.context, result.message, result.code);
                }
            } else if (this.showDialog) {
                ToastUtils.toastSystemError(this.context);
            }
        } catch (Throwable th) {
            th.printStackTrace();
            if (this.showDialog) {
                ToastUtils.toastSystemError(this.context);
            }
            callbackFailureTaobao();
        }
    }

    private void callbackSuccessTaobao() {
        if (!this.configService.openTaobaoUILogin() || OpenAccountTaobaoUIServiceImpl._preHandlerCallback == null || OpenAccountTaobaoUIServiceImpl._preHandlerCallback.get() == null) {
            return;
        }
        OpenAccountTaobaoUIServiceImpl._preHandlerCallback.get().onSuccess();
    }

    private void callbackFailureTaobao() {
        if (!this.configService.openTaobaoUILogin() || OpenAccountTaobaoUIServiceImpl._preHandlerCallback == null) {
            return;
        }
        OpenAccountTaobaoUIServiceImpl._preHandlerCallback.get().onFail(0, (Map) null);
    }

    protected void loginSuccessAction() {
        loginSuccess();
        if (this.context instanceof LoginActivity) {
            ((LoginActivity) this.context).finishWithoutCallback();
        }
    }

    protected void loginSuccess() {
        LoginCallback loginCallback = getLoginCallback();
        if (loginCallback != null) {
            loginCallback.onSuccess(this.sessionManagerService.getSession());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showTipDialog(String str) {
        if (this.context instanceof LoginActivity) {
            LoginActivity loginActivity = (LoginActivity) this.context;
            if (loginActivity.isFinishing()) {
                loginActivity.finishWithoutCallback();
            } else {
                new AlertDialog.Builder(this.context).setMessage(str).setPositiveButton(ResourceUtils.getString(this.context, "ali_sdk_openaccount_dynamic_text_iknow"), new DialogInterface.OnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.task.LoginByIVTokenTask.4
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LoginByIVTokenTask.this.loginSuccessAction();
                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.alibaba.sdk.android.openaccount.ui.task.LoginByIVTokenTask.3
                    @Override // android.content.DialogInterface.OnCancelListener
                    public void onCancel(DialogInterface dialogInterface) {
                        LoginByIVTokenTask.this.loginSuccessAction();
                    }
                }).show();
            }
        }
    }

    protected LoginCallback getLoginCallback() {
        LoginCallback loginCallback = this.mLoginCallback;
        if (loginCallback != null) {
            return loginCallback;
        }
        if (OpenAccountUIServiceImpl._loginCallback != null) {
            return OpenAccountUIServiceImpl._loginCallback;
        }
        return null;
    }
}
