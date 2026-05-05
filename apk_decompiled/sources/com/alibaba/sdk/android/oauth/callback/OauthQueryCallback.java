package com.alibaba.sdk.android.oauth.callback;

import com.alibaba.sdk.android.openaccount.callback.FailureCallback;
import com.alibaba.sdk.android.openaccount.model.OpenAccountLink;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface OauthQueryCallback extends FailureCallback {
    void onSuccess(List<OpenAccountLink> list);
}
