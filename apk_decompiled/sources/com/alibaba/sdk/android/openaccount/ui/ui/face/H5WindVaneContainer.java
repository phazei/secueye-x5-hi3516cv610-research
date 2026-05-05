package com.alibaba.sdk.android.openaccount.ui.ui.face;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.taobao.windvane.extra.uc.WVUCWebChromeClient;
import android.taobao.windvane.extra.uc.WVUCWebView;
import android.taobao.windvane.extra.uc.WVUCWebViewClient;
import android.taobao.windvane.jsbridge.WVPluginManager;
import android.text.TextUtils;
import android.widget.RelativeLayout;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.OpenAccountService;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.model.OpenAccountSession;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIService;
import com.alibaba.sdk.android.openaccount.ui.R;
import com.alibaba.sdk.android.openaccount.ui.bridge.WVJSBridge;
import com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountFaceLoginServiceImpl;
import com.alibaba.sdk.android.openaccount.ui.ui.BaseAppCompatActivity;
import com.facebook.internal.NativeProtocol;
import com.huawei.hms.framework.common.ContainerUtils;
import com.uc.webview.export.JsPromptResult;
import com.uc.webview.export.SslErrorHandler;
import com.uc.webview.export.WebSettings;
import com.uc.webview.export.WebView;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public class H5WindVaneContainer extends BaseAppCompatActivity {
    protected static final String CALLBACK = "https://www.alipay.com/webviewbridge";
    public boolean canReceiveTitle;
    protected RelativeLayout mAPRelativeLayout;
    protected Activity mActivity;
    protected Context mContext;
    protected String mUrl;
    protected WVUCWebView mWebView;
    protected boolean firstAlert = true;
    protected boolean proceed = false;

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        this.mContext = this;
        this.mActivity = this;
        initParams(bundle);
        registerPlugin();
        super.onCreate(bundle);
    }

    protected void initParams(Bundle bundle) {
        String string = null;
        if (bundle != null) {
            try {
                string = bundle.getString("title");
                this.mUrl = bundle.getString("url");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        if (TextUtils.isEmpty(string)) {
            string = getIntent().getStringExtra("title");
        }
        if (TextUtils.isEmpty(string)) {
            this.canReceiveTitle = true;
        } else {
            this.canReceiveTitle = false;
            this.mToolBar.setTitle(string);
        }
        if (TextUtils.isEmpty(this.mUrl)) {
            this.mUrl = getIntent().getStringExtra("url");
        }
    }

    protected void registerPlugin() {
        try {
            WVPluginManager.registerPlugin("OAWVPlugin", WVJSBridge.class);
        } catch (Throwable unused) {
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.BaseAppCompatActivity
    protected int getLayoutContent() {
        return R.layout.ali_sdk_openaccount_basewebview;
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.BaseAppCompatActivity
    protected void initViews() {
        super.initViews();
        this.mAPRelativeLayout = (RelativeLayout) findViewById(R.id.aliuser_webview_contain);
        createWebView();
        init();
    }

    protected void init() {
        this.mAPRelativeLayout.addView(this.mWebView);
        setWebView();
        setWebChromeClinet();
        setWebViewClient();
        webViewLoadUrl(this.mUrl);
    }

    protected void createWebView() {
        try {
            this.mWebView = new WVUCWebView(this);
        } catch (Exception e) {
            e.printStackTrace();
            finishCurrentActivity();
        }
        if (this.mWebView == null) {
            finishCurrentActivity();
        }
        this.mWebView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
    }

    protected void webViewLoadUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.mWebView.loadUrl(str);
    }

    protected void setWebView() {
        this.mWebView.setVerticalScrollbarOverlay(true);
        WebSettings settings = this.mWebView.getSettings();
        settings.setSupportMultipleWindows(false);
        settings.setJavaScriptEnabled(true);
        settings.setSavePassword(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setDomStorageEnabled(true);
        settings.setAppCachePath(OpenAccountSDK.getAndroidContext().getDir("cache", 0).getPath());
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(-1);
        settings.setBuiltInZoomControls(false);
        if (Build.VERSION.SDK_INT >= 7) {
            try {
                Method method = this.mWebView.getSettings().getClass().getMethod("setDomStorageEnabled", Boolean.TYPE);
                if (method != null) {
                    method.invoke(this.mWebView.getSettings(), true);
                }
            } catch (Exception unused) {
            }
        }
        try {
            this.mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
            this.mWebView.removeJavascriptInterface("accessibility");
            this.mWebView.removeJavascriptInterface("accessibilityTraversal");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setWebChromeClinet() {
        this.mWebView.setWebViewClient(new LoginWebViewClient(this));
    }

    protected void setWebViewClient() {
        this.mWebView.setWebChromeClient(new LoginWebChromeClient(this));
    }

    public static class LoginWebChromeClient extends WVUCWebChromeClient {
        WeakReference<H5WindVaneContainer> reference;

        public LoginWebChromeClient(H5WindVaneContainer h5WindVaneContainer) {
            super(h5WindVaneContainer);
            this.reference = new WeakReference<>(h5WindVaneContainer);
        }

        public void onReceivedTitle(WebView webView, String str) {
            H5WindVaneContainer h5WindVaneContainer = this.reference.get();
            if (h5WindVaneContainer != null && h5WindVaneContainer.getSupportActionBar() != null) {
                try {
                    h5WindVaneContainer.getSupportActionBar().setTitle(str);
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
            super.onReceivedTitle(webView, str);
        }

        public boolean onJsPrompt(WebView webView, String str, String str2, String str3, JsPromptResult jsPromptResult) {
            return super.onJsPrompt(webView, str, str2, str3, jsPromptResult);
        }
    }

    static class LoginWebViewClient extends WVUCWebViewClient {
        WeakReference<H5WindVaneContainer> reference;

        public LoginWebViewClient(H5WindVaneContainer h5WindVaneContainer) {
            super(h5WindVaneContainer);
            this.reference = new WeakReference<>(h5WindVaneContainer);
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            boolean zOverrideUrlLoading;
            H5WindVaneContainer h5WindVaneContainer = this.reference.get();
            if (h5WindVaneContainer != null) {
                try {
                    zOverrideUrlLoading = h5WindVaneContainer.overrideUrlLoading(webView, str);
                } catch (Exception unused) {
                    zOverrideUrlLoading = false;
                }
            } else {
                zOverrideUrlLoading = false;
            }
            return zOverrideUrlLoading ? zOverrideUrlLoading : super.shouldOverrideUrlLoading(webView, str);
        }

        public void onReceivedSslError(WebView webView, final SslErrorHandler sslErrorHandler, SslError sslError) {
            final H5WindVaneContainer h5WindVaneContainer = this.reference.get();
            if (h5WindVaneContainer != null && !h5WindVaneContainer.firstAlert) {
                if (h5WindVaneContainer.proceed) {
                    sslErrorHandler.proceed();
                    return;
                } else {
                    super.onReceivedSslError(webView, sslErrorHandler, sslError);
                    return;
                }
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());
            String string = webView.getContext().getResources().getString(R.string.ali_sdk_openaccount_ssl_error_info);
            builder.setPositiveButton(webView.getContext().getResources().getString(R.string.ali_sdk_openaccount_confirm), new DialogInterface.OnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.face.H5WindVaneContainer.LoginWebViewClient.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    sslErrorHandler.proceed();
                    H5WindVaneContainer h5WindVaneContainer2 = h5WindVaneContainer;
                    if (h5WindVaneContainer2 != null) {
                        h5WindVaneContainer2.proceed = true;
                    }
                }
            });
            builder.setNeutralButton(webView.getContext().getResources().getString(R.string.ali_sdk_openaccount_cancel), new DialogInterface.OnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.face.H5WindVaneContainer.LoginWebViewClient.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    sslErrorHandler.cancel();
                    H5WindVaneContainer h5WindVaneContainer2 = h5WindVaneContainer;
                    if (h5WindVaneContainer2 != null) {
                        h5WindVaneContainer2.proceed = false;
                    }
                }
            });
            try {
                AlertDialog alertDialogCreate = builder.create();
                alertDialogCreate.setTitle(webView.getContext().getResources().getString(R.string.ali_sdk_openaccount_ssl_error_title));
                alertDialogCreate.setMessage(string);
                alertDialogCreate.show();
                if (h5WindVaneContainer != null) {
                    h5WindVaneContainer.firstAlert = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected boolean overrideUrlLoading(WebView webView, String str) {
        if (!checkWebviewBridge(str)) {
            return false;
        }
        Bundle bundleSerialBundle = serialBundle(Uri.parse(str).getQuery());
        if (bundleSerialBundle == null) {
            bundleSerialBundle = new Bundle();
        }
        String string = bundleSerialBundle.getString(NativeProtocol.WEB_DIALOG_ACTION);
        String string2 = bundleSerialBundle.getString("token");
        if (TextUtils.equals(string, "gotoLoginPage")) {
            ((OpenAccountUIService) OpenAccountSDK.getService(OpenAccountUIService.class)).showLogin(this, new LoginCallback() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.face.H5WindVaneContainer.1
                @Override // com.alibaba.sdk.android.openaccount.callback.FailureCallback
                public void onFailure(int i, String str2) {
                }

                @Override // com.alibaba.sdk.android.openaccount.callback.LoginCallback
                public void onSuccess(OpenAccountSession openAccountSession) {
                    H5WindVaneContainer.this.finish();
                }
            });
            return true;
        }
        if (!TextUtils.equals(string, "loginByToken")) {
            return false;
        }
        ((OpenAccountService) OpenAccountSDK.getService(OpenAccountService.class)).tokenLogin(getApplicationContext(), string2, new LoginCallback() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.face.H5WindVaneContainer.2
            @Override // com.alibaba.sdk.android.openaccount.callback.LoginCallback
            public void onSuccess(OpenAccountSession openAccountSession) {
                H5WindVaneContainer.this.finish();
                if (OpenAccountFaceLoginServiceImpl._faceLoginCallback != null) {
                    OpenAccountFaceLoginServiceImpl._faceLoginCallback.onSuccess(openAccountSession);
                }
                OpenAccountFaceLoginServiceImpl._faceLoginCallback = null;
            }

            @Override // com.alibaba.sdk.android.openaccount.callback.FailureCallback
            public void onFailure(int i, String str2) {
                H5WindVaneContainer.this.finish();
                if (OpenAccountFaceLoginServiceImpl._faceLoginCallback != null) {
                    OpenAccountFaceLoginServiceImpl._faceLoginCallback.onFailure(i, str2);
                }
                OpenAccountFaceLoginServiceImpl._faceLoginCallback = null;
            }
        });
        return true;
    }

    public boolean checkWebviewBridge(String str) {
        Uri uri = Uri.parse(str);
        StringBuilder sb = new StringBuilder();
        sb.append(uri.getAuthority());
        sb.append(uri.getPath());
        return "https://www.alipay.com/webviewbridge".contains(sb.toString());
    }

    private Bundle serialBundle(String str) {
        if (str == null || str.length() <= 0) {
            return null;
        }
        String[] strArrSplit = str.split("&");
        Bundle bundle = new Bundle();
        for (String str2 : strArrSplit) {
            int iIndexOf = str2.indexOf(ContainerUtils.KEY_VALUE_DELIMITER);
            if (iIndexOf > 0 && iIndexOf < str2.length() - 1) {
                bundle.putString(str2.substring(0, iIndexOf), str2.substring(iIndexOf + 1));
            }
        }
        return bundle;
    }
}
