package a.a.a.a.b.a;

import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import meshprovisioner.utils.AddressUtils;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: SIGMeshBizRequestGenerator.java */
/* JADX INFO: loaded from: classes.dex */
public class s implements SIGMeshBizRequest.b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f1277a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ int f1278b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ int f1279c;

    public s(int i, int i2, int i3) {
        this.f1277a = i;
        this.f1278b = i2;
        this.f1279c = i3;
    }

    @Override // com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest.b
    public byte[] getEncodedParameters() {
        byte[] unicastAddressBytes = AddressUtils.getUnicastAddressBytes(this.f1277a);
        byte[] unicastAddressBytes2 = AddressUtils.getUnicastAddressBytes(this.f1278b);
        int i = this.f1279c;
        if (i >= -32768 && i <= 32767) {
            ByteBuffer byteBufferOrder = ByteBuffer.allocate(6).order(ByteOrder.LITTLE_ENDIAN);
            byteBufferOrder.put(unicastAddressBytes[1]);
            byteBufferOrder.put(unicastAddressBytes[0]);
            byteBufferOrder.put(unicastAddressBytes2[1]);
            byteBufferOrder.put(unicastAddressBytes2[0]);
            byteBufferOrder.putShort((short) this.f1279c);
            return byteBufferOrder.array();
        }
        ByteBuffer byteBufferOrder2 = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
        byteBufferOrder2.put(unicastAddressBytes[1]);
        byteBufferOrder2.put(unicastAddressBytes[0]);
        byteBufferOrder2.put(unicastAddressBytes2[1]);
        byteBufferOrder2.put(unicastAddressBytes2[0]);
        int i2 = this.f1279c;
        byte[] bArr = {(byte) ((i2 >> 24) & 255), (byte) ((i2 >> 16) & 255), (byte) ((i2 >> 8) & 255), (byte) (i2 & 255)};
        byteBufferOrder2.put(bArr[1]);
        byteBufferOrder2.put(bArr[0]);
        byteBufferOrder2.put(bArr[3]);
        byteBufferOrder2.put(bArr[2]);
        return byteBufferOrder2.array();
    }
}
