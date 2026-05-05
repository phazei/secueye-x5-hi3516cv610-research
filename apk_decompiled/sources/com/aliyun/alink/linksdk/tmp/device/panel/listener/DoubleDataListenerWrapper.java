package com.aliyun.alink.linksdk.tmp.device.panel.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.tmp.service.UTPoint;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class DoubleDataListenerWrapper extends ConnectListenerWrapper {
    public DoubleDataListenerWrapper(IPanelCallback iPanelCallback) {
        super(iPanelCallback);
    }

    public DoubleDataListenerWrapper(UTPoint uTPoint, IPanelCallback iPanelCallback) {
        super(uTPoint, null, iPanelCallback);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.ConnectListenerWrapper, com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
    public void onResponse(ARequest aRequest, AResponse aResponse) {
        try {
            JSONObject object = JSON.parseObject(aResponse.data.toString());
            JSONObject jSONObject = object.getJSONObject("data");
            if (jSONObject != null) {
                object.put("data", (Object) jSONObject.getJSONObject("data"));
                aResponse.data = object.toString();
            }
        } catch (Throwable th) {
            ALog.e("[Tmp]ConnectListenerWrapper", "DoubleDataListenerWrapper onResponse error : " + th.toString());
        }
        super.onResponse(aRequest, aResponse);
    }
}
