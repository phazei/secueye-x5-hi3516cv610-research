package com.alibaba.sdk.android.openaccount.ui.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.ui.R;
import com.alibaba.sdk.android.openaccount.webview.BaseWebViewActivity;
import com.alibaba.sdk.android.openaccount.webview.BaseWebViewClient;
import com.alibaba.sdk.android.openaccount.webview.TaeWebView;
import com.alibaba.sdk.android.openaccount.webview.handler.AbstractOverrideUrlHandler;

/* JADX INFO: loaded from: classes.dex */
public class LoginDoubleCheckWebActivity extends BaseWebViewActivity {
    private String callbackUrl = "https://www.alipay.com/webviewbridge";
    protected boolean firstSSLAlert = true;
    protected boolean sslProceed = false;
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
    }

    @Override // com.alibaba.sdk.android.openaccount.webview.BaseWebViewActivity
    @SuppressLint({"NewApi"})
    protected WebViewClient createWebViewClient() {
        final DoubleCheckOverrideHandler doubleCheckOverrideHandler = new DoubleCheckOverrideHandler();
        return new BaseWebViewClient() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.LoginDoubleCheckWebActivity.1
            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                if (doubleCheckOverrideHandler.isURLSupported(str)) {
                    return doubleCheckOverrideHandler.handle(webView, str);
                }
                return false;
            }

            @Override // com.alibaba.sdk.android.openaccount.webview.BaseWebViewClient, android.webkit.WebViewClient
            public void onReceivedSslError(WebView webView, final SslErrorHandler sslErrorHandler, SslError sslError) {
                if (LoginDoubleCheckWebActivity.this.isUrlInWhiteList(sslError)) {
                    sslErrorHandler.proceed();
                    return;
                }
                if (!LoginDoubleCheckWebActivity.this.firstSSLAlert) {
                    if (LoginDoubleCheckWebActivity.this.sslProceed) {
                        sslErrorHandler.proceed();
                    } else {
                        super.onReceivedSslError(webView, sslErrorHandler, sslError);
                    }
                } else {
                    String string = webView.getContext().getResources().getString(R.string.ali_sdk_openaccount_ssl_error_title);
                    String string2 = webView.getContext().getResources().getString(R.string.ali_sdk_openaccount_ssl_error_info);
                    String string3 = webView.getContext().getResources().getString(R.string.ali_sdk_openaccount_confirm);
                    DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.LoginDoubleCheckWebActivity.1.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i) {
                            sslErrorHandler.proceed();
                            LoginDoubleCheckWebActivity.this.sslProceed = true;
                        }
                    };
                    String string4 = webView.getContext().getResources().getString(R.string.ali_sdk_openaccount_cancel);
                    DialogInterface.OnClickListener onClickListener2 = new DialogInterface.OnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.LoginDoubleCheckWebActivity.1.2
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i) {
                            sslErrorHandler.cancel();
                            LoginDoubleCheckWebActivity.this.sslProceed = false;
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());
                    builder.setPositiveButton(string3, onClickListener);
                    builder.setNeutralButton(string4, onClickListener2);
                    try {
                        AlertDialog alertDialogCreate = builder.create();
                        alertDialogCreate.setTitle(string);
                        alertDialogCreate.setMessage(string2);
                        alertDialogCreate.show();
                        LoginDoubleCheckWebActivity.this.firstSSLAlert = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                sslErrorHandler.proceed();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isUrlInWhiteList(SslError sslError) {
        if (sslError == null) {
            return true;
        }
        try {
            String url = sslError.getUrl();
            AliSDKLogger.d("checkWeb", url);
            if (url != null && !url.contains("alibaba.com") && !url.contains("aliapp.org") && !url.contains("mmstat.com")) {
                if (!url.contains("tbcdn.cn")) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            AliSDKLogger.e("checkWeb", "error:", e);
            return false;
        }
    }

    private class DoubleCheckOverrideHandler extends AbstractOverrideUrlHandler {
        private DoubleCheckOverrideHandler() {
        }

        @Override // com.alibaba.sdk.android.openaccount.webview.handler.AbstractOverrideUrlHandler, com.alibaba.sdk.android.openaccount.webview.handler.OverrideURLHandler
        public boolean isURLSupported(String str) {
            return str != null && str.startsWith(LoginDoubleCheckWebActivity.this.callbackUrl);
        }

        @Override // com.alibaba.sdk.android.openaccount.webview.handler.AbstractOverrideUrlHandler
        public boolean handleWithoutException(WebView webView, String str) {
            Activity activity2 = (Activity) webView.getContext();
            Bundle bundleSerialBundle = BaseWebViewActivity.serialBundle(Uri.parse(str).getQuery());
            if (!TextUtils.isEmpty(LoginDoubleCheckWebActivity.this.token)) {
                bundleSerialBundle.putString("nativeToken", LoginDoubleCheckWebActivity.this.token);
            }
            LoginDoubleCheckWebActivity loginDoubleCheckWebActivity = LoginDoubleCheckWebActivity.this;
            loginDoubleCheckWebActivity.setResult(-1, loginDoubleCheckWebActivity.getIntent().putExtras(bundleSerialBundle));
            activity2.finish();
            return true;
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.webview.BaseWebViewActivity
    protected TaeWebView createTaeWebView() {
        return new TaeWebView(this, false) { // from class: com.alibaba.sdk.android.openaccount.ui.ui.LoginDoubleCheckWebActivity.2
            @Override // com.alibaba.sdk.android.openaccount.webview.TaeWebView
            protected String normalizeURL(String str) {
                return str;
            }
        };
    }
}
