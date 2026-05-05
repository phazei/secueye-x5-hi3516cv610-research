package com.alibaba.sdk.android.openaccount.ui.task;

import android.text.TextUtils;
import com.alibaba.sdk.android.openaccount.device.DeviceManager;
import com.alibaba.sdk.android.openaccount.message.Message;
import com.alibaba.sdk.android.openaccount.message.MessageConstants;
import com.alibaba.sdk.android.openaccount.message.MessageUtils;
import com.alibaba.sdk.android.openaccount.rpc.RpcService;
import com.alibaba.sdk.android.openaccount.rpc.model.RpcRequest;
import com.alibaba.sdk.android.openaccount.rpc.model.RpcResponse;
import com.alibaba.sdk.android.openaccount.task.AbsAsyncTask;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.alibaba.sdk.android.openaccount.ui.callback.QrConfirmLoginCallback;
import com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountUIServiceImpl;
import com.alibaba.sdk.android.openaccount.util.RpcUtils;
import com.alibaba.sdk.android.openaccount.webview.BridgeCallbackContext;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import com.facebook.internal.NativeProtocol;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class QrLoginConfirmTask extends AbsAsyncTask<String, Void, Void> {
    private BridgeCallbackContext bridgeCallbackContext;

    @Autowired
    private DeviceManager deviceManager;

    @Autowired
    private RpcService rpcService;

    @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
    protected void doFinally() {
    }

    public QrLoginConfirmTask(BridgeCallbackContext bridgeCallbackContext) {
        this.bridgeCallbackContext = bridgeCallbackContext;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
    public Void asyncExecute(String... strArr) {
        boolean z;
        if (strArr == null || strArr.length == 0) {
            onUserCancelFailure(true);
            return null;
        }
        try {
            RpcRequest rpcRequestCreate = RpcRequest.create(strArr[0]);
            Map map = (Map) rpcRequestCreate.params.get("qrConfirmInfo");
            String str = (String) map.get(NativeProtocol.WEB_DIALOG_ACTION);
            if (map != null && !TextUtils.isEmpty(str)) {
                if ("c".equals(str)) {
                    this.bridgeCallbackContext.getActivity().finish();
                    z = false;
                } else {
                    z = true;
                }
                try {
                    map.put("utdid", this.deviceManager.getUtdid());
                    RpcResponse rpcResponsePureInvokeWithRiskControlInfo = RpcUtils.pureInvokeWithRiskControlInfo(rpcRequestCreate, "qrConfirmInfo");
                    if (rpcResponsePureInvokeWithRiskControlInfo == null) {
                        onFailure(z, MessageConstants.GENERIC_RPC_ERROR, MessageUtils.getMessageContent(MessageConstants.GENERIC_RPC_ERROR, new Object[0]));
                        return null;
                    }
                    if (rpcResponsePureInvokeWithRiskControlInfo.code == 1) {
                        if ("c".equals(str)) {
                            onUserCancelFailure(z);
                        } else {
                            onSuccess();
                        }
                    } else {
                        onFailure(z, rpcResponsePureInvokeWithRiskControlInfo.code, rpcResponsePureInvokeWithRiskControlInfo.message);
                    }
                    return null;
                } catch (Exception e) {
                    e = e;
                    AliSDKLogger.e(OpenAccountUIConstants.LOG_TAG, "fail to parse the response", e);
                    onUserCancelFailure(z);
                    return null;
                }
            }
            onUserCancelFailure(true);
            return null;
        } catch (Exception e2) {
            e = e2;
            z = true;
        }
    }

    private void onFailure(boolean z, final int i, final String str) {
        if (z) {
            this.bridgeCallbackContext.getActivity().finish();
        }
        final QrConfirmLoginCallback qrConfirmLoginCallback = OpenAccountUIServiceImpl._qrLoginConfirmCallback;
        if (qrConfirmLoginCallback != null) {
            this.executorService.postUITask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.ui.task.QrLoginConfirmTask.1
                @Override // java.lang.Runnable
                public void run() {
                    qrConfirmLoginCallback.onFailure(i, str);
                }
            });
        }
    }

    private void onUserCancelFailure(boolean z) {
        onFailure(z, 10003, MessageUtils.getMessageContent(10003, new Object[0]));
    }

    @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
    protected void doWhenException(Throwable th) {
        Message messageCreateMessage = MessageUtils.createMessage(MessageConstants.GENERIC_SYSTEM_ERROR, th.getMessage());
        AliSDKLogger.log(OpenAccountUIConstants.LOG_TAG, messageCreateMessage, th);
        this.bridgeCallbackContext.onFailure(messageCreateMessage.code, messageCreateMessage.message);
        this.bridgeCallbackContext.getActivity().finish();
    }

    private void onSuccess() {
        this.bridgeCallbackContext.getActivity().finish();
        final QrConfirmLoginCallback qrConfirmLoginCallback = OpenAccountUIServiceImpl._qrLoginConfirmCallback;
        if (qrConfirmLoginCallback != null) {
            this.executorService.postUITask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.ui.task.QrLoginConfirmTask.2
                @Override // java.lang.Runnable
                public void run() {
                    qrConfirmLoginCallback.onSuccess();
                }
            });
        }
    }
}
