package com.alibaba.sdk.android.openaccount.ui.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.alibaba.sdk.android.openaccount.ui.bridge.LoginBridge;
import com.alibaba.sdk.android.openaccount.webview.BaseWebViewActivity;
import com.alibaba.sdk.android.openaccount.webview.BaseWebViewClient;
import com.alibaba.sdk.android.openaccount.webview.TaeWebView;
import com.alibaba.sdk.android.openaccount.webview.handler.AbstractOverrideUrlHandler;

/* JADX INFO: loaded from: classes.dex */
public class LoginIVWebActivity extends BaseWebViewActivity {
    private String callbackUrl = "https://www.alipay.com/webviewbridge";
    private String token;

    @Override // com.alibaba.sdk.android.openaccount.webview.BaseWebViewActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.callbackUrl = bundle.getString("callback");
            this.token = bundle.getString("token");
        }
        if (TextUtils.isEmpty(this.callbackUrl)) {
            this.callbackUrl = getIntent().getStringExtra("callback");
        }
        if (TextUtils.isEmpty(this.token)) {
            this.token = getIntent().getStringExtra("token");
        }
        this.taeWebView.addBridgeObject(OpenAccountUIConstants.loginBridgeName, new LoginBridge());
    }

    @Override // com.alibaba.sdk.android.openaccount.webview.BaseWebViewActivity
    @SuppressLint({"NewApi"})
    protected WebViewClient createWebViewClient() {
        final IVOverrideHandler iVOverrideHandler = new IVOverrideHandler();
        return new BaseWebViewClient() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.LoginIVWebActivity.1
            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                if (iVOverrideHandler.isURLSupported(str)) {
                    return iVOverrideHandler.handle(webView, str);
                }
                return false;
            }
        };
    }

    private class IVOverrideHandler extends AbstractOverrideUrlHandler {
        private IVOverrideHandler() {
        }

        @Override // com.alibaba.sdk.android.openaccount.webview.handler.AbstractOverrideUrlHandler, com.alibaba.sdk.android.openaccount.webview.handler.OverrideURLHandler
        public boolean isURLSupported(String str) {
            return str != null && str.startsWith(LoginIVWebActivity.this.callbackUrl);
        }

        @Override // com.alibaba.sdk.android.openaccount.webview.handler.AbstractOverrideUrlHandler
        public boolean handleWithoutException(WebView webView, String str) {
            Activity activity2 = (Activity) webView.getContext();
            Bundle bundleSerialBundle = BaseWebViewActivity.serialBundle(Uri.parse(str).getQuery());
            LoginIVWebActivity loginIVWebActivity = LoginIVWebActivity.this;
            loginIVWebActivity.setResult(-1, loginIVWebActivity.getIntent().putExtras(bundleSerialBundle));
            activity2.finish();
            return true;
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.webview.BaseWebViewActivity
    protected TaeWebView createTaeWebView() {
        return new TaeWebView(this, false) { // from class: com.alibaba.sdk.android.openaccount.ui.ui.LoginIVWebActivity.2
            @Override // com.alibaba.sdk.android.openaccount.webview.TaeWebView
            protected String normalizeURL(String str) {
                return str;
            }
        };
    }
}
