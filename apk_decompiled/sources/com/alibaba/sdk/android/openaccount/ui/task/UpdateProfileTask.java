package com.alibaba.sdk.android.openaccount.ui.task;

import android.app.Activity;
import android.content.Context;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.message.MessageConstants;
import com.alibaba.sdk.android.openaccount.message.MessageUtils;
import com.alibaba.sdk.android.openaccount.model.Result;
import com.alibaba.sdk.android.openaccount.model.User;
import com.alibaba.sdk.android.openaccount.session.SessionManagerService;
import com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountUIServiceImpl;
import com.alibaba.sdk.android.openaccount.util.CommonUtils;
import com.alibaba.sdk.android.openaccount.util.OpenAccountUtils;
import com.alibaba.sdk.android.openaccount.util.RpcUtils;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class UpdateProfileTask extends TaskWithToastMessage<JSONObject> {
    private Map<String, Object> profileParams;

    @Autowired
    protected SessionManagerService sessionManagerService;

    @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
    protected boolean toastMessageRequired(Result<JSONObject> result) {
        return false;
    }

    public UpdateProfileTask(Activity activity2, Map<String, Object> map) {
        super(activity2);
        this.profileParams = map;
    }

    public UpdateProfileTask(Context context, Map<String, Object> map) {
        super(context);
        this.profileParams = map;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
    public JSONObject parseData(JSONObject jSONObject) {
        return jSONObject.optJSONObject("openAccount");
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
    protected void doSuccessAfterToast(Result<JSONObject> result) {
        User userFromJSONObject = OpenAccountUtils.parseUserFromJSONObject(result.data);
        if (userFromJSONObject != null) {
            this.sessionManagerService.updateUser(userFromJSONObject);
            LoginCallback loginCallback = getLoginCallback();
            if (loginCallback != null) {
                loginCallback.onSuccess(this.sessionManagerService.getSession());
                return;
            }
            return;
        }
        CommonUtils.onFailure(getLoginCallback(), MessageUtils.createMessage(MessageConstants.GENERIC_SYSTEM_ERROR, new Object[0]));
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.task.TaskWithToastMessage
    protected void doFailAfterToast(Result<JSONObject> result) {
        if (getLoginCallback() != null) {
            getLoginCallback().onFailure(result.code, result.message);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
    public Result<JSONObject> asyncExecute(Void... voidArr) {
        HashMap map = new HashMap();
        map.put("appProfileMap", this.profileParams);
        return parseJsonResult(RpcUtils.invokeWithRiskControlInfo("accountProfileRequest", map, "updateaccountprofile"));
    }

    private LoginCallback getLoginCallback() {
        return OpenAccountUIServiceImpl._updateProfileCallback;
    }
}
