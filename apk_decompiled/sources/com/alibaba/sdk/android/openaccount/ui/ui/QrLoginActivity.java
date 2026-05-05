package com.alibaba.sdk.android.openaccount.ui.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.alibaba.sdk.android.openaccount.annotation.ExtensionPoint;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.alibaba.sdk.android.openaccount.message.MessageUtils;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountUIServiceImpl;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
@ExtensionPoint
public class QrLoginActivity extends ActivityTemplate {
    protected LoginWebViewWidget loginWebViewWidget;

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.ActivityTemplate
    protected String getLayoutName() {
        return "ali_sdk_openaccount_qr_login";
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.ActivityTemplate, com.alibaba.sdk.android.openaccount.ui.ui.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Map<String, String> map = (getIntent() == null || getIntent().getExtras() == null) ? null : (Map) getIntent().getExtras().getSerializable(OpenAccountUIConstants.QR_LOGIN_REQUEST_PARAMETERS_KEY);
        Typeface typefaceCreateFromAsset = Typeface.createFromAsset(getAssets(), "ali_sdk_openaccount/openaccount_qr_icon_font.ttf");
        Button button = (Button) findViewById(ResourceUtils.getRId(this, "alisdk_openaccount_qr_login_button_close"));
        button.setOnClickListener(new View.OnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.QrLoginActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                QrLoginActivity.this.onCancelQrLogin();
                QrLoginActivity.this.finish();
            }
        });
        button.setTypeface(typefaceCreateFromAsset);
        ((TextView) findViewById(ResourceUtils.getRId(this, "alisdk_openaccount_login_qr_text_view"))).setTypeface(typefaceCreateFromAsset);
        this.loginWebViewWidget = (LoginWebViewWidget) findViewById(ResourceUtils.getRId(this, "alisdk_openaccount_login_web_view"));
        this.loginWebViewWidget.loadQrLoginUrl(map);
    }

    @Override // android.app.Activity
    public boolean onTouchEvent(MotionEvent motionEvent) {
        setResult(10003);
        finish();
        return true;
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        this.loginWebViewWidget.destroyWebView();
        if (isFinishing()) {
            OpenAccountUIServiceImpl._qrLoginCallback = null;
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        onCancelQrLogin();
        super.onBackPressed();
    }

    protected void onCancelQrLogin() {
        final LoginCallback loginCallback = OpenAccountUIServiceImpl._qrLoginCallback;
        if (loginCallback != null) {
            this.executorService.postUITask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.QrLoginActivity.2
                @Override // java.lang.Runnable
                public void run() {
                    loginCallback.onFailure(10003, MessageUtils.getMessageContent(10003, new Object[0]));
                }
            });
        }
    }
}
