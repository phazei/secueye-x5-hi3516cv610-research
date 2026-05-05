package b.e;

import android.content.Context;
import android.os.Handler;
import androidx.annotation.VisibleForTesting;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: compiled from: AccessLayer.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f2150a = "a";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public Context f2151b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public Handler f2152c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public g f2153d;
    public Map<String, List<byte[]>> e = new LinkedHashMap();

    public void a(g gVar) {
        this.f2153d = gVar;
    }

    public final void b(b.d.a aVar) {
        byte[] bArrU = aVar.u();
        int i = (bArrU[0] & 240) >> 6;
        if (i == 0) {
            i = 1;
        }
        if (bArrU[0] == -35) {
            i = 1;
        }
        a.a.a.a.b.m.a.a(f2150a, "Opcode length: " + i + " Octets");
        aVar.f(MeshParserUtils.getOpCode(bArrU, i));
        int length = bArrU.length - i;
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(length).order(ByteOrder.BIG_ENDIAN);
        byteBufferOrder.put(bArrU, i, length);
        aVar.d(byteBufferOrder.array());
        a.a.a.a.b.m.a.a(f2150a, "Received Access PDU " + MeshParserUtils.bytesToHex(bArrU, false));
    }

    public void c(String str, byte[] bArr) {
        this.e.remove(MeshParserUtils.bytesToHex(bArr, false));
    }

    public void a(b.d.c cVar) {
        a((b.d.a) cVar);
    }

    @VisibleForTesting(otherwise = 4)
    public final void a(b.d.a aVar) {
        ByteBuffer byteBufferAllocate;
        byte[] opCodes = MeshParserUtils.getOpCodes(aVar.n());
        byte[] bArrO = aVar.o();
        if (bArrO != null) {
            byteBufferAllocate = ByteBuffer.allocate(opCodes.length + bArrO.length);
            byteBufferAllocate.put(opCodes).put(bArrO);
        } else {
            byteBufferAllocate = ByteBuffer.allocate(opCodes.length);
            byteBufferAllocate.put(opCodes);
        }
        byte[] bArrArray = byteBufferAllocate.array();
        a.a.a.a.b.m.a.a(f2150a, "Created Access PDU " + MeshParserUtils.bytesToHex(bArrArray, false));
        aVar.g(byteBufferAllocate.array());
    }

    public byte[] b(String str, byte[] bArr) {
        g gVar = this.f2153d;
        if (gVar != null) {
            return gVar.b(str, bArr);
        }
        return null;
    }

    public byte[] a(String str) {
        g gVar = this.f2153d;
        if (gVar != null) {
            return gVar.a(str);
        }
        return new byte[]{0};
    }

    public List<byte[]> a(String str, byte[] bArr) {
        List<byte[]> listA;
        String strBytesToHex = MeshParserUtils.bytesToHex(bArr, false);
        if (this.e.containsKey(strBytesToHex)) {
            return this.e.get(strBytesToHex);
        }
        g gVar = this.f2153d;
        if (gVar == null || (listA = gVar.a(str, bArr)) == null) {
            return null;
        }
        this.e.put(strBytesToHex, listA);
        return listA;
    }
}
