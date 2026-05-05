package b;

import aisscanner.ScanResult;
import com.alibaba.ailabs.iot.aisbase.scanner.BLEScannerProxy;
import meshprovisioner.configuration.ProvisionedMeshNode;

/* JADX INFO: compiled from: SubnetsBiz.java */
/* JADX INFO: loaded from: classes.dex */
public class B implements BLEScannerProxy.IMeshNetworkPUDListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ K f2092a;

    public B(K k) {
        this.f2092a = k;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.scanner.BLEScannerProxy.IMeshNetworkPUDListener
    public void onMeshNetworkPDURecevied(ScanResult scanResult) {
        if (scanResult == null || scanResult.getScanRecord() == null || scanResult.getScanRecord().getMeshNetworkPUD() == null) {
            return;
        }
        ProvisionedMeshNode provisionedMeshNodeA = a.a.a.a.b.G.a().d().a(scanResult.getDevice().getAddress());
        if (provisionedMeshNodeA == null) {
            return;
        }
        a.a.a.a.b.m.a.a("Multi_channel_upstream", "Recevied mesh network PUD from ADV channel");
        byte[] meshNetworkPUD = scanResult.getScanRecord().getMeshNetworkPUD();
        byte[] bArr = new byte[meshNetworkPUD.length + 1];
        bArr[0] = 0;
        System.arraycopy(meshNetworkPUD, 0, bArr, 1, meshNetworkPUD.length);
        this.f2092a.h.a(provisionedMeshNodeA, 31, bArr, (a.a.a.a.b.h.a) null);
    }
}
