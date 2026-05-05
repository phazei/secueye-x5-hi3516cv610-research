package com.alibaba.sdk.android.openaccount.webview.handler;

import android.webkit.WebView;

/* JADX INFO: loaded from: classes.dex */
public interface OverrideURLHandler {
    boolean handle(WebView webView, String str);

    boolean isURLSupported(String str);
}
