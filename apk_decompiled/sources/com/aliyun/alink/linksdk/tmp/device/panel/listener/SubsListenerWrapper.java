package com.aliyun.alink.linksdk.tmp.device.panel.listener;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class SubsListenerWrapper implements IConnectNotifyListener {
    private static final String TAG = "SubsListenerWrapper[Tmp]";
    protected String mIotId;
    protected IPanelEventCallback mSubListener;

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener
    public void onConnectStateChange(String str, ConnectState connectState) {
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener
    public boolean shouldHandle(String str, String str2) {
        return true;
    }

    public SubsListenerWrapper(String str, IPanelEventCallback iPanelEventCallback) {
        this.mSubListener = iPanelEventCallback;
        this.mIotId = str;
    }

    public IPanelEventCallback getmSubListener() {
        return this.mSubListener;
    }

    public String getmIotId() {
        return this.mIotId;
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener
    public void onNotify(String str, String str2, AMessage aMessage) {
        String string;
        IPanelEventCallback iPanelEventCallback = this.mSubListener;
        ALog.d(TAG, "onNotify mIotId:" + this.mIotId + " s:" + str + " s1:" + str2 + " panelEventCallback:" + iPanelEventCallback);
        if (iPanelEventCallback != null) {
            String str3 = (String) aMessage.getCachedItem("params");
            String str4 = (String) aMessage.getCachedItem("iotId");
            try {
                if (!TextUtils.isEmpty(str3) && !TextUtils.isEmpty(str4) && !TextUtils.isEmpty(this.mIotId) && this.mIotId.equalsIgnoreCase(str4)) {
                    iPanelEventCallback.onNotify(this.mIotId, str2, str3);
                    return;
                }
                if (aMessage.data instanceof byte[]) {
                    string = new String((byte[]) aMessage.data, "UTF-8");
                } else {
                    string = aMessage.data.toString();
                }
                JSONObject jSONObject = JSONObject.parseObject(string).getJSONObject("params");
                String string2 = jSONObject.getString("iotId");
                if (!TextUtils.isEmpty(string2)) {
                    aMessage.putCachedItem("iotId", string2);
                }
                aMessage.putCachedItem("params", jSONObject.toString());
                if (TextUtils.isEmpty(string2) || TextUtils.isEmpty(this.mIotId) || this.mIotId.equalsIgnoreCase(string2)) {
                    iPanelEventCallback.onNotify(this.mIotId, str2, jSONObject.toString());
                }
            } catch (Exception e) {
                ALog.e(TAG, "onNotify e:" + e.toString());
            }
        }
    }
}
