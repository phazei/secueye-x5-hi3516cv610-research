package com.alibaba.sdk.android.openaccount.ui.util;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.widget.Toast;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;

/* JADX INFO: loaded from: classes.dex */
public class ToastUtils {
    private AlertDialog mAlertDialog;

    public static void toast(Context context, String str, int i) {
        if (i == 10019) {
            toastNetworkError(context);
            return;
        }
        Toast toastMakeText = Toast.makeText(context, str, 0);
        toastMakeText.setGravity(17, 0, 0);
        toastMakeText.setText(str);
        toastMakeText.show();
    }

    public static void toast(Context context, String str) {
        Toast toastMakeText = Toast.makeText(context, str, 0);
        toastMakeText.setGravity(17, 0, 0);
        toastMakeText.setText(str);
        toastMakeText.show();
    }

    public static void toastResource(Context context, String str) {
        Toast toastMakeText = Toast.makeText(context, ResourceUtils.getString(context, str), 0);
        toastMakeText.setGravity(17, 0, 0);
        toastMakeText.show();
    }

    public static void toastSystemError(Context context) {
        toast(context, ResourceUtils.getString(context, "ali_sdk_openaccount_dynamic_text_system_exception"));
    }

    public static void toastNetworkError(Context context) {
        toast(context, ResourceUtils.getString(context, "ali_sdk_openaccount_dynamic_text_network_exception"));
    }

    public static void toastOffLineError(Context context) {
        toast(context, ResourceUtils.getString(context, "ali_sdk_openaccount_dynamic_text_offline_exception"));
    }

    public void alert(final Activity activity2, final String str, final String str2, final String str3, final DialogInterface.OnClickListener onClickListener, final String str4, final DialogInterface.OnClickListener onClickListener2) {
        if (activity2 == null) {
            return;
        }
        dismissAlertDialog(activity2);
        activity2.runOnUiThread(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.ui.util.ToastUtils.1
            @Override // java.lang.Runnable
            public void run() {
                Activity activity3 = activity2;
                if (activity3 == null || activity3.isFinishing()) {
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(activity2, R.style.Theme.Holo.Light.Dialog));
                if (!TextUtils.isEmpty(str)) {
                    builder.setTitle(str);
                }
                if (!TextUtils.isEmpty(str2)) {
                    builder.setMessage(str2);
                }
                if (!TextUtils.isEmpty(str3)) {
                    builder.setPositiveButton(str3, onClickListener);
                }
                if (!TextUtils.isEmpty(str4)) {
                    builder.setNegativeButton(str4, onClickListener2);
                }
                ToastUtils.this.mAlertDialog = builder.show();
                ToastUtils.this.mAlertDialog.setCanceledOnTouchOutside(true);
            }
        });
    }

    public void dismissAlertDialog(Activity activity2) {
        AlertDialog alertDialog = this.mAlertDialog;
        if (alertDialog == null || !alertDialog.isShowing()) {
            return;
        }
        activity2.runOnUiThread(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.ui.util.ToastUtils.2
            @Override // java.lang.Runnable
            public void run() {
                if (ToastUtils.this.mAlertDialog == null || !ToastUtils.this.mAlertDialog.isShowing()) {
                    return;
                }
                try {
                    ToastUtils.this.mAlertDialog.dismiss();
                } catch (Exception unused) {
                } catch (Throwable th) {
                    ToastUtils.this.mAlertDialog = null;
                    throw th;
                }
                ToastUtils.this.mAlertDialog = null;
            }
        });
    }
}
