package a.a.a.a.b.i;

import aisble.callback.DataReceivedCallback;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import b.InterfaceC0370d;
import com.alibaba.ailabs.iot.mesh.callback.IConnectCallback;
import com.alibaba.ailabs.iot.mesh.provision.callback.AliMeshProvisioningFrameworkStatusCallbacks;
import com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionV2StatusCallback;
import com.alibaba.ailabs.iot.mesh.provision.state.FastProvisioningState;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import datasource.bean.ProvisionInfo;
import meshprovisioner.BaseMeshNode;
import meshprovisioner.configuration.ProvisionedMeshNode;
import meshprovisioner.states.UnprovisionedMeshNode;
import meshprovisioner.states.UnprovisionedMeshNodeData;
import meshprovisioner.utils.MeshParserUtils;
import meshprovisioner.utils.UnprovisionedMeshNodeUtil;

/* JADX INFO: compiled from: FastProvisionV2Worker.java */
/* JADX INFO: loaded from: classes.dex */
public class u implements InterfaceC0370d, FastProvisionV2StatusCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public String f1439a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public String f1440b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public Context f1441c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public AliMeshProvisioningFrameworkStatusCallbacks f1442d;
    public IConnectCallback e;
    public UnprovisionedMeshNodeData f;
    public UnprovisionedMeshNode g;
    public ProvisionedMeshNode h;
    public BluetoothDevice i;
    public a.a.a.a.b.i.c.a j;
    public FastProvisioningState k;
    public b.u l;
    public ProvisionInfo m;
    public Handler n;
    public Runnable o;
    public Runnable p;
    public int q;
    public IConnectCallback r;
    public final DataReceivedCallback s;

    public u(Context context) {
        this.f1439a = "FastProvisionV2Worker";
        this.o = null;
        this.p = null;
        this.r = new C0349o(this);
        this.s = new C0350p(this);
        this.f1441c = context;
        this.l = a.a.a.a.b.G.a().d();
        this.n = new Handler(Looper.getMainLooper());
    }

    @Override // b.InterfaceC0370d
    public void a(ProvisionedMeshNode provisionedMeshNode) {
    }

    @Override // b.InterfaceC0370d
    public void b(ProvisionedMeshNode provisionedMeshNode) {
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionV2StatusCallback
    public void onProvisioningComplete(ProvisionedMeshNode provisionedMeshNode) {
        this.h = provisionedMeshNode;
        this.h.setIsProvisioned(true);
        this.h.setDevId(MeshParserUtils.bytesToHex(this.f.getDeviceUuid(), false));
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionV2StatusCallback
    public void onProvisioningDataSent(UnprovisionedMeshNode unprovisionedMeshNode) {
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionV2StatusCallback
    public void onProvisioningFailed(BaseMeshNode baseMeshNode, int i, String str) {
        a.a.a.a.b.m.a.c(this.f1439a, "onProvisionFailed: errorCode = " + i + ", errorMsg = " + str);
        c();
        AliMeshProvisioningFrameworkStatusCallbacks aliMeshProvisioningFrameworkStatusCallbacks = this.f1442d;
        if (aliMeshProvisioningFrameworkStatusCallbacks != null) {
            aliMeshProvisioningFrameworkStatusCallbacks.onProvisioningFailed(this.g, i);
        }
    }

    @Override // b.InterfaceC0370d
    public void sendPdu(BaseMeshNode baseMeshNode, byte[] bArr) {
        this.j.a(bArr, new q(this));
    }

    public a.a.a.a.b.i.c.a b() {
        return this.j;
    }

    public void c() {
        if (this.j != null) {
            a.a.a.a.b.m.a.c(this.f1439a, "Release transport layer.");
            this.j.a();
        }
    }

    public final void d() {
        this.q++;
        this.k = new a.a.a.a.b.i.b.a(this.h, this, this, this.m);
        this.k.a();
    }

    public final void e() {
        this.k = new a.a.a.a.b.i.b.b(this.g, this, this, this.m);
        this.k.a();
        a(a.a.a.a.b.i.b.a.class);
    }

    public final void f() {
        Handler handler = this.n;
        s sVar = new s(this);
        this.p = sVar;
        handler.postDelayed(sVar, AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
    }

    public final void g() {
        this.n.removeCallbacks(this.p);
    }

    public void a(UnprovisionedMeshNodeData unprovisionedMeshNodeData, BluetoothDevice bluetoothDevice, AliMeshProvisioningFrameworkStatusCallbacks aliMeshProvisioningFrameworkStatusCallbacks, IConnectCallback iConnectCallback) {
        this.f = unprovisionedMeshNodeData;
        this.i = bluetoothDevice;
        this.f1442d = aliMeshProvisioningFrameworkStatusCallbacks;
        this.e = iConnectCallback;
        this.j = new a.a.a.a.b.i.c.r(this.f1440b);
        this.j.init(this.f1441c);
        this.j.a(this.s);
    }

    public u(Context context, String str) {
        this(context);
        this.f1440b = str;
        this.f1439a += str;
    }

    public void a(ProvisionInfo provisionInfo) {
        if (provisionInfo == null) {
            return;
        }
        this.m = provisionInfo;
        this.g = UnprovisionedMeshNodeUtil.buildUnprovisionedMeshNode(this.f1441c, this.i.getAddress(), "", MeshParserUtils.bytesToHex(MeshParserUtils.toByteArray(provisionInfo.getNetKeys().get(0)), false), (provisionInfo.getNetKeyIndexes() == null || provisionInfo.getAppKeyIndexes().size() <= 0) ? 0 : provisionInfo.getNetKeyIndexes().get(0).intValue(), 0, 0, provisionInfo.getPrimaryUnicastAddress().intValue(), 5, this.l.b(), this.f.getDeviceUuid(), null);
        this.j.a(this.i, this.r);
    }

    public final void a(Class cls) {
        Handler handler = this.n;
        r rVar = new r(this, cls);
        this.o = rVar;
        handler.postDelayed(rVar, 1500L);
    }

    public final void a() {
        Runnable runnable = this.o;
        if (runnable != null) {
            this.n.removeCallbacks(runnable);
        }
    }
}
