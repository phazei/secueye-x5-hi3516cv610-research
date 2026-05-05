package com.alibaba.sdk.android.oauth.alipay;

import android.content.Context;
import anet.channel.strategy.dispatch.DispatchConstants;
import com.alibaba.sdk.android.openaccount.executor.ExecutorService;
import com.alibaba.sdk.android.openaccount.message.MessageConstants;
import com.alibaba.sdk.android.openaccount.message.MessageUtils;
import com.alibaba.sdk.android.openaccount.rpc.model.RpcResponse;
import com.alibaba.sdk.android.openaccount.task.TaskWithDialog;
import com.alibaba.sdk.android.openaccount.util.CommonUtils;
import com.alibaba.sdk.android.openaccount.util.RpcUtils;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import com.aliyun.ams.emas.push.notification.AgooMessageNotification;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class GetAlipaySignTask extends TaskWithDialog<Void, Void, Void> {

    @Autowired
    private ExecutorService executorService;
    private GetSignCallback mGetSignCallback;
    private SignRequest mSignRequest;

    @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
    protected void doWhenException(Throwable th) {
    }

    public GetAlipaySignTask(Context context, GetSignCallback getSignCallback, SignRequest signRequest) {
        super(context);
        this.mGetSignCallback = getSignCallback;
        this.mSignRequest = signRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
    public Void asyncExecute(Void... voidArr) {
        HashMap map = new HashMap();
        map.put(AgooMessageNotification.APP_ID, this.mSignRequest.app_id);
        map.put("pid", this.mSignRequest.pid);
        map.put(DispatchConstants.SIGNTYPE, this.mSignRequest.sign_type);
        RpcResponse rpcResponsePureInvokeWithoutRiskControlInfo = RpcUtils.pureInvokeWithoutRiskControlInfo("alipaySignatureRequest", map, "alipaysignature");
        if (rpcResponsePureInvokeWithoutRiskControlInfo != null && rpcResponsePureInvokeWithoutRiskControlInfo.data != null) {
            final String strOptString = rpcResponsePureInvokeWithoutRiskControlInfo.data.optString("requestStr");
            if (this.mGetSignCallback == null) {
                return null;
            }
            this.executorService.postUITask(new Runnable() { // from class: com.alibaba.sdk.android.oauth.alipay.GetAlipaySignTask.1
                @Override // java.lang.Runnable
                public void run() {
                    GetAlipaySignTask.this.mGetSignCallback.onGetSignSuccessed(strOptString);
                }
            });
            return null;
        }
        CommonUtils.onFailure(this.mGetSignCallback, MessageUtils.createMessage(MessageConstants.GENERIC_SYSTEM_ERROR, new Object[0]));
        return null;
    }
}
