package com.alibaba.ailabs.tg.utils;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import com.alibaba.ailabs.tg.UtilsConfig;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes.dex */
public final class ToastUtils {
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());
    private static int sLayoutId = -1;
    private static Toast sToast;
    private static WeakReference<View> sViewWeakReference;

    private ToastUtils() {
    }

    public static void showShort(@NonNull CharSequence charSequence) {
        show(charSequence, 0);
    }

    public static void showShortInCenter(@NonNull CharSequence charSequence) {
        showInCenter(charSequence, 0);
    }

    public static void showShort(@StringRes int i) {
        show(i, 0);
    }

    public static void showShort(@StringRes int i, Object... objArr) {
        show(i, 0, objArr);
    }

    public static void showShort(String str, Object... objArr) {
        show(str, 0, objArr);
    }

    public static void showLong(@NonNull CharSequence charSequence) {
        show(charSequence, 1);
    }

    public static void showLong(@StringRes int i) {
        show(i, 1);
    }

    public static void showLong(@StringRes int i, Object... objArr) {
        show(i, 1, objArr);
    }

    public static void showLong(String str, Object... objArr) {
        show(str, 1, objArr);
    }

    public static View showCustomShort(@LayoutRes int i) {
        View view2 = getView(i);
        show(view2, 0);
        return view2;
    }

    public static View showCustomLong(@LayoutRes int i) {
        View view2 = getView(i);
        show(view2, 1);
        return view2;
    }

    public static void cancel() {
        Toast toast = sToast;
        if (toast != null) {
            toast.cancel();
            sToast = null;
        }
    }

    private static void show(@StringRes int i, int i2) {
        show(UtilsConfig.getInstance().getAppContext().getResources().getText(i).toString(), i2);
    }

    private static void show(@StringRes int i, int i2, Object... objArr) {
        show(String.format(UtilsConfig.getInstance().getAppContext().getResources().getString(i), objArr), i2);
    }

    private static void show(String str, int i, Object... objArr) {
        show(String.format(str, objArr), i);
    }

    private static boolean isBackground() {
        return AppUtils.isAppBackground(UtilsConfig.getInstance().getAppContext());
    }

    private static void show(final CharSequence charSequence, final int i) {
        if (isBackground()) {
            return;
        }
        HANDLER.post(new Runnable() { // from class: com.alibaba.ailabs.tg.utils.ToastUtils.1
            @Override // java.lang.Runnable
            public void run() {
                ToastUtils.cancel();
                Toast unused = ToastUtils.sToast = Toast.makeText(UtilsConfig.getInstance().getAppContext(), charSequence, i);
                ToastUtils.sToast.show();
            }
        });
    }

    private static void showInCenter(final CharSequence charSequence, final int i) {
        if (isBackground()) {
            return;
        }
        HANDLER.post(new Runnable() { // from class: com.alibaba.ailabs.tg.utils.ToastUtils.2
            @Override // java.lang.Runnable
            public void run() {
                ToastUtils.cancel();
                Toast unused = ToastUtils.sToast = Toast.makeText(UtilsConfig.getInstance().getAppContext(), charSequence, i);
                ToastUtils.sToast.setGravity(17, 0, 0);
                ToastUtils.sToast.show();
            }
        });
    }

    private static void show(final View view2, final int i) {
        if (isBackground()) {
            return;
        }
        HANDLER.post(new Runnable() { // from class: com.alibaba.ailabs.tg.utils.ToastUtils.3
            @Override // java.lang.Runnable
            public void run() {
                ToastUtils.cancel();
                Toast unused = ToastUtils.sToast = new Toast(UtilsConfig.getInstance().getAppContext());
                ToastUtils.sToast.setView(view2);
                ToastUtils.sToast.setDuration(i);
                ToastUtils.sToast.show();
            }
        });
    }

    private static View getView(@LayoutRes int i) {
        WeakReference<View> weakReference;
        View view2;
        if (sLayoutId == i && (weakReference = sViewWeakReference) != null && (view2 = weakReference.get()) != null) {
            return view2;
        }
        View viewInflate = ((LayoutInflater) UtilsConfig.getInstance().getAppContext().getSystemService("layout_inflater")).inflate(i, (ViewGroup) null);
        sViewWeakReference = new WeakReference<>(viewInflate);
        sLayoutId = i;
        return viewInflate;
    }
}
