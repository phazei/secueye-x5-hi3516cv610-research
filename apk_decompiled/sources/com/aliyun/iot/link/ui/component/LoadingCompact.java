package com.aliyun.iot.link.ui.component;

import android.graphics.Color;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.aliyun.iot.link.ui.component.statusview.AbstractStatusView;
import com.aliyun.iot.link.ui.component.statusview.LinkLoadingStatusFragment;

/* JADX INFO: loaded from: classes2.dex */
public class LoadingCompact {
    public static void showLoading(FragmentActivity fragmentActivity) {
        showLoading(fragmentActivity, true);
    }

    public static void showLoading(FragmentActivity fragmentActivity, int i) {
        LinkLoadingStatusFragment.show(fragmentActivity, 0, android.R.id.content, i, false, true);
    }

    public static void showLoading(final FragmentActivity fragmentActivity, final boolean z) {
        if (fragmentActivity == null) {
            return;
        }
        fragmentActivity.runOnUiThread(new Runnable() { // from class: com.aliyun.iot.link.ui.component.LoadingCompact.1
            @Override // java.lang.Runnable
            public void run() {
                LinkLoadingStatusFragment.show(fragmentActivity, 0, android.R.id.content, Color.parseColor("#0079ff"), false, z);
            }
        });
    }

    public static void showLoading(final FragmentActivity fragmentActivity, @StringRes final int i, @IdRes final int i2, final boolean z, final boolean z2, @Nullable final AbstractStatusView.OnCancelListener onCancelListener) {
        fragmentActivity.runOnUiThread(new Runnable() { // from class: com.aliyun.iot.link.ui.component.LoadingCompact.2
            @Override // java.lang.Runnable
            public void run() {
                final FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
                if (!z2 && z && onCancelListener != null) {
                    supportFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() { // from class: com.aliyun.iot.link.ui.component.LoadingCompact.2.1
                        @Override // androidx.fragment.app.FragmentManager.OnBackStackChangedListener
                        public void onBackStackChanged() {
                            supportFragmentManager.removeOnBackStackChangedListener(this);
                            if (supportFragmentManager.findFragmentByTag(LinkLoadingStatusFragment.TAG) == null) {
                                onCancelListener.onCanceled();
                            }
                        }
                    });
                }
                LinkLoadingStatusFragment.show(fragmentActivity, i, i2, Color.parseColor("#0079ff"), z, z2);
            }
        });
    }

    public static void dismissLoading(FragmentActivity fragmentActivity) {
        dismissLoading(fragmentActivity, true);
    }

    public static void dismissLoading(final FragmentActivity fragmentActivity, final boolean z) {
        fragmentActivity.runOnUiThread(new Runnable() { // from class: com.aliyun.iot.link.ui.component.LoadingCompact.3
            @Override // java.lang.Runnable
            public void run() {
                FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
                Fragment fragmentFindFragmentByTag = supportFragmentManager.findFragmentByTag(LinkLoadingStatusFragment.TAG);
                if (fragmentFindFragmentByTag != null) {
                    FragmentTransaction fragmentTransactionRemove = supportFragmentManager.beginTransaction().remove(fragmentFindFragmentByTag);
                    if (z) {
                        fragmentTransactionRemove.commitAllowingStateLoss();
                    } else {
                        fragmentTransactionRemove.commit();
                    }
                }
            }
        });
    }
}
