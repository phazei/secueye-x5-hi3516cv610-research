package a.a.a.a.b.l;

import android.os.Handler;
import android.os.Looper;
import com.alibaba.ailabs.iot.mesh.ut.UtTraceInfo;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/* JADX INFO: compiled from: UtTraceManager.java */
/* JADX INFO: loaded from: classes.dex */
public class f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static volatile f f1487a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final ConcurrentHashMap<String, ConcurrentLinkedQueue<UtTraceInfo>> f1488b = new ConcurrentHashMap<>();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final Handler f1489c = new Handler(Looper.getMainLooper());

    public static f a() {
        if (f1487a == null) {
            synchronized (f.class) {
                if (f1487a == null) {
                    f1487a = new f();
                }
            }
        }
        return f1487a;
    }

    public UtTraceInfo b(int i) {
        ConcurrentLinkedQueue<UtTraceInfo> concurrentLinkedQueue = this.f1488b.get(String.valueOf(i));
        UtTraceInfo utTraceInfoPoll = concurrentLinkedQueue != null ? concurrentLinkedQueue.poll() : null;
        if (utTraceInfoPoll != null) {
            return utTraceInfoPoll;
        }
        UtTraceInfo utTraceInfo = new UtTraceInfo();
        utTraceInfo.setUnicastAddress(i);
        return utTraceInfo;
    }

    public UtTraceInfo a(UtTraceInfo utTraceInfo) {
        if (utTraceInfo == null) {
            return null;
        }
        String str = utTraceInfo.getUnicastAddress() + "";
        ConcurrentLinkedQueue<UtTraceInfo> concurrentLinkedQueue = this.f1488b.get(str);
        if (concurrentLinkedQueue == null) {
            concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
            this.f1488b.put(str, concurrentLinkedQueue);
        }
        concurrentLinkedQueue.add(utTraceInfo);
        this.f1488b.put(str, concurrentLinkedQueue);
        this.f1489c.postDelayed(new e(this, str, utTraceInfo), 3000L);
        return utTraceInfo;
    }

    public boolean a(String str, UtTraceInfo utTraceInfo) {
        a.a.a.a.b.m.a.c("UtTraceManager", "removeSpecialTraceInfo: " + str + ", " + utTraceInfo);
        ConcurrentLinkedQueue<UtTraceInfo> concurrentLinkedQueue = this.f1488b.get(str);
        if (concurrentLinkedQueue == null) {
            return false;
        }
        a.a.a.a.b.m.a.c("UtTraceManager", "removeSpecialTraceId " + utTraceInfo);
        return concurrentLinkedQueue.remove(utTraceInfo);
    }

    public UtTraceInfo a(int i) {
        ConcurrentLinkedQueue<UtTraceInfo> concurrentLinkedQueue = this.f1488b.get(i + "");
        UtTraceInfo utTraceInfoPeek = concurrentLinkedQueue != null ? concurrentLinkedQueue.peek() : null;
        if (utTraceInfoPeek != null) {
            return utTraceInfoPeek;
        }
        UtTraceInfo utTraceInfo = new UtTraceInfo();
        utTraceInfo.setUnicastAddress(i);
        return utTraceInfo;
    }

    public UtTraceInfo a(String str) {
        Iterator<Map.Entry<String, ConcurrentLinkedQueue<UtTraceInfo>>> it = this.f1488b.entrySet().iterator();
        while (it.hasNext()) {
            ConcurrentLinkedQueue<UtTraceInfo> value = it.next().getValue();
            if (value != null) {
                for (UtTraceInfo utTraceInfo : value) {
                    if (utTraceInfo != null && utTraceInfo.getDeviceId().equals(str)) {
                        return utTraceInfo;
                    }
                }
            }
        }
        UtTraceInfo utTraceInfo2 = new UtTraceInfo();
        utTraceInfo2.setDeviceId(str);
        return utTraceInfo2;
    }
}
