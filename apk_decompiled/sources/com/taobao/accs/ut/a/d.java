package com.taobao.accs.ut.a;

import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.UTMini;
import java.util.HashMap;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public String f6430a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public String f6431b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public String f6432c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public String f6433d;
    public String e;
    public String f;
    public String g;
    public String i;
    private final String j = "receiveMessage";
    public boolean h = false;
    private boolean k = false;

    public void a() {
        String str;
        String strValueOf;
        if (this.k) {
            return;
        }
        this.k = true;
        HashMap map = new HashMap();
        try {
            str = this.f6430a;
            try {
                strValueOf = String.valueOf(Constants.SDK_VERSION_CODE);
                try {
                    map.put("device_id", this.f6430a);
                    map.put("data_id", this.f6431b);
                    map.put("receive_date", this.f6432c);
                    map.put("to_bz_date", this.f6433d);
                    map.put("service_id", this.e);
                    map.put("data_length", this.f);
                    map.put("msg_type", this.g);
                    map.put("repeat", this.h ? "y" : "n");
                    map.put("user_id", this.i);
                    UTMini.getInstance().commitEvent(66001, "receiveMessage", str, (Object) null, strValueOf, map);
                } catch (Throwable th) {
                    th = th;
                    ALog.d("ReceiveMessage", UTMini.getCommitInfo(66001, str, (String) null, strValueOf, map) + " " + th.toString(), new Object[0]);
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
