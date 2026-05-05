package a.a.a.a.b.a;

import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest;
import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequestGenerator;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: renamed from: a.a.a.a.b.a.j, reason: case insensitive filesystem */
/* JADX INFO: compiled from: SIGMeshBizRequestGenerator.java */
/* JADX INFO: loaded from: classes.dex */
public class C0324j implements SIGMeshBizRequest.b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ short f1265a;

    public C0324j(short s) {
        this.f1265a = s;
    }

    @Override // com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest.b
    public byte[] getEncodedParameters() {
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(5).order(ByteOrder.LITTLE_ENDIAN);
        byteBufferOrder.putShort((short) 17282);
        byteBufferOrder.putShort(this.f1265a);
        byteBufferOrder.put(SIGMeshBizRequestGenerator.d());
        return byteBufferOrder.array();
    }
}
