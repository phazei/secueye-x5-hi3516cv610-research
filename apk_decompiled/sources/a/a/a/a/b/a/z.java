package a.a.a.a.b.a;

import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest;
import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequestGenerator;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: SIGMeshBizRequestGenerator.java */
/* JADX INFO: loaded from: classes.dex */
public class z implements SIGMeshBizRequest.b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ boolean f1285a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ short f1286b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final /* synthetic */ Map f1287c;

    public z(boolean z, short s, Map map) {
        this.f1285a = z;
        this.f1286b = s;
        this.f1287c = map;
    }

    @Override // com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest.b
    public byte[] getEncodedParameters() {
        int length = 3;
        if (!this.f1285a) {
            ByteBuffer byteBufferOrder = ByteBuffer.allocate(7).order(ByteOrder.LITTLE_ENDIAN);
            byteBufferOrder.putShort(this.f1286b);
            Map map = this.f1287c;
            int i = 2;
            if (map != null) {
                if (map.containsKey(SIGMeshBizRequestGenerator.Attribute.powerstate.attributeName)) {
                    byteBufferOrder.put(SIGMeshBizRequestGenerator.Attribute.powerstate.getValueEncoder().a(this.f1287c.get(SIGMeshBizRequestGenerator.Attribute.powerstate.attributeName)));
                } else {
                    length = 2;
                }
                if (this.f1287c.containsKey(SIGMeshBizRequestGenerator.Attribute.brightness.attributeName)) {
                    byteBufferOrder.put(SIGMeshBizRequestGenerator.Attribute.brightness.getValueEncoder().a(this.f1287c.get(SIGMeshBizRequestGenerator.Attribute.brightness.attributeName)));
                    i = length + 2;
                } else {
                    i = length;
                }
                if (this.f1287c.containsKey(SIGMeshBizRequestGenerator.Attribute.colorTemperature.attributeName)) {
                    i += 2;
                    byteBufferOrder.put(SIGMeshBizRequestGenerator.Attribute.colorTemperature.getValueEncoder().a(this.f1287c.get(SIGMeshBizRequestGenerator.Attribute.colorTemperature.attributeName)));
                }
            }
            return Arrays.copyOfRange(byteBufferOrder.array(), 0, i);
        }
        ByteBuffer byteBufferOrder2 = ByteBuffer.allocate(20).order(ByteOrder.LITTLE_ENDIAN);
        byteBufferOrder2.putShort(this.f1286b);
        byteBufferOrder2.put((byte) -88);
        Map map2 = this.f1287c;
        if (map2 != null) {
            for (Map.Entry entry : map2.entrySet()) {
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("attr: ");
                    sb.append((String) entry.getKey());
                    sb.append(", value: ");
                    sb.append(entry.getValue().toString());
                    a.a.a.a.b.m.a.a("SIGMeshBizRequestGenerator", sb.toString());
                    SIGMeshBizRequestGenerator.Attribute attributeValueOf = SIGMeshBizRequestGenerator.Attribute.valueOf((String) entry.getKey());
                    byteBufferOrder2.put(attributeValueOf.attrType);
                    byteBufferOrder2.put(attributeValueOf.getValueEncoder().a(entry.getValue()));
                    length += attributeValueOf.attrType.length + attributeValueOf.attrParameterLength;
                } catch (Exception e) {
                    a.a.a.a.b.m.a.b("SIGMeshBizRequestGenerator", e.toString());
                }
            }
        }
        return Arrays.copyOfRange(byteBufferOrder2.array(), 0, length);
    }
}
