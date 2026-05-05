package com.alibaba.sdk.android.openaccount.webview;

import android.app.Activity;
import android.text.TextUtils;
import android.webkit.WebView;
import com.alibaba.sdk.android.openaccount.executor.ExecutorService;
import com.alibaba.sdk.android.pluto.Pluto;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class BridgeCallbackContext {
    public int requestId;
    public WebView webView;

    public Activity getActivity() {
        return (Activity) this.webView.getContext();
    }

    public void success(final String str) {
        ((ExecutorService) Pluto.DEFAULT_INSTANCE.getBean(ExecutorService.class)).postUITask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.webview.BridgeCallbackContext.1
            @Override // java.lang.Runnable
            public void run() {
                String str2;
                if (TextUtils.isEmpty(str)) {
                    str2 = String.format("javascript:window.HavanaBridge.onSuccess(%s);", Integer.valueOf(BridgeCallbackContext.this.requestId));
                } else {
                    str2 = String.format("javascript:window.HavanaBridge.onSuccess(%s,'%s');", Integer.valueOf(BridgeCallbackContext.this.requestId), BridgeCallbackContext.formatJsonString(str));
                }
                BridgeCallbackContext.this.callback(str2);
            }
        });
    }

    public void onFailure(int i, String str) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("code", i);
            jSONObject.put("message", str);
            onFailure(jSONObject.toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void onFailure(final String str) {
        ((ExecutorService) Pluto.DEFAULT_INSTANCE.getBean(ExecutorService.class)).postUITask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.webview.BridgeCallbackContext.2
            @Override // java.lang.Runnable
            public void run() {
                String str2;
                if (TextUtils.isEmpty(str)) {
                    str2 = String.format("javascript:window.HavanaBridge.onFailure(%s,'');", Integer.valueOf(BridgeCallbackContext.this.requestId));
                } else {
                    str2 = String.format("javascript:window.HavanaBridge.onFailure(%s,'%s');", Integer.valueOf(BridgeCallbackContext.this.requestId), BridgeCallbackContext.formatJsonString(str));
                }
                BridgeCallbackContext.this.callback(str2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callback(String str) {
        WebView webView = this.webView;
        if (webView == null) {
            return;
        }
        webView.loadUrl(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String formatJsonString(String str) {
        return str.replace("\\", "\\\\");
    }
}
