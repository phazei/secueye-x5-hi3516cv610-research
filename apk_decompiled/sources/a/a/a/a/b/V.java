package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.MeshService;
import java.util.Set;
import meshprovisioner.configuration.ProvisionedMeshNode;

/* JADX INFO: compiled from: MeshService.java */
/* JADX INFO: loaded from: classes.dex */
public class V implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ProvisionedMeshNode f1236a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ MeshService f1237b;

    public V(MeshService meshService, ProvisionedMeshNode provisionedMeshNode) {
        this.f1237b = meshService;
        this.f1236a = provisionedMeshNode;
    }

    @Override // java.lang.Runnable
    public void run() {
        Set<Integer> flatSubscribeGroupAddress = G.a().b().getFlatSubscribeGroupAddress();
        byte[] bArr = new byte[flatSubscribeGroupAddress.size() * 2];
        int i = 0;
        for (Integer num : flatSubscribeGroupAddress) {
            System.arraycopy(new byte[]{(byte) ((num.intValue() >> 8) & 255), (byte) (num.intValue() & 255)}, 0, bArr, i, 2);
            i += 2;
        }
        this.f1237b.mMeshManagerApi.a(this.f1236a, bArr);
    }
}
