package com.alibaba.sdk.android.openaccount.ui.callback;

import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface OpenAccountExistCallback {
    void onFail(Map<String, String> map);

    void onSuccess();
}
