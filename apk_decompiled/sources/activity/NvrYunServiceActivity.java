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
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.seculink.app.R;
import tools.MyCallback;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;
import tools.Utils;

/* JADX INFO: loaded from: classes.dex */
public class NvrYunServiceActivity extends CommonActivity {
    LinearLayout layout_main;
    ImageView left_img;
    TextView tv_history;
    TextView tv_title;
    WebView webview;
    String iotId = "";
    String dn = "";
    String pk = "";
    String url = "";

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_nvr_yun_service;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (LinearLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.webview = (WebView) findViewById(R.id.webview);
        this.tv_title = (TextView) findViewById(R.id.tv_title);
        this.tv_history = (TextView) findViewById(R.id.tv_history);
        this.left_img = (ImageView) findViewById(R.id.left_img);
        this.iotId = getIntent().getStringExtra("iotId");
        this.dn = getIntent().getStringExtra(AlinkConstants.KEY_DN);
        this.pk = getIntent().getStringExtra(AlinkConstants.KEY_PK);
        this.url = "http://www.secueye.cn/cloud/index.html?iotId=" + this.iotId + "&dn=" + this.dn + "&pk=" + this.pk + "&username=" + Utils.getUserPhone();
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(this.url);
        Log.d("云服务链接", sb.toString());
        this.left_img.setOnClickListener(new View.OnClickListener() { // from class: activity.NvrYunServiceActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                NvrYunServiceActivity.this.finish();
            }
        });
        this.webview.clearCache(true);
        CookieManager.getInstance().removeAllCookies(null);
        this.webview.clearHistory();
        this.webview.getSettings().setCacheMode(2);
        this.webview.getSettings().setJavaScriptEnabled(true);
        this.webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.webview.setWebViewClient(new WebViewClient() { // from class: activity.NvrYunServiceActivity.2
            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                if (NvrYunServiceActivity.this.isFinishing()) {
                    return false;
                }
                if (!str.startsWith("alipays://platformapi") && !str.startsWith("weixin://wap/pay?")) {
                    return false;
                }
                NvrYunServiceActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
                return true;
            }

            @Override // android.webkit.WebViewClient
            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                super.onPageStarted(webView, str, bitmap);
            }

            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView webView, String str) {
                super.onPageFinished(webView, str);
                if (NvrYunServiceActivity.this.isFinishing()) {
                    return;
                }
                String title = webView.getTitle();
                if (TextUtils.isEmpty(title)) {
                    return;
                }
                NvrYunServiceActivity.this.tv_title.setText(title);
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedSslError(WebView webView, final SslErrorHandler sslErrorHandler, SslError sslError) {
                AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());
                builder.setMessage("SSL认证失败，是否继续访问？");
                builder.setPositiveButton(NvrYunServiceActivity.this.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() { // from class: activity.NvrYunServiceActivity.2.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sslErrorHandler.proceed();
                    }
                });
                builder.setNegativeButton(NvrYunServiceActivity.this.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() { // from class: activity.NvrYunServiceActivity.2.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sslErrorHandler.cancel();
                    }
                });
                builder.create().show();
            }
        });
        this.tv_history.setOnClickListener(new View.OnClickListener() { // from class: activity.NvrYunServiceActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Intent intent = new Intent(NvrYunServiceActivity.this.getActivity(), (Class<?>) NvrYunPayHistoryActivity.class);
                intent.putExtra("iotId", NvrYunServiceActivity.this.iotId);
                intent.putExtra(AlinkConstants.KEY_DN, NvrYunServiceActivity.this.dn);
                intent.putExtra(AlinkConstants.KEY_PK, NvrYunServiceActivity.this.pk);
                NvrYunServiceActivity.this.startActivity(intent);
            }
        });
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
        SettingsCtrl.getInstance().getProperties(this.iotId, new MyCallback() { // from class: activity.NvrYunServiceActivity.4
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
                if (z) {
                    if (SharePreferenceManager.getInstance().getSupport4G(NvrYunServiceActivity.this.iotId) == 1) {
                        NvrYunServiceActivity.this.url = NvrYunServiceActivity.this.url + "&sim=1";
                    } else {
                        NvrYunServiceActivity.this.url = NvrYunServiceActivity.this.url + "&sim=0";
                    }
                    NvrYunServiceActivity.this.runOnUiThread(new Runnable() { // from class: activity.NvrYunServiceActivity.4.1
                        @Override // java.lang.Runnable
                        public void run() {
                            NvrYunServiceActivity.this.webview.loadUrl(NvrYunServiceActivity.this.url);
                        }
                    });
                    return;
                }
                NvrYunServiceActivity.this.runOnUiThread(new Runnable() { // from class: activity.NvrYunServiceActivity.4.2
                    @Override // java.lang.Runnable
                    public void run() {
                        NvrYunServiceActivity.this.webview.loadUrl(NvrYunServiceActivity.this.url);
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
