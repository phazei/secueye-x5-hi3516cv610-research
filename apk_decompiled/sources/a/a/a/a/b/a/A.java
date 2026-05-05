package a.a.a.a.b.a;

import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: SIGMeshBizRequestGenerator.java */
/* JADX INFO: loaded from: classes.dex */
public class A implements SIGMeshBizRequest.b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ short f1244a;

    public A(short s) {
        this.f1244a = s;
    }

    @Override // com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest.b
    public byte[] getEncodedParameters() {
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
        byteBufferOrder.putShort(this.f1244a);
        return byteBufferOrder.array();
    }
}
