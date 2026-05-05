package com.aliyun.alink.linksdk.alcs.lpbs.bridge.a;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.alcs.lpbs.component.jsengine.IJSEngine;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalRspMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: CustomEventRspMsglistenerWrapper.java */
/* JADX INFO: loaded from: classes2.dex */
public class a implements PalMsgListener {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final String f4029d = "[AlcsLPBS]CustomEventRspMsglistenerWrapper";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected PalMsgListener f4030a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected String f4031b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected IJSEngine f4032c;

    public a(PalMsgListener palMsgListener, String str, IJSEngine iJSEngine) {
        this.f4030a = palMsgListener;
        this.f4031b = str;
        this.f4032c = iJSEngine;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener
    public void onLoad(PalRspMessage palRspMessage) {
        if (palRspMessage == null) {
            ALog.e(f4029d, "onLoad ioTRspMessage or paylod null");
        } else {
            String strRawDataToProtocol = null;
            ALog.d(f4029d, "onLoad response code:" + palRspMessage.code + " mJsEngine:" + this.f4032c + " mJsCode isempty:" + TextUtils.isEmpty(this.f4031b));
            if (this.f4032c != null && !TextUtils.isEmpty(this.f4031b)) {
                strRawDataToProtocol = this.f4032c.rawDataToProtocol(this.f4031b, palRspMessage.payload);
            }
            if (!TextUtils.isEmpty(strRawDataToProtocol)) {
                palRspMessage.payload = strRawDataToProtocol.getBytes();
            }
        }
        PalMsgListener palMsgListener = this.f4030a;
        if (palMsgListener != null) {
            palMsgListener.onLoad(palRspMessage);
        }
    }
}
