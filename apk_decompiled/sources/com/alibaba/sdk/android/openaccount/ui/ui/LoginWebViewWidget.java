package com.alibaba.sdk.android.openaccount.ui.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.security.SecurityGuardService;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.alibaba.sdk.android.openaccount.ui.bridge.LoginBridge;
import com.alibaba.sdk.android.openaccount.ui.util.OpenAccountUIUtils;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;
import com.alibaba.sdk.android.openaccount.webview.BridgeWebChromeClient;
import com.alibaba.sdk.android.openaccount.webview.TaeWebView;
import com.alibaba.sdk.android.pluto.Pluto;
import com.huawei.hms.framework.common.ContainerUtils;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class LoginWebViewWidget extends LinearLayout {
    private TaeWebView webView;

    public LoginWebViewWidget(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(ResourceUtils.getRLayout(context, "ali_sdk_openaccount_login_web_view_widget"), (ViewGroup) this, true);
        this.webView = (TaeWebView) findViewById(ResourceUtils.getRId(getContext(), "web_view"));
        this.webView.addBridgeObject(OpenAccountUIConstants.loginBridgeName, new LoginBridge());
        this.webView.setWebChromeClient(new BridgeWebChromeClient());
    }

    public void loadQrLoginUrl(Map<String, String> map) {
        StringBuilder sb = new StringBuilder(String.format(ConfigManager.getInstance().getStringProperty(ConfigManager.getInstance().getEnvironment().name() + "_QR_CODE_LOGIN_URL", "http://login.m.taobao.com/qrcodeShow.htm?appKey=%s"), ((SecurityGuardService) Pluto.DEFAULT_INSTANCE.getBean(SecurityGuardService.class)).getAppKey()));
        try {
            sb.append("&from=");
            sb.append(URLEncoder.encode(OpenAccountUIUtils.getQrFromParameter(map), "utf-8"));
        } catch (Exception e) {
            AliSDKLogger.e(OpenAccountUIConstants.LOG_TAG, "fail to encode the url", e);
        }
        if (map != null && !TextUtils.isEmpty(map.get("config"))) {
            String strGenerateQueryUrl = generateQueryUrl(map.get("config"));
            if (!TextUtils.isEmpty(strGenerateQueryUrl)) {
                sb.append(strGenerateQueryUrl);
            }
        }
        this.webView.loadUrl(sb.toString());
    }

    public TaeWebView getWebView() {
        return this.webView;
    }

    public void destroyWebView() {
        TaeWebView taeWebView = this.webView;
        if (taeWebView != null) {
            ViewGroup viewGroup = (ViewGroup) taeWebView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(this.webView);
            }
            this.webView.removeAllViews();
            this.webView.destroy();
        }
    }

    private String generateQueryUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        try {
            JSONObject jSONObject = new JSONObject(str);
            Iterator<String> itKeys = jSONObject.keys();
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                if (!TextUtils.isEmpty(next)) {
                    String strOptString = jSONObject.optString(next);
                    if (!TextUtils.isEmpty(strOptString)) {
                        sb.append("&");
                        sb.append(URLEncoder.encode(next, "utf-8"));
                        sb.append(ContainerUtils.KEY_VALUE_DELIMITER);
                        sb.append(URLEncoder.encode(strOptString, "utf-8"));
                    }
                }
            }
        } catch (Exception e) {
            AliSDKLogger.e(OpenAccountUIConstants.LOG_TAG, "fail to generate query url", e);
        }
        return sb.toString();
    }
}
