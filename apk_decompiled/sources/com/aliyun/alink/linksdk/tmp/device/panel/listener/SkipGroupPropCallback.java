package com.aliyun.alink.linksdk.tmp.device.panel.listener;

import androidx.annotation.Nullable;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class SkipGroupPropCallback extends SetGroupPropCallback {
    private static final String TAG = "[Tmp]SkipGroupPropCallback";

    public SkipGroupPropCallback(IPanelCallback iPanelCallback) {
        super(iPanelCallback);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.SetGroupPropCallback, com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
    public void onComplete(boolean z, @Nullable Object obj) {
        ALog.d(TAG, "SetGroupPropCallback onComplete isSuccess:" + z + " mCallback:" + this.mCallback);
    }
}
