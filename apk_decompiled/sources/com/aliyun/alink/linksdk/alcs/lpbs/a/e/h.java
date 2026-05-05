package com.aliyun.alink.linksdk.alcs.lpbs.a.e;

import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalRspMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: PkDnChangeListener.java */
/* JADX INFO: loaded from: classes2.dex */
public class h implements PalMsgListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f4012a = "[AlcsLPBS]PkDnChangeListener";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private PalMsgListener f4013b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private PalDeviceInfo f4014c;

    public h(PalDeviceInfo palDeviceInfo, PalMsgListener palMsgListener) {
        this.f4013b = palMsgListener;
        this.f4014c = palDeviceInfo;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener
    public void onLoad(PalRspMessage palRspMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append("onLoad response code:");
        sb.append(palRspMessage != null ? String.valueOf(palRspMessage.code) : " response null");
        ALog.d(f4012a, sb.toString());
        PalMsgListener palMsgListener = this.f4013b;
        if (palMsgListener != null) {
            palMsgListener.onLoad(palRspMessage);
        }
    }
}
