package b;

import java.util.Set;
import meshprovisioner.configuration.ProvisionedMeshNode;

/* JADX INFO: compiled from: SubnetsBiz.java */
/* JADX INFO: loaded from: classes.dex */
public class z implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ C0378l f2229a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ ProvisionedMeshNode f2230b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ K f2231c;

    public z(K k, C0378l c0378l, ProvisionedMeshNode provisionedMeshNode) {
        this.f2231c = k;
        this.f2229a = c0378l;
        this.f2230b = provisionedMeshNode;
    }

    @Override // java.lang.Runnable
    public void run() {
        Set<Integer> flatSubscribeGroupAddress;
        if (a.a.a.a.b.G.a().c() == null || (flatSubscribeGroupAddress = a.a.a.a.b.G.a().c().getFlatSubscribeGroupAddress()) == null || flatSubscribeGroupAddress.size() == 0) {
            return;
        }
        byte[] bArr = new byte[flatSubscribeGroupAddress.size() * 2];
        int i = 0;
        for (Integer num : flatSubscribeGroupAddress) {
            System.arraycopy(new byte[]{(byte) ((num.intValue() >> 8) & 255), (byte) (num.intValue() & 255)}, 0, bArr, i, 2);
            i += 2;
        }
        this.f2229a.a(this.f2230b, bArr);
    }
}
