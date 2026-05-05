package b.f;

import androidx.annotation.VisibleForTesting;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;
import kotlin.jvm.internal.ByteCompanionObject;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: compiled from: LowerTransportLayer.java */
/* JADX INFO: loaded from: classes.dex */
public abstract class e extends h {
    public static final long INCOMPLETE_TIMER_DELAY = 10000;
    public static final int NETWORK_PDU = 0;
    public static final int SEGMENTED_HEADER = 1;
    public static final int SEGMENTED_MESSAGE_HEADER_LENGTH = 4;
    public static final String TAG = "e";
    public static final int UNSEGMENTED_ACK_MESSAGE_HEADER_LENGTH = 3;
    public static final int UNSEGMENTED_HEADER = 0;
    public static final int UNSEGMENTED_MESSAGE_HEADER_LENGTH = 1;
    public boolean mBlockAckSent;
    public long mDuration;
    public boolean mIncompleteTimerStarted;
    public f mLowerTransportLayerCallbacks;
    public boolean mSegmentedAccessAcknowledgementTimerStarted;
    public Integer mSegmentedAccessBlockAck;
    public boolean mSegmentedControlAcknowledgementTimerStarted;
    public Integer mSegmentedControlBlockAck;
    public final Map<Integer, byte[]> segmentedAccessMessageMap = new HashMap();
    public final Map<Integer, byte[]> segmentedControlMessageMap = new HashMap();
    public Runnable mIncompleteTimerRunnable = new b(this);

    private byte[] createAcknowledgementPayload(int i, int i2) {
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(6).order(ByteOrder.BIG_ENDIAN);
        byteBufferOrder.put((byte) (((i >> 6) & 127) | 0));
        byteBufferOrder.put((byte) (((i << 2) & 252) | 0));
        byteBufferOrder.putInt(i2);
        return byteBufferOrder.array();
    }

