package com.aliyun.alink.linksdk.tmp.connect.mix;

import com.aliyun.alink.linksdk.connectsdk.ApiCallBack;
import com.aliyun.alink.linksdk.connectsdk.ApiHelper;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: MTopAndApiGMixConnect.java */
/* JADX INFO: loaded from: classes2.dex */
public class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f4295a = TmpConstant.TAG + c.class.getSimpleName();

    public static void a(final MTopAndApiGMixRequest mTopAndApiGMixRequest, final b<MTopAndApiGMixRequest, MTopAndApiGMixResponse> bVar) {
        ALog.d(f4295a, "send request:" + mTopAndApiGMixRequest + " callback:" + bVar);
        ApiHelper.getInstance().send(mTopAndApiGMixRequest.a(), new ApiCallBack() { // from class: com.aliyun.alink.linksdk.tmp.connect.mix.c.1
            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onSuccess(Object obj) {
                ALog.d(c.f4295a, "send onSuccess data:" + obj);
                b bVar2 = bVar;
                if (bVar2 == null) {
                    ALog.e(c.f4295a, "send onSuccess callback empty");
                } else {
                    bVar2.a(mTopAndApiGMixRequest, new MTopAndApiGMixResponse(obj));
                }
            }

            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onFail(int i, String str) {
                ALog.e(c.f4295a, "send onFail code:" + i + " msg:" + str + " callback:" + bVar);
                b bVar2 = bVar;
                if (bVar2 == null) {
                    ALog.e(c.f4295a, "send onFail callback empty");
                } else {
                    bVar2.a(mTopAndApiGMixRequest, new ErrorInfo(i, str));
                }
            }
        });
    }
}
