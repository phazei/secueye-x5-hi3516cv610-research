package com.alibaba.sdk.android.openaccount.task;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import com.alibaba.sdk.android.openaccount.OpenAccountConstants;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;
import com.alibaba.sdk.android.openaccount.widget.ProgressDialog;
import com.alibaba.sdk.android.pluto.Pluto;

/* JADX INFO: loaded from: classes.dex */
public abstract class TaskWithDialog<Params, Progress, Result> extends AbsAsyncTask<Params, Progress, Result> {
    protected Context context;
    protected ProgressDialog progressDialog;
    protected boolean showDialog = true;
    protected boolean showToast = true;
    protected boolean doFinally = false;

    public TaskWithDialog(Context context) {
        this.context = context;
        Pluto.DEFAULT_INSTANCE.inject(this);
    }

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        if (this.context != null) {
            showProgressDialog(ResourceUtils.getString("ali_sdk_openaccount_dynamic_loading_progress_message"), true, null, true);
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
    protected void doFinally() {
        this.doFinally = true;
        if (this.progressDialog != null) {
            dismissProgressDialog();
        }
    }

    public void showProgressDialog(final String str, final boolean z, final DialogInterface.OnCancelListener onCancelListener, final boolean z2) {
        dismissProgressDialog();
        this.executorService.postUITask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.task.TaskWithDialog.1
            @Override // java.lang.Runnable
            public void run() {
                if (TaskWithDialog.this.context != null && (TaskWithDialog.this.context instanceof Activity)) {
                    if (((TaskWithDialog.this.context instanceof Activity) && ((Activity) TaskWithDialog.this.context).isFinishing()) || TaskWithDialog.this.doFinally || !TaskWithDialog.this.needProgressDialog()) {
                        return;
                    }
                    TaskWithDialog taskWithDialog = TaskWithDialog.this;
                    taskWithDialog.progressDialog = new ProgressDialog(taskWithDialog.context);
                    TaskWithDialog.this.progressDialog.setMessage(str);
                    TaskWithDialog.this.progressDialog.setProgressVisiable(z2);
                    TaskWithDialog.this.progressDialog.setCancelable(z);
                    TaskWithDialog.this.progressDialog.setOnCancelListener(onCancelListener);
                    TaskWithDialog.this.progressDialog.show();
                    TaskWithDialog.this.progressDialog.setCanceledOnTouchOutside(false);
                }
            }
        });
    }

    public void dismissProgressDialog() {
        this.executorService.postUITask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.task.TaskWithDialog.2
            @Override // java.lang.Runnable
            public void run() {
                if (TaskWithDialog.this.progressDialog == null || !TaskWithDialog.this.progressDialog.isShowing()) {
                    return;
                }
                try {
                    try {
                        TaskWithDialog.this.progressDialog.dismiss();
                    } catch (Exception e) {
                        AliSDKLogger.e(OpenAccountConstants.LOG_TAG, e.getMessage(), e);
                    }
                } finally {
                    TaskWithDialog.this.progressDialog = null;
                }
            }
        });
    }

    protected boolean needProgressDialog() {
        return this.showDialog;
    }
}
