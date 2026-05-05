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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.seculink.app.R;
import com.tencent.mm.opensdk.modelbiz.WXOpenCustomerServiceChat;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import config.AppConfig;
import tools.MyCallback;
import tools.OnMultiClickListener;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class Traffic4GIC2Activity extends CommonActivity {
    private String dn;
    private String iccid;
    private String iotId;
    LinearLayout layout_customer;
    RelativeLayout layout_main;
    private String pk;
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
        this.layout_customer = (LinearLayout) findViewById(R.id.layout_customer);
        this.tv_title.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.Traffic4GIC2Activity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                Traffic4GIC2Activity.this.finish();
            }
        });
        this.iotId = getIntent().getStringExtra("iotId");
        this.iccid = getIntent().getStringExtra("iccid");
        this.dn = getIntent().getStringExtra(AlinkConstants.KEY_DN);
        this.pk = getIntent().getStringExtra(AlinkConstants.KEY_PK);
        Log.e("链接", "pk=" + getIntent().getStringExtra(AlinkConstants.KEY_PK) + "  dn=" + getIntent().getStringExtra(AlinkConstants.KEY_DN));
        WXAPIFactory.createWXAPI(this, AppConfig.WX_APP_ID).registerApp(AppConfig.WX_APP_ID);
        this.layout_customer.setOnClickListener(new OnMultiClickListener() { // from class: activity.Traffic4GIC2Activity.2
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                IWXAPI iwxapiCreateWXAPI = WXAPIFactory.createWXAPI(Traffic4GIC2Activity.this, AppConfig.WX_APP_ID);
                if (iwxapiCreateWXAPI.getWXAppSupportAPI() >= 671090490) {
                    WXOpenCustomerServiceChat.Req req = new WXOpenCustomerServiceChat.Req();
                    req.corpId = "ww6d78c293de291842";
                    req.url = "https://work.weixin.qq.com/kfid/kfc4e35bb68a365822f";
                    iwxapiCreateWXAPI.sendReq(req);
                }
            }
        });
        WebSettings settings = this.webview.getSettings();
        settings.setCacheMode(2);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        this.webview.setWebViewClient(new WebViewClient() { // from class: activity.Traffic4GIC2Activity.3
            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                if (Traffic4GIC2Activity.this.isFinishing()) {
                    return false;
                }
                if (!str.startsWith("alipays://platformapi") && !str.startsWith("weixin://wap/pay?") && !str.startsWith("weixin://dl/business/?")) {
                    return false;
                }
                Traffic4GIC2Activity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
                return true;
            }

            @Override // android.webkit.WebViewClient
            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                super.onPageStarted(webView, str, bitmap);
            }

            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView webView, String str) {
                super.onPageFinished(webView, str);
                if (Traffic4GIC2Activity.this.isFinishing() || Traffic4GIC2Activity.this.tv_title == null || webView == null || webView.getTitle() == null || "".equals(webView.getTitle())) {
                    return;
                }
                Traffic4GIC2Activity.this.tv_title.setTitleText(!TextUtils.isEmpty(webView.getTitle()) ? webView.getTitle() : "");
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedSslError(WebView webView, final SslErrorHandler sslErrorHandler, SslError sslError) {
                AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());
                builder.setMessage("SSL认证失败，是否继续访问？");
                builder.setPositiveButton(Traffic4GIC2Activity.this.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() { // from class: activity.Traffic4GIC2Activity.3.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sslErrorHandler.proceed();
                    }
                });
                builder.setNegativeButton(Traffic4GIC2Activity.this.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() { // from class: activity.Traffic4GIC2Activity.3.2
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
        String str = this.iotId;
        if (str != null && !"".equals(str)) {
            SettingsCtrl.getInstance().getProperties(this.iotId, new MyCallback() { // from class: activity.Traffic4GIC2Activity.4
                @Override // tools.MyCallback
                public void onComplete(boolean z) {
                    if (z) {
                        if (SharePreferenceManager.getInstance().getEnable(Traffic4GIC2Activity.this.iotId) == 1) {
                            if (SharePreferenceManager.getInstance().getURL(Traffic4GIC2Activity.this.iotId) == null || SharePreferenceManager.getInstance().getURL(Traffic4GIC2Activity.this.iotId).equals("")) {
                                if (SharePreferenceManager.getInstance().getVendorID(Traffic4GIC2Activity.this.iotId) != 0) {
                                    if (SharePreferenceManager.getInstance().getVendorID(Traffic4GIC2Activity.this.iotId) != 1) {
                                        if (SharePreferenceManager.getInstance().getVendorID(Traffic4GIC2Activity.this.iotId) == 2) {
                                            Traffic4GIC2Activity.this.runOnUiThread(new Runnable() { // from class: activity.Traffic4GIC2Activity.4.4
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    String url = SharePreferenceManager.getInstance().getURL(Traffic4GIC2Activity.this.iotId);
                                                    if (Traffic4GIC2Activity.this.webview == null || url == null) {
                                                        return;
                                                    }
                                                    Log.e("链接", url);
                                                    Traffic4GIC2Activity.this.webview.loadUrl(url);
                                                }
                                            });
                                            return;
                                        } else {
                                            Traffic4GIC2Activity.this.runOnUiThread(new Runnable() { // from class: activity.Traffic4GIC2Activity.4.5
                                                @Override // java.lang.Runnable
                                                public void run() {
                                                    String str2 = "https://traffic.seculink.com.cn/?iccid=" + Traffic4GIC2Activity.this.iccid + "&dn=" + Traffic4GIC2Activity.this.dn + "&pk=" + Traffic4GIC2Activity.this.pk;
                                                    if (Traffic4GIC2Activity.this.webview == null || str2 == null) {
                                                        return;
                                                    }
                                                    Log.e("链接", str2);
                                                    Traffic4GIC2Activity.this.webview.loadUrl(str2);
                                                }
                                            });
                                            return;
                                        }
                                    }
                                    Traffic4GIC2Activity.this.runOnUiThread(new Runnable() { // from class: activity.Traffic4GIC2Activity.4.3
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            String url = SharePreferenceManager.getInstance().getURL(Traffic4GIC2Activity.this.iotId);
                                            if (Traffic4GIC2Activity.this.webview == null || url == null) {
                                                return;
                                            }
                                            Log.e("链接", url);
                                            Traffic4GIC2Activity.this.webview.loadUrl(url);
                                        }
                                    });
                                    return;
                                }
                                Traffic4GIC2Activity.this.runOnUiThread(new Runnable() { // from class: activity.Traffic4GIC2Activity.4.2
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        String str2 = "https://traffic.seculink.com.cn/?iccid=" + Traffic4GIC2Activity.this.iccid + "&dn=" + Traffic4GIC2Activity.this.dn + "&pk=" + Traffic4GIC2Activity.this.pk;
                                        if (Traffic4GIC2Activity.this.webview == null || str2 == null) {
                                            return;
                                        }
                                        Log.e("链接", str2);
                                        Traffic4GIC2Activity.this.webview.loadUrl(str2);
                                    }
                                });
                                return;
                            }
                            Traffic4GIC2Activity.this.runOnUiThread(new Runnable() { // from class: activity.Traffic4GIC2Activity.4.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    String url = SharePreferenceManager.getInstance().getURL(Traffic4GIC2Activity.this.iotId);
                                    Log.e("链接", url);
                                    Traffic4GIC2Activity.this.webview.loadUrl(url);
                                }
                            });
                            return;
                        }
                        Traffic4GIC2Activity.this.runOnUiThread(new Runnable() { // from class: activity.Traffic4GIC2Activity.4.6
                            @Override // java.lang.Runnable
                            public void run() {
                                String str2 = "https://traffic.seculink.com.cn/?iccid=" + Traffic4GIC2Activity.this.iccid + "&dn=" + Traffic4GIC2Activity.this.dn + "&pk=" + Traffic4GIC2Activity.this.pk;
                                if (Traffic4GIC2Activity.this.webview == null || str2 == null) {
                                    return;
                                }
                                Log.e("链接", str2);
                                Traffic4GIC2Activity.this.webview.loadUrl(str2);
                            }
                        });
                        return;
                    }
                    Traffic4GIC2Activity.this.runOnUiThread(new Runnable() { // from class: activity.Traffic4GIC2Activity.4.7
                        @Override // java.lang.Runnable
                        public void run() {
                            String str2 = "https://traffic.seculink.com.cn/?iccid=" + Traffic4GIC2Activity.this.iccid + "&dn=" + Traffic4GIC2Activity.this.dn + "&pk=" + Traffic4GIC2Activity.this.pk;
                            if (Traffic4GIC2Activity.this.webview == null || str2 == null) {
                                return;
                            }
                            Log.e("链接", str2);
                            Traffic4GIC2Activity.this.webview.loadUrl(str2);
                        }
                    });
                }
            });
        } else {
            runOnUiThread(new Runnable() { // from class: activity.Traffic4GIC2Activity.5
                @Override // java.lang.Runnable
                public void run() {
                    String str2 = "https://traffic.seculink.com.cn/?iccid=" + Traffic4GIC2Activity.this.iccid + "&dn=" + Traffic4GIC2Activity.this.dn + "&pk=" + Traffic4GIC2Activity.this.pk;
                    if (Traffic4GIC2Activity.this.webview == null || str2 == null) {
                        return;
                    }
                    Log.e("链接", str2);
                    Traffic4GIC2Activity.this.webview.loadUrl(str2);
                }
            });
        }
    }
}
