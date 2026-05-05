package com.alibaba.sdk.android.openaccount.webview.handler;

import android.webkit.WebView;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractOverrideUrlHandler implements OverrideURLHandler {
    private static final String TAG = "AbstractOverrideUrlHandler";
    private String targetUrl;
    private String[] targetUrls;

    public abstract boolean handleWithoutException(WebView webView, String str);

    public AbstractOverrideUrlHandler() {
    }

    public AbstractOverrideUrlHandler(String[] strArr) {
        this.targetUrls = strArr;
    }

    public AbstractOverrideUrlHandler(String str) {
        this.targetUrl = str;
    }

    @Override // com.alibaba.sdk.android.openaccount.webview.handler.OverrideURLHandler
    public boolean handle(WebView webView, String str) {
        try {
            return handleWithoutException(webView, str);
        } catch (Throwable th) {
            AliSDKLogger.e(TAG, th.getMessage(), th);
            return false;
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.webview.handler.OverrideURLHandler
    public boolean isURLSupported(String str) {
        if (str == null) {
            return false;
        }
        String[] strArr = this.targetUrls;
        if (strArr != null) {
            for (String str2 : strArr) {
                if (str.startsWith(str2)) {
                    return true;
                }
            }
            return false;
        }
        String str3 = this.targetUrl;
        if (str3 != null) {
            return str.startsWith(str3);
        }
        return false;
    }
}
