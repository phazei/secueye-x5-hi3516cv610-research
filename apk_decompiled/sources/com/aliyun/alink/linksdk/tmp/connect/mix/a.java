package com.aliyun.alink.linksdk.tmp.connect.mix;

import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: CmpMixCallback.java */
/* JADX INFO: loaded from: classes2.dex */
public class a implements b<MTopAndApiGMixRequest, MTopAndApiGMixResponse> {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final String f4293b = TmpConstant.TAG + a.class.getSimpleName();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected IConnectSendListener f4294a;

    public a(IConnectSendListener iConnectSendListener) {
        this.f4294a = iConnectSendListener;
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.mix.b
    public void a(MTopAndApiGMixRequest mTopAndApiGMixRequest, MTopAndApiGMixResponse mTopAndApiGMixResponse) {
        IConnectSendListener iConnectSendListener = this.f4294a;
        if (iConnectSendListener == null) {
            ALog.e(f4293b, "onSuccess mListener empty");
        } else {
            iConnectSendListener.onResponse(mTopAndApiGMixRequest.b(), mTopAndApiGMixResponse.buildAResponse());
        }
    }

    @Override // com.aliyun.alink.linksdk.tmp.connect.mix.b
    public void a(MTopAndApiGMixRequest mTopAndApiGMixRequest, ErrorInfo errorInfo) {
        if (this.f4294a == null) {
            ALog.e(f4293b, "onFailed mListener empty");
            return;
        }
        AError aError = new AError();
        aError.setCode(errorInfo.getErrorCode());
        aError.setMsg(errorInfo.getErrorMsg());
        this.f4294a.onFailure(mTopAndApiGMixRequest.b(), aError);
    }
}
