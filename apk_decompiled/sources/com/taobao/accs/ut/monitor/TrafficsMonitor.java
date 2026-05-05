package com.taobao.accs.ut.monitor;

import android.content.Context;
import android.text.TextUtils;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.statist.Dimension;
import anet.channel.statist.Measure;
import anet.channel.statist.Monitor;
import com.taobao.accs.client.GlobalClientInfo;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.BaseMonitor;
import com.taobao.accs.utl.UtilityImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class TrafficsMonitor {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private Context f6443d;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Map<String, List<a>> f6440a = new HashMap();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Map<String, String> f6441b = new HashMap<String, String>() { // from class: com.taobao.accs.ut.monitor.TrafficsMonitor.1
        {
            put("im", "512");
            put("motu", "513");
            put("acds", "514");
            put(GlobalClientInfo.AGOO_SERVICE_ID, "515");
            put(AgooConstants.AGOO_SERVICE_AGOOACK, "515");
            put("agooTokenReport", "515");
            put("accsSelf", "1000");
        }
    };

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f6442c = 0;
    private String e = "";

    /* JADX INFO: compiled from: Taobao */
    @Monitor(module = "NetworkSDK", monitorPoint = "TrafficStats")
    public static class StatTrafficMonitor extends BaseMonitor {

        @Dimension
        public String bizId;

        @Dimension
        public String date;

        @Dimension
        public String host;

        @Dimension
        public boolean isBackground;

        @Dimension
        public String serviceId;

        @Measure
        public long size;
    }

    public TrafficsMonitor(Context context) {
        this.f6443d = context;
    }

    public void a(a aVar) {
        boolean z;
        if (aVar == null || aVar.e == null || aVar.f <= 0) {
            return;
        }
        aVar.f6447c = TextUtils.isEmpty(aVar.f6447c) ? "accsSelf" : aVar.f6447c;
        synchronized (this.f6440a) {
            String str = this.f6441b.get(aVar.f6447c);
            if (TextUtils.isEmpty(str)) {
                return;
            }
            aVar.f6446b = str;
            ALog.isPrintLog(ALog.Level.D);
            List<a> arrayList = this.f6440a.get(str);
            if (arrayList != null) {
                Iterator<a> it = arrayList.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        z = true;
                        break;
                    }
                    a next = it.next();
                    if (next.f6448d == aVar.f6448d && next.e != null && next.e.equals(aVar.e)) {
                        next.f += aVar.f;
                        z = false;
                        break;
                    }
                }
                if (z) {
                    arrayList.add(aVar);
                }
            } else {
                arrayList = new ArrayList<>();
                arrayList.add(aVar);
            }
            this.f6440a.put(str, arrayList);
            this.f6442c++;
            if (this.f6442c >= 10) {
                b();
            }
        }
    }

    private void b() {
        String str;
        boolean z;
        synchronized (this.f6440a) {
            String strA = UtilityImpl.a(System.currentTimeMillis());
            if (TextUtils.isEmpty(this.e) || this.e.equals(strA)) {
                str = strA;
                z = false;
            } else {
                str = this.e;
                z = true;
            }
            Iterator<String> it = this.f6440a.keySet().iterator();
            while (it.hasNext()) {
                for (a aVar : this.f6440a.get(it.next())) {
                    if (aVar != null) {
                        com.taobao.accs.a.a.a(this.f6443d).a(aVar.e, aVar.f6447c, this.f6441b.get(aVar.f6447c), aVar.f6448d, aVar.f, str);
                    }
                }
            }
            if (ALog.isPrintLog(ALog.Level.D)) {
                ALog.d("TrafficsMonitor", "savetoDay:" + str + " saveTraffics" + this.f6440a.toString(), new Object[0]);
            }
            if (z) {
                this.f6440a.clear();
                c();
            } else if (ALog.isPrintLog(ALog.Level.D)) {
                ALog.d("TrafficsMonitor", "no need commit lastsaveDay:" + this.e + " currday:" + strA, new Object[0]);
            }
            this.e = strA;
            this.f6442c = 0;
        }
    }

    public void a() {
        try {
            synchronized (this.f6440a) {
                this.f6440a.clear();
            }
            List<a> listA = com.taobao.accs.a.a.a(this.f6443d).a(true);
            if (listA == null) {
                return;
            }
            Iterator<a> it = listA.iterator();
            while (it.hasNext()) {
                a(it.next());
            }
        } catch (Exception e) {
            ALog.w("TrafficsMonitor", e.toString(), new Object[0]);
        }
    }

    private void c() {
        List<a> listA = com.taobao.accs.a.a.a(this.f6443d).a(false);
        if (listA == null) {
            return;
        }
        try {
            for (a aVar : listA) {
                if (aVar != null) {
                    StatTrafficMonitor statTrafficMonitor = new StatTrafficMonitor();
                    statTrafficMonitor.bizId = aVar.f6446b;
                    statTrafficMonitor.date = aVar.f6445a;
                    statTrafficMonitor.host = aVar.e;
                    statTrafficMonitor.isBackground = aVar.f6448d;
                    statTrafficMonitor.size = aVar.f;
                    AppMonitor.getInstance().commitStat(statTrafficMonitor);
                }
            }
            com.taobao.accs.a.a.a(this.f6443d).a();
        } catch (Throwable th) {
            ALog.e("", th.toString(), new Object[0]);
            th.printStackTrace();
        }
    }

    /* JADX INFO: compiled from: Taobao */
    public static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        String f6445a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        String f6446b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        String f6447c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        boolean f6448d;
        String e;
        long f;

        public a(String str, boolean z, String str2, long j) {
            this.f6447c = str;
            this.f6448d = z;
            this.e = str2;
            this.f = j;
        }

        public a(String str, String str2, String str3, boolean z, String str4, long j) {
            this.f6445a = str;
            this.f6446b = str2;
            this.f6447c = str3;
            this.f6448d = z;
            this.e = str4;
            this.f = j;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("date:" + this.f6445a);
            sb.append(" ");
            sb.append("bizId:" + this.f6446b);
            sb.append(" ");
            sb.append("serviceId:" + this.f6447c);
            sb.append(" ");
            sb.append("host:" + this.e);
            sb.append(" ");
            sb.append("isBackground:" + this.f6448d);
            sb.append(" ");
            sb.append("size:" + this.f);
            return sb.toString();
        }
    }
}
