package com.alibaba.sdk.android.openaccount.task;

import android.content.Context;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.callback.OneKeyCallback;
import com.alibaba.sdk.android.openaccount.executor.impl.ExecutorServiceImpl;
import com.alibaba.sdk.android.openaccount.rpc.model.RpcResponse;
import com.alibaba.sdk.android.openaccount.session.SessionManagerService;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.alibaba.sdk.android.openaccount.util.CommonUtils;
import com.alibaba.sdk.android.openaccount.util.RpcUtils;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class OneKeyCancelTask extends InitWaitTask {
    public OneKeyCancelTask(Context context, final String str, final OneKeyCallback oneKeyCallback) {
        super(context, oneKeyCallback, new Runnable() { // from class: com.alibaba.sdk.android.openaccount.task.OneKeyCancelTask.1
            @Override // java.lang.Runnable
            public void run() {
                HashMap map = new HashMap();
                if (((SessionManagerService) OpenAccountSDK.getService(SessionManagerService.class)) != null) {
                    map.put("onekeyToken", str);
                    RpcResponse rpcResponsePureInvokeWithoutRiskControlInfo = RpcUtils.pureInvokeWithoutRiskControlInfo("requestInfo", map, "ivonekeycancel");
                    if (rpcResponsePureInvokeWithoutRiskControlInfo.code != 1) {
                        CommonUtils.onFailure(oneKeyCallback, rpcResponsePureInvokeWithoutRiskControlInfo.code, rpcResponsePureInvokeWithoutRiskControlInfo.message);
                    } else if (oneKeyCallback != null) {
                        ExecutorServiceImpl.INSTANCE.postUITask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.task.OneKeyCancelTask.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                oneKeyCallback.onSuccess();
                            }
                        });
                    }
                }
            }
        }, UTConstants.E_ONE_KEY_CANCEL);
    }
}
