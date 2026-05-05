package com.alibaba.sdk.android.push.popup;

import android.content.Intent;

/* JADX INFO: loaded from: classes.dex */
public interface OnPushParseFailedListener {
    void onNotPushData(Intent intent);

    void onParseFailed(Intent intent);
}
