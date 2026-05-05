package a.a.a.a.b;

import aisble.BleManager;

/* JADX INFO: compiled from: SIGMeshGlobalConfiguration.java */
/* JADX INFO: loaded from: classes.dex */
public class oa {
    public static long a(boolean z) {
        if (a.a.a.a.b.d.a.f1315a) {
            return 60000L;
        }
        if (z) {
            return BleManager.CONNECTION_TIMEOUT_THRESHOLD;
        }
        return 30000L;
    }
}
