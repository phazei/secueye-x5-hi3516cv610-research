package com.taobao.accs.ut.a;

import com.alibaba.sdk.android.error.ErrorCode;
import com.taobao.accs.AccsErrorCode;
import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.UTMini;
import java.util.HashMap;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public String f6422a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public String f6423b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public boolean f6424c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public String f6425d;
    public String e;
    private final String f = "BindUser";
    private boolean g = false;

    public void a(String str) {
        this.f6425d = str;
    }

    public void a(ErrorCode errorCode) {
        if (errorCode.getCodeInt() != AccsErrorCode.SUCCESS.getCodeInt()) {
            a(errorCode.getMsg());
        }
    }

    public void a() {
        b("BindUser");
    }

    private void b(String str) {
        String str2;
        String strValueOf;
        if (this.g) {
            return;
        }
        this.g = true;
        HashMap map = new HashMap();
        try {
            str2 = this.f6422a;
            try {
                strValueOf = String.valueOf(Constants.SDK_VERSION_CODE);
            } catch (Throwable th) {
                th = th;
                strValueOf = null;
            }
        } catch (Throwable th2) {
            th = th2;
            str2 = null;
            strValueOf = null;
        }
        try {
            map.put("device_id", this.f6422a);
            map.put("bind_date", this.f6423b);
            map.put("ret", this.f6424c ? "y" : "n");
            map.put("fail_reasons", this.f6425d);
            map.put("user_id", this.e);
            if (ALog.isPrintLog(ALog.Level.D)) {
                ALog.d("accs.BindUserStatistic", UTMini.getCommitInfo(66001, str2, (String) null, strValueOf, map), new Object[0]);
            }
            UTMini.getInstance().commitEvent(66001, str, str2, (Object) null, strValueOf, map);
        } catch (Throwable th3) {
            th = th3;
            ALog.d("accs.BindUserStatistic", UTMini.getCommitInfo(66001, str2, (String) null, strValueOf, map) + " " + th.toString(), new Object[0]);
        }
    }
}
