package com.alibaba.sdk.android.openaccount.webview;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.alibaba.sdk.android.openaccount.OpenAccountConfigs;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.config.LanguageCode;
import com.alibaba.sdk.android.openaccount.model.ResultCode;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;
import com.alibaba.sdk.android.pluto.Pluto;
import com.huawei.hms.framework.common.ContainerUtils;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class BaseWebViewActivity extends AppCompatActivity {
    public boolean canReceiveTitle;
    public Toolbar mToolBar;
    protected TaeWebView taeWebView;

    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity
    protected void onTitleChanged(CharSequence charSequence, int i) {
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        String stringExtra;
        super.onCreate(bundle);
        Pluto.DEFAULT_INSTANCE.inject(this);
        String stringExtra2 = null;
        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(ResourceUtils.getRLayout(this, "ali_sdk_openaccount_web_view_activity"), (ViewGroup) null);
        this.taeWebView = createTaeWebView();
        this.taeWebView.setWebViewClient(createWebViewClient());
        this.taeWebView.setWebChromeClient(createWebChromeClient());
        linearLayout.addView(this.taeWebView, new LinearLayout.LayoutParams(-1, -1));
        setContentView(linearLayout);
        this.mToolBar = (Toolbar) findViewById(ResourceUtils.getIdentifier(this, "id", "ali_sdk_openaccount_title_bar"));
        Toolbar toolbar = this.mToolBar;
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            this.mToolBar.setNavigationOnClickListener(new View.OnClickListener() { // from class: com.alibaba.sdk.android.openaccount.webview.BaseWebViewActivity.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    BaseWebViewActivity.this.onBackPressed();
                }
            });
        }
        if (bundle != null) {
            stringExtra2 = bundle.getString("title");
            stringExtra = bundle.getString("url");
        } else {
            stringExtra = null;
        }
        if (TextUtils.isEmpty(stringExtra2)) {
            stringExtra2 = getIntent().getStringExtra("title");
        }
        if (TextUtils.isEmpty(stringExtra2)) {
            this.canReceiveTitle = true;
        } else {
            this.canReceiveTitle = false;
            this.mToolBar.setTitle(stringExtra2);
        }
        if (TextUtils.isEmpty(stringExtra)) {
            stringExtra = getIntent().getStringExtra("url");
        }
        this.taeWebView.loadUrl(stringExtra);
    }

    private void finishActivity() {
        finish();
    }

    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("url", this.taeWebView.getUrl());
        bundle.putString("title", this.mToolBar.getTitle().toString());
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        TaeWebView taeWebView = this.taeWebView;
        if (taeWebView != null) {
            ViewGroup viewGroup = (ViewGroup) taeWebView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(this.taeWebView);
            }
            this.taeWebView.removeAllViews();
            this.taeWebView.destroy();
        }
        super.onDestroy();
    }

    public void setResult(ResultCode resultCode) {
        onFailure(resultCode);
    }

    protected WebViewClient createWebViewClient() {
        return new BaseWebViewClient();
    }

    protected WebChromeClient createWebChromeClient() {
        return new BridgeWebChromeClient() { // from class: com.alibaba.sdk.android.openaccount.webview.BaseWebViewActivity.2
            @Override // android.webkit.WebChromeClient
            public void onReceivedTitle(WebView webView, String str) {
                BaseWebViewActivity.this.mToolBar.setTitle(str);
            }
        };
    }

    protected TaeWebView createTaeWebView() {
        return new TaeWebView(this);
    }

    protected void onFailure(ResultCode resultCode) {
        finishActivity();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (isBackPressedAsHistoryBack()) {
            onBackHistory();
        } else {
            setResult(10003, new Intent());
            super.onBackPressed();
        }
    }

    protected boolean isBackPressedAsHistoryBack() {
        return "true".equals(OpenAccountSDK.getProperty("backPressedAsHistoryBack"));
    }

    private void onBackHistory() {
        try {
            if (DefaultTaeWebViewHistoryHelper.INSTANCE.goBack(this.taeWebView)) {
                return;
            }
            setResult(10003, new Intent());
            finishActivity();
        } catch (Exception e) {
            AliSDKLogger.e("ui", "fail to go back", e);
            if (this.taeWebView.canGoBack()) {
                this.taeWebView.goBack();
            } else {
                setResult(10003, new Intent());
                finishActivity();
            }
        }
    }

    public static Bundle serialBundle(String str) {
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

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        onLanguageSwitchNotify();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        onLanguageSwitchNotify();
    }

    protected void onLanguageSwitchNotify() {
        Configuration configuration = getResources().getConfiguration();
        String str = OpenAccountConfigs.clientLocal;
        if (TextUtils.isEmpty(str)) {
            str = LanguageCode.CHINESE;
        }
        String[] strArrSplit = str.split(OpenAccountUIConstants.UNDER_LINE);
        if (strArrSplit == null || strArrSplit.length != 2) {
            return;
        }
        configuration.locale = new Locale(strArrSplit[0], strArrSplit[1]);
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }
}
