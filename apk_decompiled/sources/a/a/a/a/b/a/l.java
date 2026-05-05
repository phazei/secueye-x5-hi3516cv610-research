package a.a.a.a.b.a;

import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest;
import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequestGenerator;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: SIGMeshBizRequestGenerator.java */
/* JADX INFO: loaded from: classes.dex */
public class l implements SIGMeshBizRequest.b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f1267a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ byte f1268b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ Map f1269c;

    public l(int i, byte b2, Map map) {
        this.f1267a = i;
        this.f1268b = b2;
        this.f1269c = map;
    }

    @Override // com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest.b
    public byte[] getEncodedParameters() {
        ByteBuffer byteBufferOrder = ByteBuffer.allocate(this.f1267a).order(ByteOrder.LITTLE_ENDIAN);
        byteBufferOrder.put(this.f1268b);
        for (Map.Entry entry : this.f1269c.entrySet()) {
            byteBufferOrder.put(SIGMeshBizRequestGenerator.Attribute.valueOf((String) entry.getKey()).attrType);
            byteBufferOrder.put(SIGMeshBizRequestGenerator.Attribute.valueOf((String) entry.getKey()).getValueEncoder().a(entry.getValue()));
        }
        return byteBufferOrder.array();
    }
}
