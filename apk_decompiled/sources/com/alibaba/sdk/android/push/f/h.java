package com.alibaba.sdk.android.push.f;

import android.content.Context;
import android.util.Log;
import com.alibaba.sdk.android.ams.common.logger.AmsLogger;
import com.alibaba.sdk.android.error.ErrorCode;
import com.alibaba.sdk.android.push.CommonCallback;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class h extends com.alibaba.sdk.android.push.common.util.a.c {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static AmsLogger f3150c = AmsLogger.getLogger("MPS:VipRequestTask");

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private CommonCallback f3151d;

    public h(Context context, String str, CommonCallback commonCallback) {
        super(context, str);
        this.f3151d = commonCallback;
    }

    private void a(int i, com.alibaba.sdk.android.push.common.util.a.b bVar, CommonCallback commonCallback) {
        ErrorCode errorCodeBuild;
        String code;
        if (commonCallback == null) {
            return;
        }
        f3150c.d("requestType: " + i + ", errorCode:" + bVar.f3057c + ", httpcode: " + bVar.f3056b + ", content:" + bVar.f3055a);
        if (!bVar.f3057c.getCode().equals(com.alibaba.sdk.android.push.common.a.d.f3049a.getCode())) {
            if (commonCallback != null) {
                commonCallback.onFailed(bVar.f3057c.getCode(), bVar.f3057c.getMsg());
                return;
            }
            return;
        }
        try {
            String strA = i.a(i, bVar.f3056b, bVar.f3055a);
            if (commonCallback != null) {
                commonCallback.onSuccess(strA);
            }
        } catch (com.alibaba.sdk.android.push.b.f e) {
            f3150c.e("Vip call failed", e);
            if (commonCallback != null) {
                code = e.a().getCode();
                errorCodeBuild = e.a();
                commonCallback.onFailed(code, errorCodeBuild.getMsg());
            }
        } catch (Throwable th) {
            f3150c.e("Vip call faled.", th);
            if (commonCallback != null) {
                errorCodeBuild = com.alibaba.sdk.android.push.common.a.d.k.copy().msg(th.getMessage()).detail(Log.getStackTraceString(th)).build();
                code = errorCodeBuild.getCode();
                commonCallback.onFailed(code, errorCodeBuild.getMsg());
            }
        }
    }

    @Override // com.alibaba.sdk.android.push.common.util.a.c
    protected Map<String, String> a(Context context, Map<String, String> map) {
        return com.alibaba.sdk.android.ams.common.util.d.a(map);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.alibaba.sdk.android.push.common.util.a.c, android.os.AsyncTask
    /* JADX INFO: renamed from: a */
    public void onPostExecute(com.alibaba.sdk.android.push.common.util.a.b bVar) {
        super.onPostExecute(bVar);
        a(a(), bVar, this.f3151d);
    }
}
