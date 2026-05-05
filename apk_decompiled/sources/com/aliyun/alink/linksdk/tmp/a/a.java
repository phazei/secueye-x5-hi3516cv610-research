package com.aliyun.alink.linksdk.tmp.a;

import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: compiled from: LocalDeviceListChangeNotifier.java */
/* JADX INFO: loaded from: classes2.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f4225a = "[Tmp]LocalDeviceListChangeNotifier";

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static volatile a f4226c;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Map<Integer, InterfaceC0220a> f4227b = new ConcurrentHashMap();

    /* JADX INFO: renamed from: com.aliyun.alink.linksdk.tmp.a.a$a, reason: collision with other inner class name */
    /* JADX INFO: compiled from: LocalDeviceListChangeNotifier.java */
    public interface InterfaceC0220a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final int f4228a = 1;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public static final int f4229b = 2;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public static final int f4230c = 3;

        void onDeviceListChange(int i, DeviceBasicData deviceBasicData);
    }

    private a() {
    }

    public static a a() {
        if (f4226c == null) {
            synchronized (a.class) {
                if (f4226c == null) {
                    f4226c = new a();
                }
            }
        }
        return f4226c;
    }

    protected void a(InterfaceC0220a interfaceC0220a) {
        if (interfaceC0220a == null) {
            ALog.e(f4225a, "addDeviceListChangeListener listChangeListener empty");
        } else {
            this.f4227b.put(Integer.valueOf(interfaceC0220a.hashCode()), interfaceC0220a);
        }
    }

    protected void b(InterfaceC0220a interfaceC0220a) {
        if (interfaceC0220a == null) {
            ALog.e(f4225a, "addDeviceListChangeListener listChangeListener empty");
        } else {
            this.f4227b.remove(Integer.valueOf(interfaceC0220a.hashCode()));
        }
    }

    protected void a(int i, DeviceBasicData deviceBasicData) {
        Map<Integer, InterfaceC0220a> map = this.f4227b;
        if (map == null || map.isEmpty()) {
            return;
        }
        for (Map.Entry entry : new HashMap(this.f4227b).entrySet()) {
            if (entry.getValue() != null) {
                ((InterfaceC0220a) entry.getValue()).onDeviceListChange(i, deviceBasicData);
            }
        }
    }
}
