package com.aliyun.alink.linksdk.tmp.connect.entity.cmp;

import com.aliyun.alink.linksdk.cmp.manager.connect.IRegisterConnectListener;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tools.AError;

/* JADX INFO: compiled from: CpConnectHandler.java */
/* JADX INFO: loaded from: classes2.dex */
public class c implements IRegisterConnectListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected IDevListener f4252a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected String f4253b;

    public c(String str, IDevListener iDevListener) {
        this.f4252a = iDevListener;
        this.f4253b = str;
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IBaseListener
    public void onSuccess() {
        final OutputParams outputParams = new OutputParams(com.aliyun.alink.linksdk.tmp.connect.a.f4238b, new ValueWrapper.StringValueWrapper(this.f4253b));
        if (this.f4252a != null) {
            com.aliyun.alink.linksdk.tmp.connect.f.e.post(new Runnable() { // from class: com.aliyun.alink.linksdk.tmp.connect.entity.cmp.c.1
                @Override // java.lang.Runnable
                public void run() {
                    c.this.f4252a.onSuccess(null, outputParams);
                }
            });
        }
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IBaseListener
    public void onFailure(AError aError) {
        final ErrorInfo errorInfo;
        if (aError == null) {
            errorInfo = new ErrorInfo(300, "param is invalid");
        } else {
            errorInfo = new ErrorInfo(aError.getSubCode(), aError.getMsg());
        }
        if (this.f4252a != null) {
            com.aliyun.alink.linksdk.tmp.connect.f.e.post(new Runnable() { // from class: com.aliyun.alink.linksdk.tmp.connect.entity.cmp.c.2
                @Override // java.lang.Runnable
                public void run() {
                    c.this.f4252a.onFail(null, errorInfo);
                }
            });
        }
    }
}
