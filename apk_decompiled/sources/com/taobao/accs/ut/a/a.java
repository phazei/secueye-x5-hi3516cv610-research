package com.taobao.accs.ut.a;

import com.alibaba.sdk.android.error.ErrorCode;
import com.taobao.accs.AccsErrorCode;
import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.UTMini;
import java.util.HashMap;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public String f6418a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public String f6419b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public boolean f6420c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public String f6421d;
    private final String e = "BindApp";
    private boolean f = false;

    public void a(String str) {
        this.f6421d = str;
    }

    public void a(ErrorCode errorCode) {
        if (errorCode.getCodeInt() != AccsErrorCode.SUCCESS.getCodeInt()) {
            a(errorCode.getMsg());
        }
    }

    public void a() {
        b("BindApp");
    }

    private void b(String str) {
        String str2;
        String strValueOf;
        if (this.f) {
            return;
        }
        this.f = true;
        HashMap map = new HashMap();
        try {
            str2 = this.f6418a;
            try {
                strValueOf = String.valueOf(Constants.SDK_VERSION_CODE);
                try {
                    map.put("device_id", this.f6418a);
                    map.put("bind_date", this.f6419b);
                    map.put("ret", this.f6420c ? "y" : "n");
                    map.put("fail_reasons", this.f6421d);
                    map.put("push_token", "");
                    UTMini.getInstance().commitEvent(66001, str, str2, (Object) null, strValueOf, map);
                } catch (Throwable th) {
                    th = th;
                    ALog.d("BindAppStatistic", UTMini.getCommitInfo(66001, str2, (String) null, strValueOf, map) + " " + th.toString(), new Object[0]);
                }
            } catch (Throwable th2) {
                th = th2;
                strValueOf = null;
            }
        } catch (Throwable th3) {
            th = th3;
            str2 = null;
            strValueOf = null;
        }
    }
}
