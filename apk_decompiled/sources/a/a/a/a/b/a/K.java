package a.a.a.a.b.a;

import b.C0378l;
import b.u;
import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import java.util.Deque;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import meshprovisioner.configuration.ProvisionedMeshNode;

/* JADX INFO: compiled from: SerialExecutionDispatcher.java */
/* JADX INFO: loaded from: classes.dex */
public class K extends C0318d {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static int f1247b = 3;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static int f1248c = 300;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public String f1249d;
    public final Deque<SIGMeshBizRequest> e;
    public int f;
    public int g;
    public volatile boolean h;
    public final AtomicBoolean i;

    public K(C0378l c0378l, Deque<SIGMeshBizRequest> deque) {
        super(c0378l);
        this.f1249d = "" + K.class.getSimpleName();
        this.f = 0;
        this.g = 0;
        this.h = false;
        this.i = new AtomicBoolean(false);
        this.e = deque;
    }

    public synchronized void b() {
        if (this.e.size() != 0 && !this.i.get()) {
            this.h = true;
            new Thread(new J(this)).start();
            return;
        }
        this.h = false;
    }

    public void c() {
        this.i.set(false);
        if (this.h) {
            a.a.a.a.b.m.a.d(this.f1249d, "Already in flight status, do nothing");
        } else {
            a.a.a.a.b.m.a.a(this.f1249d, "start called");
            b();
        }
    }

    public void d() {
        this.i.set(true);
    }

    public boolean a() {
        return this.h;
    }

    public void a(int i) {
        f1247b = i;
    }

    @Override // a.a.a.a.b.a.C0318d
    public void b(SIGMeshBizRequest sIGMeshBizRequest) {
        ProvisionedMeshNode provisionedMeshNode;
        try {
            sIGMeshBizRequest.q();
        } catch (Exception e) {
            a.a.a.a.b.m.a.b(this.f1249d, e.toString());
        }
        String bluetoothAddress = TmpConstant.GROUP_ROLE_UNKNOWN;
        u.a aVarD = a.a.a.a.b.G.a().d().d();
        if (aVarD != null && (provisionedMeshNode = (ProvisionedMeshNode) aVarD.d(sIGMeshBizRequest.j())) != null) {
            bluetoothAddress = provisionedMeshNode.getBluetoothAddress();
        }
        a.a.a.a.b.m.a.d(this.f1249d, String.format(Locale.US, "Request(to %s:%s) timeout", Utils.bytes2HexString(sIGMeshBizRequest.j()), bluetoothAddress));
        this.g++;
        if (sIGMeshBizRequest.a()) {
            synchronized (this.e) {
                this.e.add(sIGMeshBizRequest);
            }
        } else {
            Utils.notifyFailed(sIGMeshBizRequest.m(), -13, "Timeout! the device is not reply");
        }
        if (this.g == this.f) {
            if (this.e.isEmpty()) {
                this.h = false;
            } else {
                b();
            }
        }
    }

    @Override // a.a.a.a.b.a.C0318d
    public void c(SIGMeshBizRequest sIGMeshBizRequest) {
        ProvisionedMeshNode provisionedMeshNode;
        try {
            sIGMeshBizRequest.q();
        } catch (Exception e) {
            a.a.a.a.b.m.a.b(this.f1249d, e.toString());
        }
        String bluetoothAddress = TmpConstant.GROUP_ROLE_UNKNOWN;
        u.a aVarD = a.a.a.a.b.G.a().d().d();
        if (aVarD != null && (provisionedMeshNode = (ProvisionedMeshNode) aVarD.d(sIGMeshBizRequest.j())) != null) {
            bluetoothAddress = provisionedMeshNode.getBluetoothAddress();
        }
        a.a.a.a.b.m.a.c(this.f1249d, String.format(Locale.US, "Request(to %s:%s) processed", Utils.bytes2HexString(sIGMeshBizRequest.j()), bluetoothAddress));
        this.g++;
        if (this.g == this.f) {
            if (this.e.isEmpty()) {
                this.h = false;
            } else {
                b();
            }
        }
    }

    public K(C0378l c0378l, Deque<SIGMeshBizRequest> deque, String str) {
        this(c0378l, deque);
        this.f1249d += str;
    }

    public void b(int i) {
        f1248c = i;
    }
}
