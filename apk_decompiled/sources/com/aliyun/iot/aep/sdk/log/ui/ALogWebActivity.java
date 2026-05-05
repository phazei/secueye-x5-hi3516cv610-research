package com.aliyun.iot.aep.sdk.log.ui;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import com.aliyun.iot.aep.sdk.log.R;

/* JADX INFO: loaded from: classes2.dex */
public class ALogWebActivity extends Activity {
    private WebView mWebView;

    private void initViews() {
        this.mWebView = (WebView) findViewById(R.id.alogWebView);
        this.mWebView.loadUrl(getIntent().getStringExtra("weburl"));
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_alog_web);
        initViews();
    }
}
