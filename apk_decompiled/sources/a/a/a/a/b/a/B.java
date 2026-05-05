package a.a.a.a.b.a;

import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest;
import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequestGenerator;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: SIGMeshBizRequestGenerator.java */
/* JADX INFO: loaded from: classes.dex */
public class B implements SIGMeshBizRequest.b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ short f1245a;

    public B(short s) {
        this.f1245a = s;
    }

    @Override // com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest.b
    public byte[] getEncodedParameters() {
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(3).order(ByteOrder.LITTLE_ENDIAN);
        byteBufferOrder.putShort(this.f1245a);
        byteBufferOrder.put(SIGMeshBizRequestGenerator.d());
        return byteBufferOrder.array();
    }
}
