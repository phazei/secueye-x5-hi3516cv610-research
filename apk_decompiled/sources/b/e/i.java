package b.e;

import android.content.Context;
import android.os.Handler;
import b.InterfaceC0370d;
import b.q;
import meshprovisioner.configuration.ProvisionedMeshNode;
import meshprovisioner.configuration.SequenceNumber;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: compiled from: MeshTransportStackEntryPoint.java */
/* JADX INFO: loaded from: classes.dex */
public class i extends j {
    public static final String r = "" + i.class.getSimpleName();
    public static volatile i s;
    public InterfaceC0370d t;
    public q u;

    public static i c() {
        if (s == null) {
            synchronized (i.class) {
                if (s == null) {
                    s = new i();
                }
            }
        }
        return s;
    }

    public void a(Context context) {
        this.f2151b = context;
        d();
        a(new h(this));
    }

    public void d() {
        this.f2152c = new Handler(this.f2151b.getMainLooper());
    }

    public b.d.c h(String str, byte[] bArr) {
        return g(str, bArr);
    }

    public b.d.b i(b.d.b bVar) {
        c(bVar);
        b((b.d.c) bVar);
        return bVar;
    }

    public b.d.b b(ProvisionedMeshNode provisionedMeshNode, byte[] bArr, byte[] bArr2, byte[] bArr3, int i, int i2, int i3, int i4, byte[] bArr4) {
        int iA = a();
        byte[] sequenceNumberBytes = MeshParserUtils.getSequenceNumberBytes(iA);
        a.a.a.a.b.m.a.a(r, "Src address: " + MeshParserUtils.bytesToHex(bArr, false));
        a.a.a.a.b.m.a.a(r, "Dst address: " + MeshParserUtils.bytesToHex(bArr2, false));
        a.a.a.a.b.m.a.a(r, "Key: " + MeshParserUtils.bytesToHex(bArr3, false));
        a.a.a.a.b.m.a.a(r, "akf: " + i);
        a.a.a.a.b.m.a.a(r, "aid: " + i2);
        a.a.a.a.b.m.a.a(r, "aszmic: " + i3);
        a.a.a.a.b.m.a.a(r, "Sequence number: " + iA);
        a.a.a.a.b.m.a.a(r, "Control message opcode: " + Integer.toHexString(i4));
        a.a.a.a.b.m.a.a(r, "Control message parameters: " + MeshParserUtils.bytesToHex(bArr4, false));
        b.d.b bVar = new b.d.b();
        bVar.f(bArr);
        bVar.a(bArr2);
        bVar.h(0);
        bVar.b(provisionedMeshNode.getIvIndex());
        bVar.e(sequenceNumberBytes);
        bVar.c(bArr3);
        bVar.b(i);
        bVar.a(i2);
        bVar.c(i3);
        bVar.f(i4);
        bVar.d(bArr4);
        bVar.g(2);
        bVar.a(provisionedMeshNode.getK2Output());
        bVar.g(new byte[0]);
        super.a((b.d.c) bVar);
        return bVar;
    }

    public void a(InterfaceC0370d interfaceC0370d) {
        this.t = interfaceC0370d;
    }

    public void a(q qVar) {
        this.u = qVar;
    }

    @Override // b.e.e
    public final void a(f fVar) {
        super.a(fVar);
    }

    @Override // b.e.e
    public int a() {
        return SequenceNumber.getInstance().incrementAndStore();
    }

    @Override // b.e.e
    public int a(byte[] bArr) {
        return SequenceNumber.getInstance().incrementAndStore(bArr);
    }

    public b.d.a a(ProvisionedMeshNode provisionedMeshNode, byte[] bArr, byte[] bArr2, int i, int i2, int i3, int i4, byte[] bArr3) {
        a.a.a.a.b.m.a.c(r, "Create mesh message");
        int iA = a();
        byte[] sequenceNumberBytes = MeshParserUtils.getSequenceNumberBytes(iA);
        a.a.a.a.b.m.a.a(r, "Src address: " + MeshParserUtils.bytesToHex(bArr, false));
        a.a.a.a.b.m.a.a(r, "Dst address: " + MeshParserUtils.bytesToHex(provisionedMeshNode.getUnicastAddress(), false));
        a.a.a.a.b.m.a.a(r, "Key: " + MeshParserUtils.bytesToHex(bArr2, false));
        a.a.a.a.b.m.a.a(r, "akf: " + i);
        a.a.a.a.b.m.a.a(r, "aid: " + i2);
        a.a.a.a.b.m.a.a(r, "aszmic: " + i3);
        a.a.a.a.b.m.a.a(r, "Sequence number: " + iA);
        a.a.a.a.b.m.a.a(r, "Access message opcode: " + i4);
        a.a.a.a.b.m.a.a(r, "Access message parameters: " + MeshParserUtils.bytesToHex(bArr3, false));
        b.d.a aVar = new b.d.a();
        aVar.f(bArr);
        aVar.a(provisionedMeshNode.getUnicastAddress());
        aVar.b(provisionedMeshNode.getIvIndex());
        aVar.e(sequenceNumberBytes);
        aVar.c(bArr2);
        aVar.b(i);
        aVar.a(i2);
        aVar.c(i3);
        aVar.f(i4);
        aVar.d(bArr3);
        aVar.g(0);
        aVar.a(provisionedMeshNode.getK2Output());
        super.a((b.d.c) aVar);
        return aVar;
    }

