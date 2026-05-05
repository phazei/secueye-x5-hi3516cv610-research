package com.alibaba.sdk.android.openaccount.ui.task;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.alibaba.sdk.android.openaccount.model.CheckOAExistResult;
import com.alibaba.sdk.android.openaccount.model.Result;
import com.alibaba.sdk.android.openaccount.task.TaskWithDialog;
import com.alibaba.sdk.android.openaccount.ui.RequestCode;
import com.alibaba.sdk.android.openaccount.ui.callback.OpenAccountExistCallback;
import com.alibaba.sdk.android.openaccount.ui.model.CaptchaModel;
import com.alibaba.sdk.android.openaccount.ui.ui.LoginDoubleCheckWebActivity;
import com.alibaba.sdk.android.openaccount.ui.util.ToastUtils;
import com.alibaba.sdk.android.openaccount.util.OpenAccountUtils;
import com.alibaba.sdk.android.openaccount.util.RpcUtils;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class CheckOpenAccountExistTask extends TaskWithDialog<Void, Void, Result<CheckOAExistResult>> {
    private Activity mActivity;
    private CaptchaModel mCaptchaModel;
    private String mLoginId;
    private OpenAccountExistCallback mOpenAccountExistCallback;

    public CheckOpenAccountExistTask(Activity activity2, boolean z, String str, CaptchaModel captchaModel, OpenAccountExistCallback openAccountExistCallback) {
        super(activity2);
        this.showDialog = z;
        this.mLoginId = str;
        this.mCaptchaModel = captchaModel;
        this.mOpenAccountExistCallback = openAccountExistCallback;
        this.mActivity = activity2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
    public Result<CheckOAExistResult> asyncExecute(Void... voidArr) {
        HashMap map = new HashMap();
        if (!TextUtils.isEmpty(this.mLoginId)) {
            map.put("loginId", this.mLoginId);
        }
        CaptchaModel captchaModel = this.mCaptchaModel;
        if (captchaModel != null) {
            if (!TextUtils.isEmpty(captchaModel.sig)) {
                map.put("sig", this.mCaptchaModel.sig);
            }
            if (!TextUtils.isEmpty(this.mCaptchaModel.csessionid)) {
                map.put("csessionid", this.mCaptchaModel.csessionid);
            }
            if (!TextUtils.isEmpty(this.mCaptchaModel.nctoken)) {
                map.put("nctoken", this.mCaptchaModel.nctoken);
            }
        }
        return OpenAccountUtils.parseCheckOAResult(RpcUtils.pureInvokeWithRiskControlInfo("checkAccountExistRequest", map, "checkaccountexist"));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(Result<CheckOAExistResult> result) {
        super.onPostExecute((Object) result);
        try {
            if (result == null) {
                if (this.showDialog) {
                    ToastUtils.toastSystemError(this.context);
                }
                handleFail(null);
                return;
            }
            int i = result.code;
            if (i == 1) {
                if (result.data != null) {
                    if (result.data.accountExist) {
                        handleSuccess();
                        return;
                    } else {
                        if (result.data.havanaExist) {
                            HashMap map = new HashMap();
                            map.put("havanaid", result.data.havanaId);
                            handleFail(map);
                            return;
                        }
                        ToastUtils.toastResource(this.context, "ali_sdk_openaccount_text_account_nonexist");
                        return;
                    }
                }
                handleFail(null);
                return;
            }
            if (i == 26053) {
                if (result.data == null || result.data.checkCodeResult == null || TextUtils.isEmpty(result.data.checkCodeResult.clientVerifyData)) {
                    return;
                }
                Uri.Builder builderBuildUpon = Uri.parse(result.data.checkCodeResult.clientVerifyData).buildUpon();
                builderBuildUpon.appendQueryParameter("callback", "https://www.alipay.com/webviewbridge");
                Intent intent = new Intent(this.mActivity, (Class<?>) LoginDoubleCheckWebActivity.class);
                intent.putExtra("url", builderBuildUpon.toString());
                intent.putExtra("title", result.message);
                intent.putExtra("callback", "https://www.alipay.com/webviewbridge");
                this.mActivity.startActivityForResult(intent, RequestCode.NO_CAPTCHA_REQUEST_CODE);
                return;
            }
            if (this.showDialog) {
                if (TextUtils.isEmpty(result.message)) {
                    ToastUtils.toastSystemError(this.context);
                } else {
                    ToastUtils.toast(this.context, result.message, result.code);
                }
            }
            handleFail(null);
        } catch (Throwable th) {
            th.printStackTrace();
            handleFail(null);
        }
    }

    private void handleSuccess() {
        if (getCallback() != null) {
            getCallback().onSuccess();
        }
        Activity activity2 = this.mActivity;
        if (activity2 != null) {
            activity2.finish();
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
    protected void doWhenException(Throwable th) {
        handleFail(null);
    }

    private void handleFail(Map<String, String> map) {
        if (getCallback() != null) {
            getCallback().onFail(map);
        }
    }

    private OpenAccountExistCallback getCallback() {
        return this.mOpenAccountExistCallback;
    }
}
