package tools;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import dialog.ProgressDialog;

/* JADX INFO: loaded from: classes4.dex */
public class ProgressDialogUtil {
    private ProgressDialog progressDialog;

    public void showProgressDialog(Activity activity2) {
        showProgressDialog(activity2, (String) null);
    }

    public void showProgressDialog(Activity activity2, int i) {
        if (activity2 != null) {
            showProgressDialog(activity2, activity2.getString(i));
        }
    }

    public void showProgressDialog(Activity activity2, String str) {
        if (activity2 == null || activity2.isFinishing()) {
            return;
        }
        if (this.progressDialog == null) {
            this.progressDialog = new ProgressDialog(activity2);
            this.progressDialog.setCanceledOnTouchOutside(false);
            this.progressDialog.setCancelable(false);
        }
        this.progressDialog.setMessage(str);
        this.progressDialog.show();
    }

    public void dismissProgressDialog(Activity activity2) {
        ProgressDialog progressDialog = this.progressDialog;
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                Context baseContext = ((ContextWrapper) this.progressDialog.getContext()).getBaseContext();
                if (baseContext instanceof Activity) {
                    Activity activity3 = (Activity) baseContext;
                    if (!activity3.isFinishing() && !activity3.isDestroyed()) {
                        this.progressDialog.dismiss();
                    }
                } else {
                    this.progressDialog.dismiss();
                }
            }
            this.progressDialog = null;
        }
    }
}
