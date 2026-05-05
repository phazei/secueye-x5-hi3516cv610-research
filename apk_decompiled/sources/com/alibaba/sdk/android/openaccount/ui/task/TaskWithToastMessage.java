package com.alibaba.sdk.android.openaccount.ui.task;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.alibaba.sdk.android.openaccount.model.Result;
import com.alibaba.sdk.android.openaccount.task.TaskWithDialog;
import com.alibaba.sdk.android.openaccount.ui.util.ToastUtils;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class TaskWithToastMessage<T> extends TaskWithDialog<Void, Void, Result<T>> {
    protected abstract void doFailAfterToast(Result<T> result);

    protected abstract void doSuccessAfterToast(Result<T> result);

    @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
    protected void doWhenException(Throwable th) {
    }

    protected abstract T parseData(JSONObject jSONObject);

    public TaskWithToastMessage(Activity activity2) {
        super(activity2);
        this.showDialog = true;
    }

    public TaskWithToastMessage(Context context) {
        super(context);
        this.showDialog = true;
    }

    protected Result<T> parseJsonResult(Result<JSONObject> result) {
        if (result.data == null) {
            return Result.result(result.code, result.message);
        }
        return Result.result(result.code, result.message, parseData(result.data));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(Result<T> result) {
        super.onPostExecute((Object) result);
        try {
            if (result == null) {
                if (this.showDialog) {
                    ToastUtils.toastSystemError(this.context);
                }
            } else {
                if (result.code == 1) {
                    doSuccessAfterToast(result);
                    return;
                }
                if (toastMessageRequired(result)) {
                    ToastUtils.toast(this.context, result.message, result.code);
                }
                doFailAfterToast(result);
            }
        } catch (Throwable th) {
            Log.e("TaskWithToastMessage", "after post execute error", th);
            ToastUtils.toastSystemError(this.context);
        }
    }

    protected boolean toastMessageRequired(Result<T> result) {
        return this.showDialog;
    }

    @Override // com.alibaba.sdk.android.openaccount.task.TaskWithDialog
    protected boolean needProgressDialog() {
        return this.showDialog;
    }
}