    public b.d.a a(ProvisionedMeshNode provisionedMeshNode, byte[] bArr, byte[] bArr2, byte[] bArr3, int i, int i2, int i3, int i4, byte[] bArr4) {
        a.a.a.a.b.m.a.c(r, "Create mesh message");
        int iA = a();
        byte[] sequenceNumberBytes = MeshParserUtils.getSequenceNumberBytes(iA);
        a.a.a.a.b.m.a.a(r, "Src address: " + MeshParserUtils.bytesToHex(bArr, false));
        a.a.a.a.b.m.a.a(r, "Dst address: " + MeshParserUtils.bytesToHex(bArr2, false));
        a.a.a.a.b.m.a.a(r, "Key: " + MeshParserUtils.bytesToHex(bArr3, false));
        a.a.a.a.b.m.a.a(r, "akf: " + i);
        a.a.a.a.b.m.a.a(r, "aid: " + i2);
        a.a.a.a.b.m.a.a(r, "aszmic: " + i3);
        a.a.a.a.b.m.a.a(r, "Sequence number: " + iA);
        a.a.a.a.b.m.a.a(r, "Access message opcode: " + Integer.toHexString(i4));
        a.a.a.a.b.m.a.a(r, "Access message parameters: " + MeshParserUtils.bytesToHex(bArr4, false));
        b.d.a aVar = new b.d.a();
        aVar.f(bArr);
        aVar.a(bArr2);
        aVar.b(provisionedMeshNode.getIvIndex());
        aVar.e(sequenceNumberBytes);
        aVar.c(bArr3);
        aVar.b(i);
        aVar.a(i2);
        aVar.c(i3);
        aVar.f(i4);
        aVar.d(bArr4);
        aVar.g(0);
        aVar.a(provisionedMeshNode.getK2Output());
        super.a((b.d.c) aVar);
        return aVar;
    }

    public b.d.b a(ProvisionedMeshNode provisionedMeshNode, int i, byte[] bArr, byte[] bArr2, byte[] bArr3, int i2, int i3, int i4, int i5, byte[] bArr4) {
        int iA = a();
        byte[] sequenceNumberBytes = MeshParserUtils.getSequenceNumberBytes(iA);
        a.a.a.a.b.m.a.a(r, "Src address: " + MeshParserUtils.bytesToHex(bArr, false));
        a.a.a.a.b.m.a.a(r, "Dst address: " + MeshParserUtils.bytesToHex(bArr2, false));
        a.a.a.a.b.m.a.a(r, "Key: " + MeshParserUtils.bytesToHex(bArr3, false));
        a.a.a.a.b.m.a.a(r, "akf: " + i2);
        a.a.a.a.b.m.a.a(r, "aid: " + i3);
        a.a.a.a.b.m.a.a(r, "aszmic: " + i4);
        a.a.a.a.b.m.a.a(r, "Sequence number: " + iA);
        a.a.a.a.b.m.a.a(r, "Control message opcode: " + Integer.toHexString(i5));
        a.a.a.a.b.m.a.a(r, "Control message parameters: " + MeshParserUtils.bytesToHex(bArr4, false));
        b.d.b bVar = new b.d.b();
        bVar.f(bArr);
        bVar.a(bArr2);
        bVar.h(provisionedMeshNode.getTtl());
        bVar.b(provisionedMeshNode.getIvIndex());
        bVar.e(sequenceNumberBytes);
        bVar.c(bArr3);
        bVar.b(i2);
        bVar.a(i3);
        bVar.c(i4);
        bVar.f(i5);
        bVar.d(bArr4);
        bVar.g(i);
        bVar.a(provisionedMeshNode.getK2Output());
        bVar.g(new byte[0]);
        super.a((b.d.c) bVar);
        return bVar;
    }
}
