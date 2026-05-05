package com.taobao.accs.ut.a;

import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.UTMini;
import java.util.HashMap;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public int f6426a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public int f6427b;
    public String f;
    public String g;
    public long h;
    public boolean i;
    public boolean j;
    private long k = 0;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public boolean f6428c = false;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public int f6429d = 0;
    public int e = 0;

    public void a() {
        String strValueOf;
        String strValueOf2;
        String strValueOf3;
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (ALog.isPrintLog(ALog.Level.D)) {
            ALog.d("MonitorStatistic", "commitUT interval:" + (jCurrentTimeMillis - this.k) + " interval1:" + (jCurrentTimeMillis - this.h), new Object[0]);
        }
        if (jCurrentTimeMillis - this.k <= 1200000 || jCurrentTimeMillis - this.h <= 60000) {
            return;
        }
        HashMap map = new HashMap();
        try {
            strValueOf = String.valueOf(this.f6429d);
            try {
                strValueOf2 = String.valueOf(this.e);
                try {
                    strValueOf3 = String.valueOf(Constants.SDK_VERSION_CODE);
                    try {
                        map.put("connStatus", String.valueOf(this.f6426a));
                        map.put("connType", String.valueOf(this.f6427b));
                        map.put("tcpConnected", String.valueOf(this.f6428c));
                        map.put("proxy", String.valueOf(this.f));
                        map.put("startServiceTime", String.valueOf(this.h));
                        map.put("commitTime", String.valueOf(jCurrentTimeMillis));
                        map.put("networkAvailable", String.valueOf(this.i));
                        map.put("threadIsalive", String.valueOf(this.j));
                        map.put("url", this.g);
                        if (ALog.isPrintLog(ALog.Level.D)) {
                            ALog.d("MonitorStatistic", UTMini.getCommitInfo(66001, strValueOf, strValueOf2, strValueOf3, map), new Object[0]);
                        }
                        UTMini.getInstance().commitEvent(66001, "MONITOR", strValueOf, strValueOf2, strValueOf3, map);
                        this.k = jCurrentTimeMillis;
                    } catch (Throwable th) {
                        th = th;
                        ALog.d("MonitorStatistic", UTMini.getCommitInfo(66001, strValueOf, strValueOf2, strValueOf3, map) + " " + th.toString(), new Object[0]);
                    }
                } catch (Throwable th2) {
                    th = th2;
                    strValueOf3 = null;
                }
            } catch (Throwable th3) {
                th = th3;
                strValueOf2 = null;
                strValueOf3 = strValueOf2;
                ALog.d("MonitorStatistic", UTMini.getCommitInfo(66001, strValueOf, strValueOf2, strValueOf3, map) + " " + th.toString(), new Object[0]);
            }
        } catch (Throwable th4) {
            th = th4;
            strValueOf = null;
            strValueOf2 = null;
        }
    }
}
