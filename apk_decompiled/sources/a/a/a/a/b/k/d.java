package a.a.a.a.b.k;

import a.a.a.a.b.i.J;
import a.a.a.a.b.i.c.g;
import aisble.callback.DataReceivedCallback;
import android.content.Context;
import androidx.annotation.RequiresApi;
import com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionTransportCallback;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import meshprovisioner.BaseMeshNode;
import meshprovisioner.utils.SecureUtils;

/* JADX INFO: compiled from: TinyMeshMessageAdvSender.java */
/* JADX INFO: loaded from: classes.dex */
@RequiresApi(api = 21)
public class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f1472a = "" + d.class.getSimpleName();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public Context f1473b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public FastProvisionTransportCallback f1474c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public BaseMeshNode f1475d;
    public a.a.a.a.a.a.a.b.a e;
    public List<a.a.a.a.a.a.a.a.a> f;
    public byte[] h;
    public J i;
    public final DataReceivedCallback j = new a(this);
    public g g = new g();

    public d(Context context, byte[] bArr, FastProvisionTransportCallback fastProvisionTransportCallback, J j) {
        this.i = null;
        this.f1473b = context;
        this.h = bArr;
        this.f1474c = fastProvisionTransportCallback;
        this.g.init(this.f1473b);
        this.f = new ArrayList();
        this.i = j;
        a.a.a.a.a.g.c().a(this.f1473b);
    }

    public final void b() {
        a.a.a.a.a.a.a.a.a aVar = this.f.get(0);
        a.a.a.a.b.m.a.c(f1472a, "checkControlBufferAndSend, expect " + aVar.e() + ", current " + this.f.size());
        Collections.sort(this.f, new c(this));
        if (aVar.e() == this.f.size()) {
            Iterator<a.a.a.a.a.a.a.a.a> it = this.f.iterator();
            int length = 0;
            while (it.hasNext()) {
                byte[] bArrB = it.next().b();
                if (bArrB != null) {
                    length += bArrB.length;
                }
            }
            ByteBuffer byteBufferAllocate = ByteBuffer.allocate(length + 1);
            byteBufferAllocate.put((byte) 0);
            Iterator<a.a.a.a.a.a.a.a.a> it2 = this.f.iterator();
            while (it2.hasNext()) {
                byte[] bArrB2 = it2.next().b();
                if (bArrB2 != null) {
                    byteBufferAllocate.put(bArrB2);
                }
            }
            this.f1474c.onReceiveFastProvisionData(this.f1475d, byteBufferAllocate.array());
        }
    }

    public final byte c() {
        a.a.a.a.b.m.a.c(f1472a, "networkKey=" + ConvertUtils.bytes2HexString(this.h));
        return SecureUtils.calculateK2(this.h, SecureUtils.K2_MASTER_INPUT).getNid();
    }

    public void a(BaseMeshNode baseMeshNode, byte[] bArr) {
        byte[] bArr2;
        a.a.a.a.b.m.a.c(f1472a, "before split package " + ConvertUtils.bytes2HexString(bArr));
        this.f1475d = baseMeshNode;
        if (bArr.length >= 1) {
            bArr2 = new byte[bArr.length - 1];
            System.arraycopy(bArr, 1, bArr2, 0, bArr2.length);
        } else {
            bArr2 = new byte[bArr.length];
            System.arraycopy(bArr, 0, bArr2, 0, bArr2.length);
        }
        this.e = new a.a.a.a.a.a.a.b.a(bArr2, c(), new b(this, baseMeshNode, bArr));
        a.a.a.a.a.g.c().b(this.f1473b);
        a.a.a.a.a.g.c().a(this.e);
    }

    public void a(Context context) {
        a.a.a.a.b.m.a.c(f1472a, "startScanDeviceAdvertise execute");
        g gVar = this.g;
        if (gVar != null) {
            gVar.a(this.j);
        }
    }

    public final synchronized void a(byte[] bArr) {
        a.a.a.a.b.m.a.c(f1472a, "assembleControlResp " + ConvertUtils.bytes2HexString(bArr));
        if (this.e == null) {
            a.a.a.a.b.m.a.b(f1472a, "There is no controlMsg");
            return;
        }
        a.a.a.a.a.a.a.a.a aVarA = a.a.a.a.a.a.a.a.a.a(bArr);
        if (aVarA == null) {
            a.a.a.a.b.m.a.b(f1472a, "failed to parse " + ConvertUtils.bytes2HexString(bArr));
            return;
        }
        byte bC = c();
        if (aVarA.d() != bC) {
            a.a.a.a.b.m.a.b(f1472a, "network id not equal, abandon. Expect " + ((int) bC) + ", receive " + ((int) this.e.d()));
            return;
        }
        if (this.f.isEmpty()) {
            this.f.add(aVarA);
            b();
        } else {
            a.a.a.a.a.a.a.a.a aVar = this.f.get(0);
            if (aVar.c() != aVarA.c()) {
                a.a.a.a.b.m.a.b(f1472a, "clear old cache ...");
                this.f.clear();
                this.f.add(aVarA);
                b();
            } else {
                if (aVar.e() != aVarA.e()) {
                    a.a.a.a.b.m.a.b(f1472a, "total package number illegal, expect " + aVar.e() + ", receive " + aVarA.e());
                    return;
                }
                Iterator<a.a.a.a.a.a.a.a.a> it = this.f.iterator();
                while (it.hasNext()) {
                    if (it.next().a() == aVarA.a()) {
                        a.a.a.a.b.m.a.c(f1472a, "index duplicate");
                        return;
                    }
                }
                this.f.add(aVarA);
                b();
            }
        }
    }
}
