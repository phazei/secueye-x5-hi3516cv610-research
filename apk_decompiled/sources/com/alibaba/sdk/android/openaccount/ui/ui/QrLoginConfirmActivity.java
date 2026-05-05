package com.alibaba.sdk.android.openaccount.ui.ui;

import android.os.Bundle;
import com.alibaba.sdk.android.openaccount.annotation.ExtensionPoint;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.alibaba.sdk.android.openaccount.ui.bridge.LoginBridge;
import com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountUIServiceImpl;
import com.alibaba.sdk.android.openaccount.webview.BaseWebViewActivity;

/* JADX INFO: loaded from: classes.dex */
@ExtensionPoint
public class QrLoginConfirmActivity extends BaseWebViewActivity {
    @Override // com.alibaba.sdk.android.openaccount.webview.BaseWebViewActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.taeWebView.addBridgeObject(OpenAccountUIConstants.OPEN_ACCOUNT_QR_LOGIN_BRIDGE, new LoginBridge());
    }

    @Override // com.alibaba.sdk.android.openaccount.webview.BaseWebViewActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            OpenAccountUIServiceImpl._qrLoginConfirmCallback = null;
        }
    }
}
