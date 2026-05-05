package b;

import aisscanner.ScanResult;
import java.util.Comparator;

/* JADX INFO: compiled from: SIGMeshNetworkTransportManager.java */
/* JADX INFO: loaded from: classes.dex */
public class x implements Comparator<ScanResult> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ y f2222a;

    public x(y yVar) {
        this.f2222a = yVar;
    }

    @Override // java.util.Comparator
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(ScanResult scanResult, ScanResult scanResult2) {
        return scanResult2.getRssi() - scanResult.getRssi();
    }
}
