package com.aliyun.alink.linksdk.tmp.device.panel.listener;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class EventNotifyListener extends SubsListenerWrapper {
    private static final String TAG = "[Tmp]EventNotifyListener";

    public EventNotifyListener(String str, IPanelEventCallback iPanelEventCallback) {
        super(str, iPanelEventCallback);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.SubsListenerWrapper, com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener
    public void onNotify(String str, String str2, AMessage aMessage) {
        String string;
        IPanelEventCallback iPanelEventCallback = this.mSubListener;
        ALog.d(TAG, "onNotify mIotId:" + this.mIotId + " s:" + str + " s1:" + str2 + " panelEventCallback:" + iPanelEventCallback);
        if (iPanelEventCallback != null) {
            try {
                if (aMessage.data instanceof byte[]) {
                    string = new String((byte[]) aMessage.data, "UTF-8");
                } else {
                    string = aMessage.data.toString();
                }
                JSONObject jSONObject = JSONObject.parseObject(string).getJSONObject("params");
                String string2 = null;
                if (jSONObject.containsKey("value")) {
                    JSONObject jSONObject2 = jSONObject.getJSONObject("value");
                    if (jSONObject2 != null && jSONObject2.containsKey("iotId")) {
                        string2 = jSONObject2.getString("iotId");
                    }
                } else if (jSONObject.containsKey("iotId")) {
                    string2 = jSONObject.getString("iotId");
                }
                if (string2 != null) {
                    if (string2.equals(this.mIotId)) {
                        iPanelEventCallback.onNotify(this.mIotId, str2, jSONObject.toString());
                        return;
                    } else {
                        ALog.d(TAG, "onNotify mIotId is not same return");
                        return;
                    }
                }
                iPanelEventCallback.onNotify(this.mIotId, str2, jSONObject.toString());
            } catch (Exception e) {
                ALog.e(TAG, "onNotify e:" + e.toString());
            }
        }
    }
}
