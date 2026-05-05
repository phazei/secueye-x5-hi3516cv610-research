package activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.seculink.app.R;
import tools.Utils;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class PayYunServiceActivity extends CommonActivity {
    TextView check_order;
    private String iotId;
    LinearLayout layout_main;
    TitleView tv_title;
    WebView webview;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_pay_yun_service;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (LinearLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.webview = (WebView) findViewById(R.id.webview);
        this.tv_title = (TitleView) findViewById(R.id.tv_title);
        this.check_order = (TextView) findViewById(R.id.check_order);
        this.iotId = getIntent().getStringExtra("iotId");
        final String str = "http://pay.umeye.com/shopping/product.html?pt=101&uid=" + this.iotId + "&appid=2000000007&user=" + Utils.getUserPhone() + "&channel=0";
        this.tv_title.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.PayYunServiceActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                PayYunServiceActivity.this.finish();
            }
        });
        this.check_order.setOnClickListener(new View.OnClickListener() { // from class: activity.PayYunServiceActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                String strReplace = str.replace("product.html", "order.html");
                Intent intent = new Intent(PayYunServiceActivity.this, (Class<?>) CSProductActivity.class);
                intent.putExtra("go2Url", strReplace);
                PayYunServiceActivity.this.startActivity(intent);
            }
        });
        this.webview.getSettings().setCacheMode(2);
        this.webview.getSettings().setJavaScriptEnabled(true);
        this.webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.webview.setWebViewClient(new WebViewClient() { // from class: activity.PayYunServiceActivity.3
            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView, String str2) {
                if (!PayYunServiceActivity.this.isFinishing()) {
                    if (str2.startsWith("alipays://platformapi") || str2.startsWith("weixin://wap/pay?") || str2.startsWith("weixin://dl/business/?")) {
                        PayYunServiceActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str2)));
                        return true;
                    }
                    if (str2.contains("more_info.html")) {
                        Intent intent = new Intent(PayYunServiceActivity.this, (Class<?>) YunServiceStatementActivity.class);
                        intent.putExtra("go2Url", str2);
                        PayYunServiceActivity.this.startActivity(intent);
                        return true;
                    }
                }
                return super.shouldOverrideUrlLoading(webView, str2);
            }

            @Override // android.webkit.WebViewClient
            public void onPageStarted(WebView webView, String str2, Bitmap bitmap) {
                super.onPageStarted(webView, str2, bitmap);
                if (str2.contains("product.html")) {
                    PayYunServiceActivity.this.tv_title.setTitleText(PayYunServiceActivity.this.getResources().getString(R.string.cloud_server));
                }
            }

            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView webView, String str2) {
                super.onPageFinished(webView, str2);
                if (PayYunServiceActivity.this.isFinishing() || str2.contains("product.html")) {
                    return;
                }
                String title = webView.getTitle();
                if (TextUtils.isEmpty(title)) {
                    return;
                }
                PayYunServiceActivity.this.tv_title.setTitleText(title);
            }
        });
        this.webview.loadUrl(str);
    }
}
