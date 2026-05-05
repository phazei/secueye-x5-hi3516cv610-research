package com.alibaba.sdk.android.openaccount.ui.widget;

import android.view.View;
import com.alibaba.sdk.android.openaccount.ui.util.ToastUtils;
import com.alibaba.sdk.android.openaccount.util.NetworkUtils;

/* JADX INFO: loaded from: classes.dex */
public abstract class NetworkCheckOnClickListener implements View.OnClickListener {
    public abstract void afterCheck(View view2);

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        if (NetworkUtils.isNetworkAvaiable(view2.getContext())) {
            afterCheck(view2);
        } else {
            ToastUtils.toastNetworkError(view2.getContext());
        }
    }
}
