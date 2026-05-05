package b;

import android.content.Context;
import android.os.Build;
import com.alibaba.ailabs.iot.mesh.provision.FastProvisionManager;
import meshprovisioner.BaseMeshNode;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: compiled from: SubnetsBiz.java */
/* JADX INFO: loaded from: classes.dex */
public class A implements InterfaceC0379m {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ K f2091a;

    public A(K k) {
        this.f2091a = k;
    }

    @Override // b.InterfaceC0379m
    public int getMtu() {
        return 20;
    }

    @Override // b.InterfaceC0379m
    public void sendPdu(BaseMeshNode baseMeshNode, byte[] bArr) {
        a.a.a.a.b.m.a.a(K.f2104a, String.format("Send data to node(%s) via adv channel", MeshParserUtils.bytesToHex(baseMeshNode.getUnicastAddress(), false)));
        if (Build.VERSION.SDK_INT >= 21) {
            if (FastProvisionManager.getInstance().getInProvisionProgress()) {
                a.a.a.a.b.m.a.b(K.f2104a, "Exist provision activity for tinyMesh, discard");
                return;
            }
            if (this.f2091a.t == null) {
                K k = this.f2091a;
                Context context = k.e;
                byte[] bArrD = this.f2091a.s.d();
                K k2 = this.f2091a;
                k.t = new a.a.a.a.b.k.d(context, bArrD, k2, k2.w);
            }
            this.f2091a.t.a(baseMeshNode, bArr);
            a.a.a.a.b.l.c.a(baseMeshNode.getUnicastAddressInt(), "1");
        }
    }
}
