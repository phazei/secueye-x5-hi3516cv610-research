package com.aliyun.alink.linksdk.tmp.device.a;

import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.config.DeviceConfig;
import com.aliyun.alink.linksdk.tmp.devicemodel.DeviceModel;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.listener.IDevRawDataListener;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.LogCat;

/* JADX INFO: compiled from: DeviceAsyncTask.java */
/* JADX INFO: loaded from: classes2.dex */
public abstract class d<Task> extends a<com.aliyun.alink.linksdk.tmp.connect.d, com.aliyun.alink.linksdk.tmp.connect.e> {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    protected static final String f4345c = "[Tmp]DeviceAsyncTask";
    protected Object e;
    protected IDevListener f;
    protected IDevRawDataListener g;
    protected com.aliyun.alink.linksdk.tmp.device.a h;
    protected com.aliyun.alink.linksdk.tmp.connect.b i;
    protected DeviceBasicData j;
    protected DeviceModel k;
    protected DeviceConfig m;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    protected Task f4346d = this;
    protected boolean l = true;

    /* JADX WARN: Multi-variable type inference failed */
    public d(com.aliyun.alink.linksdk.tmp.device.a aVar, IDevListener iDevListener) {
        this.h = aVar;
        this.f = iDevListener;
        com.aliyun.alink.linksdk.tmp.device.a aVar2 = this.h;
        if (aVar2 != null) {
            this.i = aVar2.a();
        }
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.a
    public boolean a() {
        com.aliyun.alink.linksdk.tmp.device.a aVar = this.h;
        if (aVar == null) {
            return true;
        }
        this.i = aVar.a();
        return true;
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.a
    public boolean a(a aVar, com.aliyun.alink.linksdk.tmp.connect.d dVar, com.aliyun.alink.linksdk.tmp.connect.e eVar) {
        com.aliyun.alink.linksdk.tmp.device.a aVar2 = this.h;
        if (aVar2 != null && this.i == null) {
            this.i = aVar2.a();
        }
        return super.a(aVar, dVar, eVar);
    }

    public Task a(DeviceConfig deviceConfig) {
        this.m = deviceConfig;
        return this.f4346d;
    }

    public Task a(IDevRawDataListener iDevRawDataListener) {
        this.g = iDevRawDataListener;
        return this.f4346d;
    }

    public Task a(boolean z) {
        this.l = z;
        return this.f4346d;
    }

    public Task a(DeviceBasicData deviceBasicData) {
        this.j = deviceBasicData;
        return this.f4346d;
    }

    public Task a(com.aliyun.alink.linksdk.tmp.connect.b bVar) {
        this.i = bVar;
        return this.f4346d;
    }

    public Task a(com.aliyun.alink.linksdk.tmp.device.a aVar) {
        this.h = aVar;
        return this.f4346d;
    }

    public Task a(Object obj) {
        this.e = obj;
        return this.f4346d;
    }

    public Task a(IDevListener iDevListener) {
        this.f = iDevListener;
        return this.f4346d;
    }

    public Task a(DeviceModel deviceModel) {
        this.k = deviceModel;
        return this.f4346d;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    protected void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, com.aliyun.alink.linksdk.tmp.connect.e eVar, ErrorInfo errorInfo) {
        IDevListener iDevListener = this.f;
        if (iDevListener == null) {
            LogCat.e(f4345c, "onFlowComplete handler empty error");
        } else {
            this.f = null;
            iDevListener.onSuccess(this.e, null);
        }
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.a
    /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, com.aliyun.alink.linksdk.tmp.connect.e eVar, ErrorInfo errorInfo) {
        a(dVar, eVar, errorInfo);
    }

    protected void b(com.aliyun.alink.linksdk.tmp.connect.d dVar, ErrorInfo errorInfo) {
        IDevListener iDevListener = this.f;
        if (iDevListener == null) {
            LogCat.w(f4345c, "onFlowError empty error");
        } else {
            this.f = null;
            iDevListener.onFail(this.e, errorInfo);
        }
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.a
    /* JADX INFO: renamed from: c, reason: merged with bridge method [inline-methods] */
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, ErrorInfo errorInfo) {
        b(dVar, errorInfo);
    }
}
