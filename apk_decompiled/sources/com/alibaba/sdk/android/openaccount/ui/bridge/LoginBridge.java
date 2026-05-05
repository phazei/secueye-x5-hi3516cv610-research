package com.alibaba.sdk.android.openaccount.ui.bridge;

import com.alibaba.sdk.android.openaccount.ui.task.LoginByQrCodeTask;
import com.alibaba.sdk.android.openaccount.ui.task.QrLoginConfirmTask;
import com.alibaba.sdk.android.openaccount.webview.BridgeCallbackContext;
import com.alibaba.sdk.android.openaccount.webview.BridgeMethod;

/* JADX INFO: loaded from: classes.dex */
public class LoginBridge {
    @BridgeMethod
    public void qrLoginSuccess(BridgeCallbackContext bridgeCallbackContext, String str) {
        new LoginByQrCodeTask(bridgeCallbackContext).execute(new String[]{str});
    }

    @BridgeMethod
    public void openAccountQrLoginConfirm(BridgeCallbackContext bridgeCallbackContext, String str) {
        new QrLoginConfirmTask(bridgeCallbackContext).execute(new String[]{str});
    }
}
