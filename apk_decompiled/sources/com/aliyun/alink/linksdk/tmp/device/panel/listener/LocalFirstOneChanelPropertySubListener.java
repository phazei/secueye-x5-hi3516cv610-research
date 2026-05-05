package com.aliyun.alink.linksdk.tmp.device.panel.listener;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.tmp.device.panel.linkselection.MultipleChannelDevice;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class LocalFirstOneChanelPropertySubListener extends SubsListenerWrapper {
    private static final String TAG = "[Tmp]LocalFirstOneChanelPropertySubListener";
    protected MultipleChannelDevice mMultipleChannelDevice;

    public LocalFirstOneChanelPropertySubListener(String str, IPanelEventCallback iPanelEventCallback, MultipleChannelDevice multipleChannelDevice) {
        super(str, iPanelEventCallback);
        this.mMultipleChannelDevice = multipleChannelDevice;
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.SubsListenerWrapper, com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener
    public void onNotify(String str, String str2, AMessage aMessage) {
        MultipleChannelDevice multipleChannelDevice;
        ALog.d(TAG, "onNotify mIotId:" + this.mIotId + " s:" + str + " s1:" + str2 + " panelEventCallback:" + this.mSubListener + " mMultipleChannelDevice:" + this.mMultipleChannelDevice);
        if (!TextUtils.isEmpty(str2) && str2.contains(TmpConstant.MQTT_TOPIC_PROPERTIES) && (multipleChannelDevice = this.mMultipleChannelDevice) != null && multipleChannelDevice.isLocalConnected()) {
            ALog.w(TAG, "mqtt onNotify property change not local connect . skip it");
        } else {
            super.onNotify(str, str2, aMessage);
        }
    }
}
