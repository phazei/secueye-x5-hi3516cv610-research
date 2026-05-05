package b.e;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.ArrayMap;
import androidx.annotation.VisibleForTesting;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import kotlin.jvm.internal.ByteCompanionObject;
import meshprovisioner.utils.MeshParserUtils;
import meshprovisioner.utils.SecureUtils;

/* JADX INFO: compiled from: NetworkLayer.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class j extends e {
    public static final String n = "" + j.class.getSimpleName();
    public ArrayMap<String, HashMap<Integer, byte[]>> o = new ArrayMap<>();
    public ArrayMap<String, HashMap<Integer, byte[]>> p = new ArrayMap<>();
    public ArrayMap<String, Integer> q = new ArrayMap<>();

    @Override // b.e.e, b.e.k, b.e.a
    public final void a(b.d.c cVar) {
        if (cVar instanceof b.d.a) {
            super.a(cVar);
        } else {
            super.a(cVar);
        }
        b(cVar);
    }

    @VisibleForTesting(otherwise = 4)
    public final b.d.c b(b.d.c cVar) {
        byte b2;
        SecureUtils.K2Output k2OutputH = cVar.h();
        byte nid = k2OutputH.getNid();
        byte[] encryptionKey = k2OutputH.getEncryptionKey();
        String str = n;
        StringBuilder sb = new StringBuilder();
        sb.append("Encryption key: ");
        sb.append(MeshParserUtils.bytesToHex(encryptionKey, false));
        a.a.a.a.b.m.a.a(str, sb.toString());
        byte[] privacyKey = k2OutputH.getPrivacyKey();
        a.a.a.a.b.m.a.a(n, "Privacy key: " + MeshParserUtils.bytesToHex(privacyKey, false));
        int iE = cVar.e();
        int iS = cVar.s();
        byte b3 = (byte) (nid | ((cVar.g()[3] & 1) << 7));
        byte b4 = (byte) (iS | (iE << 7));
        byte[] bArrR = cVar.r();
        HashMap<Integer, byte[]> mapJ = iE == 0 ? cVar.j() : cVar.k();
        HashMap map = new HashMap();
        ArrayList arrayList = new ArrayList();
        int iP = cVar.p();
        int iP2 = cVar.p();
        if (iP2 == 0) {
            b2 = b3;
            for (int i = 0; i < mapJ.size(); i++) {
                byte[] bArr = mapJ.get(Integer.valueOf(i));
                if (i != 0) {
                    cVar.e(MeshParserUtils.getSequenceNumberBytes(a(cVar.q())));
                }
                arrayList.add(cVar.q());
                a.a.a.a.b.m.a.a(n, "Sequence Number: " + MeshParserUtils.bytesToHex((byte[]) arrayList.get(i), false));
                byte[] bArrA = a(cVar, (byte[]) arrayList.get(i), bArr, encryptionKey);
                map.put(Integer.valueOf(i), bArrA);
                a.a.a.a.b.m.a.a(n, "Encrypted Network payload: " + MeshParserUtils.bytesToHex(bArrA, false));
            }
        } else if (iP2 != 2) {
            b2 = b3;
        } else {
            int i2 = 0;
            while (i2 < mapJ.size()) {
                byte[] bArr2 = mapJ.get(Integer.valueOf(i2));
                cVar.e(MeshParserUtils.getSequenceNumberBytes(a()));
                arrayList.add(cVar.q());
                byte[] bArrA2 = a(cVar, bArr2, encryptionKey);
                map.put(Integer.valueOf(i2), bArrA2);
                a.a.a.a.b.m.a.a(n, "Encrypted Network payload: " + MeshParserUtils.bytesToHex(bArrA2, false));
                i2++;
                b3 = b3;
            }
            b2 = b3;
        }
        HashMap<Integer, byte[]> map2 = new HashMap<>();
        for (int i3 = 0; i3 < map.size(); i3++) {
            byte[] bArr3 = (byte[]) map.get(Integer.valueOf(i3));
            byte[] bArrB = b(b4, (byte[]) arrayList.get(i3), bArrR, a(cVar.g(), b(bArr3), privacyKey));
            map2.put(Integer.valueOf(i3), ByteBuffer.allocate(bArrB.length + 2 + bArr3.length).order(ByteOrder.BIG_ENDIAN).put((byte) iP).put(b2).put(bArrB).put(bArr3).array());
            cVar.c(map2);
        }
        return cVar;
    }

    public void f(String str, byte[] bArr) {
        if (TextUtils.isEmpty(str) || bArr == null || bArr.length < 1) {
            return;
        }
        String meshNodeCacheKey = MeshParserUtils.getMeshNodeCacheKey(str, bArr);
        if (TextUtils.isEmpty(meshNodeCacheKey)) {
            return;
        }
        this.q.put(meshNodeCacheKey, -1);
        a.a.a.a.b.m.a.c(n, "clearReplaySeqNumber success, " + MeshParserUtils.bytesToHex(bArr, false));
    }

    @SuppressLint({"DefaultLocale"})
    public final b.d.c g(String str, byte[] bArr) {
        byte[] bArrA = a(SecureUtils.calculateK2(MeshParserUtils.toByteArray(str), SecureUtils.K2_MASTER_INPUT), bArr);
        byte b2 = bArrA[0];
        int i = (b2 >> 7) & 1;
        int i2 = b2 & ByteCompanionObject.MAX_VALUE;
        int netMicLength = SecureUtils.getNetMicLength(i);
        byte[] bArrArray = ByteBuffer.allocate(3).order(ByteOrder.BIG_ENDIAN).put(bArrA, 1, 3).array();
        byte[] bArrArray2 = ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).put(bArrA, 4, 2).array();
        byte[] bArrA2 = a(b2, bArrArray, bArrArray2, a(str));
        a.a.a.a.b.m.a.a(n, "Received message, TTL: " + i2 + ", CTL: " + i + ", SRC: " + MeshParserUtils.bytesToHex(bArrArray2, false));
        if (b(str, bArrArray2, MeshParserUtils.getSequenceNumber(bArrArray))) {
            return null;
        }
        if (i == 1) {
            return b(str, bArr, bArrA, bArrA2, bArrArray2, bArrArray, netMicLength);
        }
        a.a.a.a.b.m.a.a(n, "Sequence number of received access message: " + MeshParserUtils.getSequenceNumber(bArrArray));
        return a(str, bArr, bArrA, bArrA2, bArrArray2, bArrArray, netMicLength);
    }

    public final byte[] a(b.d.c cVar, byte[] bArr, byte[] bArr2, byte[] bArr3) {
        byte[] bArrA = a((byte) ((cVar.e() << 7) | cVar.s()), bArr, cVar.r(), cVar.g());
        a.a.a.a.b.m.a.a(n, "Network nonce: " + MeshParserUtils.bytesToHex(bArrA, false));
        byte[] bArrF = cVar.f();
        return SecureUtils.encryptCCM(ByteBuffer.allocate(bArrF.length + bArr2.length).order(ByteOrder.BIG_ENDIAN).put(bArrF).put(bArr2).array(), bArr3, bArrA, SecureUtils.getNetMicLength(cVar.e()));
    }

    public final byte[] a(b.d.c cVar, byte[] bArr, byte[] bArr2) {
        byte[] bArrB = b(cVar.q(), cVar.r(), cVar.g());
        a.a.a.a.b.m.a.a(n, "Proxy nonce: " + MeshParserUtils.bytesToHex(bArrB, false));
        byte[] bArrF = cVar.f();
        return SecureUtils.encryptCCM(ByteBuffer.allocate(bArrF.length + bArr.length).order(ByteOrder.BIG_ENDIAN).put(bArrF).put(bArr).array(), bArr2, bArrB, SecureUtils.getNetMicLength(cVar.e()));
    }

    public final byte[] a(SecureUtils.K2Output k2Output, byte[] bArr) {
        byte[] privacyKey = k2Output.getPrivacyKey();
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(6);
        byteBufferAllocate.order(ByteOrder.BIG_ENDIAN);
        byteBufferAllocate.put(bArr, 2, 6);
        byte[] bArrArray = byteBufferAllocate.array();
        ByteBuffer byteBufferAllocate2 = ByteBuffer.allocate(7);
        byteBufferAllocate2.order(ByteOrder.BIG_ENDIAN);
        byteBufferAllocate2.put(bArr, 8, 7);
        byte[] bArrA = a(new byte[]{0, 0, 0, 0}, b(byteBufferAllocate2.array()), privacyKey);
        byte[] bArr2 = new byte[6];
        for (int i = 0; i < 6; i++) {
            bArr2[i] = (byte) (bArrArray[i] ^ bArrA[i]);
        }
        return bArr2;
    }

    public final byte[] a(byte b2, byte[] bArr, byte[] bArr2, byte[] bArr3) {
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(13);
        byteBufferAllocate.put((byte) 0);
        byteBufferAllocate.put(b2);
        byteBufferAllocate.put(bArr);
        byteBufferAllocate.put(bArr2);
        byteBufferAllocate.put(new byte[]{0, 0});
        byteBufferAllocate.put(bArr3);
        return byteBufferAllocate.array();
    }

    public final byte[] a(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(bArr2.length + 5 + bArr.length);
        byteBufferAllocate.order(ByteOrder.BIG_ENDIAN);
        byteBufferAllocate.put(new byte[]{0, 0, 0, 0, 0});
        byteBufferAllocate.put(bArr);
        byteBufferAllocate.put(bArr2);
        return SecureUtils.encryptWithAES(byteBufferAllocate.array(), bArr3);
    }

    @VisibleForTesting
    public final b.d.a a(String str, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5, int i) {
        byte[] encryptionKey = SecureUtils.calculateK2(MeshParserUtils.toByteArray(str), SecureUtils.K2_MASTER_INPUT).getEncryptionKey();
        int i2 = bArr2[0] & ByteCompanionObject.MAX_VALUE;
        int length = bArr.length - (bArr2.length + 2);
        byte[] bArr6 = new byte[length];
        System.arraycopy(bArr, 8, bArr6, 0, length);
        byte[] bArrDecryptCCM = SecureUtils.decryptCCM(bArr6, encryptionKey, bArr3, i);
        byte[] bArrArray = ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).put(bArrDecryptCCM, 0, 2).array();
        if (a(bArrDecryptCCM[2])) {
            a(str, bArr4, false, bArr);
            b.d.a aVarD = d(str, ByteBuffer.allocate(bArr2.length + 2 + bArrDecryptCCM.length).order(ByteOrder.BIG_ENDIAN).put(bArr, 0, 2).put(bArr2).put(bArrDecryptCCM).array());
            if (aVarD != null) {
                HashMap<Integer, byte[]> mapA = a(str, bArr4, false);
                aVarD.b(a(str));
                aVarD.c(mapA);
                aVarD.e(0);
                aVarD.h(i2);
                aVarD.f(bArr4);
                aVarD.a(bArrArray);
                aVarD.a(str);
                d(aVarD);
                b(aVarD);
            }
            return aVarD;
        }
        b.d.a aVar = new b.d.a();
        aVar.b(a(str));
        HashMap<Integer, byte[]> map = new HashMap<>();
        map.put(0, bArr);
        aVar.c(map);
        aVar.h(i2);
        aVar.f(bArr4);
        aVar.a(bArrArray);
        aVar.e(bArr5);
        aVar.a(str);
        b(aVar, ByteBuffer.allocate(bArr2.length + 2 + bArrDecryptCCM.length).order(ByteOrder.BIG_ENDIAN).put(bArr, 0, 2).put(bArr2).put(bArrDecryptCCM).array());
        d(aVar);
        b(aVar);
        return aVar;
    }

    public final byte[] b(byte b2, byte[] bArr, byte[] bArr2, byte[] bArr3) {
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(bArr.length + 1 + bArr2.length).order(ByteOrder.BIG_ENDIAN);
        byteBufferOrder.put(b2);
        byteBufferOrder.put(bArr);
        byteBufferOrder.put(bArr2);
        byte[] bArrArray = byteBufferOrder.array();
        ByteBuffer.allocate(6).put(bArr3, 0, 6);
        byte[] bArr4 = new byte[6];
        for (int i = 0; i < 6; i++) {
            bArr4[i] = (byte) (bArrArray[i] ^ bArr3[i]);
        }
        return bArr4;
    }

    public final byte[] b(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(13);
        byteBufferAllocate.put((byte) 3);
        byteBufferAllocate.put((byte) 0);
        byteBufferAllocate.put(bArr);
        byteBufferAllocate.put(bArr2);
        byteBufferAllocate.put(new byte[]{0, 0});
        byteBufferAllocate.put(bArr3);
        return byteBufferAllocate.array();
    }

    public final byte[] b(byte[] bArr) {
        byte[] bArr2 = new byte[7];
        System.arraycopy(bArr, 0, bArr2, 0, bArr2.length);
        return bArr2;
    }

    @VisibleForTesting
    public final b.d.b b(String str, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5, int i) {
        byte[] encryptionKey = SecureUtils.calculateK2(MeshParserUtils.toByteArray(str), SecureUtils.K2_MASTER_INPUT).getEncryptionKey();
        int i2 = bArr2[0] & ByteCompanionObject.MAX_VALUE;
        int length = bArr.length - (bArr2.length + 2);
        byte[] bArr6 = new byte[length];
        System.arraycopy(bArr, 8, bArr6, 0, length);
        byte[] bArrDecryptCCM = SecureUtils.decryptCCM(bArr6, encryptionKey, bArr3, i);
        byte[] bArrArray = ByteBuffer.allocate(2).order(ByteOrder.BIG_ENDIAN).put(bArrDecryptCCM, 0, 2).array();
        if (a(bArrDecryptCCM[2])) {
            a(str, bArr4, true, bArr);
            b.d.b bVarE = e(str, ByteBuffer.allocate(bArr2.length + 2 + bArrDecryptCCM.length).order(ByteOrder.BIG_ENDIAN).put(bArr, 0, 2).put(bArr2).put(bArrDecryptCCM).array());
            if (bVarE != null) {
                HashMap<Integer, byte[]> mapA = a(str, bArr4, true);
                bVarE.b(a(str));
                bVarE.c(mapA);
                bVarE.e(1);
                bVarE.h(i2);
                bVarE.f(bArr4);
                bVarE.a(bArrArray);
            }
            return bVarE;
        }
        b.d.b bVar = new b.d.b();
        bVar.b(a(str));
        HashMap<Integer, byte[]> map = new HashMap<>();
        map.put(0, bArr);
        bVar.c(map);
        bVar.h(i2);
        bVar.f(bArr4);
        bVar.a(bArrArray);
        bVar.e(bArr5);
        a(bVar, ByteBuffer.allocate(bArr2.length + 2 + bArrDecryptCCM.length).order(ByteOrder.BIG_ENDIAN).put(bArr, 0, 2).put(bArr2).put(bArrDecryptCCM).array());
        return bVar;
    }

    public final HashMap<Integer, byte[]> a(String str, byte[] bArr, boolean z, byte[] bArr2) {
        HashMap<Integer, byte[]> map;
        String meshNodeCacheKey = MeshParserUtils.getMeshNodeCacheKey(str, bArr);
        if (z) {
            map = this.p.get(meshNodeCacheKey);
            if (map == null) {
                map = new HashMap<>();
                map.put(0, bArr2);
            } else {
                map.put(Integer.valueOf(map.size()), bArr2);
            }
            this.p.put(meshNodeCacheKey, map);
        } else {
            map = this.o.get(meshNodeCacheKey);
            if (map == null) {
                map = new HashMap<>();
                map.put(0, bArr2);
            } else {
                map.put(Integer.valueOf(map.size()), bArr2);
            }
            this.o.put(meshNodeCacheKey, map);
        }
        return map;
    }

    public final HashMap<Integer, byte[]> a(String str, byte[] bArr, boolean z) {
        String meshNodeCacheKey = MeshParserUtils.getMeshNodeCacheKey(str, bArr);
        if (z) {
            HashMap<Integer, byte[]> map = this.p.get(meshNodeCacheKey);
            this.p.remove(meshNodeCacheKey);
            return map;
        }
        HashMap<Integer, byte[]> map2 = this.o.get(meshNodeCacheKey);
        this.o.remove(meshNodeCacheKey);
        return map2;
    }

    public final boolean b(String str, byte[] bArr, int i) {
        if (a.a.a.a.b.d.a.f1316b) {
            return false;
        }
        String meshNodeCacheKey = MeshParserUtils.getMeshNodeCacheKey(str, bArr);
        Integer num = this.q.get(meshNodeCacheKey);
        if (num != null) {
            if (num.intValue() > i && num.intValue() - i >= 10) {
                this.q.put(meshNodeCacheKey, Integer.valueOf(i));
                return false;
            }
            if (num.intValue() >= i) {
                a.a.a.a.b.m.a.d(n, String.format(Locale.getDefault(), "detected replay attacks, device(%s) last seq: %d, received: %d", MeshParserUtils.bytesToHex(bArr, false), num, Integer.valueOf(i)));
                return true;
            }
        }
        this.q.put(meshNodeCacheKey, Integer.valueOf(i));
        return false;
    }
}
