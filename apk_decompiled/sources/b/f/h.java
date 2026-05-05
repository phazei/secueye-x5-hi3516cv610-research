package b.f;

import androidx.annotation.VisibleForTesting;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import kotlin.jvm.internal.ByteCompanionObject;
import meshprovisioner.utils.MeshParserUtils;
import meshprovisioner.utils.SecureUtils;

/* JADX INFO: compiled from: UpperTransportLayer.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class h extends a {
    public static final int APPLICATION_KEY_IDENTIFIER = 0;
    public static final int DEFAULT_UNSEGMENTED_MIC_LENGTH = 4;
    public static final int MAXIMUM_TRANSMIC_LENGTH = 8;
    public static final int MAX_SEGMENTED_ACCESS_PAYLOAD_LENGTH = 12;
    public static final int MAX_SEGMENTED_CONTROL_PAYLOAD_LENGTH = 8;
    public static final int MAX_UNSEGMENTED_ACCESS_PAYLOAD_LENGTH = 15;
    public static final int MAX_UNSEGMENTED_CONTROL_PAYLOAD_LENGTH = 11;
    public static final int MINIMUM_TRANSMIC_LENGTH = 4;
    public static final int NONCE_TYPE_APPLICATION = 1;
    public static final int NONCE_TYPE_DEVICE = 2;
    public static final int NONCE_TYPE_NETWORK = 0;
    public static final int NONCE_TYPE_PROXY = 3;
    public static final int PAD_APPLICATION_DEVICE_NONCE = 0;
    public static final int PAD_NETWORK_NONCE = 0;
    public static final int PAD_PROXY_NONCE = 0;
    public static final int SZMIC = 1;
    public static final String TAG = "h";
    public static final int TRANSPORT_SAR_SEQZERO_MASK = 8191;

    private byte[] createApplicationNonce(int i, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4) {
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(13);
        byteBufferAllocate.put((byte) 1);
        byteBufferAllocate.put((byte) ((i << 7) | 0));
        byteBufferAllocate.put(bArr);
        byteBufferAllocate.put(bArr2);
        byteBufferAllocate.put(bArr3);
        byteBufferAllocate.put(bArr4);
        return byteBufferAllocate.array();
    }

    private byte[] createDeviceNonce(int i, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4) {
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(13);
        byteBufferAllocate.put((byte) 2);
        byteBufferAllocate.put((byte) ((i << 7) | 0));
        byteBufferAllocate.put(bArr);
        byteBufferAllocate.put(bArr2);
        byteBufferAllocate.put(bArr3);
        byteBufferAllocate.put(bArr4);
        return byteBufferAllocate.array();
    }

    private byte[] decryptUpperTransportPDU(b.d.a aVar) {
        byte[] applicationKey;
        byte[] bArrCreateApplicationNonce;
        if (aVar.b() == 0) {
            applicationKey = this.mMeshNode.getDeviceKey();
            if (applicationKey == null) {
                throw new IllegalArgumentException("Unable to find the device key to decrypt the message");
            }
            bArrCreateApplicationNonce = createDeviceNonce(aVar.c(), aVar.q(), aVar.r(), aVar.f(), aVar.g());
        } else {
            applicationKey = getApplicationKey(aVar.a());
            if (applicationKey == null) {
                throw new IllegalArgumentException("Unable to find the app key to decrypt the message");
            }
            if (SecureUtils.calculateK4(applicationKey) != aVar.a()) {
                throw new IllegalArgumentException("Unable to decrypt the message, invalid application key identifier");
            }
            bArrCreateApplicationNonce = createApplicationNonce(aVar.c(), aVar.q(), aVar.r(), aVar.f(), aVar.g());
        }
        byte[] bArrDecryptCCM = aVar.c() == 1 ? SecureUtils.decryptCCM(aVar.v(), applicationKey, bArrCreateApplicationNonce, 8) : SecureUtils.decryptCCM(aVar.v(), applicationKey, bArrCreateApplicationNonce, 4);
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(new byte[bArrDecryptCCM.length]);
        byteBufferWrap.order(ByteOrder.LITTLE_ENDIAN);
        byteBufferWrap.put(bArrDecryptCCM);
        return byteBufferWrap.array();
    }

    private byte[] encryptUpperTransportPDU(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5, int i, int i2, byte[] bArr6) {
        return SecureUtils.encryptCCM(bArr6, bArr5, i == 0 ? createDeviceNonce(0, bArr, bArr2, bArr3, bArr4) : createApplicationNonce(0, bArr, bArr2, bArr3, bArr4), i2);
    }

    private byte[] getApplicationKey(int i) {
        Iterator it = new ArrayList(this.mMeshNode.getAddedAppKeys().values()).iterator();
        while (it.hasNext()) {
            byte[] byteArray = MeshParserUtils.toByteArray((String) it.next());
            if (i == SecureUtils.calculateK4(byteArray)) {
                return byteArray;
            }
        }
        return null;
    }

    public abstract void createLowerTransportAccessPDU(b.d.a aVar);

    public abstract void createLowerTransportControlPDU(b.d.b bVar);

    @Override // b.f.a
    public void createMeshMessage(b.d.c cVar) {
        super.createMeshMessage(cVar);
        b.d.a aVar = (b.d.a) cVar;
        byte[] bArrEncryptUpperTransportPDU = encryptUpperTransportPDU(aVar);
        a.a.a.a.b.m.a.a(TAG, "Encrypted upper transport pdu: " + MeshParserUtils.bytesToHex(bArrEncryptUpperTransportPDU, false));
        aVar.h(bArrEncryptUpperTransportPDU);
    }

    @VisibleForTesting(otherwise = 4)
    public void createUpperTransportPDU(b.d.a aVar) {
        byte[] bArrEncryptUpperTransportPDU = encryptUpperTransportPDU(aVar);
        a.a.a.a.b.m.a.a(TAG, "Encrypted upper transport pdu: " + MeshParserUtils.bytesToHex(bArrEncryptUpperTransportPDU, false));
        aVar.h(bArrEncryptUpperTransportPDU);
    }

    @Override // b.f.a
    public void createVendorMeshMessage(b.d.c cVar) {
        super.createVendorMeshMessage(cVar);
        b.d.a aVar = (b.d.a) cVar;
        byte[] bArrEncryptUpperTransportPDU = encryptUpperTransportPDU(aVar);
        a.a.a.a.b.m.a.a(TAG, "Encrypted upper transport pdu: " + MeshParserUtils.bytesToHex(bArrEncryptUpperTransportPDU, false));
        aVar.h(bArrEncryptUpperTransportPDU);
    }

    public final int getTransportLayerSequenceNumber(int i, int i2) {
        int i3 = i & TRANSPORT_SAR_SEQZERO_MASK;
        return i3 < i2 ? (i - (i3 - i2)) - 8192 : i - (i3 - i2);
    }

    public final void parseUpperTransportPDU(b.d.a aVar) {
        if (aVar.e() == 0) {
            reassembleLowerTransportAccessPDU(aVar);
            aVar.g(decryptUpperTransportPDU(aVar));
        }
    }

    public abstract void reassembleLowerTransportAccessPDU(b.d.a aVar);

    public abstract void reassembleLowerTransportControlPDU(b.d.b bVar);

    private byte[] encryptUpperTransportPDU(b.d.a aVar) {
        byte[] bArrCreateApplicationNonce;
        int transMicLength;
        byte[] bArrU = aVar.u();
        int iB = aVar.b();
        int iC = aVar.c();
        byte[] bArrQ = aVar.q();
        byte[] bArrR = aVar.r();
        byte[] bArrF = aVar.f();
        byte[] bArrG = aVar.g();
        byte[] bArrI = aVar.i();
        if (iB == 0) {
            bArrCreateApplicationNonce = createDeviceNonce(iC, bArrQ, bArrR, bArrF, bArrG);
            a.a.a.a.b.m.a.a(TAG, "Device nonce: " + MeshParserUtils.bytesToHex(bArrCreateApplicationNonce, false));
        } else {
            bArrCreateApplicationNonce = createApplicationNonce(iC, bArrQ, bArrR, bArrF, bArrG);
            a.a.a.a.b.m.a.a(TAG, "Application nonce: " + MeshParserUtils.bytesToHex(bArrCreateApplicationNonce, false));
        }
        if (bArrU.length + 4 <= 15) {
            transMicLength = SecureUtils.getTransMicLength(aVar.e());
        } else {
            transMicLength = SecureUtils.getTransMicLength(aVar.c());
        }
        return SecureUtils.encryptCCM(bArrU, bArrI, bArrCreateApplicationNonce, transMicLength);
    }

    public final void parseUpperTransportPDU(b.d.b bVar) {
        if (bVar.e() == 1) {
            byte[] bArrV = bVar.v();
            if (((byte) (bArrV[0] & ByteCompanionObject.MAX_VALUE)) != 10) {
                return;
            }
            byte b2 = bArrV[1];
            Arrays.copyOfRange(bArrV, 1, bArrV.length);
        }
    }
}
