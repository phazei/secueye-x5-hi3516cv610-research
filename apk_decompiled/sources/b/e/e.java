package b.e;

import android.util.ArrayMap;
import androidx.annotation.VisibleForTesting;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import kotlin.jvm.internal.ByteCompanionObject;
import meshprovisioner.utils.MeshParserUtils;
import meshprovisioner.utils.SecureUtils;
import meshprovisioner.utils.SparseIntArrayParcelable;

/* JADX INFO: compiled from: LowerTransportLayer.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class e extends k {
    public static final String h = "e";
    public ArrayMap<String, HashMap<Integer, byte[]>> i = new ArrayMap<>();
    public ArrayMap<String, HashMap<Integer, byte[]>> j = new ArrayMap<>();
    public ArrayMap<String, a> k = new ArrayMap<>();
    public SparseIntArrayParcelable l = new SparseIntArrayParcelable();
    public f m;

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: LowerTransportLayer.java */
    static final class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public String f2163a;
        public Runnable g = new d(this);
        public boolean e = false;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public boolean f2166d = false;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public boolean f2164b = false;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public Integer f2165c = null;
        public long f = 0;

        public a(String str) {
            this.f2163a = str;
        }
    }

    public abstract int a();

    public abstract int a(byte[] bArr);

    public final boolean a(byte b2) {
        return ((b2 >> 7) & 1) == 1;
    }

    public final void b(b.d.a aVar, byte[] bArr) {
        byte b2 = bArr[10];
        int i = (b2 >> 7) & 1;
        int i2 = (b2 >> 6) & 1;
        int i3 = b2 & 63;
        if (i == 0) {
            if (i2 == 0) {
                int length = bArr.length - 10;
                ByteBuffer byteBufferOrder = ByteBuffer.allocate(length).order(ByteOrder.BIG_ENDIAN);
                byteBufferOrder.put(bArr, 10, length);
                byte[] bArrArray = byteBufferOrder.array();
                HashMap<Integer, byte[]> map = new HashMap<>();
                map.put(0, bArrArray);
                aVar.a(false);
                aVar.c(0);
                aVar.b(i2);
                aVar.a(i3);
                aVar.a(map);
                return;
            }
            int length2 = bArr.length - 10;
            ByteBuffer byteBufferOrder2 = ByteBuffer.allocate(length2).order(ByteOrder.BIG_ENDIAN);
            byteBufferOrder2.put(bArr, 10, length2);
            byte[] bArrArray2 = byteBufferOrder2.array();
            HashMap<Integer, byte[]> map2 = new HashMap<>();
            map2.put(0, bArrArray2);
            aVar.a(false);
            aVar.c(0);
            aVar.b(i2);
            aVar.a(i3);
            aVar.a(map2);
        }
    }

    @VisibleForTesting(otherwise = 4)
    public final void c(b.d.b bVar) {
        if (bVar.v().length <= 11) {
            a.a.a.a.b.m.a.a(h, "Creating unsegmented transport control");
            e(bVar);
        } else {
            a.a.a.a.b.m.a.a(h, "Creating segmented transport control");
            d(bVar);
        }
    }

    public final HashMap<Integer, byte[]> d(b.d.b bVar) {
        bVar.a(false);
        byte[] bArrV = bVar.v();
        int iN = bVar.n();
        int iCalculateSeqZero = MeshParserUtils.calculateSeqZero(bVar.q());
        int length = (bArrV.length + 7) / 8;
        int i = length - 1;
        HashMap<Integer, byte[]> map = new HashMap<>();
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3++) {
            int iMin = Math.min(bArrV.length - i2, 8);
            ByteBuffer byteBufferOrder = ByteBuffer.allocate(iMin + 4).order(ByteOrder.BIG_ENDIAN);
            byteBufferOrder.put((byte) (iN | 128));
            byteBufferOrder.put((byte) (((iCalculateSeqZero >> 6) & 127) | 0));
            byteBufferOrder.put((byte) (((iCalculateSeqZero << 2) & 252) | ((i3 >> 3) & 3)));
            byteBufferOrder.put((byte) (((i3 << 5) & 224) | (i & 31)));
            byteBufferOrder.put(bArrV, i2, iMin);
            i2 += iMin;
            byte[] bArrArray = byteBufferOrder.array();
            a.a.a.a.b.m.a.a(h, "Segmented Lower transport access PDU: " + MeshParserUtils.bytesToHex(bArrArray, false) + " " + i3 + " of " + length);
            map.put(Integer.valueOf(i3), bArrArray);
        }
        bVar.b(map);
        return map;
    }

    @Override // b.e.k
    public final void e(b.d.a aVar) {
        aVar.h(MeshParserUtils.concatenateSegmentedMessages(i(aVar)));
    }

    @VisibleForTesting(otherwise = 4)
    public final void f(b.d.a aVar) {
        HashMap<Integer, byte[]> mapG;
        if (aVar.v().length <= 15) {
            aVar.a(false);
            byte[] bArrH = h(aVar);
            mapG = new HashMap<>();
            mapG.put(0, bArrH);
        } else {
            aVar.a(true);
            mapG = g(aVar);
        }
        aVar.a(mapG);
    }

    public final void g(b.d.b bVar) {
        bVar.g(MeshParserUtils.concatenateSegmentedMessages(h(bVar)));
    }

    public final HashMap<Integer, byte[]> h(b.d.b bVar) {
        HashMap<Integer, byte[]> mapK = bVar.k();
        if (mapK.size() > 1) {
            for (int i = 0; i < mapK.size(); i++) {
                byte[] bArr = mapK.get(Integer.valueOf(i));
                mapK.put(Integer.valueOf(i), a(bArr, 4, bArr.length - 4));
            }
        } else if (bVar.n() != 0) {
            byte[] bArr2 = mapK.get(0);
            mapK.put(0, a(bArr2, 1, bArr2.length - 1));
        } else {
            byte[] bArr3 = mapK.get(0);
            mapK.put(0, a(bArr3, 3, bArr3.length - 3));
        }
        return mapK;
    }

    public final HashMap<Integer, byte[]> i(b.d.a aVar) {
        HashMap<Integer, byte[]> mapJ = aVar.j();
        if (aVar.t()) {
            for (int i = 0; i < mapJ.size(); i++) {
                byte[] bArr = mapJ.get(Integer.valueOf(i));
                mapJ.put(Integer.valueOf(i), a(bArr, 4, bArr.length - 4));
            }
        } else {
            byte[] bArr2 = mapJ.get(0);
            mapJ.put(0, a(bArr2, 1, bArr2.length - 1));
        }
        return mapJ;
    }

    public void a(f fVar) {
        this.m = fVar;
    }

    @Override // b.e.k, b.e.a
    public void a(b.d.c cVar) {
        if (cVar instanceof b.d.a) {
            super.a(cVar);
            f((b.d.a) cVar);
        } else {
            c((b.d.b) cVar);
        }
    }

    @VisibleForTesting(otherwise = 4)
    public final byte[] e(b.d.b bVar) {
        ByteBuffer byteBufferOrder;
        bVar.a(false);
        int iN = bVar.n();
        byte[] bArrO = bVar.o();
        if (iN == 81 || iN == 80) {
            bArrO = a(bVar);
        }
        byte[] bArrV = bVar.v();
        byte b2 = (byte) (iN | 0);
        if (bArrO != null) {
            byteBufferOrder = ByteBuffer.allocate(bArrO.length + 1 + bArrV.length).order(ByteOrder.BIG_ENDIAN);
            byteBufferOrder.put(b2);
            byteBufferOrder.put(bArrO);
        } else {
            byteBufferOrder = ByteBuffer.allocate(bArrV.length + 1).order(ByteOrder.BIG_ENDIAN);
            byteBufferOrder.put(b2);
        }
        byteBufferOrder.put(bArrV);
        byte[] bArrArray = byteBufferOrder.array();
        a.a.a.a.b.m.a.a(h, "Unsegmented Lower transport control PDU " + MeshParserUtils.bytesToHex(bArrArray, false));
        HashMap<Integer, byte[]> map = new HashMap<>();
        map.put(0, bArrArray);
        bVar.b(map);
        return bArrArray;
    }

    public final HashMap<Integer, byte[]> g(b.d.a aVar) {
        byte[] bArrV = aVar.v();
        int iB = (aVar.b() << 6) | aVar.a();
        int iC = aVar.c();
        int iCalculateSeqZero = MeshParserUtils.calculateSeqZero(aVar.q());
        int length = (bArrV.length + 11) / 12;
        int i = length - 1;
        HashMap<Integer, byte[]> map = new HashMap<>();
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3++) {
            int iMin = Math.min(bArrV.length - i2, 12);
            ByteBuffer byteBufferOrder = ByteBuffer.allocate(iMin + 4).order(ByteOrder.BIG_ENDIAN);
            byteBufferOrder.put((byte) (iB | 128));
            byteBufferOrder.put((byte) ((iC << 7) | ((iCalculateSeqZero >> 6) & 127)));
            byteBufferOrder.put((byte) (((iCalculateSeqZero << 2) & 252) | ((i3 >> 3) & 3)));
            byteBufferOrder.put((byte) (((i3 << 5) & 224) | (i & 31)));
            byteBufferOrder.put(bArrV, i2, iMin);
            i2 += iMin;
            byte[] bArrArray = byteBufferOrder.array();
            a.a.a.a.b.m.a.a(h, "Segmented Lower transport access PDU: " + MeshParserUtils.bytesToHex(bArrArray, false) + " " + i3 + " of " + length);
            map.put(Integer.valueOf(i3), bArrArray);
        }
        return map;
    }

    public final byte[] a(byte[] bArr, int i, int i2) {
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(i2).order(ByteOrder.BIG_ENDIAN);
        byteBufferOrder.put(bArr, i, i2);
        return byteBufferOrder.array();
    }

    public final void c(a aVar, int i, int i2, byte[] bArr, byte[] bArr2, int i3) {
        Integer num = aVar.f2165c;
        if (num == null) {
            return;
        }
        int iIntValue = num.intValue();
        if (b.a.a.b(Integer.valueOf(iIntValue), i3)) {
            a.a.a.a.b.m.a.a(h, "All segments received cancelling incomplete timer");
            this.f2152c.removeCallbacks(aVar.g);
        }
        byte[] bArrB = b(i, iIntValue);
        a.a.a.a.b.m.a.a(h, "Block acknowledgement payload: " + MeshParserUtils.bytesToHex(bArrB, false));
        b.d.b bVar = new b.d.b();
        bVar.f(0);
        bVar.g(bArrB);
        bVar.h(i2);
        bVar.g(0);
        bVar.f(bArr);
        bVar.a(bArr2);
        bVar.b(a(aVar.f2163a));
        bVar.a(SecureUtils.calculateK2(MeshParserUtils.toByteArray(aVar.f2163a), SecureUtils.K2_MASTER_INPUT));
        bVar.a(aVar.f2163a);
        bVar.e(MeshParserUtils.getSequenceNumberBytes(a()));
        aVar.e = true;
        this.m.sendSegmentAcknowledgementMessage(bVar);
        aVar.f2164b = false;
        a.a.a.a.b.m.a.a(h, "Block ack value: " + iIntValue);
        aVar.f2165c = null;
    }

    public final void a(b.d.b bVar, byte[] bArr) {
        int i = bArr[10] & ByteCompanionObject.MAX_VALUE;
        int length = bArr.length - 10;
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(length).order(ByteOrder.BIG_ENDIAN);
        byteBufferOrder.put(bArr, 10, length);
        byte[] bArrArray = byteBufferOrder.array();
        HashMap<Integer, byte[]> map = new HashMap<>();
        map.put(0, bArrArray);
        bVar.a(false);
        bVar.c(0);
        bVar.f(i);
        bVar.b(map);
        f(bVar);
    }

    public final void f(b.d.b bVar) {
        g(bVar);
        byte[] bArrV = bVar.v();
        if (bVar.n() == 0) {
            bVar.a(new b.a.a(bArrV));
        }
        b(bVar);
    }

    public final byte[] h(b.d.a aVar) {
        byte[] bArrV = aVar.v();
        byte bA = (byte) (aVar.a() | (aVar.b() << 6) | ((aVar.t() ? 1 : 0) << 7));
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(bArrV.length + 1).order(ByteOrder.BIG_ENDIAN);
        byteBufferOrder.put(bA);
        byteBufferOrder.put(bArrV);
        byte[] bArrArray = byteBufferOrder.array();
        a.a.a.a.b.m.a.a(h, "Unsegmented Lower transport access PDU " + MeshParserUtils.bytesToHex(bArrArray, false));
        return bArrArray;
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x01d8  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x01f3  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x020a  */
    /* JADX WARN: Removed duplicated region for block: B:45:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final b.d.a d(java.lang.String r21, byte[] r22) {
        /*
            Method dump skipped, instruction units count: 650
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: b.e.e.d(java.lang.String, byte[]):b.d.a");
    }

    public final void a(a aVar) {
        this.f2152c.postDelayed(aVar.g, 10000L);
        aVar.f2166d = true;
    }

    public final b.d.b e(String str, byte[] bArr) {
        a aVar;
        HashMap<Integer, byte[]> map;
        int i = bArr[10];
        int i2 = (i >> 6) & 1;
        int i3 = i & 63;
        int i4 = (bArr[11] >> 7) & 1;
        int i5 = ((bArr[11] & 127) << 6) | ((bArr[12] & 252) >> 2);
        int i6 = ((bArr[12] & 3) << 3) | ((bArr[13] & 224) >> 5);
        int i7 = bArr[13] & 31;
        int i8 = bArr[2] & 127;
        byte[] srcAddress = MeshParserUtils.getSrcAddress(bArr);
        byte[] dstAddress = MeshParserUtils.getDstAddress(bArr);
        a.a.a.a.b.m.a.a(h, "SEG O: " + i6);
        a.a.a.a.b.m.a.a(h, "SEG N: " + i7);
        String meshNodeCacheKey = MeshParserUtils.getMeshNodeCacheKey(str, srcAddress);
        a aVar2 = this.k.get(meshNodeCacheKey);
        if (aVar2 == null) {
            a aVar3 = new a(str);
            this.k.put(meshNodeCacheKey, aVar3);
            aVar = aVar3;
        } else {
            aVar = aVar2;
        }
        a aVar4 = aVar;
        b(aVar, i5, i8, srcAddress, dstAddress, i7);
        aVar4.f2165c = b.a.a.a(aVar4.f2165c, i6);
        a.a.a.a.b.m.a.a(h, "Block acknowledgement value for " + aVar4.f2165c + " Seg O " + i6);
        int length = bArr.length - 10;
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(length);
        byteBufferAllocate.put(bArr, 10, length);
        HashMap<Integer, byte[]> map2 = this.j.get(meshNodeCacheKey);
        if (map2 == null) {
            HashMap<Integer, byte[]> map3 = new HashMap<>();
            this.j.put(meshNodeCacheKey, map3);
            map = map3;
        } else {
            map = map2;
        }
        map.put(Integer.valueOf(i6), byteBufferAllocate.array());
        if (i7 != map.size() - 1) {
            return null;
        }
        a.a.a.a.b.m.a.a(h, "All segments received");
        this.f2152c.removeCallbacks(aVar4.g);
        a.a.a.a.b.m.a.a(h, "Block ack sent? " + aVar4.e);
        if (aVar4.f > System.currentTimeMillis() && !aVar4.e && MeshParserUtils.isValidUnicastAddress(dstAddress)) {
            this.f2152c.removeCallbacksAndMessages(null);
            a.a.a.a.b.m.a.a(h, "Cancelling Scheduled block ack and incomplete timer, sending an immediate block ack");
            c(aVar4, i5, i8, dstAddress, srcAddress, i7);
        }
        byte[] sequenceNumberBytes = MeshParserUtils.getSequenceNumberBytes(a(MeshParserUtils.getSequenceNumberFromPDU(bArr), i5));
        b.d.b bVar = new b.d.b();
        bVar.c(i4);
        bVar.e(sequenceNumberBytes);
        bVar.b(i2);
        bVar.a(i3);
        bVar.a(true);
        HashMap<Integer, byte[]> map4 = new HashMap<>();
        map4.putAll(map);
        map.clear();
        bVar.b(map4);
        g(bVar);
        b(bVar);
        return bVar;
    }

    public final void a(a aVar, int i, int i2, byte[] bArr, byte[] bArr2, int i3) {
        if (aVar.f2164b) {
            return;
        }
        long j = (i2 * 50) + 150;
        aVar.f = System.currentTimeMillis() + j;
        this.f2152c.postDelayed(new b(this, aVar, i, i2, bArr, bArr2, i3), j);
        aVar.f2164b = true;
        aVar.e = false;
    }

    public final void b(a aVar) {
        if (aVar == null) {
            return;
        }
        if (aVar.f2166d) {
            this.f2152c.removeCallbacks(aVar.g);
        }
        a(aVar);
    }

    public final void b(a aVar, int i, int i2, byte[] bArr, byte[] bArr2, int i3) {
        if (aVar.f2164b) {
            return;
        }
        aVar.f2164b = true;
        long j = (i2 * 50) + 150;
        aVar.f = System.currentTimeMillis() + j;
        this.f2152c.postDelayed(new c(this, aVar, i, i2, bArr, bArr2, i3), j);
    }

    public final byte[] b(int i, int i2) {
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(6).order(ByteOrder.BIG_ENDIAN);
        byteBufferOrder.put((byte) (((i >> 6) & 127) | 0));
        byteBufferOrder.put((byte) (((i << 2) & 252) | 0));
        byteBufferOrder.putInt(i2);
        return byteBufferOrder.array();
    }
}
