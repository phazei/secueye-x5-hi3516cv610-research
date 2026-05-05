package com.aliyun.alink.linksdk.tmp.device.panel.listener;

import androidx.annotation.Nullable;
import com.aliyun.alink.linksdk.tmp.utils.ResponseUtils;
import com.aliyun.alink.linksdk.tools.ALog;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class SetGroupPropCallback implements IPanelCallback {
    private static final String TAG = "[Tmp]SetGroupPropCallback";
    protected volatile IPanelCallback mCallback;

    public SetGroupPropCallback(IPanelCallback iPanelCallback) {
        this.mCallback = iPanelCallback;
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
    public void onComplete(boolean z, @Nullable Object obj) {
        IPanelCallback iPanelCallback = null;
        if (this.mCallback != null) {
            synchronized (this) {
                if (this.mCallback != null) {
                    IPanelCallback iPanelCallback2 = this.mCallback;
                    this.mCallback = null;
                    iPanelCallback = iPanelCallback2;
                }
            }
            if (iPanelCallback != null) {
                iPanelCallback.onComplete(z, ResponseUtils.getSuccessRspJson(new JSONObject()));
            }
        }
        ALog.d(TAG, "SetGroupPropCallback onComplete isSuccess:" + z + " mCallback:" + this.mCallback + " iPanelCallback:" + iPanelCallback);
    }
}