    private HashMap<Integer, byte[]> createSegmentedAccessMessage(b.d.a aVar) {
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
            a.a.a.a.b.m.a.a(TAG, "Segmented Lower transport access PDU: " + MeshParserUtils.bytesToHex(bArrArray, false) + " " + i3 + " of " + length);
            map.put(Integer.valueOf(i3), bArrArray);
        }
        return map;
    }

    private HashMap<Integer, byte[]> createSegmentedControlMessage(b.d.b bVar) {
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
            a.a.a.a.b.m.a.a(TAG, "Segmented Lower transport access PDU: " + MeshParserUtils.bytesToHex(bArrArray, false) + " " + i3 + " of " + length);
            map.put(Integer.valueOf(i3), bArrArray);
        }
        bVar.b(map);
        return map;
    }

    private byte[] createUnsegmentedAccessMessage(b.d.a aVar) {
        byte[] bArrV = aVar.v();
        byte bA = (byte) (aVar.a() | (aVar.b() << 6) | ((aVar.t() ? 1 : 0) << 7));
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(bArrV.length + 1).order(ByteOrder.BIG_ENDIAN);
        byteBufferOrder.put(bA);
        byteBufferOrder.put(bArrV);
        byte[] bArrArray = byteBufferOrder.array();
        a.a.a.a.b.m.a.a(TAG, "Unsegmented Lower transport access PDU " + MeshParserUtils.bytesToHex(bArrArray, false));
        return bArrArray;
    }

    @VisibleForTesting(otherwise = 4)
    private byte[] createUnsegmentedControlMessage(b.d.b bVar) {
        ByteBuffer byteBufferOrder;
        bVar.a(false);
        int iN = bVar.n();
        byte[] bArrO = bVar.o();
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
        a.a.a.a.b.m.a.a(TAG, "Unsegmented Lower transport control PDU " + MeshParserUtils.bytesToHex(bArrArray, false));
        HashMap<Integer, byte[]> map = new HashMap<>();
        map.put(0, bArrArray);
        bVar.b(map);
        return bArrArray;
    }

    private void initIncompleteTimer() {
        this.mHandler.postDelayed(this.mIncompleteTimerRunnable, 10000L);
        this.mIncompleteTimerStarted = true;
    }

    private void initSegmentedAccessAcknowledgementTimer(int i, int i2, byte[] bArr, byte[] bArr2, int i3) {
        if (this.mSegmentedAccessAcknowledgementTimerStarted) {
            return;
        }
        long j = (i2 * 50) + 150;
        this.mDuration = System.currentTimeMillis() + j;
        this.mHandler.postDelayed(new c(this, i, i2, bArr, bArr2, i3), j);
        this.mSegmentedAccessAcknowledgementTimerStarted = true;
    }

    private void initSegmentedControlAcknowledgementTimer(int i, int i2, byte[] bArr, byte[] bArr2, int i3) {
        if (this.mSegmentedControlAcknowledgementTimerStarted) {
            return;
        }
        this.mSegmentedControlAcknowledgementTimerStarted = true;
        long j = (i2 * 50) + 150;
        this.mDuration = System.currentTimeMillis() + j;
        this.mHandler.postDelayed(new d(this, i, i2, bArr, bArr2, i3), j);
    }

    private void parseLowerTransportLayerPDU(b.d.b bVar) {
        reassembleLowerTransportControlPDU(bVar);
        byte[] bArrV = bVar.v();
        if (bVar.n() == 0) {
            bVar.a(new b.a.a(bArrV));
        }
        parseUpperTransportPDU(bVar);
    }

    private byte[] removeHeader(byte[] bArr, int i, int i2) {
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(i2).order(ByteOrder.BIG_ENDIAN);
        byteBufferOrder.put(bArr, i, i2);
        return byteBufferOrder.array();
    }

    private HashMap<Integer, byte[]> removeLowerTransportAccessMessageHeader(b.d.a aVar) {
        HashMap<Integer, byte[]> mapJ = aVar.j();
        if (aVar.t()) {
            for (int i = 0; i < mapJ.size(); i++) {
                byte[] bArr = mapJ.get(Integer.valueOf(i));
                mapJ.put(Integer.valueOf(i), removeHeader(bArr, 4, bArr.length - 4));
            }
        } else {
            byte[] bArr2 = mapJ.get(0);
            mapJ.put(0, removeHeader(bArr2, 1, bArr2.length - 1));
        }
        return mapJ;
    }

    private HashMap<Integer, byte[]> removeLowerTransportControlMessageHeader(b.d.b bVar) {
        HashMap<Integer, byte[]> mapK = bVar.k();
        if (mapK.size() > 1) {
            for (int i = 0; i < mapK.size(); i++) {
                byte[] bArr = mapK.get(Integer.valueOf(i));
                mapK.put(Integer.valueOf(i), removeHeader(bArr, 4, bArr.length - 4));
            }
        } else if (bVar.n() != 0) {
            byte[] bArr2 = mapK.get(0);
            mapK.put(0, removeHeader(bArr2, 1, bArr2.length - 1));
        } else {
            byte[] bArr3 = mapK.get(0);
            mapK.put(0, removeHeader(bArr3, 3, bArr3.length - 3));
        }
        return mapK;
    }

    private void restartIncompleteTimer() {
        if (this.mIncompleteTimerStarted) {
            this.mHandler.removeCallbacks(this.mIncompleteTimerRunnable);
        }
        initIncompleteTimer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendBlockAck(int i, int i2, byte[] bArr, byte[] bArr2, int i3) {
        Integer num = this.mSegmentedAccessBlockAck;
        if (num == null) {
            return;
        }
        int iIntValue = num.intValue();
        if (b.a.a.b(Integer.valueOf(iIntValue), i3)) {
            a.a.a.a.b.m.a.a(TAG, "All segments received cancelling incomplete timer");
            this.mHandler.removeCallbacks(this.mIncompleteTimerRunnable);
        }
        byte[] bArrCreateAcknowledgementPayload = createAcknowledgementPayload(i, iIntValue);
        a.a.a.a.b.m.a.a(TAG, "Block acknowledgement payload: " + MeshParserUtils.bytesToHex(bArrCreateAcknowledgementPayload, false));
        b.d.b bVar = new b.d.b();
        bVar.f(0);
        bVar.g(bArrCreateAcknowledgementPayload);
        bVar.h(i2);
        bVar.g(0);
        bVar.f(bArr);
        bVar.a(bArr2);
        bVar.b(this.mMeshNode.getIvIndex());
        bVar.e(MeshParserUtils.getSequenceNumberBytes(incrementSequenceNumber()));
        this.mBlockAckSent = true;
        this.mLowerTransportLayerCallbacks.sendSegmentAcknowledgementMessage(bVar);
        this.mSegmentedAccessAcknowledgementTimerStarted = false;
        a.a.a.a.b.m.a.a(TAG, "Block ack value: " + iIntValue);
        this.mSegmentedAccessBlockAck = null;
    }

    @Override // b.f.h
    @VisibleForTesting(otherwise = 4)
    public final void createLowerTransportAccessPDU(b.d.a aVar) {
        HashMap<Integer, byte[]> mapCreateSegmentedAccessMessage;
        if (aVar.v().length <= 12) {
            aVar.a(false);
            byte[] bArrCreateUnsegmentedAccessMessage = createUnsegmentedAccessMessage(aVar);
            mapCreateSegmentedAccessMessage = new HashMap<>();
            mapCreateSegmentedAccessMessage.put(0, bArrCreateUnsegmentedAccessMessage);
        } else {
            aVar.a(true);
            mapCreateSegmentedAccessMessage = createSegmentedAccessMessage(aVar);
        }
        aVar.a(mapCreateSegmentedAccessMessage);
    }

    @Override // b.f.h
    @VisibleForTesting(otherwise = 4)
    public final void createLowerTransportControlPDU(b.d.b bVar) {
        if (bVar.v().length <= 11) {
            a.a.a.a.b.m.a.a(TAG, "Creating unsegmented transport control");
            createUnsegmentedControlMessage(bVar);
        } else {
            a.a.a.a.b.m.a.a(TAG, "Creating segmented transport control");
            createSegmentedControlMessage(bVar);
        }
    }

    @Override // b.f.h, b.f.a
    public void createMeshMessage(b.d.c cVar) {
        if (!(cVar instanceof b.d.a)) {
            createLowerTransportControlPDU((b.d.b) cVar);
        } else {
            super.createMeshMessage(cVar);
            createLowerTransportAccessPDU((b.d.a) cVar);
        }
    }

    public abstract b.d.c createNetworkLayerPDU(b.d.c cVar);

    @Override // b.f.h
    @VisibleForTesting(otherwise = 4)
    public /* bridge */ /* synthetic */ void createUpperTransportPDU(b.d.a aVar) {
        super.createUpperTransportPDU(aVar);
    }

    @Override // b.f.h, b.f.a
    public void createVendorMeshMessage(b.d.c cVar) {
        if (!(cVar instanceof b.d.a)) {
            createLowerTransportControlPDU((b.d.b) cVar);
        } else {
            super.createVendorMeshMessage(cVar);
            createLowerTransportAccessPDU((b.d.a) cVar);
        }
    }

    public abstract int incrementSequenceNumber();

    public abstract int incrementSequenceNumber(byte[] bArr);

    public final boolean isSegmentedMessage(byte b2) {
        return ((b2 >> 7) & 1) == 1;
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x018a  */
    /* JADX WARN: Removed duplicated region for block: B:36:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final b.d.a parseSegmentedAccessLowerTransportPDU(byte[] r19) {
        /*
            Method dump skipped, instruction units count: 528
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: b.f.e.parseSegmentedAccessLowerTransportPDU(byte[]):b.d.a");
    }

    public final b.d.b parseSegmentedControlLowerTransportPDU(byte[] bArr) {
        byte b2 = bArr[10];
        int i = (b2 >> 6) & 1;
        int i2 = b2 & 63;
        int i3 = (bArr[11] >> 7) & 1;
        int i4 = ((bArr[11] & ByteCompanionObject.MAX_VALUE) << 6) | ((bArr[12] & 252) >> 2);
        int i5 = ((bArr[12] & 3) << 3) | ((bArr[13] & 224) >> 5);
        int i6 = bArr[13] & 31;
        int i7 = bArr[2] & ByteCompanionObject.MAX_VALUE;
        byte[] srcAddress = MeshParserUtils.getSrcAddress(bArr);
        byte[] dstAddress = MeshParserUtils.getDstAddress(bArr);
        a.a.a.a.b.m.a.a(TAG, "SEG O: " + i5);
        a.a.a.a.b.m.a.a(TAG, "SEG N: " + i6);
        initSegmentedControlAcknowledgementTimer(i4, i7, srcAddress, dstAddress, i6);
        this.mSegmentedControlBlockAck = b.a.a.a(this.mSegmentedControlBlockAck, i5);
        a.a.a.a.b.m.a.a(TAG, "Block acknowledgement value for " + this.mSegmentedControlBlockAck + " Seg O " + i5);
        int length = bArr.length - 10;
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(length);
        byteBufferAllocate.put(bArr, 10, length);
        this.segmentedControlMessageMap.put(Integer.valueOf(i5), byteBufferAllocate.array());
        if (i6 != this.segmentedControlMessageMap.size() - 1) {
            return null;
        }
        a.a.a.a.b.m.a.a(TAG, "All segments received");
        this.mHandler.removeCallbacks(this.mIncompleteTimerRunnable);
        a.a.a.a.b.m.a.a(TAG, "Block ack sent? " + this.mBlockAckSent);
        if (this.mDuration > System.currentTimeMillis() && !this.mBlockAckSent && MeshParserUtils.isValidUnicastAddress(dstAddress)) {
            this.mHandler.removeCallbacksAndMessages(null);
            a.a.a.a.b.m.a.a(TAG, "Cancelling Scheduled block ack and incomplete timer, sending an immediate block ack");
            sendBlockAck(i4, i7, dstAddress, srcAddress, i6);
        }
        byte[] sequenceNumberBytes = MeshParserUtils.getSequenceNumberBytes(getTransportLayerSequenceNumber(MeshParserUtils.getSequenceNumberFromPDU(bArr), i4));
        b.d.b bVar = new b.d.b();
        bVar.c(i3);
        bVar.e(sequenceNumberBytes);
        bVar.b(i);
        bVar.a(i2);
        bVar.a(true);
        HashMap<Integer, byte[]> map = new HashMap<>();
        map.putAll(this.segmentedControlMessageMap);
        this.segmentedControlMessageMap.clear();
        bVar.b(map);
        reassembleLowerTransportControlPDU(bVar);
        parseUpperTransportPDU(bVar);
        return bVar;
    }

    public final void parseUnsegmentedAccessLowerTransportPDU(b.d.a aVar, byte[] bArr) {
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

    public final void parseUnsegmentedControlLowerTransportPDU(b.d.b bVar, byte[] bArr) {
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
        parseLowerTransportLayerPDU(bVar);
    }

    @Override // b.f.h
    public final void reassembleLowerTransportAccessPDU(b.d.a aVar) {
        aVar.h(MeshParserUtils.concatenateSegmentedMessages(removeLowerTransportAccessMessageHeader(aVar)));
    }

    @Override // b.f.h
    public final void reassembleLowerTransportControlPDU(b.d.b bVar) {
        bVar.g(MeshParserUtils.concatenateSegmentedMessages(removeLowerTransportControlMessageHeader(bVar)));
    }

    public void setLowerTransportLayerCallbacks(f fVar) {
        this.mLowerTransportLayerCallbacks = fVar;
    }
}
