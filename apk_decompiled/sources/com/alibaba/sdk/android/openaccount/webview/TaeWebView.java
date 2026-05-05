package com.alibaba.sdk.android.openaccount.webview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.executor.ExecutorService;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.util.CommonUtils;
import com.alibaba.sdk.android.openaccount.util.FileUtils;
import com.alibaba.sdk.android.pluto.Pluto;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
@SuppressLint({"SetJavaScriptEnabled"})
public class TaeWebView extends WebView {
    private static final String TAG = "TaeWebView";
    private static final String UA_ALIAPP_APPEND = " AliApp(BC/" + OpenAccountSDK.getVersion().toString() + ")";
    private static final String UA_TAESDK_APPEND;
    private String appCacheDir;
    private HashMap<String, String> contextParameters;

    @Autowired
    private ExecutorService executorService;
    private String lastReloadUrl;
    private Map<String, Object> nameToObj;
    private String startUrl;

    @Override // android.webkit.WebView
    public final void addJavascriptInterface(Object obj, String str) {
    }

    protected String normalizeURL(String str) {
        return str;
    }

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(" tae_sdk_");
        sb.append(OpenAccountSDK.getVersion().toString());
        UA_TAESDK_APPEND = sb.toString();
    }

    public TaeWebView(Context context) {
        super(context);
        this.lastReloadUrl = "";
        this.nameToObj = new HashMap();
        this.contextParameters = new HashMap<>();
        Pluto.DEFAULT_INSTANCE.inject(this);
        initSettings(context, true);
    }

    public TaeWebView(Context context, boolean z) {
        super(context);
        this.lastReloadUrl = "";
        this.nameToObj = new HashMap();
        this.contextParameters = new HashMap<>();
        Pluto.DEFAULT_INSTANCE.inject(this);
        initSettings(context, z);
    }

    public TaeWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.lastReloadUrl = "";
        this.nameToObj = new HashMap();
        this.contextParameters = new HashMap<>();
        Pluto.DEFAULT_INSTANCE.inject(this);
        initSettings(context, true);
    }

    @Override // android.webkit.WebView
    public void loadUrl(String str, Map<String, String> map) {
        if (AliSDKLogger.isDebugEnabled()) {
            AliSDKLogger.d(TAG, "load url: " + str);
        }
        if (str != null) {
            this.startUrl = normalizeURL(str);
            String str2 = this.startUrl;
            if (str2 != null) {
                super.loadUrl(str2, map);
            }
        }
    }

    @Override // android.webkit.WebView
    public void loadUrl(String str) {
        if (AliSDKLogger.isDebugEnabled()) {
            AliSDKLogger.d(TAG, "load url: " + str);
        }
        if (str != null) {
            this.startUrl = normalizeURL(str);
            String str2 = this.startUrl;
            if (str2 != null) {
                super.loadUrl(str2);
            }
        }
    }

    public void loadUrl(String str, boolean z) {
        if (AliSDKLogger.isDebugEnabled()) {
            AliSDKLogger.d(TAG, "load url: " + str);
        }
        if (str != null) {
            if (z) {
                str = normalizeURL(str);
            }
            if (str != null) {
                super.loadUrl(str);
            }
        }
    }

    @TargetApi(21)
    private void initSettings(Context context, boolean z) {
        WebSettings settings = getSettings();
        try {
            settings.setJavaScriptEnabled(true);
        } catch (Exception unused) {
        }
        settings.setSavePassword(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setDomStorageEnabled(true);
        this.appCacheDir = context.getApplicationContext().getDir("cache", 0).getPath();
        settings.setAppCachePath(this.appCacheDir);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        if (CommonUtils.isNetworkAvailable(context)) {
            settings.setCacheMode(-1);
        } else {
            settings.setCacheMode(1);
        }
        settings.setBuiltInZoomControls(false);
        if (z) {
            StringBuilder sb = new StringBuilder();
            String userAgentString = settings.getUserAgentString();
            if (userAgentString != null) {
                sb.append(userAgentString);
            }
            sb.append(UA_TAESDK_APPEND);
            sb.append(UA_ALIAPP_APPEND);
            settings.setUserAgentString(sb.toString());
        }
        if (Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
            int intProperty = ConfigManager.getInstance().getIntProperty("mixedContentMode", -1);
            if (intProperty != -1) {
                settings.setMixedContentMode(intProperty);
            }
        }
        removeRiskJavascriptInterfaces();
    }

    public final void addBridgeObject(String str, Object obj) {
        this.nameToObj.put(str, obj);
    }

    public Object getBridgeObj(String str) {
        return this.nameToObj.get(str);
    }

    @Override // android.webkit.WebView
    public void reload() {
        String url = getUrl();
        if (AliSDKLogger.isDebugEnabled()) {
            AliSDKLogger.d(TAG, "reload url: " + url);
        }
        if (TextUtils.equals(this.lastReloadUrl, url) || TextUtils.equals(this.lastReloadUrl, this.startUrl)) {
            return;
        }
        if (url == null) {
            loadUrl(this.startUrl);
            this.lastReloadUrl = this.startUrl;
        } else {
            this.lastReloadUrl = url;
            super.reload();
        }
    }

    public void clearCache() {
        try {
            clearCache(true);
        } catch (Exception e) {
            AliSDKLogger.e("ui", "fail to clear cache ", e);
        }
        this.executorService.postTask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.webview.TaeWebView.1
            @Override // java.lang.Runnable
            public void run() {
                if (TaeWebView.this.appCacheDir != null) {
                    try {
                        FileUtils.delete(new File(TaeWebView.this.appCacheDir));
                    } catch (Exception e2) {
                        AliSDKLogger.e("ui", "fail to delete cache " + TaeWebView.this.appCacheDir, e2);
                    }
                }
            }
        });
    }

    @TargetApi(11)
    private void removeRiskJavascriptInterfaces() {
        if (Build.VERSION.SDK_INT >= 11) {
            removeJavascriptInterface("searchBoxJavaBridge_");
            removeJavascriptInterface("accessibility");
            removeJavascriptInterface("accessibilityTraversal");
        }
    }
}
