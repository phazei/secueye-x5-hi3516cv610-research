package com.aliyun.alink.linksdk.alcs.lpbs.bridge.a;

import com.aliyun.alink.linksdk.alcs.api.ICAMsgListener;
import com.aliyun.alink.linksdk.alcs.data.ica.ICARspMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: ICAMsgListenerWrapper.java */
/* JADX INFO: loaded from: classes2.dex */
public class k implements ICAMsgListener {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final String f4083b = "[AlcsLPBS]ICAMsgListenerWrapper";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected PalMsgListener f4084a;

    public k(PalMsgListener palMsgListener) {
        this.f4084a = palMsgListener;
    }

    @Override // com.aliyun.alink.linksdk.alcs.api.ICAMsgListener
    public void onLoad(ICARspMessage iCARspMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append("response code:");
        sb.append(iCARspMessage != null ? String.valueOf(iCARspMessage.code) : "response null");
        ALog.d(f4083b, sb.toString());
        PalMsgListener palMsgListener = this.f4084a;
        if (palMsgListener != null) {
            palMsgListener.onLoad(m.a(iCARspMessage));
        }
    }
}
