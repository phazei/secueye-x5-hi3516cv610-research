package com.alibaba.sdk.android.openaccount.ui.bridge;

import android.app.Activity;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;

/* JADX INFO: loaded from: classes.dex */
public class WVJSBridge extends WVApiPlugin {
    protected WVCallBackContext mCallback;

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if (!"closeWindow".equals(str)) {
            return false;
        }
        closeWindow(str2, wVCallBackContext);
        return true;
    }

    public synchronized void closeWindow(String str, WVCallBackContext wVCallBackContext) {
        try {
            ((Activity) this.mContext).finish();
        } catch (Exception unused) {
            setErrorCallback(-1, "");
        }
    }

    private void setErrorCallback(int i, String str) {
        if (this.mCallback != null) {
            WVResult wVResult = new WVResult();
            wVResult.addData("code", Integer.valueOf(i));
            wVResult.addData("message", str);
            this.mCallback.error(wVResult);
        }
    }

    private void setSuccessCallback() {
        if (this.mCallback != null) {
            WVResult wVResult = new WVResult();
            wVResult.setResult("HY_SUCCESS");
            this.mCallback.success(wVResult);
        }
    }
}
