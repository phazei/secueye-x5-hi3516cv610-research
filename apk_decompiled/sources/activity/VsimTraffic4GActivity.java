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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import com.seculink.app.R;
import com.taobao.accs.common.Constants;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class VsimTraffic4GActivity extends CommonActivity {
    private String imei;
    RelativeLayout layout_main;
    TitleView tv_title;
    WebView webview;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_traffic;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (RelativeLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.webview = (WebView) findViewById(R.id.webview);
        this.tv_title = (TitleView) findViewById(R.id.tv_title);
        this.tv_title.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.VsimTraffic4GActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                VsimTraffic4GActivity.this.finish();
            }
        });
        this.imei = getIntent().getStringExtra(Constants.KEY_IMEI);
        WebSettings settings = this.webview.getSettings();
        settings.setCacheMode(2);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        this.webview.setWebViewClient(new WebViewClient() { // from class: activity.VsimTraffic4GActivity.2
            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                if (VsimTraffic4GActivity.this.isFinishing()) {
                    return false;
                }
                if (!str.startsWith("alipays://platformapi") && !str.startsWith("weixin://wap/pay?") && !str.startsWith("weixin://dl/business/?")) {
                    return false;
                }
                VsimTraffic4GActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
                return true;
            }

            @Override // android.webkit.WebViewClient
            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                super.onPageStarted(webView, str, bitmap);
            }

            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView webView, String str) {
                super.onPageFinished(webView, str);
                if (VsimTraffic4GActivity.this.isFinishing() || VsimTraffic4GActivity.this.tv_title == null || webView == null || webView.getTitle() == null || "".equals(webView.getTitle())) {
                    return;
                }
                VsimTraffic4GActivity.this.tv_title.setTitleText(!TextUtils.isEmpty(webView.getTitle()) ? webView.getTitle() : "");
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedSslError(WebView webView, final SslErrorHandler sslErrorHandler, SslError sslError) {
                AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());
                builder.setMessage("SSL认证失败，是否继续访问？");
                builder.setPositiveButton(VsimTraffic4GActivity.this.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() { // from class: activity.VsimTraffic4GActivity.2.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sslErrorHandler.proceed();
                    }
                });
                builder.setNegativeButton(VsimTraffic4GActivity.this.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() { // from class: activity.VsimTraffic4GActivity.2.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sslErrorHandler.cancel();
                    }
                });
                builder.create().show();
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

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
        runOnUiThread(new Runnable() { // from class: activity.VsimTraffic4GActivity.3
            @Override // java.lang.Runnable
            public void run() {
                String str = "https://traffic.secueye.cn/?imei=" + VsimTraffic4GActivity.this.imei;
                if (VsimTraffic4GActivity.this.webview == null || str == null) {
                    return;
                }
                Log.e("链接", str);
                VsimTraffic4GActivity.this.webview.loadUrl(str);
            }
        });
    }
}
