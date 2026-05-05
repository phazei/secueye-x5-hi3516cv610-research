package meshprovisioner.utils;

import a.a.a.a.b.m.a;
import android.os.Parcel;
import android.os.Parcelable;
import c.a.b.e;
import c.a.b.e.b;
import c.a.b.f.c;
import c.a.b.h.k;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import kotlin.jvm.internal.ByteCompanionObject;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.spongycastle.crypto.InvalidCipherTextException;
import org.spongycastle.jce.provider.BouncyCastleProvider;

/* JADX INFO: loaded from: classes4.dex */
public class SecureUtils {
    public static final int ENC_K3_OUTPUT_MASK = 127;
    public static final int ENC_K4_OUTPUT_MASK = 63;
    public static final int HASH_LENGTH = 8;
    public static final byte[] PRCK = "prck".getBytes(Charset.forName("US-ASCII"));
    public static final byte[] PRSK = "prsk".getBytes(Charset.forName("US-ASCII"));
    public static final byte[] PRSN = "prsn".getBytes(Charset.forName("US-ASCII"));
    public static final byte[] PRDK = "prdk".getBytes(Charset.forName("US-ASCII"));
    public static final byte[] K2_MASTER_INPUT = {0};
    public static final byte[] SMK2 = "smk2".getBytes(Charset.forName("US-ASCII"));
    public static final byte[] SMK3 = "smk3".getBytes(Charset.forName("US-ASCII"));
    public static final byte[] SMK3_DATA = "id64".getBytes(Charset.forName("US-ASCII"));
    public static final byte[] SMK4 = "smk4".getBytes(Charset.forName("US-ASCII"));
    public static final byte[] SMK4_DATA = "id6".getBytes(Charset.forName("US-ASCII"));
    public static final byte[] SALT_KEY = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public static final byte[] NONCE_PADDING = {0, 0, 0, 0, 0, 0, 0, 0};
    public static final String TAG = SecureUtils.class.getSimpleName();
    public static final byte[] NKIK = "nkik".getBytes(Charset.forName("US-ASCII"));
    public static final byte[] ID128 = "id128".getBytes(Charset.forName("US-ASCII"));
    public static final byte[] HASH_PADDING = {0, 0, 0, 0, 0, 0};
    public static int NRF_MESH_KEY_SIZE = 16;

