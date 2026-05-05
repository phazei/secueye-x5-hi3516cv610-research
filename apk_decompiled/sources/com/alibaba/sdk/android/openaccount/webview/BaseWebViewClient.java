package com.alibaba.sdk.android.openaccount.webview;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.alibaba.sdk.android.openaccount.OpenAccountConfigs;
import com.alibaba.sdk.android.openaccount.OpenAccountConstants;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.trace.TraceLoggerManager;
import com.alibaba.sdk.android.openaccount.util.CommonUtils;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;

/* JADX INFO: loaded from: classes.dex */
public class BaseWebViewClient extends WebViewClient {
    private volatile long pageStartedTime;

    @Override // android.webkit.WebViewClient
    public void onReceivedError(WebView webView, int i, String str, String str2) {
        if (!CommonUtils.isNetworkAvailable()) {
            Toast.makeText(OpenAccountSDK.getAndroidContext(), ResourceUtils.getString("ali_sdk_openaccount_dynamic_network_not_available_message"), 0).show();
        }
        if (str2 == null || str2.length() <= 0) {
            return;
        }
        int iIndexOf = str2.indexOf(63);
        String strSubstring = iIndexOf > 0 ? str2.substring(0, iIndexOf) : str2;
        AliSDKLogger.d(OpenAccountConstants.LOG_TAG, "webview", str2, "failed");
        TraceLoggerManager.INSTANCE.action("webview", strSubstring).info("caseTime", Long.valueOf(System.currentTimeMillis() - this.pageStartedTime)).info("code", Integer.valueOf(i)).info(TmpConstant.SERVICE_DESC, str).failed();
    }

    @Override // android.webkit.WebViewClient
    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        this.pageStartedTime = System.currentTimeMillis();
    }

    @Override // android.webkit.WebViewClient
    public void onPageFinished(WebView webView, String str) {
        int iIndexOf = str.indexOf(63);
        String strSubstring = iIndexOf > 0 ? str.substring(0, iIndexOf) : str;
        AliSDKLogger.d(OpenAccountConstants.LOG_TAG, "webview", str, "success");
        TraceLoggerManager.INSTANCE.action("webview", strSubstring).info("caseTime", Long.valueOf(System.currentTimeMillis() - this.pageStartedTime)).success();
    }

    @Override // android.webkit.WebViewClient
    @TargetApi(14)
    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        if (sslErrorHandler == null) {
            return;
        }
        if (OpenAccountConfigs.ignoreWebViewSSLError) {
            AliSDKLogger.w(OpenAccountConstants.LOG_TAG, "ignoreWebViewSSLError IS ENABLED, WHICH MUST BE ONLY SET IN TEST ENVIRONMENT");
            sslErrorHandler.proceed();
        } else {
            sslErrorHandler.cancel();
        }
    }
}
