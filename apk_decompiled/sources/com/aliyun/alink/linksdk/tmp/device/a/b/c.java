package com.aliyun.alink.linksdk.tmp.device.a.b;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.connect.d;
import com.aliyun.alink.linksdk.tmp.connect.e;
import com.aliyun.alink.linksdk.tmp.devicemodel.DeviceModel;
import com.aliyun.alink.linksdk.tmp.event.INotifyHandler;
import com.aliyun.alink.linksdk.tmp.listener.IEventListener;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: SubscribEventTask.java */
/* JADX INFO: loaded from: classes2.dex */
public class c extends b {
    protected static final String q = "SubscribEventTask";
    protected IEventListener r;

    public c(com.aliyun.alink.linksdk.tmp.device.a aVar, DeviceBasicData deviceBasicData, DeviceModel deviceModel, IEventListener iEventListener, Object obj) {
        super(aVar, deviceBasicData, iEventListener);
        a(deviceModel);
        a(obj);
        this.r = iEventListener;
        a(deviceBasicData);
        a(aVar);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.b.b
    /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
    public c a(INotifyHandler iNotifyHandler) {
        this.o = iNotifyHandler;
        return this;
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.b.b
    /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
    public c a(String str) {
        this.p = str;
        return this;
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.a
    /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
    public void a(d dVar, e eVar) {
        b();
        super.a(dVar, eVar);
    }

    protected boolean b() {
        com.aliyun.alink.linksdk.tmp.device.a aVar = this.h;
        if (aVar == null || TextUtils.isEmpty(this.p) || this.r == null) {
            ALog.e(q, "addEventList deviceImpl empty or mEventName empty deviceImpl:" + aVar + " mEventName:" + this.p + " mEventListener:" + this.r);
            return false;
        }
        aVar.a(this.p, aVar.j(), this.r);
        return true;
    }
}
