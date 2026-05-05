package com.aliyun.alink.linksdk.alcs.lpbs.a.e;

import com.aliyun.alink.linksdk.alcs.lpbs.api.PluginMgr;
import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IThingCloudChannel;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalRspMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: UpToCloud.java */
/* JADX INFO: loaded from: classes2.dex */
public class m implements PalMsgListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f4025a = "[AlcsLPBS]UpToCloud";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private PalMsgListener f4026b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private String f4027c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private IThingCloudChannel f4028d;
    private PalDeviceInfo e;

    public m(PalDeviceInfo palDeviceInfo, IThingCloudChannel iThingCloudChannel, String str, PalMsgListener palMsgListener) {
        ALog.d(f4025a, "UpToCloud cloudChannel:" + iThingCloudChannel + " topic:" + iThingCloudChannel + " listener:" + palMsgListener);
        this.f4026b = palMsgListener;
        this.f4027c = str;
        this.f4028d = iThingCloudChannel;
        this.e = palDeviceInfo;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener
    public void onLoad(PalRspMessage palRspMessage) {
        ALog.d(f4025a, "onLoad mCloudChannel:" + this.f4028d + " mListener:" + this.f4026b + " topic:" + this.f4027c + " response:" + palRspMessage);
        if (this.f4028d != null && PluginMgr.getInstance().isDataToCloud(this.e)) {
            this.f4028d.reportData(this.f4027c, palRspMessage.payload);
        }
        PalMsgListener palMsgListener = this.f4026b;
        if (palMsgListener != null) {
            palMsgListener.onLoad(palRspMessage);
        }
    }
}
