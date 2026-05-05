package com.aliyun.alink.linksdk.alcs.lpbs.a.e;

import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalConnect;
import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IDataDownListener;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalReqMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalRspMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: DataDownListenerProxy.java */
/* JADX INFO: loaded from: classes2.dex */
public class b implements IDataDownListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f3979a = "[AlcsLPBS]DataDownListenerProxy";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private IPalConnect f3980b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private PalDeviceInfo f3981c;

    public b(IPalConnect iPalConnect, PalDeviceInfo palDeviceInfo) {
        this.f3980b = iPalConnect;
        this.f3981c = palDeviceInfo;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IDataDownListener
    public void onDataDown(String str, byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        sb.append("onDataDown mConnect:");
        sb.append(this.f3980b);
        sb.append(" topic:");
        sb.append(str);
        sb.append(" payload:");
        sb.append(bArr);
        sb.append(" payloadstr:");
        sb.append(bArr == null ? " null" : new String(bArr));
        ALog.d(f3979a, sb.toString());
        if (this.f3980b != null) {
            PalReqMessage palReqMessage = new PalReqMessage();
            palReqMessage.topic = str;
            palReqMessage.payload = bArr;
            palReqMessage.deviceInfo = this.f3981c;
            this.f3980b.asyncSendRequest(palReqMessage, new PalMsgListener() { // from class: com.aliyun.alink.linksdk.alcs.lpbs.a.e.b.1
                @Override // com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener
                public void onLoad(PalRspMessage palRspMessage) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onDataDown asyncSendRequest onLoad code:");
                    sb2.append(palRspMessage == null ? "error" : Integer.valueOf(palRspMessage.code));
                    ALog.d(b.f3979a, sb2.toString());
                }
            });
        }
    }
}
