package com.aliyun.alink.linksdk.tmp.device.panel.listener;

import androidx.annotation.Nullable;
import com.aliyun.alink.linksdk.tmp.device.panel.data.PanelMethodExtraData;
import com.aliyun.alink.linksdk.tmp.device.panel.linkselection.LocalGroup;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class LocalLastTryListenerWrapper implements IPanelCallback {
    private static final String TAG = "[Tmp]LocalLastTryListenerWrapper";
    private IPanelCallback mCallback;
    private PanelMethodExtraData mExtraData;
    private LocalGroup mLocalGroup;
    private String mParams;

    public LocalLastTryListenerWrapper(IPanelCallback iPanelCallback, LocalGroup localGroup, String str, PanelMethodExtraData panelMethodExtraData) {
        this.mCallback = iPanelCallback;
        this.mLocalGroup = localGroup;
        this.mParams = str;
        this.mExtraData = panelMethodExtraData;
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
    public void onComplete(boolean z, @Nullable Object obj) {
        LocalGroup localGroup;
        ALog.d(TAG, "onComplete isSuccess mLocalGroup:" + this.mLocalGroup + " mCallback:" + this.mCallback);
        if (!z && (localGroup = this.mLocalGroup) != null) {
            localGroup.setGroupProperties(null, this.mParams, this.mCallback, this.mExtraData);
            return;
        }
        IPanelCallback iPanelCallback = this.mCallback;
        if (iPanelCallback != null) {
            iPanelCallback.onComplete(z, obj);
        }
    }
}
