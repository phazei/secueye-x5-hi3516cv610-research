package com.aliyun.alink.linksdk.tmp.resource;

import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.listener.ITRawDataRequestHandler;
import com.aliyun.alink.linksdk.tmp.listener.ITResResponseCallback;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: TRawResRequestWrapperHandler.java */
/* JADX INFO: loaded from: classes2.dex */
public class c implements b {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final String f4414b = "[Tmp]TRawResRequestWrapperHandler";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected ITRawDataRequestHandler f4415a;

    public c(ITRawDataRequestHandler iTRawDataRequestHandler) {
        this.f4415a = iTRawDataRequestHandler;
    }

    @Override // com.aliyun.alink.linksdk.tmp.resource.b
    public void onProcess(String str, String str2, Object obj, ITResResponseCallback iTResResponseCallback) {
        ALog.d(f4414b, "onProcess identifier:" + str2 + " topic:" + str2 + " payload:" + obj + " mHandler:" + this.f4415a);
        ITRawDataRequestHandler iTRawDataRequestHandler = this.f4415a;
        if (iTRawDataRequestHandler != null) {
            iTRawDataRequestHandler.onProcess(str, obj, iTResResponseCallback);
        }
    }

    @Override // com.aliyun.alink.linksdk.tmp.listener.IDevListener
    public void onSuccess(Object obj, OutputParams outputParams) {
        ALog.d(f4414b, "onSuccess identifier:" + outputParams + " returnValue: mHandler:" + this.f4415a);
        ITRawDataRequestHandler iTRawDataRequestHandler = this.f4415a;
        if (iTRawDataRequestHandler != null) {
            iTRawDataRequestHandler.onSuccess(obj, outputParams);
        }
    }

    @Override // com.aliyun.alink.linksdk.tmp.listener.IDevListener
    public void onFail(Object obj, ErrorInfo errorInfo) {
        ALog.d(f4414b, "onFail errorInfo:" + errorInfo + " returnValue: mHandler:" + this.f4415a);
        ITRawDataRequestHandler iTRawDataRequestHandler = this.f4415a;
        if (iTRawDataRequestHandler != null) {
            iTRawDataRequestHandler.onSuccess(obj, errorInfo);
        }
    }
}
