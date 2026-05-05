package com.alibaba.sdk.android.openaccount;

import com.alibaba.sdk.android.openaccount.model.Result;

/* JADX INFO: loaded from: classes.dex */
public interface OpenAccountSessionService {
    Result<String> getSessionId();

    Result<String> refreshSession(boolean z);
}
