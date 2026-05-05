package activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.seculink.app.R;
import tools.MyCallback;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;
import tools.Utils;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class PayYunServiceActivity2 extends CommonActivity {
    LinearLayout layout_main;
    TitleView tv_title;
    WebView webview;
    String iotId = "";
    String url = "";

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_pay_yun_service2;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (LinearLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.webview = (WebView) findViewById(R.id.webview);
        this.tv_title = (TitleView) findViewById(R.id.tv_title);
        this.iotId = getIntent().getStringExtra("iotId");
        this.url = "http://www.secueye.cn/camera/index.html?iotId=" + this.iotId + "&userName=" + Utils.getUserPhone() + "&dn=" + getIntent().getStringExtra(AlinkConstants.KEY_DN) + "&pk=" + getIntent().getStringExtra(AlinkConstants.KEY_PK);
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(this.url);
        Log.e("云服务购买地址", sb.toString());
        this.tv_title.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.PayYunServiceActivity2.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                PayYunServiceActivity2.this.finish();
            }
        });
        this.webview.getSettings().setCacheMode(2);
        this.webview.getSettings().setJavaScriptEnabled(true);
        this.webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.webview.setWebViewClient(new WebViewClient() { // from class: activity.PayYunServiceActivity2.2
            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                if (PayYunServiceActivity2.this.isFinishing()) {
                    return false;
                }
                if (!str.startsWith("alipays://platformapi") && !str.startsWith("weixin://wap/pay?")) {
                    return false;
                }
                PayYunServiceActivity2.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
                return true;
            }

            @Override // android.webkit.WebViewClient
            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                super.onPageStarted(webView, str, bitmap);
            }

            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView webView, String str) {
                super.onPageFinished(webView, str);
                if (PayYunServiceActivity2.this.isFinishing() || PayYunServiceActivity2.this.tv_title == null || webView == null || webView.getTitle() == null) {
                    return;
                }
                String title = webView.getTitle();
                if (TextUtils.isEmpty(title)) {
                    return;
                }
                PayYunServiceActivity2.this.tv_title.setTitleText(title);
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedSslError(WebView webView, final SslErrorHandler sslErrorHandler, SslError sslError) {
                AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());
                builder.setMessage("SSL认证失败，是否继续访问？");
                builder.setPositiveButton(PayYunServiceActivity2.this.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() { // from class: activity.PayYunServiceActivity2.2.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sslErrorHandler.proceed();
                    }
                });
                builder.setNegativeButton(PayYunServiceActivity2.this.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() { // from class: activity.PayYunServiceActivity2.2.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sslErrorHandler.cancel();
                    }
                });
                AlertDialog alertDialogCreate = builder.create();
                if (PayYunServiceActivity2.this.isFinishing() || alertDialogCreate.isShowing()) {
                    return;
                }
                alertDialogCreate.show();
            }
        });
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
        SettingsCtrl.getInstance().getProperties(this.iotId, new MyCallback() { // from class: activity.PayYunServiceActivity2.3
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
                if (z) {
                    if (SharePreferenceManager.getInstance().getSupport4G(PayYunServiceActivity2.this.iotId) == 1) {
                        PayYunServiceActivity2.this.url = PayYunServiceActivity2.this.url + "&sim=1";
                    } else {
                        PayYunServiceActivity2.this.url = PayYunServiceActivity2.this.url + "&sim=0";
                    }
                    PayYunServiceActivity2.this.runOnUiThread(new Runnable() { // from class: activity.PayYunServiceActivity2.3.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (PayYunServiceActivity2.this.url == null || PayYunServiceActivity2.this.webview == null) {
                                return;
                            }
                            PayYunServiceActivity2.this.webview.loadUrl(PayYunServiceActivity2.this.url);
                        }
                    });
                    return;
                }
                PayYunServiceActivity2.this.runOnUiThread(new Runnable() { // from class: activity.PayYunServiceActivity2.3.2
                    @Override // java.lang.Runnable
                    public void run() {
                        PayYunServiceActivity2.this.webview.loadUrl(PayYunServiceActivity2.this.url);
                    }
                });
            }
        });
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4 && this.webview.canGoBack()) {
            this.webview.goBack();
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }
}
