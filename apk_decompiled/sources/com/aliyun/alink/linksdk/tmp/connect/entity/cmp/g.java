package com.aliyun.alink.linksdk.tmp.connect.entity.cmp;

import android.text.TextUtils;
import android.util.Log;
import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener;
import com.aliyun.alink.linksdk.tmp.event.INotifyHandler;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: CpNotifyHandler.java */
/* JADX INFO: loaded from: classes2.dex */
public class g extends com.aliyun.alink.linksdk.tmp.connect.f implements IConnectNotifyListener {
    protected static final String g = "[Tmp]CpNotifyHandler";
    protected String h;
    protected com.aliyun.alink.linksdk.tmp.connect.d i;

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener
    public void onConnectStateChange(String str, ConnectState connectState) {
    }

    public g(String str, com.aliyun.alink.linksdk.tmp.connect.d dVar, INotifyHandler iNotifyHandler) {
        super(iNotifyHandler);
        this.h = str;
        this.i = dVar;
        a(dVar);
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener
    public void onNotify(String str, String str2, AMessage aMessage) {
        AResponse aResponse = new AResponse();
        aResponse.data = aMessage.data;
        i iVar = new i(aResponse);
        ALog.d(g, "onNotify data:" + String.valueOf(aMessage.data));
        super.onMessage(this.i, iVar);
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener
    public boolean shouldHandle(String str, String str2) {
        Log.d(g, "shouldHandle connetId:" + str + " topic:" + str2 + " mConnectedId:" + this.h + " requtopic:" + this.i.d());
        return !TextUtils.isEmpty(str) && str.equalsIgnoreCase(this.h) && !TextUtils.isEmpty(str2) && str2.equalsIgnoreCase(this.i.d());
    }
}
