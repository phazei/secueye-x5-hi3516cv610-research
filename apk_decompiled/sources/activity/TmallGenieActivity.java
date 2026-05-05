package activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.seculink.app.R;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class TmallGenieActivity extends CommonActivity {
    ConstraintLayout layout_main;
    WebView mWebView;
    TitleView tv_title;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_tmall_genie;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (ConstraintLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.mWebView = (WebView) findViewById(R.id.webview);
        this.tv_title = (TitleView) findViewById(R.id.tv_title);
        this.tv_title.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.TmallGenieActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                TmallGenieActivity.this.finish();
            }
        });
        this.mWebView.getSettings().setCacheMode(2);
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.mWebView.setWebViewClient(new WebViewClient() { // from class: activity.TmallGenieActivity.2
            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView webView, String str) {
                if (TmallGenieActivity.this.isFinishing() || TmallGenieActivity.this.tv_title == null || webView == null || webView.getTitle() == null) {
                    return;
                }
                TmallGenieActivity.this.tv_title.setTitleText(webView.getTitle());
            }

            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                if (TmallGenieActivity.this.isFinishing()) {
                    return false;
                }
                String strIsTokenUrl = TmallGenieActivity.this.isTokenUrl(str);
                if (!TextUtils.isEmpty(strIsTokenUrl)) {
                    TmallGenieActivity.this.bindAccount(strIsTokenUrl);
                    return true;
                }
                webView.loadUrl(str);
                return false;
            }
        });
        this.mWebView.loadUrl("https://oauth.taobao.com/authorize?response_type=code&client_id=26001873&redirect_uri=http://secueye.seculink.com&view=wap");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String isTokenUrl(String str) {
        if (TextUtils.isEmpty(str) || !str.contains("code=")) {
            return null;
        }
        String[] strArrSplit = str.split("code=");
        if (strArrSplit.length <= 1) {
            return null;
        }
        String[] strArrSplit2 = strArrSplit[1].split("&");
        if (strArrSplit2.length > 1) {
            return strArrSplit2[0];
        }
        return null;
    }

    public void bindAccount(String str) {
        JSONObject jSONObject = new JSONObject();
        if (str != null) {
            jSONObject.put("authCode", (Object) str);
        }
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setAuthType(AlinkConstants.KEY_IOT_AUTH).setApiVersion("1.0.5").setPath("/account/taobao/bind").setParams(jSONObject.getInnerMap()).setScheme(Scheme.HTTPS).build(), new IoTCallback() { // from class: activity.TmallGenieActivity.3
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                TmallGenieActivity.this.runOnUiThread(new Runnable() { // from class: activity.TmallGenieActivity.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Toast.makeText(TmallGenieActivity.this.getApplicationContext(), R.string.authorized_failed, 1).show();
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                if (ioTResponse.getCode() == 200) {
                    TmallGenieActivity.this.runOnUiThread(new Runnable() { // from class: activity.TmallGenieActivity.3.2
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(TmallGenieActivity.this.getApplicationContext(), R.string.authorized_succeed, 1).show();
                            TmallGenieActivity.this.finish();
                        }
                    });
                } else {
                    TmallGenieActivity.this.runOnUiThread(new Runnable() { // from class: activity.TmallGenieActivity.3.3
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(TmallGenieActivity.this.getApplicationContext(), R.string.authorized_failed, 1).show();
                        }
                    });
                }
            }
        });
    }
}
