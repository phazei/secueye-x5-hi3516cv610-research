package a.a.a.a.b;

import com.alibaba.ailabs.iot.mesh.DeviceProvisioningWorker;
import java.util.Set;
import meshprovisioner.configuration.ProvisionedMeshNode;

/* JADX INFO: compiled from: DeviceProvisioningWorker.java */
/* JADX INFO: loaded from: classes.dex */
public class r implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ProvisionedMeshNode f1536a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ DeviceProvisioningWorker f1537b;

    public r(DeviceProvisioningWorker deviceProvisioningWorker, ProvisionedMeshNode provisionedMeshNode) {
        this.f1537b = deviceProvisioningWorker;
        this.f1536a = provisionedMeshNode;
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
        this.f1537b.e.a(this.f1536a, bArr);
    }
}
