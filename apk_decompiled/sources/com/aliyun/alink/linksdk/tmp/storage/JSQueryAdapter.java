package com.aliyun.alink.linksdk.tmp.storage;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IJsProvider;
import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IJsQeuryCallback;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.tmp.utils.CloudUtils;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class JSQueryAdapter implements IJsProvider {
    private static final String TAG = "[Tmp]JSQueryAdapter";

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IJsProvider
    public void queryJsCode(final String str, String str2, final IJsQeuryCallback iJsQeuryCallback) {
        String script = TmpStorage.getInstance().getScript(str);
        if (!TextUtils.isEmpty(script)) {
            iJsQeuryCallback.onLoad(str, script);
        } else {
            CloudUtils.queryProductJsCode(str, str2, str, null, null, new IConnectSendListener() { // from class: com.aliyun.alink.linksdk.tmp.storage.JSQueryAdapter.1
                @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
                public void onResponse(ARequest aRequest, AResponse aResponse) {
                    if (aResponse == null || aResponse.data == null) {
                        ALog.e(JSQueryAdapter.TAG, "queryJsCode onResponse data null");
                        iJsQeuryCallback.onLoad(str, null);
                    }
                }

                @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
                public void onFailure(ARequest aRequest, AError aError) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("queryJsCode onFailure error:");
                    sb.append(aError);
                    ALog.e(JSQueryAdapter.TAG, sb.toString() == null ? TmpConstant.GROUP_ROLE_UNKNOWN : aError.toString());
                    iJsQeuryCallback.onLoad(str, null);
                }
            });
        }
    }
}
