package com.taobao.accs.ut.a;

import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.UTMini;
import java.util.HashMap;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public String f6434a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public String f6435b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public String f6436c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public String f6437d;
    public String e;
    public String f;
    private final String g = "sendAck";
    private boolean h = false;

    public void a() {
        String str;
        String strValueOf;
        if (this.h) {
            return;
        }
        this.h = true;
        HashMap map = new HashMap();
        try {
            str = this.f6434a;
            try {
                strValueOf = String.valueOf(Constants.SDK_VERSION_CODE);
                try {
                    map.put("device_id", this.f6434a);
                    map.put("session_id", this.f6435b);
                    map.put("data_id", this.f6436c);
                    map.put("ack_date", this.f6437d);
                    map.put("service_id", this.e);
                    map.put("fail_reasons", this.f);
                    UTMini.getInstance().commitEvent(66001, "sendAck", str, (Object) null, strValueOf, map);
                } catch (Throwable th) {
                    th = th;
                    ALog.d("accs.SendAckStatistic", UTMini.getCommitInfo(66001, str, (String) null, strValueOf, map) + " " + th.toString(), new Object[0]);
                }
            } catch (Throwable th2) {
                th = th2;
                strValueOf = null;
            }
        } catch (Throwable th3) {
            th = th3;
            str = null;
            strValueOf = null;
        }
    }
}
