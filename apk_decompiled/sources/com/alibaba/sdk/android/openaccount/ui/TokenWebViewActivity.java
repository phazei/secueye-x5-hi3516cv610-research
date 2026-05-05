package com.alibaba.sdk.android.openaccount.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.alibaba.sdk.android.openaccount.OpenAccountConstants;
import com.alibaba.sdk.android.openaccount.message.MessageConstants;
import com.alibaba.sdk.android.openaccount.message.MessageUtils;
import com.alibaba.sdk.android.openaccount.task.CheckHidBindedTask;
import com.alibaba.sdk.android.openaccount.task.SSOTaoProvider;
import com.alibaba.sdk.android.openaccount.util.CommonUtils;
import com.alibaba.sdk.android.openaccount.webview.BaseWebViewActivity;
import com.alibaba.sdk.android.openaccount.webview.BaseWebViewClient;
import com.alibaba.sdk.android.openaccount.webview.TaeWebView;
import com.alibaba.sdk.android.openaccount.webview.handler.AbstractOverrideUrlHandler;

/* JADX INFO: loaded from: classes.dex */
public class TokenWebViewActivity extends BaseWebViewActivity {
    private String callbackUrl = "https://www.alipay.com/webviewbridge";
    private Context context;
    private Long havanaid;
    private String token;

    @Override // com.alibaba.sdk.android.openaccount.webview.BaseWebViewActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.context = this;
        if (bundle != null) {
            this.callbackUrl = bundle.getString("callback");
            this.token = bundle.getString("token");
            this.havanaid = Long.valueOf(bundle.getLong(OpenAccountConstants.HAVANA_ID));
        }
        if (TextUtils.isEmpty(this.callbackUrl)) {
            this.callbackUrl = getIntent().getStringExtra("callback");
        }
        if (TextUtils.isEmpty(this.token)) {
            this.token = getIntent().getStringExtra("token");
        }
        Long l = this.havanaid;
        if (l == null || 0 == l.longValue()) {
            this.havanaid = Long.valueOf(getIntent().getLongExtra(OpenAccountConstants.HAVANA_ID, 0L));
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.webview.BaseWebViewActivity
    @SuppressLint({"NewApi"})
    protected WebViewClient createWebViewClient() {
        final IVOverrideHandler iVOverrideHandler = new IVOverrideHandler();
        return new BaseWebViewClient() { // from class: com.alibaba.sdk.android.openaccount.ui.TokenWebViewActivity.1
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
            return str != null && str.startsWith(TokenWebViewActivity.this.callbackUrl);
        }

        @Override // com.alibaba.sdk.android.openaccount.webview.handler.AbstractOverrideUrlHandler
        public boolean handleWithoutException(WebView webView, String str) {
            Activity activity2 = (Activity) webView.getContext();
            Bundle bundleSerialBundle = BaseWebViewActivity.serialBundle(Uri.parse(str).getQuery());
            if (bundleSerialBundle != null && !TextUtils.isEmpty(bundleSerialBundle.getString("havana_iv_token"))) {
                new CheckHidBindedTask(TokenWebViewActivity.this.context, TokenWebViewActivity.this.havanaid, bundleSerialBundle.getString("havana_iv_token"), bundleSerialBundle.getString("actionType"), SSOTaoProvider._checkBindedHidCallback).execute(new Void[0]);
            } else {
                CommonUtils.onFailure(SSOTaoProvider._checkBindedHidCallback, MessageUtils.createMessage(MessageConstants.USER_TOKEN_LOGIN_FAIL, new Object[0]));
            }
            activity2.finish();
            return true;
        }
    }

    private void clickBack() {
        CommonUtils.onFailure(SSOTaoProvider._checkBindedHidCallback, MessageUtils.createMessage(10003, new Object[0]));
        finish();
    }

    @Override // com.alibaba.sdk.android.openaccount.webview.BaseWebViewActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        clickBack();
    }

    @Override // com.alibaba.sdk.android.openaccount.webview.BaseWebViewActivity
    protected TaeWebView createTaeWebView() {
        return new TaeWebView(this, false) { // from class: com.alibaba.sdk.android.openaccount.ui.TokenWebViewActivity.2
            @Override // com.alibaba.sdk.android.openaccount.webview.TaeWebView
            protected String normalizeURL(String str) {
                return str;
            }
        };
    }
}
