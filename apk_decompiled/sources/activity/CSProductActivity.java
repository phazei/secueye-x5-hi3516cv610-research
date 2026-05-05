package activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import com.seculink.app.R;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class CSProductActivity extends CommonActivity {
    LinearLayout layout_main;
    TitleView tv_title;
    WebView webview;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_cs_product;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (LinearLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.webview = (WebView) findViewById(R.id.webview);
        this.tv_title = (TitleView) findViewById(R.id.tv_title);
        this.tv_title.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.CSProductActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                CSProductActivity.this.finish();
            }
        });
        String stringExtra = getIntent().getStringExtra("go2Url");
        WebSettings settings = this.webview.getSettings();
        settings.setCacheMode(2);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        this.webview.setWebViewClient(new WebViewClient() { // from class: activity.CSProductActivity.2
            @Override // android.webkit.WebViewClient
            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                super.onPageStarted(webView, str, bitmap);
            }

            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView webView, String str) {
                super.onPageFinished(webView, str);
                if (CSProductActivity.this.isFinishing()) {
                    return;
                }
                CSProductActivity.this.tv_title.setTitleText(!TextUtils.isEmpty(webView.getTitle()) ? webView.getTitle() : "");
            }
        });
        this.webview.loadUrl(stringExtra);
    }
}
