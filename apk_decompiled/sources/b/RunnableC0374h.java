package b;

import com.alibaba.ailabs.iot.mesh.callback.IActionListener;
import meshprovisioner.configuration.ProvisionedMeshNode;

/* JADX INFO: renamed from: b.h, reason: case insensitive filesystem */
/* JADX INFO: compiled from: MeshManagerApi.java */
/* JADX INFO: loaded from: classes.dex */
public class RunnableC0374h implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ ProvisionedMeshNode f2181a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ byte[] f2182b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ IActionListener f2183c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final /* synthetic */ boolean f2184d;
    public final /* synthetic */ String e;
    public final /* synthetic */ byte[] f;
    public final /* synthetic */ boolean g;
    public final /* synthetic */ int h;
    public final /* synthetic */ int i;
    public final /* synthetic */ C0378l j;

    public RunnableC0374h(C0378l c0378l, ProvisionedMeshNode provisionedMeshNode, byte[] bArr, IActionListener iActionListener, boolean z, String str, byte[] bArr2, boolean z2, int i, int i2) {
        this.j = c0378l;
        this.f2181a = provisionedMeshNode;
        this.f2182b = bArr;
        this.f2183c = iActionListener;
        this.f2184d = z;
        this.e = str;
        this.f = bArr2;
        this.g = z2;
        this.h = i;
        this.i = i2;
    }

    @Override // java.lang.Runnable
    public void run() {
        C0378l c0378l = this.j;
        byte[] unicastAddress = this.f2181a.getUnicastAddress();
        byte[] bArr = this.f2182b;
        c0378l.b(unicastAddress, 13871105, new byte[]{bArr[1], bArr[2]}, this.f2183c);
        this.j.b(this.f2181a, this.f2184d, this.e, this.f, this.g, this.h, this.i, this.f2182b, this.f2183c);
    }
}
