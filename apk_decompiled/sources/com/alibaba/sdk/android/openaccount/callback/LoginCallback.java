package com.alibaba.sdk.android.openaccount.callback;

import com.alibaba.sdk.android.openaccount.model.OpenAccountSession;

/* JADX INFO: loaded from: classes.dex */
public interface LoginCallback extends FailureCallback {
    void onSuccess(OpenAccountSession openAccountSession);
}
