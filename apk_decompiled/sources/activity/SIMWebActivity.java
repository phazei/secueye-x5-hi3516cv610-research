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
import bean.DeviceInfoBeans;
import com.alibaba.cloudapi.sdk.constant.HttpConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.seculink.app.R;
import com.taobao.accs.common.Constants;
import fragment.MyAccountTabFragment;
import java.io.IOException;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tools.SharePreferenceManager;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class SIMWebActivity extends CommonActivity {
    private String iccid = "";
    private String imei = "";
    RelativeLayout layout_main;
    TitleView tv_title;
    WebView webview;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_sim_web;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (RelativeLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.webview = (WebView) findViewById(R.id.webview);
        this.tv_title = (TitleView) findViewById(R.id.tv_title);
        this.tv_title.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.SIMWebActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                SIMWebActivity.this.finish();
            }
        });
        this.iccid = getIntent().getStringExtra("iccid");
        this.imei = getIntent().getStringExtra(Constants.KEY_IMEI);
        Log.e(config.Constants.IMEI, "" + this.iccid);
        Log.e(config.Constants.IMEI, "" + this.imei);
        runOnUiThread(new Runnable() { // from class: activity.SIMWebActivity.2
            @Override // java.lang.Runnable
            public void run() {
                if (SIMWebActivity.this.imei != null && !SIMWebActivity.this.imei.isEmpty()) {
                    String str = "https://traffic.secueye.cn/?imei=" + SIMWebActivity.this.imei;
                    if (SIMWebActivity.this.webview != null && str != null) {
                        Log.e("链接", str);
                        SIMWebActivity.this.webview.loadUrl(str);
                    }
                    SIMWebActivity sIMWebActivity = SIMWebActivity.this;
                    sIMWebActivity.postImei(sIMWebActivity.imei, SIMWebActivity.this.iccid);
                }
                if (SIMWebActivity.this.iccid == null || SIMWebActivity.this.iccid.isEmpty()) {
                    return;
                }
                String str2 = "https://traffic.secueye.app/?iccidx=" + SIMWebActivity.this.iccid;
                if (SIMWebActivity.this.webview != null && str2 != null) {
                    Log.e("链接", str2);
                    SIMWebActivity.this.webview.loadUrl(str2);
                }
                SIMWebActivity sIMWebActivity2 = SIMWebActivity.this;
                sIMWebActivity2.postImei(sIMWebActivity2.imei, SIMWebActivity.this.iccid);
            }
        });
        WebSettings settings = this.webview.getSettings();
        settings.setCacheMode(2);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        this.webview.setWebViewClient(new WebViewClient() { // from class: activity.SIMWebActivity.3
            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                if (SIMWebActivity.this.isFinishing()) {
                    return false;
                }
                if (!str.startsWith("alipays://platformapi") && !str.startsWith("weixin://wap/pay?") && !str.startsWith("weixin://dl/business/?")) {
                    return false;
                }
                SIMWebActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
                return true;
            }

            @Override // android.webkit.WebViewClient
            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                super.onPageStarted(webView, str, bitmap);
            }

            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView webView, String str) {
                super.onPageFinished(webView, str);
                if (SIMWebActivity.this.isFinishing() || SIMWebActivity.this.tv_title == null || webView == null || webView.getTitle() == null || "".equals(webView.getTitle())) {
                    return;
                }
                SIMWebActivity.this.tv_title.setTitleText(!TextUtils.isEmpty(webView.getTitle()) ? webView.getTitle() : "");
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedSslError(WebView webView, final SslErrorHandler sslErrorHandler, SslError sslError) {
                AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());
                builder.setMessage("SSL认证失败，是否继续访问？");
                builder.setPositiveButton(SIMWebActivity.this.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() { // from class: activity.SIMWebActivity.3.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sslErrorHandler.proceed();
                    }
                });
                builder.setNegativeButton(SIMWebActivity.this.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() { // from class: activity.SIMWebActivity.3.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sslErrorHandler.cancel();
                    }
                });
                builder.create().show();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postImei(String str, String str2) {
        boolean z;
        List array;
        String allIMEIList = SharePreferenceManager.getInstance().getAllIMEIList();
        if (allIMEIList.isEmpty() || (array = JSON.parseArray(allIMEIList, DeviceInfoBeans.class)) == null) {
            z = false;
        } else {
            z = false;
            for (int i = 0; i < array.size(); i++) {
                if (str != null && !str.isEmpty() && array.get(i) != null && !((DeviceInfoBeans) array.get(i)).imei.isEmpty() && str.equals(((DeviceInfoBeans) array.get(i)).imei)) {
                    z = true;
                }
                if (str2 != null && !str2.isEmpty() && array.get(i) != null && !((DeviceInfoBeans) array.get(i)).iccid.isEmpty() && str2.equals(((DeviceInfoBeans) array.get(i)).iccid)) {
                    z = true;
                }
            }
        }
        if (z) {
            Log.e(config.Constants.IMEI, "存在");
            return;
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("uid", (Object) MyAccountTabFragment.getUserNick());
            if (str != null && !str.isEmpty()) {
                jSONObject.put(Constants.KEY_IMEI, (Object) str);
            }
            if (str2 != null && !str2.isEmpty()) {
                jSONObject.put("iccid", (Object) str2);
            }
            Log.e(Constants.KEY_IMEI, "json=" + jSONObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        okHttpClient.newCall(new Request.Builder().url("https://traffic.secueye.app/api/app/create/record").post(RequestBody.create(MediaType.parse(HttpConstant.CLOUDAPI_CONTENT_TYPE_JSON), jSONObject.toString())).build()).enqueue(new Callback() { // from class: activity.SIMWebActivity.4
            @Override // okhttp3.Callback
            public void onFailure(Call call, IOException iOException) {
                iOException.printStackTrace();
            }

            @Override // okhttp3.Callback
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.e(Constants.KEY_IMEI, "" + response.body().string());
                }
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
