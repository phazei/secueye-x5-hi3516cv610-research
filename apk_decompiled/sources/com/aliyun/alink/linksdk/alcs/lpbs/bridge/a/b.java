package com.aliyun.alink.linksdk.alcs.lpbs.bridge.a;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.alcs.lpbs.component.jsengine.IJSEngine;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalRspMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: CustomFormatMsgListenerWrapper.java */
/* JADX INFO: loaded from: classes2.dex */
public class b implements PalMsgListener {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final String f4034d = "[AlcsLPBS]CustomFormatMsgListenerWrapper";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected PalMsgListener f4035a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected String f4036b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected IJSEngine f4037c;

    public b(PalMsgListener palMsgListener, String str, IJSEngine iJSEngine) {
        this.f4035a = palMsgListener;
        this.f4036b = str;
        this.f4037c = iJSEngine;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener
    public void onLoad(PalRspMessage palRspMessage) {
        if (palRspMessage == null) {
            ALog.e(f4034d, "onLoad ioTRspMessage or paylod null");
        } else {
            ALog.d(f4034d, "onLoad response code:" + palRspMessage.code + "  mJsEngine:" + this.f4037c);
            IJSEngine iJSEngine = this.f4037c;
            String strRawDataToProtocol = iJSEngine != null ? iJSEngine.rawDataToProtocol(this.f4036b, palRspMessage.payload) : null;
            if (!TextUtils.isEmpty(strRawDataToProtocol)) {
                palRspMessage.payload = strRawDataToProtocol.getBytes();
            }
        }
        PalMsgListener palMsgListener = this.f4035a;
        if (palMsgListener != null) {
            palMsgListener.onLoad(palRspMessage);
        }
    }
}
