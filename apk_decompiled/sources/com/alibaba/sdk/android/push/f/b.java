package com.alibaba.sdk.android.push.f;

import com.alibaba.sdk.android.ams.common.logger.AmsLogger;
import com.alibaba.sdk.android.error.ErrorCode;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.report.ReportManager;

/* JADX INFO: loaded from: classes.dex */
public class b {
    public static void a(CommonCallback commonCallback, e eVar) {
        ErrorCode errorCodeA = eVar.a();
        AmsLogger.getImportantLogger().i("errorCode:" + errorCodeA);
        if (errorCodeA.getCode().contains(com.alibaba.sdk.android.push.common.a.d.f3049a.getCode())) {
            if (commonCallback != null) {
                commonCallback.onSuccess(errorCodeA.getMsg());
            }
        } else {
            if (commonCallback != null) {
                commonCallback.onFailed(errorCodeA.getCode(), errorCodeA.getMsg());
            }
            a(errorCodeA.getCode(), errorCodeA.getMsg());
        }
    }

    private static void a(String str, String str2) {
        com.alibaba.sdk.android.ams.common.b.b bVarA = com.alibaba.sdk.android.ams.common.b.c.a();
        ReportManager reportManager = ReportManager.getInstance();
        if (reportManager == null || bVarA == null) {
            return;
        }
        reportManager.reportErrorInit(str, str2, bVarA.b());
    }
}
