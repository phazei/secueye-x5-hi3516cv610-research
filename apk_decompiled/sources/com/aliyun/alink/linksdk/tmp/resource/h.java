package com.aliyun.alink.linksdk.tmp.resource;

import com.aliyun.alink.linksdk.tmp.api.InputParams;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.device.payload.CommonRequestPayload;
import com.aliyun.alink.linksdk.tmp.listener.ITResRequestHandler;
import com.aliyun.alink.linksdk.tmp.listener.ITResResponseCallback;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tools.ALog;
import com.google.gson.reflect.TypeToken;

/* JADX INFO: compiled from: TResRequestWrapperHandler.java */
/* JADX INFO: loaded from: classes2.dex */
public class h implements b {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final String f4428b = "[Tmp]TResRequestWrapperHandler";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected ITResRequestHandler f4429a;

    public h(ITResRequestHandler iTResRequestHandler) {
        this.f4429a = iTResRequestHandler;
    }

    @Override // com.aliyun.alink.linksdk.tmp.resource.b
    public void onProcess(String str, String str2, Object obj, ITResResponseCallback iTResResponseCallback) {
        ALog.d(f4428b, "onProcess identifier:" + str2 + " topic:" + str2 + " payload:" + obj + " mHandler:" + this.f4429a);
        if (this.f4429a != null) {
            CommonRequestPayload commonRequestPayload = (CommonRequestPayload) GsonUtils.fromJson(String.valueOf(obj), new TypeToken<CommonRequestPayload>() { // from class: com.aliyun.alink.linksdk.tmp.resource.h.1
            }.getType());
            InputParams inputParams = null;
            if (obj != null && commonRequestPayload != null) {
                inputParams = new InputParams(commonRequestPayload.getParams());
            }
            this.f4429a.onProcess(str, inputParams, iTResResponseCallback);
        }
    }

    @Override // com.aliyun.alink.linksdk.tmp.listener.IDevListener
    public void onSuccess(Object obj, OutputParams outputParams) {
        ALog.d(f4428b, "onSuccess identifier:" + outputParams + " returnValue: mHandler:" + this.f4429a);
        ITResRequestHandler iTResRequestHandler = this.f4429a;
        if (iTResRequestHandler != null) {
            iTResRequestHandler.onSuccess(obj, outputParams);
        }
    }

    @Override // com.aliyun.alink.linksdk.tmp.listener.IDevListener
    public void onFail(Object obj, ErrorInfo errorInfo) {
        ALog.d(f4428b, "onFail errorInfo:" + errorInfo + " returnValue: mHandler:" + this.f4429a);
        ITResRequestHandler iTResRequestHandler = this.f4429a;
        if (iTResRequestHandler != null) {
            iTResRequestHandler.onFail(obj, errorInfo);
        }
    }
}