    public static class K2Output implements Parcelable {
        public static final Parcelable.Creator<K2Output> CREATOR = new Parcelable.Creator<K2Output>() { // from class: meshprovisioner.utils.SecureUtils.K2Output.1
            @Override // android.os.Parcelable.Creator
            public K2Output createFromParcel(Parcel parcel) {
                return new K2Output(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public K2Output[] newArray(int i) {
                return new K2Output[i];
            }
        };
        public byte[] encryptionKey;
        public byte nid;
        public byte[] privacyKey;

        public K2Output() {
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public byte[] getEncryptionKey() {
            return this.encryptionKey;
        }

        public byte getNid() {
            return this.nid;
        }

        public byte[] getPrivacyKey() {
            return this.privacyKey;
        }

        public void setEncryptionKey(byte[] bArr) {
            this.encryptionKey = bArr;
        }

        public void setNid(byte b2) {
            this.nid = b2;
        }

        public void setPrivacyKey(byte[] bArr) {
            this.privacyKey = bArr;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeByte(this.nid);
            parcel.writeByteArray(this.encryptionKey);
            parcel.writeByteArray(this.privacyKey);
        }

        public K2Output(byte b2, byte[] bArr, byte[] bArr2) {
            this.nid = b2;
            this.encryptionKey = bArr;
            this.privacyKey = bArr2;
        }

        public K2Output(Parcel parcel) {
            this.nid = parcel.readByte();
            this.encryptionKey = parcel.createByteArray();
            this.privacyKey = parcel.createByteArray();
        }
    }

    static {
        a.c(TAG, "insert BouncyCastleProvider");
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }

    public static final byte[] calculateCMAC(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[16];
        k kVar = new k(bArr2);
        b bVar = new b(new c.a.b.c.a());
        bVar.init(kVar);
        bVar.update(bArr, 0, bArr.length);
        bVar.a(bArr3, 0);
        return bArr3;
    }

    public static byte[] calculateHash(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(HASH_PADDING.length + bArr2.length + bArr3.length).order(ByteOrder.BIG_ENDIAN);
        byteBufferOrder.put(HASH_PADDING);
        byteBufferOrder.put(bArr2);
        byteBufferOrder.put(bArr3);
        byte[] bArrEncryptWithAES = encryptWithAES(byteBufferOrder.array(), bArr);
        ByteBuffer byteBufferOrder2 = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN);
        byteBufferOrder2.put(bArrEncryptWithAES, 8, 8);
        return byteBufferOrder2.array();
    }

    public static final byte[] calculateIdentityKey(byte[] bArr) {
        byte[] bArrCalculateSalt = calculateSalt(NKIK);
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(ID128.length + 1);
        byteBufferAllocate.put(ID128);
        byteBufferAllocate.put((byte) 1);
        return calculateK1(bArr, bArrCalculateSalt, byteBufferAllocate.array());
    }

    public static final byte[] calculateK1(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        return calculateCMAC(bArr3, calculateCMAC(bArr, bArr2));
    }

    public static K2Output calculateK2(byte[] bArr, byte[] bArr2) {
        byte[] bArrCalculateCMAC = calculateCMAC(bArr, calculateSalt(SMK2));
        byte[] bArr3 = new byte[0];
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(bArr3.length + bArr2.length + 1);
        byteBufferAllocate.put(bArr3);
        byteBufferAllocate.put(bArr2);
        byteBufferAllocate.put((byte) 1);
        byte[] bArrCalculateCMAC2 = calculateCMAC(byteBufferAllocate.array(), bArrCalculateCMAC);
        byte b2 = (byte) (bArrCalculateCMAC2[15] & ByteCompanionObject.MAX_VALUE);
        ByteBuffer byteBufferAllocate2 = ByteBuffer.allocate(bArrCalculateCMAC2.length + bArr2.length + 1);
        byteBufferAllocate2.put(bArrCalculateCMAC2);
        byteBufferAllocate2.put(bArr2);
        byteBufferAllocate2.put((byte) 2);
        byte[] bArrCalculateCMAC3 = calculateCMAC(byteBufferAllocate2.array(), bArrCalculateCMAC);
        ByteBuffer byteBufferAllocate3 = ByteBuffer.allocate(bArrCalculateCMAC3.length + bArr2.length + 1);
        byteBufferAllocate3.put(bArrCalculateCMAC3);
        byteBufferAllocate3.put(bArr2);
        byteBufferAllocate3.put((byte) 3);
        return new K2Output(b2, bArrCalculateCMAC3, calculateCMAC(byteBufferAllocate3.array(), bArrCalculateCMAC));
    }

    public static byte[] calculateK3(byte[] bArr) {
        byte[] bArrCalculateCMAC = calculateCMAC(bArr, calculateSalt(SMK3));
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(SMK3_DATA.length + 1);
        byteBufferAllocate.put(SMK3_DATA);
        byteBufferAllocate.put((byte) 1);
        byte[] bArrCalculateCMAC2 = calculateCMAC(byteBufferAllocate.array(), bArrCalculateCMAC);
        byte[] bArr2 = new byte[8];
        System.arraycopy(bArrCalculateCMAC2, bArrCalculateCMAC2.length - bArr2.length, bArr2, 0, bArr2.length);
        return bArr2;
    }

    public static final byte calculateK4(byte[] bArr) {
        byte[] bArrCalculateCMAC = calculateCMAC(bArr, calculateSalt(SMK4));
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(SMK4_DATA.length + 1);
        byteBufferAllocate.put(SMK4_DATA);
        byteBufferAllocate.put((byte) 1);
        return (byte) (calculateCMAC(byteBufferAllocate.array(), bArrCalculateCMAC)[15] & 63);
    }

    public static final byte[] calculateSalt(byte[] bArr) {
        return calculateCMAC(bArr, SALT_KEY);
    }

    public static byte[] calculateSha256(byte[] bArr) {
        try {
            return MessageDigest.getInstance(MessageDigestAlgorithms.SHA_256).digest(bArr);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final byte[] decryptCCM(byte[] bArr, byte[] bArr2, byte[] bArr3, int i) {
        byte[] bArr4 = new byte[bArr.length];
        c cVar = new c(new c.a.b.c.a());
        cVar.a(false, (e) new c.a.b.h.a(new k(bArr2), i * 8, bArr3));
        cVar.a(bArr, 0, bArr.length, bArr4, 0);
        try {
            cVar.a(bArr4, 0);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
        }
        int length = bArr.length - i;
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(length).order(ByteOrder.BIG_ENDIAN);
        byteBufferOrder.put(bArr4, 0, length);
        return byteBufferOrder.array();
    }

    public static final byte[] decryptCCMWithThrowsException(byte[] bArr, byte[] bArr2, byte[] bArr3, int i) {
        byte[] bArr4 = new byte[bArr.length];
        c cVar = new c(new c.a.b.c.a());
        cVar.a(false, (e) new c.a.b.h.a(new k(bArr2), i * 8, bArr3));
        cVar.a(bArr, 0, bArr.length, bArr4, 0);
        cVar.a(bArr4, 0);
        int length = bArr.length - i;
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(length).order(ByteOrder.BIG_ENDIAN);
        byteBufferOrder.put(bArr4, 0, length);
        return byteBufferOrder.array();
    }

    public static final byte[] encryptCCM(byte[] bArr, byte[] bArr2, byte[] bArr3, int i) {
        byte[] bArr4 = new byte[bArr.length + i];
        c cVar = new c(new c.a.b.c.a());
        cVar.a(true, (e) new c.a.b.h.a(new k(bArr2), i * 8, bArr3));
        cVar.a(bArr, 0, bArr.length, bArr4, bArr.length);
        try {
            cVar.a(bArr4, 0);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
        }
        return bArr4;
    }

    public static final byte[] encryptWithAES(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[bArr.length];
        k kVar = new k(bArr2);
        c.a.b.c.b bVar = new c.a.b.c.b();
        bVar.a(true, (e) kVar);
        bVar.a(bArr, 0, bArr3, 0);
        return bArr3;
    }

    public static final String generateRandomApplicationKey() {
        return MeshParserUtils.bytesToHex(generateRandomNumber(), false);
    }

    public static final String generateRandomNetworkKey() {
        return MeshParserUtils.bytesToHex(generateRandomNumber(), false);
    }

    public static final byte[] generateRandomNonce() {
        byte[] bArr = new byte[8];
        new SecureRandom().nextBytes(bArr);
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(NONCE_PADDING.length + bArr.length).order(ByteOrder.BIG_ENDIAN);
        byteBufferOrder.put(NONCE_PADDING);
        byteBufferOrder.put(bArr);
        return bArr;
    }

    public static final byte[] generateRandomNumber() {
        byte[] bArr = new byte[16];
        new SecureRandom().nextBytes(bArr);
        return bArr;
    }

    public static final byte[] generateRandomNumberWithSeedByNonaTime(int i) {
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(System.nanoTime());
        byte[] bArr = new byte[i / 8];
        secureRandom.nextBytes(bArr);
        return bArr;
    }

    public static int getNetMicLength(int i) {
        return i == 0 ? 4 : 8;
    }

    public static int getTransMicLength(int i) {
        return i == 0 ? 4 : 8;
    }

    public static final byte[] generateRandomNumber(int i) {
        byte[] bArr = new byte[i / 8];
        new SecureRandom().nextBytes(bArr);
        return bArr;
    }
}
