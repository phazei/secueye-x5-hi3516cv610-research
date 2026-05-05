package com.alibaba.sdk.android.openaccount.ui.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.executor.ExecutorService;
import com.alibaba.sdk.android.openaccount.model.CheckCodeResult;
import com.alibaba.sdk.android.openaccount.model.Result;
import com.alibaba.sdk.android.openaccount.session.SessionManagerService;
import com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountUIServiceImpl;
import com.alibaba.sdk.android.openaccount.ui.model.SmsActionType;
import com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage;
import com.alibaba.sdk.android.openaccount.ui.ui.BaseAppCompatActivity;
import com.alibaba.sdk.android.openaccount.util.OpenAccountRiskControlContext;
import com.alibaba.sdk.android.openaccount.util.RpcUtils;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.taobao.accs.utl.UtilityImpl;
import java.lang.reflect.Field;
import java.util.HashMap;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseFragment extends Fragment {
    protected Activity mActivity;
    protected BaseAppCompatActivity mAttachedActivity;

    public interface BeforeJumpToPwdCallback {
        void onFinish();
    }

    protected String getLocationCode() {
        return "86";
    }

    protected String getMobile() {
        return "";
    }

    protected void onFail(Throwable th) {
    }

    protected void onSendSMSForRegisterSuccess(Result<CheckCodeResult> result) {
    }

    protected void onSendSMSForResetPwdSuccess(Result<Void> result) {
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Activity activity2) {
        super.onAttach(activity2);
        this.mActivity = activity2;
        if (activity2 instanceof BaseAppCompatActivity) {
            this.mAttachedActivity = (BaseAppCompatActivity) activity2;
        }
    }

    protected void onBackPressed() {
        BaseAppCompatActivity baseAppCompatActivity = this.mAttachedActivity;
        if (baseAppCompatActivity != null) {
            baseAppCompatActivity.onBackPressed();
        }
    }

    protected class SendSMSForResetPwdTask extends TaskWithToastMessage<Void> {
        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected void doFailAfterToast(Result<Void> result) {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        public Void parseData(JSONObject jSONObject) {
            return null;
        }

        public SendSMSForResetPwdTask(Activity activity2) {
            super(activity2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        public Result<Void> asyncExecute(Void... voidArr) {
            HashMap map = new HashMap();
            map.put(UtilityImpl.NET_TYPE_MOBILE, BaseFragment.this.getMobile());
            map.put(AlinkConstants.KEY_MOBILE_LOCATION_CODE, BaseFragment.this.getLocationCode());
            map.put("riskControlInfo", OpenAccountRiskControlContext.buildRiskContext());
            return parseJsonResult(RpcUtils.invokeWithRiskControlInfo("sendSmsCodeForResetPasswordRequest", map, "sendsmscodeforresetpassword"));
        }

        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected void doSuccessAfterToast(Result<Void> result) {
            BaseFragment.this.onSendSMSForResetPwdSuccess(result);
        }
    }

    protected class SendSmsCodeForRegisterTask extends TaskWithToastMessage<CheckCodeResult> {
        public SendSmsCodeForRegisterTask(Activity activity2) {
            super(activity2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        public Result<CheckCodeResult> asyncExecute(Void... voidArr) {
            HashMap map = new HashMap();
            map.put(UtilityImpl.NET_TYPE_MOBILE, BaseFragment.this.getMobile());
            map.put(AlinkConstants.KEY_MOBILE_LOCATION_CODE, BaseFragment.this.getLocationCode());
            map.put("smsActionType", SmsActionType.SDK_ACCOUNT_REGISTER);
            return parseJsonResult(RpcUtils.invokeWithRiskControlInfo("sendSmsCodeRequest", map, "sendsmscode"));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        public CheckCodeResult parseData(JSONObject jSONObject) {
            CheckCodeResult checkCodeResult = new CheckCodeResult();
            checkCodeResult.checkCodeId = jSONObject.optString("checkCodeId");
            checkCodeResult.checkCodeUrl = jSONObject.optString("checkCodeUrl");
            checkCodeResult.clientVerifyData = jSONObject.optString("clientVerifyData");
            return checkCodeResult;
        }

        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected void doSuccessAfterToast(Result<CheckCodeResult> result) {
            BaseFragment.this.onSendSMSForRegisterSuccess(result);
        }

        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected void doFailAfterToast(Result<CheckCodeResult> result) {
            int i = result.code;
        }

        @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
        protected boolean toastMessageRequired(Result<CheckCodeResult> result) {
            return result.code != 26053;
        }
    }

    protected void successCallback() {
        ((ExecutorService) OpenAccountSDK.getService(ExecutorService.class)).postUITask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.fragment.BaseFragment.1
            @Override // java.lang.Runnable
            public void run() {
                LoginCallback loginCallback = BaseFragment.this.getLoginCallback();
                if (loginCallback != null) {
                    loginCallback.onSuccess(((SessionManagerService) OpenAccountSDK.getService(SessionManagerService.class)).getSession());
                    if (BaseFragment.this.mAttachedActivity != null) {
                        BaseFragment.this.mAttachedActivity.finish();
                    }
                }
            }
        });
    }

    protected LoginCallback getLoginCallback() {
        if (OpenAccountUIServiceImpl._specialLoginCallback != null) {
            return OpenAccountUIServiceImpl._specialLoginCallback;
        }
        return null;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        try {
            Field declaredField = Fragment.class.getDeclaredField("mChildFragmentManager");
            declaredField.setAccessible(true);
            declaredField.set(this, null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e2) {
            throw new RuntimeException(e2);
        }
    }
}
